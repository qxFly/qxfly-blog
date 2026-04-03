import router from "@/router";
let hideTopBarList1 =
  /(manage|login|register|findpassword|ArticleDetail|JumpIndex|workspace)/;
/**
 * 根据路由隐藏顶栏
 * @param {string} path 路由路径
 */
export function hideTopBarByRouter(path) {
  if (path.match(hideTopBarList1) != null) {
    changeTopBar(1);
    console.log("h1", path);
  } else {
    changeTopBar(0);
    console.log("h0", path);
  }
}
/**
 * 改变顶栏状态
 * @param {int} state  0:显示 1:隐藏
 */
export function changeTopBar(state) {
  let count = 0;
  let hideTopBarInterval = setInterval(() => {
    count++;
    let topBar = document.getElementById("top-bar-1");
    if (count >= 10 || topBar.style.top) {
      clearInterval(hideTopBarInterval);
      if (state) {
        topBar.style.top = "-70px";
      } else {
        topBar.style.top = "0px";
      }
    }
  }, 200);
}

/**
 * 滚动时到指定高度隐藏状态栏
 */
let Higttimeout = null; //防抖计时
const ListenerHigt = () => {
  clearTimeout(Higttimeout);
  Higttimeout = setTimeout(() => {
    const topbar = document.getElementById("top-bar-1");
    var scrollTop =
      document.documentElement.scrollTop || document.body.scrollTop;
    if (scrollTop > 600) {
      topbar.style.top = "-70px";
    } else {
      if (router.currentRoute.value.path.match(/manage/)) return;
      topbar.style.top = "0";
    }
  }, 300);
};

// function ListenerHigt() {}
/* 当状态栏收起时，监听鼠标 */
let Mousetimeout = null; //防抖计时
function ListenerMouser(event) {
  clearTimeout(Mousetimeout);
  Mousetimeout = setTimeout(() => {
    let mouseY = event.clientY;
    const topbar = document.getElementById("top-bar-1");
    var scrollTop =
      document.documentElement.scrollTop || document.body.scrollTop;
    if (scrollTop > 600) {
      if (mouseY < 70) {
        topbar.style.top = "0px";
      } else {
        topbar.style.top = "-70px";
      }
    }
  }, 300);
}

// 鼠标离开状态栏
function mouseleave() {
  const topbarbg = document.getElementById("top-bar-bg");
  if (topbarbg == null) return;
  setTimeout(() => {
    topbarbg.style.opacity = "0.2";
  }, 2000);
}
// 鼠标移动到状态栏
function mousemove() {
  const topbarbg = document.getElementById("top-bar-bg");
  if (topbarbg == null) return;
  topbarbg.style.opacity = "1";
  topbarbg.style.backgroundImage =
    "linear-gradient(to right, var(--main-theme-color-blue), #b1dbff)";
}

/* 当状态栏长时间不操作时，透明化 */
export function ListenerTime() {
  const topbarbg = document.getElementById("top-bar-bg");
  const topbar = document.getElementById("top-bar-1");
  if (topbar == null || topbarbg == null) return;
  topbar.addEventListener("mousemove", mousemove);
  topbar.addEventListener("mouseleave", mouseleave);
  mouseleave();
}

/**
 * 添加高度检测
 */
export function addEventListenerHigt() {
  console.log("wdadawd5646");
  window.addEventListener("scroll", ListenerHigt);
}
/**
 * 移除高度检测
 */
export function removeEventListenerHigt() {
  console.log("wdadawd");

  window.removeEventListener("scroll", ListenerHigt, false);
}
/**
 * 添加鼠标检测
 */
export function addEventListenerMouser() {
  document.addEventListener("mousemove", ListenerMouser);
}
/**
 * 移除鼠标检测
 */
export function removeEventListenerMouser() {
  window.removeEventListener("mousemove", ListenerMouser, false);
}
