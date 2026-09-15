package br.com.estacionamento.service;

import br.com.estacionamento.interfaces.IMeioPagamento;

public class PagamentoPix implements IMeioPagamento {

    private String chavePix;

    @Override
    public boolean processar(double valor) {
        // TODO: integração com API Pix
        return true;
    }

    public String getChavePix() {
        return chavePix;
    }

    public void setChavePix(String chavePix) {
        this.chavePix = chavePix;
    }
}
