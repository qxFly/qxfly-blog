<template>
    <TopBar></TopBar>
    <div id="load" class="load" v-if="isload">
        <div class="load-content">{{ loadText }}</div>
    </div>
    <div id="Main" class="Main" style="display: none">
        <div
            class="article-header"
            id="article-header"
            @click="extendHeader(0, 0)"
            @mouseover="extendHeader(1, 0)"
            @mouseleave="extendHeader(2, 0)">
            <div class="article-header-content">
                <div
                    class="article-header-content-1"
                    @click.stop="extendHeader(0, 1)"
                    @mouseover.stop="extendHeader(1, 1)"
                    @mouseleave.stop="extendHeader(2, 1)">
                    <div class="article-title" id="article-title">{{ article.title }}</div>
                    <div class="article-time" id="article-time">
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
            </div>
            <div class="article-header-cover" id="article-header-cover"></div>
        </div>
        <div class="main">
            <div class="article-content">
                <div id="content" class="content"></div>
                <div class="article-actions">
                    <button class="article-actions-item likes" :class="{ clicked: isLike }" @click="ArticleLike">
                        {{ isLike ? "已点赞" : "点赞" }}
                    </button>
                    <button
                        class="article-actions-item collection"
                        :class="{ clicked: isCollection }"
                        @click="ArticleCollection">
                        {{ isCollection ? "已收藏" : "收藏" }}
                    </button>
                </div>
                <div style="margin: 10px; cursor: pointer" @click="searchClassifyTag(article.classify, 0)">
                    分类： {{ article.classify == null ? "未分类" : article.classify }}
                </div>
                <div class="tags">
                    <div
                        class="tag"
                        :id="'tag-' + k"
                        v-for="(item, k) in tags"
                        :key="item"
                        @click="searchClassifyTag(item, 1)">
                        {{ item.name }}
                    </div>
                </div>
                <div class="line" id="comments"></div>
                <ArticleComment></ArticleComment>
            </div>

            <div class="sider-right">
                <div class="sider-right-content" id="sider-right-content">
                    <div>
                        <ArticleAuthorInfoCard
                            class="ArticleAuthorInfoCard-right"
                            :authorId="article.authorId"
                            btnType="detail-message"
                            @load="authorCardLoadFun" />
                    </div>
                    <div class="attachments" v-if="showAttachmens(0)">
                        <h3>附件</h3>
                        <div v-if="showAttachmens(1)">
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
                        <div v-if="!showAttachmens(1)"><a href="/login" style="color: red">登录</a> 后查看附件</div>
                    </div>
                    <div
                        class="sider-edit-Btns"
                        v-if="route.params.editMode == mydm5.generateMD5('true' + route.params.salt)">
                        <div class="sider-edit-Btn" @click="EditArticle">编&emsp;&emsp;&emsp;辑</div>
                        <div class="sider-edit-Btn" @click="DeleteArticle">删&emsp;&emsp;&emsp;除</div>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <BackTop></BackTop>
    <canvas id="article-header-cover-canvas" style="display: none"></canvas>
</template>

<script setup>
import TopBar from "@/components/TopBar/TopBar.vue";
import {
    getArticleById,
    deleteArticle,
    addArticleView,
    articleLike,
    articleCollection,
    getArticleAttachment,
} from "@/api/Article/index";
import { onMounted, onUnmounted, ref } from "vue";
import { useRoute } from "vue-router";
import BackTop from "@/components/BackTop.vue";
import ArticleAuthorInfoCard from "@/views/Article/ArticleAuthorInfoCard.vue";
import ArticleComment from "@/views/Article/ArticleComment.vue";
import hljs from "highlight.js/lib/core";
import "highlight.js/styles/github.css";
window.hljs = hljs;
require("highlightjs-line-numbers.js");
import router from "@/router";
import mydm5 from "@/utils/md5.js";
import { ElMessage, ElMessageBox } from "element-plus";
import md5 from "js-md5";
import { preview } from "v-preview-image";
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
hljs.registerLanguage("css", require("highlight.js/lib/languages/css"));
hljs.registerLanguage("xml", require("highlight.js/lib/languages/xml"));
hljs.registerLanguage("markdown", require("highlight.js/lib/languages/markdown"));
const route = useRoute();
let timers = [];
const props = defineProps({
    articleId: {
        type: Number,
        default: 4,
    },
});
let article = ref({
    title: "",
    content: "",
});
let tags = ref([]);
let isLike = ref(false);
let isCollection = ref(false);
/* 获取文章详情 */
async function getArticleDetail() {
    loadBlur();
    await getArticleById(route.params.id).then((res) => {
        loadBlur();
        article.value = res.data.data;
        if (article.value.updateTime != null) {
            article.value.updateTime =
                article.value.updateTime.split("T")[1].split(".")[0] + "-" + article.value.updateTime.split("T")[0];
        }
        if (article.value.createTime != null) {
            article.value.createTime =
                article.value.createTime.split("T")[1].split(".")[0] + "-" + article.value.createTime.split("T")[0];
        }
        if (article.value.tag != null && article.value.tag != "") {
            let temp = article.value.tag.split(",");
            for (let i = 0; i < temp.length; i++) {
                tags.value.push({ name: temp[i] });
            }
        }
        isLike.value = article.value.isLike;
        isCollection.value = article.value.isCollection;
        let content = document.getElementById("content");
        if (content != null) {
            content.innerHTML = article.value.content;
        }

        hljs.highlightAll();
        hljs.initLineNumbersOnLoad();
        setTable();
        showConten();
        setHeaderBg();
    });
    addImagePreview();
    getAttachments();
}
/* 获取附件 */
let token = localStorage.getItem(md5("token"));
let isLogin = localStorage.getItem(md5("islogin"));
let attachments = ref([]);
function getAttachments() {
    getArticleAttachment(article.value.id).then((res) => {
        attachments.value = res.data.data;
    });
}
/* 显示附件 */
function showAttachmens(flag) {
    if (flag == 0) {
        if (attachments.value.length > 0) {
            return true;
        }
        return false;
    } else {
        if (token != null && isLogin != null) {
            return true;
        }
        return false;
    }
}
/* 下载附件 */
function downloadAttachment(file) {
    if ((token = null || isLogin == null)) {
        alert("请登入后再尝试下载");
        router.push("/login");
        return;
    }
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
/* 前往编辑文章 */
function EditArticle() {
    router.push({
        name: "editArticle",
        params: {
            salt: "(>ω･* )ﾉ",
            articleId: article.value.id,
            editMode: true,
        },
    });
}
/* 删除文章 */
function DeleteArticle() {
    ElMessageBox.confirm("此操作将永久删除该文章, 是否继续?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
        lockScroll: false,
    })
        .then(() => {
            article.value.createTime = null;
            article.value.updateTime = null;
            deleteArticle(article.value).then((res) => {
                if (res.data.code == 1) {
                    ElMessage({
                        message: "删除成功",
                        type: "success",
                        offset: 120,
                    });
                    router.replace(router.options.history.state.back);
                }
            });
        })
        .catch(() => {});
}
/* 文章浏览量计时器 */
let timer = null;
function addArticleViews() {
    addArticleView({ articleId: route.params.id });
    clearInterval(timer);
}
/* 文章点赞 */
let likeflag = 0;
let sleeplike = 0;
let sleepLikeTimer;
function ArticleLike() {
    /* 防止频繁点击 */
    if (sleeplike < 6) {
        sleeplike++;
    } else {
        ElMessage({
            message: "操作过于频繁",
            type: "error",
            offset: 120,
        });
        clearTimeout(sleepLikeTimer);
        sleepLikeTimer = setTimeout(() => {
            sleeplike = 0;
        }, 3000);
        return;
    }
    if (isLike.value) {
        likeflag = 1;
        isLike.value = false;
    } else {
        likeflag = 0;
        isLike.value = true;
    }
    articleLike({ articleId: route.params.id, views: likeflag }).then((res) => {
        if (res.data.code != 1) {
            ElMessage({
                message: res.data.msg,
                type: "error",
                offset: 120,
            });
        }
    });
}
/* 文章收藏 */
let collectionflag = 0;
let sleepcollection = 0;
let sleepCollectionTimer;
function ArticleCollection() {
    if (sleepcollection == -1) {
        ElMessage({
            message: "操作过于频繁",
            type: "error",
            offset: 120,
        });
        clearTimeout(sleepCollectionTimer);
        sleepCollectionTimer = setTimeout(() => {
            sleepcollection = 0;
        }, 3000);
        return;
    } else if (sleepcollection >= 4) {
        sleepcollection = -1;
    } else {
        sleepcollection++;
    }
    // isCollection.value == "true"
    //     ? ((collectionflag = 1), (isCollection.value = "false"))
    //     : ((collectionflag = 0), (isCollection.value = "true"));
    if (isCollection.value) {
        collectionflag = 1;
        isCollection.value = false;
    } else {
        collectionflag = 0;
        isCollection.value = true;
    }
    articleCollection({ articleId: route.params.id, views: collectionflag }).then((res) => {
        if (res.data.code != 1) {
            ElMessage({
                message: res.data.msg,
                type: "error",
                offset: 120,
            });
        }
    });
}
/* 调整表格 */
function setTable() {
    let tables = document.querySelectorAll("table");
    //	如果table存在于当前界面就执行接下的操作
    if (tables) {
        tables.forEach(function (perTable) {
            if (perTable.className == "") {
                perTable.style.width = "unset";
                // perTable.style.margin = "4px";
                //	创建一个div
                let tableFather = document.createElement("div");
                //	给div添加class名，className兼容性较好
                tableFather.className = "table-box";
                //	将新建的div替代原先的table
                perTable.parentNode.replaceChild(tableFather, perTable);
                //	给新div添加子节点table
                tableFather.appendChild(perTable);
            }
        });
    }
}
/* 侧栏监听 */
function Listener() {
    const topbar = document.getElementById("sider-right-content");
    var scrollTop = document.documentElement.scrollTop || document.body.scrollTop;
    if (scrollTop > 600) {
        topbar.style.top = "20px";
    } else {
        topbar.style.top = "90px";
    }
}
/* 判断信息是否加载完 */
let isload = ref(true);
let loadText = ref("");
let authorCardLoad = ref(false);
function authorCardLoadFun(flag) {
    loadBlur();
    authorCardLoad.value = flag;
    showConten();
}
/* 加载完显示内容 */
function showConten() {
    loadBlur();

    if (authorCardLoad.value) {
        loadBlur();

        let Main = document.getElementById("Main");
        let title = document.getElementById("article-title");
        let load = document.getElementById("load");
        let header = document.getElementById("article-header");
        Main.style.display = "block";
        load.style.opacity = 0;
        load.style.height = "200px";
        timers.push(
            setTimeout(() => {
                title.style.opacity = 1;
                title.style.top = "0";
                header.style.opacity = 1;
                header.style.height = "370px";
                // header.style.marginTop = "50px";
            }, 100)
        );

        timers.push(
            setTimeout(() => {
                isload.value = false;
                setThemeColor();
            }, 650)
        );
    }
}
/* 设置头部背景 */
function setHeaderBg() {
    const header = document.getElementById("article-header-cover");
    if (header != null) header.style.backgroundImage = "url(" + article.value.cover + ")";
}
/* 根据加载进度，调整模糊 */
let loadValue = 0;
function loadBlur(a) {
    loadValue += 2;
    const load = document.getElementById("load");
    if (load != null) {
        load.style.filter = "blur(" + loadValue + "px)";
        if (a == 0) load.style.filter = "blur(0px)";
    }
}
/* 触摸标题区域，展开标题区域 */
let extendHeaderFlag = false;
let extendHeaderFlag1 = false;
let extendHeaderSleep = true;
let extendHeaderTimer = [];
/* 0点击、1移进、2移出 */
function extendHeader(flag, flag2) {
    if (extendHeaderSleep) {
        extendHeaderTimer.push(
            setTimeout(() => {
                extendHeaderSleep = false;
            }, 100)
        );
        return;
    }
    const header = document.getElementById("article-header");
    const cover = document.getElementById("article-header-cover");
    const topbar = document.getElementById("top-bar-1");
    const articleTitle = document.getElementById("article-title");
    const articleTime = document.getElementById("article-time");
    let bodyHeight = document.body.clientHeight;
    if (extendHeaderFlag1 && flag2 == 1) {
        changeHeader(0);
        extendHeaderFlag1 = true;
        return;
    }
    if (extendHeaderFlag1 && flag == 1) {
        changeHeader(0);
        extendHeaderFlag1 = true;
        return;
    }
    if (!extendHeaderFlag1 && flag2 == 1) {
        changeHeader(1);
        extendHeaderFlag1 = false;
        return;
    }
    if (flag == 0) {
        if (extendHeaderFlag && !extendHeaderFlag1) {
            changeHeader(0);
            extendHeaderFlag1 = true;
        } else {
            changeHeader(1);
            extendHeaderFlag1 = false;
        }
    }
    if (flag == 1) {
        header.style.height = bodyHeight / 2 + "px";
        cover.style.top = "-" + bodyHeight / 2 + "px";
        cover.style.filter = "blur(2px)";
        extendHeaderFlag = true;
    } else if (flag == 2) {
        header.style.height = "370px";
        // header.style.marginTop = "50px";
        cover.style.top = "-370px";

        topbar.style.top = "0px";
        articleTitle.style.fontSize = "2em";
        articleTime.style.fontSize = "1em";
        extendHeaderFlag = false;
        extendHeaderFlag1 = false;
        cover.style.filter = "blur(5px) brightness(" + brightnessValue + ")";
        for (let i = 0; i < extendHeaderTimer.length; i++) {
            clearTimeout(extendHeaderTimer[i]);
        }

        extendHeaderTimer.push(
            setTimeout(() => {
                cover.style.transform = "scale(1.05)";
            }, 400)
        );
    }
}
function changeHeader(flag) {
    const header = document.getElementById("article-header");
    const cover = document.getElementById("article-header-cover");
    const topbar = document.getElementById("top-bar-1");
    const articleTitle = document.getElementById("article-title");
    const articleTime = document.getElementById("article-time");
    let bodyHeight = document.body.clientHeight;
    if (flag == 0) {
        header.style.height = bodyHeight + "px";
        header.style.marginTop = "0";
        cover.style.top = "-" + bodyHeight + "px";

        topbar.style.top = "-70px";
        articleTitle.style.fontSize = "2.5em";
        articleTime.style.fontSize = "1.3em";
        extendHeaderTimer.push(
            setTimeout(() => {
                cover.style.filter = "blur(0px)";
                cover.style.transform = "scale(1)";
            }, 400)
        );
    } else {
        header.style.height = bodyHeight / 2 + "px";
        // header.style.marginTop = "50px";
        cover.style.top = "-" + bodyHeight / 2 + "px";
        topbar.style.top = "0px";
        articleTitle.style.fontSize = "2em";
        articleTime.style.fontSize = "1em";
        cover.style.filter = "blur(5px) brightness(" + brightnessValue + ")";
        extendHeaderTimer.push(
            setTimeout(() => {
                cover.style.transform = "scale(1.05)";
            }, 400)
        );
    }
}
/* 点击标签、分类前往搜索 */
function searchClassifyTag(item, type) {
    if (type == 0) {
        router.push({
            path: "/index/articleClassify",
            query: {
                page: 1,
                classify: item,
                tag: "",
            },
        });
    } else if (type == 1) {
        router.push({
            path: "/index/articleClassify",
            query: {
                page: 1,
                classify: "",
                tag: item.name,
            },
        });
    }
}
/* 添加图片预览 */
function addImagePreview() {
    let content = document.getElementById("content");
    if (content != null) {
        let imgEles = content.getElementsByTagName("img");
        let imgList = ref([]);
        for (let i = 0; i < imgEles.length; i++) {
            imgList.value.push(imgEles[i].src);
            imgEles[i].addEventListener("click", () => {
                preview(i, imgList.value);
            });
        }
    }
}
/* 根据头部背景的深浅，调整字体颜色 */
let brightnessValue = 1;
function setThemeColor() {
    let img = new Image();
    let canvas = document.getElementById("article-header-cover-canvas");
    let ctx = canvas.getContext("2d");
    img.onload = function () {
        ctx.drawImage(img, 0, 0);
        img.style.display = "none";
        let imgData = ctx.getImageData(0, 0, img.width, img.height).data;
        // 存储颜色及其像素数的对象数组
        var colorCounts = {};
        // 遍历每个像素，统计颜色出现次数
        for (var i = 0; i < imgData.length; i += 4) {
            var rgba = +imgData[i] + "," + imgData[i + 1] + "," + imgData[i + 2];
            // console.log(rgba);
            if (rgba in colorCounts) {
                colorCounts[rgba]++;
            } else {
                colorCounts[rgba] = 1;
            }
        }
        // 根据像素数对颜色进行排序
        var sortedColors = Object.keys(colorCounts).sort(function (a, b) {
            return colorCounts[b] - colorCounts[a];
        });
        let mainColor = sortedColors.slice(1, 4);
        let rgb = mainColor[0].split(",");
        let r = rgb[0] / 255;
        let g = rgb[1] / 255;
        let b = rgb[2] / 255;
        let rgb2 = mainColor[1].split(",");
        let r2 = rgb2[0] / 255;
        let g2 = rgb2[1] / 255;
        let b2 = rgb2[2] / 255;

        let rgbs = [];
        let lights = [];
        for (let i = 0; i < mainColor.length; i++) {
            let rgb = mainColor[i].split(",");
            rgb[0] = rgb[0] / 255;
            rgb[1] = rgb[1] / 255;
            rgb[2] = rgb[2] / 255;
            rgbs.push(rgb);
            lights.push(rgb[0] * 0.3 + rgb[1] * 0.59 + rgb[2] * 0.11);
        }
        brightnessValue = (lights[0] + lights[1] + lights[2]) / 3;
        if (brightnessValue > 0.5) {
            brightnessValue = 1 - brightnessValue / 2;
        } else {
            brightnessValue = 0.5 + brightnessValue;
        }
        const cover = document.getElementById("article-header-cover");
        cover.style.filter = "blur(5px) brightness(" + brightnessValue + ")";
    };
    img.src = article.value.cover;
    img.crossOrigin = "Anonymous";
}
/* 清除定时器 */
function ClearTimeout() {
    for (let i = 0; i < timers.length; i++) {
        clearTimeout(timers[i]);
    }
}
onMounted(() => {
    timer = setInterval(() => {
        addArticleViews();
    }, 10000);
    const load = document.getElementById("load");
    load.style.backgroundImage = "url(" + route.params.cover + ")";
    window.addEventListener("scroll", Listener);
    document.body.scrollTop = document.documentElement.scrollTop = 0;
    getArticleDetail();
});
onUnmounted(() => {
    clearInterval(timer);
    clearInterval(sleepLikeTimer);
    ClearTimeout();
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
    background-repeat: no-repeat;
    background-size: cover;
    filter: blur(0px);
}
</style>
<style scoped lang="less">
.article-header {
    display: block;
    width: 100%;
    height: 420px;
    text-align: center;
    padding: 0;
    margin-top: 0;
    box-shadow: 0 3px 8px 6px rgba(7, 17, 27, 0.2);
    margin-bottom: 50px;
    opacity: 0;
    transition: all 0.5s ease;
    overflow: hidden;
}
.article-header-cover {
    transition: all 0.5s ease;
    position: relative;
    top: -370px;
    width: 100%;
    height: 100%;
    z-index: -1;
    background-repeat: no-repeat;
    background-size: cover;
    background-position: center center;
    filter: blur(5px);
    transform: scale(1.05);
}

.article-header-cover::before {
    content: "";
    position: absolute;
    top: 0;
    left: 0;
    right: 0;
    bottom: 0;
}
.article-header-content {
    width: 100%;
    height: 100%;
    display: flex;
    justify-content: center;
    flex-direction: column;
    align-items: center;
    gap: 20px;
}
.article-header-content-1 {
    display: flex;
    justify-content: center;
    flex-direction: column;
    align-items: center;
    gap: 20px;
}
.article-title {
    font-size: 2em;
    transition: all 0.5s ease;
    opacity: 0;
    display: -webkit-box;
    -webkit-line-clamp: 2;
    -webkit-box-orient: vertical;
    overflow: hidden;
    color: #fff;
}
.article-time {
    display: flex;
    justify-content: center;
    align-items: center;
    width: 400px;
    transition: all 0.6s ease;
    color: #fff;
}
.time-item {
    display: flex;
    align-items: center;
    justify-content: center;
    margin: 0 4px;
}
.label {
    margin-right: 10px;
}
.main {
    display: flex;
    justify-content: center;
}
.article-content {
    width: 60%;
    border-radius: 4px;
    padding: 40px 50px;
    margin: 0 20px;
    background-color: rgb(255, 255, 255);
    box-shadow: 0 3px 8px 6px rgba(7, 17, 27, 0.08);
    transition: all 0.5s ease;
    overflow: hidden;
    margin-bottom: 50px;
}
.article-content:hover {
    box-shadow: 0 3px 8px 6px rgba(7, 17, 27, 0.12);
}
.content {
    margin-bottom: 10px;
}
.tags {
    display: flex;
    margin: 10px;
    flex-wrap: wrap;
    align-items: center;
}
.tag {
    border: 1px solid #49b1f5;
    width: max-content;
    border-radius: 6px;
    padding: 6px 12px;
    cursor: pointer;
    margin: 4px 8px 0 0;
    color: #49b1f5;
    font-weight: 700;
    transition: all 0.2s ease;
}
.tag:hover {
    color: #fff;
    background-color: #49b1f5;
}
.line {
    border-bottom: 4px dashed #90ccff;
    margin: 30px 0;
}
.article-update-time {
    width: max-content;
}
.article-create-time {
    width: max-content;
}
.sider-right {
    max-width: 260px;
}
.sider-right-content {
    position: sticky;
    top: 100px;
    transition: all 0.5s ease;
}
.ArticleAuthorInfoCard-right {
    width: 260px;

    margin-bottom: 20px;
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

.sider-edit-Btns {
}
.sider-edit-Btn {
    margin: 10px 0;
    font-size: 18px;
    text-align: center;
    border-radius: 4px;
    padding: 6px;
    color: #fff;
    background-color: #a28dd1;
}
.sider-edit-Btn:hover {
    background-color: #846cb6;
}
.article-actions {
    display: flex;
    align-items: center;
    justify-content: center;
    font-size: 16px;
}
.article-actions-item {
    outline: none;
    border: none;
    background-color: #fff;
    padding: 4px;
    margin: 0 4px;
    cursor: pointer;
    width: 80px;
}
.article-actions-item.likes::before {
    content: "\e63c";
    font-family: "iconfont";
    margin-right: 4px;
    color: #6cbaff;
    font-size: 20px;
}
.article-actions-item.collection::before {
    content: "\e625";
    font-family: "iconfont";
    margin-right: 4px;
    color: #6cbaff;
    font-size: 20px;
}
.article-actions-item.likes.clicked::before {
    content: "\e63b";
    font-family: "iconfont";
    margin-right: 4px;
    color: #ff8fbe;
    font-size: 20px;
}
.article-actions-item.collection.clicked::before {
    content: "\e646";
    font-family: "iconfont";
    margin-right: 4px;
    color: #ff8fbe;
    font-size: 20px;
}
</style>
<!-- 代码高亮 -->
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
    overflow: hidden;
}
.content ul {
    list-style: disc;
}
blockquote {
    background-color: #ebebeb;
    border-left: 10px solid #84c6ff;
    padding: 10px;
    margin: 10px 0;
}
span code {
    padding: 0px 0.5em;
    font-family: "Source Code Pro", "DejaVu Sans Mono", "Ubuntu Mono", "Anonymous Pro", "Droid Sans Mono", Menlo, Monaco,
        Consolas, Inconsolata, Courier, monospace, "PingFang SC", "Microsoft YaHei", sans-serif;
}
p code {
    padding: 0px 0.5em;
    font-family: "Source Code Pro", "DejaVu Sans Mono", "Ubuntu Mono", "Anonymous Pro", "Droid Sans Mono", Menlo, Monaco,
        Consolas, Inconsolata, Courier, monospace, "PingFang SC", "Microsoft YaHei", sans-serif;
}
code {
    margin: 10px;
    border-radius: 4px;
    /* font-size: 18px; */
    /* font-weight: 700; */
    line-height: 24px;
    background-color: #fafafa !important;
    border: 2px solid #d6d6d6;
    font-family: "Source Code Pro", "DejaVu Sans Mono", "Ubuntu Mono", "Anonymous Pro", "Droid Sans Mono", Menlo, Monaco,
        Consolas, Inconsolata, Courier, monospace, "PingFang SC", "Microsoft YaHei", sans-serif;
}

pre code.hljs {
    padding: 0.5em;
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
    .ArticleAuthorInfoCard-right {
        width: 100%;
    }
    .sider-right {
        max-width: unset;
    }
    .comment-header {
        font-size: 12px;
        flex-direction: column;
        justify-content: start;
    }
    .comment-time {
        font-size: 12px;
        width: 100%;
    }
    .comment-username {
        width: 100%;
    }
    .comment-username.child {
        width: 100%;
    }
    .comment-item {
        width: 100%;
        padding-right: 10px;
    }
    .comment-content,
    .comment-content.child {
        width: 100%;
        margin: 0;
    }
}
</style>
