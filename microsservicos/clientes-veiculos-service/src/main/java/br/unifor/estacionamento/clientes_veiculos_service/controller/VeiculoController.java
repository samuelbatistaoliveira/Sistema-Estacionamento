package br.unifor.estacionamento.clientes_veiculos_service.controller;

import br.unifor.estacionamento.clientes_veiculos_service.model.Veiculo;
import br.unifor.estacionamento.clientes_veiculos_service.repository.ClienteRepository;
import br.unifor.estacionamento.clientes_veiculos_service.repository.VeiculoRepository;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/veiculos")
public class VeiculoController {

    @Autowired
    private VeiculoRepository veiculoRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    @PostMapping
    public ResponseEntity<Veiculo> cadastrar(@RequestBody Veiculo veiculo) {
        // o dono (cliente) precisa existir
        if (veiculo.getClienteId() == null || !clienteRepository.existsById(veiculo.getClienteId())) {
            return ResponseEntity.badRequest().build();
        }
        // o tipo precisa ser CARRO ou MOTO
        if (!"CARRO".equals(veiculo.getTipo()) && !"MOTO".equals(veiculo.getTipo())) {
            return ResponseEntity.badRequest().build();
        }
        Veiculo salvo = veiculoRepository.save(veiculo);
        return ResponseEntity.status(201).body(salvo);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Veiculo> buscar(@PathVariable Long id) {
        Optional<Veiculo> opt = veiculoRepository.findById(id);
        if (!opt.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(opt.get());
    }

    @GetMapping("/placa/{placa}")
    public ResponseEntity<Veiculo> buscarPorPlaca(@PathVariable String placa) {
        Veiculo veiculo = veiculoRepository.findByPlaca(placa);
        if (veiculo == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(veiculo);
    }
}