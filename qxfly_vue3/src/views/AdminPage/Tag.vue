<template>
    <el-container class="main-container">
        <el-header class="header">
            <div class="search-bar">
                <div class="search-items">
                    <el-input
                        class="search-item"
                        v-model="id"
                        placeholder="标签ID"
                        clearable
                        style="width: 160px"></el-input>
                    <el-input
                        class="search-item"
                        v-model="name"
                        placeholder="标签名称"
                        clearable
                        style="width: 160px" />

                    <el-button type="primary" @click="toSearch">筛选</el-button>
                    <el-button @click="clearAll" style="margin: 0">清空筛选</el-button>
                    <el-button @click="addTagDialog = true" style="margin: 0">添加标签</el-button>
                </div>
            </div>
        </el-header>
        <el-main class="main">
            <div>
                <el-table
                    v-loading="loading"
                    :data="Tags"
                    :border="true"
                    show-overflow-tooltip
                    stripe
                    :highlight-current-row="true"
                    style="width: 100%"
                    :row-style="rowStyle"
                    :max-height="setTableHeight()">
                    <el-table-column fixed prop="id" label="ID" width="80" align="center" />
                    <el-table-column prop="name" label="名称" align="center" />

                    <el-table-column prop="" label="操作" width="160" align="center">
                        <template #default="scope">
                            <el-button type="primary" size="small" plain @click="toUpdateTag(scope.row)">
                                修改
                            </el-button>
                            <el-button type="danger" size="small" plain @click="Delete(scope.row.id)"> 删除 </el-button>
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
    <!-- 添加 -->
    <el-dialog
        v-model="addTagDialog"
        width="40%"
        center
        :append-to-body="true"
        :show-close="false"
        :lock-scroll="false"
        :destroy-on-close="true"
        :align-center="true">
        <el-input v-model="addT"></el-input>

        <template #footer>
            <span class="dialog-footer">
                <el-button @click="addTagDialog = false">关 闭</el-button
                ><el-button @click="AddTag()" type="primary">添 加</el-button>
            </span>
        </template>
    </el-dialog>
    <!-- 修改 -->
    <el-dialog
        v-model="updateTagDialog"
        width="40%"
        center
        :append-to-body="true"
        :show-close="false"
        :lock-scroll="false"
        :destroy-on-close="true"
        :align-center="true">
        <div>
            <div class="update-dialog-item">
                <el-input type="text" v-model="updateT.id" disabled>
                    <template #prepend>
                        <div class="label">ID</div>
                    </template>
                </el-input>
            </div>
            <div class="update-dialog-item">
                <el-input clearable type="text" v-model="updateT.name">
                    <template #prepend>
                        <div class="label">标签名</div>
                    </template>
                </el-input>
            </div>
            <div class="update-dialog-item">
                <el-input clearable type="text" v-model="updateT.uid" disabled>
                    <template #prepend>
                        <div class="label">创建者</div>
                    </template>
                </el-input>
            </div>
            <div class="update-dialog-item">
                <el-input clearable type="text" v-model="updateT.createTime" disabled>
                    <template #prepend>
                        <div class="label">创建时间</div>
                    </template>
                </el-input>
            </div>
        </div>

        <template #footer>
            <span class="dialog-footer">
                <el-button @click="updateTagDialog = false">关 闭</el-button>
                <el-button @click="UpdateTag()" type="primary">修 改</el-button>
            </span>
        </template>
    </el-dialog>
</template>
<script setup>
import { getTags, deleteTag, addTag, updateTag } from "@/api/Article";
import { ref, onMounted, watch } from "vue";
import router from "@/router";
import { useRoute } from "vue-router";
import { ElMessage, ElMessageBox } from "element-plus";
import { update } from "js-md5";
let useRouter = useRoute();
let loading = ref(true);
/* 分页查询 */
let totalPages = ref(1); //总页数
let currPage = ref(1); //当前页
let pageSize = ref(20); //分页大小
/* 搜索 */
let Tags = ref([]);
let id = ref(null);
let name = ref("");
function toSearch() {
    currPage.value = 1;
    search();
}
/* 搜索筛选 */
function search() {
    loading.value = true;
    getTags(currPage.value, pageSize.value, id.value, name.value).then((res) => {
        if (res.data.code == 1) {
            loading.value = false;
            Tags.value = res.data.data.list;
            totalPages.value = res.data.data.total;
        }
    });
}
/* 删除标签 */
async function Delete(id) {
    await ElMessageBox.confirm("您确定删除该标签？", {
        confirmButtonText: "删除",
        cancelButtonText: "取消",
        type: "error",
        lockScroll: false,
        center: true,
    })
        .then(() => {
            deleteTag({ id: id }).then((res) => {
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
/* 添加标签 */
let addT = ref();
let addTagDialog = ref(false);
function AddTag() {
    addTag({ name: addT.value }).then((res) => {
        if (res.data.code == 1) {
            ElMessage.success("添加成功");
            addTagDialog.value = false;
            search();
        } else {
            ElMessage.error(res.data.msg);
        }
    });
}
/* 更新标签 */
let updateT = ref();
let updateTagDialog = ref(false);
function toUpdateTag(tag) {
    updateT.value = Object.assign({}, tag);
    updateTagDialog.value = true;
}
function UpdateTag() {
    updateTag(updateT.value).then((res) => {
        if (res.data.code == 1) {
            ElMessage.success("修改成功");
            updateTagDialog.value = false;
            search();
        } else {
            ElMessage.error("修改失败");
        }
    });
}
/* 换页操作 */
let currentChange = (page) => {
    currPage.value = page;
    router.push({
        path: "/manage/tag",
        query: {
            page: page,
            id: id.value,
            name: name.value,
        },
    });
};
/* 清空搜索 */
function clearAll() {
    id.value = null;
    name.value = null;
    currPage.value = 1;
    search();
}
/* 设置表格高度 */
function setTableHeight() {
    let height = document.body.scrollHeight - 200;
    return height;
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
const rowStyle = ({ row, rowIndex }) => {
    return {
        backgroundColor: "rgb(255, 255, 255,0.3)",
    };
};
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
    margin-bottom: 10px;
}
.main {
    padding-top: 0;
}
.setting-items {
    // min-width: 400px;
    padding: 20px 20px 20px 0;
    width: 100%;
    display: flex;
    gap: 40px;
    overflow: hidden;
}
.col {
    width: 100%;
}

.user-info-item {
    margin: 20px;
    width: 100%;
}
.label {
    font-weight: 700;
    width: 40px;
    text-align: center;
}
.label-r {
    font-weight: 700;
    width: 40px;
    text-align: center;
    margin-bottom: 10px;
}
.update-dialog-item {
    margin-bottom: 20px;
}
</style>
