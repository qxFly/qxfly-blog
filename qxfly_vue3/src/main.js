import { createApp } from "vue";
import App from "./App.vue";

import ElementPlus from "element-plus";

import zhCn from "element-plus/dist/locale/zh-cn.mjs";
import "element-plus/dist/index.css";
import router from "./router";
import request from "./utils/request";

import vPreviewImage from "v-preview-image";
import defaultImg from "@/assets/avatar.png";

import Cardview from "@/components/CardView.vue";
const app = createApp(App);

const CardView = {
    install(app) {
        // 全局注册组件
        app.component("CardView", Cardview);
    },
};
// const defineDirective = (app) => {
//     // 图片懒加载指令 v-lazy
//     // 原理：先存储图片地址不能在SRC上，当图片进入可视区，将存储的图片地址设置给图片元素即可
//     app.directive("lazy", {
//         // vue2.0 监听使用指令的DOM是否创建好，钩子函数：inserted
//         // vue3.0 的指令拥有的钩子函数和组件的一样，使用指令的DOM是否创建好，钩子函数：mounted
//         mounted(el, binding) {
//             // 1.创建一个对象，来观察当前使用指令的元素
//             const observe = new IntersectionObserver(
//                 ([{ isIntersecting }]) => {
//                     if (isIntersecting) {
//                         // 2.进入可视区,停止观察
//                         observe.unobserve(el);
//                         // 3.将指令的值(bidding.value)设置给el的SRC属性
//                         el.src = binding.value;
//                         // 4.处理图片加载失败,显示默认图
//                         el.onerror = () => {
//                             el.src = defaultImg;
//                         };
//                     }
//                 },
//                 {
//                     threshold: 0,
//                 }
//             );
//             // 开启观察
//             observe.observe(el);
//         },
//     });
// };

// app.config.globalProperties.request = request;
app.use(ElementPlus, {
    locale: zhCn,
});
app.use(CardView);
// app.use(defineDirective);
// app.use(vPreviewImage);
app.use(router);
app.mount("#app");
