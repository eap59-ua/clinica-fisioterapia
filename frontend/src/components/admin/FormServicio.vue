<template>
  <div v-if="show" class="fixed inset-0 bg-gray-600 bg-opacity-50 overflow-y-auto h-full w-full flex justify-center items-center z-50">
    <div class="bg-white p-5 rounded-lg shadow-xl w-full max-w-lg">
      <h2 class="text-xl font-bold mb-4">{{ servicio.id ? 'Editar Servicio' : 'Nuevo Servicio' }}</h2>
      <form @submit.prevent="guardar">
        <div class="mb-4">
          <label class="block text-gray-700 text-sm font-bold mb-2">Nombre</label>
          <input v-model="form.nombre" type="text" required class="shadow border rounded w-full py-2 px-3">
        </div>
        <div class="mb-4">
          <label class="block text-gray-700 text-sm font-bold mb-2">Descripción</label>
          <textarea v-model="form.descripcion" class="shadow border rounded w-full py-2 px-3"></textarea>
        </div>
        <div class="grid grid-cols-2 gap-4">
          <div class="mb-4">
            <label class="block text-gray-700 text-sm font-bold mb-2">Precio (€)</label>
            <input v-model="form.precio" type="number" step="0.01" required class="shadow border rounded w-full py-2 px-3">
          </div>
          <div class="mb-4">
            <label class="block text-gray-700 text-sm font-bold mb-2">Duración (min)</label>
            <input v-model="form.duracionMinutos" type="number" required class="shadow border rounded w-full py-2 px-3">
          </div>
        </div>
        <div class="mb-4 flex items-center">
          <input v-model="form.activo" type="checkbox" class="mr-2">
          <span class="text-sm">Servicio Activo</span>
        </div>
        <div class="flex justify-end gap-2 mt-4">
          <button type="button" @click="$emit('close')" class="bg-gray-500 text-white px-4 py-2 rounded">Cancelar</button>
          <button type="submit" class="bg-blue-600 text-white px-4 py-2 rounded">Guardar</button>
        </div>
      </form>
    </div>
  </div>
</template>

<script setup>
import { reactive, watch } from 'vue';
const props = defineProps({ show: Boolean, servicio: Object });
const emit = defineEmits(['close', 'save']);
const form = reactive({ nombre: '', descripcion: '', precio: 0, duracionMinutos: 60, activo: true });

watch(() => props.servicio, (newVal) => {
  if (newVal && newVal.id) Object.assign(form, newVal);
  else {
    form.nombre = ''; form.descripcion = ''; form.precio = 0; form.duracionMinutos = 60; form.activo = true;
  }
});
const guardar = () => emit('save', { ...form });
</script>