<script setup>
  import RecipeList from "@/components/recipe/RecipeList.vue";
  import Pagination from "@/components/Pagination.vue";
  import {ref} from "vue";

  const currentPage = ref(1);

</script>

<template>
  <div class="recipe-container align-top">
    <recipe-list :pageProp="currentPage"/>
  </div>
  <pagination @page="page => currentPage = page"/>
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