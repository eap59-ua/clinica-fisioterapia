import { defineStore } from 'pinia';
import { ref, computed } from 'vue';
import fisioterapeutaService from '@/services/fisioterapeutaService';

export const useFisioterapeutaStore = defineStore('fisioterapeuta', () => {
  // State
  const citasHoy = ref([]);
  const citasSemana = ref([]);
  const citaActual = ref(null);
  const historialCliente = ref([]);
  const loading = ref(false);
  const error = ref(null);

  // Getters (computed)
  const citasCompletadasHoy = computed(() => {
    return citasHoy.value.filter(c => c.estado === 'COMPLETADA').length;
  });

  const citasPendientesHoy = computed(() => {
    return citasHoy.value.filter(c => c.estado === 'PENDIENTE' || c.estado === 'CONFIRMADA').length;
  });

  const totalCitasHoy = computed(() => {
    return citasHoy.value.length;
  });

  // Actions
  async function fetchCitasHoy() {
    loading.value = true;
    error.value = null;
    try {
      const response = await fisioterapeutaService.getCitasHoy();
      citasHoy.value = response.data;
    } catch (err) {
      error.value = err.message || 'Error al cargar citas de hoy';
      console.error('Error fetching citas hoy:', err);
    } finally {
      loading.value = false;
    }
  }

  async function fetchCitasSemana(fechaInicio) {
    loading.value = true;
    error.value = null;
    try {
      const response = await fisioterapeutaService.getCitasSemana(fechaInicio);
      citasSemana.value = response.data;
    } catch (err) {
      error.value = err.message || 'Error al cargar citas de la semana';
      console.error('Error fetching citas semana:', err);
    } finally {
      loading.value = false;
    }
  }

  async function fetchDetalleCita(citaId) {
    loading.value = true;
    error.value = null;
    try {
      const response = await fisioterapeutaService.getDetalleCita(citaId);
      citaActual.value = response.data;
    } catch (err) {
      error.value = err.message || 'Error al cargar detalle de la cita';
      console.error('Error fetching detalle cita:', err);
    } finally {
      loading.value = false;
    }
  }

  async function completarCita(citaId) {
    try {
      const response = await fisioterapeutaService.completarCita(citaId);
      // Actualizar en el array local
      const index = citasHoy.value.findIndex(c => c.id === citaId);
      if (index !== -1) {
        citasHoy.value[index] = response.data;
      }
      if (citaActual.value?.id === citaId) {
        citaActual.value = response.data;
      }
      return response.data;
    } catch (err) {
      error.value = err.message || 'Error al completar la cita';
      console.error('Error completing cita:', err);
      throw err;
    }
  }

  async function guardarNota(citaId, notaData) {
    try {
      const response = await fisioterapeutaService.guardarNota(citaId, notaData);
      return response.data;
    } catch (err) {
      error.value = err.message || 'Error al guardar la nota';
      console.error('Error saving nota:', err);
      throw err;
    }
  }

  async function fetchHistorialCliente(clienteId) {
    loading.value = true;
    error.value = null;
    try {
      const response = await fisioterapeutaService.getHistorialCliente(clienteId);
      historialCliente.value = response.data;
    } catch (err) {
      error.value = err.message || 'Error al cargar historial del cliente';
      console.error('Error fetching historial cliente:', err);
    } finally {
      loading.value = false;
    }
  }

  return {
    // State
    citasHoy,
    citasSemana,
    citaActual,
    historialCliente,
    loading,
    error,
    // Getters
    citasCompletadasHoy,
    citasPendientesHoy,
    totalCitasHoy,
    // Actions
    fetchCitasHoy,
    fetchCitasSemana,
    fetchDetalleCita,
    completarCita,
    guardarNota,
    fetchHistorialCliente
  };
});
