<script setup>
  import { ref } from 'vue'

  const ctr = ref(0);

</script>

<template>
  <div class="align-content-center text-center">
    <h4>The current count is...</h4>
    <h1>{{ ctr }}</h1>
    <button class="btn btn-dark" @click="ctr--">-</button>
    <button class="btn btn-dark" @click="ctr++" >+</button>
  </div>
</template>

<style scoped>
</style>