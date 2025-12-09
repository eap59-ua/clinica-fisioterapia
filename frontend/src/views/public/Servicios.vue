<template>
  <div class="min-h-screen bg-gray-50 py-12">
    <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
      <h1 class="text-4xl font-bold text-center mb-12">Nuestros Servicios</h1>

      <div v-if="loading" class="text-center">
        <p class="text-gray-600">Cargando servicios...</p>
      </div>

      <div v-else class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-8">
        <div
          v-for="servicio in servicios"
          :key="servicio.id"
          class="bg-white rounded-lg shadow-md overflow-hidden hover:shadow-xl transition"
        >
          <div v-if="servicio.imagenUrl" class="h-48 bg-gray-200">
            <img
              :src="servicio.imagenUrl"
              :alt="servicio.nombre"
              class="w-full h-full object-cover"
            />
          </div>
          <div
            v-else
            class="h-48 bg-gradient-to-br from-blue-400 to-blue-600 flex items-center justify-center"
          >
            <span class="text-6xl">💆</span>
          </div>

          <div class="p-6">
            <h3 class="text-2xl font-bold mb-3">{{ servicio.nombre }}</h3>
            <p class="text-gray-600 mb-4">{{ servicio.descripcion }}</p>

            <div class="flex justify-between items-center mb-4">
              <div>
                <p class="text-gray-500 text-sm">Duración</p>
                <p class="font-semibold">
                  {{ servicio.duracionMinutos }} minutos
                </p>
              </div>
              <div class="text-right">
                <p class="text-gray-500 text-sm">Precio</p>
                <p class="text-3xl font-bold text-primary">
                  {{ servicio.precio }}€
                </p>
              </div>
            </div>

            <router-link
              to="/register"
              class="block w-full bg-primary text-white text-center py-3 rounded-lg hover:bg-blue-600 transition"
            >
              Reservar Ahora
            </router-link>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from "vue";
import publicService from "../../services/publicService";

const servicios = ref([]);
const loading = ref(true);

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
