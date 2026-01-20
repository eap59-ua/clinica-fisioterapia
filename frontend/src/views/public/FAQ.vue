<template>
  <div class="min-h-screen">
    <!-- Hero Section -->
    <section class="relative py-20 lg:py-28 overflow-hidden bg-gradient-to-br from-primary-600 via-primary-700 to-secondary-700">
      <div class="absolute inset-0 bg-hero-pattern opacity-20"></div>

      <!-- Floating Elements -->
      <div class="absolute top-20 left-10 w-20 h-20 bg-white/10 rounded-full blur-xl animate-float"></div>
      <div class="absolute bottom-20 right-20 w-32 h-32 bg-white/20 rounded-full blur-2xl animate-float" style="animation-delay: 1s;"></div>

      <div class="relative max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 text-center">
        <span class="inline-flex items-center gap-2 bg-white/10 backdrop-blur-sm px-4 py-2 rounded-full text-sm font-medium text-white mb-6">
          <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M8.228 9c.549-1.165 2.03-2 3.772-2 2.21 0 4 1.343 4 3 0 1.4-1.278 2.575-3.006 2.907-.542.104-.994.54-.994 1.093m0 3h.01M21 12a9 9 0 11-18 0 9 9 0 0118 0z"/>
          </svg>
          Resolvemos tus dudas
        </span>
        <h1 class="font-display text-4xl sm:text-5xl lg:text-6xl font-bold text-white mb-6">
          Preguntas Frecuentes
        </h1>
        <p class="text-xl text-primary-100 max-w-2xl mx-auto">
          Encuentra respuestas a las dudas mas comunes sobre nuestros servicios y tratamientos
        </p>
      </div>

      <!-- Wave Divider -->
      <div class="absolute bottom-0 left-0 right-0">
        <svg class="w-full h-16 text-dark-50" viewBox="0 0 1440 54" fill="currentColor" preserveAspectRatio="none">
          <path d="M0 22L60 16.7C120 11 240 1 360 0.7C480 1 600 11 720 16.7C840 22 960 22 1080 19.3C1200 16 1320 11 1380 8.3L1440 5.7V54H1380C1320 54 1200 54 1080 54C960 54 840 54 720 54C600 54 480 54 360 54C240 54 120 54 60 54H0V22Z"/>
        </svg>
      </div>
    </section>

    <!-- Search Bar -->
    <section class="py-8 bg-dark-50">
      <div class="max-w-3xl mx-auto px-4 sm:px-6 lg:px-8">
        <div class="relative">
          <input
            v-model="searchQuery"
            type="text"
            placeholder="Buscar pregunta..."
            class="w-full pl-12 pr-4 py-4 rounded-2xl border border-dark-200 focus:border-primary-500 focus:ring-2 focus:ring-primary-500/20 outline-none transition-all"
          />
          <svg class="absolute left-4 top-1/2 -translate-y-1/2 w-5 h-5 text-dark-400" fill="none" stroke="currentColor" viewBox="0 0 24 24">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M21 21l-6-6m2-5a7 7 0 11-14 0 7 7 0 0114 0z"/>
          </svg>
        </div>
      </div>
    </section>

    <!-- Categories -->
    <section class="py-8 bg-dark-50">
      <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <div class="flex flex-wrap justify-center gap-3">
          <button
            v-for="cat in categorias"
            :key="cat.id"
            @click="categoriaActiva = cat.id"
            :class="[
              'px-5 py-2.5 rounded-xl font-medium transition-all duration-300',
              categoriaActiva === cat.id
                ? 'bg-primary-500 text-white shadow-soft'
                : 'bg-white text-dark-600 hover:bg-primary-50 border border-dark-100'
            ]"
          >
            {{ cat.nombre }}
          </button>
        </div>
      </div>
    </section>

    <!-- FAQ Accordion -->
    <section class="py-12 bg-dark-50">
      <div class="max-w-3xl mx-auto px-4 sm:px-6 lg:px-8">
        <div class="space-y-4">
          <div
            v-for="(faq, index) in preguntasFiltradas"
            :key="index"
            class="bg-white rounded-2xl border border-dark-100 overflow-hidden shadow-soft"
          >
            <button
              @click="togglePregunta(index)"
              class="w-full px-6 py-5 flex items-center justify-between text-left hover:bg-dark-50 transition-colors"
            >
              <span class="font-semibold text-dark-800 pr-4">{{ faq.pregunta }}</span>
              <svg
                :class="[
                  'w-5 h-5 text-primary-500 transition-transform duration-300 flex-shrink-0',
                  preguntaAbierta === index ? 'rotate-180' : ''
                ]"
                fill="none"
                stroke="currentColor"
                viewBox="0 0 24 24"
              >
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19 9l-7 7-7-7"/>
              </svg>
            </button>
            <div
              v-show="preguntaAbierta === index"
              class="px-6 pb-5 text-dark-600 border-t border-dark-100"
            >
              <p class="pt-4">{{ faq.respuesta }}</p>
            </div>
          </div>
        </div>

        <div v-if="preguntasFiltradas.length === 0" class="text-center py-12">
          <svg class="w-16 h-16 text-dark-300 mx-auto mb-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9.172 16.172a4 4 0 015.656 0M9 10h.01M15 10h.01M21 12a9 9 0 11-18 0 9 9 0 0118 0z"/>
          </svg>
          <p class="text-dark-500">No se encontraron preguntas que coincidan con tu busqueda</p>
        </div>
      </div>
    </section>

    <!-- Contact CTA -->
    <section class="py-16 bg-white">
      <div class="max-w-4xl mx-auto px-4 sm:px-6 lg:px-8">
        <div class="bg-gradient-to-br from-secondary-500 to-primary-600 rounded-3xl p-10 lg:p-14 text-center text-white relative overflow-hidden">
          <div class="absolute top-0 right-0 w-40 h-40 bg-white/10 rounded-full -translate-y-1/2 translate-x-1/2"></div>
          <div class="absolute bottom-0 left-0 w-32 h-32 bg-white/10 rounded-full translate-y-1/2 -translate-x-1/2"></div>

          <div class="relative z-10">
            <svg class="w-16 h-16 mx-auto mb-6 text-white/80" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M8 12h.01M12 12h.01M16 12h.01M21 12c0 4.418-4.03 8-9 8a9.863 9.863 0 01-4.255-.949L3 20l1.395-3.72C3.512 15.042 3 13.574 3 12c0-4.418 4.03-8 9-8s9 3.582 9 8z"/>
            </svg>
            <h2 class="font-display text-3xl lg:text-4xl font-bold mb-4">
              ¿No encuentras tu respuesta?
            </h2>
            <p class="text-lg text-primary-100 mb-8 max-w-xl mx-auto">
              Nuestro equipo esta disponible para resolver cualquier duda que tengas
            </p>
            <div class="flex flex-col sm:flex-row gap-4 justify-center">
              <router-link
                to="/contacto"
                class="inline-flex items-center justify-center gap-2 bg-white text-primary-700 px-8 py-4 rounded-2xl font-semibold hover:bg-primary-50 transition-all duration-300"
              >
                <svg class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M3 8l7.89 5.26a2 2 0 002.22 0L21 8M5 19h14a2 2 0 002-2V7a2 2 0 00-2-2H5a2 2 0 00-2 2v10a2 2 0 002 2z"/>
                </svg>
                Contactar
              </router-link>
              <a
                href="tel:+34966123456"
                class="inline-flex items-center justify-center gap-2 bg-white/10 backdrop-blur-sm border border-white/20 text-white px-8 py-4 rounded-2xl font-semibold hover:bg-white/20 transition-all duration-300"
              >
                <svg class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M3 5a2 2 0 012-2h3.28a1 1 0 01.948.684l1.498 4.493a1 1 0 01-.502 1.21l-2.257 1.13a11.042 11.042 0 005.516 5.516l1.13-2.257a1 1 0 011.21-.502l4.493 1.498a1 1 0 01.684.949V19a2 2 0 01-2 2h-1C9.716 21 3 14.284 3 6V5z"/>
                </svg>
                966 123 456
              </a>
            </div>
          </div>
        </div>
      </div>
    </section>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue';

const searchQuery = ref('');
const categoriaActiva = ref('todas');
const preguntaAbierta = ref(null);

const categorias = [
  { id: 'todas', nombre: 'Todas' },
  { id: 'citas', nombre: 'Citas' },
  { id: 'tratamientos', nombre: 'Tratamientos' },
  { id: 'pagos', nombre: 'Pagos' },
  { id: 'general', nombre: 'General' }
];

const preguntas = ref([
  {
    categoria: 'citas',
    pregunta: '¿Como puedo reservar una cita?',
    respuesta: 'Puedes reservar tu cita de varias formas: a traves de nuestra pagina web registrandote como cliente, llamando por telefono al 966 123 456, o visitando directamente nuestra clinica. Te recomendamos la reserva online para mayor comodidad.'
  },
  {
    categoria: 'citas',
    pregunta: '¿Puedo cancelar o modificar mi cita?',
    respuesta: 'Si, puedes cancelar o modificar tu cita con al menos 24 horas de antelacion sin ningun cargo. Para hacerlo, accede a tu area de cliente en la web o llamanos por telefono. Las cancelaciones con menos de 24 horas pueden estar sujetas a cargos.'
  },
  {
    categoria: 'citas',
    pregunta: '¿Cual es el tiempo de espera para conseguir cita?',
    respuesta: 'Normalmente podemos atenderte en un plazo de 24-48 horas. Para casos urgentes, intentamos encontrar un hueco lo antes posible. Te recomendamos reservar con antelacion para asegurar tu horario preferido.'
  },
  {
    categoria: 'tratamientos',
    pregunta: '¿Cuanto dura una sesion de fisioterapia?',
    respuesta: 'La duracion varia segun el tratamiento. Una sesion estandar dura entre 45 y 60 minutos. Los tratamientos especializados como osteopatia pueden durar hasta 75 minutos. En tu primera visita te informaremos de la duracion estimada de tu tratamiento.'
  },
  {
    categoria: 'tratamientos',
    pregunta: '¿Cuantas sesiones necesitare?',
    respuesta: 'El numero de sesiones depende de tu patologia, su gravedad y tu respuesta al tratamiento. Tras la primera valoracion, tu fisioterapeuta te dara una estimacion. Normalmente, las lesiones agudas requieren 4-6 sesiones, mientras que las cronicas pueden necesitar mas.'
  },
  {
    categoria: 'tratamientos',
    pregunta: '¿Necesito traer algo a mi cita?',
    respuesta: 'Te recomendamos traer ropa comoda que permita acceder a la zona a tratar. Si tienes pruebas medicas (radiografias, resonancias, informes) relacionadas con tu lesion, traelas a tu primera cita. Nosotros proporcionamos toallas y todo el material necesario.'
  },
  {
    categoria: 'tratamientos',
    pregunta: '¿Los tratamientos son dolorosos?',
    respuesta: 'Nuestro objetivo es tu bienestar, por lo que trabajamos siempre dentro de tu umbral de tolerancia. Algunas tecnicas pueden generar molestias temporales, pero nunca dolor intenso. Siempre te informamos de lo que vamos a hacer y puedes pedirnos que paremos en cualquier momento.'
  },
  {
    categoria: 'pagos',
    pregunta: '¿Cuales son las formas de pago aceptadas?',
    respuesta: 'Aceptamos efectivo, tarjeta de credito/debito (Visa, Mastercard), y transferencia bancaria. Tambien ofrecemos la posibilidad de pagar online a traves de nuestra plataforma segura al reservar tu cita.'
  },
  {
    categoria: 'pagos',
    pregunta: '¿Trabajan con seguros medicos?',
    respuesta: 'Trabajamos con las principales companias de seguros. Consulta con nosotros tu poliza especifica y te informaremos si tenemos convenio. En caso de no tenerlo, te facilitamos facturas detalladas para que puedas solicitar el reembolso a tu aseguradora.'
  },
  {
    categoria: 'pagos',
    pregunta: '¿Ofrecen bonos de sesiones?',
    respuesta: 'Si, ofrecemos bonos de 5 y 10 sesiones con descuentos del 10% y 15% respectivamente. Los bonos son personales e intransferibles y tienen una validez de 6 meses desde la fecha de compra.'
  },
  {
    categoria: 'general',
    pregunta: '¿Cual es el horario de la clinica?',
    respuesta: 'Nuestro horario es de lunes a viernes de 9:00 a 20:00 horas, y sabados de 9:00 a 14:00 horas. Los domingos y festivos permanecemos cerrados.'
  },
  {
    categoria: 'general',
    pregunta: '¿Donde estan ubicados?',
    respuesta: 'Estamos ubicados en Calle Mayor 123, 03001 Alicante. Contamos con facil acceso en transporte publico (lineas de autobus 21 y 22) y parking publico cercano. La clinica es accesible para personas con movilidad reducida.'
  },
  {
    categoria: 'general',
    pregunta: '¿Atienden urgencias?',
    respuesta: 'No somos un servicio de urgencias medicas. Sin embargo, para casos de dolor agudo o lesiones recientes, intentamos darte cita lo antes posible. En caso de emergencia real, te recomendamos acudir a urgencias hospitalarias.'
  }
]);

const togglePregunta = (index) => {
  preguntaAbierta.value = preguntaAbierta.value === index ? null : index;
};

const preguntasFiltradas = computed(() => {
  let filtradas = preguntas.value;

  // Filtrar por categoria
  if (categoriaActiva.value !== 'todas') {
    filtradas = filtradas.filter(p => p.categoria === categoriaActiva.value);
  }

  // Filtrar por busqueda
  if (searchQuery.value.trim()) {
    const query = searchQuery.value.toLowerCase();
    filtradas = filtradas.filter(p =>
      p.pregunta.toLowerCase().includes(query) ||
      p.respuesta.toLowerCase().includes(query)
    );
  }

  return filtradas;
});
</script>
