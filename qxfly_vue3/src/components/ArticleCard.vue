<template>
    <div>
        <div class="articles-item">
            <a @click="ArticleDetail(article)" class="article-item-a">
                <div class="article-detail-load" :id="'article-detail-load-' + article.id">加 载 中...</div>
                <div
                    class="item-cover"
                    :class="{ right: index % 2 == 0 }"
                    :id="'item-cover-' + article.id"
                    v-if="article.cover != ''">
                    <img :src="article.cover" alt="" />
                </div>
                <div class="item-info" :id="'item-info-' + article.id">
                    <div class="item-title" :class="{ itemTitleCenter: article.title == '暂无文章' }">
                        {{ article.title }}
                    </div>
                    <div class="item-time" v-if="article.createTime != null">
                        <svg
                            style="margin-right: 4px"
                            t="1704791330489"
                            class="icon"
                            viewBox="0 0 1024 1024"
                            version="1.1"
                            xmlns="http://www.w3.org/2000/svg"
                            p-id="7432"
                            width="16"
                            height="16">
                            <path
                                d="M378.857631 467.61921l-88.76158 0 0 88.76158 88.76158 0L378.857631 467.61921zM556.38079 467.61921l-88.76158 0 0 88.76158 88.76158 0L556.38079 467.61921zM733.903949 467.61921l-88.76158 0 0 88.76158 88.76158 0L733.903949 467.61921zM822.665529 156.952658l-44.38079 0 0-88.76158-88.76158 0 0 88.76158L334.475817 156.952658l0-88.76158-88.76158 0 0 88.76158-44.38079 0c-49.262984 0-88.317465 39.942711-88.317465 88.76158l-0.444115 621.332081c0 48.818869 39.498596 88.76158 88.76158 88.76158l621.332081 0c48.818869 0 88.76158-39.942711 88.76158-88.76158L911.427108 245.714238C911.427108 196.895369 871.484398 156.952658 822.665529 156.952658zM822.665529 867.046319 201.333448 867.046319 201.333448 378.857631l621.332081 0L822.665529 867.046319z"
                                p-id="7433"></path>
                        </svg>
                        <div v-if="article.updateTime != article.createTime">
                            <span class="label">更新于:</span>
                            <span class="date">{{ adjustDate(article.updateTime) }}</span>
                            <span class="time">{{ adjustTime(article.updateTime) }}</span>
                        </div>
                        <div v-if="article.updateTime == article.createTime">
                            <span class="label">发布于:</span>
                            <span class="date">{{ adjustDate(article.createTime) }}</span>
                            <span class="time">{{ adjustTime(article.createTime) }}</span>
                        </div>
                    </div>
                    <!-- <hr /> -->
                    <div class="item-content">
                        <div class="content">
                            {{ article.preview }}
                        </div>
                    </div>
                    <div class="item-actions" v-if="article.createTime != null">
                        <div class="item-action-item item-action-author" @click.stop="toUserSpace(article.authorId)">
                            <span class="item-action-label" v-if="isDisPlayActionLabel()">作者:</span>
                            {{ article.author }}
                        </div>
                        <div class="item-action-item item-action-views">
                            <span class="item-action-label" v-if="isDisPlayActionLabel()">浏览:</span
                            >{{ article.views }}
                        </div>
                        <div class="item-action-item item-action-likes">
                            <span class="item-action-label" v-if="isDisPlayActionLabel()">点赞:</span
                            >{{ article.likes }}
                        </div>
                        <div
                            class="item-action-item item-action-classify"
                            v-if="article.classify != null && article.classify != ''">
                            <span class="item-action-label" v-if="isDisPlayActionLabel()">分类:</span
                            >{{ article.classify }}
                        </div>
                        <div class="item-action-item item-action-tags" v-if="article.tag != null && article.tag != ''">
                            <span class="item-action-label" v-if="isDisPlayActionLabel()">标签:</span>
                            {{ article.tag }}
                        </div>
                    </div>
                </div>
            </a>
        </div>
    </div>
</template>
<script setup>
import router from "@/router";
import mymd5 from "@/utils/md5.js";
import { ref } from "vue";

const props = defineProps({
    article: {
        type: Object,
        default: {
            content: "",
        },
    },
    index: {
        type: Number,
        default: 0,
    },
});
/* 查看文章 */
let isEditMode = ref(false);
function ArticleDetail(article) {
    var id = article.id;
    if (id == null) return;
    document.getElementById("article-detail-load-" + id).style.display = "block";
    document.getElementById("item-cover-" + id).style.display = "none";
    document.getElementById("item-info-" + id).style.display = "none";
    let uid = localStorage.getItem("uid");
    if (uid == article.authorId) {
        isEditMode.value = true;
    }
    article.cover = article.cover.replace(/\\/g, "/");
    router.push({
        name: "ArticleDetail",
        params: {
            id: id,
            editMode: mymd5.generateMD5BySalt(isEditMode.value),
            salt: mymd5.getSalt(),
            cover: article.cover,
        },
    });
}
/* 跳转用户空间 */
function toUserSpace(id) {
    router.push({
        path: "/user/space/userArticle",
        query: { page: 1, uid: id },
    });
}
/* 调整时间格式 */
function adjustDate(time) {
    if (time != null) return time.split("T")[0];
    else return "";
}
function adjustTime(time) {
    if (time != null) {
        let t = time.split("T")[1].split(".")[0];
        let h = t.split(":")[0];
        let m = t.split(":")[1];
        let s;
        if (6 < h && h < 12) s = "上午";
        else if (12 < h && h < 18) s = "下午";
        else s = "晚上";
        return s + " " + h + ":" + m;
    } else return "";
}
function isDisPlayActionLabel() {
    let width = document.body.clientWidth;
    if (width <= 700) {
        return false;
    } else {
        return true;
    }
}
</script>
<style scoped lang="less">
.articles {
    width: 100%;
}

.articles-item {
    height: 230px;
    margin-bottom: 20px;
    padding: 0;
    overflow: hidden;
    border-radius: 8px 4px 4px 8px;
    background-color: #ffffff50;
    box-shadow: 0 3px 8px 6px rgba(7, 17, 27, 0.08);
    transition: all ease 0.2s;
}
.articles-item:hover {
    box-shadow: 0 3px 8px 6px rgba(7, 17, 27, 0.14);
    .item-cover img {
        transform: scale(1.1);
    }
}
.article-detail-load {
    font-weight: 700;
    font-size: 24px;
    display: none;
}
.article-item-a {
    display: flex;
    width: 100%;
    height: 230px;
    justify-content: center;
    align-items: center;
    color: black;
    cursor: pointer;
}

.item-cover {
    width: 35%;
    height: 100%;
    overflow: hidden;
}
.item-cover.right {
}
.item-cover img {
    width: 100%;
    height: 100%;
    object-fit: cover;
    transition: all ease 0.5s;
}
.item-info {
    flex: 1;
    margin: 0 10px;
    text-align: left;
    overflow: hidden;
    padding: 10px;
}

.item-title {
    font-size: 24px;
    font-weight: 700;
    margin-bottom: 10px;
    text-overflow: ellipsis;
    white-space: nowrap;
    overflow: hidden;
}
.itemTitleCenter {
    text-align: center;
}
.item-time {
    display: flex;
    align-items: center;
    margin-bottom: 6px;
    padding-bottom: 6px;
    font-size: 13px;
    border-bottom: 1px solid #ddd;
    color: #333;
    text-overflow: ellipsis;
    white-space: nowrap;
    overflow: hidden;
}
.item-info span {
    margin-right: 10px;
}
.item-info .label {
}
.item-info .date {
}
.item-info .time {
    font-weight: 700;
    color: #000;
}
.item-content {
    height: 100px;
}
.content {
    display: -webkit-box;
    -webkit-line-clamp: 4;
    line-clamp: 4;
    -webkit-box-orient: vertical;
    overflow: hidden;
    line-height: 26px;
    text-indent: 2em;
}
</style>
<!-- 文章卡片 -->
<style scoped>
.item-actions {
    display: flex;
    margin: 16px 0 0 0;
    font-size: 14px;
    width: max-content;
    border-top: 1px solid #000000;
    z-index: 999;
}

.item-action-author:hover {
}
.item-action-item {
    margin-right: 14px;
    cursor: pointer;
}
.item-action-label {
    /* margin: 0 !important; */
}
.item-action-label-nodisplay {
    display: none;
}
.item-action-likes {
    margin-right: 34px;
}
.item-action-author:hover {
    color: #ff6632;
}
.item-action-author::before {
    content: "\e6e3";
    font-family: "iconfont";
}

.item-action-views::before {
    content: "\e6df";
    font-family: "iconfont";
    margin-right: 4px;
}
.item-action-likes::before {
    content: "\e6f9";
    font-family: "iconfont";
    margin-right: 4px;
}
.item-action-classify::before {
    content: "\e6da";
    font-family: "iconfont";
}
.item-action-tags::before {
    content: "\e6d3";
    font-family: "iconfont";
}
@media (max-width: 600px) {
    .article-main {
        padding: 0;
    }
    .article-main-label {
        margin-left: 10px;
    }
    .item-action-item {
        margin-right: 8px;
        font-size: 12px;
    }
}
</style>
