<template>
  <div class="min-h-screen bg-gray-50 py-12">
    <div class="max-w-4xl mx-auto px-4 sm:px-6 lg:px-8">
      <h1 class="text-4xl font-bold text-center mb-12">Contacto</h1>

      <div class="grid grid-cols-1 md:grid-cols-2 gap-8">
        <!-- Información de contacto -->
        <div class="bg-white rounded-lg shadow-md p-8">
          <h2 class="text-2xl font-bold mb-6">Información de Contacto</h2>

          <div class="space-y-4">
            <div class="flex items-start">
              <span class="text-2xl mr-4">📍</span>
              <div>
                <h3 class="font-semibold">Dirección</h3>
                <p class="text-gray-600">
                  Calle Mayor, 123<br />03001 Alicante, España
                </p>
              </div>
            </div>

            <div class="flex items-start">
              <span class="text-2xl mr-4">📞</span>
              <div>
                <h3 class="font-semibold">Teléfono</h3>
                <p class="text-gray-600">+34 965 123 456</p>
              </div>
            </div>

            <div class="flex items-start">
              <span class="text-2xl mr-4">📧</span>
              <div>
                <h3 class="font-semibold">Email</h3>
                <p class="text-gray-600">info@clinicafisio.com</p>
              </div>
            </div>

            <div class="flex items-start">
              <span class="text-2xl mr-4">🕐</span>
              <div>
                <h3 class="font-semibold">Horario</h3>
                <p class="text-gray-600">
                  Lunes - Viernes: 09:00 - 20:00<br />
                  Sábado: 09:00 - 14:00
                </p>
              </div>
            </div>
          </div>
        </div>

        <!-- Formulario de contacto -->
        <div class="bg-white rounded-lg shadow-md p-8">
          <h2 class="text-2xl font-bold mb-6">Envíanos un Mensaje</h2>

          <form @submit.prevent="handleSubmit">
            <div class="mb-4">
              <label class="block text-gray-700 mb-2">Nombre</label>
              <input
                v-model="formData.nombre"
                type="text"
                required
                class="w-full px-4 py-2 border rounded-lg focus:outline-none focus:ring-2 focus:ring-primary"
              />
            </div>

            <div class="mb-4">
              <label class="block text-gray-700 mb-2">Email</label>
              <input
                v-model="formData.email"
                type="email"
                required
                class="w-full px-4 py-2 border rounded-lg focus:outline-none focus:ring-2 focus:ring-primary"
              />
            </div>

            <div class="mb-4">
              <label class="block text-gray-700 mb-2">Mensaje</label>
              <textarea
                v-model="formData.mensaje"
                required
                rows="4"
                class="w-full px-4 py-2 border rounded-lg focus:outline-none focus:ring-2 focus:ring-primary"
              ></textarea>
            </div>

            <button
              type="submit"
              class="w-full bg-primary text-white py-3 rounded-lg hover:bg-blue-600 transition"
            >
              Enviar Mensaje
            </button>
          </form>

          <div
            v-if="mensajeEnviado"
            class="mt-4 bg-green-100 border border-green-400 text-green-700 px-4 py-3 rounded"
          >
            Mensaje enviado correctamente. Te contactaremos pronto.
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from "vue";

const formData = ref({
  nombre: "",
  email: "",
  mensaje: "",
});

const mensajeEnviado = ref(false);

const handleSubmit = () => {
  // Aquí iría la lógica para enviar el email
  console.log("Mensaje enviado:", formData.value);
  mensajeEnviado.value = true;

  // Limpiar formulario
  formData.value = {
    nombre: "",
    email: "",
    mensaje: "",
  };

  setTimeout(() => {
    mensajeEnviado.value = false;
  }, 3000);
};
</script>
