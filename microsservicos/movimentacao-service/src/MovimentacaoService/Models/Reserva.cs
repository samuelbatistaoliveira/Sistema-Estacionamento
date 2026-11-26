using System.ComponentModel.DataAnnotations.Schema;

namespace MovimentacaoService.Models;

public enum StatusReserva { PENDENTE, CONFIRMADA, CANCELADA, CONCLUIDA }

// Visão parcial da tabela "reservas" (dona: reservas-service) no Banco Operacional compartilhado.
// A Movimentação só lê reservas e as marca como CONCLUIDA na entrada; nunca cria.
[Table("reservas")]
public class Reserva
{
    [Column("id")] public int Id { get; set; }
    [Column("veiculo_id")] public long VeiculoId { get; set; }
    [Column("vaga_id")] public long VagaId { get; set; }
    [Column("data_inicio")] public DateTime DataInicio { get; set; }
    [Column("data_fim")] public DateTime DataFim { get; set; }
    [Column("status")] public StatusReserva Status { get; set; }
}
