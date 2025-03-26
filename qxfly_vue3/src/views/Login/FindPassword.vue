<template>
    <div class="login" id="login">
        <div class="bg" id="bg"></div>
        <div class="login-box">
            <form class="form-box" @submit.prevent="">
                <h2 class="title">找回密码</h2>

                <div action="" class="form">
                    <div class="col">
                        <div class="label">手机号：</div>
                        <div class="inpcol">
                            <input
                                type="text"
                                required
                                name="phone"
                                minlength="11"
                                maxlength="11"
                                placeholder="请输入手机"
                                v-model="phone" />
                        </div>
                    </div>
                    <div class="col">
                        <div class="label">验证码：</div>
                        <div class="inpcol Verification">
                            <input
                                type="text"
                                required
                                name="Verification"
                                placeholder="请输入验证码"
                                v-model="Verification" />
                            <div class="btns">
                                <button class="btn-Verification" id="btn-Verification" @click="SendCode">
                                    获取验证码
                                </button>
                            </div>
                        </div>
                    </div>
                    <div class="col">
                        <label class="label" for="password">新密码：</label>
                        <div class="inpcol">
                            <input
                                type="password"
                                id="password"
                                required
                                name="password"
                                placeholder="请输入新密码"
                                minlength="6"
                                v-model="pwd" />
                            <div class="pwd-btn" @click="showPwd('password')">
                                <i class="iconfont" v-if="!isPwdShow">&#xe6e1;</i>
                                <i class="iconfont" v-if="isPwdShow">&#xe6df;</i>
                            </div>
                        </div>
                    </div>
                    <div class="col">
                        <label class="label" for="repassword" style="font-size: 16px">确认密码：</label>
                        <div class="inpcol">
                            <input
                                type="password"
                                id="repassword"
                                required
                                name="repassword"
                                placeholder="请确认密码"
                                minlength="6"
                                v-model="rpwd" />
                            <div class="pwd-btn" @click="showPwd('repassword')">
                                <i class="iconfont" v-if="!isRePwdShow">&#xe6e1;</i>
                                <i class="iconfont" v-if="isRePwdShow">&#xe6df;</i>
                            </div>
                        </div>
                    </div>
                    <div style="height: 20px; margin-top: 8px">{{ tip }}</div>
                    <div class="btus">
                        <button class="btn-login" @click="findPwd">立即找回</button>
                        <div class="rows">
                            <router-link class="item aleft" to="login">登录</router-link>
                            <router-link class="item aright" to="register">注册</router-link
                            ><router-link class="item aleft" to="/">首页</router-link>
                        </div>
                    </div>
                </div>
            </form>
        </div>
    </div>
</template>
<script setup>
import { onMounted, onUnmounted, ref } from "vue";
import { findPassword, sendCode } from "@/api/User";
import { getGS } from "@/api/index";
import router from "@/router";
import { encrypt } from "@/utils/RSA";
var isTip = ref(false); // 错误提示
var tip = ref(); // 错误提示信息
let phone = ref(null);
let Verification = ref(null);
let pwd = ref(null);
let rpwd = ref(null);
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
/* 找回密码 */
async function findPwd() {
    if (phone.value == null || phone.value == "") {
        isTip.value = true;
        tip.value = "请输入手机号";
        setTimeout(() => {
            tip.value = null;
        }, 2000);
        return;
    }
    if (Verification.value == null || Verification.value == "") {
        isTip.value = true;
        tip.value = "请输入验证码";
        setTimeout(() => {
            tip.value = "请输入验证码";
        }, 2000);
        return;
    }
    if (pwd.value == null || pwd.value == "") {
        isTip.value = true;
        tip.value = "请输入新密码";
        setTimeout(() => {
            tip.value = null;
        }, 2000);
        return;
    }
    if (rpwd.value == null || rpwd.value == "") {
        isTip.value = true;
        tip.value = "请确认密码";
        setTimeout(() => {
            tip.value = null;
        }, 2000);
        return;
    }
    if (pwd.value != rpwd.value) {
        isTip.value = true;
        tip.value = "两次密码不一致";
        setTimeout(() => {
            tip.value = null;
        }, 2000);
        return;
    }
    var reg = /^(?=.*[a-zA-Z])(?=.*\d).{6,}$/;
    if (!reg.test(pwd.value)) {
        isTip.value = true;
        tip.value = "密码至少包含字母、数字且长度为6";
        setTimeout(() => {
            tip.value = null;
        }, 2000);
        return;
    }
    tip.value = null;
    // 发送请求

    let encodePassword = ref(null);
    let publicKey = ref(null);
    await getGS().then((res) => {
        if (res.data.code == "1") {
            publicKey.value = res.data.data;
            encodePassword.value = encrypt(pwd.value, publicKey.value);
        }
    });
    findPassword({
        phone: phone.value,
        password: encodePassword.value,
        code: Verification.value,
        salt: publicKey.value,
    }).then((res) => {
        if (res.data.code == 1) {
            isTip.value = true;
            tip.value = "修改成功";
            setTimeout(() => {
                tip.value = null;
                router.replace("/login");
            }, 1500);
        } else {
            tip.value = res.data.msg;
            setTimeout(() => {
                tip.value = null;
            }, 2000);
        }
    });
}
/* 加载背景图片 */
function loadBackGround() {
    let bgfile = require.context("@/assets/img/bg", false, /.webp$/).keys();
    let bgf = bgfile[getRandomInt(0, bgfile.length - 1)];
    let bg = document.getElementById("bg");
    bg.style.backgroundImage = "url('" + require("@/assets/img/bg/" + bgf.split("/")[1]) + "')";
}
function getRandomInt(min, max) {
    min = Math.ceil(min);
    max = Math.floor(max);
    return Math.floor(Math.random() * (max - min + 1)) + min;
}
/* 验证码倒计时 */
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
    }
}
/* 发送验证码 */
function SendCode() {
    var phonereg = /0?(13|14|15|17|18|19)[0-9]{9}/;
    if (!phonereg.test(phone.value)) {
        tip.value = "请输入正确的手机号";
        setTimeout(() => {
            tip.value = null;
        }, 2000);
        return;
    }
    if (phone.value != null && phone.value != "") {
        sendCode({ phone: phone.value }).then((res) => {
            if (res.data.code == 1) {
                sleepCode();
            } else {
                tip.value = res.data.msg;
                setTimeout(() => {
                    tip.value = null;
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

// 监听回车键执行事件
function keyDown(e) {
    // 回车则执行登录方法 enter键的ASCII是13
    if (e.keyCode === 13) {
        findPwd(); // 需要执行的方法方法
    }
}
onMounted(() => {
    loadBackGround();
    window.addEventListener("keydown", keyDown);
});
onUnmounted(() => {
    /* 移除监听按键事件 */
    window.removeEventListener("keydown", keyDown, false);
    clearTimeout(timer);
});
</script>
<style lang="less" scoped>
.login {
    width: 100%;
    height: 100%;
}
.bg {
    width: 100%;
    height: 100%;
    z-index: -1;
    position: fixed;
    background-size: cover;
    background-repeat: no-repeat;
}
.bg::before {
    content: "";
    position: absolute;
    top: 0;
    left: 0;
    right: 0;
    bottom: 0;
    z-index: -1;
    -webkit-backdrop-filter: blur(10px);
    backdrop-filter: blur(10px);
}
.login-box {
    width: 100%;
    height: 100%;
    display: flex;
    align-items: center;
    justify-content: center;
}
.form-box {
    width: 450px;
    min-height: 400px;
    background: rgba(255, 255, 255, 0.5);
    border-radius: 8px;
    padding: 15px;
    box-sizing: border-box;
    border: 2px solid #8f8f8f6e;
}
.title {
    text-align: center;
    font-size: 30px;
}
.form {
    padding: 15px;
    margin-top: 20px;
}
.col {
    width: 100%;
    margin-top: 15px;
}
.label {
    font-size: 20px;
    color: #000;
    font-weight: 800;
}
.inpcol {
    position: relative;
    margin-top: 5px;
    flex: 1;
}
input {
    height: 45px;
    width: 100%;
    padding: 0 10px;
    box-sizing: border-box;
    outline: none;
    border: 1px solid #000;
    border-radius: 5px;
    font-size: 18px;
    line-height: 45px;
    color: #000;
    font-weight: 700;
    background: transparent;
}
.btus {
    margin-top: 50px;
    user-select: none;
}
.btn-login {
    width: 100%;
    height: 45px;
    background-image: linear-gradient(to right, #a18cd1, #fbc2eb);
    border: none;
    outline: none;
    border-radius: 5px;
    font-size: 18px;
    cursor: pointer;
    font-weight: 700;
}
.btn-login:hover {
    opacity: 0.8;
}
.rows {
    display: flex;
    justify-content: space-between;
}
.item {
    margin-top: 10px;
    font-size: 14px;
    color: #000;
    font-weight: 600;
    text-decoration: none;
}
.aleft {
    color: #4c00ff;
}
.aright {
    color: #ff00b7;
}
.Verification {
    display: flex;
}
.btn-Verification {
    margin-left: 10px;
    width: 100px;
    height: 45px;
    border: 1px solid #000;
    border-radius: 5px;
    cursor: pointer;
}
.btn-Verification:hover {
    opacity: 0.8;
}
.pwd-btn {
    position: absolute;
    right: 12px;
    top: 12px;
    background-color: #00000000;
    border: none;
    cursor: pointer;
}

.iconfont {
    font-family: "iconfont" !important;
    font-style: normal;
    color: #000;
    font-size: 20px;
    user-select: none;
}
</style>
