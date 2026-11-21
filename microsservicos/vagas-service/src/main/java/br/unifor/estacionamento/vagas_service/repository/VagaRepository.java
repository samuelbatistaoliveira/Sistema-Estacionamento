package br.unifor.estacionamento.vagas_service.repository;

import br.unifor.estacionamento.vagas_service.model.Vaga;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VagaRepository extends JpaRepository<Vaga, Long> {

    List<Vaga> findBySituacao(String situacao);
}
