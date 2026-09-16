package br.com.estacionamento.service;

import br.com.estacionamento.interfaces.IMeioPagamento;

public class PagamentoDinheiro implements IMeioPagamento {

    private double troco;

    @Override
    public boolean processar(double valor) {
        return true;
    }

    public double calcularTroco(double valor) {
        return troco;
    }

    public double getTroco() {
        return troco;
    }

    public void setTroco(double troco) {
        this.troco = troco;
    }
}
