package br.com.estacionamento.service;

import br.com.estacionamento.interfaces.IMeioPagamento;
import br.com.estacionamento.interfaces.ITabelaPreco;
import br.com.estacionamento.model.Cliente;
import br.com.estacionamento.model.Reserva;
import br.com.estacionamento.model.StatusReserva;
import br.com.estacionamento.model.Veiculo;

import java.time.LocalDateTime;

public class GestorReserva {

    public Reserva criarReserva(Veiculo tipoVeiculo, Cliente cliente, ITabelaPreco tabelaPreco,
                                 LocalDateTime dataHoraInicio, LocalDateTime dataHoraFim,
                                 IMeioPagamento meioPagamento) {
        Reserva reserva = new Reserva();
        reserva.setCliente(cliente);
        reserva.setDataInicioReserva(dataHoraInicio);
        reserva.setDataFimReserva(dataHoraFim);
        reserva.setDataReserva(LocalDateTime.now());
        reserva.setSituacaoReserva(StatusReserva.PENDENTE);


        return reserva;
    }

    public boolean cancelarReserva(Reserva reserva) {
        reserva.setSituacaoReserva(StatusReserva.CANCELADA);
        return true;
    }

    public void alterarStatusReserva(int idReserva, StatusReserva novoStatus) {
    }
}
