<template>
  <div id="app" class="min-h-screen flex flex-col">
    <Navbar />
    <main class="flex-1 bg-gray-50">
      <router-view />
    </main>
    <Footer class="mt-auto" />
  </div>
</template>

<script setup>
import Navbar from "./components/layout/Navbar.vue";
import Footer from "./components/layout/Footer.vue";
</script>
