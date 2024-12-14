<template>
    <div>
        <div class="left-Sidebar-main between-sider" id="left-Sidebar-main">
            <DailyHotArticle class="DailyHotArticle" />
            <HotArticle class="HotArticle" />
        </div>
    </div>
</template>

<script setup>
import HotArticle from "@/views/Sider/HotArticle.vue";
import DailyHotArticle from "@/views/Sider/DailyHotArticle.vue";
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
.HotArticle {
    margin-bottom: 20px;
}
.DailyHotArticle {
    margin-bottom: 20px;
}
@media (max-width: 1200px) {
    .left-Sidebar-main {
        width: 200px;
    }
}
@media (max-width: 700px) {
    .left-Sidebar-main {
        flex-direction: row;
        width: 100%;
        max-width: unset;
    }
    .DailyHotArticle {
        /* flex: auto; */
        /* width: 100%; */
        margin-right: 5px;
        margin-left: 0px;
        flex: 1;
    }
    .HotArticle {
        /* flex: auto; */
        /* width: max-content; */
        margin-right: 0px;
        flex: 1;
    }
}
</style>
