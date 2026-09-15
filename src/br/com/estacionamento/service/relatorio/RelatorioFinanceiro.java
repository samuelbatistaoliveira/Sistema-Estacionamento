package br.com.estacionamento.service.relatorio;

import java.time.LocalDateTime;

public class RelatorioFinanceiro {

    private LocalDateTime periodoInicio;
    private LocalDateTime periodoFim;
    private double totalRecebido;
    private int quantidadeTransacoes;

    public void gerar(LocalDateTime inicio, LocalDateTime fim) {
        this.periodoInicio = inicio;
        this.periodoFim = fim;
        // TODO: consultar pagamentos do período (via IPagamentoRepositorio) e calcular totalRecebido/quantidadeTransacoes
    }

    public LocalDateTime getPeriodoInicio() {
        return periodoInicio;
    }

    public LocalDateTime getPeriodoFim() {
        return periodoFim;
    }

    public double getTotalRecebido() {
        return totalRecebido;
    }

    public int getQuantidadeTransacoes() {
        return quantidadeTransacoes;
    }
}
