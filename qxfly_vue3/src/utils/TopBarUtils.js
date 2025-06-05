let hideTopBarList1 = /(manage|login|register|findpassword|ArticleDetail)/;
/**
 * 根据路由隐藏顶栏
 * @param {string} path 路由路径
 */
export function hideTopBarByRouter(path) {
    if (path.match(hideTopBarList1) != null) {
        changeTopBar(1);
    } else {
        changeTopBar(0);
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
