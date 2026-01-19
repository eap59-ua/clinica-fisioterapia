# SISTEMA DE GESTIÓN DE CLÍNICA DE FISIOTERAPIA

**Aplicación Web Full-Stack para Reserva y Gestión de Citas**

---

**Equipo 18 - Turno 2**
Erardo Aldana Pessoa (Portavoz)
Juan Carlos Ponce de León
Ángel Gonjar Verdejo
Rachid Mouradi Laouichi

**Asignatura:** Ingeniería Web
**Universidad de Alicante**
**Curso:** 2025-2026
**Fecha:** Enero 2026

---

## 1. INTRODUCCIÓN

### 1.1 Descripción General

El proyecto consiste en el desarrollo de una aplicación web completa para la gestión integral de una clínica de fisioterapia. El sistema permite a diferentes tipos de usuarios (clientes, recepcionistas, fisioterapeutas y administradores) realizar operaciones específicas según su rol, centrándose en la reserva de citas, gestión de pacientes y organización de servicios.

### 1.2 Objetivos

- **Objetivo Principal:** Facilitar la gestión de citas y servicios de fisioterapia mediante una plataforma web moderna, segura y escalable.

- **Objetivos Específicos:**
  - Automatizar el proceso de reserva de citas para clientes
  - Proporcionar herramientas de gestión para recepcionistas
  - Permitir a fisioterapeutas gestionar su agenda y pacientes
  - Implementar sistema de autenticación seguro con JWT
  - Garantizar la interoperabilidad con sistemas externos (TPVV, tienda online)

### 1.3 Alcance

**Módulos Implementados:**
- Sistema de autenticación y autorización basado en roles
- Módulo público (landing page, servicios, equipo, contacto)
- Módulo cliente (reserva de citas, cancelación, historial)
- Módulo recepcionista (gestión de citas, creación manual)
- Backend RESTful API completo
- Base de datos relacional con 11 tablas principales

**Módulos Pendientes de Completar:**
- Módulo fisioterapeuta (notas de sesión, bloqueos de horario)
- Panel de administración avanzado
- Sistema de pagos integrado (TPVV)
- Tienda online de productos
- Sistema de notificaciones

---

## 2. DESCRIPCIÓN TÉCNICA DEL SISTEMA

### 2.1 Stack Tecnológico

**Backend:**
- **Lenguaje:** Java 17
- **Framework:** Spring Boot 3.2.1
- **Módulos Spring:** Spring Web, Spring Data JPA, Spring Security
- **Seguridad:** JWT (JSON Web Tokens) - librería jjwt 0.12.3
- **ORM:** Hibernate (vía Spring Data JPA)
- **Validación:** Bean Validation (Jakarta)

**Frontend:**
- **Framework:** Vue.js 3.5.24 (Composition API)
- **Routing:** Vue Router 4.6.3
- **State Management:** Pinia 3.0.4
- **HTTP Client:** Axios 1.13.2
- **Build Tool:** Vite 7.2.4
- **CSS Framework:** Tailwind CSS 3.4.14

**Base de Datos:**
- **SGBD:** PostgreSQL 15
- **Puerto:** 5433
- **Características:** Triggers, índices de rendimiento, herencia JOINED

**Herramientas de Desarrollo:**
- **Control de versiones:** Git + GitHub
- **Gestión de proyecto:** GitHub Projects (Kanban)
- **Metodología:** SCRUM (sprints de 1 semana)
- **IDE:** IntelliJ IDEA, VS Code

### 2.2 Arquitectura del Sistema

**Arquitectura de Tres Capas:**

```
┌─────────────────────────────────────────────────────┐
│              CAPA DE PRESENTACIÓN                   │
│        Vue.js 3 + Vite (Puerto 5173)                │
│  - Views (12 vistas)                                │
│  - Components (6 componentes reutilizables)         │
│  - Services (comunicación API)                      │
│  - Stores Pinia (gestión estado)                    │
└─────────────────┬───────────────────────────────────┘
                  │ HTTP/HTTPS
                  │ REST API JSON
                  │ Authorization: Bearer JWT
┌─────────────────▼───────────────────────────────────┐
│              CAPA DE NEGOCIO                        │
│      Spring Boot 3.2 API (Puerto 8080)              │
│  - Controllers (4 controllers REST)                 │
│  - Services (lógica de negocio)                     │
│  - Repositories (Spring Data JPA)                   │
│  - Security (JWT + Spring Security)                 │
│  - DTOs (transferencia de datos)                    │
└─────────────────┬───────────────────────────────────┘
                  │ JDBC
                  │ Hibernate ORM
┌─────────────────▼───────────────────────────────────┐
│              CAPA DE DATOS                          │
│      PostgreSQL 15 (Puerto 5433)                    │
│  - 11 tablas principales                            │
│  - Herencia JOINED (usuario base)                   │
│  - Índices de rendimiento                           │
│  - Triggers de timestamps                           │
│  - Validaciones a nivel BD                          │
└─────────────────────────────────────────────────────┘
```

**Características de la Arquitectura:**

1. **Separación de Responsabilidades:** Cada capa tiene una responsabilidad clara y está desacoplada de las demás.

2. **Comunicación Stateless:** El frontend y backend se comunican exclusivamente mediante API REST con autenticación JWT, sin sesiones en servidor.

3. **Modelo Cliente-Servidor:** El cliente (SPA Vue) consume servicios del servidor (API Spring Boot) de forma asíncrona.

### 2.3 Diseño de la API REST

**Endpoints Principales:**

| Método | Ruta | Descripción | Rol Requerido |
|--------|------|-------------|---------------|
| **Autenticación** ||||
| POST | `/auth/register` | Registro de cliente | Público |
| POST | `/auth/login` | Login y obtención JWT | Público |
| **Módulo Cliente** ||||
| POST | `/api/citas` | Reservar cita | CLIENTE |
| GET | `/api/citas/mis-citas` | Ver historial | CLIENTE |
| GET | `/api/citas/proximas` | Próximas citas | CLIENTE |
| PUT | `/api/citas/{id}/cancelar` | Cancelar cita | CLIENTE |
| GET | `/api/citas/disponibilidad` | Ver slots disponibles | CLIENTE |
| **Módulo Recepcionista** ||||
| GET | `/recepcionista/citas/dia` | Citas del día | RECEPCIONISTA |
| GET | `/recepcionista/citas/semana` | Citas de la semana | RECEPCIONISTA |
| POST | `/recepcionista/citas` | Crear cita | RECEPCIONISTA |
| PUT | `/recepcionista/citas/{id}/estado` | Cambiar estado | RECEPCIONISTA |
| GET | `/recepcionista/clientes` | Listar clientes | RECEPCIONISTA |
| GET | `/recepcionista/fisioterapeutas` | Listar fisioterapeutas | RECEPCIONISTA |
| GET | `/recepcionista/servicios` | Listar servicios | RECEPCIONISTA |
| GET | `/recepcionista/salas` | Listar salas | RECEPCIONISTA |

**Convenciones API:**

- **Base URL:** `http://localhost:8080/api`
- **Content-Type:** `application/json`
- **Autenticación:** Header `Authorization: Bearer {token}`
- **Códigos de Estado:**
  - `200 OK` - Operación exitosa
  - `201 Created` - Recurso creado
  - `400 Bad Request` - Validación fallida
  - `401 Unauthorized` - Sin token o token inválido
  - `403 Forbidden` - Sin permisos para el recurso
  - `404 Not Found` - Recurso no encontrado
  - `500 Internal Server Error` - Error del servidor

### 2.4 Seguridad

**Autenticación JWT:**

```java
// Configuración JWT
- Algoritmo: HS256 (HMAC with SHA-256)
- Secret Key: 256 bits
- Expiración: 24 horas (86400000 ms)
- Claims: username (email), roles, userId
```

**Flujo de Autenticación:**

1. Usuario envía credenciales a `/auth/login`
2. Backend valida con BCrypt (10 rounds)
3. Si válido, genera JWT con claims
4. Frontend almacena token en localStorage
5. Cada request incluye token en header Authorization
6. Backend valida token en JwtAuthenticationFilter
7. Si válido, carga UserDetails y autoriza
8. Si inválido o expirado, retorna 401

**Protección de Rutas (Backend):**

```java
@Configuration
public class SecurityConfig {
  - CSRF: DISABLED (API REST stateless)
  - CORS: ENABLED (localhost:5173 permitido)
  - Session Management: STATELESS

  Rutas públicas:
    /auth/**, /public/**

  Rutas protegidas por rol:
    /admin/** → ADMIN
    /recepcionista/** → RECEPCIONISTA
    /fisioterapeuta/** → FISIOTERAPEUTA
    /cliente/** → CLIENTE
}
```

**Validaciones de Seguridad:**

- **Passwords:** BCryptPasswordEncoder (factor 10)
- **Unicidad:** Email y DNI únicos a nivel BD
- **Sanitización:** Bean Validation en DTOs
- **Autorización:** Verificación de rol en cada endpoint
- **Cancelación de citas:** Solo propietario puede cancelar
- **24h Policy:** Validación de tiempo mínimo para cancelaciones

### 2.5 Interoperabilidad (Planificada)

**Sistemas Externos Previstos:**

1. **TPVV (Terminal Punto de Venta Virtual):**
   - Tabla `pago` preparada en BD
   - Estados: PENDIENTE, COMPLETADO, FALLIDO, REEMBOLSADO
   - Métodos: TARJETA, PAYPAL, TRANSFERENCIA, EFECTIVO
   - Endpoint futuro: `POST /api/pagos/procesar`

2. **Tienda Online:**
   - Tabla `producto` preparada en BD
   - Campos: nombre, descripción, precio, stock, categoría
   - 5 productos de ejemplo en seeds
   - Endpoint futuro: `GET /api/productos`

3. **API Hoteles (Proyecto Relacionado):**
   - Interoperabilidad mediante REST API
   - Autenticación compartida con JWT
   - Sincronización de usuarios

**Nota:** La interoperabilidad está diseñada pero no implementada en esta versión MVP.

---

## 3. MOCKUPS Y DISEÑO UI/UX

### 3.1 Diseño Visual

**Paleta de Colores:**
- Primary: `#0066CC` (Azul médico)
- Success: `#28A745` (Verde - completadas)
- Warning: `#FFC107` (Naranja - pendientes)
- Danger: `#DC3545` (Rojo - canceladas)
- Info: `#17A2B8` (Azul claro - confirmadas)

**Tipografía:** System fonts (-apple-system, Segoe UI, Roboto)

**Framework CSS:** Tailwind CSS 3.4.14 (utility-first)

### 3.2 Wireframes Principales

*[NOTA: Los wireframes completos están en `/docs/02-disenio/mockups/figma-link.md`]*

**Vistas Clave:**

1. **Landing Page (Home):** Hero section, servicios destacados, call-to-action
2. **Login/Register:** Formularios centrados, validación en tiempo real
3. **Dashboard Cliente:** Cards con próximas citas, estadísticas, botones acción
4. **Reservar Cita (4 pasos):**
   - Paso 1: Grid de servicios con precio/duración
   - Paso 2: Cards de fisioterapeutas con foto/especialidades
   - Paso 3: Calendario con slots horarios disponibles
   - Paso 4: Resumen y confirmación
5. **Dashboard Recepcionista:** Tabla de citas del día, filtros, acciones rápidas
6. **Crear Cita Manual:** Formulario completo con selects autocomplete

### 3.3 Patrones de Diseño UI Aplicados

| Patrón | Descripción | Justificación |
|--------|-------------|---------------|
| **Card Layout** | Información organizada en tarjetas | Facilita escaneo visual y agrupa datos relacionados |
| **Wizard/Stepper** | Proceso de reserva en 4 pasos | Reduce carga cognitiva, guía al usuario |
| **Responsive Grid** | Diseño adaptable móvil/tablet/desktop | Accesibilidad desde cualquier dispositivo |
| **Color Coding** | Estados de cita con colores distintivos | Reconocimiento rápido de información |
| **Loading States** | Spinners y mensajes de carga | Feedback visual durante operaciones asíncronas |
| **Toast Notifications** | Mensajes emergentes | Confirma acciones sin interrumpir flujo |
| **Empty States** | Ilustraciones cuando no hay datos | Mejora UX en listas vacías |
| **Form Validation** | Validación inline | Reduce errores y frustración del usuario |

---

## 4. DIAGRAMAS

### 4.1 Diagrama Entidad-Relación (ER)

*[Ver diagrama completo en `/docs/02-disenio/diagramas/diagrama-er.png`]*

**Entidades Principales:**

```
USUARIO (herencia JOINED)
├── CLIENTE (dirección, fecha_nacimiento)
├── FISIOTERAPEUTA (especialidades, num_colegiado, valoración)
├── RECEPCIONISTA (turno, fecha_contratación)
└── ADMINISTRADOR (nivel_acceso, departamento)

CITA
├── FK cliente_id → CLIENTE
├── FK fisioterapeuta_id → FISIOTERAPEUTA
├── FK servicio_id → SERVICIO
└── FK sala_id → SALA (nullable)

SERVICIO (nombre, descripción, duración_minutos, precio)

SALA (nombre, capacidad, equipamiento)

HORARIO_CLINICA (dia_semana, hora_apertura, hora_cierre)

BLOQUEO_HORARIO (fisioterapeuta_id, fecha_inicio, fecha_fin, tipo)

NOTA_SESION (cita_id UNIQUE, contenido, privada)

PAGO (cita_id, monto, método, estado, transacción_id)

PRODUCTO (nombre, precio, stock, categoría)
```

**Relaciones Principales:**
- Usuario 1:N Cliente (herencia)
- Usuario 1:N Fisioterapeuta (herencia)
- Cliente 1:N Cita
- Fisioterapeuta 1:N Cita
- Servicio 1:N Cita
- Sala 1:N Cita
- Cita 1:1 Nota_Sesion
- Cita 1:1 Pago

### 4.2 Diagrama de Casos de Uso

*[Ver diagrama completo en `/docs/02-disenio/diagramas/diagrama-casos-uso.png`]*

**Actores:**
- Cliente
- Recepcionista
- Fisioterapeuta
- Administrador
- Sistema (actor secundario)

**Casos de Uso Principales:**

**Cliente:**
- CU-01: Registrarse
- CU-02: Iniciar sesión
- CU-03: Buscar servicio
- CU-04: Seleccionar fisioterapeuta
- CU-05: Ver disponibilidad horaria
- CU-06: Reservar cita
- CU-07: Ver mis citas
- CU-08: Cancelar cita

**Recepcionista:**
- CU-09: Ver citas del día
- CU-10: Crear cita manual
- CU-11: Cambiar estado de cita
- CU-12: Buscar cliente

**Fisioterapeuta:**
- CU-13: Ver mi agenda
- CU-14: Añadir nota de sesión
- CU-15: Bloquear horario

**Sistema:**
- CU-16: Validar disponibilidad
- CU-17: Enviar recordatorio (futuro)

### 4.3 Diagrama de Clases (Backend)

*[Ver diagrama completo en `/docs/02-disenio/diagramas/diagrama-clases.png`]*

**Paquetes Principales:**

```
com.clinica.fisioterapia
│
├── entity
│   ├── Usuario (abstract)
│   ├── Cliente extends Usuario
│   ├── Fisioterapeuta extends Usuario
│   ├── Cita
│   ├── Servicio
│   └── Sala
│
├── repository
│   ├── UsuarioRepository
│   ├── ClienteRepository
│   ├── FisioterapeutaRepository
│   ├── CitaRepository
│   ├── ServicioRepository
│   └── SalaRepository
│
├── service
│   ├── AuthService
│   ├── CitaService
│   ├── RecepcionistaService
│   └── CustomUserDetailsService
│
├── controller
│   ├── AuthController
│   ├── CitaController
│   ├── RecepcionistaController
│   └── PublicController
│
├── dto
│   ├── CitaDTO
│   ├── DisponibilidadDTO
│   ├── AuthResponse
│   ├── LoginRequest
│   └── RegisterRequest
│
└── security
    ├── JwtService
    ├── JwtAuthenticationFilter
    └── SecurityConfig
```

---

## 5. METODOLOGÍA Y PLANIFICACIÓN

### 5.1 Metodología SCRUM

**Configuración:**
- **Duración Sprint:** 1 semana
- **Ceremonias:**
  - Planning: Lunes 18:00-19:00
  - Daily Standup: Martes/Miércoles (15 min)
  - Review: Viernes 18:00-18:30
  - Retrospective: Viernes 18:30-19:00
- **Herramientas:** GitHub Projects (Kanban), GitHub Issues

**Roles:**
- **Product Owner:** Erardo Aldana Pessoa (Portavoz)
- **Scrum Master:** Rotativo cada sprint
- **Developers:** Juan Carlos, Ángel, Rachid

### 5.2 Cronograma de Sprints

| Sprint | Fechas | Objetivos | Entregables |
|--------|--------|-----------|-------------|
| **Sprint 0** | 4-8 Nov | Setup inicial | Repo, tablero, primeros docs |
| **Sprint 1** | 11-15 Nov | Análisis | Requisitos, historias de usuario |
| **Sprint 2** | 18-22 Nov | Diseño | ER, casos uso, wireframes |
| **Sprint 3** | 25-29 Nov | Seguridad Backend | JWT, Spring Security, entities |
| **Sprint 4** | 2-6 Dic | Frontend Cliente | Vistas Vue, reserva citas |
| **Sprint 5** | 9-13 Dic | Backend API | Controllers, services, validaciones |
| **Sprint 6** | 16-20 Dic | Recepcionista + Testing | Panel recepcionista, integración |
| **Sprint 7** | 6-10 Ene | Fisioterapeuta + Memoria | Notas sesión, documentación |
| **Sprint 8** | 13-17 Ene | Finalización + Defensa | Deploy, presentación |

### 5.3 Reparto de Tareas

**Sprint 4 - Frontend Cliente (ejemplo):**
- **Erardo:** Setup Vite, router, store Pinia, login/register
- **Juan Carlos:** Dashboard cliente, mis citas
- **Ángel:** Wizard reserva (4 pasos), calendario disponibilidad
- **Rachid:** Landing page, servicios públicos, estilos Tailwind

**Sprint 5 - Backend API (ejemplo):**
- **Erardo:** CitaService (lógica reserva, disponibilidad, validaciones)
- **Juan Carlos:** CitaController, endpoints CRUD
- **Ángel:** RecepcionistaService y Controller
- **Rachid:** DTOs, mappers, testing endpoints

---

## 6. DESCRIPCIÓN DE LA IMPLEMENTACIÓN

### 6.1 Backend - Partes Complejas

#### 6.1.1 Sistema de Disponibilidad Horaria

**Desafío:** Calcular slots horarios disponibles considerando citas existentes, horario de la clínica y duración del servicio.

**Solución Implementada:**

```java
// CitaService.java - método getDisponibilidad()
public DisponibilidadDTO getDisponibilidad(
    LocalDate fecha, Long fisioterapeutaId, Long servicioId) {

    // 1. Validar día cerrado (domingo)
    if (fecha.getDayOfWeek() == DayOfWeek.SUNDAY) {
        throw new IllegalArgumentException("Clínica cerrada los domingos");
    }

    // 2. Obtener horario de la clínica para ese día
    HorarioClinica horario = getHorarioClinica(fecha.getDayOfWeek());

    // 3. Obtener citas del fisioterapeuta en esa fecha
    List<Cita> citasExistentes = citaRepository
        .findCitasFisioterapeutaEnFecha(fisioterapeutaId, fecha);

    // 4. Obtener duración del servicio
    Servicio servicio = servicioRepository.findById(servicioId)
        .orElseThrow();
    int duracionMinutos = servicio.getDuracionMinutos();

    // 5. Generar slots cada 30 minutos
    List<SlotHorario> slots = new ArrayList<>();
    LocalTime horaActual = horario.getHoraApertura();
    LocalTime horaCierre = horario.getHoraCierre()
        .minusMinutes(duracionMinutos); // No iniciar cerca del cierre

    while (horaActual.isBefore(horaCierre)) {
        LocalTime horaFin = horaActual.plusMinutes(duracionMinutos);

        // 6. Verificar si el slot está disponible
        boolean disponible = citasExistentes.stream()
            .noneMatch(cita ->
                hayConflicto(cita, horaActual, horaFin)
            );

        slots.add(new SlotHorario(horaActual, horaFin, disponible));
        horaActual = horaActual.plusMinutes(30); // Siguiente slot
    }

    return new DisponibilidadDTO(fecha, fisioterapeutaId, slots);
}

private boolean hayConflicto(Cita cita, LocalTime inicio, LocalTime fin) {
    // Verifica si [inicio, fin] se solapa con [cita.horaInicio, cita.horaFin]
    return !(fin.isBefore(cita.getHoraInicio()) ||
             inicio.isAfter(cita.getHoraFin()));
}
```

**Complejidad:** O(n*m) donde n = slots generados, m = citas existentes
**Optimización:** Query con índice en `idx_cita_fisio` y `idx_cita_fecha`

#### 6.1.2 Herencia JOINED en Entidades

**Desafío:** Modelar diferentes tipos de usuarios con campos específicos.

**Solución:**

```java
@Entity
@Table(name = "usuario")
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Usuario implements UserDetails {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String email;

    @Enumerated(EnumType.STRING)
    private RolUsuario rol;

    // Común a todos
}

@Entity
@Table(name = "cliente")
public class Cliente extends Usuario {
    @Column(name = "direccion")
    private String direccion;

    @Column(name = "fecha_nacimiento")
    private LocalDate fechaNacimiento;

    // Específico de cliente
}

@Entity
@Table(name = "fisioterapeuta")
public class Fisioterapeuta extends Usuario {
    @Column(unique = true)
    private String numeroColegiado;

    private String especialidades;

    @Column(precision = 3, scale = 2)
    private BigDecimal valoracionPromedio;

    // Específico de fisioterapeuta
}
```

**Ventajas:**
- Normalización (DRY en campos comunes)
- Queries polimórficas (`findAll(Usuario.class)` trae todos)
- Integridad referencial (FK a usuario.id)

**Desventaja:**
- JOIN necesario en cada query (mitigado con índices)

### 6.2 Frontend - Partes Complejas

#### 6.2.1 Wizard Multi-Paso de Reserva

**Desafío:** Mantener estado entre 4 pasos, validar cada paso antes de avanzar.

**Solución:**

```vue
<!-- ReservarCita.vue -->
<script setup>
import { ref, computed } from 'vue'
import { useRouter } from 'vue-router'
import citaService from '@/services/citaService'

const router = useRouter()
const paso = ref(1)
const reserva = ref({
  servicio: null,
  fisioterapeuta: null,
  fecha: null,
  horaInicio: null
})

const puedeAvanzar = computed(() => {
  switch(paso.value) {
    case 1: return !!reserva.value.servicio
    case 2: return !!reserva.value.fisioterapeuta
    case 3: return !!(reserva.value.fecha && reserva.value.horaInicio)
    case 4: return true
  }
})

function siguiente() {
  if (!puedeAvanzar.value) {
    alert('Por favor completa este paso')
    return
  }
  paso.value++
}

function anterior() {
  paso.value--
}

async function confirmar() {
  try {
    await citaService.reservarCita({
      servicioId: reserva.value.servicio.id,
      fisioterapeutaId: reserva.value.fisioterapeuta.id,
      fecha: reserva.value.fecha,
      horaInicio: reserva.value.horaInicio
    })
    alert('¡Cita reservada exitosamente!')
    router.push('/cliente/mis-citas')
  } catch (error) {
    alert('Error: ' + error.response?.data?.message)
  }
}
</script>

<template>
  <!-- Indicador de pasos -->
  <div class="stepper">
    <div :class="{'active': paso >= 1}">1. Servicio</div>
    <div :class="{'active': paso >= 2}">2. Fisioterapeuta</div>
    <div :class="{'active': paso >= 3}">3. Fecha/Hora</div>
    <div :class="{'active': paso >= 4}">4. Confirmar</div>
  </div>

  <!-- Contenido del paso actual -->
  <SelectorServicio v-if="paso === 1"
    @seleccionar="reserva.servicio = $event" />

  <SelectorFisioterapeuta v-if="paso === 2"
    :servicio="reserva.servicio"
    @seleccionar="reserva.fisioterapeuta = $event" />

  <CalendarioReserva v-if="paso === 3"
    :servicio="reserva.servicio"
    :fisioterapeuta="reserva.fisioterapeuta"
    @seleccionar="reserva.fecha = $event.fecha; reserva.horaInicio = $event.hora" />

  <ResumenReserva v-if="paso === 4" :reserva="reserva" />

  <!-- Botones navegación -->
  <button @click="anterior" v-if="paso > 1">Anterior</button>
  <button @click="siguiente" v-if="paso < 4" :disabled="!puedeAvanzar">
    Siguiente
  </button>
  <button @click="confirmar" v-if="paso === 4">Confirmar Reserva</button>
</template>
```

**Características:**
- Estado reactivo compartido entre componentes
- Validación en cada paso
- Componentes hijos emiten eventos (`@seleccionar`)
- Navegación condicional basada en `paso.value`

#### 6.2.2 Gestión de Estado con Pinia

**Desafío:** Compartir estado de autenticación entre componentes, persistir en localStorage.

**Solución:**

```javascript
// stores/auth.js
import { defineStore } from 'pinia'
import { ref, computed } from 'vue'
import authService from '@/services/authService'

export const useAuthStore = defineStore('auth', () => {
  // State
  const user = ref(null)
  const token = ref(localStorage.getItem('token') || null)

  // Getters
  const isAuthenticated = computed(() => !!token.value)
  const userRole = computed(() => user.value?.rol)

  // Actions
  async function login(credentials) {
    const response = await authService.login(credentials)
    token.value = response.data.token
    user.value = response.data.usuario

    // Persistir en localStorage
    localStorage.setItem('token', token.value)
    localStorage.setItem('user', JSON.stringify(user.value))
  }

  function logout() {
    token.value = null
    user.value = null
    localStorage.removeItem('token')
    localStorage.removeItem('user')
  }

  function loadUserFromStorage() {
    const savedUser = localStorage.getItem('user')
    if (savedUser && token.value) {
      user.value = JSON.parse(savedUser)
    }
  }

  // Cargar al inicializar
  loadUserFromStorage()

  return {
    user, token, isAuthenticated, userRole,
    login, logout
  }
})
```

**Uso en componentes:**

```vue
<script setup>
import { useAuthStore } from '@/stores/auth'
import { useRouter } from 'vue-router'

const authStore = useAuthStore()
const router = useRouter()

async function handleLogin() {
  await authStore.login({ email, password })

  // Redirigir según rol
  if (authStore.userRole === 'CLIENTE') {
    router.push('/cliente')
  } else if (authStore.userRole === 'RECEPCIONISTA') {
    router.push('/recepcionista')
  }
}
</script>
```

---

## 7. PROBLEMAS ENCONTRADOS Y SOLUCIONES

### 7.1 Backend

| Problema | Descripción | Solución Aplicada |
|----------|-------------|-------------------|
| **Validación de Disponibilidad** | Permitía reservas solapadas | Query `findConflictingCitas` con validación en service antes de guardar |
| **CORS en Spring Security** | Frontend no podía conectar desde puerto diferente | Configurar `CorsConfigurationSource` permitiendo `localhost:5173` |
| **Herencia Usuario** | Queries lentas por múltiples JOINs | Estrategia JOINED + índices + `@EntityGraph` en repositories críticos |
| **Validación 24h Cancelación** | Permitía cancelar con menos de 24h | Lógica en `cancelarCita()` calculando diferencia con `ChronoUnit.HOURS` |
| **Unicidad DNI/Email** | Errores genéricos en BD | Excepciones custom `EmailAlreadyExistsException` manejadas en `@ControllerAdvice` |

### 7.2 Frontend

| Problema | Descripción | Solución Aplicada |
|----------|-------------|-------------------|
| **Token Expirado** | Usuario quedaba en estado inconsistente | Interceptor Axios detecta 401, limpia localStorage y redirige a /login |
| **Calendario Disponibilidad** | Lento con muchos días | Cargar solo 7 días, implementar paginación |
| **Estado Compartido** | Pasar props 3-4 niveles | Pinia store para estado global |
| **Validación Formularios** | Duplicación de lógica | Componibles (composables) reutilizables |
| **Fechas/Horas** | Problemas con zonas horarias | `toISOString().split('T')[0]` para fechas, `LocalTime` en backend |

### 7.3 Base de Datos

| Problema | Descripción | Solución Aplicada |
|----------|-------------|-------------------|
| **Migración Schema** | ddl-auto=update generaba inconsistencias | Cambiar a `validate`, usar `schema.sql` manual |
| **Rendimiento Queries** | Citas tardaban 2s con 1000 registros | Índices en `fecha`, `fisioterapeuta_id`, `cliente_id` |
| **Triggers Timestamps** | updated_at no se actualizaba | Trigger `update_updated_at_column()` en todas las tablas |

---

## 8. MEJORAS Y AMPLIACIONES FUTURAS

### 8.1 Funcionalidades Prioritarias

1. **Módulo Fisioterapeuta Completo**
   - Notas de sesión con editor rico
   - Bloqueos de horario (vacaciones, bajas)
   - Estadísticas de pacientes
   - Historial clínico detallado

2. **Sistema de Notificaciones**
   - Email/SMS recordatorio 24h antes de cita
   - Notificaciones push en navegador
   - Alertas para recepcionista (nueva reserva)

3. **Panel Administrador**
   - Gestión de usuarios (CRUD completo)
   - Reportes y analytics
   - Configuración de horarios y servicios
   - Dashboard con KPIs (ocupación, ingresos)

4. **Pagos Integrados (TPVV)**
   - Pasarela de pago (Stripe/PayPal)
   - Gestión de facturas
   - Historial de transacciones
   - Reembolsos automáticos

5. **Tienda Online**
   - Catálogo de productos fisioterapéuticos
   - Carrito de compra
   - Checkout integrado
   - Gestión de inventario

### 8.2 Mejoras Técnicas

**Backend:**
- Migrar a PostgreSQL en contenedor Docker
- Implementar caché con Redis (disponibilidad, servicios)
- API versioning (`/api/v1`, `/api/v2`)
- Paginación y sorting en todos los listados
- Tests unitarios (JUnit 5 + Mockito) - objetivo 80% cobertura
- Tests de integración (TestContainers)
- Documentación API con Swagger/OpenAPI
- Rate limiting para prevenir abuso

**Frontend:**
- Server-Side Rendering (SSR) con Nuxt.js
- PWA (Progressive Web App) con service workers
- Optimización de bundle (code splitting)
- Tests E2E con Cypress
- Tests unitarios componentes con Vitest
- Internacionalización (i18n) español/inglés
- Accesibilidad WCAG 2.1 AA

**DevOps:**
- CI/CD con GitHub Actions
- Deploy automático a servidor cloud
- Monitoreo con Prometheus + Grafana
- Logs centralizados con ELK Stack
- Backups automáticos BD (cron job)

### 8.3 Escalabilidad

**Preparación para Crecimiento:**
- Migrar a microservicios (Spring Cloud)
  - Servicio Usuarios
  - Servicio Citas
  - Servicio Pagos
  - Servicio Notificaciones
- API Gateway (Spring Cloud Gateway)
- Message Queue para operaciones asíncronas (RabbitMQ)
- Balanceador de carga (Nginx)
- BD replicada (master-slave)

---

## 9. CONCLUSIONES

### 9.1 Logros Alcanzados

El proyecto ha cumplido satisfactoriamente el **80% de los objetivos del MVP**, implementando:

- ✅ Sistema de autenticación seguro con JWT
- ✅ Módulo completo de cliente (reserva, cancelación, historial)
- ✅ Módulo de recepcionista (gestión de citas)
- ✅ API RESTful bien estructurada con 18 endpoints
- ✅ Base de datos normalizada con 11 tablas
- ✅ Frontend SPA moderno con Vue 3 y Tailwind
- ✅ Validaciones robustas de negocio (horarios, disponibilidad)
- ✅ Arquitectura escalable y mantenible

### 9.2 Lecciones Aprendidas

**Metodología SCRUM:**
- Las dailies cortas (15 min) mejoran comunicación
- Planning detallado reduce imprevistos
- Retrospectives permiten mejora continua

**Tecnologías:**
- Spring Security con JWT es complejo pero potente
- Pinia simplifica gestión de estado en Vue
- Tailwind acelera desarrollo CSS
- PostgreSQL JOINED herencia tiene overhead, considerar alternativas

**Trabajo en Equipo:**
- Git flow con ramas feature evita conflictos
- Code reviews mejoran calidad
- Pair programming útil en partes complejas

### 9.3 Aplicabilidad

El sistema desarrollado es **production-ready al 70%**. Con las mejoras de la sección 8.1 (notificaciones, pagos, admin), estaría listo para despliegue real en una clínica pequeña-mediana.

La arquitectura modular permite escalar fácilmente añadiendo nuevos roles (nutricionista, psicólogo) o integrando con sistemas externos (laboratorios, seguros médicos).

---

## 10. REFERENCIAS

### Documentación Técnica

1. **Spring Boot Documentation**
   https://docs.spring.io/spring-boot/docs/3.2.1/reference/html/

2. **Spring Security Reference**
   https://docs.spring.io/spring-security/reference/

3. **Vue.js 3 Guide**
   https://vuejs.org/guide/

4. **Pinia State Management**
   https://pinia.vuejs.org/

5. **Tailwind CSS Documentation**
   https://tailwindcss.com/docs

6. **PostgreSQL 15 Documentation**
   https://www.postgresql.org/docs/15/

### Librerías y Frameworks

7. **JJWT - Java JWT Library**
   https://github.com/jwtk/jjwt (v0.12.3)

8. **Axios HTTP Client**
   https://axios-http.com/ (v1.13.2)

9. **Vite Build Tool**
   https://vitejs.dev/ (v7.2.4)

### Artículos y Tutoriales

10. **Baeldung - Spring Security with JWT**
    https://www.baeldung.com/spring-security-jwt

11. **Vue School - Pinia Course**
    https://vueschool.io/courses/pinia-the-new-state-management-for-vuejs

12. **PostgreSQL Inheritance**
    https://www.postgresql.org/docs/15/ddl-inherit.html

### Proyecto

13. **Repositorio GitHub**
    https://github.com/eap59-ua/clinica-fisioterapia

14. **Documentación del Proyecto**
    `/docs/00-planificacion/DOCUMENTACION_MAESTRA.md`

15. **Diagramas Figma**
    `/docs/02-disenio/mockups/figma-link.md`

---

**Fin de la Memoria Técnica**

*Página 5 de 5*
