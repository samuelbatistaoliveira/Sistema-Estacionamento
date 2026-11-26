using System.Text.Json.Serialization;

namespace ReservasService.Clients;

public record ResultadoPagamento(string Id, bool Aprovado);

// pagamentos-service (Python). O serviço ainda não tem endpoints no main; este é o contrato esperado:
// POST /pagamentos {origem, referencia_id, valor, metodo} -> {id, status: "APROVADO" | "RECUSADO"}
public class PagamentosClient(HttpClient http)
{
    public static readonly string[] MetodosValidos = ["DINHEIRO", "CARTAO", "PIX"];

    private record Requisicao([property: JsonPropertyName("origem")] string Origem,
                              [property: JsonPropertyName("referencia_id")] int ReferenciaId,
                              [property: JsonPropertyName("valor")] decimal Valor,
                              [property: JsonPropertyName("metodo")] string Metodo);

    private record Resposta([property: JsonPropertyName("id")] string Id,
                            [property: JsonPropertyName("status")] string Status);

    public async Task<ResultadoPagamento> PagarAsync(string origem, int referenciaId, decimal valor, string metodo)
    {
        using var resposta = await http.PostAsJsonAsync("/pagamentos", new Requisicao(origem, referenciaId, valor, metodo));
        resposta.EnsureSuccessStatusCode();
        var corpo = await resposta.Content.ReadFromJsonAsync<Resposta>();
        return new ResultadoPagamento(corpo!.Id, corpo.Status == "APROVADO");
    }
}
