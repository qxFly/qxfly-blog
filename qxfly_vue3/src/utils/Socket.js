let socket;
let islogin = localStorage.getItem(md5("islogin"));
import md5 from "js-md5";

//初始化websocket
function initWebsocket() {
    if (islogin == null || islogin != md5("true")) {
        return;
    }
    let uid = localStorage.getItem("uid");
    socket = new WebSocket(process.env.VUE_APP_SOCKET_BASE_URL + uid);
    socket.onopen = function () {};
}
//获取socket
function getSocket() {
    return socket;
}

//关闭socket
function closeSocket() {
    if (socket) {
        socket.close();
    }
}

//发送消息
function sendSocketMessage(msgId, data, toUid) {
    if (islogin == null || islogin != md5("true")) {
        return;
    }
    let message = {
        message: data,
        toUid: toUid,
        msgId: msgId,
    };
    socket.send(JSON.stringify(message));
}

function addSocketEventListener(callback) {
    if (socket != null) socket.addEventListener("message", callback);
}
function removeSocketEventListener(callback) {
    if (socket != null) socket.removeEventListener("message", callback);
}

export { initWebsocket, getSocket, closeSocket, sendSocketMessage, addSocketEventListener, removeSocketEventListener };
