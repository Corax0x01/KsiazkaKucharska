<script setup>
import {faTrash} from "@fortawesome/free-solid-svg-icons";
</script>

<template>
  <button class="btn btn-bd-trash">
    <font-awesome-icon :icon="faTrash"/>
  </button>
</template>

<style scoped>
  .btn-bd-trash {
    --bs-btn-bg: var(--bs-red);
    --bs-btn-hover-bg: #dc354595;
    --bs-btn-active-bg: #dc354580;
    --bs-btn-color: var(--bs-white);
    --bs-btn-border-radius: 100%;
  }
</style>