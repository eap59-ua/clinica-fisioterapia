<template>
  <div class="min-h-screen">
    <!-- Hero Section -->
    <section class="relative py-20 lg:py-28 overflow-hidden bg-gradient-to-br from-accent-500 via-accent-600 to-primary-700">
      <div class="absolute inset-0 bg-hero-pattern opacity-20"></div>
      <div class="absolute top-0 right-0 w-1/3 h-full bg-gradient-to-l from-primary-500/20 to-transparent"></div>

      <!-- Floating Elements -->
      <div class="absolute top-20 left-10 w-20 h-20 bg-white/10 rounded-full blur-xl animate-float"></div>
      <div class="absolute bottom-20 right-20 w-32 h-32 bg-white/20 rounded-full blur-2xl animate-float" style="animation-delay: 1s;"></div>

      <div class="relative max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 text-center">
        <span class="inline-flex items-center gap-2 bg-white/10 backdrop-blur-sm px-4 py-2 rounded-full text-sm font-medium text-white mb-6">
          <span class="w-2 h-2 bg-white rounded-full animate-pulse"></span>
          Lo que dicen nuestros pacientes
        </span>
        <h1 class="font-display text-4xl sm:text-5xl lg:text-6xl font-bold text-white mb-6">
          Testimonios
        </h1>
        <p class="text-xl text-accent-100 max-w-2xl mx-auto">
          Historias reales de pacientes que han recuperado su bienestar con nosotros
        </p>
      </div>

      <!-- Wave Divider -->
      <div class="absolute bottom-0 left-0 right-0">
        <svg class="w-full h-16 text-dark-50" viewBox="0 0 1440 54" fill="currentColor" preserveAspectRatio="none">
          <path d="M0 22L60 16.7C120 11 240 1 360 0.7C480 1 600 11 720 16.7C840 22 960 22 1080 19.3C1200 16 1320 11 1380 8.3L1440 5.7V54H1380C1320 54 1200 54 1080 54C960 54 840 54 720 54C600 54 480 54 360 54C240 54 120 54 60 54H0V22Z"/>
        </svg>
      </div>
    </section>

    <!-- Stats Section -->
    <section class="py-12 bg-dark-50">
      <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <div class="grid grid-cols-2 md:grid-cols-4 gap-6">
          <div class="bg-white rounded-2xl p-6 text-center shadow-soft">
            <div class="text-4xl font-bold text-primary-600 mb-1">+500</div>
            <div class="text-dark-500">Pacientes atendidos</div>
          </div>
          <div class="bg-white rounded-2xl p-6 text-center shadow-soft">
            <div class="text-4xl font-bold text-secondary-600 mb-1">4.9</div>
            <div class="text-dark-500">Valoracion media</div>
          </div>
          <div class="bg-white rounded-2xl p-6 text-center shadow-soft">
            <div class="text-4xl font-bold text-accent-600 mb-1">98%</div>
            <div class="text-dark-500">Recomendarian</div>
          </div>
          <div class="bg-white rounded-2xl p-6 text-center shadow-soft">
            <div class="text-4xl font-bold text-primary-600 mb-1">+10</div>
            <div class="text-dark-500">Anos de experiencia</div>
          </div>
        </div>
      </div>
    </section>

    <!-- Featured Testimonial -->
    <section class="py-16 bg-dark-50">
      <div class="max-w-4xl mx-auto px-4 sm:px-6 lg:px-8">
        <div class="bg-gradient-to-br from-primary-600 to-secondary-600 rounded-3xl p-8 lg:p-12 text-white relative overflow-hidden">
          <div class="absolute top-0 right-0 w-40 h-40 bg-white/10 rounded-full -translate-y-1/2 translate-x-1/2"></div>
          <svg class="w-16 h-16 text-white/20 mb-6" fill="currentColor" viewBox="0 0 24 24">
            <path d="M14.017 21v-7.391c0-5.704 3.731-9.57 8.983-10.609l.995 2.151c-2.432.917-3.995 3.638-3.995 5.849h4v10h-9.983zm-14.017 0v-7.391c0-5.704 3.748-9.57 9-10.609l.996 2.151c-2.433.917-3.996 3.638-3.996 5.849h3.983v10h-9.983z"/>
          </svg>
          <blockquote class="text-xl lg:text-2xl font-medium mb-6 relative z-10">
            "Despues de anos sufriendo dolor de espalda cronico, encontre en esta clinica la solucion que tanto buscaba. El equipo es excepcional y el trato humano incomparable. Hoy puedo decir que he recuperado mi calidad de vida."
          </blockquote>
          <div class="flex items-center gap-4">
            <img
              src="https://ui-avatars.com/api/?name=Maria+Garcia&size=60&background=ffffff&color=0D9488&bold=true"
              alt="Maria Garcia"
              class="w-14 h-14 rounded-full border-2 border-white/30"
            />
            <div>
              <div class="font-bold text-lg">Maria Garcia Lopez</div>
              <div class="text-primary-200">Paciente desde 2022</div>
            </div>
          </div>
        </div>
      </div>
    </section>

    <!-- Testimonials Grid -->
    <section class="py-16 bg-white">
      <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <div class="text-center mb-12">
          <h2 class="font-display text-3xl lg:text-4xl font-bold text-dark-800 mb-4">
            Mas historias de exito
          </h2>
          <p class="text-lg text-dark-500 max-w-2xl mx-auto">
            Cada paciente tiene una historia unica. Estas son algunas de ellas.
          </p>
        </div>

        <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-8">
          <div
            v-for="(testimonio, index) in testimonios"
            :key="index"
            class="bg-white rounded-3xl p-6 border border-dark-100 shadow-soft hover:shadow-soft-lg transition-all duration-300 hover:-translate-y-1"
          >
            <!-- Rating -->
            <div class="flex gap-1 mb-4">
              <svg v-for="star in 5" :key="star" class="w-5 h-5 text-accent-500" fill="currentColor" viewBox="0 0 20 20">
                <path d="M9.049 2.927c.3-.921 1.603-.921 1.902 0l1.07 3.292a1 1 0 00.95.69h3.462c.969 0 1.371 1.24.588 1.81l-2.8 2.034a1 1 0 00-.364 1.118l1.07 3.292c.3.921-.755 1.688-1.54 1.118l-2.8-2.034a1 1 0 00-1.175 0l-2.8 2.034c-.784.57-1.838-.197-1.539-1.118l1.07-3.292a1 1 0 00-.364-1.118L2.98 8.72c-.783-.57-.38-1.81.588-1.81h3.461a1 1 0 00.951-.69l1.07-3.292z"/>
              </svg>
            </div>

            <!-- Quote -->
            <p class="text-dark-600 mb-6 line-clamp-4">
              "{{ testimonio.texto }}"
            </p>

            <!-- Author -->
            <div class="flex items-center gap-3 pt-4 border-t border-dark-100">
              <img
                :src="testimonio.avatar"
                :alt="testimonio.nombre"
                class="w-12 h-12 rounded-full"
              />
              <div>
                <div class="font-semibold text-dark-800">{{ testimonio.nombre }}</div>
                <div class="text-sm text-dark-400">{{ testimonio.tratamiento }}</div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </section>

    <!-- Video Testimonials Placeholder -->
    <section class="py-16 bg-gradient-to-b from-dark-50 to-white">
      <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <div class="text-center mb-12">
          <span class="inline-block bg-secondary-100 text-secondary-700 px-4 py-1.5 rounded-full text-sm font-semibold mb-4">
            Proximamente
          </span>
          <h2 class="font-display text-3xl lg:text-4xl font-bold text-dark-800 mb-4">
            Testimonios en video
          </h2>
          <p class="text-lg text-dark-500 max-w-2xl mx-auto">
            Pronto podras ver testimonios en video de nuestros pacientes
          </p>
        </div>

        <div class="grid grid-cols-1 md:grid-cols-3 gap-6">
          <div v-for="i in 3" :key="i" class="bg-dark-100 rounded-2xl aspect-video flex items-center justify-center">
            <div class="text-center">
              <div class="w-16 h-16 bg-dark-200 rounded-full flex items-center justify-center mx-auto mb-3">
                <svg class="w-8 h-8 text-dark-400" fill="currentColor" viewBox="0 0 24 24">
                  <path d="M8 5v14l11-7z"/>
                </svg>
              </div>
              <p class="text-dark-400 text-sm">Video {{ i }}</p>
            </div>
          </div>
        </div>
      </div>
    </section>

    <!-- CTA Section -->
    <section class="py-16 bg-white">
      <div class="max-w-4xl mx-auto px-4 sm:px-6 lg:px-8 text-center">
        <div class="bg-gradient-to-br from-primary-50 to-secondary-50 rounded-3xl p-10 lg:p-14 border border-primary-100">
          <h2 class="font-display text-3xl lg:text-4xl font-bold text-dark-800 mb-4">
            Se parte de nuestras historias de exito
          </h2>
          <p class="text-lg text-dark-500 mb-8 max-w-2xl mx-auto">
            Reserva tu primera cita y comienza tu camino hacia el bienestar
          </p>
          <router-link
            to="/register"
            class="inline-flex items-center justify-center gap-2 bg-gradient-to-r from-primary-500 to-primary-600 text-white px-8 py-4 rounded-2xl font-semibold shadow-soft hover:shadow-glow transition-all duration-300 hover:-translate-y-1"
          >
            Reservar Primera Cita
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
import { ref } from 'vue';

const testimonios = ref([
  {
    nombre: 'Carlos Rodriguez',
    tratamiento: 'Fisioterapia deportiva',
    texto: 'Tras una lesion de rodilla jugando al futbol, pense que no volveria a correr. Gracias al tratamiento personalizado, en 3 meses estaba de vuelta en el campo.',
    avatar: 'https://ui-avatars.com/api/?name=Carlos+Rodriguez&size=60&background=0D9488&color=ffffff&bold=true'
  },
  {
    nombre: 'Ana Martinez',
    tratamiento: 'Rehabilitacion postquirurgica',
    texto: 'Despues de mi operacion de hombro, el equipo me acompano en todo el proceso de recuperacion. Su profesionalidad y cercania marcaron la diferencia.',
    avatar: 'https://ui-avatars.com/api/?name=Ana+Martinez&size=60&background=0891B2&color=ffffff&bold=true'
  },
  {
    nombre: 'Pedro Sanchez',
    tratamiento: 'Dolor cronico de espalda',
    texto: 'Llevaba anos con dolor lumbar. Tras varias sesiones de terapia manual, he notado una mejoria increible. Por fin puedo dormir bien.',
    avatar: 'https://ui-avatars.com/api/?name=Pedro+Sanchez&size=60&background=7C3AED&color=ffffff&bold=true'
  },
  {
    nombre: 'Laura Fernandez',
    tratamiento: 'Fisioterapia prenatal',
    texto: 'Durante mi embarazo, las sesiones me ayudaron muchisimo con los dolores de espalda y la preparacion al parto. Totalmente recomendable.',
    avatar: 'https://ui-avatars.com/api/?name=Laura+Fernandez&size=60&background=DB2777&color=ffffff&bold=true'
  },
  {
    nombre: 'Miguel Torres',
    tratamiento: 'Cervicalgia',
    texto: 'El estres del trabajo me provocaba dolores de cuello terribles. Con las sesiones de fisioterapia y los ejercicios para casa, he mejorado muchisimo.',
    avatar: 'https://ui-avatars.com/api/?name=Miguel+Torres&size=60&background=EA580C&color=ffffff&bold=true'
  },
  {
    nombre: 'Sofia Lopez',
    tratamiento: 'Drenaje linfatico',
    texto: 'Despues de mi mastectomia, el drenaje linfatico ha sido fundamental en mi recuperacion. El trato es exquisito y muy profesional.',
    avatar: 'https://ui-avatars.com/api/?name=Sofia+Lopez&size=60&background=059669&color=ffffff&bold=true'
  }
]);
</script>
