import { createApp } from "vue";
import App from "./App.vue";

import ElementPlus from "element-plus";

import zhCn from "element-plus/dist/locale/zh-cn.mjs";
import "element-plus/dist/index.css";
import router from "./router";

import Cardview from "@/components/CardView.vue";
import "@/assets/css/iconfont.css";
import "@/assets/css/common.css";
const app = createApp(App);

const CardView = {
    install(app) {
        // 全局注册组件
        app.component("CardView", Cardview);
    },
};

app.use(ElementPlus, {
    locale: zhCn,
});
app.use(CardView);
app.use(router);
app.mount("#app");
