<template>
    <div class="imgContent">
        <div class="Col">
            <div v-for="(col, item1) in colData" :key="item1" class="colImg">
                <div class="Img" v-for="(url, item2) in col" :key="item2">
                    <div class="image-mask" :id="'mask-data-' + url.id">
                        <img
                            :src="url.url"
                            :id="'data-' + url.id"
                            :alt="url.name"
                            @load="loadImageSuccess(url)"
                            @error="loadImageSuccess(url)"
                            @click="preImg(item1, item2)" />
                    </div>
                </div>
            </div>
        </div>
    </div>
</template>

<script setup>
import { onMounted, ref, watchEffect, onActivated } from "vue";
import { preview } from "v-preview-image";
import router from "@/router";
const props = defineProps({
    imgUrls: {
        type: Array,
        default: [],
    },
    /* 列 */
    col: {
        type: Number,
        default: 1,
    },
});
const emit = defineEmits(["load"]);
// 图片数据
var imgs = ref([]);
// 列数据 colData = [[1,4,7],[2,5,8],[3,6,9]]
var colData = ref([]);

// 监听图片数据变化
watchEffect(a);
var localCol = props.col;
function a() {
    props.imgUrls;
    if (localCol == null) {
        localCol = props.col;
    } else if (localCol == props.col) {
        updata();
    } else {
        isCurrCol();
    }
}
function isCurrCol() {
    if (localCol != props.col) {
        localCol = props.col;
        count = 1;
        addImg();
    }
}
/* 更新图片数据 */
function updata() {
    for (let i = 0; i < props.imgUrls.length; i++) {
        imgs.value.push(props.imgUrls[i]);
    }
    addImg();
}

/* 添加图片 */
function addImg() {
    colData.value = [];
    // 每列行数
    var rownum = Math.ceil(imgs.value.length / props.col);
    //循环列 共 props.col 列
    for (var i = 1; i <= props.col; i++) {
        var data = [];
        // 循环行 共 rownum 行。j 的起始值为当前行,即为 i, 每次步进数值为列数(props.col)
        for (var k = 1, j = i; k <= rownum; k++, j += props.col) {
            // 当前列数据
            if (imgs.value[j - 1] != null) {
                data.push(imgs.value[j - 1]);
            } else {
                continue;
            }
        }
        colData.value.push(data);
    }
}
/* 图片加载完成 执行 */
var count = 0; // 记录当前加载图片数量
let loaded = [];
function loadImageSuccess(item) {
    count = count + 1;
    if (count === imgs.value.length) {
        emit("load", true); // 图片全部加载完成向父组件发送完成事件
    }
    loaded.push(item);
    if (router.currentRoute.value.path == "/index/imageView") showLoadedImage();
}
function showLoadedImage() {
    for (let i = 0; i < loaded.length; i++) {
        const ele = document.getElementById("data-" + loaded[i].id);
        ele.style.opacity = 1;
    }
}
/* 预览图片 */
function preImg(i1, i2) {
    let a = i2 * props.col + i1;
    preview(a, imgs.value, "url");
}

onMounted(() => {});
onActivated(() => {
    showLoadedImage();
    if (count === 3) {
        emit("load", true); // 图片全部加载完成向父组件发送完成事件
        count = 0;
    }
});
</script>

<style scoped>
.imgContent {
    width: 100%;
}
.Img img {
    width: 100%;
    opacity: 0;
    border-radius: 4px;
    box-shadow: 0 1px 6px 4px rgba(7, 17, 27, 0.3);
    transition: all 0.3s ease;
}
.Img img:hover {
    box-shadow: 0 1px 6px 4px rgba(7, 17, 27, 0.6);
}
.Img {
    margin-bottom: 14px;
}
.Col {
    display: flex;
    justify-content: space-between;
}
.colImg {
    flex: 1;
    display: flex;
    flex-direction: column;
    margin: 0 7px;
}
.image-mask {
    /* background-color: aliceblue; */
}
</style>
