<template>
  <div class="usuarios-management p-4 pt-24 min-h-screen">
    <div class="flex justify-between items-center mb-6">
      <h1 class="text-2xl font-bold text-gray-800">Gestión de Usuarios</h1>
      <button @click="abrirModalCrear" class="bg-green-600 text-white px-4 py-2 rounded hover:bg-green-700 transition duration-200">
        + Nuevo Usuario
      </button>
    </div>

    <!-- Filtros -->
    <div class="mb-4">
      <input v-model="filtro" type="text" placeholder="Buscar por nombre, email o DNI..." class="border border-gray-300 rounded px-4 py-2 w-full md:w-1/3" />
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
        <tr v-for="usuario in usuariosFiltrados" :key="usuario.id">
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
        <tr v-if="usuarios.length === 0">
          <td colspan="6" class="px-5 py-5 border-b border-gray-200 bg-white text-sm text-center text-gray-500">
            No se han encontrado usuarios o no hay conexión.
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
import { ref, computed, onMounted } from 'vue';
import axios from 'axios';
import FormUsuario from '@/components/admin/FormUsuario.vue';

const usuarios = ref([]);
const mostrarModal = ref(false);
const usuarioEditando = ref({});
const filtro = ref('');

const usuariosFiltrados = computed(() => {
  if (!filtro.value) return usuarios.value;
  return usuarios.value.filter(u =>
    u.nombre?.toLowerCase().includes(filtro.value.toLowerCase()) ||
    u.apellidos?.toLowerCase().includes(filtro.value.toLowerCase()) ||
    u.email?.toLowerCase().includes(filtro.value.toLowerCase()) ||
    u.dni?.toLowerCase().includes(filtro.value.toLowerCase())
  );
});

const API_URL = 'http://localhost:8080/api/admin/usuarios';

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

// Cargar usuarios
const cargarUsuarios = async () => {
  try {
    const response = await axios.get(API_URL, getAuthConfig());
    usuarios.value = response.data;
  } catch (error) {
    console.error('Error cargando usuarios:', error);
    // Si falla la autorización, redirigimos al login (opcional)
    if (error.response && error.response.status === 403) {
      console.warn("Token inválido o expirado");
    }
  }
};

// Abrir modal CREAR
const abrirModalCrear = () => {
  usuarioEditando.value = {};
  mostrarModal.value = true;
};

// Abrir modal EDITAR
const abrirModalEditar = (usuario) => {
  usuarioEditando.value = { ...usuario };
  mostrarModal.value = true;
};

// Guardar
const guardarUsuario = async (datosUsuario) => {
  try {
    if (usuarioEditando.value.id) {
      await axios.put(`${API_URL}/${usuarioEditando.value.id}`, datosUsuario, getAuthConfig());
    } else {
      await axios.post(API_URL, datosUsuario, getAuthConfig());
    }

    await cargarUsuarios();
    mostrarModal.value = false;
    alert('Operación realizada con éxito');
  } catch (error) {
    console.error('Error al guardar:', error);
    if (error.response) {
      alert(`Error: ${error.response.data}`);
    } else {
      alert('Error de conexión con el servidor');
    }
  }
};

// Eliminar
const eliminarUsuario = async (id) => {
  if (!confirm('¿Estás seguro de que quieres eliminar este usuario?')) return;

  try {
    await axios.delete(`${API_URL}/${id}`, getAuthConfig());
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