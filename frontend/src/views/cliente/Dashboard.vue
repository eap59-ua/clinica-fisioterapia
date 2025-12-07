<template>
  <div class="min-h-screen bg-gray-50 py-8">
    <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
      <div class="mb-8">
        <h1 class="text-3xl font-bold">Bienvenido, {{ authStore.userName }}</h1>
        <p class="text-gray-600">Panel de cliente</p>
      </div>

      <!-- Estadísticas rápidas -->
      <div class="grid grid-cols-1 md:grid-cols-3 gap-6 mb-8">
        <div class="bg-white rounded-lg shadow-md p-6">
          <div class="flex items-center justify-between">
            <div>
              <p class="text-gray-500 text-sm">Próximas Citas</p>
              <p class="text-3xl font-bold text-primary">3</p>
            </div>
            <div class="text-4xl">📅</div>
          </div>
        </div>

        <div class="bg-white rounded-lg shadow-md p-6">
          <div class="flex items-center justify-between">
            <div>
              <p class="text-gray-500 text-sm">Citas Completadas</p>
              <p class="text-3xl font-bold text-green-600">12</p>
            </div>
            <div class="text-4xl">✅</div>
          </div>
        </div>

        <div class="bg-white rounded-lg shadow-md p-6">
          <div class="flex items-center justify-between">
            <div>
              <p class="text-gray-500 text-sm">Gastos Total</p>
              <p class="text-3xl font-bold text-gray-700">540€</p>
            </div>
            <div class="text-4xl">💰</div>
          </div>
        </div>
      </div>

      <!-- Acciones rápidas -->
      <div class="bg-white rounded-lg shadow-md p-6 mb-8">
        <h2 class="text-2xl font-bold mb-6">Acciones Rápidas</h2>
        <div class="grid grid-cols-1 md:grid-cols-3 gap-4">
          <button
            class="bg-primary text-white py-3 px-6 rounded-lg hover:bg-blue-600 transition"
          >
            📅 Reservar Cita
          </button>
          <button
            class="bg-secondary text-white py-3 px-6 rounded-lg hover:bg-green-600 transition"
          >
            📋 Ver Mis Citas
          </button>
          <button
            class="bg-gray-600 text-white py-3 px-6 rounded-lg hover:bg-gray-700 transition"
          >
            👤 Editar Perfil
          </button>
        </div>
      </div>

      <!-- Próximas citas -->
      <div class="bg-white rounded-lg shadow-md p-6">
        <h2 class="text-2xl font-bold mb-6">Próximas Citas</h2>
        <div class="space-y-4">
          <div class="border-l-4 border-primary bg-gray-50 p-4 rounded">
            <div class="flex justify-between items-start">
              <div>
                <h3 class="font-bold text-lg">Masaje Terapéutico</h3>
                <p class="text-gray-600">Dr. Antonio Ruiz Fernández</p>
                <p class="text-sm text-gray-500 mt-2">
                  📅 15 Diciembre 2025 - 10:00h
                </p>
                <p class="text-sm text-gray-500">📍 Sala 2 - Masajes</p>
              </div>
              <div class="text-right">
                <span
                  class="bg-yellow-100 text-yellow-800 px-3 py-1 rounded-full text-sm"
                >
                  Pendiente
                </span>
              </div>
            </div>
          </div>

          <div class="border-l-4 border-primary bg-gray-50 p-4 rounded">
            <div class="flex justify-between items-start">
              <div>
                <h3 class="font-bold text-lg">Rehabilitación Deportiva</h3>
                <p class="text-gray-600">Dra. Elena Jiménez Torres</p>
                <p class="text-sm text-gray-500 mt-2">
                  📅 18 Diciembre 2025 - 16:30h
                </p>
                <p class="text-sm text-gray-500">📍 Sala 3 - Gimnasio</p>
              </div>
              <div class="text-right">
                <span
                  class="bg-yellow-100 text-yellow-800 px-3 py-1 rounded-full text-sm"
                >
                  Pendiente
                </span>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { useAuthStore } from "../../stores/auth";

const authStore = useAuthStore();
</script>
