let socket;
let timer;
let status = WebSocket.CLOSED;
let listenerList = [];
let islogin = localStorage.getItem(md5("islogin"));
import { ElMessage } from "element-plus";
import md5 from "js-md5";

//初始化websocket
function initWebsocket() {
    if (islogin == null || islogin != md5("true")) {
        return;
    }
    let uid = localStorage.getItem("uid");
    socket = new WebSocket(process.env.VUE_APP_SOCKET_BASE_URL + uid);
    socket.onopen = onOpen;
    socket.onerror = onError;
}

//发送消息
function sendSocketMessage(msgId, data, toUid) {
    if (islogin == null || islogin != md5("true") || status != WebSocket.OPEN) {
        ElMessage.error({ message: "发送失败", offset: 120 });
        return;
    }
    let message = {
        message: data,
        toUid: toUid,
        msgId: msgId,
    };

    socket.send(JSON.stringify(message));
}

/* scoket开启时触发 */
function onOpen() {
    clearInterval(timer);
    status = WebSocket.OPEN;
    /* 重新连接后，恢复之前的监听器 */
    for (let i = 0; i < listenerList.length; i++) {
        socket.addEventListener("message", listenerList[i]);
    }
    socket.onclose = onClose;
}

/* scoket关闭时触发 */
function onClose() {
    socket = null;
    status = WebSocket.CLOSED;
    timer = setInterval(() => {
        initWebsocket();
    }, 3000);
}
/* scoket错误时触发 */
function onError() {
    socket = null;
}
//获取socket
function getSocket() {
    return socket;
}
/**
 *
 * @returns 返回socket状态
 */
function getStatus() {
    return status;
}
//关闭socket
function closeSocket() {
    if (socket) {
        socket.close();
    }
}

/* 添加监听器 */
let ltimer;
function addSocketEventListener(callback) {
    if (socket != null && status == WebSocket.OPEN) {
        listenerList.push(callback);
        clearInterval(ltimer);
        socket.addEventListener("message", callback);
    } else {
        ltimer = setInterval(() => {
            addSocketEventListener(callback);
        }, 500);
    }
}
function removeSocketEventListener(callback) {
    if (socket != null) {
        socket.removeEventListener("message", callback);
        listenerList = listenerList.filter((item) => item != callback);
    }
}

export {
    initWebsocket,
    getSocket,
    getStatus,
    closeSocket,
    sendSocketMessage,
    addSocketEventListener,
    removeSocketEventListener,
};
