<script setup>
  import {useRoute} from "vue-router";
  import {ref, onMounted} from "vue";
  import RecipeService from "@/services/RecipeService.js";
  import FileUploadService from "@/services/FileUploadService.js";

  const route = useRoute();
  const recipe = ref({});
  const ingredients = ref([]);
  const image = ref();

  onMounted(async () => {
    const recipeId = route.params.id;
    recipe.value = await RecipeService.getRecipeData(recipeId);
    ingredients.value = await RecipeService.getRecipeIngredients(recipeId);
    image.value = await FileUploadService.getFile(recipe.value.imageURL);
  });
</script>

<template>
  <div class="recipe ps-3">
    <img class="recipe-img" :src="image" :alt="recipe.title">
    <h2>{{ recipe.title }}</h2>
    <p class="recipe-description">{{recipe.description}}</p>
    <ul class="ingredients">
      <li v-for="ingredient in ingredients">{{ingredient.ingredient.name}}: {{ingredient.quantity}}</li>
    </ul>
  </div>
</template>

<style scoped>
  .recipe {
    width: 66vw;
  }

  .recipe-img {
    max-height: 20vw;
  }

  .ingredients {
    list-style: none;
    padding: 0;
  }
</style>