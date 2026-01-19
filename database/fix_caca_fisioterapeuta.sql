-- Script para crear el registro de fisioterapeuta para el usuario CACA CACA
-- Ejecutar este script en la base de datos PostgreSQL

-- Verificar si el usuario CACA CACA existe y obtener su ID
DO $$
DECLARE
    usuario_caca_id BIGINT;
BEGIN
    -- Buscar el usuario por nombre o email
    SELECT id INTO usuario_caca_id
    FROM usuario
    WHERE nombre = 'CACA' OR nombre LIKE '%CACA%' OR email LIKE '%caca%'
    LIMIT 1;

    -- Si encontramos el usuario, crear su registro en fisioterapeuta
    IF usuario_caca_id IS NOT NULL THEN
        -- Verificar si ya existe el registro
        IF NOT EXISTS (SELECT 1 FROM fisioterapeuta WHERE usuario_id = usuario_caca_id) THEN
            -- Insertar registro de fisioterapeuta
            INSERT INTO fisioterapeuta (usuario_id, especialidades, numero_colegiado, valoracion_promedio)
            VALUES (
                usuario_caca_id,
                'Fisioterapia General',
                'FIS-999-TEST',
                4.5
            );

            RAISE NOTICE 'Registro de fisioterapeuta creado para usuario ID: %', usuario_caca_id;
        ELSE
            RAISE NOTICE 'El usuario ya tiene registro de fisioterapeuta';
        END IF;
    ELSE
        RAISE NOTICE 'Usuario CACA no encontrado en la base de datos';
    END IF;
END $$;
