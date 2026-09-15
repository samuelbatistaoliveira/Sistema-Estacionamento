package br.com.estacionamento.service;

import br.com.estacionamento.interfaces.ITabelaPreco;

public class TabelaPrecoMoto implements ITabelaPreco {

    private static final double VALOR_POR_MINUTO = 0.12;

    @Override
    public double consultarValorBase(int minutos, String tipoVeiculo) {
        return minutos * VALOR_POR_MINUTO;
    }
}
