<script setup>
  import {useRoute} from "vue-router";
  import {ref, onMounted} from "vue";

  const route = useRoute()
  const recipe = ref()

  onMounted(() =>
      fetch("/api/recipes/" + route.params.id)
          .then((response) => response.json())
          .then((data) => {
            recipe.value = data;
          })
  )
</script>

<template>
  <div class="recipe">
<!--    <img :src="recipe.imageURL" :alt="recipe.title">-->
<!--    <h2>{{ recipe.title }}</h2>-->
    <h2>Recipe info</h2>
  </div>
</template>

<style scoped>

</style>