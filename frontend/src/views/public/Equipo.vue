<template>
  <div class="min-h-screen bg-gray-50 py-12">
    <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
      <h1 class="text-4xl font-bold text-center mb-4">Nuestro Equipo</h1>
      <p class="text-center text-gray-600 mb-12">
        Profesionales altamente cualificados a tu servicio
      </p>

      <div v-if="loading" class="text-center">
        <p class="text-gray-600">Cargando equipo...</p>
      </div>

      <div v-else class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-8">
        <div
          v-for="fisio in fisioterapeutas"
          :key="fisio.id"
          class="bg-white rounded-lg shadow-md overflow-hidden hover:shadow-xl transition"
        >
          <div class="h-64 bg-gray-200">
            <img
              v-if="fisio.fotoUrl"
              :src="fisio.fotoUrl"
              :alt="fisio.nombre"
              class="w-full h-full object-cover"
            />
            <div
              v-else
              class="w-full h-full flex items-center justify-center bg-gradient-to-br from-blue-400 to-blue-600"
            >
              <span class="text-8xl">👨‍⚕️</span>
            </div>
          </div>

          <div class="p-6">
            <h3 class="text-2xl font-bold mb-2">
              {{ fisio.nombre }} {{ fisio.apellidos }}
            </h3>

            <div class="flex items-center mb-3">
              <span class="text-yellow-400">⭐</span>
              <span class="ml-2 font-semibold">{{
                fisio.valoracionPromedio.toFixed(1)
              }}</span>
              <span class="ml-1 text-gray-500">/ 5.0</span>
            </div>

            <p class="text-sm text-gray-600 mb-3">
              <strong>Colegiado:</strong> {{ fisio.numeroColegiado }}
            </p>

            <div class="mb-4">
              <p class="text-sm font-semibold text-gray-700 mb-1">
                Especialidades:
              </p>
              <p class="text-sm text-gray-600">{{ fisio.especialidades }}</p>
            </div>

            <p class="text-gray-600 text-sm mb-4">{{ fisio.biografia }}</p>

            <router-link
              to="/register"
              class="block w-full bg-primary text-white text-center py-2 rounded-lg hover:bg-blue-600 transition"
            >
              Solicitar Cita
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

const fisioterapeutas = ref([]);
const loading = ref(true);

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
