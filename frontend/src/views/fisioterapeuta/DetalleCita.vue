<template>
  <div>
    <nav class="nav">
      <div class="nav-container">
        <h1>🏥 Clínica Fisioterapia</h1>
        <ul class="nav-links">
          <li><router-link to="/fisioterapeuta/dashboard" class="nav-link">Dashboard</router-link></li>
          <li><router-link to="/fisioterapeuta/agenda" class="nav-link">Agenda Semanal</router-link></li>
          <li><a href="#" @click.prevent="logout" class="nav-link">Cerrar Sesión</a></li>
        </ul>
      </div>
    </nav>

    <div class="container">
      <div class="breadcrumb">
        <router-link to="/fisioterapeuta/dashboard">← Volver al Dashboard</router-link>
      </div>

      <div v-if="loading" class="loading">
        <p>Cargando detalle de la cita...</p>
      </div>

      <div v-else-if="error" class="error">
        {{ error }}
        <button @click="cargarCita" class="btn btn-secondary">Reintentar</button>
      </div>

      <div v-else-if="cita">
        <div class="page-header">
          <h2>Detalle de Cita</h2>
          <p class="subtitle">{{ formatFecha(cita.fechaHora) }}</p>
        </div>

        <div class="detalle-container">
          <!-- Información del cliente -->
          <div class="card">
            <div class="card-header">
              <h3>👤 Información del Cliente</h3>
            </div>
            <div class="card-body">
              <div class="info-row">
                <span class="info-label">Nombre:</span>
                <span class="info-value">{{ cita.clienteNombre }}</span>
              </div>
              <div class="info-row" v-if="cita.clienteTelefono">
                <span class="info-label">Teléfono:</span>
                <span class="info-value">{{ cita.clienteTelefono }}</span>
              </div>
              <div class="info-row" v-if="cita.clienteEmail">
                <span class="info-label">Email:</span>
                <span class="info-value">{{ cita.clienteEmail }}</span>
              </div>
              <div class="card-actions">
                <button
                  @click="verHistorial"
                  class="btn btn-secondary"
                >
                  📋 Ver historial completo
                </button>
              </div>
            </div>
          </div>

          <!-- Información del servicio -->
          <div class="card">
            <div class="card-header">
              <h3>💼 Información del Servicio</h3>
            </div>
            <div class="card-body">
              <div class="info-row">
                <span class="info-label">Servicio:</span>
                <span class="info-value">{{ cita.servicioNombre }}</span>
              </div>
              <div class="info-row">
                <span class="info-label">Duración:</span>
                <span class="info-value">{{ cita.servicioDuracion }} minutos</span>
              </div>
              <div class="info-row" v-if="cita.servicioPrecio">
                <span class="info-label">Precio:</span>
                <span class="info-value">{{ formatPrecio(cita.servicioPrecio) }}</span>
              </div>
            </div>
          </div>

          <!-- Información de la cita -->
          <div class="card">
            <div class="card-header">
              <h3>🏥 Información de la Cita</h3>
            </div>
            <div class="card-body">
              <div class="info-row">
                <span class="info-label">Sala:</span>
                <span class="info-value">{{ cita.salaNombre }}</span>
              </div>
              <div class="info-row">
                <span class="info-label">Horario:</span>
                <span class="info-value">
                  {{ formatHora(cita.fechaHora) }} - {{ formatHoraFin(cita.fechaHora, cita.servicioDuracion) }}
                </span>
              </div>
              <div class="info-row">
                <span class="info-label">Estado:</span>
                <span class="badge" :class="`badge-${cita.estado.toLowerCase()}`">
                  {{ cita.estado }}
                </span>
              </div>

              <div class="card-actions" v-if="cita.estado === 'PENDIENTE' || cita.estado === 'CONFIRMADA'">
                <button
                  @click="completarCita"
                  class="btn btn-success"
                  :disabled="completando"
                >
                  {{ completando ? 'Completando...' : '✅ Marcar como completada' }}
                </button>
              </div>
            </div>
          </div>
        </div>

        <!-- Formulario de nota de sesión -->
        <FormNotaSesion
          :cita-id="parseInt(citaId)"
          :nota-existente="nota"
          @nota-guardada="onNotaGuardada"
        />
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { useFisioterapeutaStore } from '@/stores/fisioterapeuta'
import FormNotaSesion from '@/components/fisioterapeuta/FormNotaSesion.vue'

const router = useRouter()
const route = useRoute()
const store = useFisioterapeutaStore()

const citaId = computed(() => route.params.id)
const cita = computed(() => store.citaActual)
const loading = computed(() => store.loading)
const error = computed(() => store.error)

const nota = ref(null)
const completando = ref(false)

function formatFecha(fechaHora) {
  const fecha = new Date(fechaHora)
  return fecha.toLocaleDateString('es-ES', {
    weekday: 'long',
    year: 'numeric',
    month: 'long',
    day: 'numeric'
  })
}

function formatHora(fechaHora) {
  const fecha = new Date(fechaHora)
  return fecha.toLocaleTimeString('es-ES', {
    hour: '2-digit',
    minute: '2-digit'
  })
}

function formatHoraFin(fechaHora, duracion) {
  const fecha = new Date(fechaHora)
  fecha.setMinutes(fecha.getMinutes() + duracion)
  return fecha.toLocaleTimeString('es-ES', {
    hour: '2-digit',
    minute: '2-digit'
  })
}

function formatPrecio(precio) {
  return `${precio.toFixed(2)}€`
}

async function cargarCita() {
  try {
    await store.fetchDetalleCita(citaId.value)
    await cargarNota()
  } catch (err) {
    console.error('Error al cargar cita:', err)
  }
}

async function cargarNota() {
  try {
    nota.value = await store.fetchNotaCita(citaId.value)
  } catch (err) {
    // Es normal que no exista nota
    nota.value = null
  }
}

async function completarCita() {
  if (!confirm('¿Marcar esta cita como completada?')) {
    return
  }

  completando.value = true
  try {
    await store.completarCita(citaId.value)
    alert('Cita completada exitosamente')
  } catch (err) {
    alert('Error al completar la cita: ' + (err.message || 'Error desconocido'))
  } finally {
    completando.value = false
  }
}

async function onNotaGuardada() {
  // Recargar la nota después de guardar
  await cargarNota()
}

function verHistorial() {
  if (cita.value?.clienteId) {
    router.push(`/fisioterapeuta/clientes/${cita.value.clienteId}/historial`)
  }
}

function logout() {
  localStorage.removeItem('token')
  localStorage.removeItem('user')
  router.push('/login')
}

onMounted(() => {
  cargarCita()
})
</script>

<style scoped>
.breadcrumb {
  margin-bottom: 20px;
}

.breadcrumb a {
  color: #0066CC;
  text-decoration: none;
  font-weight: 500;
}

.breadcrumb a:hover {
  text-decoration: underline;
}

.page-header {
  margin-bottom: 30px;
}

.page-header h2 {
  color: #333;
  margin-bottom: 5px;
}

.subtitle {
  color: #666;
  font-size: 14px;
  text-transform: capitalize;
}

.detalle-container {
  display: grid;
  gap: 20px;
  margin-bottom: 20px;
}

.card-header {
  border-bottom: 1px solid #eee;
  padding-bottom: 10px;
  margin-bottom: 15px;
}

.card-header h3 {
  color: #333;
  font-size: 18px;
  margin: 0;
}

.card-body {
  padding: 5px 0;
}

.info-row {
  display: flex;
  justify-content: space-between;
  padding: 10px 0;
  border-bottom: 1px solid #f5f5f5;
}

.info-row:last-child {
  border-bottom: none;
}

.info-label {
  font-weight: 600;
  color: #666;
}

.info-value {
  color: #333;
  text-align: right;
}

.card-actions {
  margin-top: 20px;
  padding-top: 15px;
  border-top: 1px solid #eee;
}

@media (min-width: 768px) {
  .detalle-container {
    grid-template-columns: repeat(2, 1fr);
  }

  .card:last-child {
    grid-column: 1 / -1;
  }
}
</style>
