<script setup>
  import IngredientsList from "@/components/ingredient/IngredientsList.vue"
  import RecipeDescription from "@/components/recipe/RecipeDescription.vue";
  import RecipeTags from "@/components/recipe/RecipeTags.vue";
  import RecipeService from "@/services/RecipeService.js";
  import {ref} from "vue";
  import {useRouter} from "vue-router";
  import FileUploadService from "@/services/FileUploadService.js";

  const router = useRouter();
  const recipe = ref({
    "title": "",
    "description": "",
    "imageURL": "",
    "recipeURL": "",
    "tags": [],
    "user": {}
  });
  const ingredientsList = ref([]);
  const file = ref();

  const createRecipeButtonClicked = async () => {
    // TODO: swap with logged user id
    recipe.value.user = {
      "id": 1
    };
    const recipeResponse = await RecipeService.createRecipe(recipe.value, ingredientsList.value);
    await FileUploadService.uploadFile(file.value, recipeResponse.id);
    await router.push("/recipes/" + recipeResponse.id);
  }

  const setFile = (event) => {
    file.value = event.target.files[0];
    recipe.value.imageURL = file.value.name;
  }
</script>

<template>
  <div class="create-recipe-view ps-3 pt-2">
    <input v-model="recipe.title" class="form-control w-50" type="text" placeholder="Recipe name"><br>
    <input type="file" accept="image/*" placeholder="image" v-on:change="setFile($event)" class="form-control w-50">
    <IngredientsList @recipe-ingredients="ingredients => ingredientsList = ingredients"/>
    <RecipeDescription @recipe-description="description => recipe.description = description"/>
    <RecipeTags @recipe-tags="tags => recipe.tags = tags"/>
    <button class="btn btn-primary btn-dark" @click="createRecipeButtonClicked">Create recipe</button>
  </div>
</template>

<style scoped>
  .create-recipe-view {
    width: 66vw;
  }
</style>
