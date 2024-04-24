<script setup>
  import {onMounted, ref} from 'vue'
  import UserItem from "@/components/user/UserItem.vue";
  import UserService from "@/services/UserService.js";

  const users = ref([]);

  onMounted(async () => {
    users.value = await UserService.getUsersData();
  });
</script>

<template>
  <table v-if="users.length" class="table table-dark table-striped table-hover">
    <thead>
      <tr>
        <th scope="col">ID</th>
        <th scope="col">Username</th>
        <th scope="col">Password</th>
        <th scope="col">Email</th>
      </tr>
    </thead>
    <tbody>
      <user-item v-for="user in users" :user="user"/>
    </tbody>
  </table>
  <div v-else class="text-center">
    <h2 class="text-white-50">There are no users in database</h2>
  </div>
</template>

<style scoped>
  table {
    width: 50vw;
  }
</style>