<template>
    <CardView>
        <div class="sider-hot-article-label">推荐作者</div>
        <div class="main">
            <div class="items">
                <div class="item" v-for="(author, item) in authors" :key="item" @click="toUserSpace(author.id)">
                    <div class="item-col">
                        <div class="spsen-avatar">
                            <img v-if="author.avatar != null" :src="author.avatar" alt="" />
                            <img
                                v-if="author.avatar == null || author.avatar == ''"
                                src="../../assets/avatar.png"
                                alt="" />
                        </div>
                        <div class="span-title">{{ author.username }}</div>
                    </div>
                </div>
                <div class="item" v-if="authors == ''">
                    <div class="item-col">
                        <div class="span-title">暂无推荐</div>
                    </div>
                </div>
            </div>
        </div>
    </CardView>
</template>
<script setup>
import { ref, onMounted } from "vue";
import { getSuggestAuthor } from "@/api/User/index";
import router from "@/router";
import CardView from "@/components/CardView.vue";
let authors = ref([]);
async function GetSuggestAuthor() {
    await getSuggestAuthor(1, 5).then((res) => {
        if ((res.data.code = 1)) {
            authors.value = res.data.data.list;
        }
    });
}
/* 查看文章 */
function toUserSpace(id) {
    router.push({
        path: "/user/space/userArticle",
        query: { page: 1, uid: id },
    });
}
onMounted(() => {
    GetSuggestAuthor();
});
</script>
<style scoped lang="less">
.sider-hot-article-label {
    font-size: 18px;
    font-weight: 700;
}
.items {
    display: flex;
    flex-direction: column;
    gap: 6px;
    margin-top: 10px;
}

.item {
    width: 100%;
}
.item-col {
    display: flex;
    align-items: center;
    cursor: pointer;
    transition: all 0.3s ease;
    // border: 1px solid #ffd0c1;
    border-radius: 4px;
    padding-left: 8px;
}
.item-col:hover {
    background-color: #ffded4;
}
.spsen-avatar {
    width: 36px;
    height: 36px;
    margin-right: 6px;
    border-radius: 50%;
    overflow: hidden;
}
.spsen-avatar img {
    width: 100%;
}
.span-title {
    overflow: hidden;
    text-overflow: ellipsis;
    white-space: nowrap;
    padding: 2px 2px;
    font-size: 16px;
}
</style>
