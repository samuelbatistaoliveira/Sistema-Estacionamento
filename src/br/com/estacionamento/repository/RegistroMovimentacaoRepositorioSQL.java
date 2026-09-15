package br.com.estacionamento.repository;

import br.com.estacionamento.interfaces.IRegistroMovimentacaoRepositorio;
import br.com.estacionamento.model.RegistroMovimentacao;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class RegistroMovimentacaoRepositorioSQL implements IRegistroMovimentacaoRepositorio {

    @Override
    public void salvar(RegistroMovimentacao registro) {
    }

    @Override
    public List<RegistroMovimentacao> buscarPorPeriodo(LocalDateTime inicio, LocalDateTime fim) {
        return new ArrayList<>();
    }

    @Override
    public RegistroMovimentacao buscarPorId(int id) {
        return null;
    }
}
