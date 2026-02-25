<template>
  <div class="main">
    <div class="header">
      <el-button type="primary" @click="warehouseDialog = true">入库</el-button>
      <el-button type="primary" @click="ListInventory">查询</el-button>
      <el-button type="primary" @click="GetExcel">导出Excel</el-button>
    </div>

    <el-table
      v-loading="loading"
      :data="warehouses"
      :border="true"
      show-overflow-tooltip
      stripe
      :highlight-current-row="true"
      style="width: 100%">
      <!-- <el-table-column  prop="id" label="ID" align="center" /> -->
      <el-table-column fixed prop="name" label="名称" align="center" />
      <el-table-column prop="shelf" label="货架" align="center" />
      <el-table-column prop="layer" label="层数" align="center" />
      <el-table-column prop="quantity" label="数量" align="center" />
      <el-table-column prop="" label="图片" align="center">
        <template #default="scope">
          <el-button type="primary" plain @click="viewPicture(scope.row.picture)" size="small">查看</el-button>
        </template>
      </el-table-column>
      <el-table-column prop="" label="操作" width="150" align="center">
        <template #default="scope">
          <el-button type="primary" size="small" plain @click="toEditStock(scope.row)"> 编辑 </el-button>
          <el-button type="danger" size="small" plain @click="Delete(scope.row.id)"> 删除 </el-button>
        </template>
      </el-table-column>
    </el-table>
    <div class="pagination">
      <el-pagination
        v-model:current-page="currPage"
        :page-size="pageSize"
        :pager-count="11"
        background
        layout="total, prev, pager, next, jumper"
        :total="total"
        @current-change="currentChange" />
    </div>
    <!-- 添加库存 -->
    <el-dialog
      title=""
      v-model="warehouseDialog"
      :append-to-body="true"
      :close-on-click-modal="false"
      :show-close="false"
      :lock-scroll="false"
      center
      class="warehousingDialog">
      <el-form :rules="rules" ref="formRef" :model="warehouse">
        <div class="warehousingDialog-content">
          <el-form-item prop="shelf">
            <el-input clearable type="number" min="1" v-model="warehouse.shelf">
              <template #prepend>
                <div class="label">货架</div>
              </template>
            </el-input>
          </el-form-item>
          <el-form-item prop="layer">
            <el-input clearable type="number" min="1" v-model="warehouse.layer">
              <template #prepend>
                <div class="label">层数</div>
              </template>
            </el-input>
          </el-form-item>
          <el-form-item prop="name">
            <el-input clearable rows="2" v-model="warehouse.name">
              <template #prepend>
                <div class="label">货物名称</div>
              </template>
            </el-input>
          </el-form-item>
          <el-form-item prop="quantity">
            <el-input clearable type="number" min="0" v-model="warehouse.quantity">
              <template #prepend>
                <div class="label">数量</div>
              </template>
            </el-input>
          </el-form-item>

          <el-form-item prop="picture">
            <el-upload
              ref="uploadRef"
              class="upload-demo"
              :action="UploadURL"
              :limit="1"
              :on-progress="handleProgress"
              :on-success="handleSuccess"
              :on-error="handleError">
              <template #trigger>
                <el-button type="primary">选择图片</el-button>
              </template>
            </el-upload>
          </el-form-item>
        </div>
        <div style="height: 30px; margin-top: 10px">
          <div v-if="isTip" style="text-align: center; font-size: 18px; font-weight: 700; color: #846cb6">
            {{ tip }}
          </div>
        </div>
        <div class="dialog-footer">
          <el-button @click="cencel">取 消</el-button>
          <el-button type="primary" @click="EditStock" v-if="editMode">编 辑</el-button>
          <el-button type="primary" @click="Warehousing" v-else>确 定</el-button>
        </div>
      </el-form>
    </el-dialog>
    <!-- 图片预览窗 -->
    <el-dialog
      v-model="viewPictureDialogVisible"
      width="100%"
      center
      :append-to-body="true"
      :show-close="false"
      :lock-scroll="false"
      :destroy-on-close="true"
      :align-center="true">
      <el-image :src="picture" fit="contain" />
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="viewPictureDialogVisible = false" type="primary">关 闭</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import router from "@/router";
import { onMounted, reactive, ref, watch } from "vue";
import { warehousing, listInventory, deleteStock, getExcel, editWarehouse } from "@/api/WorkSpace";
import { ElMessage, ElMessageBox } from "element-plus";
import { useRoute } from "vue-router";
let useRouter = useRoute();
let loading = ref(false);
let warehouse = ref({});
let warehouseDialog = ref(false);
let isTip = ref(false);
let tip = ref("");
let warehouses = ref();
let currPage = ref(1);
let pageSize = ref(10);
let total = ref(0);
let searchData = ref({
  shelf: null,
  layer: null,
  name: null,
});
/**
 * 分页获取库存
 */
function ListInventory() {
  listInventory({
    currPage: currPage.value,
    pageSize: pageSize.value,
    shelf: searchData.value.shelf,
    layer: searchData.value.layer,
    name: searchData.value.name,
  }).then((res) => {
    if (res.data.code != 1) return;
    warehouses.value = res.data.data.list;
    total.value = res.data.data.total;
  });
}
/**
 * 入库
 */
async function Warehousing() {
  if (isUpload.value) {
    ElMessage.error("图片上传中");
    return;
  }
  formRef.value.validate(async (valid) => {
    if (valid) {
      await UploadPicture();
      console.log(warehouse.value);

      warehousing(warehouse.value).then((res) => {
        if (res.data.code == 0) {
          console.log("error");
        } else {
          ListInventory();
          warehouseDialog.value = false;
        }
      });
    }
  });
}
let formRef = ref(null);
const rules = ref({
  name: [{ required: true, message: "请输入货物名称", trigger: "blur" }],
  shelf: [{ required: true, message: "请输入货架", trigger: "blur" }],
  layer: [{ required: true, message: "请输入层数", trigger: "blur" }],
  quantity: [{ required: true, message: "请输入数量", trigger: "blur" }],
});

/**
 * 上传图片
 */
let uploadRef = ref(null);
let UploadURL = ref(process.env.VUE_APP_BASE_URL + "/warehouse/uploadPicture");
let isUpload = ref(false);
async function UploadPicture() {
  uploadRef.value.submit();
}
function handleProgress() {
  isUpload.value = true;
}

function handleSuccess(e) {
  console.log(e.data.data);
  warehouse.value.picture = e.data;

  isUpload.value = false;
}
function handleError() {
  isUpload.value = false;
}
/**
 * 编辑库存
 * @param data
 */
let editMode = ref(false);
function toEditStock(data) {
  editMode.value = true;
  warehouse.value = Object.assign({}, data);
  warehouseDialog.value = true;
}

function EditStock() {
  formRef.value.validate((valid) => {
    if (valid) {
      editWarehouse(warehouse.value).then((res) => {
        if (res.data.code == 0) {
          console.log("error");
        } else {
          editMode.value = false;

          ListInventory();
          warehouseDialog.value = false;
        }
      });
    }
  });
}
/**
 * 删除
 * @param id id
 */
function Delete(id) {
  ElMessageBox.confirm("确定删除吗？", "提示", {
    confirmButtonText: "确定",
    cancelButtonText: "取消",
    type: "warning",
  })
    .then(() => {
      deleteStock({ id: id }).then((res) => {
        if (res.data.code != 1) {
          ElMessage.error("删除失败");
          return;
        }
        ListInventory();
      });
    })
    .catch((e) => {});
}

/* 换页操作 */
let currentChange = (page) => {
  currPage.value = page;
  router.push({
    path: "/warehouse",
    query: { page: page },
  });
};
/* 路由后还原数据 */
function setSearchData() {
  if (useRouter.query.page != null) {
    currPage.value = parseInt(useRouter.query.page);
  }
}
/* 路由分页 */
let routePage = ref(useRouter);
watch(
  routePage.value,
  (newVal, oldVal) => {
    ListInventory();
  },
  {
    deep: true,
  }
);
/**
 * 查看图片
 * @param row 数据
 */
let picture = ref();
let viewPictureDialogVisible = ref(false);
function viewPicture(row) {
  viewPictureDialogVisible.value = true;
  picture.value = row;
}
/**
 * 取消弹窗
 */
function cencel() {
  warehouseDialog.value = false;
  warehouse.value = {};
  editMode.value = false;
}
/**
 * 导出Excel
 */
function GetExcel() {
  getExcel().then((res) => {
    let a = document.createElement("a");
    a.download = "库存列表.xlsx";
    a.style.display = "none";
    const blob = new Blob([res.data], { ype: "application/x-msdownload" });
    a.href = URL.createObjectURL(blob);
    document.body.appendChild(a);
    a.click();
    document.body.removeChild(a);
  });
}

onMounted(() => {
  setSearchData();
  ListInventory();
});
</script>

<style>
.main {
  margin-top: 70px;
}
.warehousingDialog {
  width: 100%;
}
.header {
  display: flex;
  justify-content: center;
  align-items: center;
  margin: 20px;
}
.pagination {
  display: flex;
  justify-content: center;
  align-items: center;
}
</style>
