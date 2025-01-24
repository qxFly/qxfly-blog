<template>
    <div class="space-bg theme-bg" id="space-bg"></div>
    <!-- <TopBar></TopBar> -->
    <div class="user-space-main">
        <div class="user-space-left-sider">
            <div class="user-space-nav user-space-sider">
                <div class="nav-item" v-for="item in showNavigations" :key="item">
                    <router-link
                        :to="item.path"
                        active-class="router-link-active"
                        class="router-link"
                        @click="clearTip(item.name)">
                        <div class="nav-item-left">
                            <i class="iconfont" v-html="item.icon"></i>
                            <div class="nav-item-span">{{ item.name }}</div>
                        </div>
                        <div class="nav-item-right">
                            <div class="navItemTip" v-if="noReadMsgCount > 0 && item.name == '消息'"></div>
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
import { onMounted, onUnmounted, ref } from "vue";
import md5 from "js-md5";
import { useRoute } from "vue-router";
import router from "@/router";
import { checkA } from "@/api/Article/index";
import { getNoReadMessageCount, listUserSpaceNav } from "@/api/User/index";
import { ElMessage } from "element-plus";
let useRouter = useRoute();
/* 设置菜单栏 */
let uid = ref(parseInt(useRouter.query.uid));
/* 获取用户空间导航栏 */
let navigations;
let showNavigations = ref([]);
function getUserSpaceNav() {
    listUserSpaceNav().then((res) => {
        if (res.data.code == 1) {
            navigations = res.data.data;
            setNav();
        }
    });
}

async function setNav() {
    for (let i = 0; i < navigations.length; i++) {
        //检查是否为自己空间
        if (navigations[i].path.match(/userInfo|userMessage/) != null) {
            if (localStorage.getItem(md5("token")) != null && isSelfSpace.value != null) {
                if (isSelfSpace.value == uid.value) {
                    setNavPathParam(navigations[i]);
                    showNavigations.value.push(navigations[i]);
                }
            } else {
                continue;
            }
        } else {
            setNavPathParam(navigations[i]);
            showNavigations.value.push(navigations[i]);
        }
    }
}
// 设置路径参数
function setNavPathParam(nav) {
    if (nav.path.match(/uid/)) nav.path = nav.path + uid.value;
}
//检查是否为自己空间,是=uid，否=null
let isSelfSpace = ref(null);
async function checkIsSelfSpace() {
    await checkA().then((res) => {
        if (res.data.code == 1) {
            isSelfSpace.value = res.data.data.id;
        }
    });
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
    if (localStorage.getItem(md5("token")) == null || localStorage.getItem(md5("islogin") == md5("false"))) return;
    getNoReadMessageCount().then((res) => {
        if (res.data.code == 1) {
            noReadMsgCount.value = res.data.data;
        } else {
            ElMessage.error({ message: res.data.msg, offset: 120 });
        }
    });
}
function clearTip(name) {
    if (name == "消息") noReadMsgCount.value = 0;
}
onMounted(async () => {
    if (useRouter.path == "/user/space")
        router.push({ path: "/user/space/userArticle", query: { page: 1, uid: uid.value } });
    window.addEventListener("scroll", Listener);
    await checkIsSelfSpace();
    getUserSpaceNav();
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
    // filter: blur(10px);
    // -webkit-backdrop-filter: blur(10px);
    // backdrop-filter: blur(10px);
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
    min-width: 550px;
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
.iconfont {
    font-family: iconfont !important;
    font-style: normal;
    margin-right: 4px;
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
