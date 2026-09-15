package br.com.estacionamento.repository;

import br.com.estacionamento.interfaces.IRegistroReservaRepositorio;
import br.com.estacionamento.model.Reserva;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class RegistroReservaRepositorioSQL implements IRegistroReservaRepositorio {

    @Override
    public Reserva buscarPorId(int idReserva) {
        return null;
    }

    @Override
    public void salvar(Reserva reserva) {
    }

    @Override
    public List<Reserva> buscarPorPeriodo(LocalDateTime dataInicio, LocalDateTime dataFim) {
        return new ArrayList<>();
    }
}
