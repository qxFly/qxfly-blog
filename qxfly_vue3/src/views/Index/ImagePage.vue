<template>
    <div class="image-main">
        <div style="display: flex; flex-direction: column">
            <div class="article-sort-select">
                <div class="article-sort">
                    <div class="article-sort-label" v-text="sortLabel"></div>
                    <div class="article-sort-item" value="new" @click="Sort('new')">最新发布</div>
                    <div class="article-sort-item" value="hot" @click="Sort('old')">最早发布</div>
                    <div class="article-sort-item" value="likes" @click="Sort('random')">随机</div>
                </div>
            </div>
            <!-- 视图菜单 -->
            <div class="viewMenu">
                <div class="grid-list" @click="column = 3">
                    <div class="menu-font grid">网格视图</div>
                </div>
                <div class="menu-tips">受网络影响，可能会加载失败</div>
                <div class="grid-list" @click="column = 1">
                    <div class="menu-font list">列表视图</div>
                </div>
            </div>
            <!-- 内容区 -->
            <div class="imageContent">
                <AddImg :imgUrls="imgList" :col="column" @load="isload" :key="AddImgID"></AddImg>
            </div>
        </div>
        <div>
            <div class="getImg" v-text="getImgTips" @click="getImg(6)" v-if="getImgTips == '加载更多'"></div>
            <div class="getImg" v-text="getImgTips" v-if="getImgTips == '加载中'"></div>
        </div>
    </div>
</template>

<script setup>
import { onMounted, onUnmounted, ref } from "vue";
import { defineAsyncComponent } from "vue";
import AddImg from "@/components/AddImg.vue";
import { getImage } from "@/api/index";
import { ElMessage } from "element-plus";
/* 异步组件（暂无用处） */
const AsyncAddImg = defineAsyncComponent(async () => {
    const module = await import("@/components/AddImg.vue");
    return module.default;
});

var column = ref(3); //列数

var getImgTips = ref("加载更多");
var imgList = ref([]); //图片列表
let allImgList = []; //以获取的所有图片
/* 获取图片 */
var timeout; // 计时器，如果超过一分钟图片还没加载完成，则可强制加载下一张

let currPage = 0;
let pageSize = 3;
let sort = ref("new");
let pushImgList = [];
function getImg(count) {
    pageSize = count;
    currPage++;
    getImgTips.value = "加载中";
    load.value = false;
    setTimeout(() => {
        getImage(currPage, pageSize, sort.value).then((res) => {
            if (sort.value != "random") {
                if (res.data.data.length > 0) {
                    imgList.value = res.data.data;
                } else {
                    getImgTips.value = "加载更多";
                    ElMessage.error({ message: "没有更多图片了", offset: 300 });
                    return;
                }
            } else {
                let tempImgList = res.data.data;
                let reGetCount = 0; /* 判断是否有重复 */
                if (allImgList.length > 0) {
                    for (let i = 0; i < tempImgList.length; i++) {
                        let f = false;
                        for (let j = 0; j < allImgList.length; j++) {
                            if (tempImgList[i].id == allImgList[j].id) {
                                reGetCount++;
                                f = true;
                                break;
                            }
                        }
                        if (!f) {
                            allImgList.push(tempImgList[i]);
                            pushImgList.push(tempImgList[i]);
                        }
                    }
                } else {
                    for (let i = 0; i < tempImgList.length; i++) {
                        let f = false;
                        for (let j = i + 1; j < tempImgList.length; j++) {
                            if (tempImgList[i].id == tempImgList[j].id) {
                                reGetCount++;
                                f = true;
                                break;
                            }
                        }
                        if (!f) {
                            allImgList.push(tempImgList[i]);
                            pushImgList.push(tempImgList[i]);
                        }
                    }
                }
                if (reGetCount != 0) {
                    getImg(reGetCount);
                }
                if (pushImgList.length >= 3) {
                    imgList.value = pushImgList;
                    pushImgList = [];
                }
            }
        });
    }, 2000);
    clearTimeout(timeout);
    // 计时器，如果超过一分钟图片还没加载完成，则可强制加载下一张
    timeout = setTimeout(() => {
        if (getImgTips.value != "加载更多") {
            getImgTips.value = "加载更多";
        }
    }, 600000);
}

/* 获取图片加载状态 */
var load = ref(false); //判断第一列是否加载完成
function getLoadStatue() {
    if (load.value) {
        getImgTips.value = "加载更多";
    }
}
function isload(flag) {
    load.value = flag;
    getLoadStatue();
}
let sortLabel = ref("最新发布");
let AddImgID = ref(0);
function Sort(value) {
    sort.value = value;
    AddImgID.value++;
    currPage = 0;
    if (value == "random") {
        sortLabel.value = "随机排序";
    } else if (value == "new") {
        sortLabel.value = "最新发布";
    } else if (value == "old") {
        sortLabel.value = "最早发布";
    }
    getImg(6);
}
onMounted(() => {
    getImgTips.value = "加载中";
    setTimeout(() => {
        getImg(6);
    }, 1000);
});
onUnmounted(() => {
    clearTimeout(timeout);
});
</script>

<style scoped>
.image-main {
    text-align: center;
    /* margin-bottom: 20px; */
}
.imageContent {
    padding: 10px 5px;
    overflow: hidden;
}
.listContent {
    display: block;
}

.viewMenu {
    display: flex;
    justify-content: space-between;
    font-weight: 600;
    letter-spacing: 2px;
    padding: 1px;
    margin: 0 0 0 0;
    border-radius: 4px;
    align-items: center;
    box-shadow: 0 1px 6px 4px rgba(7, 17, 27, 0.08);
    -webkit-user-select: none;
    -moz-user-select: none;
    -ms-user-select: none;
    user-select: none;
    transition: all 0.3s ease-in-out;
}
.viewMenu:hover {
    box-shadow: 0 1px 6px 4px rgba(7, 17, 27, 0.14);
}
.grid-list {
    display: flex;
    padding: 4px 12px;
    transition: all 0.15s ease-in-out;
}

.grid-list:hover {
    cursor: pointer;
    box-shadow: inset 0 1px 6px 4px rgba(7, 17, 27, 0.14);
    border-radius: 4px;
}

/* .grid::before {
    content: "\e608";
    font-family: "iconfont";
    margin-right: 4px;
}
.list::before {
    content: "\e608";
    font-family: "iconfont";
    margin-right: 4px;
} */
.menu-font {
    line-height: 30px;
    font-size: 16px;
    white-space: nowrap; /* 防止文本换行 */
    overflow: hidden; /* 隐藏超出容器的部分 */
    text-overflow: ellipsis; /* 用省略号表示被裁剪的文本 */
}
.menu-tips {
    font-size: 10px;
    font-weight: 500;
    white-space: nowrap; /* 防止文本换行 */
    overflow: hidden; /* 隐藏超出容器的部分 */
    text-overflow: ellipsis; /* 用省略号表示被裁剪的文本 */
}
.article-sort-select {
    position: relative;
    min-width: 90px;
    text-align: center;
    margin-top: 10px;
}
.article-sort {
    position: absolute;
    top: -30px;
    height: 26px;
    width: 100%;
    overflow: hidden;
    border-radius: 4px;
    border: 1px solid #000;
    background-color: #fff;
    z-index: 9;
    transition: all 0.2s ease;
}
.article-sort:hover {
    height: 99px;
}
.article-sort-item {
    font-size: 15px;
    padding: 2px 4px;
    cursor: pointer;
    font-weight: 500;
    letter-spacing: normal;
}
.article-sort-label {
    font-size: 15px;
    padding: 2px 4px;
    border-bottom: 1px solid #000;
    cursor: pointer;
    font-weight: 500;
    letter-spacing: normal;
}
.article-sort-item:hover {
    background-color: #84c6ff;
}
/* 获取更多图片 */
.getImg {
    font-size: 20px;
    font-weight: 600;
    letter-spacing: 10px;
    padding: 9px;
    margin: 0;
    border-radius: 4px;
    box-shadow: 0 1px 6px 4px rgba(7, 17, 27, 0.08);
    -webkit-user-select: none;
    -moz-user-select: none;
    -ms-user-select: none;
    user-select: none;
    transition: all 0.3s ease-in-out;
}
.getImg:hover {
    box-shadow: 0 1px 6px 4px rgba(7, 17, 27, 0.14);
}

@media (max-width: 700px) {
    .article-sort-select {
        height: 30px;
    }
    .article-sort {
        top: 0;
    }
}
</style>
