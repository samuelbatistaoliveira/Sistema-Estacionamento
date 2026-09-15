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

        // TODO: calcular valor da reserva usando tabelaPreco e processar pagamento com meioPagamento

        return reserva;
    }

    public boolean cancelarReserva(Reserva reserva) {
        reserva.setSituacaoReserva(StatusReserva.CANCELADA);
        // TODO: persistir alteração de status via repositório
        return true;
    }

    public void alterarStatusReserva(String idReserva, StatusReserva novoStatus) {
        // TODO: buscar reserva pelo id no repositório e atualizar o status
    }

    public StatusReserva consultarStatusReserva(int idReserva) {
        // TODO: buscar reserva pelo id no repositório e retornar o status
        return null;
    }
}
