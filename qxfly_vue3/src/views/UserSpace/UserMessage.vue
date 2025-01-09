<template>
    <div class="main" id="main">
        <div class="user-nav" id="user-nav">
            <div v-for="(n, index) in userNav" :key="n">
                <div class="user-nav-items" :class="{ active: activeUser.id == n.id }">
                    <div class="user-nav-item" @click="listUserMessage(n)">
                        <div class="item-both">
                            <div class="avatar">
                                <img :src="setUserAvatar(n.avatar)" alt="" />
                            </div>
                            <div class="username">{{ n.username }}</div>
                        </div>
                        <div class="item-both">
                            <div :class="{ msgCount: getNoReadCount(n) > 0 }">
                                {{ getNoReadCount(n) }}
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <div v-if="userNav.length == 0">
                <div class="user-nav-items active">
                    <div class="user-nav-item">
                        <div class="item-both">
                            <div class="avatar">
                                <img :src="require('@/assets/avatar.png')" alt="" />
                            </div>
                            <div class="username" style="font-size: 14px">暂无聊天记录</div>
                        </div>
                        <div class="item-both">
                            <div></div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <div class="user-message-window" id="user-message-window">
            <UserMessageDetail
                :uid="activeUser == null ? null : activeUser.id"
                :username="activeUser == null ? null : activeUser.username"
                :avatar="activeUser == null ? null : activeUser.avatar"
                :msgId="msgId"
                @msgId="returnMsgId"></UserMessageDetail>
        </div>
    </div>
</template>

<script setup>
import { onMounted, onUnmounted, ref } from "vue";
import md5 from "js-md5";
import { listMessageUserNav, readMessage, sendMessage, listMessage, initUserMessage } from "@/api/User/index";
import router from "@/router";
import UserMessageDetail from "@/views/UserSpace/UserMessageDetail.vue";
import { ElMessage } from "element-plus";
import { useRoute, onBeforeRouteUpdate } from "vue-router";
import * as socketUtil from "@/utils/Socket";
let route = useRoute();
let uid = ref(sessionStorage.getItem("uid"));
/* 获取给当前用户发送消息的用户 */
let navSize = ref(10);
let userNav = ref([]);
let activeUser = ref(); // 选中的用户
let msgId = ref(); //对话id
function listUserAsNav() {
    listMessageUserNav({ uid: uid.value, size: navSize.value }).then((res) => {
        if (res.data.code == 1) {
            if (res.data.data.length > 0) {
                userNav.value = res.data.data;
            }
            initMessageNav();
        } else {
            ElMessage.error(res.data.msg);
        }
    });
}
/* 获取用户消息 */
async function listUserMessage(n) {
    await readMessage({ msgId: n.msgId, toUid: uid.value }).then((res) => {
        if (res.data.code == 1) {
        }
    });
    activeUser.value = n;
    msgId.value = n.msgId;
    n.noReadCount = 0;
}
/* 初始化消息导航 */
function initMessageNav() {
    if (route.query.muid == null && userNav.value.length == 0) return;
    if (route.query.muid == null) {
        activeUser.value = userNav.value[0];
        activeUser.value.noReadCount = 0;
        msgId.value = userNav.value[0].msgId;
        listUserMessage(userNav.value[0]);
    } else {
        activeUser.value = { id: parseInt(route.query.muid), avatar: require("@/assets/avatar.png") };
        let u = userNav.value.find((item) => item.id == route.query.muid);
        if (u != null) {
            msgId.value = u.msgId;
            activeUser.value = u;
        } else {
            initUserMessage({ uid: activeUser.value.id }).then((res) => {
                if (res.data.code == 1) {
                    userNav.value.push(res.data.data);
                    activeUser.value = res.data.data;
                }
            });
        }
    }
}
function setMainSize() {
    let main = document.getElementById("main");
    let height = document.body.scrollHeight - 200;
    main.style.height = height + "px";
}
/* 设置未读消息数 */
function getNoReadCount(n) {
    if (activeUser.value.id == n.id) return;
    return n.noReadCount == 0 ? "" : n.noReadCount;
}
/* 返回的msgId */
let returnMsgId = (id) => {
    msgId.value = id;
};
/* 设置头像 */
function setUserAvatar(a) {
    if (a == null) {
        return require("@/assets/avatar.png");
    } else {
        return a;
    }
}
//socket监听器
function socketEventListener(event) {
    let msgObj = JSON.parse(event.data); //socket消息对象
    //发送后自己给自己的回执消息
    if (msgObj.fromUid == msgObj.toUid) {
        // msgId.value = msgObj.msgId;
        return;
    }
    //查找消息列表是否有该用户
    let u = userNav.value.find((item) => item.id == msgObj.fromUid);
    if (u != null) {
        if (activeUser.value.id != u.id) {
            u.noReadCount++;
        } else u.noReadCount = 0;
    } else {
        //没有则添加
        initUserMessage({ uid: msgObj.fromUid }).then((res) => {
            if (res.data.code == 1) {
                let m = res.data.data; //用户消息对象
                m.msgId = msgObj.msgId; //设置msgId
                userNav.value.push(m);
                //设置选中用户
                if (activeUser.value == null) {
                    activeUser.value = m;
                    msgId.value = msgObj.msgId;
                } else {
                    if (activeUser.value.id != m.id) m.noReadCount++;
                    else m.noReadCount = 0;
                }
            }
        });
    }
}
onMounted(() => {
    if (localStorage.getItem(md5("token")) != null) {
        listUserAsNav(); //获取给当前用户发送消息的用户,初始化消息导航
        setMainSize(); //设置消息窗口大小
        socketUtil.addSocketEventListener(socketEventListener); //添加socket监听器
    }
});
onUnmounted(() => {
    socketUtil.removeSocketEventListener(socketEventListener);
});
</script>

<style scoped>
.main {
    border: 1px solid #ccc;
    border-radius: 8px;
    overflow: hidden;
    display: flex;
    min-width: 500px;
}
.user-nav {
    max-width: 250px;
    min-width: 150px;
    border-right: 2px solid #c9c9c9;
    overflow: auto;
}
.user-nav-items {
    padding: 10px;
}
.user-nav-items:hover {
    background-color: #e4e5e6;
}
.active {
    background-color: #e4e5e6;
}
.user-nav-item {
    display: flex;
    justify-content: space-between;
    align-items: center;
    cursor: pointer;
}
.item-both {
    display: flex;
    justify-content: left;
    align-items: center;
    cursor: pointer;
}
.avatar {
    width: 40px;
    height: 40px;
    border-radius: 50%;
    overflow: hidden;
    object-fit: cover;
    margin-right: 10px;
}
.avatar img {
    width: 100%;
    height: 100%;
}
.username {
    text-overflow: ellipsis;
    white-space: nowrap;
    overflow: hidden;
}
.user-message-window {
    width: 100%;
    /* padding: 10px; */
}
.msgCount {
    border-radius: 50%;
    background-color: #ff8e68;
    width: 16px;
    height: 16px;
    font-size: 12px;
    text-align: center;
    color: #fff;
}
</style>
