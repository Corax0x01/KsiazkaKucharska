<script>
class userClass {
  constructor(id, username, password, email) {
    this.id = id
    this.username = username
    this.password = password
    this.email = email
  }
}
</script>

<script setup>
  defineProps({
    user: userClass
  })
</script>
<template>
  <tr>
    <td>{{user.id}}</td>
    <td>{{user.username}}</td>
    <td>{{user.password}}</td>
    <td>{{user.email}}</td>
  </tr>
</template>

<style scoped>
  tr, td {
    border: 1px solid darkolivegreen;
  }
</style>