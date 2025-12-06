-- ============================================
-- SCHEMA: CLÍNICA DE FISIOTERAPIA
-- Versión: 1.0 - PostgreSQL 9.6 Compatible
-- Fecha: Diciembre 2025
-- ============================================

-- Eliminar tablas si existen
DROP TABLE IF EXISTS nota_sesion CASCADE;
DROP TABLE IF EXISTS pago CASCADE;
DROP TABLE IF EXISTS producto CASCADE;
DROP TABLE IF EXISTS cita CASCADE;
DROP TABLE IF EXISTS bloqueo_horario CASCADE;
DROP TABLE IF EXISTS horario_clinica CASCADE;
DROP TABLE IF EXISTS servicio CASCADE;
DROP TABLE IF EXISTS sala CASCADE;
DROP TABLE IF EXISTS fisioterapeuta CASCADE;
DROP TABLE IF EXISTS cliente CASCADE;
DROP TABLE IF EXISTS administrador CASCADE;
DROP TABLE IF EXISTS recepcionista CASCADE;
DROP TABLE IF EXISTS usuario CASCADE;

-- Eliminar tipos si existen
DROP TYPE IF EXISTS rol_usuario CASCADE;
DROP TYPE IF EXISTS estado_cita CASCADE;
DROP TYPE IF EXISTS tipo_bloqueo CASCADE;
DROP TYPE IF EXISTS metodo_pago CASCADE;
DROP TYPE IF EXISTS estado_pago CASCADE;

-- Tipos ENUM
CREATE TYPE rol_usuario AS ENUM ('ADMIN', 'RECEPCIONISTA', 'FISIOTERAPEUTA', 'CLIENTE');
CREATE TYPE estado_cita AS ENUM ('PENDIENTE', 'COMPLETADA', 'CANCELADA', 'NO_ASISTIO');
CREATE TYPE tipo_bloqueo AS ENUM ('PERSONAL', 'GLOBAL');
CREATE TYPE metodo_pago AS ENUM ('TARJETA', 'PAYPAL', 'TRANSFERENCIA', 'EFECTIVO');
CREATE TYPE estado_pago AS ENUM ('PENDIENTE', 'COMPLETADO', 'FALLIDO', 'REEMBOLSADO');

-- ============================================
-- TABLA PRINCIPAL: USUARIO
-- ============================================
CREATE TABLE usuario (
    id SERIAL PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    apellidos VARCHAR(100) NOT NULL,
    dni VARCHAR(9) UNIQUE NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    telefono VARCHAR(15),
    password VARCHAR(255) NOT NULL,
    rol rol_usuario NOT NULL,
    activo BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Validación DNI (PostgreSQL 9.6)
ALTER TABLE usuario ADD CONSTRAINT chk_dni 
CHECK (dni ~ '^[0-9]{8}[A-Z]$');

-- Validación Email (PostgreSQL 9.6)
ALTER TABLE usuario ADD CONSTRAINT chk_email 
CHECK (email ~* '^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\.[A-Za-z]{2,}$');

-- ============================================
-- TABLAS HIJAS: PERFILES ESPECÍFICOS
-- ============================================

CREATE TABLE administrador (
    usuario_id INT PRIMARY KEY REFERENCES usuario(id) ON DELETE CASCADE,
    nivel_acceso INT DEFAULT 1,
    departamento VARCHAR(50)
);

CREATE TABLE recepcionista (
    usuario_id INT PRIMARY KEY REFERENCES usuario(id) ON DELETE CASCADE,
    turno VARCHAR(20),
    fecha_contratacion DATE DEFAULT CURRENT_DATE
);

CREATE TABLE fisioterapeuta (
    usuario_id INT PRIMARY KEY REFERENCES usuario(id) ON DELETE CASCADE,
    especialidades VARCHAR(255),
    foto_url VARCHAR(255),
    biografia TEXT,
    numero_colegiado VARCHAR(20) UNIQUE,
    valoracion_promedio DECIMAL(3,2) DEFAULT 0.00
);

-- Validación valoración
ALTER TABLE fisioterapeuta ADD CONSTRAINT chk_valoracion 
CHECK (valoracion_promedio >= 0 AND valoracion_promedio <= 5);

CREATE TABLE cliente (
    usuario_id INT PRIMARY KEY REFERENCES usuario(id) ON DELETE CASCADE,
    direccion VARCHAR(255),
    fecha_nacimiento DATE
);

-- Validación mayor de edad (PostgreSQL 9.6 compatible)
ALTER TABLE cliente ADD CONSTRAINT chk_mayor_edad 
CHECK (fecha_nacimiento <= CURRENT_DATE - INTERVAL '18 years');

-- ============================================
-- INSTALACIONES Y SERVICIOS
-- ============================================

CREATE TABLE sala (
    id SERIAL PRIMARY KEY,
    nombre VARCHAR(50) NOT NULL UNIQUE,
    capacidad INT DEFAULT 1,
    equipamiento TEXT,
    activa BOOLEAN DEFAULT TRUE
);

ALTER TABLE sala ADD CONSTRAINT chk_capacidad CHECK (capacidad > 0);

CREATE TABLE servicio (
    id SERIAL PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    descripcion TEXT,
    duracion_minutos INT NOT NULL,
    precio DECIMAL(10,2) NOT NULL,
    activo BOOLEAN DEFAULT TRUE,
    imagen_url VARCHAR(255)
);

ALTER TABLE servicio ADD CONSTRAINT chk_duracion CHECK (duracion_minutos > 0);
ALTER TABLE servicio ADD CONSTRAINT chk_precio CHECK (precio >= 0);

-- ============================================
-- HORARIOS Y BLOQUEOS
-- ============================================

CREATE TABLE horario_clinica (
    id SERIAL PRIMARY KEY,
    dia_semana INT NOT NULL,
    hora_apertura TIME NOT NULL,
    hora_cierre TIME NOT NULL,
    CONSTRAINT uq_dia UNIQUE (dia_semana)
);

ALTER TABLE horario_clinica ADD CONSTRAINT chk_dia_semana 
CHECK (dia_semana >= 0 AND dia_semana <= 6);

ALTER TABLE horario_clinica ADD CONSTRAINT chk_horario 
CHECK (hora_apertura < hora_cierre);

CREATE TABLE bloqueo_horario (
    id SERIAL PRIMARY KEY,
    fisioterapeuta_id INT REFERENCES fisioterapeuta(usuario_id) ON DELETE CASCADE,
    fecha_inicio TIMESTAMP NOT NULL,
    fecha_fin TIMESTAMP NOT NULL,
    motivo VARCHAR(255),
    tipo tipo_bloqueo DEFAULT 'PERSONAL'
);

ALTER TABLE bloqueo_horario ADD CONSTRAINT chk_fechas_bloqueo 
CHECK (fecha_inicio < fecha_fin);

-- ============================================
-- SISTEMA DE CITAS
-- ============================================

CREATE TABLE cita (
    id SERIAL PRIMARY KEY,
    cliente_id INT NOT NULL REFERENCES cliente(usuario_id) ON DELETE CASCADE,
    fisioterapeuta_id INT NOT NULL REFERENCES fisioterapeuta(usuario_id) ON DELETE RESTRICT,
    servicio_id INT NOT NULL REFERENCES servicio(id) ON DELETE RESTRICT,
    sala_id INT REFERENCES sala(id) ON DELETE SET NULL,
    fecha DATE NOT NULL,
    hora_inicio TIME NOT NULL,
    hora_fin TIME NOT NULL,
    estado estado_cita DEFAULT 'PENDIENTE',
    notas TEXT,
    precio_pagado DECIMAL(10,2),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

ALTER TABLE cita ADD CONSTRAINT chk_horas_cita CHECK (hora_inicio < hora_fin);
-- ALTER TABLE cita ADD CONSTRAINT chk_fecha_futura CHECK (fecha >= CURRENT_DATE);

CREATE TABLE nota_sesion (
    id SERIAL PRIMARY KEY,
    cita_id INT UNIQUE NOT NULL REFERENCES cita(id) ON DELETE CASCADE,
    fisioterapeuta_id INT NOT NULL REFERENCES fisioterapeuta(usuario_id) ON DELETE CASCADE,
    contenido TEXT NOT NULL,
    privada BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- ============================================
-- SISTEMA DE PAGOS
-- ============================================

CREATE TABLE pago (
    id SERIAL PRIMARY KEY,
    cita_id INT REFERENCES cita(id) ON DELETE SET NULL,
    monto DECIMAL(10,2) NOT NULL,
    metodo metodo_pago NOT NULL,
    estado estado_pago DEFAULT 'PENDIENTE',
    transaccion_id VARCHAR(100) UNIQUE,
    descripcion VARCHAR(255),
    fecha_pago TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

ALTER TABLE pago ADD CONSTRAINT chk_monto CHECK (monto > 0);

-- ============================================
-- TIENDA ONLINE
-- ============================================

CREATE TABLE producto (
    id SERIAL PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    descripcion TEXT,
    precio DECIMAL(10,2) NOT NULL,
    stock INT DEFAULT 0,
    imagen_url VARCHAR(255),
    categoria VARCHAR(50),
    activo BOOLEAN DEFAULT TRUE
);

ALTER TABLE producto ADD CONSTRAINT chk_precio_producto CHECK (precio >= 0);
ALTER TABLE producto ADD CONSTRAINT chk_stock CHECK (stock >= 0);

-- ============================================
-- ÍNDICES
-- ============================================

CREATE INDEX idx_cita_fecha ON cita(fecha);
CREATE INDEX idx_cita_fisio ON cita(fisioterapeuta_id);
CREATE INDEX idx_cita_cliente ON cita(cliente_id);
CREATE INDEX idx_cita_estado ON cita(estado);
CREATE INDEX idx_usuario_email ON usuario(email);
CREATE INDEX idx_usuario_dni ON usuario(dni);
CREATE INDEX idx_usuario_rol ON usuario(rol);
CREATE INDEX idx_bloqueo_fechas ON bloqueo_horario(fecha_inicio, fecha_fin);
CREATE INDEX idx_bloqueo_fisio ON bloqueo_horario(fisioterapeuta_id);

-- ============================================
-- TRIGGERS (PostgreSQL 9.6)
-- ============================================

-- Función para actualizar timestamp
CREATE OR REPLACE FUNCTION update_timestamp()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- Triggers
CREATE TRIGGER trg_usuario_updated
BEFORE UPDATE ON usuario
FOR EACH ROW EXECUTE PROCEDURE update_timestamp();

CREATE TRIGGER trg_cita_updated
BEFORE UPDATE ON cita
FOR EACH ROW EXECUTE PROCEDURE update_timestamp();

-- ============================================
-- VISTAS
-- ============================================

CREATE OR REPLACE VIEW v_citas_pendientes AS
SELECT 
    c.id,
    c.fecha,
    c.hora_inicio,
    c.hora_fin,
    u_cli.nombre || ' ' || u_cli.apellidos AS cliente,
    u_fis.nombre || ' ' || u_fis.apellidos AS fisioterapeuta,
    s.nombre AS servicio,
    sa.nombre AS sala
FROM cita c
INNER JOIN cliente cl ON c.cliente_id = cl.usuario_id
INNER JOIN usuario u_cli ON cl.usuario_id = u_cli.id
INNER JOIN fisioterapeuta f ON c.fisioterapeuta_id = f.usuario_id
INNER JOIN usuario u_fis ON f.usuario_id = u_fis.id
INNER JOIN servicio s ON c.servicio_id = s.id
LEFT JOIN sala sa ON c.sala_id = sa.id
WHERE c.estado = 'PENDIENTE'
ORDER BY c.fecha, c.hora_inicio;

-- ============================================
-- FIN SCHEMA
-- ============================================