import request from "@/utils/request";
/**
 * 获取系统设置
 * @returns
 */
export function getSysSetting() {
    return request({
        url: "/sysSetting/getSysSettings",
        method: "post",
    });
}
