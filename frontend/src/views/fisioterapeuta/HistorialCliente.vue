<template>
  <div class="p-6">
    <div class="flex justify-between items-center mb-6">
      <div>
        <h1 class="text-3xl font-bold text-gray-800">Historial de Cliente</h1>
        <p v-if="store.historialCliente.length > 0" class="text-gray-600 mt-1">
          {{ store.historialCliente[0]?.nombreCliente }}
        </p>
      </div>

      <div class="flex gap-3">
        <button
          @click="$router.back()"
          class="bg-gray-200 text-gray-700 px-4 py-2 rounded hover:bg-gray-300"
        >
          ← Volver
        </button>
        <router-link
          to="/fisioterapeuta/dashboard"
          class="bg-indigo-600 text-white px-4 py-2 rounded hover:bg-indigo-700"
        >
          🏠 Dashboard
        </router-link>
      </div>
    </div>

    <!-- Loading state -->
    <div v-if="store.loading" class="text-center py-10">
      <div class="inline-block animate-spin rounded-full h-12 w-12 border-b-2 border-indigo-600"></div>
      <p class="text-gray-600 mt-4">Cargando historial...</p>
    </div>

    <!-- Error state -->
    <div v-else-if="store.error" class="bg-red-100 border border-red-400 text-red-700 px-4 py-3 rounded mb-4">
      {{ store.error }}
    </div>

    <!-- Contenido principal -->
    <div v-else>
      <!-- Resumen -->
      <div v-if="store.historialCliente.length > 0" class="bg-white shadow rounded-lg p-6 mb-6">
        <h3 class="text-lg font-semibold text-gray-800 mb-4">Resumen</h3>
        <div class="grid grid-cols-1 md:grid-cols-3 gap-4">
          <div class="bg-blue-50 p-4 rounded">
            <p class="text-sm text-gray-600">Total de citas</p>
            <p class="text-2xl font-bold text-blue-600">{{ store.historialCliente.length }}</p>
          </div>
          <div class="bg-green-50 p-4 rounded">
            <p class="text-sm text-gray-600">Citas completadas</p>
            <p class="text-2xl font-bold text-green-600">{{ citasCompletadas }}</p>
          </div>
          <div class="bg-purple-50 p-4 rounded">
            <p class="text-sm text-gray-600">Con nota de sesión</p>
            <p class="text-2xl font-bold text-purple-600">{{ citasConNota }}</p>
          </div>
        </div>
      </div>

      <!-- Lista de citas -->
      <div v-if="store.historialCliente.length === 0" class="bg-white shadow rounded-lg p-10 text-center text-gray-500">
        No hay historial de citas para este cliente
      </div>

      <div v-else class="space-y-4">
        <div
          v-for="entrada in store.historialCliente"
          :key="entrada.citaId"
          class="bg-white shadow rounded-lg p-6 hover:shadow-lg transition cursor-pointer"
          :class="{'opacity-70': entrada.estado === 'CANCELADA'}"
          @click="verDetalle(entrada.citaId)"
        >
          <div class="flex justify-between items-start">
            <!-- Información principal -->
            <div class="flex-1">
              <div class="flex items-center gap-3 mb-2">
                <span class="text-xl font-bold text-gray-700">
                  📅 {{ formatFecha(entrada.fecha) }}
                </span>
                <span class="text-gray-500">-</span>
                <span class="text-gray-600">
                  {{ formatHora(entrada.horaInicio) }} - {{ formatHora(entrada.horaFin) }}
                </span>
              </div>

              <div class="mb-3">
                <div class="text-lg font-semibold text-gray-800">
                  {{ entrada.nombreServicio }}
                </div>
                <div class="text-sm text-gray-500">
                  Duración: {{ entrada.duracionServicio }} minutos
                </div>
              </div>

              <!-- Nota de sesión (resumen) -->
              <div v-if="entrada.tieneNota" class="mt-3 bg-blue-50 p-3 rounded border-l-4 border-blue-500">
                <div class="flex items-start gap-2">
                  <span class="text-blue-600 font-semibold">✅ Nota:</span>
                  <div class="flex-1">
                    <p class="text-sm text-gray-700 line-clamp-2">
                      {{ entrada.resumenNota || 'Nota de sesión disponible' }}
                    </p>
                  </div>
                </div>
              </div>

              <div v-else class="mt-3 bg-gray-50 p-3 rounded border-l-4 border-gray-300">
                <span class="text-gray-500 text-sm">❌ Sin nota de sesión</span>
              </div>
            </div>

            <!-- Estado -->
            <div class="flex flex-col items-end gap-2">
              <span
                class="px-3 py-1 text-xs font-semibold rounded-full"
                :class="estadoClass(entrada.estado)"
              >
                {{ entrada.estado }}
              </span>

              <button
                @click.stop="verDetalle(entrada.citaId)"
                class="text-blue-600 hover:text-blue-800 text-sm font-medium"
              >
                Ver detalle →
              </button>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { useFisioterapeutaStore } from '@/stores/fisioterapeuta';

const route = useRoute();
const router = useRouter();
const store = useFisioterapeutaStore();

const clienteId = ref(parseInt(route.params.id));

onMounted(async () => {
  await store.fetchHistorialCliente(clienteId.value);
});

const citasCompletadas = computed(() => {
  return store.historialCliente.filter(e => e.estado === 'COMPLETADA').length;
});

const citasConNota = computed(() => {
  return store.historialCliente.filter(e => e.tieneNota).length;
});

const verDetalle = (citaId) => {
  router.push(`/fisioterapeuta/citas/${citaId}`);
};

const formatHora = (horaStr) => {
  if (!horaStr) return '';
  return horaStr.substring(0, 5);
};

const formatFecha = (fechaStr) => {
  if (!fechaStr) return '';
  const fecha = new Date(fechaStr + 'T00:00:00');
  return fecha.toLocaleDateString('es-ES', {
    day: 'numeric',
    month: 'short',
    year: 'numeric'
  });
};

const estadoClass = (estado) => {
  switch(estado) {
    case 'COMPLETADA': return 'bg-green-100 text-green-800';
    case 'CANCELADA': return 'bg-red-100 text-red-800';
    case 'CONFIRMADA': return 'bg-blue-100 text-blue-800';
    case 'PENDIENTE': return 'bg-yellow-100 text-yellow-800';
    default: return 'bg-gray-100 text-gray-800';
  }
};
</script>

<style scoped>
.line-clamp-2 {
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}
</style>
