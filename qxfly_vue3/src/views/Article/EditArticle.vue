<template>
    <!-- <TopBar></TopBar> -->
    <div class="Toolbar" id="Toolbar">
        <Toolbar style="border-bottom: 1px solid #ccc" :editor="editorRef" :defaultConfig="toolbarConfig" mode="mode" />
    </div>
    <div class="topbar"></div>
    <!-- <div id="load" class="load" v-if="isload">
    <div class="load-content">努力加载中...</div>
  </div> -->
    <div class="add-article-main" id="add-article-main">
        <div class="main">
            <div class="content">
                <div class="sider-left">
                    <div class="sider-items">
                        <div class="sider-item">
                            <div class="sider-item-label">是否公开文章</div>
                            <el-radio-group v-model="articlePub" size="small">
                                <el-radio-button label="公开" value="1" />
                                <el-radio-button label="不公开" value="2" />
                            </el-radio-group>
                        </div>
                        <div class="sider-item">
                            <div class="sider-item-label">在图片页显示文章内图片</div>
                            <el-radio-group v-model="imagePub" size="small">
                                <el-radio-button label="在图片页显示" value="1" />
                                <el-radio-button label="仅文章内可看" value="2" />
                            </el-radio-group>
                        </div>
                        <div class="sider-item">
                            <div class="sider-item-label">分类</div>
                            <el-select v-model="article.classify" class="classify" placeholder="无">
                                <el-option
                                    v-for="item in classifys"
                                    :key="item.id"
                                    :label="item.name"
                                    :value="item.name" />
                                <el-option label="无" value="" />
                            </el-select>
                        </div>
                        <div class="sider-item">
                            <div class="sider-item-label">标签</div>
                            <div class="sider-item-tags">
                                <div class="select-Tags">
                                    <div class="tag selected" v-for="item in tags" :key="item" @click="rmTag(item)">
                                        {{ item }}
                                        <span @click="rmTag(item)" class="rmTag"></span>
                                    </div>
                                </div>
                                <div class="tag-line"></div>
                                <div class="select-Tags scorll">
                                    <div class="tag" v-for="item in allTags" :key="item" @click="addTag(item.name)">
                                        {{ item.name }}
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="add-article-form">
                    <div class="add-article-item">
                        <input
                            type="text"
                            class="add-article-input-title"
                            placeholder="请输入标题"
                            v-model="article.title"
                            maxlength="50"
                            required />
                    </div>
                    <hr />
                    <div class="add-article-item">
                        <div class="Editor" id="Editor">
                            <Editor
                                v-model="valueHtml"
                                :defaultConfig="editorConfig"
                                mode="mode"
                                @onCreated="handleCreated" />
                        </div>
                    </div>
                </div>
                <div class="sider-right">
                    <div class="sider-right-item">
                        <div class="add-article-cover-title">添加封面</div>
                        <div>
                            <label for="input-cover" class="input-cover">上传封面</label>
                            <input
                                type="file"
                                id="input-cover"
                                style="display: none"
                                @change="getFile($event)"
                                :key="asd" />
                        </div>
                        <label
                            for="input-cover"
                            class="previewCoverLabel"
                            @dragstart.prevent
                            @dragover.prevent
                            @dragenter.prevent
                            @drop="dragenter($event)">
                            <img v-if="!isChoice" :src="previewCover" alt="" class="previewCover" />
                            <img v-if="isChoice" :src="previewCover" alt="" class="previewCover choice" />
                        </label>
                    </div>
                    <div class="sider-right-item">
                        <div class="add-article-cover-title">添加附件</div>
                        <div>
                            <label for="input-attachment" class="input-cover">上传附件</label>
                            <input
                                type="file"
                                id="input-attachment"
                                style="display: none"
                                @change="getAttachment($event)"
                                :key="asd" />
                        </div>
                        <div class="input-attachment-list">
                            <div
                                class="input-attachment-list-item"
                                v-for="(file, index) in attachmentList"
                                :key="index">
                                <div class="input-attachment-list-item-content">
                                    <div class="span-number">{{ index + 1 }}</div>
                                    <div class="input-attachment-list-item-name">{{ file.fileOriginName }}</div>
                                    <el-button
                                        v-if="file.fileName != ''"
                                        type="danger"
                                        size="small"
                                        plain
                                        @click="DeleteAttachment(file)"
                                        class="input-attachment-list-item-remove">
                                        删除
                                    </el-button>
                                </div>
                                <div class="input-attachment-list-item-process">
                                    <div
                                        class="input-attachment-process"
                                        :id="'input-attachment-process-' + file.index"></div>
                                </div>
                            </div>
                            <div></div>
                        </div>
                    </div>
                    <div class="release-btns">
                        <button
                            @click="release"
                            v-if="userRoute.params.editMode != 'true'"
                            v-text="releaseBtn"></button>
                        <button @click="EditArticle" v-if="userRoute.params.editMode == 'true'">确 认 编 辑</button>
                        <button @click="SaveArticle">保存</button>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <!-- 裁剪图片 -->
    <el-dialog
        v-model="cropperCoverDialog"
        :append-to-body="true"
        :close-on-click-modal="false"
        :show-close="false"
        :lock-scroll="false"
        center
        width="600px">
        <div style="width: 500px; height: 500px; margin: 0 auto">
            <vueCropper
                style="width: 100%; height: 100%"
                ref="cropper"
                :img="option.img"
                :outputSize="option.outputSize"
                :outputType="option.outputType"
                :info="option.info"
                :canScale="option.canScale"
                :autoCrop="option.autoCrop"
                :autoCropWidth="option.autoCropWidth"
                :autoCropHeight="option.autoCropHeight"
                :fixedBox="option.fixedBox"
                :fixed="option.fixed"
                :fixedNumber="option.fixedNumber"
                :canMove="option.canMove"
                :canMoveBox="option.canMoveBox"
                :original="option.original"
                :centerBox="option.centerBox"
                :infoTrue="option.infoTrue"
                :full="option.full"
                :enlarge="option.enlarge"
                :mode="option.mode">
            </vueCropper>
        </div>
        <template #footer>
            <span class="dialog-footer">
                <el-button @click="closeCropper">取 消</el-button>
                <el-button type="primary" @click="cropperCover">确 定</el-button>
            </span>
        </template>
    </el-dialog>
</template>

<script setup>
import TopBar from "@/components/TopBar/TopBar.vue";
import "@wangeditor/editor/dist/css/style.css"; // 引入 css
import { onBeforeUnmount, ref, shallowRef, onMounted, onUnmounted, reactive, watch } from "vue";
import { Editor, Toolbar } from "@wangeditor/editor-for-vue";
import {
    releaseArticle,
    getArticleById,
    updateArticleCover,
    editArticle,
    deleteArticleImage,
    getClassifies,
    getTags,
    saveAttachment,
    getArticleAttachment,
    deleteAttachment,
} from "@/api/Article/index";
import { upload } from "@/api/index";
import md5 from "js-md5";
import router from "@/router";
import { useRoute, onBeforeRouteLeave } from "vue-router";
import "vue-cropper/dist/index.css";
import { VueCropper } from "vue-cropper";
import hljs from "highlight.js/lib/core";
import "highlight.js/styles/github.css";
import { ElMessage, ElMessageBox } from "element-plus";
import request from "@/utils/request";
window.hljs = hljs;
require("highlightjs-line-numbers.js");
// 编辑器实例，必须用 shallowRef
const editorRef = shallowRef();
let userRoute = useRoute();
// let isload = ref(true);
let article = ref({
    title: "",
    content: "",
    preview: "",
    cover: "",
    classify: null,
    tag: [],
    pub: 1,
});
let articlePub = ref(1);
let imagePub = ref(1);
/**
 * 发布文章
 */
let isrelease = ref(false);
let releaseBtn = ref("发布文章");
async function release() {
    releaseBtn.value = "发布中...";
    if (article.value.title.trim().length == 0) {
        releaseBtn.value = "发布文章";
        alert("文章标题不能为空");
        return;
    }
    if (uploadingList.value.length > 0) {
        releaseBtn.value = "发布文章";
        alert("附件正在上传中，请稍后");
        return;
    }
    if (formData.get("file") != null) {
        /* 先上传封面 */
        await updateArticleCover(formData).then((res) => {
            if (res.data.code == 1) {
                article.value.cover = res.data.data;
                /* 再上传内容 */
                article.value.content = editorRef.value.getHtml();
                article.value.preview = editorRef.value.getText().substring(0, 250);
                if (article.value.preview.trim().length == 0) {
                    releaseBtn.value = "发布文章";
                    alert("请输入内容（不包括图片）");
                    return;
                }
                /* 删除差异图片 */
                delimg();
                /* 格式化标签 */
                let temptag = [];
                for (let i = 0; i < tags.value.length; i++) {
                    temptag.push(tags.value[i]);
                }
                article.value.tag = temptag.toString();
                /* 是否公开 */
                article.value.pub = parseInt(articlePub.value) * 10 + parseInt(imagePub.value);
                releaseArticle(article.value).then(async (res) => {
                    if (res.data.code == 1) {
                        if (attachmentList.value.length > 0) {
                            /* 保存附件 */
                            await saveAttachment({
                                aid: res.data.data,
                                attachment: JSON.stringify(attachmentList.value),
                            }).then((res) => {
                                if (res.data.code == 1) {
                                    attachmentList.value = [];
                                }
                            });
                        }

                        releaseBtn.value = "发布文章";
                        alert("发布成功");
                        localStorage.removeItem("saveArticle");
                        isrelease.value = true;
                        router.replace("/index/articleView");
                    } else {
                        releaseBtn.value = "发布文章";
                        alert("发布失败,请重试。可能原因：" + res.data.msg);
                    }
                });
            } else {
                releaseBtn.value = "发布文章";
                alert("上传封面失败,可能原因有：" + res.data.msg);
            }
        });
    } else {
        releaseBtn.value = "发布文章";
        alert("请上传封面");
    }
}
/**
 * 编辑文章
 */
async function EditArticle() {
    if (article.value.title.trim().length == 0) {
        alert("文章标题不能为空");
        return;
    }
    if (uploadingList.value.length > 0) {
        releaseBtn.value = "发布文章";
        alert("附件正在上传中，请稍后");
        return;
    }
    if (formData.get("file") != null) {
        /* 先上传封面 */
        await updateArticleCover(formData).then((res) => {
            if (res.data.code == 1) {
                article.value.cover = res.data.data;
            } else {
                releaseBtn.value = "发布文章";
                alert("发布失败,请重试。可能原因：" + res.data.msg);
            }
        });
    } /* 再上传内容 */
    article.value.content = editorRef.value.getHtml();
    article.value.preview = editorRef.value.getText().substring(0, 250);
    if (article.value.preview.trim().length == 0) {
        alert("请输入内容（不包括图片）");
        return;
    }
    delimg();
    let temptag = [];
    for (let i = 0; i < tags.value.length; i++) {
        temptag.push(tags.value[i]);
    }
    article.value.tag = temptag.toString();
    /* 是否公开 */
    article.value.pub = parseInt(articlePub.value) * 10 + parseInt(imagePub.value);
    editArticle(article.value).then(async (res) => {
        if (res.data.code == 1) {
            for (let i = 0; i < removeAttachmentList.value.length; i++) {
                await deleteAttachment(article.value.id, removeAttachmentList.value[i].fileName);
            }
            await saveAttachment({
                aid: article.value.id,
                attachment: JSON.stringify(editNewAttachmentList.value),
            }).then((res) => {});

            alert("编辑成功");
            localStorage.removeItem("saveArticle");
            isrelease.value = true;
            router.back();
        } else {
            releaseBtn.value = "发布文章";
            alert("发布失败,请重试。可能原因：" + res.data.msg);
        }
    });
}
let tempAttachmentList = ref([]);
function toEditArticle(articleId) {
    getArticleById(articleId).then((res) => {
        article.value = res.data.data;
        previewCover.value = article.value.cover;
        editorRef.value.setHtml(article.value.content);
        if (article.value.tag != null && article.value.tag != "") {
            let temp = article.value.tag.split(",");
            for (let i = 0; i < temp.length; i++) {
                tags.value.push(temp[i]);
            }
        }
        let temp = editorRef.value.getElemsByType("image");
        for (let i = 0; i < temp.length; i++) {
            imageUrl.push(temp[i].src);
        }

        imagePub.value = article.value.pub % 10;
        articlePub.value = Math.floor(article.value.pub / 10);
    });
    getArticleAttachment(articleId).then((res) => {
        if (res.data.code == 1) {
            attachmentList.value = res.data.data;
            for (let i = 0; i < attachmentList.value.length; i++) {
                attachmentList.value[i].index = attachmentIndex.value++;
            }
            tempAttachmentList.value = res.data.data;
        }
    });
}
/* 保存文章 */
function SaveArticle() {
    if (editorRef.value.getText() != "" && editorRef.value.getHtml() != "<p></p>") {
        /* 保存文章标题 */
        localStorage.setItem("articleTitle", article.value.title);
        /* 保存文章内容 */
        localStorage.setItem("articleContent", editorRef.value.getHtml());
        /* 保存分类 */
        if (article.value.classify != null && article.value.classify != "") {
            localStorage.setItem("articleCalssify", article.value.classify);
        }
        /* 保存标签 */
        let temptag = [];
        for (let i = 0; i < tags.value.length; i++) {
            temptag.push(tags.value[i]);
        }
        if (temptag.length > 0) {
            localStorage.setItem("articleTags", temptag.toString());
        }

        ElMessageBox.alert("文章已保存至本地，清空浏览器缓存后将被删除，请尽快编辑发布", {
            confirmButtonText: "确定",
            type: "warning",
            lockScroll: false,
            center: true,
        });
    } else {
        ElMessageBox.alert("文章内容为空", {
            confirmButtonText: "确定",
            type: "warning",
            lockScroll: false,
            center: true,
        }).catch();
    }
}
/* 加载保存的文章 */
function loadSaveArticle() {
    let a = {
        title: localStorage.getItem("articleTitle"),
        content: localStorage.getItem("articleContent"),
        calssify: localStorage.getItem("articleCalssify"),
        Tags: localStorage.getItem("articleTags"),
    };
    if (a.content != null || a.title != null || a.Tags != null || a.calssify != null) {
        ElMessageBox.confirm("有本地保存的文章，是否继续编辑", {
            confirmButtonText: "确定",
            cancelButtonText: "取消",
            type: "warning",
            lockScroll: false,
            center: true,
        })
            .then(() => {
                editorRef.value.setHtml(a.content);
                article.value.title = a.title;
                article.value.classify = a.calssify;
                tags.value = a.Tags.split(",");
                localStorage.removeItem("articleContent");
                localStorage.removeItem("articleTitle");
                localStorage.removeItem("articleCalssify");
                localStorage.removeItem("articleTags");
            })
            .catch(() => {});
    }
}
/* 获取分类 */
let classifys = ref([]);
function GetClassifies() {
    getClassifies().then((res) => {
        classifys.value = res.data.data;
    });
}
/* 标签 */
let allTags = ref([]);
let tags = ref([]);
/* 获取标签 */
function GetTags() {
    getTags().then((res) => {
        allTags.value = res.data.data;
        /* 进行排序 */
        allTags.value = allTags.value.sort((a, b) => {
            if (a.name < b.name) {
                return -1;
            }
            if (a.name > b.name) {
                return 1;
            }
            return 0;
        });
    });
}
/* 添加标签 */
function addTag(tag) {
    if (tag == "") return;
    if (tags != null && tags.value.indexOf(tag) != -1) {
        tag = "";
    } else {
        tags.value.push(tag);
        allTags.value = allTags.value.filter((item) => item.name != tag);
        tag = "";
    }
}
/* 删除标签 */
function rmTag(tag) {
    tags.value = tags.value.filter((item) => item != tag);
    allTags.value.push({ name: tag });
    /* 进行排序 */
    allTags.value = allTags.value.sort((a, b) => {
        if (a.name < b.name) {
            return -1;
        }
        if (a.name > b.name) {
            return 1;
        }
        return 0;
    });
}
/* 删除文章已经移除的图片 */
function delimg() {
    /* 从服务端删除已在文章中移除的图片 */
    var temp = editorRef.value.getElemsByType("image"); //获取修改后文章已有图片
    var tempImageUrl = []; //修改后文章已有图片的url
    for (let j = 0; j < temp.length; j++) {
        tempImageUrl.push(temp[j].src);
    }
    /* 保存文章中的图片 */
    article.value.author = tempImageUrl.toString();
    let imageList = []; //要删除的图片
    // 通过比较修改前文章中已有图片，删除差异项
    for (let i = 0; i < imageUrl.length; i++) {
        let flag = 0;
        for (let j = 0; j < tempImageUrl.length; j++) {
            if (imageUrl[i] == temp[j].src) {
                flag = 1;
                break;
            }
        }
        if (flag == 0) {
            let splits = imageUrl[i].split("articleImage/");
            let name = splits[splits.length - 1];
            imageList.push(name);
        }
    }
    if (imageList.length > 0) {
        deleteArticleImage(imageList.join(","));
    }
}
/*  */
/* 上传封面 */
let isChoice = ref(false);
let previewCover = ref(require("@/assets/img/icon/addImage.svg"));
let formData = new FormData();
let coverFileName = "";
/* 获取文件数据 */
function getFile(event) {
    let file = event.target.files[0];
    fileHandler(file);
}
/* 拖拽上传 */
function dragenter(event) {
    event.preventDefault();
    event.stopPropagation();
    let file = event.dataTransfer.files[0];
    fileHandler(file);
}
function fileHandler(file) {
    if (file != null) {
        if (file.type.match(/image/) != null) {
            if (file.size < 1024 * 1024 * 10) {
                /* 展示预览图片 */
                coverFileName = file.name;
                isChoice.value = true;
                cropperCoverDialog.value = true;
                option.img = URL.createObjectURL(file);
                // prePreviewCover.value = previewCover.value;
                previewCover.value = URL.createObjectURL(file);
                const a = document.getElementById("input-cover");
                a.value = null;
            } else {
                alert("封面大小不能超过10MB");
            }
        } else {
            alert("请选择选择格式为png、jpg、jpeg、webp的图片");
        }
    }
}
/* 裁剪封面 */
let cropperCoverDialog = ref(false);
let cropper = ref();
let option = reactive({
    img: null, // 裁剪图片的地址 url 地址, base64, blob
    outputSize: 0.3, // 裁剪生成图片的质量
    outputType: "webp", // 裁剪生成图片的格式 jpeg, png, webp
    info: true, // 裁剪框的大小信息
    canScale: true, // 图片是否允许滚轮缩放
    autoCrop: true, // 是否默认生成截图框
    autoCropWidth: 150, // 默认生成截图框宽度
    autoCropHeight: 150, // 默认生成截图框高度
    fixedBox: false, // 固定截图框大小 不允许改变
    fixed: true, // 是否开启截图框宽高固定比例，这个如果设置为true，截图框会是固定比例缩放的，如果设置为false，则截图框的狂宽高比例就不固定了
    fixedNumber: [1.6, 1], // 截图框的宽高比例 [ 宽度 , 高度 ]
    canMove: true, // 上传图片是否可以移动
    canMoveBox: true, // 截图框能否拖动
    original: false, // 上传图片按照原始比例渲染
    centerBox: true, // 截图框是否被限制在图片里面
    infoTrue: true, // true 为展示真实输出图片宽高 false 展示看到的截图框宽高
    full: true, // 是否输出原图比例的截图
    enlarge: "1", // 图片根据截图框输出比例倍数
    mode: "contain", // 图片默认渲染方式 contain , cover, 100px, 100% auto
});
/* 裁剪封面 */
let prePreviewCover = ref(null);
function cropperCover() {
    cropperCoverDialog.value = false;
    cropper.value.getCropBlob((data) => {
        formData = new FormData();
        let file = new File([data], coverFileName.split(".")[0] + ".webp");
        formData.append("file", file);
        // formData.append("token", localStorage.getItem(md5("token")));
        prePreviewCover.value = previewCover.value;
        previewCover.value = URL.createObjectURL(data);
    });
}
let asd = ref(0);
function closeCropper() {
    cropperCoverDialog.value = false;
    asd.value = asd.value + 1;
    isChoice.value = false;
    option.img = null;
    if (prePreviewCover.value != null) {
        previewCover.value = prePreviewCover.value;
    } else {
        previewCover.value = require("@/assets/img/icon/addImage.svg");
    }
}

/* 上传附件 */
let attachmentList = ref([]); //附件队列
let editNewAttachmentList = ref([]); //编辑模式下附件队列
let uploadList = ref([]); //附件上传队列
let attachmentIndex = ref(0); //附件队列索引
let uploadingList = ref([]); //上传中的附件的索引
function getAttachment(e) {
    if (attachmentList.value.length + 1 > 10) {
        alert("只能上传10个附件");
        return;
    }
    let file = e.target.files[0];
    let formData = new FormData();
    formData.append("file", file);
    let index = ref(attachmentIndex.value++);
    uploadingList.value.push(index.value);
    attachmentList.value.push({ index: index.value, fileName: "", fileOriginName: file.name });
    if (editMode.value) {
        editNewAttachmentList.value.push({ index: index.value, fileName: "", fileOriginName: file.name });
    }
    uploadList.value.push({
        index: index.value,
        req: request
            .post("article/uploadAttachment", formData, {
                onUploadProgress: (e) => {
                    onProcess(e, index.value);
                },
            })
            .then((res) => {
                const processEle = document.getElementById("input-attachment-process-" + index.value);
                if (res.data.code == 1) {
                    processEle.style.width = "0%";
                    attachmentList.value.find((item) => item.index == index.value).fileName = res.data.data;
                    if (editMode.value) {
                        editNewAttachmentList.value.find((item) => item.index == index.value).fileName = res.data.data;
                    }
                    uploadingList.value = uploadingList.value.filter((item) => item != index.value);
                } else {
                    ElMessage.error({ message: res.data.msg, offset: 120 });
                    processEle.style.width = "0%";
                    attachmentList.value = attachmentList.value.filter((item) => item.index == index.value);
                    uploadingList.value = uploadingList.value.filter((item) => item != index.value);
                }
            }),
    });
}
/* 上传进度条 */
function onProcess(e, index) {
    const processEle = document.getElementById("input-attachment-process-" + index);
    processEle.style.width = e.progress * 100 + "%";
}
/* 删除附件 */
let removeAttachmentList = ref([]);
function DeleteAttachment(file) {
    /* 清空input记录，防止已删除文章无法再次上传 */
    const a = document.getElementById("input-attachment");
    a.value = null;
    if (editMode.value) {
        removeAttachmentList.value.push(file);
        attachmentList.value = attachmentList.value.filter((item) => item.fileName != file.fileName);
    } else {
        deleteAttachment(0, file.fileName).then((res) => {
            if (res.data.code == 1) {
                attachmentList.value = attachmentList.value.filter((item) => item.fileName != file.fileName);
            } else {
                ElMessage.error({ message: res.data.msg, offset: 120 });
            }
        });
    }
}
// 内容 HTML
let imageUrl = [];
const valueHtml = ref("");
const toolbarConfig = {};
const editorConfig = {
    placeholder: "请输入内容...",
    MENU_CONF: {
        uploadImage: {
            // server: "http://localhost:8081/article/uploadArticleImage",
            server:
                window.location.hostname == "38.55.199.233"
                    ? process.env.VUE_APP_IP_BASE_URL + "/article/uploadArticleImage"
                    : process.env.VUE_APP_BASE_URL + "/article/uploadArticleImage",
            fieldName: "file",
            headers: { token: localStorage.getItem(md5("token")) },
            maxFileSize: 10 * 1024 * 1024,
            base64LimitSize: 5 * 1024,
            allowedFileTypes: ["image/*"],
            customInsert(res, insertFn) {
                const imgUrl = res.data;
                if (!imgUrl) throw new Error(`Image url is empty`);
                insertFn(imgUrl, res.msg, imgUrl);
            },
            onBeforeUpload(file) {
                for (const key in file) {
                    if (file[key].size > 1024 * 1024 * 10) {
                        alert("图片大小不能超过10MB");
                        delete file[key];
                        return false;
                    } else {
                        return file;
                    }
                }
            },
            // onProgress(progress) {
            //   console.log("onProgress", progress);
            // },
            // onSuccess(file, res) {
            //   console.log("onSuccess", file, res);
            // },
            // onFailed(file, res) {
            //   alert(res.message);
            //   console.log("onFailed", file, res);
            // },
            // onError(file, err, res) {
            //   console.error("onError", file, err.message, res);
            // },
        },
        codeSelectLang: {
            codeLangs: [
                { text: "C", value: "c" },
                { text: "C++", value: "cpp" },
                { text: "Java", value: "java" },
                { text: "Python", value: "python" },
                { text: "GO", value: "go" },
                { text: "C#", value: "csharp" },
                { text: "SQL", value: "sql" },
                { text: "JavaScript", value: "javascript" },
                { text: "Typescript", value: "typescript" },
                { text: "Markdown", value: "markdown" },
                { text: "CSS", value: "css" },
                { text: "HTML", value: "html" },
                { text: "XML", value: "xml" },
            ],
        },
        // 插入图片
        insertImage: {
            onInsertedImage(imageNode) {
                if (imageNode == null) return;
                const { src, alt, url, href } = imageNode;
                imageUrl.push(src);
            },
            // checkImage: customCheckImageFn, // 也支持 async 函数
            // parseImageSrc: customParseImageSrc, // 也支持 async 函数
        },
    },
};

/* 排除不用功能 */
toolbarConfig.excludeKeys = ["group-video", "todo", "fullScreen"];
/* 根据窗口大小自动调整编辑区大小 */
function Listener() {
    const Toolbar = document.getElementById("Toolbar");
    var scrollTop = document.documentElement.scrollTop || document.body.scrollTop;
    if (scrollTop > 600) {
        Toolbar.style.top = "0px";
    } else {
        Toolbar.style.top = "74px";
    }
}
const handleCreated = (editor) => {
    editorRef.value = editor;
};
/* 离开页面保存文章 */
onBeforeRouteLeave(async (to, from, next) => {
    if (!isrelease.value && editorRef.value.getText() != "") {
        await ElMessageBox.confirm("是否保存文章？", {
            confirmButtonText: "保存",
            cancelButtonText: "不保存",
            type: "warning",
            lockScroll: false,
            center: true,
        })
            .then(() => {
                SaveArticle();
                next();
            })
            .catch(() => {
                next();
            });
    } else {
        next();
    }
});

var onbeforeunloadHeadler = (e) => {
    e.returnValue = "";
};
let editMode = ref(false);
onMounted(() => {
    GetClassifies();
    GetTags();
    /* 根据窗口大小控制编辑区的高度 */
    const Editor = document.getElementById("Editor");
    let height = document.body.scrollHeight - 300;
    Editor.style.height = height + "px";
    /* 判断是否可以编辑文章 */
    if (userRoute.params.editMode == "true") {
        editMode.value = true;
        toEditArticle(userRoute.params.articleId);
    } else {
        loadSaveArticle();
    }
    window.addEventListener("scroll", Listener);
    window.addEventListener("beforeunload", onbeforeunloadHeadler);
});
onUnmounted(() => {
    window.removeEventListener("scroll", Listener, false);
    window.removeEventListener("beforeunload", onbeforeunloadHeadler);
});
// 组件销毁时，也及时销毁编辑器
onBeforeUnmount(() => {
    const editor = editorRef.value;
    if (editor == null) return;
    editor.destroy();
    /* 删除已上传附件 */
    if (!editMode.value && attachmentList.value.length > 0) {
        attachmentList.value.forEach((item) => {
            deleteAttachment(0, item.fileName);
        });
    }
    /* 删除已上传图片 */
    if (!editMode.value && !isrelease.value) {
        var temp = editorRef.value.getElemsByType("image"); //获取修改后文章已有图片
        var tempImageUrl = []; //修改后文章已有图片的url
        for (let j = 0; j < temp.length; j++) {
            let splits = temp[j].src.split("/");
            let name = splits[splits.length - 1];
            tempImageUrl.push(name);
        }
        if (tempImageUrl.length > 0) {
            deleteArticleImage(tempImageUrl.join(","));
        }
    }
});
</script>

<style scoped>
.main {
    width: 100%;
    height: 100%;
    margin-bottom: 20px;
    position: relative;
    top: 50px;
}
.add-article-main {
    width: 100%;
}
.topbar {
    display: flex;
    width: 100%;
    background-color: rgb(255, 255, 255);
    z-index: 999;
}
.Toolbar {
    width: 100%;
    position: fixed;
    top: 70px;
    margin: 0 auto;
    z-index: 99;
    transition: all 0.3s ease;
}
.content {
    display: flex;
    justify-content: center;
    margin-top: 80px;
    transition: all 0.3s ease;
}
.add-article-form {
    /* min-width: 1000px; */
    width: 60%;
    border-radius: 4px;
    padding: 40px 50px;
    background-color: rgb(255, 255, 255);
    margin-right: 20px;
    box-shadow: 0 3px 8px 6px rgba(7, 17, 27, 0.08);
    transition: all 0.3s ease;
}
.add-article-form:hover {
    box-shadow: 0 3px 8px 6px rgba(7, 17, 27, 0.14);
}
.add-article-item {
    margin: 0 0 20px 0;
}
.Editor {
    overflow: hidden;
}
.add-article-label {
    font-size: 24px;
    font-weight: 700;
}
.add-article-input-title {
    width: 100%;
    border: none;
    font-size: 24px;
    font-weight: 700;
    outline: none;
}
.sider-left {
    min-width: 260px;
    width: 260px;
    height: max-content;
    max-height: 800px;
    border-radius: 4px;
    padding: 40px 20px;
    box-shadow: 0 3px 8px 6px rgba(7, 17, 27, 0.08);
    transition: all 0.3s ease;
    margin-right: 20px;
    overflow: auto;
}
.sider-item {
    margin-bottom: 10px;
}
.sider-item-label {
    font-weight: 700;
    font-size: 14px;
}
.sider-item-tags {
    /* display: flex; */
}
.scorll {
    max-height: 400px;
    overflow: auto;
}
.tag-line {
    border: 1px solid #000;
    margin: 10px;
}
.tag {
    border: 1px solid #49b1f5;
    width: max-content;
    border-radius: 4px;
    padding: 2px 8px;
    cursor: pointer;
    margin: 0 6px 6px 0;
    color: #49b1f5;
}
.tag:hover {
    background-color: #49b1f5;
    color: #fff;
}
.tag.selected:hover {
    border: 1px solid #f56c6c;
    background-color: #f56c6c;
    color: #fff;
}
.select-Tags {
    display: flex;
    flex-wrap: wrap;
    /* flex: 1; */
}
.rmTag::before {
    content: "\e601";
    font-family: "iconfont";
    font-size: 18px;
    font-weight: 700;
}
.tags:hover {
    background-color: #ecf5ff;
}
.classify {
    margin: 5px 0;
}
.sider-right {
    min-width: 260px;
    width: 260px;
    height: max-content;
    transition: all 0.3s ease;
}

.sider-right-item {
    border-radius: 4px;
    padding: 20px 20px;
    box-shadow: 0 3px 8px 6px rgba(7, 17, 27, 0.08);
    transition: all 0.3s ease;
    margin-bottom: 20px;
    overflow: hidden;
}
.sider-right-item:hover {
    box-shadow: 0 3px 8px 6px rgba(7, 17, 27, 0.14);
}
.add-article-cover-title {
    text-align: center;
    font-weight: 700;
    font-size: 24px;
    padding-bottom: 10px;
    border-bottom: 3px dashed rgb(218, 218, 218);
    margin-bottom: 10px;
}
.input-cover {
    display: block;
    text-align: center;
    color: #fff;
    padding: 6px 12px;
    border-radius: 4px;
    font-weight: 500;
    background-color: #a28dd1;
    cursor: pointer;
    margin: 20px auto;
    user-select: none;
}
.input-cover:hover {
    background-color: #846cb6;
}
.previewCover {
    width: 100%;
    border-radius: 4px;
    border: 1px solid #000;
    transition: all 0.3s ease;
    opacity: 0.6;
}
.previewCover.choice {
    opacity: 1;
}
.previewCover:hover {
    opacity: 1;
}
.input-attachment-list-item {
    border-radius: 4px;
    margin-bottom: 4px;
}
.input-attachment-list-item-content:hover {
    background-color: #836cb63a;
}
.input-attachment-list-item-content {
    display: flex;
    transition: all 0.3s ease;
}
.span-number {
    width: 24px;
    min-width: 24px;
    background-color: #a28dd1;
    text-align: center;
    font-weight: 700;
    border-radius: 0 4px 4px 0;
    margin-right: 6px;
    padding: 2px 0;
    color: #fff;
    font-size: 15px;
}
.input-attachment-list-item-name {
    flex: 1;
    overflow: scroll;
    margin-right: 6px;
}
.input-attachment-list-item-name::-webkit-scrollbar {
    display: none;
}
.input-attachment-process {
    height: 2px;
    width: 0%;
    background-color: #846cb6;
    transition: all 0.3s ease;
    margin-top: 4px;
}
.release-btns {
    display: flex;
    justify-content: center;
    margin-top: 20px;
    flex-direction: column;
}
.release-btns button {
    text-align: center;
    color: #fff;
    width: 100%;
    height: 34px;
    padding: 6px 12px;
    border-radius: 4px;
    font-size: 16px;
    font-weight: 500;
    background-color: #a28dd1;
    cursor: pointer;
    margin: 10px auto;
    user-select: none;
    border: none;
}
.release-btns button:hover {
    background-color: #846cb6;
}
@media (max-width: 1200px) {
    /* .sider-left,
    .sider-right {
        min-width: 200px;
        width: 200px;
        padding: 40px 20px;
    } */
}
@media (max-width: 700px) {
    .content {
        flex-direction: column;
        margin-top: 100px;
    }
    .sider-left {
        min-width: unset;
        width: 100%;
        margin-bottom: 20px;
    }
    .sider-right {
        min-width: unset;
        width: 100%;
        margin-top: 20px;
    }
    .sider-right-item {
        min-width: unset;
        width: 100%;
        margin-top: 20px;
    }
    .add-article-form {
        width: 100%;
        margin-bottom: 20px;
    }
    .sider-right-item {
    }
    .previewCoverLabel {
        display: block;
        width: 100%;
        height: 100%;
    }
}
@media (max-width: 440px) {
    .content {
        margin-top: 140px;
    }
}
@media (max-width: 350px) {
    .content {
        margin-top: 180px;
    }
}
@media (max-width: 280px) {
    .content {
        margin-top: 220px;
    }
}
@media (max-width: 245px) {
    .content {
        margin-top: 260px;
    }
}
@media (max-width: 211px) {
    .content {
        margin-top: 300px;
    }
}
@media (max-width: 192px) {
    .content {
        margin-top: 380px;
    }
}
@media (max-width: 150px) {
    .content {
        margin-top: 460px;
    }
}
@media (max-width: 150px) {
    .content {
        margin-top: 460px;
    }
}
</style>
<style>
.w-e-bar.w-e-bar-show.w-e-toolbar {
    justify-content: center;
}
span code {
    padding: 0 0.5em !important;
    font-family: "Source Code Pro", "DejaVu Sans Mono", "Ubuntu Mono", "Anonymous Pro", "Droid Sans Mono", Menlo, Monaco,
        Consolas, Inconsolata, Courier, monospace, "PingFang SC", "Microsoft YaHei", sans-serif !important;
}
blockquote {
    background-color: #ebebeb !important;
    border-left: 10px solid #84c6ff !important;
    padding: 10px;
    margin: 10px 0;
}
code {
    margin: 10px !important;
    border-radius: 4px !important;
    font-size: 16px !important;
    font-weight: 500 !important;
    line-height: 24px !important;
    background-color: #fafafa !important;
    border: 2px solid #d6d6d6 !important;
    font-family: "Source Code Pro", "DejaVu Sans Mono", "Ubuntu Mono", "Anonymous Pro", "Droid Sans Mono", Menlo, Monaco,
        Consolas, Inconsolata, Courier, monospace, "PingFang SC", "Microsoft YaHei", sans-serif !important;
}
.hljs-ln-n {
    text-align: right !important;
    padding-right: 6px !important;
    border-right: 1px solid #333 !important;
    margin-right: 10px !important;
}
pre code {
    padding: 0.5em !important;
}
</style>

<!-- 表格样式 -->
<style>
.content table:not(.hljs-ln) {
    border-collapse: separate;
    border-spacing: 0;
    border: 1px solid #000;
    border-radius: 4px;
    width: 1000px;
    table-layout: fixed;
}
/* 第一行标题总设置 */
.content table:not(.hljs-ln) th {
    padding: 5px;
    background-color: rgb(220, 238, 255);
    border-bottom: 1px solid #0e0f1a;
    border-right: 1px dashed #0e0f1a;
    border-right-width: 1px !important;
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
    border-radius: 4px 0 0 0;
}
/* 第一行标题最后一个元素 */
.content table:not(.hljs-ln) th:last-child {
    border-right: none;
    border-radius: 0 4px 0 0;
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
    border-radius: 0 0 0 4px;
}
/* 最后一行最后一个元素 */
.content table:not(.hljs-ln) tr:last-child td:last-child {
    border-bottom: 0px;
    border-radius: 0 0 4px 0;
}
.content table:not(.hljs-ln) tr {
    overflow: hidden;
    text-overflow: ellipsis;
    white-space: nowrap;
}
</style>
