package br.com.estacionamento.service;

import br.com.estacionamento.interfaces.ICalculoTarifa;
import br.com.estacionamento.interfaces.ITabelaPreco;

import java.time.Duration;
import java.time.LocalDateTime;

public class Calculo implements ICalculoTarifa {

    @Override
    public double calcularTempoPermanencia(LocalDateTime dataHoraEntrada, LocalDateTime dataHoraSaida) {
        return Duration.between(dataHoraEntrada, dataHoraSaida).toMinutes();
    }

    @Override
    public double calcularValor(ITabelaPreco tabelaPreco, long tempoPermanencia, String tipoVeiculo) {
        return tabelaPreco.consultarValorBase((int) tempoPermanencia, tipoVeiculo);
    }
}
