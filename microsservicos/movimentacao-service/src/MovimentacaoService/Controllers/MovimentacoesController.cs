using Microsoft.AspNetCore.Mvc;
using MovimentacaoService.Models;
using MovimentacaoService.Services;

namespace MovimentacaoService.Controllers;

public record EntradaRequest(string Placa);
public record SaidaRequest(string Placa, string? MetodoPagamento);

[ApiController]
[Route("movimentacoes")]
public class MovimentacoesController(GestorMovimentacao gestor) : ControllerBase
{
    [HttpPost("entrada")]
    public async Task<ActionResult<Movimentacao>> Entrada(EntradaRequest req)
    {
        var movimentacao = await gestor.RegistrarEntradaAsync(req.Placa);
        return CreatedAtAction(nameof(Buscar), new { id = movimentacao.Id }, movimentacao);
    }

    [HttpPost("saida")]
    public Task<Movimentacao> Saida(SaidaRequest req) => gestor.RegistrarSaidaAsync(req.Placa, req.MetodoPagamento);

    [HttpGet]
    public Task<List<Movimentacao>> Listar([FromQuery] StatusMovimentacao? status) => gestor.ListarAsync(status);

    [HttpGet("{id:int}")]
    public Task<Movimentacao> Buscar(int id) => gestor.BuscarAsync(id);
}
