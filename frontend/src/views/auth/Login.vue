<template>
  <div class="login-container">
    <div class="login-card">
      <h1>🏥 Clínica Fisioterapia</h1>
      <h2>Iniciar Sesión</h2>

      <div v-if="error" class="error">
        {{ error }}
      </div>

      <form @submit.prevent="handleLogin">
        <div class="form-group">
          <label for="email" class="form-label">Email</label>
          <input
            id="email"
            v-model="form.email"
            type="email"
            class="form-input"
            placeholder="correo@ejemplo.com"
            required
          />
        </div>

        <div class="form-group">
          <label for="password" class="form-label">Contraseña</label>
          <input
            id="password"
            v-model="form.password"
            type="password"
            class="form-input"
            placeholder="••••••••"
            required
          />
        </div>

        <button type="submit" class="btn btn-primary btn-block" :disabled="loading">
          {{ loading ? 'Iniciando sesión...' : 'Iniciar Sesión' }}
        </button>
      </form>

      <div class="login-help">
        <p><strong>Usuarios de prueba:</strong></p>
        <p>Fisioterapeuta: fisio@clinic.com / password123</p>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import api from '@/services/api'

const router = useRouter()

const form = ref({
  email: '',
  password: ''
})

const loading = ref(false)
const error = ref(null)

async function handleLogin() {
  loading.value = true
  error.value = null

  try {
    const response = await api.post('/auth/login', {
      email: form.value.email,
      password: form.value.password
    })

    const { token, usuario } = response.data

    // Guardar token y usuario en localStorage
    localStorage.setItem('token', token)
    localStorage.setItem('user', JSON.stringify(usuario))

    // Redirigir según el rol
    if (usuario.rol === 'FISIOTERAPEUTA') {
      router.push('/fisioterapeuta/dashboard')
    } else if (usuario.rol === 'RECEPCIONISTA') {
      router.push('/recepcionista/dashboard')
    } else if (usuario.rol === 'CLIENTE') {
      router.push('/cliente/dashboard')
    } else {
      router.push('/')
    }
  } catch (err) {
    console.error('Error login:', err)
    error.value = err.response?.data?.message || 'Error al iniciar sesión. Verifica tus credenciales.'
  } finally {
    loading.value = false
  }
}
</script>

<style scoped>
.login-container {
  min-height: 100vh;
  display: flex;
  justify-content: center;
  align-items: center;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  padding: 20px;
}

.login-card {
  background: white;
  border-radius: 12px;
  padding: 40px;
  box-shadow: 0 10px 40px rgba(0, 0, 0, 0.2);
  max-width: 450px;
  width: 100%;
}

.login-card h1 {
  text-align: center;
  color: #0066CC;
  margin-bottom: 10px;
  font-size: 28px;
}

.login-card h2 {
  text-align: center;
  color: #333;
  margin-bottom: 30px;
  font-size: 20px;
  font-weight: 500;
}

.btn-block {
  width: 100%;
  margin-top: 10px;
}

.login-help {
  margin-top: 30px;
  padding-top: 20px;
  border-top: 1px solid #eee;
  text-align: center;
  font-size: 13px;
  color: #666;
}

.login-help p {
  margin: 5px 0;
}
</style>
