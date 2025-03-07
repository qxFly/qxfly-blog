<template>
    <CardView>
        <div class="user-info">
            <div class="user-img">
                <div class="img">
                    <img :src="userCardInfo.avatar" alt="" @click="toUserInfoPage(userCardInfo.id)" />
                </div>
                <div class="user-name" @click="toUserInfoPage(userCardInfo.id)">{{ userCardInfo.username }}</div>
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
            <div v-if="props.btnType == 'detail-message' && uid != userCardInfo.id">
                <div class="user-info-btn" @click="toUserInfoPage(userCardInfo.id)">详 &emsp;细 &emsp;信 &emsp;息</div>
                <div class="user-info-btn" @click="sendMessageToUser(userCardInfo.id)">私 信</div>
            </div>
            <div
                class="user-info-btn"
                @click="toUserInfoPage(userCardInfo.id)"
                v-if="props.btnType == 'detail-message' && uid == userCardInfo.id">
                详 &emsp;细 &emsp;信 &emsp;息
            </div>
            <div class="user-info-btn" @click="toUserInfoPage(userCardInfo.id)" v-if="props.btnType == 'detail'">
                详 &emsp;细 &emsp;信 &emsp;息
            </div>
            <div class="user-info-btn" @click="toLogin()" v-if="props.btnType == 'login' && !islogin()">
                登 &emsp;录
            </div>
            <div
                class="user-info-btn"
                @click="toIndex"
                v-if="props.btnType == 'index' || props.btnType == 'index-message'">
                返 &emsp;回 &emsp;首 &emsp;页
            </div>
            <div
                class="user-info-btn"
                @click="sendMessageToUser(userCardInfo.id)"
                v-if="props.btnType == 'index-message' && uid != userCardInfo.id">
                私 信
            </div>
        </div>
    </CardView>
</template>

<script setup>
import { onMounted, onUnmounted, ref, watchEffect } from "vue";
import { getUserInfo } from "@/api/User/index";
import router from "@/router";
import md5 from "js-md5";
import CardView from "@/components/CardView.vue";
let uid = ref(sessionStorage.getItem("uid"));
const props = defineProps({
    btnType: {
        type: String,
        default: "none",
    },
    authorId: {
        type: Number,
        default: 0,
    },
});
const refProp = ref(props);
const emit = defineEmits(["load"]);
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
/* 获取文章作者信息 */
function GetUserInfo() {
    if (refProp.value.authorId != 0) {
        getUserInfo({ id: refProp.value.authorId }).then((res) => {
            if (res.data.data != null) {
                userCardInfo.value = res.data.data;
                if (userCardInfo.value.avatar == "" || userCardInfo.value.avatar == null) {
                    userCardInfo.value.avatar = require("@/assets/avatar.png");
                }
                emit("load", true);
            }
        });
    }
}
/* 前往文章作者空间 */
function toUserInfoPage() {
    if (router.currentRoute.value.path.match(/space/) != null) return;
    if (localStorage.getItem(md5("token")) != null) {
        router.push({
            path: "/user/space",
            query: {
                uid: userCardInfo.value.id,
            },
        });
    } else {
        router.push("/login");
    }
}
function toUserArticle() {
    if (localStorage.getItem(md5("token")) != null)
        router.push("/user/space/userArticle?page=1&uid=" + userCardInfo.value.id);
}
function toUserCollections() {
    if (localStorage.getItem(md5("token")) != null)
        router.push("/user/space/userCollection?page=1&uid=" + userCardInfo.value.id);
}
function toLogin() {
    router.push("/login");
}
function toIndex() {
    router.push("/");
}
/* 私信 */
function sendMessageToUser(muid) {
    if (localStorage.getItem(md5("token")) != null) {
        if (router.currentRoute.value.path.match(/space/) != null) {
            router.push("/");
        }
        setTimeout(() => {
            router.push({
                path: "/user/space/userMessage",
                query: {
                    uid: uid.value,
                    muid: muid,
                },
            });
        }, 200);
    } else {
        router.push("/login");
    }
}
watchEffect(GetUserInfo);
onMounted(() => {});
onUnmounted(() => {});
</script>

<style scoped>
.user-info-card {
    width: 100%;
    padding: 16px 10px;
    border-radius: 4px;
    box-shadow: 0 3px 8px 6px rgba(7, 17, 27, 0.08);
    transition: all 0.3s ease;
}
.user-info-card:hover {
    box-shadow: 0 3px 8px 6px rgba(7, 17, 27, 0.14);
}
.user-info {
    width: 100%;
}
.user-img {
    /* margin-top: 16px; */
    text-align: center;
}
.user-img .img {
    width: 100px;
    height: 100px;
    border-radius: 50%;
    overflow: hidden;
    margin: 0 auto;
    cursor: pointer;
}
img:hover {
    transform: scale(1.1);
}
.user-img img {
    width: 100%;
    height: 100%;
    object-fit: cover;
    transition: all 0.3s ease;
}
.user-name {
    margin-top: 10px;
    font-size: 20px;
    font-weight: 600;
    cursor: pointer;
}
.user-name:hover {
    color: #ff8e68;
}
.user-introduction {
    margin-top: 8px;
    font-size: 14px;
    min-height: 14px;
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
    /* transition: all 0.3s ease; */
}
.item-btn:hover {
    color: #ff8e68;
}
.item-title {
    font-size: 14px;
}
.item-data {
    font-size: 24px;
}
.user-info-btn {
    margin: 10px 0;
    padding: 6px 20px;
    text-align: center;
    border-radius: 4px;
    cursor: pointer;
    background-color: #84c6ff;
    color: #ffffff;
    font-size: 14px;
}
.user-info-btn:hover {
    background-color: #ff8e68;
}
@media (max-width: 1200px) {
    .user-info-data-item {
        margin: 0 10px;
    }
}
@media (max-width: 700px) {
    .user-info-btn {
        margin: 4px 0;
    }
    .user-info-card {
        /* padding: 5px 0; */
    }
}
</style>
