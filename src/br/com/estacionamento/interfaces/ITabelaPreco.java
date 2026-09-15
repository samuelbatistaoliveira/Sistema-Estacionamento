package br.com.estacionamento.interfaces;

public interface ITabelaPreco {

    double consultarValorBase(int minutos, String tipoVeiculo);
}
