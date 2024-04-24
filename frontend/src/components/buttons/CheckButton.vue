<script setup>
import {faCheck} from "@fortawesome/free-solid-svg-icons";
</script>

<template>
  <button class="btn btn-bd-check me-1">
    <font-awesome-icon :icon="faCheck"/>
  </button>
</template>

<style scoped>
  .btn-bd-check {
    --bs-btn-bg: var(--bs-green);
    --bs-btn-hover-bg: #19875495;
    --bs-btn-active-bg: #19875480;
    --bs-btn-color: var(--bs-white);
    --bs-btn-border-radius: 100%;
  }
</style>