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
        <el-select v-model="params.sensorId" placeholder="请选择" @change="getDataBySensorId">
          <el-option
              v-for="item in sensorLists"
              :key="item.id"
              :label="item.name"
              :value="item.id">
          </el-option>
        </el-select>
        <el-select v-model="params.interval" placeholder="请选择">
          <el-option
              v-for="item in intervalOptions"
              :key="item.value"
              :label="item.label"
              :value="item.value">
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
            :default-time="['00:00:00', '23:59:59']">
        </el-date-picker>
        <el-button class="el-icon-search" type="primary" @click="searchData">检索</el-button>
        <el-button class="el-icon-download" type="primary" @click="downloadData" :loading="downloading">导出</el-button>
      </div>
      <div class="data-main">
        <div class="data-table">
          <el-table :data="dataLists" border stripe fit class="table-scroll-bar" height="100%" :style="curHeight2">
            <el-table-column label="时间" align="center">
              <template slot-scope="scope">
                {{ scope.row.recordTime }}
              </template>
            </el-table-column>
            <el-table-column :label="name" align="center">
              <template slot-scope="scope">
                {{ scope.row.recordData }}
              </template>
            </el-table-column>
          </el-table>
        </div>
        <!-- 分页 -->
        <el-pagination
            background
            @size-change="handleSizeChange"
            @current-change="handleCurrentChange"
            :current-page="currentPage"
            :page-sizes="[10, 20, 30, 40, 50, 100]"
            :page-size="pageSize"
            layout="total, sizes, prev, pager, next, jumper"
            :total="total">
        </el-pagination>
      </div>
    </div>
  </div>
</template>

<script>
import {exportToExcel, formatDateTime} from "../../assets/js/tool";

export default {
  data() {
    return {
      // 设备列表
      groupLists: [],
      // 传感器列表
      sensorLists: [],
      // 数据列表
      dataLists: [],
      // 当前设备id
      defaultGroupId: 0,
      // 当前传感器id
      defaultSensorId: 0,
      // 当前设备索引
      defaultIndex: 0,
      // 页面高度自适应
      curHeight: {
        height: '',
        overflow: 'auto'
      },
      curHeight2: {
        height: '',
        overflow: 'auto'
      },
      // 日期选择
      pickerOptions: {
        shortcuts: [{
          text: '最近一周',
          onClick(picker) {
            const end = new Date();
            const start = new Date();
            start.setTime(start.getTime() - 3600 * 1000 * 24 * 7);
            picker.$emit('pick', [start, end]);
          }
        }, {
          text: '最近一个月',
          onClick(picker) {
            const end = new Date();
            const start = new Date();
            start.setTime(start.getTime() - 3600 * 1000 * 24 * 30);
            picker.$emit('pick', [start, end]);
          }
        }, {
          text: '最近三个月',
          onClick(picker) {
            const end = new Date();
            const start = new Date();
            start.setTime(start.getTime() - 3600 * 1000 * 24 * 90);
            picker.$emit('pick', [start, end]);
          }
        }]
      },
      // 选择的日期的值
      timeValue: [],
      // 数据查询参数
      params: {
        recordTimeS: null,
        recordTimeE: null,
        interval: 1,
        groupId: null,
        sensorId: null,
        page: null,
        limit: null,
      },
      // 数据间隔
      intervalOptions: [
        {label: '1分钟', value: 1},
        {label: '2分钟', value: 2},
        {label: '5分钟', value: 5},
        {label: '10分钟', value: 10},
        {label: '30分钟', value: 30},
        {label: '1小时', value: 60},
        {label: '4小时', value: 240},
        {label: '6小时', value: 360},
        {label: '12小时', value: 720},
        {label: '24小时', value: 1440}
      ],
      // 分页
      currentPage: 1,
      pageSize: 20,
      total: 0,
      // table表头名称
      downLoadContentsTitle: [],
      // 待下载table数据
      downLoadContents: [],
      // 下载文件名称
      downLoadFileName: '未定义',
      // 下载等待
      downloading: false,
      // 表名
      name: ''
    }
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
      let w = document.documentElement.clientWidth || document.body.clientWidth;
      let h = document.documentElement.clientHeight || document.body.clientHeight;
      this.curHeight.height = h - 100 + 'px';
      this.curHeight2.width = w - 440 + 'px';
      this.curHeight2.height = h - 212 + 'px';
    },
    // 获取全部设备
    async getAllGroup() {
      const {data: res} = await this.$http.get('zsSys/DeviceGroup/pageByCId', {
        params: {creatorId: window.sessionStorage.getItem('enterpriseId')}
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
      const {data: res} = await this.$http.get("zsSys/DeviceSensor/pageByGId", {
        params: {groupId}
      });
      if (res.status !== 200) return this.$message.error("获取传感器数据失败!");
      this.sensorLists = res.data.rows;
      if (this.sensorLists && this.sensorLists.length > 0) {
        this.getDataBySensorId(this.sensorLists[0].id);
      }
    },
    // 根据传感器id获取数据
    async getDataBySensorId(sensorId) {
      this.defaultSensorId = sensorId;
      this.params.page = this.currentPage;
      this.params.limit = this.pageSize;
      this.params.recordTimeS = this.timeValue && this.timeValue.length > 0 ? this.timeValue[0] : null;
      this.params.recordTimeE = this.timeValue && this.timeValue.length > 0 ? this.timeValue[1] : null;
      this.params.sensorId = sensorId;
      const {data: res} = await this.$http.get('zsSys/DeviceSensorData/pageBySId', {params: this.params});
      if (res.status !== 200) return this.$message.error('获取数据失败!');
      this.dataLists = res.data.rows;
      this.total = res.data.total;
      this.sensorLists.map(item => {
        if (item.id === sensorId) {
          this.name = `${item.name}(${item.unit})`
        }
      });
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
          this.timeValue = [];
          this.params.interval = 1;
          const end = new Date();
          const start = new Date();
          start.setTime(start.getTime() - 3600 * 1000 * 24 * 7);
          this.timeValue.push(formatDateTime(start));
          this.timeValue.push(formatDateTime(end));
        }
      }
      this.getSensorByGroupId(groupId);
    },
    // 查询数据
    searchData() {
      this.currentPage = 1;
      this.getDataBySensorId(this.defaultSensorId);
    },
    // 每页多少条
    handleSizeChange(val) {
      this.pageSize = val;
      this.getDataBySensorId(this.defaultSensorId);
    },
    // 当前页
    handleCurrentChange(val) {
      this.currentPage = val;
      this.getDataBySensorId(this.defaultSensorId);
    },
    // 获取下载文件名称
    getDownloadFileName() {
      if (this.defaultGroupId && this.defaultSensorId) {
        this.groupLists.map(item => {
          if (item.id === this.defaultGroupId) {
            this.downLoadFileName = item.name;
          }
        });
        this.sensorLists.map(item => {
          if (item.id === this.defaultSensorId) {
            this.downLoadFileName = `${this.downLoadFileName}-${item.name}`;
          }
        });
      }
      return this.downLoadFileName;
    },
    // 下载导出excel
    async downloadData() {
      this.downLoadContents = [];
      this.downloading = true;
      let tmpArr = [];
      const {data: res} = await this.$http.get('zsSys/DeviceSensorData/pageBySId', {
        params: {
          recordTimeS: this.timeValue && this.timeValue.length > 0 ? this.timeValue[0] : null,
          recordTimeE: this.timeValue && this.timeValue.length > 0 ? this.timeValue[1] : null,
          sensorId: this.defaultSensorId,
          interval: this.params.interval
        }
      });
      if (res.status !== 200) return this.$message.error('下载数据失败!');
      res.data.rows.map(item => {
        tmpArr = [];
        tmpArr.push(item.recordTime);
        tmpArr.push(item.recordData);
        this.downLoadContents.push(tmpArr);
      });
      this.downLoadContentsTitle = ['时间', this.name]
      exportToExcel(this.downLoadContentsTitle, this.downLoadContents, this.getDownloadFileName());
      this.downloading = false;
    }
  }
}
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
      height: 50px;
      width: 100%;

      .el-select {
        width: 120px;
        margin: 10px 10px 0 0;
      }

      .el-button {
        margin-left: 10px;
      }
    }

    // 数据内容样式
    .data-main {
      top: 60px;
      position: absolute;
      right: 0;
      left: 0;
      bottom: 0;
      // background-color: pink;

      .data-table {
        position: absolute;
        top: 0;
        right: 0;
        left: 0;
        bottom: 32px;
        margin-bottom: 10px;

        .el-table {
          margin-top: 0;
        }
      }

      .el-pagination {
        height: 32px;
        bottom: 0;
        left: 0;
        right: 0;
        position: absolute;
        margin-top: 0;
        margin-bottom: 0;
      }
    }

    // 滚动条样式
    .table-scroll-bar::-webkit-scrollbar {
      width: 6px !important;
      height: 10px;
    }

    //滑块部分
    .table-scroll-bar::-webkit-scrollbar-thumb {
      height: 10%;
      border-radius: 5px;
      background-color: #bbbfca;
    }
  }
}
</style>
