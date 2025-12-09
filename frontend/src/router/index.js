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
