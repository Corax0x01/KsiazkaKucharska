<script setup>
  import {useRoute} from "vue-router";
  import {ref, watch} from "vue";
  import {useAuthStore} from "@/stores/AuthStore.js";

  const route = useRoute();
  const authStore = useAuthStore();
  const allowCreateRecipe = ref(false);
  const allowCreateUser = ref(false);

  watch(route, () => {
    allowCreateRecipe.value = route.path === '/recipes';
    allowCreateUser.value = route.path === '/users';
  });
</script>

<template>
  <nav class="navbar navbar-expand-lg navbar-dark bg-dark">
    <div class="container-fluid">
      <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNavAltMarkup"
              aria-controls="navbarNavAltMarkup" aria-expanded="false" aria-label="Toggle navigation">
        <span class="navbar-toggler-icon"></span>
      </button>
      <div class="collapse navbar-collapse" id="navbarNavAltMarkup">
        <div class="navbar-nav">
          <RouterLink class="nav-link" active-class="active" to="/">Home</RouterLink>
          <RouterLink class="nav-link" active-class="active" to="/users">Users</RouterLink>
          <RouterLink class="nav-link" active-class="active" to="/recipes">Recipes</RouterLink>
          <RouterLink v-if="allowCreateUser" class="nav-link" active-class="active" to="/create-user">Create User</RouterLink>
          <RouterLink v-if="allowCreateRecipe" class="nav-link" to="/create-recipe">Create Recipe</RouterLink>
          <RouterLink v-if="!authStore.getAuthenticated()" class="nav-link" to="/login">Sign in</RouterLink>
          <RouterLink v-if="!authStore.getAuthenticated()" class="nav-link" to="/register">Register</RouterLink>
          <RouterLink v-if="authStore.getAuthenticated()" class="nav-link"
                      @click="authStore.logout()" to="/">LogOut</RouterLink>
        </div>
      </div>
    </div>
  </nav>
</template>