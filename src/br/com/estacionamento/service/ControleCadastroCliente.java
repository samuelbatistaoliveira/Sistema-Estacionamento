package br.com.estacionamento.service;

import br.com.estacionamento.interfaces.ICadastroCliente;
import br.com.estacionamento.model.Cliente;

public class ControleCadastroCliente implements ICadastroCliente {

    private int idCliente;
    private String nomeCliente;
    private int cpf;

    @Override
    public void cadastrarCliente(Cliente cliente) {
        this.idCliente = cliente.getIdCliente();
        this.nomeCliente = cliente.getNomeCliente();
        this.cpf = cliente.getCpf();
        // TODO: persistir o cliente (via repositório, quando existir)
    }

    public int getIdCliente() {
        return idCliente;
    }

    public String getNomeCliente() {
        return nomeCliente;
    }

    public int getCpf() {
        return cpf;
    }
}
