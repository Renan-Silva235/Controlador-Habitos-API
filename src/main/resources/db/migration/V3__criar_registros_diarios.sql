CREATE TABLE RegistrosDiarios(
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    habito_id UUID NOT NULL,
    data DATE NOT NULL UNIQUE,
    status VARCHAR(50) NOT NULL,
    data_atualização TIMESTAMP,
    CONSTRAINT fk_habito FOREIGN KEY (habito_id) REFERENCES Habitos(id) ON DELETE CASCADE
);