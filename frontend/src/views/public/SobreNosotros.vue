<template>
  <div class="min-h-screen">
    <!-- Hero Section -->
    <section class="relative py-20 lg:py-28 overflow-hidden bg-gradient-to-br from-secondary-600 via-primary-600 to-primary-800">
      <div class="absolute inset-0 bg-hero-pattern opacity-20"></div>

      <!-- Floating Elements -->
      <div class="absolute top-20 left-10 w-20 h-20 bg-white/10 rounded-full blur-xl animate-float"></div>
      <div class="absolute bottom-20 right-20 w-32 h-32 bg-white/20 rounded-full blur-2xl animate-float" style="animation-delay: 1s;"></div>

      <div class="relative max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 text-center">
        <span class="inline-flex items-center gap-2 bg-white/10 backdrop-blur-sm px-4 py-2 rounded-full text-sm font-medium text-white mb-6">
          <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19 21V5a2 2 0 00-2-2H7a2 2 0 00-2 2v16m14 0h2m-2 0h-5m-9 0H3m2 0h5M9 7h1m-1 4h1m4-4h1m-1 4h1m-5 10v-5a1 1 0 011-1h2a1 1 0 011 1v5m-4 0h4"/>
          </svg>
          Nuestra historia
        </span>
        <h1 class="font-display text-4xl sm:text-5xl lg:text-6xl font-bold text-white mb-6">
          Sobre Nosotros
        </h1>
        <p class="text-xl text-primary-100 max-w-2xl mx-auto">
          Mas de 10 anos cuidando de la salud y el bienestar de nuestros pacientes
        </p>
      </div>

      <!-- Wave Divider -->
      <div class="absolute bottom-0 left-0 right-0">
        <svg class="w-full h-16 text-white" viewBox="0 0 1440 54" fill="currentColor" preserveAspectRatio="none">
          <path d="M0 22L60 16.7C120 11 240 1 360 0.7C480 1 600 11 720 16.7C840 22 960 22 1080 19.3C1200 16 1320 11 1380 8.3L1440 5.7V54H1380C1320 54 1200 54 1080 54C960 54 840 54 720 54C600 54 480 54 360 54C240 54 120 54 60 54H0V22Z"/>
        </svg>
      </div>
    </section>

    <!-- Historia Section -->
    <section class="py-20 bg-white">
      <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <div class="grid grid-cols-1 lg:grid-cols-2 gap-12 items-center">
          <div>
            <span class="inline-block bg-primary-100 text-primary-700 px-4 py-1.5 rounded-full text-sm font-semibold mb-4">
              Nuestra Historia
            </span>
            <h2 class="font-display text-3xl lg:text-4xl font-bold text-dark-800 mb-6">
              Un proyecto nacido de la vocacion
            </h2>
            <div class="space-y-4 text-dark-600">
              <p>
                FisioClinic nacio en 2014 con un objetivo claro: ofrecer tratamientos de fisioterapia de la mas alta calidad en un entorno cercano y profesional.
              </p>
              <p>
                Fundada por el Dr. Antonio Ruiz, la clinica comenzo como un pequeno centro en el corazon de Alicante. Su vision era crear un espacio donde cada paciente recibiera una atencion personalizada y los tratamientos mas avanzados del sector.
              </p>
              <p>
                A lo largo de los anos, hemos crecido tanto en instalaciones como en equipo, pero siempre manteniendo nuestra esencia: el compromiso con el bienestar de cada persona que cruza nuestras puertas.
              </p>
              <p>
                Hoy somos un equipo de 5 profesionales especializados en diferentes areas, con mas de 500 pacientes que confian en nosotros y una valoracion media de 4.9 sobre 5.
              </p>
            </div>
          </div>
          <div class="relative">
            <div class="aspect-square rounded-3xl overflow-hidden bg-gradient-to-br from-primary-100 to-secondary-100 flex items-center justify-center">
              <div class="text-center p-8">
                <div class="w-32 h-32 bg-gradient-to-br from-primary-500 to-secondary-500 rounded-full flex items-center justify-center mx-auto mb-6">
                  <svg class="w-16 h-16 text-white" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M4.318 6.318a4.5 4.5 0 000 6.364L12 20.364l7.682-7.682a4.5 4.5 0 00-6.364-6.364L12 7.636l-1.318-1.318a4.5 4.5 0 00-6.364 0z"/>
                  </svg>
                </div>
                <h3 class="text-2xl font-bold text-dark-800 mb-2">Desde 2014</h3>
                <p class="text-dark-500">Cuidando de ti</p>
              </div>
            </div>
            <!-- Decorative elements -->
            <div class="absolute -bottom-4 -right-4 w-24 h-24 bg-accent-500 rounded-2xl -z-10"></div>
            <div class="absolute -top-4 -left-4 w-16 h-16 bg-secondary-500 rounded-xl -z-10"></div>
          </div>
        </div>
      </div>
    </section>

    <!-- Timeline Section -->
    <section class="py-20 bg-dark-50">
      <div class="max-w-4xl mx-auto px-4 sm:px-6 lg:px-8">
        <div class="text-center mb-16">
          <span class="inline-block bg-secondary-100 text-secondary-700 px-4 py-1.5 rounded-full text-sm font-semibold mb-4">
            Nuestro recorrido
          </span>
          <h2 class="font-display text-3xl lg:text-4xl font-bold text-dark-800">
            Hitos importantes
          </h2>
        </div>

        <div class="relative">
          <!-- Timeline line -->
          <div class="absolute left-1/2 transform -translate-x-1/2 h-full w-1 bg-primary-200"></div>

          <!-- Timeline items -->
          <div class="space-y-12">
            <div v-for="(hito, index) in hitos" :key="index" class="relative">
              <div :class="[
                'flex items-center gap-8',
                index % 2 === 0 ? 'flex-row' : 'flex-row-reverse'
              ]">
                <div class="w-1/2" :class="index % 2 === 0 ? 'text-right pr-8' : 'text-left pl-8'">
                  <div class="bg-white rounded-2xl p-6 shadow-soft inline-block">
                    <span class="text-primary-600 font-bold text-lg">{{ hito.año }}</span>
                    <h3 class="text-dark-800 font-semibold mt-1">{{ hito.titulo }}</h3>
                    <p class="text-dark-500 text-sm mt-2">{{ hito.descripcion }}</p>
                  </div>
                </div>
                <div class="absolute left-1/2 transform -translate-x-1/2 w-4 h-4 bg-primary-500 rounded-full border-4 border-white shadow-soft"></div>
                <div class="w-1/2"></div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </section>

    <!-- Valores Section -->
    <section class="py-20 bg-white">
      <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <div class="text-center mb-16">
          <span class="inline-block bg-accent-100 text-accent-700 px-4 py-1.5 rounded-full text-sm font-semibold mb-4">
            Nuestra filosofia
          </span>
          <h2 class="font-display text-3xl lg:text-4xl font-bold text-dark-800 mb-4">
            Valores que nos definen
          </h2>
          <p class="text-lg text-dark-500 max-w-2xl mx-auto">
            Principios fundamentales que guian cada decision y tratamiento
          </p>
        </div>

        <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-8">
          <div
            v-for="(valor, index) in valores"
            :key="index"
            class="bg-white rounded-3xl p-8 border border-dark-100 shadow-soft hover:shadow-soft-lg transition-all duration-300 hover:-translate-y-2 text-center"
          >
            <div :class="[
              'w-16 h-16 rounded-2xl flex items-center justify-center mx-auto mb-6',
              valor.bgColor
            ]">
              <component :is="valor.icon" :class="['w-8 h-8', valor.iconColor]" />
            </div>
            <h3 class="text-xl font-bold text-dark-800 mb-3">{{ valor.titulo }}</h3>
            <p class="text-dark-500">{{ valor.descripcion }}</p>
          </div>
        </div>
      </div>
    </section>

    <!-- Instalaciones Section -->
    <section class="py-20 bg-gradient-to-b from-dark-50 to-white">
      <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <div class="text-center mb-16">
          <span class="inline-block bg-primary-100 text-primary-700 px-4 py-1.5 rounded-full text-sm font-semibold mb-4">
            Nuestro espacio
          </span>
          <h2 class="font-display text-3xl lg:text-4xl font-bold text-dark-800 mb-4">
            Instalaciones modernas
          </h2>
          <p class="text-lg text-dark-500 max-w-2xl mx-auto">
            Espacios disenados para tu comodidad y recuperacion
          </p>
        </div>

        <div class="grid grid-cols-2 md:grid-cols-4 gap-4">
          <div
            v-for="(instalacion, index) in instalaciones"
            :key="index"
            :class="[
              'rounded-2xl overflow-hidden bg-gradient-to-br shadow-soft group cursor-pointer',
              instalacion.size === 'large' ? 'col-span-2 row-span-2' : '',
              instalacion.gradient
            ]"
          >
            <div class="h-full p-6 flex flex-col justify-end" :class="instalacion.size === 'large' ? 'min-h-[300px]' : 'min-h-[150px]'">
              <div class="bg-white/90 backdrop-blur-sm rounded-xl p-4 transform group-hover:scale-105 transition-transform">
                <h3 class="font-semibold text-dark-800">{{ instalacion.nombre }}</h3>
                <p class="text-sm text-dark-500 mt-1">{{ instalacion.descripcion }}</p>
              </div>
            </div>
          </div>
        </div>
      </div>
    </section>

    <!-- CTA Section -->
    <section class="py-16 bg-white">
      <div class="max-w-4xl mx-auto px-4 sm:px-6 lg:px-8 text-center">
        <div class="bg-gradient-to-br from-primary-600 to-secondary-600 rounded-3xl p-10 lg:p-14 text-white relative overflow-hidden">
          <div class="absolute top-0 right-0 w-40 h-40 bg-white/10 rounded-full -translate-y-1/2 translate-x-1/2"></div>
          <div class="absolute bottom-0 left-0 w-32 h-32 bg-white/10 rounded-full translate-y-1/2 -translate-x-1/2"></div>

          <div class="relative z-10">
            <h2 class="font-display text-3xl lg:text-4xl font-bold mb-4">
              Ven a conocernos
            </h2>
            <p class="text-lg text-primary-100 mb-8 max-w-xl mx-auto">
              Reserva tu primera consulta y descubre por que mas de 500 pacientes confian en nosotros
            </p>
            <div class="flex flex-col sm:flex-row gap-4 justify-center">
              <router-link
                to="/register"
                class="inline-flex items-center justify-center gap-2 bg-white text-primary-700 px-8 py-4 rounded-2xl font-semibold hover:bg-primary-50 transition-all duration-300"
              >
                Reservar Cita
                <svg class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M17 8l4 4m0 0l-4 4m4-4H3"/>
                </svg>
              </router-link>
              <router-link
                to="/equipo"
                class="inline-flex items-center justify-center gap-2 bg-white/10 backdrop-blur-sm border border-white/20 text-white px-8 py-4 rounded-2xl font-semibold hover:bg-white/20 transition-all duration-300"
              >
                Conocer al equipo
              </router-link>
            </div>
          </div>
        </div>
      </div>
    </section>
  </div>
</template>

<script setup>
import { ref, h } from 'vue';

const hitos = ref([
  {
    año: '2014',
    titulo: 'Fundacion de FisioClinic',
    descripcion: 'Apertura de nuestra primera clinica en el centro de Alicante'
  },
  {
    año: '2016',
    titulo: 'Ampliacion del equipo',
    descripcion: 'Incorporacion de especialistas en osteopatia y fisioterapia deportiva'
  },
  {
    año: '2018',
    titulo: 'Nuevas instalaciones',
    descripcion: 'Traslado a instalaciones mas amplias con equipamiento de ultima generacion'
  },
  {
    año: '2020',
    titulo: 'Digitalizacion',
    descripcion: 'Lanzamiento del sistema de reservas online y telemedicina'
  },
  {
    año: '2023',
    titulo: '+500 pacientes',
    descripcion: 'Superamos los 500 pacientes satisfechos con valoracion media de 4.9'
  }
]);

// Icon components
const HeartIcon = {
  render() {
    return h('svg', { fill: 'none', stroke: 'currentColor', viewBox: '0 0 24 24' }, [
      h('path', { 'stroke-linecap': 'round', 'stroke-linejoin': 'round', 'stroke-width': '2', d: 'M4.318 6.318a4.5 4.5 0 000 6.364L12 20.364l7.682-7.682a4.5 4.5 0 00-6.364-6.364L12 7.636l-1.318-1.318a4.5 4.5 0 00-6.364 0z' })
    ])
  }
};

const StarIcon = {
  render() {
    return h('svg', { fill: 'none', stroke: 'currentColor', viewBox: '0 0 24 24' }, [
      h('path', { 'stroke-linecap': 'round', 'stroke-linejoin': 'round', 'stroke-width': '2', d: 'M11.049 2.927c.3-.921 1.603-.921 1.902 0l1.519 4.674a1 1 0 00.95.69h4.915c.969 0 1.371 1.24.588 1.81l-3.976 2.888a1 1 0 00-.363 1.118l1.518 4.674c.3.922-.755 1.688-1.538 1.118l-3.976-2.888a1 1 0 00-1.176 0l-3.976 2.888c-.783.57-1.838-.197-1.538-1.118l1.518-4.674a1 1 0 00-.363-1.118l-3.976-2.888c-.784-.57-.38-1.81.588-1.81h4.914a1 1 0 00.951-.69l1.519-4.674z' })
    ])
  }
};

const ShieldIcon = {
  render() {
    return h('svg', { fill: 'none', stroke: 'currentColor', viewBox: '0 0 24 24' }, [
      h('path', { 'stroke-linecap': 'round', 'stroke-linejoin': 'round', 'stroke-width': '2', d: 'M9 12l2 2 4-4m5.618-4.016A11.955 11.955 0 0112 2.944a11.955 11.955 0 01-8.618 3.04A12.02 12.02 0 003 9c0 5.591 3.824 10.29 9 11.622 5.176-1.332 9-6.03 9-11.622 0-1.042-.133-2.052-.382-3.016z' })
    ])
  }
};

const LightningIcon = {
  render() {
    return h('svg', { fill: 'none', stroke: 'currentColor', viewBox: '0 0 24 24' }, [
      h('path', { 'stroke-linecap': 'round', 'stroke-linejoin': 'round', 'stroke-width': '2', d: 'M13 10V3L4 14h7v7l9-11h-7z' })
    ])
  }
};

const valores = ref([
  {
    titulo: 'Cercania',
    descripcion: 'Trato humano y personalizado. Cada paciente es unico y merece atencion individualizada.',
    icon: HeartIcon,
    bgColor: 'bg-primary-100',
    iconColor: 'text-primary-600'
  },
  {
    titulo: 'Excelencia',
    descripcion: 'Formacion continua y tecnicas de vanguardia para ofrecer los mejores resultados.',
    icon: StarIcon,
    bgColor: 'bg-accent-100',
    iconColor: 'text-accent-600'
  },
  {
    titulo: 'Confianza',
    descripcion: 'Transparencia total en diagnosticos, tratamientos y tarifas.',
    icon: ShieldIcon,
    bgColor: 'bg-secondary-100',
    iconColor: 'text-secondary-600'
  },
  {
    titulo: 'Eficacia',
    descripcion: 'Tratamientos basados en evidencia cientifica para resultados demostrables.',
    icon: LightningIcon,
    bgColor: 'bg-green-100',
    iconColor: 'text-green-600'
  }
]);

const instalaciones = ref([
  {
    nombre: 'Sala de tratamientos',
    descripcion: '4 cabinas individuales totalmente equipadas',
    gradient: 'from-primary-400 to-primary-600',
    size: 'large'
  },
  {
    nombre: 'Recepcion',
    descripcion: 'Zona de espera confortable',
    gradient: 'from-secondary-400 to-secondary-600',
    size: 'small'
  },
  {
    nombre: 'Gimnasio',
    descripcion: 'Equipamiento de rehabilitacion',
    gradient: 'from-accent-400 to-accent-600',
    size: 'small'
  },
  {
    nombre: 'Sala de electroterapia',
    descripcion: 'Tecnologia de ultima generacion',
    gradient: 'from-green-400 to-green-600',
    size: 'small'
  },
  {
    nombre: 'Consulta medica',
    descripcion: 'Valoracion y seguimiento',
    gradient: 'from-purple-400 to-purple-600',
    size: 'small'
  }
]);
</script>
