<template>
    <div id="top-bar-1">
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

onMounted(() => {
    // loadUserTheme();
    setTimeout(() => {
        const topbar = document.getElementById("top-bar-1");
        if (topbar != null) topbar.style.top = "0px";
    }, 300);

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
    padding: 0 80px;
    background-image: linear-gradient(to right, #84c6ff, #b1dbff);
    /* background-image: linear-gradient(to right, #b0c1ff, #b8b0ff); */
    z-index: 9999;
    box-shadow: 0 1px 6px 4px rgba(7, 17, 27, 0.5);
    transition: all 0.4s ease;
}
#top-bar-1:hover {
    background-image: linear-gradient(to right, #84c6ff, #b1dbff);
    box-shadow: 0 1px 6px 4px rgba(7, 17, 27, 0.5);
}
#top-bar-2 {
    position: relative;
    /* min-width: 1080px; */
    height: 70px;
    margin: 0;
    display: flex;
    width: 100%;
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
