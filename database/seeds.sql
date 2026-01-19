-- ============================================
-- SEEDS: DATOS DE PRUEBA - PostgreSQL 9.6
-- Compatible con BIGINT (BIGSERIAL)
-- ============================================

BEGIN;

-- Deshabilitar triggers temporalmente para datos históricos
ALTER TABLE cita DISABLE TRIGGER ALL;

-- ============================================
-- HORARIO DE LA CLÍNICA
-- ============================================
INSERT INTO horario_clinica (dia_semana, hora_apertura, hora_cierre) VALUES
(1, '09:00', '20:00'),
(2, '09:00', '20:00'),
(3, '09:00', '20:00'),
(4, '09:00', '20:00'),
(5, '09:00', '20:00'),

(6, '09:00', '14:00');

-- ============================================
-- SALAS
-- ============================================
INSERT INTO sala (nombre, capacidad, equipamiento, activa) VALUES
('Sala 1 - Rehabilitación', 1, 'Camilla eléctrica, ultrasonidos, electroterapia', true),
('Sala 2 - Masajes', 1, 'Camilla, aceites esenciales, música ambiental', true),
('Sala 3 - Gimnasio', 5, 'Esterillas, mancuernas, bandas elásticas, pelotas pilates', true);

-- ============================================
-- SERVICIOS
-- ============================================
INSERT INTO servicio (nombre, descripcion, duracion_minutos, precio, activo) VALUES
('Masaje Terapéutico', 'Masaje para alivio de tensiones musculares y estrés', 60, 45.00, true),
('Rehabilitación Deportiva', 'Tratamiento para lesiones deportivas con ejercicios específicos', 45, 50.00, true),
('Fisioterapia Traumatológica', 'Tratamiento post-operatorio y recuperación de fracturas', 60, 55.00, true),
('Drenaje Linfático', 'Técnica para eliminar retención de líquidos', 45, 40.00, true),
('Punción Seca', 'Tratamiento de puntos gatillo miofasciales', 30, 35.00, true),
('Osteopatía', 'Tratamiento global del sistema musculoesquelético', 60, 60.00, true);

-- ============================================
-- USUARIOS
-- ============================================

-- ADMINISTRADOR
INSERT INTO usuario (nombre, apellidos, dni, email, telefono, password, rol, activo) VALUES
('Carlos', 'Administrador López', '12345678A', 'admin@clinica.com', '666111222',
 '$2a$10$XQz9Z8XO/KVhT9xJ0wQ5/.YmGvF7YZ4Z9Q0J5wQ5XQz9Z8XO', 'ADMIN', true);

INSERT INTO administrador (usuario_id, nivel_acceso, departamento)
SELECT u.id, 3, 'Dirección General' 
FROM usuario u WHERE u.email = 'admin@clinica.com';

-- RECEPCIONISTAS
INSERT INTO usuario (nombre, apellidos, dni, email, telefono, password, rol, activo) VALUES
('María', 'García Sánchez', '23456789B', 'recepcion@clinica.com', '666222333',
 '$2a$10$XQz9Z8XO/KVhT9xJ0wQ5/.YmGvF7YZ4Z9Q0J5wQ5XQz9Z8XO', 'RECEPCIONISTA', true),
('Laura', 'Martínez Pérez', '34567890C', 'laura.recepcion@clinica.com', '666333444',
 '$2a$10$XQz9Z8XO/KVhT9xJ0wQ5/.YmGvF7YZ4Z9Q0J5wQ5XQz9Z8XO', 'RECEPCIONISTA', true);

INSERT INTO recepcionista (usuario_id, turno, fecha_contratacion)
SELECT u.id, 'MAÑANA', '2023-01-15'::date
FROM usuario u WHERE u.email = 'recepcion@clinica.com'
UNION ALL
SELECT u.id, 'TARDE', '2023-03-20'::date
FROM usuario u WHERE u.email = 'laura.recepcion@clinica.com';

-- FISIOTERAPEUTAS
INSERT INTO usuario (nombre, apellidos, dni, email, telefono, password, rol, activo) VALUES
('Dr. Antonio', 'Ruiz Fernández', '45678901D', 'antonio.ruiz@clinica.com', '666444555',
 '$2a$10$XQz9Z8XO/KVhT9xJ0wQ5/.YmGvF7YZ4Z9Q0J5wQ5XQz9Z8XO', 'FISIOTERAPEUTA', true),
('Dra. Elena', 'Jiménez Torres', '56789012E', 'elena.jimenez@clinica.com', '666555666',
 '$2a$10$XQz9Z8XO/KVhT9xJ0wQ5/.YmGvF7YZ4Z9Q0J5wQ5XQz9Z8XO', 'FISIOTERAPEUTA', true),
('Dr. Miguel', 'Hernández Gómez', '67890123F', 'miguel.hernandez@clinica.com', '666666777',
 '$2a$10$XQz9Z8XO/KVhT9xJ0wQ5/.YmGvF7YZ4Z9Q0J5wQ5XQz9Z8XO', 'FISIOTERAPEUTA', true),
('Dra. Carmen', 'López Díaz', '78901234G', 'carmen.lopez@clinica.com', '666777888',
 '$2a$10$XQz9Z8XO/KVhT9xJ0wQ5/.YmGvF7YZ4Z9Q0J5wQ5XQz9Z8XO', 'FISIOTERAPEUTA', true),
('Dr. Javier', 'Moreno Silva', '89012345H', 'javier.moreno@clinica.com', '666888999',
 '$2a$10$XQz9Z8XO/KVhT9xJ0wQ5/.YmGvF7YZ4Z9Q0J5wQ5XQz9Z8XO', 'FISIOTERAPEUTA', true);

INSERT INTO fisioterapeuta (usuario_id, especialidades, foto_url, biografia, numero_colegiado, valoracion_promedio)
SELECT u.id, 'Traumatología, Deportiva', '/assets/fisios/antonio.jpg', '15 años de experiencia...', 'FIS-001-AL', 4.8
FROM usuario u WHERE u.email = 'antonio.ruiz@clinica.com'
UNION ALL
SELECT u.id, 'Osteopatía, Drenaje Linfático', '/assets/fisios/elena.jpg', 'Experta en técnicas osteopáticas...', 'FIS-002-AL', 4.9
FROM usuario u WHERE u.email = 'elena.jimenez@clinica.com'
UNION ALL
SELECT u.id, 'Rehabilitación, Geriatría', '/assets/fisios/miguel.jpg', 'Especialista en rehabilitación post-operatoria...', 'FIS-003-AL', 4.7
FROM usuario u WHERE u.email = 'miguel.hernandez@clinica.com'
UNION ALL
SELECT u.id, 'Suelo Pélvico, Embarazo', '/assets/fisios/carmen.jpg', 'Fisioterapeuta especializada en suelo pélvico...', 'FIS-004-AL', 5.0
FROM usuario u WHERE u.email = 'carmen.lopez@clinica.com'
UNION ALL
SELECT u.id, 'Punción Seca, ATM', '/assets/fisios/javier.jpg', 'Experto en terapias invasivas...', 'FIS-005-AL', 4.6
FROM usuario u WHERE u.email = 'javier.moreno@clinica.com';

-- CLIENTES
INSERT INTO usuario (nombre, apellidos, dni, email, telefono, password, rol, activo) VALUES
('Juan', 'Pérez Gómez', '11111111A', 'juan.perez@gmail.com', '611111111', '$2a$10$XQz9Z8XO/KVhT9xJ0wQ5/.YmGvF7YZ4Z9Q0J5wQ5XQz9Z8XO','CLIENTE', true),
('Ana', 'Rodríguez López', '22222222B', 'ana.rodriguez@gmail.com', '622222222', '$2a$10$XQz9Z8XO/KVhT9xJ0wQ5/.YmGvF7YZ4Z9Q0J5wQ5XQz9Z8XO','CLIENTE', true),
('Pedro', 'Sánchez Martín', '33333333C', 'pedro.sanchez@hotmail.com', '633333333', '$2a$10$XQz9Z8XO/KVhT9xJ0wQ5/.YmGvF7YZ4Z9Q0J5wQ5XQz9Z8XO','CLIENTE', true),
('Isabel', 'García Fernández', '44444444D', 'isabel.garcia@outlook.com', '644444444', '$2a$10$XQz9Z8XO/KVhT9xJ0wQ5/.YmGvF7YZ4Z9Q0J5wQ5XQz9Z8XO','CLIENTE', true),
('Luis', 'Martínez Ruiz', '55555555E', 'luis.martinez@yahoo.es', '655555555', '$2a$10$XQz9Z8XO/KVhT9xJ0wQ5/.YmGvF7YZ4Z9Q0J5wQ5XQz9Z8XO','CLIENTE', true),
('Sofía', 'Hernández Torres', '66666666F', 'sofia.hernandez@gmail.com', '666666666', '$2a$10$XQz9Z8XO/KVhT9xJ0wQ5/.YmGvF7YZ4Z9Q0J5wQ5XQz9Z8XO','CLIENTE', true),
('David', 'López Jiménez', '77777777G', 'david.lopez@gmail.com', '677777777', '$2a$10$XQz9Z8XO/KVhT9xJ0wQ5/.YmGvF7YZ4Z9Q0J5wQ5XQz9Z8XO','CLIENTE', true),
('Marta', 'Díaz Moreno', '88888888H', 'marta.diaz@hotmail.com', '688888888', '$2a$10$XQz9Z8XO/KVhT9xJ0wQ5/.YmGvF7YZ4Z9Q0J5wQ5XQz9Z8XO','CLIENTE', true),
('Raúl', 'Torres Silva', '99999999I', 'raul.torres@gmail.com', '699999999', '$2a$10$XQz9Z8XO/KVhT9xJ0wQ5/.YmGvF7YZ4Z9Q0J5wQ5XQz9Z8XO','CLIENTE', true),
('Eva', 'Romero Castro', '00000000J', 'eva.romero@outlook.com', '600000000', '$2a$10$XQz9Z8XO/KVhT9xJ0wQ5/.YmGvF7YZ4Z9Q0J5wQ5XQz9Z8XO','CLIENTE', true);

INSERT INTO cliente (usuario_id, direccion, fecha_nacimiento)
SELECT u.id, 'Calle Mayor, 15, Alicante', '1985-03-15'::date FROM usuario u WHERE u.email = 'juan.perez@gmail.com'
UNION ALL
SELECT u.id, 'Av. Maisonnave, 22, Alicante', '1990-07-22'::date FROM usuario u WHERE u.email = 'ana.rodriguez@gmail.com'
UNION ALL
SELECT u.id, 'Calle San Fernando, 8, Alicante', '1978-11-30'::date FROM usuario u WHERE u.email = 'pedro.sanchez@hotmail.com'
UNION ALL
SELECT u.id, 'Rambla Méndez Núñez, 45, Alicante', '1995-02-14'::date FROM usuario u WHERE u.email = 'isabel.garcia@outlook.com'
UNION ALL
SELECT u.id, 'Calle Castaños, 12, Alicante', '1982-09-05'::date FROM usuario u WHERE u.email = 'luis.martinez@yahoo.es'
UNION ALL
SELECT u.id, 'Av. Oscar Esplá, 33, Alicante', '1998-06-18'::date FROM usuario u WHERE u.email = 'sofia.hernandez@gmail.com'
UNION ALL
SELECT u.id, 'Calle Gerona, 7, Alicante', '1987-12-25'::date FROM usuario u WHERE u.email = 'david.lopez@gmail.com'
UNION ALL
SELECT u.id, 'Paseo Marítimo, 10, Alicante', '1992-04-08'::date FROM usuario u WHERE u.email = 'marta.diaz@hotmail.com'
UNION ALL
SELECT u.id, 'Calle Italia, 19, Alicante', '1975-10-12'::date FROM usuario u WHERE u.email = 'raul.torres@gmail.com'
UNION ALL
SELECT u.id, 'Av. Denia, 27, Alicante', '2000-01-20'::date FROM usuario u WHERE u.email = 'eva.romero@outlook.com';

-- ============================================
-- CITAS DE EJEMPLO
-- ============================================
INSERT INTO cita (cliente_id, fisioterapeuta_id, servicio_id, sala_id, fecha, hora_inicio, hora_fin, estado, precio_pagado)
SELECT 
    cl.usuario_id, f.usuario_id, s.id, sl.id,
    CURRENT_DATE - 7, '10:00'::time, '11:00'::time, 'COMPLETADA', 45.00
FROM usuario uc 
JOIN cliente cl ON cl.usuario_id = uc.id
CROSS JOIN (SELECT ft.usuario_id FROM usuario u JOIN fisioterapeuta ft ON ft.usuario_id = u.id WHERE u.email = 'antonio.ruiz@clinica.com') f
CROSS JOIN (SELECT id FROM servicio WHERE nombre = 'Masaje Terapéutico') s
CROSS JOIN (SELECT id FROM sala WHERE nombre = 'Sala 2 - Masajes') sl
WHERE uc.email = 'juan.perez@gmail.com'

UNION ALL

SELECT 
    cl.usuario_id, f.usuario_id, s.id, sl.id,
    CURRENT_DATE - 5, '12:00'::time, '13:00'::time, 'COMPLETADA', 55.00
FROM usuario uc 
JOIN cliente cl ON cl.usuario_id = uc.id
CROSS JOIN (SELECT ft.usuario_id FROM usuario u JOIN fisioterapeuta ft ON ft.usuario_id = u.id WHERE u.email = 'elena.jimenez@clinica.com') f
CROSS JOIN (SELECT id FROM servicio WHERE nombre = 'Fisioterapia Traumatológica') s
CROSS JOIN (SELECT id FROM sala WHERE nombre = 'Sala 1 - Rehabilitación') sl
WHERE uc.email = 'ana.rodriguez@gmail.com'

UNION ALL

SELECT 
    cl.usuario_id, f.usuario_id, s.id, sl.id,
    CURRENT_DATE - 3, '16:00'::time, '16:45'::time, 'COMPLETADA', 50.00
FROM usuario uc 
JOIN cliente cl ON cl.usuario_id = uc.id
CROSS JOIN (SELECT ft.usuario_id FROM usuario u JOIN fisioterapeuta ft ON ft.usuario_id = u.id WHERE u.email = 'miguel.hernandez@clinica.com') f
CROSS JOIN (SELECT id FROM servicio WHERE nombre = 'Rehabilitación Deportiva') s
CROSS JOIN (SELECT id FROM sala WHERE nombre = 'Sala 3 - Gimnasio') sl
WHERE uc.email = 'pedro.sanchez@hotmail.com'

UNION ALL

SELECT 
    cl.usuario_id, f.usuario_id, s.id, sl.id,
    CURRENT_DATE + 2, '09:00'::time, '09:45'::time, 'PENDIENTE', 50.00
FROM usuario uc 
JOIN cliente cl ON cl.usuario_id = uc.id
CROSS JOIN (SELECT ft.usuario_id FROM usuario u JOIN fisioterapeuta ft ON ft.usuario_id = u.id WHERE u.email = 'antonio.ruiz@clinica.com') f
CROSS JOIN (SELECT id FROM servicio WHERE nombre = 'Rehabilitación Deportiva') s
CROSS JOIN (SELECT id FROM sala WHERE nombre = 'Sala 1 - Rehabilitación') sl
WHERE uc.email = 'juan.perez@gmail.com'

UNION ALL

SELECT 
    cl.usuario_id, f.usuario_id, s.id, sl.id,
    CURRENT_DATE + 2, '11:00'::time, '12:00'::time, 'PENDIENTE', 60.00
FROM usuario uc 
JOIN cliente cl ON cl.usuario_id = uc.id
CROSS JOIN (SELECT ft.usuario_id FROM usuario u JOIN fisioterapeuta ft ON ft.usuario_id = u.id WHERE u.email = 'elena.jimenez@clinica.com') f
CROSS JOIN (SELECT id FROM servicio WHERE nombre = 'Osteopatía') s
CROSS JOIN (SELECT id FROM sala WHERE nombre = 'Sala 2 - Masajes') sl
WHERE uc.email = 'isabel.garcia@outlook.com'

UNION ALL

SELECT 
    cl.usuario_id, f.usuario_id, s.id, sl.id,
    CURRENT_DATE + 3, '10:00'::time, '10:45'::time, 'PENDIENTE', 40.00
FROM usuario uc 
JOIN cliente cl ON cl.usuario_id = uc.id
CROSS JOIN (SELECT ft.usuario_id FROM usuario u JOIN fisioterapeuta ft ON ft.usuario_id = u.id WHERE u.email = 'carmen.lopez@clinica.com') f
CROSS JOIN (SELECT id FROM servicio WHERE nombre = 'Drenaje Linfático') s
CROSS JOIN (SELECT id FROM sala WHERE nombre = 'Sala 2 - Masajes') sl
WHERE uc.email = 'luis.martinez@yahoo.es'

UNION ALL

SELECT 
    cl.usuario_id, f.usuario_id, s.id, sl.id,
    CURRENT_DATE + 3, '15:00'::time, '15:30'::time, 'PENDIENTE', 35.00
FROM usuario uc 
JOIN cliente cl ON cl.usuario_id = uc.id
CROSS JOIN (SELECT ft.usuario_id FROM usuario u JOIN fisioterapeuta ft ON ft.usuario_id = u.id WHERE u.email = 'javier.moreno@clinica.com') f
CROSS JOIN (SELECT id FROM servicio WHERE nombre = 'Punción Seca') s
CROSS JOIN (SELECT id FROM sala WHERE nombre = 'Sala 1 - Rehabilitación') sl
WHERE uc.email = 'sofia.hernandez@gmail.com'

UNION ALL

SELECT 
    cl.usuario_id, f.usuario_id, s.id, sl.id,
    CURRENT_DATE + 5, '16:00'::time, '17:00'::time, 'PENDIENTE', 45.00
FROM usuario uc 
JOIN cliente cl ON cl.usuario_id = uc.id
CROSS JOIN (SELECT ft.usuario_id FROM usuario u JOIN fisioterapeuta ft ON ft.usuario_id = u.id WHERE u.email = 'antonio.ruiz@clinica.com') f
CROSS JOIN (SELECT id FROM servicio WHERE nombre = 'Masaje Terapéutico') s
CROSS JOIN (SELECT id FROM sala WHERE nombre = 'Sala 2 - Masajes') sl
WHERE uc.email = 'david.lopez@gmail.com'

UNION ALL

SELECT 
    cl.usuario_id, f.usuario_id, s.id, sl.id,
    CURRENT_DATE + 7, '09:30'::time, '10:30'::time, 'PENDIENTE', 55.00
FROM usuario uc 
JOIN cliente cl ON cl.usuario_id = uc.id
CROSS JOIN (SELECT ft.usuario_id FROM usuario u JOIN fisioterapeuta ft ON ft.usuario_id = u.id WHERE u.email = 'miguel.hernandez@clinica.com') f
CROSS JOIN (SELECT id FROM servicio WHERE nombre = 'Fisioterapia Traumatológica') s
CROSS JOIN (SELECT id FROM sala WHERE nombre = 'Sala 1 - Rehabilitación') sl
WHERE uc.email = 'marta.diaz@hotmail.com'

UNION ALL

SELECT 
    cl.usuario_id, f.usuario_id, s.id, sl.id,
    CURRENT_DATE + 7, '12:00'::time, '13:00'::time, 'PENDIENTE', 60.00
FROM usuario uc 
JOIN cliente cl ON cl.usuario_id = uc.id
CROSS JOIN (SELECT ft.usuario_id FROM usuario u JOIN fisioterapeuta ft ON ft.usuario_id = u.id WHERE u.email = 'elena.jimenez@clinica.com') f
CROSS JOIN (SELECT id FROM servicio WHERE nombre = 'Osteopatía') s
CROSS JOIN (SELECT id FROM sala WHERE nombre = 'Sala 2 - Masajes') sl
WHERE uc.email = 'raul.torres@gmail.com';

-- ============================================
-- PRODUCTOS
-- ============================================
INSERT INTO producto (nombre, descripcion, precio, stock, imagen_url, categoria, activo) VALUES
('Banda Elástica Resistencia Media', 'Banda elástica para ejercicios de rehabilitación', 12.99, 50, '/assets/productos/banda.jpg', 'Material Deportivo', true),
('Crema Analgésica 100ml', 'Crema con efecto calor para dolores musculares', 8.50, 30, '/assets/productos/crema.jpg', 'Cuidado Personal', true),
('Pelota Pilates 65cm', 'Pelota de ejercicios para fortalecer core', 19.99, 20, '/assets/productos/pelota.jpg', 'Material Deportivo', true),
('Electroestimulador TENS', 'Dispositivo de electroestimulación portátil', 45.00, 10, '/assets/productos/tens.jpg', 'Tecnología', true),
('Rodillo Foam Roller', 'Rodillo de espuma para automasaje', 15.99, 25, '/assets/productos/roller.jpg', 'Material Deportivo', true);

-- ============================================
-- BLOQUEOS DE HORARIO
-- ============================================
INSERT INTO bloqueo_horario (fisioterapeuta_id, fecha_inicio, fecha_fin, motivo, tipo)
SELECT f.usuario_id, 
       CURRENT_TIMESTAMP + INTERVAL '14 days', 
       CURRENT_TIMESTAMP + INTERVAL '21 days', 
       'Vacaciones de verano', 
       'PERSONAL'
FROM usuario u
JOIN fisioterapeuta f ON f.usuario_id = u.id
WHERE u.email = 'antonio.ruiz@clinica.com';

INSERT INTO bloqueo_horario (fisioterapeuta_id, fecha_inicio, fecha_fin, motivo, tipo) VALUES
(NULL, 
 CURRENT_TIMESTAMP + INTERVAL '30 days', 
 CURRENT_TIMESTAMP + INTERVAL '31 days',
 'Día festivo nacional', 
 'GLOBAL');

-- Rehabilitar triggers
ALTER TABLE cita ENABLE TRIGGER ALL;

COMMIT;

-- ============================================
-- VERIFICACIÓN DE DATOS
-- ============================================
SELECT 'Usuarios creados: ' || COUNT(*) as resumen FROM usuario;
SELECT 'Fisioterapeutas: ' || COUNT(*) as resumen FROM fisioterapeuta;
SELECT 'Clientes: ' || COUNT(*) as resumen FROM cliente;
SELECT 'Salas: ' || COUNT(*) as resumen FROM sala;
SELECT 'Servicios: ' || COUNT(*) as resumen FROM servicio;
SELECT 'Citas: ' || COUNT(*) as resumen FROM cita;
SELECT 'Productos: ' || COUNT(*) as resumen FROM producto;
SELECT 'Bloqueos: ' || COUNT(*) as resumen FROM bloqueo_horario;
