<template>
    <div>
        <div class="right-Sidebar-main between-sider" id="right-Sidebar-main">
            <Date class="Date item"></Date>
            <!-- <Saying></Saying> -->
            <ArticleAuthorInfoCard :authorId="uid" class="UserInfoCard item" :btnType="btnType"></ArticleAuthorInfoCard>
            <MessageTips></MessageTips>
            <SuggestAuthor class="SuggestAuthor item"></SuggestAuthor>
            <!-- <Resource class="Resource item"></Resource> -->
            <Site class="item"></Site>
        </div>
    </div>
</template>

<script setup>
import Date from "@/views/Sider/Date.vue";
import MessageTips from "@/views/Sider/MessageTips.vue";
import Site from "@/views/Sider/Site.vue";
import SuggestAuthor from "@/views/Sider/SuggestAuthor.vue";
import ArticleAuthorInfoCard from "@/components/ArticleAuthorInfoCard.vue";
import { onMounted, onUnmounted, ref } from "vue";
import md5 from "js-md5";
let uid = ref(0);
let btnType = ref("none");
function setBtnType() {
    if (localStorage.getItem(md5("token")) != null) {
        uid.value = parseInt(localStorage.getItem("uid"));
        btnType.value = "detail";
    } else {
        btnType.value = "login";
    }
}

onMounted(() => {
    setTimeout(() => {
        setBtnType();
    }, 200);
    setTimeout(() => {
        setBtnType();
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
@media (max-width: 1200px) {
    .right-Sidebar-main {
        width: 200px;
        max-width: unset;
    }
}
@media (max-width: 800px) {
    .right-Sidebar-main {
        width: 100%;
        margin: 0px;
    }
}
</style>
