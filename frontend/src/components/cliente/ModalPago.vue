<template>
  <div
      v-if="open"
      class="fixed inset-0 bg-black/50 flex items-center justify-center z-50 p-4"
      @click.self="emit('close')"
  >
    <div class="bg-white rounded-2xl w-full max-w-md shadow-2xl overflow-hidden">
      <div class="px-6 py-4 border-b flex items-center justify-between">
        <div>
          <h2 class="text-xl font-bold text-gray-800">Pago de la cita</h2>
          <p class="text-sm text-gray-500" v-if="citaId">Cita #{{ citaId }}</p>
        </div>

        <button
            class="text-gray-400 hover:text-gray-600 transition"
            @click="emit('close')"
            aria-label="Cerrar"
        >
          <svg class="w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12" />
          </svg>
        </button>
      </div>

      <div class="p-6 space-y-4">
        <div class="bg-gray-50 border border-gray-200 rounded-xl p-4">
          <p class="text-sm text-gray-600">Concepto</p>
          <p class="font-semibold text-gray-800">{{ concepto }}</p>

          <div class="mt-3 flex items-center justify-between">
            <p class="text-sm text-gray-600">Importe</p>
            <p class="text-2xl font-bold text-teal-700">{{ importeFormateado }}</p>
          </div>

          <div v-if="token" class="mt-3">
            <p class="text-xs text-gray-500">Token de transacción</p>
            <p class="font-mono text-xs text-gray-700 break-all">{{ token }}</p>
          </div>
        </div>

        <!-- Nota: no mostramos “saldo restante” para no inventar datos -->
        <div class="bg-teal-50 border border-teal-200 rounded-xl p-4">
          <p class="text-sm text-teal-800 font-semibold mb-1">Pasarela de pago externa (TPV)</p>
          <p class="text-sm text-teal-700">
            Al continuar, se abrirá el TPV para completar el pago. Si lo prefieres, puedes pagar más tarde desde tus citas.
          </p>
        </div>
      </div>

      <div class="px-6 py-4 border-t flex gap-3">
        <button
            class="flex-1 bg-teal-600 text-white py-2.5 rounded-lg font-semibold hover:bg-teal-700 transition"
            :disabled="!paymentUrl"
            @click="abrirTpv"
        >
          Ir al TPV
        </button>
        <button
            class="flex-1 border-2 border-gray-300 text-gray-700 py-2.5 rounded-lg font-semibold hover:bg-gray-50 transition"
            @click="emit('later')"
        >
          Pagar más tarde
        </button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { computed } from "vue";

const props = defineProps({
  open: { type: Boolean, default: false },
  citaId: { type: [Number, String], default: null },
  concepto: { type: String, default: "Cita en Clínica de Fisioterapia" },
  amount: { type: [Number, String], default: 0 },
  paymentUrl: { type: String, default: "" },
  token: { type: String, default: "" },
});

const emit = defineEmits(["close", "later", "opened"]);

const importeFormateado = computed(() => {
  const n = typeof props.amount === "string" ? Number(props.amount) : props.amount;
  if (!Number.isFinite(n)) return `${props.amount}€`;
  return `${n.toFixed(2)}€`;
});




const abrirTpv = () => {
  if (!props.paymentUrl) return;

  // ✅ Guardamos lo mínimo para “monedero simulado” / página de éxito
  sessionStorage.setItem("pendingPayment", JSON.stringify({
    citaId: props.citaId,
    amount: Number(props.amount)
  }));

  // “Ventanita” (popup). Si el navegador lo bloquea, redirigimos en la misma pestaña.
  const popup = window.open(
      props.paymentUrl,
      "tpv_pago",
      "width=480,height=720,menubar=no,toolbar=no,location=no,status=no,scrollbars=yes"
  );

  if (!popup) {
    window.location.href = props.paymentUrl;
  }

  emit("opened");
};
</script>