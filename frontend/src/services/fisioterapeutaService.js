import api from './api';

export default {
  // Obtener todas mis citas (con filtro opcional por fecha)
  getCitas(fecha = null) {
    const params = fecha ? { fecha } : {};
    return api.get('/fisioterapeuta/citas', { params });
  },

  // Obtener citas de hoy
  getCitasHoy() {
    return api.get('/fisioterapeuta/citas/hoy');
  },

  // Obtener citas de la semana
  getCitasSemana(fechaInicio) {
    return api.get('/fisioterapeuta/citas/semana', {
      params: { fechaInicio }
    });
  },

  // Obtener detalle de una cita
  getDetalleCita(citaId) {
    return api.get(`/fisioterapeuta/citas/${citaId}`);
  },

  // Marcar cita como completada
  completarCita(citaId) {
    return api.put(`/fisioterapeuta/citas/${citaId}/completar`);
  },

  // Crear/actualizar nota de sesión
  guardarNota(citaId, notaData) {
    return api.post(`/fisioterapeuta/citas/${citaId}/nota`, notaData);
  },

  // Obtener nota de una cita
  getNotaCita(citaId) {
    return api.get(`/fisioterapeuta/citas/${citaId}/nota`);
  },

  // Obtener historial completo de un cliente
  getHistorialCliente(clienteId) {
    return api.get(`/fisioterapeuta/clientes/${clienteId}/historial`);
  }
};
