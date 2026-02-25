<template>
  <el-container class="main-container">
    <el-header class="header">
      <div class="search-bar">
        <div class="search-items">
          <el-input
            class="search-item"
            v-model="searchData.applicant"
            placeholder="申请人"
            clearable
            style="width: 160px"></el-input>
          <el-input
            class="search-item"
            v-model="searchData.month"
            placeholder="申请年月"
            clearable
            style="width: 160px"></el-input>
          <el-input
            class="search-item"
            v-model="searchData.reporting"
            placeholder="待提报"
            clearable
            style="width: 160px"></el-input>
          <el-input
            class="search-item"
            v-model="searchData.reported"
            placeholder="已提报"
            clearable
            style="width: 160px"></el-input>
          <el-button type="primary" @click="ListPurchases()">筛选</el-button>
          <el-button @click="clearAll" style="margin: 0">清空筛选</el-button>
          <el-button type="primary" @click="toAddPurchase">添加采购</el-button>
        </div>
      </div>
      <div class="menu">
        <el-menu :default-active="activeIndex" class="el-menu" mode="horizontal" @select="handleSelect">
          <el-menu-item index="1" @click="changeMenu(null)">全部</el-menu-item>
          <el-menu-item index="2" @click="changeMenu(0)">待提报</el-menu-item>
          <el-menu-item index="3" @click="changeMenu(1)">申请资产号</el-menu-item>
          <el-menu-item index="4" @click="changeMenu(2)">部分提报</el-menu-item>
          <el-menu-item index="5" @click="changeMenu(3)">提报审批中</el-menu-item>
          <el-menu-item index="6" @click="changeMenu(4)">已提报</el-menu-item>
          <el-menu-item index="7" @click="changeMenu(5)">已发放</el-menu-item>
        </el-menu>
      </div>
    </el-header>
    <el-main class="main">
      <el-table
        v-loading="loading"
        :data="purchases"
        :border="true"
        show-overflow-tooltip
        stripe
        :highlight-current-row="true"
        style="width: 100%">
        <el-table-column fixed prop="applicant" label="申请人" align="center" width="120" />
        <el-table-column fixed prop="month" label="申请年月" align="center" width="100" />
        <el-table-column prop="status" label="状态" align="center" width="110">
          <template #default="scope">
            <el-tag type="danger" v-if="scope.row.status == 0">待提报</el-tag>
            <el-tag type="info" v-if="scope.row.status == 1">申请资产号</el-tag>
            <el-tag type="warning" v-if="scope.row.status == 2">部分提报</el-tag>
            <el-tag type="primary" v-if="scope.row.status == 3">提报审批中</el-tag>
            <el-tag type="primary" v-if="scope.row.status == 4">已提报</el-tag>
            <el-tag type="success" v-if="scope.row.status == 5">已发放</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="" label="图片" align="center" width="400">
          <template #default="scope">
            <!-- <el-button type="primary" plain @click="viewPicture(scope.row.picture)" size="small">查看</el-button> -->
            <el-image
              :z-index="9999999"
              :src="scope.row.picture"
              :preview-src-list="srcList"
              :initial-index="getPicIndex(scope.row.picture)"
              fit="contain"
              :hide-on-click-modal="true"
              :preview-teleported="true" />
          </template>
        </el-table-column>
        <el-table-column prop="reporting" label="未提报" align="center" />
        <el-table-column prop="reported" label="已提报" align="center" />

        <el-table-column prop="" label="操作" width="150" align="center">
          <template #default="scope">
            <el-button type="primary" size="small" plain @click="toEdit(scope.row)"> 编辑 </el-button>
            <el-button type="danger" size="small" plain @click="Delete(scope.row.id)"> 删除 </el-button>
          </template>
        </el-table-column>
      </el-table>
      <div class="pagination">
        <el-pagination
          v-model:current-page="currPage"
          :page-size="pageSize"
          :pager-count="11"
          :page-sizes="[20, 30, 50, 100]"
          background
          layout="total, sizes, prev, pager, next, jumper"
          :total="total"
          @current-change="currentChange"
          @size-change="handleSizeChange" />
      </div>
      <!-- 添加库存 -->
      <el-dialog
        title=""
        v-model="Dialog"
        :append-to-body="true"
        :close-on-click-modal="true"
        :show-close="false"
        :lock-scroll="false"
        draggable
        center
        class="warehousingDialog">
        <el-form :rules="rules" ref="formRef" :model="purchase">
          <div class="warehousingDialog-content">
            <el-form-item prop="applicant">
              <el-input clearable type="text" v-model="purchase.applicant">
                <template #prepend>
                  <div class="label">申请人</div>
                </template>
              </el-input>
            </el-form-item>
            <el-form-item prop="month">
              <el-input clearable type="text" v-model="purchase.month">
                <template #prepend>
                  <div class="label">申请年月</div>
                </template>
              </el-input>
            </el-form-item>
            <el-form-item>
              <div style="width: 100%; display: flex">
                <el-input clearable style="width: min-content">
                  <template #prepend>
                    <div class="label">待提报部分</div>
                  </template>
                </el-input>
                <el-input
                  clearable
                  type="textarea"
                  rows="5"
                  v-model="purchase.reporting"
                  style="width: 100%; left: -22px">
                </el-input>
              </div>
            </el-form-item>
            <el-form-item>
              <div style="width: 100%; display: flex">
                <el-input clearable style="width: min-content">
                  <template #prepend>
                    <div class="label">已提报部分</div>
                  </template>
                </el-input>
                <el-input
                  clearable
                  type="textarea"
                  rows="5"
                  v-model="purchase.reported"
                  style="width: 100%; left: -22px">
                </el-input>
              </div>
            </el-form-item>
            <el-form-item prop="status">
              <div class="update-dialog-item">
                <el-input style="width: 100px">
                  <template #prepend>
                    <div class="label">状态</div>
                  </template>
                </el-input>
                <el-select class="search-item" v-model="purchase.status" style="width: 160px" placeholder="状态">
                  <el-option :value="0" label="待提报">待提报</el-option>
                  <el-option :value="1" label="申请资产号">申请资产号</el-option>
                  <el-option :value="2" label="部分提报">部分提报</el-option>
                  <el-option :value="3" label="提报审批中">提报审批中</el-option>
                  <el-option :value="4" label="已提报">已提报</el-option>
                  <el-option :value="5" label="已发放">已发放</el-option>
                </el-select>
              </div>
            </el-form-item>

            <el-form-item prop="picture">
              <el-upload
                ref="uploadRef"
                class="upload-demo"
                :action="UploadURL"
                :limit="1"
                :on-preview="handlePreview"
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
            <el-button type="primary" @click="EditPurchase" v-if="editMode">编 辑</el-button>
            <el-button type="primary" @click="addPurchase" v-else>确 定</el-button>
          </div>
        </el-form>
      </el-dialog>
      <!-- 图片预览窗 -->
      <el-dialog
        v-model="viewPictureDialogVisible"
        width="40%"
        center
        :append-to-body="true"
        :show-close="false"
        :lock-scroll="false"
        :destroy-on-close="true"
        :align-center="true">
        <el-image :preview-src-list="srcList" :src="picture" fit="contain" />
        <template #footer>
          <span class="dialog-footer">
            <el-button @click="viewPictureDialogVisible = false" type="primary">关 闭</el-button>
          </span>
        </template>
      </el-dialog>
    </el-main>
  </el-container>
</template>

<script setup>
import router from "@/router";
import { onMounted, reactive, ref, watch } from "vue";
import { addItem, listItems, deleteItem, editItem } from "@/api/WorkSpace";
import { ElMessage, ElMessageBox } from "element-plus";
import { useRoute } from "vue-router";
let useRouter = useRoute();
let loading = ref(false);
let purchase = ref({});
let Dialog = ref(false);
let isTip = ref(false);
let tip = ref("");
let purchases = ref();
let currPage = ref(1);
let pageSize = ref(20);
let total = ref(0);
let searchData = ref({
  applicant: null,
  reporting: null,
  reported: null,
  picture: null,
  status: null,
  month: null,
});
let srcList = ref([]);
const activeIndex = ref("1");
const handleSelect = (key, keyPath) => {
  activeIndex.value = key;
};
function clearAll() {
  searchData.value = {
    applicant: null,
    reporting: null,
    reported: null,
    picture: null,
    status: null,
    month: null,
  };
  activeIndex.value = "1";
  ListPurchases();
}
/**
 * 分页获取库存
 */
function changeMenu(status) {
  if (status == null) {
    searchData.value.status = null;
  } else if (status != -1) {
    searchData.value.status = status;
  }
  ListPurchases();
}
function ListPurchases() {
  listItems({
    currPage: currPage.value,
    pageSize: pageSize.value,
    applicant: searchData.value.applicant,
    reported: searchData.value.reported,
    reporting: searchData.value.reporting,
    picture: searchData.value.picture,
    status: searchData.value.status,
    month: searchData.value.month,
  }).then((res) => {
    if (res.data.code != 1) return;
    purchases.value = res.data.data.list;
    srcList.value = [];
    total.value = res.data.data.total;
    for (let i = 0; i < purchases.value.length; i++) {
      srcList.value.push(purchases.value[i].picture);
    }
  });
}

function getPicIndex(pic) {
  for (let i = 0; i < srcList.value.length; i++) {
    if (srcList.value[i] == pic) {
      return i;
    }
  }
}
/**
 * 入库
 */
function toAddPurchase() {
  Dialog.value = true;
  let t = new Date();
  purchase.value.month = t.getFullYear() + "-" + String(t.getMonth() + 1).padStart(2, "0");
}
async function addPurchase() {
  if (isUpload.value) {
    ElMessage.error("图片上传中");
    return;
  }
  formRef.value.validate(async (valid) => {
    if (valid) {
      await UploadPicture();
      addItem(purchase.value).then((res) => {
        if (res.data.code == 0) {
          console.log("error");
        } else {
          Dialog.value = false;
          ListPurchases();
          uploadRef.value.clearFiles();
        }
      });
    }
  });
}
let formRef = ref(null);
const rules = ref({
  applicant: [{ required: true, message: "请输入申请人", trigger: "blur" }],
  month: [{ required: true, message: "请输入月份", trigger: "blur" }],
  status: [{ required: true, message: "请输入状态", trigger: "blur" }],
});

/**
 * 上传图片
 */
let uploadRef = ref(null);
let UploadURL = ref(process.env.VUE_APP_BASE_URL + "/purchase/uploadPicture");
let isUpload = ref(false);
async function UploadPicture() {
  uploadRef.value.submit();
}
function handleProgress() {
  isUpload.value = true;
}

function handleSuccess(e) {
  purchase.value.picture = e.data;
  isUpload.value = false;
}
function handleError() {
  isUpload.value = false;
}
function handlePreview(file) {}
/**
 * 编辑库存
 * @param data
 */
let editMode = ref(false);
function toEdit(data) {
  editMode.value = true;
  purchase.value = Object.assign({}, data);
  Dialog.value = true;
}

async function EditPurchase() {
  formRef.value.validate(async (valid) => {
    if (valid) {
      if (uploadRef.value != null) await UploadPicture();
      editItem(purchase.value).then((res) => {
        if (res.data.code == 0) {
          console.log("error");
        } else {
          editMode.value = false;
          ListPurchases();
          Dialog.value = false;
          uploadRef.value.clearFiles();
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
      deleteItem({ id: id }).then((res) => {
        if (res.data.code != 1) {
          ElMessage.error("删除失败");
          return;
        }
        ListPurchases();
      });
    })
    .catch((e) => {});
}
/* 换页操作 */
let currentChange = (page) => {
  currPage.value = page;
  router.push({
    path: "/manage/purchase",
    query: { page: page },
  });
};
let handleSizeChange = (size) => {
  pageSize.value = size;
  router.push({
    path: "/manage/purchase",
    query: { page: currPage.value, pageSize: size },
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
    ListPurchases();
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
  Dialog.value = false;
  purchase.value = {};
  editMode.value = false;
}
/**
 * 导出Excel
 */
function GetExcel() {
  getExcel().then((res) => {
    console.log(res.data);
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
  ListPurchases();
});
</script>

<style scoped>
.warehousingDialog {
  width: 50%;
}
.update-dialog-item {
  display: flex;
}
.label {
  font-weight: 700;
  width: 60px;
  text-align: center;
}
.label-r {
  font-weight: 700;
  width: 60px;
  text-align: center;
  margin-bottom: 10px;
}
.search-items {
  display: flex;
  gap: 10px;
  margin: 4px;
}
.search-item {
  min-width: 50px;
}
.main-container {
  margin-top: 20px;
}
.header {
  height: max-content;
}
.dialog-footer {
  display: flex;
  align-items: center;
  justify-content: center;
}
.pagination {
  display: flex;
  align-items: center;
  justify-content: center;
  margin-top: 10px;
  margin-right: 100px;
}
</style>
