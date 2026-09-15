package br.com.estacionamento.model;

import java.time.LocalDateTime;

public class RegistroMovimentacao {

    private int idRegistro;
    private LocalDateTime dataHoraEntrada;
    private LocalDateTime dataHoraSaida;
    private long tempoPermanencia;
    private double valorTotal;
    private Veiculo veiculo;
    private Vaga vaga;
    private Funcionario funcionario;
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

    public long getTempoPermanencia() {
        return tempoPermanencia;
    }

    public void setTempoPermanencia(long tempoPermanencia) {
        this.tempoPermanencia = tempoPermanencia;
    }

    public double getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(double valorTotal) {
        this.valorTotal = valorTotal;
    }

    public Veiculo getVeiculo() {
        return veiculo;
    }

    public void setVeiculo(Veiculo veiculo) {
        this.veiculo = veiculo;
    }

    public Vaga getVaga() {
        return vaga;
    }

    public void setVaga(Vaga vaga) {
        this.vaga = vaga;
    }

    public Funcionario getFuncionario() {
        return funcionario;
    }

    public void setFuncionario(Funcionario funcionario) {
        this.funcionario = funcionario;
    }

    public Pagamento getPagamento() {
        return pagamento;
    }

    public void setPagamento(Pagamento pagamento) {
        this.pagamento = pagamento;
    }
}
