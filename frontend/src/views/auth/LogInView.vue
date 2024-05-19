<script setup>
  import {useRouter} from "vue-router";
  import {useAuthStore} from "@/stores/AuthStore.js";
  import AuthenticationService from "@/services/AuthenticationService.js";
  import DynamicForm from "@/components/DynamicForm.vue";
  import {loginSchema} from "@/assets/formschemas/loginSchema.js";

  const authStore = useAuthStore();
  const router = useRouter();

  const logIn = async (credentials) => {
    const receivedToken = await AuthenticationService.logIn(credentials);

    if(receivedToken != null) {
      authStore.token = receivedToken;
      authStore.authenticated = true;
      await router.push("/recipes");
    } else {
      authStore.token = null;
      authStore.authenticated = false;
      console.error("Error when logging in");
    }

  }

</script>

<template>
  <DynamicForm :schema="loginSchema" @submitted="credentials => logIn(credentials)"/>
</template>
