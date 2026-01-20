<template>
  <div class="min-h-screen bg-gradient-to-br from-dark-50 via-green-50/50 to-primary-50/30 flex items-center justify-center p-6">
    <!-- Background Elements -->
    <div class="absolute inset-0 overflow-hidden pointer-events-none">
      <div class="absolute -top-40 -right-40 w-80 h-80 bg-green-200/40 rounded-full blur-3xl"></div>
      <div class="absolute -bottom-40 -left-40 w-80 h-80 bg-primary-200/30 rounded-full blur-3xl"></div>
    </div>

    <div class="relative w-full max-w-md">
      <div class="bg-white/80 backdrop-blur-xl rounded-3xl shadow-soft-xl p-8 border border-white/50 text-center animate-scale-in">
        <!-- Success Icon -->
        <div class="relative mb-6">
          <div class="w-20 h-20 bg-gradient-to-br from-green-400 to-green-600 rounded-full flex items-center justify-center mx-auto shadow-glow animate-bounce-soft">
            <svg class="w-10 h-10 text-white" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2.5" d="M5 13l4 4L19 7" />
            </svg>
          </div>
          <!-- Confetti effect circles -->
          <div class="absolute top-0 left-1/4 w-3 h-3 bg-accent-400 rounded-full animate-float"></div>
          <div class="absolute top-4 right-1/4 w-2 h-2 bg-primary-400 rounded-full animate-float" style="animation-delay: 0.3s;"></div>
          <div class="absolute bottom-2 left-1/3 w-2 h-2 bg-secondary-400 rounded-full animate-float" style="animation-delay: 0.5s;"></div>
        </div>

        <h1 class="font-display text-3xl font-bold text-dark-800 mb-3">¡Pago Exitoso!</h1>
        <p class="text-dark-500 mb-6">
          Tu pago ha sido procesado correctamente. Hemos enviado un email de confirmación con los detalles de tu cita.
        </p>

        <!-- Cita Reference -->
        <div v-if="citaId" class="bg-gradient-to-br from-green-50 to-primary-50 rounded-2xl p-5 mb-6 border border-green-100">
          <p class="text-sm text-dark-500 mb-1">Referencia de cita</p>
          <p class="font-mono text-2xl font-bold text-primary-600">#{{ citaId }}</p>
        </div>

        <div v-if="saldoAntes !== null" class="bg-dark-50 rounded-xl p-4 mb-6">
          <p class="text-xs text-dark-400 mb-1">Monedero virtual (demo)</p>
          <div class="flex justify-between text-sm">
            <span>Saldo antes</span><span class="font-mono">{{ saldoAntes }} €</span>
          </div>
          <div class="flex justify-between text-sm">
            <span>Saldo después</span><span class="font-mono font-bold">{{ saldoDespues }} €</span>
          </div>
        </div>


        <!-- Transaction Info -->
        <div v-if="transactionId" class="bg-dark-50 rounded-xl p-4 mb-6">
          <p class="text-xs text-dark-400 mb-1">ID de transacción</p>
          <p class="font-mono text-sm text-dark-600">{{ transactionId }}</p>
        </div>

        <div class="space-y-3">
          <button
            @click="$router.push('/cliente/mis-citas')"
            class="w-full bg-gradient-to-r from-primary-500 to-primary-600 text-white py-3.5 rounded-xl font-semibold shadow-soft hover:shadow-glow transition-all duration-300 flex items-center justify-center gap-2 hover:-translate-y-0.5"
          >
            <svg class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M8 7V3m8 4V3m-9 8h10M5 21h14a2 2 0 002-2V7a2 2 0 00-2-2H5a2 2 0 00-2 2v12a2 2 0 002 2z" />
            </svg>
            Ver Mis Citas
          </button>
          <button
            @click="$router.push('/cliente')"
            class="w-full border-2 border-dark-200 text-dark-600 py-3.5 rounded-xl font-semibold hover:bg-dark-50 transition-all duration-300"
          >
            Volver al Panel
          </button>
        </div>

        <!-- Help Text -->
        <p class="text-xs text-dark-400 mt-6">
          ¿Tienes alguna pregunta? Contacta con nosotros al <span class="font-semibold text-dark-600">965 123 456</span>
        </p>
      </div>
    </div>
  </div>
</template>

<script setup>
import { computed, onMounted, ref } from 'vue'
import { useRoute } from 'vue-router'

const route = useRoute()
const citaId = computed(() => route.query.citaId)
const transactionId = computed(() => route.query.transactionId)

// ✅ Monedero virtual (demo)
const saldoAntes = ref(null)
const saldoDespues = ref(null)

onMounted(() => {
  const pendingRaw = sessionStorage.getItem("pendingPayment")
  if (!pendingRaw) return

  const pending = JSON.parse(pendingRaw) // { citaId, amount }

  // saldo inicial si no existe (demo)
  const current = Number(localStorage.getItem("walletBalance") ?? "200")
  saldoAntes.value = current

  const after = Math.max(0, current - Number(pending.amount || 0))
  saldoDespues.value = after
  localStorage.setItem("walletBalance", String(after))

  sessionStorage.removeItem("pendingPayment")
})
</script>

