<template>
    <div class="main">
        <div class="Content">
            <router-view v-slot="{ Component }">
                <keep-alive>
                    <component :is="Component" :key="$route.path" v-if="$route.meta.keepAlive" />
                </keep-alive>
                <keep-alive>
                    <component :is="Component" :key="$route.path" v-if="!$route.meta.keepAlive" />
                </keep-alive>
            </router-view>
        </div>
    </div>
</template>

<script setup></script>
<style scoped>
.main {
    /* position: relative; */
    width: 60%;
    min-width: 340px;
    margin: 0 20px;
    margin-bottom: 20px;
    transition: all 0.3s ease;
    overflow: hidden;
    height: 100%;
}
.Content {
    overflow: hidden;
    padding: 30px 24px;
}
@media (max-width: 700px) {
    .main {
        min-width: unset;
        width: 100%;
        margin: 0px 0px 20px 0px;
        padding: 0;
    }
    .Content {
        padding: 0;
    }
}
</style>
