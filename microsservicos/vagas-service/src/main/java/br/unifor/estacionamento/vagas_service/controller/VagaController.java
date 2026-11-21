package br.unifor.estacionamento.vagas_service.controller;

import br.unifor.estacionamento.vagas_service.client.VeiculoClient;
import br.unifor.estacionamento.vagas_service.client.VeiculoDTO;
import br.unifor.estacionamento.vagas_service.model.Vaga;
import br.unifor.estacionamento.vagas_service.repository.VagaRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.ResourceAccessException;

@RestController
@RequestMapping("/vagas")
public class VagaController {

    @Autowired
    private VagaRepository vagaRepository;

    @Autowired
    private VeiculoClient veiculoClient;

    @PostMapping
    public ResponseEntity<Vaga> cadastrar(@RequestBody Vaga vaga) {
        if (!"CARRO".equals(vaga.getTipo()) && !"MOTO".equals(vaga.getTipo())) {
            return ResponseEntity.badRequest().build();
        }
        vaga.setSituacao("LIVRE");
        vaga.setVeiculoId(null);
        Vaga salva = vagaRepository.save(vaga);
        return ResponseEntity.status(201).body(salva);
    }

    @GetMapping
    public List<Vaga> listar() {
        return vagaRepository.findAll();
    }

    @GetMapping("/disponiveis")
    public List<Vaga> listarDisponiveis() {
        return vagaRepository.findBySituacao("LIVRE");
    }

    @GetMapping("/{id}")
    public ResponseEntity<Vaga> buscar(@PathVariable Long id) {
        Optional<Vaga> opt = vagaRepository.findById(id);
        if (!opt.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(opt.get());
    }

    @PutMapping("/{id}/ocupar")
    public ResponseEntity<Vaga> ocupar(@PathVariable Long id, @RequestParam Long veiculoId) {
        Optional<Vaga> opt = vagaRepository.findById(id);
        if (!opt.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        Vaga vaga = opt.get();

        // vaga já ocupada
        if (!"LIVRE".equals(vaga.getSituacao())) {
            return ResponseEntity.status(409).build();
        }

        // pergunta ao serviço de Clientes e Veículos se o veículo existe
        VeiculoDTO veiculo;
        try {
            veiculo = veiculoClient.buscarPorId(veiculoId);
        } catch (ResourceAccessException e) {
            return ResponseEntity.status(503).build();   // o outro serviço está fora do ar
        }
        if (veiculo == null) {
            return ResponseEntity.badRequest().build();  // veículo não existe
        }

        // o tipo do veículo precisa ser o mesmo tipo da vaga
        if (!vaga.getTipo().equals(veiculo.getTipo())) {
            return ResponseEntity.badRequest().build();
        }

        vaga.setSituacao("OCUPADA");
        vaga.setVeiculoId(veiculoId);
        return ResponseEntity.ok(vagaRepository.save(vaga));
    }

    @PutMapping("/{id}/liberar")
    public ResponseEntity<Vaga> liberar(@PathVariable Long id) {
        Optional<Vaga> opt = vagaRepository.findById(id);
        if (!opt.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        Vaga vaga = opt.get();
        vaga.setSituacao("LIVRE");
        vaga.setVeiculoId(null);
        return ResponseEntity.ok(vagaRepository.save(vaga));
    }
}