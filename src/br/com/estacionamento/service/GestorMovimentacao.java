package br.com.estacionamento.service;

import br.com.estacionamento.model.Funcionario;
import br.com.estacionamento.model.RegistroMovimentacao;
import br.com.estacionamento.model.Veiculo;

public class GestorMovimentacao {

    public RegistroMovimentacao iniciarMovimentacao(Veiculo veiculo, Funcionario funcionario) {
        RegistroMovimentacao registro = new RegistroMovimentacao();
        registro.registrarEntrada(veiculo);
        registro.setFuncionario(funcionario);
        return registro;
    }

    public boolean finalizarMovimentacao(RegistroMovimentacao registro) {
        registro.registrarSaida(registro.getVeiculo());
        // TODO: calcular valor total (usando Calculo/ICalculoTarifa) e associar pagamento
        return true;
    }

    public void consultarMovimentacao(RegistroMovimentacao registro) {
        // TODO: implementar consulta de detalhes da movimentação
    }
}
