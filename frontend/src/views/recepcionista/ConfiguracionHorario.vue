<template>
  <div class="p-6">
    <div class="flex justify-between items-center mb-6">
      <h1 class="text-3xl font-bold text-gray-800">⚙️ Horarios de Apertura</h1>
      <router-link
        to="/recepcionista/dashboard"
        class="text-gray-600 hover:text-gray-900 flex items-center gap-2">
        ← Volver al Dashboard
      </router-link>
    </div>

    <div class="bg-white shadow rounded-lg overflow-hidden">
      <table class="min-w-full leading-normal">
        <thead>
          <tr>
            <th class="px-5 py-3 border-b-2 bg-gray-100 text-left text-xs font-semibold text-gray-600 uppercase">Día Semana</th>
            <th class="px-5 py-3 border-b-2 bg-gray-100 text-left text-xs font-semibold text-gray-600 uppercase">Hora Apertura</th>
            <th class="px-5 py-3 border-b-2 bg-gray-100 text-left text-xs font-semibold text-gray-600 uppercase">Hora Cierre</th>
            <th class="px-5 py-3 border-b-2 bg-gray-100 text-left text-xs font-semibold text-gray-600 uppercase">Acción</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="dia in horarios" :key="dia.diaSemana">
            <td class="px-5 py-5 border-b border-gray-200 bg-white text-sm font-bold text-gray-700">
              {{ nombreDia(dia.diaSemana) }}
            </td>
            <td class="px-5 py-5 border-b border-gray-200 bg-white text-sm">
              <input
                type="time"
                v-model="dia.horaApertura"
                class="border rounded p-2 focus:ring-2 focus:ring-blue-500 outline-none w-full"
              >
            </td>
            <td class="px-5 py-5 border-b border-gray-200 bg-white text-sm">
              <input
                type="time"
                v-model="dia.horaCierre"
                class="border rounded p-2 focus:ring-2 focus:ring-blue-500 outline-none w-full"
              >
            </td>
            <td class="px-5 py-5 border-b border-gray-200 bg-white text-sm">
              <button
                @click="guardarCambio(dia)"
                class="bg-blue-600 text-white px-3 py-1 rounded hover:bg-blue-700 transition shadow-sm text-xs font-bold"
              >
                Guardar
              </button>
            </td>
          </tr>
        </tbody>
      </table>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import horarioService from '@/services/horarioService';

const horarios = ref([]);
const diasNombres = { 1: "Lunes", 2: "Martes", 3: "Miércoles", 4: "Jueves", 5: "Viernes", 6: "Sábado", 7: "Domingo" };

const cargarHorarios = async () => {
  try {
    const res = await horarioService.obtenerHorarioSemanal();
    // Ordenamos por día de la semana (1 al 7)
    horarios.value = res.data.sort((a, b) => a.diaSemana - b.diaSemana);
  } catch (error) {
    console.error("Error cargando horarios", error);
  }
};

const guardarCambio = async (dia) => {
  try {
    await horarioService.actualizarDia(dia);
    alert(`Horario del ${nombreDia(dia.diaSemana)} actualizado.`);
  } catch (error) {
    console.error(error);
    alert("Error al actualizar");
  }
};

const nombreDia = (num) => diasNombres[num] || 'Desconocido';

onMounted(() => {
  cargarHorarios();
});
</script>