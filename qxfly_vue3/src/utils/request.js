import { check } from "@/api/Admin";
import router from "@/router";
import axios from "axios";
import md5 from "js-md5";
import { ref } from "vue";
let token = localStorage.getItem(md5("token"));
const service = axios.create({
    baseURL: process.env.VUE_APP_BASE_URL,
    // baseURL: "https://qxfly.fun/fly",
    // baseURL: "http://120.24.195.4:8081",
    headers: {
        token: token,
    },
});
const passurl = [
    "/login",
    "/logout",
    "/register",
    "/gs",
    "/updateLoginStatue",
    "/listFile",
    "/listSite",
    "/download",
    "/fileList",
    "/listWord",
    "/searchWord",
    "/searchWord",
    "/likeSearchWord",
    "/userImg",
    "/user/getUserInfo",
    "/user/getSuggestAuthor",
    "/user/findPassword",
    "/user/sendCode",
    "/check",
    "/getImage",
    "/saying/getWord",
    "/article/getArticles",
    "/article/getArticleById",
    "/article/getArticleComments",
    "/article/addArticleView",
    "/article/getTags",
    "/article/getClassifies",
    "/article/getArticleAttachment",
    "/article/getCollectionArticles",
];
/* 请求拦截器 */
service.interceptors.request.use(async (config) => {
    /* 无需拦截的白名单路径 */
    if (passurl.includes(config.url, 0)) {
        return config;
    } else {
        let urls = config.url.split("/");
        if (urls.includes("manage", 0)) {
            if (urls.includes("check", 0)) {
                return config;
            } else {
                let isAdmin = ref(false);
                await check().then((res) => {
                    if (res.data.code === 1) {
                        isAdmin.value = true;
                    }
                });
                if (isAdmin.value) {
                    return config;
                } else {
                    // alert("您没有权限访问该页面！");
                    router.push("/");
                    return config;
                }
            }
        } else {
           // TODO if (token == md5("nologin")) {
            if (token == null) {
                console.log("nologin", config.url);
                router.replace("/login");
                return config;
            } else {
                return config;
            }
        }
    }
});

export default service;
