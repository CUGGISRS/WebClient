<template>
  <div>
    <!-- 面包屑导航区域 -->
    <el-breadcrumb separator-class="el-icon-arrow-right">
      <el-breadcrumb-item>首页</el-breadcrumb-item>
      <el-breadcrumb-item>统计详情</el-breadcrumb-item>
    </el-breadcrumb>
    <!-- 卡片视图区域 -->
    <el-card :style="curHeight">
      <!-- 方块数量显示 -->
      <div class="box-container">
        <div class="box">
          <span>收购数</span>
          <span>{{ buys }}</span>
        </div>
        <div class="box">
          <span>生产批次</span>
          <span>{{ batchs }}</span>
        </div>
        <div class="box">
          <span>销售订单</span>
          <span>{{ orders }}</span>
        </div>
        <div class="box">
          <span>检验报告</span>
          <span>{{ inspections }}</span>
        </div>
      </div>
      <!-- 柱形图 -->
      <div class="echart-container">
        <!-- 生产批次数柱状图 -->
        <div id="main1" class="echart-box"></div>

        <!-- 销售订单数柱状图 -->
        <div id="main2" class="echart-box"></div>
      </div>
      <!-- 时间选择器 -->
      <div class="time-select-container">
        <div class="time-bar">
          <span class="time-circle-point"></span>
          <span class="time-circle-point"></span>
          <span class="time-circle-point active-point"></span>
        </div>

        <div class="time-year">
          <span v-for="(item, index) in yearLists" :key="index" class="time-year-text" @click="selectChange(index)">{{
            item
          }}</span>
        </div>
      </div>
    </el-card>
  </div>
</template>

<script>
import echarts from 'echarts';
export default {
  data() {
    return {
      // 参数条件
      params: {
        enterpriseId: window.sessionStorage.getItem('enterpriseId'),
        baseType: '养殖基地',
        before: null,
      },
      // 卡片高度
      curHeight: {
        height: '',
        overflow: 'auto',
      },
      // echart实例
      myChart01: null,
      myChart02: null,
      // 收购数
      buys: 0,
      // 生产批次
      batchs: 0,
      // 销售订单
      orders: 0,
      // 检验报告
      inspections: 0,
      // 月份
      monthLists: [],
      // 生产批次数
      dataProduction: [],
      // 销售订单数
      dataOrder: [],
      // 年份数组
      yearLists: [],
    };
  },
  mounted() {
    let date = new Date();
    let year = date.getFullYear();
    this.params.before = year + '-1';
    this.yearLists.push(year - 2);
    this.yearLists.push(year - 1);
    this.yearLists.push(year);
    this.getStatics();
    this.setTableHeight();
    window.onresize = () => {
      this.setTableHeight();
      this.myChart01.resize();
      this.myChart02.resize();
    };
  },
  methods: {
    // 数据获取
    async getStatics() {
      const { data: res } = await this.$http.get('zsSys/ProcessBatch/statisticsByEId', { params: this.params });
      if (res.status !== 200) {
        return this.$message({
          duration: 1000,
          type: 'error',
          message: '获取统计数据失败！',
        });
      }
      this.buys = res.data.upper.aqzs_buy;
      this.batchs = res.data.upper.aqzs_process_batch;
      this.orders = res.data.upper.aqzs_sale;
      this.inspections = res.data.upper.aqzs_finish_product_test;
      this.monthLists = [];
      this.dataProduction = [];
      this.dataOrder = [];
      for (let i = 0; i < res.data.lower.length; i++) {
        this.monthLists.push(res.data.lower[i].month);
        this.dataProduction.push(res.data.lower[i].aqzs_process_batch);
        this.dataOrder.push(res.data.lower[i].aqzs_sale);
      }
      this.showGraphic();
    },
    // 绘制图表
    showGraphic() {
      // 基于准备好的dom，初始化echarts实例
      this.myChart01 = echarts.init(document.getElementById('main1'));
      this.myChart02 = echarts.init(document.getElementById('main2'));
      // 配置属性
      let option1 = {
        title: {
          text: '生产批次数',
          left: 'center',
        },
        tooltip: {
          appendToBody: true,
        },
        toolbox: {
          feature: {
            // dataView: {show: true, readOnly: true},
            magicType: { show: true, type: ['line', 'bar'] },
            restore: { show: true },
            saveAsImage: { show: true },
          },
          right: 20,
        },
        xAxis: {
          axisLabel: {
            interval: 0,
            formatter: function(value) {
              return value.split('').join('\n');
            },
          },
          data: this.monthLists,
        },
        yAxis: {},
        series: [
          {
            name: '生产批次数',
            type: 'bar',
            color: '#66bfbf',
            data: this.dataProduction,
            label: {
              normal: {
                show: true,
                position: 'top',
                fontSize: 16,
              },
            },
          },
        ],
      };
      let option2 = {
        title: {
          text: '销售订单数',
          left: 'center',
        },
        tooltip: {
          appendToBody: true,
        },
        toolbox: {
          feature: {
            // dataView: {show: true, readOnly: true},
            magicType: { show: true, type: ['line', 'bar'] },
            restore: { show: true },
            saveAsImage: { show: true },
          },
          right: 20,
        },
        xAxis: {
          axisLabel: {
            interval: 0,
            formatter: function(value) {
              return value.split('').join('\n');
            },
          },
          data: this.monthLists,
        },
        yAxis: {},
        series: [
          {
            name: '销售订单数',
            type: 'bar',
            color: '#66bfbf',
            data: this.dataOrder,
            label: {
              normal: {
                show: true,
                position: 'top',
                fontSize: 16,
              },
            },
          },
        ],
      };
      // 绘制图表
      this.myChart01.setOption(option1);
      this.myChart02.setOption(option2);
    },
    // 设定卡片高度
    setTableHeight() {
      let h = document.documentElement.clientHeight || document.body.clientHeight;
      this.curHeight.height = h - 180 + 'px';
    },
    // 切换年份
    selectChange(index) {
      this.myChart01.clear();
      this.myChart02.clear();
      let pointArr = document.querySelectorAll('.time-circle-point');
      let yearTextArr = document.querySelectorAll('.time-year-text');
      for (let i = 0; i < yearTextArr.length; i++) {
        pointArr[i].classList.remove('active-point');
        if (index === i) {
          pointArr[i].classList.add('active-point');
          this.params.before = this.yearLists[i] + '-1';
          this.getStatics();
        }
      }
    },
  },
};
</script>

<style lang="less" scoped>
// 方块显示区域样式
.box-container {
  width: 100%;
  display: flex;
  justify-content: space-between;

  .box {
    flex: 1;
    height: 130px;
    border-radius: 10px;
    margin: 0 20px;
    font-size: 32px;
    color: #fff;
    display: flex;
    align-items: center;
    justify-content: center;
    flex-direction: column;
  }

  .box:nth-child(1) {
    background-color: #35aa75;
  }
  .box:nth-child(2) {
    background-color: #f2745a;
  }
  .box:nth-child(3) {
    background-color: #29b5c9;
  }
  .box:nth-child(4) {
    background-color: #f5b252;
  }
}

// echarts样式
.echart-container {
  width: 100%;
  margin-top: 20px;
  overflow: auto;

  .echart-box {
    width: 50%;
    float: left;
    height: 540px;
  }
}

// 时间选择器样式
.time-select-container {
  width: 100%;
  height: 30px;
  margin-top: 30px;
  // background-color: skyblue;
  position: relative;

  .time-bar {
    width: 80%;
    height: 10px;
    position: absolute;
    left: 50%;
    transform: translateX(-50%);
    background-color: #fa7f72;
    border-radius: 5px;

    .time-circle-point {
      display: inline-block;
      width: 10px;
      height: 10px;
      border: 2px solid #9ba4b4;
      background-color: #fff;
      border-radius: 50%;
      position: absolute;
      top: -2px;
    }

    .time-circle-point:nth-child(1) {
      left: 0;
    }
    .time-circle-point:nth-child(2) {
      left: 50%;
      transform: translateX(-50%);
    }
    .time-circle-point:nth-child(3) {
      right: 0;
    }

    .active-point {
      background-color: #19d3da;
    }
  }

  .time-year {
    width: 80%;
    height: 20px;
    position: absolute;
    left: 50%;
    top: 15px;
    transform: translateX(-50%);

    .time-year-text {
      display: inline-block;
      width: 80px;
      height: 37px;
      line-height: 57px;
      font-size: 16px;
      text-align: center;
      position: absolute;
      top: -17px;
      cursor: pointer;
    }

    .time-year-text:nth-child(1) {
      left: 0;
      transform: translateX(-50%);
    }
    .time-year-text:nth-child(2) {
      left: 50%;
      transform: translateX(-50%);
    }
    .time-year-text:nth-child(3) {
      right: 0;
      transform: translateX(50%);
    }
  }
}
</style>
