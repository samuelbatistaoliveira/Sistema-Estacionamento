using System.Net;

namespace ReservasService.Clients;

public record VagaDto(long Id, string Numero, string Tipo, string Situacao, long? VeiculoId);

// vagas-service (Java): GET /vagas/{id}
public class VagasClient(HttpClient http)
{
    public async Task<VagaDto?> BuscarAsync(long id)
    {
        using var resposta = await http.GetAsync($"/vagas/{id}");
        if (resposta.StatusCode == HttpStatusCode.NotFound) return null;
        resposta.EnsureSuccessStatusCode();
        return await resposta.Content.ReadFromJsonAsync<VagaDto>();
    }
}
