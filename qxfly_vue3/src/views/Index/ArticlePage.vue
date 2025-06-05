<template>
    <ArticleClassify :total="totalArticleCount" @classify="Classifys" @tag="Tags" v-if="isClassify"></ArticleClassify>
    <div class="article-main">
        <div class="header">
            <div class="article-main-label">{{ isClassify ? "" : "文章" }}</div>
            <MySelect :sortLabel="sortLabel">
                <div value="new" @click="sort('new')">最新发布</div>
                <div value="hot" @click="sort('hot')">浏览最多</div>
                <div value="likes" @click="sort('likes')">点赞最多</div>
            </MySelect>
        </div>
        <div class="articles">
            <div class="articles-item" v-for="(article, index) in articles" :key="index">
                <ArticleCard :article="article" :index="index" :isload="isload"></ArticleCard>
            </div>
        </div>
        <!-- 分页-->
        <pagination v-model="currPage" :totalPages="totalPages" @changePage="changePage"></pagination>
    </div>
</template>

<script setup>
import { onMounted, ref, watch } from "vue";
import { listArticles } from "@/api/Article/index";
import router from "@/router";
import { useRoute } from "vue-router";
import md5 from "js-md5";
import mymd5 from "@/utils/md5.js";
import ArticleClassify from "@/views/Index/ArticleClassify.vue";
import pagination from "@/components/Pagination";
import ArticleCard from "@/components/ArticleCard.vue";
import MySelect from "@/components/MySelect.vue";
let indexPath = process.env.VUE_APP_INDEX_PATH;
let useRouter = useRoute();
let isload = ref(true);
/* 分页查询 */
let totalPages = ref(1); //总分页数
let currPage = ref(1); //当前页
let pageSize = ref(15); //分页大小
let articles = ref([]);
let searchData = ref("");
let isClassify = ref(false);
let totalArticleCount = ref(0);
async function ListArticles() {
    await listArticles(
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
        if (res.data.code == 1) {
            let resData = res.data.data;
            if (resData.list != "") {
                articles.value = res.data.data.list;
                // Backtop();
            } else {
                articles.value = [
                    {
                        title: "暂无文章",
                        cover: "",
                    },
                ];
            }

            totalPages.value = resData.pages;
            totalArticleCount.value = resData.total;
            isload.value = false;
        } else {
            isload.value = false;
            articles.value = [
                {
                    title: "暂无文章",
                    cover: "",
                },
            ];
        }
    });
}
/* 分类 */
let classify = ref("");
let tags = ref([]);
function Classifys(value) {
    classify.value = value;
    router.push({
        path: "/index/articleClassify",
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
        path: "/index/articleClassify",
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
    let backTop = document.getElementById("backTop");
    if (backTop != null) backTop.click();
}
/* 排序 */
let sortValue = ref("new");
let sortLabel = ref("最新发布");
function sort(value) {
    if (value == "new") sortLabel.value = "最新发布";
    if (value == "hot") sortLabel.value = "浏览最多";
    if (value == "likes") sortLabel.value = "点赞最多";
    sortValue.value = value;
    let params = {
        page: 1,
        search: searchData.value,
        sort: sortValue.value,
        tag: tags.value.toString(),
    };
    let currRouterIsClassify = router.currentRoute.value.path.includes("Classify");
    if (classify.value != "" || currRouterIsClassify) {
        params.classify = classify.value;
    }
    if (currRouterIsClassify) {
        router.push({
            path: "/index/articleClassify",
            query: params,
        });
    } else {
        router.push({
            path: indexPath,
            query: params,
        });
    }
}
/* 展开排序列表 */
let isExtendSort = ref(false);
function extendArticleSort() {
    let ele = document.getElementById("article-sort");
    ele.addEventListener("mouseleave", closeSort);
    if (ele != null) {
        if (isExtendSort.value) {
            ele.style.height = "25px";
        } else {
            ele.style.height = "99px";
        }
        isExtendSort.value = !isExtendSort.value;
    }
}
/* 关闭排序列表 */
function closeSort() {
    let ele = document.getElementById("article-sort");
    if (ele != null) {
        isExtendSort.value = false;
        ele.style.height = "25px";
        ele.removeEventListener("mouseleave", closeSort);
    }
}
/* 换页操作 */
function changePage(page = 0) {
    if (totalPages.value != 0 && page != 0) {
        if (page > totalPages.value) {
            page = totalPages.value;
        }
        let params = {
            page: page,
            search: searchData.value,
            sort: sortValue.value,
            tag: tags.value.toString(),
        };
        let currRouterIsClassify = router.currentRoute.value.path.includes("Classify");
        if (classify.value != "" || currRouterIsClassify) {
            params.classify = classify.value;
        }
        if (currRouterIsClassify) {
            router.push({
                path: "/index/articleClassify",
                query: params,
            });
        } else {
            router.push({
                path: indexPath,
                query: params,
            });
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
        ListArticles();
    },
    {
        deep: true,
    }
);
/* 是否显示分类 */
function showClassify() {
    if (router.currentRoute.value.path.includes("Classify")) {
        isClassify.value = true;
    } else {
        isClassify.value = false;
        classify.value = "";
        tags.value = [];
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
    ListArticles();
});
</script>

<style scoped lang="less">
.article-page-cardview {
    padding: 40px 30px;
}
.header {
    display: flex;
    justify-content: space-between;
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
.articles-item {
    height: 230px;
    margin-bottom: 20px;
    padding: 0;
    overflow: hidden;
    border-radius: 8px 4px 4px 8px;
    background-color: var(--main-background-color);
    box-shadow: 0 3px 8px 6px rgba(7, 17, 27, 0.08);
    transition: all ease 0.2s;
}
.articles-item:hover {
    box-shadow: 0 3px 8px 6px rgba(7, 17, 27, 0.14);
    .item-cover img {
        transform: scale(1.1);
    }
}
.article-item-a {
    display: flex;
    width: 100%;
    height: 230px;
    justify-content: center;
    align-items: center;
    color: black;
    cursor: pointer;
}
.item-title {
    font-size: 24px;
    font-weight: 700;
    margin-bottom: 10px;
    text-overflow: ellipsis;
    white-space: nowrap;
    overflow: hidden;
}
</style>
