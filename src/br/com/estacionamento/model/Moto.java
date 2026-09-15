package br.com.estacionamento.model;

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
