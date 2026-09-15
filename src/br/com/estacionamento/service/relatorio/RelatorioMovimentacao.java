package br.com.estacionamento.service.relatorio;

import java.time.LocalDateTime;

public class RelatorioMovimentacao {

    private LocalDateTime periodoInicio;
    private LocalDateTime periodoFim;
    private int totalVeiculos;

    public void gerar(LocalDateTime inicio, LocalDateTime fim) {
        this.periodoInicio = inicio;
        this.periodoFim = fim;
        // TODO: consultar movimentações do período (via IRegistroMovimentacaoRepositorio) e calcular totalVeiculos
    }

    public LocalDateTime getPeriodoInicio() {
        return periodoInicio;
    }

    public LocalDateTime getPeriodoFim() {
        return periodoFim;
    }

    public int getTotalVeiculos() {
        return totalVeiculos;
    }
}
