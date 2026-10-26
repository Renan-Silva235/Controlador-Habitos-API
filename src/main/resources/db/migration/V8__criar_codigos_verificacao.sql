CREATE TABLE codigos_verificacao (
     id UUID PRIMARY KEY,
     usuario_id UUID NOT NULL REFERENCES usuarios(id) ON DELETE CASCADE,
     codigo_hash VARCHAR(255) NOT NULL,
     tipo VARCHAR(30) NOT NULL,
     data_expiracao TIMESTAMP NOT NULL,
     tentativas INT NOT NULL DEFAULT 0,
     ativo BOOLEAN NOT NULL DEFAULT TRUE,
     data_criacao TIMESTAMP NOT NULL
);