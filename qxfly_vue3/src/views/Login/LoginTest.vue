<template>
  <div class="body">
    <div class="main-box">
      <!-- 注册 -->
      <div :class="['container', 'container-register', { 'is-txl': isLogin }]">
        <form @submit.prevent="">
          <h2 class="title">注册</h2>
          <input class="form__input" type="text" placeholder="请输入用户名" />
          <input class="form__input" type="text" placeholder="请输入邮箱" />
          <input class="form__input" type="password" placeholder="请输入密码" />
          <input class="form__input" type="password" placeholder="请输入密码" />
          <div class="form__button" type="submit">立即注册</div>
        </form>
      </div>
      <!-- 登录 -->
      <div :class="['container', 'container-login', { 'is-txl is-z200': isLogin }]">
        <form @submit.prevent="">
          <h2 class="title">登录</h2>
          <input
            class="form__input"
            type="text"
            maxlength="20"
            required
            id="account"
            name="account"
            placeholder="请输入用户名或手机号"
            v-model="username" />
          <input
            class="form__input"
            type="password"
            id="password"
            name="password"
            required="password"
            v-model="password"
            placeholder="请输入密码" />
          <div class="remember">
            <div style="display: flex; align-items: center">
              <input type="checkbox" name="remember" id="remember" class="remember-input" v-model="remember" />
              <label for="remember" class="remember-label">自动登陆</label>
            </div>
            <div>
              {{ msg }}
            </div>
          </div>
          <!-- <div class="form__button" type="submit">立即登录</div> -->
          <button class="form__button" id="btn-login" type="submit" @click="login">
            {{ loginText }}
          </button>
        </form>
      </div>
      <div :class="['switch', { login: isLogin }]">
        <div class="switch__circle"></div>
        <div class="switch__circle switch__circle_top"></div>
        <div class="switch__container">
          <h2>{{ isLogin ? "您好 !" : "欢迎回来 !" }}</h2>
          <p>
            {{
              isLogin
                ? "如果您还没有账号，请点击下方立即注册按钮进行账号注册"
                : "如果您已经注册过账号，请点击下方立即登录按钮进行登录"
            }}
          </p>
          <div class="form__button" @click="isLogin = !isLogin">
            {{ isLogin ? "立即注册" : "立即登录" }}
          </div>
        </div>
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
let isLogin = ref(true);
var username = ref(null); //账号
var password = ref(null); //密码
var isError = ref(); // 错误提示
var msg = ref(); // 错误提示信息
var loginText = ref("立即登录");
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
  const res = await Login({ username: username.value, password: encodePassword.value, salt: publicKey.value });
  if (res.data.code == "1") {
    isError.value = 0;
    let resBody = res.data.data;
    localStorage.setItem(md5("token"), resBody.token);
    localStorage.setItem(md5("refreshToken"), resBody.refreshToken);
    localStorage.setItem("username", resBody.username);
    localStorage.setItem("uid", resBody.uid);
    localStorage.setItem(md5("islogin"), md5("true"));
    if (remember.value) localStorage.setItem("autologin", true);
    else sessionStorage.setItem("autologin", remember.value);
    location.replace("/");
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
// function loadBackGround() {
//   let bgfile = require.context("@/assets/img/bg", false, /.webp$/).keys();
//   let bgf = bgfile[getRandomInt(0, bgfile.length - 1)];
//   let bg = document.getElementById("bg");
//   bg.style.backgroundImage = "url('" + require("@/assets/img/bg/" + bgf.split("/")[1]) + "')";
// }
// function getRandomInt(min, max) {
//   min = Math.ceil(min);
//   max = Math.floor(max);
//   return Math.floor(Math.random() * (max - min + 1)) + min;
// }

/* 显示密码 */
let isPwdShow = ref(false);
let isRePwdShow = ref(false);
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
  //   loadBackGround();
  /* 添加监听按键事件 */
  window.addEventListener("keydown", keyDown);
});
onUnmounted(() => {
  /* 移除监听按键事件 */
  window.removeEventListener("keydown", keyDown, false);
});
</script>

<style lang="scss" scoped>
.body {
  width: 100%;
  height: 100vh;
  display: flex;
  justify-content: center;
  align-items: center;
  font-family: "Montserrat", sans-serif;
  font-size: 12px;
  //   background-image: url("@/assets/images/background.jpg");
  color: #a0a5a8;
}
.main-box {
  position: relative;
  width: 1000px;
  min-width: 1000px;
  min-height: 600px;
  height: 600px;
  padding: 25px;
  background-color: #ecf0f3;
  box-shadow: 1px 1px 100px 10px #ecf0f3;
  border-radius: 12px;
  overflow: hidden;

  .container {
    display: flex;
    justify-content: center;
    align-items: center;
    position: absolute;
    top: 0;
    width: 600px;
    height: 100%;
    padding: 25px;
    background-color: #ecf0f3;
    transition: all 1.25s;

    form {
      display: flex;
      justify-content: center;
      align-items: center;
      flex-direction: column;
      width: 100%;
      height: 100%;
      color: #a0a5a8;

      .form__icon {
        object-fit: contain;
        width: 30px;
        margin: 0 5px;
        opacity: 0.5;
        transition: 0.15s;

        &:hover {
          opacity: 1;
          transition: 0.15s;
          cursor: pointer;
        }
      }

      .title {
        font-size: 34px;
        font-weight: 700;
        line-height: 3;
        color: #181818;
      }

      .text {
        margin-top: 30px;
        margin-bottom: 12px;
      }

      .form__input {
        width: 350px;
        height: 40px;
        margin: 4px 0;
        padding-left: 25px;
        font-size: 13px;
        letter-spacing: 0.15px;
        border: none;
        outline: none;
        // font-family: 'Montserrat', sans-serif;
        background-color: #ecf0f3;
        transition: 0.25s ease;
        border-radius: 8px;
        box-shadow: inset 2px 2px 4px #d1d9e6, inset -2px -2px 4px #f9f9f9;

        &::placeholder {
          color: #a0a5a8;
        }
      }
    }
  }

  .container-register {
    z-index: 100;
    left: calc(100% - 600px);
  }

  .container-login {
    left: calc(100% - 600px);
    z-index: 0;
  }

  .is-txl {
    left: 0;
    transition: 1.25s;
    transform-origin: right;
  }

  .is-z200 {
    z-index: 200;
    transition: 1.25s;
  }

  .switch {
    display: flex;
    justify-content: center;
    align-items: center;
    position: absolute;
    top: 0;
    left: 0;
    height: 100%;
    width: 400px;
    padding: 50px;
    z-index: 200;
    transition: 1.25s;
    background-color: #ecf0f3;
    overflow: hidden;
    box-shadow: 4px 4px 10px #d1d9e6, -4px -4px 10px #f9f9f9;
    color: #a0a5a8;

    .switch__circle {
      position: absolute;
      width: 500px;
      height: 500px;
      border-radius: 50%;
      background-color: #ecf0f3;
      box-shadow: inset 8px 8px 12px #d1d9e6, inset -8px -8px 12px #f9f9f9;
      bottom: -60%;
      left: -60%;
      transition: 1.25s;
    }

    .switch__circle_top {
      top: -30%;
      left: 60%;
      width: 300px;
      height: 300px;
    }

    .switch__container {
      display: flex;
      justify-content: center;
      align-items: center;
      flex-direction: column;
      position: absolute;
      width: 400px;
      padding: 50px 55px;
      transition: 1.25s;

      h2 {
        font-size: 34px;
        font-weight: 700;
        line-height: 3;
        color: #181818;
      }

      p {
        font-size: 14px;
        letter-spacing: 0.25px;
        text-align: center;
        line-height: 1.6;
      }
    }
  }

  .login {
    left: calc(100% - 400px);

    .switch__circle {
      left: 0;
    }
  }

  .form__button {
    border: 0;
    width: 180px;
    height: 50px;
    border-radius: 25px;
    margin-top: 50px;
    text-align: center;
    line-height: 50px;
    font-size: 14px;
    letter-spacing: 2px;
    background-color: #4b70e2;
    color: #f9f9f9;
    cursor: pointer;
    box-shadow: 8px 8px 16px #d1d9e6, -8px -8px 16px #f9f9f9;

    &:hover {
      box-shadow: 2px 2px 3px 0 rgba(255, 255, 255, 50%), -2px -2px 3px 0 rgba(116, 125, 136, 50%),
        inset -2px -2px 3px 0 rgba(255, 255, 255, 20%), inset 2px 2px 3px 0 rgba(0, 0, 0, 30%);
    }
  }
}
.remember {
  width: 350px;
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
</style>
