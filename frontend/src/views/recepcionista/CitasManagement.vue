<template>
  <div class="p-6">
    <div class="flex justify-between items-center mb-6">
      <h1 class="text-2xl font-bold">Calendario Semanal</h1>

      <div class="flex gap-4 items-center">
        <button @click="cambiarSemana(-7)" class="px-3 py-1 bg-gray-200 rounded">&lt; Anterior</button>
        <span class="font-semibold">{{ fechaInicioSemana }}</span>
        <button @click="cambiarSemana(7)" class="px-3 py-1 bg-gray-200 rounded">Siguiente &gt;</button>

        <router-link to="/recepcionista/crear-cita" class="ml-4 bg-indigo-600 text-white px-4 py-2 rounded shadow">
          + Agendar
        </router-link>
      </div>
    </div>

    <CalendarioSemanal
      :citas="citas"
      :fecha-inicio="fechaInicioSemana"
      @ver-detalle="verDetalleCita"
    />
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import recepcionistaService from '@/services/recepcionistaService';
import CalendarioSemanal from '@/components/recepcionista/CalendarioSemanal.vue';

const citas = ref([]);
const fechaActual = ref(new Date());

// Calcular el lunes de la semana actual
const getLunes = (d) => {
  d = new Date(d);
  var day = d.getDay(),
      diff = d.getDate() - day + (day == 0 ? -6:1);
  return new Date(d.setDate(diff));
}

const fechaInicioSemana = ref(getLunes(new Date()).toISOString().split('T')[0]);

const cargarSemana = async () => {
  try {
    const res = await recepcionistaService.getCitasSemana(fechaInicioSemana.value);
    citas.value = res.data;
  } catch (error) {
    console.error("Error cargando semana:", error);
  }
};

const cambiarSemana = (dias) => {
  const fecha = new Date(fechaInicioSemana.value);
  fecha.setDate(fecha.getDate() + dias);
  fechaInicioSemana.value = fecha.toISOString().split('T')[0];
  cargarSemana();
};

const verDetalleCita = (cita) => {
  alert(`Cita de ${cita.cliente.nombre}\nServicio: ${cita.servicio.nombre}\nEstado: ${cita.estado}`);
  // Aquí podrías abrir un modal para editar
};

onMounted(() => {
  cargarSemana();
});
</script>