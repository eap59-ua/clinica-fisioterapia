<template>
  <footer class="bg-gray-800 text-white py-8 mt-auto">
    <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
      <div class="grid grid-cols-1 md:grid-cols-3 gap-8">
        <div>
          <h3 class="text-xl font-bold mb-4">Clínica de Fisioterapia</h3>
          <p class="text-gray-400">
            Tu salud y bienestar son nuestra prioridad desde 2010.
          </p>
        </div>

        <div>
          <h3 class="text-xl font-bold mb-4">Enlaces Rápidos</h3>
          <ul class="space-y-2">
            <li>
              <router-link
                to="/servicios"
                class="text-gray-400 hover:text-white"
              >
                Servicios
              </router-link>
            </li>
            <li>
              <router-link to="/equipo" class="text-gray-400 hover:text-white">
                Nuestro Equipo
              </router-link>
            </li>
            <li>
              <router-link
                to="/contacto"
                class="text-gray-400 hover:text-white"
              >
                Contacto
              </router-link>
            </li>
          </ul>
        </div>

        <div>
          <h3 class="text-xl font-bold mb-4">Contacto</h3>
          <p class="text-gray-400">
            📍 Calle Mayor, 123, Alicante<br />
            📞 +34 965 123 456<br />
            📧 info@clinicafisio.com
          </p>
        </div>
      </div>

      <div class="border-t border-gray-700 mt-8 pt-8 text-center text-gray-400">
        <p>
          &copy; 2025 Clínica de Fisioterapia. Todos los derechos reservados.
        </p>
        <p class="mt-2 text-sm">
          Proyecto Académico - Ingeniería Web - Universidad de Alicante
        </p>
      </div>
    </div>
  </footer>
</template>
