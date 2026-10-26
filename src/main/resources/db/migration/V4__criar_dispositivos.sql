CREATE TABLE Dispositivos(
     id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
     usuario_id UUID NOT NULL,
     nome_dispositivo VARCHAR(100) NOT NULL,
     token_notificacao VARCHAR(255) NOT NULL,
     data_ultimo_acesso TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
     ativo BOOLEAN DEFAULT TRUE,
     CONSTRAINT fk_usuario_dispositivo FOREIGN KEY (usuario_id) REFERENCES Usuarios(id) ON DELETE CASCADE
);

