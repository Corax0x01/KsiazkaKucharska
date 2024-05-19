<script setup>
import {onMounted, ref} from "vue";
import {useAuthStore} from "@/stores/AuthStore.js";
import RecipeService from "@/services/RecipeService.js";

  const emit = defineEmits(['page']);

  const currentPage = ref(1);
  const totalPages = ref();
  const pageNumbers = ref([1, 2, 3]);
  const disabledPrev = ref(true);
  const disabledNext = ref(false);

  const setPage = (page) => {

    if(page < 1 || page > totalPages) return
    currentPage.value = page;

    if(currentPage.value === 1) {
      pageNumbers.value = [1, 2, 3];
      disabledPrev.value = true;
    } else if (currentPage.value === totalPages.value) {
      pageNumbers.value = [currentPage.value - 2, currentPage.value - 1, currentPage.value];
      disabledNext.value = true;
      disabledPrev.value = false;
    } else {
      pageNumbers.value = [currentPage.value - 1, currentPage.value, currentPage.value + 1];
      disabledPrev.value = false;
      disabledNext.value = false;
    }

    emit('page', currentPage.value);
  }

  onMounted(async () => {
    totalPages.value = await RecipeService.getNumberOfPages(useAuthStore().token);
  })

</script>

<template>
  <div class="pagination-container">
    <ul class="pagination">
      <li class="page-item pointer" :class="{disabled: disabledPrev}"><a class="page-link" @click="setPage(currentPage - 1)">Previous</a></li>
      <li class="page-item pointer" :class="{active: pageNumbers[0] === currentPage}"><a class="page-link" @click="setPage(pageNumbers[0])">{{pageNumbers[0]}}</a></li>
      <li class="page-item pointer" :class="{active: pageNumbers[1] === currentPage}"><a class="page-link" @click="setPage(pageNumbers[1])">{{pageNumbers[1]}}</a></li>
      <li class="page-item pointer" :class="{active: pageNumbers[2] === currentPage}"><a class="page-link" @click="setPage(pageNumbers[2])">{{pageNumbers[2]}}</a></li>
      <li class="page-item pointer" :class="{disabled: disabledNext}"><a class="page-link" @click="setPage(currentPage + 1)">Next</a></li>
    </ul>
  </div>
</template>

<style scoped>
.pointer {
  cursor: pointer;
}

.pagination-container {
  position: absolute;
  top: 94%;
  left: 40%;
}
</style>