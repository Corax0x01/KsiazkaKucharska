<script setup>
  import AuthenticationService from "@/services/AuthenticationService.js";
  import { useRouter } from "vue-router";
  import { activateAccountSchema } from "@/assets/formschemas/activateAccountSchema.js";
  import DynamicForm from "@/components/DynamicForm.vue";

  const router = useRouter();

  const activateAccount = async (token) => {
    await AuthenticationService.activateAccount(token);

    // TODO: change to something else, e.g. account view
    await router.push("/home")
  }
</script>

<template>
  <DynamicForm :schema="activateAccountSchema" @submitted="value => activateAccount(value.token)"/>
</template>

<style scoped>

</style>