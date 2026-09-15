package br.com.estacionamento.service;

import br.com.estacionamento.interfaces.ICalculoTarifa;
import br.com.estacionamento.interfaces.ITabelaPreco;
import br.com.estacionamento.model.Veiculo;

import java.time.Duration;
import java.time.LocalDateTime;

public class Calculo implements ICalculoTarifa {

    @Override
    public long calcularTempoPermanencia(LocalDateTime dataHoraEntrada, LocalDateTime dataHoraSaida) {
        return Duration.between(dataHoraEntrada, dataHoraSaida).toMinutes();
    }

    @Override
    public double calcularValor(Veiculo veiculo, ITabelaPreco tabelaPreco, long tempoPermanencia) {
        return tabelaPreco.consultarValorBase((int) tempoPermanencia, veiculo.getTipo());
    }
}
