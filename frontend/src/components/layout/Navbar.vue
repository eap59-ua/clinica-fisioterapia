<template>
  <nav
    class="fixed top-0 left-0 right-0 z-50 bg-white/80 backdrop-blur-xl border-b border-dark-100 shadow-soft"
  >
    <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
      <div class="flex justify-between h-18 py-3">
        <!-- Logo -->
        <div class="flex items-center">
          <router-link to="/" class="flex items-center gap-3 group">
            <div
              class="w-10 h-10 bg-gradient-to-br from-primary-500 to-primary-600 rounded-xl flex items-center justify-center shadow-soft group-hover:shadow-glow transition-all duration-300"
            >
              <svg
                class="w-5 h-5 text-white"
                fill="none"
                stroke="currentColor"
                viewBox="0 0 24 24"
              >
                <path
                  stroke-linecap="round"
                  stroke-linejoin="round"
                  stroke-width="2"
                  d="M4.318 6.318a4.5 4.5 0 000 6.364L12 20.364l7.682-7.682a4.5 4.5 0 00-6.364-6.364L12 7.636l-1.318-1.318a4.5 4.5 0 00-6.364 0z"
                />
              </svg>
            </div>
            <span
              class="font-display text-xl font-bold text-dark-800 hidden sm:block"
              >FisioClínica</span
            >
          </router-link>
        </div>

        <!-- Desktop Navigation -->
        <div class="hidden lg:flex items-center space-x-1">
          <router-link
            to="/"
            class="px-4 py-2 rounded-xl text-dark-600 font-medium hover:text-primary-600 hover:bg-primary-50 transition-all duration-200"
            exact-active-class="text-primary-600 bg-primary-50"
          >
            Inicio
          </router-link>
          <router-link
            to="/servicios"
            class="px-4 py-2 rounded-xl text-dark-600 font-medium hover:text-primary-600 hover:bg-primary-50 transition-all duration-200"
            active-class="text-primary-600 bg-primary-50"
          >
            Servicios
          </router-link>
          <router-link
            to="/equipo"
            class="px-4 py-2 rounded-xl text-dark-600 font-medium hover:text-primary-600 hover:bg-primary-50 transition-all duration-200"
            active-class="text-primary-600 bg-primary-50"
          >
            Equipo
          </router-link>
          <router-link
            to="/testimonios"
            class="px-4 py-2 rounded-xl text-dark-600 font-medium hover:text-primary-600 hover:bg-primary-50 transition-all duration-200"
            active-class="text-primary-600 bg-primary-50"
          >
            Testimonios
          </router-link>

          <!-- Dropdown Más -->
          <div class="relative" @mouseenter="dropdownOpen = true" @mouseleave="dropdownOpen = false">
            <button
              class="px-4 py-2 rounded-xl text-dark-600 font-medium hover:text-primary-600 hover:bg-primary-50 transition-all duration-200 inline-flex items-center gap-1"
            >
              Más
              <svg class="w-4 h-4 transition-transform" :class="{ 'rotate-180': dropdownOpen }" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19 9l-7 7-7-7"/>
              </svg>
            </button>
            <!-- Wrapper con padding para evitar gap en hover -->
            <div
              v-show="dropdownOpen"
              class="absolute top-full left-0 pt-2 z-50"
            >
              <div class="w-48 bg-white rounded-xl shadow-soft-lg border border-dark-100 py-2">
                <router-link
                  to="/sobre-nosotros"
                  @click="dropdownOpen = false"
                  class="block px-4 py-2.5 text-dark-600 hover:bg-primary-50 hover:text-primary-600 transition-all"
                >
                  Sobre Nosotros
                </router-link>
                <router-link
                  to="/faq"
                  @click="dropdownOpen = false"
                  class="block px-4 py-2.5 text-dark-600 hover:bg-primary-50 hover:text-primary-600 transition-all"
                >
                  Preguntas Frecuentes
                </router-link>
                <router-link
                  to="/contacto"
                  @click="dropdownOpen = false"
                  class="block px-4 py-2.5 text-dark-600 hover:bg-primary-50 hover:text-primary-600 transition-all"
                >
                  Contacto
                </router-link>
              </div>
            </div>
          </div>
        </div>

        <!-- Auth Section -->
        <div class="flex items-center gap-3">
          <template v-if="!authStore.isAuthenticated">
            <router-link
              to="/login"
              class="hidden sm:inline-flex items-center px-4 py-2 text-dark-600 font-medium hover:text-primary-600 transition-colors"
            >
              Iniciar Sesión
            </router-link>
            <router-link
              to="/register"
              class="inline-flex items-center gap-2 bg-gradient-to-r from-primary-500 to-primary-600 text-white px-5 py-2.5 rounded-xl font-semibold shadow-soft hover:shadow-glow transition-all duration-300 hover:-translate-y-0.5"
            >
              <span class="hidden sm:inline">Registrarse</span>
              <span class="sm:hidden">Registro</span>
            </router-link>
          </template>

          <template v-else>
            <!-- User Menu -->
            <div class="flex items-center gap-3">
              <div
                class="hidden md:flex items-center gap-2 bg-dark-50 px-3 py-1.5 rounded-lg"
              >
                <div
                  class="w-8 h-8 bg-gradient-to-br from-primary-400 to-primary-600 rounded-lg flex items-center justify-center"
                >
                  <span class="text-white text-sm font-semibold">{{
                    authStore.userName?.charAt(0)?.toUpperCase()
                  }}</span>
                </div>
                <div class="text-sm">
                  <p class="font-medium text-dark-800">
                    {{ authStore.userName }}
                  </p>
                  <p class="text-xs text-dark-500 capitalize">
                    {{ authStore.userRole?.toLowerCase() }}
                  </p>
                </div>
              </div>

              <router-link
                :to="`/${authStore.userRole.toLowerCase()}`"
                class="inline-flex items-center gap-2 bg-primary-50 text-primary-600 px-4 py-2 rounded-xl font-medium hover:bg-primary-100 transition-all duration-200"
              >
                <svg
                  class="w-4 h-4"
                  fill="none"
                  stroke="currentColor"
                  viewBox="0 0 24 24"
                >
                  <path
                    stroke-linecap="round"
                    stroke-linejoin="round"
                    stroke-width="2"
                    d="M4 6a2 2 0 012-2h2a2 2 0 012 2v2a2 2 0 01-2 2H6a2 2 0 01-2-2V6zM14 6a2 2 0 012-2h2a2 2 0 012 2v2a2 2 0 01-2 2h-2a2 2 0 01-2-2V6zM4 16a2 2 0 012-2h2a2 2 0 012 2v2a2 2 0 01-2 2H6a2 2 0 01-2-2v-2zM14 16a2 2 0 012-2h2a2 2 0 012 2v2a2 2 0 01-2 2h-2a2 2 0 01-2-2v-2z"
                  />
                </svg>
                <span class="hidden sm:inline">Mi Panel</span>
              </router-link>

              <button
                @click="handleLogout"
                class="inline-flex items-center gap-2 text-dark-500 hover:text-red-500 px-3 py-2 rounded-xl hover:bg-red-50 transition-all duration-200"
              >
                <svg
                  class="w-5 h-5"
                  fill="none"
                  stroke="currentColor"
                  viewBox="0 0 24 24"
                >
                  <path
                    stroke-linecap="round"
                    stroke-linejoin="round"
                    stroke-width="2"
                    d="M17 16l4-4m0 0l-4-4m4 4H7m6 4v1a3 3 0 01-3 3H6a3 3 0 01-3-3V7a3 3 0 013-3h4a3 3 0 013 3v1"
                  />
                </svg>
                <span class="hidden md:inline">Salir</span>
              </button>
            </div>
          </template>

          <!-- Mobile Menu Button -->
          <button
            @click="mobileMenuOpen = !mobileMenuOpen"
            class="lg:hidden inline-flex items-center justify-center w-10 h-10 rounded-xl text-dark-600 hover:bg-dark-100 transition-colors"
          >
            <svg
              v-if="!mobileMenuOpen"
              class="w-6 h-6"
              fill="none"
              stroke="currentColor"
              viewBox="0 0 24 24"
            >
              <path
                stroke-linecap="round"
                stroke-linejoin="round"
                stroke-width="2"
                d="M4 6h16M4 12h16M4 18h16"
              />
            </svg>
            <svg
              v-else
              class="w-6 h-6"
              fill="none"
              stroke="currentColor"
              viewBox="0 0 24 24"
            >
              <path
                stroke-linecap="round"
                stroke-linejoin="round"
                stroke-width="2"
                d="M6 18L18 6M6 6l12 12"
              />
            </svg>
          </button>
        </div>
      </div>
    </div>

    <!-- Mobile Menu -->
    <div
      v-if="mobileMenuOpen"
      class="lg:hidden bg-white border-t border-dark-100 animate-fade-in-down"
    >
      <div class="px-4 py-4 space-y-1">
        <router-link
          to="/"
          @click="mobileMenuOpen = false"
          class="block px-4 py-3 rounded-xl text-dark-600 font-medium hover:bg-primary-50 hover:text-primary-600 transition-all"
        >
          Inicio
        </router-link>
        <router-link
          to="/servicios"
          @click="mobileMenuOpen = false"
          class="block px-4 py-3 rounded-xl text-dark-600 font-medium hover:bg-primary-50 hover:text-primary-600 transition-all"
        >
          Servicios
        </router-link>
        <router-link
          to="/equipo"
          @click="mobileMenuOpen = false"
          class="block px-4 py-3 rounded-xl text-dark-600 font-medium hover:bg-primary-50 hover:text-primary-600 transition-all"
        >
          Equipo
        </router-link>
        <router-link
          to="/testimonios"
          @click="mobileMenuOpen = false"
          class="block px-4 py-3 rounded-xl text-dark-600 font-medium hover:bg-primary-50 hover:text-primary-600 transition-all"
        >
          Testimonios
        </router-link>
        <router-link
          to="/sobre-nosotros"
          @click="mobileMenuOpen = false"
          class="block px-4 py-3 rounded-xl text-dark-600 font-medium hover:bg-primary-50 hover:text-primary-600 transition-all"
        >
          Sobre Nosotros
        </router-link>
        <router-link
          to="/faq"
          @click="mobileMenuOpen = false"
          class="block px-4 py-3 rounded-xl text-dark-600 font-medium hover:bg-primary-50 hover:text-primary-600 transition-all"
        >
          Preguntas Frecuentes
        </router-link>
        <router-link
          to="/contacto"
          @click="mobileMenuOpen = false"
          class="block px-4 py-3 rounded-xl text-dark-600 font-medium hover:bg-primary-50 hover:text-primary-600 transition-all"
        >
          Contacto
        </router-link>

        <div
          v-if="!authStore.isAuthenticated"
          class="pt-4 border-t border-dark-100 space-y-2"
        >
          <router-link
            to="/login"
            @click="mobileMenuOpen = false"
            class="block px-4 py-3 rounded-xl text-dark-600 font-medium hover:bg-dark-50 transition-all text-center"
          >
            Iniciar Sesion
          </router-link>
          <router-link
            to="/register"
            @click="mobileMenuOpen = false"
            class="block px-4 py-3 rounded-xl bg-gradient-to-r from-primary-500 to-primary-600 text-white font-semibold text-center"
          >
            Registrarse
          </router-link>
        </div>
      </div>
    </div>
  </nav>

  <!-- Spacer for fixed navbar -->
  <div class="h-20"></div>
</template>

<script setup>
import { ref } from "vue";
import { useAuthStore } from "../../stores/auth";
import { useRouter } from "vue-router";

const authStore = useAuthStore();
const router = useRouter();
const mobileMenuOpen = ref(false);
const dropdownOpen = ref(false);

const handleLogout = () => {
  authStore.logout();
  mobileMenuOpen.value = false;
  router.push("/");
};
</script>