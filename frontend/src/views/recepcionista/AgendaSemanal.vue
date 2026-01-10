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
        <h2>Agenda Semanal</h2>

        <div class="week-navigator">
          <button @click="semanaAnterior" class="btn btn-secondary">
            ← Semana Anterior
          </button>

          <span class="week-range">
            {{ rangoSemana }}
          </span>

          <button @click="semanaSiguiente" class="btn btn-secondary">
            Semana Siguiente →
          </button>
        </div>
      </div>

      <div v-if="loading" class="loading">
        <p>Cargando agenda...</p>
      </div>

      <div v-else-if="error" class="error">
        {{ error }}
        <button @click="cargarAgenda" class="btn btn-secondary">Reintentar</button>
      </div>

      <div v-else class="agenda-container">
        <!-- Calendario semanal -->
        <div class="calendar-grid">
          <!-- Header con días -->
          <div class="calendar-header calendar-time">Hora</div>
          <div
              v-for="dia in diasSemana"
              :key="dia.fecha"
              class="calendar-header"
              :class="{ 'hoy': esHoy(dia.fecha) }"
          >
            <div class="dia-nombre">{{ dia.nombre }}</div>
            <div class="dia-fecha">{{ dia.dia }}</div>
          </div>

          <!-- Filas por hora -->
          <template v-for="hora in horas" :key="hora">
            <div class="calendar-time">{{ hora }}</div>

            <div
                v-for="dia in diasSemana"
                :key="`${dia.fecha}-${hora}`"
                class="calendar-cell"
            >
              <div
                  v-for="cita in getCitasEnHora(dia.fecha, hora)"
                  :key="cita.id"
                  class="cita-mini"
                  :class="`estado-${cita.estado.toLowerCase()}`"
                  @click="verDetalle(cita.id)"
              >
                <div class="cita-mini-hora">{{ formatHoraCita(cita.fechaHora) }}</div>
                <div class="cita-mini-cliente">{{ cita.clienteNombre }}</div>
                <div class="cita-mini-servicio">{{ cita.servicioNombre }}</div>
              </div>
            </div>
          </template>
        </div>

        <!-- Leyenda -->
        <div class="leyenda">
          <h3>Leyenda:</h3>
          <div class="leyenda-items">
            <div class="leyenda-item">
              <span class="leyenda-badge estado-pendiente"></span>
              Pendiente
            </div>
            <div class="leyenda-item">
              <span class="leyenda-badge estado-confirmada"></span>
              Confirmada
            </div>
            <div class="leyenda-item">
              <span class="leyenda-badge estado-completada"></span>
              Completada
            </div>
            <div class="leyenda-item">
              <span class="leyenda-badge estado-cancelada"></span>
              Cancelada
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

const fechaInicioSemana = ref(getInicioSemana(new Date()))

const horas = [
  '09:00', '10:00', '11:00', '12:00', '13:00', '14:00',
  '15:00', '16:00', '17:00', '18:00', '19:00', '20:00'
]

const loading = computed(() => store.loading)
const error = computed(() => store.error)

const diasSemana = computed(() => {
  const dias = []
  const nombresDias = ['Lunes', 'Martes', 'Miércoles', 'Jueves', 'Viernes', 'Sábado', 'Domingo']

  for (let i = 0; i < 7; i++) {
    const fecha = new Date(fechaInicioSemana.value)
    fecha.setDate(fecha.getDate() + i)

    dias.push({
      fecha: fecha.toISOString().split('T')[0],
      nombre: nombresDias[i],
      dia: fecha.getDate()
    })
  }

  return dias
})

const rangoSemana = computed(() => {
  const inicio = new Date(fechaInicioSemana.value)
  const fin = new Date(fechaInicioSemana.value)
  fin.setDate(fin.getDate() + 6)

  return `${inicio.toLocaleDateString('es-ES', { day: 'numeric', month: 'short' })} - ${fin.toLocaleDateString('es-ES', { day: 'numeric', month: 'short', year: 'numeric' })}`
})

function getInicioSemana(fecha) {
  const d = new Date(fecha)
  const dia = d.getDay()
  const diff = d.getDate() - dia + (dia === 0 ? -6 : 1) // Lunes
  const lunes = new Date(d.setDate(diff))
  lunes.setHours(0, 0, 0, 0)
  return lunes
}

function esHoy(fecha) {
  const hoy = new Date().toISOString().split('T')[0]
  return fecha === hoy
}

function getCitasEnHora(fecha, hora) {
  return store.citasSemana.filter(cita => {
    const citaFecha = new Date(cita.fechaHora)
    const citaFechaStr = citaFecha.toISOString().split('T')[0]
    const citaHora = citaFecha.getHours()
    const horaNum = parseInt(hora.split(':')[0])

    return citaFechaStr === fecha && citaHora === horaNum
  })
}

function formatHoraCita(fechaHora) {
  const fecha = new Date(fechaHora)
  return fecha.toLocaleTimeString('es-ES', {
    hour: '2-digit',
    minute: '2-digit'
  })
}

function semanaAnterior() {
  const nuevaFecha = new Date(fechaInicioSemana.value)
  nuevaFecha.setDate(nuevaFecha.getDate() - 7)
  fechaInicioSemana.value = nuevaFecha
  cargarAgenda()
}

function semanaSiguiente() {
  const nuevaFecha = new Date(fechaInicioSemana.value)
  nuevaFecha.setDate(nuevaFecha.getDate() + 7)
  fechaInicioSemana.value = nuevaFecha
  cargarAgenda()
}

async function cargarAgenda() {
  const fechaStr = fechaInicioSemana.value.toISOString().split('T')[0]
  await store.fetchCitasSemana(fechaStr)
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
  cargarAgenda()
})
</script>

<style scoped>
.page-header {
  margin-bottom: 30px;
}

.week-navigator {
  display: flex;
  justify-content: center;
  align-items: center;
  gap: 20px;
  margin-top: 20px;
}

.week-range {
  font-size: 16px;
  font-weight: 600;
  color: #333;
  min-width: 200px;
  text-align: center;
}

.agenda-container {
  background: white;
  border-radius: 8px;
  padding: 20px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

/* Calendario */
.calendar-grid {
  display: grid;
  grid-template-columns: 80px repeat(7, 1fr);
  gap: 1px;
  background-color: #ddd;
  border: 1px solid #ddd;
  margin-bottom: 20px;
  overflow-x: auto;
}

.calendar-header {
  background-color: #f8f9fa;
  padding: 10px;
  font-weight: 600;
  text-align: center;
  min-height: 60px;
  display: flex;
  flex-direction: column;
  justify-content: center;
}

.calendar-header.hoy {
  background-color: #0066CC;
  color: white;
}

.dia-nombre {
  font-size: 14px;
}

.dia-fecha {
  font-size: 20px;
  margin-top: 5px;
}

.calendar-time {
  background-color: #f8f9fa;
  padding: 10px;
  font-weight: 500;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 13px;
}

.calendar-cell {
  background-color: white;
  padding: 5px;
  min-height: 80px;
  position: relative;
}

/* Citas en el calendario */
.cita-mini {
  background: #e3f2fd;
  border-left: 3px solid #0066CC;
  padding: 5px;
  margin-bottom: 5px;
  border-radius: 3px;
  cursor: pointer;
  transition: transform 0.2s;
  font-size: 11px;
}

.cita-mini:hover {
  transform: scale(1.05);
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.2);
}

.cita-mini-hora {
  font-weight: 600;
  margin-bottom: 2px;
}

.cita-mini-cliente {
  font-weight: 500;
  margin-bottom: 2px;
}

.cita-mini-servicio {
  color: #666;
  font-size: 10px;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

/* Estados */
.estado-pendiente {
  background: #FFF3CD;
  border-left-color: #FFC107;
}

.estado-confirmada {
  background: #D1ECF1;
  border-left-color: #17A2B8;
}

.estado-completada {
  background: #D4EDDA;
  border-left-color: #28A745;
}

.estado-cancelada {
  background: #F8D7DA;
  border-left-color: #DC3545;
  opacity: 0.6;
}

/* Leyenda */
.leyenda {
  padding: 15px;
  background: #f8f9fa;
  border-radius: 5px;
}

.leyenda h3 {
  margin-bottom: 10px;
  font-size: 14px;
  color: #333;
}

.leyenda-items {
  display: flex;
  gap: 20px;
  flex-wrap: wrap;
}

.leyenda-item {
  display: flex;
  align-items: center;
  gap: 8px;
  font-size: 13px;
}

.leyenda-badge {
  width: 30px;
  height: 15px;
  border-radius: 3px;
  border-left: 3px solid;
}

@media (max-width: 768px) {
  .calendar-grid {
    grid-template-columns: 60px repeat(7, 80px);
    font-size: 10px;
  }

  .week-navigator {
    flex-direction: column;
  }

  .cita-mini {
    font-size: 9px;
  }
}
</style>