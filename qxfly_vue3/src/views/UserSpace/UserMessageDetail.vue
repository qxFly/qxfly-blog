<template>
    <div class="user-message-window">
        <div class="head-username">{{ username }}</div>
        <div class="msg-window">
            <div class="msg-list" id="msg-list">
                <div v-for="(m, index) in currMsg.msg" :key="index">
                    <div class="msg-item" :class="{ right: m.fromUid == loginUid }">
                        <div class="avatar">
                            <img :src="getUserAvatar(m)" alt="" />
                        </div>
                        <div class="msg-item-content">{{ m.content }}</div>
                    </div>
                </div>
            </div>
        </div>
        <hr />
        <div class="input-msg">
            <textarea v-model="msg" placeholder="请输入消息" id="msg-input-textarea"></textarea>
            <div class="input-msg-btn">
                <button @click="sendMessageToUser">发送</button>
            </div>
        </div>
    </div>
</template>
<script setup>
import { onMounted, ref, watch, onUnmounted } from "vue";
import { sendMessage, listMessage, initUserMessage } from "@/api/User/index";
import { ElMessage } from "element-plus";
import md5 from "js-md5";
import * as socketUtil from "@/utils/Socket";
let props = defineProps({
    uid: {
        type: Number,
        default: 0,
    },
    username: {
        type: String,
        default: "",
    },
    avatar: {
        type: String,
        default: "",
    },
    msgId: {
        type: String,
        default: "",
    },
});
let socket;
let emits = defineEmits(["msgId"]);
let refProps = ref(props);
/* 获取指定用户的消息 */
let loginUid = ref(parseInt(sessionStorage.getItem("uid")));
let toUid;
let msgId = null;
let currMsg = ref({ toUid: 0, size: 100, msg: [] });
async function listUserMessage() {
    msgId = refProps.value.msgId;
    toUid = refProps.value.uid;
    currMsg.value.toUid = toUid;
    if (refProps.value.msgId == null || refProps.value.msgId == "") {
        return;
    }
    await listMessage({ msgId: refProps.value.msgId, size: currMsg.value.size }).then((res) => {
        currMsg.value.msg = res.data.data;
    });
    toBottom();
}
watch(
    refProps,
    (newVal, oldVal) => {
        currMsg.value = { toUid: 0, size: 100, msg: [] };
        listUserMessage();
    },
    { deep: true }
);
/* 消息拉至最底下 */
function toBottom() {
    const ele = document.getElementById("msg-list");
    let h = ele.scrollHeight;
    setTimeout(() => {
        ele.scrollTo({
            top: h,
            behavior: "smooth",
        });
    }, 10);
}
/* 发送消息 */
let msg = ref("");
function sendMessageToUser() {
    let status = socketUtil.getStatus();
    if (status == WebSocket.CLOSED) {
        ElMessage.error({ message: "发送失败", offset: 120 });
        return;
    }
    //发送消息后重新聚焦输入框
    let input = document.getElementById("msg-input-textarea");
    input.focus();
    //判断是否为空
    if (msg.value.trim() == "") {
        return;
    }
    //判断是否有聊天对象
    if (toUid == null) {
        ElMessage.warning({ message: "进入文章详情，与作者进行聊天吧", offset: 120 });
        return;
    }
    let msgdata = {
        content: msg.value,
        fromUid: loginUid.value,
        msgId: refProps.value.msgId,
        toUid: toUid,
    };
    currMsg.value.msg.push(msgdata);
    //发送消息
    socketUtil.sendSocketMessage(msgId, msg.value, toUid);
    //清空输入框
    msg.value = "";
    toBottom();
}
/* socket接收消息 */
function receiveMessage(event) {
    let msgObj = JSON.parse(event.data);
    if (msgObj.toUid == msgObj.fromUid) {
        // 消息发送后自己给自己的回执
        if (!msgObj.sendSuccess) {
            ElMessage.error({ message: "消息发送失败，请检查网络", offset: 120 });
        } else {
            msgId = msgObj.msgId;
        }
    } else {
        // 接收别人发来的消息
        if (msgObj.fromUid == toUid) {
            currMsg.value.msg.push(msgObj);
            toBottom();
        }
    }
}
function getUserAvatar(m) {
    if (m.fromUid == loginUid.value) {
        return sessionStorage.getItem("useravatar");
    } else {
        if (refProps.value.avatar != null && refProps.value.avatar != "") {
            return refProps.value.avatar;
        } else {
            return require("@/assets/avatar.png");
        }
    }
}
function setMainSize() {
    let main = document.getElementById("msg-list");
    let height = document.body.scrollHeight - 380;
    main.style.height = height + "px";
}
onMounted(() => {
    setMainSize();

    if (localStorage.getItem(md5("token")) != null) socketUtil.addSocketEventListener(receiveMessage);
});
onUnmounted(() => {
    socketUtil.removeSocketEventListener(receiveMessage);
});
</script>

<style lang="less" scoped>
.user-message-window {
    width: 100%;
    box-sizing: border-box;
}
.head-username {
    text-align: center;
    padding: 4px 0;
}
.msg-window {
    font-family: "Microsoft YaHei", "Microsoft Sans Serif", "Microsoft SanSerf", "微软雅黑";
    width: 100%;
    background-color: rgba(218, 218, 218, 0.5);
}
.msg-list {
    padding: 20px;
    overflow: auto;
}
.msg-item {
    max-width: 100%;
    display: flex;
    align-items: center;
    margin: 0 10px 10px 0;
}
.msg-item.right {
    justify-content: right;
}
.msg-item-content {
    border-radius: 0 8px 8px 8px;
    max-width: 300px;
    padding: 6px 14px;
    margin: 0 10px;
    background-color: #ffffff;
    font-size: 14px;
    overflow-wrap: break-word;
}
.msg-item.right .msg-item-content {
    border-radius: 8px 0 8px 8px;
    color: #fff;
    background-color: var(--main-theme-color-blue);
}
.msg-item .avatar {
    width: 30px;
    height: 30px;
    min-width: 30px;
    min-height: 30px;
    object-fit: cover;
    border-radius: 50%;
    overflow: hidden;
}
.msg-item .avatar img {
    width: 100%;
    height: 100%;
}
.msg-item.right .avatar {
    order: 1;
}
.input-msg {
    box-sizing: border-box;
    width: 100%;
}
.input-msg textarea {
    width: 100%;
    height: 100px;
    padding: 10px;
    border-radius: 4px;
    border: 1px solid #dcdfe6;
    box-sizing: border-box;
    resize: none;
    font-size: 14px;
    font-family: "Microsoft YaHei", "Microsoft Sans Serif", "Microsoft SanSerf", "微软雅黑";
}
.input-msg textarea:focus {
    // border: none;
    outline: none;
}
.input-msg-btn {
    display: flex;
    justify-content: right;
    // padding: 0 10px;
    margin-right: 20px;
}
.input-msg button {
    border: none;
    background-color: var(--main-theme-color-blue);
    color: #fff;
    padding: 5px 10px;
    border-radius: 4px;
}
</style>
