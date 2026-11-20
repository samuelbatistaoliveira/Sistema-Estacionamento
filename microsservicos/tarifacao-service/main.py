
from fastapi import FastAPI, HTTPException
from pydantic import BaseModel, Field

app = FastAPI(title="Microsserviço de Tarifação")

# Tarifas provisórias por hora iniciada
TARIFAS = {
    "carro": 5.00,
    "moto": 3.00
}


class CalculoTarifa(BaseModel):
    tipo_veiculo: str
    minutos: int = Field(gt=0)


@app.get("/")
def inicio():
    return {"mensagem": "Microsserviço de Tarifação funcionando!"}


@app.get("/tarifas")
def listar_tarifas():
    return TARIFAS


@app.post("/tarifas/calcular")
def calcular_tarifa(dados: CalculoTarifa):
    tipo = dados.tipo_veiculo.lower()

    if tipo not in TARIFAS:
        raise HTTPException(
            status_code=400,
            detail="Tipo de veículo inválido"
        )

    horas = (dados.minutos + 59) // 60
    valor_total = horas * TARIFAS[tipo]

    return {
        "tipo_veiculo": tipo,
        "minutos": dados.minutos,
        "horas_cobradas": horas,
        "valor_total": round(valor_total, 2)
    }
