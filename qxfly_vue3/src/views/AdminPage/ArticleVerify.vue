<template>
    <div id="load" class="load" v-if="isload">
        <div class="load-content">{{ loadText }}</div>
    </div>
    <div id="Main" class="Main" style="display: none">
        <div class="article-header" id="article-header">
            <div class="article-title" id="article-title">{{ article.title }}</div>
            <div class="article-time">
                <div class="time-item">
                    <div class="label">UpdateTime:</div>
                    <div class="article-update-time">{{ article.updateTime }}</div>
                </div>
                <div class="time-item">|</div>
                <div class="time-item">
                    <div class="label">CreateTime:</div>
                    <div class="article-create-time">{{ article.createTime }}</div>
                </div>
            </div>
        </div>
        <div class="main">
            <div class="sider-left">
                <div class="sider-container" id="sider-left-content">
                    <div class="left-content">
                        <h2>封面</h2>
                        <el-image class="img" :src="article.cover" @click="viewCover(article.cover)" />
                    </div>
                </div>
            </div>
            <div class="article-content">
                <div id="content" class="content"></div>
                <div class="tags">
                    <div class="tag" :id="'tag-' + k" v-for="(item, k) in tags" :key="item">
                        {{ item.name }} <span @click="removeTag(item)" class="rmTag"></span>
                    </div>
                </div>
            </div>
            <div class="sider-right">
                <div class="sider-container" id="sider-right-content">
                    <div>
                        <ArticleAuthorInfoCard
                            class="ArticleAuthorInfoCard-right"
                            :authorId="article.authorId"
                            btnType="detail-message"
                            @load="authorCardLoadFun" />
                    </div>
                    <div class="attachments">
                        <h3>附件</h3>
                        <div>
                            <div
                                class="attachment"
                                v-for="(a, index) in attachments"
                                :key="index"
                                @click="downloadAttachment(a)">
                                <div class="input-attachment-list-item-content">
                                    <div style="display: flex; width: 100%">
                                        <div class="span-number">{{ index + 1 }}</div>
                                        <div class="input-attachment-list-item-name">{{ a.fileOriginName }}</div>
                                    </div>
                                    <div
                                        class="input-attachment-process-number"
                                        :id="'input-attachment-process-number-' + a.fileName"></div>
                                </div>
                                <div class="input-attachment-list-item-process">
                                    <div
                                        class="input-attachment-process"
                                        :id="'input-attachment-process-' + a.fileName"></div>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="sider-edit-Btns">
                        <div class="sider-edit-Btn ok" @click="pass(3)">审核通过</div>
                        <div class="sider-edit-Btn no" @click="pass(2)">审核不通过</div>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <el-dialog
        v-model="viewImgDialogVisible"
        width="40%"
        center
        :append-to-body="true"
        :show-close="false"
        :lock-scroll="false"
        :destroy-on-close="true">
        <el-image :src="cover" fit="contain" />
        <template #footer>
            <span class="dialog-footer">
                <el-button @click="close">关 闭</el-button>
            </span>
        </template>
    </el-dialog>
    <!-- 审核不通过 -->
    <el-dialog
        v-model="noPassDialogVisible"
        width="40%"
        center
        align-center
        :append-to-body="true"
        :show-close="false"
        :lock-scroll="false"
        :destroy-on-close="true">
        <el-input class="search-item" v-model="reason" placeholder="不通过的原因" clearable></el-input>
        <template #footer>
            <span class="dialog-footer">
                <el-button @click="noPassDialogVisible = false">关 闭</el-button>
                <el-button @click="ArticleVerify(2)" type="primary">确 认</el-button>
            </span>
        </template>
    </el-dialog>
    <BackTop></BackTop>
</template>

<script setup>
import TopBar from "@/components/TopBar/TopBar.vue";
import { getArticleById, getArticleAttachment } from "@/api/Article/index";
import { articleVerify } from "@/api/Admin";
import { onMounted, onUnmounted, ref } from "vue";
import { useRoute } from "vue-router";
import router from "@/router";
import BackTop from "@/components/BackTop.vue";
import ArticleAuthorInfoCard from "@/components/ArticleAuthorInfoCard.vue";
import hljs from "highlight.js/lib/core";
import "highlight.js/styles/github.css";
window.hljs = hljs;
require("highlightjs-line-numbers.js");
import { ElMessage, ElMessageBox } from "element-plus";
import request from "@/utils/request";
hljs.registerLanguage("c", require("highlight.js/lib/languages/c"));
hljs.registerLanguage("cpp", require("highlight.js/lib/languages/cpp"));
hljs.registerLanguage("java", require("highlight.js/lib/languages/java"));
hljs.registerLanguage("python", require("highlight.js/lib/languages/python"));
hljs.registerLanguage("csharp", require("highlight.js/lib/languages/csharp"));
hljs.registerLanguage("go", require("highlight.js/lib/languages/go"));
hljs.registerLanguage("sql", require("highlight.js/lib/languages/sql"));
hljs.registerLanguage("javascript", require("highlight.js/lib/languages/javascript"));
hljs.registerLanguage("typescript", require("highlight.js/lib/languages/typescript"));
hljs.registerLanguage("markdown", require("highlight.js/lib/languages/markdown"));
hljs.registerLanguage("css", require("highlight.js/lib/languages/css"));
hljs.registerLanguage("xml", require("highlight.js/lib/languages/xml"));
const route = useRoute();
let currPage = ref(1);
let article = ref({
    title: "",
    content: "",
});
let isload = ref(true);
let loadText = ref("加载中");
let tags = ref([]);
/* 获取文章详情 */
async function getArticleDetail() {
    await getArticleById(route.query.id).then((res) => {
        article.value = res.data.data;
        if (article.value.updateTime != null) {
            article.value.updateTime =
                article.value.updateTime.split("T")[1].split(".")[0] + " — " + article.value.updateTime.split("T")[0];
        }
        if (article.value.createTime != null) {
            article.value.createTime =
                article.value.createTime.split("T")[1].split(".")[0] + " — " + article.value.createTime.split("T")[0];
        }
        if (article.value.tag != null && article.value.tag != "") {
            let temp = article.value.tag.split(",");
            for (let i = 0; i < temp.length; i++) {
                tags.value.push({ name: temp[i] });
            }
        }

        let content = document.getElementById("content");
        content.innerHTML = article.value.content;
        hljs.highlightAll();
        hljs.initLineNumbersOnLoad();
        showConten();
    });
}
/* 审核文章 */
let noPassDialogVisible = ref(false);
let reason = ref("");
async function pass(verify) {
    if (verify == 3) {
        await ElMessageBox.confirm("您确定该文章审核通过？", {
            confirmButtonText: "通过",
            cancelButtonText: "取消",
            type: "warning",
            lockScroll: false,
            center: true,
        })
            .then(() => {
                ArticleVerify(verify);
            })
            .catch(() => {});
    } else {
        await ElMessageBox.confirm("您确定该文章审核不通过？", {
            confirmButtonText: "不通过",
            cancelButtonText: "取消",
            type: "warning",
            lockScroll: false,
            center: true,
        })
            .then(() => {
                noPassDialogVisible.value = true;
            })
            .catch(() => {});
    }
}
function ArticleVerify(verify) {
    let temptag = [];
    for (let i = 0; i < tags.value.length; i++) {
        temptag.push(tags.value[i].name);
    }
    articleVerify({
        aid: parseInt(route.query.id),
        uid: article.value.authorId,
        verify: verify,
        reason: reason.value,
        tags: temptag.toString(),
        classify: article.value.classify,
    }).then((res) => {
        if (res.data.code == 1) {
            ElMessage.success({ message: "审核完成！", offset: 300 });
            router.back();
        } else {
            ElMessage.error({ message: "审核失败！", offset: 300 });
        }
    });
}
/* 浏览封面 */
let viewImgDialogVisible = ref(false);
let cover = ref("");
function viewCover(c) {
    cover.value = c;
    viewImgDialogVisible.value = true;
}
function close() {
    cover.value = "";
    viewImgDialogVisible.value = false;
}
/* 判断信息是否加载完 */
let authorCardLoad = ref(false);
function authorCardLoadFun(flag) {
    authorCardLoad.value = flag;
    showConten();
}
/* 加载完显示内容 */
function showConten() {
    if (authorCardLoad.value) {
        let Main = document.getElementById("Main");
        let title = document.getElementById("article-title");
        let load = document.getElementById("load");
        let header = document.getElementById("article-header");
        Main.style.display = "block";
        loadText.value = article.value.title;
        load.style.opacity = 0;
        load.style.height = "200px";
        setTimeout(() => {
            title.style.opacity = 1;
            title.style.top = "0";
            header.style.opacity = 1;
            header.style.height = "200px";
        }, 100);
        setTimeout(() => {
            isload.value = false;
        }, 800);
    }
}
/* 删除标签 */
function removeTag(tag) {
    tags.value = tags.value.filter((t) => t !== tag);
}
/* 获取附件 */
let attachments = ref([]);
function getAttachments() {
    getArticleAttachment(parseInt(route.query.id)).then((res) => {
        attachments.value = res.data.data;
    });
}

/* 下载附件 */
function downloadAttachment(file) {
    request({
        url: file.downloadUrl, // 请求地址
        method: "get",
        responseType: "blob", // 表明返回服务器返回的数据类型
        onDownloadProgress: (e) => {
            onProcess(e, file);
        },
    }).then((res) => {
        const link = document.createElement("a");
        link.href = URL.createObjectURL(res.data);
        link.download = file.fileOriginName;
        link.target = "_blank";
        document.body.appendChild(link);
        link.click();
        link.remove();
    });
}
/* 下载进度条 */
function onProcess(e, file) {
    const processEle = document.getElementById("input-attachment-process-" + file.fileName);
    const processnumber = document.getElementById("input-attachment-process-number-" + file.fileName);
    processEle.style.display = "block";
    processEle.style.width = e.progress * 100 + "%";
    processnumber.style.display = "block";
    processnumber.innerText = Math.floor(e.progress * 100) + "%";
    if (e.progress == 1) {
        processEle.style.display = "none";
        processEle.style.width = 0 + "%";
        processnumber.style.display = "none";
    }
}
function Listener() {
    const siderRight = document.getElementById("sider-right-content");
    const siderLeft = document.getElementById("sider-left-content");
    var scrollTop = document.documentElement.scrollTop || document.body.scrollTop;
    if (scrollTop > 600) {
        siderRight.style.top = "20px";
        siderLeft.style.top = "20px";
    } else {
        siderRight.style.top = "100px";
        siderLeft.style.top = "100px";
    }
}

onMounted(() => {
    window.addEventListener("scroll", Listener);
    document.body.scrollTop = document.documentElement.scrollTop = 0;
    currPage.value = route.query.page;
    getArticleDetail();
    getAttachments();
});
onUnmounted(() => {
    window.removeEventListener("scroll", Listener, false);
});
</script>
<style scoped lang="less">
.load {
    position: fixed;
    width: 100%;
    height: 100%;
    font-size: 24px;
    font-weight: 700;
    opacity: 1;
    transition: all 0.6s ease;
    display: flex;
    justify-content: center;
    align-items: center;
    background-color: #c0e1ff;
    z-index: 999;
}
</style>
<style scoped lang="less">
.article-header {
    width: 100%;
    height: 280px;
    text-align: center;
    padding: 80px 0 0 0;
    background-color: #c0e1ff;
    box-shadow: 0 3px 8px 6px rgba(7, 17, 27, 0.2);
    margin-bottom: 50px;
    opacity: 0;
    transition: all 0.4s ease;
}
.article-title {
    position: relative;
    top: 20px;
    font-size: 26px;
    font-weight: 700;
    margin-bottom: 20px;
    transition: all 0.5s ease;
    opacity: 0;
    display: -webkit-box;
    -webkit-line-clamp: 2;
    -webkit-box-orient: vertical;
    overflow: hidden;
}
.article-time {
    display: flex;
    justify-content: center;
    align-items: center;
    width: 400px;
    margin: 0 auto;
}
.time-item {
    display: flex;
    align-items: center;
    justify-content: center;
    margin: 0 4px;
}
.label {
    font-weight: 700;
    margin-right: 10px;
}

.main {
    display: flex;
    justify-content: center;
}
.article-content {
    min-width: 1000px;
    width: 60%;
    border-radius: 4px;
    padding: 40px 50px;
    margin: 0 20px 40px 20px;
    background-color: rgb(255, 255, 255);
    box-shadow: 0 3px 8px 6px rgba(7, 17, 27, 0.08);
    transition: all 0.5s ease;
    overflow: hidden;
}
.article-content:hover {
    box-shadow: 0 3px 8px 6px rgba(7, 17, 27, 0.12);
}
.tags {
    display: flex;
    margin: 10px;
    flex-wrap: wrap;
}
.tag {
    border: 1px solid #49b1f5;
    width: max-content;
    border-radius: 6px;
    padding: 6px 12px;
    margin: 4px 8px 0 0;
    color: #49b1f5;
    font-weight: 700;
    transition: all 0.2s ease;
}
.tag:hover {
    color: #fff;
    background-color: #49b1f5;
}
.rmTag::before {
    content: "\e601";
    font-family: "iconfont";
    font-size: 18px;
    font-weight: 700;
    cursor: pointer;
}
.rmTag:hover {
    color: #fff;
}
.article-update-time {
    width: max-content;
}
.article-create-time {
    width: max-content;
}
.sider-right {
    width: 260px;
}
.sider-container {
    position: sticky;
    top: 100px;
    transition: all 0.5s ease;
}
.left-content {
    padding: 10px;
    border-radius: 4px;
    background-color: rgb(255, 255, 255);
    box-shadow: 0 3px 8px 6px rgba(7, 17, 27, 0.08);
    transition: all 0.5s ease;
}
.img {
    width: 260px;
    object-fit: cover;
}
.ArticleAuthorInfoCard-right {
    margin-bottom: 20px;
}
.sider-edit-Btns {
}
.sider-edit-Btn.ok {
    margin: 10px 0;
    font-size: 18px;
    text-align: center;
    border-radius: 4px;
    padding: 6px;
    color: #fff;
    background-color: #67c23a;
}
.sider-edit-Btn.no {
    margin: 10px 0;
    font-size: 18px;
    text-align: center;
    border-radius: 4px;
    padding: 6px;
    color: #fff;
    background-color: #f56c6c;
}
.sider-edit-Btn:hover {
    opacity: 0.8;
}

.input-attachment-list-item-name {
    flex: 1;
    overflow: scroll;
    margin-right: 6px;
}
.input-attachment-list-item-name::-webkit-scrollbar {
    display: none;
}
.attachments {
    border-radius: 4px;
    padding: 10px;
    margin-bottom: 20px;
    background-color: rgb(255, 255, 255);
    box-shadow: 0 3px 8px 6px rgba(7, 17, 27, 0.08);
    transition: all 0.3s ease;
    display: flex;
    flex-direction: column;
}
.attachments:hover {
    box-shadow: 0 3px 8px 6px rgba(7, 17, 27, 0.14);
}
.attachment {
    // display: flex;
    border-radius: 2px;
    border: 1px solid #84c6ff;
    margin: 2px 0;
    transition: all 0.3s ease;
    cursor: pointer;
}
.attachment:hover {
    background-color: #dceeff;
}
.input-attachment-list-item-content {
    display: flex;
    transition: all 0.3s ease;
    justify-content: space-between;
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
.input-attachment-process {
    display: none;
    height: 3px;
    width: 0%;
    background-color: #ff8e68;
    transition: all 0.3s ease;
    // margin-top: 4px;
}
.input-attachment-process-number {
    display: none;
    width: max-content;
    min-width: 24px;
    background-color: #ff8e68;
    text-align: center;
    font-weight: 700;
    border-radius: 4px 0 0 4px;
    padding: 2px 0;
    color: #fff;
    font-size: 15px;
}
</style>
<style>
h1 {
    margin: 12px 0;
}
h2 {
    margin: 10px 0;
}
h3 {
    margin: 8px 0;
}

p {
    margin: 6px 0;
}

span code {
    font-family: "Source Code Pro", "DejaVu Sans Mono", "Ubuntu Mono", "Anonymous Pro", "Droid Sans Mono", Menlo, Monaco,
        Consolas, Inconsolata, Courier, monospace, "PingFang SC", "Microsoft YaHei", sans-serif;
}
code {
    margin: 20px;
    border-radius: 4px;
    /* font-size: 18px; */
    /* font-weight: 700; */
    line-height: 24px;
    background-color: #fafafa !important;
    border: 2px solid #d6d6d6;
    font-family: "Source Code Pro", "DejaVu Sans Mono", "Ubuntu Mono", "Anonymous Pro", "Droid Sans Mono", Menlo, Monaco,
        Consolas, Inconsolata, Courier, monospace, "PingFang SC", "Microsoft YaHei", sans-serif;
}
.hljs-ln-n {
    text-align: right;
    padding-right: 6px;
    border-right: 1px solid #333;
    margin-right: 10px;
}
@media (max-width: 700px) {
    span code {
        font-size: 16px;
    }
    code {
        font-size: 16px;
        margin: 20px 0 !important;
    }
}
</style>
<!-- 表格样式 -->
<style>
.table-box {
    overflow-x: auto;
    /* display: flex; */
    /* justify-content: center; */
}
.content table:not(.hljs-ln) {
    border-collapse: separate;
    border-spacing: 0;
    border: 1px solid #000;
    border-radius: 2px;
    table-layout: fixed;
    margin: 4px auto;
}
/* 第一行标题总设置 */
.content table:not(.hljs-ln) th {
    padding: 5px;
    background-color: rgb(220, 238, 255);
    border-bottom: 1px solid #0e0f1a;
    border-right: 1px dashed #0e0f1a;
}
/* 单元格总设置 */
.content table:not(.hljs-ln) td {
    text-align: center;
    padding: 10px;
    background-color: aliceblue;
    border-bottom: 1px solid #0e0f1a;
    border-right: 1px dashed #0e0f1a;
}
/* 第一行标题第一个元素 */
.content table:not(.hljs-ln) th:first-child {
    border-radius: 2px 0 0 0;
}
/* 第一行标题最后一个元素 */
.content table:not(.hljs-ln) th:last-child {
    border-right: none;
    border-radius: 0 2px 0 0;
}
/* 最后一排元素 */
.content table:not(.hljs-ln) td:last-child {
    border-right: 0px;
}
/* 最后一行元素 */
.content table:not(.hljs-ln) tr:last-child td {
    border-bottom: 0px;
}
/* 最后一行最第一个元素 */
.content table:not(.hljs-ln) tr:last-child td:first-child {
    border-bottom: 0px;
    border-radius: 0 0 0 2px;
}
/* 最后一行最后一个元素 */
.content table:not(.hljs-ln) tr:last-child td:last-child {
    border-bottom: 0px;
    border-radius: 0 0 2px 0;
}
.content table:not(.hljs-ln) tr {
    overflow: hidden;
    text-overflow: ellipsis;
    white-space: nowrap;
}
</style>
<!-- 响应布局 -->
<style scoped>
@media (max-width: 700px) {
    .article-time {
        flex-direction: column;
    }
    .Main {
        width: 100%;
    }
    .main {
        flex-direction: column;
    }
    .article-content {
        min-width: unset;
        width: 100%;
        margin: 0 0 20px 0;
        padding: 20px;
    }
    .sider-right {
        width: 100%;
    }
    .ArticleAuthorInfoCard-right {
        width: 100%;
    }
}
</style>
