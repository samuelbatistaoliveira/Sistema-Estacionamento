package br.com.estacionamento.service;

import br.com.estacionamento.model.Funcionario;
import br.com.estacionamento.model.RegistroMovimentacao;
import br.com.estacionamento.model.Veiculo;

public class GestorMovimentacao {

    public RegistroMovimentacao iniciarMovimentacao(Veiculo veiculo) {
        RegistroMovimentacao registro = new RegistroMovimentacao();
        registro.registrarEntrada(veiculo);
        return registro;
    }

    public boolean finalizarMovimentacao(RegistroMovimentacao registro) {
        registro.registrarSaida(registro.getVeiculo());
        return true;
    }

    public void consultarMovimentacao(RegistroMovimentacao registro) {
    }
}
