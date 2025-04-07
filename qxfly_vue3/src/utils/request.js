import { check } from "@/api/Admin";
import router from "@/router";
import axios from "axios";
import md5 from "js-md5";
import { ref } from "vue";
import { updateLoginStatue } from "@/api/";
let accessToken = ref(localStorage.getItem(md5("token")));
let refreshToken = ref(localStorage.getItem(md5("refreshToken")));
let domain = window.location.hostname;
const service = axios.create({
    baseURL: domain == "38.55.199.233" ? process.env.VUE_APP_IP_BASE_URL : process.env.VUE_APP_BASE_URL,
    // baseURL: "https://qxfly.fun/fly",
    // baseURL: "http://120.24.195.4:8081",
});
const passurl = [
    "/gs", //获取公匙
    "/login", //登陆
    "/logout", //退出
    "/register", //注册
    "/updateLoginStatue", //检测更用户信息
    "/getImage", //图片列表
    "/index/listSite", //网站列表
    "/index/listIndexNav", //首页导航
    "/download/**", // 下载
    "/v3/**", //api文档
    "/swagger-ui/**", //api文档
    "/userAvatar/**", //用户头像
    "/user/getUserInfo", //获取用户信息
    "/user/getSuggestAuthor", // 获取推荐作者
    "/user/findPassword", // 找回密码
    "/user/sendCode", // 找回密码
    "/article/listArticles", //文章列表
    "/article/getArticleById", //文章详情
    "/articleCover/**", //文章封面
    "/articleImage/**", //文章内容图片
    "/article/getArticleComments", //获取文章评论
    "/article/addArticleView", // 增加浏览量
    "/article/getTags", // 获取标签
    "/article/getClassifies", // 获取分类
    "/article/getArticleAttachment", //文章附件
    "/articleAttachment/**", //附件下载
    "/article/getCollectionArticles", //获取用户收藏
    "/index/listLeaveMessage", //列出留言
    "/index/sendLeaveMessage", //发送留言
    "/index/deleteLeaveMessage", //删除留言
    "/index/getSiteStatus", //获取站点状态
];
/* 请求拦截器 */
service.interceptors.request.use(async (config) => {
    console.log("request:", config.url);
    /* 设置token */
    config.headers.token = accessToken.value;
    /* 无需拦截的白名单路径 */
    if (passurl.includes(config.url, 0)) {
        return config;
    } else {
        if (accessToken.value == null) {
            router.replace("/login");
            return config;
        } else {
            return config;
        }
        // let urls = config.url.split("/");
        // if (urls.includes("manage", 0)) {
        //     if (urls.includes("check", 0)) {
        //         return config;
        //     } else {
        //         let isAdmin = ref(false);
        //         await check().then((res) => {
        //             if (res.data.code === 1) {
        //                 isAdmin.value = true;
        //             }
        //         });
        //         if (isAdmin.value) {
        //             return config;
        //         } else {
        //             // alert("您没有权限访问该页面！");
        //             router.push("/");
        //             return config;
        //         }
        //     }
        // } else {
        //     if (accessToken.value == null) {
        //         router.replace("/login");
        //         return config;
        //     } else {
        //         return config;
        //     }
        // }
    }
});

/* 响应拦截器 */
service.interceptors.response.use(async (response) => {
    return interceptors(response);
});
export async function interceptors(response) {
    let code = response.data.code;
    /* 登录过期：双token都已经过期 */
    if (code === 1102 || code === 1201) {
        localStorage.clear();
        sessionStorage.clear();
        location.replace("/login");
        setTimeout(() => {
            location.reload();
        }, 500);

        return response;
    }
    /* token过期 */
    if (code === 1101) {
        /* 获取新token */
        const isSuccess = await refresh_Token();
        if (isSuccess) {
            /* 将token过期的请求在发一遍 */
            response.config.headers.token = accessToken.value;
            const r = await service.request(response.config);
            return r;
        } else {
            localStorage.clear();
            sessionStorage.clear();
            location.reload();
            return response;
        }
    } else {
        return response;
    }
}
let promise = null;
async function refresh_Token() {
    /* 刷新token时，有大量请求返回第一个 */
    if (promise) {
        return promise;
    }
    promise = new Promise(async (resolve) => {
        const res = await updateLoginStatue(refreshToken.value);
        let data = res.data;
        if (data.code == 1) {
            accessToken.value = data.data.accessToken;
            refreshToken.value = data.data.refreshToken;
            localStorage.setItem(md5("token"), accessToken.value);
            localStorage.setItem(md5("refreshToken"), refreshToken.value);
        }
        resolve(data.code === 1);
    });
    promise.finally(() => {
        promise = null;
    });
    return promise;
}
export default service;
