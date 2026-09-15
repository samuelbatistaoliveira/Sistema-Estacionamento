package br.com.estacionamento.interfaces;

import br.com.estacionamento.model.Veiculo;

import java.time.LocalDateTime;

public interface ICalculoTarifa {

    long calcularTempoPermanencia(LocalDateTime dataHoraEntrada, LocalDateTime dataHoraSaida);

    double calcularValor(Veiculo veiculo, ITabelaPreco tabelaPreco, long tempoPermanencia);
}
