<template>
  <div class="usuarios-management p-4">
    <div class="flex justify-between items-center mb-6">
      <h1 class="text-2xl font-bold">Gestión de Usuarios</h1>
      <button @click="abrirModalCrear" class="bg-green-600 text-white px-4 py-2 rounded hover:bg-green-700">
        + Nuevo Usuario
      </button>
    </div>

    <div class="overflow-x-auto bg-white shadow-md rounded-lg">
      <table class="min-w-full leading-normal">
        <thead>
        <tr>
          <th class="px-5 py-3 border-b-2 border-gray-200 bg-gray-100 text-left text-xs font-semibold text-gray-600 uppercase tracking-wider">ID</th>
          <th class="px-5 py-3 border-b-2 border-gray-200 bg-gray-100 text-left text-xs font-semibold text-gray-600 uppercase tracking-wider">Usuario</th>
          <th class="px-5 py-3 border-b-2 border-gray-200 bg-gray-100 text-left text-xs font-semibold text-gray-600 uppercase tracking-wider">DNI / Email</th>
          <th class="px-5 py-3 border-b-2 border-gray-200 bg-gray-100 text-left text-xs font-semibold text-gray-600 uppercase tracking-wider">Rol</th>
          <th class="px-5 py-3 border-b-2 border-gray-200 bg-gray-100 text-left text-xs font-semibold text-gray-600 uppercase tracking-wider">Estado</th>
          <th class="px-5 py-3 border-b-2 border-gray-200 bg-gray-100 text-left text-xs font-semibold text-gray-600 uppercase tracking-wider">Acciones</th>
        </tr>
        </thead>
        <tbody>
        <tr v-for="usuario in usuarios" :key="usuario.id">
          <td class="px-5 py-5 border-b border-gray-200 bg-white text-sm">{{ usuario.id }}</td>
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
                <span class="relative text-xs">{{ usuario.rol }}</span>
              </span>
          </td>
          <td class="px-5 py-5 border-b border-gray-200 bg-white text-sm">
            <span v-if="usuario.activo" class="text-green-600 font-bold text-xs">Activo</span>
            <span v-else class="text-red-600 font-bold text-xs">Inactivo</span>
          </td>
          <td class="px-5 py-5 border-b border-gray-200 bg-white text-sm">
            <button @click="abrirModalEditar(usuario)" class="text-blue-600 hover:text-blue-900 mr-3">Editar</button>
            <button @click="eliminarUsuario(usuario.id)" class="text-red-600 hover:text-red-900">Eliminar</button>
          </td>
        </tr>
        </tbody>
      </table>
    </div>

    <FormUsuario
        :show="mostrarModal"
        :usuario="usuarioEditando"
        @close="mostrarModal = false"
        @save="guardarUsuario"
    />
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import axios from 'axios';
import FormUsuario from '@/components/admin/FormUsuario.vue'; // Ajusta la ruta si es necesario

const usuarios = ref([]);
const mostrarModal = ref(false);
const usuarioEditando = ref({}); // Objeto vacío para crear, lleno para editar

const API_URL = 'http://localhost:8080/api/admin/usuarios';

// Cargar usuarios
const cargarUsuarios = async () => {
  try {
    const response = await axios.get(API_URL);
    usuarios.value = response.data;
  } catch (error) {
    console.error('Error cargando usuarios:', error);
  }
};

// Abrir modal para CREAR
const abrirModalCrear = () => {
  usuarioEditando.value = {}; // Objeto vacío
  mostrarModal.value = true;
};

// Abrir modal para EDITAR
const abrirModalEditar = (usuario) => {
  usuarioEditando.value = { ...usuario }; // Copia para no editar directamente en la tabla
  mostrarModal.value = true;
};

// Guardar (Crear o Editar según si tiene ID)
const guardarUsuario = async (datosUsuario) => {
  try {
    if (usuarioEditando.value.id) {
      // EDITAR (PUT)
      await axios.put(`${API_URL}/${usuarioEditando.value.id}`, datosUsuario);
    } else {
      // CREAR (POST)
      await axios.post(API_URL, datosUsuario);
    }
    // Refrescar tabla y cerrar modal
    await cargarUsuarios();
    mostrarModal.value = false;
    alert('Operación realizada con éxito');
  } catch (error) {
    console.error('Error al guardar:', error);
    // Mostrar mensaje de error del backend (ej: "Email repetido")
    alert(error.response?.data || 'Error al guardar usuario');
  }
};

// Eliminar
const eliminarUsuario = async (id) => {
  if (!confirm('¿Estás seguro de que quieres eliminar este usuario?')) return;

  try {
    await axios.delete(`${API_URL}/${id}`);
    await cargarUsuarios();
  } catch (error) {
    console.error('Error eliminando:', error);
    alert('No se pudo eliminar el usuario');
  }
};

onMounted(() => {
  cargarUsuarios();
});
</script>