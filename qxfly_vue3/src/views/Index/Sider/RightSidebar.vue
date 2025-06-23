<template>
    <div>
        <div class="right-Sidebar-main index-siders" id="right-Sidebar-main">
            <EnglishVocabulary></EnglishVocabulary>
            <ArticleAuthorInfoCard :authorId="uid" :btnType="btnType" v-if="uid != 0"></ArticleAuthorInfoCard>
            <MessageTips></MessageTips>
            <Site></Site>
        </div>
    </div>
</template>

<script setup>
import MessageTips from "@/views/Index/Sider/MessageTips.vue";
import Site from "@/views/Index/Sider/Site.vue";
import ArticleAuthorInfoCard from "@/components/ArticleAuthorInfoCard.vue";
import EnglishVocabulary from "@/views/Index/Sider/EnglishVocabulary.vue";
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
    setBtnType();
    // setTimeout(() => {

    // }, 200);
    // setTimeout(() => {
    //     setBtnType();
    // }, 1000);
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
