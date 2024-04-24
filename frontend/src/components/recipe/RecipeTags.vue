<script setup>
  import RecipeService from "@/services/RecipeService.js";
  import {onMounted, ref, watch} from "vue";

  const tags = ref([]);
  const chosenTags = ref([]);
  const emit = defineEmits(['recipeTags']);

  onMounted(async () => {
    tags.value = await RecipeService.getAllTags();
  });

  watch(chosenTags, () => {
    emit('recipeTags', chosenTags.value);
  });
</script>

<template>
  <div class="tags">
    <div class="tag pe-2" v-for="tag in tags">
      <input :id="tag" type="checkbox" :value="tag" v-model="chosenTags">
      <label :for="tag">{{tag}}</label>
    </div>
  </div>
</template>

<style scoped>
  .tags {
    display: flex;
    flex-wrap: wrap;
  }

</style>