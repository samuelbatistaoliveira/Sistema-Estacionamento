package br.com.estacionamento.model;

public class Carro extends Veiculo {

    private int potencia;

    @Override
    public String getTipo() {
        return "Carro";
    }

    public int getPotencia() {
        return potencia;
    }

    public void setPotencia(int potencia) {
        this.potencia = potencia;
    }
}
