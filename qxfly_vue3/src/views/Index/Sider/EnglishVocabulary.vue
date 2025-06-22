<template>
    <CardView>
        <div class="englishVocabulary-sider-main">
            <i class="refresh-icon" @click="getVocabulary">&#xe6f5;</i>
            <div v-for="v in vocabularies" class="vocabulary-item">
                <div class="item word">
                    <div>{{ v.word }}</div>
                </div>
                <div class="item phoneticSymbol" v-text="v.phoneticSymbol"></div>
                <div class="item interpretation" v-for="i in v.interpretation">
                    <div class="cx">{{ i.cx }}</div>
                    <span class="fy">{{ i.fy }}</span>
                </div>
            </div>
        </div>
    </CardView>
</template>

<script setup>
import { onMounted, ref } from "vue";
import CardView from "@/components/CardView.vue";
import { getRandomEnglishVocabularies } from "@/api/EnglishVocabulary";
import { ElMessage } from "element-plus";
let vocabularies = ref([]);
let interval = false;
function getVocabulary() {
    if (!interval) {
        interval = true;
        getRandomEnglishVocabularies(1).then((res) => {
            vocabularies.value = res.data.data;
            formatInterpretation();
        });
        setTimeout(() => {
            interval = false;
        }, 2000);
    } else {
        ElMessage.warning({
            message: "请勿频繁刷新",
            offset: 120,
        });
    }
}
function formatInterpretation() {
    vocabularies.value.forEach((v) => {
        let fy = v.interpretation.split(/[a-z]+\.+/g); // 翻译
        let cx = v.interpretation.match(/[a-z]+\.+/g); // 词性
        let match = [];
        let i = 0;
        let j = 0;
        for (; i < fy.length && j < cx.length; i++, j++) {
            if (fy[i] == "") i++;
            match.push({ cx: cx[j], fy: fy[i] });
        }
        v.interpretation = match;
    });
}
onMounted(() => {
    getVocabulary();
});
</script>

<style scoped>
.vocabulary-item {
    display: flex;
    align-items: center;
    flex-direction: column;
    justify-content: center;
}
.item {
    margin-bottom: 4px;
}
.word {
    font-weight: 700;
    font-size: 22px;
    margin-bottom: 4px;
    display: flex;
    align-items: center;
    justify-content: center;
}
.phoneticSymbol {
    text-align: center;
}
.interpretation {
    /* font-weight: 700; */
    width: 100%;
}
.cx {
    border-radius: 4px;
    display: inline-block;
    width: 50px;
    text-align: center;
    margin-right: 2px;
    background-color: var(--main-theme-color-blue);
    color: #fff;
    font-weight: 700;
}
.fy {
}
.refresh-icon {
    font-family: "iconfont" !important;
    font-style: normal;
    color: #000;
    font-size: 15px;
    user-select: none;
    position: absolute;
    right: 10px;
}
.refresh-icon:hover {
    cursor: pointer;
    color: #409eff;
}
</style>
