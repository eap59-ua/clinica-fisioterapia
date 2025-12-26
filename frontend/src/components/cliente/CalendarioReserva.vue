<template>
  <div class="space-y-4">
    <h3 class="text-lg font-semibold text-gray-800">Selecciona fecha y hora</h3>

    <!-- Selector de días -->
    <div class="flex gap-2 overflow-x-auto pb-2">
      <div
        v-for="dia in diasDisponibles"
        :key="dia.fecha"
        @click="seleccionarDia(dia)"
        :class="[
          'flex-shrink-0 border-2 rounded-lg p-3 cursor-pointer transition-all min-w-[80px] text-center',
          diaSeleccionado?.fecha === dia.fecha
            ? 'border-teal-500 bg-teal-50'
            : 'border-gray-200 hover:border-teal-300'
        ]"
      >
        <div class="text-xs text-gray-600 uppercase">{{ dia.diaNombre }}</div>
        <div class="text-2xl font-bold text-gray-800">{{ dia.diaNumero }}</div>
        <div class="text-xs text-gray-600">{{ dia.mesNombre }}</div>
      </div>
    </div>

    <!-- Selector de horas -->
    <div v-if="diaSeleccionado">
      <h4 class="text-sm font-semibold text-gray-700 mb-3">Horarios disponibles</h4>

      <div v-if="cargando" class="text-center py-8">
        <div class="animate-spin rounded-full h-8 w-8 border-b-2 border-teal-600 mx-auto"></div>
        <p class="text-sm text-gray-600 mt-2">Consultando disponibilidad...</p>
      </div>

      <div v-else-if="horasDisponibles.length === 0" class="text-center py-8">
        <p class="text-gray-600">No hay horarios disponibles para este día</p>
      </div>

      <div v-else>
        <!-- Mañana -->
        <div v-if="horasMañana.length > 0" class="mb-4">
          <p class="text-sm text-gray-600 mb-2">MAÑANA</p>
          <div class="grid grid-cols-4 gap-2">
            <button
              v-for="hora in horasMañana"
              :key="hora"
              @click="$emit('seleccionar-hora', hora)"
              :class="[
                'py-2 px-3 rounded-lg border-2 transition-all',
                horaSeleccionada === hora
                  ? 'border-teal-500 bg-teal-500 text-white'
                  : 'border-gray-200 hover:border-teal-300'
              ]"
            >
              {{ formatearHora(hora) }}
            </button>
          </div>
        </div>

        <!-- Tarde -->
        <div v-if="horasTarde.length > 0">
          <p class="text-sm text-gray-600 mb-2">TARDE</p>
          <div class="grid grid-cols-4 gap-2">
            <button
              v-for="hora in horasTarde"
              :key="hora"
              @click="$emit('seleccionar-hora', hora)"
              :class="[
                'py-2 px-3 rounded-lg border-2 transition-all',
                horaSeleccionada === hora
                  ? 'border-teal-500 bg-teal-500 text-white'
                  : 'border-gray-200 hover:border-teal-300'
              ]"
            >
              {{ formatearHora(hora) }}
            </button>
          </div>
        </div>
      </div>

      <!-- Mensaje informativo -->
      <div class="mt-4 text-xs text-gray-500 bg-gray-50 p-3 rounded">
        ⏰ Cada slot indica la hora de inicio. La duración será según el servicio seleccionado.
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, watch } from 'vue'
import { defineProps, defineEmits } from 'vue'

const props = defineProps({
  disponibilidad: {
    type: Array,
    default: () => []
  },
  horaSeleccionada: {
    type: String,
    default: null
  },
  cargando: {
    type: Boolean,
    default: false
  }
})

const emit = defineEmits(['seleccionar-dia', 'seleccionar-hora'])

const diaSeleccionado = ref(null)

const diasDisponibles = computed(() => {
  return props.disponibilidad.map(dia => {
    const fecha = new Date(dia.fecha + 'T00:00:00')
    return {
      fecha: dia.fecha,
      diaNombre: fecha.toLocaleDateString('es-ES', { weekday: 'short' }).toUpperCase(),
      diaNumero: fecha.getDate(),
      mesNombre: fecha.toLocaleDateString('es-ES', { month: 'short' }).toUpperCase(),
      horas: dia.horasDisponibles
    }
  })
})

const horasDisponibles = computed(() => {
  return diaSeleccionado.value?.horas || []
})

const horasMañana = computed(() => {
  return horasDisponibles.value.filter(hora => {
    const h = parseInt(hora.split(':')[0])
    return h < 14
  })
})

const horasTarde = computed(() => {
  return horasDisponibles.value.filter(hora => {
    const h = parseInt(hora.split(':')[0])
    return h >= 14
  })
})

const seleccionarDia = (dia) => {
  diaSeleccionado.value = dia
  emit('seleccionar-dia', dia.fecha)
}

const formatearHora = (hora) => {
  return hora.substring(0, 5) // "09:00:00" -> "09:00"
}

// Auto-seleccionar primer día disponible
watch(() => props.disponibilidad, (newVal) => {
  if (newVal.length > 0 && !diaSeleccionado.value) {
    seleccionarDia(diasDisponibles.value[0])
  }
}, { immediate: true })
</script>
