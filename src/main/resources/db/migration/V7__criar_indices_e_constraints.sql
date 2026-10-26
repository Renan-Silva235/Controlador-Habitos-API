ALTER TABLE RegistrosDiarios
    ADD CONSTRAINT uq_registros_habito_data UNIQUE (habito_id, data);

ALTER TABLE RegistrosDiarios
    ADD CONSTRAINT ck_registros_status
        CHECK (status IN ('TRANQUILO', 'DIFICULDADE', 'RECAIDA'));

CREATE INDEX idx_habitos_usuario_arquivado ON habitos (usuario_id, arquivado);
CREATE INDEX idx_dispositivos_usuario      ON dispositivos (usuario_id);
CREATE INDEX idx_sessoes_usuario_ativo     ON sessoes (usuario_id, ativo);
CREATE INDEX idx_sessoes_dispositivo       ON sessoes (dispositivo_id);