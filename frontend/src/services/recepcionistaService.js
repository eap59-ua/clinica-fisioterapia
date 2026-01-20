import api from './api';

export default {
  getClientes() {
    return api.get('/recepcionista/clientes'); // O la ruta donde tengas listado de clientes
  },
  getFisioterapeutas() {
    return api.get('/recepcionista/fisioterapeutas'); // Filtrar por activos si es posible
  },
  getServicios() {
    return api.get('/recepcionista/servicios');
  },
  getSalas() {
    return api.get('/recepcionista/salas');
  },

  getCitasDia(fecha) {
    return api.get(`/recepcionista/citas/dia?fecha=${fecha}`);
  },

  getCitasSemana(fecha) {
    return api.get(`/recepcionista/citas/semana?fecha=${fecha}`);
  },

  crearCita(citaData) {
    return api.post('/recepcionista/citas', citaData);
  },

  cambiarEstado(id, estado) {
    return api.put(`/recepcionista/citas/${id}/estado?estado=${estado}`);
  },

  // FR-REC-02
    buscarClientes(query) {
      return api.get(`/recepcionista/clientes/buscar?query=${query}`);
    },

    // FR-REC-04: Obtener datos para rellenar el formulario de edición
    getCitaPorId(id) {
      return api.get(`/recepcionista/citas/${id}`);
    },

    // FR-REC-04: Guardar los cambios
    actualizarCita(id, cita) {
      return api.put(`/recepcionista/citas/${id}`, cita);
    },

    getHuecosLibres(fecha, fisioterapeutaId, salaId, duracion) { // <--- AÑADIDO PARAMETRO salaId
        // Creamos el objeto params dinámicamente
        const params = { fecha, fisioterapeutaId, duracion };

        // Solo enviamos salaId si tiene valor (no null ni string vacio)
        if (salaId) {
            params.salaId = salaId;
        }

        return api.get(`/recepcionista/disponibilidad`, { params });
    }
};