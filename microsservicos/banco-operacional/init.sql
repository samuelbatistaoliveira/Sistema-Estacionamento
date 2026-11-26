-- Banco Operacional: compartilhado APENAS por reservas-service e movimentacao-service (C#).
-- O esquema mora aqui (e não em migrations de um dos serviços) porque as duas aplicações
-- usam as mesmas tabelas: a Movimentação consome a reserva na entrada do veículo.

CREATE EXTENSION IF NOT EXISTS btree_gist;

CREATE TABLE IF NOT EXISTS reservas (
    id             SERIAL PRIMARY KEY,
    cliente_id     BIGINT        NOT NULL,          -- id no serviço de Cadastro
    veiculo_id     BIGINT        NOT NULL,          -- id no serviço de Cadastro
    vaga_id        BIGINT        NOT NULL,          -- id no serviço de Vagas
    tipo_veiculo   VARCHAR(10)   NOT NULL CHECK (tipo_veiculo IN ('CARRO', 'MOTO')),
    data_reserva   TIMESTAMPTZ   NOT NULL,
    data_inicio    TIMESTAMPTZ   NOT NULL,
    data_fim       TIMESTAMPTZ   NOT NULL,
    valor          NUMERIC(10,2) NOT NULL CHECK (valor >= 0),
    status         VARCHAR(12)   NOT NULL CHECK (status IN ('PENDENTE', 'CONFIRMADA', 'CANCELADA', 'CONCLUIDA')),
    pagamento_id   VARCHAR(64),
    CHECK (data_fim > data_inicio),
    -- duas reservas ativas nunca se sobrepõem na mesma vaga (protege contra corrida entre requisições)
    CONSTRAINT ex_reserva_vaga_periodo EXCLUDE USING gist (
        vaga_id WITH =,
        tstzrange(data_inicio, data_fim) WITH &&
    ) WHERE (status IN ('PENDENTE', 'CONFIRMADA'))
);

CREATE INDEX IF NOT EXISTS ix_reservas_veiculo_status ON reservas (veiculo_id, status);

CREATE TABLE IF NOT EXISTS movimentacoes (
    id                 SERIAL PRIMARY KEY,
    veiculo_id         BIGINT        NOT NULL,
    placa              VARCHAR(10)   NOT NULL,
    tipo_veiculo       VARCHAR(10)   NOT NULL CHECK (tipo_veiculo IN ('CARRO', 'MOTO')),
    vaga_id            BIGINT        NOT NULL,
    reserva_id         INT           REFERENCES reservas (id),
    data_hora_entrada  TIMESTAMPTZ   NOT NULL,
    data_hora_saida    TIMESTAMPTZ,
    minutos_cobrados   INT,
    valor              NUMERIC(10,2) CHECK (valor >= 0),
    pagamento_id       VARCHAR(64),
    status             VARCHAR(10)   NOT NULL CHECK (status IN ('ABERTA', 'FINALIZADA'))
);

-- um veículo só pode ter uma movimentação aberta por vez
CREATE UNIQUE INDEX IF NOT EXISTS ux_movimentacao_veiculo_aberta
    ON movimentacoes (veiculo_id) WHERE status = 'ABERTA';
CREATE INDEX IF NOT EXISTS ix_movimentacoes_placa_status ON movimentacoes (placa, status);
