<template>
    <el-container class="main-container">
        <el-header class="header">
            <div class="search-bar">
                <div class="search-items">
                    <el-input
                        class="search-item"
                        v-model="articleId"
                        placeholder="文章ID"
                        clearable
                        style="width: 160px"></el-input>
                    <el-input
                        class="search-item"
                        v-model="title"
                        placeholder="文章标题"
                        clearable
                        style="width: 160px"></el-input>
                    <el-input
                        class="search-item"
                        v-model="author"
                        placeholder="文章作者"
                        clearable
                        style="width: 160px"></el-input>
                    <el-input
                        class="search-item"
                        v-model="authorId"
                        placeholder="作者ID"
                        clearable
                        style="width: 160px"></el-input>
                    <el-input
                        class="search-item"
                        v-model="tag"
                        placeholder="文章标签"
                        clearable
                        style="width: 160px"></el-input>
                    <el-input
                        class="search-item"
                        v-model="classify"
                        placeholder="文章分类"
                        clearable
                        style="width: 160px"></el-input>
                    <el-date-picker
                        :default-time="[new Date(2000, 1, 1, 0, 0, 0), new Date(2000, 1, 1, 23, 59, 59)]"
                        value-format="YYYY-MM-DD[T]HH:mm:ss.sss[Z]"
                        v-model="createTime"
                        start-placeholder="起始创建日期"
                        end-placeholder="截止创建日期"
                        type="daterange"
                        unlink-panels
                        style="max-width: 300px" />
                    <el-date-picker
                        :default-time="[new Date(2000, 1, 1, 0, 0, 0), new Date(2000, 1, 1, 23, 59, 59)]"
                        v-model="updateTime"
                        value-format="YYYY-MM-DD[T]HH:mm:ss.sss[Z]"
                        start-placeholder="起始更新日期"
                        end-placeholder="截止更新日期"
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
                    :data="articles"
                    :border="true"
                    show-overflow-tooltip
                    stripe
                    :highlight-current-row="true"
                    style="width: 100%"
                    :row-style="rowStyle"
                    :max-height="setTableHeight()">
                    <el-table-column fixed prop="id" label="ID" width="80" align="center" />
                    <el-table-column prop="title" label="标题" width="240" align="center" />
                    <el-table-column prop="preview" label="预览" align="center" />
                    <el-table-column prop="author" label="用户" width="100" align="center" />
                    <el-table-column prop="authorId" label="UID" width="80" align="center" />
                    <el-table-column prop="" label="封面" width="80" align="center">
                        <template #default="scope">
                            <el-button type="primary" plain @click="viewCover(scope.row.cover)" size="small"
                                >查看</el-button
                            >
                        </template>
                    </el-table-column>
                    <el-table-column prop="createTime" label="创建时间" width="170" align="center">
                        <template #default="scope">
                            {{ adjustTime(scope.row.createTime) }}
                        </template>
                    </el-table-column>
                    <el-table-column prop="updateTime" label="更新时间" width="170" align="center" sortable>
                        <template #default="scope">
                            {{ adjustTime(scope.row.updateTime) }}
                        </template>
                    </el-table-column>
                    <el-table-column prop="tag" label="标签" width="80" />
                    <el-table-column prop="classify" label="分类" width="80" align="center" />
                    <el-table-column prop="" label="审核" width="140" align="center">
                        <template #default="scope">
                            <el-button type="primary" size="small" plain @click="verify(scope.row.id)">
                                审核
                            </el-button>
                            <el-tag type="warning" v-if="scope.row.verify == 1">未审核</el-tag>
                            <el-tag type="danger" v-if="scope.row.verify == 2">未通过 </el-tag>
                            <el-tag type="success" v-if="scope.row.verify == 3">已通过 </el-tag>
                        </template>
                    </el-table-column>
                    <el-table-column prop="" label="操作" width="80" align="center">
                        <template #default="scope">
                            <el-button type="danger" size="small" plain @click="DeleteArticle(scope.row)">
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
                    :total="total"
                    @current-change="currentChange" />
            </div>
        </el-main>
    </el-container>
    <!-- 封面预览窗 -->
    <el-dialog
        v-model="viewCoverDialogVisible"
        width="40%"
        center
        :append-to-body="true"
        :show-close="false"
        :lock-scroll="false"
        :destroy-on-close="true"
        :align-center="true">
        <el-image :src="cover" fit="contain" />
        <template #footer>
            <span class="dialog-footer">
                <el-button @click="viewCoverDialogVisible = false" type="primary">关 闭</el-button>
            </span>
        </template>
    </el-dialog>
</template>
<script setup>
import { searchArticle, deleteArticle } from "@/api/Admin";
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
let articles = ref([]);
/* 分页查询 */
let total = ref(1); //总页数
let currPage = ref(1); //当前页
let pageSize = ref(15); //分页大小
let loading = ref(true);
function toListArticle(verify) {
    currPage.value = 1;
    VERIFY.value = verify;
    router.push({
        path: "/manage/article",
        query: {
            activeIndex: activeIndex.value,
            VERIFY: VERIFY.value,
            page: currPage.value,
            articleId: articleId.value != null && articleId.value != "" ? parseInt(articleId.value) : null,
            title: title.value,
            author: author.value,
            authorId: authorId.value != null && authorId.value != "" ? parseInt(authorId.value) : null,
            tag: tag.value,
            classify: classify.value,
            createTimeStart: createTimeStart.value,
            createTimeEnd: createTimeEnd.value,
            updateTimeStart: updateTimeStart.value,
            updateTimeEnd: updateTimeEnd.value,
        },
    });
}
/* 搜索 */
let articleId = ref(null);
let title = ref(null);
let author = ref(null);
let authorId = ref(null);
let tag = ref(null);
let classify = ref(null);
let createTime = ref(null);
let createTimeStart = ref(null);
let createTimeEnd = ref(null);
let updateTime = ref(null);
let updateTimeStart = ref(null);
let updateTimeEnd = ref(null);
let isSearch = ref(false);
let VERIFY = ref(0);

/* 搜索筛选 */
function toSearch() {
    currPage.value = 1;
    search();
}
function search() {
    loading.value = true;
    if ((isSearch.value = false)) currPage.value = 1;
    isSearch.value = true;
    if (createTime.value != null) {
        createTimeStart.value = new Date(createTime.value[0]);
        createTimeEnd.value = new Date(createTime.value[1]);
    } else {
        createTimeStart.value = null;
        createTimeEnd.value = null;
    }
    if (updateTime.value != null) {
        updateTimeStart.value = new Date(updateTime.value[0]);
        updateTimeEnd.value = new Date(updateTime.value[1]);
    } else {
        updateTimeStart.value = null;
        updateTimeEnd.value = null;
    }
    searchArticle({
        activeIndex: activeIndex.value,
        VERIFY: VERIFY.value,
        currPage: currPage.value,
        pageSize: pageSize.value,
        articleId: parseInt(articleId.value),
        title: title.value,
        author: author.value,
        authorId: parseInt(authorId.value),
        tag: tag.value,
        classify: classify.value,
        createTimeStart: createTimeStart.value,
        createTimeEnd: createTimeEnd.value,
        updateTimeStart: updateTimeStart.value,
        updateTimeEnd: updateTimeEnd.value,
    }).then((res) => {
        if (res.data.code == 1) {
            loading.value = false;
            articles.value = res.data.data.list;
            total.value = res.data.data.total;
        }
    });
}
/* 审核文章 */
function verify(id) {
    if (id == null) return;
    router.push({
        path: "/manage/articleVerify",
        query: {
            id: id,
        },
    });
}
/* 删除文章 */
async function DeleteArticle(article) {
    await ElMessageBox.confirm("您确定删除该文章？", {
        confirmButtonText: "删除",
        cancelButtonText: "取消",
        type: "error",
        lockScroll: false,
        center: true,
    })
        .then(() => {
            deleteArticle(article).then((res) => {
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
/* 浏览封面 */
let cover = ref("");
let viewCoverDialogVisible = ref(false);
function viewCover(c) {
    viewCoverDialogVisible.value = true;
    cover.value = c;
}
/* 换页操作 */
let currentChange = (page) => {
    currPage.value = page;
    router.push({
        path: "/manage/article",
        query: {
            activeIndex: activeIndex.value,
            VERIFY: VERIFY.value,
            page: page,
            articleId: articleId.value != null && articleId.value != "" ? parseInt(articleId.value) : null,
            title: title.value,
            author: author.value,
            authorId: authorId.value != null && authorId.value != "" ? parseInt(authorId.value) : null,
            tag: tag.value,
            classify: classify.value,
            createTimeStart: createTimeStart.value,
            createTimeEnd: createTimeEnd.value,
            updateTimeStart: updateTimeStart.value,
            updateTimeEnd: updateTimeEnd.value,
        },
    });
};
/* 清空搜索 */
function clearAll() {
    articleId.value = null;
    title.value = null;
    author.value = null;
    authorId.value = null;
    tag.value = null;
    classify.value = null;
    createTime.value = null;
    createTimeStart.value = null;
    createTimeEnd.value = null;
    updateTime.value = null;
    updateTimeStart.value = null;
    updateTimeEnd.value = null;
    currPage.value = 1;
    search();
}
/* 路由后还原数据 */
function setSearchData() {
    if (useRouter.query.activeIndex != null) {
        activeIndex.value = useRouter.query.activeIndex;
    }
    if (useRouter.query.VERIFY != null) {
        VERIFY.value = parseInt(useRouter.query.VERIFY);
    }
    if (useRouter.query.page != null) {
        currPage.value = parseInt(useRouter.query.page);
    }
    if (useRouter.query.articleId != null) {
        articleId.value = parseInt(useRouter.query.articleId);
    }
    if (useRouter.query.title != null) {
        title.value = useRouter.query.title;
    }
    if (useRouter.query.author != null) {
        author.value = useRouter.query.author;
    }
    if (useRouter.query.authorId != null) {
        authorId.value = parseInt(useRouter.query.authorId);
    }
    if (useRouter.query.tag != null) {
        tag.value = useRouter.query.tag;
    }
    if (useRouter.query.createTimeStart != null) {
        createTimeStart.value = useRouter.query.createTimeStart;
    }
    if (useRouter.query.createTimeEnd != null) {
        createTimeEnd.value = useRouter.query.createTimeEnd;
    }
    if (useRouter.query.createTimeEnd != null) {
        updateTimeStart.value = useRouter.query.createTimeEnd;
    }
    if (useRouter.query.updateTimeEnd != null) {
        updateTimeEnd.value = useRouter.query.updateTimeEnd;
    }
}

const rowStyle = ({ row, rowIndex }) => {
    return {
        backgroundColor: "rgb(255, 255, 255,0.3)",
    };
};
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
    setSearchData();
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
