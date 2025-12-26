<template>
  <div class="max-w-4xl mx-auto p-6">
    <!-- Header -->
    <div class="mb-6">
      <button
        @click="$router.push('/cliente')"
        class="flex items-center text-gray-600 hover:text-gray-800 mb-4"
      >
        <svg class="w-5 h-5 mr-2" fill="none" stroke="currentColor" viewBox="0 0 24 24">
          <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M15 19l-7-7 7-7" />
        </svg>
        Volver al Inicio
      </button>
      <h1 class="text-3xl font-bold text-gray-800">Reservar Nueva Cita</h1>
    </div>

    <!-- Stepper -->
    <div class="flex items-center justify-between mb-8">
      <div
        v-for="(step, index) in steps"
        :key="index"
        class="flex items-center"
      >
        <div :class="[
          'flex items-center justify-center w-10 h-10 rounded-full font-semibold',
          currentStep > index ? 'bg-teal-500 text-white' :
          currentStep === index ? 'bg-teal-500 text-white' :
          'bg-gray-200 text-gray-600'
        ]">
          {{ index + 1 }}
        </div>
        <span class="ml-2 text-sm font-medium text-gray-700">{{ step }}</span>
        <div
          v-if="index < steps.length - 1"
          :class="[
            'h-1 w-16 mx-4',
            currentStep > index ? 'bg-teal-500' : 'bg-gray-200'
          ]"
        ></div>
      </div>
    </div>

    <!-- Contenido de cada paso -->
    <div class="bg-white rounded-lg shadow-md p-6">
      <!-- Paso 1: Servicio -->
      <div v-show="currentStep === 0">
        <SelectorServicio
          :servicios="servicios"
          :servicio-seleccionado="reserva.servicio"
          @seleccionar="seleccionarServicio"
        />
      </div>

      <!-- Paso 2: Profesional -->
      <div v-show="currentStep === 1">
        <SelectorFisioterapeuta
          :fisioterapeutas="fisioterapeutas"
          :fisioterapeuta-seleccionado="reserva.fisioterapeuta"
          @seleccionar="seleccionarFisioterapeuta"
        />
      </div>

      <!-- Paso 3: Fecha y Hora -->
      <div v-show="currentStep === 2">
        <CalendarioReserva
          :disponibilidad="disponibilidad"
          :hora-seleccionada="reserva.horaInicio"
          :cargando="cargandoDisponibilidad"
          @seleccionar-dia="seleccionarDia"
          @seleccionar-hora="seleccionarHora"
        />
      </div>

      <!-- Paso 4: Confirmación -->
      <div v-show="currentStep === 3">
        <h3 class="text-lg font-semibold text-gray-800 mb-4">Resumen de tu Cita</h3>

        <div class="space-y-4 bg-gray-50 rounded-lg p-6">
          <div class="flex justify-between items-center pb-3 border-b">
            <span class="text-sm text-gray-600">TRATAMIENTO</span>
            <div class="text-right">
              <p class="font-semibold text-gray-800">{{ reserva.servicio?.nombre }}</p>
              <p class="text-sm text-gray-600">{{ reserva.servicio?.duracionMinutos }} min</p>
            </div>
          </div>

          <div class="flex justify-between items-center pb-3 border-b">
            <span class="text-sm text-gray-600">PROFESIONAL</span>
            <div class="text-right">
              <p class="font-semibold text-gray-800">
                {{ reserva.fisioterapeuta
                  ? `${reserva.fisioterapeuta.nombre} ${reserva.fisioterapeuta.apellidos}`
                  : 'Cualquiera disponible' }}
              </p>
            </div>
          </div>

          <div class="flex justify-between items-center pb-3 border-b">
            <span class="text-sm text-gray-600">FECHA Y HORA</span>
            <div class="text-right">
              <p class="font-semibold text-gray-800">
                {{ formatearFecha(reserva.fecha) }}
              </p>
              <p class="text-sm text-gray-600">{{ reserva.horaInicio }}</p>
            </div>
          </div>

          <div class="flex justify-between items-center text-lg font-bold pt-3">
            <span>PRECIO</span>
            <span class="text-teal-600">{{ reserva.servicio?.precio }}€</span>
          </div>
        </div>

        <div class="mt-4 bg-blue-50 border border-blue-200 rounded-lg p-4 flex items-start gap-3">
          <svg class="w-5 h-5 text-blue-600 flex-shrink-0 mt-0.5" fill="currentColor" viewBox="0 0 20 20">
            <path fill-rule="evenodd" d="M18 10a8 8 0 11-16 0 8 8 0 0116 0zm-7-4a1 1 0 11-2 0 1 1 0 012 0zM9 9a1 1 0 000 2v3a1 1 0 001 1h1a1 1 0 100-2v-3a1 1 0 00-1-1H9z" clip-rule="evenodd" />
          </svg>
          <div class="text-sm text-blue-800">
            <p class="font-semibold mb-1">Política de cancelación:</p>
            <p>La cancelación es gratuita hasta 24h antes de la cita. El pago se realizará en la clínica.</p>
          </div>
        </div>
      </div>
    </div>

    <!-- Botones de navegación -->
    <div class="flex justify-between mt-6">
      <button
        v-if="currentStep > 0"
        @click="anteriorPaso"
        class="px-6 py-2 border-2 border-gray-300 rounded-lg hover:bg-gray-50 transition"
      >
        Anterior
      </button>
      <div v-else></div>

      <button
        v-if="currentStep < 3"
        @click="siguientePaso"
        :disabled="!puedeAvanzar"
        :class="[
          'px-6 py-2 rounded-lg transition font-semibold',
          puedeAvanzar
            ? 'bg-teal-500 text-white hover:bg-teal-600'
            : 'bg-gray-300 text-gray-500 cursor-not-allowed'
        ]"
      >
        Siguiente
      </button>

      <button
        v-else
        @click="confirmarReserva"
        :disabled="enviando"
        :class="[
          'px-8 py-2 rounded-lg transition font-semibold',
          enviando
            ? 'bg-gray-300 text-gray-500 cursor-not-allowed'
            : 'bg-teal-500 text-white hover:bg-teal-600'
        ]"
      >
        <span v-if="!enviando">Confirmar Reserva</span>
        <span v-else class="flex items-center gap-2">
          <svg class="animate-spin h-5 w-5" fill="none" viewBox="0 0 24 24">
            <circle class="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" stroke-width="4"></circle>
            <path class="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4zm2 5.291A7.962 7.962 0 014 12H0c0 3.042 1.135 5.824 3 7.938l3-2.647z"></path>
          </svg>
          Procesando...
        </span>
      </button>
    </div>

    <!-- Modal de éxito -->
    <div
      v-if="mostrarExito"
      class="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50"
      @click="cerrarModalExito"
    >
      <div class="bg-white rounded-lg p-8 max-w-md text-center" @click.stop>
        <div class="w-16 h-16 bg-green-100 rounded-full flex items-center justify-center mx-auto mb-4">
          <svg class="w-8 h-8 text-green-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M5 13l4 4L19 7" />
          </svg>
        </div>
        <h3 class="text-2xl font-bold text-gray-800 mb-2">¡Cita Confirmada!</h3>
        <p class="text-gray-600 mb-6">
          Hemos enviado un email con los detalles a tu correo. Te esperamos el {{ formatearFecha(reserva.fecha) }}.
        </p>
        <button
          @click="volverAlPanel"
          class="w-full bg-gray-800 text-white py-3 rounded-lg hover:bg-gray-900 transition font-semibold"
        >
          Volver al Panel
        </button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import SelectorServicio from '@/components/cliente/SelectorServicio.vue'
import SelectorFisioterapeuta from '@/components/cliente/SelectorFisioterapeuta.vue'
import CalendarioReserva from '@/components/cliente/CalendarioReserva.vue'
import publicService from '@/services/publicService'
import citaService from '@/services/citaService'

const router = useRouter()

const steps = ['Servicio', 'Profesional', 'Fecha/Hora', 'Confirmar']
const currentStep = ref(0)

const servicios = ref([])
const fisioterapeutas = ref([])
const disponibilidad = ref([])

const reserva = ref({
  servicio: null,
  fisioterapeuta: null,
  fecha: null,
  horaInicio: null
})

const cargandoDisponibilidad = ref(false)
const enviando = ref(false)
const mostrarExito = ref(false)

// Cargar datos iniciales
onMounted(async () => {
  try {
    const [serviciosData, fisiosData] = await Promise.all([
      publicService.getServicios(),
      publicService.getFisioterapeutas()
    ])
    servicios.value = serviciosData.filter(s => s.activo)
    fisioterapeutas.value = fisiosData.filter(f => f.activo)
  } catch (error) {
    console.error('Error cargando datos:', error)
    alert('Error al cargar los datos. Por favor, recarga la página.')
  }
})

// Computed
const puedeAvanzar = computed(() => {
  switch (currentStep.value) {
    case 0:
      return reserva.value.servicio !== null
    case 1:
      return true // Siempre puede avanzar (puede elegir "cualquiera")
    case 2:
      return reserva.value.fecha !== null && reserva.value.horaInicio !== null
    default:
      return false
  }
})

// Métodos
const seleccionarServicio = (servicio) => {
  reserva.value.servicio = servicio
}

const seleccionarFisioterapeuta = async (fisioterapeuta) => {
  reserva.value.fisioterapeuta = fisioterapeuta

  // Cargar disponibilidad cuando se selecciona fisioterapeuta
  if (fisioterapeuta) {
    await cargarDisponibilidad(fisioterapeuta.usuarioId)
  } else {
    // Si selecciona "cualquiera", cargar del primer fisio disponible
    if (fisioterapeutas.value.length > 0) {
      await cargarDisponibilidad(fisioterapeutas.value[0].usuarioId)
    }
  }
}

const cargarDisponibilidad = async (fisioId) => {
  if (!reserva.value.servicio) {
    console.warn('No se puede cargar disponibilidad sin servicio seleccionado')
    return
  }

  cargandoDisponibilidad.value = true
  try {
    const hoy = new Date().toISOString().split('T')[0]
    disponibilidad.value = await citaService.getDisponibilidadConServicio(
      fisioId,
      reserva.value.servicio.id,
      hoy,
      7
    )
  } catch (error) {
    console.error('Error cargando disponibilidad:', error)
    alert('Error al cargar disponibilidad. Intenta de nuevo.')
  } finally {
    cargandoDisponibilidad.value = false
  }
}

const seleccionarDia = (fecha) => {
  reserva.value.fecha = fecha
  reserva.value.horaInicio = null // Reset hora al cambiar día
}

const seleccionarHora = (hora) => {
  reserva.value.horaInicio = hora.substring(0, 5) // "09:00:00" -> "09:00"
}

const siguientePaso = () => {
  if (puedeAvanzar.value && currentStep.value < 3) {
    currentStep.value++

    // Al pasar al paso 3 (fecha/hora), cargar disponibilidad si no está cargada
    if (currentStep.value === 2 && disponibilidad.value.length === 0) {
      const fisioId = reserva.value.fisioterapeuta?.usuarioId || fisioterapeutas.value[0]?.usuarioId
      if (fisioId) {
        cargarDisponibilidad(fisioId)
      }
    }
  }
}

const anteriorPaso = () => {
  if (currentStep.value > 0) {
    currentStep.value--
  }
}

const confirmarReserva = async () => {
  enviando.value = true
  try {
    const payload = {
      servicioId: reserva.value.servicio.id,
      fisioterapeutaId: reserva.value.fisioterapeuta?.usuarioId || fisioterapeutas.value[0].usuarioId,
      fecha: reserva.value.fecha,
      horaInicio: reserva.value.horaInicio + ':00' // "09:00" -> "09:00:00"
    }

    await citaService.reservarCita(payload)
    mostrarExito.value = true
  } catch (error) {
    console.error('Error al reservar cita:', error)
    const mensaje = error.response?.data?.message || 'Error al reservar la cita. Intenta de nuevo.'
    alert(mensaje)
  } finally {
    enviando.value = false
  }
}

const formatearFecha = (fecha) => {
  if (!fecha) return ''
  const date = new Date(fecha + 'T00:00:00')
  return date.toLocaleDateString('es-ES', {
    weekday: 'long',
    year: 'numeric',
    month: 'long',
    day: 'numeric'
  })
}

const cerrarModalExito = () => {
  mostrarExito.value = false
  volverAlPanel()
}

const volverAlPanel = () => {
  router.push('/cliente')
}
</script>
