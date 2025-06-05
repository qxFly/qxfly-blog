<template>
    <div class="my-select">
        <div class="article-sort" :id="'my-select-' + sortLabel" @click="extendArticleSort">
            <div class="my-select-label" v-text="sortLabel"></div>
            <div class="my-select-items">
                <slot></slot>
            </div>
        </div>
    </div>
</template>
<script setup>
import { ref } from "vue";
let props = defineProps({
    sortLabel: {
        type: String,
        default: "排序",
    },
});
/* 展开排序列表 */
let isExtendSort = ref(false);
function extendArticleSort() {
    let ele = document.getElementById("my-select-" + props.sortLabel);
    ele.addEventListener("mouseleave", closeSort);
    if (ele != null) {
        if (isExtendSort.value) {
            ele.style.height = "25px";
        } else {
            ele.style.height = "99px";
        }
        isExtendSort.value = !isExtendSort.value;
    }
}
/* 关闭排序列表 */
function closeSort() {
    let ele = document.getElementById("my-select-" + props.sortLabel);
    if (ele != null) {
        isExtendSort.value = false;
        ele.style.height = "25px";
        ele.removeEventListener("mouseleave", closeSort);
    }
}
</script>
<style scoped>
.my-select {
    position: relative;
    min-width: 90px;
    text-align: center;
}
.article-sort {
    position: absolute;
    height: 26px;
    width: 100%;
    overflow: hidden;
    border-radius: 4px;
    border: 1px solid;
    background-color: var(--main-background-color);
    z-index: 9;
    transition: all 0.2s ease;
}
.my-select-label {
    font-size: 15px;
    padding: 2px 4px;
    border-bottom: 1px solid;
    cursor: pointer;
}
</style>
<style>
.my-select-items div {
    font-size: 15px;
    padding: 2px 4px;
    cursor: pointer;
}

.my-select-items div:hover {
    color: #fff;
    background-color: var(--main-theme-color-blue);
}
</style>
