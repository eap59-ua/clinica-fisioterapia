import api from "./api";

export default {
  /**
   * Obtener perfil del usuario autenticado
   */
  async getPerfil() {
    const response = await api.get("/perfil");
    return response.data;
  },

  /**
   * Actualizar perfil del usuario
   * @param {Object} datos - Datos a actualizar
   * @param {string} datos.nombre - Nombre del usuario
   * @param {string} datos.apellidos - Apellidos del usuario
   * @param {string} datos.email - Email del usuario
   * @param {string} datos.telefono - Teléfono del usuario
   * @param {string} [datos.currentPassword] - Contraseña actual (solo si se cambia contraseña)
   * @param {string} [datos.newPassword] - Nueva contraseña (solo si se cambia contraseña)
   */
  async updatePerfil(datos) {
    const response = await api.put("/perfil", datos);
    return response.data;
  }
};
