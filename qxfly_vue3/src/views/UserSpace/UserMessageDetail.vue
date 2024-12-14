<template>
    <div class="user-message-window">
        <div class="head-username">{{ username }}</div>
        <div class="msg-window">
            <div class="msg-list" id="msg-list">
                <div v-for="(m, index) in currMsg.msg" :key="index">
                    <div class="msg-item" :class="{ right: m.uid1 == loginUid }">
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
            <textarea v-model="msg" placeholder="请输入消息"></textarea>
            <div class="input-msg-btn">
                <button @click="sendMessageToUser">发送</button>
            </div>
        </div>
    </div>
</template>
<script setup>
import { onMounted, ref, watch } from "vue";
import { sendMessage, listMessage } from "@/api/User/index";
import { ElMessage } from "element-plus";
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

let emits = defineEmits(["msgId"]);
let refProps = ref(props);
/* 获取指定用户的消息 */
let loginUid = ref(parseInt(sessionStorage.getItem("uid")));
let msgList = ref([]);
let currMsg = ref({ otherUid: 0, size: 100, msg: [] });
async function listMessageUser() {
    let currUserId = refProps.value.uid;
    /* 先获取本地和当前用户的聊天记 */
    currMsg.value = msgList.value.find((item) => item.otherUid == currUserId);
    /* 在从服务端获取 */
    currMsg.value = { otherUid: currUserId, size: 100 };
    if (refProps.value.msgId == null || refProps.value.msgId == "") return;
    await listMessage({ msgId: refProps.value.msgId, size: currMsg.value.size }).then((res) => {
        currMsg.value.msg = res.data.data;
    });
    msgList.value.push(currMsg.value);
    toBottom();
}
watch(
    refProps,
    (newVal, oldVal) => {
        listMessageUser();
    },
    { deep: true }
);
/* 消息拉至最底下 */
function toBottom() {
    const ele = document.getElementById("msg-list");
    let h = ele.scrollHeight;
    ele.scrollTo(0, h);
}
/* 发送消息 */
let msg = ref("");
function sendMessageToUser() {
    if (msg.value.trim() == "") {
        return;
    }
    if (refProps.value.uid == null) {
        ElMessage.warning({ message: "进入文章详情，与作者进行聊天吧", offset: 120 });
        return;
    }
    sendMessage({
        uid1: loginUid.value,
        uid2: refProps.value.uid,
        content: msg.value,
        msgId: refProps.value.msgId,
    }).then((res) => {
        if (res.data.code == 1) {
            ElMessage.success({ message: "发送成功", offset: 120 });
            msg.value = "";
            if (res.data.data != null && res.data.data != "") {
                emits("msgId", res.data.data);
            }
            if (refProps.value.msgId != "") listMessageUser();
        } else {
            ElMessage.error({ message: res.data.msg, offset: 120 });
        }
    });
}
function getUserAvatar(m) {
    if (m.uid1 == loginUid.value) {
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
    background-color: #f4f5f7;
}
.msg-list {
    padding: 20px;
    overflow: auto;
}
.msg-item {
    width: 100%;
    display: flex;
    align-items: center;
    margin: 0 10px 10px 0;
}
.msg-item.right {
    justify-content: right;
}
.msg-item-content {
    border-radius: 0 8px 8px 8px;
    // width: max-content;
    padding: 6px 14px;
    margin: 0 10px;
    background-color: #ffffff;
    font-size: 14px;
}
.msg-item.right .msg-item-content {
    border-radius: 8px 0 8px 8px;
    color: #fff;
    background-color: #84c6ff;
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
    background-color: #84c6ff;
    color: #fff;
    padding: 5px 10px;
    border-radius: 4px;
}
</style>
