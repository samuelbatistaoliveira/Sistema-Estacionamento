package br.com.estacionamento.model;

import br.com.estacionamento.interfaces.IMeioPagamento;
import br.com.estacionamento.model.TipoPagamento;

public class Pagamento {

    private int idPagamento;
    private double valor;
    private br.com.estacionamento.model.TipoPagamento tipo;
    private IMeioPagamento meioPagamento;

    public boolean processar() {
        return meioPagamento.processar(valor);
    }

    public int getIdPagamento() {
        return idPagamento;
    }

    public void setIdPagamento(int idPagamento) {
        this.idPagamento = idPagamento;
    }

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }

    public void setMeioPagamento(IMeioPagamento meioPagamento) {
        this.meioPagamento = meioPagamento;
    }

    public br.com.estacionamento.model.TipoPagamento getTipo() {
        return tipo;
    }

    public void setTipo(TipoPagamento tipo) {
        this.tipo = tipo;
    }
}
