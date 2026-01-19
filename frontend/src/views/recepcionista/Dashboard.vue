<template>
  <div class="p-6">
    <div class="flex flex-col md:flex-row justify-between items-center mb-6 gap-4">
      <h1 class="text-3xl font-bold text-gray-800">Agenda de Recepción</h1>

      <div class="flex flex-wrap gap-3 items-center justify-end">
        <input
          type="date"
          v-model="fechaSeleccionada"
          @change="cargarCitas"
          class="border p-2 rounded shadow-sm focus:outline-none focus:ring-2 focus:ring-blue-500"
          title="Seleccionar fecha"
        />

        <router-link
          to="/recepcionista/configuracion-horarios"
          class="bg-gray-600 text-white px-3 py-2 rounded hover:bg-gray-700 shadow-sm flex items-center transition gap-2 text-sm font-medium"
          title="Configurar horas de apertura y cierre"
        >
          Horarios
        </router-link>

        <router-link
          to="/recepcionista/bloqueos"
          class="bg-red-500 text-white px-3 py-2 rounded hover:bg-red-600 shadow-sm flex items-center transition gap-2 text-sm font-medium"
          title="Gestionar festivos y vacaciones"
        >
          Festivos
        </router-link>

        <router-link
          to="/recepcionista/citas-management"
          class="bg-indigo-600 text-white px-3 py-2 rounded hover:bg-indigo-700 shadow-sm flex items-center transition gap-2 text-sm font-medium"
        >
          ⚙️ Gestión Avanzada
        </router-link>

        <router-link
          to="/recepcionista/crear-cita"
          class="bg-blue-600 text-white px-4 py-2 rounded hover:bg-blue-700 shadow-sm flex items-center transition gap-2 text-sm font-bold"
        >
          + Nueva Cita
        </router-link>
      </div>
    </div>

    <div class="bg-white shadow rounded-lg overflow-hidden">
      <table class="min-w-full leading-normal">
        <thead>
          <tr>
            <th class="px-5 py-3 border-b-2 bg-gray-100 text-left text-xs font-semibold text-gray-600 uppercase">Hora</th>
            <th class="px-5 py-3 border-b-2 bg-gray-100 text-left text-xs font-semibold text-gray-600 uppercase">Paciente</th>
            <th class="px-5 py-3 border-b-2 bg-gray-100 text-left text-xs font-semibold text-gray-600 uppercase">Fisioterapeuta</th>
            <th class="px-5 py-3 border-b-2 bg-gray-100 text-left text-xs font-semibold text-gray-600 uppercase">Estado</th>
            <th class="px-5 py-3 border-b-2 bg-gray-100 text-left text-xs font-semibold text-gray-600 uppercase">Acciones</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="cita in citas" :key="cita.id" :class="{'opacity-50': cita.estado === 'CANCELADA'}">

            <td class="px-5 py-5 border-b border-gray-200 bg-white text-sm">
              <span class="font-bold text-gray-700">{{ formatHora(cita.horaInicio) }}</span>
              <span class="text-gray-400 text-xs block">hasta {{ formatHora(cita.horaFin) }}</span>
            </td>

            <td class="px-5 py-5 border-b border-gray-200 bg-white text-sm">
              <div class="font-bold">{{ cita.cliente?.nombre }} {{ cita.cliente?.apellidos }}</div>
              <div class="text-gray-500 text-xs">{{ cita.cliente?.telefono }}</div>
            </td>

            <td class="px-5 py-5 border-b border-gray-200 bg-white text-sm">
              {{ cita.fisioterapeuta?.nombre }}
            </td>

            <td class="px-5 py-5 border-b border-gray-200 bg-white text-sm">
              <span :class="estadoClass(cita.estado)" class="px-2 py-1 text-xs font-semibold rounded-full">
                {{ cita.estado }}
              </span>
            </td>

            <td class="px-5 py-5 border-b border-gray-200 bg-white text-sm">
              <div v-if="cita.estado === 'PENDIENTE'" class="flex gap-2 items-center">
                <button
                  @click="cambiarEstado(cita.id, 'COMPLETADA')"
                  class="text-green-600 hover:text-green-900 font-medium text-xs border border-green-600 px-2 py-1 rounded hover:bg-green-50 transition"
                  title="Marcar que el cliente ha llegado y pagado"
                >
                  Confirmar
                </button>

                <router-link
                  :to="`/recepcionista/editar-cita/${cita.id}`"
                  class="text-blue-600 hover:text-blue-900 font-medium text-xs border border-blue-600 px-2 py-1 rounded hover:bg-blue-50 transition no-underline flex items-center justify-center"
                >
                  Editar
                </router-link>

                <button
                  @click="cambiarEstado(cita.id, 'CANCELADA')"
                  class="text-red-600 hover:text-red-900 font-medium text-xs px-2 py-1 transition"
                >
                  Cancelar
                </button>
              </div>
              <span v-else class="text-gray-400 text-xs italic">Sin acciones</span>
            </td>
          </tr>

          <tr v-if="citas.length === 0">
            <td colspan="5" class="px-5 py-10 text-center text-gray-500">
              No hay citas programadas para el {{ fechaSeleccionada }}
            </td>
          </tr>
        </tbody>
      </table>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import recepcionistaService from '@/services/recepcionistaService';

const citas = ref([]);
// Inicializamos la fecha con el día de hoy en formato YYYY-MM-DD
const fechaSeleccionada = ref(new Date().toISOString().split('T')[0]);

// Cargar citas de la fecha seleccionada
const cargarCitas = async () => {
  try {
    const res = await recepcionistaService.getCitasDia(fechaSeleccionada.value);
    citas.value = res.data;
  } catch (error) {
    console.error("Error cargando citas:", error);
  }
};

// Cambiar estado (Confirmar / Cancelar)
const cambiarEstado = async (id, nuevoEstado) => {
  const mensaje = nuevoEstado === 'COMPLETADA'
    ? '¿Confirmar asistencia y pago del cliente?'
    : '¿Seguro que deseas cancelar esta cita?';

  if (!confirm(mensaje)) return;

  try {
    await recepcionistaService.cambiarEstado(id, nuevoEstado);
    await cargarCitas(); // Recargar tabla para refrescar la vista
  } catch (error) {
    console.error(error);
    alert("Error al actualizar el estado");
  }
};

// Utilidad para quitar los segundos (10:00:00 -> 10:00)
const formatHora = (horaStr) => {
  if (!horaStr) return '';
  return horaStr.substring(0, 5);
};

// Estilos CSS dinámicos según el estado
const estadoClass = (estado) => {
  switch(estado) {
    case 'COMPLETADA': return 'bg-green-100 text-green-800';
    case 'CANCELADA': return 'bg-red-100 text-red-800';
    case 'PENDIENTE': return 'bg-blue-100 text-blue-800';
    default: return 'bg-gray-100 text-gray-800';
  }
};

// Al montar el componente, cargamos las citas de hoy
onMounted(() => {
  cargarCitas();
});
</script>