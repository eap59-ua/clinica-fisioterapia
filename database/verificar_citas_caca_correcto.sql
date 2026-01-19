-- Query SQL correcto para PostgreSQL (no JPQL)

-- 1. Ver todas las citas del fisioterapeuta CACA (usuario_id = 25) para HOY
SELECT
    c.id,
    c.fecha,
    c.hora_inicio,
    c.hora_fin,
    c.estado,
    -- Datos del cliente
    uc.id as cliente_id,
    uc.nombre as cliente_nombre,
    uc.apellidos as cliente_apellidos,
    uc.email as cliente_email,
    uc.telefono as cliente_telefono,
    -- Datos del fisioterapeuta
    uf.id as fisio_id,
    uf.nombre as fisio_nombre,
    uf.apellidos as fisio_apellidos,
    f.especialidades,
    -- Datos del servicio
    s.id as servicio_id,
    s.nombre as servicio_nombre,
    s.descripcion as servicio_descripcion,
    s.duracion_minutos,
    s.precio,
    -- Datos de la sala
    sl.id as sala_id,
    sl.nombre as sala_nombre
FROM cita c
INNER JOIN usuario uc ON c.cliente_id = uc.id
INNER JOIN cliente cl ON cl.usuario_id = uc.id
INNER JOIN usuario uf ON c.fisioterapeuta_id = uf.id
INNER JOIN fisioterapeuta f ON f.usuario_id = uf.id
INNER JOIN servicio s ON c.servicio_id = s.id
LEFT JOIN sala sl ON c.sala_id = sl.id
WHERE c.fisioterapeuta_id = 25
  AND c.fecha = CURRENT_DATE
ORDER BY c.hora_inicio ASC;

-- 2. Si no hay citas para hoy, ver todas las citas del fisioterapeuta
SELECT
    c.id,
    c.fecha,
    c.hora_inicio,
    c.estado,
    uc.nombre as cliente_nombre,
    s.nombre as servicio_nombre
FROM cita c
INNER JOIN usuario uc ON c.cliente_id = uc.id
INNER JOIN servicio s ON c.servicio_id = s.id
WHERE c.fisioterapeuta_id = 25
ORDER BY c.fecha DESC, c.hora_inicio DESC
LIMIT 10;

-- 3. Verificar que el fisioterapeuta CACA tiene todos sus datos correctos
SELECT
    u.id,
    u.nombre,
    u.apellidos,
    u.email,
    u.rol,
    u.activo,
    f.especialidades,
    f.numero_colegiado,
    f.valoracion_promedio
FROM usuario u
INNER JOIN fisioterapeuta f ON f.usuario_id = u.id
WHERE u.id = 25;
