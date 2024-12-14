<template></template>

<script setup>
import router from "@/router";
import { onMounted } from "vue";

onMounted(() => {
  router.replace("/articleView?page=1");
});
</script>

<style></style>
