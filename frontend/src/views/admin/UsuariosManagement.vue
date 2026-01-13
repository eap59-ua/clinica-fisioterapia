<template>
  <div class="usuarios-management p-4">
    <div class="flex justify-between items-center mb-6">
      <h1 class="text-2xl font-bold">Gestión de Usuarios</h1>
      <button class="bg-green-600 text-white px-4 py-2 rounded hover:bg-green-700">
        + Nuevo Usuario
      </button>
    </div>

    <div class="overflow-x-auto bg-white shadow-md rounded-lg">
      <table class="min-w-full leading-normal">
        <thead>
        <tr>
          <th class="px-5 py-3 border-b-2 border-gray-200 bg-gray-100 text-left text-xs font-semibold text-gray-600 uppercase tracking-wider">
            ID
          </th>
          <th class="px-5 py-3 border-b-2 border-gray-200 bg-gray-100 text-left text-xs font-semibold text-gray-600 uppercase tracking-wider">
            Usuario
          </th>
          <th class="px-5 py-3 border-b-2 border-gray-200 bg-gray-100 text-left text-xs font-semibold text-gray-600 uppercase tracking-wider">
            DNI / Email
          </th>
          <th class="px-5 py-3 border-b-2 border-gray-200 bg-gray-100 text-left text-xs font-semibold text-gray-600 uppercase tracking-wider">
            Rol
          </th>
          <th class="px-5 py-3 border-b-2 border-gray-200 bg-gray-100 text-left text-xs font-semibold text-gray-600 uppercase tracking-wider">
            Estado
          </th>
          <th class="px-5 py-3 border-b-2 border-gray-200 bg-gray-100 text-left text-xs font-semibold text-gray-600 uppercase tracking-wider">
            Acciones
          </th>
        </tr>
        </thead>
        <tbody>
        <tr v-for="usuario in usuarios" :key="usuario.id">
          <td class="px-5 py-5 border-b border-gray-200 bg-white text-sm">
            <p class="text-gray-900 whitespace-no-wrap">{{ usuario.id }}</p>
          </td>
          <td class="px-5 py-5 border-b border-gray-200 bg-white text-sm">
            <p class="text-gray-900 font-bold whitespace-no-wrap">{{ usuario.nombre }} {{ usuario.apellidos }}</p>
          </td>
          <td class="px-5 py-5 border-b border-gray-200 bg-white text-sm">
            <p class="text-gray-900 whitespace-no-wrap">{{ usuario.dni }}</p>
            <p class="text-gray-600 text-xs">{{ usuario.email }}</p>
          </td>
          <td class="px-5 py-5 border-b border-gray-200 bg-white text-sm">
              <span class="relative inline-block px-3 py-1 font-semibold leading-tight text-blue-900">
                <span aria-hidden class="absolute inset-0 bg-blue-200 opacity-50 rounded-full"></span>
                <span class="relative">{{ usuario.rol }}</span>
              </span>
          </td>
          <td class="px-5 py-5 border-b border-gray-200 bg-white text-sm">
            <span v-if="usuario.activo" class="text-green-600 font-bold">Activo</span>
            <span v-else class="text-red-600 font-bold">Inactivo</span>
          </td>
          <td class="px-5 py-5 border-b border-gray-200 bg-white text-sm">
            <button class="text-blue-600 hover:text-blue-900 mr-3">Editar</button>
            <button class="text-red-600 hover:text-red-900">Eliminar</button>
          </td>
        </tr>
        </tbody>
      </table>

      <div v-if="usuarios.length === 0" class="p-4 text-center text-gray-500">
        No hay usuarios registrados o no se pudo conectar con el servidor.
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import axios from 'axios';

const usuarios = ref([]);

const cargarUsuarios = async () => {
  try {
    const response = await axios.get('http://localhost:8080/api/admin/usuarios');
    usuarios.value = response.data;
  } catch (error) {
    console.error('Error al cargar usuarios:', error);
    alert('Error de conexión con el backend');
  }
};

onMounted(() => {
  cargarUsuarios();
});
</script>