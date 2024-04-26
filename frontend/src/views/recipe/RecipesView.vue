<script setup>
  import RecipeList from "@/components/recipe/RecipeList.vue";
</script>

<template>
  <div class="recipe-container align-top">
    <recipe-list/>
  </div>
</template>

<style scoped>
  .recipe-container {
    display: flex;
    flex-wrap: wrap;
    width: 90vw;
    padding-top: 40px;
    margin-left: 5vw;
    margin-right: 5vw;
  }
</style>