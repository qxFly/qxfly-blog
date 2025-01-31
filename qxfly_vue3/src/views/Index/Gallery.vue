<template>
    <div class="main">
        <div class="item">
            <el-button class="item-btn" type="primary" @click="getToken">获取token</el-button>
            <el-button class="item-btn" type="primary" @click="upload">上传</el-button>
            <el-button class="item-btn" type="primary" @click="getUploadFair">获取上传失败列表</el-button>
        </div>
        <div class="item">
            <el-upload
                v-model:file-list="fileList"
                :multiple="true"
                class="item-btn"
                action="https://run.mocky.io/v3/9d059bf9-4660-45f2-925d-ce80ad6c4d15"
                :on-change="handleChange"
                :show-file-list="true"
                :auto-upload="false">
                <el-button type="primary">选择文件</el-button>
                <template #tip>
                    <div class="el-upload__tip">jpg/png files with a size less than 500kb</div>
                </template>
            </el-upload>
        </div>
    </div>
</template>

<script setup>
import { onMounted, onUnmounted, ref } from "vue";
import router from "@/router";
import axios from "axios";
import { ElMessage, ElMessageBox } from "element-plus";
/* 获取token */
let token = ref("");
function getToken() {
    axios({
        method: "post",
        url: "http://38.55.199.233:40027/api/v1/tokens",
        headers: {
            Accept: "application/json",
        },
        data: {
            email: "2541536499@qq.com",
            password: "fly334955",
        },
    }).then((res) => {
        if (res.data.status) {
            token.value = res.data.data.token;
            ElMessage.success("获取成功");
        }
    });
}
let fileList = ref([]);
let uploadFair = ref([]);
function handleChange(e) {}

function upload() {
    let i = 0;
    let interval = setInterval(() => {
        if (i > fileList.value.length) {
            clearInterval(interval);
            return;
        }
        let file = fileList.value[i++];
        if (file == null) return;
        axios({
            method: "post",
            url: "http://38.55.199.233:40027/api/v1/upload",
            headers: {
                Authorization: "Bearer 2|cKXu8RWZYXU1FOOujUi2Nl7UjyyYVw7FX3vx0Hq0",
                "Content-Type": "multipart/form-data",
            },
            data: {
                file: file.raw,
                strategy_id: 1,
            },
        }).then((res) => {
            if (res.data.status) {
                ElMessage.success("上传成功:" + res.data.data.origin_name);
            } else {
                uploadFair.value.push(file);
            }
        });
    }, 1500);
}

/* 获取上传失败列表 */
function getUploadFair() {
    console.log("uploadFair:", uploadFair.value);
}
onMounted(() => {});
onUnmounted(() => {});
</script>

<style scoped>
.main {
    width: 100%;
    padding-top: 100px;
    display: flex;
    justify-content: center;
    align-items: center;
    flex-direction: column;
}
.item {
    margin: 10px 0;
}
.item-btn {
    margin: 0 10px;
}
</style>
