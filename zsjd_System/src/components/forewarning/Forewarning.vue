<template>
  <div>
    <!-- 面包屑导航区域 -->
    <el-breadcrumb separator-class="el-icon-arrow-right">
      <el-breadcrumb-item>首页</el-breadcrumb-item>
      <el-breadcrumb-item>安全预警</el-breadcrumb-item>
    </el-breadcrumb>
    <!-- 卡片视图区域 -->
    <el-card :style="curHeight">
      <div class="box-head">
        <span>预警阈值:</span>
        <el-input-number v-model="thresholdRow.threshold" :precision="1" :min="0" :max="100" style="margin: 0 10px;">
        </el-input-number>
        <el-button type="primary" icon="el-icon-upload2" @click="editThreshold">保存阈值</el-button>
        <el-button type="info" icon="el-icon-refresh-left" @click="getThreshold">重置阈值</el-button>
        <el-badge :value="forewaringNum" :max="99" style="margin: 0 10px;">
          <el-button type="warning" icon="el-icon-bell" @click="showForewaringInfo">预警信息</el-button>
        </el-badge>
      </div>
      <div
        id="main"
        class="box-container"
        v-loading="loading"
        element-loading-text="Echarts加载中..."
        element-loading-spinner="el-icon-loading"
        element-loading-background="rgba(0, 0, 0, 0.8)"
      ></div>
    </el-card>
    <!-- 预警信息 -->
    <el-dialog v-cloak title="预警信息" center :visible.sync="dialogShow" width="1000px">
      <!-- 表格显示区域 -->
      <el-table
        :data="rows"
        border
        stripe
        fit
        highlight-current-row
        :row-style="{ height: '5px' }"
        :cell-style="{ padding: '5px 0' }"
        :height="curHeight2"
      >
        <el-table-column align="center" label="序号" width="80">
          <template slot-scope="scope">
            {{ (currentPage - 1) * pageSize + scope.$index + 1 }}
          </template>
        </el-table-column>
        <el-table-column align="center" label="报警时间" width="200">
          <template slot-scope="scope">
            {{ scope.row.date }}
          </template>
        </el-table-column>
        <el-table-column align="center" label="报警信息" min-width="200">
          <template slot-scope="scope">
            <el-input
              v-model="scope.row.content"
              v-if="disable == scope.$index"
              @blur="confirmEdit(scope.row)"
            ></el-input>
            <span v-else>{{ scope.row.content }}</span>
          </template>
        </el-table-column>
        <el-table-column align="center" label="发布情况" width="120">
          <template slot-scope="scope">
            <el-button
              v-if="scope.row.status === 1"
              type="success"
              size="mini"
              style="width: 80px"
              @click="editProcessing(scope.row, 0)"
            >
              已发布
            </el-button>
            <el-button
              v-if="scope.row.status === 0"
              type="danger"
              size="mini"
              style="width: 80px"
              @click="editProcessing(scope.row, 1)"
            >
              待发布
            </el-button>
          </template>
        </el-table-column>
        <el-table-column align="center" label="操作">
          <template slot-scope="scope">
            <el-button type="warning" size="mini" @click="handelEditContent(scope.$index)">编辑</el-button>
          </template>
        </el-table-column>
      </el-table>
      <!-- 分页 -->
      <el-pagination
        background
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
        :current-page="currentPage"
        :page-sizes="[10, 20, 30, 40]"
        :page-size="pageSize"
        layout="total, sizes, prev, pager, next, jumper"
        :total="total"
      >
      </el-pagination>
    </el-dialog>
  </div>
</template>

<script>
import echarts from 'echarts';
export default {
  data() {
    return {
      // 卡片高度
      curHeight: {
        height: '',
        overflow: 'auto',
      },
      // 加载等待
      loading: true,
      // echart实例
      myChart: null,
      // 阈值
      thresholdRow: {
        id: 0,
        threshold: 0,
      },
      // 预警事件数量
      forewaringNum: null,
      // 表单
      rows: [],
      // 分页
      currentPage: 1,
      total: 0,
      pageSize: 10,
      // 表单高度
      curHeight2: 0,
      // 对话框
      dialogShow: false,
      // 处理情况
      processingOptions: [
        {
          label: '未处理',
          value: 0,
        },
        {
          label: '已处理',
          value: 1,
        },
      ],
      // 数据
      xAxixData1: [],
      xAxixData2: [],
      drawXAxixData: [],
      zzData: [],
      scData: [],
      disable: null,
    };
  },
  mounted() {
    this.setTableHeight();
    this.getStatics();
    this.getForewaringInfo();
    this.getThreshold();
    window.onresize = () => {
      this.setTableHeight();
      this.myChart.resize();
    };
  },
  methods: {
    //编辑按钮
    handelEditContent(index) {
      this.disable = index;
    },
    //input框失去焦点
    async confirmEdit(val) {
      this.disable = null;
      let params = {
        id: val.id,
        content: val.content,
      };
      const { data: res } = await this.$http.put('jdSys/WarningInfo', params);
      if (res.status == 200) {
        this.$message.success('编辑成功！');
      } else {
        this.$message.error('编辑失败！');
      }
      this.getForewaringInfo();
    },
    // 获取数据
    async getStatics() {
      const { data: res } = await this.$http.get('jdSys/DeptTest/statisticsTotalPassRate');
      if (res.status !== 200) {
        return this.$message({
          duration: 1000,
          type: 'error',
          message: '获取统计数据失败！',
        });
      }
      this.xAxixData1 = [];
      this.xAxixData2 = [];
      this.drawXAxixData = [];
      this.zzData = [];
      this.scData = [];
      // 数据获取
      for (let i = 0; i < res.data.zz.length; i++) {
        this.xAxixData1.push(res.data.zz[i][0]);
      }
      for (let i = 0; i < res.data.sc.length; i++) {
        this.xAxixData2.push(res.data.sc[i][0]);
      }
      this.zzData = res.data.zz;
      this.scData = res.data.sc;
      // 拼接
      this.drawXAxixData = [...this.xAxixData1, ...this.xAxixData2];
      // 去重
      this.drawXAxixData = [...new Set(this.drawXAxixData)];
      // 排序
      this.drawXAxixData.sort((a, b) => {
        return a.replace(new RegExp('-', 'g'), '') - b.replace(new RegExp('-', 'g'), '');
      });
      this.showGraphic();
    },
    async getForewaringInfo() {
      const { data: res } = await this.$http.get('jdSys/WarningInfo/pageByCondition', {
        params: {
          page: this.currentPage,
          limit: this.pageSize,
        },
      });
      if (res.status !== 200) {
        return this.$message({
          duration: 1000,
          type: 'error',
          message: '获取预警信息失败！',
        });
      }
      let tmpNum = 0;
      for (let i = 0; i < res.data.rows.length; i++) {
        if (res.data.rows[i].status === 0) {
          tmpNum++;
        }
      }
      this.forewaringNum = tmpNum === 0 ? null : tmpNum;
      this.rows = res.data.rows;
      this.total = res.data.total;
    },
    async getThreshold() {
      const { data: res } = await this.$http.get('jdSys/WarningThreshold/pageByCondition');
      if (res.status !== 200) {
        return this.$message({
          duration: 1000,
          type: 'error',
          message: '获取阈值失败！',
        });
      }
      this.thresholdRow = res.data.rows[0];
    },
    // 绘制图表
    showGraphic() {
      // 基于准备好的dom，初始化echarts实例
      this.myChart = echarts.init(document.getElementById('main'));
      // 配置属性
      let option = {
        title: {
          text: '农产品抽查合格率',
          left: 'center',
          align: 'right',
        },
        grid: {
          left: '50',
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
        color: ['#7ea04d', '#1f6f8b'],
        legend: {
          data: ['种植产品', '水产品'],
          left: 80,
          top: 10,
        },
        dataZoom: [
          {
            show: true,
            realtime: true,
            start: 0,
            // end: 45
          },
          {
            type: 'inside',
            realtime: true,
            start: 0,
            // end: 45
          },
        ],
        xAxis: [
          {
            type: 'category',
            boundaryGap: false,
            data: this.drawXAxixData,
          },
        ],
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
            name: '种植产品',
            type: 'line',
            lineStyle: {
              width: 2,
              color: '#7ea04d',
            },
            label: {
              show: true,
              position: 'top',
              formatter: function(params) {
                return params.value[params.encode.y[0]] + '%';
              },
            },
            symbol: 'circle',
            color: '#7ea04d',
            symbolSize: 8,
            data: this.zzData,
            // 这是让时间刻度缺失的点，数据正常连接不断开
            connectNulls: true,
          },
          {
            name: '水产品',
            type: 'line',
            lineStyle: {
              width: 2,
              color: '#1f6f8b',
            },
            label: {
              show: true,
              position: 'bottom',
              formatter: function(params) {
                return params.value[params.encode.y[0]] + '%';
              },
            },
            symbol: 'circle',
            color: '#1f6f8b',
            symbolSize: 8,
            data: this.scData,
            // 这是让h刻度缺失的点，数据正常连接不断开
            connectNulls: true,
          },
          {
            type: 'line',
            markLine: {
              silent: true,
              label: {
                formatter: '预警阈值: {c}',
                position: 'insideEndTop',
              },
              lineStyle: {
                color: '#ec0101',
                width: 2,
              },
              data: [{ yAxis: this.thresholdRow.threshold }],
            },
          },
        ],
      };
      // 绘制图表
      this.myChart.setOption(option);
      this.loading = false;
    },
    // 设定卡片高度
    setTableHeight() {
      let h = document.documentElement.clientHeight || document.body.clientHeight;
      this.curHeight.height = h - 177 + 'px';
      this.curHeight2 = h - 510;
    },
    // 修改阈值
    editThreshold() {
      this.$confirm('此操作将修改预警阈值, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning',
      })
        .then(async () => {
          const { data: res } = await this.$http.put('jdSys/WarningThreshold', this.thresholdRow);
          if (res.status !== 200) {
            return this.$message({
              duration: 1000,
              type: 'error',
              message: '修改阈值失败！',
            });
          }
          this.$message({
            duration: 1000,
            type: 'success',
            message: '修改阈值成功！',
          });
          this.getThreshold();
          this.getForewaringInfo();
          this.showGraphic();
        })
        .catch(() => {
          this.$message({
            duration: 1000,
            type: 'info',
            message: '已取消操作',
          });
        });
    },
    // 展示预警信息
    showForewaringInfo() {
      this.dialogShow = true;
    },
    // 每页多少条
    handleSizeChange(val) {
      this.pageSize = val;
      this.getForewaringInfo();
    },
    // 当前页
    handleCurrentChange(val) {
      this.currentPage = val;
      this.getForewaringInfo();
    },
    // 修改报警信息处理状态
    editProcessing(row, item) {
      let status;
      if (item === 0) status = '待发布';
      if (item === 1) status = '已发布';
      this.$confirm(`是否将报警状态修改为 ${status} ?`, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning',
      })
        .then(async () => {
          let params = {
            id: row.id,
            status: row.status === 0 ? 1 : 0,
          };
          console.log(params);
          const { data: res } = await this.$http.put('jdSys/WarningInfo', params);
          console.log(res);
          if (res.status === 200) {
            this.$message.success('修改成功！');
          }
          this.getForewaringInfo();
        })
        .catch(() => {
          this.$message({ type: 'info', message: '已取消操作' });
        });
    },
  },
};
</script>

<style lang="less" scoped>
.box-head {
  width: 100%;
  height: 60px;
  display: flex;
  align-items: center;
  // background-color: #f9813a;
}

.box-container {
  width: 100%;
  height: 680px;
  // background-color: #3797a4;
}
</style>
