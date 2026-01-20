<template>
  <div class="min-h-screen">
    <!-- Hero Section -->
    <section class="relative py-20 lg:py-28 overflow-hidden bg-gradient-to-br from-secondary-600 via-secondary-700 to-primary-800">
      <div class="absolute inset-0 bg-hero-pattern opacity-20"></div>
      <div class="absolute top-0 right-0 w-1/3 h-full bg-gradient-to-l from-primary-500/20 to-transparent"></div>

      <!-- Floating Elements -->
      <div class="absolute top-20 left-10 w-20 h-20 bg-white/10 rounded-full blur-xl animate-float"></div>
      <div class="absolute bottom-20 right-20 w-32 h-32 bg-accent-400/20 rounded-full blur-2xl animate-float" style="animation-delay: 1s;"></div>

      <div class="relative max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 text-center">
        <span class="inline-flex items-center gap-2 bg-white/10 backdrop-blur-sm px-4 py-2 rounded-full text-sm font-medium text-white mb-6">
          <span class="w-2 h-2 bg-accent-400 rounded-full animate-pulse"></span>
          Profesionales de confianza
        </span>
        <h1 class="font-display text-4xl sm:text-5xl lg:text-6xl font-bold text-white mb-6">
          Nuestro Equipo
        </h1>
        <p class="text-xl text-secondary-100 max-w-2xl mx-auto">
          Profesionales altamente cualificados comprometidos con tu bienestar y recuperación
        </p>
      </div>

      <!-- Wave Divider -->
      <div class="absolute bottom-0 left-0 right-0">
        <svg class="w-full h-16 text-dark-50" viewBox="0 0 1440 54" fill="currentColor" preserveAspectRatio="none">
          <path d="M0 22L60 16.7C120 11 240 1 360 0.7C480 1 600 11 720 16.7C840 22 960 22 1080 19.3C1200 16 1320 11 1380 8.3L1440 5.7V54H1380C1320 54 1200 54 1080 54C960 54 840 54 720 54C600 54 480 54 360 54C240 54 120 54 60 54H0V22Z"/>
        </svg>
      </div>
    </section>

    <!-- Team Section -->
    <section class="py-20 bg-dark-50">
      <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <!-- Loading State -->
        <div v-if="loading" class="text-center py-20">
          <div class="w-16 h-16 bg-secondary-100 rounded-2xl flex items-center justify-center mx-auto mb-4 animate-pulse">
            <svg class="w-8 h-8 text-secondary-500" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M4 4v5h.582m15.356 2A8.001 8.001 0 004.582 9m0 0H9m11 11v-5h-.581m0 0a8.003 8.003 0 01-15.357-2m15.357 2H15"/>
            </svg>
          </div>
          <p class="text-dark-500 text-lg">Cargando equipo...</p>
        </div>

        <!-- Team Grid -->
        <div v-else class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-8">
          <div
            v-for="(fisio, index) in fisioterapeutas"
            :key="fisio.id"
            class="group bg-white rounded-3xl overflow-hidden shadow-soft hover:shadow-soft-xl transition-all duration-500 hover:-translate-y-2 animate-fade-in-up"
            :style="{ animationDelay: `${index * 0.1}s` }"
          >
            <!-- Image Section -->
            <div class="relative h-72 overflow-hidden">
              <img
                :src="fisio.fotoUrl || getAvatarUrl(fisio)"
                :alt="fisio.nombre"
                class="w-full h-full object-cover group-hover:scale-110 transition-transform duration-500"
              />

              <!-- Rating Badge -->
              <div class="absolute top-4 right-4 bg-white/90 backdrop-blur-sm px-3 py-2 rounded-xl shadow-soft flex items-center gap-1.5">
                <svg class="w-5 h-5 text-accent-500" fill="currentColor" viewBox="0 0 20 20">
                  <path d="M9.049 2.927c.3-.921 1.603-.921 1.902 0l1.07 3.292a1 1 0 00.95.69h3.462c.969 0 1.371 1.24.588 1.81l-2.8 2.034a1 1 0 00-.364 1.118l1.07 3.292c.3.921-.755 1.688-1.54 1.118l-2.8-2.034a1 1 0 00-1.175 0l-2.8 2.034c-.784.57-1.838-.197-1.539-1.118l1.07-3.292a1 1 0 00-.364-1.118L2.98 8.72c-.783-.57-.38-1.81.588-1.81h3.461a1 1 0 00.951-.69l1.07-3.292z"/>
                </svg>
                <span class="font-bold text-dark-800">{{ fisio.valoracionPromedio.toFixed(1) }}</span>
              </div>
            </div>

            <!-- Content Section -->
            <div class="p-6">
              <h3 class="text-xl font-bold text-dark-800 mb-1 group-hover:text-secondary-600 transition-colors">
                {{ fisio.nombre }} {{ fisio.apellidos }}
              </h3>

              <p class="text-sm text-dark-400 mb-4">
                Colegiado: {{ fisio.numeroColegiado }}
              </p>

              <!-- Especialidades -->
              <div class="mb-4">
                <p class="text-xs font-semibold text-dark-500 uppercase tracking-wider mb-2">
                  Especialidades
                </p>
                <p class="text-dark-600 text-sm">{{ fisio.especialidades }}</p>
              </div>

              <!-- Biografía -->
              <p class="text-dark-500 text-sm mb-6 line-clamp-3">
                {{ fisio.biografia }}
              </p>

              <!-- CTA Button -->
              <button
                @click="solicitarCita(fisio)"
                class="group/btn w-full bg-gradient-to-r from-secondary-500 to-primary-600 text-white py-3.5 rounded-xl font-semibold shadow-soft hover:shadow-glow transition-all duration-300 flex items-center justify-center gap-2 hover:-translate-y-0.5"
              >
                Solicitar Cita
                <svg class="w-5 h-5 group-hover/btn:translate-x-1 transition-transform" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M17 8l4 4m0 0l-4 4m4-4H3"/>
                </svg>
              </button>
            </div>
          </div>
        </div>

        <!-- Empty State -->
        <div v-if="!loading && fisioterapeutas.length === 0" class="text-center py-20">
          <div class="w-20 h-20 bg-dark-100 rounded-3xl flex items-center justify-center mx-auto mb-6">
            <svg class="w-10 h-10 text-dark-400" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M17 20h5v-2a3 3 0 00-5.356-1.857M17 20H7m10 0v-2c0-.656-.126-1.283-.356-1.857M7 20H2v-2a3 3 0 015.356-1.857M7 20v-2c0-.656.126-1.283.356-1.857m0 0a5.002 5.002 0 019.288 0M15 7a3 3 0 11-6 0 3 3 0 016 0zm6 3a2 2 0 11-4 0 2 2 0 014 0zM7 10a2 2 0 11-4 0 2 2 0 014 0z"/>
            </svg>
          </div>
          <h3 class="text-xl font-bold text-dark-800 mb-2">No hay fisioterapeutas disponibles</h3>
          <p class="text-dark-500">Vuelve a intentarlo más tarde</p>
        </div>
      </div>
    </section>

    <!-- Why Choose Us Section -->
    <section class="py-20 bg-white">
      <div class="max-w-4xl mx-auto px-4 sm:px-6 lg:px-8 text-center">
        <div class="bg-gradient-to-br from-secondary-50 to-primary-50 rounded-3xl p-10 lg:p-14 border border-secondary-100">
          <h2 class="font-display text-3xl lg:text-4xl font-bold text-dark-800 mb-4">
            Confianza y profesionalidad
          </h2>
          <p class="text-lg text-dark-500 mb-8 max-w-2xl mx-auto">
            Nuestro equipo está formado por fisioterapeutas colegiados con amplia experiencia en diferentes especialidades
          </p>

          <div class="grid grid-cols-1 sm:grid-cols-3 gap-6 mb-8">
            <div class="bg-white rounded-2xl p-6 shadow-soft">
              <div class="text-3xl font-bold text-secondary-600 mb-1">+10</div>
              <div class="text-dark-500 text-sm">Años de experiencia</div>
            </div>
            <div class="bg-white rounded-2xl p-6 shadow-soft">
              <div class="text-3xl font-bold text-primary-600 mb-1">+500</div>
              <div class="text-dark-500 text-sm">Pacientes satisfechos</div>
            </div>
            <div class="bg-white rounded-2xl p-6 shadow-soft">
              <div class="text-3xl font-bold text-accent-600 mb-1">4.9</div>
              <div class="text-dark-500 text-sm">Valoración media</div>
            </div>
          </div>

          <router-link
            to="/contacto"
            class="inline-flex items-center justify-center gap-2 bg-gradient-to-r from-secondary-500 to-primary-600 text-white px-8 py-4 rounded-2xl font-semibold shadow-soft hover:shadow-glow transition-all duration-300 hover:-translate-y-1"
          >
            Contactar con nosotros
            <svg class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M17 8l4 4m0 0l-4 4m4-4H3"/>
            </svg>
          </router-link>
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

const fisioterapeutas = ref([]);
const loading = ref(true);

// Genera avatar usando UI Avatars API
const getAvatarUrl = (fisio) => {
  const name = `${fisio.nombre} ${fisio.apellidos}`;
  const colors = ['0D9488', '0891B2', '7C3AED', 'DB2777', 'EA580C', '059669'];
  const colorIndex = fisio.id % colors.length;
  const bgColor = colors[colorIndex];
  return `https://ui-avatars.com/api/?name=${encodeURIComponent(name)}&size=300&background=${bgColor}&color=ffffff&bold=true&format=svg`;
};

const solicitarCita = (fisio) => {
  if (authStore.isAuthenticated && authStore.userRole === 'CLIENTE') {
    // Usuario autenticado como cliente: ir a reservar con fisioterapeuta preseleccionado
    router.push({
      path: '/cliente/reservar-cita',
      query: { fisioterapeutaId: fisio.id }
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
    fisioterapeutas.value = await publicService.getFisioterapeutas();
  } catch (error) {
    console.error("Error cargando fisioterapeutas:", error);
  } finally {
    loading.value = false;
  }
});
</script>
