<template>
  <div class="p-8 text-center">
    <div v-if="loading">
      <h2 class="text-xl">Verificando pago...</h2>
    </div>

    <div v-else-if="exito" class="text-green-600">
      <h1 class="text-3xl font-bold">¡Pago Confirmado! ✅</h1>
      <p>Tu cita ha sido pagada correctamente.</p>
      <button @click="$router.push('/cliente/mis-citas')" class="btn-primary mt-4">Volver a mis citas</button>
    </div>

    <div v-else class="text-red-600">
      <h1 class="text-3xl font-bold">Error en el pago ❌</h1>
      <p>{{ mensajeError }}</p>
    </div>
  </div>
</template>

<script setup>
import { onMounted, ref } from 'vue';
import { useRoute } from 'vue-router';
import axios from 'axios'; // O tu servicio de API configurado

const route = useRoute();
const loading = ref(true);
const exito = ref(false);
const mensajeError = ref('');

onMounted(async () => {
  // 1. Capturamos el token de la URL (viene del Mock del backend)
  const token = route.query.token;

  if (!token) {
    exito.value = false;
    mensajeError.value = "No se recibió token de pago";
    loading.value = false;
    return;
  }

  try {
    // 2. Preguntamos al Backend: "¿Este pago es real?"
    // OJO: Ajusta la URL a tu controlador real
    const response = await axios.get(`http://localhost:8080/api/pagos/callback?transactionId=${token}&status=COMPLETED&citaId=1`);
    // NOTA: En tu Mock, asegúrate de que el callbackUrl incluya el citaId si lo necesitas,
    // o usa el endpoint /api/pagos/estado/{citaId} si prefieres.

    // Simplificación para la demo si usas el endpoint de verificar estado:
    // const response = await axios.get(`/api/pagos/verificar?token=${token}`);

    exito.value = true;
  } catch (error) {
    exito.value = false;
    mensajeError.value = "El sistema no pudo verificar el pago.";
  } finally {
    loading.value = false;
  }
});
</script>