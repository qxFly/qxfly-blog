<template>
    <div id="top-bar-1">
        <div class="top-bar-bg" id="top-bar-bg"></div>
        <div id="top-bar-2">
            <UserImg></UserImg>
            <div class="top-bar-right">
                <TopBarNavigation class="TopBarNavigation"></TopBarNavigation>
                <LoginLabel></LoginLabel>
            </div>
        </div>
    </div>
</template>

<script setup>
import TopBarNavigation from "./TopBarNavigation.vue";
import LoginLabel from "./LoginLabel.vue";
import UserImg from "./UserImg.vue";
import { onMounted, onUnmounted } from "vue";
const props = defineProps({
    hide: {
        type: Boolean,
        default: false,
    },
});
function Listener() {
    const topbar = document.getElementById("top-bar-1");
    var scrollTop = document.documentElement.scrollTop || document.body.scrollTop;
    if (scrollTop > 600) {
        topbar.style.top = "-70px";
    } else {
        topbar.style.top = "0";
    }
}
/* 当状态栏收起时，监听鼠标 */
function ListenerMouser(event) {
    let mouseX = event.clientX;
    let mouseY = event.clientY;
    // console.log(mouseX, mouseY);
    const topbar = document.getElementById("top-bar-1");
    var scrollTop = document.documentElement.scrollTop || document.body.scrollTop;
    if (scrollTop > 600) {
        if (mouseY < 70) {
            topbar.style.top = "0px";
        } else {
            topbar.style.top = "-70px";
        }
    }
}
/* 当状态栏长时间不操作时，透明化 */
function ListenerTime() {
    const topbarbg = document.getElementById("top-bar-bg");
    const topbar = document.getElementById("top-bar-1");
    if (topbar == null || topbarbg == null) return;
    topbar.addEventListener("mousemove", mousemove);
    topbar.addEventListener("mouseleave", mouseleave);
    mouseleave();
}
function mouseleave() {
    const topbarbg = document.getElementById("top-bar-bg");
    if (topbarbg == null) return;
    setTimeout(() => {
        topbarbg.style.opacity = "0.2";
    }, 2000);
}
function mousemove() {
    const topbarbg = document.getElementById("top-bar-bg");
    if (topbarbg == null) return;
    topbarbg.style.opacity = "1";
    topbarbg.style.backgroundImage = "linear-gradient(to right, #84c6ff, #b1dbff)";
}
onMounted(() => {
    // loadUserTheme();
    setTimeout(() => {
        const topbar = document.getElementById("top-bar-1");
        if (topbar != null) topbar.style.top = "0px";
    }, 300);
    ListenerTime();
    window.addEventListener("scroll", Listener);
    document.addEventListener("mousemove", ListenerMouser);
});
onUnmounted(() => {
    window.removeEventListener("scroll", Listener, false);
    window.removeEventListener("mousemove", ListenerMouser, false);
});
</script>

<style>
#top-bar-1 {
    display: block; /* 设定块元素独占一行 */
    position: fixed;
    width: 100%;
    height: 70px;
    margin: 0;
    top: -70px;
    /* background-image: linear-gradient(to right, #b0c1ff, #b8b0ff); */
    z-index: 9999;
    /* box-shadow: 0 1px 6px 4px rgba(7, 17, 27, 0.5);*/
    transition: all 0.4s ease;
}
#top-bar-1:hover {
    /* background-image: linear-gradient(to right, #84c6ff, #b1dbff); */
    box-shadow: 0 1px 6px 4px rgba(7, 17, 27, 0.5);
}
.top-bar-bg {
    width: 100%;
    height: 70px;
    position: fixed;
    background-image: linear-gradient(to right, #84c6ff, #b1dbff);
    box-shadow: 0 1px 6px 4px rgba(7, 17, 27, 0.5);
    transition: all 1s ease;
}
#top-bar-2 {
    position: relative;
    /* min-width: 1080px; */
    height: 70px;
    margin: 0;
    display: flex;
    width: 100%;
    padding: 0 80px;
    justify-content: space-between;
}
.top-bar-right {
    display: flex;
}
.TopBarNavigation {
    margin-right: 16px;
}
@media (max-width: 1100px) {
}
@media (max-width: 700px) {
    .top-bar-right {
        /* width: 100%; */
    }
    #top-bar-1 {
        padding: 0;
    }
    #top-bar-2 {
        padding: 0 10px 0 0;
        min-width: unset;
        /* justify-content: left; */
    }
}
</style>
