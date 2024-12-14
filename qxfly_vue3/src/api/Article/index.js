import request from "@/utils/request";

/**
 * 发布文章
 * @param {*} data
 * @returns
 */
export function releaseArticle(data) {
    return request({
        url: "/article/releaseArticle",
        method: "post",
        data: data,
    });
}

/**
 * 编辑文章
 * @param {*} data
 * @returns
 */
export function editArticle(data) {
    return request({
        url: "/article/editArticle",
        method: "post",
        data: data,
    });
}

/**
 * 上传文章封面
 * @param {*} data
 * @returns
 */
export function updateArticleCover(data) {
    return request({
        url: "/article/updateArticleCover",
        method: "post",
        data: data,
        headers: { "Content-Type": "multipart/form-data" },
    });
}
/**
 * 上传文章封面
 * @param {*} data
 * @returns
 */
export function uploadArticleImage(data) {
    return request({
        url: "/article/uploadArticleImage",
        method: "post",
        data: data,
        headers: { "Content-Type": "multipart/form-data" },
    });
}
/**
 * 分页获取文章
 * @param {*} data
 * @returns
 */
export function getArticles(currPage, pageSize, searchData, sort, authorId, daily, verify, classify, tag) {
    return request({
        url: "/article/getArticles",
        method: "get",
        params: {
            currPage: currPage,
            pageSize: pageSize,
            searchData: searchData,
            sort: sort,
            authorId: authorId,
            daily: daily,
            verify: verify,
            classify: classify,
            tag: tag,
        },
    });
}
/**
 * 分页获取收藏文章
 * @param {*} data
 * @returns
 */
export function getCollectionArticles(currPage, pageSize, searchData, sort, uid) {
    return request({
        url: "/article/getCollectionArticles",
        method: "get",
        params: {
            currPage: currPage,
            pageSize: pageSize,
            searchData: searchData,
            sort: sort,
            uid: uid,
        },
    });
}

/**
 * 根据id获取文章
 * @param {*} id
 * @returns
 */
export function getArticleById(id) {
    return request({
        url: "/article/getArticleById",
        method: "get",
        params: { id: id },
    });
}

/* 删除文章 */
export function deleteArticle(data) {
    return request({
        url: "/article/deleteArticle",
        method: "post",
        data: data,
    });
}
/* 批量删除文章 */
export function batchDeleteArticle(data) {
    return request({
        url: "/article/batchDeleteArticle",
        method: "post",
        data: data,
    });
}
/* 获取文章评论 */
export function getArticleComments(currPage, pageSize, sort, id) {
    return request({
        url: "/article/getArticleComments",
        method: "get",
        params: { currPage: currPage, pageSize: pageSize, sort, sort, id: id },
    });
}

/* 发布评论 */
export function releaseComment(data) {
    return request({
        url: "/article/releaseComment",
        method: "post",
        data: data,
    });
}
/* 检测用户章是否可编辑文章 */
export function checkA() {
    return request({
        url: "/article/checkA",
        method: "post",
    });
}
/* 检测用户章是否可编辑文章 */
export function addArticleView(articleId) {
    return request({
        url: "/article/addArticleView",
        method: "post",
        data: articleId,
    });
}
/* 文章点赞 */
export function articleLike(articleId) {
    return request({
        url: "/article/articleLike",
        method: "post",
        data: articleId,
    });
}
/* 判断文章是否点赞删除 */
export function isArticleLike(articleId) {
    return request({
        url: "/article/isArticleLike",
        method: "post",
        data: articleId,
    });
}
/* 文章收藏 */
export function articleCollection(articleId) {
    return request({
        url: "/article/articleCollection",
        method: "post",
        data: articleId,
    });
}
/* 文章收藏 */
export function deleteArticleImage(imageList) {
    return request({
        url: "/article/deleteArticleImage",
        method: "get",
        params: { imageList: imageList },
    });
}
/* 评论点赞 */
export function likeComment(comm) {
    return request({
        url: "/article/likeComment",
        method: "post",
        data: comm,
    });
}
/* 获取用户该文章评论点赞 */
export function getUserLikeComment(id) {
    return request({
        url: "/article/getUserLikeComment",
        method: "get",
        params: {
            aid: id,
        },
    });
}

/* 获取分类 */
export function getClassifies(currPage, pageSize, id, name) {
    return request({
        url: "/article/getClassifies",
        method: "get",
        params: {
            currPage: currPage,
            pageSize: pageSize,
            id: id,
            name: name,
        },
    });
}
/* 删除分类 */
export function deleteClassify(id) {
    return request({
        url: "/manage/deleteClassify",
        method: "post",
        data: id,
    });
}
/* 添加分类 */
export function addClassify(data) {
    return request({
        url: "/manage/addClassify",
        method: "post",
        data: data,
    });
}
/* 修改分类 */
export function updateClassify(data) {
    return request({
        url: "/manage/updateClassify",
        method: "post",
        data: data,
    });
}
/* 获取标签 */
export function getTags(currPage, pageSize, id, name, uid) {
    return request({
        url: "/article/getTags",
        method: "get",
        params: {
            currPage: currPage,
            pageSize: pageSize,
            id: id,
            name: name,
            uid: uid,
        },
    });
}
/* 删除标签 */
export function deleteTag(id) {
    return request({
        url: "/manage/deleteTag",
        method: "post",
        data: id,
    });
}
/* 添加标签 */
export function addTag(data) {
    return request({
        url: "/manage/addTag",
        method: "post",
        data: data,
    });
}
/* 修改标签 */
export function updateTag(data) {
    return request({
        url: "/manage/updateTag",
        method: "post",
        data: data,
    });
}

/* 附件上传和删除在@/views/Article/EditArticle.vue 中*/

/* 保存附件*/
export function saveAttachment(data) {
    return request({
        url: "/article/saveAttachment",
        method: "post",
        data: data,
    });
}

/**
 * 根据id获取文章附件
 * @param {*} id
 * @returns
 */
export function getArticleAttachment(id) {
    return request({
        url: "/article/getArticleAttachment",
        method: "get",
        params: { id: id },
    });
}

/**
 * 根据id获取文章附件
 * @param {*} id
 * @returns
 */
export function deleteAttachment(aid, fileName) {
    return request({
        url: "/article/deleteAttachment",
        method: "get",
        params: { aid: aid, fileName: fileName },
    });
}
