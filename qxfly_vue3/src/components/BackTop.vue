<template>
    <div class="backtop" id="backTop" :class="{ nobacktop: showbacktop }" @click="Backtop">
        <svg
            t="1705219391113"
            class="icon"
            viewBox="0 0 1024 1024"
            version="1.1"
            xmlns="http://www.w3.org/2000/svg"
            p-id="5867"
            width="40"
            height="40">
            <path
                d="M527.8 300.8c-9.3-9.1-24.2-9.1-33.6 0.1l-317 311c-9.5 9.3-9.6 24.5-0.3 33.9 9.3 9.5 24.5 9.6 33.9 0.3L488 374.2V858c0 13.3 10.7 24 24 24s24-10.7 24-24V375.9l277.2 270.3c4.7 4.5 10.7 6.8 16.8 6.8 6.2 0 12.5-2.4 17.2-7.2 9.3-9.5 9.1-24.7-0.4-33.9l-319-311.1zM896 153H125c-13.3 0-24 10.7-24 24s10.7 24 24 24h771c13.3 0 24-10.7 24-24s-10.7-24-24-24z"
                fill="#fff"
                p-id="5868"></path>
        </svg>
    </div>
</template>

<script setup>
import { ref, onMounted } from "vue";

var showbacktop = ref(true);

function Backtop() {
    var timer = null;
    cancelAnimationFrame(timer);
    timer = requestAnimationFrame(function fn() {
        var oTop = document.body.scrollTop || document.documentElement.scrollTop;

        if (oTop > 0) {
            document.body.scrollTop = document.documentElement.scrollTop = oTop - 80;
            timer = requestAnimationFrame(fn);
        } else {
            cancelAnimationFrame(timer);
        }
    });
}

onMounted(() => {
    window.addEventListener("scroll", function () {
        var scrollTop = document.documentElement.scrollTop || document.body.scrollTop;
        if (scrollTop > 800) {
            showbacktop.value = false;
        } else {
            showbacktop.value = true;
        }
    });
});
</script>

<style scoped>
.backtop {
    display: flex;
    align-items: center;
    justify-content: center;
    height: 40px;
    width: 40px;
    position: fixed;
    right: 0px;
    bottom: 100px;
    border-radius: 4px 0 0 4px;
    border-right: none;
    background-color: var(--main-theme-color-blue);
}
.nobacktop {
    display: none;
}

.backtop:hover {
    background-color: var(--main-theme-color-orange);
    /* box-shadow: 0 3px 8px 6px rgba(7, 17, 27, 0.3); */
    /* animation: launch infinite 0.5s step-start; */
    /* background-image: url(../assets/img/icon/backtop-a.png); */
}
@keyframes launch {
    0% {
        background-position: 0 0;
    }
    25% {
        background-position: -150px 0;
    }
    50% {
        background-position: -300px 0;
    }
    75% {
        background-position: -450px 0;
    }
    100% {
        background-position: -600px 0;
    }
}
</style>
