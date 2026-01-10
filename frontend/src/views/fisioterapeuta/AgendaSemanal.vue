<template>
  <div class="p-6">
    <div class="flex justify-between items-center mb-6">
      <h1 class="text-3xl font-bold text-gray-800">Agenda Semanal</h1>

      <div class="flex gap-4">
        <div class="flex gap-2">
          <button
            @click="semanaAnterior"
            class="bg-gray-200 text-gray-700 px-4 py-2 rounded hover:bg-gray-300"
          >
            ← Anterior
          </button>
          <button
            @click="hoy"
            class="bg-indigo-600 text-white px-4 py-2 rounded hover:bg-indigo-700"
          >
            Hoy
          </button>
          <button
            @click="semanaSiguiente"
            class="bg-gray-200 text-gray-700 px-4 py-2 rounded hover:bg-gray-300"
          >
            Siguiente →
          </button>
        </div>

        <router-link
          to="/fisioterapeuta/dashboard"
          class="bg-blue-600 text-white px-4 py-2 rounded hover:bg-blue-700"
        >
          🏠 Dashboard
        </router-link>
      </div>
    </div>

    <!-- Indicador de semana -->
    <div class="bg-white shadow rounded-lg p-4 mb-4">
      <div class="text-center text-gray-700">
        <span class="font-semibold">Semana del:</span>
        <span class="ml-2">{{ formatFecha(fechaInicio) }}</span>
        <span class="mx-2">-</span>
        <span>{{ formatFecha(calcularFechaFin()) }}</span>
      </div>
    </div>

    <!-- Loading state -->
    <div v-if="store.loading" class="text-center py-10">
      <div class="inline-block animate-spin rounded-full h-12 w-12 border-b-2 border-indigo-600"></div>
      <p class="text-gray-600 mt-4">Cargando agenda...</p>
    </div>

    <!-- Error state -->
    <div v-else-if="store.error" class="bg-red-100 border border-red-400 text-red-700 px-4 py-3 rounded mb-4">
      {{ store.error }}
    </div>

    <!-- Calendario semanal -->
    <div v-else class="bg-white shadow rounded-lg p-4">
      <CalendarioSemanal
        :citas="store.citasSemana"
        :fecha-inicio="fechaInicio"
        @ver-detalle="verDetalle"
      />
    </div>

    <!-- Leyenda -->
    <div class="mt-4 bg-white shadow rounded-lg p-4">
      <div class="text-sm font-semibold text-gray-700 mb-2">Leyenda:</div>
      <div class="flex flex-wrap gap-4 text-xs">
        <div class="flex items-center gap-2">
          <div class="w-4 h-4 bg-yellow-100 border-l-4 border-yellow-500 rounded"></div>
          <span>Pendiente</span>
        </div>
        <div class="flex items-center gap-2">
          <div class="w-4 h-4 bg-blue-100 border-l-4 border-blue-500 rounded"></div>
          <span>Confirmada</span>
        </div>
        <div class="flex items-center gap-2">
          <div class="w-4 h-4 bg-green-100 border-l-4 border-green-500 rounded"></div>
          <span>Completada</span>
        </div>
        <div class="flex items-center gap-2">
          <div class="w-4 h-4 bg-red-100 border-l-4 border-red-500 rounded"></div>
          <span>Cancelada</span>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, watch } from 'vue';
import { useFisioterapeutaStore } from '@/stores/fisioterapeuta';
import { useRouter } from 'vue-router';
import CalendarioSemanal from '@/components/recepcionista/CalendarioSemanal.vue';

const store = useFisioterapeutaStore();
const router = useRouter();

// Inicializar con el lunes de esta semana
const fechaInicio = ref(getLunesDeLaSemana(new Date()));

onMounted(async () => {
  await cargarCitas();
});

watch(fechaInicio, async () => {
  await cargarCitas();
});

const cargarCitas = async () => {
  await store.fetchCitasSemana(fechaInicio.value);
};

const verDetalle = (cita) => {
  router.push(`/fisioterapeuta/citas/${cita.id}`);
};

const semanaAnterior = () => {
  const fecha = new Date(fechaInicio.value);
  fecha.setDate(fecha.getDate() - 7);
  fechaInicio.value = fecha.toISOString().split('T')[0];
};

const semanaSiguiente = () => {
  const fecha = new Date(fechaInicio.value);
  fecha.setDate(fecha.getDate() + 7);
  fechaInicio.value = fecha.toISOString().split('T')[0];
};

const hoy = () => {
  fechaInicio.value = getLunesDeLaSemana(new Date());
};

const calcularFechaFin = () => {
  const fecha = new Date(fechaInicio.value);
  fecha.setDate(fecha.getDate() + 6); // Domingo
  return fecha.toISOString().split('T')[0];
};

const formatFecha = (fechaStr) => {
  if (!fechaStr) return '';
  const fecha = new Date(fechaStr + 'T00:00:00');
  return fecha.toLocaleDateString('es-ES', {
    day: 'numeric',
    month: 'long',
    year: 'numeric'
  });
};

// Obtener el lunes de la semana de una fecha dada
function getLunesDeLaSemana(fecha) {
  const date = new Date(fecha);
  const day = date.getDay();
  const diff = date.getDate() - day + (day === 0 ? -6 : 1); // Ajustar cuando es domingo
  const lunes = new Date(date.setDate(diff));
  return lunes.toISOString().split('T')[0];
}
</script>
