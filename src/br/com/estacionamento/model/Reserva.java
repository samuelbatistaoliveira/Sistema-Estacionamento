package br.com.estacionamento.model;

import java.time.LocalDateTime;

public class Reserva {

    private int idReserva;
    private LocalDateTime dataReserva;
    private StatusReserva situacaoReserva;
    private double valor;
    private Cliente cliente;
    private Vaga vaga;
    private LocalDateTime dataInicioReserva;
    private LocalDateTime dataFimReserva;

    public StatusReserva consultarStatusReserva() {
        return situacaoReserva;
    }

    public int getIdReserva() {
        return idReserva;
    }

    public void setIdReserva(int idReserva) {
        this.idReserva = idReserva;
    }

    public LocalDateTime getDataReserva() {
        return dataReserva;
    }

    public void setDataReserva(LocalDateTime dataReserva) {
        this.dataReserva = dataReserva;
    }

    public StatusReserva getSituacaoReserva() {
        return situacaoReserva;
    }

    public void setSituacaoReserva(StatusReserva situacaoReserva) {
        this.situacaoReserva = situacaoReserva;
    }

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public Vaga getVaga() {
        return vaga;
    }

    public void setVaga(Vaga vaga) {
        this.vaga = vaga;
    }

    public LocalDateTime getDataInicioReserva() {
        return dataInicioReserva;
    }

    public void setDataInicioReserva(LocalDateTime dataInicioReserva) {
        this.dataInicioReserva = dataInicioReserva;
    }

    public LocalDateTime getDataFimReserva() {
        return dataFimReserva;
    }

    public void setDataFimReserva(LocalDateTime dataFimReserva) {
        this.dataFimReserva = dataFimReserva;
    }
}
