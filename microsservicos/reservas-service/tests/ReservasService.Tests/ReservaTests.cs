using ReservasService;
using ReservasService.Models;
using Xunit;

public class ReservaTests
{
    private static readonly DateTime Inicio = new(2026, 12, 1, 17, 0, 0, DateTimeKind.Utc);

    [Fact]
    public void MinutosDoPeriodo_arredonda_fracao_para_cima()
    {
        Assert.Equal(180, Reserva.MinutosDoPeriodo(Inicio, Inicio.AddHours(3)));
        Assert.Equal(61, Reserva.MinutosDoPeriodo(Inicio, Inicio.AddMinutes(60).AddSeconds(1)));
    }

    [Fact]
    public void Confirmar_so_aceita_pendente()
    {
        var r = new Reserva { Status = StatusReserva.PENDENTE };
        r.Confirmar("pg-1");
        Assert.Equal(StatusReserva.CONFIRMADA, r.Status);
        Assert.Equal("pg-1", r.PagamentoId);

        var erro = Assert.Throws<ErroNegocioException>(() => r.Confirmar("pg-2"));
        Assert.Equal(409, erro.Status);
    }

    [Fact]
    public void Cancelar_confirmada_so_antes_do_inicio()
    {
        var antes = new Reserva { Status = StatusReserva.CONFIRMADA, DataInicio = Inicio };
        antes.Cancelar(Inicio.AddMinutes(-1));
        Assert.Equal(StatusReserva.CANCELADA, antes.Status);

        var depois = new Reserva { Status = StatusReserva.CONFIRMADA, DataInicio = Inicio };
        Assert.Equal(409, Assert.Throws<ErroNegocioException>(() => depois.Cancelar(Inicio)).Status);
    }

    [Theory]
    [InlineData(StatusReserva.CANCELADA)]
    [InlineData(StatusReserva.CONCLUIDA)]
    public void Cancelar_rejeita_status_final(StatusReserva status)
    {
        var r = new Reserva { Status = status, DataInicio = Inicio };
        Assert.Equal(409, Assert.Throws<ErroNegocioException>(() => r.Cancelar(Inicio.AddDays(-1))).Status);
    }
}
