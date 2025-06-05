<template>
    <div>
        <div class="comments">
            <div class="comments-label">发布评论：</div>
            <div class="release-comment">
                <textarea class="release-comment-textarea" required v-model="commentText" maxlength="250"></textarea>
                <button class="release-comment-btn" @click="ReleaseComment()">发布</button>
            </div>
            <div class="Comment-section">
                <div class="comments-label">评论：</div>
                <div class="comment-list">
                    <div class="comment-item" v-for="comment in comments" :key="comment">
                        <div class="comment-userImg" @click="toUserSpace(comment)">
                            <img :src="getUserAvatar(comment.user.avatar)" />
                        </div>
                        <div class="comment-item-content">
                            <div class="comment-header">
                                <div class="comment-username" v-text="comment.user.username"></div>
                                <div class="comment-header-right">
                                    <div
                                        class="comment-delete"
                                        v-if="isSelf(comment)"
                                        @click="DeleteComment(comment.id)">
                                        删除
                                    </div>
                                    <div class="comment-time" v-text="adjustTime(comment.createTime)"></div>
                                </div>
                            </div>
                            <p class="comment-content" v-text="comment.content"></p>
                            <div class="comment-actions">
                                <div
                                    class="action-item likes"
                                    :class="{ liked: isCommLiked(comment) }"
                                    @click="likeComm(comment)">
                                    {{ comment.likeCount }} 点赞
                                </div>
                                <div class="action-item comm" @click="actionItemComment(comment.id, 0)">评论</div>
                                <div class="action-item"></div>
                            </div>
                            <div class="child-comment-input" :id="'child-comment-' + comment.id">
                                <input
                                    type="text"
                                    class="child-comment-textarea"
                                    placeholder="回复楼主"
                                    required
                                    v-model="itemCommentText" />
                                <button @click="ReleaseComment(comment.id, comment.user.id, comment.user.username, 1)">
                                    评论
                                </button>
                            </div>
                            <!-- 子评论 -->
                            <div class="child-comments" v-if="comment.childComment.length != 0">
                                <p style="font-size: 14px; font-weight: 700; padding-left: 10px">相关回复：</p>
                                <div class="child-comments-items" :id="'comment-' + comment.id">
                                    <div
                                        class="comment-item child"
                                        :class="'commentItemChild-' + comment.id"
                                        v-for="chComment in comment.childComment"
                                        :key="chComment">
                                        <div class="comment-userImg" @click="toUserSpace(chComment)">
                                            <img :src="getUserAvatar(chComment.user.avatar)" />
                                        </div>
                                        <div class="comment-item-content">
                                            <div class="comment-header child">
                                                <div class="comment-username child">
                                                    <!-- {{ chComment.username }} -->
                                                    <div class="comment-username-item">
                                                        {{
                                                            chComment.user.username == comment.user.username
                                                                ? "楼主"
                                                                : chComment.user.username
                                                        }}
                                                    </div>

                                                    <span style="font-weight: 500; margin: 0 4px">回复:</span>
                                                    <div class="comment-username-item">
                                                        {{
                                                            chComment.toUsername == comment.user.username
                                                                ? "楼主"
                                                                : chComment.toUsername
                                                        }}
                                                    </div>
                                                </div>
                                                <div class="comment-header-right">
                                                    <div
                                                        class="comment-delete"
                                                        v-if="isSelf(chComment)"
                                                        @click="DeleteComment(chComment.id)">
                                                        删除
                                                    </div>
                                                    <div
                                                        class="comment-time"
                                                        v-text="adjustTime(chComment.createTime)"></div>
                                                </div>
                                            </div>
                                            <p class="comment-content child" v-text="chComment.content"></p>
                                            <div class="comment-actions">
                                                <div
                                                    class="action-item likes"
                                                    :class="{ liked: isCommLiked(chComment) }"
                                                    @click="likeComm(chComment)">
                                                    {{ chComment.likeCount }} 点赞
                                                </div>
                                                <div
                                                    class="action-item comm"
                                                    @click="actionItemComment(comment.id, chComment.id)">
                                                    评论
                                                </div>
                                                <div class="action-item"></div>
                                            </div>
                                            <div class="child-comment-input" :id="'child-comment-' + chComment.id">
                                                <input
                                                    type="text"
                                                    class="child-comment-textarea"
                                                    placeholder="回复楼主"
                                                    required
                                                    v-model="itemCommentText" />
                                                <button
                                                    @click="
                                                        ReleaseComment(
                                                            comment.id,
                                                            chComment.user.id,
                                                            chComment.user.username,
                                                            1
                                                        )
                                                    ">
                                                    评论
                                                </button>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                                <button
                                    class="extend-comment"
                                    @click="extendComment(comment.id)"
                                    :id="'extend-comment-' + comment.id"
                                    v-if="comment.childComment.length > 2 || getChildCommentHeight(comment.id)">
                                    展开评论
                                </button>
                            </div>
                        </div>
                    </div>
                    <div class="comment-item" style="box-shadow: none" v-if="comments.length == 0">
                        <!-- <div class="comment-userImg"><img src="../../assets/avatar.png" alt="" /></div> -->
                        <div class="comment-item-content" style="display: flex; align-items: center">
                            <div style="margin: 0 auto">暂无评论,请发表你的评论把！</div>
                        </div>
                    </div>
                </div>
            </div>
            <pagination v-model="currPage" :totalPages="totalPages" @changePage="changePage"></pagination>
        </div>
    </div>
</template>
<script setup>
import Pagination from "@/components/Pagination";
import {
    getArticleComments,
    releaseComment,
    likeComment,
    getUserLikeComment,
    deleteComment,
} from "@/api/Article/index";
import { onMounted, ref } from "vue";
import md5 from "js-md5";
import router from "@/router";
import { useRoute } from "vue-router";
import { ElMessage, ElMessageBox } from "element-plus";
const route = useRoute();
let articleId = route.params.id;
/* 获取评论 */
/* 分页查询 */
let totalPages = ref(1); //总页数
let currPage = ref(1); //当前页
let pageSize = ref(20); //分页大小
let comments = ref([]);
async function getComment() {
    await getArticleComments(currPage.value, pageSize.value, sortValue.value, articleId).then((res) => {
        comments.value = res.data.data.list;
        totalPages.value = res.data.data.pages;
        setTimeout(() => {
            if (exflag.value.length > 0) {
                for (let i = 0; i < exflag.value.length; i++) {
                    const ele = document.getElementById("comment-" + exflag.value[i]);
                    if (ele != null) {
                        ele.style.maxHeight = "none";
                    }
                }
            }
        }, 1);
    });
} /* 展开评论 */
let exflag = ref([]);
let extendCommentId = ref();
function extendComment(id) {
    extendCommentId.value = id;
    const ele = document.getElementById("comment-" + id);
    let ex = document.getElementById("extend-comment-" + id);
    if (!exflag.value.includes(id)) {
        ex.innerText = "收起评论";
        exflag.value.push(id);
        ele.style.maxHeight = "unset";
    } else {
        ex.innerText = "展开评论";
        exflag.value.pop(id);
        ele.style.maxHeight = "226px";
    }
}
function getChildCommentHeight(id) {
    let eles = document.getElementsByClassName("commentItemChild-" + id);
    for (let i = 0; i < eles.length; i++) {
        if (i >= 2) break;
    }
}
/* 点击评论区的回复 */
let showChildCommentarea = ref(false);
let aa = ref(0);
let bb = ref(0);
function actionItemComment(parentid, id) {
    const a = document.getElementsByClassName("child-comment-input");
    for (let i = 0; i < a.length; i++) {
        a[i].style.display = "none";
    }
    if (aa.value != parentid || bb.value != id) {
        aa.value = parentid;
        bb.value = id;
        showChildCommentarea.value = false;
    }

    if (id == 0) {
        const ele = document.getElementById("child-comment-" + parentid);
        if (!showChildCommentarea.value) {
            ele.style.display = "flex";
            showChildCommentarea.value = true;
        } else {
            ele.style.display = "none";
            showChildCommentarea.value = false;
        }
    } else {
        const ele = document.getElementById("child-comment-" + id);
        if (!showChildCommentarea.value) {
            ele.style.display = "flex";
            showChildCommentarea.value = true;
        } else {
            ele.style.display = "none";
            showChildCommentarea.value = false;
        }
    }
}
/* 排序 */
let sortValue = ref("new");
let sortLabel = ref("最新发布");
function sort(value) {
    if (value == "new") sortLabel.value = "最新发布";
    if (value == "hot") sortLabel.value = "浏览最多";
    if (value == "likes") sortLabel.value = "点赞最多";
    sortValue.value = value;
    router.push({
        path: "/articleView",
        query: {
            page: 1,
            search: searchData.value,
            sort: value,
        },
    });
}
/* 评论区换页操作 */
function changePage(page = 0) {
    document.querySelector("#comments").scrollIntoView({ behavior: "smooth" });
    if (totalPages.value != 0) {
        if (page != 0) {
            if (page > totalPages.value) {
                page = totalPages.value;
            }
            currPage.value = page;
            getComment();
        }
    }
}
/* 发布评论 */
let commentText = ref("");
let itemCommentText = ref("");
async function ReleaseComment(parentId = 0, toUserId = 0, toUsername = null, flag = 0) {
    let content;
    if (flag == 0) {
        content = commentText.value;
    } else {
        content = itemCommentText.value;
    }
    if (content.length == 0) {
        return;
    }
    await releaseComment({
        content: content,
        articleId: parseInt(articleId),
        parentCommentId: parentId,
        toUserId: toUserId,
        toUsername: toUsername,
    }).then((res) => {
        if (res.data.code != 1) {
            ElMessage.error({
                message: res.data.msg,
                offset: 120,
            });
        }
        commentText.value = "";
        itemCommentText.value = "";
        // exflag.value = [];
        getComment();
    });
}
/* 获取用户评论点赞 */
let userLikeComm = ref([]);
async function getUserLikeComm(comm, f) {
    if (localStorage.getItem(md5("token")) != null) {
        await getUserLikeComment(articleId).then((res) => {
            if (res.data.code == 1) {
                userLikeComm.value = res.data.data;
                if (comm != null) {
                    if (f == 0) {
                        comm.likeCount--;
                    } else {
                        comm.likeCount++;
                    }
                }
            }
        });
    }
}
/* 评论点赞 */
/* 0、点赞， 1、取消点赞 */
let csleeplike = ref(0);
function likeComm(comm) {
    if (csleeplike.value == 1) return;
    csleeplike.value = 1;
    likeComment(comm).then((res) => {
        if (res.data.code == 1) {
            getUserLikeComm(comm, res.data.data);
        } else {
            ElMessage({
                message: res.data.msg,
                type: "error",
                offset: 120,
            });
        }
        csleeplike.value = 0;
    });
}
function isCommLiked(comm) {
    for (let i = 0; i < userLikeComm.value.length; i++) {
        if (userLikeComm.value[i] == comm.id) {
            return true;
        }
    }
    return false;
}
/* 调整时间格式 */
function adjustTime(time) {
    if (time != null) return time.split("T")[1].split(".")[0] + " / " + time.split("T")[0];
    else return "00:00:00 — 2000-01-01";
}
/* 获取用户头像 */
function getUserAvatar(data) {
    if (data == null || data == "") {
        return require("@/assets/avatar.png");
    } else {
        return data;
    }
}
/* 点击头像或名字前往用户空间 */
function toUserSpace(com) {
    router.push({
        path: "/user/space/userArticle",
        query: {
            page: 1,
            uid: com.user.id,
        },
    });
}
function isSelf(com) {
    return localStorage.getItem("uid") == com.user.id;
}
function DeleteComment(cid) {
    ElMessageBox.confirm("是否确认删除?", "提示", {
        confirmButtonText: "确认",
        cancelButtonText: "取消",
        type: "warning",
    })
        .then((res) => {
            deleteComment(cid).then((res) => {
                if (res.data.code == 1) {
                    ElMessage.success({
                        message: res.data.msg,
                        offset: 120,
                    });
                    getComment();
                } else {
                    ElMessage.error({
                        message: res.data.msg,
                        offset: 120,
                    });
                }
            });
        })
        .catch((res) => {});
}
onMounted(() => {
    getComment();
    getUserLikeComm();
});
</script>
<style lang="less" scoped>
.comments {
    border-radius: 4px;
    /* box-shadow: inset 0 3px 8px 6px rgba(7, 17, 27, 0.1); */
    padding: 0 20px 20px 20px;
    transition: all 0.3s ease;
    width: 100%;
    min-width: 360px;
}
.comments:hover {
    /* box-shadow: inset 0 3px 8px 6px rgba(7, 17, 27, 0.15); */
}
.release-comment {
    display: flex;
    align-items: center;
}
.release-comment-textarea {
    min-height: 40px;
    max-height: 200px;
    flex: 1;
    border-radius: 4px;
    font-size: 16px;
    padding: 10px;
    outline: none;
    border: 2px solid #b6ddff;
}
.release-comment-textarea:focus {
    border: 2px solid var(--main-theme-color-blue);
}
.release-comment-btn {
    margin-left: 20px;
    border-radius: 4px;
    font-size: 16px;
    padding: 4px 12px;
    transition: all 0.3s ease;
    background-color: var(--main-theme-color-blue);
    border: none;
    color: #fff;
    cursor: pointer;
}
.release-comment-btn:hover {
    background-color: var(--main-theme-color-orange);
}
.comment-item-content {
    flex: 1;
}
.Comment-section {
    margin-top: 20px;
    width: 100%;
}
.comments-label {
    font-size: 24px;
    font-weight: 700;
    margin-bottom: 24px;
}
.comment-list {
    width: 100%;
}
.comment-item {
    display: flex;
    margin-bottom: 16px;
    border-radius: 4px;
    // padding: 5px;
    box-shadow: 0 3px 8px 6px rgba(7, 17, 27, 0.08);
    transition: all 0.3s ease;
    width: 100%;
    padding: 0 5px;
}
.comment-header-right {
    display: flex;
}
.comment-delete {
    color: #ff9a9a;
    margin: 0 12px;
    cursor: pointer;
    border: 1px solid #ffffff;
    padding: 0 4px;
    border-radius: 4px;
    line-height: 16px;
}
.comment-delete:hover {
    color: rgb(245, 108, 108);
    border: 1px solid #ff9a9a;
}
.comment-item.child {
    box-shadow: none;
    font-size: 14px;
    margin-bottom: 0;
}
.comment-item.child:hover {
    box-shadow: none;
}
.comment-item:hover {
    box-shadow: 0 3px 8px 6px rgba(7, 17, 27, 0.14);
}
.comment-userImg {
    margin-top: 10px;
    width: 50px;
    height: 50px;
    min-width: 50px;
    min-height: 50px;
    border-radius: 25px;
    margin-right: 10px;
    overflow: hidden;
    cursor: pointer;
}
.comment-userImg img {
    width: 100%;
    height: 100%;
    object-fit: cover;
}
.comment-header {
    width: 100%;
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: 8px;
    border-bottom: #c9c9c9 solid 1px;
}
.comment-header.child {
    padding: 4px;
}
.comment-username {
    // width: 100%;
    font-weight: 700;
    color: var(--main-theme-color-blue);
    // width: 100%;
    overflow: hidden;
}
.comment-username.child {
    // width: 100%;
    display: flex;
    flex-wrap: wrap;
}
.comment-content {
    width: 100%;
    font-size: 14px;
    // padding: 4px 10px 4px 10px;
    border-radius: 4px;
    word-break: break-all;
}
.comment-content.child {
    padding: 4px;
}
.comment-time {
    font-size: 14px;
}
.comment-userImg img {
    width: 100%;
}
.comment-actions {
    display: flex;
    padding-top: 4px;
    margin-bottom: 10px;
}
.action-item {
    margin: 0 8px;
    font-size: 12px;
    cursor: pointer;
}
.liked::before {
    content: "\e63b";
    font-family: "iconfont";
    margin-right: 4px;
    color: #ff8fbe;
}
.action-item.likes::before {
    content: "\e6f9";
    font-family: "iconfont";
    margin-right: 4px;
}
.action-item.comm::before {
    content: "\e6e5";
    font-family: "iconfont";
    margin-right: 4px;
}
.child-comment-input {
    display: none;
    align-items: center;
}
.child-comment-input button {
    margin-left: 20px;
    border-radius: 4px;
    width: 80px;
    height: 30px;
    font-size: 16px;
    padding: 0 12px;
    transition: all 0.3s ease;
    background-color: var(--main-theme-color-blue);
    border: none;
    color: #fff;
}
.child-comment-input button:hover {
    background-color: var(--main-theme-color-orange);
}
.child-comment-textarea {
    width: 100%;
    height: 14px;
    border: 1px solid #000000;
    padding: 4px;
    font-size: 14px;
    background-color: #ffffff;
    border-radius: 4px;
    outline: none;
}
.child-comments {
    background-color: #f5f5f5;
    border-radius: 4px;
    padding: 4px;
    width: 100%;
}
.child-comments-items {
    max-height: 226px;
    height: 100%;
    overflow: hidden;
    transition: all 0.3s ease;
    width: 100%;
}
.extend-comment {
    width: 100%;
    background-color: #fafafa;
    border-radius: 4px;
    border: none;
    transition: all 0.3s ease;
    z-index: 0;
}
.extend-comment:hover {
    background-color: #ffffff;
}
</style>
