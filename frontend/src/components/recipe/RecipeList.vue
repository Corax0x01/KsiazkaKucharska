<script setup>
import {ref, watch} from "vue";
  import RecipeItem from "@/components/recipe/RecipeItem.vue";
  import RecipeService from "@/services/RecipeService.js";

  const props = defineProps(['pageProp']);

  const recipes = ref([]);

  watch(() => props.pageProp, async () => {
    recipes.value = await RecipeService.getRecipesData(props.pageProp - 1);
  },
      {immediate: true}
  );

</script>

<template>
  <recipe-item v-if="recipes.length" v-for="recipe in recipes" :key="recipe.id" :recipe="recipe"/>
  <div v-else>
    <h1 class="text-white-50 text-center align-content-center">There are no recipes in database</h1>
  </div>
</template>