<template>
    <el-container class="main-container">
        <el-header class="header">
            <div class="search-bar">
                <div class="search-items">
                    <el-input
                        class="search-item"
                        v-model="navId"
                        placeholder="导航ID"
                        clearable
                        style="width: 160px"></el-input>
                    <el-input
                        class="search-item"
                        v-model="name"
                        placeholder="导航标题"
                        clearable
                        style="width: 160px"></el-input>

                    <el-select class="search-item" v-model="role" style="width: 160px" placeholder="所属角色">
                        <el-option value="1" label="管理员">管理员</el-option>
                        <el-option value="2" label="文章审核员">文章审核员</el-option>
                        <el-option value="3" label="评论审核员">评论审核员</el-option>
                        <el-option value="4" label="用户审核员">用户审核员</el-option>
                        <el-option value="0" label="普通用户">普通用户</el-option>
                    </el-select>
                    <el-button type="primary" @click="toSearch">筛选</el-button>
                    <el-button @click="clearAll" style="margin: 0">清空筛选</el-button>
                    <el-button @click="addNavDialog = true" style="margin: 0">添加导航栏</el-button>
                </div>
            </div>
            <div class="menu">
                <el-menu :default-active="activeIndex" class="el-menu" mode="horizontal" @select="handleSelect">
                    <el-menu-item index="1" @click="toListNavigation('admin')">管理员界面</el-menu-item>
                    <el-menu-item index="2" @click="toListNavigation('userSpace')">用户空间</el-menu-item>
                    <el-menu-item index="3" @click="toListNavigation('index')">首页</el-menu-item>
                    <el-menu-item index="4" @click="toListNavigation('common')">通用</el-menu-item>
                    <!-- <el-menu-item index="3" @click="toListNavigation(1)">未审核</el-menu-item> -->
                </el-menu>
            </div>
        </el-header>
        <el-main class="main">
            <div>
                <el-table
                    v-loading="loading"
                    :data="navigations"
                    :border="true"
                    show-overflow-tooltip
                    stripe
                    :highlight-current-row="true"
                    style="width: 100%"
                    :row-style="rowStyle"
                    :max-height="setTableHeight()">
                    <el-table-column fixed prop="id" label="ID" width="80" align="center" />

                    <el-table-column prop="name" label="导航栏标题" width="120" align="center" />
                    <el-table-column label="图标" width="60" align="center">
                        <template #default="scope">
                            <i class="iconfont" v-html="scope.row.icon"></i>
                        </template>
                    </el-table-column>
                    <el-table-column prop="icon" label="图标代码" width="120" align="center" />
                    <el-table-column prop="path" label="路径" align="center" />
                    <el-table-column prop="cssClass" label="CSS类名" align="center" />
                    <el-table-column label="类别" width="120" align="center">
                        <template #default="scope">
                            {{ formatType(scope.row.type) }}
                        </template>
                    </el-table-column>
                    <el-table-column label="所属角色" width="120" align="center">
                        <template #default="scope">
                            {{ formatRole(scope.row.role) }}
                        </template>
                    </el-table-column>

                    <el-table-column prop="parent" label="父导航" width="80" align="center">
                        <template #default="scope">
                            {{ getNavParent(scope.row.parent) }}
                        </template>
                    </el-table-column>
                    <el-table-column prop="index" label="索引顺序" width="100" align="center" />
                    <el-table-column prop="" label="操作" width="200" align="center">
                        <template #default="scope">
                            <el-button type="primary" size="small" plain @click="UpdateNavigation(scope.row)">
                                编辑
                            </el-button>
                            <el-button type="danger" size="small" plain @click="DeleteNavigation(scope.row)">
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
    <!-- 添加 -->
    <el-dialog
        v-model="addNavDialog"
        width="40%"
        center
        :append-to-body="true"
        :show-close="false"
        :lock-scroll="false"
        :destroy-on-close="true"
        :align-center="true">
        <div class="update-dialog-item">
            <el-input clearable type="text" v-model="addNavObj.name">
                <template #prepend>
                    <div class="label"><span style="color: rgb(255, 121, 121)">* </span>名字</div>
                </template>
            </el-input>
        </div>
        <div class="update-dialog-item">
            <el-input clearable type="text" v-model="addNavObj.icon">
                <template #prepend>
                    <div class="label">图标代码</div>
                </template>
            </el-input>
        </div>
        <div class="update-dialog-item">
            <el-input clearable type="text" v-model="addNavObj.path">
                <template #prepend>
                    <div class="label"><span style="color: rgb(255, 121, 121)">* </span>路径</div>
                </template>
            </el-input>
        </div>
        <div class="update-dialog-item">
            <el-input clearable type="text" v-model="addNavObj.cssClass">
                <template #prepend>
                    <div class="label">类名</div>
                </template>
            </el-input>
        </div>
        <div class="update-dialog-item">
            <el-input style="width: 100px">
                <template #prepend>
                    <div class="label"><span style="color: rgb(255, 121, 121)">* </span>父导航</div>
                </template>
            </el-input>
            <el-select class="search-item" v-model="addNavObj.parent" style="width: 160px" placeholder="父导航">
                <el-option v-for="nav in navigations" :value="nav.id" :label="nav.name">{{ nav.name }}</el-option>
            </el-select>
        </div>
        <div class="update-dialog-item">
            <el-input clearable type="number" v-model="addNavObj.index">
                <template #prepend>
                    <div class="label">索引顺序</div>
                </template>
            </el-input>
        </div>
        <div class="update-dialog-item">
            <el-input style="width: 100px">
                <template #prepend>
                    <div class="label"><span style="color: rgb(255, 121, 121)">* </span>类别</div>
                </template>
            </el-input>
            <el-select class="search-item" v-model="addNavObj.type" style="width: 160px" placeholder="类别">
                <el-option value="admin" label="管理员">管理员</el-option>
                <el-option value="userSpace" label="用户空间">用户空间</el-option>
                <el-option value="index" label="首页">首页</el-option>
                <el-option value="common" label="首页">通用</el-option>
            </el-select>
        </div>
        <div class="update-dialog-item">
            <el-input style="width: 100px">
                <template #prepend>
                    <div class="label"><span style="color: rgb(255, 121, 121)">* </span>所属角色</div>
                </template>
            </el-input>
            <el-select class="search-item" v-model="addNavObj.role" style="width: 160px" placeholder="所属角色">
                <el-option value="1" label="管理员">管理员</el-option>
                <el-option value="2" label="文章审核员">文章审核员</el-option>
                <el-option value="3" label="评论审核员">评论审核员</el-option>
                <el-option value="4" label="用户审核员">用户审核员</el-option>
                <el-option value="0" label="普通用户">普通用户</el-option>
            </el-select>
        </div>

        <template #footer>
            <span class="dialog-footer">
                <el-button @click="addNavDialog = false">关 闭</el-button
                ><el-button @click="AddNav()" type="primary">添 加</el-button>
            </span>
        </template>
    </el-dialog>
    <!-- 编辑 -->
    <el-dialog
        v-model="updateNavDialog"
        width="40%"
        center
        :append-to-body="true"
        :show-close="false"
        :lock-scroll="false"
        :destroy-on-close="true"
        :align-center="true">
        <div>
            <div class="update-dialog-item">
                <el-input type="text" v-model="updateNav.id" disabled>
                    <template #prepend>
                        <div class="label">ID</div>
                    </template>
                </el-input>
            </div>
            <div class="update-dialog-item">
                <el-input clearable type="text" v-model="updateNav.name">
                    <template #prepend>
                        <div class="label"><span style="color: rgb(255, 121, 121)">* </span>名字</div>
                    </template>
                </el-input>
            </div>
            <div class="update-dialog-item">
                <el-input clearable type="text" v-model="updateNav.icon">
                    <template #prepend>
                        <div class="label">图标代码</div>
                    </template>
                </el-input>
            </div>
            <div class="update-dialog-item">
                <el-input clearable type="text" v-model="updateNav.path">
                    <template #prepend>
                        <div class="label"><span style="color: rgb(255, 121, 121)">* </span>路径</div>
                    </template>
                </el-input>
            </div>
            <div class="update-dialog-item">
                <el-input clearable type="text" v-model="updateNav.cssClass">
                    <template #prepend>
                        <div class="label">CSS类名</div>
                    </template>
                </el-input>
            </div>
            <div class="update-dialog-item">
                <el-input style="width: 100px">
                    <template #prepend>
                        <div class="label"><span style="color: rgb(255, 121, 121)">* </span>父导航</div>
                    </template>
                </el-input>
                <el-select
                    class="search-item"
                    v-model="updateNav.parent"
                    style="width: 160px"
                    :placeholder="updateNav.parent">
                    <el-option value="" label="无">无</el-option>
                    <el-option v-for="nav in navigations" :value="nav.id" :label="nav.name">{{ nav.name }}</el-option>
                </el-select>
            </div>
            <div class="update-dialog-item">
                <el-input clearable type="number" v-model="updateNav.index">
                    <template #prepend>
                        <div class="label">索引顺序</div>
                    </template>
                </el-input>
            </div>
            <div class="update-dialog-item">
                <el-input style="width: 100px">
                    <template #prepend>
                        <div class="label"><span style="color: rgb(255, 121, 121)">* </span>类别</div>
                    </template>
                </el-input>
                <el-select class="search-item" v-model="updateNav.type" style="width: 160px" placeholder="类别">
                    <el-option value="admin" label="管理员">管理员</el-option>
                    <el-option value="userSpace" label="用户空间">用户空间</el-option>
                    <el-option value="index" label="首页">首页</el-option>
                    <el-option value="common" label="首页">通用</el-option>
                </el-select>
            </div>
            <div class="update-dialog-item">
                <el-input style="width: 100px">
                    <template #prepend>
                        <div class="label"><span style="color: rgb(255, 121, 121)">* </span>所属角色</div>
                    </template>
                </el-input>
                <el-select class="search-item" v-model="updateNav.role" style="width: 160px" placeholder="所属角色">
                    <el-option :value="1" label="管理员">管理员</el-option>
                    <el-option :value="2" label="文章审核员">文章审核员</el-option>
                    <el-option :value="3" label="评论审核员">评论审核员</el-option>
                    <el-option :value="4" label="用户审核员">用户审核员</el-option>
                    <el-option :value="0" label="普通用户">普通用户</el-option>
                </el-select>
            </div>
        </div>

        <template #footer>
            <span class="dialog-footer">
                <el-button @click="updateNavDialog = false">关 闭</el-button>
                <el-button @click="UpdateNav()" type="primary">修 改</el-button>
            </span>
        </template>
    </el-dialog>
</template>
<script setup>
import { listNavigations, addNavigation, updateNavigation, deleteNavigation } from "@/api/Admin";
import { ref, onMounted, watch } from "vue";
import router from "@/router";
import { useRoute } from "vue-router";
import { ElMessage, ElMessageBox } from "element-plus";
let useRouter = useRoute();
const activeIndex = ref("1");
const handleSelect = (key, keyPath) => {
    activeIndex.value = key;
};
let navigations = ref([]);
/* 搜索 */
let navId = ref(null);
let name = ref(null);
let role = ref(null);
let isSearch = ref(false);
/* 分页查询 */
let total = ref(1); //总页数
let currPage = ref(1); //当前页
let pageSize = ref(15); //分页大小
let loading = ref(true);
let navType = ref("admin");
function toListNavigation(type) {
    currPage.value = 1;
    navType.value = type;
    router.push({
        path: "/manage/navigation",
        query: {
            activeIndex: activeIndex.value,
            type: navType.value,
            page: currPage.value,
            navId: navId.value != null && navId.value != "" ? parseInt(navId.value) : null,
            title: name.value,
            role: role.value,
        },
    });
}

/* 搜索筛选 */
function toSearch() {
    currPage.value = 1;
    search();
}
function search() {
    loading.value = true;
    if (isSearch.value == false) currPage.value = 1;
    isSearch.value = true;
    listNavigations({
        currPage: currPage.value,
        pageSize: pageSize.value,
        id: navId.value,
        name: name.value,
        role: role.value,
        type: navType.value,
    }).then((res) => {
        if (res.data.code == 1) {
            loading.value = false;
            navigations.value = res.data.data.list;
            total.value = res.data.data.total;
            navigations.value.lastIndex;
            addNavObj.value.index = navigations.value[navigations.value.length - 1].index + 1;
        } else {
            ElMessage.error("获取数据失败");
            loading.value = false;
        }
    });
}

let updateNavDialog = ref(false);
/* 编辑导航栏 */
let updateNav = ref({});
function UpdateNavigation(nav) {
    updateNavDialog.value = true;
    updateNav.value = Object.assign({}, nav);
}
function UpdateNav() {
    if (updateNav.value.name == "" || updateNav.value.path.trim() == "" || updateNav.value.role == null) {
        ElMessage.error("请填写完整信息");
        return;
    }
    updateNavigation(updateNav.value).then((res) => {
        if (res.data.code == 1) {
            ElMessage.success("修改成功");
            updateNavDialog.value = false;
            search();
        } else {
            ElMessage.error("修改失败");
        }
    });
}
/* 添加导航栏 */
let addNavDialog = ref(false);
let addNavObj = ref({
    name: "",
    icon: "",
    path: "",
    cssClass: "",
    type: "admin",
    role: "1",
    parent: "",
});
function AddNav() {
    if (addNavObj.value.name == "" || addNavObj.value.path.trim() == "" || addNavObj.value.role == null) {
        ElMessage.error("请填写完整信息");
        return;
    }
    addNavigation(addNavObj.value).then((res) => {
        if (res.data.code == 1) {
            ElMessage.success("添加成功");
            addNavDialog.value = false;
            addNavObj.value = {
                name: "",
                icon: "",
                path: "",
                cssClass: "",
                type: "admin",
                role: "1",
            };
            search();
        } else {
            ElMessage.error("添加失败");
        }
    });
}

/* 删除导航栏 */
async function DeleteNavigation(nav) {
    await ElMessageBox.confirm("您确定删除该导航栏？", {
        confirmButtonText: "删除",
        cancelButtonText: "取消",
        type: "error",
        lockScroll: false,
        center: true,
    })
        .then(() => {
            deleteNavigation(nav).then((res) => {
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
/* 格式化角色 */
function formatRole(data) {
    if (data == 0) return "普通用户";
    else if (data == 1) return "管理员";
    else if (data == 2) return "文章审核员";
    else if (data == 3) return "评论审核员";
    else if (data == 4) return "用户审核员";
}
/* 格式化类型 */
function formatType(params) {
    if (params == "admin") return "管理员";
    else if (params == "userSpace") return "用户空间";
    else if (params == "index") return "首页";
}
/* 获取父导航名字 */
function getNavParent(id) {
    return navigations.value.filter((nav) => nav.id == id).map((nav) => nav.name)[0];
}
/* 换页操作 */
let currentChange = (page) => {
    currPage.value = page;
    router.push({
        path: "/manage/navigation",
        query: {
            activeIndex: activeIndex.value,
            page: page,
            navId: navId.value != null && navId.value != "" ? parseInt(navId.value) : null,
            title: name.value,
            role: role.value,
        },
    });
};
/* 清空搜索 */
function clearAll() {
    navId.value = "";
    name.value = "";
    role.value = "";
    search();
}
/* 路由后还原数据 */
function setSearchData() {
    if (useRouter.query.activeIndex != null) {
        activeIndex.value = useRouter.query.activeIndex;
    }
    if (useRouter.query.page != null) {
        currPage.value = parseInt(useRouter.query.page);
    }
    if (useRouter.query.navId != null) {
        navId.value = parseInt(useRouter.query.navId);
    }
    if (useRouter.query.title != null) {
        name.value = useRouter.query.title;
    }
    if (useRouter.query.role != null) {
        role.value = useRouter.query.role;
    }
    if (useRouter.query.type != null) {
        navType.value = useRouter.query.type;
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
.update-dialog-item {
    margin-bottom: 20px;
}
.label {
    font-weight: 700;
    width: 60px;
    text-align: center;
}
.label-r {
    font-weight: 700;
    width: 60px;
    text-align: center;
    margin-bottom: 10px;
}
.menu {
}
.el-menu {
}
.main {
    padding-top: 0;
}
.iconfont {
    font-style: normal;
    font-family: "iconfont" !important;
    margin-right: 4px;
}
</style>
