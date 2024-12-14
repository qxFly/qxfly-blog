<template>
    <div class="container">
        <!--输入框-->
        <input
            id="input"
            type="text"
            :placeholder="placeholder"
            @input="showBtn"
            @focus="Focus"
            @focusout="Focusout"
            @mouseover="showBtn"
            :value="modelValue" />
        <div>
            <button v-if="btnShow" @click="clearText()"></button>
        </div>
    </div>
</template>
<script setup>
import { ref, defineProps, defineEmits, onMounted } from "vue";
var btnShow = ref(false);
const props = defineProps({
    //定义是否显示清除按钮
    clear: {
        type: Boolean,
        default: false,
    },
    //v-model默认中value
    modelValue: {
        type: String,
    },
    //提示语
    placeholder: {
        type: String,
        default: "",
    },
});
const emit = defineEmits(["update:modelValue", "enter"]);
function Focus() {
    window.addEventListener("keydown", keyDown);
}
function Focusout() {
    window.removeEventListener("keydown", keyDown);
}
// 监听回车键执行事件
function keyDown(e) {
    // 回车则执行登录方法 enter键的ASCII是13
    if (e.keyCode === 13) {
        emit("enter");
    }
}

function showBtn(e) {
    if (props.clear == true && e.target.value != "") {
        btnShow.value = true;
        emit("update:modelValue", e.target.value);
    } else {
        btnShow.value = false;
        emit("update:modelValue", e.target.value);
    }
}
function clearText() {
    emit("update:modelValue", "");
    document.getElementById("input").focus();
    btnShow.value = false;
}
onMounted(() => {});
</script>
<style>
.container {
    width: 100%;
    display: flex;
    align-items: center;
    justify-content: center;
    overflow: hidden;
}

/*设置输入框的默认样式*/
.container input {
    width: 100%;
    padding: 6px;
    padding-right: 24px;
    border-radius: 4px;
    border: 1px solid #00000000;
    outline: none;
}
/*设置输入框选中时的样式*/
.container input:focus {
    border: 1px #000 solid;
}
.container div {
    width: 20px;
    height: 20px;
}
.container button {
    width: 20px;
    height: 20px;
    position: relative;
    right: 24px;
    border: none;
    outline: none;
    transition: 0.4s;
    background-color: #00000000;
}
.container button::before {
    content: "\e601";
    font-family: "iconfont";
    font-size: 18px;
    font-weight: 700;
}
</style>
