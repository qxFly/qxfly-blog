<template>
    <!-- 分页 -->
    <div class="Pages" v-if="totalPages > 1">
        <div class="pages">
            <!-- 往前一页 -->
            <button class="page-button-pre" @click="changePage(modelValue > 1 ? modelValue - 1 : modelValue)">
                <svg
                    t="1704979629175"
                    class="icon-pre"
                    viewBox="0 0 1024 1024"
                    version="1.1"
                    xmlns="http://www.w3.org/2000/svg"
                    p-id="2400"
                    width="20"
                    height="20">
                    <path
                        d="M703.36 175.95c11.58 13.35 10.14 33.56-3.22 45.14l-331.38 282.1c-4.61 4-4.6 11.17 0.04 15.14l331.39 284.48c13.41 11.51 14.95 31.71 3.44 45.12-11.51 13.41-31.71 14.95-45.12 3.44L279.62 526.12c-9.27-7.96-9.3-22.29-0.07-30.29l378.68-323.1c13.35-11.57 33.56-10.13 45.13 3.22z"
                        p-id="2401"></path>
                </svg>
            </button>
            <!-- 跳转第一页 -->
            <button class="page-button-start" @click="changePage(1)"><span class="page-btn-span">1</span></button>
            <span class="span-blank"></span>
            <!-- 分页 -->
            <div class="page-button" v-for="page in pages" :key="page">
                <button @click="changePage(page)" :class="{ currPage: modelValue == page }">
                    <span class="page-btn-span">{{ page }}</span>
                </button>
            </div>
            <span class="span-blank"></span>
            <!-- 跳转最后一页 -->
            <button class="page-button-end" @click="changePage(totalPages)">
                <span class="page-btn-span">{{ totalPages == 0 || totalPages == null ? 1 : totalPages }}</span>
            </button>
            <!-- 往下一页 -->
            <button class="page-button-next" @click="changePage(modelValue < totalPages ? modelValue + 1 : modelValue)">
                <svg
                    t="1704979335825"
                    class="icon-end"
                    viewBox="0 0 1024 1024"
                    version="1.1"
                    xmlns="http://www.w3.org/2000/svg"
                    p-id="2247"
                    width="20"
                    height="20">
                    <path
                        d="M320.64 848.05c-11.58-13.35-10.14-33.56 3.22-45.14l331.38-282.1c4.61-4 4.6-11.17-0.04-15.14L323.81 221.19c-13.41-11.51-14.95-31.71-3.44-45.12 11.51-13.41 31.71-14.95 45.12-3.44l378.88 325.25c9.27 7.96 9.3 22.29 0.07 30.29l-378.68 323.1c-13.34 11.57-33.55 10.13-45.12-3.22z"
                        p-id="2248"></path>
                </svg>
            </button>
        </div>
    </div>
</template>
<script setup>
import { ref, watchEffect } from "vue";
let pages = ref([]); // 分页栏
let pageCount = ref(0); // 分页栏按钮个数
let beginPage = ref(1); //分页栏第一个按钮
let endPage = ref(); //分页栏最后一个按钮
const props = defineProps({
    /* 总页数 */
    totalPages: {
        type: Number,
        default: 0,
    },
    /* 分页大小 */
    pageSize: {
        type: Number,
        default: 1,
    },
    /* 当前页 */
    modelValue: {
        type: Number,
        default: 1,
    },
});
let emit = defineEmits(["changePage", "update:modelValue"]);

/* 设置分页栏 */
function GetPage() {
    if (props.totalPages <= 10) {
        pageCount.value = props.totalPages;
        endPage.value = props.totalPages;
    } else {
        pageCount.value = 10;
        endPage.value = 10;
    }
    if (props.modelValue > 6) {
        beginPage.value = props.modelValue - 5;
        endPage.value = props.modelValue + 4;
        if (endPage.value > props.totalPages) {
            endPage.value = props.totalPages;
        }
    } else {
        beginPage.value = 1;
    }
    pages.value = [];
    for (var i = beginPage.value, j = 0; i <= endPage.value; i++, j++) {
        pages.value[j] = i;
    }
}

/* 换页操作 */
function changePage(page = 0) {
    emit("changePage", page);
    emit("update:modelValue", page);
}
watchEffect(GetPage);
</script>

<!--分页栏-->
<style scoped>
.pages {
    margin-top: 40px;
    display: flex;
    padding: 0;
    justify-content: center;
    align-items: center;
    border-radius: 4px;
    box-shadow: 0 1px 6px 4px rgba(7, 17, 27, 0.08);
    padding: 20px 0;
    transition: all ease 0.2s;
    background-color: transparent;
}
.pages:hover {
    box-shadow: 0 1px 6px 4px rgba(7, 17, 27, 0.14);
}
.span-blank {
    height: 20px;
    margin: 0 10px;
    border: 2px solid #5a5a5a;
    font-size: 20px;
    font-weight: 700;
}
.page-btn-span {
    font-weight: 700;
    font-size: 16px;
}
.page-button {
    margin: 0 5px;
    max-width: 50px;
    width: 100%;
}
.pages button {
    display: block;
    height: 30px;
    width: 100%;
    border-radius: 4px;
    background-color: rgba(255, 255, 255, 0.5);
    box-shadow: 0 1px 6px 4px rgba(7, 17, 27, 0.2);
    transition: all ease 0.3s;
    border: none;
    cursor: pointer;
    overflow: hidden;
}
.page-button-pre,
.page-button-next {
    max-width: 50px;
    padding: 5px;
    margin: 0 5px;
}
.page-button-start,
.page-button-end {
    margin: 0 5px;
    max-width: 50px;
    /* width: 60px !important; */
}
.currPage {
    box-shadow: 0 1px 3px 3px rgba(7, 17, 27, 0.8) !important;
}
.pages button:hover {
    box-shadow: 0 1px 3px 3px rgba(7, 17, 27, 0.5);
}
@media (max-width: 1200px) {
    .page-button-start,
    .page-button-end {
        display: none !important;
    }
    .span-blank {
        display: none;
    }

    .page-button-pre,
    .page-button-next {
        padding: 0;
    }
}
@media (max-width: 800px) {
    .icon-end,
    .icon-pre {
        width: 10px;
        height: 10px;
    }
}
</style>
