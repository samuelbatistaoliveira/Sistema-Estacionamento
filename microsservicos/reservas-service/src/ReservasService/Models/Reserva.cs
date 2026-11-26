using System.ComponentModel.DataAnnotations.Schema;

namespace ReservasService.Models;

public enum StatusReserva { PENDENTE, CONFIRMADA, CANCELADA, CONCLUIDA }

[Table("reservas")]
public class Reserva
{
    [Column("id")] public int Id { get; set; }
    [Column("cliente_id")] public long ClienteId { get; set; }
    [Column("veiculo_id")] public long VeiculoId { get; set; }
    [Column("vaga_id")] public long VagaId { get; set; }
    [Column("tipo_veiculo")] public string TipoVeiculo { get; set; } = "";
    [Column("data_reserva")] public DateTime DataReserva { get; set; }
    [Column("data_inicio")] public DateTime DataInicio { get; set; }
    [Column("data_fim")] public DateTime DataFim { get; set; }
    [Column("valor")] public decimal Valor { get; set; }
    [Column("status")] public StatusReserva Status { get; set; }
    [Column("pagamento_id")] public string? PagamentoId { get; set; }

    // Information Expert: a própria reserva sabe quanto tempo cobre e quais transições de status são válidas.
    public static int MinutosDoPeriodo(DateTime inicio, DateTime fim) =>
        (int)Math.Ceiling((fim - inicio).TotalMinutes);

    public void Confirmar(string pagamentoId)
    {
        if (Status != StatusReserva.PENDENTE)
            throw new ErroNegocioException(409, $"Só reservas PENDENTE podem ser confirmadas (atual: {Status}).");
        Status = StatusReserva.CONFIRMADA;
        PagamentoId = pagamentoId;
    }

    public void Cancelar(DateTime agora)
    {
        if (Status is not (StatusReserva.PENDENTE or StatusReserva.CONFIRMADA))
            throw new ErroNegocioException(409, $"Reserva {Status} não pode ser cancelada.");
        if (Status == StatusReserva.CONFIRMADA && agora >= DataInicio)
            throw new ErroNegocioException(409, "Reserva confirmada já iniciou e não pode mais ser cancelada.");
        Status = StatusReserva.CANCELADA;
    }
}
