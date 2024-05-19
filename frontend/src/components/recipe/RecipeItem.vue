<script setup>
  import {onMounted, ref} from "vue";
  import {useRouter} from "vue-router";
  import FileUploadService from "@/services/FileUploadService.js";
  import {useAuthStore} from "@/stores/AuthStore.js";

  const { recipe } = defineProps(['recipe'])

  const router = useRouter()
  const image = ref();

  const navigateToRecipe = () => {
    router.push(`/recipes/${recipe.id}`)
  }

  onMounted(async () => {
    image.value = await FileUploadService.getFile(recipe.imageURL, useAuthStore().token);
  })
</script>

<template>
  <div class="card bg-dark me-2 mb-3 text-center" :key="recipe.id" @click="navigateToRecipe" style="width: 20%; cursor: pointer">
    <img class="card-img-top" style="height: 90%" :src="image" :alt="recipe.title">
    <div class="card-body">
      <h5>{{ recipe.title }}</h5>
    </div>
  </div>
</template>