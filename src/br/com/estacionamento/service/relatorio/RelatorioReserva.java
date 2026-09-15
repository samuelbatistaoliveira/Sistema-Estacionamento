package br.com.estacionamento.service.relatorio;

import java.time.LocalDateTime;

public class RelatorioReserva {

    private LocalDateTime periodoInicio;
    private LocalDateTime periodoFim;
    private int totalReserva;

    public void gerar(LocalDateTime periodoInicio, LocalDateTime periodoFim) {
        this.periodoInicio = periodoInicio;
        this.periodoFim = periodoFim;
        // TODO: consultar reservas do período (via IRegistroReservaRepositorio) e calcular totalReserva
    }

    public LocalDateTime getPeriodoInicio() {
        return periodoInicio;
    }

    public LocalDateTime getPeriodoFim() {
        return periodoFim;
    }

    public int getTotalReserva() {
        return totalReserva;
    }
}
