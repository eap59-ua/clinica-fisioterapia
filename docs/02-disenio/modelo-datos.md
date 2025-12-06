# Modelo de Datos - Clínica de Fisioterapia

## Diagrama Entidad-Relación

### Entidades Principales

```
┌─────────────────────────────────────────────────────────┐
│                        USUARIO                          │
├─────────────────────────────────────────────────────────┤
│ PK  id: SERIAL                                          │
│     nombre: VARCHAR(100) NOT NULL                       │
│     apellidos: VARCHAR(100) NOT NULL                    │
│ UQ  dni: VARCHAR(9) NOT NULL                           │
│ UQ  email: VARCHAR(100) NOT NULL                       │
│     telefono: VARCHAR(15)                               │
│     password: VARCHAR(255) NOT NULL                     │
│     rol: ENUM('ADMIN','RECEP','FISIO','CLIENTE')       │
│     activo: BOOLEAN DEFAULT TRUE                        │
│     created_at: TIMESTAMP DEFAULT NOW()                 │
└─────────────────────────────────────────────────────────┘
                              │
                              │ HERENCIA (JOINED)
              ┌───────────────┼───────────────┐
              │               │               │
              ▼               ▼               ▼
┌──────────────────┐  ┌──────────────┐  ┌────────────────┐
│ FISIOTERAPEUTA   │  │   CLIENTE    │  │ ADMINISTRADOR  │
├──────────────────┤  ├──────────────┤  ├────────────────┤
│ PK FK usuario_id │  │ PK FK user_id│  │ PK FK user_id  │
│ especialidades   │  │ direccion    │  │ nivel_acceso   │
│ foto_url         │  │ fecha_nac    │  │ departamento   │
│ biografia        │  │              │  │                │
└──────────────────┘  └──────────────┘  └────────────────┘
       │
       │ 1:N
       │
       ▼
┌─────────────────────────────────────────────────────────┐
│                         CITA                            │
├─────────────────────────────────────────────────────────┤
│ PK  id: SERIAL                                          │
│ FK  cliente_id → Cliente                                │
│ FK  fisioterapeuta_id → Fisioterapeuta                  │
│ FK  servicio_id → Servicio                              │
│ FK  sala_id → Sala (NULLABLE)                           │
│     fecha: DATE NOT NULL                                │
│     hora_inicio: TIME NOT NULL                          │
│     hora_fin: TIME NOT NULL                             │
│     estado: ENUM('PENDIENTE','COMPLETADA',              │
│                  'CANCELADA','NO_ASISTIO')              │
│     notas: TEXT                                         │
│     precio_pagado: DECIMAL(10,2)                        │
│     created_at: TIMESTAMP DEFAULT NOW()                 │
└─────────────────────────────────────────────────────────┘
       │ N:1
       ▼
┌─────────────────────────────────────────────────────────┐
│                       SERVICIO                          │
├─────────────────────────────────────────────────────────┤
│ PK  id: SERIAL                                          │
│     nombre: VARCHAR(100) NOT NULL                       │
│     descripcion: TEXT                                   │
│     duracion_minutos: INT NOT NULL                      │
│     precio: DECIMAL(10,2) NOT NULL                      │
│     activo: BOOLEAN DEFAULT TRUE                        │
└─────────────────────────────────────────────────────────┘

┌─────────────────────────────────────────────────────────┐
│                         SALA                            │
├─────────────────────────────────────────────────────────┤
│ PK  id: SERIAL                                          │
│     nombre: VARCHAR(50) NOT NULL                        │
│     capacidad: INT                                      │
│     equipamiento: TEXT                                  │
│     activa: BOOLEAN DEFAULT TRUE                        │
└─────────────────────────────────────────────────────────┘

┌─────────────────────────────────────────────────────────┐
│                   HORARIO_CLINICA                       │
├─────────────────────────────────────────────────────────┤
│ PK  id: SERIAL                                          │
│     dia_semana: INT CHECK(dia_semana BETWEEN 0 AND 6)  │
│     hora_apertura: TIME NOT NULL                        │
│     hora_cierre: TIME NOT NULL                          │
│ UQ  (dia_semana)                                        │
└─────────────────────────────────────────────────────────┘

┌─────────────────────────────────────────────────────────┐
│                  BLOQUEO_HORARIO                        │
├─────────────────────────────────────────────────────────┤
│ PK  id: SERIAL                                          │
│ FK  fisioterapeuta_id → Fisioterapeuta (NULLABLE)       │
│     fecha_inicio: TIMESTAMP NOT NULL                    │
│     fecha_fin: TIMESTAMP NOT NULL                       │
│     motivo: VARCHAR(255)                                │
│     tipo: ENUM('PERSONAL','GLOBAL') DEFAULT 'PERSONAL'  │
└─────────────────────────────────────────────────────────┘

┌─────────────────────────────────────────────────────────┐
│                    NOTA_SESION                          │
├─────────────────────────────────────────────────────────┤
│ PK  id: SERIAL                                          │
│ FK  cita_id → Cita (UNIQUE)                             │
│ FK  fisioterapeuta_id → Fisioterapeuta                  │
│     contenido: TEXT NOT NULL                            │
│     privada: BOOLEAN DEFAULT TRUE                       │
│     created_at: TIMESTAMP DEFAULT NOW()                 │
└─────────────────────────────────────────────────────────┘
```

## Restricciones de Integridad

### Reglas de Negocio

1. **No doble reserva:** Un fisioterapeuta no puede tener 2 citas simultáneas
2. **Horario válido:** Las citas deben estar dentro del horario de la clínica
3. **Respeto bloqueos:** No se pueden reservar horas bloqueadas
4. **Cancelación anticipada:** Cliente solo puede cancelar con mínimo 24h antelación
5. **Email único:** Cada email solo puede tener una cuenta
6. **DNI único:** Cada DNI solo puede estar registrado una vez

### Índices Recomendados

```sql
CREATE INDEX idx_cita_fecha ON cita(fecha);
CREATE INDEX idx_cita_fisio ON cita(fisioterapeuta_id);
CREATE INDEX idx_cita_cliente ON cita(cliente_id);
CREATE INDEX idx_usuario_email ON usuario(email);
CREATE INDEX idx_usuario_dni ON usuario(dni);
CREATE INDEX idx_bloqueo_fecha ON bloqueo_horario(fecha_inicio, fecha_fin);
```

## Modelo de Datos para Interoperación

### Tabla de Pagos (TPVV)

```sql
CREATE TABLE pago (
    id SERIAL PRIMARY KEY,
    cita_id INT REFERENCES cita(id),
    monto DECIMAL(10,2) NOT NULL,
    metodo VARCHAR(50), -- 'TARJETA', 'PAYPAL', 'TRANSFERENCIA'
    estado VARCHAR(20), -- 'PENDIENTE', 'COMPLETADO', 'FALLIDO'
    transaccion_id VARCHAR(100), -- ID del TPVV externo
    fecha_pago TIMESTAMP DEFAULT NOW()
);
```

### Tabla de Productos (Tienda Online)

```sql
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
```

## Estadísticas y Reportes

### Vistas Útiles

```sql
-- Vista: Ocupación por fisioterapeuta
CREATE VIEW v_ocupacion_fisio AS
SELECT
    f.usuario_id,
    u.nombre || ' ' || u.apellidos AS fisioterapeuta,
    COUNT(c.id) AS total_citas,
    COUNT(CASE WHEN c.estado = 'COMPLETADA' THEN 1 END) AS citas_completadas,
    SUM(c.precio_pagado) AS ingresos_generados
FROM fisioterapeuta f
JOIN usuario u ON f.usuario_id = u.id
LEFT JOIN cita c ON c.fisioterapeuta_id = f.usuario_id
GROUP BY f.usuario_id, u.nombre, u.apellidos;
```
