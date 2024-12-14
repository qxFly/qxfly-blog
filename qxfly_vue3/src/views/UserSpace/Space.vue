<template>
    <div class="space-bg theme-bg" id="space-bg"></div>
    <TopBar></TopBar>
    <div class="user-space-main">
        <div class="user-space-left-sider">
            <div class="user-space-nav user-space-sider">
                <div class="nav-item" v-for="item in navItems" :key="item">
                    <router-link
                        :to="item.path"
                        active-class="router-link-active"
                        class="router-link"
                        @click="clearTip(item.name)">
                        <div class="nav-item-left">
                            <div class="nav-item-icon" v-html="item.icon"></div>
                            <div class="nav-item-span">{{ item.name }}</div>
                        </div>
                        <div class="nav-item-right">
                            <div class="navItemTip" v-if="noReadMsgCount > 0 && item.name == '消息'">
                                <!-- {{ noReadMsgCount }} -->
                            </div>
                        </div>
                    </router-link>
                </div>
            </div>
        </div>

        <div class="router-view">
            <router-view v-slot="{ Component }">
                <keep-alive>
                    <component :is="Component" :key="$route.path" v-if="$route.meta.keepAlive" />
                </keep-alive>
                <component :is="Component" :key="$route.path" v-if="!$route.meta.keepAlive" />
            </router-view>
        </div>
        <div class="user-space-right-sider">
            <ArticleAuthorInfoCard class="UserInfoCard user-space-sider" :authorId="uid" btnType="index-message" />
        </div>
    </div>
    <BackTop></BackTop>
</template>

<script setup>
import BackTop from "@/components/BackTop.vue";
import TopBar from "@/components/TopBar/TopBar.vue";
import ArticleAuthorInfoCard from "@/views/Article/ArticleAuthorInfoCard.vue";
import { check } from "@/api/Admin";
import { onMounted, onUnmounted, ref } from "vue";
import md5 from "js-md5";
import { useRoute } from "vue-router";
import router from "@/router";
import { checkA } from "@/api/Article/index";
import { getNoReadMessageCount } from "@/api/User/index";
import { ElMessage } from "element-plus";
let useRouter = useRoute();
/* 设置菜单栏 */
let uid = ref(parseInt(useRouter.query.uid));
let navItems = ref([]);
let allNavItems = ref([
    {
        name: "用户信息",
        path: "/user/space/userInfo?uid=" + uid.value,
        icon: '<svg t="1705936460886" class="icon" viewBox="0 0 1024 1024" version="1.1" xmlns="http://www.w3.org/2000/svg" p-id="6835" width="16" height="16"><path d="M512 0C371.2 0 256 115.2 256 256s115.2 256 256 256 256-115.2 256-256-115.2-256-256-256z m0 426.666667c-93.866667 0-170.666667-76.8-170.666667-170.666667s76.8-170.666667 170.666667-170.666667 170.666667 76.8 170.666667 170.666667-76.8 170.666667-170.666667 170.666667zM738.133333 597.333333H285.866667C128 597.333333 0 725.333333 0 883.2 0 960 64 1024 140.8 1024h738.133333c76.8 0 140.8-64 140.8-140.8C1024 725.333333 896 597.333333 738.133333 597.333333z m145.066667 341.333334H140.8c-29.866667 0-55.466667-25.6-55.466667-55.466667C85.333333 772.266667 174.933333 682.666667 285.866667 682.666667h456.533333c110.933333 0 200.533333 89.6 200.533333 200.533333-4.266667 29.866667-29.866667 55.466667-59.733333 55.466667z" fill="#303133" p-id="6836"></path></svg>',
    },
    {
        name: "文章",
        path: "/user/space/userArticle?page=1&uid=" + uid.value,
        icon: '<svg t="1706529813008" class="icon" viewBox="0 0 1024 1024" version="1.1" xmlns="http://www.w3.org/2000/svg" p-id="5081" width="16" height="16"><path d="M832.9 435.7H631.3c-69.1 0-125.3-56.2-125.3-125.4V128.8c0-24.7 20-44.8 44.8-44.8s44.8 20.1 44.8 44.8v181.5c0 19.7 16 35.8 35.8 35.8H833c24.7 0 44.8 20.1 44.8 44.8-0.1 24.7-20.1 44.8-44.9 44.8zM671.7 597.9H349.2c-24.7 0-44.8-20.1-44.8-44.8 0-24.7 20.1-44.8 44.8-44.8h322.5c24.7 0 44.8 20.1 44.8 44.8 0 24.7-20.1 44.8-44.8 44.8zM671.7 770.3H349.2c-24.7 0-44.8-20.1-44.8-44.8s20.1-44.8 44.8-44.8h322.5c24.7 0 44.8 20.1 44.8 44.8s-20.1 44.8-44.8 44.8z" fill="#333333" p-id="5082"></path><path d="M793.2 959.7H227.7c-68.9 0-124.9-56-124.9-124.9v-646c0-68.9 56-124.9 124.9-124.9h365.1c30.8 0 60.4 11.3 83.4 31.9l200.3 179.5c26.4 23.7 41.5 57.6 41.5 93v466.5c0 68.9-56 124.9-124.8 124.9zM227.7 153.5c-19.5 0-35.3 15.8-35.3 35.3v646.1c0 19.5 15.8 35.3 35.3 35.3h565.4c19.5 0 35.3-15.8 35.3-35.3V368.3c0-10-4.3-19.6-11.7-26.3L616.4 162.5c-6.5-5.8-14.9-9-23.6-9H227.7z" fill="#333333" p-id="5083"></path></svg>',
    },
    {
        name: "收藏",
        path: "/user/space/userCollection?page=1&uid=" + uid.value,
        icon: '<svg t="1706690570369" class="icon" viewBox="0 0 1024 1024" version="1.1" xmlns="http://www.w3.org/2000/svg" p-id="4763" width="16" height="16"><path d="M511.7 919.4c-4.6 0-9.8-0.7-14.1-2.1-9.8-3.1-241.3-78.2-366.7-295.8C62.4 502.6 23 321.3 137.3 201.8c53.2-55.6 112.9-84.1 177.7-84.8 64.9-1.5 132.4 27.4 196.3 81.9 64-54.4 131.1-82.4 196.3-81.9 64.7 0.7 124.5 29.2 177.7 84.8 114.4 119.5 74.9 300.8 6.4 419.7-125.3 217.7-356.8 292.8-366.6 295.9-4.3 1.3-8.9 2-13.4 2zM317.5 206.3H316c-39.8 0.4-78.3 19.7-114.2 57.3-75.1 78.5-50.9 214 6.4 313.5 92.3 160.3 255.8 232.4 303 250.5 47.2-18.2 211.3-91 303.2-250.5 57.3-99.5 81.5-235 6.4-313.5-35.9-37.6-74.4-56.8-114.2-57.3h-1.5c-63.8 0-122.8 45.5-161.2 83.9-1.6 1.6-3.3 3.1-5.2 4.4-7.6 6.4-17.1 9.8-27.4 10.1-11.7 0.6-24.5-5.6-33.2-15-37.8-37.9-96.8-83.4-160.6-83.4z" fill="#333333" p-id="4764"></path></svg>',
    },
    {
        name: "消息",
        path: "/user/space/userMessage?uid=" + uid.value,
        icon: '<svg t="1712300076344" class="icon" viewBox="0 0 1024 1024" version="1.1" xmlns="http://www.w3.org/2000/svg" p-id="5089" width="16" height="16"><path d="M783.1 899.3H242.9c-97.7 0-177.3-79.5-177.3-177.3V302.6c0-97.7 79.5-177.3 177.3-177.3H783c97.7 0 177.3 79.5 177.3 177.3V722c0.1 97.7-79.5 177.3-177.2 177.3zM242.9 214.8c-48.4 0-87.8 39.4-87.8 87.8V722c0 48.4 39.4 87.8 87.8 87.8H783c48.4 0 87.8-39.4 87.8-87.8V302.6c0-48.4-39.4-87.8-87.8-87.8H242.9z" fill="#333333" p-id="5090"></path><path d="M513 600.5c-24.9 0-49.9-7.3-71.6-21.8l-2.9-2.1-214.9-170.1c-19.4-15.3-22.7-43.5-7.3-62.8 15.3-19.4 43.5-22.6 62.8-7.3l213.2 168.8c12.7 7.8 28.7 7.8 41.5 0L747 336.4c19.3-15.3 47.5-12.1 62.8 7.3 15.3 19.4 12.1 47.5-7.3 62.8L584.6 578.7c-21.7 14.5-46.7 21.8-71.6 21.8z" fill="#333333" p-id="5091"></path></svg>',
    },
    {
        name: "后台",
        path: "/manage",
        icon: '<svg t="1705998965693" class="icon" viewBox="0 0 1024 1024" version="1.1" xmlns="http://www.w3.org/2000/svg" p-id="4113" width="16" height="16"><path d="M512 960.4c-23.2 0-46.5-6-67.2-17.9L173.5 785.8c-41.4-23.9-67.2-68.5-67.2-116.3V356.1c0-47.8 25.7-92.4 67.2-116.3L444.8 83.1c41.4-23.9 92.9-23.9 134.3 0l271.4 156.7c41.4 23.9 67.2 68.5 67.2 116.3v313.4c0 47.8-25.7 92.4-67.2 116.3L579.2 942.5c-20.7 11.9-44 17.9-67.2 17.9z m0-805.6c-7.7 0-15.4 2-22.3 6L218.3 317.5c-13.8 7.9-22.3 22.7-22.3 38.6v313.4c0 15.9 8.5 30.7 22.3 38.6l271.4 156.7c13.8 7.9 30.8 7.9 44.6 0l271.4-156.7c13.8-7.9 22.3-22.7 22.3-38.6V356.1c0-15.9-8.5-30.7-22.3-38.6L534.3 160.8c-6.9-4-14.6-6-22.3-6z" fill="#333333" p-id="4114"></path><path d="M514 559c-7.8 0-15.5-2-22.4-6L270.9 425.4c-21.5-12.4-28.8-39.8-16.4-61.3 12.4-21.5 39.8-28.8 61.3-16.4l198.1 114.5 189.7-110.9c21.4-12.5 48.9-5.3 61.4 16.1 12.5 21.4 5.3 48.9-16.1 61.4l-212.2 124c-7 4.1-14.9 6.2-22.7 6.2z" fill="#333333" p-id="4115"></path><path d="M512 805.8c-24.8 0-44.9-20.1-44.9-44.9V514.1c0-24.8 20.1-44.9 44.9-44.9s44.9 20.1 44.9 44.9V761c0 24.7-20.1 44.8-44.9 44.8z" fill="#333333" p-id="4116"></path></svg>',
    },
]);
async function setNav() {
    let checkAUid = ref(null);
    for (let i = 0; i < allNavItems.value.length; i++) {
        if (allNavItems.value[i].path.match(/userInfo|userMessage/) != null) {
            if (localStorage.getItem(md5("token")) == md5("nologin")) {
                continue;
            }
            if (checkAUid.value == null) {
                await checkA().then((res) => {
                    if (res.data.code == 1) {
                        checkAUid.value = res.data.data.id;
                    }
                });
            }
            if (checkAUid.value == uid.value) {
                navItems.value.push(allNavItems.value[i]);
            }
        } else if (allNavItems.value[i].path.match(/manage/) != null) {
            check().then((res) => {
                if (res.data.code == 1) {
                    navItems.value.push(allNavItems.value[i]);
                }
            });
        } else {
            navItems.value.push(allNavItems.value[i]);
        }
    }
}
router.afterEach((to, from) => {
    if (from.path.match(/space/)) {
        if (to.path == "/user/space") {
            router.back();
        }
    }
});
function Listener() {
    const SidebarMain = document.getElementsByClassName("user-space-sider");
    var scrollTop = document.documentElement.scrollTop || document.body.scrollTop;
    if (scrollTop > 600) {
        SidebarMain[0].style.top = "20px";
        SidebarMain[1].style.top = "20px";
    } else {
        SidebarMain[0].style.top = "90px";
        SidebarMain[1].style.top = "90px";
    }
}
/* 是否有未读消息 */
let noReadMsgCount = ref(0);
function hasNoReadMsg() {
    if (localStorage.getItem(md5("token")) == md5("nologin") || localStorage.getItem(md5("islogin") == md5("false"))) {
        return;
    }
    getNoReadMessageCount().then((res) => {
        if (res.data.code == 1) {
            noReadMsgCount.value = res.data.data;
        } else {
            ElMessage.error({ message: res.data.msg, offset: 120 });
        }
    });
}
function clearTip(name) {
    if (name == "消息") {
        noReadMsgCount.value = 0;
    }
}
onMounted(() => {
    window.addEventListener("scroll", Listener);
    if (useRouter.path == "/user/space")
        router.push({ path: "/user/space/userArticle", query: { page: 1, uid: uid.value } });
    setNav();
    hasNoReadMsg();
});
onUnmounted(() => {
    window.removeEventListener("scroll", Listener, false);
});
</script>

<style scoped lang="less">
.space-bg {
    width: 100%;
    height: 100%;
    position: fixed;
    background-size: cover;
    background-repeat: no-repeat;
    z-index: -99999;
    // background-image: url("../../assets/img/bg/bg1.webp");
}
.space-bg::before {
    content: "";
    position: absolute;
    top: 0;
    left: 0;
    right: 0;
    bottom: 0;
    filter: blur(10px);
    -webkit-backdrop-filter: blur(10px);
    backdrop-filter: blur(10px);
}
.user-space-right-sider {
    margin-bottom: 10px;
}
.UserInfoCard {
    position: sticky;
    top: 90px;
    width: 260px;
    transition: all 0.6s ease;
}
.user-space-main {
    display: flex;
    justify-content: center;
    position: relative;
    top: 90px;
    width: 100%;
}
.router-view {
    width: 60%;
    margin: 20px;
    margin-top: 0;
    border-radius: 4px;
    box-shadow: 0 3px 8px 6px rgba(7, 17, 27, 0.08);
    transition: all 0.3s ease;
    padding: 30px 24px;
}
.router-view:hover {
    box-shadow: 0 3px 8px 6px rgba(7, 17, 27, 0.14);
}
.user-space-left-sider {
    width: 260px;
    padding-left: 60px;
    transition: all 0.3s ease;
}
.user-space-nav {
    width: 200px;
    height: 500px;
    border-radius: 8px;
    padding: 20px;
    box-shadow: 6px 0px 6px rgba(7, 17, 27, 0.08);
    transition: all 0.6s ease;
    overflow: hidden;
    position: sticky;
    top: 70px;
}
.user-space-nav:hover {
    box-shadow: 6px 0px 6px rgba(7, 17, 27, 0.14);
}
.nav-item {
    margin: 20px 0;
    border-radius: 4px;
    transition: all 0.3s ease;
}
.nav-item:hover {
    box-shadow: 0 1px 3px 3px rgba(7, 17, 27, 0.3);
}
.router-link {
    display: block;
    display: flex;
    height: 30px;
    padding: 0 10px;
    align-items: center;
    justify-content: space-between;
}
.nav-item-left {
    display: block;
    display: flex;
    height: 30px;
    // padding: 0 10px;
    align-items: center;
    // min-width: 110px;
}
.router-link-active {
    border-radius: 4px;
    box-shadow: 0 1px 3px 3px rgba(7, 17, 27, 0.3);
}
.nav-item-icon {
    width: 16px;
    height: 16px;
    margin-right: 6px;
}
.nav-item-span {
    font-size: 16px;
    color: #000;
    user-select: none;
}
.navItemTip {
    border-radius: 50%;
    background-color: #ff8e68;
    width: 8px;
    height: 8px;
    // font-size: 12px;
    text-align: center;
    // color: #fff;
}
@media (max-width: 1200px) {
    .user-space-left-sider {
        width: 200px;
        padding-left: 20px;
    }
    .user-space-nav {
        width: 180px;
        padding: 10px;
    }
    .UserInfoCard {
        width: 200px;
        margin: 0;
    }
    .nav-item-span {
        font-size: 14px;
    }
}
@media (max-width: 600px) {
    .user-space-main {
        flex-direction: column;
        width: 100%;
        padding-top: 0;
    }
    .user-space-left-sider {
        width: 100%;
        padding-left: 0;
    }
    .user-space-nav {
        height: auto;
        width: 100%;
    }
    .router-view {
        min-width: unset;
        width: 100%;
        margin: 10px 0;
    }
    .UserInfoCard {
        width: 100%;
        margin: 0;
    }
}
</style>
