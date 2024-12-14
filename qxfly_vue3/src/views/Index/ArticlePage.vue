<template>
    <ArticleClassify :total="totalArticleCount" @classify="Classifys" @tag="Tags" v-if="isClassify"></ArticleClassify>
    <div class="article-main">
        <div class="header">
            <div class="article-main-label">{{ isClassify ? "" : "文章" }}</div>
            <div class="article-sort-select">
                <div class="article-sort">
                    <div class="article-sort-label" v-text="sortLabel"></div>
                    <div class="article-sort-item" value="new" @click="sort('new')">最新发布</div>
                    <div class="article-sort-item" value="hot" @click="sort('hot')">浏览最多</div>
                    <div class="article-sort-item" value="likes" @click="sort('likes')">点赞最多</div>
                </div>
            </div>
        </div>

        <div class="posts">
            <div class="posts-item" v-for="(post, item) in posts" :key="post">
                <a @click="ArticleDetail(post)" class="post-item-a">
                    <div class="article-detail-load" :id="'article-detail-load-' + post.id">加 载 中...</div>
                    <div
                        class="item-cover"
                        :class="{ right: item % 2 == 0 }"
                        :id="'item-cover-' + post.id"
                        v-if="post.cover != ''">
                        <img :src="post.cover" alt="" />
                    </div>
                    <div class="item-info" :id="'item-info-' + post.id">
                        <div class="item-title" :class="{ itemTitleCenter: post.title == '暂无文章' }">
                            {{ post.title }}
                        </div>
                        <div class="item-time" v-if="post.createTime != null">
                            <svg
                                style="margin-right: 4px"
                                t="1704791330489"
                                class="icon"
                                viewBox="0 0 1024 1024"
                                version="1.1"
                                xmlns="http://www.w3.org/2000/svg"
                                p-id="7432"
                                width="16"
                                height="16">
                                <path
                                    d="M378.857631 467.61921l-88.76158 0 0 88.76158 88.76158 0L378.857631 467.61921zM556.38079 467.61921l-88.76158 0 0 88.76158 88.76158 0L556.38079 467.61921zM733.903949 467.61921l-88.76158 0 0 88.76158 88.76158 0L733.903949 467.61921zM822.665529 156.952658l-44.38079 0 0-88.76158-88.76158 0 0 88.76158L334.475817 156.952658l0-88.76158-88.76158 0 0 88.76158-44.38079 0c-49.262984 0-88.317465 39.942711-88.317465 88.76158l-0.444115 621.332081c0 48.818869 39.498596 88.76158 88.76158 88.76158l621.332081 0c48.818869 0 88.76158-39.942711 88.76158-88.76158L911.427108 245.714238C911.427108 196.895369 871.484398 156.952658 822.665529 156.952658zM822.665529 867.046319 201.333448 867.046319 201.333448 378.857631l621.332081 0L822.665529 867.046319z"
                                    p-id="7433"></path>
                            </svg>
                            <div v-if="post.updateTime != post.createTime">
                                <span class="label">更新于:</span>
                                <span class="date">{{ adjustDate(post.updateTime) }}</span>
                                <span class="time">{{ adjustTime(post.updateTime) }}</span>
                            </div>
                            <div v-if="post.updateTime == post.createTime">
                                <span class="label">发布于:</span>
                                <span class="date">{{ adjustDate(post.createTime) }}</span>
                                <span class="time">{{ adjustTime(post.createTime) }}</span>
                            </div>
                        </div>
                        <!-- <hr /> -->
                        <div class="item-content">
                            <div class="content">
                                {{ post.preview }}
                            </div>
                        </div>
                        <div class="item-actions" v-if="post.createTime != null">
                            <div class="item-action-item item-action-author" @click.stop="toUserSpace(post.authorId)">
                                <span class="item-action-label" v-if="isDisPlayActionLabel()">作者:</span>
                                {{ post.author }}
                            </div>
                            <div class="item-action-item item-action-views">
                                <span class="item-action-label" v-if="isDisPlayActionLabel()">浏览:</span
                                >{{ post.views }}
                            </div>
                            <div class="item-action-item item-action-likes">
                                <span class="item-action-label" v-if="isDisPlayActionLabel()">点赞:</span
                                >{{ post.likes }}
                            </div>
                            <div
                                class="item-action-item item-action-classify"
                                v-if="post.classify != null && post.classify != ''">
                                <span class="item-action-label" v-if="isDisPlayActionLabel()">分类:</span
                                >{{ post.classify }}
                            </div>
                            <div class="item-action-item item-action-tags" v-if="post.tag != null && post.tag != ''">
                                <span class="item-action-label" v-if="isDisPlayActionLabel()">标签:</span>
                                {{ post.tag }}
                            </div>
                        </div>
                    </div>
                </a>
            </div>
            <div class="posts-item" v-if="isload">
                <a class="post-item-a">
                    <div class="item-title" style="text-align: center">加 载 中...</div>
                </a>
            </div>
            <div class="posts-item" v-if="isload">
                <a class="post-item-a">
                    <div class="item-title" style="text-align: center">加 载 中...</div>
                </a>
            </div>
            <div class="posts-item" v-if="isload">
                <a class="post-item-a">
                    <div class="item-title" style="text-align: center">加 载 中...</div>
                </a>
            </div>
            <div class="posts-item" v-if="isload">
                <a class="post-item-a">
                    <div class="item-title" style="text-align: center">加 载 中...</div>
                </a>
            </div>
            <div class="posts-item" v-if="isload">
                <a class="post-item-a">
                    <div class="item-title" style="text-align: center">加 载 中...</div>
                </a>
            </div>
        </div>
        <!-- 分页-->
        <pagination v-model="currPage" :totalPages="totalPages" @changePage="changePage"></pagination>
    </div>
</template>

<script setup>
import { onMounted, ref, watch } from "vue";
import { getArticles } from "@/api/Article/index";
import router from "@/router";
import { useRoute } from "vue-router";
import md5 from "js-md5";
import mymd5 from "@/utils/md5.js";
import ArticleClassify from "@/views/Index/ArticleClassify.vue";
import Pagination from "@/components/Pagination";
let useRouter = useRoute();
let isload = ref(true);
/* 分页查询 */
let totalPages = ref(1); //总分页数
let currPage = ref(1); //当前页
let pageSize = ref(10); //分页大小
let posts = ref([]);
let searchData = ref("");
let isClassify = ref(false);
let totalArticleCount = ref(0);
async function GetArticles() {
    await getArticles(
        currPage.value,
        pageSize.value,
        searchData.value,
        sortValue.value,
        0,
        false,
        3,
        classify.value.toString(),
        tags.value.toString()
    ).then((res) => {
        if ((res.data.code = 1)) {
            if (res.data.data.data != "") {
                posts.value = res.data.data.data;

                Backtop();
            } else {
                posts.value = [
                    {
                        title: "暂无文章",
                        cover: "",
                    },
                ];
            }

            totalPages.value = res.data.data.totalPage;
            totalArticleCount.value = res.data.data.totalRecord;
            isload.value = false;
        } else {
            isload.value = false;
            posts.value = [
                {
                    title: "暂无文章",
                    cover: "",
                },
            ];
        }
    });
}
/* 查看文章 */
let a = {
    b: md5("editMode"),
};
function ArticleDetail(post) {
    var id = post.id;
    if (id == null) return;
    document.getElementById("article-detail-load-" + id).style.display = "block";
    document.getElementById("item-cover-" + id).style.display = "none";
    document.getElementById("item-info-" + id).style.display = "none";
    router.push({
        name: "ArticleDetail",
        params: { id: id, editMode: mymd5.generateMD5BySalt(false), salt: mymd5.getSalt(), cover: post.cover },
    });
}
/* 跳转用户空间 */
function toUserSpace(id) {
    router.push({
        path: "/user/space/userArticle",
        query: { page: 1, uid: id },
    });
}
/* 调整时间格式 */
function adjustDate(time) {
    if (time != null) return time.split("T")[0];
    else return "";
}
function adjustTime(time) {
    if (time != null) {
        let t = time.split("T")[1].split(".")[0];
        let h = t.split(":")[0];
        let m = t.split(":")[1];
        let s;
        if (6 < h && h < 12) s = "上午";
        else if (12 < h && h < 18) s = "下午";
        else s = "晚上";
        return s + " " + h + ":" + m;
    } else return "";
}
/* 分类 */
let classify = ref("");
let tags = ref([]);
function Classifys(value) {
    classify.value = value;
    router.push({
        path: "/articleView",
        query: {
            page: 1,
            search: searchData.value,
            sort: sortValue.value,
            classify: classify.value,
            tag: tags.value.toString(),
        },
    });
}

function Tags(value) {
    tags.value = value;
    router.push({
        path: "/articleView",
        query: {
            page: 1,
            search: searchData.value,
            sort: sortValue.value,
            classify: classify.value,
            tag: tags.value.toString(),
        },
    });
}
/* 每次进入界面回到顶部 */
function Backtop() {
    document.body.scrollTop = document.documentElement.scrollTop = 0;
}
/* 排序 */
let sortValue = ref("new");
let sortLabel = ref("最新发布");
function sort(value) {
    if (value == "new") sortLabel.value = "最新发布";
    if (value == "hot") sortLabel.value = "浏览最多";
    if (value == "likes") sortLabel.value = "点赞最多";
    sortValue.value = value;
    if (classify.value != "" || router.currentRoute.value.fullPath.includes("classify")) {
        router.push({
            path: "/articleView",
            query: {
                page: 1,
                search: searchData.value,
                sort: value,
                classify: classify.value,
                tag: tags.value.toString(),
            },
        });
    } else {
        router.push({
            path: "/articleView",
            query: {
                page: 1,
                search: searchData.value,
                sort: value,
                tag: tags.value.toString(),
            },
        });
    }
}
/* 换页操作 */
function changePage(page = 0) {
    if (totalPages.value != 0) {
        if (page != 0) {
            if (page > totalPages.value) {
                page = totalPages.value;
            }
            if (classify.value != "" || router.currentRoute.value.fullPath.includes("classify")) {
                router.push({
                    path: "articleView",
                    query: {
                        page: page,
                        search: searchData.value,
                        sort: sortValue.value,
                        classify: classify.value,
                        tag: tags.value.toString(),
                    },
                });
            } else {
                router.push({
                    path: "/articleView",
                    query: {
                        page: page,
                        search: searchData.value,
                        sort: sortValue.value,
                        tag: tags.value.toString(),
                    },
                });
            }
        }
    }
}
/* 路由后恢复分页等数据 */
let routePage = ref(useRouter);
function setRoutePage() {
    if (useRouter.query.page == null) {
        currPage.value = 1;
    } else {
        currPage.value = parseInt(useRouter.query.page);
    }

    if (useRouter.query.search != null && useRouter.query.search.trim() != "") {
        searchData.value = useRouter.query.search;
    } else {
        searchData.value = "";
    }

    if (useRouter.query.classify != null) {
        classify.value = useRouter.query.classify;
    }

    if (useRouter.query.tag != null) {
        tags.value = useRouter.query.tag;
    }
}
watch(
    routePage.value,
    (newVal, oldVal) => {
        showClassify();
        setRoutePage();
        GetArticles();
    },
    {
        deep: true,
    }
);
/* 是否显示分类 */
function showClassify() {
    if (router.currentRoute.value.fullPath.includes("classify")) {
        isClassify.value = true;
    } else {
        isClassify.value = false;
        classify.value = "";
        tags.value = [];
    }
}
function isDisPlayActionLabel() {
    let width = document.body.clientWidth;
    if (width <= 700) {
        return false;
    } else {
        return true;
    }
}
onMounted(() => {
    showClassify();
    searchData.value = useRouter.query.search;
    if (useRouter.query.sort != null) {
        if (useRouter.query.sort == "new") sortLabel.value = "最新发布";
        if (useRouter.query.sort == "hot") sortLabel.value = "浏览最多";
        if (useRouter.query.sort == "likes") sortLabel.value = "点赞最多";
        sortValue.value = useRouter.query.sort;
    }
    setRoutePage();
    GetArticles();
});
</script>

<style scoped lang="less">
.header {
    display: flex;
    justify-content: space-between;
}
.article-sort-select {
    position: relative;
    min-width: 90px;
    text-align: center;
}
.article-sort {
    position: absolute;
    height: 26px;
    width: 100%;
    overflow: hidden;
    border-radius: 4px;
    border: 1px solid #000;
    background-color: #fff;
    z-index: 9;
    transition: all 0.2s ease;
}
.article-sort:hover {
    height: 99px;
}
.article-sort-item {
    font-size: 15px;
    padding: 2px 4px;
    cursor: pointer;
}
.article-sort-label {
    font-size: 15px;
    padding: 2px 4px;
    border-bottom: 1px solid #000;
    cursor: pointer;
}
.article-sort-item:hover {
    background-color: #84c6ff;
}
.article-main-label {
    font-weight: 700;
    font-size: 26px;
    margin: 0 0 30px 0;
    width: 500px;
    border-bottom: 1px solid #000;
    text-overflow: ellipsis;
    white-space: nowrap;
    overflow: hidden;
}
.article-main {
    transition: all 0.3s ease;
}
.posts {
    width: 100%;
}
.content {
    text-indent: 2em;
}
.posts-item {
    height: 230px;
    margin-bottom: 20px;
    padding: 0;
    overflow: hidden;
    border-radius: 8px 4px 4px 8px;
    background-color: rgb(255, 255, 255);
    box-shadow: 0 3px 8px 6px rgba(7, 17, 27, 0.08);
    transition: all ease 0.2s;
}
.posts-item:hover {
    box-shadow: 0 3px 8px 6px rgba(7, 17, 27, 0.14);
    .item-cover img {
        transform: scale(1.1);
    }
}
.article-detail-load {
    font-weight: 700;
    font-size: 24px;
    display: none;
}
.post-item-a {
    display: flex;
    width: 100%;
    height: 230px;
    justify-content: center;
    align-items: center;
    color: black;
    cursor: pointer;
}

.item-cover {
    width: 35%;
    height: 100%;
    overflow: hidden;
}
.item-cover.right {
}
.item-cover img {
    width: 100%;
    height: 100%;
    object-fit: cover;
    transition: all ease 0.5s;
}
.item-info {
    flex: 1;
    margin: 0 10px;
    text-align: left;
    overflow: hidden;
    padding: 10px;
}

.item-title {
    font-size: 24px;
    font-weight: 700;
    margin-bottom: 10px;
    text-overflow: ellipsis;
    white-space: nowrap;
    overflow: hidden;
}
.itemTitleCenter {
    text-align: center;
}
.item-time {
    display: flex;
    align-items: center;
    margin-bottom: 6px;
    padding-bottom: 6px;
    font-size: 13px;
    border-bottom: 1px solid #ddd;
    color: #333;
    text-overflow: ellipsis;
    white-space: nowrap;
    overflow: hidden;
}
.item-info span {
    margin-right: 10px;
}
.item-info .label {
}
.item-info .date {
}
.item-info .time {
    font-weight: 700;
    color: #000;
}
.item-content {
    height: 100px;
}
.content {
    display: -webkit-box;
    -webkit-line-clamp: 4;
    -webkit-box-orient: vertical;
    overflow: hidden;
    line-height: 26px;
}
</style>
<!-- 文章卡片 -->
<style scoped>
.item-actions {
    display: flex;
    margin: 16px 0 0 0;
    font-size: 14px;
    width: max-content;
    border-top: 1px solid #000000;
    z-index: 999;
}

.item-action-author:hover {
}
.item-action-item {
    margin-right: 14px;
    cursor: pointer;
}
.item-action-label {
    /* margin: 0 !important; */
}
.item-action-label-nodisplay {
    display: none;
}
.item-action-likes {
    margin-right: 34px;
}
.item-action-author:hover {
    color: #ff6632;
}
.item-action-author::before {
    content: "\e6e3";
    font-family: "iconfont";
}

.item-action-views::before {
    content: "\e6df";
    font-family: "iconfont";
    margin-right: 4px;
}
.item-action-likes::before {
    content: "\e6f9";
    font-family: "iconfont";
    margin-right: 4px;
}
.item-action-classify::before {
    content: "\e6da";
    font-family: "iconfont";
}
.item-action-tags::before {
    content: "\e6d3";
    font-family: "iconfont";
}
@media (max-width: 600px) {
    .article-main {
        padding: 0;
    }
    .article-main-label {
        margin-left: 10px;
    }
    .item-action-item {
        margin-right: 8px;
        font-size: 12px;
    }
}
</style>
