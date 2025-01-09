<template>
    <div id="navigation">
        <div class="search">
            <div class="search-bar">
                <button class="search-btn" @click="search"></button>
                <my-input
                    class="search-input"
                    :clear="true"
                    @enter="search"
                    v-model="searchData"
                    placeholder="输入要搜索的文章标题" />
            </div>
        </div>

        <div class="nav-items" v-for="item in navigation" :key="item" :class="item.cssClass + '-1'">
            <div :class="{ active: currRoute(item) }">
                <router-link
                    :class="item.cssClass"
                    :id="item.cssClass"
                    :to="item.path"
                    @click="back(item)"
                    active-class="menu-item-active">
                    <i class="iconfont" v-html="item.icon"></i>
                    <span v-text="item.name"></span>
                </router-link>
            </div>
        </div>
    </div>
</template>

<script setup>
import router from "@/router";
import { onMounted, ref } from "vue";
import { onBeforeRouteUpdate, useRoute } from "vue-router";
import { listIndexNav } from "@/api";
import MyInput from "../MyInput.vue";
let useRouter = useRoute();

/* 首页导航栏列表 */
let navigation = ref([]);
function getNavigation() {
    listIndexNav().then((res) => {
        if (res.data.code == 1) {
            /* 文章编辑模式时移除发布按钮 */
            if (router.currentRoute.value.fullPath.match(/editArticle/) != null) editArticleMode(res.data.data);
            else navigation.value = res.data.data;
        }
    });
}
/* 文章编辑模式时移除发布按钮 */
function editArticleMode(data) {
    navigation.value = data.filter((item) => item.path.match(/editArticle/) == null);
}
let searchData = ref("");
/* 搜索 */
function search() {
    if (searchData.value == null || searchData.value.trim() == "") {
        searchData.value = "";
    }
    let query = router.currentRoute.value.query;
    if (router.currentRoute.value.path.match(/articleView/) != null) {
        router.push({
            path: process.env.APP_VUE_INDEX_PATH,
            query: {
                page: 1,
                search: searchData.value.trim(),
                classify: query.classify,
                sort: query.sort,
                tag: query.tag,
            },
        });
    } else if (router.currentRoute.value.path.match(/userArticle/) != null) {
        router.push({
            path: "/user/space/userArticle",
            query: {
                page: 1,
                search: searchData.value.trim(),
                sort: query.sort,
            },
        });
    } else if (router.currentRoute.value.path.match(/userCollection/) != null) {
        router.push({
            path: "/user/space/userCollection",
            query: {
                page: 1,
                search: searchData.value.trim(),
                sort: query.sort,
            },
        });
    } else {
        router.push({
            path: process.env.APP_VUE_INDEX_PATH,
            query: {
                page: 1,
                search: searchData.value.trim(),
                classify: "",
            },
        });
    }
}
function back(item) {
    if (item.path == "" && !router.currentRoute.value.fullPath.match(/articleView/)) router.go(-1);
}
/* 高亮当前导航 */
function currRoute(item) {
    // 当前路由是否是首页,是则继续
    if (router.currentRoute.value.fullPath.match(/index/) == null) return false;
    // 当前路由是否与文章相关
    let isArticleView =
        item.path.match(/articleView/) != null && router.currentRoute.value.fullPath.match(/articleView/) != null;
    // 当前路由是否是分类
    let currentRouteIsClassify = router.currentRoute.value.fullPath.match(/classify/) != null;

    if (item.path == router.currentRoute.value.fullPath) {
        // 当前路由是否是编辑文章
        if (!item.path.includes("/article/editArticle")) return true;
    } else if (isArticleView && item.path.match(/classify/) == null && !currentRouteIsClassify) {
        // 当前路由是文章页面，单不是文章分类
        return true;
    } else if (isArticleView && item.path.match(/classify/) != null && currentRouteIsClassify) {
        // 当前路由是文章分类页面
        return true;
    }
}

/* 判断首页，是否显示返回首页 */
function isIndex(item) {
    if (item.path != "/") {
        return true;
    } else {
        if (router.currentRoute.value.fullPath.match(/articleView/) != null) {
            return false;
        } else {
            return true;
        }
    }
}
router.beforeEach((to) => {
    if (to.path.includes("/index/article//editArticle/")) {
        let ele = document.getElementsByClassName("nav-item-editArticle");
        ele[0].innerHTML = "加载中";
        ele[0].style["content"] = "";
    }
});
onBeforeRouteUpdate((to) => {
    if (to.fullPath === "/") {
        router.push({
            path: process.env.APP_VUE_INDEX_PATH,
            query: {
                page: 1,
            },
        });
    }
});
onMounted(() => {
    getNavigation();
    searchData.value = useRouter.query.search;
});
</script>
<style></style>
<style scoped lang="less">
#navigation {
    display: flex;
    justify-content: center;
    align-items: center;
}
.search-bar {
    display: flex;
    align-items: center;
    margin-bottom: 2px;
}
.search {
    position: relative;
    top: 3px;
    height: 33px;
}
.search-input {
    width: 100%;
    margin: 0 0 0 10px;
    transition: all 0.3s ease;
}
.search-input:hover {
    .container input {
        background-color: #fff;
    }
}
// .search-bar::after {
//   position: relative;
//   top: 2px;
//   content: "\e6d2";
//   font-family: "iconfont";
//   margin-left: 4px;
//   color: #000000;
//   font-weight: 700;
//   font-size: 18px;
//   cursor: pointer;
// }
.search-btn {
    background-color: #a5d5ff;
    border: 1px solid #000;
    border-radius: 4px;
    width: 70px;
    height: 29px;
    cursor: pointer;
    line-height: 29px;
}
.search-btn::before {
    content: "搜索";
}
.search-btn:hover {
    color: #fff;
    background-color: #ff8e68;
    border: none;
}
/* 导航栏按钮 */
.nav-items:hover {
    .nav-item {
        border-bottom: 2px solid #000000;
    }
}
.active {
    border-bottom: 2px solid #000000;
}
.nav-items {
    display: block;
    margin: 0 8px;
    font-size: 16px;
    font-weight: bold;
    border: none;
    color: #3f3f3f;
    background-color: #00000000;
    height: min-content;
    cursor: pointer;
    transition: all 0.3s ease;
}
.iconfont {
    font-family: "iconfont" !important;
    font-style: normal;
    margin-right: 4px;
    color: #000;
}
.nav-item-editArticle {
    display: inline-block;
    width: 80px;
    font-weight: 500;
    font-size: 18px;
    border-radius: 4px;
    padding: 4px 14px;
    background-color: #ff8e68;
    color: #fff;
    text-align: center;
    // transition: all 0.3s ease;
}
.nav-item-editArticle:hover {
    background-color: #ff6632;
}
.nav-item-editArticle::before {
    content: "";
}
@media (max-width: 1150px) {
    .search {
        margin: 0 0 0 10px;
    }
    .nav-items {
        font-size: 14px;
        width: max-content;
        padding: 0;
        margin: 6px;
        min-width: 34px;
    }
    .nav-item-home::before {
        content: "";
    }
    .nav-item-image::before {
        content: "";
    }
    .nav-item-article::before {
        content: "";
    }
    .nav-item-word::before {
        content: "";
    }
    .nav-item-back::before {
        content: "";
    }

    .nav-item-editArticle {
        // font-size: 14px;
        padding: 0;
        width: max-content;
        background-color: unset;
        color: #ff6632;
        margin: 0;
        text-align: center;
    }
    .nav-item-editArticle::before {
        content: "";
    }
    .nav-item-classify::before {
        content: "";
    }
    .TopBarNavigation {
        margin: 0;
    }
}
@media (max-width: 700px) {
    .search-btn {
        font-size: 14px;
        background-color: #00000000;
        border: none;
        border-radius: 0;
        width: 20px;
        height: 20px;
        cursor: pointer;
        line-height: 0;
        overflow: hidden;
    }
    .search-btn::before {
        content: "\e6d2";
        font-family: "iconfont";
        // margin-right: 4px;
    }
    .nav-items {
        margin: 3px;
    }
    .nav-item-back-1 {
        display: none;
    }
}
@media (max-width: 435px) {
    .nav-items {
        margin: 0;
    }
    .search-input {
        margin: 2px;
    }
}
</style>
