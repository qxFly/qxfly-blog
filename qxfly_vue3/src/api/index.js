//导入request.js
import request from "@/utils/request"; //@==>src

/**
 * 登入
 * @param {username, password} userdata
 * @returns
 */
export const Login = (userdata) =>
    request.post("/login", userdata, {
        headers: { "Content-Type": "application/json" },
    });

/**
 * 注册
 * @param {username, password} userdata
 * @returns
 */
export const Register = (userdata) =>
    request.post("/register", userdata, {
        headers: { "Content-Type": "application/json" },
    });

/**
 * 更新登入状态
 * @param {username, token} params
 * @returns
 */
export const updateLoginStatue = (params) => request.post("/updateLoginStatue", params);

/**
 * 退出登陆
 * @param {token, username} params
 * @returns
 */
export const Logout = () =>
    request.post("/logout", {
        headers: { "Content-Type": "application/json" },
    });

/**
 * 获取公匙
 * @returns
 */
export function getGS() {
    return request({
        url: "/gs",
        method: "post",
    });
}

/**
 * 上传
 * @param {*} file
 * @returns
 */
export const upload = (file) =>
    request.post("/upload", file, {
        headers: { "Content-Type": "multipart/form-data" },
    });

/**
 * 获取文件列表
 * @returns
 */
export const listFile = () => request.post("/listFile");

/**
 * 获取文件列表
 * @returns
 */
// export const downloadtest = (fileName) => request.post("/downloadtest", fileName);

export function downloadtest(fileName) {
    return request({
        url: "/downloadtest",
        method: "post",
        responseType: "blob",
        params: { fileName: fileName },
    });
}

/**
 * 获取网站列表
 * @returns
 */
export function listSite(data) {
    return request({
        url: "/index/listSite",
        method: "get",
        params: data,
    });
}

/**
 * 添加网站
 * @returns
 */
export function addSite(site) {
    return request({
        url: "/addSite",
        method: "post",
        data: site,
    });
}

/**
 * 删除网站
 * @returns
 */
export function deleteSite(id) {
    return request({
        url: "/deleteSite",
        method: "post",
        data: id,
    });
}

/**
 * 获取图片链接
 */
export function getImage(currPage, pageSize, sort) {
    return request({
        url: "/getImage",
        method: "get",
        params: { currPage: currPage, pageSize: pageSize, sort: sort },
    });
}
/**
 * 更新图片链接
 */
export const updateImage = () =>
    request({
        url: "/updateImage",
        method: "get",
    });

/**
 * 获取用户卡片信息
 */
export function getUserCardInfo(token) {
    return request({
        url: "/user/getUserCardInfo",
        method: "post",
        data: token,
    });
}

/**
 * 首页导航栏列表
 */
export function listIndexNav() {
    return request({
        url: "/index/listIndexNav",
        method: "get",
    });
}

/**
 * 分页获取留言列表
 */
export function listLeaveMessage(data) {
    return request({
        url: "/index/listLeaveMessage",
        method: "post",
        data: data,
    });
}
/**
 * 发送留言
 */
export function sendLeaveMessage(data) {
    return request({
        url: "/index/sendLeaveMessage",
        method: "post",
        data: data,
    });
}
/**
 * 删除留言
 */
export function deleteLeaveMessage(id) {
    return request({
        url: "/index/deleteLeaveMessage",
        method: "get",
        params: { id: id },
    });
}
