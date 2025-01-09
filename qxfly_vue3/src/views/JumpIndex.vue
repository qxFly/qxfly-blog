<template></template>

<script setup>
import router from "@/router";
import { onMounted } from "vue";

onMounted(() => {
    router.replace(process.env.APP_VUE_INDEX_PATH + "?page=1");
});
</script>

<style></style>
