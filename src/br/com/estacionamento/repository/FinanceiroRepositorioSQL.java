package br.com.estacionamento.repository;

import br.com.estacionamento.interfaces.IPagamentoRepositorio;
import br.com.estacionamento.model.Pagamento;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class FinanceiroRepositorioSQL implements IPagamentoRepositorio {

    @Override
    public List<Pagamento> buscarPorPeriodo(LocalDateTime inicio, LocalDateTime fim) {
        return new ArrayList<>();
    }

    @Override
    public void salvar(Pagamento pagamento) {
    }
}
