<template>
    <el-container class="main-container">
        <el-header class="header">
            <div class="search-bar">
                <div class="search-items">
                    <el-input
                        class="search-item"
                        v-model="id"
                        placeholder="用户ID"
                        clearable
                        style="width: 160px"></el-input>
                    <el-input
                        class="search-item"
                        v-model="username"
                        placeholder="用户名"
                        clearable
                        style="width: 160px" />
                    <el-input class="search-item" v-model="phone" placeholder="手机号" clearable style="width: 160px" />
                    <el-input class="search-item" v-model="email" placeholder="邮箱" clearable style="width: 160px" />
                    <el-input
                        class="search-item"
                        v-model="introduction"
                        placeholder="简介"
                        clearable
                        style="width: 160px" />
                    <el-input
                        class="search-item"
                        v-model="location"
                        placeholder="地址"
                        clearable
                        style="width: 160px" />
                    <!-- <el-input class="search-item" v-model="role" placeholder="角色" clearable style="width: 160px" /> -->
                    <el-select v-model="role" style="width: 160px" placeholder="角色">
                        <el-option value="1" label="管理员">管理员</el-option>
                        <el-option value="2" label="文章审核员">文章审核员</el-option>
                        <el-option value="3" label="评论审核员">评论审核员</el-option>
                        <el-option value="4" label="用户审核员">用户审核员</el-option>
                        <el-option value="0" label="普通用户">普通用户</el-option>
                    </el-select>

                    <el-button type="primary" @click="toSearch">筛选</el-button>
                    <el-button @click="clearAll" style="margin: 0">清空筛选</el-button>
                </div>
            </div>
        </el-header>
        <el-main class="main">
            <div>
                <el-table
                    v-loading="loading"
                    :data="users"
                    :border="true"
                    show-overflow-tooltip
                    stripe
                    :highlight-current-row="true"
                    style="width: 100%"
                    :max-height="setTableHeight()">
                    <el-table-column fixed prop="id" label="ID" width="80" align="center" />
                    <el-table-column prop="username" label="用户名" width="160" align="center" />
                    <el-table-column prop="phone" label="手机号" width="140" align="center" />
                    <el-table-column prop="email" label="邮箱" width="200" align="center" />
                    <el-table-column prop="introduction" min-width="300" label="简介" align="center" />
                    <el-table-column prop="location" label="地址" width="250" align="center" />
                    <el-table-column prop="" label="角色" width="100" align="center">
                        <template #default="scope">
                            <span v-if="scope.row.role == 0">普通用户</span>
                            <span v-if="scope.row.role == 1">管理员</span>
                            <span v-if="scope.row.role == 2">文章审核员</span>
                            <span v-if="scope.row.role == 3">评论审核员</span>
                            <span v-if="scope.row.role == 4">用户审核员</span>
                        </template>
                    </el-table-column>
                    <el-table-column prop="" label="头像" width="80" align="center">
                        <template #default="scope">
                            <el-button
                                type="primary"
                                plain
                                @click="viewAvatar(scope.row)"
                                size="small"
                                :disabled="scope.row.avatar == null ? true : false">
                                查看
                            </el-button>
                        </template>
                    </el-table-column>
                    <el-table-column prop="birthday" label="生日" width="170" align="center">
                        <template #default="scope">
                            {{ adjustTime(scope.row.birthday) }}
                        </template>
                    </el-table-column>
                    <el-table-column prop="" label="操作" width="160" align="center">
                        <template #default="scope">
                            <el-button type="primary" size="small" plain @click="toEditUser(scope.row)">
                                编辑
                            </el-button>
                            <el-button type="danger" size="small" plain @click="DeleteUser(scope.row.id)">
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
    <!-- 编辑用户弹窗 -->
    <el-dialog
        title="编辑用户"
        v-model="editUserDialogVisible"
        :append-to-body="true"
        :close-on-click-modal="false"
        :show-close="false"
        :lock-scroll="false"
        center>
        <div class="user-info-content">
            <div class="col">
                <div class="user-info-item">
                    <div class="edit-id">
                        <el-input clearable type="text" v-model="editUser.id" disabled>
                            <template #prepend>
                                <div class="label">ID</div>
                            </template>
                        </el-input>
                    </div>
                </div>
                <div class="user-info-item">
                    <div class="edit-name">
                        <el-input clearable type="text" v-model="editUser.username" maxlength="20">
                            <template #prepend>
                                <div class="label">用户名</div>
                            </template>
                        </el-input>
                    </div>
                </div>
                <div class="user-info-item">
                    <div class="edit-email">
                        <el-input clearable type="text" v-model="editUser.email" maxlength="20">
                            <template #prepend>
                                <div class="label">邮箱</div>
                            </template>
                        </el-input>
                    </div>
                </div>
                <div class="user-info-item">
                    <div class="edit-phone">
                        <el-input clearable type="text" v-model="editUser.phone" maxlength="11">
                            <template #prepend>
                                <div class="label">手机</div>
                            </template>
                        </el-input>
                    </div>
                </div>
                <div class="user-info-item">
                    <div class="edit-location">
                        <el-input clearable type="text" v-model="editUser.location">
                            <template #prepend>
                                <div class="label">地址</div>
                            </template>
                        </el-input>
                    </div>
                </div>
                <div class="user-info-item">
                    <div class="edit-birthday">
                        <el-input clearable type="date" v-model="editUser.birthday">
                            <template #prepend>
                                <div class="label">生日</div>
                            </template>
                        </el-input>
                    </div>
                </div>
            </div>
            <div class="col">
                <div class="user-info-item">
                    <div class="label-r">简介:</div>
                    <div class="edit-introduction">
                        <el-input
                            class="elinput"
                            resize="none"
                            :autosize="{ minRows: 2, maxRows: 4 }"
                            type="textarea"
                            v-model="editUser.introduction"></el-input>
                    </div>
                </div>
                <div class="user-info-item" v-if="showUserAuthority">
                    <div class="label-r">角色:</div>
                    <div class="edit-role">
                        <el-select v-model="editUser.role">
                            <el-option value="1" label="管理员">管理员</el-option>
                            <el-option value="2" label="文章审核员">文章审核员</el-option>
                            <el-option value="3" label="评论审核员">评论审核员</el-option>
                            <el-option value="4" label="用户审核员">用户审核员</el-option>
                            <el-option value="0" label="普通用户">普通用户</el-option>
                        </el-select>
                    </div>
                </div>
                <div class="user-info-item">
                    <div class="label-r">头像:</div>
                    <div class="edit-avatar">
                        <img :src="editUser.avatar" alt="" style="height: 200px" @click="viewAvatar(editUser)" />
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
    <!-- 头像预览窗 -->
    <el-dialog
        v-model="viewAvatarDialogVisible"
        width="40%"
        center
        :append-to-body="true"
        :show-close="false"
        :lock-scroll="false"
        :destroy-on-close="true"
        :align-center="true">
        <el-image :src="avatar" fit="contain" />
        <template #footer>
            <span class="dialog-footer">
                <el-button @click="deleteAvatar(avatarId)">删除头像</el-button>
                <el-button @click="closeAvatar" type="primary">关 闭</el-button>
            </span>
        </template>
    </el-dialog>
</template>
<script setup>
import { listUser, deleteUser, editUserInfo, deleteUserAvatar } from "@/api/Admin";
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
let users = ref([]);
let id = ref(null);
let username = ref(null);
let introduction = ref(null);
let location = ref(null);
let role = ref(null);
let email = ref(null);
let phone = ref(null);
/* 搜索筛选 */
function toSearch() {
    currPage.value = 1;
    search();
}
function search() {
    loading.value = true;
    listUser({
        currPage: currPage.value,
        pageSize: pageSize.value,
        id: parseInt(id.value),
        username: username.value,
        introduction: introduction.value,
        phone: phone.value,
        location: location.value,
        role: parseInt(role.value),
        email: email.value,
    }).then((res) => {
        if (res.data.code == 1) {
            loading.value = false;
            users.value = res.data.data.list;
            totalPages.value = res.data.data.total;
        }
    });
}
/* 删除用户 */
async function DeleteUser(id) {
    await ElMessageBox.confirm("您确定删除该用户？", {
        confirmButtonText: "删除",
        cancelButtonText: "取消",
        type: "error",
        lockScroll: false,
        center: true,
    })
        .then(() => {
            deleteUser(id).then((res) => {
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
/* 编辑用户 */
let isTip = ref(false);
let tip = ref("");
let editUserDialogVisible = ref(false);
let editUser = ref();
function toEditUser(user) {
    editUser.value = Object.assign({}, user);
    editUser.value.role = editUser.value.role.toString();
    let date = editUser.value.birthday;
    if (date != null) {
        editUser.value.birthday = date.split("T")[0];
    }
    editUserDialogVisible.value = true;
}
function EditUser() {
    editUserInfo(editUser.value).then((res) => {
        if (res.data.code == 1) {
            ElMessage.success("编辑成功");
            editUserDialogVisible.value = false;
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
    editUserDialogVisible.value = false;
}
/* 编辑头像 */

let avatar = ref(null);
let avatarId = ref(null);
let viewAvatarDialogVisible = ref(false);
/* 浏览头像 */
function viewAvatar(ava) {
    avatar.value = ava.avatar;
    avatarId.value = ava.id;
    viewAvatarDialogVisible.value = true;
}
/* 删除头像 */
function deleteAvatar(id) {
    ElMessageBox.confirm("确定删除头像吗?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
    })
        .then(() => {
            deleteUserAvatar({ id: id, avatar: avatar.value }).then((res) => {
                if (res.data.code == 1) {
                    ElMessage.success("删除成功");
                    // editUser.value.avatar = null;
                    search();
                    viewAvatarDialogVisible.value = false;
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
function closeAvatar() {
    viewAvatarDialogVisible.value = false;
    avatar.value = null;
    avatarId.value = null;
}
/* 换页操作 */
let currentChange = (page) => {
    currPage.value = page;
    router.push({
        path: "/manage/user",
        query: {
            page: page,
            id: parseInt(id.value),
            username: username.value,
            introduction: introduction.value,
            location: location.value,
            role: role.value,
            email: email.value,
        },
    });
};
/* 清空搜索 */
function clearAll() {
    id.value = null;
    username.value = null;
    introduction.value = null;
    location.value = null;
    role.value = null;
    email.value = null;
    phone.value = null;
    currPage.value = 1;
    search();
}
let showUserAuthority = ref(false);
async function testAuthority() {
    await check().then((res) => {
        if (res.data.data === 1) {
            showUserAuthority.value = true;
        }
    });
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
    if (time != null) return time.split("T")[0];
    else return "";
}
/* 设置表格高度 */
function setTableHeight() {
    let height = document.body.scrollHeight - 250;
    return height;
}
onMounted(() => {
    search();
    testAuthority();
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
    width: 40px;
    text-align: center;
}
.label-r {
    font-weight: 700;
    width: 40px;
    text-align: center;
    margin-bottom: 10px;
}
</style>
