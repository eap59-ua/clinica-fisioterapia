import { defineStore } from 'pinia'
import { ref, computed } from 'vue'
import fisioterapeutaService from '@/services/fisioterapeutaService'

export const useFisioterapeutaStore = defineStore('fisioterapeuta', () => {
  // State
  const citasHoy = ref([])
  const citasSemana = ref([])
  const citaActual = ref(null)
  const historialCliente = ref([])
  const loading = ref(false)
  const error = ref(null)

  // Getters (computed)
  const citasCompletadasHoy = computed(() => {
    return citasHoy.value.filter(c => c.estado === 'COMPLETADA').length
  })

  const citasPendientesHoy = computed(() => {
    return citasHoy.value.filter(
      c => c.estado === 'PENDIENTE' || c.estado === 'CONFIRMADA'
    ).length
  })

  const totalCitasHoy = computed(() => {
    return citasHoy.value.length
  })

  // Actions
  async function fetchCitasHoy() {
    loading.value = true
    error.value = null
    try {
      const response = await fisioterapeutaService.getCitasHoy()
      citasHoy.value = response.data
    } catch (err) {
      error.value = err.response?.data?.message || 'Error al cargar citas de hoy'
      console.error('Error fetchCitasHoy:', err)
    } finally {
      loading.value = false
    }
  }

  async function fetchCitasSemana(fechaInicio) {
    loading.value = true
    error.value = null
    try {
      const response = await fisioterapeutaService.getCitasSemana(fechaInicio)
      citasSemana.value = response.data
    } catch (err) {
      error.value = err.response?.data?.message || 'Error al cargar citas de la semana'
      console.error('Error fetchCitasSemana:', err)
    } finally {
      loading.value = false
    }
  }

  async function fetchDetalleCita(citaId) {
    loading.value = true
    error.value = null
    try {
      const response = await fisioterapeutaService.getDetalleCita(citaId)
      citaActual.value = response.data
      return response.data
    } catch (err) {
      error.value = err.response?.data?.message || 'Error al cargar detalle de cita'
      console.error('Error fetchDetalleCita:', err)
      throw err
    } finally {
      loading.value = false
    }
  }

  async function completarCita(citaId) {
    try {
      const response = await fisioterapeutaService.completarCita(citaId)

      // Actualizar en el array de citas de hoy
      const indexHoy = citasHoy.value.findIndex(c => c.id === citaId)
      if (indexHoy !== -1) {
        citasHoy.value[indexHoy] = response.data
      }

      // Actualizar en el array de citas de la semana
      const indexSemana = citasSemana.value.findIndex(c => c.id === citaId)
      if (indexSemana !== -1) {
        citasSemana.value[indexSemana] = response.data
      }

      // Actualizar cita actual si coincide
      if (citaActual.value?.id === citaId) {
        citaActual.value = response.data
      }

      return response.data
    } catch (err) {
      error.value = err.response?.data?.message || 'Error al completar cita'
      console.error('Error completarCita:', err)
      throw err
    }
  }

  async function guardarNota(citaId, notaData) {
    try {
      const response = await fisioterapeutaService.guardarNota(citaId, notaData)
      return response.data
    } catch (err) {
      error.value = err.response?.data?.message || 'Error al guardar nota'
      console.error('Error guardarNota:', err)
      throw err
    }
  }

  async function fetchNotaCita(citaId) {
    try {
      const response = await fisioterapeutaService.getNotaCita(citaId)
      return response.data
    } catch (err) {
      // Es normal que no exista nota, no es un error crítico
      if (err.response?.status === 404) {
        return null
      }
      error.value = err.response?.data?.message || 'Error al cargar nota'
      console.error('Error fetchNotaCita:', err)
      throw err
    }
  }

  async function fetchHistorialCliente(clienteId) {
    loading.value = true
    error.value = null
    try {
      const response = await fisioterapeutaService.getHistorialCliente(clienteId)
      historialCliente.value = response.data
      return response.data
    } catch (err) {
      error.value = err.response?.data?.message || 'Error al cargar historial del cliente'
      console.error('Error fetchHistorialCliente:', err)
      throw err
    } finally {
      loading.value = false
    }
  }

  function clearError() {
    error.value = null
  }

  function resetCitaActual() {
    citaActual.value = null
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
    fetchNotaCita,
    fetchHistorialCliente,
    clearError,
    resetCitaActual
  }
})
