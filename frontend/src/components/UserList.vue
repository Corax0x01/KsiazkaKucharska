<script setup>
import {onMounted, reactive, ref} from 'vue'
import UserItem from "@/components/UserItem.vue";

const users = ref()

  onMounted(() =>
      fetch("/api/users")
          .then((response) => response.json())
          .then((data) => {
            users.value = data;
          })
  )
</script>

<template>
  <table>
    <thead>
      <tr>
        <th>ID</th>
        <th>username</th>
        <th>password</th>
        <th>email</th>
      </tr>
    </thead>
    <tbody v-for="user in users">
      <user-item :user="user"/>
    </tbody>
  </table>
</template>

<style scoped>
  table {
    border: 2px solid darkolivegreen;
  }
  th {
    border: 1px solid darkolivegreen;
  }

</style>