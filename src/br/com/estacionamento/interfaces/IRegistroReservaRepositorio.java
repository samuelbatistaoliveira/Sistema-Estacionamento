package br.com.estacionamento.interfaces;

import br.com.estacionamento.model.Reserva;

import java.time.LocalDateTime;
import java.util.List;

public interface IRegistroReservaRepositorio {

    List<Reserva> buscarPorPeriodo(LocalDateTime dataInicio, LocalDateTime dataFim);

    void salvar(Reserva reserva);

    Reserva buscarPorId(int idReserva);
}
