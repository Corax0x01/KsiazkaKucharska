<script setup>
import {onMounted, ref} from "vue";
  import RecipeItem from "@/components/recipe/RecipeItem.vue";
  import RecipeService from "@/services/RecipeService.js";

  const recipes = ref([]);

  onMounted(async () => {
    recipes.value = await RecipeService.getRecipesData();
  });
</script>

<template>
  <recipe-item v-if="recipes.length" v-for="recipe in recipes" :recipe="recipe"/>
  <div v-else>
    <h1 class="text-white-50 text-center align-content-center">There are no recipes in database</h1>
  </div>
</template>