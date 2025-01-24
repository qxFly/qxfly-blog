<template>
    <div class="main">
        <div class="setting-form">
            <div class="setting-items">
                <div class="col">
                    <div class="setting-item">
                        <div class="item-header">
                            背景图片
                            <span class="bg-switchs">
                                <input
                                    type="radio"
                                    name="bg-switch"
                                    id="bg-switch-off"
                                    v-model="userSettings.bgSwitch"
                                    :value="0" />
                                <label for="bg-switch-off">关</label>
                                <input
                                    type="radio"
                                    name="bg-switch"
                                    id="bg-switch-on"
                                    v-model="userSettings.bgSwitch"
                                    :value="1" />
                                <label for="bg-switch-on"> 开</label>
                            </span>
                        </div>
                        <div for="upload-input" class="img-box" @click="editBg">
                            <p v-if="userSettings.bgImgPath">点击图片进行修改</p>
                            <img v-if="userSettings.bgImgPath" :src="userSettings.bgImgPath" />
                            <div v-else style="cursor: pointer">点击设置背景图片</div>
                        </div>
                    </div>
                    <div class="setting-item">
                        <div class="item-header">
                            <label for="bg-blur-input">背景模糊度</label>
                        </div>

                        <input
                            type="number"
                            id="bg-blur-input"
                            min="0"
                            v-model="userSettings.bgBlur"
                            placeholder="请输入背景模糊度" />
                    </div>
                </div>
                <div class="col"></div>
            </div>

            <div class="profile" @click="UpdateUserSettings">确认更改</div>
        </div>
        <!-- 更换背景弹框 -->
        <el-dialog
            title="更换背景"
            v-model="editBgDialog"
            :append-to-body="true"
            :close-on-click-modal="false"
            :show-close="false"
            :lock-scroll="false"
            center
            class="editBgDialogMain">
            <div class="editBgDialog-content">
                <div>
                    <p style="text-align: center">原背景</p>
                    <div class="editBgDialog-imgdiv editBgDialog-origin-bg">
                        <img v-if="prebgimg != null && prebgimg != ''" :src="prebgimg" alt="" />
                    </div>
                </div>
                <div>
                    <p style="text-align: center">可点击新背景进行编辑</p>

                    <div class="editBgDialog-imgdiv editBgDialog-new-bg">
                        <label for="upload-input" class="editBgDialog-imgdiv-label new" v-if="uploadBg == null">
                            请选择背景
                        </label>
                        <img
                            v-if="uploadBg != null"
                            :src="uploadBg"
                            @click="option.img != null ? (cropperBgImgDialog = true) : null"
                            alt="" />
                    </div>
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
                                选择新背景
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
            v-model="cropperBgImgDialog"
            :append-to-body="true"
            :close-on-click-modal="false"
            :show-close="false"
            :lock-scroll="false"
            center
            class="editBgDialogMain">
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
                    <el-button type="primary" @click="cropperBgImg">确 定</el-button>
                </span>
            </template>
        </el-dialog>
    </div>
</template>

<script setup>
import { onMounted, onUnmounted, reactive, ref, watch } from "vue";
import md5 from "js-md5";
import { getUserSettings, updateUserSettings, uploadBgImg } from "@/api/User/index";
import { useRoute } from "vue-router";
import router from "@/router";
import "vue-cropper/dist/index.css";
import { VueCropper } from "vue-cropper";
import { ElMessage } from "element-plus";
let useRouter = useRoute();
let isTip = ref(false);
let tip = ref("");
/* 获取用户设置 */
let uid = useRouter.query.uid;
let userSettings = ref({});
let originalUserSettings = null;
function GetUserSettings() {
    getUserSettings(uid).then((res) => {
        if (res.data.code != 1) return;
        userSettings.value = res.data.data;
        originalUserSettings = Object.assign({}, userSettings.value);
        prebgimg.value = res.data.data.bgImgPath;
    });
}

/* 更新用户设置 */
function UpdateUserSettings() {
    updateUserSettings(userSettings.value).then((res) => {
        if (res.data.code != 1) return;
        ElMessage.success({ message: "修改成功", offset: 120 });
        location.reload();
    });
}

/* 编辑用户背景 */
let editBgDialog = ref(false);
let prebgimg = ref();
let uploadBg = ref();
let formData = new FormData();
let fileName = "";
function editBg() {
    editBgDialog.value = true;
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
                uploadBg.value = URL.createObjectURL(file);
                cropperBgImgDialog.value = true;
                fileName = file.name;
            }
        } else {
            alert("请选择选择格式为png、jpg、jpeg、webp的图片");
        }
    }
}
/* 1、截取背景 */
let cropperBgImgDialog = ref(false);
let cropper = ref();
let option = reactive({
    img: null, // 裁剪图片的地址 url 地址, base64, blob
    outputSize: 0.9, // 裁剪生成图片的质量
    outputType: "webp", // 裁剪生成图片的格式 jpeg, png, webp
    info: true, // 裁剪框的大小信息
    canScale: false, // 图片是否允许滚轮缩放
    autoCrop: true, // 是否默认生成截图框
    autoCropWidth: 160, // 默认生成截图框宽度
    autoCropHeight: 90, // 默认生成截图框高度
    fixedBox: false, // 固定截图框大小 不允许改变
    fixed: true, // 是否开启截图框宽高固定比例，这个如果设置为true，截图框会是固定比例缩放的，如果设置为false，则截图框的狂宽高比例就不固定了
    fixedNumber: [16, 9], // 截图框的宽高比例 [ 宽度 , 高度 ]
    canMove: true, // 上传图片是否可以移动
    canMoveBox: true, // 截图框能否拖动
    original: false, // 上传图片按照原始比例渲染
    centerBox: true, // 截图框是否被限制在图片里面
    infoTrue: true, // true 为展示真实输出图片宽高 false 展示看到的截图框宽高
    full: true, // 是否输出原图比例的截图
    enlarge: "1", // 图片根据截图框输出比例倍数
    mode: "contain", // 图片默认渲染方式 contain , cover, 100px, 100% auto
});
/* 2、截取背景 */
function cropperBgImg() {
    cropperBgImgDialog.value = false;
    cropper.value.getCropBlob((data) => {
        formData = new FormData();
        let file = new File([data], fileName.split(".")[0] + ".webp");
        formData.append("file", file);
        /* 展示预览图片 */
        uploadBg.value = URL.createObjectURL(data);
    });
}
/* 3、截取背景 */
let asd = ref(0); // 解决裁剪框不刷新问题
function closeCropper() {
    cropperBgImgDialog.value = false;
    asd.value = asd.value + 1;
}
/* 上传图片 */
function UpdateImg() {
    if (formData.has("file")) {
        uploadBgImg(formData).then((res) => {
            if (res.data.code == 1) {
                ElMessage.success("上传成功");
                // location.reload();
                formData = new FormData();
                cropperBgImgDialog.value = false;
                userSettings.value.bgImgPath = res.data.data;
                originalUserSettings = Object.assign({}, userSettings.value);
                prebgimg.value = res.data.data;
                uploadBg.value = null;
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

function cencel() {
    editBgDialog.value = false;
    formData = new FormData();
    isTip.value = false;
    tip.value = "";
}

/* 未保存背景图片设置时恢复原来设置 */
function resetBg() {
    let bg = document.getElementById("index_bg");
    if (originalUserSettings.bgSwitch == 1) {
        originalUserSettings.bgImgPath = originalUserSettings.bgImgPath.replace(/\\/g, "/");
        bg.style.backgroundImage = "url(" + originalUserSettings.bgImgPath + ")";
    } else {
        bg.style.backgroundImage = "none";
    }
    bg.style.filter = "blur(" + originalUserSettings.bgBlur + "px)";
    bg.style.backdropFilter = "blur(" + originalUserSettings.bgBlur + "px)";
}
watch(
    userSettings,
    (newVal, oldVal) => {
        let bg = document.getElementById("index_bg");
        if (newVal.bgSwitch == 1) {
            newVal.bgImgPath = newVal.bgImgPath.replace(/\\/g, "/");
            bg.style.backgroundImage = "url(" + newVal.bgImgPath + ")";
        } else {
            bg.style.backgroundImage = "none";
        }
        bg.style.filter = "blur(" + newVal.bgBlur + "px)";
        bg.style.backdropFilter = "blur(" + newVal.bgBlur + "px)";
    },
    {
        deep: true,
    }
);
onMounted(() => {
    GetUserSettings();
});
onUnmounted(() => {
    resetBg();
});
</script>

<style scoped>
.main {
    width: 100%;
}
.setting-form {
    width: 100%;
    height: 100%;
}
.setting-items {
    display: flex;
    /* grid-template-columns: 1fr 1fr;
  justify-items: center; */
    gap: 24px;
}
.col {
    width: 100%;
}
.setting-item {
    margin-bottom: 20px;
}
.setting-item p {
    font-size: 13px;
    text-decoration: underline;
    margin-bottom: 4px;
}
.item-header {
    font-weight: bold;
    margin-bottom: 16px;
}
.bg-switchs {
    font-size: 14px;
}
.bg-switchs input {
    width: 12px;
    height: 12px;
    margin: 0 0 0 8px;
    background-color: #fff;
    outline: none;
    appearance: none;
    border: 1px solid #84c6ff;
    border-radius: 50%;
}
.bg-switchs input[type="radio"]:checked {
    background: #84c6ff;
}
.setting-item input[type="text"],
.setting-item input[type="number"],
.setting-item textarea {
    max-width: 370px;
    width: 100%;
    padding: 6px 8px;
    border-radius: 6px;
    border: 2px solid #d0d7de;
    background-color: rgba(246, 248, 250, 0.5);
    font-size: 16px;
    max-height: 100px;
}
.setting-item input[type="text"]:hover,
.setting-item input[type="number"]:hover,
.setting-item textarea:hover {
    border: 2px solid #90ccff;
    outline: none;
}
.setting-item input[type="text"]:focus,
.setting-item input[type="number"]:focus,
.setting-item textarea:focus {
    border: 2px solid #90ccff;
    outline: none;
}
.img-box {
    display: block;
    width: 300px;
    /* height: 200px; */
}
.img-box img {
    width: 100%;
    border: 1px solid #000;
    border-radius: 4px;
    /* height: 100%; */
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
.editBgDialog-content {
    display: flex;
    justify-content: center;
    align-items: center;
    gap: 20px;
    margin: 0 auto;
}
.editBgDialog-imgdiv {
    width: 320px;
    height: 180px;
    border: 1px solid #000;
    border-radius: 8px;
}
.editBgDialog-imgdiv img {
    width: 100%;
    height: 100%;
    border-radius: 8px;
}
.editBgDialog-imgdiv-label {
    width: 320px;
    height: 180px;
}
.editBgDialog-imgdiv-label.new {
    display: flex;
    font-size: 24px;
    align-items: center;
    justify-content: center;
    /* text-align: center; */
    /* line-height: 300px; */
}
.editBgDialog-operation-item {
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
.CropperDialog {
    width: 500px;
    height: 500px;
    margin: 0 auto;
}
@media (max-width: 600px) {
    .setting-items {
        flex-direction: column;
    }
    .profile {
        margin-top: 10px;
    }

    .editBgDialog-content {
        width: 100%;
        flex-direction: column;
    }
    .editBgDialog-imgdiv-label {
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
.editBgDialogMain {
    width: 50%;
    min-width: 690px;
    /* height: 100%; */
}
@media (max-width: 600px) {
    .editBgDialogMain {
        width: 100%;
        min-width: unset;
        /* height: 100%; */
    }
}
</style>
