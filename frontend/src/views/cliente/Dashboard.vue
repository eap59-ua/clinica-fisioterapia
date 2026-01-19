<template>
  <div class="min-h-screen bg-gradient-to-br from-dark-50 via-primary-50/20 to-secondary-50/20">
    <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
      <!-- Header -->
      <div class="mb-8 animate-fade-in-up">
        <div class="flex flex-col sm:flex-row sm:items-center sm:justify-between gap-4">
          <div>
            <h1 class="font-display text-3xl font-bold text-dark-800">
              Hola, {{ authStore.userName }}
            </h1>
            <p class="text-dark-500 mt-1">Bienvenido a tu panel de cliente</p>
          </div>
          <button
            @click="$router.push('/cliente/reservar-cita')"
            class="inline-flex items-center justify-center gap-2 bg-gradient-to-r from-primary-500 to-primary-600 text-white px-6 py-3 rounded-xl font-semibold shadow-soft hover:shadow-glow transition-all duration-300 hover:-translate-y-0.5"
          >
            <svg class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 4v16m8-8H4" />
            </svg>
            Nueva Cita
          </button>
        </div>
      </div>

      <!-- Stats Grid -->
      <div class="grid grid-cols-1 md:grid-cols-3 gap-6 mb-8">
        <div class="bg-white rounded-2xl shadow-soft p-6 border border-dark-100 animate-fade-in-up" style="animation-delay: 0.1s;">
          <div class="flex items-center justify-between">
            <div>
              <p class="text-sm font-medium text-dark-500">Próximas Citas</p>
              <p class="text-3xl font-bold text-primary-600 mt-1">{{ stats.proximas }}</p>
            </div>
            <div class="w-14 h-14 bg-gradient-to-br from-primary-100 to-primary-200 rounded-2xl flex items-center justify-center">
              <svg class="w-7 h-7 text-primary-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M8 7V3m8 4V3m-9 8h10M5 21h14a2 2 0 002-2V7a2 2 0 00-2-2H5a2 2 0 00-2 2v12a2 2 0 002 2z" />
              </svg>
            </div>
          </div>
          <router-link to="/cliente/mis-citas" class="mt-4 flex items-center text-sm hover:underline">
            <span class="text-primary-600 font-medium">Ver agenda</span>
            <svg class="w-4 h-4 ml-1 text-primary-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 5l7 7-7 7"/>
            </svg>
          </router-link>
        </div>

        <div class="bg-white rounded-2xl shadow-soft p-6 border border-dark-100 animate-fade-in-up" style="animation-delay: 0.2s;">
          <div class="flex items-center justify-between">
            <div>
              <p class="text-sm font-medium text-dark-500">Citas Completadas</p>
              <p class="text-3xl font-bold text-green-600 mt-1">{{ stats.completadas }}</p>
            </div>
            <div class="w-14 h-14 bg-gradient-to-br from-green-100 to-green-200 rounded-2xl flex items-center justify-center">
              <svg class="w-7 h-7 text-green-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 12l2 2 4-4m6 2a9 9 0 11-18 0 9 9 0 0118 0z" />
              </svg>
            </div>
          </div>
          <router-link to="/cliente/mis-citas" class="mt-4 flex items-center text-sm hover:underline">
            <span class="text-green-600 font-medium">Ver historial</span>
            <svg class="w-4 h-4 ml-1 text-green-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 5l7 7-7 7"/>
            </svg>
          </router-link>
        </div>

        <div class="bg-white rounded-2xl shadow-soft p-6 border border-dark-100 animate-fade-in-up" style="animation-delay: 0.3s;">
          <div class="flex items-center justify-between">
            <div>
              <p class="text-sm font-medium text-dark-500">Gasto Total</p>
              <p class="text-3xl font-bold text-dark-800 mt-1">{{ stats.gastoTotal }}€</p>
            </div>
            <div class="w-14 h-14 bg-gradient-to-br from-accent-100 to-accent-200 rounded-2xl flex items-center justify-center">
              <svg class="w-7 h-7 text-accent-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 8c-1.657 0-3 .895-3 2s1.343 2 3 2 3 .895 3 2-1.343 2-3 2m0-8c1.11 0 2.08.402 2.599 1M12 8V7m0 1v8m0 0v1m0-1c-1.11 0-2.08-.402-2.599-1M21 12a9 9 0 11-18 0 9 9 0 0118 0z" />
              </svg>
            </div>
          </div>
          <div class="mt-4 flex items-center text-sm">
            <span class="text-dark-500">Citas completadas</span>
          </div>
        </div>
      </div>

      <!-- Quick Actions -->
      <div class="bg-white rounded-2xl shadow-soft p-6 border border-dark-100 mb-8 animate-fade-in-up" style="animation-delay: 0.4s;">
        <h2 class="font-display text-xl font-bold text-dark-800 mb-6">Acciones Rápidas</h2>
        <div class="grid grid-cols-1 sm:grid-cols-3 gap-4">
          <button
            @click="$router.push('/cliente/reservar-cita')"
            class="group flex items-center gap-4 p-4 rounded-xl bg-gradient-to-br from-primary-50 to-primary-100 border border-primary-200 hover:shadow-soft transition-all duration-300"
          >
            <div class="w-12 h-12 bg-gradient-to-br from-primary-500 to-primary-600 rounded-xl flex items-center justify-center group-hover:scale-110 transition-transform">
              <svg class="w-6 h-6 text-white" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 4v16m8-8H4" />
              </svg>
            </div>
            <div class="text-left">
              <p class="font-semibold text-dark-800">Reservar Cita</p>
              <p class="text-sm text-dark-500">Agenda una nueva sesión</p>
            </div>
          </button>

          <button
            @click="$router.push('/cliente/mis-citas')"
            class="group flex items-center gap-4 p-4 rounded-xl bg-gradient-to-br from-secondary-50 to-secondary-100 border border-secondary-200 hover:shadow-soft transition-all duration-300"
          >
            <div class="w-12 h-12 bg-gradient-to-br from-secondary-500 to-secondary-600 rounded-xl flex items-center justify-center group-hover:scale-110 transition-transform">
              <svg class="w-6 h-6 text-white" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M8 7V3m8 4V3m-9 8h10M5 21h14a2 2 0 002-2V7a2 2 0 00-2-2H5a2 2 0 00-2 2v12a2 2 0 002 2z" />
              </svg>
            </div>
            <div class="text-left">
              <p class="font-semibold text-dark-800">Ver Mis Citas</p>
              <p class="text-sm text-dark-500">Consulta tu agenda</p>
            </div>
          </button>

          <button
            class="group flex items-center gap-4 p-4 rounded-xl bg-gradient-to-br from-dark-50 to-dark-100 border border-dark-200 hover:shadow-soft transition-all duration-300"
          >
            <div class="w-12 h-12 bg-gradient-to-br from-dark-500 to-dark-600 rounded-xl flex items-center justify-center group-hover:scale-110 transition-transform">
              <svg class="w-6 h-6 text-white" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M16 7a4 4 0 11-8 0 4 4 0 018 0zM12 14a7 7 0 00-7 7h14a7 7 0 00-7-7z" />
              </svg>
            </div>
            <div class="text-left">
              <p class="font-semibold text-dark-800">Editar Perfil</p>
              <p class="text-sm text-dark-500">Actualiza tus datos</p>
            </div>
          </button>
        </div>
      </div>

      <!-- Upcoming Appointments -->
      <div class="bg-white rounded-2xl shadow-soft p-6 border border-dark-100 animate-fade-in-up" style="animation-delay: 0.5s;">
        <div class="flex items-center justify-between mb-6">
          <h2 class="font-display text-xl font-bold text-dark-800">Próximas Citas</h2>
          <router-link
            to="/cliente/mis-citas"
            class="text-primary-600 hover:text-primary-700 font-medium text-sm flex items-center gap-1"
          >
            Ver todas
            <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 5l7 7-7 7"/>
            </svg>
          </router-link>
        </div>

        <!-- Loading -->
        <div v-if="cargando" class="flex items-center justify-center py-12">
          <div class="animate-spin rounded-full h-8 w-8 border-b-2 border-primary-500"></div>
        </div>

        <!-- Sin citas -->
        <div v-else-if="proximasCitas.length === 0" class="text-center py-12">
          <div class="w-16 h-16 bg-dark-100 rounded-full flex items-center justify-center mx-auto mb-4">
            <svg class="w-8 h-8 text-dark-400" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M8 7V3m8 4V3m-9 8h10M5 21h14a2 2 0 002-2V7a2 2 0 00-2-2H5a2 2 0 00-2 2v12a2 2 0 002 2z"/>
            </svg>
          </div>
          <p class="text-dark-500 mb-4">No tienes citas próximas</p>
          <router-link
            to="/cliente/reservar-cita"
            class="inline-flex items-center gap-2 bg-primary-600 text-white px-4 py-2 rounded-lg font-medium hover:bg-primary-700 transition-colors"
          >
            <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 4v16m8-8H4"/>
            </svg>
            Reservar una cita
          </router-link>
        </div>

        <!-- Lista de citas -->
        <div v-else class="space-y-4">
          <div
            v-for="(cita, index) in proximasCitas"
            :key="cita.id"
            class="group relative overflow-hidden rounded-xl border border-dark-100 hover:border-primary-200 hover:shadow-soft transition-all duration-300"
          >
            <div
              class="absolute left-0 top-0 bottom-0 w-1"
              :class="index % 2 === 0 ? 'bg-gradient-to-b from-primary-500 to-primary-600' : 'bg-gradient-to-b from-secondary-500 to-secondary-600'"
            ></div>
            <div class="p-5 pl-6">
              <div class="flex flex-col sm:flex-row sm:items-center sm:justify-between gap-4">
                <div class="flex items-start gap-4">
                  <div
                    class="w-12 h-12 rounded-xl flex items-center justify-center flex-shrink-0"
                    :class="index % 2 === 0 ? 'bg-primary-100' : 'bg-secondary-100'"
                  >
                    <svg
                      class="w-6 h-6"
                      :class="index % 2 === 0 ? 'text-primary-600' : 'text-secondary-600'"
                      fill="none"
                      stroke="currentColor"
                      viewBox="0 0 24 24"
                    >
                      <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M4.318 6.318a4.5 4.5 0 000 6.364L12 20.364l7.682-7.682a4.5 4.5 0 00-6.364-6.364L12 7.636l-1.318-1.318a4.5 4.5 0 00-6.364 0z"/>
                    </svg>
                  </div>
                  <div>
                    <h3 class="font-semibold text-dark-800 text-lg">{{ cita.servicio?.nombre }}</h3>
                    <p class="text-dark-500 text-sm">{{ cita.fisioterapeuta?.nombre }} {{ cita.fisioterapeuta?.apellidos }}</p>
                    <div class="flex flex-wrap items-center gap-3 mt-2 text-sm text-dark-500">
                      <span class="inline-flex items-center gap-1.5">
                        <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                          <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M8 7V3m8 4V3m-9 8h10M5 21h14a2 2 0 002-2V7a2 2 0 00-2-2H5a2 2 0 00-2 2v12a2 2 0 002 2z"/>
                        </svg>
                        {{ formatearFecha(cita.fecha) }} - {{ formatearHora(cita.horaInicio) }}
                      </span>
                      <span class="inline-flex items-center gap-1.5">
                        <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                          <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M17.657 16.657L13.414 20.9a1.998 1.998 0 01-2.827 0l-4.244-4.243a8 8 0 1111.314 0z"/>
                        </svg>
                        {{ cita.sala?.nombre || 'Por asignar' }}
                      </span>
                    </div>
                  </div>
                </div>
                <div class="flex items-center gap-3 sm:flex-col sm:items-end">
                  <span
                    class="inline-flex items-center px-3 py-1.5 rounded-full text-xs font-semibold"
                    :class="getEstadoClase(cita.estado)"
                  >
                    {{ cita.estado }}
                  </span>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from "vue";
import { useAuthStore } from "../../stores/auth";
import citaService from "@/services/citaService";

const authStore = useAuthStore();

const cargando = ref(true);
const misCitas = ref([]);
const proximasCitas = ref([]);

// Stats calculados
const stats = computed(() => {
  const proximas = misCitas.value.filter(c =>
    c.estado === 'PENDIENTE' || c.estado === 'CONFIRMADA'
  ).length;

  const completadas = misCitas.value.filter(c =>
    c.estado === 'COMPLETADA'
  ).length;

  const gastoTotal = misCitas.value
    .filter(c => c.estado === 'COMPLETADA')
    .reduce((sum, c) => sum + (c.precioPagado || 0), 0);

  return { proximas, completadas, gastoTotal };
});

// Formatear fecha
const formatearFecha = (fecha) => {
  if (!fecha) return '';
  const date = new Date(fecha + 'T00:00:00');
  return date.toLocaleDateString('es-ES', {
    day: 'numeric',
    month: 'short',
    year: 'numeric'
  });
};

// Formatear hora
const formatearHora = (hora) => {
  if (!hora) return '';
  return hora.substring(0, 5) + 'h';
};

// Obtener color de estado
const getEstadoClase = (estado) => {
  const clases = {
    'PENDIENTE': 'bg-amber-100 text-amber-700',
    'CONFIRMADA': 'bg-green-100 text-green-700',
    'COMPLETADA': 'bg-blue-100 text-blue-700',
    'CANCELADA': 'bg-red-100 text-red-700'
  };
  return clases[estado] || 'bg-gray-100 text-gray-700';
};

onMounted(async () => {
  try {
    const [todas, proximas] = await Promise.all([
      citaService.getMisCitas(),
      citaService.getProximasCitas()
    ]);
    misCitas.value = todas || [];
    proximasCitas.value = (proximas || []).slice(0, 3); // Máximo 3 para el dashboard
  } catch (error) {
    console.error('Error cargando citas:', error);
  } finally {
    cargando.value = false;
  }
});
</script>
