import axios from "axios";
import md5 from "js-md5";
// import request from "@/utils/request";
let token = localStorage.getItem(md5("token"));
const request = axios.create({
    baseURL: process.env.VUE_APP_ADMIN_BASE_URL,
    // baseURL: "https://qxfly.fun/fly",
    // baseURL: "http://120.24.195.4:8081",
    headers: {
        token: token,
    },
});
/**
 * 检查是否为管理员
 */
export function check() {
    return request({
        url: "/check",
        method: "post",
    });
}
/**
 * 删除用户
 * @returns
 */
export function deleteUser(id) {
    return request({
        url: "/deleteUser",
        method: "get",
        params: {
            id: id,
        },
    });
}
/**
 * 更改用户信息
 * @returns
 */
export function editUserInfo(data) {
    return request({
        url: "/editUserInfo",
        method: "post",
        data: data,
    });
}
/**
 * 删除用户头像
 * @returns
 */
export function deleteUserAvatar(data) {
    return request({
        url: "/deleteUserAvatar",
        method: "post",
        data: data,
    });
}

/**
 *列出用户
 * @returns
 */
export function listUser(data) {
    return request({
        url: "/listUser",
        method: "post",
        data: data,
    });
}
/**
 * 审核文章
 * @returns
 */
export function articleVerify(data) {
    return request({
        url: "/articleVerify",
        method: "post",
        data: data,
    });
}
/**
 * 删除文章
 * @returns
 */
export function deleteArticle(data) {
    return request({
        url: "/deleteArticle",
        method: "post",
        data: data,
    });
}
/**
 *搜索文章
 * @returns
 */
export function searchArticle(data) {
    return request({
        url: "/searchArticle",
        method: "post",
        data: data,
    });
}
/**
 * 审核文章评论
 * @returns
 */
export function articleCommentVerify(data) {
    return request({
        url: "/articleCommentVerify",
        method: "post",
        data: data,
    });
}
/**
 * 删除文章评论
 * @returns
 */
export function deleteArticleComment(data) {
    return request({
        url: "/deleteArticleComment",
        method: "post",
        data: data,
    });
}
/**
 *搜索文章评论
 * @returns
 */
export function listArticleComment(data) {
    return request({
        url: "/listArticleComment",
        method: "post",
        data: data,
    });
}
/**
 *添加站点
 @returns
 */
export function addSite(data) {
    return request({
        url: "/addSite",
        method: "post",
        data: data,
    });
}
/**
 *删除站点
 @returns
 */
export function deleteSite(data) {
    return request({
        url: "/deleteSite",
        method: "post",
        data: data,
    });
}
/**
 *修改站点
 @returns
 */
export function updateSite(data) {
    return request({
        url: "/updateSite",
        method: "post",
        data: data,
    });
}
/* 获取站点状态 */
export function getSiteStatus(id) {
    return request({
        url: "/getSiteStatus",
        method: "get",
        params: {
            id: id,
        },
    });
}

/* 更新图库 */
export function updateImage() {
    return request({
        url: "/updateImage",
        method: "get",
    });
}
/**
 * 获取图库
 * @returns
 */
export function listImages(data) {
    return request({
        url: "/listImages",
        method: "post",
        data: data,
    });
}
/**
 * 删除图片
 * @returns
 */
export function deleteImage(id) {
    return request({
        url: "/deleteImage",
        method: "get",
        params: {
            id: id,
        },
    });
}

/**
 * 列出管理员导航栏
 * @returns
 */
export function listAdminNavigations() {
    return request({
        url: "/listAdminNavigations",
        method: "get",
    });
}

/**
 * 分页列出导航栏
 * @returns
 */
export function listNavigations(data) {
    return request({
        url: "/listNavigations",
        method: "post",
        data: data,
    });
}
/**
 * 新增导航栏
 * @returns
 */
export function addNavigation(data) {
    return request({
        url: "/addNavigation",
        method: "post",
        data: data,
    });
}

/**
 * 编辑导航栏
 * @returns
 */
export function updateNavigation(data) {
    return request({
        url: "/updateNavigation",
        method: "post",
        data: data,
    });
}

/**
 * 删除导航栏
 * @returns
 */
export function deleteNavigation(data) {
    return request({
        url: "/deleteNavigation",
        method: "post",
        data: data,
    });
}
