import os
import httpx

from fastapi import FastAPI, HTTPException
from pydantic import BaseModel, Field
from sqlalchemy import text
from sqlalchemy.exc import IntegrityError
from database import engine

app = FastAPI(title="Microsserviço de Pagamentos")

TARIFACAO_URL = os.getenv(
    "TARIFACAO_URL",
    "http://127.0.0.1:8001"
)


class CriarPagamento(BaseModel):
    movimentacao_id: int = Field(gt=0)
    tipo_veiculo: str
    minutos: int = Field(gt=0)
    forma_pagamento: str


@app.get("/")
def inicio():
    return {
        "mensagem": "Microsserviço de Pagamentos funcionando!"
    }


@app.get("/pagamentos")
def listar_pagamentos():
    with engine.connect() as connection:
        resultado = connection.execute(
            text("SELECT * FROM pagamentos")
        ).mappings().all()

        return [dict(pagamento) for pagamento in resultado]


@app.get("/pagamentos/{id}")
def consultar_pagamento(id: int):
    with engine.connect() as connection:
        resultado = connection.execute(
            text("SELECT * FROM pagamentos WHERE id = :id"),
            {"id": id}
        ).mappings().first()

        if resultado is None:
            raise HTTPException(
                status_code=404,
                detail="Pagamento não encontrado"
            )

        return dict(resultado)

@app.post("/pagamentos", status_code=201)
def criar_pagamento(dados: CriarPagamento):
    forma = dados.forma_pagamento.strip().lower()

    if forma not in ["pix", "cartao", "dinheiro"]:
        raise HTTPException(
            status_code=400,
            detail="Forma de pagamento inválida"
        )

    try:
        resposta = httpx.post(
            f"{TARIFACAO_URL}/tarifas/calcular",
            json={
                "tipo_veiculo": dados.tipo_veiculo,
                "minutos": dados.minutos
            },
            timeout=10.0
        )

    except httpx.RequestError:
        raise HTTPException(
            status_code=503,
            detail="Microsserviço de Tarifação indisponível"
        )

    if resposta.status_code == 400:
        raise HTTPException(
            status_code=400,
            detail="Tipo de veículo inválido"
        )

    if resposta.status_code != 200:
        raise HTTPException(
            status_code=502,
            detail="Erro ao consultar a Tarifação"
        )

    valor = resposta.json()["valor_total"]

    try:
        with engine.begin() as connection:
            resultado = connection.execute(
                text("""
                    INSERT INTO pagamentos (
                        movimentacao_id,
                        valor,
                        forma_pagamento,
                        status
                    )
                    VALUES (
                        :movimentacao_id,
                        :valor,
                        :forma_pagamento,
                        'pendente'
                    )
                """),
                {
                    "movimentacao_id": dados.movimentacao_id,
                    "valor": valor,
                    "forma_pagamento": forma
                }
            )

            pagamento_id = resultado.lastrowid

    except IntegrityError as erro:
        if erro.orig.args[0] == 1062:
            raise HTTPException(
                status_code=409,
                detail="Já existe um pagamento para essa movimentação"
            )
        raise

    return {
        "id": pagamento_id,
        "movimentacao_id": dados.movimentacao_id,
        "valor": valor,
        "forma_pagamento": forma,
        "status": "pendente"
    }

@app.patch("/pagamentos/{id}/confirmar")
def confirmar_pagamento(id: int):
    with engine.begin() as connection:
        pagamento = connection.execute(
            text("""
                SELECT * FROM pagamentos
                WHERE id = :id
                FOR UPDATE
            """),
            {"id": id}
        ).mappings().first()

        if pagamento is None:
            raise HTTPException(
                status_code=404,
                detail="Pagamento não encontrado"
            )

        if pagamento["status"] != "pendente":
            raise HTTPException(
                status_code=409,
                detail="O pagamento não está pendente"
            )

        connection.execute(
            text("""
                UPDATE pagamentos
                SET status = 'pago',
                    data_pagamento = NOW()
                WHERE id = :id
            """),
            {"id": id}
        )

        resultado = connection.execute(
            text("SELECT * FROM pagamentos WHERE id = :id"),
            {"id": id}
        ).mappings().first()

        return dict(resultado)