<template>
  <div class="sider-hot-resource">
    <ul class="resource">
      <el-scrollbar max-height="200px">
        <li class="resource-title">
          资源合集<span>( 功能未完善，请前往 <a href="/File" target="_blank">下载页 </a>)</span>
        </li>
        <hr />
        <li>
          <ul class="resource-content">
            <li v-for="item in fileList" :key="item.fileMd5Name">
              <a :href="item.filePath" :download="item.fileName">{{ item.fileName }}</a>
              <hr style="margin: 4px" />
            </li>
          </ul>
        </li>
      </el-scrollbar>
    </ul>
  </div>
</template>
<script setup>
import { listFile, upload } from "@/api/index";
import { onMounted, onUnmounted, ref } from "vue";

var formData = new FormData();
var fileListPage = ref([]);
var fileList = ref([]);
var fileListTemp = ref([]);

/* 获取文件 */
function getFile(event) {
  let file = event.target.files[0];
  formData.append("file", file);
}
/* 列出文件 */
async function listfile() {
  const res = await listFile();
  fileListTemp.value = res.data.data;
  for (var i = 0; i < fileListTemp.value.length; i++) {
    // console.log(fileListTemp.value.fileRealName);
    if (fileListTemp.value[i].fileRealName == null) {
      fileList.value.push(fileListTemp.value[i]);
      // console.log("fileList: " + i + JSON.stringify(fileList.value[i]));
    } else {
      fileListPage.value.push(fileListTemp.value[i]);
      // console.log("fileListPage: " + i + JSON.stringify(fileListPage.value[i]));
    }
    // console.log(i + JSON.stringify(fileListTemp.value[i]));
  }
  // console.log(" res.data.data:" + JSON.stringify(res.data.data));
}
/* 下载文件 */
function downloadFile(url, fileName) {
  // var x = new XMLHttpRequest();
  // x.open("GET", url, true);
  // x.responseType = "blob";
  // x.onload = function () {
  //   var url = window.URL.createObjectURL(x.response);
  //   var a = document.createElement("a");
  //   a.href = url;
  //   a.download = fileName;
  //   a.click();
  // };
  // x.send();

  const link = document.createElement("a");
  fetch(url)
    .then((res) => res.blob())
    .then((blob) => {
      link.href = URL.createObjectURL(blob);
      link.download = fileName;
      link.target = "_blank";
      document.body.appendChild(link);
      link.click();
      link.remove();
    });
}

/* 上传文件 */
async function Upload() {
  const res = await upload(formData);
  formData = new FormData();
  listfile();
}
function Listener() {
  const leftSidebarMain = document.getElementById("left-Sidebar-main");
  var scrollTop = document.documentElement.scrollTop || document.body.scrollTop;
  if (scrollTop > 600) {
    leftSidebarMain.style.top = "40px";
  } else {
    leftSidebarMain.style.top = "70px";
  }
}
onMounted(() => {
  listfile();
  window.addEventListener("scroll", Listener);
});
onUnmounted(() => {
  window.removeEventListener("scroll", Listener, false);
});
// data() {
//   return {
//     formData: new FormData(),
//     fileList: [],
//   };
// },
// methods: {
// /* 获取文件 */
// getFile(event) {
//   let file = event.target.files[0];
//   this.formData.append("file", file);
// },
// /* 列出文件 */
// async listfile() {
//   const res = await listFile();
//   this.fileList = res.data.data;
//   console.log(this.fileList);
// },
// /* 下载文件 */
// downloadFile(url, fileName) {
//   var x = new XMLHttpRequest();
//   x.open("GET", url, true);
//   x.responseType = "blob";
//   x.onload = function () {
//     var url = window.URL.createObjectURL(x.response);
//     var a = document.createElement("a");
//     a.href = url;
//     a.download = fileName;
//     a.click();
//   };
//   x.send();
// },
// /* 上传文件 */
// async upload() {
//   const res = await upload(this.formData);
//   this.formData = new FormData();
//   this.listfile();
// },
// islogin() {
//   return window.localStorage.getItem(this.$md5("islogin"));
// },
</script>
<style scoped lang="less">
.resource {
  padding: 0px 10px;
  height: 100%;
  border-radius: 4px;
  background-color: transparent;
  box-shadow: 0 3px 8px 6px rgba(7, 17, 27, 0.08);
  transition: all 0.3s ease;
  overflow: hidden;
}
.resource:hover {
  box-shadow: 0 3px 8px 6px rgba(7, 17, 27, 0.14);
}
/* 左侧栏资源列表标题 */
.resource-title {
  font-weight: 600;
  font-size: 18px;
  margin: 10px 0 5px 0;
}

/* 左侧栏资源列表内容 */
.resource-content {
  margin: 0;
  padding: 0;
}

.resource-content li {
  margin: 10px 0;
  padding: 0;
  overflow: hidden;
  user-select: none;
}

/* 左侧栏资源列表字体 */
.resource {
  font-size: 1rem;
}

.resource-content li a {
  padding: 3px;
  display: inline-block;
  width: 95%;
}

.resource-content li a:hover {
  background-color: #b89ad7;
  border-radius: 3px;
}

.listfile {
  margin: 5px;
}
.resource a:link {
  color: #000000;
}

.resource a:visited {
  color: #000000;
}

.resource a:hover {
  color: #000000;
}
</style>
<style lang="less">
:deep(.el-scrollbar__bar.is-horizontal) {
  height: 0 !important;
}
</style>

<style scoped>
.resource-title span {
  display: block;
  font-size: 10px;
  color: rgb(0, 0, 0);
  margin: 0;
  padding: 0;
}

.resource-title span a:link {
  font-size: 10px;
  margin: 0;
  padding: 0;
  color: rgb(255, 144, 144);
}

.resource-title span a:visited {
  font-size: 10px;
  margin: 0;
  padding: 0;
  color: rgb(255, 144, 144);
}
</style>
