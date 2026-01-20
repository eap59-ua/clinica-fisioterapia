<template>
  <div class="min-h-screen bg-gradient-to-br from-dark-50 via-primary-50/20 to-secondary-50/20">
    <div class="max-w-4xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
      <!-- Header -->
      <div class="mb-8 animate-fade-in-up">
        <button
          @click="$router.back()"
          class="inline-flex items-center gap-2 text-dark-500 hover:text-dark-700 mb-4 transition-colors"
        >
          <svg class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M15 19l-7-7 7-7"/>
          </svg>
          Volver
        </button>
        <h1 class="font-display text-3xl font-bold text-dark-800">Editar Perfil</h1>
        <p class="text-dark-500 mt-1">Actualiza tu información personal</p>
      </div>

      <!-- Loading State -->
      <div v-if="cargando" class="flex items-center justify-center py-20">
        <div class="animate-spin rounded-full h-12 w-12 border-b-2 border-primary-500"></div>
      </div>

      <!-- Error State -->
      <div v-else-if="errorCarga" class="bg-red-50 border border-red-200 rounded-2xl p-6 text-center">
        <svg class="w-12 h-12 text-red-400 mx-auto mb-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
          <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 9v2m0 4h.01m-6.938 4h13.856c1.54 0 2.502-1.667 1.732-3L13.732 4c-.77-1.333-2.694-1.333-3.464 0L3.34 16c-.77 1.333.192 3 1.732 3z"/>
        </svg>
        <p class="text-red-600 font-medium">{{ errorCarga }}</p>
        <button @click="cargarPerfil" class="mt-4 text-primary-600 hover:text-primary-700 font-medium">
          Intentar de nuevo
        </button>
      </div>

      <!-- Profile Form -->
      <form v-else @submit.prevent="guardarCambios" class="space-y-6">
        <!-- Info Card -->
        <div class="bg-white rounded-2xl shadow-soft border border-dark-100 overflow-hidden animate-fade-in-up">
          <!-- Profile Header -->
          <div class="bg-gradient-to-r from-primary-500 to-primary-600 px-6 py-8">
            <div class="flex items-center gap-6">
              <div class="w-20 h-20 bg-white/20 backdrop-blur-sm rounded-2xl flex items-center justify-center">
                <span class="text-3xl font-bold text-white">{{ perfil.nombre?.charAt(0)?.toUpperCase() }}{{ perfil.apellidos?.charAt(0)?.toUpperCase() }}</span>
              </div>
              <div class="text-white">
                <h2 class="text-2xl font-bold">{{ perfil.nombre }} {{ perfil.apellidos }}</h2>
                <p class="text-primary-100 flex items-center gap-2 mt-1">
                  <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M3 8l7.89 5.26a2 2 0 002.22 0L21 8M5 19h14a2 2 0 002-2V7a2 2 0 00-2-2H5a2 2 0 00-2 2v10a2 2 0 002 2z"/>
                  </svg>
                  {{ perfil.email }}
                </p>
                <span class="inline-flex items-center px-3 py-1 mt-2 rounded-full text-xs font-semibold bg-white/20 text-white">
                  {{ perfil.rol }}
                </span>
              </div>
            </div>
          </div>

          <!-- Form Fields -->
          <div class="p-6 space-y-6">
            <h3 class="font-display text-lg font-semibold text-dark-800 flex items-center gap-2">
              <svg class="w-5 h-5 text-primary-500" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M16 7a4 4 0 11-8 0 4 4 0 018 0zM12 14a7 7 0 00-7 7h14a7 7 0 00-7-7z"/>
              </svg>
              Datos Personales
            </h3>

            <div class="grid grid-cols-1 md:grid-cols-2 gap-6">
              <!-- Nombre -->
              <div>
                <label class="block text-sm font-medium text-dark-700 mb-2">Nombre</label>
                <input
                  v-model="form.nombre"
                  type="text"
                  required
                  class="w-full px-4 py-3 rounded-xl border border-dark-200 focus:border-primary-500 focus:ring-2 focus:ring-primary-500/20 transition-all outline-none"
                  placeholder="Tu nombre"
                />
              </div>

              <!-- Apellidos -->
              <div>
                <label class="block text-sm font-medium text-dark-700 mb-2">Apellidos</label>
                <input
                  v-model="form.apellidos"
                  type="text"
                  required
                  class="w-full px-4 py-3 rounded-xl border border-dark-200 focus:border-primary-500 focus:ring-2 focus:ring-primary-500/20 transition-all outline-none"
                  placeholder="Tus apellidos"
                />
              </div>

              <!-- Email -->
              <div>
                <label class="block text-sm font-medium text-dark-700 mb-2">Email</label>
                <input
                  v-model="form.email"
                  type="email"
                  required
                  class="w-full px-4 py-3 rounded-xl border border-dark-200 focus:border-primary-500 focus:ring-2 focus:ring-primary-500/20 transition-all outline-none"
                  placeholder="tu@email.com"
                />
              </div>

              <!-- Teléfono -->
              <div>
                <label class="block text-sm font-medium text-dark-700 mb-2">Teléfono</label>
                <input
                  v-model="form.telefono"
                  type="tel"
                  pattern="[0-9]{9}"
                  class="w-full px-4 py-3 rounded-xl border border-dark-200 focus:border-primary-500 focus:ring-2 focus:ring-primary-500/20 transition-all outline-none"
                  placeholder="612345678"
                />
              </div>

              <!-- DNI (solo lectura) -->
              <div class="md:col-span-2">
                <label class="block text-sm font-medium text-dark-700 mb-2">DNI</label>
                <input
                  :value="perfil.dni"
                  type="text"
                  disabled
                  class="w-full px-4 py-3 rounded-xl border border-dark-100 bg-dark-50 text-dark-500 cursor-not-allowed"
                />
                <p class="text-xs text-dark-400 mt-1">El DNI no se puede modificar</p>
              </div>
            </div>
          </div>
        </div>

        <!-- Password Change Card -->
        <div class="bg-white rounded-2xl shadow-soft border border-dark-100 overflow-hidden animate-fade-in-up" style="animation-delay: 0.1s;">
          <div class="p-6 space-y-6">
            <div class="flex items-center justify-between">
              <h3 class="font-display text-lg font-semibold text-dark-800 flex items-center gap-2">
                <svg class="w-5 h-5 text-primary-500" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 15v2m-6 4h12a2 2 0 002-2v-6a2 2 0 00-2-2H6a2 2 0 00-2 2v6a2 2 0 002 2zm10-10V7a4 4 0 00-8 0v4h8z"/>
                </svg>
                Cambiar Contraseña
              </h3>
              <button
                type="button"
                @click="mostrarCambioPassword = !mostrarCambioPassword"
                class="text-primary-600 hover:text-primary-700 font-medium text-sm"
              >
                {{ mostrarCambioPassword ? 'Cancelar' : 'Cambiar' }}
              </button>
            </div>

            <div v-if="mostrarCambioPassword" class="space-y-4 pt-4 border-t border-dark-100">
              <!-- Contraseña Actual -->
              <div>
                <label class="block text-sm font-medium text-dark-700 mb-2">Contraseña Actual</label>
                <div class="relative">
                  <input
                    v-model="form.currentPassword"
                    :type="showCurrentPassword ? 'text' : 'password'"
                    class="w-full px-4 py-3 rounded-xl border border-dark-200 focus:border-primary-500 focus:ring-2 focus:ring-primary-500/20 transition-all outline-none pr-12"
                    placeholder="Tu contraseña actual"
                  />
                  <button
                    type="button"
                    @click="showCurrentPassword = !showCurrentPassword"
                    class="absolute right-3 top-1/2 -translate-y-1/2 text-dark-400 hover:text-dark-600"
                  >
                    <svg v-if="!showCurrentPassword" class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                      <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M15 12a3 3 0 11-6 0 3 3 0 016 0z"/>
                      <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M2.458 12C3.732 7.943 7.523 5 12 5c4.478 0 8.268 2.943 9.542 7-1.274 4.057-5.064 7-9.542 7-4.477 0-8.268-2.943-9.542-7z"/>
                    </svg>
                    <svg v-else class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                      <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M13.875 18.825A10.05 10.05 0 0112 19c-4.478 0-8.268-2.943-9.543-7a9.97 9.97 0 011.563-3.029m5.858.908a3 3 0 114.243 4.243M9.878 9.878l4.242 4.242M9.88 9.88l-3.29-3.29m7.532 7.532l3.29 3.29M3 3l3.59 3.59m0 0A9.953 9.953 0 0112 5c4.478 0 8.268 2.943 9.543 7a10.025 10.025 0 01-4.132 5.411m0 0L21 21"/>
                    </svg>
                  </button>
                </div>
              </div>

              <!-- Nueva Contraseña -->
              <div>
                <label class="block text-sm font-medium text-dark-700 mb-2">Nueva Contraseña</label>
                <div class="relative">
                  <input
                    v-model="form.newPassword"
                    :type="showNewPassword ? 'text' : 'password'"
                    minlength="6"
                    class="w-full px-4 py-3 rounded-xl border border-dark-200 focus:border-primary-500 focus:ring-2 focus:ring-primary-500/20 transition-all outline-none pr-12"
                    placeholder="Mínimo 6 caracteres"
                  />
                  <button
                    type="button"
                    @click="showNewPassword = !showNewPassword"
                    class="absolute right-3 top-1/2 -translate-y-1/2 text-dark-400 hover:text-dark-600"
                  >
                    <svg v-if="!showNewPassword" class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                      <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M15 12a3 3 0 11-6 0 3 3 0 016 0z"/>
                      <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M2.458 12C3.732 7.943 7.523 5 12 5c4.478 0 8.268 2.943 9.542 7-1.274 4.057-5.064 7-9.542 7-4.477 0-8.268-2.943-9.542-7z"/>
                    </svg>
                    <svg v-else class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                      <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M13.875 18.825A10.05 10.05 0 0112 19c-4.478 0-8.268-2.943-9.543-7a9.97 9.97 0 011.563-3.029m5.858.908a3 3 0 114.243 4.243M9.878 9.878l4.242 4.242M9.88 9.88l-3.29-3.29m7.532 7.532l3.29 3.29M3 3l3.59 3.59m0 0A9.953 9.953 0 0112 5c4.478 0 8.268 2.943 9.543 7a10.025 10.025 0 01-4.132 5.411m0 0L21 21"/>
                    </svg>
                  </button>
                </div>
              </div>

              <!-- Confirmar Nueva Contraseña -->
              <div>
                <label class="block text-sm font-medium text-dark-700 mb-2">Confirmar Nueva Contraseña</label>
                <input
                  v-model="confirmPassword"
                  :type="showNewPassword ? 'text' : 'password'"
                  minlength="6"
                  class="w-full px-4 py-3 rounded-xl border border-dark-200 focus:border-primary-500 focus:ring-2 focus:ring-primary-500/20 transition-all outline-none"
                  :class="{ 'border-red-300 focus:border-red-500': confirmPassword && confirmPassword !== form.newPassword }"
                  placeholder="Repite la nueva contraseña"
                />
                <p v-if="confirmPassword && confirmPassword !== form.newPassword" class="text-xs text-red-500 mt-1">
                  Las contraseñas no coinciden
                </p>
              </div>
            </div>

            <p v-else class="text-sm text-dark-500">
              Haz clic en "Cambiar" para actualizar tu contraseña
            </p>
          </div>
        </div>

        <!-- Info Card -->
        <div class="bg-blue-50 border border-blue-200 rounded-2xl p-4 animate-fade-in-up" style="animation-delay: 0.2s;">
          <div class="flex gap-3">
            <svg class="w-5 h-5 text-blue-500 flex-shrink-0 mt-0.5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M13 16h-1v-4h-1m1-4h.01M21 12a9 9 0 11-18 0 9 9 0 0118 0z"/>
            </svg>
            <div class="text-sm text-blue-700">
              <p class="font-medium">Cuenta creada el {{ formatearFecha(perfil.createdAt) }}</p>
              <p class="mt-1">Última actualización: {{ formatearFecha(perfil.updatedAt) }}</p>
            </div>
          </div>
        </div>

        <!-- Error Message -->
        <div v-if="error" class="bg-red-50 border border-red-200 rounded-xl p-4 flex items-start gap-3 animate-fade-in">
          <svg class="w-5 h-5 text-red-500 flex-shrink-0 mt-0.5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 8v4m0 4h.01M21 12a9 9 0 11-18 0 9 9 0 0118 0z"/>
          </svg>
          <p class="text-red-700 text-sm">{{ error }}</p>
        </div>

        <!-- Success Message -->
        <div v-if="exito" class="bg-green-50 border border-green-200 rounded-xl p-4 flex items-start gap-3 animate-fade-in">
          <svg class="w-5 h-5 text-green-500 flex-shrink-0 mt-0.5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 12l2 2 4-4m6 2a9 9 0 11-18 0 9 9 0 0118 0z"/>
          </svg>
          <p class="text-green-700 text-sm">{{ exito }}</p>
        </div>

        <!-- Submit Button -->
        <div class="flex justify-end gap-4">
          <button
            type="button"
            @click="$router.back()"
            class="px-6 py-3 rounded-xl border border-dark-200 text-dark-600 font-medium hover:bg-dark-50 transition-all"
          >
            Cancelar
          </button>
          <button
            type="submit"
            :disabled="guardando || (mostrarCambioPassword && confirmPassword !== form.newPassword)"
            class="inline-flex items-center gap-2 bg-gradient-to-r from-primary-500 to-primary-600 text-white px-6 py-3 rounded-xl font-semibold shadow-soft hover:shadow-glow transition-all duration-300 hover:-translate-y-0.5 disabled:opacity-50 disabled:cursor-not-allowed disabled:hover:translate-y-0"
          >
            <svg v-if="guardando" class="w-5 h-5 animate-spin" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M4 4v5h.582m15.356 2A8.001 8.001 0 004.582 9m0 0H9m11 11v-5h-.581m0 0a8.003 8.003 0 01-15.357-2m15.357 2H15"/>
            </svg>
            <svg v-else class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M5 13l4 4L19 7"/>
            </svg>
            {{ guardando ? 'Guardando...' : 'Guardar Cambios' }}
          </button>
        </div>
      </form>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from "vue";
import { useAuthStore } from "../../stores/auth";
import perfilService from "@/services/perfilService";

const authStore = useAuthStore();

// Estado
const cargando = ref(true);
const guardando = ref(false);
const errorCarga = ref(null);
const error = ref(null);
const exito = ref(null);

const perfil = ref({});
const form = ref({
  nombre: '',
  apellidos: '',
  email: '',
  telefono: '',
  currentPassword: '',
  newPassword: ''
});

const confirmPassword = ref('');
const mostrarCambioPassword = ref(false);
const showCurrentPassword = ref(false);
const showNewPassword = ref(false);

// Cargar perfil
const cargarPerfil = async () => {
  cargando.value = true;
  errorCarga.value = null;

  try {
    const data = await perfilService.getPerfil();
    perfil.value = data;

    // Inicializar formulario con datos actuales
    form.value = {
      nombre: data.nombre || '',
      apellidos: data.apellidos || '',
      email: data.email || '',
      telefono: data.telefono || '',
      currentPassword: '',
      newPassword: ''
    };
  } catch (err) {
    console.error('Error cargando perfil:', err);
    errorCarga.value = 'No se pudo cargar tu perfil. Por favor, intenta de nuevo.';
  } finally {
    cargando.value = false;
  }
};

// Guardar cambios
const guardarCambios = async () => {
  error.value = null;
  exito.value = null;

  // Validar contraseñas si se quiere cambiar
  if (mostrarCambioPassword.value) {
    if (!form.value.currentPassword) {
      error.value = 'Debes introducir tu contraseña actual para cambiarla';
      return;
    }
    if (!form.value.newPassword) {
      error.value = 'Debes introducir la nueva contraseña';
      return;
    }
    if (form.value.newPassword !== confirmPassword.value) {
      error.value = 'Las contraseñas no coinciden';
      return;
    }
    if (form.value.newPassword.length < 6) {
      error.value = 'La nueva contraseña debe tener al menos 6 caracteres';
      return;
    }
  }

  guardando.value = true;

  try {
    // Preparar datos para enviar
    const datosActualizar = {
      nombre: form.value.nombre,
      apellidos: form.value.apellidos,
      email: form.value.email,
      telefono: form.value.telefono
    };

    // Solo incluir contraseñas si se quiere cambiar
    if (mostrarCambioPassword.value && form.value.newPassword) {
      datosActualizar.currentPassword = form.value.currentPassword;
      datosActualizar.newPassword = form.value.newPassword;
    }

    const data = await perfilService.updatePerfil(datosActualizar);

    // Actualizar perfil local
    perfil.value = data;

    // Actualizar store si cambió el nombre o email
    if (authStore.user) {
      authStore.user.nombre = data.nombre;
      authStore.user.email = data.email;
      localStorage.setItem('user', JSON.stringify(authStore.user));
    }

    // Limpiar campos de contraseña
    form.value.currentPassword = '';
    form.value.newPassword = '';
    confirmPassword.value = '';
    mostrarCambioPassword.value = false;

    exito.value = 'Perfil actualizado correctamente';

    // Ocultar mensaje de éxito después de 3 segundos
    setTimeout(() => {
      exito.value = null;
    }, 3000);

  } catch (err) {
    console.error('Error actualizando perfil:', err);
    error.value = err.response?.data?.message || 'Error al actualizar el perfil. Por favor, intenta de nuevo.';
  } finally {
    guardando.value = false;
  }
};

// Formatear fecha
const formatearFecha = (fecha) => {
  if (!fecha) return 'No disponible';
  return new Date(fecha).toLocaleDateString('es-ES', {
    day: 'numeric',
    month: 'long',
    year: 'numeric',
    hour: '2-digit',
    minute: '2-digit'
  });
};

onMounted(() => {
  cargarPerfil();
});
</script>
