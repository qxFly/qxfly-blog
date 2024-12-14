import { createRouter, createWebHistory } from "vue-router";
import md5 from "js-md5";
import { check } from "@/api/Admin/index";
const routes = [
    {
        path: "/",
        redirect: "/articleView?page=1",
        component: () => import("@/views/Index/Index"),
        meta: {
            keepAlive: true,
        },
        children: [
            {
                path: "/imageView",
                component: () => import("@/views/Index/ImagePage.vue"),
                meta: {
                    keepAlive: true,
                },
            },
            {
                path: "/articleView",
                name: "articleView",
                component: () => import("@/views/Index/ArticlePage.vue"),
                meta: {
                    keepAlive: true,
                },
            },
        ],
    },

    {
        path: "/login",
        component: () => import("@/views/Login/Login.vue"),
    },
    {
        path: "/register",
        component: () => import("@/views/Login/Register.vue"),
    },
    {
        path: "/findPassword",
        component: () => import("@/views/Login/FindPassword.vue"),
    },
    // {
    //     path: "/file",
    //     component: () => import("@/views/FileIndex/FileIndex"),
    // },
    {
        path: "/manage",
        component: () => import("@/views/AdminPage/AdminIndex"),
        children: [
            {
                path: "/manage/user",
                component: () => import("@/views/AdminPage/User"),
            },
            {
                path: "/manage/image",
                component: () => import("@/views/AdminPage/ImagePage"),
            },
            {
                path: "/manage/article",
                component: () => import("@/views/AdminPage/Article"),
            },
            {
                path: "/manage/articleComment",
                component: () => import("@/views/AdminPage/ArticleComment"),
            },
            {
                path: "/manage/classify",
                component: () => import("@/views/AdminPage/Classify"),
            },
            {
                path: "/manage/tag",
                component: () => import("@/views/AdminPage/Tag"),
            },
            {
                path: "/manage/site",
                component: () => import("@/views/AdminPage/Site"),
            },
        ],
    },
    {
        path: "/manage/articleVerify",
        name: "articleVerify",
        component: () => import("@/views/AdminPage/ArticleVerify"),
    },
    {
        path: "/user/space",
        component: () => import("@/views/UserSpace/Space"),
        children: [
            {
                path: "/user/space/userInfo",
                component: () => import("@/views/UserSpace/UserInfoPage.vue"),
                meta: {
                    keepAlive: true,
                },
            },
            {
                path: "/user/space/userArticle",
                meta: {
                    keepAlive: true,
                },
                component: () => import("@/views/UserSpace/UserArticle.vue"),
            },
            {
                path: "/user/space/userCollection",
                meta: {
                    keepAlive: true,
                },
                component: () => import("@/views/UserSpace/UserCollection.vue"),
            },
            {
                path: "/user/space/userMessage",
                meta: {
                    keepAlive: false,
                },
                component: () => import("@/views/UserSpace/UserMessage.vue"),
            },
        ],
    },
    {
        path: "/article/editArticle/:salt/:articleId/:editMode",
        name: "editArticle",
        component: () => import("@/views/Article/EditArticle.vue"),
    },
    {
        path: "/article/ArticleDetail/:salt/:id/:editMode/:cover",
        name: "ArticleDetail",
        component: () => import("@/views/Article/ArticleDetail.vue"),
    },
    {
        path: "/:pathMatch(.*)*",
        component: () => import("@/views/JumpIndex"),
    },
    {
        path: "/JumpIndex",
        component: () => import("@/views/JumpIndex"),
    },
];

const router = createRouter({
    history: createWebHistory(),
    // history: createWebHashHistory(),
    routes,
});
let black = /(editArticle)/;
router.beforeEach(async (to, from, next) => {
    if (to.path.includes("/manage")) {
        // 获取 token
        await check().then((res) => {
            if (res.data.code == 1) {
                next();
            } else {
                // alert("您没有权限访问该页面！");
                next("/");
            }
        });
    } else {
        let isStop = false;
        if (to.path.match(black) != null) {
            isStop = true;
        }

        if (isStop) {
            // 获取 token
            const token = localStorage.getItem(md5("token"));
            // TODO if (token != md5("nologin")) {
            if (token != null) {
                next();
            } else {
                next("/login");
            }
        } else {
            next();
        }
    }
});

export default router;
