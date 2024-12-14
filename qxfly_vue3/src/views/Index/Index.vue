<template>
    <div>
        <!-- 背景主体 -->
        <div id="index_bg" class="theme-bg"></div>
        <!-- 顶栏 -->
        <TopBar></TopBar>

        <div class="Main" id="Main">
            <!-- 左侧栏资源列表 -->
            <LeftSidebar class="LeftSidebar"></LeftSidebar>

            <!-- 内容主体 -->

            <CentreContent class="CentreContent"></CentreContent>

            <!-- 右侧栏资源列表 -->
            <RightSidebar></RightSidebar>
        </div>

        <BackTop></BackTop>
        <!-- <ChangeBackgroundImage class="changebg"></ChangeBackgroundImage> -->
    </div>
    <div class="Footer">
        <div class="footer">
            <a class="IPCbeian" href="https://beian.miit.gov.cn" target="_blank">闽ICP备2024032632号</a>
            <a
                class="GngAnbeian"
                href="https://beian.mps.gov.cn/#/query/webSearch?code="
                rel="noreferrer"
                target="_blank">
                闽公网安备35080202351433
            </a>
            <!-- <a class="github" href="https://github.com/qxFly" target="_blank">qxFly</a> -->
        </div>
    </div>
</template>

<script setup>
import TopBar from "@/components/TopBar/TopBar.vue";
import LeftSidebar from "@/views/Sider/LeftSidebar.vue";
import CentreContent from "@/views/Index/CentreContent";
import RightSidebar from "@/views/Sider/RightSidebar";
import BackTop from "@/components/BackTop.vue";
import ChangeBackgroundImage from "./ChangeBackgroundImage.vue";
import { onMounted, onUnmounted } from "vue";
import router from "@/router";
// 判断是否是手机端，如果是，返回true
function isMobile() {
    let flag = navigator.userAgent.match(
        /(phone|pad|pod|iPhone|iPod|ios|iPad|Android|Mobile|BlackBerry|IEMobile|MQQBrowser|JUC|Fennec|wOSBrowser|BrowserNG|WebOS|Symbian|Windows Phone)/i
    );
    return flag;
}
function Listener() {
    const rightSidebarMain = document.getElementsByClassName("between-sider");
    var scrollTop = document.documentElement.scrollTop || document.body.scrollTop;
    if (scrollTop > 600) {
        rightSidebarMain[0].style.top = "20px";
        rightSidebarMain[1].style.top = "20px";
    } else {
        rightSidebarMain[0].style.top = "90px";
        rightSidebarMain[1].style.top = "90px";
    }
}
onMounted(() => {
    window.addEventListener("scroll", Listener);
    // 根据不同路由跳转不同页面
    if (sessionStorage.getItem("userAgent") == null) {
        if (isMobile()) {
            console.log("手机端");
            sessionStorage.setItem("userAgent", "phone");
            router.replace({
                path: "/articleView?page=1",
            });
        } else {
            console.log("pc端");
            sessionStorage.setItem("userAgent", "pc");
            router.replace({
                path: "/articleView?page=1",
            });
        }
    }
});
onUnmounted(() => {
    window.removeEventListener("scroll", Listener, false);
});
</script>

<style scoped>
/* 主背景 */
#index_bg {
    width: 100%;
    height: 100%;
    position: fixed;
    background-size: cover;
    background-repeat: no-repeat;
    z-index: -99999;
}
#index_bg::before {
    content: "";
    position: absolute;
    top: 0;
    left: 0;
    right: 0;
    bottom: 0;
    z-index: -1;
    filter: blur(10px);
    -webkit-backdrop-filter: blur(10px);
    backdrop-filter: blur(10px);
    background-size: cover;
    background-repeat: no-repeat;
}
.Main {
    display: flex;
    justify-content: center;
    padding-top: 100px;
}
.CentreContent {
    border-radius: 4px;
    box-shadow: 0 3px 8px 6px rgba(7, 17, 27, 0.05);
}
.CentreContent:hover {
    box-shadow: 0 3px 8px 6px rgba(7, 17, 27, 0.1);
    /* transform: scale(1.005); */
}
.changebg {
    position: fixed;
    left: 50px;
    bottom: 50px;
}

/* 页脚 */
.Footer {
    display: flex;
    justify-content: center;
    align-items: center;
    height: 70px;
}
.footer {
    color: #555555;
    display: flex;
    flex-direction: column;
    justify-content: center;
    align-items: center;
}
.GngAnbeian::before {
    background-repeat: no-repeat;
    width: 16px;
    height: 16px;
    display: inline-block;
    content: "";
    position: relative;
    top: 2px;
    background-image: url("../../assets/img/icon/备案图标.png");
    background-size: cover;
}
.github {
    color: #555555;
    border-bottom: 1px solid #242424;
    font-weight: 700;
}
.github::before {
    content: "\e628";
    font-family: "iconfont";
    padding-right: 5px;
}
.footer a {
    margin-bottom: 1px;
}
.footer a:link {
    color: #333;
}
.footer a:visited {
    color: #333;
}
.footer a:hover {
    color: #333;
    border-bottom: 1px solid #333;
    margin-bottom: 0;
}
.footer a:active {
    color: #333;
}

@media (max-width: 700px) {
    .Main {
        flex-direction: column;
        width: 100%;
        padding-top: 70px;
    }
    .LeftSidebar {
        width: 100%;
    }
}
</style>
