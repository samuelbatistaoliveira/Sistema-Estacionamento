package br.com.estacionamento.interfaces;

import br.com.estacionamento.model.RegistroMovimentacao;

import java.time.LocalDateTime;
import java.util.List;

public interface IRegistroMovimentacaoRepositorio {

    List<RegistroMovimentacao> buscarPorPeriodo(LocalDateTime inicio, LocalDateTime fim);

    void salvar(RegistroMovimentacao registro);

    RegistroMovimentacao buscarPorId(int id);
}
