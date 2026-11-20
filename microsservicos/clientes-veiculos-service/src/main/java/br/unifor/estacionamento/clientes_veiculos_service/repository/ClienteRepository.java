package br.unifor.estacionamento.clientes_veiculos_service.repository;

import br.unifor.estacionamento.clientes_veiculos_service.model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {
}