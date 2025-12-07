<template>
  <nav class="bg-white shadow-lg">
    <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
      <div class="flex justify-between h-16">
        <div class="flex">
          <div class="flex-shrink-0 flex items-center">
            <router-link to="/" class="text-2xl font-bold text-primary">
              🏥 Clínica Fisio
            </router-link>
          </div>
          <div class="hidden sm:ml-6 sm:flex sm:space-x-8">
            <router-link
              to="/"
              class="inline-flex items-center px-1 pt-1 text-gray-700 hover:text-primary"
            >
              Inicio
            </router-link>
            <router-link
              to="/servicios"
              class="inline-flex items-center px-1 pt-1 text-gray-700 hover:text-primary"
            >
              Servicios
            </router-link>
            <router-link
              to="/equipo"
              class="inline-flex items-center px-1 pt-1 text-gray-700 hover:text-primary"
            >
              Nuestro Equipo
            </router-link>
            <router-link
              to="/contacto"
              class="inline-flex items-center px-1 pt-1 text-gray-700 hover:text-primary"
            >
              Contacto
            </router-link>
          </div>
        </div>

        <div class="flex items-center">
          <div v-if="!authStore.isAuthenticated">
            <router-link
              to="/login"
              class="mr-4 text-gray-700 hover:text-primary"
            >
              Iniciar Sesión
            </router-link>
            <router-link
              to="/register"
              class="bg-primary text-white px-4 py-2 rounded-md hover:bg-blue-600"
            >
              Registrarse
            </router-link>
          </div>
          <div v-else class="flex items-center space-x-4">
            <span class="text-gray-700">{{ authStore.userName }}</span>
            <router-link
              :to="`/${authStore.userRole.toLowerCase()}`"
              class="text-primary hover:text-blue-600"
            >
              Mi Panel
            </router-link>
            <button
              @click="handleLogout"
              class="text-red-600 hover:text-red-700"
            >
              Cerrar Sesión
            </button>
          </div>
        </div>
      </div>
    </div>
  </nav>
</template>

<script setup>
import { useAuthStore } from "../../stores/auth";
import { useRouter } from "vue-router";

const authStore = useAuthStore();
const router = useRouter();

const handleLogout = () => {
  authStore.logout();
  router.push("/");
};
</script>
