import { createRouter, createWebHistory } from 'vue-router'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      redirect: '/login'
    },
    {
      path: '/login',
      name: 'Login',
      component: () => import('@/views/auth/Login.vue')
    },
    {
      path: '/fisioterapeuta',
      meta: { requiresAuth: true, role: 'FISIOTERAPEUTA' },
      children: [
        {
          path: '',
          redirect: '/fisioterapeuta/dashboard'
        },
        {
          path: 'dashboard',
          name: 'FisioterapeutaDashboard',
          component: () => import('@/views/fisioterapeuta/Dashboard.vue')
        },
        {
          path: 'agenda',
          name: 'FisioterapeutaAgenda',
          component: () => import('@/views/fisioterapeuta/AgendaSemanal.vue')
        },
        {
          path: 'citas/:id',
          name: 'FisioterapeutaDetalleCita',
          component: () => import('@/views/fisioterapeuta/DetalleCita.vue')
        },
        {
          path: 'clientes/:id/historial',
          name: 'FisioterapeutaHistorialCliente',
          component: () => import('@/views/fisioterapeuta/HistorialCliente.vue')
        }
      ]
    }
  ]
})

// Guard de navegación para rutas protegidas
router.beforeEach((to, from, next) => {
  const token = localStorage.getItem('token')
  const userStr = localStorage.getItem('user')
  const user = userStr ? JSON.parse(userStr) : null

  if (to.meta.requiresAuth) {
    if (!token) {
      // No hay token, redirigir a login
      next('/login')
    } else if (to.meta.role && user?.rol !== to.meta.role) {
      // Tiene token pero no el rol adecuado
      console.warn('Usuario sin permisos para esta ruta')
      next('/login')
    } else {
      // Todo OK
      next()
    }
  } else {
    next()
  }
})

export default router
