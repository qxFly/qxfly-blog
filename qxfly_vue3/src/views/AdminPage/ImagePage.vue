<template>
    <el-container class="main-container">
        <el-header class="header">
            <div class="search-bar">
                <div class="search-items">
                    <el-input
                        class="search-item"
                        v-model="image.aid"
                        placeholder="文章ID"
                        clearable
                        style="width: 160px"></el-input>
                    <el-input
                        class="search-item"
                        v-model="image.name"
                        placeholder="名称"
                        v-if="galleryType == '2d'"
                        clearable
                        style="width: 160px"></el-input>
                    <el-input
                        class="search-item"
                        v-model="image.originName"
                        placeholder="原始名称"
                        clearable
                        v-if="galleryType == '2d'"
                        style="width: 160px"></el-input>
                    <el-date-picker
                        :default-time="[new Date(2000, 1, 1, 0, 0, 0), new Date(2000, 1, 1, 23, 59, 59)]"
                        value-format="YYYY-MM-DD[T]HH:mm:ss.sss[Z]"
                        v-model="image.createTime"
                        start-placeholder="起始创建日期"
                        end-placeholder="截止创建日期"
                        type="daterange"
                        unlink-panels
                        style="max-width: 300px" />

                    <el-button type="primary" @click="toSearch">筛选</el-button>
                    <el-button @click="clearAll" style="margin: 0">清空筛选</el-button>
                    <el-button @click="UpdateImage" style="margin: 0">更新图库</el-button>
                    <el-radio-group v-model="isPreviewImage">
                        <el-radio-button label="展示预览" :value="true" />
                        <el-radio-button label="关闭预览" :value="false" />
                    </el-radio-group>
                    <el-radio-group v-model="galleryType">
                        <el-radio-button label="博客图片" value="blog" @click="ChangeGallery('blog')" />
                        <el-radio-button label="二次元" value="2d" @click="ChangeGallery('2d')" />
                    </el-radio-group>
                </div>
            </div>
            <div class="menu">
                <el-menu :default-active="activeIndex" class="el-menu" mode="horizontal" @select="handleSelect">
                    <el-menu-item index="1" @click="toListImage(0)">全部</el-menu-item>
                    <el-sub-menu index="2">
                        <template #title>已审核</template>
                        <el-menu-item index="2-1" @click="toListImage(3)">通过</el-menu-item>
                        <el-menu-item index="2-2" @click="toListImage(2)">未通过</el-menu-item>
                    </el-sub-menu>
                    <el-menu-item index="3" @click="toListImage(1)">未审核</el-menu-item>
                </el-menu>
            </div>
        </el-header>
        <el-main class="main">
            <div>
                <el-table
                    v-loading="loading"
                    :data="images"
                    :border="true"
                    show-overflow-tooltip
                    stripe
                    :highlight-current-row="true"
                    style="width: 100%"
                    :row-style="rowStyle"
                    :max-height="setTableHeight()">
                    <el-table-column fixed prop="id" label="ID" width="80" align="center" />
                    <!-- <el-table-column prop="originName" label="原始名字" align="center" /> -->
                    <el-table-column prop="name" label="文件名" width="330" align="center" />
                    <el-table-column prop="url" label="Url" align="center" />
                    <el-table-column prop="" label="预览" align="center">
                        <template #default="scope">
                            <el-image
                                v-if="isPreviewImage || isPreviewImage == 'true'"
                                :src="scope.row.url"
                                style="width: 200px"
                                fit="cover"
                                :preview-src-list="previewList"
                                :preview-teleported="true"
                                :hide-on-click-modal="true"></el-image>
                        </template>
                    </el-table-column>
                    <el-table-column prop="createTime" label="创建时间" width="170" align="center">
                        <!-- <template #default="scope">
                            {{ adjustTime(scope.row.createTime) }}
                        </template> -->
                    </el-table-column>
                    <el-table-column prop="aid" label="文章ID" width="80" align="center" />
                    <el-table-column prop="" label="原文章" width="110" align="center">
                        <template #default="scope">
                            <el-button size="small" plain type="primary" @click="verifyArticle(scope.row)">
                                审核原文章
                            </el-button>
                            <br />
                            <el-tag type="warning" v-if="scope.row.verify == 1">未审核</el-tag>
                            <el-tag type="danger" v-if="scope.row.verify == 2">未通过 </el-tag>
                            <el-tag type="success" v-if="scope.row.verify == 3">已通过 </el-tag>
                        </template>
                    </el-table-column>
                    <el-table-column prop="" label="操作" width="80" align="center">
                        <template #default="scope">
                            <el-button type="danger" size="small" disabled plain @click="DeleteImage(scope.row)">
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
    <!-- 图片预览窗 -->
    <el-dialog
        v-model="viewCoverDialogVisible"
        width="40%"
        center
        :append-to-body="true"
        :show-close="false"
        :lock-scroll="false"
        :destroy-on-close="true"
        :align-center="true"
        style="display: flex; align-items: center; justify-content: center; flex-direction: column">
        <el-image :src="previewImageUrl" fit="cover" />
        <template #footer>
            <span class="dialog-footer">
                <el-button @click="viewCoverDialogVisible = false" type="primary">关 闭</el-button>
                <el-button @click="pass()" type="success">通 过</el-button>
                <el-button @click="nopass()" type="danger">不通过</el-button>
            </span>
        </template>
    </el-dialog>
</template>
<script setup>
import { listImages, deleteImage, updateImage, changeGallery, getGalleryType } from "@/api/Admin";
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
let isPreviewImage = ref(false);

let images = ref([]);
/* 分页查询 */
let total = ref(1); //总页数
let currPage = ref(1); //当前页
let pageSize = ref(10); //分页大小
let loading = ref(true);
function toListImage(verify) {
    currPage.value = 1;
    VERIFY.value = verify;
    router.push({
        path: "/manage/image",
        query: {
            activeIndex: activeIndex.value,
            verify: VERIFY.value,
            page: currPage.value,
            originName: image.value.originName,
            aid: image.value.aid != null && image.value.aid != "" ? parseInt(image.value.aid) : null,
            createTimeStart: image.value.createTimeStart,
            createTimeEnd: image.value.createTimeEnd,
        },
    });
}
/* 搜索 */
let image = ref({
    aid: null,
    name: null,
    originName: null,
    createTime: null,
    createTimeStart: null,
    createTimeEnd: null,
});
let isSearch = ref(false);
let VERIFY = ref(0);
let previewList = ref([]);
/* 搜索筛选 */
function toSearch() {
    currPage.value = 1;
    search();
}
function search() {
    loading.value = true;
    if (isSearch.value == false) currPage.value = 1;
    isSearch.value = true;
    if (image.value.createTime != null) {
        image.value.createTimeStart = new Date(image.value.createTime[0]);
        image.value.createTimeEnd = new Date(image.value.createTime[1]);
    } else {
        image.value.createTimeStart = null;
        image.value.createTimeEnd = null;
    }
    listImages({
        verify: VERIFY.value,
        currPage: currPage.value,
        pageSize: pageSize.value,
        aid: parseInt(image.value.aid),
        originName: image.value.originName,
        name: image.value.name,
        createTimeStart: image.value.createTimeStart,
        createTimeEnd: image.value.createTimeEnd,
    }).then((res) => {
        if (res.data.code == 1) {
            loading.value = false;
            images.value = res.data.data.list;
            total.value = res.data.data.total;
            for (let i = 0; i < images.value.length; i++) {
                previewList.value.push(images.value[i].url);
            }
        } else {
            ElMessage.error("获取数据失败");
        }
    });
}
/* 审核文章 */
let previewImageUrl = ref("");
let viewCoverDialogVisible = ref(false);
/* 删除图片 */
async function DeleteImage(article) {
    await ElMessageBox.confirm("您确定删除该图片？", {
        confirmButtonText: "删除",
        cancelButtonText: "取消",
        type: "error",
        lockScroll: false,
        center: true,
    })
        .then(() => {
            deleteImage(article).then((res) => {
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
/* 获取图库类型 2d:自己的二次元图片，blog:博客图片 */
let galleryType = ref("blog"); //是否开启自己的二次元图库
function GetGalleryType() {
    getGalleryType().then((res) => {
        if (res.data.code == 1) {
            galleryType.value = res.data.data;
        } else {
            ElMessage.error(res.data.msg);
        }
    });
}
/* 切换图库 */
function ChangeGallery(type) {
    changeGallery(type).then((res) => {
        if (res.data.code == 1) {
            ElMessage.success("切换成功");
            search();
        } else {
            ElMessage.error("切换失败");
        }
    });
}
/* 审核文章 */
function verifyArticle(article) {
    if (article.aid == null) return;
    router.push({
        path: "/manage/articleVerify",
        query: {
            id: article.aid,
        },
    });
}

/* 换页操作 */
let currentChange = (page) => {
    currPage.value = page;
    router.push({
        path: "/manage/image",
        query: {
            activeIndex: activeIndex.value,
            VERIFY: VERIFY.value,
            page: page,
            originName: image.value.originName,
            aid: image.value.aid != null && image.value.aid != "" ? parseInt(image.value.aid) : null,
            createTimeStart: image.value.createTimeStart,
            createTimeEnd: image.value.createTimeEnd,
        },
    });
};
/* 清空搜索 */
function clearAll() {
    image.value.aid = null;
    image.value.originName = null;
    image.value.createTime = null;
    image.value.name = null;
    image.value.createTimeStart = null;
    image.value.createTimeEnd = null;
    currPage.value = 1;
    search();
}
function UpdateImage() {
    updateImage().then((res) => {
        if (res.data.code == 1) {
            ElMessage.success("更新成功");
        } else {
            ElMessage.error("更新失败");
        }
    });
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
    if (useRouter.query.aid != null) {
        image.value.aid = parseInt(useRouter.query.aid);
    }
    if (useRouter.query.createTimeStart != null) {
        image.value.createTimeStart = useRouter.query.createTimeStart;
    }
    if (useRouter.query.createTimeEnd != null) {
        image.value.createTimeEnd = useRouter.query.createTimeEnd;
    }
    if (useRouter.query.originName != null) {
        image.value.originName = useRouter.query.originName;
    }
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
/* 设置表格高度 */
function setTableHeight() {
    let height = document.body.scrollHeight - 250;
    return height;
}
const rowStyle = ({ row, rowIndex }) => {
    return {
        backgroundColor: "rgb(255, 255, 255,0.3)",
    };
};
onMounted(() => {
    setSearchData();
    GetGalleryType();
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
