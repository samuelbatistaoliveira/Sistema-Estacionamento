using System.Net;

namespace MovimentacaoService.Clients;

public record VagaDto(long Id, string Numero, string Tipo, string Situacao, long? VeiculoId);

// vagas-service (Java): GET /vagas/disponiveis, PUT /vagas/{id}/ocupar?veiculoId=, PUT /vagas/{id}/liberar
public class VagasClient(HttpClient http)
{
    public async Task<List<VagaDto>> ListarDisponiveisAsync() =>
        await http.GetFromJsonAsync<List<VagaDto>>("/vagas/disponiveis") ?? [];

    // false quando outra requisição ocupou a vaga antes (409)
    public async Task<bool> OcuparAsync(long vagaId, long veiculoId)
    {
        using var resposta = await http.PutAsync($"/vagas/{vagaId}/ocupar?veiculoId={veiculoId}", null);
        if (resposta.StatusCode is HttpStatusCode.Conflict or HttpStatusCode.NotFound) return false;
        resposta.EnsureSuccessStatusCode();
        return true;
    }

    public async Task LiberarAsync(long vagaId)
    {
        using var resposta = await http.PutAsync($"/vagas/{vagaId}/liberar", null);
        resposta.EnsureSuccessStatusCode();
    }
}
