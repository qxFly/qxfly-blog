<template>
    <CardView>
        <div class="sider-hot-article-label">热门文章</div>
        <div class="main">
            <div class="items">
                <div class="item" v-for="(article, item) in articles" :key="article" @click="ArticleDetail(article)">
                    <div class="item-col">
                        <div class="span-number">{{ item + 1 }}</div>
                        <div class="span-title">{{ article.title }}</div>
                    </div>
                </div>
                <div class="item" v-if="articles == ''">
                    <div class="item-col">
                        <div class="span-number">1</div>
                        <div class="span-title">暂无文章</div>
                    </div>
                </div>
            </div>
        </div></CardView
    >
</template>
<script setup>
import { ref, onMounted } from "vue";
import { getArticles } from "@/api/Article/index";
import mymd5 from "@/utils/md5.js";
import router from "@/router";
let articles = ref([]);
async function GetArticles() {
    await getArticles(1, 10, "", "hot", 0).then((res) => {
        if ((res.data.code = 1)) {
            articles.value = res.data.data.data;
        }
    });
}
/* 查看文章 */
function ArticleDetail(article) {
    router.push({
        name: "ArticleDetail",
        params: {
            id: article.id,
            editMode: mymd5.generateMD5BySalt(false),
            salt: mymd5.getSalt(),
            cover: article.cover,
        },
    });
}
onMounted(() => {
    GetArticles();
});
</script>
<style scoped lang="less">
.sider-hot-article:hover {
    box-shadow: 0 3px 8px 6px rgba(7, 17, 27, 0.14);
}
.sider-hot-article-label {
    font-size: 18px;
    font-weight: 700;
}
.items {
    display: flex;
    flex-direction: column;
    margin-top: 10px;
}

.item {
    width: 100%;
    margin: 2px 0;
}
.item-col {
    display: flex;
    align-items: center;
    cursor: pointer;
    transition: all 0.3s ease;
    border: 1px solid #90ccff;
    border-radius: 0 4px 4px 0;
    background-color: #fff;
}
.item-col:hover {
    background-color: rgba(185, 222, 255, 0.5);
}
.span-number {
    width: 24px;
    min-width: 24px;
    background-color: #90ccff;
    text-align: center;
    font-weight: 700;
    border-radius: 0 4px 4px 0;
    margin-right: 6px;
    padding: 2px 0;
    color: #fff;
    font-size: 15px;
}
.span-title {
    overflow: hidden;
    text-overflow: ellipsis;
    white-space: nowrap;
    padding: 2px 2px;
    font-size: 15px;
}
</style>
