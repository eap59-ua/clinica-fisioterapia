-- ============================================
-- SCRIPT PARA SOLUCIONAR EL ERROR 500
-- ============================================
-- Este script crea el registro de fisioterapeuta para usuarios
-- que tienen rol FISIOTERAPEUTA pero no tienen registro en la tabla fisioterapeuta

-- Paso 1: Ver qué usuarios tienen rol FISIOTERAPEUTA pero no tienen registro en fisioterapeuta
SELECT u.id, u.nombre, u.apellidos, u.email, u.rol
FROM usuario u
WHERE u.rol = 'FISIOTERAPEUTA'
  AND NOT EXISTS (SELECT 1 FROM fisioterapeuta f WHERE f.usuario_id = u.id);

-- Paso 2: Insertar registros faltantes en la tabla fisioterapeuta
INSERT INTO fisioterapeuta (usuario_id, especialidades, numero_colegiado, valoracion_promedio)
SELECT
    u.id,
    'Fisioterapia General',
    'FIS-' || LPAD(u.id::text, 3, '0') || '-TEMP',
    4.0
FROM usuario u
WHERE u.rol = 'FISIOTERAPEUTA'
  AND NOT EXISTS (SELECT 1 FROM fisioterapeuta f WHERE f.usuario_id = u.id);

-- Paso 3: Verificar que se hayan insertado correctamente
SELECT u.id as usuario_id, u.nombre, u.apellidos, u.email, f.especialidades, f.numero_colegiado
FROM usuario u
INNER JOIN fisioterapeuta f ON f.usuario_id = u.id
WHERE u.rol = 'FISIOTERAPEUTA';
