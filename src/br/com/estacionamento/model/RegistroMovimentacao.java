package br.com.estacionamento.model;

import java.time.LocalDateTime;

public class RegistroMovimentacao {

    private int idRegistro;
    private LocalDateTime dataHoraEntrada;
    private LocalDateTime dataHoraSaida;
    private Veiculo veiculo;
    private Pagamento pagamento;

    public void registrarEntrada(Veiculo veiculo) {
        this.veiculo = veiculo;
        this.dataHoraEntrada = LocalDateTime.now();
    }

    public void registrarSaida(Veiculo veiculo) {
        this.veiculo = veiculo;
        this.dataHoraSaida = LocalDateTime.now();
    }

    public int getIdRegistro() {
        return idRegistro;
    }

    public void setIdRegistro(int idRegistro) {
        this.idRegistro = idRegistro;
    }

    public LocalDateTime getDataHoraEntrada() {
        return dataHoraEntrada;
    }

    public void setDataHoraEntrada(LocalDateTime dataHoraEntrada) {
        this.dataHoraEntrada = dataHoraEntrada;
    }

    public LocalDateTime getDataHoraSaida() {
        return dataHoraSaida;
    }

    public void setDataHoraSaida(LocalDateTime dataHoraSaida) {
        this.dataHoraSaida = dataHoraSaida;
    }

    public Veiculo getVeiculo() {
        return veiculo;
    }

    public void setVeiculo(Veiculo veiculo) {
        this.veiculo = veiculo;
    }

    public Pagamento getPagamento() {
        return pagamento;
    }

    public void setPagamento(Pagamento pagamento) {
        this.pagamento = pagamento;
    }
}
