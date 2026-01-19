<template>
  <div class="max-w-3xl mx-auto p-8 bg-white shadow-lg rounded-lg mt-10">
    <div class="border-b pb-4 mb-6 flex justify-between items-center">
      <div>
        <h2 class="text-2xl font-bold text-gray-800">Editar Cita #{{ route.params.id }}</h2>
        <p class="text-gray-500 text-sm">Modifique los detalles de la reserva.</p>
      </div>
      <span class="px-3 py-1 bg-yellow-100 text-yellow-800 rounded-full text-xs font-bold">
        Edición
      </span>
    </div>

    <div v-if="cargando" class="text-center py-10">
      <p class="text-blue-600">Cargando datos de la cita...</p>
    </div>

    <form v-else @submit.prevent="actualizarCita" class="space-y-6">

      <div class="bg-gray-50 p-4 rounded border border-gray-200">
        <ClienteSearch
          :clienteInicial="datosOriginales.cliente"
          @seleccionar-cliente="(c) => form.cliente.id = c?.id"
        />
        <p class="text-xs text-gray-500 mt-1 ml-1">
          Nota: Si cambia el paciente, asegúrese de que sea correcto.
        </p>
      </div>

      <div class="grid grid-cols-1 md:grid-cols-2 gap-6">
        <div>
          <label class="block text-sm font-semibold text-gray-700 mb-1">Fisioterapeuta</label>
          <select v-model="form.fisioterapeuta.id" class="w-full border p-2 rounded" required>
            <option v-for="f in listas.fisioterapeutas" :key="f.id" :value="f.id">
              {{ f.nombre }} {{ f.apellidos }}
            </option>
          </select>
        </div>

        <div>
          <label class="block text-sm font-semibold text-gray-700 mb-1">Sala</label>
          <select v-model="form.sala.id" class="w-full border p-2 rounded" required>
            <option v-for="sala in listas.salas" :key="sala.id" :value="sala.id">
              {{ sala.nombre }}
            </option>
          </select>
        </div>
      </div>

      <div class="grid grid-cols-1 md:grid-cols-2 gap-6">
        <div>
          <label class="block text-sm font-semibold text-gray-700 mb-1">Servicio</label>
          <select
            v-model="form.servicio.id"
            @change="alCambiarServicio"
            class="w-full border p-2 rounded"
            required
          >
            <option v-for="s in listas.servicios" :key="s.id" :value="s.id">
              {{ s.nombre }} ({{ s.duracionMinutos }} min)
            </option>
          </select>
        </div>

        <div>
          <label class="block text-sm font-semibold text-gray-700 mb-1">Precio (€)</label>
          <input
            v-model="form.precioPagado"
            type="number" step="0.01"
            class="w-full border p-2 rounded bg-gray-50"
          />
        </div>
      </div>

      <div class="grid grid-cols-2 gap-6 bg-blue-50 p-4 rounded border border-blue-100">
        <div>
          <label class="block text-sm font-semibold text-gray-700 mb-1">Fecha</label>
          <input v-model="form.fecha" type="date" class="w-full border p-2 rounded" required />
        </div>
        <div>
          <label class="block text-sm font-semibold text-gray-700 mb-1">Hora Inicio</label>
          <input v-model="form.horaInicio" type="time" class="w-full border p-2 rounded" required />
        </div>
      </div>

      <div>
        <label class="block text-sm font-semibold text-gray-700 mb-1">Notas</label>
        <textarea v-model="form.notas" rows="3" class="w-full border p-2 rounded"></textarea>
      </div>

      <div class="flex gap-4 pt-4 border-t">
        <button
          type="button"
          @click="router.back()"
          class="w-1/3 bg-gray-200 text-gray-700 py-3 rounded hover:bg-gray-300 font-bold"
        >
          Cancelar
        </button>
        <button
          type="submit"
          class="w-2/3 bg-blue-600 text-white py-3 rounded hover:bg-blue-700 font-bold shadow-md"
        >
          Guardar Cambios
        </button>
      </div>
    </form>
  </div>
</template>

<script setup>
import { ref, onMounted, reactive } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import recepcionistaService from '@/services/recepcionistaService';
import ClienteSearch from '@/components/recepcionista/ClienteSearch.vue'; // Importamos el buscador

const route = useRoute();
const router = useRouter();
const cargando = ref(true);

const listas = reactive({ fisioterapeutas: [], servicios: [], salas: [] });
const datosOriginales = ref(null); // Para pasar al buscador

const form = ref({
  cliente: { id: null },
  fisioterapeuta: { id: null },
  servicio: { id: null },
  sala: { id: null },
  fecha: '',
  horaInicio: '',
  precioPagado: 0,
  notas: ''
});

onMounted(async () => {
  const idCita = route.params.id;
  try {
    // 1. Cargar listas auxiliares
    const [fisios, servs, salas] = await Promise.all([
      recepcionistaService.getFisioterapeutas(),
      recepcionistaService.getServicios(),
      recepcionistaService.getSalas()
    ]);
    listas.fisioterapeutas = fisios.data;
    listas.servicios = servs.data;
    listas.salas = salas.data;

    // 2. Cargar la cita a editar
    const res = await recepcionistaService.getCitaPorId(idCita);
    const cita = res.data;
    datosOriginales.value = cita; // Guardamos todo el objeto para el buscador

    // 3. Rellenar formulario (mapeo)
    form.value = {
      cliente: { id: cita.cliente.id },
      fisioterapeuta: { id: cita.fisioterapeuta.id },
      servicio: { id: cita.servicio.id },
      sala: { id: cita.sala?.id }, // ?. por si venía null
      fecha: cita.fecha,
      horaInicio: cita.horaInicio ? cita.horaInicio.substring(0, 5) : '', // Cortar segundos HH:mm:ss -> HH:mm
      precioPagado: cita.precioPagado,
      notas: cita.notas
    };

  } catch (error) {
    console.error(error);
    alert("Error cargando la cita. Verifique que existe.");
    router.push('/recepcionista/dashboard');
  } finally {
    cargando.value = false;
  }
});

const alCambiarServicio = () => {
  const s = listas.servicios.find(x => x.id === form.value.servicio.id);
  if (s) {
    // Sugerir actualizar precio (opcional, aquí forzamos actualización si cambia servicio)
    form.value.precioPagado = s.precio;
  }
};

const actualizarCita = async () => {
  try {
    const payload = {
      ...form.value,
      horaInicio: form.value.horaInicio + ":00" // Añadir segundos para el LocalTime de Java
    };

    await recepcionistaService.actualizarCita(route.params.id, payload);
    alert('Cita modificada correctamente');
    router.push('/recepcionista/dashboard');
  } catch (error) {
    console.error(error);
    alert('Error al actualizar');
  }
};
</script>