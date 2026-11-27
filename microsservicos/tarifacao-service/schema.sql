CREATE DATABASE IF NOT EXISTS estacionamento_tarifacao
CHARACTER SET utf8mb4
COLLATE utf8mb4_unicode_ci;

USE estacionamento_tarifacao;

CREATE TABLE IF NOT EXISTS tarifas (
    id INT AUTO_INCREMENT PRIMARY KEY,
    tipo_veiculo VARCHAR(30) NOT NULL UNIQUE,
    valor_hora DECIMAL(10,2) NOT NULL CHECK (valor_hora > 0)
);

INSERT IGNORE INTO tarifas (tipo_veiculo, valor_hora)
VALUES
    ('carro', 5.00),
    ('moto', 3.00);