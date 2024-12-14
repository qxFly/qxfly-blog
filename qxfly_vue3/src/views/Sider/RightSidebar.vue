<template>
    <div>
        <div class="right-Sidebar-main between-sider" id="right-Sidebar-main">
            <Date class="Date item"></Date>
            <!-- <Saying></Saying> -->
            <ArticleAuthorInfoCard :authorId="uid" class="UserInfoCard item" :btnType="btnType"></ArticleAuthorInfoCard>
            <SuggestAuthor class="SuggestAuthor item"></SuggestAuthor>
            <!-- <Resource class="Resource item"></Resource> -->
            <Site class="item"></Site>
        </div>
    </div>
</template>

<script setup>
import Date from "@/views/Sider/Date.vue";
import Saying from "@/views/Sider/Saying.vue";
import Site from "@/views/Sider/Site.vue";
import SuggestAuthor from "@/views/Sider/SuggestAuthor.vue";
import ArticleAuthorInfoCard from "@/views/Article/ArticleAuthorInfoCard.vue";
import { onMounted, onUnmounted, ref } from "vue";
let uid = ref(0);
let timer = ref();
let btnType = ref("none");
function getUid() {
    if (
        sessionStorage.getItem("uid") != "null" &&
        sessionStorage.getItem("uid") != null &&
        sessionStorage.getItem("uid") != "undefined"
    ) {
        uid.value = parseInt(sessionStorage.getItem("uid"));
        clearInterval(timer.value);
        btnType.value = "detail";
    } else {
        btnType.value = "login";
    }
}

onMounted(() => {
    setTimeout(() => {
        getUid();
    }, 200);
    setTimeout(() => {
        getUid();
    }, 1000);
});
onUnmounted(() => {});
</script>

<style scoped>
.right-Sidebar-main {
    display: flex;
    flex-direction: column;
    position: sticky;
    top: 70px;
    transition: all 0.6s ease;
    max-width: 260px;
    width: 260px;
}
.item {
    margin-bottom: 20px;
}
@media (max-width: 1200px) {
    .right-Sidebar-main {
        width: 200px;
        max-width: unset;
    }
}
@media (max-width: 700px) {
    .right-Sidebar-main {
        width: 100%;
        margin: 0px;
    }
}
</style>
