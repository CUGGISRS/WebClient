<template>
  <div>
    <!-- 面包屑导航区域 -->
    <el-breadcrumb separator-class="el-icon-arrow-right">
      <el-breadcrumb-item>首页</el-breadcrumb-item>
      <el-breadcrumb-item>企业信用评定</el-breadcrumb-item>
    </el-breadcrumb>
    <el-card>
      <!-- 查询区域 -->
      <div class="query-condition">
        <div class="query-item">
          <span>企业信用查询:</span>
          <el-input
            v-model="params.name"
            clearable
            style="width: 240px; margin:0 10px"
            placeholder="请输入企业名称"
          ></el-input>
          <el-select v-model="creditValue" clearable placeholder="请选择评定等级">
            <el-option v-for="item in creditOptions" :key="item.value" :label="item.label" :value="item.value">
            </el-option>
          </el-select>
        </div>
        <el-button size="small" type="primary" icon="el-icon-search" @click="searchRow">查询</el-button>
      </div>
      <el-table
        :data="rows"
        border
        stripe
        fit
        highlight-current-row
        :row-style="{ height: '5px' }"
        :cell-style="{ padding: '5px 0' }"
        :height="curHeight"
      >
        <el-table-column align="center" label="序号" width="80">
          <template slot-scope="scope">
            {{ (currentPage - 1) * pageSize + scope.$index + 1 }}
          </template>
        </el-table-column>
        <el-table-column align="center" label="企业名称" min-width="250">
          <template slot-scope="scope">
            {{ scope.row.name }}
          </template>
        </el-table-column>

        <el-table-column align="center" label="企业信用等级" min-width="200">
          <template slot-scope="scope">
            {{ scope.row.evaluateGrade2 }}
          </template>
        </el-table-column>
        <el-table-column align="center" label="评定时间" min-width="200">
          <template slot-scope="scope">
            {{ scope.row.evaluateTime }}
          </template>
        </el-table-column>
        <el-table-column align="center" label="操作" width="200">
          <template slot-scope="scope">
            <el-button
              size="small"
              type="warning"
              icon="el-icon-s-flag"
              @click="handleCreditRating(scope.row.name, scope.row.id, scope.row.evaluateGrade)"
              >评定等级</el-button
            >
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
    </el-card>

    <!-- 抽检弹出框 -->
    <el-dialog title="抽检信息" :visible.sync="dialogVisible" width="80%">
      <el-tabs type="border-card">
        <el-tab-pane label="产品抽检">
          <el-table
            :data="spotRows"
            border
            stripe
            fit
            highlight-current-row
            :row-style="{ height: '5px' }"
            :cell-style="{ padding: '5px 0' }"
            :height="curHeight"
          >
            <el-table-column align="center" label="序号" width="80">
              <template slot-scope="scope">
                {{ (currentPage - 1) * pageSize + scope.$index + 1 }}
              </template>
            </el-table-column>
            <el-table-column align="center" label="产品名称" min-width="200">
              <template slot-scope="scope">
                {{ scope.row.pName }}
              </template>
            </el-table-column>
            <el-table-column align="center" label="产品合格率（%）" min-width="200">
              <template slot-scope="scope">
                {{ scope.row.passRate }}
              </template>
            </el-table-column>
          </el-table>
        </el-tab-pane>
        <el-tab-pane label="投诉建议表">
          <span>选择时间段：</span>
          <el-date-picker
            v-model="value"
            type="daterange"
            align="right"
            unlink-panels
            value-format="yyyy-MM-dd"
            range-separator="至"
            start-placeholder="开始日期"
            end-placeholder="结束日期"
            :picker-options="pickerOptions"
          >
          </el-date-picker>
          <el-button type="primary" @click="handleSearchSuggest" style="margin-left: 30px">查询</el-button>
          <el-table
            :data="suggestRows"
            border
            stripe
            fit
            highlight-current-row
            :row-style="{ height: '5px' }"
            :cell-style="{ padding: '5px 0' }"
            :height="curHeight"
          >
            <el-table-column align="center" label="序号" width="80">
              <template slot-scope="scope">
                {{ (currentPage - 1) * pageSize + scope.$index + 1 }}
              </template>
            </el-table-column>
            <el-table-column align="center" label="反馈人" width="120">
              <template slot-scope="scope">
                {{ scope.row.pName }}
              </template>
            </el-table-column>
            <el-table-column align="center" label="联系方式" width="120">
              <template slot-scope="scope">
                {{ scope.row.phone }}
              </template>
            </el-table-column>
            <el-table-column align="center" label="类型" width="80">
              <template slot-scope="scope">
                {{ scope.row.type }}
              </template>
            </el-table-column>
            <el-table-column align="center" label="主题" width="260">
              <template slot-scope="scope">
                {{ scope.row.theme }}
              </template>
            </el-table-column>
            <el-table-column align="center" label="内容">
              <template slot-scope="scope">
                {{ scope.row.content }}
              </template>
            </el-table-column>
            <el-table-column align="center" label="时间" width="200">
              <template slot-scope="scope">
                {{ scope.row.updTime }}
              </template>
            </el-table-column>
          </el-table>
        </el-tab-pane>
      </el-tabs>
      <div style="margin-top: 30px;">
        <span>评定等级：</span>
        <el-select v-model="spotValue" placeholder="请选择评定等级">
          <el-option v-for="item in options" :key="item.value" :label="item.label" :value="item.value"> </el-option>
        </el-select>
        <span style="margin-left: 30px;">评定时间：</span>
        <el-date-picker v-model="value2" type="datetime" value-format="yyyy-MM-dd HH:mm:ss" placeholder="选择日期时间">
        </el-date-picker>
      </div>
      <span slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="submitSpotlevel">确 定</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import userService from '@/globals/service/user.js';
export default {
  data() {
    return {
      rows: [],
      // 表单高度
      curHeight: 0,
      // 参数条件
      params: {
        page: null,
        limit: null,
        name: null,
        status: 1,
      },
      // 分页
      currentPage: 1,
      pageSize: 20,
      total: 0,
      dialogVisible: false,
      spotRows: [],
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
      options: [],
      creditOptions: [],
      spotValue: '',
      creditValue: '',
      value: '',
      value2: '',
      companyId: '',
      companyName: '',
      suggestRows: [],
    };
  },
  created() {
    this.getAllList();
    this.setTableHeight();
    window.onresize = () => {
      this.setTableHeight();
    };
  },
  methods: {
    async getAllList() {
      this.params.page = this.currentPage;
      this.params.limit = this.pageSize;
      const { data: res } = await this.$http.get('zsSys/Enterprise/pageByCondition', { params: this.params });
      this.rows = res.data.rows;
      this.total = res.data.total;
      const { data: res2 } = await this.$http.get('comSys/DataDictionary/getAllMayToCondition', {
        params: { type: '企业评价等级' },
      });
      this.options = res2.data.map((item) => {
        this.rows.map((data) => {
          if (data.evaluateGrade !== null && data.evaluateGrade == item.code) {
            data.evaluateGrade2 = item.name;
          }
        });
        return {
          value: item.code,
          label: item.name,
        };
      });
      this.creditOptions = this.options;
      this.creditOptions.push({ value: '未评价', label: '未评价' });
    },
    async handleCreditRating(name, id, evaluateGrade) {
      this.companyName = name;
      this.companyId = id;
      this.spotValue = evaluateGrade;
      this.value2 = this.getNowTime();
      const { data: res } = await this.$http.get('jdSys/DeptTest/getPBNPassRateByEId', {
        params: { enterpriseId: id },
      });
      if (res.status == 200) {
        this.spotRows = res.data;
      }
      this.dialogVisible = true;
    },
    getNowTime() {
      let dateTime;
      let yy = new Date().getFullYear();
      let mm = new Date().getMonth() + 1;
      let dd = new Date().getDate();
      let hh = new Date().getHours();
      let mf = new Date().getMinutes() < 10 ? '0' + new Date().getMinutes() : new Date().getMinutes();
      let ss = new Date().getSeconds() < 10 ? '0' + new Date().getSeconds() : new Date().getSeconds();
      dateTime = yy + '-' + mm + '-' + dd + ' ' + hh + ':' + mf + ':' + ss;
      return dateTime;
    },
    handleSearchSuggest() {
      var formData = new FormData();
      formData.append('page', this.currentPage);
      formData.append('size', this.pageSize);
      formData.append('theme', this.companyName);
      if (this.value.length > 2) {
        formData.append('before', this.value[0]);
        formData.append('after', this.value[1]);
      }

      userService.searchSuggest(formData, { withCredentials: true }).then((res) => {
        console.log(res);
        if (res.status == 200) {
          this.suggestRows = res.data.rows;
        }
      });
    },
    async submitSpotlevel() {
      if (this.spotValue !== '' && this.value2 !== '') {
        const { data: res } = await this.$http.put('zsSys/Enterprise', {
          id: this.companyId,
          evaluateGrade: this.spotValue,
          evaluateTime: this.value2,
        });
        if (res.status == 200) {
          this.$message.success('评定成功！');
        }
      } else {
        this.$message.warning('请填写必要参数！');
      }
      this.getAllList();
      this.dialogVisible = false;
    },
    // 设定表格高度
    setTableHeight() {
      let h = document.documentElement.clientHeight || document.body.clientHeight;
      this.curHeight = h - 330;
    },
    // 查询数据
    searchRow() {
      this.currentPage = 1;
      this.params.evaluateGrade = this.creditValue;
      this.getAllList();
    },
    // 每页多少条
    handleSizeChange(val) {
      this.pageSize = val;
      this.getAllList();
    },
    // 当前页
    handleCurrentChange(val) {
      this.currentPage = val;
      this.getAllList();
    },
  },
};
</script>

<style scoped lang="less"></style>
