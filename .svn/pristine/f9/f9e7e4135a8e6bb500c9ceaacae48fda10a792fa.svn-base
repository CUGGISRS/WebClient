<template>
  <div class="container">
    <div class="container-group">
      <ul :style="curHeight">
        <li v-for="(item, index) in groupLists" :key="index" @click="changeGroup(item.id, index)" class="group-li">
          {{ item.name }}
        </li>
      </ul>
    </div>
    <div class="container-data">
      <div class="data-head">
        <el-select v-model="params.interval" placeholder="请选择">
          <el-option v-for="item in intervalOptions" :key="item.value" :label="item.label" :value="item.value">
          </el-option>
        </el-select>
        <el-date-picker
          v-model="timeValue"
          type="datetimerange"
          value-format="yyyy-MM-dd HH:mm:ss"
          :picker-options="pickerOptions"
          range-separator="至"
          start-placeholder="开始日期"
          end-placeholder="结束日期"
          align="left"
          :default-time="['00:00:00', '23:59:59']"
        >
        </el-date-picker>
        <el-button class="el-icon-search" type="primary" @click="searchData">分析</el-button>
      </div>
      <div
        id="main"
        :style="curHeight2"
        v-loading="loading"
        element-loading-text="Echarts加载中..."
        element-loading-spinner="el-icon-loading"
        element-loading-background="rgba(0, 0, 0, 0.8)"
      ></div>
    </div>
  </div>
</template>

<script>
import echarts from 'echarts';
import { formatDateTime, dateFormat } from '../../assets/js/tool';

export default {
  data() {
    return {
      // 设备列表
      groupLists: [],
      // 传感器列表
      sensorLists: [],
      // 当前设备id
      defaultGroupId: 0,
      // 当前设备索引
      defaultIndex: 0,
      // 页面高度自适应
      curHeight: {
        height: '',
        overflow: 'auto',
      },
      curHeight2: {
        height: '',
        overflow: 'auto',
      },
      // 日期选择
      pickerOptions: {
        shortcuts: [
          {
            text: '最近一周',
            onClick(picker) {
              const end = new Date();
              const start = new Date();
              start.setTime(start.getTime() - 3600 * 1000 * 24 * 7);
              picker.$emit('pick', [start, end]);
            },
          },
          {
            text: '最近一个月',
            onClick(picker) {
              const end = new Date();
              const start = new Date();
              start.setTime(start.getTime() - 3600 * 1000 * 24 * 30);
              picker.$emit('pick', [start, end]);
            },
          },
          {
            text: '最近三个月',
            onClick(picker) {
              const end = new Date();
              const start = new Date();
              start.setTime(start.getTime() - 3600 * 1000 * 24 * 90);
              picker.$emit('pick', [start, end]);
            },
          },
        ],
      },
      // 选择的日期的值
      timeValue: [],
      // 数据间隔
      intervalOptions: [
        { label: '1分钟', value: 1 },
        { label: '2分钟', value: 2 },
        { label: '5分钟', value: 5 },
        { label: '10分钟', value: 10 },
        { label: '30分钟', value: 30 },
        { label: '1小时', value: 60 },
        { label: '4小时', value: 240 },
        { label: '6小时', value: 360 },
        { label: '12小时', value: 720 },
        { label: '24小时', value: 1440 },
      ],
      // 数据查询参数
      params: {
        recordTimeS: null,
        recordTimeE: null,
        groupId: null,
        interval: 60,
      },
      // 设备全部数据
      groupAllData: [],
      // table表头名称
      contentsTitle: [],
      // 递归结束标志
      flagIndex: 0,
      // 加载等待
      loading: true,
      // echart实例
      myChart: null,
      // echarts数据
      data: [],
      // 颜色数组
      colorArr: [
        '#4e89ae',
        '#ed6663',
        '#43658b',
        '#ffa372',
        '#ff4b5c',
        '#ffc93c',
        '#81b214',
        '#ea5455',
        '#799351',
        '#00b7c2',
        '#dd2c00',
        '#cdb30c',
        '#62760c',
        '#535204',
        '#523906',
        '#848ccf',
        '#93b5e1',
        '#be5683',
        '#a2de96',
        '#3ca59d',
        '#5a3d55',
        '#e79c2a',
        '#c70039',
        '#f37121',
      ],
    };
  },
  created() {
    this.getAllGroup();
    // this.setHeight();
    // window.onresize = () => {
    //   this.setHeight();
    // }
  },
  methods: {
    // 设定高度
    setHeight() {
      let h = document.documentElement.clientHeight || document.body.clientHeight;
      this.curHeight.height = h - 100 + 'px';
      this.curHeight2.height = h - 160 + 'px';
    },
    // 获取全部设备
    async getAllGroup() {
      const { data: res } = await this.$http.get('zsSys/DeviceGroup/pageByCId', {
        params: { creatorId: window.sessionStorage.getItem('enterpriseId') },
      });
      if (res.status !== 200) return this.$message.error('获取全部设备失败!');
      this.groupLists = res.data.rows;
      setTimeout(() => {
        if (this.groupLists && this.groupLists.length > 0) {
          this.changeGroup(this.groupLists[0].id, this.defaultIndex);
        }
      }, 0);
    },
    // 根据设备id获取传感器信息
    async getSensorByGroupId(groupId) {
      const { data: res } = await this.$http.get('zsSys/DeviceSensor/pageByGId', {
        params: { groupId },
      });
      if (res.status !== 200) return this.$message.error('获取传感器数据失败!');
      this.sensorLists = res.data.rows;
      this.contentsTitle = [];
      this.sensorLists.map((item) => {
        this.contentsTitle.unshift(`${item.name}(${item.unit})`);
      });
      await this.getDataBySensorId(groupId);
    },
    // 根据传感器id获取数据
    async getDataBySensorId(groupId) {
      this.params.recordTimeS = this.timeValue && this.timeValue.length > 0 ? this.timeValue[0] : null;
      this.params.recordTimeE = this.timeValue && this.timeValue.length > 0 ? this.timeValue[1] : null;
      this.params.groupId = groupId;
      const { data: res } = await this.$http.get('zsSys/DeviceSensorData/getByGId', { params: this.params });
      if (res.status !== 200) {
        this.data = [];
        this.showGraphic();
        return this.$message.error('获取数据失败!');
      }
      this.groupAllData = res.data.dsList;
      this.groupAllData.map((item, index) => {
        let tmpObj = {
          name: `${item.name}(${item.unit})`,
          type: 'line',
          lineStyle: {
            width: 2,
            color: this.colorArr[index],
          },
          color: this.colorArr[index],
          showSymbol: false,
          data: this.handleData(item.dsdList),
          // 这是让时间刻度缺失的点，数据正常连接不断开
          connectNulls: true,
        };
        this.data.push(tmpObj);
      });
      this.showGraphic();
    },
    // 点击切换设备
    changeGroup(groupId, index) {
      let lis = document.getElementsByClassName('group-li');
      for (let i = 0; i < lis.length; i++) {
        lis[i].classList.remove('activeLi');
        if (index === i) {
          lis[i].classList.add('activeLi');
          this.defaultIndex = index;
          this.defaultGroupId = groupId;
          this.params.interval = 60;
          this.timeValue = [];
          const end = new Date();
          const start = new Date();
          start.setTime(start.getTime() - 3600 * 1000 * 24 * 7);
          this.timeValue.push(formatDateTime(start));
          this.timeValue.push(formatDateTime(end));
          this.groupAllData = [];
          this.loading = true;
          this.data = [];
        }
      }
      this.getSensorByGroupId(groupId);
    },
    // 查询数据
    searchData() {
      this.groupAllData = [];
      this.loading = true;
      this.data = [];
      this.getDataBySensorId(this.defaultGroupId);
    },
    // 绘制图表
    showGraphic() {
      // 基于准备好的dom，初始化echarts实例
      this.myChart = echarts.init(document.getElementById('main'));
      // 配置属性
      let option = {
        grid: {
          left: '70',
          top: '50',
          right: '50',
          bottom: '80',
        },
        toolbox: {
          right: 40,
          feature: {
            dataZoom: {
              yAxisIndex: 'none',
            },
            restore: {},
            saveAsImage: {},
          },
        },
        tooltip: {
          trigger: 'axis',
          axisPointer: {
            type: 'line',
            animation: false,
            label: {
              backgroundColor: '#505765',
            },
          },
        },
        color: this.colorArr,
        legend: {
          data: this.contentsTitle,
          left: 'center',
          top: 10,
        },
        dataZoom: [
          {
            show: true,
            realtime: true,
            start: 0,
          },
          {
            type: 'inside',
            realtime: true,
            start: 0,
          },
        ],
        xAxis: [
          {
            type: 'time',
            axisLabel: {
              formatter: function(params) {
                let time = new Date(params);
                return dateFormat('YYYY-mm-dd HH:MM', time);
              },
            },
          },
        ],
        yAxis: {},
        series: this.data,
      };
      // 清除数据残留
      this.myChart.clear();
      // 绘制图表
      this.myChart.setOption(option);
      window.onresize = this.myChart.resize;
      this.loading = false;
    },
    // 数据处理
    handleData(arr) {
      if (arr && arr.length !== 0) {
        let resultArr = [];
        arr.map((item) => {
          let tmpArr = [];
          tmpArr.push(item.recordTime);
          tmpArr.push(item.recordData);
          resultArr.push(tmpArr);
        });
        return resultArr;
      }
    },
  },
};
</script>

<style lang="less" scoped>
.container {
  width: 100%;
  height: 100%;
  display: flex;

  // 设备
  .container-group {
    width: 220px;
    height: 100%;

    // 设备栏样式
    ul {
      width: 100%;
      margin: 0;
      padding: 0;
      list-style: none;
      text-align: center;

      // 单个设备样式
      .group-li {
        width: 100%;
        height: 40px;
        line-height: 40px;
        font-size: 14px;
        color: #606266;
        border-bottom: 1px solid #e1e1e1;
        cursor: pointer;
        position: relative;
      }

      // 活跃设备
      .activeLi {
        background-color: rgb(45, 183, 245);
        color: #fff;
        border: 0;
      }
    }

    // 隐藏滚动条
    ul::-webkit-scrollbar {
      width: 0 !important;
      height: 0;
    }
  }

  // 数据
  .container-data {
    position: relative;
    left: 0;
    bottom: 0;
    width: 100%;
    margin-left: 10px;

    // 查询栏样式
    .data-head {
      width: 100%;

      .el-select {
        width: 120px;
        margin: 10px 10px 0 0;
      }

      .el-date-editor {
        margin: 10px 10px 10px 0;
      }
    }

    // echarts样式
    #main {
      top: 50px;
      position: absolute;
      right: 0;
      left: 0;
      bottom: 0;
    }

    // 滚动条样式
    #main::-webkit-scrollbar {
      width: 6px !important;
      height: 10px;
    }

    //滑块部分
    #main::-webkit-scrollbar-thumb {
      height: 50%;
      border-radius: 5px;
      background-color: #bbbfca;
    }
  }
}
</style>
