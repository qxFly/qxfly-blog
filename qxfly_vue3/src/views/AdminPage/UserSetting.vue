<template>
    <el-container class="main-container">
        <el-header class="header">
            <div class="search-bar">
                <div class="search-items">
                    <el-input class="search-item" v-model="uid" placeholder="用户ID" clearable style="width: 160px" />
                    <el-input
                        class="search-item"
                        v-model="username"
                        placeholder="用户名"
                        clearable
                        style="width: 160px" />
                    <el-button type="primary" @click="toSearch">筛选</el-button>
                    <el-button @click="clearAll" style="margin: 0">清空筛选</el-button>
                </div>
            </div>
        </el-header>
        <el-main class="main">
            <div>
                <el-table
                    v-loading="loading"
                    :data="userSettings"
                    :border="true"
                    show-overflow-tooltip
                    stripe
                    :highlight-current-row="true"
                    style="width: 100%"
                    :row-style="rowStyle"
                    :max-height="setTableHeight()">
                    <el-table-column fixed prop="uid" label="用户ID" width="80" align="center" />
                    <el-table-column prop="username" label="用户名" width="160" align="center" />
                    <el-table-column prop="" label="背景图片" width="100" align="center">
                        <template #default="scope">
                            <el-button
                                type="primary"
                                plain
                                @click="viewBackground(scope.row)"
                                size="small"
                                :disabled="scope.row.bgImgPath == null ? true : false">
                                查看
                            </el-button>
                        </template>
                    </el-table-column>
                    <el-table-column prop="bgSwitch" label="背景图片开关" width="150" align="center">
                        <template #default="scope">
                            {{ scope.row.bgSwitch == 1 ? "开启" : "关闭" }}
                        </template>
                    </el-table-column>
                    <el-table-column prop="bgBlur" min-width="300" label="背景模糊" align="center" />
                    <el-table-column prop="" label="操作" align="center">
                        <template #default="scope">
                            <el-button type="primary" size="small" plain @click="toEditUser(scope.row)">
                                编辑
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
    <!-- 编辑用户设置弹窗 -->
    <el-dialog
        title="编辑用户设置"
        v-model="editUserSettingDialogVisible"
        :append-to-body="true"
        :close-on-click-modal="false"
        :show-close="false"
        :lock-scroll="false"
        center>
        <div class="user-info-content">
            <div class="col">
                <div class="user-info-item">
                    <div class="edit-uid">
                        <el-input clearable type="text" v-model="editUS.uid" disabled>
                            <template #prepend>
                                <div class="label">UID</div>
                            </template>
                        </el-input>
                    </div>
                </div>
                <div class="user-info-item">
                    <div class="edit-username">
                        <el-input clearable type="text" disabled v-model="editUS.username" maxlength="20">
                            <template #prepend>
                                <div class="label">用户名</div>
                            </template>
                        </el-input>
                    </div>
                </div>
                <div class="user-info-item">
                    <div class="edit-bgSwitch">
                        <el-input clearable type="text" v-model="editUS.bgSwitch" maxlength="11">
                            <template #prepend>
                                <div class="label">背景图片开关</div>
                            </template>
                        </el-input>
                    </div>
                </div>
                <div class="user-info-item">
                    <div class="edit-bgBlur">
                        <el-input clearable type="number" v-model="editUS.bgBlur">
                            <template #prepend>
                                <div class="label">背景模糊</div>
                            </template>
                        </el-input>
                    </div>
                </div>
            </div>
            <div class="col">
                <div class="user-info-item">
                    <div class="edit-bgSwitch">
                        <div style="width: 100%; font-weight: bold">背景图片开关:</div>
                        <div class="edit-role">
                            <el-select v-model="editUS.bgSwitch">
                                <el-option value="1" label="开启">开启</el-option>
                                <el-option value="0" label="关闭">关闭</el-option>
                            </el-select>
                        </div>
                    </div>
                </div>
                <div class="user-info-item">
                    <div class="edit-bgImgPath">
                        <div style="width: 100%; font-weight: bold">背景图片:</div>
                        <el-image
                            v-if="editUS.bgImgPath"
                            @click="viewBackground(editUS)"
                            :src="editUS.bgImgPath"
                            fit="contain" />
                    </div>
                </div>
            </div>
            <div class="user-info-item" v-if="isTip" style="font-weight: 700; color: #846cb6">
                {{ tip }}
            </div>
        </div>
        <template #footer>
            <span class="dialog-footer">
                <el-button @click="Cencel">取 消</el-button>
                <el-button type="primary" @click="EditUser">确 定</el-button>
            </span>
        </template>
    </el-dialog>
    <!-- 背景预览窗 -->
    <el-dialog
        v-model="viewBackgroundDialogVisible"
        width="40%"
        center
        :append-to-body="true"
        :show-close="false"
        :lock-scroll="false"
        :destroy-on-close="true"
        :align-center="true">
        <div style="display: flex; justify-content: center; align-items: center">
            <el-image :src="background" fit="contain" />
        </div>

        <template #footer>
            <span class="dialog-footer">
                <el-button @click="deleteBackground(backgroundId)">删除背景</el-button>
                <el-button @click="closeBackground" type="primary">关 闭</el-button>
            </span>
        </template>
    </el-dialog>
</template>
<script setup>
import { listUserSetting, editUserSetting, deleteUserBackground } from "@/api/Admin";
import { ref, onMounted, watch } from "vue";
import router from "@/router";
import { useRoute } from "vue-router";
import { ElMessage, ElMessageBox } from "element-plus";
import { check } from "@/api/Admin";
let useRouter = useRoute();
let loading = ref(true);
/* 分页查询 */
let totalPages = ref(1); //总页数
let currPage = ref(1); //当前页
let pageSize = ref(15); //分页大小
/* 搜索 */
let userSettings = ref([]);
let uid = ref(null);
let username = ref(null);
/* 搜索筛选 */
function toSearch() {
    currPage.value = 1;
    search();
}
function search() {
    loading.value = true;
    listUserSetting({
        currPage: currPage.value,
        pageSize: pageSize.value,
        uid: parseInt(uid.value),
        username: username.value,
    }).then((res) => {
        if (res.data.code == 1) {
            loading.value = false;
            if (res.data.data == null) return;
            userSettings.value = res.data.data.list;
            totalPages.value = res.data.data.total;
        }
    });
}

/* 编辑用户设置 */
let isTip = ref(false);
let tip = ref("");
let editUserSettingDialogVisible = ref(false);
let editUS = ref();
function toEditUser(user) {
    editUS.value = Object.assign({}, user);
    editUS.value.bgSwitch = editUS.value.bgSwitch.toString();
    editUserSettingDialogVisible.value = true;
}
function EditUser() {
    editUserSetting(editUS.value).then((res) => {
        if (res.data.code == 1) {
            ElMessage.success("编辑成功");
            editUserSettingDialogVisible.value = false;
            search();
        } else {
            isTip.value = true;
            tip.value = res.data.msg;
            setTimeout(() => {
                isTip.value = false;
            }, 2000);
        }
    });
}
function Cencel() {
    editUserSettingDialogVisible.value = false;
}
/* 浏览背景 */
let background = ref(null);
let backgroundId = ref(null);
let viewBackgroundDialogVisible = ref(false);
function viewBackground(data) {
    let s = "da";
    background.value = data.bgImgPath;
    backgroundId.value = data.uid;
    viewBackgroundDialogVisible.value = true;
}
/* 删除背景 */
function deleteBackground(id) {
    ElMessageBox.confirm("确定删除背景吗?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
    })
        .then(() => {
            deleteUserBackground({ uid: id, bgPath: background.value }).then((res) => {
                if (res.data.code == 1) {
                    ElMessage.success("删除成功");
                    // editUser.value.avatar = null;
                    search();
                    viewBackgroundDialogVisible.value = false;
                } else {
                    isTip.value = true;
                    tip.value = res.data.msg;
                    setTimeout(() => {
                        isTip.value = false;
                    }, 2000);
                }
            });
        })
        .catch(() => {});
}
function closeBackground() {
    viewBackgroundDialogVisible.value = false;
    background.value = null;
    backgroundId.value = null;
}
/* 换页操作 */
let currentChange = (page) => {
    currPage.value = page;
    router.push({
        path: "/manage/user",
        query: {
            page: page,
            id: parseInt(uid.value),
            username: username.value,
        },
    });
};
/* 清空搜索 */
function clearAll() {
    uid.value = null;
    username.value = null;
    currPage.value = 1;
    search();
}
// let showUserAuthority = ref(false);
// async function testAuthority() {
//     await check().then((res) => {
//         if (res.data.data === 1) {
//             showUserAuthority.value = true;
//         }
//     });
// }
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
    search();
    // testAuthority();
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
.user-info-content {
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
    width: 90px;
    text-align: center;
}
.label-r {
    font-weight: 700;
    width: 40px;
    text-align: center;
    margin-bottom: 10px;
}
</style>
