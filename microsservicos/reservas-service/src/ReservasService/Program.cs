using System.Text.Json.Serialization;
using Microsoft.EntityFrameworkCore;
using ReservasService;
using ReservasService.Clients;
using ReservasService.Data;
using ReservasService.Services;

var builder = WebApplication.CreateBuilder(args);
var config = builder.Configuration;

builder.Services.AddControllers()
    .AddJsonOptions(o => o.JsonSerializerOptions.Converters.Add(new JsonStringEnumConverter()));

builder.Services.AddDbContext<OperacionalDbContext>(o => o.UseNpgsql(config.GetConnectionString("Operacional")));

void Cliente<T>(string chave) where T : class =>
    builder.Services.AddHttpClient<T>(c =>
    {
        c.BaseAddress = new Uri(config[$"Servicos:{chave}"]!);
        c.Timeout = TimeSpan.FromSeconds(5);
    });
Cliente<CadastroClient>("Cadastro");
Cliente<VagasClient>("Vagas");
Cliente<TarifacaoClient>("Tarifacao");
Cliente<PagamentosClient>("Pagamentos");

builder.Services.AddScoped<GestorReserva>();

var app = builder.Build();

// Converte erros de negócio e falhas de serviços dependentes em respostas HTTP claras.
app.Use(async (ctx, next) =>
{
    try
    {
        await next(ctx);
    }
    catch (ErroNegocioException e)
    {
        ctx.Response.StatusCode = e.Status;
        await ctx.Response.WriteAsJsonAsync(new { erro = e.Message });
    }
    catch (Exception e) when ((e is HttpRequestException || e is TaskCanceledException) && !ctx.RequestAborted.IsCancellationRequested)
    {
        app.Logger.LogError(e, "Serviço dependente indisponível");
        ctx.Response.StatusCode = StatusCodes.Status503ServiceUnavailable;
        await ctx.Response.WriteAsJsonAsync(new { erro = "Serviço dependente indisponível. Tente novamente." });
    }
});

app.MapControllers();
app.Run();
