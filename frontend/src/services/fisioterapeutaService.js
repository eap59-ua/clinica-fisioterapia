import api from './api'

export default {
  /**
   * Obtener citas de hoy del fisioterapeuta
   * @returns {Promise} Lista de citas de hoy
   */
  getCitasHoy() {
    return api.get('/fisioterapeuta/citas/hoy')
  },

  /**
   * Obtener citas de la semana
   * @param {string} fechaInicio - Fecha de inicio en formato YYYY-MM-DD
   * @returns {Promise} Lista de citas de la semana
   */
  getCitasSemana(fechaInicio) {
    return api.get('/fisioterapeuta/citas/semana', {
      params: { fechaInicio }
    })
  },

  /**
   * Obtener todas las citas del fisioterapeuta
   * @param {string} fecha - Fecha opcional en formato YYYY-MM-DD
   * @returns {Promise} Lista de citas
   */
  getCitas(fecha = null) {
    const params = fecha ? { fecha } : {}
    return api.get('/fisioterapeuta/citas', { params })
  },

  /**
   * Obtener detalle de una cita específica
   * @param {number} citaId - ID de la cita
   * @returns {Promise} Detalle de la cita
   */
  getDetalleCita(citaId) {
    return api.get(`/fisioterapeuta/citas/${citaId}`)
  },

  /**
   * Marcar una cita como completada
   * @param {number} citaId - ID de la cita
   * @returns {Promise} Cita actualizada
   */
  completarCita(citaId) {
    return api.put(`/fisioterapeuta/citas/${citaId}/completar`)
  },

  /**
   * Crear o actualizar nota de sesión
   * @param {number} citaId - ID de la cita
   * @param {object} notaData - Datos de la nota
   * @param {string} notaData.contenido - Contenido principal (required)
   * @param {string} notaData.diagnostico - Diagnóstico (optional)
   * @param {string} notaData.tratamientoAplicado - Tratamiento aplicado (optional)
   * @param {string} notaData.recomendaciones - Recomendaciones (optional)
   * @returns {Promise} Nota creada/actualizada
   */
  guardarNota(citaId, notaData) {
    return api.post(`/fisioterapeuta/citas/${citaId}/nota`, notaData)
  },

  /**
   * Obtener nota de una cita
   * @param {number} citaId - ID de la cita
   * @returns {Promise} Nota de la sesión
   */
  getNotaCita(citaId) {
    return api.get(`/fisioterapeuta/citas/${citaId}/nota`)
  },

  /**
   * Obtener historial completo de un cliente
   * @param {number} clienteId - ID del cliente
   * @returns {Promise} Lista de historial de citas
   */
  getHistorialCliente(clienteId) {
    return api.get(`/fisioterapeuta/clientes/${clienteId}/historial`)
  }
}
