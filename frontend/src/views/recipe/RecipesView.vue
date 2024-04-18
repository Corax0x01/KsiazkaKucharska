<script setup>
  import RecipeList from "@/components/recipe/RecipeList.vue";
</script>

<template>
  <div class="recipe-container justify-content-center align-top">
    <recipe-list/>
  </div>
</template>

<style scoped>
  .recipe-container {
    width: 90vw;
    display: flex;
    flex-wrap: wrap;
    padding-top: 40px;
    margin-left: 5vw;
    margin-right: 5vw;
  }
</style>