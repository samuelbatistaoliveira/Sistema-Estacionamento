package br.com.estacionamento.model;

public class Vaga {

    private int idVaga;
    private String tipo;
    private boolean situacaoVaga;

    public Vaga() {
    }

    public Vaga(int idVaga, String tipo, boolean situacaoVaga) {
        this.idVaga = idVaga;
        this.tipo = tipo;
        this.situacaoVaga = situacaoVaga;
    }

    public int getIdVaga() {
        return idVaga;
    }

    public void setIdVaga(int idVaga) {
        this.idVaga = idVaga;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public boolean isSituacaoVaga() {
        return situacaoVaga;
    }

    public void setSituacaoVaga(boolean situacaoVaga) {
        this.situacaoVaga = situacaoVaga;
    }
}
