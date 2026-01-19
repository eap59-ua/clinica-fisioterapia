import api from "./api";

export default {
  /**
   * Inicia el proceso de pago para una cita
   * @param {number} citaId - ID de la cita a pagar
   * @returns {Promise<Object>} Respuesta con URL de pago
   */
  async iniciarPago(citaId) {
    const response = await api.post(`/pagos/iniciar/${citaId}`);
    return response.data;
  },

  /**
   * Verifica el estado de pago de una cita
   * @param {number} citaId - ID de la cita
   * @returns {Promise<Object>} Estado del pago
   */
  async verificarEstado(citaId) {
    const response = await api.get(`/pagos/estado/${citaId}`);
    return response.data;
  },

  /**
   * Solicita reembolso de una cita
   * @param {number} citaId - ID de la cita
   * @returns {Promise<Object>} Resultado del reembolso
   */
  async solicitarReembolso(citaId) {
    const response = await api.post(`/pagos/reembolso/${citaId}`);
    return response.data;
  },

  /**
   * Confirma pago en modo mock (solo desarrollo)
   * @param {number} citaId - ID de la cita
   * @returns {Promise<string>} Mensaje de confirmación
   */
  async mockConfirmarPago(citaId) {
    const response = await api.post(`/pagos/mock-confirmar/${citaId}`);
    return response.data;
  },
};
