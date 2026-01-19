<template>
  <div class="overflow-x-auto pb-4">
    <div class="min-w-[800px]">
      <div class="grid grid-cols-8 gap-0 mb-2 text-center font-bold bg-gray-50 border rounded-t-lg">
        <div class="p-4 text-gray-400 border-r border-b">Hora</div>
        <div
          v-for="dia in diasSemana"
          :key="dia.fecha"
          class="p-2 border-b border-r last:border-r-0 transition-colors"
          :class="{'bg-blue-50 text-blue-700 border-b-blue-300': esHoy(dia.fecha)}"
        >
          <div class="text-xs uppercase tracking-wider">{{ dia.nombre }}</div>
          <div class="text-xl">{{ dia.diaNum }}</div>
        </div>
      </div>

      <div class="relative bg-white border border-gray-200 rounded-b-lg shadow-sm">
        <div v-for="hora in horas" :key="hora" class="grid grid-cols-8 h-24 border-b last:border-b-0">

          <div class="text-xs text-gray-400 p-2 text-right border-r relative">
            <span class="-top-3 relative bg-white px-1">{{ hora }}</span>
          </div>

          <div
            v-for="dia in diasSemana"
            :key="dia.fecha + hora"
            class="relative border-r last:border-r-0 border-gray-100 h-full group transition-colors"
            :class="getEstiloCelda(dia, hora)"
          >
            <div v-if="esBloqueado(dia.fecha, hora)" class="absolute inset-0 flex items-center justify-center opacity-10 pointer-events-none">
              <span class="text-xs font-bold -rotate-45 text-red-800 uppercase tracking-widest">Cerrado</span>
            </div>

            <template v-for="cita in getCitasEnHora(dia.fecha, hora)" :key="cita.id">
              <div
                class="absolute z-10 w-[92%] left-[4%] rounded p-1.5 text-xs shadow-sm cursor-pointer border-l-4 overflow-hidden transition hover:scale-105 hover:shadow-md hover:z-20"
                :class="colorEstado(cita.estado)"
                :style="estiloPosicionCita(cita)"
                @click.stop="$emit('ver-detalle', cita)"
              >
                <div class="font-bold truncate text-gray-800">
                  {{ cita.cliente?.nombre }} {{ cita.cliente?.apellidos }}
                </div>
                <div class="truncate text-[10px] opacity-80 mt-0.5">
                  {{ cita.servicio?.nombre }}
                </div>
                <div class="absolute top-1 right-1 text-[9px] font-mono opacity-70">
                  {{ cita.horaInicio.substring(0,5) }}
                </div>
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
  citas: { type: Array, default: () => [] },
  horarios: { type: Array, default: () => [] }, // Configuración Semanal
  bloqueos: { type: Array, default: () => [] }, // Festivos
  fechaInicio: String
});

const horas = ['09:00', '10:00', '11:00', '12:00', '13:00', '14:00', '15:00', '16:00', '17:00', '18:00', '19:00', '20:00'];

const diasSemana = computed(() => {
  const dias = ['Lunes', 'Martes', 'Miércoles', 'Jueves', 'Viernes', 'Sábado', 'Domingo'];
  let current = new Date(props.fechaInicio);

  return dias.map((nombre, index) => {
    const dia = new Date(current);
    dia.setDate(current.getDate() + index);
    return {
      nombre,
      diaNum: dia.getDate(),
      diaSemanaIndice: index + 1, // 1=Lunes, 7=Domingo (Para comparar con DB)
      fecha: dia.toISOString().split('T')[0],
      fechaStr: dia.toLocaleDateString()
    };
  });
});

// --- LÓGICA VISUAL DE COLORES (FR-REC-14) ---

// Determina si una celda está abierta, cerrada (por horario) o bloqueada (festivo)
const getEstiloCelda = (dia, horaStr) => {
  // 1. Verificar Bloqueos (Festivos Globales)
  if (esBloqueado(dia.fecha, horaStr)) {
    return 'bg-red-50 hover:bg-red-100'; // Color rojo suave para festivos
  }

  // 2. Verificar Horario de Apertura/Cierre
  // Buscamos la configuración de ese día (ej: Lunes = 1)
  const configDia = props.horarios.find(h => h.diaSemana === dia.diaSemanaIndice);

  if (!configDia) return 'bg-gray-100'; // Si no hay config, asumimos cerrado

  // Comparar horas (String "09:00" vs String "21:00")
  // Formato horaStr es "09:00". Backend suele devolver "09:00:00".
  const horaActual = horaStr + ":00";
  const apertura = configDia.horaApertura.length === 5 ? configDia.horaApertura + ":00" : configDia.horaApertura;
  const cierre = configDia.horaCierre.length === 5 ? configDia.horaCierre + ":00" : configDia.horaCierre;

  if (horaActual < apertura || horaActual >= cierre) {
    return 'bg-gray-200'; // Color gris oscuro para "Fuera de horario"
  }

  return 'bg-white hover:bg-blue-50'; // Abierto
};

const esBloqueado = (fechaStr, horaStr) => {
  const fechaHoraCelda = new Date(`${fechaStr}T${horaStr}`);

  return props.bloqueos.some(b => {
    // Solo bloqueos globales (Clínica cerrada)
    if (b.tipo !== 'GLOBAL') return false;

    const inicio = new Date(b.fechaInicio);
    const fin = new Date(b.fechaFin);

    // Comprobar si la celda cae dentro del bloqueo
    return fechaHoraCelda >= inicio && fechaHoraCelda < fin;
  });
};

// --- LÓGICA DE CITAS ---

const getCitasEnHora = (fecha, horaStr) => {
  if (!props.citas) return [];
  return props.citas.filter(c =>
    c.fecha === fecha &&
    c.horaInicio.startsWith(horaStr.split(':')[0])
  );
};

// Cálculo visual para que la cita ocupe su duración real (opcional)
const estiloPosicionCita = (cita) => {
  // Aquí podrías calcular height basado en duración.
  // Por defecto ocupamos el 90% de la celda.
  return { top: '2px', height: '90%' };
};

const esHoy = (fechaStr) => fechaStr === new Date().toISOString().split('T')[0];

const colorEstado = (estado) => {
  switch(estado) {
    case 'PENDIENTE': return 'bg-blue-100 border-blue-500 text-blue-800';
    case 'COMPLETADA': return 'bg-green-100 border-green-500 text-green-800';
    case 'CANCELADA': return 'bg-red-100 border-red-500 text-red-800 opacity-60 decoration-line-through';
    default: return 'bg-gray-100 border-gray-400 text-gray-800';
  }
};
</script>