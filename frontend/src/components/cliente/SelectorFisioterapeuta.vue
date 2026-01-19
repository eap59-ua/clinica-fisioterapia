<template>
  <div class="space-y-4">
    <h3 class="text-lg font-semibold text-gray-800">Elige al profesional</h3>

    <!-- Opción: Cualquiera disponible -->
    <div
      @click="$emit('seleccionar', null)"
      :class="[
        'border-2 rounded-lg p-4 cursor-pointer transition-all',
        fisioterapeutaSeleccionado === null
          ? 'border-teal-500 bg-teal-50'
          : 'border-gray-200 hover:border-teal-300'
      ]"
    >
      <div class="flex items-center gap-3">
        <div class="w-10 h-10 rounded-full bg-gray-300 flex items-center justify-center">
          <span class="text-lg">❓</span>
        </div>
        <div>
          <h4 class="font-semibold text-gray-800">Cualquier Profesional Disponible</h4>
          <p class="text-sm text-gray-600">Máxima disponibilidad horaria</p>
        </div>
      </div>
    </div>

    <!-- Lista de fisioterapeutas -->
    <div
      v-for="fisio in fisioterapeutas"
      :key="fisio.id"
      @click="$emit('seleccionar', fisio)"
      :class="[
        'border-2 rounded-lg p-4 cursor-pointer transition-all',
        fisioterapeutaSeleccionado?.id === fisio.id
          ? 'border-teal-500 bg-teal-50'
          : 'border-gray-200 hover:border-teal-300'
      ]"
    >
      <div class="flex items-center gap-3">
        <!-- Avatar -->
        <div class="w-12 h-12 rounded-full bg-teal-200 flex items-center justify-center text-teal-700 font-semibold">
          {{ getInitials(fisio.nombre, fisio.apellidos) }}
        </div>

        <div class="flex-1">
          <div class="flex items-center gap-2">
            <h4 class="font-semibold text-gray-800">
              {{ fisio.nombre }} {{ fisio.apellidos }}
            </h4>
            <div class="flex items-center text-yellow-500">
              <svg class="w-4 h-4" fill="currentColor" viewBox="0 0 20 20">
                <path d="M9.049 2.927c.3-.921 1.603-.921 1.902 0l1.07 3.292a1 1 0 00.95.69h3.462c.969 0 1.371 1.24.588 1.81l-2.8 2.034a1 1 0 00-.364 1.118l1.07 3.292c.3.921-.755 1.688-1.54 1.118l-2.8-2.034a1 1 0 00-1.175 0l-2.8 2.034c-.784.57-1.838-.197-1.539-1.118l1.07-3.292a1 1 0 00-.364-1.118L2.98 8.72c-.783-.57-.38-1.81.588-1.81h3.461a1 1 0 00.951-.69l1.07-3.292z" />
              </svg>
              <span class="text-sm ml-1">{{ fisio.valoracionPromedio }}</span>
            </div>
          </div>
          <p class="text-sm text-gray-600">{{ fisio.especialidades }}</p>
          <p class="text-xs text-gray-500 mt-1">Col: {{ fisio.numeroColegiado }}</p>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { defineProps, defineEmits } from 'vue'

defineProps({
  fisioterapeutas: {
    type: Array,
    required: true
  },
  fisioterapeutaSeleccionado: {
    type: Object,
    default: null
  }
})

defineEmits(['seleccionar'])

const getInitials = (nombre, apellidos) => {
  return (nombre[0] + apellidos[0]).toUpperCase()
}
</script>
