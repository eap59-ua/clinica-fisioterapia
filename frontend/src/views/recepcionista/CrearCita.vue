<template>
  <div class="max-w-3xl mx-auto p-8 bg-white shadow-lg rounded-lg mt-10">
    <div class="border-b pb-4 mb-6">
      <h2 class="text-2xl font-bold text-gray-800">Nueva Cita</h2>
      <p class="text-gray-500 text-sm">Complete los datos para agendar una nueva sesión.</p>
    </div>

    <div v-if="cargando" class="text-center py-4 text-blue-600">
      Cargando listas de datos...
    </div>

    <form v-else @submit.prevent="guardarCita" class="space-y-6">

      <div class="grid grid-cols-1 md:grid-cols-2 gap-6">

        <div>
          <label class="block text-sm font-semibold text-gray-700 mb-1">Paciente</label>
          <select
            v-model="form.cliente.id"
            class="w-full border border-gray-300 p-2 rounded focus:ring-2 focus:ring-blue-500 focus:outline-none"
            required
          >
            <option value="" disabled>Seleccione un paciente</option>
            <option v-for="c in listas.clientes" :key="c.id" :value="c.id">
              {{ c.nombre }} {{ c.apellidos }} ({{ c.dni || 'Sin DNI' }})
            </option>
          </select>
        </div>

        <div>
          <label class="block text-sm font-semibold text-gray-700 mb-1">Fisioterapeuta</label>
          <select
            v-model="form.fisioterapeuta.id"
            class="w-full border border-gray-300 p-2 rounded focus:ring-2 focus:ring-blue-500 focus:outline-none"
            required
          >
            <option value="" disabled>Seleccione un fisioterapeuta</option>
            <option v-for="f in listas.fisioterapeutas" :key="f.id" :value="f.id">
              {{ f.nombre }} {{ f.apellidos }} - {{ f.especialidades }}
            </option>
          </select>
        </div>
      </div>

      <div class="grid grid-cols-1 md:grid-cols-3 gap-6">

        <div class="md:col-span-1">
          <label class="block text-sm font-semibold text-gray-700 mb-1">Servicio</label>
          <select
            v-model="form.servicio.id"
            @change="alCambiarServicio"
            class="w-full border border-gray-300 p-2 rounded focus:ring-2 focus:ring-blue-500 focus:outline-none"
            required
          >
            <option value="" disabled>Seleccione servicio</option>
            <option v-for="s in listas.servicios" :key="s.id" :value="s.id">
              {{ s.nombre }} ({{ s.duracionMinutos }} min)
            </option>
          </select>
        </div>

        <div>
          <label class="block text-sm font-semibold text-gray-700 mb-1">Sala / Cabina</label>
          <select
            v-model="form.sala.id"
            class="w-full border border-gray-300 p-2 rounded focus:ring-2 focus:ring-blue-500 focus:outline-none"
            required
          >
            <option value="" disabled>Seleccione sala</option>
            <option v-for="sala in listas.salas" :key="sala.id" :value="sala.id">
              {{ sala.nombre }}
            </option>
          </select>
        </div>

        <div>
          <label class="block text-sm font-semibold text-gray-700 mb-1">Precio a Cobrar (€)</label>
          <input
            v-model="form.precioPagado"
            type="number"
            step="0.01"
            min="0"
            class="w-full border border-gray-300 p-2 rounded focus:ring-2 focus:ring-blue-500 focus:outline-none"
            placeholder="0.00"
          />
        </div>
      </div>

      <div class="grid grid-cols-2 gap-6 bg-gray-50 p-4 rounded border border-gray-200">
        <div>
          <label class="block text-sm font-semibold text-gray-700 mb-1">Fecha</label>
          <input
            v-model="form.fecha"
            type="date"
            class="w-full border border-gray-300 p-2 rounded focus:ring-2 focus:ring-blue-500"
            required
          />
        </div>
        <div>
          <label class="block text-sm font-semibold text-gray-700 mb-1">Hora Inicio</label>
          <input
            v-model="form.horaInicio"
            type="time"
            class="w-full border border-gray-300 p-2 rounded focus:ring-2 focus:ring-blue-500"
            required
          />
          <p class="text-xs text-gray-500 mt-1" v-if="duracionEstimada">
            Fin estimado: {{ calcularHoraFin() }}
          </p>
        </div>
      </div>

      <div>
        <label class="block text-sm font-semibold text-gray-700 mb-1">Notas Adicionales</label>
        <textarea
          v-model="form.notas"
          rows="3"
          class="w-full border border-gray-300 p-2 rounded focus:ring-2 focus:ring-blue-500 focus:outline-none"
          placeholder="Alergias, preferencias, dolor específico..."
        ></textarea>
      </div>

      <div class="flex gap-4 pt-4">
        <button
          type="button"
          @click="router.back()"
          class="w-1/3 bg-gray-200 text-gray-700 py-3 rounded hover:bg-gray-300 font-bold transition"
        >
          Cancelar
        </button>
        <button
          type="submit"
          class="w-2/3 bg-blue-600 text-white py-3 rounded hover:bg-blue-700 font-bold transition shadow-md"
        >
          Confirmar Reserva
        </button>
      </div>

    </form>
  </div>
</template>

<script setup>
import { ref, onMounted, reactive } from 'vue';
import { useRouter } from 'vue-router';
import recepcionistaService from '@/services/recepcionistaService';

const router = useRouter();
const cargando = ref(true);
const duracionEstimada = ref(0);

// Listas para los desplegables
const listas = reactive({
  clientes: [],
  fisioterapeutas: [],
  servicios: [],
  salas: []
});

// Estructura del formulario igual a tu DTO/Entidad
const form = ref({
  cliente: { id: '' },
  fisioterapeuta: { id: '' },
  servicio: { id: '' },
  sala: { id: '' },
  precioPagado: 0,
  fecha: new Date().toISOString().split('T')[0], // Hoy por defecto
  horaInicio: '09:00',
  notas: ''
});

onMounted(async () => {
  try {
    const [clientesRes, fisiosRes, serviciosRes, salasRes] = await Promise.all([
      recepcionistaService.getClientes(),
      recepcionistaService.getFisioterapeutas(),
      recepcionistaService.getServicios(),
      recepcionistaService.getSalas()
    ]);

    listas.clientes = clientesRes.data;
    listas.fisioterapeutas = fisiosRes.data;
    listas.servicios = serviciosRes.data;
    listas.salas = salasRes.data;
  } catch (error) {
    console.error("Error cargando listas:", error);
    alert("No se pudieron cargar los datos necesarios (clientes/fisios/servicios). Verifique la conexión.");
  } finally {
    cargando.value = false;
  }
});

const alCambiarServicio = () => {
  const servicioId = form.value.servicio.id;
  const servicioEncontrado = listas.servicios.find(s => s.id === servicioId);

  if (servicioEncontrado) {
    form.value.precioPagado = servicioEncontrado.precio;
    duracionEstimada.value = servicioEncontrado.duracionMinutos;
  }
};

const calcularHoraFin = () => {
  if (!form.value.horaInicio || !duracionEstimada.value) return '';

  const [horas, minutos] = form.value.horaInicio.split(':').map(Number);
  const fecha = new Date();
  fecha.setHours(horas);
  fecha.setMinutes(minutos + duracionEstimada.value);

  return fecha.toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' });
};

const guardarCita = async () => {
  const payload = {
    cliente: { id: form.value.cliente.id },
    fisioterapeuta: { id: form.value.fisioterapeuta.id },
    servicio: { id: form.value.servicio.id },
    sala: { id: form.value.sala.id },
    fecha: form.value.fecha,
    horaInicio: form.value.horaInicio + ":00",
    precioPagado: form.value.precioPagado,
    notas: form.value.notas,
    estado: 'PENDIENTE'
  };

  try {
    await recepcionistaService.crearCita(payload);
    // SweetAlert o alert nativo
    alert('¡Cita creada con éxito!');
    router.push('/recepcionista/dashboard');
  } catch (error) {
    console.error(error);
    const mensaje = error.response?.data || "Error desconocido al guardar";
    alert('Error: ' + mensaje);
  }
};
</script>