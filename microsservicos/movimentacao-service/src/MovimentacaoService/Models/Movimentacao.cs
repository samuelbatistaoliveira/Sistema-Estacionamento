using System.ComponentModel.DataAnnotations.Schema;

namespace MovimentacaoService.Models;

public enum StatusMovimentacao { ABERTA, FINALIZADA }

[Table("movimentacoes")]
public class Movimentacao
{
    [Column("id")] public int Id { get; set; }
    [Column("veiculo_id")] public long VeiculoId { get; set; }
    [Column("placa")] public string Placa { get; set; } = "";
    [Column("tipo_veiculo")] public string TipoVeiculo { get; set; } = "";
    [Column("vaga_id")] public long VagaId { get; set; }
    [Column("reserva_id")] public int? ReservaId { get; set; }
    [Column("data_hora_entrada")] public DateTime DataHoraEntrada { get; set; }
    [Column("data_hora_saida")] public DateTime? DataHoraSaida { get; set; }
    [Column("minutos_cobrados")] public int? MinutosCobrados { get; set; }
    [Column("valor")] public decimal? Valor { get; set; }
    [Column("pagamento_id")] public string? PagamentoId { get; set; }
    [Column("status")] public StatusMovimentacao Status { get; set; }

    // Sem reserva: cobra toda a permanência (mínimo 1 min, a Tarifação exige minutos > 0).
    // Com reserva: o período reservado já foi pago; cobra só o que passar de fimReserva.
    public static int MinutosCobraveis(DateTime entrada, DateTime saida, DateTime? fimReserva)
    {
        if (fimReserva is { } fim)
            return saida <= fim ? 0 : (int)Math.Ceiling((saida - fim).TotalMinutes);
        return Math.Max(1, (int)Math.Ceiling((saida - entrada).TotalMinutes));
    }

    public void Finalizar(DateTime saida, int minutos, decimal valor, string? pagamentoId)
    {
        if (Status != StatusMovimentacao.ABERTA)
            throw new ErroNegocioException(409, "Movimentação já finalizada.");
        DataHoraSaida = saida;
        MinutosCobrados = minutos;
        Valor = valor;
        PagamentoId = pagamentoId;
        Status = StatusMovimentacao.FINALIZADA;
    }
}
