<template>
  <!--为echarts准备一个具备大小的容器dom-->
  <div id="main" style="width: 100%;height:100%; background-color: #ffffff;"></div>
</template>
<script>
import echarts from "echarts";
export default {
  name: "",
  data() {
    return {
      charts: "",
      one: [1.5, 1.2, 1, 0.7, 1, 1, 1.3],
      two: [0.3, 1.1, 1.5, 0.8, 0.2, 1.2, 2],
      three: [1.5, 0.9, 1.6, 1, 0.5, 1.1, 0.5],
    };
  },
  methods: {
    drawLine(id) {
      this.charts = echarts.init(document.getElementById(id));
      this.charts.setOption({
        toolbox: {
          feature: {
            saveAsImage: {},
          },
        },
        xAxis: {
          type: "category",
          data: ["Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"],
        },
        yAxis: {
          type: "value",
        },

        series: [
          {
            data: [120, 200, 150, 80, 70, 110, 130],
            type: "bar",
            showBackground: true,
            backgroundStyle: {
              color: "rgba(220, 220, 220, 0.8)",
            },
          },
        ],
      });
    },
  },
  //调用
  mounted() {
    this.$nextTick(function () {
      this.drawLine("main");
    });
  },
};
</script>
<style scoped>
</style>