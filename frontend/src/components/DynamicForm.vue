<script setup>
  import {Form, Field, ErrorMessage} from "vee-validate";

  const emit = defineEmits(['submitted']);
  const { schema } = defineProps(['schema']);

  const onSubmit = async (values) => {
    emit('submitted', values);
  }
</script>

<template>
  <Form @submit="onSubmit" :validation-schema="schema.validationSchema">
    <div
        v-for="{ as, name, label, ...attrs } in schema.fields"
        :key="name"
    >
      <label :for="name">{{ label }}</label>
      <Field :as="as" :id="name" :name="name" v-bind="attrs" />

<!-- TODO: style error message -->
      <ErrorMessage :name="name"/>
    </div>

    <button>Submit</button>
  </Form>
</template>

<style scoped>

</style>