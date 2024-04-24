<script setup>
  import IngredientService from "@/services/IngredientService.js";
  import CheckButton from "@/components/buttons/CheckButton.vue";
  import {onMounted, ref} from "vue";

  const ingredients = ref([]);
  const ingredientNameSearchText = ref("");
  const ingredientQuantitySearchText = ref("");
  const emit = defineEmits(["createdItem"]);
  const showWarning = ref(false);
  const warningText = ref("");

  onMounted(async () => {
    ingredients.value = await IngredientService.getAllIngredients();
  });

  const checkButtonClicked = () => {
    if(ingredientNameSearchText.value.trim().length < 3) {
      ingredientNameSearchText.value = ingredientNameSearchText.value.trim();
      warningText.value = "Ingredient name must have at least 3 characters";
      showWarning.value = true;
      return;
    }

    if(ingredientQuantitySearchText.value.trim().length < 1){
      warningText.value = "Fill quantity field ";
      showWarning.value = true;
      return;
    }

    emit('createdItem', {
      "name": ingredientNameSearchText.value.trim(),
      "quantity" : ingredientQuantitySearchText.value.trim()
    });

    ingredientNameSearchText.value = "";
    ingredientQuantitySearchText.value = "";
    showWarning.value = false;
  };

</script>

<template>
  <div class="ingredient-create-item pt-2">
    <input v-model="ingredientNameSearchText" class="form-control-sm me-2" list="ingredientsList" placeholder="Ingredient name">
    <datalist id="ingredientsList">
      <option v-for="ingredient in ingredients" :value="ingredient.name"/>
    </datalist>
    <input v-model="ingredientQuantitySearchText" class="form-control-sm me-2" placeholder="Quantity">
    <check-button @click="checkButtonClicked"/>
    <div v-if="showWarning" class="warning">
      <p>{{warningText}}</p>
    </div>
  </div>
</template>

<style scoped>
  .warning {
    color: red;
    font-size: 12px;
  }
</style>