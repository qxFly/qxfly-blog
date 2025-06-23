<template>
    <div id="navigation">
        <div class="search">
            <div class="search-bar">
                <button class="search-btn" @click="search"></button>
                <MyInput
                    class="search-input"
                    :clear="true"
                    @enter="search"
                    v-model="searchData"
                    placeholder="输入要搜索的文章标题">
                </MyInput>
            </div>
        </div>

        <div class="nav-items">
            <div class="nav-item" :class="{ active: currRoute(item) }" v-for="item in navigation" :key="item">
                <div :class="isHeightLight(item.path) ? 'nav-item-heightlight' : ''">
                    <div :class="item.cssClass" :id="item.cssClass" @click="toPath(item.path)">
                        <i class="iconfont" v-html="item.icon"></i>
                        <span v-text="item.name"></span>
                    </div>
                </div>
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
            path: process.env.VUE_APP_INDEX_PATH,
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
            path: "/index/articleClassify",
            query: {
                page: 1,
                search: searchData.value.trim(),
                classify: "",
            },
        });
    }
}
function toPath(path) {
    if (path == "-1") {
        router.back();
    } else if (path.match(/http/)) {
        window.open(path, "_blank");
    } else {
        router.push(path);
    }
}
/* 高亮当前导航 */
function currRoute(item) {
    // 当前路由是否是首页,是则继续
    if (router.currentRoute.value.fullPath.match(/index/) == null) return false;
    let path = item.path.split("?")[0]; // 去除参数的路径
    if (path == router.currentRoute.value.path && !path.includes("/article/editArticle")) return true;
    return false;
}

/* 触摸时是否高亮 */
function isHeightLight(path) {
    if (path.match(/editArticle/)) return false;
    return true;
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
// router.beforeEach((to) => {
//     if (to.path.includes("/index/article//editArticle/")) {
//         let ele = document.getElementsByClassName("nav-item-editArticle");
//         ele[0].innerHTML = "加载中";
//         ele[0].style["content"] = "";
//     }
// });
onMounted(() => {
    getNavigation();
    searchData.value = useRouter.query.search;
});
</script>
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
    color: #000000;
    background-color: #fff;
    border: 1px solid var(--main-theme-color-blue);
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
    background-color: var(--main-theme-color-orange);
    border: none;
}
/* 导航栏按钮 */
.nav-item {
    // color: #000000;
    display: block;
    margin: 0 8px;
    font-size: 16px;
    // font-weight: bold;
    border: none;
    background-color: #00000000;
    height: min-content;
    cursor: pointer;
    transition: all 0.3s ease;
    // white-space: nowrap;
}
.nav-items {
    display: flex;
    align-items: center;
    justify-content: center;
    user-select: none;
}
.nav-item-heightlight:hover {
    border-bottom: 1px solid;
}
.active {
    // color: #000;
    border-bottom: 1px solid;
}

.iconfont {
    font-family: "iconfont" !important;
    font-style: normal;
    margin-right: 4px;
    // color: #000;
}
.nav-item-editArticle {
    display: inline-block;
    width: 80px;
    font-weight: 500;
    font-size: 18px;
    border-radius: 4px;
    padding: 4px 14px;
    background-color: var(--main-theme-color-orange);
    color: #fff;
    text-align: center;
    // transition: all 0.3s ease;
}
.nav-item-editArticle:hover {
    background-color: #ff6632;
    .nav-item {
        border-bottom: none;
    }
}
.nav-item-editArticle::before {
    content: "";
}
@media (max-width: 1150px) {
    .search {
        margin: 0 0 0 10px;
    }
    .nav-items {
        width: max-content;
        padding: 0;
        margin: 6px;
        min-width: 34px;
    }
    .nav-item {
        font-size: 14px;
    }
    .iconfont {
        display: none;
    }
    .n .nav-item-editArticle {
        // font-size: 14px;
        padding: 0;
        width: max-content;
        background-color: unset;
        color: #ff6632;
        margin: 0;
        text-align: center;
    }
    .TopBarNavigation {
        margin: 0;
    }
}
@media (max-width: 850px) {
    .nav-item-editArticle {
        font-size: 16px;
        width: 20px;
        padding: 2px;
        margin-top: 4px;
        display: flex;
        align-items: center;
        justify-content: center;
    }
}
@media (max-width: 800px) {
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
@media (max-width: 500px) {
    .nav-items {
        margin: 0;
        width: 200px;
    }
    .nav-item {
        // margin: 8px;
    }
    .search-input {
        margin: 0 0 0 2px;
    }
    .search {
        width: 100px;
    }
}
</style>
