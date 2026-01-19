<template>
  <div class="p-6 max-w-4xl mx-auto">
    <div class="flex justify-between items-center mb-6">
      <h1 class="text-3xl font-bold text-gray-800">⛔ Gestión de Bloqueos</h1>
      <router-link to="/recepcionista/dashboard" class="text-blue-600 hover:underline">Volver</router-link>
    </div>

    <div class="bg-white p-6 rounded-lg shadow mb-8">
      <h2 class="text-xl font-semibold mb-4 text-gray-700">Nuevo Bloqueo (Festivo o Vacaciones)</h2>
      <form @submit.prevent="crearBloqueo" class="grid grid-cols-1 md:grid-cols-2 gap-4">

        <div>
          <label class="block text-sm font-medium text-gray-700">Fecha Inicio</label>
          <input type="datetime-local" v-model="bloqueo.fechaInicio" required
            class="mt-1 block w-full border border-gray-300 rounded-md shadow-sm p-2 focus:ring-red-500 focus:border-red-500">
        </div>

        <div>
          <label class="block text-sm font-medium text-gray-700">Fecha Fin</label>
          <input type="datetime-local" v-model="bloqueo.fechaFin" required
            class="mt-1 block w-full border border-gray-300 rounded-md shadow-sm p-2 focus:ring-red-500 focus:border-red-500">
        </div>

        <div class="md:col-span-2">
          <label class="block text-sm font-medium text-gray-700">Motivo</label>
          <input type="text" v-model="bloqueo.motivo" placeholder="Ej: Navidad, Vacaciones Dr. Juan..." required
            class="mt-1 block w-full border border-gray-300 rounded-md shadow-sm p-2 focus:ring-red-500 focus:border-red-500">
        </div>

        <div>
          <label class="block text-sm font-medium text-gray-700">Tipo de Bloqueo</label>
          <select v-model="bloqueo.tipo" class="mt-1 block w-full border border-gray-300 rounded-md shadow-sm p-2">
            <option value="GLOBAL">🏢 Clínica Cerrada (Festivo)</option>
            <option value="PERSONAL">👨‍⚕️ Fisio de Vacaciones</option>
          </select>
        </div>

        <div v-if="bloqueo.tipo === 'PERSONAL'">
          <label class="block text-sm font-medium text-gray-700">ID Fisioterapeuta</label>
          <input type="number" v-model="bloqueo.fisioterapeutaId" placeholder="ID del fisio"
            class="mt-1 block w-full border border-gray-300 rounded-md shadow-sm p-2">
           </div>

        <div class="md:col-span-2 mt-4">
          <button type="submit"
            class="w-full bg-red-600 text-white font-bold py-2 px-4 rounded hover:bg-red-700 transition">
            Crear Bloqueo
          </button>
        </div>
      </form>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue';
import horarioService from '@/services/horarioService';

const bloqueo = ref({
  fechaInicio: '',
  fechaFin: '',
  motivo: '',
  tipo: 'GLOBAL',
  fisioterapeutaId: null
});

const crearBloqueo = async () => {
  try {
    await horarioService.crearBloqueo(bloqueo.value);
    alert("Bloqueo creado correctamente");
    // Resetear formulario
    bloqueo.value = { fechaInicio: '', fechaFin: '', motivo: '', tipo: 'GLOBAL', fisioterapeutaId: null };
  } catch (error) {
    console.error(error);
    alert("Error al crear. Verifica que la fecha fin sea posterior a la de inicio.");
  }
};
</script>