package br.com.estacionamento.interfaces;

import br.com.estacionamento.interfaces.ITabelaPreco;

import java.time.LocalDateTime;

public interface ICalculoTarifa {

    double calcularTempoPermanencia(LocalDateTime dataHoraEntrada, LocalDateTime dataHoraSaida);

    double calcularValor(ITabelaPreco tabelaPreco, long tempoPermanencia, String tipoVeiculo);
}
