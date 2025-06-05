<template>
    <el-container class="main-container">
        <el-header class="header"> </el-header>
        <el-main class="main">
            <div>
                <el-descriptions title="" direction="vertical" :column="2" border>
                    <el-descriptions-item label="背景开关">
                        <el-radio-group v-model="sysSettings.bgSwitch">
                            <el-radio-button label="开" value="1" />
                            <el-radio-button label="关" value="0" />
                        </el-radio-group>
                    </el-descriptions-item>
                    <el-descriptions-item label="背景模糊度（px）">
                        <el-input v-model="sysSettings.bgBlur"></el-input>
                    </el-descriptions-item>
                    <el-descriptions-item label="背景图片" :span="2">
                        <div style="width: 100%; display: flex; justify-content: center" @click="changeBgImage">
                            <el-image style="width: 30%" :src="sysSettings.bgImgPath" fit="contain" />
                        </div>
                    </el-descriptions-item>
                    <el-descriptions-item label="字体颜色">
                        <el-input v-model="sysSettings.fontColor"></el-input>
                    </el-descriptions-item>
                </el-descriptions>
                <div class="savebtn">
                    <el-button type="primary" style="margin-top: 20px" @click="saveSysSettings">保存</el-button>
                </div>
            </div>
        </el-main>
    </el-container>
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
                        <label for="upload-input" style="display: block; width: 90px; height: 30px; line-height: 30px">
                            选择新背景
                        </label>
                    </el-button>
                    <input type="file" class="upload-input" id="upload-input" :key="asd" @change="getFile($event)" />
                </div>
                <el-button type="primary" @click="determine">确 定</el-button>
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
</template>
<script setup>
import { getSysSetting } from "@/api/System";
import { updateSysSetting, uploadBgImg } from "@/api/Admin";
import { ref, onMounted, reactive } from "vue";
import { useRoute } from "vue-router";
import { ElMessage } from "element-plus";
import "vue-cropper/dist/index.css";
import { VueCropper } from "vue-cropper";
let loading = ref(true);
/* 查询 */
let sysSettings = ref({});
function search() {
    loading.value = true;
    getSysSetting().then((res) => {
        if (res.data.code == 1) {
            loading.value = false;
            if (res.data.data == null) return;
            sysSettings.value = res.data.data;
            prebgimg.value = sysSettings.value.bgImgPath;
        }
    });
}
/* 保存设置 */
async function saveSysSettings() {
    await UpdateImg();
    updateSysSetting(sysSettings.value).then((res) => {
        search();
    });
}
/* 修改背景图片 */
let editBgDialog = ref(false);
let prebgimg = ref();
let uploadBg = ref();
let formData = new FormData();
let fileName = "";
let isTip = ref(false);
let tip = ref("");
let originalSysSettings = null;
function changeBgImage() {
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
    autoCropWidth: 1600, // 默认生成截图框宽度
    autoCropHeight: 1600, // 默认生成截图框高度
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
        // uploadBg.value = URL.createObjectURL(data);
        sysSettings.value.bgImgPath = URL.createObjectURL(data);
    });
}
/* 3、截取背景 */
let asd = ref(0); // 解决裁剪框不刷新问题
function closeCropper() {
    cropperBgImgDialog.value = false;
    asd.value = asd.value + 1;
}
/* 上传图片 */
async function UpdateImg() {
    if (formData.has("file")) {
        await uploadBgImg(formData).then((res) => {
            if (res.data.code == 1) {
                ElMessage.success("上传成功");
                formData = new FormData();
                sysSettings.value.bgImgPath = res.data.data.replace(/\\/g, "/");
                originalSysSettings = Object.assign({}, sysSettings.value);
                prebgimg.value = res.data.data;
                asd.value = asd.value + 1;
            } else {
                isTip.value = true;
                tip.value = res.data.msg;
            }
        });
    } else {
        ElMessage.success("请选择图片或确认裁剪的图片");
    }
}

function cencel() {
    editBgDialog.value = false;
    formData = new FormData();
    isTip.value = false;
    tip.value = "";
}
function determine() {
    editBgDialog.value = false;
}
onMounted(() => {
    search();
    // testAuthority();
});
</script>
<style lang="less" scoped>
.main-container {
    margin-top: 20px;
}
.search-items {
    display: flex;
    gap: 10px;
    margin: 4px;
}
.search-item {
    min-width: 50px;
}
.pagination {
    display: flex;
    justify-content: center;
    align-items: center;
    margin-top: 20px;
}
.header {
    // display: block;
    // height: 100px;
    height: max-content;
    margin-bottom: 10px;
}
.main {
    padding-top: 0;
}
.savebtn {
    display: flex;
    justify-content: center;
    align-items: center;
}
.user-info-content {
    // min-width: 400px;
    padding: 20px 20px 20px 0;
    width: 100%;
    display: flex;
    gap: 40px;
    overflow: hidden;
}
.col {
    width: 100%;
}

.user-info-item {
    margin: 20px;
    width: 100%;
}
.label {
    font-weight: 700;
    width: 90px;
    text-align: center;
}
.label-r {
    font-weight: 700;
    width: 40px;
    text-align: center;
    margin-bottom: 10px;
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
</style>
