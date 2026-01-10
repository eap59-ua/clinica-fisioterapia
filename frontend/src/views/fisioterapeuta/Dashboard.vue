<template>
  <div class="p-6">
    <div class="flex justify-between items-center mb-6">
      <h1 class="text-3xl font-bold text-gray-800">Dashboard - Mis Citas de Hoy</h1>

      <router-link
        to="/fisioterapeuta/agenda"
        class="bg-indigo-600 text-white px-4 py-2 rounded hover:bg-indigo-700 shadow-sm flex items-center"
      >
        📅 Ver Agenda Semanal
      </router-link>
    </div>

    <!-- Resumen -->
    <div class="grid grid-cols-1 md:grid-cols-3 gap-4 mb-6">
      <div class="bg-white p-6 rounded-lg shadow border-l-4 border-blue-500">
        <div class="text-sm text-gray-500 uppercase">Total de Hoy</div>
        <div class="text-3xl font-bold text-gray-800">{{ store.totalCitasHoy }}</div>
      </div>

      <div class="bg-white p-6 rounded-lg shadow border-l-4 border-green-500">
        <div class="text-sm text-gray-500 uppercase">Completadas</div>
        <div class="text-3xl font-bold text-green-600">{{ store.citasCompletadasHoy }}</div>
      </div>

      <div class="bg-white p-6 rounded-lg shadow border-l-4 border-yellow-500">
        <div class="text-sm text-gray-500 uppercase">Pendientes</div>
        <div class="text-3xl font-bold text-yellow-600">{{ store.citasPendientesHoy }}</div>
      </div>
    </div>

    <!-- Loading state -->
    <div v-if="store.loading" class="text-center py-10">
      <div class="inline-block animate-spin rounded-full h-12 w-12 border-b-2 border-indigo-600"></div>
      <p class="text-gray-600 mt-4">Cargando citas...</p>
    </div>

    <!-- Error state -->
    <div v-else-if="store.error" class="bg-red-100 border border-red-400 text-red-700 px-4 py-3 rounded mb-4">
      {{ store.error }}
    </div>

    <!-- Lista de citas -->
    <div v-else>
      <div v-if="store.citasHoy.length === 0" class="bg-white shadow rounded-lg p-10 text-center text-gray-500">
        No tienes citas programadas para hoy
      </div>

      <div v-else class="space-y-4">
        <div
          v-for="cita in store.citasHoy"
          :key="cita.id"
          class="bg-white shadow rounded-lg p-6 hover:shadow-lg transition"
          :class="{'opacity-60': cita.estado === 'CANCELADA'}"
        >
          <div class="flex justify-between items-start">
            <!-- Info principal -->
            <div class="flex-1">
              <div class="flex items-center gap-3 mb-2">
                <span class="text-2xl font-bold text-gray-700">
                  {{ formatHora(cita.horaInicio) }}
                </span>
                <span class="text-gray-400">-</span>
                <span class="text-gray-600">{{ formatHora(cita.horaFin) }}</span>
              </div>

              <div class="mb-3">
                <div class="text-xl font-semibold text-gray-800">
                  {{ cita.cliente?.nombre }} {{ cita.cliente?.apellidos }}
                </div>
                <div class="text-sm text-gray-500">
                  {{ cita.cliente?.telefono }} | {{ cita.cliente?.email }}
                </div>
              </div>

              <div class="flex gap-4 text-sm">
                <div>
                  <span class="text-gray-500">Servicio:</span>
                  <span class="font-medium text-gray-700 ml-1">{{ cita.servicio?.nombre }}</span>
                  <span class="text-gray-400 ml-1">({{ cita.servicio?.duracion }} min)</span>
                </div>
                <div>
                  <span class="text-gray-500">Sala:</span>
                  <span class="font-medium text-gray-700 ml-1">{{ cita.sala?.nombre }}</span>
                </div>
              </div>
            </div>

            <!-- Estado y acciones -->
            <div class="flex flex-col items-end gap-3">
              <span
                class="px-3 py-1 text-xs font-semibold rounded-full"
                :class="estadoClass(cita.estado)"
              >
                {{ cita.estado }}
              </span>

              <div class="flex gap-2">
                <router-link
                  :to="`/fisioterapeuta/citas/${cita.id}`"
                  class="bg-blue-600 text-white px-3 py-1 rounded text-sm hover:bg-blue-700"
                >
                  Ver Detalle
                </router-link>

                <button
                  v-if="cita.estado !== 'COMPLETADA' && cita.estado !== 'CANCELADA'"
                  @click="completar(cita.id)"
                  :disabled="completando === cita.id"
                  class="bg-green-600 text-white px-3 py-1 rounded text-sm hover:bg-green-700 disabled:bg-gray-400"
                >
                  {{ completando === cita.id ? 'Completando...' : 'Completar' }}
                </button>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { useFisioterapeutaStore } from '@/stores/fisioterapeuta';
import { useRouter } from 'vue-router';

const store = useFisioterapeutaStore();
const router = useRouter();
const completando = ref(null);

onMounted(async () => {
  await store.fetchCitasHoy();
});

const completar = async (citaId) => {
  if (!confirm('¿Marcar esta cita como completada?')) return;

  completando.value = citaId;
  try {
    await store.completarCita(citaId);
    alert('Cita marcada como completada exitosamente');
  } catch (error) {
    alert('Error al completar la cita. Por favor, intenta de nuevo.');
  } finally {
    completando.value = null;
  }
};

const formatHora = (horaStr) => {
  if (!horaStr) return '';
  return horaStr.substring(0, 5);
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
