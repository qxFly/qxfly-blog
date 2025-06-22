<template>
    <div class="article-main">
        <div class="header">
            <div class="article-main-label"></div>
            <div class="article-select-bar">
                <div class="article-deletion delete" v-if="batchDeletionFlag">
                    <div class="batch-deletion-confirm" @click="batchDeletionConfirm">删除</div>
                </div>
                <div class="article-deletion" id="article-deletion" v-if="loginUid != '' && uid == loginUid">
                    <div
                        class="batch-deletion"
                        @click="batchDeletion"
                        v-text="batchDeletionFlag ? '取消' : '批量删除'"></div>
                </div>
                <MySelect class="article-sort-select" :sort-label="verifyLabel">
                    <div @click="verify(1)">审核中</div>
                    <div @click="verify(3)">审核通过</div>
                    <div @click="verify(2)">审核未通过</div>
                </MySelect>
                <MySelect class="article-sort-select" :sort-label="sortLabel">
                    <div @click="sort('new')">最新发布</div>
                    <div @click="sort('hot')">浏览最多</div>
                    <div @click="sort('likes')">点赞最多</div>
                </MySelect>
            </div>
        </div>
        <div class="articles">
            <div class="articles-item" v-for="(article, index) in articles" :key="index">
                <input
                    v-if="batchDeletionFlag"
                    class="delete-articles-checkbox"
                    type="checkbox"
                    @change="deleteArticles($event, article.id)" />
                <ArticleCard :article="article" :index="index"></ArticleCard>
                <!-- <a @click="ArticleDetail(article.id, article.authorId, article.author, article.cover)" class="article-item-a">
                    <div class="item-cover" :class="{ right: item % 2 == 0 }">
                        <div class="noPassedTipHide" :class="{ noPassedTip: showVerifyTip }">
                            {{ article.reason }}
                        </div>
                        <img :src="article.cover" alt="" />
                    </div>
                    <div class="item-info">
                        <div class="item-title">{{ article.title }}</div>
                        <div class="item-time">
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
                            <div v-if="article.updateTime != article.createTime">
                                <span class="label">更新于:</span>
                                <span class="date">{{ adjustDate(article.updateTime) }}</span>
                                <span class="time">{{ adjustTime(article.updateTime) }}</span>
                            </div>
                            <div v-if="article.updateTime == article.createTime">
                                <span class="label">发布于:</span>
                                <span class="date">{{ adjustDate(article.createTime) }}</span>
                                <span class="time">{{ adjustTime(article.createTime) }}</span>
                            </div>
                        </div>
                        <div class="item-content">
                            <div class="content">{{ article.preview }}</div>
                        </div>
                        <div class="item-actions" v-if="article.createTime != null">
                            <div class="item-action-item item-action-views">
                                <span class="item-action-label" v-if="isDisPlayActionLabel()">浏览:</span
                                >{{ article.views }}
                            </div>
                            <div class="item-action-item item-action-likes">
                                <span class="item-action-label" v-if="isDisPlayActionLabel()">点赞:</span
                                >{{ article.likes }}
                            </div>
                            <div
                                class="item-action-item item-action-classify"
                                v-if="article.classify != null && article.classify != ''">
                                <span class="item-action-label" v-if="isDisPlayActionLabel()">分类:</span
                                >{{ article.classify }}
                            </div>
                            <div class="item-action-item item-action-tags" v-if="article.tag != null && article.tag != ''">
                                <span class="item-action-label" v-if="isDisPlayActionLabel()">标签:</span>
                                {{ article.tag }}
                            </div>
                        </div>
                    </div>
                </a> -->
            </div>
            <div class="articles-item" v-if="isBlank">
                <a class="article-item-a">
                    <div class="item-info">
                        <div class="item-title">没有任何文章</div>
                        <div class="item-time">
                            <svg
                                t="1704791330489"
                                class="icon"
                                viewBox="0 0 1024 1024"
                                version="1.1"
                                xmlns="http://www.w3.org/2000/svg"
                                p-id="7432"
                                width="20"
                                height="20">
                                <path
                                    d="M378.857631 467.61921l-88.76158 0 0 88.76158 88.76158 0L378.857631 467.61921zM556.38079 467.61921l-88.76158 0 0 88.76158 88.76158 0L556.38079 467.61921zM733.903949 467.61921l-88.76158 0 0 88.76158 88.76158 0L733.903949 467.61921zM822.665529 156.952658l-44.38079 0 0-88.76158-88.76158 0 0 88.76158L334.475817 156.952658l0-88.76158-88.76158 0 0 88.76158-44.38079 0c-49.262984 0-88.317465 39.942711-88.317465 88.76158l-0.444115 621.332081c0 48.818869 39.498596 88.76158 88.76158 88.76158l621.332081 0c48.818869 0 88.76158-39.942711 88.76158-88.76158L911.427108 245.714238C911.427108 196.895369 871.484398 156.952658 822.665529 156.952658zM822.665529 867.046319 201.333448 867.046319 201.333448 378.857631l621.332081 0L822.665529 867.046319z"
                                    p-id="7433"></path>
                            </svg>
                            <span>updateTime:</span> <span></span>
                        </div>
                        <div class="item-content"></div>
                    </div>
                </a>
            </div>
            <div class="articles-item" v-if="isload">
                <a class="article-item-a">
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
import { listArticles, batchDeleteArticle } from "@/api/Article/index";
import router from "@/router";
import mymd5 from "@/utils/md5";
import { useRoute } from "vue-router";
import Pagination from "@/components/Pagination";
import { ElMessageBox } from "element-plus";
import md5 from "js-md5";
import ArticleCard from "@/components/ArticleCard.vue";
import MySelect from "@/components/MySelect.vue";
let useRouter = useRoute();
/* 分页查询 */
let totalPages = ref(); //总页数
let currPage = ref(1); //当前页
let pageSize = ref(10); //分页大小
let articles = ref([]); //文章
let isBlank = ref(false); //是否为空
let isload = ref(true); //是否加载
let searchData = ref(""); //搜索数据
let uid = ref(""); //用户id
let loginUid = ref(localStorage.getItem("uid")); //登录用户id
let showVerifyTip = ref(false); //是否显示审核提示
function GetArticles() {
    listArticles(
        currPage.value,
        pageSize.value,
        searchData.value,
        sortValue.value,
        uid.value,
        false,
        verifyValue.value
    ).then((res) => {
        if ((res.data.code = 1)) {
            isload.value = false;
            let a = res.data.data;
            if (a.list.length != 0) {
                articles.value = a.list;
                totalPages.value = a.pages;
                isBlank.value = false;
                if (verifyValue.value == 2) showVerifyTip.value = true;
                else {
                    showVerifyTip.value = false;
                }
                Backtop();
            } else {
                articles.value = [];
                totalPages.value = 0;
                isBlank.value = true;
            }
        }
    });
}
/* 排序 */
let sortValue = ref("new");
let sortLabel = ref("最新发布");
function sort(value) {
    if (value == "new") sortLabel.value = "最新发布";
    if (value == "hot") sortLabel.value = "浏览最多";
    if (value == "likes") sortLabel.value = "点赞最多";
    sortValue.value = value;
    router.push({
        path: "/user/space/userArticle",
        query: {
            page: 1,
            search: searchData.value,
            uid: uid.value,
            sort: value,
            verify: verifyValue.value,
        },
    });
}
/* 审核筛选 */
let verifyValue = ref(3);
let verifyLabel = ref("审核通过");
function verify(value) {
    if (value == 3) verifyLabel.value = "审核通过";
    if (value == 2) verifyLabel.value = "审核未通过";
    if (value == 1) verifyLabel.value = "审核中";
    verifyValue.value = value;
    router.push({
        path: "/user/space/userArticle",
        query: {
            page: 1,
            search: searchData.value,
            uid: uid.value,
            sort: sortValue.value,
            verify: value,
        },
    });
}
/* 批量删除文章 */
let batchDeletionFlag = ref(false);
/* 显示批量删除按钮 */
function batchDeletion() {
    batchDeletionFlag.value = !batchDeletionFlag.value;
    let w = document.getElementById("article-deletion");
    if (batchDeletionFlag.value) {
        w.style.width = "46px";
    } else {
        w.style.width = "90px";
    }
}
/* 添加批量删除列表 */
let deleteArticlesList = ref([]);
let deleteArticles = (e, v) => {
    if (e.target.checked) {
        deleteArticlesList.value.push(v);
    } else {
        deleteArticlesList.value = deleteArticlesList.value.filter((item) => item != v);
    }
};
/* 确认批量删除 */
function batchDeletionConfirm() {
    ElMessageBox.confirm("是否确认删除?", "提示", {
        confirmButtonText: "确认",
        cancelButtonText: "取消",
        type: "warning",
    })
        .then((res) => {
            if (deleteArticlesList.value.length > 0) {
                batchDeleteArticle({ content: deleteArticlesList.value.toString() }).then((res) => {
                    if (res.data.code == 1) {
                        GetArticles();
                    }
                });
            }
        })
        .catch((res) => {});
}
/* 展开排序列表 */
let isExtendSort = ref(false);
function extendArticleSort(eleID) {
    let ele = document.getElementById(eleID);
    ele.addEventListener("mouseleave", function () {
        closeSort(eleID);
    });
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
function closeSort(eleID) {
    let ele = document.getElementById(eleID);
    if (ele != null) {
        isExtendSort.value = false;
        ele.style.height = "25px";
        ele.removeEventListener("mouseleave", closeSort);
    }
}
/* 换页操作 */
function changePage(page = 0) {
    if (page != 0) {
        if (page > totalPages.value) {
            page = totalPages.value;
        }
        router.push({
            path: "/user/space/userArticle",
            query: {
                page: page,
                search: searchData.value,
                uid: uid.value,
                sort: sortValue.value,
                verify: verifyValue.value,
            },
        });
    }
}
function Backtop() {
    var timer = null;
    cancelAnimationFrame(timer);

    timer = requestAnimationFrame(function fn() {
        var oTop = document.body.scrollTop || document.documentElement.scrollTop;

        if (oTop > 0) {
            document.body.scrollTop = document.documentElement.scrollTop = oTop - 100;
            timer = requestAnimationFrame(fn);
        } else {
            cancelAnimationFrame(timer);
        }
    });
}
/* 路由分页 */
let routePage = ref(useRouter);
function restoreRoutePage() {
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
}
watch(
    routePage.value,
    () => {
        restoreRoutePage();
        GetArticles();
    },
    {
        deep: true,
    }
);
onMounted(() => {
    searchData.value = useRouter.query.search;
    let tuid = useRouter.query.uid;
    if (!isNaN(parseFloat(tuid)) && isFinite(tuid)) {
        uid.value = tuid;
    } else {
        router.replace("/");
        return;
    }

    if (useRouter.query.sort != null) {
        if (useRouter.query.sort == "new") sortLabel.value = "最新发布";
        if (useRouter.query.sort == "hot") sortLabel.value = "浏览最多";
        if (useRouter.query.sort == "likes") sortLabel.value = "点赞最多";
        sortValue.value = useRouter.query.sort;
    }
    if (useRouter.query.verify != null) {
        if (useRouter.query.verify == 3) verifyLabel.value = "审核通过";
        if (useRouter.query.verify == 2) verifyLabel.value = "审核未通过";
        if (useRouter.query.verify == 1) verifyLabel.value = "审核中";
        verifyValue.value = useRouter.query.verify;
    }
    restoreRoutePage();
    GetArticles();
});
</script>

<style scoped lang="less">
.header {
    display: flex;
    justify-content: space-between;
    // height: 100px;
}
.article-sort-select {
    position: relative;
    top: -20px;
    width: 90px;
    text-align: center;
}
.article-deletion {
    position: relative;
    top: -20px;
    width: 90px;
    // background-color: #fff;
    text-align: center;
}
.article-deletion.delete {
    width: 46px;
}
.batch-deletion,
.batch-deletion-confirm {
    position: absolute;
    height: 26px;
    width: 100%;
    overflow: hidden;
    border-radius: 4px;
    border: 1px solid;
    background-color: var(--main-background-color);
    z-index: 99;
    transition: all 0.2s ease;
    user-select: none;
}

.batch-deletion:hover {
    background-color: #49b1f5;
    color: #fff;
}
.batch-deletion-confirm:hover {
    background-color: var(--main-theme-color-orange);
    border: 1px solid var(--main-theme-color-orange);
    color: #fff;
}
.article-main-label {
    font-weight: 700;
    font-size: 26px;
    margin: 0 0 30px 0;
    width: 500px;
    border-bottom: 1px solid #000;
}
.article-select-bar {
    display: flex;
    gap: 4px;
}
.article-main {
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
