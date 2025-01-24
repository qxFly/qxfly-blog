<template>
    <el-container class="main-container">
        <el-header class="header">
            <div class="search-bar">
                <div class="search-items">
                    <el-input
                        class="search-item"
                        v-model="id"
                        placeholder="留言ID"
                        clearable
                        style="width: 160px"></el-input>
                    <el-input
                        class="search-item"
                        v-model="content"
                        placeholder="留言内容"
                        clearable
                        style="width: 160px"></el-input>
                    <el-input
                        class="search-item"
                        v-model="uid"
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
                        v-model="date"
                        start-placeholder="起始留言日期"
                        end-placeholder="截止留言日期"
                        type="daterange"
                        unlink-panels
                        style="max-width: 300px" />

                    <el-button type="primary" @click="toSearch">筛选</el-button>
                    <el-button @click="clearAll" style="margin: 0">清空筛选</el-button>
                </div>
            </div>
        </el-header>
        <el-main class="main">
            <div>
                <el-table
                    v-loading="loading"
                    :data="lmsg"
                    :border="true"
                    show-overflow-tooltip
                    stripe
                    :highlight-current-row="true"
                    style="width: 100%"
                    :row-style="rowStyle"
                    :max-height="setTableHeight()">
                    <el-table-column fixed prop="id" label="ID" width="80" align="center" />
                    <el-table-column prop="username" label="用户" width="160" align="center" />
                    <el-table-column prop="uid" label="用户ID" width="80" align="center" />
                    <el-table-column prop="content" min-width="300" label="内容" align="center" />
                    <el-table-column prop="date" label="留言时间" width="170" align="center">
                        <template #default="scope">
                            {{ adjustTime(scope.row.date) }}
                        </template>
                    </el-table-column>
                    <el-table-column prop="" label="操作" width="80" align="center">
                        <template #default="scope">
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
</template>
<script setup>
import { listLeaveMessage, deleteLeaveMessage } from "@/api";
import { ref, onMounted, watch } from "vue";
import router from "@/router";
import { useRoute } from "vue-router";
import { ElMessage, ElMessageBox } from "element-plus";
let useRouter = useRoute();
let lmsg = ref([]);
let loading = ref(true);
/* 分页查询 */
let totalPages = ref(1); //总页数
let currPage = ref(1); //当前页
let pageSize = ref(15); //分页大小

/* 搜索 */
let id = ref(null);
let uid = ref(null);
let date = ref(null);
let dateTimeStart = ref(null);
let dateTimeEnd = ref(null);
let content = ref(null);
let username = ref(null);
/* 搜索筛选 */
/* 搜索筛选 */
function toSearch() {
    currPage.value = 1;
    search();
}
function search() {
    loading.value = true;
    if (date.value != null) {
        dateTimeStart.value = new Date(date.value[0]);
        dateTimeEnd.value = new Date(date.value[1]);
    } else {
        dateTimeStart.value = null;
        dateTimeEnd.value = null;
    }
    listLeaveMessage({
        currPage: currPage.value,
        pageSize: pageSize.value,
        id: parseInt(id.value),
        uid: parseInt(uid.value),
        content: content.value,
        username: username.value,
        dateTimeStart: dateTimeStart.value,
        dateTimeEnd: dateTimeEnd.value,
    }).then((res) => {
        if (res.data.code != 1) return;
        loading.value = false;
        lmsg.value = res.data.data.list;
        totalPages.value = res.data.data.total;
    });
}
/* 删除留言 */
async function Delete(id) {
    console.log(id);

    await ElMessageBox.confirm("您确定删除该留言？", {
        confirmButtonText: "删除",
        cancelButtonText: "取消",
        type: "error",
        lockScroll: false,
        center: true,
    })
        .then(() => {
            deleteLeaveMessage(id).then((res) => {
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
        path: "/manage/leaveMessage",
        query: {
            currPage: currPage.value,
            pageSize: pageSize.value,
            id: id.value,
            uid: parseInt(uid.value),
            content: content.value,
            username: username.value,
            dateTimeStart: dateTimeStart.value,
            dateTimeEnd: dateTimeEnd.value,
        },
    });
};
/* 清空搜索 */
function clearAll() {
    id.value = null;
    uid.value = null;
    content.value = null;
    username.value = null;
    date.value = null;
    dateTimeStart.value = null;
    dateTimeEnd.value = null;
    currPage.value = 1;
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
}
.menu {
}
.el-menu {
}
.main {
    padding-top: 0;
}
</style>
