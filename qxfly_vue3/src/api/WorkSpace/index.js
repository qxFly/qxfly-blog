//导入request.js
import request from "@/utils/request"; //@==>src

/**
 * 获取库存列表
 * @returns
 */
export function listInventory(data) {
  return request({
    url: "/warehouse/listInventory",
    method: "get",
    params: data,
  });
}
/**
 * 导出excel
 * @returns
 */
export function getExcel() {
  return request({
    responseType: "blob",
    url: "/warehouse/getExcel",
    method: "get",
  });
}

/**
 * 入库
 * @returns
 */
export function warehousing(data) {
  return request({
    url: "/warehouse/warehousing",
    method: "post",
    data: data,
  });
}

/**
 * 编辑
 * @returns
 */
export function editWarehouse(data) {
  return request({
    url: "/warehouse/editWarehouse",
    method: "post",
    data: data,
  });
}
/**
 * 删除
 * @returns
 */
export function deleteStock(id) {
  return request({
    url: "/warehouse/deleteStock",
    method: "post",
    data: id,
  });
}

/**
 * 获取采购列表
 * @returns
 */
export function listItems(data) {
  return request({
    url: "/purchase/listItems",
    method: "get",
    params: data,
  });
}

/**
 * 添加项目
 * @returns
 */
export function addItem(data) {
  return request({
    url: "/purchase/addItem",
    method: "post",
    data: data,
  });
}

/**
 * 编辑
 * @returns
 */
export function editItem(data) {
  return request({
    url: "/purchase/editItem",
    method: "post",
    data: data,
  });
}
/**
 * 删除
 * @returns
 */
export function deleteItem(id) {
  return request({
    url: "/purchase/deleteItem",
    method: "post",
    data: id,
  });
}
