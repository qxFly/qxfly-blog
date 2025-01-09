<template>
    <div class="cardview" id="cardview">
        <slot></slot>
    </div>
</template>
<script setup></script>
<style scoped>
.cardview {
    width: 100%;
    padding: 10px;
    border-radius: 4px;
    background-color: transparent;
    box-shadow: 0 3px 8px 6px rgba(7, 17, 27, 0.08);
    transition: all 0.3s ease;
    overflow: hidden;
    margin-bottom: 20px;
}
.cardview:hover {
    box-shadow: 0 3px 8px 6px rgba(7, 17, 27, 0.14);
}
</style>
