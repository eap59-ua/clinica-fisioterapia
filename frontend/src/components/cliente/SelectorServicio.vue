<template>
  <div class="space-y-4">
    <h3 class="text-lg font-semibold text-gray-800">Selecciona el tratamiento</h3>

    <div class="grid grid-cols-1 md:grid-cols-2 gap-4">
      <div
        v-for="servicio in servicios"
        :key="servicio.id"
        @click="$emit('seleccionar', servicio)"
        :class="[
          'border-2 rounded-lg p-4 cursor-pointer transition-all',
          servicioSeleccionado?.id === servicio.id
            ? 'border-teal-500 bg-teal-50'
            : 'border-gray-200 hover:border-teal-300'
        ]"
      >
        <div class="flex justify-between items-start">
          <div>
            <h4 class="font-semibold text-gray-800">{{ servicio.nombre }}</h4>
            <p class="text-sm text-gray-600 mt-1">{{ servicio.descripcion }}</p>
          </div>
          <span class="text-lg font-bold text-teal-600">{{ servicio.precio }}€</span>
        </div>
        <div class="flex items-center gap-2 mt-3 text-sm text-gray-500">
          <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 8v4l3 3m6-3a9 9 0 11-18 0 9 9 0 0118 0z" />
          </svg>
          <span>{{ servicio.duracionMinutos }} min</span>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { defineProps, defineEmits } from 'vue'

defineProps({
  servicios: {
    type: Array,
    required: true
  },
  servicioSeleccionado: {
    type: Object,
    default: null
  }
})

defineEmits(['seleccionar'])
</script>
