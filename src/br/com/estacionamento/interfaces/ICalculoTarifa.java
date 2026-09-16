package br.com.estacionamento.interfaces;

import br.com.estacionamento.model.Veiculo;

import java.time.LocalDateTime;

public interface ICalculoTarifa {

    double calcularTempoPermanencia(LocalDateTime dataHoraEntrada, LocalDateTime dataHoraSaida);

    double calcularValor(ITabelaPreco tabelaPreco, long tempoPermanencia);
}
