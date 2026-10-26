CREATE TABLE Habitos(
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    usuario_id UUID NOT NULL,
    nome_habito varchar(250) NOT NULL,
    data_criacao TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    arquivado BOOLEAN DEFAULT FALSE,
    data_arquivado TIMESTAMP,
    data_atualizacao TIMESTAMP,
    CONSTRAINT fk_usuario FOREIGN KEY (usuario_id) REFERENCES Usuarios(id) ON DELETE CASCADE
);