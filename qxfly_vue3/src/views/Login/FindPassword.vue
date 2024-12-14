<template>
    <div class="login" id="login">
        <div class="bg" id="bg"></div>
        <div class="login-box">
            <div class="form-box">
                <h2 class="title">找回密码</h2>

                <div action="" class="form">
                    <div class="col">
                        <div class="label">手机号：</div>
                        <div class="inpcol">
                            <input type="text" required name="phone" placeholder="请输入手机" v-model="phone" />
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
                                v-model="pwd" />
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
                    <div class="col">
                        <label class="label" for="repassword" style="font-size: 16px">确认密码：</label>
                        <div class="inpcol">
                            <input
                                type="password"
                                id="repassword"
                                required
                                name="repassword"
                                placeholder="请确认密码"
                                v-model="rpwd" />
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
                    <div style="height: 20px; margin-top: 8px">{{ tip }}</div>
                    <div class="btus">
                        <button class="btn-login" @click="findPwd">立即找回</button>
                        <div class="rows">
                            <router-link class="item aleft" to="login">登录</router-link>
                            <router-link class="item aright" to="register">注册</router-link>
                        </div>
                    </div>
                </div>
            </div>
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
</style>
