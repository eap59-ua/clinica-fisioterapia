<template>
  <div class="admin-dashboard p-4">
    <h1 class="text-2xl font-bold mb-6">Panel de Administración</h1>

    <div class="grid grid-cols-1 md:grid-cols-3 gap-6">
      <div class="bg-blue-100 p-6 rounded-lg shadow-md">
        <h3 class="text-lg font-semibold text-blue-800">Usuarios Registrados</h3>
        <p class="text-4xl font-bold text-blue-600 mt-2">{{ totalUsuarios }}</p>
      </div>

      <div class="bg-green-100 p-6 rounded-lg shadow-md">
        <h3 class="text-lg font-semibold text-green-800">Citas este mes</h3>
        <p class="text-4xl font-bold text-green-600 mt-2">124</p> </div>

      <div class="bg-yellow-100 p-6 rounded-lg shadow-md">
        <h3 class="text-lg font-semibold text-yellow-800">Ingresos Estimados</h3>
        <p class="text-4xl font-bold text-yellow-600 mt-2">4.500 €</p> </div>
    </div>

    <div class="mt-8">
      <h2 class="text-xl font-bold mb-4">Accesos Rápidos</h2>
      <div class="flex gap-4">
        <router-link to="/admin/usuarios" class="bg-blue-600 text-white px-4 py-2 rounded hover:bg-blue-700">
          Gestionar Usuarios
        </router-link>
        <button class="bg-gray-400 text-white px-4 py-2 rounded cursor-not-allowed">Gestionar Servicios (Próximamente)</button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import axios from 'axios';

const totalUsuarios = ref(0);

// Conectamos con tu backend para contar cuántos usuarios hay realmente
const fetchStats = async () => {
  try {
    // Asegúrate de que la URL coincida con tu puerto de backend (normalmente 8080)
    const response = await axios.get('http://localhost:8080/api/admin/usuarios');
    totalUsuarios.value = response.data.length;
  } catch (error) {
    console.error('Error cargando estadísticas:', error);
  }
};

onMounted(() => {
  fetchStats();
});
</script>