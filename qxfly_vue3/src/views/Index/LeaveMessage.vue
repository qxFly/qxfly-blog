<template>
    <div class="leave-message-main">
        <div class="leave-message-title">留言板</div>
        <div class="leave-message-input">
            <textarea
                placeholder="请输入留言"
                v-model="lmsgObj.content"
                @keyup.enter="sendLeaveMessage"
                maxlength="200"></textarea>
            <div class="leave-message-input-btn" @click="SendLeaveMessage">发送</div>
        </div>
        <div class="leave-message-list">
            <CardView class="leave-message-item" v-for="item in lmsg" :key="item.id">
                <div class="leave-message-left-avatar">
                    <img :src="setAvatar(item.avatar)" alt="" />
                </div>
                <div class="leave-message-right-content">
                    <div class="leave-message-right-content-header">
                        <div class="leave-message-right-content-header-right-username">
                            {{ item.username }}
                        </div>
                        <div class="leave-message-right-content-header-right">
                            <div
                                class="leave-message-right-content-operate"
                                @click="DeleteLeaveMessage(item.id)"
                                v-if="isShowDel(item.username)">
                                删除
                            </div>
                            <div class="leave-message-right-content-header-date" v-text="formatDate(item.date)"></div>
                        </div>
                    </div>
                    <div class="leave-message-right-content-content">{{ item.content }}</div>
                </div>
            </CardView>
        </div>
        <pagination v-model="currPage" :totalPages="totalPages" @changePage="changePage"></pagination>
    </div>
</template>

<script setup>
import { onMounted, onUnmounted, ref, watch } from "vue";
import { listLeaveMessage, sendLeaveMessage, deleteLeaveMessage } from "@/api";
import CardView from "@/components/CardView.vue";
import pagination from "@/components/Pagination.vue";
import { useRoute } from "vue-router";
import router from "@/router";
import md5 from "js-md5";
import { ElMessageBox } from "element-plus";
let useRouter = useRoute();
let lmsg = ref([]);
let totalPages = ref(0);
let currPage = ref(1);
let pageSize = ref(20);
/* 获取留言 */
function getLeaveMessage() {
    listLeaveMessage({ currPage: currPage.value, pageSize: pageSize.value }).then((res) => {
        if (res.data.code != 1) return;
        lmsg.value = res.data.data.list;
        totalPages.value = res.data.data.pages;
        Backtop();
    });
}
/* 留言对象 */
let lmsgObj = ref({
    username: localStorage.getItem("username"),
    content: "",
});
/* 发送留言 */
function SendLeaveMessage() {
    if (lmsgObj.value.content.trim() == "") return;
    sendLeaveMessage(lmsgObj.value).then((res) => {
        if (res.data.code != 1) return;
        lmsgObj.value.content = "";
        getLeaveMessage();
    });
}
/* 删除留言 */
function DeleteLeaveMessage(id) {
    ElMessageBox.confirm("是否确认删除?", "提示", {
        confirmButtonText: "确认",
        cancelButtonText: "取消",
        type: "warning",
    })
        .then((res) => {
            deleteLeaveMessage(id).then((res) => {
                if (res.data.code != 1) return;
                getLeaveMessage();
            });
        })
        .catch((res) => {});
}

/* 是否显示删除 */
function isShowDel(username) {
    return localStorage.getItem("username") == username;
}
/* 换页操作 */
function changePage(page = 0) {
    if (totalPages.value != 0 && page != 0) {
        if (page > totalPages.value) {
            page = totalPages.value;
        }
        let params = {
            page: page,
        };
        router.push({
            path: "/index/leaveMessage",
            query: params,
        });
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
}
watch(
    routePage.value,
    (newVal, oldVal) => {
        setRoutePage();
        getLeaveMessage();
    },
    {
        deep: true,
    }
);
/* 每次进入界面回到顶部 */
function Backtop() {
    let backTop = document.getElementById("backTop");
    if (backTop != null) backTop.click();
}
function setAvatar(item) {
    return item ? item : require("@/assets/avatar.png");
}
function formatDate(date) {
    return new Date(date).toLocaleString();
}
onMounted(() => {
    setRoutePage();
    getLeaveMessage();
});
onUnmounted(() => {});
</script>

<style scoped>
.leave-message-title {
    font-weight: 700;
    font-size: 26px;
    margin: 0 0 30px 0;
    width: 500px;
    border-bottom: 1px solid #000;
    text-overflow: ellipsis;
    white-space: nowrap;
    overflow: hidden;
}
.leave-message-input {
    display: flex;
    margin: 20px 0;
    align-items: center;
    gap: 10px;
}
.leave-message-input textarea {
    min-height: 40px;
    max-height: 100px;
    flex: 1;
    border-radius: 4px;
    font-size: 16px;
    padding: 10px;
    outline: none;
    border: 2px solid #b6ddff;
}
.leave-message-input-btn {
    border-radius: 4px;
    font-size: 16px;
    padding: 4px 12px;
    transition: all 0.3s ease;
    background-color: #84c6ff;
    border: none;
    color: #fff;
}
.leave-message-input-btn:hover {
    background-color: #ff9977;
}
.leave-message-input textarea:focus {
    outline: #84c6ff;
}
.leave-message-list {
}
.leave-message-item {
    width: 100%;
    display: flex;
    padding: 10px;
}
.leave-message-left-avatar {
    /* border-right: 1px solid #ccc; */
}
.leave-message-left-avatar img {
    width: 40px;
    height: 40px;
    border-radius: 50%;
    margin-right: 10px;
}
.leave-message-right-content {
    width: 100%;
    padding: 0 10px;
}
.leave-message-right-content-header {
    display: flex;
    justify-content: space-between;
    border-bottom: 1px solid #ccc;
    padding: 4px 0;
    font-size: 14px;
    font-weight: bold;
    color: #6cbaff;
}
.leave-message-right-content-header-right-username {
}
.leave-message-right-content-header-right {
    display: flex;
}
.leave-message-right-content-operate {
    color: #ff9a9a;
    margin: 0 12px;
    cursor: pointer;
    border: 1px solid #ffffff;
    padding: 0 4px;
    border-radius: 4px;
    line-height: 16px;
}
.leave-message-right-content-operate:hover {
    color: rgb(245, 108, 108);
    border: 1px solid #ff9a9a;
}
.leave-message-right-content-content {
    padding: 4px 0;
}
</style>
