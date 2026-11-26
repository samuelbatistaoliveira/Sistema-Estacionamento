# reservas-service (C# / ASP.NET Core)

Microsserviço de **Reservas** do Sistema de Estacionamento. Usa o [Banco Operacional](../banco-operacional), compartilhado só com o `movimentacao-service`.

## Fluxo

1. `POST /reservas` valida, no **Cadastro**, que o cliente existe e é dono do veículo; nas **Vagas**, que a vaga existe e é do mesmo tipo do veículo; verifica conflito de horário (mesma vaga ou mesmo veículo); calcula o valor na **Tarifação**. A reserva nasce `PENDENTE`.
2. `PUT /reservas/{id}/confirmar` cobra o valor no **Pagamentos**. Aprovado → `CONFIRMADA`; recusado → `402`.
3. `PUT /reservas/{id}/cancelar` cancela reservas `PENDENTE` ou `CONFIRMADA` que ainda não começaram.
4. Na entrada do veículo, o `movimentacao-service` lê a reserva `CONFIRMADA` no Banco Operacional, usa a vaga reservada e marca a reserva como `CONCLUIDA`.

A vaga **não** é ocupada no momento da reserva (a reserva é para um horário futuro); quem ocupa e libera a vaga é a Movimentação.

| Método | Rota                        | Respostas                                   |
| ------ | --------------------------- | ------------------------------------------- |
| POST   | `/reservas`                 | 201, 400 (dados inválidos), 409 (conflito), 503 |
| GET    | `/reservas?clienteId=`      | 200                                         |
| GET    | `/reservas/{id}`            | 200, 404                                    |
| PUT    | `/reservas/{id}/confirmar`  | 200, 400, 402 (recusado), 404, 409, 503     |
| PUT    | `/reservas/{id}/cancelar`   | 200, 404, 409                               |

Exemplos prontos em [`ReservasService.http`](src/ReservasService/ReservasService.http).

## Executar

Pré-requisitos: .NET SDK 10 e o Banco Operacional no ar (`docker compose up -d` em `../banco-operacional`).

```
dotnet run --project src/ReservasService      # sobe em http://localhost:8083
dotnet test tests/ReservasService.Tests
```

Endereços dos outros serviços e a connection string ficam em `appsettings.json` e podem ser sobrescritos por variáveis de ambiente, por exemplo `Servicos__Tarifacao=http://tarifacao:8000` ou `ConnectionStrings__Operacional=...`.

## Contrato esperado do pagamentos-service

O `pagamentos-service` ainda não tem endpoints na `main`. Este serviço chama:

```
POST /pagamentos  {"origem": "RESERVA", "referencia_id": 1, "valor": 15.0, "metodo": "PIX"}
→ 201 {"id": "...", "status": "APROVADO" | "RECUSADO"}
```
