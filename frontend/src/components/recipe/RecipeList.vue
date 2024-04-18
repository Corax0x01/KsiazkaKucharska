<script setup>
import {onMounted, ref} from "vue";
  import RecipeItem from "@/components/recipe/RecipeItem.vue";

  const recipes = ref("asdah")

  // onMounted(() =>
  //   fetch("/api/recipes")
  //       .then((response) => response.json())
  //       .then((data) => {
  //         recipes.value = data;
  //       })
  // )
</script>

<template>
  <div v-if="recipes" v-for="recipe in recipes">
    <recipe-item :recipe="recipe"/>
  </div>
  <div v-else>
    <h1 class="text-white-50 text-center align-content-center">There are no recipes in database</h1>
  </div>
</template>