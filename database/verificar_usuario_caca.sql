-- Verificar el usuario CACA CACA y sus datos

-- 1. Ver el usuario completo
SELECT * FROM usuario WHERE id = 25;

-- 2. Ver el registro de fisioterapeuta
SELECT * FROM fisioterapeuta WHERE usuario_id = 25;

-- 3. Ver las citas del fisioterapeuta CACA CACA
SELECT
    c.id, c.fecha, c.hora_inicio, c.hora_fin, c.estado,
    uc.nombre as cliente_nombre,
    uf.nombre as fisio_nombre,
    s.nombre as servicio_nombre
FROM cita c
INNER JOIN usuario uc ON c.cliente_id = uc.id
INNER JOIN usuario uf ON c.fisioterapeuta_id = uf.id
INNER JOIN servicio s ON c.servicio_id = s.id
WHERE c.fisioterapeuta_id = 25
ORDER BY c.fecha DESC, c.hora_inicio DESC
LIMIT 10;

-- 4. Ver citas de hoy
SELECT
    c.id, c.fecha, c.hora_inicio, c.hora_fin, c.estado,
    uc.nombre as cliente_nombre,
    s.nombre as servicio_nombre
FROM cita c
INNER JOIN usuario uc ON c.cliente_id = uc.id
INNER JOIN servicio s ON c.servicio_id = s.id
WHERE c.fisioterapeuta_id = 25
  AND c.fecha = CURRENT_DATE;
