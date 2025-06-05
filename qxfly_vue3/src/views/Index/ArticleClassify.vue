<template>
    <div class="classify-main">
        <div class="part classify-part">
            <div class="header">
                <div class="label">
                    <span> 分类：</span>
                </div>
                <div class="tag-search">
                    <MyInput :clear="true" v-model="classifySearch"></MyInput>
                </div>
            </div>
            <div class="classify">
                <div
                    class="item classify-item"
                    :class="{ classifyActive: isActive(c, 0) }"
                    v-for="c in Classifys"
                    :key="c.id"
                    @click="clickItem(c.name, 0)">
                    {{ c.name }}
                </div>
            </div>
        </div>

        <div class="part tags-part">
            <div class="header">
                <div class="label">
                    <span> 标签：</span>
                </div>
                <div class="tag-search">
                    <MyInput :clear="true" v-model="tagSearch"></MyInput>
                </div>
            </div>

            <div class="tags">
                <div
                    class="item tags-item"
                    :class="{ tagActive: isActive(t, 1) }"
                    v-for="t in Tags"
                    :key="t.id"
                    @click="clickItem(t.name, 1)">
                    {{ t.name }}
                </div>
            </div>
        </div>
        <div style="text-align: center; font-size: 14px; margin-top: 5px; font-weight: 700">共 {{ total }} 篇文章</div>
    </div>
</template>

<script setup>
import { onMounted, ref, watch } from "vue";
import { useRoute } from "vue-router";
import { getClassifies, getTags } from "@/api/Article/index";
import MyInput from "@/components/MyInput.vue";
let useRouter = useRoute();
const props = defineProps({
    /* 总记录数 */
    total: {
        type: Number,
        // default: 0,
    },
});
let emit = defineEmits(["classify", "tag"]);
/* 获取分类 */
let allClassifys = ref([]);
let Classifys = ref([]);
function GetClassifies() {
    getClassifies().then((res) => {
        allClassifys.value = res.data.data;
        allClassifys.value = allClassifys.value.sort((a, b) => {
            if (a.name < b.name) {
                return -1;
            }
            if (a.name > b.name) {
                return 1;
            }
            return 0;
        });
        sleepC(allClassifys.value);
    });
}
let C = 0;
function sleepC(data) {
    if (C < data.length)
        setTimeout(() => {
            Classifys.value.push(data[C++]);
            sleepC(data);
        }, 20);
}
/* 搜索分类 */
let classifySearch = ref("");
watch(classifySearch, (oldVal, newVal) => {
    Classifys.value = allClassifys.value.filter((i) =>
        i.name.toLowerCase().includes(classifySearch.value.toLowerCase())
    );
});
/* 标签 */
let allTags = ref([]);
let Tags = ref([]);
function GetTags() {
    getTags().then((res) => {
        allTags.value = res.data.data;
        allTags.value = allTags.value.sort((a, b) => {
            if (a.name < b.name) {
                return -1;
            }
            if (a.name > b.name) {
                return 1;
            }
            return 0;
        });

        sleepT(allTags.value);
    });
}
let T = 0;
function sleepT(data) {
    let t = Math.floor(Math.random() * 10) + 10;
    if (T < data.length)
        setTimeout(() => {
            Tags.value.push(data[T++]);
            sleepT(data);
        }, t);
}
/* 搜索标签 */
let tagSearch = ref("");
watch(tagSearch, (oldVal, newVal) => {
    Tags.value = allTags.value.filter((i) => i.name.toLowerCase().includes(tagSearch.value.toLowerCase()));
});
let ActiveClassify = ref("");
let ActiveTag = ref([]);
/**
 *
 * @param item 名称
 * @param type 0:分类；1：标签
 */
function clickItem(item, type) {
    if (type == 0) {
        if (ActiveClassify.value != item) {
            ActiveClassify.value = item;
            emit("classify", item);
        } else {
            ActiveClassify.value = "";
            emit("classify", "");
        }
    } else {
        if (!ActiveTag.value.includes(item)) {
            ActiveTag.value.push(item);
            emit("tag", ActiveTag.value);
        } else {
            ActiveTag.value = ActiveTag.value.filter((i) => i != item);
            emit("tag", ActiveTag.value);
        }
    }
}
let isActive = (item, type) => {
    if (ActiveClassify.value.includes(item.name) && type == 0) {
        return true;
    }
    if (ActiveTag.value.includes(item.name) && type == 1) {
        return true;
    }
};
function routerRecovery() {
    if (useRouter.query.classify != null) {
        ActiveClassify.value = useRouter.query.classify;
    }

    if (useRouter.query.tag != null) {
        ActiveTag.value = useRouter.query.tag.split(",");
    }
}
onMounted(() => {
    routerRecovery();
    GetClassifies();
    GetTags();
});
</script>

<style scoped lang="less">
.classify-main {
    border-radius: 4px;
    background-color: var(--main-background-color);
    box-shadow: 0 3px 8px 6px rgba(7, 17, 27, 0.08);
    transition: all 0.3s ease;
    padding: 6px;
    margin-bottom: 20px;
}
.classify-main:hover {
    box-shadow: 0 3px 8px 6px rgba(7, 17, 27, 0.14);
}
.part {
    margin-bottom: 6px;
    border-bottom: 2px dashed #888888;
}
.header {
    display: flex;
    // justify-content: center;
    align-items: center;
    margin-bottom: 6px;
}
.label {
    font-size: large;
    font-weight: 700;
}
.classify,
.tags {
    display: flex;
    flex-wrap: wrap;
    margin-bottom: 6px;
    min-height: 35px;
    max-height: 105px;
    overflow: auto;
}
.tags {
}
.label {
    font-size: large;
    font-weight: 700;
}
.classify,
.tags {
    display: flex;
    flex-wrap: wrap;
    margin-bottom: 6px;
    min-height: 35px;
    max-height: 105px;
    overflow: auto;
}
.tags {
    // height: 70px;
    overflow: auto;
    height: auto;
}
.item {
    min-width: 40px;
    width: max-content;
    margin: 4px 6px;
    cursor: pointer;
    border-radius: 4px;
    border: 1px solid var(--main-theme-color-blue);
    padding: 2px 6px;
    height: 27px;
    transition: all 0.2s ease;
    overflow: hidden;
    text-overflow: ellipsis;
    text-align: center;
    white-space: nowrap;
    background-color: var(--main-background-color);
    font-weight: 700;
}
.tagActive {
    color: #fff;
    background-color: var(--main-theme-color-blue);
}
.item.tags-item:hover {
    color: #000;
    background-color: var(--hover-color-blue);
}
.classifyActive {
    color: #fff;
    background-color: var(--main-theme-color-orange);
}
.item.classify-item {
    border: 1px solid var(--main-theme-color-orange);
}
.item.classify-item:hover {
    color: #000;
    background-color: var(--hover-color-orange);
}
</style>
