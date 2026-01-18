<template>
  <div class="min-h-screen">
    <!-- Hero Section -->
    <section class="relative py-20 lg:py-28 overflow-hidden bg-gradient-to-br from-primary-600 via-primary-700 to-secondary-700">
      <div class="absolute inset-0 bg-hero-pattern opacity-20"></div>
      <div class="absolute top-0 right-0 w-1/3 h-full bg-gradient-to-l from-accent-500/20 to-transparent"></div>

      <!-- Floating Elements -->
      <div class="absolute top-20 left-10 w-20 h-20 bg-white/10 rounded-full blur-xl animate-float"></div>
      <div class="absolute bottom-20 right-20 w-32 h-32 bg-accent-400/20 rounded-full blur-2xl animate-float" style="animation-delay: 1s;"></div>

      <div class="relative max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 text-center">
        <span class="inline-flex items-center gap-2 bg-white/10 backdrop-blur-sm px-4 py-2 rounded-full text-sm font-medium text-white mb-6">
          <span class="w-2 h-2 bg-accent-400 rounded-full animate-pulse"></span>
          Estamos aquí para ayudarte
        </span>
        <h1 class="font-display text-4xl sm:text-5xl lg:text-6xl font-bold text-white mb-6">
          Contacto
        </h1>
        <p class="text-xl text-primary-100 max-w-2xl mx-auto">
          ¿Tienes alguna pregunta? Ponte en contacto con nosotros y te responderemos lo antes posible
        </p>
      </div>

      <!-- Wave Divider -->
      <div class="absolute bottom-0 left-0 right-0">
        <svg class="w-full h-16 text-dark-50" viewBox="0 0 1440 54" fill="currentColor" preserveAspectRatio="none">
          <path d="M0 22L60 16.7C120 11 240 1 360 0.7C480 1 600 11 720 16.7C840 22 960 22 1080 19.3C1200 16 1320 11 1380 8.3L1440 5.7V54H1380C1320 54 1200 54 1080 54C960 54 840 54 720 54C600 54 480 54 360 54C240 54 120 54 60 54H0V22Z"/>
        </svg>
      </div>
    </section>

    <!-- Contact Section -->
    <section class="py-20 bg-dark-50">
      <div class="max-w-5xl mx-auto px-4 sm:px-6 lg:px-8">
        <div class="grid grid-cols-1 lg:grid-cols-2 gap-8">
          <!-- Contact Info -->
          <div class="bg-white rounded-3xl shadow-soft p-8 lg:p-10 animate-fade-in-up">
            <h2 class="font-display text-2xl font-bold text-dark-800 mb-8">
              Información de Contacto
            </h2>

            <div class="space-y-6">
              <!-- Dirección -->
              <div class="flex items-start gap-4 group">
                <div class="w-12 h-12 bg-primary-100 rounded-xl flex items-center justify-center flex-shrink-0 group-hover:bg-primary-200 transition-colors">
                  <svg class="w-6 h-6 text-primary-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M17.657 16.657L13.414 20.9a1.998 1.998 0 01-2.827 0l-4.244-4.243a8 8 0 1111.314 0z"/>
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M15 11a3 3 0 11-6 0 3 3 0 016 0z"/>
                  </svg>
                </div>
                <div>
                  <h3 class="font-semibold text-dark-800 mb-1">Dirección</h3>
                  <p class="text-dark-500">
                    Calle Mayor, 123<br />03001 Alicante, España
                  </p>
                </div>
              </div>

              <!-- Teléfono -->
              <div class="flex items-start gap-4 group">
                <div class="w-12 h-12 bg-secondary-100 rounded-xl flex items-center justify-center flex-shrink-0 group-hover:bg-secondary-200 transition-colors">
                  <svg class="w-6 h-6 text-secondary-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M3 5a2 2 0 012-2h3.28a1 1 0 01.948.684l1.498 4.493a1 1 0 01-.502 1.21l-2.257 1.13a11.042 11.042 0 005.516 5.516l1.13-2.257a1 1 0 011.21-.502l4.493 1.498a1 1 0 01.684.949V19a2 2 0 01-2 2h-1C9.716 21 3 14.284 3 6V5z"/>
                  </svg>
                </div>
                <div>
                  <h3 class="font-semibold text-dark-800 mb-1">Teléfono</h3>
                  <p class="text-dark-500">+34 965 123 456</p>
                </div>
              </div>

              <!-- Email -->
              <div class="flex items-start gap-4 group">
                <div class="w-12 h-12 bg-accent-100 rounded-xl flex items-center justify-center flex-shrink-0 group-hover:bg-accent-200 transition-colors">
                  <svg class="w-6 h-6 text-accent-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M3 8l7.89 5.26a2 2 0 002.22 0L21 8M5 19h14a2 2 0 002-2V7a2 2 0 00-2-2H5a2 2 0 00-2 2v10a2 2 0 002 2z"/>
                  </svg>
                </div>
                <div>
                  <h3 class="font-semibold text-dark-800 mb-1">Email</h3>
                  <p class="text-dark-500">info@clinicafisio.com</p>
                </div>
              </div>

              <!-- Horario -->
              <div class="flex items-start gap-4 group">
                <div class="w-12 h-12 bg-primary-100 rounded-xl flex items-center justify-center flex-shrink-0 group-hover:bg-primary-200 transition-colors">
                  <svg class="w-6 h-6 text-primary-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 8v4l3 3m6-3a9 9 0 11-18 0 9 9 0 0118 0z"/>
                  </svg>
                </div>
                <div>
                  <h3 class="font-semibold text-dark-800 mb-1">Horario</h3>
                  <p class="text-dark-500">
                    Lunes - Viernes: 09:00 - 20:00<br />
                    Sábado: 09:00 - 14:00
                  </p>
                </div>
              </div>
            </div>

            <!-- Map Placeholder -->
            <div class="mt-8 h-48 bg-dark-100 rounded-2xl flex items-center justify-center overflow-hidden">
              <div class="text-center text-dark-400">
                <svg class="w-10 h-10 mx-auto mb-2" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 20l-5.447-2.724A1 1 0 013 16.382V5.618a1 1 0 011.447-.894L9 7m0 13l6-3m-6 3V7m6 10l4.553 2.276A1 1 0 0021 18.382V7.618a1 1 0 00-.553-.894L15 4m0 13V4m0 0L9 7"/>
                </svg>
                <p class="text-sm">Mapa de ubicación</p>
              </div>
            </div>
          </div>

          <!-- Contact Form -->
          <div class="bg-white rounded-3xl shadow-soft p-8 lg:p-10 animate-fade-in-up" style="animation-delay: 0.1s;">
            <h2 class="font-display text-2xl font-bold text-dark-800 mb-8">
              Envíanos un Mensaje
            </h2>

            <form @submit.prevent="handleSubmit" class="space-y-5">
              <!-- Nombre -->
              <div>
                <label class="block text-sm font-medium text-dark-700 mb-2">Nombre</label>
                <div class="relative">
                  <div class="absolute inset-y-0 left-0 pl-4 flex items-center pointer-events-none">
                    <svg class="w-5 h-5 text-dark-400" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                      <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M16 7a4 4 0 11-8 0 4 4 0 018 0zM12 14a7 7 0 00-7 7h14a7 7 0 00-7-7z"/>
                    </svg>
                  </div>
                  <input
                    v-model="formData.nombre"
                    type="text"
                    required
                    class="w-full pl-12 pr-4 py-3 bg-dark-50 border border-dark-200 rounded-xl focus:outline-none focus:ring-2 focus:ring-primary-500 focus:border-transparent transition-all duration-200 text-dark-800 placeholder-dark-400"
                    placeholder="Tu nombre"
                  />
                </div>
              </div>

              <!-- Email -->
              <div>
                <label class="block text-sm font-medium text-dark-700 mb-2">Email</label>
                <div class="relative">
                  <div class="absolute inset-y-0 left-0 pl-4 flex items-center pointer-events-none">
                    <svg class="w-5 h-5 text-dark-400" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                      <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M16 12a4 4 0 10-8 0 4 4 0 008 0zm0 0v1.5a2.5 2.5 0 005 0V12a9 9 0 10-9 9m4.5-1.206a8.959 8.959 0 01-4.5 1.207"/>
                    </svg>
                  </div>
                  <input
                    v-model="formData.email"
                    type="email"
                    required
                    class="w-full pl-12 pr-4 py-3 bg-dark-50 border border-dark-200 rounded-xl focus:outline-none focus:ring-2 focus:ring-primary-500 focus:border-transparent transition-all duration-200 text-dark-800 placeholder-dark-400"
                    placeholder="tu@email.com"
                  />
                </div>
              </div>

              <!-- Mensaje -->
              <div>
                <label class="block text-sm font-medium text-dark-700 mb-2">Mensaje</label>
                <div class="relative">
                  <textarea
                    v-model="formData.mensaje"
                    required
                    rows="5"
                    class="w-full px-4 py-3 bg-dark-50 border border-dark-200 rounded-xl focus:outline-none focus:ring-2 focus:ring-primary-500 focus:border-transparent transition-all duration-200 text-dark-800 placeholder-dark-400 resize-none"
                    placeholder="¿En qué podemos ayudarte?"
                  ></textarea>
                </div>
              </div>

              <!-- Submit Button -->
              <button
                type="submit"
                class="w-full bg-gradient-to-r from-primary-500 to-primary-600 text-white py-3.5 rounded-xl font-semibold shadow-soft hover:shadow-glow transition-all duration-300 flex items-center justify-center gap-2 hover:-translate-y-0.5"
              >
                <svg class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 19l9 2-9-18-9 18 9-2zm0 0v-8"/>
                </svg>
                Enviar Mensaje
              </button>
            </form>

            <!-- Success Message -->
            <div
              v-if="mensajeEnviado"
              class="mt-6 bg-green-50 border border-green-200 text-green-700 px-4 py-4 rounded-xl flex items-center gap-3 animate-fade-in"
            >
              <div class="w-10 h-10 bg-green-100 rounded-full flex items-center justify-center flex-shrink-0">
                <svg class="w-5 h-5 text-green-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M5 13l4 4L19 7"/>
                </svg>
              </div>
              <div>
                <p class="font-semibold">Mensaje enviado correctamente</p>
                <p class="text-sm text-green-600">Te contactaremos pronto.</p>
              </div>
            </div>
          </div>
        </div>
      </div>
    </section>
  </div>
</template>

<script setup>
import { ref } from "vue";

const formData = ref({
  nombre: "",
  email: "",
  mensaje: "",
});

const mensajeEnviado = ref(false);

const handleSubmit = () => {
  console.log("Mensaje enviado:", formData.value);
  mensajeEnviado.value = true;

  formData.value = {
    nombre: "",
    email: "",
    mensaje: "",
  };

  setTimeout(() => {
    mensajeEnviado.value = false;
  }, 3000);
};
</script>
