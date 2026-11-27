from fastapi import FastAPI, HTTPException, Depends
from pydantic import BaseModel, Field
from sqlalchemy import text
from sqlalchemy.orm import Session
from database import get_db

app = FastAPI(title="Microsserviço de Tarifação")


class CalculoTarifa(BaseModel):
    tipo_veiculo: str
    minutos: int = Field(gt=0)


@app.get("/")
def inicio():
    return {
        "mensagem": "Microsserviço de Tarifação funcionando!"
    }


@app.get("/tarifas")
def listar_tarifas(db: Session = Depends(get_db)):
    resultado = db.execute(
        text("SELECT tipo_veiculo, valor_hora FROM tarifas")
    ).mappings().all()

    return {
        tarifa["tipo_veiculo"]: float(tarifa["valor_hora"])
        for tarifa in resultado
    }


@app.post("/tarifas/calcular")
def calcular_tarifa(
    dados: CalculoTarifa,
    db: Session = Depends(get_db)
):
    tipo = dados.tipo_veiculo.lower()

    tarifa = db.execute(
        text("""
            SELECT valor_hora
            FROM tarifas
            WHERE tipo_veiculo = :tipo
        """),
        {"tipo": tipo}
    ).scalar_one_or_none()

    if tarifa is None:
        raise HTTPException(
            status_code=400,
            detail="Tipo de veículo inválido"
        )

    horas = (dados.minutos + 59) // 60
    valor_total = horas * tarifa

    return {
        "tipo_veiculo": tipo,
        "minutos": dados.minutos,
        "horas_cobradas": horas,
        "valor_total": float(round(valor_total, 2))
    }