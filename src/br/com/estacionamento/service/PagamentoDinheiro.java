package br.com.estacionamento.service;

import br.com.estacionamento.interfaces.IMeioPagamento;

public class PagamentoDinheiro implements IMeioPagamento {

    private double troco;

    @Override
    public boolean processar(double valor) {
        // TODO: implementar lógica de recebimento em dinheiro
        return true;
    }

    public double calcularTroco(double valor) {
        // TODO: calcular troco com base no valor recebido
        return troco;
    }

    public double getTroco() {
        return troco;
    }

    public void setTroco(double troco) {
        this.troco = troco;
    }
}
