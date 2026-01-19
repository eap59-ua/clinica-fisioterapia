-- Crear citas de prueba para el fisioterapeuta CACA CACA (usuario_id = 25)

-- Primero verificar si hay clientes disponibles
SELECT id, nombre, apellidos FROM usuario WHERE rol = 'CLIENTE' LIMIT 5;

-- Crear una cita para HOY
INSERT INTO cita (
    cliente_id,
    fisioterapeuta_id,
    servicio_id,
    sala_id,
    fecha,
    hora_inicio,
    hora_fin,
    estado,
    precio_pagado
)
SELECT
    (SELECT MIN(id) FROM usuario WHERE rol = 'CLIENTE') as cliente_id,
    25 as fisioterapeuta_id,
    (SELECT MIN(id) FROM servicio WHERE nombre = 'Masaje Terapéutico') as servicio_id,
    (SELECT MIN(id) FROM sala WHERE nombre = 'Sala 1 - Rehabilitación') as sala_id,
    CURRENT_DATE as fecha,
    '10:00'::time as hora_inicio,
    '11:00'::time as hora_fin,
    'PENDIENTE' as estado,
    45.00 as precio_pagado
WHERE NOT EXISTS (
    -- Evitar duplicados
    SELECT 1 FROM cita
    WHERE fisioterapeuta_id = 25
      AND fecha = CURRENT_DATE
      AND hora_inicio = '10:00'::time
);

-- Crear otra cita para HOY
INSERT INTO cita (
    cliente_id,
    fisioterapeuta_id,
    servicio_id,
    sala_id,
    fecha,
    hora_inicio,
    hora_fin,
    estado,
    precio_pagado
)
SELECT
    (SELECT MAX(id) FROM usuario WHERE rol = 'CLIENTE') as cliente_id,
    25 as fisioterapeuta_id,
    (SELECT MIN(id) FROM servicio WHERE nombre = 'Fisioterapia Traumatológica') as servicio_id,
    (SELECT MIN(id) FROM sala WHERE nombre = 'Sala 2 - Masajes') as sala_id,
    CURRENT_DATE as fecha,
    '15:00'::time as hora_inicio,
    '16:00'::time as hora_fin,
    'CONFIRMADA' as estado,
    55.00 as precio_pagado
WHERE NOT EXISTS (
    -- Evitar duplicados
    SELECT 1 FROM cita
    WHERE fisioterapeuta_id = 25
      AND fecha = CURRENT_DATE
      AND hora_inicio = '15:00'::time
);

-- Crear cita para la semana (mañana)
INSERT INTO cita (
    cliente_id,
    fisioterapeuta_id,
    servicio_id,
    sala_id,
    fecha,
    hora_inicio,
    hora_fin,
    estado,
    precio_pagado
)
SELECT
    (SELECT id FROM usuario WHERE rol = 'CLIENTE' LIMIT 1 OFFSET 1) as cliente_id,
    25 as fisioterapeuta_id,
    (SELECT MIN(id) FROM servicio WHERE nombre = 'Osteopatía') as servicio_id,
    (SELECT MIN(id) FROM sala WHERE nombre = 'Sala 3 - Gimnasio') as sala_id,
    CURRENT_DATE + 1 as fecha,
    '11:00'::time as hora_inicio,
    '12:00'::time as hora_fin,
    'PENDIENTE' as estado,
    60.00 as precio_pagado
WHERE NOT EXISTS (
    SELECT 1 FROM cita
    WHERE fisioterapeuta_id = 25
      AND fecha = CURRENT_DATE + 1
      AND hora_inicio = '11:00'::time
);

-- Verificar las citas creadas
SELECT
    c.id,
    c.fecha,
    c.hora_inicio,
    c.hora_fin,
    c.estado,
    uc.nombre as cliente_nombre,
    s.nombre as servicio_nombre,
    sl.nombre as sala_nombre
FROM cita c
INNER JOIN usuario uc ON c.cliente_id = uc.id
INNER JOIN servicio s ON c.servicio_id = s.id
LEFT JOIN sala sl ON c.sala_id = sl.id
WHERE c.fisioterapeuta_id = 25
ORDER BY c.fecha, c.hora_inicio;
