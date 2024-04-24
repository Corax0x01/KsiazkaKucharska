<script setup>
  import {ref} from "vue";

  const recipeDescriptionText = ref("");
  const emit = defineEmits(['recipeDescription']);

</script>

<template>
  <div class="recipe-description">
    <textarea v-model="recipeDescriptionText" @input="emit('recipeDescription', recipeDescriptionText)"
              placeholder="Recipe description" class="form-control"></textarea>
  </div>

</template>

<style scoped>
  textarea {
    border-radius: 5px;
    width: 100%;
    height: 50vh;
    resize: none;
  }
</style>