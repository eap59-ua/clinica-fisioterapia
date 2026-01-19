<template>
  <div class="p-4 pt-24">
    <div class="flex justify-between items-center mb-6">
      <h1 class="text-2xl font-bold text-gray-800">Gestión de Servicios</h1>
      <button @click="abrirModalCrear" class="bg-green-600 text-white px-4 py-2 rounded">+ Nuevo Servicio</button>
    </div>

    <!-- Filtros -->
    <div class="mb-4">
      <input
        v-model="filtro"
        type="text"
        placeholder="Buscar por nombre..."
        class="border border-gray-300 rounded px-4 py-2 w-full md:w-1/3"
      />
    </div>

    <div class="bg-white shadow-md rounded-lg overflow-hidden">
      <table class="min-w-full leading-normal">
        <thead class="bg-gray-100">
        <tr>
          <th class="px-5 py-3 border-b-2 text-left text-xs font-semibold text-gray-600 uppercase cursor-pointer hover:bg-gray-200" @click="ordenar('nombre')">
            Nombre {{ ordenCampo === 'nombre' ? (ordenDir === 'asc' ? '↑' : '↓') : '' }}
          </th>
          <th class="px-5 py-3 border-b-2 text-left text-xs font-semibold text-gray-600 uppercase cursor-pointer hover:bg-gray-200" @click="ordenar('precio')">
            Precio {{ ordenCampo === 'precio' ? (ordenDir === 'asc' ? '↑' : '↓') : '' }}
          </th>
          <th class="px-5 py-3 border-b-2 text-left text-xs font-semibold text-gray-600 uppercase cursor-pointer hover:bg-gray-200" @click="ordenar('duracionMinutos')">
            Duración {{ ordenCampo === 'duracionMinutos' ? (ordenDir === 'asc' ? '↑' : '↓') : '' }}
          </th>
          <th class="px-5 py-3 border-b-2 text-left text-xs font-semibold text-gray-600 uppercase">Estado</th>
          <th class="px-5 py-3 border-b-2 text-left text-xs font-semibold text-gray-600 uppercase">Acciones</th>
        </tr>
        </thead>
        <tbody>
        <tr v-for="servicio in serviciosFiltrados" :key="servicio.id" class="border-b">
          <td class="px-5 py-5 bg-white text-sm">{{ servicio.nombre }}</td>
          <td class="px-5 py-5 bg-white text-sm">{{ servicio.precio }} €</td>
          <td class="px-5 py-5 bg-white text-sm">{{ servicio.duracionMinutos }} min</td>
          <td class="px-5 py-5 bg-white text-sm">
            <span v-if="servicio.activo" class="text-green-600 font-bold">Activo</span>
            <span v-else class="text-red-600">Inactivo</span>
          </td>
          <td class="px-5 py-5 bg-white text-sm">
            <button @click="abrirModalEditar(servicio)" class="text-blue-600 mr-3">Editar</button>
            <button @click="eliminar(servicio.id)" class="text-red-600">Eliminar</button>
          </td>
        </tr>
        </tbody>
      </table>
    </div>
    <FormServicio :show="mostrarModal" :servicio="servicioEditando" @close="mostrarModal = false" @save="guardar" />
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue';
import axios from 'axios';
import FormServicio from '@/components/admin/FormServicio.vue';

const servicios = ref([]);
const mostrarModal = ref(false);
const servicioEditando = ref({});
const filtro = ref('');
const ordenCampo = ref('nombre');
const ordenDir = ref('asc');
const API_URL = 'http://localhost:8080/api/admin/servicios';

const getAuthConfig = () => ({ headers: { 'Authorization': `Bearer ${localStorage.getItem('token')}` } });

const serviciosFiltrados = computed(() => {
  let resultado = servicios.value;

  // Filtrar por búsqueda
  if (filtro.value) {
    resultado = resultado.filter(s =>
      s.nombre.toLowerCase().includes(filtro.value.toLowerCase())
    );
  }

  // Ordenar
  resultado = [...resultado].sort((a, b) => {
    let valorA = a[ordenCampo.value];
    let valorB = b[ordenCampo.value];

    if (typeof valorA === 'string') {
      valorA = valorA.toLowerCase();
      valorB = valorB.toLowerCase();
    }

    if (ordenDir.value === 'asc') {
      return valorA > valorB ? 1 : -1;
    } else {
      return valorA < valorB ? 1 : -1;
    }
  });

  return resultado;
});

const ordenar = (campo) => {
  if (ordenCampo.value === campo) {
    ordenDir.value = ordenDir.value === 'asc' ? 'desc' : 'asc';
  } else {
    ordenCampo.value = campo;
    ordenDir.value = 'asc';
  }
};

const cargar = async () => {
  try { servicios.value = (await axios.get(API_URL, getAuthConfig())).data; }
  catch (e) { console.error(e); }
};

const abrirModalCrear = () => { servicioEditando.value = {}; mostrarModal.value = true; };
const abrirModalEditar = (s) => { servicioEditando.value = { ...s }; mostrarModal.value = true; };

const guardar = async (datos) => {
  try {
    if (servicioEditando.value.id) await axios.put(`${API_URL}/${servicioEditando.value.id}`, datos, getAuthConfig());
    else await axios.post(API_URL, datos, getAuthConfig());
    await cargar(); mostrarModal.value = false;
  } catch (e) { alert('Error al guardar'); }
};

const eliminar = async (id) => {
  if (confirm('¿Eliminar servicio?')) {
    await axios.delete(`${API_URL}/${id}`, getAuthConfig());
    await cargar();
  }
};

onMounted(cargar);
</script>