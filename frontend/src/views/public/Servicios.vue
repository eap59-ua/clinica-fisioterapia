<template>
  <div class="min-h-screen">
    <!-- Hero Section -->
    <section class="relative py-20 lg:py-28 overflow-hidden bg-gradient-to-br from-primary-600 via-primary-700 to-primary-900">
      <div class="absolute inset-0 bg-hero-pattern opacity-20"></div>
      <div class="absolute top-0 right-0 w-1/3 h-full bg-gradient-to-l from-secondary-500/20 to-transparent"></div>

      <!-- Floating Elements -->
      <div class="absolute top-20 left-10 w-20 h-20 bg-white/10 rounded-full blur-xl animate-float"></div>
      <div class="absolute bottom-20 right-20 w-32 h-32 bg-accent-400/20 rounded-full blur-2xl animate-float" style="animation-delay: 1s;"></div>

      <div class="relative max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 text-center">
        <span class="inline-flex items-center gap-2 bg-white/10 backdrop-blur-sm px-4 py-2 rounded-full text-sm font-medium text-white mb-6">
          <span class="w-2 h-2 bg-accent-400 rounded-full animate-pulse"></span>
          Tratamientos profesionales
        </span>
        <h1 class="font-display text-4xl sm:text-5xl lg:text-6xl font-bold text-white mb-6">
          Nuestros Servicios
        </h1>
        <p class="text-xl text-primary-100 max-w-2xl mx-auto">
          Descubre nuestra amplia gama de tratamientos de fisioterapia diseñados para tu bienestar
        </p>
      </div>

      <!-- Wave Divider -->
      <div class="absolute bottom-0 left-0 right-0">
        <svg class="w-full h-16 text-dark-50" viewBox="0 0 1440 54" fill="currentColor" preserveAspectRatio="none">
          <path d="M0 22L60 16.7C120 11 240 1 360 0.7C480 1 600 11 720 16.7C840 22 960 22 1080 19.3C1200 16 1320 11 1380 8.3L1440 5.7V54H1380C1320 54 1200 54 1080 54C960 54 840 54 720 54C600 54 480 54 360 54C240 54 120 54 60 54H0V22Z"/>
        </svg>
      </div>
    </section>

    <!-- Services Section -->
    <section class="py-20 bg-dark-50">
      <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <!-- Loading State -->
        <div v-if="loading" class="text-center py-20">
          <div class="w-16 h-16 bg-primary-100 rounded-2xl flex items-center justify-center mx-auto mb-4 animate-pulse">
            <svg class="w-8 h-8 text-primary-500" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M4 4v5h.582m15.356 2A8.001 8.001 0 004.582 9m0 0H9m11 11v-5h-.581m0 0a8.003 8.003 0 01-15.357-2m15.357 2H15"/>
            </svg>
          </div>
          <p class="text-dark-500 text-lg">Cargando servicios...</p>
        </div>

        <!-- Services Grid -->
        <div v-else class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-8">
          <div
            v-for="(servicio, index) in servicios"
            :key="servicio.id"
            class="group bg-white rounded-3xl overflow-hidden shadow-soft hover:shadow-soft-xl transition-all duration-500 hover:-translate-y-2 animate-fade-in-up"
            :style="{ animationDelay: `${index * 0.1}s` }"
          >
            <!-- Image Section -->
            <div class="relative h-52 overflow-hidden">
              <div v-if="servicio.imagenUrl" class="absolute inset-0">
                <img
                  :src="servicio.imagenUrl"
                  :alt="servicio.nombre"
                  class="w-full h-full object-cover group-hover:scale-110 transition-transform duration-500"
                />
              </div>
              <div
                v-else
                class="absolute inset-0 bg-gradient-to-br from-primary-500 via-primary-600 to-secondary-600 flex items-center justify-center"
              >
                <div class="w-20 h-20 bg-white/20 backdrop-blur-sm rounded-2xl flex items-center justify-center">
                  <svg class="w-10 h-10 text-white" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M4.318 6.318a4.5 4.5 0 000 6.364L12 20.364l7.682-7.682a4.5 4.5 0 00-6.364-6.364L12 7.636l-1.318-1.318a4.5 4.5 0 00-6.364 0z"/>
                  </svg>
                </div>
              </div>

              <!-- Price Badge -->
              <div class="absolute top-4 right-4 bg-white/90 backdrop-blur-sm px-4 py-2 rounded-xl shadow-soft">
                <span class="text-2xl font-bold text-primary-600">{{ servicio.precio }}€</span>
              </div>
            </div>

            <!-- Content Section -->
            <div class="p-6">
              <h3 class="text-xl font-bold text-dark-800 mb-3 group-hover:text-primary-600 transition-colors">
                {{ servicio.nombre }}
              </h3>
              <p class="text-dark-500 mb-6 line-clamp-3">
                {{ servicio.descripcion }}
              </p>

              <!-- Duration Info -->
              <div class="flex items-center gap-4 pb-6 border-b border-dark-100">
                <div class="flex items-center gap-2 text-dark-400">
                  <svg class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 8v4l3 3m6-3a9 9 0 11-18 0 9 9 0 0118 0z"/>
                  </svg>
                  <span class="font-medium">{{ servicio.duracionMinutos }} min</span>
                </div>
              </div>

              <!-- CTA Button -->
              <button
                @click="reservarServicio(servicio)"
                class="group/btn mt-6 w-full bg-gradient-to-r from-primary-500 to-primary-600 text-white py-3.5 rounded-xl font-semibold shadow-soft hover:shadow-glow transition-all duration-300 flex items-center justify-center gap-2 hover:-translate-y-0.5"
              >
                Reservar Ahora
                <svg class="w-5 h-5 group-hover/btn:translate-x-1 transition-transform" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M17 8l4 4m0 0l-4 4m4-4H3"/>
                </svg>
              </button>
            </div>
          </div>
        </div>

        <!-- Empty State -->
        <div v-if="!loading && servicios.length === 0" class="text-center py-20">
          <div class="w-20 h-20 bg-dark-100 rounded-3xl flex items-center justify-center mx-auto mb-6">
            <svg class="w-10 h-10 text-dark-400" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9.172 16.172a4 4 0 015.656 0M9 10h.01M15 10h.01M21 12a9 9 0 11-18 0 9 9 0 0118 0z"/>
            </svg>
          </div>
          <h3 class="text-xl font-bold text-dark-800 mb-2">No hay servicios disponibles</h3>
          <p class="text-dark-500">Vuelve a intentarlo más tarde</p>
        </div>
      </div>
    </section>

    <!-- CTA Section -->
    <section class="py-20 bg-white">
      <div class="max-w-4xl mx-auto px-4 sm:px-6 lg:px-8 text-center">
        <div class="bg-gradient-to-br from-primary-50 to-secondary-50 rounded-3xl p-10 lg:p-14 border border-primary-100">
          <h2 class="font-display text-3xl lg:text-4xl font-bold text-dark-800 mb-4">
            ¿Necesitas más información?
          </h2>
          <p class="text-lg text-dark-500 mb-8 max-w-2xl mx-auto">
            Nuestro equipo está aquí para ayudarte a elegir el tratamiento más adecuado para ti
          </p>
          <div class="flex flex-col sm:flex-row gap-4 justify-center">
            <router-link
              to="/contacto"
              class="inline-flex items-center justify-center gap-2 bg-gradient-to-r from-primary-500 to-primary-600 text-white px-8 py-4 rounded-2xl font-semibold shadow-soft hover:shadow-glow transition-all duration-300 hover:-translate-y-1"
            >
              Contactar
              <svg class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M17 8l4 4m0 0l-4 4m4-4H3"/>
              </svg>
            </router-link>
            <router-link
              to="/equipo"
              class="inline-flex items-center justify-center gap-2 bg-white border-2 border-primary-200 text-primary-600 px-8 py-4 rounded-2xl font-semibold hover:bg-primary-50 transition-all duration-300"
            >
              Ver Nuestro Equipo
            </router-link>
          </div>
        </div>
      </div>
    </section>
  </div>
</template>

<script setup>
import { ref, onMounted } from "vue";
import { useRouter } from "vue-router";
import { useAuthStore } from "../../stores/auth";
import publicService from "../../services/publicService";

const router = useRouter();
const authStore = useAuthStore();

const servicios = ref([]);
const loading = ref(true);

const reservarServicio = (servicio) => {
  if (authStore.isAuthenticated && authStore.userRole === 'CLIENTE') {
    // Usuario autenticado como cliente: ir a reservar con servicio preseleccionado
    router.push({
      path: '/cliente/reservar-cita',
      query: { servicioId: servicio.id }
    });
  } else if (authStore.isAuthenticated) {
    // Usuario autenticado pero no es cliente
    alert('Solo los clientes pueden reservar citas. Por favor, registra una cuenta de cliente.');
    router.push('/register');
  } else {
    // Usuario no autenticado: ir a login
    router.push('/login');
  }
};

onMounted(async () => {
  try {
    servicios.value = await publicService.getServicios();
  } catch (error) {
    console.error("Error cargando servicios:", error);
  } finally {
    loading.value = false;
  }
});
</script>
