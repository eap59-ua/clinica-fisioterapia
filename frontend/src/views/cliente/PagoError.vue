<template>
  <div class="min-h-screen bg-gradient-to-br from-dark-50 via-red-50/50 to-orange-50/30 flex items-center justify-center p-6">
    <!-- Background Elements -->
    <div class="absolute inset-0 overflow-hidden pointer-events-none">
      <div class="absolute -top-40 -right-40 w-80 h-80 bg-red-200/40 rounded-full blur-3xl"></div>
      <div class="absolute -bottom-40 -left-40 w-80 h-80 bg-orange-200/30 rounded-full blur-3xl"></div>
    </div>

    <div class="relative w-full max-w-md">
      <div class="bg-white/80 backdrop-blur-xl rounded-3xl shadow-soft-xl p-8 border border-white/50 text-center animate-scale-in">
        <!-- Error Icon -->
        <div class="mb-6">
          <div class="w-20 h-20 bg-gradient-to-br from-red-400 to-red-600 rounded-full flex items-center justify-center mx-auto shadow-lg">
            <svg class="w-10 h-10 text-white" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2.5" d="M6 18L18 6M6 6l12 12" />
            </svg>
          </div>
        </div>

        <h1 class="font-display text-3xl font-bold text-dark-800 mb-3">Error en el Pago</h1>
        <p class="text-dark-500 mb-6">
          No se ha podido procesar tu pago. Tu cita ha sido reservada pero está pendiente de pago.
        </p>

        <!-- Error Message -->
        <div v-if="errorMessage" class="bg-red-50 border border-red-200 rounded-xl p-4 mb-6 text-left">
          <div class="flex items-start gap-3">
            <div class="w-8 h-8 bg-red-100 rounded-lg flex items-center justify-center flex-shrink-0">
              <svg class="w-4 h-4 text-red-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 8v4m0 4h.01M21 12a9 9 0 11-18 0 9 9 0 0118 0z"/>
              </svg>
            </div>
            <p class="text-sm text-red-600">{{ errorMessage }}</p>
          </div>
        </div>

        <!-- Cita Reference -->
        <div v-if="citaId" class="bg-dark-50 rounded-xl p-4 mb-6">
          <p class="text-xs text-dark-400 mb-1">Referencia de cita</p>
          <p class="font-mono text-lg font-semibold text-dark-700">#{{ citaId }}</p>
        </div>

        <div class="space-y-3">
          <button
            @click="reintentar"
            class="w-full bg-gradient-to-r from-primary-500 to-primary-600 text-white py-3.5 rounded-xl font-semibold shadow-soft hover:shadow-glow transition-all duration-300 flex items-center justify-center gap-2 hover:-translate-y-0.5"
          >
            <svg class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M4 4v5h.582m15.356 2A8.001 8.001 0 004.582 9m0 0H9m11 11v-5h-.581m0 0a8.003 8.003 0 01-15.357-2m15.357 2H15"/>
            </svg>
            Reintentar Pago
          </button>
          <button
            @click="$router.push('/cliente/mis-citas')"
            class="w-full border-2 border-dark-200 text-dark-600 py-3.5 rounded-xl font-semibold hover:bg-dark-50 transition-all duration-300"
          >
            Ver Mis Citas
          </button>
        </div>

        <!-- Help Text -->
        <div class="mt-6 p-4 bg-amber-50 border border-amber-200 rounded-xl">
          <p class="text-sm text-amber-700">
            Si el problema persiste, contacta con nosotros al <span class="font-semibold">965 123 456</span>
          </p>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'

const route = useRoute()
const router = useRouter()

const citaId = computed(() => route.query.citaId)
const errorMessage = computed(() => route.query.error || 'Error desconocido al procesar el pago')

const reintentar = () => {
  if (citaId.value) {
    router.push(`/cliente/mis-citas?reintentar=${citaId.value}`)
  } else {
    router.push('/cliente/reservar-cita')
  }
}
</script>
