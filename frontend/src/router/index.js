import { createRouter, createWebHistory } from "vue-router";
import { useAuthStore } from "../stores/auth";

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: "/",
      name: "home",
      component: () => import("../views/public/Home.vue"),
    },
    {
      path: "/servicios",
      name: "servicios",
      component: () => import("../views/public/Servicios.vue"),
    },
    {
      path: "/equipo",
      name: "equipo",
      component: () => import("../views/public/Equipo.vue"),
    },
    {
      path: "/contacto",
      name: "contacto",
      component: () => import("../views/public/Contacto.vue"),
    },
    {
      path: "/login",
      name: "login",
      component: () => import("../views/auth/Login.vue"),
    },
    {
      path: "/register",
      name: "register",
      component: () => import("../views/auth/Register.vue"),
    },
    {
      path: "/cliente",
      name: "cliente-dashboard",
      component: () => import("../views/cliente/Dashboard.vue"),
      meta: { requiresAuth: true, role: "CLIENTE" },
    },
    {
      path: "/cliente/reservar-cita",
      name: "reservar-cita",
      component: () => import("../views/cliente/ReservarCita.vue"),
      meta: { requiresAuth: true, role: "CLIENTE" },
    },
    {
      path: "/cliente/mis-citas",
      name: "mis-citas",
      component: () => import("../views/cliente/MisCitas.vue"),
      meta: { requiresAuth: true, role: "CLIENTE" },
    },
    {
      path: "/recepcionista",
      redirect: "/recepcionista/dashboard"
    },
    {
      path: "/recepcionista/dashboard",
      name: "recepcionista-dashboard",
      component: () => import("../views/recepcionista/Dashboard.vue"),
      meta: { requiresAuth: true, role: "RECEPCIONISTA" },
    },
    {
      path: "/recepcionista/citas-management",
      name: "recepcionista-calendario",
      component: () => import("../views/recepcionista/CitasManagement.vue"),
      meta: { requiresAuth: true, role: "RECEPCIONISTA" },
    },
    {
      path: "/recepcionista/crear-cita",
      name: "recepcionista-crear",
      component: () => import("../views/recepcionista/CrearCita.vue"),
      meta: { requiresAuth: true, role: "RECEPCIONISTA" },
    },
    {
      path: "/fisioterapeuta",
      redirect: "/fisioterapeuta/dashboard"
    },
    {
      path: "/fisioterapeuta/dashboard",
      name: "fisioterapeuta-dashboard",
      component: () => import("../views/fisioterapeuta/Dashboard.vue"),
      meta: { requiresAuth: true, role: "FISIOTERAPEUTA" },
    },
    {
      path: "/fisioterapeuta/agenda",
      name: "fisioterapeuta-agenda",
      component: () => import("../views/fisioterapeuta/AgendaSemanal.vue"),
      meta: { requiresAuth: true, role: "FISIOTERAPEUTA" },
    },
    {
      path: "/fisioterapeuta/citas/:id",
      name: "fisioterapeuta-detalle-cita",
      component: () => import("../views/fisioterapeuta/DetalleCita.vue"),
      meta: { requiresAuth: true, role: "FISIOTERAPEUTA" },
    },
    {
      path: "/fisioterapeuta/clientes/:id/historial",
      name: "fisioterapeuta-historial-cliente",
      component: () => import("../views/fisioterapeuta/HistorialCliente.vue"),
      meta: { requiresAuth: true, role: "FISIOTERAPEUTA" },
    },
  ],
});

// Guardia de navegación
router.beforeEach((to, from, next) => {
  const authStore = useAuthStore();

  if (to.meta.requiresAuth && !authStore.isAuthenticated) {
    next("/login");
  } else if (to.meta.role && authStore.user?.rol !== to.meta.role) {
    next("/");
  } else {
    next();
  }
});

export default router;
