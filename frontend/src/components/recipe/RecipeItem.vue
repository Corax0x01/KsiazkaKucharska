<script setup>
  import {useRouter} from "vue-router";

  const { recipe } = defineProps(['recipe'])

  const router = useRouter()

  const navigateToRecipe = () => {
    router.push(`/recipes/${recipe.id}`)
  }
</script>

<template>
  <div :key="recipe.id" class="recipe-tile" @click="navigateToRecipe">
    <img src="https://cdn.aniagotuje.com/pictures/articles/2022/01/23318621-v-1080x1080.jpg" alt="jajecznica">
    <div class="recipe-text">
      <h2>Jajecznica</h2>
    </div>

<!--    <img src=" {{ recipe.imageURL }}" alt="{{ recipe.title }}">-->
<!--    <div class="recipe-text">-->
<!--      <h2>{{ recipe.title }}</h2>-->
<!--    </div>-->
  </div>
</template>

<style scoped>
  .recipe-tile {
    width: 256px;
    overflow: hidden;
    border-radius: 5%;
    border: 1px solid rgba(0, 0, 0, 0.8);
    box-shadow: 1px 1px 10px rgba(0, 0, 0, 0.1);
    margin-bottom: 35px;
    margin-right: 20px;
    cursor: pointer;
    background-color: dimgray;
  }

  .recipe-tile img {
    width: 100%;
    height: 190px;
    margin: 0;
  }

  .recipe-tile .recipe-text {
    padding: 0 5px;
  }

  .recipe-tile .recipe-text h2 {
    font-weight: bold;
    text-align: center;
  }
</style>