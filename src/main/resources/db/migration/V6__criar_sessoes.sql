CREATE TABLE sessoes (
     id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
     usuario_id UUID NOT NULL,
     dispositivo_id UUID NOT NULL REFERENCES dispositivos(id) ON DELETE CASCADE,
     refresh_token_hash VARCHAR(255) NOT NULL UNIQUE,
     data_criacao TIMESTAMP NOT NULL,
     data_expiracao TIMESTAMP NOT NULL,
     data_ultimo_acesso TIMESTAMP,
     ativo BOOLEAN NOT NULL,
     CONSTRAINT fk_usuario_sessoes FOREIGN KEY (usuario_id) REFERENCES Usuarios(id) ON DELETE CASCADE
);