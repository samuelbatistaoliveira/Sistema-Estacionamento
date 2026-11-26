using System.Text.Json.Serialization;

namespace ReservasService.Clients;

// tarifacao-service (Python): POST /tarifas/calcular {tipo_veiculo, minutos} -> {valor_total, ...}
public class TarifacaoClient(HttpClient http)
{
    private record Requisicao([property: JsonPropertyName("tipo_veiculo")] string TipoVeiculo,
                              [property: JsonPropertyName("minutos")] int Minutos);

    private record Resposta([property: JsonPropertyName("valor_total")] decimal ValorTotal);

    public async Task<decimal> CalcularAsync(string tipoVeiculo, int minutos)
    {
        using var resposta = await http.PostAsJsonAsync("/tarifas/calcular", new Requisicao(tipoVeiculo.ToLowerInvariant(), minutos));
        resposta.EnsureSuccessStatusCode();
        var corpo = await resposta.Content.ReadFromJsonAsync<Resposta>();
        return corpo!.ValorTotal;
    }
}
