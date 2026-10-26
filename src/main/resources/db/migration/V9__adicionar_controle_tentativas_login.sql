ALTER TABLE usuarios
    ADD COLUMN tentativas_login INT NOT NULL DEFAULT 0,
    ADD COLUMN bloqueado_ate TIMESTAMP;