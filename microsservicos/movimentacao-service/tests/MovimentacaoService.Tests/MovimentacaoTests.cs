using MovimentacaoService;
using MovimentacaoService.Models;
using Xunit;

public class MovimentacaoTests
{
    private static readonly DateTime Entrada = new(2026, 12, 1, 12, 0, 0, DateTimeKind.Utc);

    [Fact]
    public void Sem_reserva_cobra_toda_permanencia_arredondada_para_cima()
    {
        Assert.Equal(90, Movimentacao.MinutosCobraveis(Entrada, Entrada.AddMinutes(90), null));
        Assert.Equal(91, Movimentacao.MinutosCobraveis(Entrada, Entrada.AddMinutes(90).AddSeconds(1), null));
    }

    [Fact]
    public void Sem_reserva_cobra_no_minimo_um_minuto()
    {
        Assert.Equal(1, Movimentacao.MinutosCobraveis(Entrada, Entrada.AddSeconds(5), null));
        Assert.Equal(1, Movimentacao.MinutosCobraveis(Entrada, Entrada, null));
    }

    [Fact]
    public void Com_reserva_nao_cobra_dentro_do_periodo_reservado()
    {
        var fim = Entrada.AddHours(3);
        Assert.Equal(0, Movimentacao.MinutosCobraveis(Entrada, fim, fim));
        Assert.Equal(0, Movimentacao.MinutosCobraveis(Entrada, fim.AddHours(-1), fim));
    }

    [Fact]
    public void Com_reserva_cobra_so_o_excedente()
    {
        var fim = Entrada.AddHours(3);
        Assert.Equal(20, Movimentacao.MinutosCobraveis(Entrada, fim.AddMinutes(20), fim));
    }

    [Fact]
    public void Finalizar_preenche_saida_e_bloqueia_segunda_saida()
    {
        var m = new Movimentacao { Status = StatusMovimentacao.ABERTA, DataHoraEntrada = Entrada };
        m.Finalizar(Entrada.AddHours(1), 60, 5m, "pg-1");

        Assert.Equal(StatusMovimentacao.FINALIZADA, m.Status);
        Assert.Equal(5m, m.Valor);
        Assert.Equal(409, Assert.Throws<ErroNegocioException>(() => m.Finalizar(Entrada.AddHours(2), 120, 10m, null)).Status);
    }
}
