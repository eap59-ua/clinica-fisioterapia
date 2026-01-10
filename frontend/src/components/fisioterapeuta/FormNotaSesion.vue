<template>
  <div class="form-nota">
    <h3>📝 Nota de Sesión</h3>

    <div v-if="error" class="error">
      {{ error }}
    </div>

    <div v-if="success" class="success">
      {{ success }}
    </div>

    <form @submit.prevent="handleSubmit">
      <div class="form-group">
        <label for="contenido" class="form-label">
          Descripción de la sesión <span class="required">*</span>
        </label>
        <textarea
          id="contenido"
          v-model="form.contenido"
          class="form-textarea"
          rows="5"
          placeholder="Descripción detallada de la sesión realizada..."
          required
          :maxlength="5000"
        ></textarea>
        <small class="char-count">
          {{ form.contenido.length }} / 5000 caracteres
        </small>
      </div>

      <div class="form-group">
        <label for="diagnostico" class="form-label">
          Diagnóstico
        </label>
        <input
          id="diagnostico"
          v-model="form.diagnostico"
          type="text"
          class="form-input"
          placeholder="Diagnóstico realizado (opcional)"
          :maxlength="500"
        />
      </div>

      <div class="form-group">
        <label for="tratamiento" class="form-label">
          Tratamiento aplicado
        </label>
        <textarea
          id="tratamiento"
          v-model="form.tratamientoAplicado"
          class="form-textarea"
          rows="3"
          placeholder="Descripción del tratamiento aplicado (opcional)"
          :maxlength="2000"
        ></textarea>
      </div>

      <div class="form-group">
        <label for="recomendaciones" class="form-label">
          Recomendaciones
        </label>
        <textarea
          id="recomendaciones"
          v-model="form.recomendaciones"
          class="form-textarea"
          rows="3"
          placeholder="Recomendaciones para el paciente (opcional)"
          :maxlength="2000"
        ></textarea>
      </div>

      <div class="form-actions">
        <button
          type="submit"
          class="btn btn-primary"
          :disabled="guardando || !form.contenido.trim()"
        >
          {{ guardando ? 'Guardando...' : (notaExistente ? 'Actualizar Nota' : 'Guardar Nota') }}
        </button>

        <button
          v-if="!notaExistente"
          type="button"
          class="btn btn-secondary"
          @click="resetForm"
        >
          Limpiar
        </button>
      </div>
    </form>
  </div>
</template>

<script setup>
import { ref, watch } from 'vue'
import { useFisioterapeutaStore } from '@/stores/fisioterapeuta'

const props = defineProps({
  citaId: {
    type: Number,
    required: true
  },
  notaExistente: {
    type: Object,
    default: null
  }
})

const emit = defineEmits(['nota-guardada'])

const store = useFisioterapeutaStore()

const form = ref({
  contenido: '',
  diagnostico: '',
  tratamientoAplicado: '',
  recomendaciones: ''
})

const guardando = ref(false)
const error = ref(null)
const success = ref(null)

// Si hay nota existente, pre-llenar el formulario
watch(() => props.notaExistente, (nota) => {
  if (nota) {
    form.value = {
      contenido: nota.contenido || '',
      diagnostico: nota.diagnostico || '',
      tratamientoAplicado: nota.tratamientoAplicado || '',
      recomendaciones: nota.recomendaciones || ''
    }
  }
}, { immediate: true })

async function handleSubmit() {
  error.value = null
  success.value = null

  if (!form.value.contenido.trim()) {
    error.value = 'El contenido de la sesión es obligatorio'
    return
  }

  guardando.value = true

  try {
    await store.guardarNota(props.citaId, form.value)
    success.value = 'Nota guardada exitosamente'
    emit('nota-guardada')

    // Limpiar mensaje de éxito después de 3 segundos
    setTimeout(() => {
      success.value = null
    }, 3000)
  } catch (err) {
    error.value = err.message || 'Error al guardar la nota'
  } finally {
    guardando.value = false
  }
}

function resetForm() {
  if (confirm('¿Estás seguro de que quieres limpiar el formulario?')) {
    form.value = {
      contenido: '',
      diagnostico: '',
      tratamientoAplicado: '',
      recomendaciones: ''
    }
  }
}
</script>

<style scoped>
.form-nota {
  background: #f8f9fa;
  border-radius: 8px;
  padding: 20px;
  margin-top: 20px;
}

.form-nota h3 {
  margin-bottom: 20px;
  color: #333;
}

.required {
  color: #DC3545;
}

.char-count {
  display: block;
  margin-top: 5px;
  color: #666;
  font-size: 12px;
  text-align: right;
}

.form-actions {
  display: flex;
  gap: 10px;
  margin-top: 20px;
}

.form-actions .btn {
  flex: 1;
}

@media (max-width: 768px) {
  .form-actions {
    flex-direction: column;
  }
}
</style>
