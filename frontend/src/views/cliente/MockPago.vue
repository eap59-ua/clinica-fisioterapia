<template>
  <div class="min-h-screen bg-gradient-to-br from-dark-100 via-primary-50/30 to-accent-50/20 flex items-center justify-center p-6">
    <!-- Background Elements -->
    <div class="absolute inset-0 overflow-hidden pointer-events-none">
      <div class="absolute -top-40 -right-40 w-80 h-80 bg-primary-200/40 rounded-full blur-3xl"></div>
      <div class="absolute -bottom-40 -left-40 w-80 h-80 bg-accent-200/30 rounded-full blur-3xl"></div>
    </div>

    <div class="relative w-full max-w-md">
      <!-- Mock Banner -->
      <div class="bg-amber-500 text-white text-center py-2 px-4 rounded-t-2xl font-semibold text-sm">
        MODO SIMULACION - TPV MOCK
      </div>

      <div class="bg-white/90 backdrop-blur-xl rounded-b-3xl shadow-soft-xl p-8 border border-white/50">
        <!-- TPV Logo Mock -->
        <div class="text-center mb-6">
          <div class="w-16 h-16 bg-gradient-to-br from-primary-500 to-primary-700 rounded-2xl flex items-center justify-center mx-auto mb-3 shadow-lg">
            <svg class="w-8 h-8 text-white" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M3 10h18M7 15h1m4 0h1m-7 4h12a3 3 0 003-3V8a3 3 0 00-3-3H6a3 3 0 00-3 3v8a3 3 0 003 3z" />
            </svg>
          </div>
          <h1 class="font-display text-2xl font-bold text-dark-800">Pasarela de Pago</h1>
          <p class="text-dark-500 text-sm">Clinica Fisioterapia</p>
        </div>

        <!-- Payment Details -->
        <div class="bg-gradient-to-br from-dark-50 to-primary-50/50 rounded-2xl p-5 mb-6 border border-dark-100">
          <div class="flex justify-between items-center mb-3">
            <span class="text-dark-500 text-sm">Referencia</span>
            <span class="font-mono text-dark-700 font-semibold">#{{ orderId }}</span>
          </div>
          <div class="flex justify-between items-center mb-3">
            <span class="text-dark-500 text-sm">Transaccion</span>
            <span class="font-mono text-xs text-dark-600">{{ transactionId }}</span>
          </div>
          <div class="border-t border-dark-200 pt-3 mt-3">
            <div class="flex justify-between items-center">
              <span class="text-dark-700 font-semibold">Total a pagar</span>
              <span class="text-2xl font-bold text-primary-600">{{ formattedAmount }}</span>
            </div>
          </div>
        </div>

        <!-- Mock Card Form -->
        <div class="space-y-4 mb-6">
          <div>
            <label class="block text-sm font-medium text-dark-600 mb-1">Numero de tarjeta</label>
            <input
              type="text"
              v-model="cardNumber"
              placeholder="4242 4242 4242 4242"
              class="w-full px-4 py-3 border border-dark-200 rounded-xl focus:ring-2 focus:ring-primary-500 focus:border-transparent transition-all"
              maxlength="19"
            />
          </div>
          <div class="grid grid-cols-2 gap-4">
            <div>
              <label class="block text-sm font-medium text-dark-600 mb-1">Caducidad</label>
              <input
                type="text"
                v-model="expiry"
                placeholder="MM/AA"
                class="w-full px-4 py-3 border border-dark-200 rounded-xl focus:ring-2 focus:ring-primary-500 focus:border-transparent transition-all"
                maxlength="5"
              />
            </div>
            <div>
              <label class="block text-sm font-medium text-dark-600 mb-1">CVV</label>
              <input
                type="text"
                v-model="cvv"
                placeholder="123"
                class="w-full px-4 py-3 border border-dark-200 rounded-xl focus:ring-2 focus:ring-primary-500 focus:border-transparent transition-all"
                maxlength="3"
              />
            </div>
          </div>
        </div>

        <!-- Action Buttons -->
        <div class="space-y-3">
          <button
            @click="confirmarPago"
            :disabled="processing"
            class="w-full bg-gradient-to-r from-green-500 to-green-600 text-white py-4 rounded-xl font-semibold shadow-soft hover:shadow-glow transition-all duration-300 flex items-center justify-center gap-2 hover:-translate-y-0.5 disabled:opacity-50 disabled:cursor-not-allowed"
          >
            <svg v-if="!processing" class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M5 13l4 4L19 7" />
            </svg>
            <svg v-else class="w-5 h-5 animate-spin" fill="none" viewBox="0 0 24 24">
              <circle class="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" stroke-width="4"></circle>
              <path class="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4zm2 5.291A7.962 7.962 0 014 12H0c0 3.042 1.135 5.824 3 7.938l3-2.647z"></path>
            </svg>
            {{ processing ? 'Procesando...' : 'Pagar ' + formattedAmount }}
          </button>

          <button
            @click="cancelarPago"
            :disabled="processing"
            class="w-full border-2 border-red-200 text-red-600 py-3.5 rounded-xl font-semibold hover:bg-red-50 transition-all duration-300 disabled:opacity-50"
          >
            Cancelar Pago
          </button>
        </div>

        <!-- Security Badge -->
        <div class="flex items-center justify-center gap-2 mt-6 text-dark-400 text-xs">
          <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 15v2m-6 4h12a2 2 0 002-2v-6a2 2 0 00-2-2H6a2 2 0 00-2 2v6a2 2 0 002 2zm10-10V7a4 4 0 00-8 0v4h8z" />
          </svg>
          <span>Pago seguro simulado (entorno de desarrollo)</span>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import pagoService from '@/services/pagoService'

const route = useRoute()
const router = useRouter()

// Query params from mock TPV URL
const transactionId = computed(() => route.query.txn || 'N/A')
const amount = computed(() => parseFloat(route.query.amount) || 0)
const orderId = computed(() => route.query.orderId || 'N/A')

// Format amount as currency
const formattedAmount = computed(() => {
  return new Intl.NumberFormat('es-ES', {
    style: 'currency',
    currency: 'EUR'
  }).format(amount.value)
})

// Mock form fields (not actually validated)
const cardNumber = ref('4242 4242 4242 4242')
const expiry = ref('12/28')
const cvv = ref('123')
const processing = ref(false)

async function confirmarPago() {
  processing.value = true

  try {
    // Call the mock confirm endpoint
    await pagoService.mockConfirmarPago(orderId.value)

    // Redirect to success page
    router.push({
      path: '/cliente/pago-exitoso',
      query: {
        citaId: orderId.value,
        transactionId: transactionId.value
      }
    })
  } catch (error) {
    console.error('Error al confirmar pago:', error)
    router.push({
      path: '/cliente/pago-error',
      query: {
        citaId: orderId.value,
        error: 'Error al procesar el pago'
      }
    })
  } finally {
    processing.value = false
  }
}

function cancelarPago() {
  router.push({
    path: '/cliente/pago-error',
    query: {
      citaId: orderId.value,
      error: 'Pago cancelado por el usuario'
    }
  })
}
</script>
