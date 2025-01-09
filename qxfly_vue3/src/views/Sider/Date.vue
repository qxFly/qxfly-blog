<template>
    <CardView>
        <div class="date">
            <div>
                <!-- <div class="title">当前时间</div> -->
            </div>
            <!-- <hr /> -->
            <div class="content">
                <div class="YMD">
                    <span style="font-size: 20px; color: #f27fcb">{{ year }}</span> 年
                    <span style="font-size: 20px; color: #f275b5">{{ month }}</span> 月
                    <span style="font-size: 20px; color: #f5586d">{{ day }}</span> 日
                </div>
                <div class="HMS">
                    <span style="font-size: 18px; color: #dfc3fc">{{ hour }}</span> 时
                    <span style="font-size: 18px; color: #becdfa">{{ minute }}</span> 分
                    <span style="font-size: 18px; color: #8fc5fc">{{ second }}</span> 秒
                </div>
            </div>
        </div>
    </CardView>
</template>

<script setup>
import { onMounted, ref } from "vue";
import CardView from "@/components/CardView.vue";
let year = ref("0000");
let month = ref("00");
let day = ref("00");
let hour = ref("00");
let minute = ref("00");
let second = ref("00");

function getDate() {
    setInterval(() => {
        let date = new Date();
        year.value = date.getFullYear();
        month.value = date.getMonth() + 1;
        day.value = date.getDate();
        hour.value = date.getHours();
        minute.value = date.getMinutes();
        second.value = date.getSeconds();
    }, 1000);
}

onMounted(async () => {
    getDate();
});
</script>

<style scoped>
.date {
    /* width: 200px; */
}
.date .title {
    font-size: 18px;
    font-weight: 700;
    margin-bottom: 4px;
}
.content {
    display: flex;
    width: 100%;
    /* text-align: center; */
    justify-content: center;
    align-items: center;
    flex-direction: column;
    /* margin: 4px 0 0 0; */
    /* font-weight: 600; */
}
.YMD {
    margin-bottom: 4px;
}
</style>
