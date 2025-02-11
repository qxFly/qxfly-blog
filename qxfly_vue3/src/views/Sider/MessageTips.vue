<template>
    <CardView v-if="messageList.length > 0">
        <div class="sider-hot-article-label">消息</div>
        <div class="main">
            <div class="items">
                <div class="item" v-for="(msg, index) in messageList" :key="index" @click="toUserMeaasge(msg)">
                    <div class="item-col">
                        <div class="item-col-username">{{ msg.username }}：</div>
                        <div class="item-col-msg">{{ msg.content }}</div>
                    </div>
                </div>
            </div>
        </div>
    </CardView>
</template>
<script setup>
import { ref, onMounted, onUnmounted } from "vue";
import { getUserInfo } from "@/api/User/index";
import router from "@/router";
import CardView from "@/components/CardView.vue";
import md5 from "js-md5";
import * as socket from "@/utils/Socket";
let messageList = ref([]);
/* 监听消息 */
function listenSocket(event) {
    let msgObj = JSON.parse(event.data);
    if (msgObj.fromUid == null && msgObj.fromUid == msgObj.toUid) return;
    getUserInfo({ id: msgObj.fromUid }).then((res) => {
        if (res.data.code != 1) return;
        let fromUserInfo = res.data.data;
        msgObj.avatar = fromUserInfo.avatar;
        msgObj.username = fromUserInfo.username;
        if (messageList.value.length > 5) {
            messageList.value.shift();
        }
        messageList.value.push(msgObj);
    });
}
/* 查看消息 */
function toUserMeaasge(msg) {
    router.push({
        path: "/user/space/userMessage",
        query: {
            uid: msg.toUid,
            muid: msg.fromUid,
        },
    });
}
onMounted(() => {
    let token = localStorage.getItem(md5("token"));
    if (token == null) return;
    socket.addSocketEventListener(listenSocket);
});
onUnmounted(() => {
    if (socket != null) socket.removeSocketEventListener(listenSocket);
});
</script>
<style scoped lang="less">
.sider-hot-article-label {
    font-size: 18px;
    font-weight: 700;
}
.items {
    display: flex;
    flex-direction: column;
    gap: 6px;
    margin-top: 10px;
}

.item {
    width: 100%;
}
.item-col {
    display: flex;
    align-items: center;
    cursor: pointer;
    transition: all 0.3s ease;
    // border: 1px solid #ffd0c1;
    border-radius: 4px;
    padding-left: 8px;
}
.item-col:hover {
    background-color: #ffded4;
}
.item-col-avatar {
    width: 36px;
    height: 36px;
    margin-right: 6px;
    border-radius: 50%;
    overflow: hidden;
}

.item-col-avatar img {
    width: 100%;
    min-width: 40px;
}
.item-col-username {
    min-width: 50px;
    text-overflow: ellipsis;
    white-space: nowrap;
    padding: 2px 2px;
    font-size: 16px;
    margin-right: 4px;
}
.item-col-msg {
    overflow: hidden;
    text-overflow: ellipsis;
    white-space: nowrap;
    padding: 2px 2px;
    font-size: 14px;
    color: #999;
}
</style>
