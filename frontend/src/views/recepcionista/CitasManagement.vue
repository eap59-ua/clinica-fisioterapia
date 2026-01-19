<template>
  <div class="p-6">
    <div class="flex justify-between items-center mb-6">
      <h1 class="text-2xl font-bold text-gray-800">📅 Calendario Semanal</h1>

      <div class="flex gap-4 items-center">
        <div class="flex items-center bg-white rounded shadow overflow-hidden border">
          <button @click="cambiarSemana(-7)" class="px-3 py-2 hover:bg-gray-100 border-r">
            &lt;
          </button>
          <span class="px-4 font-semibold text-sm">{{ rangoFechaStr }}</span>
          <button @click="cambiarSemana(7)" class="px-3 py-2 hover:bg-gray-100 border-l">
            &gt;
          </button>
        </div>

        <router-link to="/recepcionista/crear-cita" class="ml-4 bg-indigo-600 text-white px-4 py-2 rounded shadow hover:bg-indigo-700 transition font-bold text-sm">
          + Agendar
        </router-link>
      </div>
    </div>

    <CalendarioSemanal
      :citas="citas"
      :horarios="horarios"
      :bloqueos="bloqueos"
      :fecha-inicio="fechaInicioSemana"
      @ver-detalle="verDetalleCita"
    />
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue';
import recepcionistaService from '@/services/recepcionistaService';
import horarioService from '@/services/horarioService'; // IMPORTANTE: Importar esto
import CalendarioSemanal from '@/components/recepcionista/CalendarioSemanal.vue';

const citas = ref([]);
const horarios = ref([]);
const bloqueos = ref([]);
const fechaInicioSemana = ref('');

// Calcular Lunes
const getLunes = (d) => {
  d = new Date(d);
  const day = d.getDay(), diff = d.getDate() - day + (day == 0 ? -6:1);
  return new Date(d.setDate(diff));
}

// Inicializar fecha
fechaInicioSemana.value = getLunes(new Date()).toISOString().split('T')[0];

// Texto visual del rango (ej: "20 Ene - 26 Ene")
const rangoFechaStr = computed(() => {
  if(!fechaInicioSemana.value) return '';
  const inicio = new Date(fechaInicioSemana.value);
  const fin = new Date(inicio);
  fin.setDate(inicio.getDate() + 6);
  return `${inicio.toLocaleDateString()} - ${fin.toLocaleDateString()}`;
});

const cargarDatos = async () => {
  try {
    // 1. Cargar Citas
    const resCitas = await recepcionistaService.getCitasSemana(fechaInicioSemana.value);
    citas.value = resCitas.data;

    // 2. Cargar Configuración de Horarios (Apertura/Cierre)
    const resHorarios = await horarioService.obtenerHorarioSemanal();
    horarios.value = resHorarios.data;

    // 3. Cargar Bloqueos (Festivos)
    // Para simplificar, traemos todos. Lo ideal sería filtrar por fecha en backend.
    const resBloqueos = await horarioService.obtenerCalendarioGlobal();
    bloqueos.value = resBloqueos.data;

  } catch (error) {
    console.error("Error cargando datos del calendario:", error);
  }
};

const cambiarSemana = (dias) => {
  const fecha = new Date(fechaInicioSemana.value);
  fecha.setDate(fecha.getDate() + dias);
  fechaInicioSemana.value = fecha.toISOString().split('T')[0];
  cargarDatos();
};

const verDetalleCita = (cita) => {
  // Aquí podrías redirigir a la edición
  alert(`Cita: ${cita.cliente.nombre} - ${cita.horaInicio}`);
};

onMounted(() => {
  cargarDatos();
});
</script>