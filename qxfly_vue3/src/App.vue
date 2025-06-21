<template>
    <!-- 背景主体 -->
    <TopBar></TopBar>
    <div id="index_bg" class="theme-bg index_bg"></div>
    <router-view></router-view>
</template>

<script setup>
import { getSysSetting } from "@/api/System";
import router from "@/router";
import { onBeforeMount, onMounted, onBeforeUnmount, onUnmounted, onRenderTracked, ref } from "vue";
import md5 from "js-md5";
import { getUserInfo } from "@/api/User";
import * as socketUtil from "@/utils/Socket";
import { getUserSettings } from "@/api/User";
import TopBar from "@/components/TopBar/TopBar.vue";
import { onBeforeRouteUpdate } from "vue-router";
let token = localStorage.getItem(md5("token"));
let user = ref({
    id: getUUID(),
    username: "游客" + getUUID(),
    avatar: require("@/assets/avatar.png"),
});
/* 设置初始状态 */
function setInitStatue() {
    localStorage.clear();
    sessionStorage.clear();
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
    if (token == null) {
        clearLoginStatue();
        return;
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
/* 初始化主题 */
async function initTheme() {
    await GetUserSettings();
    await GetSysSettings();
    compactSetting();
    setBackgroundImage();
    setFontColor();
}
/* 获取用户设置 */
let userSettingObj = ref({});
async function GetUserSettings() {
    let uid = ref(localStorage.getItem("uid"));
    if (uid.value == null) return;
    await getUserSettings(uid.value).then((res) => {
        if (res.data.code != 1) return;
        userSettingObj.value = res.data.data;
    });
}
/* 获取系统设置 */
let sysSettingObj = ref({});
async function GetSysSettings() {
    await getSysSetting().then((res) => {
        if (res.data.code != 1) return;
        sysSettingObj.value = res.data.data;
    });
}
function compactSetting() {
    for (let key in sysSettingObj.value) {
        if (userSettingObj.value[key] != null) {
            sysSettingObj.value[key] = userSettingObj.value[key];
        }
    }
}
/* 设置字体颜色 */
function setFontColor() {
    document.documentElement.style.setProperty("--main-theme-font-color", sysSettingObj.value.fontColor);
}
/* 设置背景 */
async function setBackgroundImage() {
    let bg = document.getElementById("index_bg");
    let localbgimg = localStorage.getItem("bgimg");
    // 检查是否开启背景
    if (sysSettingObj.value.bgSwitch == 0) return;
    // 检查是否设置背景图片
    let onlineBg = sysSettingObj.value.bgImgPath;
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
    bg.style.filter = "blur(" + sysSettingObj.value.bgBlur + "px)";
    bg.style.backdropFilter = "blur(" + sysSettingObj.value.bgBlur + "px)";
}

onBeforeMount(() => {
    if (token != null) {
        let sa = sessionStorage.getItem("autologin");
        let la = localStorage.getItem("autologin");
        /* 如果其中一个存在则，无需退出登录，否则退出登录。记住密码时放在localStorage，否则放在sessionStorage */
        if (sa == null && la == null) {
            clearLoginStatue();
        }
    }
});
onMounted(async () => {
    initTheme();
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

        setInterval(async () => {
            // await checkStatus();
        }, 100000);
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
<style>
/* body {
    color: var(--main-theme-font-color);
}
div {
    color: var(--main-theme-font-color);
} */
:root {
    --bg-blur: blur(10px);
    --main-background-color: #ffffff3b; /* 主背景颜色 */
    --main-theme-color-blue: #84c6ff; /* 主题颜色-蓝 */
    --main-theme-color-orange: #ff8e68; /* 主题颜色-橙 */
    --main-theme-color-purple: #a28dd1; /* 主题颜色-紫 */
    --main-theme-font-color: #242424; /* 主题颜色-紫 */
    --hover-color-blue: #b9deff80; /* 鼠标悬浮颜色-蓝 */
    --hover-color-orange: #ffded499; /* 鼠标悬浮颜色-橙 */
    --hover-color-purple: #a38dd1da; /* 鼠标悬浮颜色-紫 */
    --input-background-color: #f6f8fa80; /* 输入框背景颜色 */
}
</style>
