import api from "./api";

export default {
  /**
   * Reservar una nueva cita
   */
  async reservarCita(datos) {
    const response = await api.post("/citas", datos);
    return response.data;
  },

  /**
   * Obtener todas las citas del cliente autenticado
   */
  async getMisCitas() {
    const response = await api.get("/citas/mis-citas");
    return response.data;
  },

  /**
   * Obtener próximas citas del cliente autenticado
   */
  async getProximasCitas() {
    const response = await api.get("/citas/proximas");
    return response.data;
  },

  /**
   * Cancelar una cita
   */
  async cancelarCita(citaId) {
    const response = await api.put(`/citas/${citaId}/cancelar`);
    return response.data;
  },

  /**
   * Obtener disponibilidad de un fisioterapeuta para múltiples días
   * @param {number} fisioterapeutaId - ID del fisioterapeuta
   * @param {string} fechaInicio - Fecha inicial en formato YYYY-MM-DD
   * @param {number} dias - Número de días a consultar
   * @returns {Promise<Array>} Array de objetos con fecha y horas disponibles
   */
  async getDisponibilidad(fisioterapeutaId, fechaInicio, dias = 7) {
    const disponibilidad = [];

    // Validar parámetros requeridos
    if (!fisioterapeutaId) {
      console.warn('getDisponibilidad: fisioterapeutaId no proporcionado');
      return disponibilidad;
    }

    // Obtener el servicio ID (asumimos que ya está seleccionado en el contexto)
    // Por ahora usaremos un valor por defecto, pero esto debería venir del componente
    const servicioId = 1; // TODO: Pasar como parámetro

    for (let i = 0; i < dias; i++) {
      const fecha = new Date(fechaInicio);
      fecha.setDate(fecha.getDate() + i);
      const fechaStr = fecha.toISOString().split('T')[0];

      try {
        const response = await api.get('/citas/disponibilidad', {
          params: {
            fisioterapeutaId,
            servicioId,
            fecha: fechaStr
          }
        });

        const data = response.data;

        // Transformar slots a formato simple (solo horas disponibles)
        const horasDisponibles = data.slots
          .filter(slot => slot.disponible)
          .map(slot => slot.horaInicio);

        // Solo añadir días que tengan horarios disponibles
        if (horasDisponibles.length > 0) {
          disponibilidad.push({
            fecha: fechaStr,
            horasDisponibles
          });
        }
      } catch (error) {
        console.error(`Error cargando disponibilidad para ${fechaStr}:`, error);
        // Continuar con el siguiente día incluso si este falla
      }
    }

    return disponibilidad;
  },

  /**
   * Obtener disponibilidad para un servicio y fisioterapeuta específicos
   * @param {number} fisioterapeutaId - ID del fisioterapeuta
   * @param {number} servicioId - ID del servicio
   * @param {string} fechaInicio - Fecha inicial en formato YYYY-MM-DD
   * @param {number} dias - Número de días a consultar
   */
  async getDisponibilidadConServicio(fisioterapeutaId, servicioId, fechaInicio, dias = 7) {
    const disponibilidad = [];

    // Validar parámetros requeridos
    if (!fisioterapeutaId || !servicioId) {
      console.warn('getDisponibilidadConServicio: fisioterapeutaId o servicioId no proporcionados', { fisioterapeutaId, servicioId });
      return disponibilidad;
    }

    for (let i = 0; i < dias; i++) {
      const fecha = new Date(fechaInicio);
      fecha.setDate(fecha.getDate() + i);
      const fechaStr = fecha.toISOString().split('T')[0];

      try {
        const response = await api.get('/citas/disponibilidad', {
          params: {
            fisioterapeutaId,
            servicioId,
            fecha: fechaStr
          }
        });

        const data = response.data;

        // Transformar slots a formato simple (solo horas disponibles)
        const horasDisponibles = data.slots
          .filter(slot => slot.disponible)
          .map(slot => slot.horaInicio);

        // Solo añadir días que tengan horarios disponibles
        if (horasDisponibles.length > 0) {
          disponibilidad.push({
            fecha: fechaStr,
            horasDisponibles
          });
        }
      } catch (error) {
        console.error(`Error cargando disponibilidad para ${fechaStr}:`, error);
      }
    }

    return disponibilidad;
  }
};
