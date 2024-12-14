<template>
    <el-container class="main-container">
        <el-header class="header">
            <div class="search-bar">
                <div class="search-items">
                    <el-input
                        class="search-item"
                        v-model="comId"
                        placeholder="评论ID"
                        clearable
                        style="width: 160px"></el-input>
                    <el-input
                        class="search-item"
                        v-model="parentCommentId"
                        placeholder="父ID"
                        clearable
                        style="width: 160px"></el-input>
                    <el-input
                        class="search-item"
                        v-model="articleId"
                        placeholder="文章ID"
                        clearable
                        style="width: 160px"></el-input>
                    <el-input
                        class="search-item"
                        v-model="content"
                        placeholder="评论内容"
                        clearable
                        style="width: 160px"></el-input>

                    <el-input
                        class="search-item"
                        v-model="userId"
                        placeholder="用户ID"
                        clearable
                        style="width: 160px"></el-input>
                    <el-input
                        class="search-item"
                        v-model="username"
                        placeholder="用户名"
                        clearable
                        style="width: 160px"></el-input>
                    <el-date-picker
                        :default-time="[new Date(2000, 1, 1, 0, 0, 0), new Date(2000, 1, 1, 23, 59, 59)]"
                        value-format="YYYY-MM-DD[T]HH:mm:ss.sss[Z]"
                        v-model="createTime"
                        start-placeholder="起始评论日期"
                        end-placeholder="截止评论日期"
                        type="daterange"
                        unlink-panels
                        style="max-width: 300px" />

                    <el-button type="primary" @click="toSearch">筛选</el-button>
                    <el-button @click="clearAll" style="margin: 0">清空筛选</el-button>
                </div>
            </div>
            <div class="menu">
                <el-menu :default-active="activeIndex" class="el-menu" mode="horizontal" @select="handleSelect">
                    <el-menu-item index="1" @click="toListArticle(0)">全部</el-menu-item>
                    <el-sub-menu index="2">
                        <template #title>已审核</template>
                        <el-menu-item index="2-1" @click="toListArticle(3)">通过</el-menu-item>
                        <el-menu-item index="2-2" @click="toListArticle(2)">未通过</el-menu-item>
                    </el-sub-menu>
                    <el-menu-item index="3" @click="toListArticle(1)">未审核</el-menu-item>
                </el-menu>
            </div>
        </el-header>
        <el-main class="main">
            <div>
                <el-table
                    v-loading="loading"
                    :data="comments"
                    :border="true"
                    show-overflow-tooltip
                    stripe
                    :highlight-current-row="true"
                    style="width: 100%"
                    :max-height="setTableHeight()">
                    <el-table-column fixed prop="id" label="ID" width="80" align="center" />
                    <el-table-column prop="parentCommentId" label="父ID" width="80" align="center" />
                    <el-table-column prop="" label="用户" width="160" align="center">
                        <template #default="scope">
                            {{ scope.row.user.username }}
                        </template>
                    </el-table-column>
                    <el-table-column prop="" label="用户ID" width="80" align="center">
                        <template #default="scope">
                            {{ scope.row.user.id }}
                        </template>
                    </el-table-column>
                    <el-table-column prop="content" min-width="300" label="内容" align="center" />
                    <el-table-column prop="createTime" label="评论时间" width="170" align="center">
                        <template #default="scope">
                            {{ adjustTime(scope.row.createTime) }}
                        </template>
                    </el-table-column>
                    <el-table-column prop="articleId" label="文章ID" width="80" align="center" />
                    <el-table-column prop="" label="查看原文章" width="110" align="center">
                        <template #default="scope">
                            <el-button size="small" plain @click="toArticle(scope.row)"> 查看原文章 </el-button>
                        </template>
                    </el-table-column>
                    <el-table-column prop="" label="审核" width="130" align="center">
                        <template #default="scope">
                            <el-button type="primary" size="small" plain @click="verify(scope.row)"> 审核 </el-button>
                            <el-tag type="warning" v-if="scope.row.verify == 1">未审核</el-tag>
                            <el-tag type="danger" v-if="scope.row.verify == 2">未通过 </el-tag>
                            <el-tag type="success" v-if="scope.row.verify == 3">已通过 </el-tag>
                        </template>
                    </el-table-column>
                    <el-table-column prop="" label="操作" width="80" align="center">
                        <template #default="scope">
                            <el-button type="danger" size="small" plain @click="DeleteComment(scope.row.id)">
                                删除
                            </el-button>
                        </template>
                    </el-table-column>
                </el-table>
            </div>
            <div class="pagination">
                <el-pagination
                    v-model:current-page="currPage"
                    :page-size="pageSize"
                    :pager-count="11"
                    background
                    layout="total, prev, pager, next, jumper"
                    :total="totalPages"
                    @current-change="currentChange" />
            </div>
        </el-main>
    </el-container>
</template>
<script setup>
import { articleCommentVerify, deleteArticleComment, listArticleComment } from "@/api/Admin";
import { ref, onMounted, watch } from "vue";
import router from "@/router";
import { useRoute } from "vue-router";
import md5 from "js-md5";
import mymd5 from "@/utils/md5.js";
import { ElMessage, ElMessageBox } from "element-plus";
let useRouter = useRoute();
const activeIndex = ref("1");
const handleSelect = (key, keyPath) => {
    activeIndex.value = key;
};
let comments = ref([]);
let loading = ref(true);
/* 分页查询 */
let totalPages = ref(1); //总页数
let currPage = ref(1); //当前页
let pageSize = ref(15); //分页大小
function toListArticle(verify) {
    currPage.value = 1;
    VERIFY.value = verify;
    router.push({
        path: "/manage/articleComment",
        query: {
            activeIndex: activeIndex.value,
            verify: VERIFY.value,
            currPage: currPage.value,
            pageSize: pageSize.value,
            comId: comId.value,
            articleId: parseInt(articleId.value),
            parentCommentId: parseInt(parentCommentId.value),
            content: content.value,
            username: username.value,
            userId: parseInt(userId.value),
            createTimeStart: createTimeStart.value,
            createTimeEnd: createTimeEnd.value,
        },
    });
}
/* 搜索 */
let comId = ref(null);
let articleId = ref(null);
let createTime = ref(null);
let createTimeStart = ref(null);
let createTimeEnd = ref(null);
let VERIFY = ref(0);
let content = ref(null);
let username = ref(null);
let userId = ref(null);
let parentCommentId = ref(null);
/* 搜索筛选 */
/* 搜索筛选 */
function toSearch() {
    currPage.value = 1;
    search();
}
function search() {
    loading.value = true;
    if (createTime.value != null) {
        createTimeStart.value = new Date(createTime.value[0]);
        createTimeEnd.value = new Date(createTime.value[1]);
    } else {
        createTimeStart.value = null;
        createTimeEnd.value = null;
    }
    listArticleComment({
        verify: VERIFY.value,
        currPage: currPage.value,
        pageSize: pageSize.value,
        comId: parseInt(comId.value),
        articleId: parseInt(articleId.value),
        parentCommentId: parseInt(parentCommentId.value),
        content: content.value,
        username: username.value,
        userId: parseInt(userId.value),
        createTimeStart: createTimeStart.value,
        createTimeEnd: createTimeEnd.value,
    }).then((res) => {
        if (res.data.code == 1) {
            loading.value = false;
            comments.value = res.data.data.list;
            totalPages.value = res.data.data.total;
        }
    });
}
/* 审核评论 */
async function verify(com) {
    let v = 1;
    await ElMessageBox.confirm(com.content, {
        distinguishCancelAndClose: true,
        confirmButtonText: "通过",
        cancelButtonText: "不通过",
        type: "warning",
        lockScroll: false,
        center: true,
    })
        .then(() => {
            v = 3;
        })
        .catch((action) => {
            if (action == "cancel") v = 2;
        });
    if (v != 1)
        articleCommentVerify({ id: com.id, verify: v }).then((res) => {
            if (res.data.code == 1) {
                ElMessage.success("审核完成");
                search();
            } else {
                ElMessage.error("审核失败");
            }
        });
}
/* 跳转原文章 */
function toArticle(art) {
    router.push({
        name: "ArticleDetail",
        params: { id: art.articleId, editMode: mymd5.generateMD5BySalt(false), salt: mymd5.getSalt(), cover: "cover" },
    });
}
/* 删除评论 */
async function DeleteComment(commentId) {
    await ElMessageBox.confirm("您确定删除该评论？", {
        confirmButtonText: "删除",
        cancelButtonText: "取消",
        type: "error",
        lockScroll: false,
        center: true,
    })
        .then(() => {
            deleteArticleComment({ id: commentId }).then((res) => {
                if (res.data.code == 1) {
                    ElMessage.success("删除成功");
                    search();
                } else {
                    ElMessage.error("删除失败");
                }
            });
        })
        .catch(() => {});
}
/* 换页操作 */
let currentChange = (page) => {
    currPage.value = page;
    router.push({
        path: "/manage/articleComment",
        query: {
            activeIndex: activeIndex.value,
            verify: VERIFY.value,
            currPage: currPage.value,
            pageSize: pageSize.value,
            comId: comId.value,
            articleId: parseInt(articleId.value),
            parentCommentId: parseInt(parentCommentId.value),
            content: content.value,
            username: username.value,
            userId: parseInt(userId.value),
            createTimeStart: createTimeStart.value,
            createTimeEnd: createTimeEnd.value,
        },
    });
};
/* 清空搜索 */
function clearAll() {
    comId.value = null;
    articleId.value = null;
    content.value = null;
    username.value = null;
    userId.value = null;
    createTime.value = null;
    createTimeStart.value = null;
    createTimeEnd.value = null;
    currPage.value = 1;
    parentCommentId.value = null;
    search();
}
/* 路由分页 */
let routePage = ref(useRouter);
watch(
    routePage.value,
    (newVal, oldVal) => {
        search();
    },
    {
        deep: true,
    }
);
/* 调整时间格式 */
function adjustTime(time) {
    if (time != null) return time.split("T")[0] + " " + time.split("T")[1].split(".")[0];
    else return "";
}
/* 设置表格高度 */
function setTableHeight() {
    let height = document.body.scrollHeight - 250;
    return height;
}
onMounted(() => {
    search();
});
</script>
<style lang="less" scoped>
.main-container {
    margin-top: 20px;
}
.search-items {
    display: flex;
    gap: 10px;
    margin: 4px;
}
.search-item {
    min-width: 50px;
}
.pagination {
    display: flex;
    justify-content: center;
    align-items: center;
    margin-top: 20px;
}
.header {
    // display: block;
    // height: 100px;
    height: max-content;
}
.menu {
}
.el-menu {
}
.main {
    padding-top: 0;
}
</style>
