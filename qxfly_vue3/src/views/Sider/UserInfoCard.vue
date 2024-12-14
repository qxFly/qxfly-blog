<template>
    <CardView>
        <div class="user-info">
            <div class="user-img">
                <div class="img">
                    <img :src="userCardInfo.avatar" alt="" />
                </div>
                <div class="user-name">{{ userCardInfo.username }}</div>
                <div class="user-introduction">{{ userCardInfo.introduction }}</div>
            </div>
            <div class="user-info-data">
                <div class="user-info-data-item">
                    <div class="item-btn" @click="toUserArticle">
                        <div class="item-title">Articles</div>
                        <div class="item-data">{{ userCardInfo.articles }}</div>
                    </div>
                </div>
                <div class="user-info-data-item">
                    <div class="item-btn" @click="toUserCollections">
                        <div class="item-title">Collections</div>
                        <div class="item-data">{{ userCardInfo.collection }}</div>
                    </div>
                </div>
                <div class="user-info-data-item">
                    <div class="item-btn" @click="toUserArticle">
                        <div class="item-title">Likes</div>
                        <div class="item-data">{{ userCardInfo.likes }}</div>
                    </div>
                </div>
            </div>
            <div class="user-info-btn" @click="toUserInfoPage" v-if="!props.backToIndex && islogin()">
                个 &emsp;人 &emsp;空 &emsp;间
            </div>
            <div class="user-info-btn" @click="toIndex" v-if="props.backToIndex">Back to Index</div>
        </div></CardView
    >
</template>

<script setup>
import { onMounted, ref } from "vue";
import { getUserInfo } from "@/api/User/index";
import router from "@/router";
import md5 from "js-md5";

const props = defineProps({
    backToIndex: {
        type: Boolean,
        default: false,
    },
});
const userCardInfo = ref({
    username: "",
    introduction: "",
    avatar: require("@/assets/avatar.png"),
    articles: 0,
    tags: 0,
    likes: 0,
    collection: 0,
});
function islogin() {
    return localStorage.getItem(md5("islogin")) == md5("true") ? true : false;
}
function GetUserInfo() {
    let token = localStorage.getItem(md5("token"));
    if (token != md5("nologin")) {
        getUserInfo({ token }).then((res) => {
            userCardInfo.value = res.data.data;
            if (userCardInfo.value.avatar == "" || userCardInfo.value.avatar == null) {
                userCardInfo.value.avatar = require("@/assets/avatar.png");
            }
        });
    }
}

function toUserInfoPage() {
    if (localStorage.getItem(md5("token")) != null && localStorage.getItem(md5("token")) != md5("nologin")) {
        router.push({
            path: "/user/space",
            query: {
                uid: userCardInfo.value.id,
            },
        });
    }
}
function toUserArticle() {
    if (localStorage.getItem(md5("token")) != null && localStorage.getItem(md5("token")) != md5("nologin"))
        router.push({
            path: "/user/space/userArticle",
            query: {
                uid: userCardInfo.value.id,
            },
        });
}
function toUserCollections() {
    if (localStorage.getItem(md5("token")) != null && localStorage.getItem(md5("token")) != md5("nologin"))
        router.push("/user/space/userCollection?page=1&uid=" + userCardInfo.value.id);
}
function toIndex() {
    router.push({
        path: "/articleView",
        query: {
            page: 1,
        },
    });
}

onMounted(() => {
    GetUserInfo();
});
</script>

<style scoped>
.user-info {
    width: 100%;
}
.user-img {
    margin-top: 16px;
    text-align: center;
}
.user-img .img {
    width: 100px;
    height: 100px;
    border-radius: 50%;
    overflow: hidden;
    margin: 0 auto;
}
.user-img img {
    width: 100%;
    height: 100%;
    object-fit: cover;
}
.user-name {
    margin-top: 10px;
    font-size: 20px;
    font-weight: 600;
}
.user-introduction {
    margin-top: 8px;
    font-size: 14px;
}
.user-info-data {
    margin-top: 14px;
    display: flex;
    align-items: center;
    justify-content: center;
}
.user-info-data-item {
    margin: 0 20px;
    user-select: none;
    transition: all 0.3s ease;
}
.item-btn {
    display: flex;
    flex-direction: column;
    justify-content: center;
    align-items: center;
    cursor: pointer;
}
.item-title {
    font-size: 14px;
}
.item-data {
    font-size: 24px;
}

.user-info-btn {
    margin: 14px 0;
    padding: 6px 4px;
    text-align: center;
    border-radius: 4px;
    cursor: pointer;
    background-color: #8fcbff;
    color: #ffffff;
    font-weight: 600;
}
.user-info-btn:hover {
    background-color: #ff8e68;
}
@media (max-width: 1200px) {
    .user-info-data-item {
        margin: 0 10px;
    }
}
</style>
