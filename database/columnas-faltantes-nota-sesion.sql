-- Agregar columnas faltantes a la tabla nota_sesion
ALTER TABLE nota_sesion
    ADD COLUMN IF NOT EXISTS diagnostico VARCHAR(255),
    ADD COLUMN IF NOT EXISTS tratamiento_aplicado TEXT,
    ADD COLUMN IF NOT EXISTS recomendaciones TEXT;


