<template>
    <router-view></router-view>
</template>

<script setup>
import { updateLoginStatue } from "./api";
import router from "@/router";
import { onBeforeMount, onMounted, onBeforeUnmount, onUnmounted, onRenderTracked, ref } from "vue";
import md5 from "js-md5";
import { getUserInfo } from "@/api/User";

function clearLoginStatue() {
    localStorage.clear();
    localStorage.setItem(md5("islogin"), md5("false"));
    localStorage.setItem("username", "未登录");
    localStorage.removeItem(md5("token"));
    router.replace({
        path: "/articleView?page=1",
    });
    location.reload();
}
let user = ref({
    username: "未登录",
    avatar: require("@/assets/avatar.png"),
});
/* 保存用户名和头像，防止重复发送获取请求 */
async function setUsernameAndAvatar() {
    var uid = localStorage.getItem("uid");
    if (uid != null) {
        await getUserInfo({ id: uid }).then((res) => {
            if (res.data.code == 1) {
                user.value = res.data.data;
                if (user.value.avatar == "" || user.value.avatar == null) {
                    user.value.avatar = require("@/assets/avatar.png");
                }
            } else {
                user.value.username = "未登录";
                user.value.avatar = require("@/assets/avatar.png");
                user.value.id = null;
            }
        });
    }
    sessionStorage.setItem("username", user.value.username);
    sessionStorage.setItem("useravatar", user.value.avatar);
    sessionStorage.setItem("uid", user.value.id);
}
async function checkStatus() {
    let token = localStorage.getItem(md5("token"));
    await updateLoginStatue({
        username: localStorage.getItem("username"),
        token: token,
    }).then((ress) => {
        if (ress.data.code == 1) {
            if (ress.data.data == null) {
                clearLoginStatue();
                /* !ok 表示需要续期，替换成新的token */
            } else if (ress.data.data != "ok") {
                localStorage.setItem(md5("token"), ress.data.data);
                location.reload();
            } else {
                console.log("登录！");
            }
        } else {
            clearLoginStatue();
        }
    });
}
onBeforeMount(() => {
    let log = localStorage.getItem("autologin");
    let ses = sessionStorage.getItem("a");
    if (log == null) {
        localStorage.setItem("autologin", false);
    } else if (log != "true" && ses == null) {
        clearLoginStatue();
    }
});

onMounted(() => {
    let token = localStorage.getItem(md5("token"));

    /* 初次访问设置初始状态 */
    if (localStorage.getItem("username") == null) {
        localStorage.clear();
        localStorage.setItem(md5("islogin"), md5("false"));
        localStorage.setItem("username", "未登录");
        window.location.reload();
    }
    if (localStorage.getItem(md5("islogin")) == md5("true")) {
        console.log("用户登录。检查登录状态");
        /* 检查用户登录状态 */
        if (token == null) token = null;
        checkStatus();
        setInterval(() => {
            checkStatus();
        }, 10000);
    }

    setUsernameAndAvatar();
});
</script>
