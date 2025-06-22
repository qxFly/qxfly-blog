<template>
    <div>
        <div class="left-Sidebar-main index-siders" id="left-Sidebar-main">
            <Date></Date>
            <DailyHotArticle class="DailyHotArticle" />
            <HotArticle class="HotArticle" />
        </div>
    </div>
</template>

<script setup>
import HotArticle from "@/views/Index/Sider/HotArticle.vue";
import DailyHotArticle from "@/views/Index/Sider/DailyHotArticle.vue";
import Date from "@/views/Index/Sider/Date.vue";
</script>

<style scoped>
.left-Sidebar-main {
    display: flex;
    flex-direction: column;
    position: sticky;
    top: 70px;
    transition: all 0.6s ease;
    width: 260px;
    max-width: 260px;
}
@media (max-width: 1200px) {
    .left-Sidebar-main {
        width: 200px;
    }
}
@media (max-width: 800px) {
    .left-Sidebar-main {
        flex-direction: row;
        width: 100%;
        max-width: unset;
    }
    .DailyHotArticle {
        margin-right: 5px;
        margin-left: 0px;
        flex: 1;
    }
    .HotArticle {
        margin-right: 0px;
        flex: 1;
    }
}
</style>
