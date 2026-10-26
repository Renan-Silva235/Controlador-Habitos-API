CREATE TABLE ConfiguracoesNotificacoes(
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    usuario_id UUID NOT NULL,
    notificacoes_ativas BOOLEAN NOT NULL,
    lembrete_diario_ativo BOOLEAN NOT NULL,
    horario_lembrete TIME NOT NULL DEFAULT '20:00',
    fuso_horario VARCHAR(50) NOT NULL,
    lembrete_registro_ativo BOOLEAN NOT NULL,
    CONSTRAINT fk_conf_notificacoes FOREIGN KEY (usuario_id) REFERENCES Usuarios(id) ON DELETE CASCADE
);