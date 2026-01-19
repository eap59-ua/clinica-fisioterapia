<template>
  <header class="h-20 relative z-50">

    <nav class="fixed top-0 left-0 right-0 z-50 bg-white/90 backdrop-blur-xl border-b border-gray-100 shadow-sm transition-all duration-300">
      <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <div class="flex justify-between h-20 items-center"> <div class="flex items-center">
            <router-link to="/" class="flex items-center gap-3 group">
              <div class="w-10 h-10 bg-gradient-to-br from-teal-500 to-teal-600 rounded-xl flex items-center justify-center shadow-lg shadow-teal-500/20 group-hover:shadow-teal-500/40 transition-all duration-300">
                <svg class="w-6 h-6 text-white" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M4.318 6.318a4.5 4.5 0 000 6.364L12 20.364l7.682-7.682a4.5 4.5 0 00-6.364-6.364L12 7.636l-1.318-1.318a4.5 4.5 0 00-6.364 0z"/>
                </svg>
              </div>
              <span class="font-display text-xl font-bold text-gray-800 hidden sm:block tracking-tight">FisioClínica</span>
            </router-link>
          </div>

          <div class="hidden lg:flex items-center gap-1">
            <router-link to="/" class="nav-link">Inicio</router-link>
            <router-link to="/servicios" class="nav-link">Servicios</router-link>
            <router-link to="/equipo" class="nav-link">Equipo</router-link>
            <router-link to="/contacto" class="nav-link">Contacto</router-link>
          </div>

          <div class="flex items-center gap-3">
            <template v-if="!authStore.isAuthenticated">
              <router-link to="/login" class="hidden sm:inline-flex px-4 py-2 text-gray-600 font-medium hover:text-teal-600 transition-colors">
                Iniciar Sesión
              </router-link>
              <router-link to="/register" class="btn-primary">
                <span class="hidden sm:inline">Registrarse</span>
                <span class="sm:hidden">Registro</span>
              </router-link>
            </template>

            <template v-else>
              <div class="flex items-center gap-4">
                <div class="hidden md:flex flex-col items-end mr-2">
                  <span class="text-sm font-bold text-gray-700">{{ authStore.userName }}</span>
                  <span class="text-xs text-teal-600 font-medium uppercase tracking-wider">{{ authStore.userRole }}</span>
                </div>

                <router-link :to="`/${authStore.userRole?.toLowerCase()}`" class="p-2 text-gray-500 hover:text-teal-600 hover:bg-teal-50 rounded-lg transition-all" title="Mi Panel">
                  <svg class="w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M4 6a2 2 0 012-2h2a2 2 0 012 2v2a2 2 0 01-2 2H6a2 2 0 01-2-2V6zM14 6a2 2 0 012-2h2a2 2 0 012 2v2a2 2 0 01-2 2h-2a2 2 0 01-2-2V6zM4 16a2 2 0 012-2h2a2 2 0 012 2v2a2 2 0 01-2 2H6a2 2 0 01-2-2v-2zM14 16a2 2 0 012-2h2a2 2 0 012 2v2a2 2 0 01-2 2h-2a2 2 0 01-2-2v-2z"/>
                  </svg>
                </router-link>

                <button @click="handleLogout" class="p-2 text-gray-400 hover:text-red-500 hover:bg-red-50 rounded-lg transition-all" title="Cerrar Sesión">
                  <svg class="w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M17 16l4-4m0 0l-4-4m4 4H7m6 4v1a3 3 0 01-3 3H6a3 3 0 01-3-3V7a3 3 0 013-3h4a3 3 0 013 3v1"/>
                  </svg>
                </button>
              </div>
            </template>

            <button @click="mobileMenuOpen = !mobileMenuOpen" class="lg:hidden p-2 text-gray-600 hover:bg-gray-100 rounded-lg transition-colors">
              <svg v-if="!mobileMenuOpen" class="w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M4 6h16M4 12h16M4 18h16"/>
              </svg>
              <svg v-else class="w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12"/>
              </svg>
            </button>
          </div>
        </div>
      </div>

      <div v-show="mobileMenuOpen" class="lg:hidden bg-white border-t border-gray-100 shadow-xl absolute w-full left-0 z-50">
        <div class="px-4 py-4 space-y-2">
          <router-link to="/" @click="mobileMenuOpen = false" class="mobile-link">Inicio</router-link>
          <router-link to="/servicios" @click="mobileMenuOpen = false" class="mobile-link">Servicios</router-link>
          <router-link to="/equipo" @click="mobileMenuOpen = false" class="mobile-link">Equipo</router-link>
          <router-link to="/contacto" @click="mobileMenuOpen = false" class="mobile-link">Contacto</router-link>
        </div>
      </div>
    </nav>
  </header>
</template>

<script setup>
import { ref } from "vue";
import { useAuthStore } from "@/stores/auth";
import { useRouter } from "vue-router";

const authStore = useAuthStore();
const router = useRouter();
const mobileMenuOpen = ref(false);

const handleLogout = () => {
  authStore.logout();
  mobileMenuOpen.value = false;
  router.push("/");
};
</script>

<style scoped>
/* Clases de utilidad para limpiar el HTML */
.nav-link {
  @apply px-4 py-2 rounded-xl text-gray-600 font-medium hover:text-teal-600 hover:bg-teal-50 transition-all duration-200;
}
.nav-link.router-link-active {
  @apply text-teal-600 bg-teal-50 font-semibold;
}
.btn-primary {
  @apply inline-flex items-center gap-2 bg-gradient-to-r from-teal-500 to-teal-600 text-white px-5 py-2.5 rounded-xl font-semibold shadow-lg shadow-teal-500/30 hover:shadow-teal-500/50 transition-all duration-300 hover:-translate-y-0.5;
}
.mobile-link {
  @apply block px-4 py-3 rounded-xl text-gray-600 font-medium hover:bg-gray-50 hover:text-teal-600 transition-all;
}
</style>