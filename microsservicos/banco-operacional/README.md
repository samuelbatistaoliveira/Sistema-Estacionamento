# Banco Operacional

Banco PostgreSQL **compartilhado só por `reservas-service` e `movimentacao-service`** (ambos em C#), conforme o diagrama de arquitetura. Os demais serviços têm bancos próprios (Cadastral e Financeiro).

| Tabela          | Quem escreve                                   | Quem lê                 |
| --------------- | ---------------------------------------------- | ----------------------- |
| `reservas`      | Reservas (cria/confirma/cancela); Movimentação (marca `CONCLUIDA` na entrada) | os dois |
| `movimentacoes` | Movimentação                                   | Movimentação            |

O esquema fica em [`init.sql`](init.sql) e é aplicado automaticamente na primeira subida do container. Os serviços **não** criam tabelas sozinhos, para que nenhum dos dois seja "dono" do esquema.

Regras garantidas pelo próprio banco:

- duas reservas `PENDENTE`/`CONFIRMADA` nunca se sobrepõem na mesma vaga (`EXCLUDE USING gist`);
- um veículo só tem uma movimentação `ABERTA` por vez (índice único parcial).

## Subir

```
docker compose up -d
```

Conexão padrão: `Host=localhost;Port=5433;Database=operacional_db;Username=operacional;Password=operacional`.
Em outros ambientes, sobrescreva com a variável `ConnectionStrings__Operacional`.

Para recriar o banco do zero (apaga os dados): `docker compose down -v`.
