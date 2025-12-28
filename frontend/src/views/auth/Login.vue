<template>
  <div class="min-h-screen bg-gray-100 flex items-center justify-center">
    <div class="max-w-md w-full bg-white rounded-lg shadow-md p-8">
      <h2 class="text-3xl font-bold text-center mb-6">Iniciar Sesión</h2>

      <div
        v-if="error"
        class="bg-red-100 border border-red-400 text-red-700 px-4 py-3 rounded mb-4"
      >
        {{ error }}
      </div>

      <form @submit.prevent="handleLogin">
        <div class="mb-4">
          <label class="block text-gray-700 mb-2">Email</label>
          <input
            v-model="formData.email"
            type="email"
            required
            class="w-full px-4 py-2 border rounded-lg focus:outline-none focus:ring-2 focus:ring-primary"
            placeholder="tu@email.com"
          />
        </div>

        <div class="mb-6">
          <label class="block text-gray-700 mb-2">Contraseña</label>
          <input
            v-model="formData.password"
            type="password"
            required
            class="w-full px-4 py-2 border rounded-lg focus:outline-none focus:ring-2 focus:ring-primary"
            placeholder="********"
          />
        </div>

        <button
          type="submit"
          :disabled="loading"
          class="w-full bg-primary text-white py-2 rounded-lg hover:bg-blue-600 disabled:opacity-50"
        >
          {{ loading ? "Iniciando..." : "Iniciar Sesión" }}
        </button>
      </form>

      <p class="text-center mt-4">
        ¿No tienes cuenta?
        <router-link to="/register" class="text-primary hover:underline">
          Regístrate aquí
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
  email: "",
  password: "",
});

const loading = ref(false);
const error = ref("");

const handleLogin = async () => {
  loading.value = true;
  error.value = "";

  try {
    await authStore.login(formData.value);
    if(authStore.userRole == "CLIENTE") router.push("/cliente");
    else if(authStore.userRole == "RECEPCIONISTA") router.push("/recepcionista");
    else router.push("/");
  } catch (err) {
    error.value = err.response?.data?.message || "Error al iniciar sesión";
  } finally {
    loading.value = false;
  }
};
</script>
