<script setup>
  import DynamicForm from "@/components/DynamicForm.vue";
  import AuthenticationService from "@/services/AuthenticationService.js";
  import {useRouter} from "vue-router";
  import { registerSchema } from "@/assets/formschemas/registerSchema.js";

  const router = useRouter();

  const register = async (credentials) => {
    delete credentials.confirmPassword;
    await AuthenticationService.register(credentials);

    await router.push("/activate-account");
  }
</script>

<template>
  <DynamicForm :schema="registerSchema" @submitted="credentials => register(credentials)"/>
</template>

<style scoped>

</style>