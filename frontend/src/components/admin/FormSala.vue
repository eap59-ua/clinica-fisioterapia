<template>
  <div v-if="show" class="fixed inset-0 bg-gray-600 bg-opacity-50 overflow-y-auto h-full w-full flex justify-center items-center z-50">
    <div class="bg-white p-5 rounded-lg shadow-xl w-full max-w-lg">

      <h2 class="text-xl font-bold mb-4">{{ sala.id ? 'Editar Sala' : 'Nueva Sala' }}</h2>

      <form @submit.prevent="guardar">

        <div class="mb-4">
          <label class="block text-gray-700 text-sm font-bold mb-2">Nombre de la Sala</label>
          <input v-model="form.nombre" type="text" placeholder="Ej: Consulta 1, Gimnasio..." required class="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline">
        </div>

        <div class="mb-4">
          <label class="block text-gray-700 text-sm font-bold mb-2">Capacidad (Personas)</label>
          <input v-model="form.capacidad" type="number" min="1" required class="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline">
        </div>

        <div class="flex justify-end gap-2 mt-4">
          <button type="button" @click="$emit('close')" class="bg-gray-500 hover:bg-gray-700 text-white font-bold py-2 px-4 rounded">
            Cancelar
          </button>
          <button type="submit" class="bg-blue-600 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded">
            Guardar
          </button>
        </div>
      </form>
    </div>
  </div>
</template>

<script setup>
import { reactive, watch } from 'vue';

const props = defineProps({
  show: Boolean,
  sala: Object
});

const emit = defineEmits(['close', 'save']);

const form = reactive({
  nombre: '',
  capacidad: 1
});

watch(() => props.sala, (newVal) => {
  if (newVal && newVal.id) {
    Object.assign(form, newVal);
  } else {
    form.nombre = '';
    form.capacidad = 1;
  }
});

const guardar = () => {
  emit('save', { ...form });
};
</script>