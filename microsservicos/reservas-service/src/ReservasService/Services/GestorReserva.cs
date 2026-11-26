using Microsoft.EntityFrameworkCore;
using Npgsql;
using ReservasService.Clients;
using ReservasService.Data;
using ReservasService.Models;

namespace ReservasService.Services;

public record NovaReservaRequest(long ClienteId, long VeiculoId, long VagaId, DateTimeOffset DataInicio, DateTimeOffset DataFim);

// Controller (GRASP) e Creator de Reserva: coordena Cadastro, Vagas, Tarifação e Pagamentos.
public class GestorReserva(
    OperacionalDbContext db,
    CadastroClient cadastro,
    VagasClient vagas,
    TarifacaoClient tarifacao,
    PagamentosClient pagamentos)
{
    private static readonly StatusReserva[] Ativas = [StatusReserva.PENDENTE, StatusReserva.CONFIRMADA];

    public async Task<Reserva> CriarAsync(NovaReservaRequest req)
    {
        var agora = DateTime.UtcNow;
        var inicio = req.DataInicio.UtcDateTime;
        var fim = req.DataFim.UtcDateTime;

        if (fim <= inicio)
            throw new ErroNegocioException(400, "dataFim deve ser posterior a dataInicio.");
        if (inicio < agora.AddMinutes(-5)) // tolerância para relógios levemente diferentes
            throw new ErroNegocioException(400, "dataInicio não pode estar no passado.");

        if (!await cadastro.ClienteExisteAsync(req.ClienteId))
            throw new ErroNegocioException(400, "Cliente não encontrado no Cadastro.");

        var veiculo = await cadastro.BuscarVeiculoAsync(req.VeiculoId)
            ?? throw new ErroNegocioException(400, "Veículo não encontrado no Cadastro.");
        if (veiculo.ClienteId != req.ClienteId)
            throw new ErroNegocioException(400, "O veículo não pertence a este cliente.");

        var vaga = await vagas.BuscarAsync(req.VagaId)
            ?? throw new ErroNegocioException(400, "Vaga não encontrada.");
        if (vaga.Tipo != veiculo.Tipo)
            throw new ErroNegocioException(400, $"Vaga de {vaga.Tipo} não aceita veículo do tipo {veiculo.Tipo}.");

        // checagem amigável; a garantia contra corrida é a constraint EXCLUDE do banco
        bool conflito = await db.Reservas.AnyAsync(r =>
            Ativas.Contains(r.Status) &&
            (r.VagaId == req.VagaId || r.VeiculoId == req.VeiculoId) &&
            r.DataInicio < fim && inicio < r.DataFim);
        if (conflito)
            throw new ErroNegocioException(409, "Já existe reserva ativa para esta vaga ou este veículo no período.");

        var reserva = new Reserva
        {
            ClienteId = req.ClienteId,
            VeiculoId = req.VeiculoId,
            VagaId = req.VagaId,
            TipoVeiculo = veiculo.Tipo,
            DataReserva = agora,
            DataInicio = inicio,
            DataFim = fim,
            Valor = await tarifacao.CalcularAsync(veiculo.Tipo, Reserva.MinutosDoPeriodo(inicio, fim)),
            Status = StatusReserva.PENDENTE,
        };
        db.Reservas.Add(reserva);
        try
        {
            await db.SaveChangesAsync();
        }
        catch (DbUpdateException e) when (e.InnerException is PostgresException { SqlState: PostgresErrorCodes.ExclusionViolation })
        {
            throw new ErroNegocioException(409, "A vaga acabou de ser reservada para este período.");
        }
        return reserva;
    }

    public async Task<Reserva> ConfirmarAsync(int id, string metodoPagamento)
    {
        var metodo = metodoPagamento?.ToUpperInvariant() ?? "";
        if (!PagamentosClient.MetodosValidos.Contains(metodo))
            throw new ErroNegocioException(400, "metodoPagamento deve ser DINHEIRO, CARTAO ou PIX.");

        var reserva = await BuscarAsync(id);
        if (reserva.Status != StatusReserva.PENDENTE)
            throw new ErroNegocioException(409, $"Só reservas PENDENTE podem ser confirmadas (atual: {reserva.Status}).");

        var pagamento = await pagamentos.PagarAsync("RESERVA", reserva.Id, reserva.Valor, metodo);
        if (!pagamento.Aprovado)
            throw new ErroNegocioException(402, "Pagamento recusado.");

        reserva.Confirmar(pagamento.Id);
        await db.SaveChangesAsync();
        return reserva;
    }

    public async Task<Reserva> CancelarAsync(int id)
    {
        var reserva = await BuscarAsync(id);
        reserva.Cancelar(DateTime.UtcNow);
        await db.SaveChangesAsync();
        return reserva;
    }

    public async Task<Reserva> BuscarAsync(int id) =>
        await db.Reservas.FindAsync(id) ?? throw new ErroNegocioException(404, "Reserva não encontrada.");

    public Task<List<Reserva>> ListarAsync(long? clienteId) =>
        db.Reservas.AsNoTracking()
            .Where(r => clienteId == null || r.ClienteId == clienteId)
            .OrderByDescending(r => r.DataInicio)
            .ToListAsync();
}
