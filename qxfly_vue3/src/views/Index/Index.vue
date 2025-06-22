<template>
    <div>
        <div class="Main" id="Main">
            <!-- 左侧栏资源列表 -->
            <LeftSidebar class="LeftSidebar"></LeftSidebar>
            <!-- 内容主体 -->
            <CentreContent class="CentreContent"></CentreContent>
            <!-- 右侧栏资源列表 -->
            <RightSidebar></RightSidebar>
        </div>
        <BackTop></BackTop>
    </div>
    <div class="Footer">
        <div class="footer">
            <!-- <a class="IPCbeian" href="https://beian.miit.gov.cn" target="_blank">闽ICP备2024032632号</a>
            <a
                class="GngAnbeian"
                href="https://beian.mps.gov.cn/#/query/webSearch?code="
                rel="noreferrer"
                target="_blank">
                闽公网安备35080202351433
            </a> -->
            <a class="github" href="https://github.com/qxFly/qxfly-blog" target="_blank">qxfly-blog</a>
        </div>
    </div>
</template>

<script setup>
import LeftSidebar from "@/views/Index/Sider/LeftSidebar.vue";
import CentreContent from "@/views/Index/CentreContent";
import RightSidebar from "@/views/Index/Sider/RightSidebar";
import BackTop from "@/components/BackTop.vue";
import { onMounted, onUnmounted } from "vue";

function ListenerSiders() {
    const indexSoders = document.getElementsByClassName("index-siders");
    var scrollTop = document.documentElement.scrollTop || document.body.scrollTop;
    if (scrollTop > 600) {
        indexSoders[0].style.top = "20px";
        indexSoders[1].style.top = "20px";
    } else {
        indexSoders[0].style.top = "90px";
        indexSoders[1].style.top = "90px";
    }
}

onMounted(() => {
    window.addEventListener("scroll", ListenerSiders);
});
onUnmounted(() => {
    window.removeEventListener("scroll", ListenerSiders, false);
});
</script>

<style scoped>
.Main {
    display: flex;
    justify-content: center;
    padding-top: 100px;
}
/* .CentreContent {
    border-radius: 4px;
    box-shadow: 0 3px 8px 6px rgba(7, 17, 27, 0.05);
}
.CentreContent:hover {
    box-shadow: 0 3px 8px 6px rgba(7, 17, 27, 0.1);
} */
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

@media (max-width: 800px) {
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
