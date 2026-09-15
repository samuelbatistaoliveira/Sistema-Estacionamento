package br.com.estacionamento.model;

import br.com.estacionamento.interfaces.IMeioPagamento;

public class Pagamento {

    private int idPagamento;
    private double valor;
    private IMeioPagamento meioPagamento;
    private TipoPagamento tipo;

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

    public IMeioPagamento getMeioPagamento() {
        return meioPagamento;
    }

    public void setMeioPagamento(IMeioPagamento meioPagamento) {
        this.meioPagamento = meioPagamento;
    }

    public TipoPagamento getTipo() {
        return tipo;
    }

    public void setTipo(TipoPagamento tipo) {
        this.tipo = tipo;
    }
}
