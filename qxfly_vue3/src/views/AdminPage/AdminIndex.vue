<template>
    <div class="common-layout">
        <el-container>
            <el-aside width="160px" class="sider">
                <h1 class="sider-title">管理员后台</h1>
                <div class="roleLabel">
                    登入身份：<span>{{ roleName }}</span>
                </div>
                <div class="bg-switch">
                    <el-radio-group
                        v-model="isOpenBg"
                        @change="switchChange"
                        style="margin: 0 auto"
                        size="small"
                        :disabled="isAbleBg">
                        <el-radio-button label="关闭背景" :value="false" />
                        <el-radio-button label="打开背景" :value="true" />
                    </el-radio-group>
                </div>

                <el-menu
                    :default-active="selectNav"
                    :default-openeds="openedNav"
                    class="nav-items"
                    @open="handleOpen"
                    @close="handleClose"
                    :router="true"
                    @select="handleSelect">
                    <div
                        v-for="(item, index) in activeNav"
                        :key="index"
                        class="nav-item"
                        :class="{ active: isCurrNav(item, false) }">
                        <div v-if="item.children.length > 0">
                            <el-sub-menu :index="item.path">
                                <template #title>
                                    <i class="iconfont" v-html="item.icon"></i>
                                    <div class="nav-item-span">{{ item.name }}</div>
                                </template>
                                <div class="nav-item" :class="{ active: isCurrNav(item, true) }">
                                    <el-menu-item :index="item.path">
                                        <template #title>
                                            <i class="iconfont" v-html="item.icon"></i>
                                            <div class="nav-item-span">{{ item.name }}</div>
                                        </template>
                                    </el-menu-item>
                                </div>
                                <div
                                    v-for="(child, cindex) in item.children"
                                    :key="cindex"
                                    class="nav-item"
                                    :class="{ active: isCurrNav(child) }">
                                    <el-menu-item :index="child.path">
                                        <template #title>
                                            <i class="iconfont" v-html="child.icon"></i>
                                            <div class="nav-item-span">{{ child.name }}</div>
                                        </template>
                                    </el-menu-item>
                                </div>
                            </el-sub-menu>
                        </div>
                        <div v-else>
                            <el-menu-item :index="item.path">
                                <i class="iconfont" v-html="item.icon"></i>
                                <div class="nav-item-span">{{ item.name }}</div>
                            </el-menu-item>
                        </div>
                    </div>
                </el-menu>
            </el-aside>
            <el-main class="main">
                <router-view></router-view>
            </el-main>
        </el-container>
    </div>
</template>

<script setup>
import router from "@/router";
import { ref, onMounted, watch, onUnmounted } from "vue";
import { check, listAdminNavigations } from "@/api/Admin";
import { onBeforeRouteUpdate } from "vue-router";
import { ElMessage } from "element-plus";
import { useRoute } from "vue-router";
let useRouter = useRoute();
let navItems = ref([]);
//列出导航栏
function listNav() {
    listAdminNavigations().then((res) => {
        if (res.data.code == 1) {
            navItems.value = res.data.data;
        } else {
            ElMessage.error("获取导航栏失败");
            navItems.value = [
                {
                    name: "首页",
                    path: "/",
                    cssClass: "nav-item-home",
                    role: 0,
                },
            ];
        }
        checkNav();
    });
}

let role = ref(0);
let roleName = ref(""); // 角色名
let activeNav = ref([]); // 激活展示的导航栏
let selectNav = ref(""); // 当前选中的导航栏
let openedNav = ref([]);
function checkNav() {
    navItems.value.forEach((item) => {
        if (item.role == role.value || role.value == 1 || item.role == 0) {
            activeNav.value.push(item);
        }
    });
    if (router.currentRoute.value.path == "/manage") {
        router.push(activeNav.value[1].path);
    }
    openedNav.value.push(router.currentRoute.value.path);
}
/* 高亮当前导航， f:是否是二级菜单 */
function isCurrNav(nav, f) {
    if (nav.children != null && nav.children.length > 0) {
        let a = router.currentRoute.value.path == nav.path && f;
        return a;
    } else {
        return router.currentRoute.value.path == nav.path;
    }
}

/* 打开导航栏 */
function handleOpen(e) {
    // console.log(e);
}

/* 关闭导航栏 */
function handleClose(e) {
    // console.log(e);
}
/* 选择导航栏 */
function handleSelect(e) {
    selectNav.value = e;
}
/* 隐藏顶栏 */
function hideTopBar() {
    let topBar = document.getElementById("top-bar-1");
    if (topBar != null) {
        topBar.style.top = "-70px";
    }
}
/* 显示顶栏 */
function showTopBar() {
    let topBar = document.getElementById("top-bar-1");
    if (topBar != null) {
        topBar.style.top = "0px";
    }
}
/* 路由后恢复激活的导航栏 */
let routePage = ref(useRouter);
function setRoutePage() {
    let path = routePage.value.path;
    if (path != null && path != "") {
        selectNav.value = routePage.value.path;
    }
}
/* 背景开关 */
let isOpenBg = ref(true);
let isAbleBg = ref(false);
function switchChange(event) {
    isOpenBg.value = event;
}
function checkBg() {
    let bg = document.getElementById("index_bg");
    if (bg.style.backgroundImage == null || bg.style.backgroundImage == "" || bg.style.backgroundImage == "none") {
        isOpenBg.value = false;
        isAbleBg.value = true;
    }
}
watch(isOpenBg, (newVal, oldVal) => {
    let bg = document.getElementById("index_bg");
    if (newVal) {
        bg.style.display = "block";
    } else {
        bg.style.display = "none";
    }
});

watch(
    routePage.value,
    (newVal, oldVal) => {
        setRoutePage();
    },
    {
        deep: true,
    }
);
onMounted(async () => {
    await check().then((res) => {
        role.value = res.data.data;
        if (role.value == 1) roleName.value = "管理员";
        else if (role.value == 2) roleName.value = "文章审核员";
        else if (role.value == 3) roleName.value = "评论审核员";
        else if (role.value == 4) roleName.value = "用户审核员";
    });

    listNav();
    setRoutePage();
    hideTopBar();
    checkBg();
});
onUnmounted(() => {
    showTopBar();
});
</script>

<style scoped lang="less">
.sider {
    // padding: 10px;
    height: 100vh;
    // display: flex;
    // flex-direction: column;
    // align-content: center;
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
.bg-switch {
    width: 100%;
    display: flex;
    justify-content: center;
    align-items: center;
    margin: 10px 0;
}
.main {
    display: flex;
    justify-content: center;
}
.nav-items {
    margin-top: 50px;
}
.nav-item {
    transition: all 0.3s ease;
    border-bottom: 1px solid #ddd;
    // display: flex;
    // justify-content: center;
    // align-items: center;
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
    // color: #3f3f3f;
    user-select: none;
    // font-size: 14px;
}
.router-link {
    display: flex;
    height: 100%;
    width: 100%;
    // align-items: center;
    // justify-content: center;
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
.iconfont {
    font-style: normal;
    font-family: "iconfont" !important;
    margin-right: 4px;
}
/* .nav-item-home::before {
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
.nav-item-navigation::before {
    content: "\e6e4";
    font-family: "iconfont";
    margin-right: 4px;
} */
</style>

<style>
.el-menu-item:hover {
    background-color: #fff !important;
}
.el-menu {
    background-color: rgba(255, 255, 255, 0.5) !important;
}
.el-table {
    background-color: rgba(255, 255, 255, 0.3) !important;
}
tr.el-table__row--striped td.el-table__cell {
    background-color: rgba(250, 250, 250, 0.7) !important;
}
</style>
