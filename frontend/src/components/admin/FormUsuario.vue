<template>
  <div v-if="show" class="fixed inset-0 bg-gray-600 bg-opacity-50 overflow-y-auto h-full w-full flex justify-center items-center z-50">
    <div class="bg-white p-5 rounded-lg shadow-xl w-full max-w-lg">

      <h2 class="text-xl font-bold mb-4">{{ usuario.id ? 'Editar Usuario' : 'Nuevo Usuario' }}</h2>

      <form @submit.prevent="guardar">
        <div class="grid grid-cols-2 gap-4">
          <div class="mb-4">
            <label class="block text-gray-700 text-sm font-bold mb-2">Nombre</label>
            <input v-model="form.nombre" type="text" required class="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline">
          </div>
          <div class="mb-4">
            <label class="block text-gray-700 text-sm font-bold mb-2">Apellidos</label>
            <input v-model="form.apellidos" type="text" required class="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline">
          </div>
        </div>

        <div class="grid grid-cols-2 gap-4">
          <div class="mb-4">
            <label class="block text-gray-700 text-sm font-bold mb-2">DNI</label>
            <input v-model="form.dni" type="text" required class="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline">
          </div>
          <div class="mb-4">
            <label class="block text-gray-700 text-sm font-bold mb-2">Teléfono</label>
            <input v-model="form.telefono" type="text" class="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline">
          </div>
        </div>

        <div class="mb-4">
          <label class="block text-gray-700 text-sm font-bold mb-2">Email</label>
          <input v-model="form.email" type="email" required class="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline">
        </div>

        <div class="mb-4">
          <label class="block text-gray-700 text-sm font-bold mb-2">
            Contraseña <span v-if="usuario.id" class="text-xs font-normal text-gray-500">(Dejar en blanco para no cambiar)</span>
          </label>
          <input v-model="form.password" type="password" :required="!usuario.id" class="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline">
        </div>

        <div class="grid grid-cols-2 gap-4">
          <div class="mb-4">
            <label class="block text-gray-700 text-sm font-bold mb-2">Rol</label>
            <select v-model="form.rol" class="shadow border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline">
              <option value="PACIENTE">Paciente</option>
              <option value="FISIOTERAPEUTA">Fisioterapeuta</option>
              <option value="RECEPCIONISTA">Recepcionista</option>
              <option value="ADMIN">Administrador</option>
            </select>
          </div>
          <div class="mb-4 flex items-center mt-6">
            <input v-model="form.activo" type="checkbox" class="mr-2 leading-tight">
            <span class="text-sm">Usuario Activo</span>
          </div>
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
  usuario: Object // Si es null o vacío, es modo crear
});

const emit = defineEmits(['close', 'save']);

// Estado local del formulario
const form = reactive({
  nombre: '',
  apellidos: '',
  email: '',
  dni: '',
  telefono: '',
  password: '',
  rol: 'PACIENTE',
  activo: true
});

// Cada vez que abrimos el modal o cambiamos de usuario, reseteamos el formulario
watch(() => props.usuario, (newVal) => {
  if (newVal && newVal.id) {
    // Modo Edición: Copiamos los datos
    Object.assign(form, newVal);
    form.password = ''; // Limpiamos password para no sobreescribirla accidentalmente
  } else {
    // Modo Crear: Limpiamos todo
    form.nombre = '';
    form.apellidos = '';
    form.email = '';
    form.dni = '';
    form.telefono = '';
    form.password = '';
    form.rol = 'PACIENTE';
    form.activo = true;
  }
});

const guardar = () => {
  emit('save', { ...form });
};
</script>