<template>
    <div class="main">
        <div class="item">
            <div class="item-word">
                {{ word.word }}
            </div>
            <div class="item-definition" v-for="d in word.definition" :key="d">
                <span class="item-definition-1">{{ d.split(" ")[0] }}</span>
                <span class="item-definition-2">{{ d.split(" ")[1] }}</span>
            </div>
        </div>
    </div>
</template>

<script setup>
import { onMounted, ref } from "vue";
import { addWord, getWord } from "@/api/Saying";
let word = ref({ word: "", definition: "" });
let a = ref();
function GetWord() {
    getWord(true, true).then((res) => {
        a = res.data.split(": ");
        word.value = { word: a[0], definition: a[1] };
        word.value.definition = word.value.definition.split("; ");
        // a.value = JSON.parse(word.value);
    });
}
onMounted(() => {
    // console.log(process.env.URL);
    GetWord();
});
</script>

<style scoped>
.main {
    margin: 0;
    margin-bottom: 20px;
    padding: 5px 10px;
    border-radius: 4px;
    background-color: transparent;
    box-shadow: 0 3px 8px 6px rgba(7, 17, 27, 0.08);
    transition: all 0.3s ease;
}
.item-word {
    font-weight: 700;
    font-size: 24px;
    text-align: center;
}
.item-definition {
    margin-top: 10px;
}
.item-definition-1 {
    font-weight: 700;
    margin-right: 10px;
}
</style>
