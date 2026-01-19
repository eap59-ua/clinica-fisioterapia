<template>
  <div class="p-6 pt-24">
    <div class="flex justify-between items-center mb-6">
      <h1 class="text-3xl font-bold text-gray-800">Detalle de Cita</h1>

      <div class="flex gap-3">
        <router-link
          to="/fisioterapeuta/agenda"
          class="bg-gray-200 text-gray-700 px-4 py-2 rounded hover:bg-gray-300"
        >
          ← Volver a Agenda
        </router-link>
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
      <p class="text-gray-600 mt-4">Cargando detalle...</p>
    </div>

    <!-- Error state -->
    <div v-else-if="store.error" class="bg-red-100 border border-red-400 text-red-700 px-4 py-3 rounded mb-4">
      {{ store.error }}
    </div>

    <!-- Contenido principal -->
    <div v-else-if="store.citaActual" class="space-y-6">
      <!-- Información de la cita -->
      <div class="bg-white shadow rounded-lg p-6">
        <div class="flex justify-between items-start mb-4">
          <div>
            <h2 class="text-2xl font-semibold text-gray-800">
              {{ formatFecha(store.citaActual.fecha) }}
            </h2>
            <p class="text-gray-600">
              {{ formatHora(store.citaActual.horaInicio) }} - {{ formatHora(store.citaActual.horaFin) }}
            </p>
          </div>

          <span
            class="px-4 py-2 text-sm font-semibold rounded-full"
            :class="estadoClass(store.citaActual.estado)"
          >
            {{ store.citaActual.estado }}
          </span>
        </div>

        <div class="border-t pt-4">
          <button
            v-if="store.citaActual.estado !== 'COMPLETADA' && store.citaActual.estado !== 'CANCELADA'"
            @click="completar"
            :disabled="completando"
            class="bg-green-600 text-white px-4 py-2 rounded hover:bg-green-700 disabled:bg-gray-400"
          >
            {{ completando ? 'Completando...' : '✓ Marcar como Completada' }}
          </button>
        </div>
      </div>

      <!-- Información del cliente -->
      <div class="bg-white shadow rounded-lg p-6">
        <div class="flex justify-between items-start mb-4">
          <h3 class="text-xl font-bold text-gray-800 flex items-center gap-2">
            👤 Cliente
          </h3>
          <router-link
            :to="`/fisioterapeuta/clientes/${store.citaActual.cliente.id}/historial`"
            class="text-blue-600 hover:text-blue-800 text-sm font-medium"
          >
            Ver historial completo →
          </router-link>
        </div>

        <div class="grid grid-cols-1 md:grid-cols-2 gap-4">
          <div>
            <p class="text-sm text-gray-500">Nombre completo</p>
            <p class="text-lg font-semibold text-gray-800">
              {{ store.citaActual.cliente.nombre }} {{ store.citaActual.cliente.apellidos }}
            </p>
          </div>
          <div>
            <p class="text-sm text-gray-500">Teléfono</p>
            <p class="text-lg font-semibold text-gray-800">
              {{ store.citaActual.cliente.telefono }}
            </p>
          </div>
          <div>
            <p class="text-sm text-gray-500">Email</p>
            <p class="text-lg font-semibold text-gray-800">
              {{ store.citaActual.cliente.email }}
            </p>
          </div>
        </div>
      </div>

      <!-- Información del servicio -->
      <div class="bg-white shadow rounded-lg p-6">
        <h3 class="text-xl font-bold text-gray-800 mb-4 flex items-center gap-2">
          💼 Servicio
        </h3>

        <div class="grid grid-cols-1 md:grid-cols-3 gap-4">
          <div>
            <p class="text-sm text-gray-500">Servicio</p>
            <p class="text-lg font-semibold text-gray-800">
              {{ store.citaActual.servicio.nombre }}
            </p>
          </div>
          <div>
            <p class="text-sm text-gray-500">Duración</p>
            <p class="text-lg font-semibold text-gray-800">
              {{ store.citaActual.servicio.duracion }} minutos
            </p>
          </div>
          <div>
            <p class="text-sm text-gray-500">Precio</p>
            <p class="text-lg font-semibold text-gray-800">
              {{ formatPrecio(store.citaActual.servicio.precio) }}
            </p>
          </div>
        </div>

        <div v-if="store.citaActual.servicio.descripcion" class="mt-4">
          <p class="text-sm text-gray-500">Descripción</p>
          <p class="text-gray-700">{{ store.citaActual.servicio.descripcion }}</p>
        </div>
      </div>

      <!-- Información de la sala -->
      <div class="bg-white shadow rounded-lg p-6">
        <h3 class="text-xl font-bold text-gray-800 mb-4 flex items-center gap-2">
          🏥 Sala
        </h3>

        <div class="grid grid-cols-1 md:grid-cols-2 gap-4">
          <div>
            <p class="text-sm text-gray-500">Sala asignada</p>
            <p class="text-lg font-semibold text-gray-800">
              {{ store.citaActual.sala.nombre }}
            </p>
          </div>
          <div v-if="store.citaActual.sala.capacidad">
            <p class="text-sm text-gray-500">Capacidad</p>
            <p class="text-lg font-semibold text-gray-800">
              {{ store.citaActual.sala.capacidad }} personas
            </p>
          </div>
        </div>
      </div>

      <!-- Nota de sesión -->
      <div class="bg-white shadow rounded-lg p-6">
        <h3 class="text-xl font-bold text-gray-800 mb-4 flex items-center gap-2">
          📝 Nota de Sesión
        </h3>

        <!-- Mostrar nota existente (modo lectura) -->
        <div v-if="notaCargada && !editandoNota" class="space-y-4">
          <div v-if="notaActual.contenido">
            <p class="text-sm text-gray-500 font-medium">Descripción de la sesión</p>
            <p class="text-gray-800 whitespace-pre-wrap bg-gray-50 p-4 rounded border">{{ notaActual.contenido }}</p>
          </div>

          <div v-if="notaActual.diagnostico" class="grid grid-cols-1 md:grid-cols-2 gap-4">
            <div>
              <p class="text-sm text-gray-500 font-medium">Diagnóstico</p>
              <p class="text-gray-800 bg-gray-50 p-3 rounded border">{{ notaActual.diagnostico }}</p>
            </div>
          </div>

          <div v-if="notaActual.tratamientoAplicado">
            <p class="text-sm text-gray-500 font-medium">Tratamiento aplicado</p>
            <p class="text-gray-800 whitespace-pre-wrap bg-gray-50 p-4 rounded border">{{ notaActual.tratamientoAplicado }}</p>
          </div>

          <div v-if="notaActual.recomendaciones">
            <p class="text-sm text-gray-500 font-medium">Recomendaciones</p>
            <p class="text-gray-800 whitespace-pre-wrap bg-gray-50 p-4 rounded border">{{ notaActual.recomendaciones }}</p>
          </div>

          <div class="border-t pt-4">
            <button
              @click="editandoNota = true"
              class="bg-blue-600 text-white px-4 py-2 rounded hover:bg-blue-700"
            >
              Editar Nota
            </button>
          </div>
        </div>

        <!-- Formulario para crear/editar nota -->
        <div v-else>
          <FormNotaSesion
            :cita-id="store.citaActual.id"
            :nota-existente="notaActual"
            @nota-guardada="handleNotaGuardada"
            @cancelar="handleCancelar"
          />
        </div>

        <!-- Mensaje si no hay nota -->
        <div v-if="!notaCargada && !editandoNota" class="text-center py-6">
          <p class="text-gray-500 mb-4">No hay nota de sesión para esta cita</p>
          <button
            @click="editandoNota = true"
            class="bg-blue-600 text-white px-4 py-2 rounded hover:bg-blue-700"
          >
            + Crear Nota de Sesión
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { useRoute } from 'vue-router';
import { useFisioterapeutaStore } from '@/stores/fisioterapeuta';
import FormNotaSesion from '@/components/fisioterapeuta/FormNotaSesion.vue';
import fisioterapeutaService from '@/services/fisioterapeutaService';

const route = useRoute();
const store = useFisioterapeutaStore();

const completando = ref(false);
const notaActual = ref(null);
const notaCargada = ref(false);
const editandoNota = ref(false);

onMounted(async () => {
  const citaId = parseInt(route.params.id);
  await store.fetchDetalleCita(citaId);
  await cargarNota(citaId);
});

const cargarNota = async (citaId) => {
  try {
    const response = await fisioterapeutaService.getNotaCita(citaId);
    notaActual.value = response.data;
    notaCargada.value = true;
  } catch (error) {
    // Si no existe nota (404), no hacemos nada
    if (error.response?.status !== 404) {
      console.error('Error al cargar nota:', error);
    }
    notaCargada.value = false;
  }
};

const completar = async () => {
  if (!confirm('¿Marcar esta cita como completada?')) return;

  completando.value = true;
  try {
    await store.completarCita(store.citaActual.id);
    alert('Cita marcada como completada exitosamente');
  } catch (error) {
    alert('Error al completar la cita. Por favor, intenta de nuevo.');
  } finally {
    completando.value = false;
  }
};

const handleNotaGuardada = async (notaGuardada) => {
  notaActual.value = notaGuardada;
  notaCargada.value = true;
  editandoNota.value = false;
  alert('Nota guardada exitosamente');
};

const handleCancelar = () => {
  editandoNota.value = false;
};

const formatHora = (horaStr) => {
  if (!horaStr) return '';
  return horaStr.substring(0, 5);
};

const formatFecha = (fechaStr) => {
  if (!fechaStr) return '';
  const fecha = new Date(fechaStr + 'T00:00:00');
  return fecha.toLocaleDateString('es-ES', {
    weekday: 'long',
    day: 'numeric',
    month: 'long',
    year: 'numeric'
  });
};

const formatPrecio = (precio) => {
  if (precio === null || precio === undefined) return '-';
  return `${precio.toFixed(2)}€`;
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
