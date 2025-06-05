<template>
    <div class="register">
        <div class="login-main">
            <div class="bg" id="bg"></div>
            <div class="login-box">
                <form class="form-box" @submit.prevent="">
                    <h2 class="title">注册</h2>
                    <div class="form">
                        <div class="col">
                            <label class="label" for="account">用户名：</label>
                            <div class="inpcol">
                                <input
                                    type="text"
                                    id="account"
                                    maxlength="20"
                                    minlength="5"
                                    required
                                    name="name"
                                    placeholder="用户名"
                                    ref="inputdata"
                                    v-model="username" />
                            </div>
                        </div>
                        <div class="col">
                            <label class="label" for="phone">手机号：</label>
                            <div class="inpcol">
                                <input
                                    type="text"
                                    id="phone"
                                    maxlength="11"
                                    minlength="11"
                                    required
                                    name="name"
                                    placeholder="手机号"
                                    ref="inputdata"
                                    v-model="phone" />
                            </div>
                        </div>
                        <div class="col">
                            <div class="label">验证码：</div>
                            <div class="inpcol Verification">
                                <input
                                    type="text"
                                    name="Verification"
                                    placeholder="请输入验证码"
                                    v-model="Verification" />
                                <div class="btns">
                                    <button class="btn-Verification" id="btn-Verification" @click.prevent="SendCode">
                                        获取验证码
                                    </button>
                                </div>
                            </div>
                        </div>
                        <div class="col">
                            <label class="label" for="password">密&emsp;码：</label>
                            <div class="inpcol">
                                <input
                                    type="password"
                                    id="password"
                                    required
                                    name="password"
                                    placeholder="请输入密码"
                                    minlength="6"
                                    v-model="password" />
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
                                    v-model="repassword" />
                                <div class="pwd-btn" @click="showPwd('repassword')">
                                    <i class="iconfont" v-if="!isRePwdShow">&#xe6e1;</i>
                                    <i class="iconfont" v-if="isRePwdShow">&#xe6df;</i>
                                </div>
                            </div>
                        </div>
                        <div class="col">
                            <label class="label" for="invite">邀请码：</label>
                            <div class="inpcol">
                                <input
                                    type="text"
                                    id="invite"
                                    name="invite"
                                    placeholder="邀请码(可选)，与验证码可二选一"
                                    v-model="invite" />
                            </div>
                        </div>
                        <div style="height: 20px">{{ msg }}</div>
                        <div class="btus">
                            <button class="btn-login" type="submit" @click="register">立即注册</button>
                            <div class="rows">
                                <router-link class="item aleft" to="login">登录</router-link>
                                <router-link class="item aright" to="findpassword">找回密码</router-link>
                                <router-link class="item aleft" to="/">首页</router-link>
                            </div>
                        </div>
                    </div>
                </form>
            </div>
        </div>
    </div>
</template>

<script setup>
import { Register, getGS } from "@/api/index";
import { sendCode } from "@/api/User";
import { onMounted, onUnmounted, ref } from "vue";
import router from "@/router";
import { encrypt } from "@/utils/RSA";
var username = ref(null); //账号
var password = ref(null); //密码
var repassword = ref(null); //重复密码
let Verification = ref(""); //验证码
var invite = ref(""); //邀请码
var msg = ref(""); // 错误提示信息
var phone = ref(""); // 手机号
var isTip = ref(false); // 错误提示
/* 注册 */
async function register() {
    var flag = false;
    if (username.value != null && password.value != null && repassword.value != null && phone != null) {
        if (username.value.length < 5) {
            isTip.value = 1;
            msg.value = "用户名不能少于5位";
            setTimeout(() => {
                msg.value = null;
            }, 2000);
            return;
        }
        var usernamereg = /^[0-9]*$/;
        if (usernamereg.test(username.value)) {
            isTip.value = 1;
            msg.value = "用户名不能为纯数字";
            setTimeout(() => {
                msg.value = null;
            }, 2000);
            return;
        }
        var phonereg = /0?(13|14|15|17|18|19)[0-9]{9}/;
        if (!phonereg.test(phone.value)) {
            isTip.value = 1;
            msg.value = "请输入正确的手机号";
            setTimeout(() => {
                msg.value = null;
            }, 2000);
            return;
        }
        if (invite.value == null && invite.value == "") {
            if (Verification.value == null || Verification.value == "") {
                isTip.value = 1;
                msg.value = "请输入验证码或邀请码";
                setTimeout(() => {
                    msg.value = null;
                }, 2000);
                return;
            }
        }
        if (password.value != repassword.value) {
            isTip.value = 1;
            msg.value = "两次密码不一致";
            setTimeout(() => {
                msg.value = null;
            }, 2000);
            return;
        }
        var reg = /^(?=.*[a-zA-Z])(?=.*\d).{6,}$/; //使用正则表达式匹配密码
        if (!reg.test(password.value)) {
            isTip.value = 1;
            msg.value = "密码至少包含字母、数字且长度为6";
            setTimeout(() => {
                msg.value = null;
            }, 2000);
            return;
        }
    } else {
        isTip.value = 1;
        msg.value = "请填写完整信息";
        return;
    }
    flag = true;
    if (flag) {
        let encodePassword = ref(null);
        let publicKey = ref(null);
        await getGS().then((res) => {
            if (res.data.code == "1") {
                publicKey.value = res.data.data;
                encodePassword.value = encrypt(password.value, publicKey.value);
            }
        });
        const res = await Register({
            username: username.value,
            password: encodePassword.value,
            phone: phone.value,
            invite: invite.value,
            code: Verification.value,
            salt: publicKey.value,
        });
        if (res.data.code == 1) {
            isTip.value = 1;
            msg.value = res.data.data;
            setTimeout(() => {
                router.replace("/login");
            }, 2000);
        } else {
            msg.value = res.data.msg;
            isTip.value = 1;
        }
    }
}
// 监听回车键执行事件
function keyDown(e) {
    if (e.keyCode === 13) {
        register(); // 需要执行的方法方法
    }
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
        msg.value = "请输入正确的手机号";
        setTimeout(() => {
            msg.value = null;
        }, 2000);
        return;
    }
    if (phone.value != null && phone.value != "") {
        sendCode({ phone: phone.value }).then((res) => {
            if (res.data.code == 1) {
                sleepCode();
            } else {
                msg.value = res.data.msg;
                setTimeout(() => {
                    msg.value = null;
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
    loadBackGround();
    /* 添加监听按键事件 */
    window.addEventListener("keydown", keyDown);
});
onUnmounted(() => {
    /* 移除监听按键事件 */
    window.removeEventListener("keydown", keyDown, false);
    clearTimeout(timer);
});
</script>

<style scoped>
.register {
    width: 100%;
    height: 100%;
}
.login-main {
    width: 100%;
    height: 100%;
    display: flex;
    justify-content: center;
    align-items: center;
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
    display: flex;
    margin-top: 15px;
    align-items: center;
    padding-bottom: 15px;
}
.label {
    text-align: right;
    width: 80px;
    font-size: 20px;
    color: #000;
    font-weight: 800;
}
.inpcol {
    position: relative;
    margin-top: 0px;
    flex: 1;
}
input {
    height: 45px;
    width: 100%;
    padding: 3px 10px 0 10px;
    box-sizing: border-box;
    outline: none;
    border: 1px solid #000;
    border-radius: 4px;
    font-size: 18px;
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
    border-radius: 4px;
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
    cursor: pointer;
}
.aright {
    color: #ff00b7;
    cursor: pointer;
}
.pwd-btn {
    position: absolute;
    right: 12px;
    top: 12px;
    background-color: #00000000;
    border: none;
    cursor: pointer;
}
.Verification {
    display: flex;
}
.btn-Verification {
    margin-left: 10px;
    width: 100px;
    height: 45px;
    border: 1px solid #000;
    border-radius: 4px;
    cursor: pointer;
}
.btn-Verification:hover {
    opacity: 0.8;
}

.iconfont {
    font-family: "iconfont" !important;
    font-style: normal;
    color: #000;
    font-size: 20px;
    user-select: none;
}
</style>
