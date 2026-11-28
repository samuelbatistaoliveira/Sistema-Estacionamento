CREATE DATABASE IF NOT EXISTS estacionamento_pagamentos
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

USE estacionamento_pagamentos;

CREATE TABLE IF NOT EXISTS pagamentos (
    id INT AUTO_INCREMENT PRIMARY KEY,
    movimentacao_id INT NOT NULL UNIQUE,
    valor DECIMAL(10,2) NOT NULL CHECK (valor > 0),
    forma_pagamento VARCHAR(30) NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'pendente',
    data_pagamento DATETIME DEFAULT NULL
);