<template>
    <div class="main">
        <div class="user-info-main">
            <div class="user-info-from">
                <div class="user-info-content">
                    <div class="col">
                        <div class="user-info-item">
                            <div class="label">ID:</div>
                            <div class="edit-id">
                                <input type="text" v-model="userInfo.id" disabled />
                            </div>
                        </div>
                        <div class="user-info-item">
                            <div class="label">名称</div>
                            <div class="edit-name">
                                <input type="text" v-model="userInfo.username" maxlength="20" />
                            </div>
                        </div>
                        <div class="user-info-item">
                            <div class="label">简介</div>
                            <div class="edit-introduction">
                                <textarea
                                    type="text"
                                    v-model="userInfo.introduction"
                                    style="min-height: 18px; min-width: 160px"></textarea>
                            </div>
                        </div>
                        <div class="user-info-item">
                            <div class="label">邮箱</div>
                            <div class="edit-email">
                                <input type="text" v-model="userInfo.email" maxlength="20" />
                            </div>
                        </div>
                        <div class="user-info-item">
                            <div class="label">地址</div>
                            <div class="edit-location">
                                <input type="text" v-model="userInfo.location" />
                            </div>
                        </div>
                        <div class="user-info-item">
                            <div class="label">生日</div>
                            <div class="edit-birthday">
                                <input type="date" v-model="userInfo.birthday" />
                            </div>
                        </div>
                        <div class="user-info-item">
                            <div class="label">手机号修改</div>
                            <p>需进行手机验证</p>
                        </div>
                        <div class="user-info-item">
                            <div class="label">手机</div>
                            <div class="edit-phone">
                                <input type="text" v-model="userInfo.phone" maxlength="11" />
                            </div>
                        </div>
                    </div>
                    <div class="col">
                        <div class="user-info-item img">
                            <div class="label">头像</div>
                            <p style="display: inline">点击头像进行修改</p>
                            <div class="edit-img" @click="editAatar">
                                <img :src="userInfo.avatar" />
                            </div>
                        </div>
                        <hr />
                        <div class="user-info-item">
                            <div class="label">密码修改</div>
                            <p>输入非空格字符时，将对密码进行修改，需进行手机验证</p>
                        </div>
                        <div class="user-info-item">
                            <div class="label">密码</div>
                            <div class="edit-password">
                                <input type="password" v-model="password" maxlength="20" id="password" />
                                <div class="pwd-btn" @click="showPwd('password')">
                                    <svg
                                        v-if="isPwdShow"
                                        t="1704704463513"
                                        class="icon"
                                        viewBox="0 0 1024 1024"
                                        version="1.1"
                                        xmlns="http://www.w3.org/2000/svg"
                                        p-id="1916"
                                        width="20"
                                        height="20">
                                        <path
                                            d="M512 235.77c44 0 87.11 7.4 128.12 22 39.64 14.11 76.51 34.62 109.6 60.96 32.81 26.12 60.94 57.21 83.63 92.4 20.21 31.36 35.6 65.22 45.83 100.86-10.24 35.64-25.62 69.5-45.83 100.86-22.69 35.2-50.82 66.28-83.63 92.4-33.09 26.34-69.96 46.85-109.6 60.96-41.01 14.6-84.12 22-128.12 22s-87.11-7.4-128.12-22c-39.64-14.11-76.51-34.62-109.6-60.96-32.81-26.12-60.94-57.21-83.63-92.4-20.21-31.36-35.6-65.22-45.83-100.86 10.24-35.64 25.62-69.5 45.83-100.86a383.148 383.148 0 0 1 83.63-92.4c33.09-26.34 69.96-46.85 109.6-60.96 41.01-14.6 84.12-22 128.12-22m0-64c-209.88 0-385.89 144.97-433.38 340.23C126.11 707.26 302.13 852.23 512 852.23S897.89 707.26 945.38 512C897.89 316.74 721.88 171.77 512 171.77z"
                                            p-id="1917"></path>
                                        <path
                                            d="M512 386c33.66 0 65.3 13.11 89.1 36.9 23.8 23.8 36.9 55.44 36.9 89.1s-13.11 65.3-36.9 89.1S545.66 638 512 638s-65.3-13.11-89.1-36.9C399.1 577.3 386 545.66 386 512s13.11-65.3 36.9-89.1c23.8-23.79 55.44-36.9 89.1-36.9m0-64c-104.93 0-190 85.07-190 190s85.07 190 190 190 190-85.07 190-190-85.07-190-190-190z"
                                            p-id="1918"></path>
                                    </svg>
                                    <svg
                                        v-if="!isPwdShow"
                                        t="1704704502010"
                                        class="icon"
                                        viewBox="0 0 1024 1024"
                                        version="1.1"
                                        xmlns="http://www.w3.org/2000/svg"
                                        p-id="2067"
                                        width="20"
                                        height="20">
                                        <path
                                            d="M190.65 612.86c-20.21-31.36-35.6-65.22-45.83-100.86 10.24-35.64 25.62-69.5 45.83-100.86 22.69-35.2 50.82-66.29 83.63-92.41 33.09-26.34 69.96-46.85 109.6-60.96 41.01-14.6 84.12-22 128.12-22s87.11 7.4 128.12 22c8.29 2.95 16.46 6.2 24.5 9.71l48.09-48.09C652.38 188.93 584.2 171.77 512 171.77c-209.88 0-385.89 144.97-433.38 340.23 20.42 83.96 64.62 158.6 125.04 216.44l45.26-45.26a382.718 382.718 0 0 1-58.27-70.32zM769.21 253.39l-46.02 46.02c9.08 6.04 17.94 12.48 26.53 19.32 32.81 26.12 60.94 57.21 83.63 92.41 20.21 31.36 35.6 65.22 45.83 100.86-10.24 35.64-25.62 69.5-45.83 100.86-22.69 35.2-50.82 66.29-83.63 92.41-33.09 26.34-69.96 46.85-109.6 60.96-41.01 14.6-84.12 22-128.12 22s-87.11-7.4-128.12-22a379.444 379.444 0 0 1-84.75-42.76l-45.97 45.97c72.99 52.11 162.33 82.78 258.84 82.78 209.88 0 385.89-144.97 433.38-340.23-25.81-106.13-89.61-197.39-176.17-258.6z"
                                            p-id="2068"></path>
                                        <path
                                            d="M601.1 422.9c23.8 23.8 36.9 55.44 36.9 89.1s-13.11 65.3-36.9 89.1C577.3 624.9 545.66 638 512 638s-65.3-13.11-89.1-36.9c-0.23-0.23-0.46-0.47-0.69-0.71l-45.26 45.26C411.4 680.44 459.18 702 512 702c104.93 0 190-85.07 190-190 0-52.82-21.56-100.6-56.36-135.04l-45.26 45.25c0.24 0.24 0.48 0.46 0.72 0.69zM322 512c0 28.71 6.38 55.92 17.78 80.32l49.9-49.9c-2.43-9.83-3.68-20.03-3.68-30.42 0-33.66 13.11-65.3 36.9-89.1 23.8-23.8 55.44-36.9 89.1-36.9 10.39 0 20.58 1.25 30.42 3.68l49.9-49.9C567.92 328.38 540.71 322 512 322c-104.93 0-190 85.07-190 190z"
                                            p-id="2069"></path>
                                        <path
                                            d="M847.06 188.09c-8.19 0-16.38 3.12-22.63 9.37L185.21 836.69c-12.5 12.5-12.5 32.76 0 45.25 6.25 6.25 14.44 9.37 22.63 9.37s16.38-3.12 22.63-9.37l639.22-639.22c12.5-12.5 12.5-32.76 0-45.25-6.25-6.26-14.44-9.38-22.63-9.38z"
                                            p-id="2070"></path>
                                    </svg>
                                </div>
                            </div>
                        </div>
                        <div class="user-info-item">
                            <div class="label">重复密码</div>
                            <div class="edit-repassword">
                                <input type="password" v-model="repassword" maxlength="20" id="repassword" />
                                <div class="pwd-btn" @click="showPwd('repassword')">
                                    <svg
                                        v-if="isRePwdShow"
                                        t="1704704463513"
                                        class="icon"
                                        viewBox="0 0 1024 1024"
                                        version="1.1"
                                        xmlns="http://www.w3.org/2000/svg"
                                        p-id="1916"
                                        width="20"
                                        height="20">
                                        <path
                                            d="M512 235.77c44 0 87.11 7.4 128.12 22 39.64 14.11 76.51 34.62 109.6 60.96 32.81 26.12 60.94 57.21 83.63 92.4 20.21 31.36 35.6 65.22 45.83 100.86-10.24 35.64-25.62 69.5-45.83 100.86-22.69 35.2-50.82 66.28-83.63 92.4-33.09 26.34-69.96 46.85-109.6 60.96-41.01 14.6-84.12 22-128.12 22s-87.11-7.4-128.12-22c-39.64-14.11-76.51-34.62-109.6-60.96-32.81-26.12-60.94-57.21-83.63-92.4-20.21-31.36-35.6-65.22-45.83-100.86 10.24-35.64 25.62-69.5 45.83-100.86a383.148 383.148 0 0 1 83.63-92.4c33.09-26.34 69.96-46.85 109.6-60.96 41.01-14.6 84.12-22 128.12-22m0-64c-209.88 0-385.89 144.97-433.38 340.23C126.11 707.26 302.13 852.23 512 852.23S897.89 707.26 945.38 512C897.89 316.74 721.88 171.77 512 171.77z"
                                            p-id="1917"></path>
                                        <path
                                            d="M512 386c33.66 0 65.3 13.11 89.1 36.9 23.8 23.8 36.9 55.44 36.9 89.1s-13.11 65.3-36.9 89.1S545.66 638 512 638s-65.3-13.11-89.1-36.9C399.1 577.3 386 545.66 386 512s13.11-65.3 36.9-89.1c23.8-23.79 55.44-36.9 89.1-36.9m0-64c-104.93 0-190 85.07-190 190s85.07 190 190 190 190-85.07 190-190-85.07-190-190-190z"
                                            p-id="1918"></path>
                                    </svg>
                                    <svg
                                        v-if="!isRePwdShow"
                                        t="1704704502010"
                                        class="icon"
                                        viewBox="0 0 1024 1024"
                                        version="1.1"
                                        xmlns="http://www.w3.org/2000/svg"
                                        p-id="2067"
                                        width="20"
                                        height="20">
                                        <path
                                            d="M190.65 612.86c-20.21-31.36-35.6-65.22-45.83-100.86 10.24-35.64 25.62-69.5 45.83-100.86 22.69-35.2 50.82-66.29 83.63-92.41 33.09-26.34 69.96-46.85 109.6-60.96 41.01-14.6 84.12-22 128.12-22s87.11 7.4 128.12 22c8.29 2.95 16.46 6.2 24.5 9.71l48.09-48.09C652.38 188.93 584.2 171.77 512 171.77c-209.88 0-385.89 144.97-433.38 340.23 20.42 83.96 64.62 158.6 125.04 216.44l45.26-45.26a382.718 382.718 0 0 1-58.27-70.32zM769.21 253.39l-46.02 46.02c9.08 6.04 17.94 12.48 26.53 19.32 32.81 26.12 60.94 57.21 83.63 92.41 20.21 31.36 35.6 65.22 45.83 100.86-10.24 35.64-25.62 69.5-45.83 100.86-22.69 35.2-50.82 66.29-83.63 92.41-33.09 26.34-69.96 46.85-109.6 60.96-41.01 14.6-84.12 22-128.12 22s-87.11-7.4-128.12-22a379.444 379.444 0 0 1-84.75-42.76l-45.97 45.97c72.99 52.11 162.33 82.78 258.84 82.78 209.88 0 385.89-144.97 433.38-340.23-25.81-106.13-89.61-197.39-176.17-258.6z"
                                            p-id="2068"></path>
                                        <path
                                            d="M601.1 422.9c23.8 23.8 36.9 55.44 36.9 89.1s-13.11 65.3-36.9 89.1C577.3 624.9 545.66 638 512 638s-65.3-13.11-89.1-36.9c-0.23-0.23-0.46-0.47-0.69-0.71l-45.26 45.26C411.4 680.44 459.18 702 512 702c104.93 0 190-85.07 190-190 0-52.82-21.56-100.6-56.36-135.04l-45.26 45.25c0.24 0.24 0.48 0.46 0.72 0.69zM322 512c0 28.71 6.38 55.92 17.78 80.32l49.9-49.9c-2.43-9.83-3.68-20.03-3.68-30.42 0-33.66 13.11-65.3 36.9-89.1 23.8-23.8 55.44-36.9 89.1-36.9 10.39 0 20.58 1.25 30.42 3.68l49.9-49.9C567.92 328.38 540.71 322 512 322c-104.93 0-190 85.07-190 190z"
                                            p-id="2069"></path>
                                        <path
                                            d="M847.06 188.09c-8.19 0-16.38 3.12-22.63 9.37L185.21 836.69c-12.5 12.5-12.5 32.76 0 45.25 6.25 6.25 14.44 9.37 22.63 9.37s16.38-3.12 22.63-9.37l639.22-639.22c12.5-12.5 12.5-32.76 0-45.25-6.25-6.26-14.44-9.38-22.63-9.38z"
                                            p-id="2070"></path>
                                    </svg>
                                </div>
                            </div>
                        </div>
                        <div class="user-info-item">
                            <div class="label">验证码</div>
                            <div class="edit-Verification">
                                <input type="text" v-model="Verification" maxlength="20" id="Verification" />
                                <button @click="SendCode" id="btn-Verification">获取验证码</button>
                            </div>
                        </div>
                        <div class="user-info-item" v-if="isTip" style="font-weight: 700; color: #846cb6">
                            {{ tip }}
                        </div>
                    </div>
                </div>
                <div class="profile" @click="update">更新信息</div>
            </div>
        </div>
        <!-- 更换头像弹框 -->
        <el-dialog
            title="更换头像"
            v-model="editAatarDialog"
            :append-to-body="true"
            :close-on-click-modal="false"
            :show-close="false"
            :lock-scroll="false"
            center
            class="editAatarDialogMain">
            <div class="editAatarDialog-content">
                <div>
                    <p style="text-align: center">原头像</p>
                    <img class="editAatarDialog" :src="avatar" alt="" />
                </div>
                <div>
                    <p style="text-align: center">可点击新头像进行编辑</p>
                    <label for="upload-input" class="editAatarDialog new" v-if="uploadAvatar == null">
                        请选择头像
                    </label>
                    <img
                        v-if="uploadAvatar != null"
                        class="editAatarDialog"
                        :src="uploadAvatar"
                        @click="option.img != null ? (cropperAatarDialog = true) : null"
                        alt="" />
                </div>
            </div>
            <div style="height: 30px; margin-top: 10px">
                <div v-if="isTip" style="text-align: center; font-size: 18px; font-weight: 700; color: #846cb6">
                    {{ tip }}
                </div>
            </div>

            <template #footer>
                <span class="dialog-footer">
                    <el-button @click="cencel">取 消</el-button>
                    <div style="display: inline-block; margin: 0 10px">
                        <el-button type="warning" style="padding: 0">
                            <label
                                for="upload-input"
                                style="display: block; width: 90px; height: 30px; line-height: 30px">
                                选择新头像
                            </label>
                        </el-button>
                        <input
                            type="file"
                            class="upload-input"
                            id="upload-input"
                            :key="asd"
                            @change="getFile($event)" />
                    </div>
                    <el-button type="primary" @click="UpdateImg">确 定</el-button>
                </span>
            </template>
        </el-dialog>
        <!-- 裁剪图片 -->
        <el-dialog
            v-model="cropperAatarDialog"
            :append-to-body="true"
            :close-on-click-modal="false"
            :show-close="false"
            :lock-scroll="false"
            center
            class="editAatarDialogMain">
            <div class="CropperDialog">
                <vueCropper
                    style="width: 100%; height: 100%"
                    ref="cropper"
                    :img="option.img"
                    :outputSize="option.outputSize"
                    :outputType="option.outputType"
                    :info="option.info"
                    :canScale="option.canScale"
                    :autoCrop="option.autoCrop"
                    :autoCropWidth="option.autoCropWidth"
                    :autoCropHeight="option.autoCropHeight"
                    :fixedBox="option.fixedBox"
                    :fixed="option.fixed"
                    :fixedNumber="option.fixedNumber"
                    :canMove="option.canMove"
                    :canMoveBox="option.canMoveBox"
                    :original="option.original"
                    :centerBox="option.centerBox"
                    :infoTrue="option.infoTrue"
                    :full="option.full"
                    :enlarge="option.enlarge"
                    :mode="option.mode">
                </vueCropper>
            </div>
            <div style="height: 30px; margin-top: 10px">
                <div v-if="isTip" style="text-align: center; font-size: 18px; font-weight: 700; color: #846cb6">
                    {{ tip }}
                </div>
            </div>
            <template #footer>
                <span class="dialog-footer">
                    <el-button @click="closeCropper">取 消</el-button>
                    <el-button type="primary" @click="cropperAvatar">确 定</el-button>
                </span>
            </template>
        </el-dialog>
    </div>
</template>

<script setup>
// import UserInfoCard from "@/pages/Index/UserInfoCard.vue";
import { onMounted, onUnmounted, reactive, ref } from "vue";
import md5 from "js-md5";
import { getUserInfo, updateUserInfo, updateAvatar, sendCode } from "@/api/User/index";
import router from "@/router";
import "vue-cropper/dist/index.css";
import { VueCropper } from "vue-cropper";
import { ElMessage } from "element-plus";
let isTip = ref(false);
let tip = ref("");

const userInfo = ref({});
let Verification = ref(null); //验证码
let isPwdShow = ref(false);
let isRePwdShow = ref(false);
/* 显示密码 */
function showPwd(flag) {
    let ele = null;
    if (flag == "password") {
        ele = document.getElementById("password");
        isPwdShow.value = !isPwdShow.value;
    } else if (flag == "repassword") {
        ele = document.getElementById("repassword");
        isRePwdShow.value = !isRePwdShow.value;
    }
    if (ele.type === "password") {
        ele.type = "text";
    } else {
        ele.type = "password";
    }
}
/* 获取用户信息 */
function GetUserInfo() {
    var uid = localStorage.getItem("uid");
    if (uid != null) {
        // 获取用户信息
        getUserInfo({ id: uid }).then((res) => {
            if (res.data.code == 1) {
                userInfo.value = res.data.data;
                let date = res.data.data.birthday;
                if (res.data.data.birthday != null) {
                    userInfo.value.birthday = date.split("T")[0];
                }

                if (userInfo.value.avatar == "" || userInfo.value.avatar == null) {
                    userInfo.value.avatar = require("@/assets/avatar.png");
                }
                avatar.value = userInfo.value.avatar;
                originalUsername = userInfo.value.username;
                originalPhone.value = userInfo.value.phone;
            } else {
                ElMessage.error({ message: "获取用户信息失败", offset: 300 });
            }
        });
    } else {
    }
}
/* 编辑用户头像 */
let editAatarDialog = ref(false);
let avatar = ref();
let uploadAvatar = ref();
let formData = new FormData();
let fileName = "";
function editAatar() {
    editAatarDialog.value = true;
}
/* 获取文件数据 */
function getFile(event) {
    let file = event.target.files[0];
    if (file != null) {
        if (file.type.match(/image/) != null) {
            if (file.size > 1024 * 1024 * 3) {
                alert("图片过大，请选择小于3MB的图片！");
            } else {
                /* 裁剪图片 */
                option.img = URL.createObjectURL(file);
                uploadAvatar.value = URL.createObjectURL(file);
                cropperAatarDialog.value = true;
                fileName = file.name;
            }
        } else {
            alert("请选择选择格式为png、jpg、jpeg、webp的图片");
        }
    }
}
/* 截取头像 */
let cropperAatarDialog = ref(false);
let cropper = ref();
let option = reactive({
    img: null, // 裁剪图片的地址 url 地址, base64, blob
    outputSize: 0.3, // 裁剪生成图片的质量
    outputType: "webp", // 裁剪生成图片的格式 jpeg, png, webp
    info: true, // 裁剪框的大小信息
    canScale: false, // 图片是否允许滚轮缩放
    autoCrop: true, // 是否默认生成截图框
    autoCropWidth: 150, // 默认生成截图框宽度
    autoCropHeight: 150, // 默认生成截图框高度
    fixedBox: false, // 固定截图框大小 不允许改变
    fixed: true, // 是否开启截图框宽高固定比例，这个如果设置为true，截图框会是固定比例缩放的，如果设置为false，则截图框的狂宽高比例就不固定了
    fixedNumber: [1, 1], // 截图框的宽高比例 [ 宽度 , 高度 ]
    canMove: true, // 上传图片是否可以移动
    canMoveBox: true, // 截图框能否拖动
    original: false, // 上传图片按照原始比例渲染
    centerBox: true, // 截图框是否被限制在图片里面
    infoTrue: true, // true 为展示真实输出图片宽高 false 展示看到的截图框宽高
    full: true, // 是否输出原图比例的截图
    enlarge: "1", // 图片根据截图框输出比例倍数
    mode: "contain", // 图片默认渲染方式 contain , cover, 100px, 100% auto
});

function cropperAvatar() {
    cropperAatarDialog.value = false;
    cropper.value.getCropBlob((data) => {
        formData = new FormData();
        let file = new File([data], fileName.split(".")[0] + ".webp");
        formData.append("file", file);
        /* 展示预览图片 */
        uploadAvatar.value = URL.createObjectURL(data);
    });
}
let asd = ref(0);
function closeCropper() {
    cropperAatarDialog.value = false;
    asd.value = asd.value + 1;
}
/* 上传图片 */
function UpdateImg() {
    if (formData.has("file")) {
        updateAvatar(formData).then((res) => {
            if (res.data.code == 1) {
                location.reload();
                formData = new FormData();
                editAatarDialog.value = false;
                userInfo.value.avatar = res.data.data;
                avatar.value = res.data.data;
                uploadAvatar.value = null;
                isTip.value = false;
                tip.value = "";
                asd.value = asd.value + 1;
            } else {
                isTip.value = true;
                tip.value = res.data.msg;
            }
        });
    } else {
        isTip.value = true;
        tip.value = "请选择图片或确认裁剪的图片";
        setTimeout(() => {
            isTip.value = false;
            tip.value = "";
        }, 1500);
    }
}
/* 编辑用户信息 */
let password = ref("");
let repassword = ref("");
let originalUsername = "";
let originalPhone = ref("");
function update() {
    if (userInfo.value.username.length < 5) {
        isTip.value = 1;
        tip.value = "用户名不能少于5位";
        setTimeout(() => {
            tip.value = null;
        }, 2000);
        return;
    }
    var usernamereg = /^[0-9]*$/;
    if (usernamereg.test(userInfo.value.username)) {
        isTip.value = 1;
        tip.value = "用户名不能为纯数字";
        setTimeout(() => {
            tip.value = null;
        }, 2000);
        return;
    }
    var phonereg = /0?(13|14|15|17|18|19)[0-9]{9}/;
    if (!phonereg.test(userInfo.value.phone)) {
        isTip.value = 1;
        tip.value = "请输入正确的手机号";
        setTimeout(() => {
            tip.value = null;
        }, 2000);
        return;
    }
    if (originalPhone.value != userInfo.value.phone) {
        if (Verification.value == null || Verification.value == "") {
            isTip.value = true;
            tip.value = "请获取并输入验证码";
            setTimeout(() => {
                isTip.value = false;
                tip.value = null;
            }, 2000);
            return;
        }
    }
    let flag = false;
    let ispwd = false;

    if (repassword.value.trim() == "" && password.value.trim() == "") {
        repassword.value = password.value = "";
        flag = true;
    } else {
        /* 如果密码框不为空，则校验密码 */
        ispwd = true;
        var reg = /^(?=.*[a-zA-Z])(?=.*\d).{6,}$/; //使用正则表达式匹配密码
        if (reg.test(password.value) && reg.test(repassword.value)) {
            /* 密码不一致 */
            if (password.value != repassword.value) {
                isTip.value = true;
                tip.value = "两次输入的密码不一致";
                setTimeout(() => {
                    isTip.value = false;
                    tip.value = null;
                }, 2000);
                return;
            } else {
                /* 密码一致 */
                flag = true;
            }
        } else {
            /* 密码格式不规范 */
            isTip.value = true;
            tip.value = "密码至少包含字母、数字,长度为6";
            setTimeout(() => {
                isTip.value = false;
                tip.value = null;
            }, 2000);
            return;
        }
    }

    if (flag) {
        userInfo.value.password = password.value;
        userInfo.value.role = Verification.value;
        updateUserInfo(userInfo.value).then((res) => {
            if (res.data.code == 1) {
                isTip.value = true;
                tip.value = "修改成功";
                ElMessage.success({ message: "修改成功", offset: 300 });

                originalPhone.value = userInfo.value.phone;

                if (ispwd || originalUsername != userInfo.value.username) {
                    localStorage.clear();
                    setTimeout(() => {
                        router.replace("/login");
                    }, 2000);
                }
                if (!ispwd && originalUsername == userInfo.value.username) {
                    location.reload();
                }
                var senbte = document.getElementById("btn-Verification");
                senbte.innerText = "获取验证码";
                secend = 120;
                clearTimeout(timer);
                senbte.disabled = false;
                f = 0;
            } else {
                var senbte = document.getElementById("btn-Verification");
                senbte.innerText = "获取验证码";
                secend = 120;
                clearTimeout(timer);
                senbte.disabled = false;
                f = 0;
                isTip.value = true;
                tip.value = res.data.msg;
            }
        });
    }
}

function cencel() {
    editAatarDialog.value = false;
    formData = new FormData();
    uploadAvatar.value = null;
    isTip.value = false;
    tip.value = "";
}

/* 验证码倒计时 */
let f = 0;
var timer = null;
var secend = 120;
function sleepCode() {
    var senbte = document.getElementById("btn-Verification");
    senbte.disabled = true;
    if (secend != 0) {
        timer = setTimeout(() => {
            secend--;
            senbte.innerText = "剩余 " + secend + " s";
            sleepCode();
        }, 1000);
    } else {
        senbte.innerText = "获取验证码";
        secend = 120;
        clearTimeout(timer);
        senbte.disabled = false;
        f = 0;
    }
}
/* 发送验证码 */
function SendCode() {
    if (f == 1) return;
    var phonereg = /0?(13|14|15|17|18|19)[0-9]{9}/;
    let phone = originalPhone.value;
    console.log(phone);
    
    if (!phonereg.test(phone)) {
        isTip.value = true;
        tip.value = "请输入正确的手机号";
        setTimeout(() => {
            tip.value = null;
            isTip.value = false;
        }, 2000);
        return;
    }
    if (phone != null && phone != "") {
        sendCode({ phone: phone }).then((res) => {
            if (res.data.code == 1) {
                f = 1;
                sleepCode();
            } else {
                isTip.value = true;
                tip.value = res.data.msg;
                setTimeout(() => {
                    tip.value = null;
                    isTip.value = false;
                }, 2000);
            }
        });
    } else {
        tip.value = "请输入手机号";
        setTimeout(() => {
            tip.value = null;
        }, 2000);
    }
}
onMounted(() => {
    GetUserInfo();
});
onUnmounted(() => {
    clearTimeout(timer);
});
</script>

<style scoped>
/* .main {
  width: 100%;
}
.main:hover {
  box-shadow: 0 3px 8px 6px rgba(7, 17, 27, 0.14);
} */
.user-info-main {
    width: 100%;
    overflow: hidden;
}
.user-info-from {
    width: 100%;
}
.user-info-content {
    display: flex;
    /* grid-template-columns: 1fr 1fr;
  justify-items: center; */
    gap: 24px;
}
.col {
    /* margin: 0 20px 20px 0; */
    width: 100%;
}
.user-info-card {
    width: 240px;
}

.user-info-item {
    margin-bottom: 16px;
    width: 100%;
}
.user-info-item p {
    font-size: 13px;
    text-decoration: underline;
}
.user-info-item input,
.user-info-item textarea {
    max-width: 370px;
    width: 100%;
    padding: 6px 8px;
    border-radius: 6px;
    border: 2px solid #d0d7de;
    background-color: #f6f8fa;
    font-size: 16px;
    max-height: 100px;
}
.user-info-item input:hover,
.user-info-item textarea:hover {
    border: 2px solid #90ccff;
    outline: none;
}
.user-info-item input:focus,
.user-info-item textarea:focus {
    border: 2px solid #90ccff;
    outline: none;
}
.user-info-item.img {
    height: 180px;
}
.edit-img {
    width: 100px;
    height: 100px;
    border-radius: 50%;
    overflow: hidden;
    margin: 13px 0 0 23px;
    cursor: pointer;
    transition: 0.3s;
}
.edit-img:hover {
    transform: scale(1.1);
}
.editAatarDialog-content {
    display: flex;
    justify-content: center;
    align-items: center;
    gap: 20px;
    margin: 0 auto;
}
.editAatarDialog {
    width: 300px;
    height: 300px;
    border: 1px solid #000;
    border-radius: 8px;
}
.editAatarDialog.new {
    display: flex;
    font-size: 24px;
    align-items: center;
    justify-content: center;
    /* text-align: center; */
    /* line-height: 300px; */
}
.editAatarDialog-operation-item {
    width: 120px;
    margin-bottom: 4px;
    font-size: 16px;
    /* font-weight: 600; */
    line-height: 26px;
    height: 26px;
    text-align: center;
    border-radius: 6px;
    color: #fff;
    background-color: #9a8fff;
}
.upload-input {
    display: none;
    font-size: 0;
}
.label {
    font-weight: 700;
    width: 80px;
    font-size: 15px;
    margin-bottom: 6px;
}
.edit-img img {
    width: 100%;
    height: 100%;
    object-fit: cover;
}

.profile {
    text-align: center;
    color: #fff;
    width: max-content;
    height: 34px;
    padding: 0px 20px;
    border-radius: 6px;
    font-weight: 500;
    line-height: 34px;
    background-color: #a28dd1;
    cursor: pointer;
    margin: 0 auto;
}
.profile:hover {
    background-color: #846cb6;
}
.edit-password,
.edit-repassword {
    display: flex;
}
.edit-Verification {
    display: flex;
}
.edit-Verification input {
    max-width: 200px;
    margin-right: 10px;
}
.edit-Verification button {
    max-width: 120px;
    text-align: center;
    color: #fff;
    width: max-content;
    height: 34px;
    padding: 0px 10px;
    border-radius: 6px;
    font-weight: 500;
    line-height: 34px;
    background-color: #a28dd1;
    cursor: pointer;
    border: none;
    font-size: 16px;
}
.pwd-btn {
    position: relative;
    top: 7px;
    right: 40px;
    width: 34px;
    height: 34px;
    background-color: #00000000;
    border: none;
    cursor: pointer;
}
.CropperDialog {
    width: 500px;
    height: 500px;
    margin: 0 auto;
}
@media (max-width: 600px) {
    .user-info-content {
        flex-direction: column;
    }
    .profile {
        margin-top: 10px;
    }

    .editAatarDialog-content {
        width: 100%;
        flex-direction: column;
    }
    .editAatarDialog {
        width: 100%;
        height: 100%;
    }
    .CropperDialog {
        width: 300px;
        height: 300px;
    }
}
</style>
<style>
.editAatarDialogMain {
    width: 50%;
    min-width: 650px;
    /* height: 100%; */
}
@media (max-width: 600px) {
    .editAatarDialogMain {
        width: 100%;
        min-width: unset;
        /* height: 100%; */
    }
}
</style>
