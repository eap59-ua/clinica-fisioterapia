import { defineStore } from "pinia";
import { ref, computed } from "vue";
import authService from "../services/authService";

export const useAuthStore = defineStore("auth", () => {
  // State
  const user = ref(null);
  const token = ref(localStorage.getItem("token") || null);

  // Getters
  const isAuthenticated = computed(() => !!token.value);
  const userName = computed(() => user.value?.nombre || "");
  const userRole = computed(() => user.value?.rol || "");

  // Actions
  async function login(credentials) {
    try {
      const response = await authService.login(credentials);

      user.value = {
        id: response.usuarioId,
        nombre: response.nombre,
        email: response.email,
        rol: response.rol,
      };
      token.value = response.token;

      localStorage.setItem("token", response.token);
      localStorage.setItem("user", JSON.stringify(user.value));

      return response;
    } catch (error) {
      throw error;
    }
  }

  async function register(userData) {
    try {
      const response = await authService.register(userData);

      user.value = {
        id: response.usuarioId,
        nombre: response.nombre,
        email: response.email,
        rol: response.rol,
      };
      token.value = response.token;

      localStorage.setItem("token", response.token);
      localStorage.setItem("user", JSON.stringify(user.value));

      return response;
    } catch (error) {
      throw error;
    }
  }

  function logout() {
    user.value = null;
    token.value = null;
    localStorage.removeItem("token");
    localStorage.removeItem("user");
  }

  function loadUserFromStorage() {
    const storedUser = localStorage.getItem("user");
    if (storedUser) {
      user.value = JSON.parse(storedUser);
    }
  }

  // Cargar usuario al iniciar
  loadUserFromStorage();

  return {
    user,
    token,
    isAuthenticated,
    userName,
    userRole,
    login,
    register,
    logout,
  };
});
