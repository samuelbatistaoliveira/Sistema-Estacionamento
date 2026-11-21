package br.unifor.estacionamento.vagas_service.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

@Component
public class VeiculoClient {

    @Value("${clientes.service.url}")
    private String clientesUrl;

    // chama GET /veiculos/{id} no servico de Clientes e Veiculos
    // devolve o veiculo, ou null se ele nao existir (404)
    public VeiculoDTO buscarPorId(Long id) {
        RestClient client = RestClient.create(clientesUrl);
        try {
            return client.get()
                    .uri("/veiculos/{id}", id)
                    .retrieve()
                    .body(VeiculoDTO.class);
        } catch (HttpClientErrorException.NotFound e) {
            return null;
        }
    }
}
