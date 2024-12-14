//导入request.js
import request from "@/utils/request"; //@==>src

/**
 * 获取一言单词
 * @returns
 */
export function getWord(single, text) {
    return request({
        url: "/saying/getWord",
        method: "get",
        params: { single: single, text: text },
    });
}

/**
 * 添加一言单词
 * @returns
 */
export function addWord(data) {
    return request({
        url: "/saying/addWord",
        method: "post",
        data: data,
    });
}
