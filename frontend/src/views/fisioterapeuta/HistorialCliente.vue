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
        <a href="#" @click.prevent="$router.back()">← Volver</a>
      </div>

      <div v-if="loading" class="loading">
        <p>Cargando historial...</p>
      </div>

      <div v-else-if="error" class="error">
        {{ error }}
        <button @click="cargarHistorial" class="btn btn-secondary">Reintentar</button>
      </div>

      <div v-else>
        <div class="page-header">
          <h2>📋 Historial del Cliente</h2>
          <p class="subtitle" v-if="historial.length > 0">
            {{ historial[0].clienteNombre }}
          </p>
        </div>

        <div v-if="historial.length === 0" class="empty-state">
          <p>No hay historial de citas para este cliente</p>
        </div>

        <div v-else class="historial-list">
          <div
            v-for="entrada in historial"
            :key="entrada.citaId"
            class="historial-card"
          >
            <div class="historial-header">
              <div class="historial-fecha">
                📅 {{ formatFecha(entrada.fechaHora) }}
                <span class="historial-hora">{{ formatHora(entrada.fechaHora) }}</span>
              </div>
              <span class="badge" :class="`badge-${entrada.estado.toLowerCase()}`">
                {{ entrada.estado }}
              </span>
            </div>

            <div class="historial-body">
              <div class="historial-servicio">
                <strong>{{ entrada.servicioNombre }}</strong>
                ({{ entrada.servicioDuracion }} min)
              </div>

              <div class="historial-sala">
                Sala: {{ entrada.salaNombre }}
              </div>

              <div class="historial-nota">
                <div class="nota-header">
                  <strong>Nota de sesión:</strong>
                  <span v-if="entrada.tieneNota" class="nota-badge tiene-nota">
                    ✅ Con nota
                  </span>
                  <span v-else class="nota-badge sin-nota">
                    ❌ Sin nota
                  </span>
                </div>

                <div v-if="entrada.tieneNota && entrada.notaResumen" class="nota-resumen">
                  {{ entrada.notaResumen }}
                </div>
              </div>
            </div>

            <div class="historial-actions">
              <button
                @click="verDetalle(entrada.citaId)"
                class="btn btn-primary"
              >
                Ver detalle completo
              </button>
            </div>
          </div>
        </div>

        <!-- Resumen estadístico -->
        <div v-if="historial.length > 0" class="resumen-estadistico">
          <h3>Resumen</h3>
          <div class="stats-grid">
            <div class="stat-item">
              <div class="stat-numero">{{ historial.length }}</div>
              <div class="stat-label">Total Sesiones</div>
            </div>
            <div class="stat-item">
              <div class="stat-numero">{{ sesionesCompletadas }}</div>
              <div class="stat-label">Completadas</div>
            </div>
            <div class="stat-item">
              <div class="stat-numero">{{ sesionesConNota }}</div>
              <div class="stat-label">Con Nota</div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { useFisioterapeutaStore } from '@/stores/fisioterapeuta'

const router = useRouter()
const route = useRoute()
const store = useFisioterapeutaStore()

const clienteId = computed(() => route.params.id)
const historial = computed(() => store.historialCliente)
const loading = computed(() => store.loading)
const error = computed(() => store.error)

const sesionesCompletadas = computed(() => {
  return historial.value.filter(h => h.estado === 'COMPLETADA').length
})

const sesionesConNota = computed(() => {
  return historial.value.filter(h => h.tieneNota).length
})

function formatFecha(fechaHora) {
  const fecha = new Date(fechaHora)
  return fecha.toLocaleDateString('es-ES', {
    day: '2-digit',
    month: '2-digit',
    year: 'numeric'
  })
}

function formatHora(fechaHora) {
  const fecha = new Date(fechaHora)
  return fecha.toLocaleTimeString('es-ES', {
    hour: '2-digit',
    minute: '2-digit'
  })
}

async function cargarHistorial() {
  await store.fetchHistorialCliente(clienteId.value)
}

function verDetalle(citaId) {
  router.push(`/fisioterapeuta/citas/${citaId}`)
}

function logout() {
  localStorage.removeItem('token')
  localStorage.removeItem('user')
  router.push('/login')
}

onMounted(() => {
  cargarHistorial()
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
  font-size: 16px;
  font-weight: 500;
}

.historial-list {
  display: grid;
  gap: 20px;
  margin-bottom: 30px;
}

.historial-card {
  background: white;
  border-radius: 8px;
  padding: 20px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.historial-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 15px;
  padding-bottom: 15px;
  border-bottom: 1px solid #eee;
}

.historial-fecha {
  font-size: 16px;
  font-weight: 600;
  color: #333;
}

.historial-hora {
  margin-left: 10px;
  color: #0066CC;
  font-weight: 500;
}

.historial-body {
  margin-bottom: 15px;
}

.historial-servicio {
  font-size: 16px;
  margin-bottom: 8px;
  color: #333;
}

.historial-sala {
  font-size: 14px;
  color: #666;
  margin-bottom: 15px;
}

.historial-nota {
  background: #f8f9fa;
  padding: 15px;
  border-radius: 5px;
  margin-top: 15px;
}

.nota-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 10px;
}

.nota-badge {
  font-size: 12px;
  padding: 4px 8px;
  border-radius: 4px;
  font-weight: 500;
}

.nota-badge.tiene-nota {
  background: #D4EDDA;
  color: #155724;
}

.nota-badge.sin-nota {
  background: #F8D7DA;
  color: #721C24;
}

.nota-resumen {
  font-size: 14px;
  color: #666;
  font-style: italic;
  line-height: 1.5;
}

.historial-actions {
  padding-top: 15px;
  border-top: 1px solid #eee;
}

/* Resumen estadístico */
.resumen-estadistico {
  background: white;
  border-radius: 8px;
  padding: 20px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.resumen-estadistico h3 {
  margin-bottom: 15px;
  color: #333;
}

.stats-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(150px, 1fr));
  gap: 20px;
}

.stat-item {
  text-align: center;
  padding: 15px;
  background: #f8f9fa;
  border-radius: 8px;
}

.stat-numero {
  font-size: 32px;
  font-weight: bold;
  color: #0066CC;
  margin-bottom: 5px;
}

.stat-label {
  font-size: 14px;
  color: #666;
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
  .historial-header {
    flex-direction: column;
    align-items: flex-start;
    gap: 10px;
  }

  .nota-header {
    flex-direction: column;
    align-items: flex-start;
    gap: 8px;
  }
}
</style>
