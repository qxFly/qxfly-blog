<template>
    <div class="user_info" @click="toUserSpace()">
        <div>
            <img :src="user.avatar" :alt="user.username" />
        </div>
        <div class="name">{{ user.username }}</div>
    </div>
</template>

<script setup>
import md5 from "js-md5";
import { onMounted, onUnmounted, ref } from "vue";
import router from "@/router";
let timer;
var user = ref({
    username: "",
    avatar: require("@/assets/avatar.png"),
});
// 获取用户名
function getUsername() {
    let a = sessionStorage.getItem("useravatar");
    let username = sessionStorage.getItem("username");
    let uid = sessionStorage.getItem("uid");
    user.value.avatar = a != null ? a : require("@/assets/avatar.png");
    user.value.username = username != null ? username : "未登入";
    user.value.id = uid != null ? uid : null;
}
// 跳转到用户空间
function toUserSpace() {
    if (localStorage.getItem(md5("token")) != null && localStorage.getItem(md5("token")) != md5("nologin")) {
        if (router.currentRoute.value.path.match(/space/) == null) {
            router.push({
                path: "/user/space",
                query: {
                    uid: user.value.id,
                },
            });
        }
    } else {
        router.push("/login");
    }
}

onMounted(() => {
    timer = setTimeout(() => {
        getUsername();
    }, 200);
    timer = setTimeout(() => {
        getUsername();
    }, 1000);
});
onUnmounted(() => {
    clearTimeout(timer);
});
</script>

<style scoped lang="less">
.user_info {
    display: flex;
    justify-content: center;
    align-items: center;
    cursor: pointer;
    transition: all 0.3s ease;
}
img {
    width: 54px;
    height: 54px;
    margin: 4px 0 0 0;
    border-radius: 50%;
    object-fit: cover;
    transition: all 0.3s ease;
}
.user_info:hover {
    img {
        transform: scale(1.1);
    }
}
.name {
    margin-left: 10px;
    text-align: center;
    font-size: 18px;
    line-height: 30px;
    font-weight: 700;
    border-bottom: 2px solid white;
    overflow: hidden;
    color: #000;
}
@media (max-width: 700px) {
    .user_info {
        // width: 100%;
    }
    .name {
        display: none;
        margin-left: 5px;
        font-size: 14px;
    }
}
</style>
