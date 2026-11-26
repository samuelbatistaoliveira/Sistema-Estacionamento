using System.Net;

namespace ReservasService.Clients;

public record VeiculoDto(long Id, string Placa, string Tipo, long ClienteId);

// clientes-veiculos-service (Java): GET /clientes/{id} e GET /veiculos/{id}
public class CadastroClient(HttpClient http)
{
    public async Task<bool> ClienteExisteAsync(long id)
    {
        using var resposta = await http.GetAsync($"/clientes/{id}");
        if (resposta.StatusCode == HttpStatusCode.NotFound) return false;
        resposta.EnsureSuccessStatusCode();
        return true;
    }

    public async Task<VeiculoDto?> BuscarVeiculoAsync(long id)
    {
        using var resposta = await http.GetAsync($"/veiculos/{id}");
        if (resposta.StatusCode == HttpStatusCode.NotFound) return null;
        resposta.EnsureSuccessStatusCode();
        return await resposta.Content.ReadFromJsonAsync<VeiculoDto>();
    }
}
