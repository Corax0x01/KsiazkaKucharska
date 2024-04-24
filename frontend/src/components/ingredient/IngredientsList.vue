<script setup>
import {ref} from "vue";
import DeleteButton from "@/components/buttons/DeleteButton.vue";
import IngredientCreateItem from "@/components/ingredient/IngredientCreateItem.vue";

const emit = defineEmits(['recipeIngredients']);

const addedIngredients = ref([]);

const addIngredient = (ingredient) => {
  addedIngredients.value.push(ingredient);
  emit('recipeIngredients', addedIngredients.value);
}

const removeIngredient = (ingredient) => {
  const removeIndex = addedIngredients.value.indexOf(ingredient);
  addedIngredients.value.splice(removeIndex, 1);
}
</script>

<template>
  <div class="ingredients-list">
    <ul>
      <li v-for="ingredient in addedIngredients">
        {{ingredient.name}}: {{ingredient.quantity}}
        <DeleteButton @click="removeIngredient(ingredient)"/>
      </li>
      <li>
        <IngredientCreateItem @created-item="(ingredient) => addIngredient(ingredient)"/>
      </li>
    </ul>
  </div>
</template>

<style scoped>
  ul {
    list-style: none;
    padding: 0;
  }
</style>