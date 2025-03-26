<template>
    <div class="login">
        <div class="login-main">
            <div class="bg" id="bg"></div>
            <div class="login-box">
                <form class="form-box" @submit.prevent="">
                    <h2 class="title">欢迎登录</h2>
                    <div class="form">
                        <div class="col">
                            <label for="account" class="label">账号：</label>
                            <div class="inpcol">
                                <input
                                    type="text"
                                    maxlength="20"
                                    id="account"
                                    required
                                    name="account"
                                    placeholder="请输入用户名或手机号"
                                    v-model="username" />
                            </div>
                        </div>
                        <div class="col">
                            <label for="password" class="label">密码：</label>
                            <div class="inpcol">
                                <input
                                    type="password"
                                    id="password"
                                    name="password"
                                    required="password"
                                    placeholder="请输入密码"
                                    v-model="password" />
                                <div class="pwd-btn" @click="showPwd('password')">
                                    <i class="iconfont" v-if="!isPwdShow">&#xe6e1;</i>
                                    <i class="iconfont" v-if="isPwdShow">&#xe6df;</i>
                                </div>
                            </div>
                        </div>
                        <div class="remember">
                            <div>
                                {{ msg }}
                            </div>
                            <div style="display: flex; align-items: center">
                                <input
                                    type="checkbox"
                                    name="remember"
                                    id="remember"
                                    class="remember-input"
                                    v-model="remember" />
                                <label for="remember" class="remember-label">自动登陆</label>
                            </div>
                        </div>

                        <div class="btus">
                            <button class="btn-login" id="btn-login" type="submit" @click="login">
                                {{ loginText }}
                            </button>
                            <div class="rows">
                                <router-link class="item aleft" to="register">注册</router-link>
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
import { Login, getGS } from "@/api/index";
import router from "@/router";
import md5 from "js-md5";
import { onMounted, ref, onBeforeMount, onUnmounted } from "vue";
import { encrypt } from "@/utils/RSA";

var username = ref(null); //账号
var password = ref(null); //密码
var isError = ref(); // 错误提示
var msg = ref(); // 错误提示信息
var loginText = ref("登录");
var remember = ref(true); // 记住密码

/* 登陆 */
async function login() {
    if (username.value == null || username.value == "" || password.value == null || password.value == "") {
        msg.value = "账号或密码不能为空";
        setTimeout(() => {
            msg.value = null;
        }, 2000);
        return;
    }
    loginText.value = "正在登录";
    let loginBtn = ref(document.getElementById("btn-login"));
    loginBtn.value.disabled = true;
    let encodePassword = ref(null);
    let publicKey = ref(null);
    await getGS().then((res) => {
        if (res.data.code == "1") {
            publicKey.value = res.data.data;
            encodePassword.value = encrypt(password.value, publicKey.value);
        }
    });
    const res = await Login({
        username: username.value,
        password: encodePassword.value,
        salt: publicKey.value,
    });
    /* 返回状态码，1 为登录成功， 0 为登录失败*/
    if (res.data.code == "1") {
        isError.value = 0;
        let resBody = res.data.data;
        localStorage.setItem(md5("token"), resBody.token);
        localStorage.setItem(md5("refreshToken"), resBody.refreshToken);
        localStorage.setItem("username", resBody.username);
        localStorage.setItem("uid", resBody.uid);
        localStorage.setItem(md5("islogin"), md5("true"));
        if (remember.value) {
            localStorage.setItem("autologin", true);
        } else {
            sessionStorage.setItem("autologin", remember.value);
        }

        setTimeout(() => {
            location.reload();
        }, 1000);
    } else {
        loginText.value = "登录";
        msg.value = res.data.msg;
        isError.value = 1;
        loginBtn.value.disabled = false;
    }
}
// 监听回车键执行事件
function keyDown(e) {
    // 回车则执行登录方法 enter键的ASCII是13
    if (e.keyCode === 13) {
        login(); // 需要执行的方法方法
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
onBeforeMount(() => {
    if (localStorage.getItem(md5("token")) != null && localStorage.getItem(md5("islogin")) == md5("true"))
        router.replace("/");
});
onMounted(() => {
    loadBackGround();
    /* 添加监听按键事件 */
    window.addEventListener("keydown", keyDown);
});
onUnmounted(() => {
    /* 移除监听按键事件 */
    window.removeEventListener("keydown", keyDown, false);
});
</script>

<style scoped>
.login {
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
    margin-top: 15px;
    padding-bottom: 15px;
}
.label {
    font-size: 20px;
    color: #000;
    font-weight: 800;
}
.inpcol {
    position: relative;
    width: 100%;
    margin-top: 5px;
}
.inpcol input {
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
    user-select: none;
    font-weight: 700;
}
.btn-login:hover {
    opacity: 0.8;
}
.rows {
    display: flex;
    justify-content: space-between;
    margin-top: 10px;
}
.item {
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
.remember {
    display: flex;
    align-items: center;
    justify-content: space-between;
}
.remember-input {
    margin-right: 4px;
}
.remember-label {
    display: block;
    padding-bottom: 2px;
}

.iconfont {
    font-family: "iconfont" !important;
    font-style: normal;
    font-size: 20px;
    user-select: none;
    color: #000;
}
</style>
