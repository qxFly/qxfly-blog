<template>
    <div class="common-layout">
        <el-container>
            <el-aside width="160px" class="sider">
                <h1 class="sider-title">管理员后台</h1>
                <div class="roleLabel">
                    登入身份：<span>{{ roleName }}</span>
                </div>
                <div class="nav-items">
                    <div
                        class="nav-item"
                        :class="{ active: isCurrNav(item.path) }"
                        v-for="item in activeNav"
                        :key="item">
                        <router-link :to="item.path" active-class="router-link-active" class="router-link">
                            <div class="nav-item-span" :class="item.class">{{ item.name }}</div>
                        </router-link>
                    </div>
                </div>
            </el-aside>

            <el-main class="main">
                <router-view></router-view>
            </el-main>
        </el-container>
    </div>
</template>

<script setup>
import router from "@/router";
import { ref, onMounted } from "vue";
import { check } from "@/api/Admin";
import { onBeforeRouteUpdate } from "vue-router";

let role = ref(0);
let roleName = ref("");
let activeNav = ref([]);
let flag = ref(false);
function checkNav() {
    navItems.value.forEach((item) => {
        if (item.role == role.value || role.value == 1 || item.role == 0) {
            activeNav.value.push(item);
        }
    });
    if (router.currentRoute.value.path == "/manage") {
        router.push(activeNav.value[1].path);
    }
    flag.value = true;
}
function isCurrNav(path) {
    return router.currentRoute.value.path == path;
}
let navItems = ref([
    {
        name: "首页",
        path: "/",
        class: "nav-item-home",
        role: 0,
    },
    {
        name: "用户",
        path: "/manage/user",
        class: "nav-item-user",
        role: 4,
    },
    {
        name: "文章",
        path: "/manage/article",
        class: "nav-item-article",
        role: 2,
    },
    {
        name: "评论",
        path: "/manage/articleComment",
        class: "nav-item-comment",
        role: 3,
    },
    {
        name: "分类",
        path: "/manage/classify",
        class: "nav-item-classify",
        role: 1,
    },
    {
        name: "标签",
        path: "/manage/tag",
        class: "nav-item-tag",
        role: 1,
    },
    {
        name: "图片",
        path: "/manage/image",
        class: "nav-item-image",
        role: 1,
    },
    {
        name: "站点",
        path: "/manage/site",
        class: "nav-item-site",
        role: 1,
    },
]);
onMounted(async () => {
    await check().then((res) => {
        role.value = res.data.data;
        if (role.value == 1) roleName.value = "管理员";
        else if (role.value == 2) roleName.value = "文章审核员";
        else if (role.value == 3) roleName.value = "评论审核员";
        else if (role.value == 4) roleName.value = "用户审核员";
    });
    checkNav();
});
</script>

<style scoped lang="less">
.sider {
    // padding: 10px;
    height: 100vh;
    display: flex;
    flex-direction: column;
    align-content: center;
    border-right: 1px solid #ddd;
}
.sider-title {
    text-align: center;
    margin-top: 40px;
}
.roleLabel {
    text-align: center;
    margin-top: 30px;
    font-size: 14px;
}
.roleLabel span {
    font-weight: 700;
}
.main {
    display: flex;
    justify-content: center;
}
.nav-items {
    margin-top: 70px;
}
.nav-item {
    height: 60px;
    transition: all 0.3s ease;
    border-bottom: 1px solid #ddd;
    display: flex;
    justify-content: center;
    align-items: center;
}
.nav-item.active {
    border-bottom: 1px solid #333;
    border-top: 1px solid #333;
    border-right: 1px solid #333;
}
.nav-item.active:hover {
    box-shadow: 0 1px 3px 3px rgba(7, 17, 27, 0.14);
    border-bottom: 1px solid #fff;
    border-top: 1px solid #fff;
}
.nav-item:hover {
    box-shadow: 0 1px 3px 3px rgba(7, 17, 27, 0.14);
    border-bottom: 1px solid #fff;
}
.nav-item-icon {
    width: 16px;
    height: 16px;
    margin-right: 6px;
}
.nav-item-span {
    color: #3f3f3f;
    user-select: none;
}
.router-link {
    display: flex;
    height: 100%;
    width: 100%;
    align-items: center;
    justify-content: center;
}
</style>
<style scoped>
.Content {
    margin-left: 70px;
}
.content {
    width: min-content;
    height: 100vh;
    margin: 70px auto;
}
.nav-item-home::before {
    content: "\e6fe";
    font-family: "iconfont";
    margin-right: 4px;
}
.nav-item-user::before {
    content: "\e737";
    font-family: "iconfont";
    margin-right: 4px;
}
.nav-item-article::before {
    content: "\e6db";
    font-family: "iconfont";
    margin-right: 4px;
}
.nav-item-comment::before {
    content: "\e6e5";
    font-family: "iconfont";
    margin-right: 4px;
}
.nav-item-classify::before {
    content: "\e6da";
    font-family: "iconfont";
    margin-right: 4px;
}
.nav-item-tag::before {
    content: "\e6d3";
    font-family: "iconfont";
    margin-right: 4px;
}
.nav-item-image::before {
    content: "\e6c5";
    font-family: "iconfont";
    margin-right: 4px;
}
.nav-item-site::before {
    content: "\e703";
    font-family: "iconfont";
    margin-right: 4px;
}
</style>
