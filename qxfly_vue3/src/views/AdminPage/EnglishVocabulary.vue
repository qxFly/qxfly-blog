<template>
    <el-container class="english-vocabulary-main-container">
        <el-header class="header">
            <div class="search-bar">
                <div class="search-items">
                    <el-input class="search-item" v-model="word" placeholder="单词" clearable style="width: 160px" />
                    <el-input
                        class="search-item"
                        v-model="interpretation"
                        placeholder="释义"
                        clearable
                        style="width: 160px" />
                    <el-input
                        class="search-item"
                        v-model="levelType"
                        placeholder="等级类型"
                        clearable
                        style="width: 160px" />

                    <el-button type="primary" @click="toSearch">筛选</el-button>
                    <el-button @click="clearAll" style="margin: 0">清空筛选</el-button>
                    <el-button @click="addEnglishVocabularyDialog = true" style="margin: 0">添加单词</el-button>
                    <el-button @click="toAddEnglishVocabularyByExcel" style="margin: 0">通过excel添加单词</el-button>
                    <input type="file" id="file" :key="key" style="display: none" @change="uploadExcel($event)" />
                </div>
            </div>
        </el-header>
        <el-main class="main">
            <div>
                <el-table
                    v-loading="loading"
                    :data="EnglishVocabularys"
                    :border="true"
                    show-overflow-tooltip
                    stripe
                    :highlight-current-row="true"
                    style="width: 100%"
                    :row-style="rowStyle"
                    :max-height="setTableHeight()">
                    <el-table-column fixed prop="id" label="ID" width="80" align="center" />
                    <el-table-column prop="word" label="单词" width="150" align="center" />
                    <el-table-column prop="phoneticSymbol" width="150" label="音标" align="center" />
                    <el-table-column prop="interpretation" label="释义" align="center" />
                    <el-table-column prop="levelType" label="等级类型" align="center" />
                    <el-table-column prop="" label="操作" width="160" align="center">
                        <template #default="scope">
                            <el-button type="primary" size="small" plain @click="toUpdateEnglishVocabulary(scope.row)">
                                修改
                            </el-button>
                            <el-button type="danger" size="small" plain @click="Delete(scope.row.id)"> 删除 </el-button>
                        </template>
                    </el-table-column>
                </el-table>
            </div>
            <div class="pagination">
                <el-pagination
                    v-model:current-page="currPage"
                    :page-size="pageSize"
                    :pager-count="11"
                    background
                    layout="total, prev, pager, next, jumper"
                    :total="totalPages"
                    @current-change="currentChange" />
            </div>

            <iframe src="" frameborder="0" id="iframe" style="display: none"></iframe>
        </el-main>
    </el-container>

    <!-- 添加 -->
    <el-dialog
        v-model="addEnglishVocabularyDialog"
        width="40%"
        center
        :append-to-body="true"
        :show-close="false"
        :lock-scroll="false"
        :destroy-on-close="true"
        :align-center="true">
        <div class="update-dialog-item">
            <el-input clearable type="text" v-model="vocabulary.word">
                <template #prepend>
                    <div class="label">单词</div>
                </template>
            </el-input>
        </div>
        <div class="update-dialog-item">
            <el-input clearable type="text" v-model="vocabulary.phoneticSymbol">
                <template #prepend>
                    <div class="label">音标</div>
                </template>
            </el-input>
        </div>
        <div class="update-dialog-item">
            <el-input clearable type="text" v-model="vocabulary.interpretation">
                <template #prepend>
                    <div class="label">释义</div>
                </template>
            </el-input>
        </div>
        <div class="update-dialog-item">
            <div class="label" style="width: max-content">单词等级类型</div>
            <el-checkbox-group v-model="vocabulary.levelType" class="search-item" style="width: 160px">
                <el-checkbox label="CET4" value="CET4" />
                <el-checkbox label="CET6" value="CET6" />
            </el-checkbox-group>
        </div>
        <template #footer>
            <span class="dialog-footer">
                <el-button @click="addEnglishVocabularyDialog = false">关 闭</el-button
                ><el-button @click="AddEnglishVocabulary()" type="primary">添 加</el-button>
            </span>
        </template>
    </el-dialog>
    <!-- 通过excel添加单词 -->
    <el-dialog
        v-model="addEnglishVocabularyByExcelDialog"
        width="40%"
        center
        :append-to-body="true"
        :show-close="false"
        :lock-scroll="false"
        :destroy-on-close="true"
        :align-center="true">
        <div class="update-dialog-item">
            <div class="label" style="width: max-content">单词等级类型</div>
            <el-checkbox-group v-model="wordLevelType" class="search-item" style="width: 160px">
                <el-checkbox label="CET4" value="CET4" />
                <el-checkbox label="CET6" value="CET6" />
            </el-checkbox-group>
        </div>
        <div class="update-dialog-item">
            <el-input type="text" style="width: 69px">
                <template #prepend>
                    <div class="label" style="width: max-content">文件</div>
                </template>
            </el-input>
            <el-button
                :disabled="!wordLevelType.length > 0"
                type="primary"
                @click="openFileWindow"
                style="margin: 0; z-index: 100; position: relative"
                v-text="fileName ? fileName : '选择文件'">
            </el-button>
        </div>

        <template #footer>
            <span class="dialog-footer">
                <el-button @click="addEnglishVocabularyByExcelDialog = false">关 闭</el-button
                ><el-button @click="AddEnglishVocabularyByExcel()" type="primary">添 加</el-button>
            </span>
        </template>
    </el-dialog>
    <!-- 修改 -->
    <el-dialog
        v-model="updateEnglishVocabularyDialog"
        width="40%"
        center
        :append-to-body="true"
        :show-close="false"
        :lock-scroll="false"
        :destroy-on-close="true"
        :align-center="true">
        <div>
            <div class="update-dialog-item">
                <el-input type="text" v-model="updateV.id" disabled>
                    <template #prepend>
                        <div class="label">ID</div>
                    </template>
                </el-input>
            </div>
            <div class="update-dialog-item">
                <el-input clearable type="text" v-model="updateV.word">
                    <template #prepend>
                        <div class="label">单词</div>
                    </template>
                </el-input>
            </div>
            <div class="update-dialog-item">
                <el-input clearable type="text" v-model="updateV.phoneticSymbol">
                    <template #prepend>
                        <div class="label">音标</div>
                    </template>
                </el-input>
            </div>
            <div class="update-dialog-item">
                <el-input clearable type="text" v-model="updateV.interpretation">
                    <template #prepend>
                        <div class="label">释义</div>
                    </template>
                </el-input>
            </div>
            <div class="update-dialog-item">
                <div class="label" style="width: max-content">单词等级类型</div>
                <el-checkbox-group v-model="updateV.levelType" class="search-item" style="width: 160px">
                    <el-checkbox label="CET4" value="CET4" />
                    <el-checkbox label="CET6" value="CET6" />
                </el-checkbox-group>
            </div>
        </div>

        <template #footer>
            <span class="dialog-footer">
                <el-button @click="updateEnglishVocabularyDialog = false">关 闭</el-button>
                <el-button @click="UpdateEnglishVocabulary()" type="primary">修 改</el-button>
            </span>
        </template>
    </el-dialog>
</template>
<script setup>
import {
    addVocabulariesByExcel,
    getVocabularies,
    updateEnglishVocabulary,
    addVocabulary,
    deleteEnglishVocabulary,
} from "@/api/EnglishVocabulary";
import { ref, onMounted, watch } from "vue";
import router from "@/router";
import { useRoute } from "vue-router";
import { ElMessage, ElMessageBox } from "element-plus";
let useRouter = useRoute();
let loading = ref(true);
/* 分页查询 */
let totalPages = ref(1); //总页数
let currPage = ref(1); //当前页
let pageSize = ref(20); //分页大小
/* 搜索 */
let EnglishVocabularys = ref([]);
let word = ref("");
let interpretation = ref("");
let levelType = ref("");
function toSearch() {
    currPage.value = 1;
    search();
}
/* 搜索筛选 */
function search() {
    loading.value = true;
    getVocabularies({
        currPage: currPage.value,
        pageSize: pageSize.value,
        word: word.value,
        interpretation: interpretation.value,
        levelType: levelType.value,
    }).then((res) => {
        loading.value = false;
        if (res.data.code == 1) {
            EnglishVocabularys.value = res.data.data.list;
            totalPages.value = res.data.data.total;
        } else {
            ElMessage.error("查询失败");
        }
    });
}

/* 删除单词 */
async function Delete(id) {
    await ElMessageBox.confirm("您确定删除该单词？", {
        confirmButtonText: "删除",
        cancelButtonText: "取消",
        type: "error",
        lockScroll: false,
        center: true,
    })
        .then(() => {
            deleteEnglishVocabulary(id).then((res) => {
                if (res.data.code == 1) {
                    ElMessage.success("删除成功");
                    search();
                } else {
                    ElMessage.error("删除失败");
                }
            });
        })
        .catch(() => {});
}
/* 添加单词 */
let addEnglishVocabularyDialog = ref(false);
let vocabulary = ref({
    word: "",
    interpretation: "",
    phoneticSymbol: "",
    levelType: [],
});
function AddEnglishVocabulary() {
    let temp = Object.assign({}, vocabulary.value);
    temp.levelType = formatLevelType(temp.levelType);
    addVocabulary(temp).then((res) => {
        if (res.data.code == 1) {
            ElMessage.success("添加成功");
            addEnglishVocabularyDialog.value = false;
            clearAll();
            search();
        } else {
            ElMessage.error("添加失败");
        }
    });
}
/* 通过excel添加单词 */
let wordLevelType = ref([]);
let addEnglishVocabularyByExcelDialog = ref(false);
function toAddEnglishVocabularyByExcel() {
    addEnglishVocabularyByExcelDialog.value = true;
}
function openFileWindow() {
    document.getElementById("file").click();
}
let key = ref(0);
let formData;
let fileName = ref("");
function uploadExcel(event) {
    let file = event.target.files[0];
    if (file != null) {
        fileName.value = file.name;
        formData = new FormData();
        formData.append("file", file);
        let a = formatLevelType(wordLevelType.value);
        formData.append("levelType", a);
    }
    key.value++;
}
function AddEnglishVocabularyByExcel() {
    addVocabulariesByExcel(formData).then((res) => {
        if (res.data.code == 1) {
            ElMessage.success("添加成功");
            addEnglishVocabularyByExcelDialog.value = false;
            fileName.value = "";
            wordLevelType.value = [];
            clearAll();
            search();
        }
    });
}
/* 更新单词 */
let updateV = ref({});
let updateEnglishVocabularyDialog = ref(false);
function toUpdateEnglishVocabulary(voca) {
    updateV.value = Object.assign({}, voca);
    updateV.value.levelType = updateV.value.levelType.split(",");
    updateEnglishVocabularyDialog.value = true;
}
function UpdateEnglishVocabulary() {
    let temp = Object.assign({}, updateV.value);
    temp.levelType = formatLevelType(temp.levelType);
    updateEnglishVocabulary(temp).then((res) => {
        if (res.data.code == 1) {
            ElMessage.success("修改成功");
            updateEnglishVocabularyDialog.value = false;
            search();
        } else {
            ElMessage.error("修改失败");
        }
    });
}
/* 格式化英语单词等级类型 */
function formatLevelType(levelType) {
    let temp = "";
    for (let i = 0; i < levelType.length; i++) {
        if (levelType[i] != "") temp += levelType[i] + ",";
    }
    return temp.lastIndexOf(",") == -1 ? temp : temp.substring(0, temp.lastIndexOf(","));
}

/* 换页操作 */
let currentChange = (page) => {
    currPage.value = page;
    router.push({
        path: "/manage/englishVocabulary",
        query: {
            page: page,
            name: name.value,
        },
    });
};
/* 清空搜索 */
function clearAll() {
    word.value = "";
    interpretation.value = "";
    levelType.value = "";
    currPage.value = 1;
    search();
}
/* 设置表格高度 */
function setTableHeight() {
    let height = document.body.scrollHeight - 200;
    return height;
}

/* 路由分页 */
let routePage = ref(useRouter);
watch(
    routePage.value,
    (newVal, oldVal) => {
        search();
    },
    {
        deep: true,
    }
);
const rowStyle = ({ row, rowIndex }) => {
    return {
        backgroundColor: "rgb(255, 255, 255,0.3)",
    };
};
onMounted(() => {
    search();
});
</script>
<style scoped lang="less">
.english-vocabulary-main-container {
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
.setting-items {
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
    width: 40px;
    text-align: center;
}
.label-r {
    font-weight: 700;
    width: 40px;
    text-align: center;
    margin-bottom: 10px;
}
.update-dialog-item {
    margin-bottom: 20px;
}
</style>
