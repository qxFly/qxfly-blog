//导入request.js
import request from "@/utils/request"; //@==>src

/**
 * 获得单词列表
 * @param {*} data
 * @returns
 */
export function getVocabularies(data) {
    return request({
        url: "/englishVocabulary/getVocabularies",
        method: "post",
        data,
    });
}

/**
 * 通过excel添加单词
 * @param {*} file
 * @returns
 */
export function addVocabulariesByExcel(file) {
    return request({
        url: "/englishVocabulary/addVocabulariesByExcel",
        method: "post",
        data: file,
        headers: { "Content-Type": "multipart/form-data" },
    });
}

/**
 * 添加单词
 * @param {*} file
 * @returns
 */
export function addVocabulary(data) {
    return request({
        url: "/englishVocabulary/addVocabulary",
        method: "post",
        data,
    });
}

/**
 * 修改单词
 * @param {*} data
 * @returns
 */
export function updateEnglishVocabulary(data) {
    return request({
        url: "/englishVocabulary/updateEnglishVocabulary",
        method: "post",
        data,
    });
}

/**
 * 删除单词
 * @param {*} data
 * @returns
 */
export function deleteEnglishVocabulary(id) {
    return request({
        url: "/englishVocabulary/deleteEnglishVocabulary",
        method: "get",
        params: {
            id: id,
        },
    });
}
