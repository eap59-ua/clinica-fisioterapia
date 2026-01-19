<template>
  <div class="max-w-4xl mx-auto p-6">
    <div class="mb-6">
      <button
        @click="$router.push('/cliente')"
        class="flex items-center text-gray-600 hover:text-gray-800 mb-4"
      >
        <svg
          class="w-5 h-5 mr-2"
          fill="none"
          stroke="currentColor"
          viewBox="0 0 24 24"
        >
          <path
            stroke-linecap="round"
            stroke-linejoin="round"
            stroke-width="2"
            d="M15 19l-7-7 7-7"
          />
        </svg>
        Volver al Inicio
      </button>
      <h1 class="text-3xl font-bold text-gray-800">Reservar Nueva Cita</h1>
    </div>

    <div class="flex items-center justify-between mb-8 overflow-x-auto pb-4">
      <div
        v-for="(step, index) in steps"
        :key="index"
        class="flex items-center flex-shrink-0"
      >
        <div
          :class="[
            'flex items-center justify-center w-10 h-10 rounded-full font-semibold transition-colors',
            currentStep >= index
              ? 'bg-teal-500 text-white'
              : 'bg-gray-200 text-gray-600',
          ]"
        >
          {{ index + 1 }}
        </div>
        <span class="ml-2 text-sm font-medium text-gray-700 mr-4">{{
          step
        }}</span>
        <div
          v-if="index < steps.length - 1"
          :class="[
            'h-1 w-8 sm:w-16 mr-4',
            currentStep > index ? 'bg-teal-500' : 'bg-gray-200',
          ]"
        ></div>
      </div>
    </div>

    <div class="bg-white rounded-lg shadow-md p-6 min-h-[400px]">
      <div v-if="currentStep === 0">
        <SelectorServicio
          :servicios="servicios"
          :servicio-seleccionado="reserva.servicio"
          @seleccionar="seleccionarServicio"
        />
      </div>

      <div v-if="currentStep === 1">
        <SelectorFisioterapeuta
          :fisioterapeutas="fisioterapeutas"
          :fisioterapeuta-seleccionado="reserva.fisioterapeuta"
          @seleccionar="seleccionarFisioterapeuta"
        />
      </div>

      <div v-if="currentStep === 2">
        <div
          v-if="cargandoDisponibilidad"
          class="flex flex-col items-center justify-center py-12"
        >
          <div
            class="animate-spin rounded-full h-12 w-12 border-b-2 border-teal-500 mb-4"
          ></div>
          <p class="text-gray-500">Buscando huecos disponibles...</p>
        </div>
        <div v-else-if="disponibilidad.length === 0" class="text-center py-12">
          <p class="text-gray-500 italic">
            No hay horarios disponibles para los próximos días.
          </p>
          <button
            @click="cargarDisponibilidad"
            class="mt-4 text-teal-600 underline"
          >
            Reintentar
          </button>
        </div>
        <CalendarioReserva
          v-else
          :disponibilidad="disponibilidad"
          :hora-seleccionada="reserva.horaInicio"
          @seleccionar-dia="seleccionarDia"
          @seleccionar-hora="seleccionarHora"
        />
      </div>

      <div v-if="currentStep === 3">
        <h3 class="text-lg font-semibold text-gray-800 mb-4">
          Resumen de tu Cita
        </h3>
        <div class="space-y-4 bg-gray-50 rounded-lg p-6 border">
          <div class="flex justify-between border-b pb-2">
            <span class="text-gray-600 font-medium">Servicio:</span>
            <span class="font-bold">{{ reserva.servicio?.nombre }}</span>
          </div>
          <div class="flex justify-between border-b pb-2">
            <span class="text-gray-600 font-medium">Profesional:</span>
            <span class="font-bold">{{
              reserva.fisioterapeuta?.nombre || "Cualquier profesional"
            }}</span>
          </div>
          <div class="flex justify-between border-b pb-2">
            <span class="text-gray-600 font-medium">Fecha:</span>
            <span class="font-bold">{{ formatearFecha(reserva.fecha) }}</span>
          </div>
          <div class="flex justify-between border-b pb-2">
            <span class="text-gray-600 font-medium">Hora:</span>
            <span class="font-bold text-teal-600 text-xl"
              >{{ reserva.horaInicio }} h</span
            >
          </div>
          <div class="flex justify-between pt-2 text-lg font-bold">
            <span>Total:</span>
            <span class="text-teal-700">{{ reserva.servicio?.precio }}€</span>
          </div>
        </div>
      </div>
    </div>

    <div class="flex justify-between mt-8">
      <button
        v-if="currentStep > 0"
        @click="currentStep--"
        class="px-6 py-2 border border-gray-300 rounded-lg hover:bg-gray-50"
      >
        Anterior
      </button>
      <div v-else></div>
      <button
        v-if="currentStep < 3"
        @click="siguientePaso"
        :disabled="!puedeAvanzar"
        :class="[
          'px-8 py-2 rounded-lg font-bold transition',
          puedeAvanzar
            ? 'bg-teal-600 text-white'
            : 'bg-gray-200 text-gray-400 cursor-not-allowed',
        ]"
      >
        Siguiente
      </button>
      <button
        v-else
        @click="confirmarReserva"
        :disabled="enviando"
        class="px-8 py-2 bg-teal-600 text-white rounded-lg font-bold hover:bg-teal-700 disabled:opacity-50"
      >
        {{ enviando ? "Procesando..." : "Confirmar Reserva" }}
      </button>
    </div>

    <div
      v-if="mostrarExito"
      class="fixed inset-0 bg-black/50 flex items-center justify-center z-50 p-4"
    >
      <div
        class="bg-white p-8 rounded-xl max-w-sm w-full text-center shadow-2xl"
      >
        <div
          class="w-16 h-16 bg-green-100 rounded-full flex items-center justify-center mx-auto mb-4 text-green-500 text-3xl"
        >
          ✓
        </div>
        <h2 class="text-2xl font-bold mb-2">¡Cita Reservada!</h2>
        <button
          @click="router.push('/cliente')"
          class="w-full bg-teal-600 text-white py-2 rounded-lg font-bold"
        >
          Ir a mis citas
        </button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted, watch } from "vue";
import { useRouter } from "vue-router";
import SelectorServicio from "@/components/cliente/SelectorServicio.vue";
import SelectorFisioterapeuta from "@/components/cliente/SelectorFisioterapeuta.vue";
import CalendarioReserva from "@/components/cliente/CalendarioReserva.vue";
import publicService from "@/services/publicService";
import citaService from "@/services/citaService";

const router = useRouter();
const steps = ["Servicio", "Profesional", "Fecha/Hora", "Confirmar"];
const currentStep = ref(0);

const servicios = ref([]);
const fisioterapeutas = ref([]);
const disponibilidad = ref([]);
const cargandoDisponibilidad = ref(false);
const enviando = ref(false);
const mostrarExito = ref(false);

const reserva = ref({
  servicio: null,
  fisioterapeuta: null,
  fecha: null,
  horaInicio: null,
});

onMounted(async () => {
  try {
    console.log("RESERVA: Cargando datos iniciales...");
    const [sData, fData] = await Promise.all([
      publicService.getServicios(),
      publicService.getFisioterapeutas(),
    ]);
    console.log("RESERVA: Servicios recibidos:", sData);
    console.log("RESERVA: Fisios recibidos:", fData);

    servicios.value = Array.isArray(sData) ? sData.filter((s) => s.activo) : [];
    fisioterapeutas.value = Array.isArray(fData)
      ? fData.filter((f) => f.activo)
      : [];

    console.log("RESERVA: Fisios tras filtrar:", fisioterapeutas.value);
  } catch (error) {
    console.error("RESERVA: Error en onMounted:", error);
  }
});

const puedeAvanzar = computed(() => {
  if (currentStep.value === 0) return !!reserva.value.servicio;
  if (currentStep.value === 1) return true;
  if (currentStep.value === 2)
    return reserva.value.fecha && reserva.value.horaInicio;
  return false;
});

const seleccionarServicio = (s) => {
  reserva.value.servicio = s;
  reserva.value.fecha = null;
  reserva.value.horaInicio = null;
};

const seleccionarFisioterapeuta = (f) => {
  reserva.value.fisioterapeuta = f;
  reserva.value.fecha = null;
  reserva.value.horaInicio = null;
};

const cargarDisponibilidad = async () => {
  // 1. Validar servicio
  if (!reserva.value.servicio?.id) {
    console.warn("RESERVA: No hay servicio seleccionado aún.");
    return;
  }

  // 2. Obtener el ID del fisio (del seleccionado o del primero de la lista)
  // El backend devuelve 'id' (heredado de Usuario), no 'usuarioId'
  let fisioId = reserva.value.fisioterapeuta?.id;

  if (!fisioId && fisioterapeutas.value && fisioterapeutas.value.length > 0) {
    fisioId = fisioterapeutas.value[0].id;
    console.log("RESERVA: Usando fisio por defecto ID:", fisioId);
  }

  if (!fisioId) {
    console.error("RESERVA: No se pudo obtener ID de fisioterapeuta");
    cargandoDisponibilidad.value = false;
    return;
  }

  cargandoDisponibilidad.value = true;
  try {
    const hoy = new Date().toISOString().split("T")[0];
    console.log(
      `RESERVA: Consultando API para fisio ${fisioId} y servicio ${reserva.value.servicio.id}`,
    );

    const data = await citaService.getDisponibilidadConServicio(
      fisioId,
      reserva.value.servicio.id,
      hoy,
      14,
    );
    disponibilidad.value = data || [];
  } catch (error) {
    console.error("RESERVA: Error consultando disponibilidad:", error);
  } finally {
    cargandoDisponibilidad.value = false;
  }
};

watch(currentStep, (n) => {
  if (n === 2) cargarDisponibilidad();
});

const siguientePaso = () => {
  if (puedeAvanzar.value) currentStep.value++;
};
const seleccionarDia = (f) => {
  reserva.value.fecha = f;
  reserva.value.horaInicio = null;
};
const seleccionarHora = (h) => {
  reserva.value.horaInicio = h.substring(0, 5);
};

const confirmarReserva = async () => {
  enviando.value = true;
  try {
    const payload = {
      servicioId: reserva.value.servicio.id,
      fisioterapeutaId:
        reserva.value.fisioterapeuta?.id || fisioterapeutas.value[0].id,
      fecha: reserva.value.fecha,
      horaInicio: reserva.value.horaInicio + ":00",
    };
    await citaService.reservarCita(payload);
    mostrarExito.value = true;
  } catch (e) {
    alert("Error: " + (e.response?.data?.message || e.message));
  } finally {
    enviando.value = false;
  }
};

const formatearFecha = (f) => {
  if (!f) return "";
  return new Date(f + "T00:00:00").toLocaleDateString("es-ES", {
    weekday: "long",
    day: "numeric",
    month: "long",
    year: "numeric",
  });
};
</script>
