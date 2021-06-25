<template>
  <div>
    <!-- 面包屑导航区域 -->
    <el-breadcrumb separator-class="el-icon-arrow-right">
      <el-breadcrumb-item>首页</el-breadcrumb-item>
      <el-breadcrumb-item>统计分析</el-breadcrumb-item>
    </el-breadcrumb>
    <!-- 卡片视图区域 -->
    <el-card :style="curHeight">
      <div
        class="echart-container"
        v-loading="loading"
        element-loading-text="Echarts加载中..."
        element-loading-spinner="el-icon-loading"
        element-loading-background="rgba(0, 0, 0, 0.8)"
      >
        <!-- 抽检合格率区域图 -->
        <div id="main"></div>
        <div class="box">
          <div id="main2"></div>
          <div id="main3"></div>
        </div>
        <div class="quarter-box">
          <div class="year-text">
            <el-select v-model="year" @change="selectChangeYear">
              <el-option v-for="item in yearList" :key="item" :label="item" :value="item"> </el-option>
            </el-select>
          </div>
          <div class="quarter-text active-quarter" @click="selectChange(0)">第一季度</div>
          <div class="quarter-text" @click="selectChange(1)">第二季度</div>
          <div class="quarter-text" @click="selectChange(2)">第三季度</div>
          <div class="quarter-text" @click="selectChange(3)">第四季度</div>
        </div>
      </div>
    </el-card>
  </div>
</template>

<script>
import echarts from 'echarts';
import geoJson from '../../assets/json/jianli.json';

export default {
  data() {
    return {
      // 参数条件
      params: {
        testDateS: null,
        testDateE: null,
      },
      // 加载等待
      loading: true,
      // 卡片高度
      curHeight: {
        height: '',
        overflow: 'auto',
      },
      // echart实例
      myChart: null,
      myChart02: null,
      myChart03: null,
      // 区域列表
      inspectPassRoute: [],
      districLists: [],
      dataInspectPass: [],
      dataInspectNum: [],
      // 季度
      quarter: '',
      year: null,
      yearList: [],
      curIndex: 0,
    };
  },
  mounted() {
    this.setTableHeight();
    let date = new Date();
    let m = date.getMonth();
    this.getDateTime(date, m);
    window.onresize = () => {
      this.setTableHeight();
      this.myChart.resize();
      this.myChart02.resize();
      this.myChart03.resize();
    };
    let curYear = new Date().getFullYear();
    for (let year = 2020; year <= curYear; year++) {
      this.year = year;
      this.yearList.push(year);
    }
  },
  methods: {
    // 获取时间
    getDateTime(date, m) {
      let quarterList = document.querySelectorAll('.quarter-text');
      if (m >= 0 && m <= 2) {
        this.quarter = '第一季度';
        quarterList[0].classList.add('active-quarter');
        this.params.testDateS = this.setStartTime(date, 0);
        this.params.testDateE = this.setEndTime(date, 2, 31);
      } else if (m > 2 && m <= 5) {
        this.quarter = '第二季度';
        quarterList[1].classList.add('active-quarter');
        this.params.testDateS = this.setStartTime(date, 3);
        this.params.testDateE = this.setEndTime(date, 5, 30);
      } else if (m > 5 && m <= 8) {
        this.quarter = '第三季度';
        quarterList[2].classList.add('active-quarter');
        this.params.testDateS = this.setStartTime(date, 6);
        this.params.testDateE = this.setEndTime(date, 8, 30);
      } else if (m > 8 && m <= 11) {
        this.quarter = '第四季度';
        quarterList[3].classList.add('active-quarter');
        this.params.testDateS = this.setStartTime(date, 9);
        this.params.testDateE = this.setEndTime(date, 11, 31);
      }
      this.getStatics();
    },
    // 起始时间
    setStartTime(date, month) {
      date.setMonth(month);
      date.setDate(1);
      date.setHours(0);
      date.setMinutes(0);
      date.setSeconds(0);
      return this.formatTimeToStr(date);
    },
    // 结束时间
    setEndTime(date, month, day) {
      date.setMonth(month);
      date.setDate(day);
      date.setHours(23);
      date.setMinutes(59);
      date.setSeconds(59);
      return this.formatTimeToStr(date);
    },
    // 格式化时间
    formatTimeToStr(times) {
      let d = new Date(times).Format('yyyy-MM-dd hh:mm:ss');
      return d.toLocaleString();
    },
    // 获取数据
    async getStatics() {
      const { data: res } = await this.$http.get('jdSys/DeptTest/statisticsArea', { params: this.params });
      if (res.status !== 200) {
        return this.$message({
          duration: 1000,
          type: 'error',
          message: '获取统计数据失败！',
        });
      }
      this.inspectPassRoute = [];
      this.districLists = [];
      this.dataInspectPass = [];
      this.dataInspectNum = [];
      let tmpObj = {};
      for (let item of res.data) {
        tmpObj.name = item.name;
        tmpObj.value = item.cRoute * 100;
        this.inspectPassRoute.push(tmpObj);
        tmpObj = {};
        this.districLists.push(item.name);
        this.dataInspectPass.push(item.cRoute * 100);
        this.dataInspectNum.push(item.cNum);
      }
      this.showGraphic();
    },
    // 绘制图表
    showGraphic() {
      // 基于准备好的dom，初始化echarts实例
      this.myChart = echarts.init(document.getElementById('main'));
      this.myChart02 = echarts.init(document.getElementById('main2'));
      this.myChart03 = echarts.init(document.getElementById('main3'));
      echarts.registerMap('监利', geoJson);
      // 配置属性
      let option = {
        title: {
          text: this.quarter + '监利市农产品抽检合格率区域图',
          left: 'center',
        },
        tooltip: {
          trigger: 'item',
          formatter: '{a}<br/>{b}: {c}%',
        },
        visualMap: {
          left: 'center',
          bottom: 60,
          orient: 'horizontal',
          splitList: [
            { start: 90, label: '90%以上', color: '#81b214' },
            { start: 60, end: 90, label: '60-90%', color: '#f9813a' },
            { start: 0, end: 60, label: '0-60%', color: '#bb2205' },
            { start: 0, end: 0, label: '0%', color: '#968c83' },
          ],
          textStyle: {
            color: '#1f6f8b', // 值域文字颜色
          },
          selectedMode: 'multiple',
        },
        series: [
          {
            name: '部门抽检合格率',
            type: 'map',
            mapType: '监利',
            // 缩放与拖动
            roam: false,
            label: {
              show: true,
            },
            data: this.inspectPassRoute,
          },
        ],
      };
      let option2 = {
        title: {
          text: this.quarter + '合格率统计',
          left: 'center',
        },
        tooltip: {
          trigger: 'axis',
          formatter: '{a}<br/>{b}: {c}%',
        },
        toolbox: {
          feature: {
            magicType: { show: true, type: ['line'] },
            restore: { show: true },
            saveAsImage: { show: true },
          },
          right: 20,
        },
        grid: {
          left: '40',
          top: '50',
          right: '20',
          bottom: '70',
        },
        xAxis: {
          axisLabel: {
            interval: 0,
            formatter: function(value) {
              let j = 0,
                o = j;
              let newArray = [];
              while (j < value.length) {
                j += 2;
                newArray.push([value.slice(o, j)]);
                o = j;
              }
              return newArray.join('\n');
            },
          },
          data: this.districLists,
        },
        yAxis: [
          {
            name: '合格率',
            type: 'value',
            axisLabel: {
              show: true,
              interval: 'auto',
              formatter: '{value} %',
            },
          },
        ],
        series: [
          {
            name: '合格率统计',
            type: 'bar',
            color: '#66bfbf',
            data: this.dataInspectPass,
            label: {
              normal: {
                show: true,
                position: 'top',
                fontSize: 14,
                formatter: '{c}%',
              },
            },
          },
        ],
      };
      let option3 = {
        title: {
          text: this.quarter + '检测数量对比',
          left: 'center',
        },
        tooltip: {
          trigger: 'axis',
        },
        toolbox: {
          feature: {
            magicType: { show: true, type: ['line'] },
            restore: { show: true },
            saveAsImage: { show: true },
          },
          right: 20,
        },
        grid: {
          left: '40',
          top: '50',
          right: '20',
          bottom: '70',
        },
        xAxis: {
          axisLabel: {
            interval: 0,
            formatter: function(value) {
              let j = 0,
                o = j;
              let newArray = [];
              while (j < value.length) {
                j += 2;
                newArray.push([value.slice(o, j)]);
                o = j;
              }
              return newArray.join('\n');
            },
          },
          data: this.districLists,
        },
        yAxis: {
          name: '检测数量',
        },
        series: [
          {
            name: '检测数量',
            type: 'bar',
            color: '#66bfbf',
            data: this.dataInspectNum,
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
      this.myChart.setOption(option);
      this.myChart02.setOption(option2);
      this.myChart03.setOption(option3);
      this.loading = false;
    },
    // 设定卡片高度
    setTableHeight() {
      let h = document.documentElement.clientHeight || document.body.clientHeight;
      this.curHeight.height = h - 177 + 'px';
    },
    selectChangeYear() {
      this.selectChange(this.curIndex);
    },
    // 切换季度
    selectChange(index) {
      this.myChart02.clear();
      this.myChart03.clear();
      this.curIndex = index;
      let quarterList = document.querySelectorAll('.quarter-text');
      let date = new Date();
      date.setFullYear(this.year);
      for (let i = 0; i < quarterList.length; i++) {
        quarterList[i].classList.remove('active-quarter');
      }
      if (index === 0) {
        this.getDateTime(date, 0);
      } else if (index === 1) {
        this.getDateTime(date, 3);
      } else if (index === 2) {
        this.getDateTime(date, 6);
      } else if (index === 3) {
        this.getDateTime(date, 9);
      }
    },
  },
};
</script>

<style lang="less" scoped>
.echart-container {
  width: 100%;
  height: 100%;
  // overflow: auto;
  position: relative;

  // 左边区域地图
  #main {
    width: 35%;
    height: 760px;
    left: 0;
    top: 0;
    position: absolute;
    // background-color: #bedbbb;
  }

  // 右边柱状图
  .box {
    width: 65%;
    height: 760px;
    position: absolute;
    right: 0;
    top: 0;
    // background-color: #ffd5cd;

    // 合格统计率柱状图
    #main2 {
      width: 100%;
      height: 370px;
    }

    // 挤出水来柱状图
    #main3 {
      width: 100%;
      height: 370px;
      margin-top: 20px;
    }
  }

  // 季度切换样式
  .quarter-box {
    width: 35%;
    height: 40px;
    position: absolute;
    left: 0;
    top: 720px;
    display: flex;
    justify-content: space-evenly;
    align-items: center;

    .year-text {
      width: 17%;
      height: 40px;
      line-height: 30px;
      border-radius: 5px;
      font-size: 16px;
      color: #fff;
      text-align: center;
      background-color: #008891;
      cursor: pointer;
      user-select: none;
    }

    .quarter-text {
      width: 17%;
      height: 30px;
      line-height: 30px;
      border-radius: 5px;
      font-size: 16px;
      color: #fff;
      text-align: center;
      background-color: #008891;
      cursor: pointer;
      user-select: none;
    }

    .active-quarter {
      background-color: #fa7f72;
    }
  }
}
</style>
