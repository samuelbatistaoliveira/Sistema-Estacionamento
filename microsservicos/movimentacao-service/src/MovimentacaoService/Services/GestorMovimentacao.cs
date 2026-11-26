using Microsoft.EntityFrameworkCore;
using Npgsql;
using MovimentacaoService.Clients;
using MovimentacaoService.Data;
using MovimentacaoService.Models;

namespace MovimentacaoService.Services;

// Controller (GRASP) e Creator de Movimentacao: coordena Cadastro, Vagas, Tarifação, Pagamentos e as reservas do Banco Operacional.
public class GestorMovimentacao(
    OperacionalDbContext db,
    CadastroClient cadastro,
    VagasClient vagas,
    TarifacaoClient tarifacao,
    PagamentosClient pagamentos,
    ILogger<GestorMovimentacao> logger)
{
    // quem tem reserva pode chegar até 15 min antes do início
    public static readonly TimeSpan ToleranciaEntrada = TimeSpan.FromMinutes(15);

    public async Task<Movimentacao> RegistrarEntradaAsync(string placa)
    {
        placa = NormalizarPlaca(placa);
        var veiculo = await cadastro.BuscarVeiculoPorPlacaAsync(placa)
            ?? throw new ErroNegocioException(400, "Veículo não cadastrado.");

        if (await db.Movimentacoes.AnyAsync(m => m.VeiculoId == veiculo.Id && m.Status == StatusMovimentacao.ABERTA))
            throw new ErroNegocioException(409, "Este veículo já está dentro do estacionamento.");

        var agora = DateTime.UtcNow;
        var limite = agora + ToleranciaEntrada;

        // reservas confirmadas valendo agora (dentro da tolerância de chegada antecipada)
        var reservasVigentes = await db.Reservas
            .Where(r => r.Status == StatusReserva.CONFIRMADA && r.DataInicio <= limite && r.DataFim > agora)
            .ToListAsync();
        var reserva = reservasVigentes.Where(r => r.VeiculoId == veiculo.Id).MinBy(r => r.DataInicio);
        var vagasReservadasPorOutros = reservasVigentes.Where(r => r.VeiculoId != veiculo.Id).Select(r => r.VagaId).ToHashSet();

        // vaga reservada primeiro; se estiver ocupada (ex.: veículo avulso ainda não saiu), qualquer outra livre do mesmo tipo
        var candidatas = (await vagas.ListarDisponiveisAsync())
            .Where(v => v.Tipo == veiculo.Tipo && !vagasReservadasPorOutros.Contains(v.Id))
            .Select(v => v.Id)
            .ToList();
        if (reserva != null)
            candidatas.Insert(0, reserva.VagaId);

        long? vagaId = null;
        foreach (var id in candidatas.Distinct())
        {
            if (await vagas.OcuparAsync(id, veiculo.Id)) { vagaId = id; break; }
        }
        if (vagaId == null)
            throw new ErroNegocioException(409, $"Não há vaga livre para {veiculo.Tipo}.");

        var movimentacao = new Movimentacao
        {
            VeiculoId = veiculo.Id,
            Placa = placa,
            TipoVeiculo = veiculo.Tipo,
            VagaId = vagaId.Value,
            ReservaId = reserva?.Id,
            DataHoraEntrada = agora,
            Status = StatusMovimentacao.ABERTA,
        };
        db.Movimentacoes.Add(movimentacao);
        if (reserva != null)
            reserva.Status = StatusReserva.CONCLUIDA; // mesma transação do INSERT da movimentação

        try
        {
            await db.SaveChangesAsync();
        }
        catch (Exception e)
        {
            await LiberarVagaSemFalharAsync(vagaId.Value); // compensação: não deixa a vaga presa sem registro
            if (e is DbUpdateException { InnerException: PostgresException { SqlState: PostgresErrorCodes.UniqueViolation } })
                throw new ErroNegocioException(409, "Este veículo já está dentro do estacionamento.");
            throw;
        }
        return movimentacao;
    }

    public async Task<Movimentacao> RegistrarSaidaAsync(string placa, string? metodoPagamento)
    {
        placa = NormalizarPlaca(placa);
        var movimentacao = await db.Movimentacoes
            .FirstOrDefaultAsync(m => m.Placa == placa && m.Status == StatusMovimentacao.ABERTA)
            ?? throw new ErroNegocioException(404, "Nenhuma movimentação aberta para esta placa.");

        var agora = DateTime.UtcNow;
        DateTime? fimReserva = movimentacao.ReservaId is { } reservaId
            ? (await db.Reservas.FindAsync(reservaId))?.DataFim
            : null;

        int minutos = Movimentacao.MinutosCobraveis(movimentacao.DataHoraEntrada, agora, fimReserva);
        decimal valor = minutos > 0 ? await tarifacao.CalcularAsync(movimentacao.TipoVeiculo, minutos) : 0m;

        string? pagamentoId = null;
        if (valor > 0)
        {
            var metodo = metodoPagamento?.ToUpperInvariant() ?? "";
            if (!PagamentosClient.MetodosValidos.Contains(metodo))
                throw new ErroNegocioException(400, $"Valor a pagar R$ {valor:F2}: informe metodoPagamento (DINHEIRO, CARTAO ou PIX).");

            var pagamento = await pagamentos.PagarAsync("MOVIMENTACAO", movimentacao.Id, valor, metodo);
            if (!pagamento.Aprovado)
                throw new ErroNegocioException(402, "Pagamento recusado.");
            pagamentoId = pagamento.Id;
        }

        // grava antes de liberar a vaga: o pagamento já aconteceu e não pode se perder
        movimentacao.Finalizar(agora, minutos, valor, pagamentoId);
        await db.SaveChangesAsync();

        await LiberarVagaSemFalharAsync(movimentacao.VagaId);
        return movimentacao;
    }

    public async Task<Movimentacao> BuscarAsync(int id) =>
        await db.Movimentacoes.FindAsync(id) ?? throw new ErroNegocioException(404, "Movimentação não encontrada.");

    public Task<List<Movimentacao>> ListarAsync(StatusMovimentacao? status) =>
        db.Movimentacoes.AsNoTracking()
            .Where(m => status == null || m.Status == status)
            .OrderByDescending(m => m.DataHoraEntrada)
            .ToListAsync();

    private static string NormalizarPlaca(string? placa)
    {
        var normalizada = (placa ?? "").Trim().ToUpperInvariant();
        if (normalizada.Length == 0)
            throw new ErroNegocioException(400, "Informe a placa.");
        return normalizada;
    }

    private async Task LiberarVagaSemFalharAsync(long vagaId)
    {
        try
        {
            await vagas.LiberarAsync(vagaId);
        }
        catch (Exception e) when (e is HttpRequestException || e is TaskCanceledException)
        {
            logger.LogError(e, "Não foi possível liberar a vaga {VagaId} no vagas-service; liberar manualmente.", vagaId);
        }
    }
}
