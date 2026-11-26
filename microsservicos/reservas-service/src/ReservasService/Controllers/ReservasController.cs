using Microsoft.AspNetCore.Mvc;
using ReservasService.Models;
using ReservasService.Services;

namespace ReservasService.Controllers;

public record ConfirmarReservaRequest(string MetodoPagamento);

[ApiController]
[Route("reservas")]
public class ReservasController(GestorReserva gestor) : ControllerBase
{
    [HttpPost]
    public async Task<ActionResult<Reserva>> Criar(NovaReservaRequest req)
    {
        var reserva = await gestor.CriarAsync(req);
        return CreatedAtAction(nameof(Buscar), new { id = reserva.Id }, reserva);
    }

    [HttpGet]
    public Task<List<Reserva>> Listar([FromQuery] long? clienteId) => gestor.ListarAsync(clienteId);

    [HttpGet("{id:int}")]
    public Task<Reserva> Buscar(int id) => gestor.BuscarAsync(id);

    [HttpPut("{id:int}/confirmar")]
    public Task<Reserva> Confirmar(int id, ConfirmarReservaRequest req) => gestor.ConfirmarAsync(id, req.MetodoPagamento);

    [HttpPut("{id:int}/cancelar")]
    public Task<Reserva> Cancelar(int id) => gestor.CancelarAsync(id);
}
