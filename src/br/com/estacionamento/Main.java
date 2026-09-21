package br.com.estacionamento;

import br.com.estacionamento.interfaces.ICalculoTarifa;
import br.com.estacionamento.interfaces.IMeioPagamento;
import br.com.estacionamento.interfaces.ITabelaPreco;
import br.com.estacionamento.model.Carro;
import br.com.estacionamento.model.Cliente;
import br.com.estacionamento.model.Moto;
import br.com.estacionamento.model.Pagamento;
import br.com.estacionamento.model.RegistroMovimentacao;
import br.com.estacionamento.model.Reserva;
import br.com.estacionamento.model.TipoPagamento;
import br.com.estacionamento.model.Veiculo;
import br.com.estacionamento.service.Calculo;
import br.com.estacionamento.service.GestorMovimentacao;
import br.com.estacionamento.service.GestorReserva;
import br.com.estacionamento.service.PagamentoCartao;
import br.com.estacionamento.service.PagamentoPix;
import br.com.estacionamento.service.TabelaPrecoCarro;
import br.com.estacionamento.service.TabelaPrecoMoto;

import java.time.LocalDateTime;
import java.util.Locale;


public class Main {

    private static final Locale PT_BR = Locale.forLanguageTag("pt-BR");

    public static void main(String[] args) {
        Cliente cliente = new Cliente();
        cliente.setIdCliente(1);
        cliente.setNomeCliente("Cliente de Demonstração");

        Carro carro = new Carro();
        carro.setIdVeiculo(1);
        carro.setMarca("Chevrolet");
        carro.setModelo("Onix");
        carro.setCor("Prata");
        carro.setPlaca("ABC1D23");
        carro.setCliente(cliente);

        Moto moto = new Moto();
        moto.setIdVeiculo(2);
        moto.setMarca("Honda");
        moto.setModelo("CG 160");
        moto.setCor("Vermelha");
        moto.setPlaca("XYZ9K87");
        moto.setCliente(cliente);

        GestorMovimentacao gestorMovimentacao = new GestorMovimentacao();
        ICalculoTarifa calculo = new Calculo();

        System.out.println("=== SISTEMA DE ESTACIONAMENTO ===");

        simularMovimentacao(gestorMovimentacao, calculo, carro,
                new TabelaPrecoCarro(), new PagamentoPix(), TipoPagamento.PIX, 90);

        simularMovimentacao(gestorMovimentacao, calculo, moto,
                new TabelaPrecoMoto(), new PagamentoCartao(), TipoPagamento.CARTAO, 45);

        simularReserva(cliente, carro);
    }

    private static void simularMovimentacao(GestorMovimentacao gestor, ICalculoTarifa calculo,
                                            Veiculo veiculo, ITabelaPreco tabela,
                                            IMeioPagamento meioPagamento, TipoPagamento tipo,
                                            int minutosDePermanencia) {
        System.out.println();
        System.out.println("--- " + veiculo.getTipo() + ": " + veiculo.getMarca() + " " + veiculo.getModelo() + " ---");

        RegistroMovimentacao registro = gestor.iniciarMovimentacao(veiculo);
        registro.setDataHoraEntrada(LocalDateTime.now().minusMinutes(minutosDePermanencia));

        gestor.finalizarMovimentacao(registro);
        System.out.println("Entrada e saída registradas");

        long minutos = (long) calculo.calcularTempoPermanencia(
                registro.getDataHoraEntrada(), registro.getDataHoraSaida());
        double valor = calculo.calcularValor(tabela, minutos, veiculo.getTipo());
        System.out.println("Permanência:        " + minutos + " min");
        System.out.println(String.format(PT_BR, "Valor a pagar:      R$ %.2f", valor));

        Pagamento pagamento = new Pagamento();
        pagamento.setValor(valor);
        pagamento.setTipo(tipo);
        pagamento.setMeioPagamento(meioPagamento);
        registro.setPagamento(pagamento);

        boolean aprovado = pagamento.processar();
        System.out.println("Pagamento (" + tipo + "): " + (aprovado ? "aprovado" : "recusado"));
    }

    private static void simularReserva(Cliente cliente, Veiculo veiculo) {
        System.out.println();
        System.out.println("--- Reserva ---");

        GestorReserva gestorReserva = new GestorReserva();
        LocalDateTime inicio = LocalDateTime.now().plusHours(1);
        LocalDateTime fim = inicio.plusHours(3);

        Reserva reserva = gestorReserva.criarReserva(
                veiculo, cliente, new TabelaPrecoCarro(), inicio, fim, new PagamentoPix());
        System.out.println("Reserva criada para " + reserva.getCliente().getNomeCliente() + " (duração: 3 horas)");
        System.out.println("Status: " + reserva.consultarStatusReserva());

        gestorReserva.cancelarReserva(reserva);
        System.out.println("Status após cancelamento: " + reserva.consultarStatusReserva());
    }
}