<template>
  <div>
    <!-- Hero Section -->
    <section
      class="bg-gradient-to-r from-blue-500 to-blue-600 text-white py-20"
    >
      <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 text-center">
        <h1 class="text-5xl font-bold mb-4">
          Bienvenido a Clínica de Fisioterapia
        </h1>
        <p class="text-xl mb-8">
          Tu salud y bienestar son nuestra prioridad. Agenda tu cita hoy mismo.
        </p>
        <div class="space-x-4">
          <router-link
            to="/register"
            class="bg-white text-blue-600 px-8 py-3 rounded-lg font-semibold hover:bg-gray-100 inline-block"
          >
            Reservar Cita
          </router-link>
          <router-link
            to="/servicios"
            class="bg-transparent border-2 border-white text-white px-8 py-3 rounded-lg font-semibold hover:bg-white hover:text-blue-600 inline-block"
          >
            Ver Servicios
          </router-link>
        </div>
      </div>
    </section>

    <!-- Servicios Destacados -->
    <section class="py-16 bg-gray-50">
      <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <h2 class="text-3xl font-bold text-center mb-12">Nuestros Servicios</h2>
        <div class="grid grid-cols-1 md:grid-cols-3 gap-8">
          <div
            v-for="servicio in serviciosDestacados"
            :key="servicio.id"
            class="bg-white rounded-lg shadow-md p-6 hover:shadow-lg transition"
          >
            <div class="text-4xl mb-4">💆</div>
            <h3 class="text-xl font-bold mb-2">{{ servicio.nombre }}</h3>
            <p class="text-gray-600 mb-4">{{ servicio.descripcion }}</p>
            <div class="flex justify-between items-center">
              <span class="text-2xl font-bold text-primary"
                >{{ servicio.precio }}€</span
              >
              <span class="text-gray-500"
                >{{ servicio.duracionMinutos }} min</span
              >
            </div>
          </div>
        </div>
        <div class="text-center mt-8">
          <router-link
            to="/servicios"
            class="text-primary hover:underline font-semibold"
          >
            Ver todos los servicios →
          </router-link>
        </div>
      </div>
    </section>

    <!-- Por qué elegirnos -->
    <section class="py-16">
      <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <h2 class="text-3xl font-bold text-center mb-12">
          ¿Por qué elegirnos?
        </h2>
        <div class="grid grid-cols-1 md:grid-cols-4 gap-8 text-center">
          <div>
            <div class="text-5xl mb-4">👨‍⚕️</div>
            <h3 class="font-bold mb-2">Profesionales Cualificados</h3>
            <p class="text-gray-600">
              Fisioterapeutas certificados con años de experiencia
            </p>
          </div>
          <div>
            <div class="text-5xl mb-4">🏥</div>
            <h3 class="font-bold mb-2">Instalaciones Modernas</h3>
            <p class="text-gray-600">Equipamiento de última generación</p>
          </div>
          <div>
            <div class="text-5xl mb-4">📅</div>
            <h3 class="font-bold mb-2">Horarios Flexibles</h3>
            <p class="text-gray-600">De lunes a sábado para tu comodidad</p>
          </div>
          <div>
            <div class="text-5xl mb-4">⭐</div>
            <h3 class="font-bold mb-2">Atención Personalizada</h3>
            <p class="text-gray-600">
              Tratamientos adaptados a tus necesidades
            </p>
          </div>
        </div>
      </div>
    </section>

    <!-- CTA Final -->
    <section class="bg-primary text-white py-16">
      <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 text-center">
        <h2 class="text-3xl font-bold mb-4">
          ¿Listo para mejorar tu bienestar?
        </h2>
        <p class="text-xl mb-8">Regístrate ahora y agenda tu primera cita</p>
        <router-link
          to="/register"
          class="bg-white text-blue-600 px-8 py-3 rounded-lg font-semibold hover:bg-gray-100 inline-block"
        >
          Crear Cuenta Gratis
        </router-link>
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
    serviciosDestacados.value = servicios.slice(0, 3); // Primeros 3 servicios
  } catch (error) {
    console.error("Error cargando servicios:", error);
  }
});
</script>
