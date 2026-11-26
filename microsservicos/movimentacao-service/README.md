# movimentacao-service (C# / ASP.NET Core)

Microsserviço de **Movimentação (entrada e saída)** do Sistema de Estacionamento. Usa o [Banco Operacional](../banco-operacional), compartilhado só com o `reservas-service`.

## Fluxo

**Entrada** — `POST /movimentacoes/entrada {placa}`

1. Busca o veículo pela placa no **Cadastro** (não cadastrado → `400`).
2. Recusa se o veículo já tem movimentação aberta (`409`).
3. Procura no Banco Operacional uma reserva `CONFIRMADA` do veículo valendo agora (aceita chegar até 15 min antes do início).
4. Ocupa a vaga no **Vagas**: primeiro a vaga reservada; se ela estiver ocupada, outra livre do mesmo tipo. Vagas com reserva confirmada de **outro** veículo valendo agora são puladas.
5. Grava a movimentação `ABERTA` e marca a reserva como `CONCLUIDA`, na mesma transação. Se a gravação falhar, a vaga é liberada de volta.

**Saída** — `POST /movimentacoes/saida {placa, metodoPagamento}`

1. Calcula os minutos cobráveis: sem reserva, toda a permanência (mínimo 1 min); com reserva, só o que passou do fim da reserva (o período reservado já foi pago na confirmação).
2. Se houver minutos, calcula o valor na **Tarifação** e cobra no **Pagamentos** (recusado → `402`, o veículo continua dentro).
3. Grava a movimentação `FINALIZADA` com valor e id do pagamento e libera a vaga no **Vagas**. Se o Vagas estiver fora do ar nesse momento, a saída continua registrada e o erro fica no log para liberar a vaga manualmente.

| Método | Rota                              | Respostas                                    |
| ------ | --------------------------------- | -------------------------------------------- |
| POST   | `/movimentacoes/entrada`          | 201, 400, 409 (já dentro / sem vaga), 503    |
| POST   | `/movimentacoes/saida`            | 200, 400 (falta método), 402, 404, 503       |
| GET    | `/movimentacoes?status=ABERTA`    | 200                                          |
| GET    | `/movimentacoes/{id}`             | 200, 404                                     |

Exemplos prontos em [`MovimentacaoService.http`](src/MovimentacaoService/MovimentacaoService.http).

## Executar

Pré-requisitos: .NET SDK 10 e o Banco Operacional no ar (`docker compose up -d` em `../banco-operacional`).

```
dotnet run --project src/MovimentacaoService      # sobe em http://localhost:8084
dotnet test tests/MovimentacaoService.Tests
```

Endereços dos outros serviços e a connection string ficam em `appsettings.json` e podem ser sobrescritos por variáveis de ambiente, por exemplo `Servicos__Vagas=http://vagas:8082` ou `ConnectionStrings__Operacional=...`.

## Contrato esperado do pagamentos-service

O `pagamentos-service` ainda não tem endpoints na `main`. Este serviço chama:

```
POST /pagamentos  {"origem": "MOVIMENTACAO", "referencia_id": 1, "valor": 10.0, "metodo": "PIX"}
→ 201 {"id": "...", "status": "APROVADO" | "RECUSADO"}
```
