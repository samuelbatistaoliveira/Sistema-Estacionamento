using System.Net;

namespace MovimentacaoService.Clients;

public record VeiculoDto(long Id, string Placa, string Tipo, long ClienteId);

// clientes-veiculos-service (Java): GET /veiculos/placa/{placa}
public class CadastroClient(HttpClient http)
{
    public async Task<VeiculoDto?> BuscarVeiculoPorPlacaAsync(string placa)
    {
        using var resposta = await http.GetAsync($"/veiculos/placa/{Uri.EscapeDataString(placa)}");
        if (resposta.StatusCode == HttpStatusCode.NotFound) return null;
        resposta.EnsureSuccessStatusCode();
        return await resposta.Content.ReadFromJsonAsync<VeiculoDto>();
    }
}
