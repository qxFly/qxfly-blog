import request from "@/utils/request";

/**
 * 获取用户信息
 */
export function getUserInfo(data) {
    return request({
        url: "/user/getUserInfo",
        method: "post",
        data: data,
    });
}
/**
 * 更改用户信息
 */
export function updateUserInfo(data) {
    return request({
        url: "/user/updateUserInfo",
        method: "post",
        data: data,
    });
}

/**
 * 上传用户头像
 * @param {*} file
 * @returns
 */
export function updateAvatar(file) {
    return request({
        url: "/user/updateAvatar",
        method: "post",
        data: file,
        headers: { "Content-Type": "multipart/form-data" },
    });
}

/**
 * 分页获取用户文章
 * @param {*} data
 * @returns
 */
export function getUserArticles(currPage, pageSize) {
    return request({
        url: "/article/getUserArticles",
        method: "get",
        params: { currPage: currPage, pageSize: pageSize },
    });
}
/**
 * 获取推荐作者
 * @param {*} currPage
 * @param {*} pageSize
 * @returns
 */
export function getSuggestAuthor(currPage, pageSize) {
    return request({
        url: "/user/getSuggestAuthor",
        method: "get",
        params: { currPage: currPage, pageSize: pageSize },
    });
}
/**
 * 找回密码
 * @param {*} data
 * @returns
 */
export function findPassword(data) {
    return request({
        url: "/user/findPassword",
        method: "post",
        data: data,
    });
}
/**
 * 发送验证码
 * @param {*} data
 * @returns
 */
export function sendCode(data) {
    return request({
        url: "/user/sendCode",
        method: "post",
        data: data,
    });
}
/**
 * 初始化消息对话
 * @param {uid,fromUid,size} data
 * @returns
 */
export function initUserMessage(data) {
    return request({
        url: "/user/initUserMessage",
        method: "get",
        params: data,
    });
}
/**
 * 获取指定用户发送的消息
 * @param {uid,fromUid,size} data
 * @returns
 */
export function listMessage(data) {
    return request({
        url: "/user/listMessage",
        method: "get",
        params: data,
    });
}
/**
 * 获取给当前用户发送的消息的用户
 * @param {uid, size} data
 *
 * @returns
 */
export function listMessageUserNav(data) {
    return request({
        url: "/user/listMessageUserNav",
        method: "get",
        params: data,
    });
}
/**
 * 发送消息
 * @param {*} data
 * @returns
 */
export function sendMessage(data) {
    return request({
        url: "/user/sendMessage",
        method: "post",
        data: data,
    });
}
/**
 * 用户读未读消息
 * @param {*} data
 * @returns
 */
export function readMessage(data) {
    return request({
        url: "/user/readMessage",
        method: "post",
        data: data,
    });
}
/**
 * 根据用户id查看是否有未读消息
 * @param {*} data
 * @returns
 */
export function getNoReadMessageCount(data) {
    return request({
        url: "/user/getNoReadMessageCount",
        method: "get",
        params: data,
    });
}

/**
 * 获取用户空间导航栏
 * @returns
 */
export function listUserSpaceNav() {
    return request({
        url: "/user/listUserSpaceNav",
        method: "get",
    });
}

/**
 * 获取用户设置
 * @returns
 */
export function getUserSettings() {
    return request({
        url: "/user/setting/getUserSettings",
        method: "get",
    });
}

/**
 * 更新用户设置
 * @returns
 */
export function updateUserSettings(data) {
    return request({
        url: "/user/setting/updateUserSettings",
        method: "post",
        data: data,
    });
}
/**
 * 上传背景图片
 * @returns
 */
export function uploadBgImg(file) {
    return request({
        url: "/user/setting/uploadBgImg",
        method: "post",
        data: file,
        headers: { "Content-Type": "multipart/form-data" },
    });
}
/**
 * 删除用户背景图
 * @returns
 */
export function deleteUserBackground(data) {
    return request({
        url: "/user/setting/deleteUserBackground",
        method: "post",
        data: data,
    });
}
