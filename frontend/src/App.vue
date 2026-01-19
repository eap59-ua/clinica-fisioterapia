<template>
  <div id="app">

    <router-view />
    <Footer />
  </div>
</template>

<script setup>
import Navbar from "./components/layout/Navbar.vue";
import Footer from "./components/layout/Footer.vue";
</script>
