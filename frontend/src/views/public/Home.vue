<template>
  <div class="min-h-screen">
    <!-- Hero Section -->
    <section class="relative overflow-hidden bg-gradient-to-br from-primary-600 via-primary-700 to-primary-900 text-white">
      <!-- Background Pattern -->
      <div class="absolute inset-0 bg-hero-pattern opacity-30"></div>
      <div class="absolute top-0 right-0 w-1/2 h-full bg-gradient-to-l from-primary-500/20 to-transparent"></div>

      <!-- Floating Elements -->
      <div class="absolute top-20 left-10 w-20 h-20 bg-white/10 rounded-full blur-xl animate-float"></div>
      <div class="absolute bottom-20 right-20 w-32 h-32 bg-accent-400/20 rounded-full blur-2xl animate-float" style="animation-delay: 1s;"></div>
      <div class="absolute top-1/2 left-1/4 w-16 h-16 bg-secondary-400/10 rounded-full blur-xl animate-float" style="animation-delay: 2s;"></div>

      <div class="relative max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-24 lg:py-32">
        <div class="grid lg:grid-cols-2 gap-12 items-center">
          <!-- Text Content -->
          <div class="text-center lg:text-left animate-fade-in-up">
            <span class="inline-flex items-center gap-2 bg-white/10 backdrop-blur-sm px-4 py-2 rounded-full text-sm font-medium mb-6">
              <span class="w-2 h-2 bg-accent-400 rounded-full animate-pulse"></span>
              Cuidamos de tu bienestar
            </span>
            <h1 class="font-display text-4xl sm:text-5xl lg:text-6xl font-bold mb-6 leading-tight">
              Tu salud en las
              <span class="text-transparent bg-clip-text bg-gradient-to-r from-accent-300 to-accent-400">mejores manos</span>
            </h1>
            <p class="text-lg sm:text-xl text-primary-100 mb-8 max-w-xl mx-auto lg:mx-0">
              Recupera tu movilidad y bienestar con nuestros tratamientos especializados de fisioterapia. Profesionales certificados a tu servicio.
            </p>
            <div class="flex flex-col sm:flex-row gap-4 justify-center lg:justify-start">
              <router-link
                to="/register"
                class="group inline-flex items-center justify-center gap-2 bg-white text-primary-700 px-8 py-4 rounded-2xl font-semibold shadow-soft-xl hover:shadow-glow transition-all duration-300 hover:-translate-y-1"
              >
                Reservar Cita
                <svg class="w-5 h-5 group-hover:translate-x-1 transition-transform" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M17 8l4 4m0 0l-4 4m4-4H3"/>
                </svg>
              </router-link>
              <router-link
                to="/servicios"
                class="inline-flex items-center justify-center gap-2 bg-white/10 backdrop-blur-sm border border-white/20 text-white px-8 py-4 rounded-2xl font-semibold hover:bg-white/20 transition-all duration-300"
              >
                Ver Servicios
              </router-link>
            </div>
          </div>

          <!-- Hero Image/Illustration -->
          <div class="hidden lg:block relative animate-fade-in" style="animation-delay: 0.3s;">
            <div class="relative">
              <!-- Stats Cards -->
              <div class="absolute -left-8 top-1/4 bg-white rounded-2xl shadow-soft-xl p-4 animate-float">
                <div class="flex items-center gap-3">
                  <div class="w-12 h-12 bg-primary-100 rounded-xl flex items-center justify-center">
                    <svg class="w-6 h-6 text-primary-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                      <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 12l2 2 4-4m6 2a9 9 0 11-18 0 9 9 0 0118 0z"/>
                    </svg>
                  </div>
                  <div>
                    <p class="text-2xl font-bold text-dark-800">+500</p>
                    <p class="text-sm text-dark-500">Pacientes felices</p>
                  </div>
                </div>
              </div>

              <div class="absolute -right-4 bottom-1/4 bg-white rounded-2xl shadow-soft-xl p-4 animate-float" style="animation-delay: 0.5s;">
                <div class="flex items-center gap-3">
                  <div class="w-12 h-12 bg-accent-100 rounded-xl flex items-center justify-center">
                    <svg class="w-6 h-6 text-accent-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                      <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M11.049 2.927c.3-.921 1.603-.921 1.902 0l1.519 4.674a1 1 0 00.95.69h4.915c.969 0 1.371 1.24.588 1.81l-3.976 2.888a1 1 0 00-.363 1.118l1.518 4.674c.3.922-.755 1.688-1.538 1.118l-3.976-2.888a1 1 0 00-1.176 0l-3.976 2.888c-.783.57-1.838-.197-1.538-1.118l1.518-4.674a1 1 0 00-.363-1.118l-3.976-2.888c-.784-.57-.38-1.81.588-1.81h4.914a1 1 0 00.951-.69l1.519-4.674z"/>
                    </svg>
                  </div>
                  <div>
                    <p class="text-2xl font-bold text-dark-800">4.9</p>
                    <p class="text-sm text-dark-500">Valoración media</p>
                  </div>
                </div>
              </div>

              <!-- Main Card -->
              <div class="bg-white/10 backdrop-blur-md rounded-3xl p-8 border border-white/20">
                <div class="aspect-square rounded-2xl bg-gradient-to-br from-white/20 to-white/5 flex items-center justify-center">
                  <svg class="w-32 h-32 text-white/80" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="1.5" d="M4.318 6.318a4.5 4.5 0 000 6.364L12 20.364l7.682-7.682a4.5 4.5 0 00-6.364-6.364L12 7.636l-1.318-1.318a4.5 4.5 0 00-6.364 0z"/>
                  </svg>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>

      <!-- Wave Divider -->
      <div class="absolute bottom-0 left-0 right-0">
        <svg class="w-full h-16 text-white" viewBox="0 0 1440 54" fill="currentColor" preserveAspectRatio="none">
          <path d="M0 22L60 16.7C120 11 240 1 360 0.7C480 1 600 11 720 16.7C840 22 960 22 1080 19.3C1200 16 1320 11 1380 8.3L1440 5.7V54H1380C1320 54 1200 54 1080 54C960 54 840 54 720 54C600 54 480 54 360 54C240 54 120 54 60 54H0V22Z"/>
        </svg>
      </div>
    </section>

    <!-- Servicios Destacados -->
    <section class="py-20 lg:py-28 bg-white">
      <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <div class="text-center mb-16">
          <span class="inline-block bg-primary-100 text-primary-700 px-4 py-1.5 rounded-full text-sm font-semibold mb-4">
            Nuestros Servicios
          </span>
          <h2 class="font-display text-3xl sm:text-4xl lg:text-5xl font-bold text-dark-800 mb-4">
            Tratamientos especializados
          </h2>
          <p class="text-lg text-dark-500 max-w-2xl mx-auto">
            Ofrecemos una amplia gama de servicios de fisioterapia para ayudarte a recuperar tu bienestar
          </p>
        </div>

        <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-8">
          <div
            v-for="(servicio, index) in serviciosDestacados"
            :key="servicio.id"
            class="group bg-white rounded-3xl p-8 border border-dark-100 hover:border-primary-200 shadow-soft hover:shadow-soft-lg transition-all duration-300 hover:-translate-y-2"
            :style="{ animationDelay: `${index * 0.1}s` }"
          >
            <div class="w-16 h-16 bg-gradient-to-br from-primary-500 to-primary-600 rounded-2xl flex items-center justify-center mb-6 group-hover:scale-110 transition-transform duration-300">
              <svg class="w-8 h-8 text-white" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M4.318 6.318a4.5 4.5 0 000 6.364L12 20.364l7.682-7.682a4.5 4.5 0 00-6.364-6.364L12 7.636l-1.318-1.318a4.5 4.5 0 00-6.364 0z"/>
              </svg>
            </div>
            <h3 class="text-xl font-bold text-dark-800 mb-3 group-hover:text-primary-600 transition-colors">
              {{ servicio.nombre }}
            </h3>
            <p class="text-dark-500 mb-6 line-clamp-2">
              {{ servicio.descripcion }}
            </p>
            <div class="flex items-center justify-between pt-6 border-t border-dark-100">
              <div>
                <span class="text-3xl font-bold text-primary-600">{{ servicio.precio }}€</span>
              </div>
              <span class="inline-flex items-center gap-1 text-sm text-dark-400 bg-dark-50 px-3 py-1.5 rounded-full">
                <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 8v4l3 3m6-3a9 9 0 11-18 0 9 9 0 0118 0z"/>
                </svg>
                {{ servicio.duracionMinutos }} min
              </span>
            </div>
          </div>
        </div>

        <div class="text-center mt-12">
          <router-link
            to="/servicios"
            class="inline-flex items-center gap-2 text-primary-600 hover:text-primary-700 font-semibold group"
          >
            Ver todos los servicios
            <svg class="w-5 h-5 group-hover:translate-x-1 transition-transform" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M17 8l4 4m0 0l-4 4m4-4H3"/>
            </svg>
          </router-link>
        </div>
      </div>
    </section>

    <!-- Por qué elegirnos -->
    <section class="py-20 lg:py-28 bg-gradient-to-b from-dark-50 to-white">
      <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <div class="text-center mb-16">
          <span class="inline-block bg-secondary-100 text-secondary-700 px-4 py-1.5 rounded-full text-sm font-semibold mb-4">
            Por qué elegirnos
          </span>
          <h2 class="font-display text-3xl sm:text-4xl lg:text-5xl font-bold text-dark-800 mb-4">
            Excelencia en cada tratamiento
          </h2>
          <p class="text-lg text-dark-500 max-w-2xl mx-auto">
            Nos comprometemos con tu salud y bienestar ofreciendo el mejor servicio
          </p>
        </div>

        <div class="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-8">
          <div class="group text-center p-8 rounded-3xl bg-white border border-dark-100 hover:border-primary-200 shadow-soft hover:shadow-soft-lg transition-all duration-300">
            <div class="w-20 h-20 bg-gradient-to-br from-primary-100 to-primary-200 rounded-3xl flex items-center justify-center mx-auto mb-6 group-hover:scale-110 transition-transform duration-300">
              <svg class="w-10 h-10 text-primary-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M16 7a4 4 0 11-8 0 4 4 0 018 0zM12 14a7 7 0 00-7 7h14a7 7 0 00-7-7z"/>
              </svg>
            </div>
            <h3 class="font-bold text-lg text-dark-800 mb-2">Profesionales Cualificados</h3>
            <p class="text-dark-500">Fisioterapeutas certificados con años de experiencia</p>
          </div>

          <div class="group text-center p-8 rounded-3xl bg-white border border-dark-100 hover:border-secondary-200 shadow-soft hover:shadow-soft-lg transition-all duration-300">
            <div class="w-20 h-20 bg-gradient-to-br from-secondary-100 to-secondary-200 rounded-3xl flex items-center justify-center mx-auto mb-6 group-hover:scale-110 transition-transform duration-300">
              <svg class="w-10 h-10 text-secondary-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19 21V5a2 2 0 00-2-2H7a2 2 0 00-2 2v16m14 0h2m-2 0h-5m-9 0H3m2 0h5M9 7h1m-1 4h1m4-4h1m-1 4h1m-5 10v-5a1 1 0 011-1h2a1 1 0 011 1v5m-4 0h4"/>
              </svg>
            </div>
            <h3 class="font-bold text-lg text-dark-800 mb-2">Instalaciones Modernas</h3>
            <p class="text-dark-500">Equipamiento de última generación</p>
          </div>

          <div class="group text-center p-8 rounded-3xl bg-white border border-dark-100 hover:border-accent-200 shadow-soft hover:shadow-soft-lg transition-all duration-300">
            <div class="w-20 h-20 bg-gradient-to-br from-accent-100 to-accent-200 rounded-3xl flex items-center justify-center mx-auto mb-6 group-hover:scale-110 transition-transform duration-300">
              <svg class="w-10 h-10 text-accent-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M8 7V3m8 4V3m-9 8h10M5 21h14a2 2 0 002-2V7a2 2 0 00-2-2H5a2 2 0 00-2 2v12a2 2 0 002 2z"/>
              </svg>
            </div>
            <h3 class="font-bold text-lg text-dark-800 mb-2">Horarios Flexibles</h3>
            <p class="text-dark-500">De lunes a sábado para tu comodidad</p>
          </div>

          <div class="group text-center p-8 rounded-3xl bg-white border border-dark-100 hover:border-primary-200 shadow-soft hover:shadow-soft-lg transition-all duration-300">
            <div class="w-20 h-20 bg-gradient-to-br from-primary-100 to-secondary-100 rounded-3xl flex items-center justify-center mx-auto mb-6 group-hover:scale-110 transition-transform duration-300">
              <svg class="w-10 h-10 text-primary-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M11.049 2.927c.3-.921 1.603-.921 1.902 0l1.519 4.674a1 1 0 00.95.69h4.915c.969 0 1.371 1.24.588 1.81l-3.976 2.888a1 1 0 00-.363 1.118l1.518 4.674c.3.922-.755 1.688-1.538 1.118l-3.976-2.888a1 1 0 00-1.176 0l-3.976 2.888c-.783.57-1.838-.197-1.538-1.118l1.518-4.674a1 1 0 00-.363-1.118l-3.976-2.888c-.784-.57-.38-1.81.588-1.81h4.914a1 1 0 00.951-.69l1.519-4.674z"/>
              </svg>
            </div>
            <h3 class="font-bold text-lg text-dark-800 mb-2">Atención Personalizada</h3>
            <p class="text-dark-500">Tratamientos adaptados a tus necesidades</p>
          </div>
        </div>
      </div>
    </section>

    <!-- CTA Final -->
    <section class="relative py-20 lg:py-28 overflow-hidden">
      <div class="absolute inset-0 bg-gradient-to-br from-primary-600 via-primary-700 to-secondary-700"></div>
      <div class="absolute inset-0 bg-hero-pattern opacity-20"></div>

      <!-- Floating Elements -->
      <div class="absolute top-10 left-10 w-24 h-24 bg-white/10 rounded-full blur-2xl"></div>
      <div class="absolute bottom-10 right-10 w-32 h-32 bg-accent-400/20 rounded-full blur-2xl"></div>

      <div class="relative max-w-4xl mx-auto px-4 sm:px-6 lg:px-8 text-center">
        <span class="inline-flex items-center gap-2 bg-white/10 backdrop-blur-sm px-4 py-2 rounded-full text-sm font-medium text-white mb-6">
          <span class="w-2 h-2 bg-accent-400 rounded-full animate-pulse"></span>
          Comienza tu recuperación hoy
        </span>
        <h2 class="font-display text-3xl sm:text-4xl lg:text-5xl font-bold text-white mb-6">
          ¿Listo para mejorar tu bienestar?
        </h2>
        <p class="text-xl text-primary-100 mb-10 max-w-2xl mx-auto">
          Regístrate ahora y agenda tu primera cita con nuestros especialistas. Tu salud no puede esperar.
        </p>
        <div class="flex flex-col sm:flex-row gap-4 justify-center">
          <router-link
            to="/register"
            class="group inline-flex items-center justify-center gap-2 bg-white text-primary-700 px-8 py-4 rounded-2xl font-semibold shadow-soft-xl hover:shadow-glow transition-all duration-300 hover:-translate-y-1"
          >
            Crear Cuenta Gratis
            <svg class="w-5 h-5 group-hover:translate-x-1 transition-transform" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M17 8l4 4m0 0l-4 4m4-4H3"/>
            </svg>
          </router-link>
          <router-link
            to="/contacto"
            class="inline-flex items-center justify-center gap-2 bg-white/10 backdrop-blur-sm border border-white/20 text-white px-8 py-4 rounded-2xl font-semibold hover:bg-white/20 transition-all duration-300"
          >
            Contactar
          </router-link>
        </div>
      </div>
    </section>
  </div>
</template>

<script setup>
import { ref, onMounted } from "vue";
import publicService from "../../services/publicService";

const serviciosDestacados = ref([]);

onMounted(async () => {
  try {
    const servicios = await publicService.getServicios();
    serviciosDestacados.value = servicios.slice(0, 3);
  } catch (error) {
    console.error("Error cargando servicios:", error);
  }
});
</script>
