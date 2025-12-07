# SPRINT 3: Seguridad y Autenticación

**Estado:** ✅ COMPLETADO
**Fecha Inicio:** 7 Diciembre 2025
**Fecha Fin:** 7 Diciembre 2025
**Duración:** 5 horas

## 🎯 Objetivos

- [x] Configurar Spring Security con JWT
- [x] Implementar sistema de autenticación (registro + login)
- [x] Crear entidades JPA con herencia JOINED
- [x] Desarrollar vistas públicas (Home, Servicios, Equipo, Contacto)
- [x] Desarrollar vistas de autenticación (Login, Register)
- [x] Crear panel básico de cliente
- [x] Configurar Vue Router con guardias de navegación
- [x] Implementar Pinia store para autenticación
- [x] Configurar Axios con interceptors JWT

## 📦 Entregables

### Backend

#### Entidades
- `entity/Usuario.java` (clase base con UserDetails)
- `entity/Cliente.java`, `Fisioterapeuta.java`, `Administrador.java`, `Recepcionista.java`
- `entity/RolUsuario.java` (enum)
- `entity/Servicio.java`, `Cita.java`, `Pago.java`

#### Seguridad
- `security/JwtService.java` (generación y validación tokens)
- `security/JwtAuthenticationFilter.java` (interceptor requests)
- `config/SecurityConfig.java` (configuración Spring Security)
- `service/CustomUserDetailsService.java` (carga usuarios)

#### Lógica de Negocio
- `service/AuthService.java` (lógica negocio auth)
- `controller/AuthController.java` (endpoints `/auth/register`, `/auth/login`)
- `controller/PublicController.java` (endpoints `/public/servicios`, `/public/fisioterapeutas`)

#### DTOs y Excepciones
- `dto/RegisterRequest.java`, `LoginRequest.java`, `AuthResponse.java`, `ErrorResponse.java`
- `exception/GlobalExceptionHandler.java` (manejo errores global)
- `exception/EmailAlreadyExistsException.java`, `DniAlreadyExistsException.java`

#### Repositorios
- `repository/UsuarioRepository.java`
- `repository/ClienteRepository.java`
- `repository/FisioterapeutaRepository.java`
- `repository/ServicioRepository.java`

### Frontend

#### Router y Store
- `router/index.js` (Vue Router + guardias navegación)
- `stores/auth.js` (Pinia store autenticación)

#### Servicios
- `services/api.js` (Axios configurado con interceptors)
- `services/authService.js`, `publicService.js`

#### Componentes de Layout
- `components/layout/Navbar.vue`
- `components/layout/Footer.vue`

#### Vistas Públicas
- `views/public/Home.vue`
- `views/public/Servicios.vue`
- `views/public/Equipo.vue`
- `views/public/Contacto.vue`

#### Vistas de Autenticación
- `views/auth/Login.vue`
- `views/auth/Register.vue`

#### Vistas Privadas
- `views/cliente/Dashboard.vue`

### Configuración

#### Backend
- `application.yml` (PostgreSQL 9.6, JWT secret, CORS)
- `pom.xml` (dependencias Maven: Spring Boot 3.2.1, PostgreSQL, JWT, Lombok, etc.)

#### Frontend
- `tailwind.config.js` (Tailwind CSS personalizado)
- `package.json` (dependencias npm: Vue 3, Pinia, Vue Router, Axios, Tailwind)
- `vite.config.js`

#### Base de Datos
- `database/schema.sql` (migrado de ENUMs nativos a VARCHAR con constraints)
- `database/seeds.sql` (datos de prueba)

## 🧪 Testing Realizado

### ✅ Registro de Usuario
- Validaciones funcionando (DNI español, email, edad >18)
- Password encriptado con BCrypt (10 rounds)
- Token JWT generado correctamente
- Cliente creado en tabla `cliente`
- Redirección automática a dashboard

**Endpoint:** `POST /api/auth/register`

**Request:**
```json
{
  "nombre": "Juan",
  "apellidos": "Test Usuario",
  "dni": "99999999Z",
  "email": "juan.test@test.com",
  "telefono": "666777888",
  "password": "password123",
  "direccion": "Calle Test, 1",
  "fechaNacimiento": "2000-01-08"
}
```

**Response:**
```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9...",
  "tipo": "Bearer",
  "usuarioId": 19,
  "nombre": "Juan Test Usuario",
  "email": "juan.test@test.com",
  "rol": "CLIENTE"
}
```

### ✅ Login de Usuario
- Autenticación con Spring Security
- Token JWT válido (24h de expiración)
- Usuario cargado en Pinia store
- LocalStorage actualizado

**Endpoint:** `POST /api/auth/login`

**Request:**
```json
{
  "email": "juan.perez@gmail.com",
  "password": "password123"
}
```

**Response:**
```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9...",
  "tipo": "Bearer",
  "usuarioId": 11,
  "nombre": "Juan Pérez Gómez",
  "email": "juan.perez@gmail.com",
  "rol": "CLIENTE"
}
```

### ✅ Rutas Protegidas
- Redirección a `/login` si no autenticado
- Dashboard cliente accesible tras login
- Guardias de navegación funcionando correctamente
- Logout limpia store y localStorage

### ✅ API Pública
**Servicios:**
- `GET /api/public/servicios` → 6 servicios activos
- Respuesta sin autenticación requerida

**Fisioterapeutas:**
- `GET /api/public/fisioterapeutas` → 5 fisioterapeutas activos
- Passwords ocultos en respuesta

### ✅ CORS
- Frontend (localhost:5173) → Backend (localhost:8080)
- Sin errores de CORS
- Peticiones OPTIONS manejadas correctamente

## 📊 Métricas

- **Líneas de código:** ~3500
- **Archivos creados:** 40+
- **Tests manuales:** 4/4 pasados
- **Endpoints:** 4 (2 auth, 2 public)
- **Componentes Vue:** 12
- **Tiempo invertido:** 5 horas

## 🔐 Seguridad Implementada

### Autenticación
- Passwords hasheados con BCrypt (10 rounds)
- JWT con HMAC-SHA256 (clave 256-bit)
- Expiración tokens: 24 horas (86400000 ms)
- Secret key: 256 bits en `application.yml`

### Validaciones
- DNI español: regex `^[0-9]{8}[A-Z]$`
- Email estándar: regex validación
- Edad mínima: 18 años (CHECK constraint en BD)
- Contraseñas únicas por email/DNI

### Spring Security
- Protección CSRF deshabilitada (API REST stateless)
- Session management: STATELESS
- CORS configurado para localhost:5173
- Rutas públicas: `/auth/**`, `/public/**`
- Rutas protegidas por rol: ADMIN, RECEPCIONISTA, FISIOTERAPEUTA, CLIENTE

### Base de Datos
- Constraints CHECK para validación de datos
- Tipos ENUM migrados a VARCHAR con validación
- Índices en email, DNI, rol para performance
- Triggers para actualización de timestamps

## 🐛 Issues Resueltos

### 1. Problema de ENUMs de PostgreSQL
**Error:** `column "rol" is of type rol_usuario but expression is of type character varying`

**Causa:** Incompatibilidad entre ENUMs nativos de PostgreSQL y `@Enumerated(EnumType.STRING)` de Hibernate

**Solución:**
- Migrar de ENUMs nativos a VARCHAR
- Agregar constraints CHECK para validación
- Actualizar seeds.sql eliminando casts `::rol_usuario`
- Modificar entidades eliminando `columnDefinition = "rol_usuario"`

### 2. Configuración de CORS
**Error:** Peticiones bloqueadas desde frontend

**Solución:**
- Configurar `CorsConfiguration` en SecurityConfig
- Permitir origen `http://localhost:5173`
- Permitir métodos: GET, POST, PUT, DELETE, OPTIONS
- Permitir todas las headers
- `setAllowCredentials(true)`

### 3. Context Path en Spring Security
**Error:** 403 Forbidden en rutas públicas

**Solución:**
- `application.yml` tiene `context-path: /api`
- RequestMatchers deben usar rutas sin `/api`: `/auth/**`, `/public/**`
- Spring añade el prefix automáticamente

### 4. Firewall de Spring Security
**Error:** URLs rechazadas con caracteres `%0A`

**Solución:**
- Configurar `HttpFirewall` personalizado
- `setAllowUrlEncodedSlash(true)`
- `setAllowSemicolon(true)`

## 🚀 Próximos Pasos (Sprint 4)

1. Sistema de reserva de citas
2. Calendario de disponibilidad
3. Panel fisioterapeuta (agenda)
4. Panel recepcionista (gestión citas)
5. Panel administrador (CRUDs)
6. Integración con sistema de pagos
7. Notificaciones por email
8. Exportación de informes PDF

## 👥 Equipo

- **Portavoz:** Erardo Aldana Pessoa
- **Desarrollo Backend:** Erardo Aldana Pessoa
- **Desarrollo Frontend:** Erardo Aldana Pessoa
- **Testing:** Erardo Aldana Pessoa
- **Documentación:** Erardo Aldana Pessoa

## 📈 Retrospectiva

### ✅ Qué funcionó bien
- Integración backend-frontend sin problemas mayores
- Arquitectura clara con separación de responsabilidades
- JWT funcionando correctamente
- Tailwind CSS acelera el desarrollo de UI

### ⚠️ Qué mejorar
- Más tiempo en planificación de estructura de base de datos
- Documentar decisiones técnicas durante el desarrollo
- Agregar tests automatizados (JUnit, Vitest)
- Mejorar manejo de errores en frontend

### 🎓 Aprendizajes
- Incompatibilidad ENUMs PostgreSQL + Hibernate
- Configuración correcta de CORS en Spring Boot 3
- Guardias de navegación en Vue Router 4
- Interceptores de Axios para JWT

---

**Firma Digital:**
Erardo Aldana Pessoa
7 Diciembre 2025 - 22:30 CET

**Pull Request:** #3
**Commits principales:**
- `ce419d6` feat(sprint-3): Implementar autenticación completa frontend + backend
- `cb237fa` feat(sprint-3): Configurar proyecto Spring Boot + Vue.js

**Estado PR:** 🔄 Pendiente de aprobación en `develop`

---

🤖 Generated with [Claude Code](https://claude.com/claude-code)
