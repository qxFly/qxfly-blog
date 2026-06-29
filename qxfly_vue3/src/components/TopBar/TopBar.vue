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
import { nextTick, onMounted, onUnmounted, onUpdated } from "vue";
import router from "@/router";
import * as TopBarUtils from "@/utils/TopBarUtils";
const props = defineProps({
  hide: {
    type: Boolean,
    default: false,
  },
});
function setTopbar() {
  setTimeout(() => {
    const topbar = document.getElementById("top-bar-1");
    if (topbar != null) topbar.style.top = "0px";
  }, 300);
}
onMounted(() => {
  setTopbar();
  TopBarUtils.ListenerTime();
  TopBarUtils.addEventListenerHigt();
  TopBarUtils.addEventListenerMouser();
});
onUnmounted(() => {
  TopBarUtils.removeEventListenerHigt();
  TopBarUtils.removeEventListenerMouser();
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
  /* background-image: linear-gradient(to right, var(--main-theme-color-blue), #b1dbff); */
  box-shadow: 0 1px 6px 4px rgba(7, 17, 27, 0.5);
}
.top-bar-bg {
  width: 100%;
  height: 70px;
  position: fixed;
  background-image: linear-gradient(to right, var(--main-theme-color-blue), #b1dbff);
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
@media (max-width: 800px) {
  .top-bar-right {
    /* width: 100%; */
  }
  #top-bar-1 {
    padding: 0;
    height: 50px;
  }
  #top-bar-2 {
    padding: 0 10px 0 0;
    min-width: unset;
    height: 50px;
    justify-content: left;
  }
  .top-bar-bg {
    height: 55px;
  }
}
</style>
