<script setup>
  const { user } = defineProps(['user'])
</script>

<template>
  <tr>
    <td>{{user.id}}</td>
    <td>{{user.username}}</td>
    <td>{{user.password}}</td>
    <td>{{user.email}}</td>
  </tr>
</template>