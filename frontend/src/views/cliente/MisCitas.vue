<template>
  <div class="max-w-6xl mx-auto p-6">
    <!-- Header -->
    <div class="mb-6">
      <h1 class="text-3xl font-bold text-gray-800">Mis Citas</h1>
      <p class="text-gray-600 mt-2">Gestiona tus citas pendientes e historial de sesiones</p>
    </div>

    <!-- Tabs -->
    <div class="border-b border-gray-200 mb-6">
      <nav class="flex gap-8">
        <button
          @click="tabActiva = 'proximas'"
          :class="[
            'pb-3 px-1 border-b-2 font-medium text-sm transition',
            tabActiva === 'proximas'
              ? 'border-teal-500 text-teal-600'
              : 'border-transparent text-gray-500 hover:text-gray-700'
          ]"
        >
          Próximas Citas
          <span v-if="proximasCitas.length > 0" class="ml-2 bg-teal-100 text-teal-600 py-0.5 px-2 rounded-full text-xs">
            {{ proximasCitas.length }}
          </span>
        </button>
        <button
          @click="tabActiva = 'historial'"
          :class="[
            'pb-3 px-1 border-b-2 font-medium text-sm transition',
            tabActiva === 'historial'
              ? 'border-teal-500 text-teal-600'
              : 'border-transparent text-gray-500 hover:text-gray-700'
          ]"
        >
          Historial
        </button>
      </nav>
    </div>

    <!-- Loading -->
    <div v-if="cargando" class="text-center py-12">
      <div class="animate-spin rounded-full h-12 w-12 border-b-2 border-teal-600 mx-auto"></div>
      <p class="text-gray-600 mt-4">Cargando tus citas...</p>
    </div>

    <!-- Contenido -->
    <div v-else>
      <!-- Próximas Citas -->
      <div v-if="tabActiva === 'proximas'">
        <div v-if="proximasCitas.length === 0" class="text-center py-12 bg-gray-50 rounded-lg">
          <svg class="w-16 h-16 text-gray-400 mx-auto mb-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M8 7V3m8 4V3m-9 8h10M5 21h14a2 2 0 002-2V7a2 2 0 00-2-2H5a2 2 0 00-2 2v12a2 2 0 002 2z" />
          </svg>
          <h3 class="text-xl font-semibold text-gray-800 mb-2">No tienes citas pendientes</h3>
          <p class="text-gray-600 mb-6">¿Necesitas agendar una nueva sesión?</p>
          <button
            @click="$router.push('/cliente/reservar-cita')"
            class="bg-teal-500 text-white px-6 py-3 rounded-lg hover:bg-teal-600 transition font-semibold"
          >
            Reservar Nueva Cita
          </button>
        </div>

        <div v-else class="grid grid-cols-1 md:grid-cols-2 gap-4">
          <div
            v-for="cita in proximasCitas"
            :key="cita.id"
            class="bg-white border-2 border-gray-200 rounded-lg p-6 hover:border-teal-300 transition"
          >
            <!-- Fecha destacada -->
            <div class="flex items-start justify-between mb-4">
              <div class="flex items-center gap-3">
                <div class="bg-teal-100 rounded-lg p-3 text-center">
                  <div class="text-xs text-teal-700 font-semibold uppercase">{{ getMes(cita.fecha) }}</div>
                  <div class="text-2xl font-bold text-teal-900">{{ getDia(cita.fecha) }}</div>
                </div>
                <div>
                  <p class="text-sm text-gray-500">{{ getDiaSemana(cita.fecha) }}</p>
                  <p class="text-lg font-semibold text-gray-800">{{ cita.horaInicio }}</p>
                </div>
              </div>
              <span :class="[
                'px-3 py-1 rounded-full text-xs font-semibold',
                getEstadoClasses(cita.estado)
              ]">
                {{ getEstadoTexto(cita.estado) }}
              </span>
            </div>

            <!-- Info de la cita -->
            <div class="space-y-2 mb-4">
              <div class="flex items-start gap-2">
                <svg class="w-5 h-5 text-gray-400 mt-0.5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 5H7a2 2 0 00-2 2v12a2 2 0 002 2h10a2 2 0 002-2V7a2 2 0 00-2-2h-2M9 5a2 2 0 002 2h2a2 2 0 002-2M9 5a2 2 0 012-2h2a2 2 0 012 2" />
                </svg>
                <div>
                  <p class="text-sm text-gray-600">Tratamiento</p>
                  <p class="font-semibold text-gray-800">{{ cita.servicio.nombre }}</p>
                  <p class="text-xs text-gray-500">{{ cita.servicio.duracionMinutos }} minutos</p>
                </div>
              </div>

              <div class="flex items-start gap-2">
                <svg class="w-5 h-5 text-gray-400 mt-0.5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M16 7a4 4 0 11-8 0 4 4 0 018 0zM12 14a7 7 0 00-7 7h14a7 7 0 00-7-7z" />
                </svg>
                <div>
                  <p class="text-sm text-gray-600">Fisioterapeuta</p>
                  <p class="font-semibold text-gray-800">{{ cita.fisioterapeuta.nombre }} {{ cita.fisioterapeuta.apellidos }}</p>
                  <p class="text-xs text-gray-500">{{ cita.fisioterapeuta.especialidades }}</p>
                </div>
              </div>

              <div class="flex items-start gap-2">
                <svg class="w-5 h-5 text-gray-400 mt-0.5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M17.657 16.657L13.414 20.9a1.998 1.998 0 01-2.827 0l-4.244-4.243a8 8 0 1111.314 0z" />
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M15 11a3 3 0 11-6 0 3 3 0 016 0z" />
                </svg>
                <div>
                  <p class="text-sm text-gray-600">Sala</p>
                  <p class="font-semibold text-gray-800">{{ cita.sala?.nombre || 'Por asignar' }}</p>
                </div>
              </div>
            </div>

            <!-- Precio -->
            <div class="border-t pt-3 mb-4">
              <div class="flex justify-between items-center">
                <span class="text-sm text-gray-600">Precio</span>
                <span class="text-xl font-bold text-teal-600">{{ cita.servicio.precio }}€</span>
              </div>
            </div>

            <!-- Acciones -->
            <div class="flex gap-2">
              <button
                @click="verDetalle(cita)"
                class="flex-1 border-2 border-gray-300 text-gray-700 py-2 rounded-lg hover:bg-gray-50 transition font-medium"
              >
                Ver Detalles
              </button>
              <button
                @click="confirmarCancelacion(cita)"
                class="flex-1 border-2 border-red-300 text-red-600 py-2 rounded-lg hover:bg-red-50 transition font-medium"
              >
                Cancelar
              </button>
            </div>
          </div>
        </div>
      </div>

      <!-- Historial -->
      <div v-if="tabActiva === 'historial'">
        <div v-if="historialCitas.length === 0" class="text-center py-12 bg-gray-50 rounded-lg">
          <svg class="w-16 h-16 text-gray-400 mx-auto mb-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 12h6m-6 4h6m2 5H7a2 2 0 01-2-2V5a2 2 0 012-2h5.586a1 1 0 01.707.293l5.414 5.414a1 1 0 01.293.707V19a2 2 0 01-2 2z" />
          </svg>
          <h3 class="text-xl font-semibold text-gray-800 mb-2">Aún no tienes historial</h3>
          <p class="text-gray-600">Aquí aparecerán tus citas pasadas y canceladas</p>
        </div>

        <div v-else class="space-y-4">
          <div
            v-for="cita in historialCitas"
            :key="cita.id"
            class="bg-white border border-gray-200 rounded-lg p-5 hover:shadow-md transition"
          >
            <div class="flex items-start justify-between">
              <div class="flex-1">
                <div class="flex items-center gap-3 mb-2">
                  <span class="text-lg font-semibold text-gray-800">
                    {{ formatearFechaCompleta(cita.fecha) }}
                  </span>
                  <span class="text-gray-500">•</span>
                  <span class="text-gray-600">{{ cita.horaInicio }}</span>
                  <span :class="[
                    'px-2 py-1 rounded-full text-xs font-semibold',
                    getEstadoClasses(cita.estado)
                  ]">
                    {{ getEstadoTexto(cita.estado) }}
                  </span>
                </div>
                <p class="text-gray-800 font-medium">{{ cita.servicio.nombre }}</p>
                <p class="text-sm text-gray-600">Con {{ cita.fisioterapeuta.nombre }} {{ cita.fisioterapeuta.apellidos }}</p>
              </div>
              <div class="text-right">
                <p class="text-lg font-bold text-gray-800">{{ cita.servicio.precio }}€</p>
                <button
                  @click="verDetalle(cita)"
                  class="text-teal-600 hover:text-teal-700 text-sm font-medium mt-2"
                >
                  Ver detalles →
                </button>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- Modal de detalle -->
    <div
      v-if="modalDetalle"
      class="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50 p-4"
      @click="cerrarDetalle"
    >
      <div class="bg-white rounded-lg max-w-2xl w-full max-h-[90vh] overflow-y-auto" @click.stop>
        <!-- Header del modal -->
        <div class="sticky top-0 bg-white border-b px-6 py-4 flex justify-between items-center">
          <h3 class="text-xl font-bold text-gray-800">Detalle de la Cita</h3>
          <button @click="cerrarDetalle" class="text-gray-400 hover:text-gray-600">
            <svg class="w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12" />
            </svg>
          </button>
        </div>

        <!-- Contenido del modal -->
        <div class="p-6 space-y-6">
          <!-- Fecha y hora -->
          <div class="bg-teal-50 rounded-lg p-4">
            <p class="text-sm text-teal-700 font-semibold mb-1">FECHA Y HORA</p>
            <p class="text-2xl font-bold text-teal-900">
              {{ formatearFechaCompleta(citaSeleccionada.fecha) }}
            </p>
            <p class="text-lg text-teal-800">{{ citaSeleccionada.horaInicio }} - {{ citaSeleccionada.horaFin }}</p>
          </div>

          <!-- Información detallada -->
          <div class="grid grid-cols-2 gap-4">
            <div>
              <p class="text-sm text-gray-600 mb-1">Tratamiento</p>
              <p class="font-semibold text-gray-800">{{ citaSeleccionada.servicio.nombre }}</p>
              <p class="text-sm text-gray-500">{{ citaSeleccionada.servicio.duracionMinutos }} minutos</p>
            </div>
            <div>
              <p class="text-sm text-gray-600 mb-1">Precio</p>
              <p class="text-2xl font-bold text-teal-600">{{ citaSeleccionada.servicio.precio }}€</p>
            </div>
            <div>
              <p class="text-sm text-gray-600 mb-1">Fisioterapeuta</p>
              <p class="font-semibold text-gray-800">{{ citaSeleccionada.fisioterapeuta.nombre }} {{ citaSeleccionada.fisioterapeuta.apellidos }}</p>
              <p class="text-sm text-gray-500">{{ citaSeleccionada.fisioterapeuta.especialidades }}</p>
            </div>
            <div>
              <p class="text-sm text-gray-600 mb-1">Sala</p>
              <p class="font-semibold text-gray-800">{{ citaSeleccionada.sala?.nombre || 'Por asignar' }}</p>
            </div>
            <div class="col-span-2">
              <p class="text-sm text-gray-600 mb-1">Estado</p>
              <span :class="[
                'inline-block px-3 py-1 rounded-full text-sm font-semibold',
                getEstadoClasses(citaSeleccionada.estado)
              ]">
                {{ getEstadoTexto(citaSeleccionada.estado) }}
              </span>
            </div>
          </div>

          <!-- Notas (si existen) -->
          <div v-if="citaSeleccionada.notas" class="bg-gray-50 rounded-lg p-4">
            <p class="text-sm text-gray-600 font-semibold mb-2">NOTAS</p>
            <p class="text-gray-800">{{ citaSeleccionada.notas }}</p>
          </div>
        </div>

        <!-- Footer del modal -->
        <div class="border-t px-6 py-4 bg-gray-50">
          <button
            @click="cerrarDetalle"
            class="w-full bg-gray-800 text-white py-3 rounded-lg hover:bg-gray-900 transition font-semibold"
          >
            Cerrar
          </button>
        </div>
      </div>
    </div>

    <!-- Modal de confirmación de cancelación -->
    <div
      v-if="modalCancelar"
      class="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50 p-4"
      @click="cerrarModalCancelar"
    >
      <div class="bg-white rounded-lg max-w-md w-full" @click.stop>
        <div class="p-6">
          <div class="w-12 h-12 bg-red-100 rounded-full flex items-center justify-center mx-auto mb-4">
            <svg class="w-6 h-6 text-red-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 9v2m0 4h.01m-6.938 4h13.856c1.54 0 2.502-1.667 1.732-3L13.732 4c-.77-1.333-2.694-1.333-3.464 0L3.34 16c-.77 1.333.192 3 1.732 3z" />
            </svg>
          </div>
          <h3 class="text-xl font-bold text-gray-800 text-center mb-2">¿Cancelar esta cita?</h3>
          <p class="text-gray-600 text-center mb-6">
            Estás a punto de cancelar tu cita del {{ formatearFechaCompleta(citaACancelar?.fecha) }} a las {{ citaACancelar?.horaInicio }}.
          </p>
          <div class="flex gap-3">
            <button
              @click="cerrarModalCancelar"
              class="flex-1 border-2 border-gray-300 text-gray-700 py-3 rounded-lg hover:bg-gray-50 transition font-semibold"
            >
              No, mantener
            </button>
            <button
              @click="ejecutarCancelacion"
              :disabled="cancelando"
              class="flex-1 bg-red-600 text-white py-3 rounded-lg hover:bg-red-700 transition font-semibold disabled:bg-gray-400"
            >
              {{ cancelando ? 'Cancelando...' : 'Sí, cancelar' }}
            </button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import citaService from '@/services/citaService'

const router = useRouter()

const tabActiva = ref('proximas')
const cargando = ref(true)
const todasLasCitas = ref([])
const modalDetalle = ref(false)
const citaSeleccionada = ref(null)
const modalCancelar = ref(false)
const citaACancelar = ref(null)
const cancelando = ref(false)

// Computed
const proximasCitas = computed(() => {
  return todasLasCitas.value.filter(cita => {
    const hoy = new Date()
    hoy.setHours(0, 0, 0, 0)
    const fechaCita = new Date(cita.fecha + 'T00:00:00')
    return fechaCita >= hoy && cita.estado === 'PENDIENTE'
  }).sort((a, b) => {
    const fechaA = new Date(a.fecha + 'T' + a.horaInicio)
    const fechaB = new Date(b.fecha + 'T' + b.horaInicio)
    return fechaA - fechaB
  })
})

const historialCitas = computed(() => {
  return todasLasCitas.value.filter(cita => {
    const hoy = new Date()
    hoy.setHours(0, 0, 0, 0)
    const fechaCita = new Date(cita.fecha + 'T00:00:00')
    return fechaCita < hoy || cita.estado !== 'PENDIENTE'
  }).sort((a, b) => {
    const fechaA = new Date(a.fecha + 'T' + a.horaInicio)
    const fechaB = new Date(b.fecha + 'T' + b.horaInicio)
    return fechaB - fechaA // Más recientes primero
  })
})

// Métodos
onMounted(async () => {
  await cargarCitas()
})

const cargarCitas = async () => {
  cargando.value = true
  try {
    todasLasCitas.value = await citaService.getMisCitas()
  } catch (error) {
    console.error('Error cargando citas:', error)
    alert('Error al cargar tus citas. Por favor, recarga la página.')
  } finally {
    cargando.value = false
  }
}

const verDetalle = (cita) => {
  citaSeleccionada.value = cita
  modalDetalle.value = true
}

const cerrarDetalle = () => {
  modalDetalle.value = false
  citaSeleccionada.value = null
}

const confirmarCancelacion = (cita) => {
  citaACancelar.value = cita
  modalCancelar.value = true
}

const cerrarModalCancelar = () => {
  modalCancelar.value = false
  citaACancelar.value = null
}

const ejecutarCancelacion = async () => {
  cancelando.value = true
  try {
    await citaService.cancelarCita(citaACancelar.value.id)
    alert('Cita cancelada correctamente')
    cerrarModalCancelar()
    await cargarCitas() // Recargar lista
  } catch (error) {
    console.error('Error cancelando cita:', error)
    const mensaje = error.response?.data?.message || 'Error al cancelar la cita. Intenta de nuevo.'
    alert(mensaje)
  } finally {
    cancelando.value = false
  }
}

// Helpers de formato
const getDia = (fecha) => {
  const date = new Date(fecha + 'T00:00:00')
  return date.getDate()
}

const getMes = (fecha) => {
  const date = new Date(fecha + 'T00:00:00')
  return date.toLocaleDateString('es-ES', { month: 'short' }).toUpperCase()
}

const getDiaSemana = (fecha) => {
  const date = new Date(fecha + 'T00:00:00')
  return date.toLocaleDateString('es-ES', { weekday: 'long' })
}

const formatearFechaCompleta = (fecha) => {
  const date = new Date(fecha + 'T00:00:00')
  return date.toLocaleDateString('es-ES', {
    weekday: 'long',
    year: 'numeric',
    month: 'long',
    day: 'numeric'
  })
}

const getEstadoTexto = (estado) => {
  const estados = {
    PENDIENTE: 'Confirmada',
    COMPLETADA: 'Completada',
    CANCELADA: 'Cancelada',
    NO_ASISTIO: 'No asistió'
  }
  return estados[estado] || estado
}

const getEstadoClasses = (estado) => {
  const clases = {
    PENDIENTE: 'bg-green-100 text-green-700',
    COMPLETADA: 'bg-blue-100 text-blue-700',
    CANCELADA: 'bg-red-100 text-red-700',
    NO_ASISTIO: 'bg-gray-100 text-gray-700'
  }
  return clases[estado] || 'bg-gray-100 text-gray-700'
}
</script>
