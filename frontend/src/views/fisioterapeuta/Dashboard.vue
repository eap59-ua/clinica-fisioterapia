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
      <div class="page-header">
        <h2>Dashboard - Mis Citas de Hoy</h2>
        <p class="subtitle">{{ fechaHoy }}</p>
      </div>

      <div v-if="loading" class="loading">
        <p>Cargando citas...</p>
      </div>

      <div v-else-if="error" class="error">
        {{ error }}
        <button @click="cargarCitas" class="btn btn-secondary">Reintentar</button>
      </div>

      <div v-else>
        <!-- Resumen -->
        <div class="resumen">
          <div class="resumen-card">
            <div class="resumen-icon">📊</div>
            <div class="resumen-info">
              <div class="resumen-numero">{{ store.totalCitasHoy }}</div>
              <div class="resumen-label">Total Citas</div>
            </div>
          </div>

          <div class="resumen-card success">
            <div class="resumen-icon">✅</div>
            <div class="resumen-info">
              <div class="resumen-numero">{{ store.citasCompletadasHoy }}</div>
              <div class="resumen-label">Completadas</div>
            </div>
          </div>

          <div class="resumen-card warning">
            <div class="resumen-icon">⏳</div>
            <div class="resumen-info">
              <div class="resumen-numero">{{ store.citasPendientesHoy }}</div>
              <div class="resumen-label">Pendientes</div>
            </div>
          </div>
        </div>

        <!-- Lista de citas -->
        <div v-if="store.citasHoy.length === 0" class="empty-state">
          <p>🎉 No tienes citas programadas para hoy</p>
        </div>

        <div v-else class="citas-list">
          <div
            v-for="cita in citasOrdenadas"
            :key="cita.id"
            class="cita-card"
            :class="{ 'completada': cita.estado === 'COMPLETADA' }"
          >
            <div class="cita-header">
              <div class="cita-hora">
                🕐 {{ formatHora(cita.fechaHora) }}
              </div>
              <span class="badge" :class="`badge-${cita.estado.toLowerCase()}`">
                {{ cita.estado }}
              </span>
            </div>

            <div class="cita-body">
              <h3>{{ cita.clienteNombre }}</h3>
              <p class="cita-servicio">
                <strong>Servicio:</strong> {{ cita.servicioNombre }} ({{ cita.servicioDuracion }} min)
              </p>
              <p class="cita-sala">
                <strong>Sala:</strong> {{ cita.salaNombre }}
              </p>
            </div>

            <div class="cita-actions">
              <button
                @click="verDetalle(cita.id)"
                class="btn btn-primary"
              >
                Ver Detalle
              </button>

              <button
                v-if="cita.estado === 'PENDIENTE' || cita.estado === 'CONFIRMADA'"
                @click="completar(cita.id)"
                class="btn btn-success"
                :disabled="completando === cita.id"
              >
                {{ completando === cita.id ? 'Completando...' : 'Completar' }}
              </button>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { useFisioterapeutaStore } from '@/stores/fisioterapeuta'

const router = useRouter()
const store = useFisioterapeutaStore()

const completando = ref(null)

const fechaHoy = computed(() => {
  const hoy = new Date()
  return hoy.toLocaleDateString('es-ES', {
    weekday: 'long',
    year: 'numeric',
    month: 'long',
    day: 'numeric'
  })
})

const citasOrdenadas = computed(() => {
  return [...store.citasHoy].sort((a, b) => {
    return new Date(a.fechaHora) - new Date(b.fechaHora)
  })
})

const loading = computed(() => store.loading)
const error = computed(() => store.error)

function formatHora(fechaHora) {
  const fecha = new Date(fechaHora)
  return fecha.toLocaleTimeString('es-ES', {
    hour: '2-digit',
    minute: '2-digit'
  })
}

async function cargarCitas() {
  await store.fetchCitasHoy()
}

function verDetalle(citaId) {
  router.push(`/fisioterapeuta/citas/${citaId}`)
}

async function completar(citaId) {
  if (!confirm('¿Marcar esta cita como completada?')) {
    return
  }

  completando.value = citaId
  try {
    await store.completarCita(citaId)
    alert('Cita completada exitosamente')
  } catch (err) {
    alert('Error al completar la cita: ' + (err.message || 'Error desconocido'))
  } finally {
    completando.value = null
  }
}

function logout() {
  localStorage.removeItem('token')
  localStorage.removeItem('user')
  router.push('/login')
}

onMounted(() => {
  cargarCitas()
})
</script>

<style scoped>
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

/* Resumen */
.resumen {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
  gap: 20px;
  margin-bottom: 30px;
}

.resumen-card {
  background: white;
  border-radius: 8px;
  padding: 20px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  display: flex;
  align-items: center;
  gap: 15px;
  border-left: 4px solid #0066CC;
}

.resumen-card.success {
  border-left-color: #28A745;
}

.resumen-card.warning {
  border-left-color: #FFC107;
}

.resumen-icon {
  font-size: 32px;
}

.resumen-numero {
  font-size: 28px;
  font-weight: bold;
  color: #333;
}

.resumen-label {
  color: #666;
  font-size: 13px;
}

/* Citas */
.citas-list {
  display: grid;
  gap: 20px;
}

.cita-card {
  background: white;
  border-radius: 8px;
  padding: 20px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  transition: transform 0.2s;
}

.cita-card:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.15);
}

.cita-card.completada {
  opacity: 0.7;
}

.cita-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 15px;
  padding-bottom: 15px;
  border-bottom: 1px solid #eee;
}

.cita-hora {
  font-size: 18px;
  font-weight: 600;
  color: #0066CC;
}

.cita-body h3 {
  color: #333;
  margin-bottom: 10px;
}

.cita-body p {
  margin: 5px 0;
  color: #666;
  font-size: 14px;
}

.cita-actions {
  display: flex;
  gap: 10px;
  margin-top: 15px;
  padding-top: 15px;
  border-top: 1px solid #eee;
}

.empty-state {
  text-align: center;
  padding: 60px 20px;
  background: white;
  border-radius: 8px;
  color: #666;
  font-size: 18px;
}

@media (max-width: 768px) {
  .cita-actions {
    flex-direction: column;
  }

  .cita-actions .btn {
    width: 100%;
  }
}
</style>
