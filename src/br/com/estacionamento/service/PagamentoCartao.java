package br.com.estacionamento.service;

import br.com.estacionamento.interfaces.IMeioPagamento;

public class PagamentoCartao implements IMeioPagamento {

    @Override
    public boolean processar(double valor) {
        return true;
    }
}
