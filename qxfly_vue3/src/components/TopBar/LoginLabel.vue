<template>
    <div class="user_login">
        <div v-if="islogin() == md5('false') || islogin() == null">
            <router-link class="nav-item login" to="/login">登录</router-link>
        </div>
        <div v-if="islogin() == md5('false') || islogin() == null">
            <router-link class="nav-item register" to="/register">注册</router-link>
        </div>
        <div v-if="islogin() == md5('true')">
            <a class="nav-item logout" @click="toLogout">退出</a>
        </div>
    </div>
    <!-- 退出 -->
    <el-dialog
        title="是否退出"
        v-model="dialogLogoutVisible"
        :append-to-body="true"
        :close-on-click-modal="false"
        :show-close="false"
        :lock-scroll="false"
        center
        :width="adjustWidth()">
        <template #footer>
            <span class="dialog-footer">
                <el-button @click="cencel">取 消</el-button>
                <el-button type="primary" @click="logout">确 定</el-button>
            </span>
        </template>
    </el-dialog>
</template>

<script setup>
import { Logout } from "@/api/index";
import md5 from "js-md5";
import { ref } from "vue";
import router from "@/router";
var dialogLogoutVisible = ref(false); // 退出弹窗

/* 前往退出 */
function toLogout() {
    /* 添加监听按键事件 */
    window.addEventListener("keydown", keyDown);
    dialogLogoutVisible.value = true;
}

/* 关闭 退出 页面 */
function cencel() {
    /* 销毁监听按键事件 */
    window.removeEventListener("keydown", keyDown, false);
    dialogLogoutVisible.value = false;
}

/* 退出 */
async function logout() {
    const res = await Logout();
    localStorage.clear();
    sessionStorage.clear();
    dialogLogoutVisible.value = false;
    setTimeout(() => {
        location.reload();
    }, 200);
    router.replace("/");
}

/* 检查登录状态*/
function islogin() {
    return localStorage.getItem(md5("islogin"));
}

// 监听回车键执行事件
function keyDown(e) {
    // 回车则执行登录方法 enter键的ASCII是13
    if (e.keyCode === 13) {
        if (dialogLogoutVisible.value) {
            logout(); // 需要执行的方法方法
        }
    }
}
function adjustWidth() {
    let width = document.body.clientWidth;
    if (width <= 700) {
        return "100%";
    } else {
        return "20%";
    }
}
</script>

<style scoped>
.user_login {
    display: flex;
    justify-content: center;
    align-items: center;
    user-select: none;
}
.nav-item {
    display: block;
    margin: 0 8px;
    font-size: 18px;
    /* font-weight: bold; */
    border: none;
    background-color: #00000000;
    height: min-content;
    cursor: pointer;
    color: #000000;
}
.nav-item:hover {
    border-bottom: 1px solid #000000;
}
label {
    font-size: 18px;
}
.login::before {
    content: "\e62e";
    font-family: "iconfont";
    margin-right: 4px;
}
.register::before {
    content: "\e6dd";
    font-family: "iconfont";
    margin-right: 4px;
}
.logout::before {
    content: "\e600";
    font-family: "iconfont";
    margin-right: 4px;
}
@media (max-width: 1100px) {
    .nav-item {
        width: max-content;
        font-size: 14px;
        margin: 0;
        padding: 0;
    }
    .login::before {
        content: "";
    }
    .register::before {
        content: "";
    }
    .logout::before {
        content: "";
    }
}
</style>
