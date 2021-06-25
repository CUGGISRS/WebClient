<template>
  <div class="container">
    <!-- 查询区域 -->
    <div class="query-condition">
      <div class="query-item">
        <span style="margin-left: 20px;">传感器:</span>
        <el-select v-model="params.sensorId" style="width: 140px;">
          <el-option
              v-for="item in sensorOptions"
              :key="item.id"
              :label="item.name"
              :value="item.id">
          </el-option>
        </el-select>
      </div>
      <div class="query-item">
        <span>报警级别:</span>
        <el-select v-model="params.warningLevel" clearable style="width: 100px;" placeholder="请选择">
          <el-option
              v-for="item in levelOptions"
              :key="item.value"
              :label="item.label"
              :value="item.value">
          </el-option>
        </el-select>
      </div>
      <div class="query-item">
        <span>状态:</span>
        <el-select v-model="params.status" clearable style="width: 120px;" placeholder="请选择">
          <el-option
              v-for="item in statusOptions"
              :key="item.value"
              :label="item.label"
              :value="item.value">
          </el-option>
        </el-select>
      </div>
      <div class="query-item">
        <span>开始时间:</span>
        <el-date-picker
            value-format="yyyy-MM-dd HH:mm:ss"
            v-model="params.warningDateS"
            type="datetime" clearable
            style="width: 200px;"
            placeholder="请选择开始时间"
            default-time="00:00:00">
        </el-date-picker>
      </div>
      <div class="query-item">
        <span>结束时间:</span>
        <el-date-picker
            value-format="yyyy-MM-dd HH:mm:ss"
            v-model="params.warningDateE"
            type="datetime" clearable
            style="width: 200px;"
            placeholder="请选择结束时间"
            default-time="23:59:59">
        </el-date-picker>
      </div>
      <el-button size="small" type="primary" icon="el-icon-search" @click="searchRow">查询</el-button>
    </div>
    <!-- 按钮操作区域 -->
    <div>
      <el-button size="small" type="danger" icon="el-icon-delete" @click="deleteList" style="margin-left: 20px;">删除
      </el-button>
    </div>
    <div class="data-table">
      <!-- 表格显示区域 -->
      <el-table :data="rows"
                ref="myTable"
                border stripe fit highlight-current-row
                height="100%"
                :cell-style="{padding:'5px 0'}"
                @selection-change="handleSelectionChange">
        <el-table-column type="selection" align="center"></el-table-column>
        <el-table-column align="center" label="序号" width="60">
          <template slot-scope="scope">
            {{ (currentPage - 1) * pageSize + scope.$index + 1 }}
          </template>
        </el-table-column>
        <el-table-column align="center" label="报警时间" width="160">
          <template slot-scope="scope">
            {{ scope.row.warningDate }}
          </template>
        </el-table-column>
        <el-table-column align="center" label="级别" width="100">
          <template slot-scope="scope">
            {{ scope.row.warningLevel + '级' }}
          </template>
        </el-table-column>
        <el-table-column align="center" label="预警信息" min-width="100">
          <template slot-scope="scope">
            <el-tooltip effect="light" :content="scope.row.warningInfo" placement="top-end">
              <span>{{ textSubstr(scope.row.warningInfo, 40) }}</span>
            </el-tooltip>
          </template>
        </el-table-column>
        <el-table-column align="center" label="状态" width="160">
          <template slot-scope="scope">
            <el-select v-model="scope.row.status" @change="changeStatus(scope.row)">
              <el-option
                  v-for="item in statusOptions"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value">
              </el-option>
            </el-select>
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
        :page-size="pageSize"
        layout="total, sizes, prev, pager, next, jumper"
        :total="total">
    </el-pagination>
  </div>
</template>

<script>
export default {
  data() {
    return {
      // 查询参数
      params: {
        enterpriseId: window.sessionStorage.getItem('enterpriseId'),
        sensorId: null,
        warningLevel: null,
        status: null,
        warningDateS: null,
        warningDateE: null,
        page: null,
        limit: null
      },
      // 报警信息数据
      rows: [],
      // 分页
      currentPage: 1,
      pageSize: 20,
      total: 0,
      // 选中删除项
      selectRows: [],
      // 页面高度自适应
      curHeight: 0,
      // 选择项
      sensorOptions: [],
      levelOptions: [
        {label: '1级', value: 1},
        {label: '2级', value: 2},
        {label: '3级', value: 3},
        {label: '4级', value: 4},
        {label: '5级', value: 5}
      ],
      statusOptions: [
        {label: '未读', value: 0},
        {label: '已读', value: 1}
      ]
    };
  },
  created() {
    this.getAllSensor();
    // this.setHeight();
    // window.onresize = () => {
    //   this.setHeight();
    // };
  },
  methods: {
    // 设定高度
    setHeight() {
      let h = document.documentElement.clientHeight || document.body.clientHeight;
      this.curHeight = h - 262;
    },
    // 获取全部传感器列表
    async getAllSensor() {
      const {data: res} = await this.$http.get("zsSys/DeviceSensor/pageByCId", {
        params: {creatorId: window.sessionStorage.getItem('enterpriseId')}
      });
      if (res.status !== 200) return this.$message.error("获取报警配置数据失败!");
      this.sensorOptions = res.data.rows;
      this.params.sensorId = this.sensorOptions[0].id;
      this.getAllWarning();
    },
    // 获取全部报警配置列表
    async getAllWarning() {
      this.params.page = this.currentPage;
      this.params.limit = this.pageSize;
      const {data: res} = await this.$http.get("zsSys/WarningInfo/pageByEId", {params: this.params});
      if (res.status !== 200) return this.$message.error("获取报警信息数据失败!");
      this.rows = res.data.rows;
      this.total = res.data.total;
    },
    // 每页多少条
    handleSizeChange(val) {
      this.pageSize = val;
      this.getAllWarning();
    },
    // 当前页
    handleCurrentChange(val) {
      this.currentPage = val;
      this.getAllWarning();
    },
    // 处理选中数据
    handleSelectionChange(val) {
      this.selectRows = val;
    },
    // 清空全部筛选
    clearFilter() {
      this.$refs.myTable.clearSelection();
    },
    // 查询数据
    searchRow() {
      this.currentPage = 1;
      this.getAllWarning();
    },
    // 删除数据
    deleteList() {
      this.$confirm('此操作将删除选中的数据, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async () => {
        let delIds = [];
        for (let i = 0; i < this.selectRows.length; i++) {
          let item = this.selectRows[i];
          delIds.push(item.id);
        }
        const {data: res} = await this.$http.post('zsSys/WarningInfo/batchDelete', delIds);
        if (res.status !== 200) {
          return this.$message({
            duration: 2000,
            type: 'error',
            message: '删除报警信息失败！'
          });
        }
        this.$message({
          duration: 2000,
          type: 'success',
          message: '删除报警信息成功！'
        });
        this.clearFilter();
        this.getAllWarning();
      }).catch(() => {
        this.$message({
          duration: 1000,
          type: 'info',
          message: '已取消操作'
        });
      });
    },
    async changeStatus(row) {
      const {data: res} = await this.$http.put("zsSys/WarningInfo", row);
      if (res.status !== 200) {
        return this.$message({
          duration: 2000,
          type: "error",
          message: "修改报警信息状态失败！",
        });
      }
      this.$message({
        duration: 2000,
        type: "success",
        message: "修改报警信息状态成功！",
      });
      this.getAllWarning();
    },
    // 文字截取
    textSubstr(value, length) {
      let val = value
      if (value == '' || value == undefined) {
        return ''
      }
      if (value.length > length) {
        val = val.substring(0, length) + '...'
      }
      return val
    }
  }
};
</script>

<style lang="less" scoped>
// 容器样式
.container {
  position: relative;
  left: 0;
  bottom: 0;
  width: 100%;
  height: 100%;

  /* 查询栏样式 */

  .query-condition {
    display: flex;
    flex-wrap: wrap;
    margin-bottom: 15px;
  }

  .data-table {
    position: absolute;
    top: 62px;
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

  .query-condition .el-button {
    width: 80px;
    height: 40px;
    margin-top: 10px;
  }

  .query-condition .query-item {
    margin-top: 10px;
    margin-right: 20px;
  }

  .query-condition .query-item span {
    display: inline-block;
    height: 40px;
    line-height: 40px;
    margin-right: 5px;
  }
}
</style>
