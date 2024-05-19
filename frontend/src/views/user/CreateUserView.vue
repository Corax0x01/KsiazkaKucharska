<script setup>
  import UserService from "@/services/UserService.js";
  import {ref} from "vue";
  import {useRouter} from "vue-router";
  import {useAuthStore} from "@/stores/AuthStore.js";

  const authStore = useAuthStore();
  const user = ref({
    "isAdmin": false
  });
  const router = useRouter();

  const createUser = (async () => {
    if(!user.value.username || !user.value.email || !user.value.password) return;
    await UserService.createUser(user.value, authStore.token);

    await router.push("/users");
  })

</script>

<template>
<!-- TODO: create form schema and use dynamic form component -->
  <form>
    <div class="mb-2">
      <label for="username" class="form-label">Username:</label>
      <input v-model="user.username" type="text" class="form-control-sm" id="username" name="username">
    </div>
    <div class="mb-2">
      <label for="email" class="form-label">Email:</label>
      <input v-model="user.email" class="form-control-sm" type="email" id="email" name="email">
    </div>
    <div class="mb-2">
      <label for="password" class="form-label">Password:</label>
      <input v-model="user.password" class="form-control-sm" type="password" id="password" name="password">
    </div>
    <div class="mb-2 form-check">
      <label for="isAdmin" class="form-check-label">IsAdmin:</label>
      <input v-model="user.isAdmin" class="form-check-input" type="checkbox" id="isAdmin" name="isAdmin">
    </div>
    <input type="button" class="btn btn-primary" @click="createUser" value="Create user">
  </form>
</template>