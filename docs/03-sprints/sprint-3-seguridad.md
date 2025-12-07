# Sprint 3: Seguridad y Base Funcional

**Fecha inicio:** 2 diciembre 2025  
**Fecha fin:** 9 diciembre 2025  
**Duración:** 1 semana

---

## 🎯 Objetivos Principales

### Backend (Spring Boot)

- [ ] Configurar proyecto Spring Boot
- [ ] Implementar Spring Security
- [ ] Crear sistema de autenticación JWT
- [ ] Desarrollar endpoints /auth/login y /auth/register
- [ ] Conectar con PostgreSQL

### Frontend (Vue.js)

- [ ] Configurar proyecto Vue 3 + Vite
- [ ] Crear componentes Login y Registro
- [ ] Implementar guardias de navegación
- [ ] Configurar Axios + interceptores
- [ ] Diseñar layout principal

### Web Pública

- [ ] Página de inicio (Home)
- [ ] Sección "Quiénes Somos"
- [ ] Catálogo de servicios
- [ ] Formulario de contacto

---

## 📊 Backlog del Sprint

| ID     | Historia               | Estimación | Responsable |
| ------ | ---------------------- | ---------- | ----------- |
| HU-001 | Registro de cliente    | 5 pts      | Juan Carlos |
| HU-002 | Inicio de sesión       | 3 pts      | Erardo      |
| HU-010 | Ver servicios públicos | 2 pts      | Ángel       |
| HU-030 | Página home pública    | 5 pts      | Ángel       |

**Total:** 30 puntos estimados

---

## 🛠️ Configuración Inicial

### Backend

```
backend/
├── src/main/java/com/clinica/fisioterapia/
│   ├── config/
│   │   └── SecurityConfig.java
│   ├── controller/
│   │   └── AuthController.java
│   ├── dto/
│   │   ├── LoginRequest.java
│   │   └── RegisterRequest.java
│   ├── entity/
│   │   └── Usuario.java
│   ├── repository/
│   │   └── UsuarioRepository.java
│   └── service/
│       ├── AuthService.java
│       └── JwtService.java
```

### Frontend

```
frontend/src/
├── components/
│   ├── LoginForm.vue
│   └── RegisterForm.vue
├── views/
│   ├── Home.vue
│   ├── Login.vue
│   └── Register.vue
├── router/
│   └── index.js
├── stores/
│   └── auth.js
└── services/
    └── authService.js
```
