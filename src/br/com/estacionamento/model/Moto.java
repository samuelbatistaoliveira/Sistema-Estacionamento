package br.com.estacionamento.model;

import br.com.estacionamento.model.Veiculo;

public class Moto extends Veiculo {

    private int cilindradas;

    @Override
    public String getTipo() {
        return "Moto";
    }

    public int getCilindradas() {
        return cilindradas;
    }

    public void setCilindradas(int cilindradas) {
        this.cilindradas = cilindradas;
    }
}
