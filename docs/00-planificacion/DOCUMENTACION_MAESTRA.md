# 🏥 CLÍNICA DE FISIOTERAPIA - DOCUMENTACIÓN MAESTRA

> **Proyecto:** Sistema de gestión y reservas para clínica de fisioterapia  
> **Equipo 18 - Turno 2**  
> **Curso:** Ingeniería Web 2025-2026  
> **Universidad de Alicante**

---

## 👥 EQUIPO

| Rol           | Nombre                    | Email                  | Responsabilidades                   |
| ------------- | ------------------------- | ---------------------- | ----------------------------------- |
| **Portavoz**  | Erardo Aldana Pessoa      | erardo@example.com     | Coordinación, comunicación profesor |
| Desarrollador | Juan Carlos Ponce de León | juancarlos@example.com | Base de datos, DevOps               |
| Desarrollador | Ángel Gonjar Verdejo      | angel@example.com      | Frontend, UI/UX                     |
| Desarrollador | Rachid Mouradi Laouichi   | rachid@example.com     | Backend, API REST                   |

---

## 🎯 PROYECTO ASIGNADO

**Cliente:** Clínica de Fisioterapia  
**Objetivo:** Sistema web para gestión de reservas de citas con profesionales

### Perfiles de Usuario

1. **Administrador:** Gestión completa, informes
2. **Recepcionista:** Reservas, horarios, asignación
3. **Fisioterapeuta:** Agenda, bloqueos, notas
4. **Cliente:** Reservas, historial
5. **Visitante:** Información, registro

### Interoperación Obligatoria

- ✅ TPVV (pagos online)
- ✅ Tienda Online (productos)
- ✅ API para Hoteles (servicios externos)

---

## 🛠️ STACK TECNOLÓGICO

```yaml
Backend:
  Lenguaje: Java 17
  Framework: Spring Boot 3.2.x
  Dependencias: Spring Web, Spring Data JPA, Spring Security, PostgreSQL

Frontend:
  Framework: Vue.js 3.x
  Build Tool: Vite
  Estado: Pinia
  Routing: Vue Router 4
  Estilos: Tailwind CSS
  HTTP: Axios

Base de Datos:
  Motor: PostgreSQL 15
  ORM: Hibernate (JPA)
  Administración: pgAdmin 4

Metodología:
  Framework: SCRUM
  Sprints: 2 semanas
  Herramientas: GitHub Projects, Figma
```

---

## 📅 CRONOGRAMA GENERAL

| Sprint       | Fecha    | Fase                    | Entregables Clave                                 |
| ------------ | -------- | ----------------------- | ------------------------------------------------- |
| **Sprint 1** | 18 Nov   | Análisis                | Requisitos, Casos de Uso, Mockups iniciales       |
| **Sprint 2** | 25 Nov   | Diseño                  | Mockups completos, Modelo BD, Estructura proyecto |
| **Sprint 3** | 2 Dic    | Seguridad               | BD poblada, Login/Registro, Web pública           |
| Sprint 4     | 9 Dic    | Front Office            | Dashboard cliente, reservas                       |
| Sprint 5     | 16 Dic   | Back Office             | Panel recepcionista, CRUDs                        |
| Sprint 6-8   | Ene 2026 | Interoperación, Testing | TPVV, API, Memoria                                |

---

## 🎯 ESTADO ACTUAL

### ✅ Completado

- [x] Formación del equipo
- [x] Asignación del proyecto
- [x] Selección de tecnologías
- [x] Creación del repositorio

### 🔄 En Progreso (Sprint 1)

- [ ] Análisis de requisitos funcionales completo
- [ ] Diseño de mockups en Figma
- [ ] Modelo de datos entidad-relación
- [ ] Configuración entorno de desarrollo

### ⏳ Pendiente

- [ ] Sprint 2: Diagramas de clases, estructura backend/frontend
- [ ] Sprint 3: Implementación seguridad, web pública

---

## 📂 NAVEGACIÓN RÁPIDA

### Documentos Esenciales

- 📖 [Requisitos Funcionales](../01-analisis/requisitos-funcionales.md)
- 📖 [Historias de Usuario](../01-analisis/historias-de-usuario.md)
- 🗺️ [Modelo de Datos](../02-disenio/modelo-datos.md)
- 🎨 [Mockups Figma](../02-disenio/mockups/figma-link.md)

### Sprints

- 📅 [Sprint 1 - Análisis](../03-sprints/sprint-1-analisis.md)
- 📅 [Sprint 2 - Diseño](../03-sprints/sprint-2-disenio.md)
- 📅 [Sprint 3 - Seguridad](../03-sprints/sprint-3-seguridad.md)

### Técnico

- ⚙️ [Guía de Instalación](../04-tecnico/guia-instalacion.md)
- 🔌 [Documentación API](../04-tecnico/api-endpoints.md)

---

## 🚀 ENLACES RÁPIDOS

- **Repositorio:** https://github.com/eap59-ua/clinica-fisioterapia
- **Tablero Kanban:** https://github.com/eap59-ua/clinica-fisioterapia/projects/1
- **Mockups Figma:** [URL cuando esté creado]
- **Entorno Dev Backend:** http://localhost:8080
- **Entorno Dev Frontend:** http://localhost:5173

---

## 📞 CONTACTO PROFESOR

- **Profesor:** [Aragonés Ferrero, Jaume]
- **Email:** [email]
- **Horario tutorías:** [horario]
- **Aula prácticas:** Turno 2

---

## 🔄 HISTORIAL DE CAMBIOS

| Fecha      | Versión | Cambios                    | Autor  |
| ---------- | ------- | -------------------------- | ------ |
| 05/12/2025 | 1.0     | Creación documento maestro | Erardo |
|            |         |                            |        |

---

## 📌 NOTAS IMPORTANTES

⚠️ **Recordatorios:**

- Sincronizar cambios en GitHub diariamente
- Actualizar tablero Kanban tras cada sesión
- Documentar decisiones técnicas importantes
- Comunicar bloqueos al equipo inmediatamente

✨ **Buenas prácticas:**

- Commits descriptivos en español
- Code review antes de merge a main
- Testear antes de push
- Mantener esta documentación actualizada
