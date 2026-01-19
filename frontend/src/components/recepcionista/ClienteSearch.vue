<template>
  <div class="relative">
    <label class="block text-sm font-semibold text-gray-700 mb-1">Buscar Paciente</label>

    <div class="flex gap-2">
      <input
        type="text"
        v-model="busqueda"
        @input="buscar"
        placeholder="Nombre, Apellidos o DNI..."
        class="w-full border border-gray-300 p-2 rounded focus:ring-2 focus:ring-blue-500 focus:outline-none"
        :class="{'border-green-500 bg-green-50': clienteSeleccionado}"
      />
      <button
        v-if="clienteSeleccionado"
        @click="limpiar"
        type="button"
        class="text-red-500 text-sm hover:text-red-700 font-bold"
      >
        ✕
      </button>
    </div>

    <ul v-if="resultados.length > 0 && !clienteSeleccionado" class="absolute z-10 w-full bg-white border border-gray-300 rounded shadow-lg max-h-60 overflow-y-auto mt-1">
      <li
        v-for="cliente in resultados"
        :key="cliente.id"
        @click="seleccionar(cliente)"
        class="p-2 hover:bg-blue-100 cursor-pointer border-b last:border-b-0"
      >
        <div class="font-bold text-gray-800">{{ cliente.nombre }} {{ cliente.apellidos }}</div>
        <div class="text-xs text-gray-500">DNI: {{ cliente.dni }} | Tlf: {{ cliente.telefono }}</div>
      </li>
    </ul>

    <div v-if="busqueda.length > 2 && resultados.length === 0 && !clienteSeleccionado" class="text-xs text-gray-500 mt-1">
      No se encontraron pacientes.
    </div>
  </div>
</template>

<script setup>
import { ref, watch } from 'vue';
import recepcionistaService from '@/services/recepcionistaService';

// Props: permite pasar un cliente inicial (para cuando editamos)
const props = defineProps(['clienteInicial']);
const emit = defineEmits(['seleccionar-cliente']);

const busqueda = ref('');
const resultados = ref([]);
const clienteSeleccionado = ref(null);

// Si nos pasan un cliente inicial (ej. al editar), lo mostramos
watch(() => props.clienteInicial, (newVal) => {
  if (newVal && newVal.nombre) {
    clienteSeleccionado.value = newVal;
    busqueda.value = `${newVal.nombre} ${newVal.apellidos}`;
  }
}, { immediate: true });

const buscar = async () => {
  // Si el usuario borra, reiniciamos
  if (busqueda.value.length < 2) {
    resultados.value = [];
    clienteSeleccionado.value = null;
    emit('seleccionar-cliente', null);
    return;
  }

  // Si ya hay uno seleccionado y escribe, asumimos que quiere buscar otro
  if (clienteSeleccionado.value) {
    clienteSeleccionado.value = null;
    emit('seleccionar-cliente', null);
  }

  try {
    const res = await recepcionistaService.buscarClientes(busqueda.value);
    resultados.value = res.data;
  } catch (error) {
    console.error("Error buscando clientes", error);
  }
};

const seleccionar = (cliente) => {
  clienteSeleccionado.value = cliente;
  busqueda.value = `${cliente.nombre} ${cliente.apellidos}`;
  resultados.value = []; // Ocultar lista
  emit('seleccionar-cliente', cliente); // Avisar al padre
};

const limpiar = () => {
  busqueda.value = '';
  clienteSeleccionado.value = null;
  resultados.value = [];
  emit('seleccionar-cliente', null);
};
</script>