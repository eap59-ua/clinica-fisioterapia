import api from './api'

export default {
  /**
   * Obtener citas de hoy del fisioterapeuta
   */
  getCitasHoy() {
    return api.get('/fisioterapeuta/citas/hoy')
  },

  /**
   * Obtener citas de la semana
   * @param {string} fechaInicio - Fecha en formato YYYY-MM-DD
   */
  getCitasSemana(fechaInicio) {
    return api.get('/fisioterapeuta/citas/semana', {
      params: { fechaInicio }
    })
  },

  /**
   * Obtener detalle de una cita
   * @param {number} citaId
   */
  getDetalleCita(citaId) {
    return api.get(`/fisioterapeuta/citas/${citaId}`)
  },

  /**
   * Marcar cita como completada
   * @param {number} citaId
   */
  completarCita(citaId) {
    return api.put(`/fisioterapeuta/citas/${citaId}/completar`)
  },

  /**
   * Guardar nota de sesión
   * @param {number} citaId
   * @param {object} notaData
   */
  guardarNota(citaId, notaData) {
    return api.post(`/fisioterapeuta/citas/${citaId}/nota`, notaData)
  },

  /**
   * Obtener nota de una cita
   * @param {number} citaId
   */
  getNotaCita(citaId) {
    return api.get(`/fisioterapeuta/citas/${citaId}/nota`)
  },

  /**
   * Obtener historial de un cliente
   * @param {number} clienteId
   */
  getHistorialCliente(clienteId) {
    return api.get(`/fisioterapeuta/clientes/${clienteId}/historial`)
  }
}
