package br.unifor.estacionamento.clientes_veiculos_service.repository;

import br.unifor.estacionamento.clientes_veiculos_service.model.Veiculo;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VeiculoRepository extends JpaRepository<Veiculo, Long> {

    Veiculo findByPlaca(String placa);

    List<Veiculo> findByClienteId(Long clienteId);
}