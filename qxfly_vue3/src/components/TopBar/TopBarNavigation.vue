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

        <div class="nav-items" v-for="item in navigation" :key="item" :class="item.class + '-1'">
            <div :class="{ active: currRoute(item) }">
                <router-link
                    :class="judgeStyle(item.class)"
                    :id="item.class"
                    :to="item.path"
                    @click="back(item)"
                    active-class="menu-item-active">
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
import MyInput from "../MyInput.vue";
let useRouter = useRoute();
let navigation = ref([
    { name: "首页", path: "/", class: "nav-item-home" },
    { name: "图片", path: "/imageView", class: "nav-item-image" },
    { name: "文章", path: "/articleView?page=1", class: "nav-item-article" },
    { name: "分类", path: "/articleView?classify=", class: "nav-item-classify", classify: true },
    { name: "返回", path: "", class: "nav-item-back" },
    {
        name: "发布",
        path: "/article/editArticle/14/45/14",
        class: "nav-item-editArticle",
    },
]);
let searchData = ref("");
/* 搜索 */
function search() {
    if (searchData.value == null || searchData.value.trim() == "") {
        searchData.value = "";
    }
    let query = router.currentRoute.value.query;
    if (router.currentRoute.value.path.match(/articleView/) != null) {
        router.push({
            path: "/articleView",
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
            path: "/articleView",
            query: {
                page: 1,
                search: searchData.value.trim(),
                classify: "",
            },
        });
    }
}
/* 样式判断 */
function judgeStyle(cla) {
    if (cla != "nav-item-editArticle") {
        return cla + " nav-item";
    } else {
        return cla;
    }
}
function back(item) {
    if (item.path == "" && router.currentRoute != "/articleView") router.go(-1);
}
/* 高亮当前导航 */
function currRoute(item) {
    if (
        item.path.match(/articleView/) != null &&
        router.currentRoute.value.fullPath.match(/articleView/) &&
        !item.classify &&
        router.currentRoute.value.fullPath.match(/classify/) == null
    ) {
        return true;
    }
    if (
        item.path.match(/articleView/) != null &&
        router.currentRoute.value.fullPath.match(/classify/) &&
        item.classify
    ) {
        return true;
    }
    if (item.path == router.currentRoute.value.fullPath) {
        if (!item.path.includes("/article/editArticle")) {
            return true;
        }
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
    if (to.path.includes("/article/editArticle/editArticle/")) {
        let ele = document.getElementsByClassName("nav-item-editArticle");
        ele[0].innerHTML = "加载中";
        ele[0].style["content"] = "";
    }
});
onBeforeRouteUpdate((to) => {
    if (to.fullPath === "/") {
        router.push({
            path: "/articleView",
            query: {
                page: 1,
            },
        });
    }
});
onMounted(() => {
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
.nav-item-home::before {
    content: "\e6fe";
    font-family: "iconfont";
    margin-right: 4px;
}
.nav-item-image::before {
    content: "\e6c5";
    font-family: "iconfont";
    margin-right: 4px;
}
.nav-item-article::before {
    content: "\e6db";
    font-family: "iconfont";
    margin-right: 4px;
}
.nav-item-word::before {
    content: "\e64e";
    font-family: "iconfont";
    margin-right: 4px;
}
.nav-item-back::before {
    content: "\e623";
    font-family: "iconfont";
    margin-right: 4px;
}
.nav-item-classify::before {
    content: "\e6da";
    font-family: "iconfont";
    margin-right: 4px;
}

.nav-item-editArticle {
    display: block;
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
