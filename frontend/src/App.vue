<script setup>
  import Navbar from "@/components/Navbar.vue";
</script>

<template>
  <header>
    <Navbar/>
  </header>
  <main>
    <RouterView />
  </main>
</template>

<style scoped>
  main {
    width: 100vw;
    min-height: 100vh;
    background-color: darkslategray;
  }
</style>