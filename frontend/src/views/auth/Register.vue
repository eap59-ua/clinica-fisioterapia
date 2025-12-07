<template>
  <div class="min-h-screen bg-gray-100 py-12">
    <div class="max-w-2xl mx-auto bg-white rounded-lg shadow-md p-8">
      <h2 class="text-3xl font-bold text-center mb-6">Crear Cuenta</h2>

      <div
        v-if="error"
        class="bg-red-100 border border-red-400 text-red-700 px-4 py-3 rounded mb-4"
      >
        {{ error }}
      </div>

      <form @submit.prevent="handleRegister" class="grid grid-cols-2 gap-4">
        <div>
          <label class="block text-gray-700 mb-2">Nombre *</label>
          <input
            v-model="formData.nombre"
            type="text"
            required
            class="w-full px-4 py-2 border rounded-lg focus:outline-none focus:ring-2 focus:ring-primary"
          />
        </div>

        <div>
          <label class="block text-gray-700 mb-2">Apellidos *</label>
          <input
            v-model="formData.apellidos"
            type="text"
            required
            class="w-full px-4 py-2 border rounded-lg focus:outline-none focus:ring-2 focus:ring-primary"
          />
        </div>

        <div>
          <label class="block text-gray-700 mb-2">DNI *</label>
          <input
            v-model="formData.dni"
            type="text"
            required
            pattern="[0-9]{8}[A-Z]"
            placeholder="12345678A"
            class="w-full px-4 py-2 border rounded-lg focus:outline-none focus:ring-2 focus:ring-primary"
          />
        </div>

        <div>
          <label class="block text-gray-700 mb-2">Teléfono</label>
          <input
            v-model="formData.telefono"
            type="tel"
            class="w-full px-4 py-2 border rounded-lg focus:outline-none focus:ring-2 focus:ring-primary"
          />
        </div>

        <div class="col-span-2">
          <label class="block text-gray-700 mb-2">Email *</label>
          <input
            v-model="formData.email"
            type="email"
            required
            class="w-full px-4 py-2 border rounded-lg focus:outline-none focus:ring-2 focus:ring-primary"
          />
        </div>

        <div class="col-span-2">
          <label class="block text-gray-700 mb-2"
            >Contraseña * (mínimo 8 caracteres)</label
          >
          <input
            v-model="formData.password"
            type="password"
            required
            minlength="8"
            class="w-full px-4 py-2 border rounded-lg focus:outline-none focus:ring-2 focus:ring-primary"
          />
        </div>

        <div class="col-span-2">
          <label class="block text-gray-700 mb-2">Dirección</label>
          <input
            v-model="formData.direccion"
            type="text"
            class="w-full px-4 py-2 border rounded-lg focus:outline-none focus:ring-2 focus:ring-primary"
          />
        </div>

        <div>
          <label class="block text-gray-700 mb-2">Fecha de Nacimiento</label>
          <input
            v-model="formData.fechaNacimiento"
            type="date"
            class="w-full px-4 py-2 border rounded-lg focus:outline-none focus:ring-2 focus:ring-primary"
          />
        </div>

        <div class="col-span-2">
          <button
            type="submit"
            :disabled="loading"
            class="w-full bg-primary text-white py-3 rounded-lg hover:bg-blue-600 disabled:opacity-50"
          >
            {{ loading ? "Registrando..." : "Crear Cuenta" }}
          </button>
        </div>
      </form>

      <p class="text-center mt-4">
        ¿Ya tienes cuenta?
        <router-link to="/login" class="text-primary hover:underline">
          Inicia sesión aquí
        </router-link>
      </p>
    </div>
  </div>
</template>

<script setup>
import { ref } from "vue";
import { useRouter } from "vue-router";
import { useAuthStore } from "../../stores/auth";

const router = useRouter();
const authStore = useAuthStore();

const formData = ref({
  nombre: "",
  apellidos: "",
  dni: "",
  email: "",
  telefono: "",
  password: "",
  direccion: "",
  fechaNacimiento: "",
});

const loading = ref(false);
const error = ref("");

const handleRegister = async () => {
  loading.value = true;
  error.value = "";

  try {
    await authStore.register(formData.value);
    router.push("/cliente");
  } catch (err) {
    error.value = err.response?.data?.message || "Error al registrarse";
  } finally {
    loading.value = false;
  }
};
</script>
