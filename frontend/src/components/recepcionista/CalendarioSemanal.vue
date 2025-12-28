<template>
  <div class="overflow-x-auto">
    <div class="min-w-[800px]">
      <div class="grid grid-cols-8 gap-1 mb-2 text-center font-bold bg-gray-100 p-2 rounded">
        <div class="text-gray-500">Hora</div> <div v-for="dia in diasSemana" :key="dia.fecha" class="p-2 border-b-2" :class="{'border-blue-500 text-blue-600': esHoy(dia.fecha)}">
          {{ dia.nombre }} <br> <span class="text-sm font-normal">{{ dia.fechaStr }}</span>
        </div>
      </div>

      <div class="relative bg-white border border-gray-200 rounded">
        <div v-for="hora in horas" :key="hora" class="grid grid-cols-8 gap-1 border-b h-20">
          <div class="text-xs text-gray-400 p-1 text-right -mt-2">{{ hora }}</div>

          <div v-for="dia in diasSemana" :key="dia.fecha + hora" class="relative border-l border-gray-100 h-full hover:bg-gray-50 group">

            <template v-for="cita in getCitasEnHora(dia.fecha, hora)" :key="cita.id">
              <div
                class="absolute z-10 w-[95%] left-[2.5%] rounded p-1 text-xs shadow cursor-pointer overflow-hidden transition hover:scale-105"
                :class="colorEstado(cita.estado)"
                :style="{ top: '2px', height: '90%' }"
                @click="$emit('ver-detalle', cita)"
              >
                <div class="font-bold truncate">{{ cita.cliente.nombre }} {{ cita.cliente.apellidos }}</div>
                <div class="truncate text-[10px]">{{ cita.servicio.nombre }}</div>
                <div class="absolute top-0 right-1 text-[10px]">{{ cita.horaInicio }}</div>
              </div>
            </template>

          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { computed } from 'vue';

const props = defineProps({
  citas: Array,
  fechaInicio: String // Fecha del lunes de la semana actual
});

const horas = ['09:00', '10:00', '11:00', '12:00', '13:00', '14:00', '15:00', '16:00', '17:00', '18:00', '19:00', '20:00'];

const diasSemana = computed(() => {
  const dias = ['Lunes', 'Martes', 'Miércoles', 'Jueves', 'Viernes', 'Sábado', 'Domingo'];
  let current = new Date(props.fechaInicio);

  return dias.map((nombre, index) => {
    // Clona y suma días
    const dia = new Date(current);
    dia.setDate(current.getDate() + index);
    return {
      nombre,
      fecha: dia.toISOString().split('T')[0],
      fechaStr: dia.toLocaleDateString()
    };
  });
});

const getCitasEnHora = (fecha, horaStr) => {
  if (!props.citas) return [];
  // Filtramos las citas que coincidan con la fecha y la hora de inicio (simple para ejemplo)
  // Nota: horaStr es "09:00", cita.horaInicio viene como "09:00:00"
  return props.citas.filter(c =>
    c.fecha === fecha &&
    c.horaInicio.startsWith(horaStr.split(':')[0]) // Compara solo la hora (09)
  );
};

const esHoy = (fechaStr) => fechaStr === new Date().toISOString().split('T')[0];

const colorEstado = (estado) => {
  switch(estado) {
    case 'PENDIENTE': return 'bg-blue-100 border-l-4 border-blue-500 text-blue-800';
    case 'COMPLETADA': return 'bg-green-100 border-l-4 border-green-500 text-green-800';
    case 'CANCELADA': return 'bg-red-100 border-l-4 border-red-500 text-red-800 opacity-60';
    default: return 'bg-gray-100 text-gray-800';
  }
};
</script>