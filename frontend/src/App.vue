<template>
  <div id="app" class="min-h-screen flex flex-col">
    <Navbar class="flex-shrink-0" />
    <main class="flex-1 overflow-y-auto bg-gray-50">
      <router-view />
    </main>
    <Footer class="flex-shrink-0 mt-auto" />
  </div>
</template>

<script setup>
import Navbar from "./components/layout/Navbar.vue";
import Footer from "./components/layout/Footer.vue";
</script>
