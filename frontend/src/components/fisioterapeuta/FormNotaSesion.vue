<template>
  <div class="bg-white rounded-lg p-6">
    <h3 class="text-xl font-bold text-gray-800 mb-4">
      {{ notaExistente ? 'Editar Nota de Sesión' : 'Crear Nota de Sesión' }}
    </h3>

    <form @submit.prevent="submitNota">
      <!-- Contenido (requerido) -->
      <div class="mb-4">
        <label for="contenido" class="block text-sm font-medium text-gray-700 mb-2">
          Descripción de la sesión <span class="text-red-500">*</span>
        </label>
        <textarea
          id="contenido"
          v-model="form.contenido"
          required
          rows="5"
          maxlength="5000"
          placeholder="Descripción detallada de la sesión de fisioterapia..."
          class="w-full border border-gray-300 rounded-md p-3 focus:ring-2 focus:ring-blue-500 focus:border-blue-500"
        ></textarea>
        <div class="text-xs text-gray-500 mt-1">
          {{ form.contenido.length }}/5000 caracteres
        </div>
      </div>

      <!-- Diagnóstico (opcional) -->
      <div class="mb-4">
        <label for="diagnostico" class="block text-sm font-medium text-gray-700 mb-2">
          Diagnóstico
        </label>
        <input
          id="diagnostico"
          v-model="form.diagnostico"
          type="text"
          maxlength="500"
          placeholder="Diagnóstico del paciente (opcional)"
          class="w-full border border-gray-300 rounded-md p-3 focus:ring-2 focus:ring-blue-500 focus:border-blue-500"
        />
      </div>

      <!-- Tratamiento aplicado (opcional) -->
      <div class="mb-4">
        <label for="tratamientoAplicado" class="block text-sm font-medium text-gray-700 mb-2">
          Tratamiento aplicado
        </label>
        <textarea
          id="tratamientoAplicado"
          v-model="form.tratamientoAplicado"
          rows="3"
          maxlength="2000"
          placeholder="Descripción del tratamiento aplicado durante la sesión (opcional)"
          class="w-full border border-gray-300 rounded-md p-3 focus:ring-2 focus:ring-blue-500 focus:border-blue-500"
        ></textarea>
        <div class="text-xs text-gray-500 mt-1">
          {{ form.tratamientoAplicado.length }}/2000 caracteres
        </div>
      </div>

      <!-- Recomendaciones (opcional) -->
      <div class="mb-4">
        <label for="recomendaciones" class="block text-sm font-medium text-gray-700 mb-2">
          Recomendaciones para el paciente
        </label>
        <textarea
          id="recomendaciones"
          v-model="form.recomendaciones"
          rows="3"
          maxlength="2000"
          placeholder="Recomendaciones, ejercicios o cuidados para el paciente (opcional)"
          class="w-full border border-gray-300 rounded-md p-3 focus:ring-2 focus:ring-blue-500 focus:border-blue-500"
        ></textarea>
        <div class="text-xs text-gray-500 mt-1">
          {{ form.recomendaciones.length }}/2000 caracteres
        </div>
      </div>

      <!-- Botones -->
      <div class="flex gap-3 justify-end">
        <button
          type="button"
          @click="$emit('cancelar')"
          class="px-4 py-2 border border-gray-300 rounded-md text-gray-700 hover:bg-gray-50"
        >
          Cancelar
        </button>
        <button
          type="submit"
          :disabled="guardando || !form.contenido.trim()"
          class="px-4 py-2 bg-blue-600 text-white rounded-md hover:bg-blue-700 disabled:bg-gray-400"
        >
          {{ guardando ? 'Guardando...' : 'Guardar Nota' }}
        </button>
      </div>
    </form>
  </div>
</template>

<script setup>
import { ref, watch } from 'vue';
import { useFisioterapeutaStore } from '@/stores/fisioterapeuta';

const props = defineProps({
  citaId: {
    type: Number,
    required: true
  },
  notaExistente: {
    type: Object,
    default: null
  }
});

const emit = defineEmits(['nota-guardada', 'cancelar']);

const store = useFisioterapeutaStore();
const guardando = ref(false);

const form = ref({
  contenido: '',
  diagnostico: '',
  tratamientoAplicado: '',
  recomendaciones: ''
});

// Si existe una nota, pre-llenar el formulario
watch(() => props.notaExistente, (nota) => {
  if (nota) {
    form.value.contenido = nota.contenido || '';
    form.value.diagnostico = nota.diagnostico || '';
    form.value.tratamientoAplicado = nota.tratamientoAplicado || '';
    form.value.recomendaciones = nota.recomendaciones || '';
  }
}, { immediate: true });

const submitNota = async () => {
  if (!form.value.contenido.trim()) {
    alert('El contenido de la nota es obligatorio');
    return;
  }

  guardando.value = true;
  try {
    const notaData = {
      contenido: form.value.contenido.trim(),
      diagnostico: form.value.diagnostico.trim() || null,
      tratamientoAplicado: form.value.tratamientoAplicado.trim() || null,
      recomendaciones: form.value.recomendaciones.trim() || null
    };

    const notaGuardada = await store.guardarNota(props.citaId, notaData);
    emit('nota-guardada', notaGuardada);
  } catch (error) {
    alert('Error al guardar la nota. Por favor, intenta de nuevo.');
  } finally {
    guardando.value = false;
  }
};
</script>
