<template>
  <div class="salas-management p-4 min-h-screen">
    <div class="flex justify-between items-center mb-6">
      <h1 class="text-2xl font-bold text-gray-800">Gestión de Salas</h1>
      <button @click="abrirModalCrear" class="bg-green-600 text-white px-4 py-2 rounded hover:bg-green-700 transition duration-200">
        + Nueva Sala
      </button>
    </div>

    <div class="overflow-x-auto bg-white shadow-md rounded-lg">
      <table class="min-w-full leading-normal">
        <thead>
        <tr>
          <th class="px-5 py-3 border-b-2 border-gray-200 bg-gray-100 text-left text-xs font-semibold text-gray-600 uppercase tracking-wider">ID</th>
          <th class="px-5 py-3 border-b-2 border-gray-200 bg-gray-100 text-left text-xs font-semibold text-gray-600 uppercase tracking-wider">Nombre</th>
          <th class="px-5 py-3 border-b-2 border-gray-200 bg-gray-100 text-left text-xs font-semibold text-gray-600 uppercase tracking-wider">Capacidad (Personas)</th>
          <th class="px-5 py-3 border-b-2 border-gray-200 bg-gray-100 text-left text-xs font-semibold text-gray-600 uppercase tracking-wider">Acciones</th>
        </tr>
        </thead>
        <tbody>
        <tr v-for="sala in salas" :key="sala.id">
          <td class="px-5 py-5 border-b border-gray-200 bg-white text-sm">{{ sala.id }}</td>
          <td class="px-5 py-5 border-b border-gray-200 bg-white text-sm">
            <p class="text-gray-900 font-bold whitespace-no-wrap">{{ sala.nombre }}</p>
          </td>
          <td class="px-5 py-5 border-b border-gray-200 bg-white text-sm">
            <p class="text-gray-900 whitespace-no-wrap">{{ sala.capacidad }}</p>
          </td>
          <td class="px-5 py-5 border-b border-gray-200 bg-white text-sm">
            <button @click="abrirModalEditar(sala)" class="text-blue-600 hover:text-blue-900 mr-3">Editar</button>
            <button @click="eliminarSala(sala.id)" class="text-red-600 hover:text-red-900">Eliminar</button>
          </td>
        </tr>
        <tr v-if="salas.length === 0">
          <td colspan="4" class="px-5 py-5 border-b border-gray-200 bg-white text-sm text-center text-gray-500">
            No hay salas registradas.
          </td>
        </tr>
        </tbody>
      </table>
    </div>

    <FormSala
        :show="mostrarModal"
        :sala="salaEditando"
        @close="mostrarModal = false"
        @save="guardarSala"
    />
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import axios from 'axios';
import FormSala from '@/components/admin/FormSala.vue'; // Asegúrate de crear este archivo después

const salas = ref([]);
const mostrarModal = ref(false);
const salaEditando = ref({});

const API_URL = 'http://localhost:8080/api/admin/salas';

// Configuración del Header con Token
const getAuthConfig = () => {
  const token = localStorage.getItem('token');
  return {
    headers: {
      'Authorization': `Bearer ${token}`,
      'Content-Type': 'application/json'
    }
  };
};

const cargarSalas = async () => {
  try {
    const response = await axios.get(API_URL, getAuthConfig());
    salas.value = response.data;
  } catch (error) {
    console.error('Error cargando salas:', error);
  }
};

const abrirModalCrear = () => {
  salaEditando.value = {};
  mostrarModal.value = true;
};

const abrirModalEditar = (sala) => {
  salaEditando.value = { ...sala };
  mostrarModal.value = true;
};

const guardarSala = async (datosSala) => {
  try {
    if (salaEditando.value.id) {
      await axios.put(`${API_URL}/${salaEditando.value.id}`, datosSala, getAuthConfig());
    } else {
      await axios.post(API_URL, datosSala, getAuthConfig());
    }

    await cargarSalas();
    mostrarModal.value = false;
  } catch (error) {
    console.error('Error al guardar:', error);
    alert('Error al guardar la sala');
  }
};

const eliminarSala = async (id) => {
  if (!confirm('¿Estás seguro de que quieres eliminar esta sala?')) return;

  try {
    await axios.delete(`${API_URL}/${id}`, getAuthConfig());
    await cargarSalas();
  } catch (error) {
    console.error('Error eliminando:', error);
    alert('No se pudo eliminar la sala');
  }
};

onMounted(() => {
  cargarSalas();
});
</script>