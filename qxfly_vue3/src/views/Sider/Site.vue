<template>
    <CardView>
        <div class="sider-hot-article-label">推荐站点</div>
        <div class="items">
            <div v-for="site in sites" :key="site" class="item-col">
                <div class="site-item" @click="toSite(site)">
                    {{ site.name }}
                </div>
                <div class="site-status" :class="GetSiteStatus(site)"></div>
            </div>
            <div v-if="sites.length == 0 && !loading" class="item-col">暂无站点</div>
            <div v-if="loading" class="item-col">努力获取站点状态 {{ second }}</div>
        </div>
    </CardView>
    <!-- <div class="sider-hot-article">
       
    </div> -->
</template>
<script setup>
import { onMounted, onUnmounted, ref } from "vue";
import { listSite, getSiteStatus } from "@/api";
var sites = ref([]);
// 获取站点列表
let currPage = ref(1);
let pageSize = ref(10);
let loading = ref(true);
let second = ref("");
async function ListSite() {
    let timer = setInterval(() => {
        if (second.value == ". . . . . . ") {
            second.value = "";
        }
        second.value += ". ";
    }, 1000);
    const res = await listSite({ currPage: currPage.value, pageSize: pageSize.value, name: "" });
    loading.value = false;
    clearInterval(timer);
    sites.value = res.data.data.list;
    /* 获取站点状态 */
    getSiteStatus(sites.value).then((res) => {
        if (res.data.code == 1) sites.value = res.data.data;
    });
}
function GetSiteStatus(site) {
    if (site.status == 200) {
        return "success";
    } else if (site.status == 404) {
        return "error";
    } else {
        return "wait";
    }
}
function toSite(site) {
    window.open(site.url);
}
onMounted(() => {
    ListSite();
});
onUnmounted(() => {});
</script>
<style lang="less" scoped>
.sider-hot-article-label {
    font-size: 18px;
    font-weight: 700;
}
.items {
    display: flex;
    flex-direction: column;
    margin-top: 10px;
}

.item-col {
    display: flex;
    align-items: center;
    justify-content: space-between;
    cursor: pointer;
    transition: all 0.3s ease;
    // border: 1px solid #ffd0c1;
    border-radius: 4px;
    padding: 4px 4px 4px 8px;
}
.site-item {
    width: 100%;
    overflow: hidden;
}
.item-col:hover {
    background-color: var(--hover-color-orange);
}
.site-status.success::before {
    content: "\e6bc";
    font-family: "iconfont";
    margin-right: 4px;
    font-size: 20px;
}
.site-status.wait::before {
    content: "\e713";
    font-family: "iconfont";
    margin-right: 4px;
    font-size: 20px;
}
.site-status.error::before {
    content: "\e6bd";
    font-family: "iconfont";
    margin-right: 4px;
    font-size: 20px;
}
</style>
