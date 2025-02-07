<template>
    <!-- 背景主体 -->
    <TopBar></TopBar>
    <div id="index_bg" class="theme-bg index_bg"></div>
    <router-view></router-view>
</template>

<script setup>
import { updateLoginStatue } from "./api";
import router from "@/router";
import { onBeforeMount, onMounted, onBeforeUnmount, onUnmounted, onRenderTracked, ref } from "vue";
import md5 from "js-md5";
import { getUserInfo } from "@/api/User";
import * as socketUtil from "@/utils/Socket";
import { getUserSettings } from "@/api/User";
import TopBar from "@/components/TopBar/TopBar.vue";
let user = ref({
    id: getUUID(),
    username: "游客" + getUUID(),
    avatar: require("@/assets/avatar.png"),
});
/* 设置初始状态 */
function setInitStatue() {
    localStorage.clear();
    localStorage.setItem(md5("islogin"), md5("false"));
    localStorage.setItem("username", user.value.username);
}
/* 清除登陆状态 */
function clearLoginStatue() {
    setInitStatue();
    router.replace({
        path: process.env.VUE_APP_INDEX_PATH + "?page=1",
    });
    location.reload();
}

/*登录后，保存用户名和头像，防止重复发送获取请求 */
async function setUsernameAndAvatar() {
    var uid = localStorage.getItem("uid");
    if (uid != null) {
        await getUserInfo({ id: uid }).then((res) => {
            if (res.data.code == 1) {
                user.value = res.data.data;
                /* 若用户头像为空，则设置默认头像 */
                if (user.value.avatar == "" || user.value.avatar == null) {
                    user.value.avatar = require("@/assets/avatar.png");
                }
            }
        });
    }
    sessionStorage.setItem("username", user.value.username);
    sessionStorage.setItem("useravatar", user.value.avatar);
    sessionStorage.setItem("uid", user.value.id);
}
/* 检查登录状态 */
async function checkStatus() {
    let token = localStorage.getItem(md5("token"));
    if (token == null) {
        clearLoginStatue();
        return;
    } else {
        await updateLoginStatue().then((ress) => {
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
}
/* 未登录时设置唯一标识 */
function bin2hex(s) {
    var i,
        l,
        o = "",
        n;

    s += "";

    for (i = 0, l = s.length; i < l; i++) {
        n = s.charCodeAt(i).toString(16);
        o += n.length < 2 ? "0" + n : n;
    }

    return o;
}
function getUUID() {
    var canvas = document.createElement("canvas");
    var ctx = canvas.getContext("2d");
    var txt = "qxfly.fun";
    ctx.textBaseline = "top";
    ctx.font = "14px 'Arial'";
    ctx.textBaseline = "tencent";
    ctx.fillStyle = "#f60";
    ctx.fillRect(125, 1, 62, 20);
    ctx.fillStyle = "#069";
    ctx.fillText(txt, 2, 15);
    ctx.fillStyle = "rgba(102, 204, 0, 0.7)";
    ctx.fillText(txt, 4, 17);

    var b64 = canvas.toDataURL().replace("data:image/png;base64,", "");
    var bin = atob(b64);
    var crc = bin2hex(bin.slice(-16, -12));
    return crc;
}

/* 获取用户背景图片 */
let bgimg = ref();
let bgSwitch = ref(0);
let settingObj = ref({});
async function getUserBgImg() {
    let uid = ref(localStorage.getItem("uid"));
    await getUserSettings(uid.value).then((res) => {
        if (res.data.code != 1) return;
        settingObj.value = res.data.data;
    });
}
/* 设置背景 */
async function setBackgroundImage() {
    let bg = document.getElementById("index_bg");
    let localbgimg = localStorage.getItem("bgimg");
    // 获取用户背景图片
    await getUserBgImg();
    // 检查是否开启背景
    if (settingObj.value.bgSwitch == 0) return;
    // 检查是否设置背景图片
    let onlineBg = settingObj.value.bgImgPath;
    if (localbgimg == null && onlineBg != null) {
        localStorage.setItem("bgimg", onlineBg);
    }
    // 检查本地背景是否和云端一致
    if (localbgimg != onlineBg) {
        localbgimg = onlineBg;
        localStorage.setItem("bgimg", onlineBg);
    }
    if (localbgimg == null) {
        return;
    }
    localbgimg = localbgimg.replace(/\\/g, "/");
    bg.style.backgroundImage = "url(" + localbgimg + ")";
    bg.style.filter = "blur(" + settingObj.value.bgBlur + "px)";
    bg.style.backdropFilter = "blur(" + settingObj.value.bgBlur + "px)";
    // bg.style.webkitBackdropFilter = "blur(" + settingObj.value.bgBlur + "px)";
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
onMounted(async () => {
    /* 初次访问设置初始状态 */
    if (localStorage.getItem("username") == null) {
        console.log("用户未登录");
        setInitStatue();
        window.location.reload();
    }
    if (localStorage.getItem(md5("islogin")) == md5("true")) {
        console.log("用户登录。检查登录状态");
        /* 检查用户登录状态 */
        await checkStatus();
        await setUsernameAndAvatar();
        setBackgroundImage();
        setInterval(async () => {
            await checkStatus();
        }, 10000);
        // 初始化socket
        socketUtil.initWebsocket();
    }
});
</script>
<style scoped>
/* 主背景 */
.index_bg {
    width: 100%;
    height: 100%;
    position: fixed;
    background-size: cover;
    background-repeat: no-repeat;
    z-index: -99999;
}
.index_bg::before {
    content: "";
    position: absolute;
    top: 0;
    left: 0;
    right: 0;
    bottom: 0;
    z-index: -1;
    /* filter: var(--bg-blur);
    -webkit-backdrop-filter: var(--bg-blur);
    backdrop-filter: var(--bg-blur); */
    background-size: cover;
    background-repeat: no-repeat;
}
</style>
