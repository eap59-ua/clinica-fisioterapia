<script setup>
import { ref, onMounted } from 'vue';
import axios from 'axios';
import FormUsuario from '@/components/admin/FormUsuario.vue';

const usuarios = ref([]);
const mostrarModal = ref(false);
const usuarioEditando = ref({});

const API_URL = 'http://localhost:8080/api/admin/usuarios';

// Función auxiliar para obtener la configuración con el Token
const getAuthConfig = () => {
  const token = localStorage.getItem('token'); // Ojo: verifica si Erardo guardó el token como 'token' o 'jwt'
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
  }
};

// Abrir modal para CREAR
const abrirModalCrear = () => {
  usuarioEditando.value = {};
  mostrarModal.value = true;
};

// Abrir modal para EDITAR
const abrirModalEditar = (usuario) => {
  usuarioEditando.value = { ...usuario };
  mostrarModal.value = true;
};

// Guardar (Crear o Editar)
const guardarUsuario = async (datosUsuario) => {
  try {
    if (usuarioEditando.value.id) {
      // EDITAR (PUT) - Pasamos la configuración como tercer parámetro
      await axios.put(`${API_URL}/${usuarioEditando.value.id}`, datosUsuario, getAuthConfig());
    } else {
      // CREAR (POST) - Pasamos la configuración como tercer parámetro
      await axios.post(API_URL, datosUsuario, getAuthConfig());
    }

    await cargarUsuarios();
    mostrarModal.value = false;
    alert('Operación realizada con éxito'); // Feedback positivo
  } catch (error) {
    console.error('Error al guardar:', error);
    // Intentamos mostrar el mensaje exacto del backend (ej: "El email ya existe")
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