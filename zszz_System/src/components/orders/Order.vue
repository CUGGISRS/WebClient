<template>
  <div>
    <!-- 面包屑导航区域 -->
    <el-breadcrumb separator-class="el-icon-arrow-right">
      <el-breadcrumb-item>首页</el-breadcrumb-item>
      <el-breadcrumb-item>销售管理</el-breadcrumb-item>
    </el-breadcrumb>
    <!-- 卡片视图区域 -->
    <el-card>
      <!-- 查询区域 -->
      <div class="query-condition">
        <div class="query-item">
          <span>订单编号:</span>
          <el-input
            v-model="params.orderNumber"
            clearable
            style="width: 200px;"
            placeholder="请输入订单编号"
          ></el-input>
        </div>
        <div class="query-item">
          <span>追溯码:</span>
          <el-select
            v-model="params.productBatchNumber"
            clearable
            filterable
            style="width: 220px;"
            placeholder="请选择追溯码"
          >
            <el-option v-for="item in productBatchOptions" :key="item" :label="item" :value="item"> </el-option>
          </el-select>
        </div>
        <div class="query-item">
          <span>销售商名称:</span>
          <el-select v-model="params.sellerId" clearable style="width: 200px;" placeholder="请选择销售商">
            <el-option v-for="item in retailerOptions" :key="item.id" :label="item.name" :value="item.id"> </el-option>
          </el-select>
        </div>
        <div class="query-item">
          <span>发货日期:</span>
          <el-date-picker
            value-format="yyyy-MM-dd"
            v-model="params.sendDateS"
            type="date"
            style="width: 160px;"
            placeholder="请选择发货日期"
          >
          </el-date-picker>
        </div>
        <el-button size="small" type="primary" icon="el-icon-search" @click="searchRow">查询</el-button>
      </div>
      <!-- 按钮操作区域 -->
      <div>
        <el-button size="small" type="success" icon="el-icon-plus" @click="addRow">新增</el-button>
        <el-button size="small" type="danger" icon="el-icon-delete" @click="deleteList">删除</el-button>
      </div>
      <!-- 表格显示区域 -->
      <el-table
        :data="rows"
        ref="myTable"
        border
        stripe
        fit
        highlight-current-row
        :row-style="{ height: '5px' }"
        :cell-style="{ padding: '5px 0' }"
        :height="curHeight"
        @selection-change="handleSelectionChange"
      >
        <el-table-column type="selection" align="center"></el-table-column>
        <el-table-column align="center" label="序号" width="60">
          <template slot-scope="scope">
            {{ (currentPage - 1) * pageSize + scope.$index + 1 }}
          </template>
        </el-table-column>
        <el-table-column align="center" label="操作" width="100">
          <template slot-scope="scope">
            <el-button size="mini" icon="el-icon-edit-outline" @click="editRow(scope.row)">编辑</el-button>
          </template>
        </el-table-column>
        <el-table-column align="center" label="订单编号" width="200">
          <template slot-scope="scope">
            {{ scope.row.orderNumber }}
          </template>
        </el-table-column>
        <el-table-column align="center" label="追溯码" width="200">
          <template slot-scope="scope">
            {{ scope.row.productBatchNumber }}
          </template>
        </el-table-column>
        <el-table-column align="center" label="销售商" width="200">
          <template slot-scope="scope">
            {{ getName(scope.row.sellerId) }}
          </template>
        </el-table-column>
        <el-table-column align="center" label="销售数量" width="200">
          <template slot-scope="scope">
            {{ scope.row.saleAmount + getDictionaryName(scope.row.saleAmountUnit) }}
          </template>
        </el-table-column>
        <el-table-column align="center" label="发货时间" width="120">
          <template slot-scope="scope">
            {{ filterTime(scope.row.sendDate) }}
          </template>
        </el-table-column>
        <el-table-column align="center" label="目的地" min-width="100">
          <template slot-scope="scope">
            <el-tooltip effect="light" :content="scope.row.endPlace" placement="top">
              <span>{{ scope.row.endPlace }}</span>
            </el-tooltip>
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
    <!-- 新增编辑对话框 -->
    <el-dialog
      v-cloak
      title="订单信息"
      @close="closeDialog"
      center
      :visible.sync="dialogShow"
      width="1000px"
      class="row-form"
    >
      <el-form :model="row" label-width="120px" class="demo-ruleForm" :rules="rules" ref="row">
        <el-row>
          <el-col :span="12">
            <el-form-item label="订单编号:" prop="orderNumber">
              <el-input v-model="row.orderNumber" clearable maxlength="50" placeholder="请输入订单编号"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="追溯码:" prop="productBatchNumber">
              <el-select v-model="row.productBatchNumber" clearable filterable placeholder="请选择追溯码">
                <el-option v-for="item in productBatchOptions" :key="item" :label="item" :value="item"> </el-option>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row>
          <el-col :span="12">
            <el-form-item label="销售商名称:" prop="sellerId">
              <el-select v-model="row.sellerId" clearable placeholder="请选择销售商">
                <el-option v-for="item in retailerOptions" :key="item.id" :label="item.name" :value="item.id">
                </el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="发货时间:" prop="sendDate">
              <el-date-picker
                value-format="yyyy-MM-dd"
                v-model="row.sendDate"
                type="date"
                clearable
                placeholder="选择发货时间"
              >
              </el-date-picker>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row>
          <el-col :span="12">
            <el-form-item label="发货人:" prop="sender">
              <el-input v-model="row.sender" clearable maxlength="50" placeholder="请输入发货人"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="收货人:" prop="receiver">
              <el-input v-model="row.receiver" clearable maxlength="50" placeholder="请输入收货人"></el-input>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row>
          <el-col :span="12">
            <el-form-item label="始发地:" prop="startPlace">
              <el-input v-model="row.startPlace" clearable maxlength="255" placeholder="请输入始发地"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="目的地:" prop="endPlace">
              <el-input v-model="row.endPlace" clearable maxlength="255" placeholder="请输入目的地"></el-input>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row>
          <el-col :span="12">
            <el-form-item label="销售数量:" prop="saleAmount">
              <el-input
                v-model="row.saleAmount"
                clearable
                onkeyup="value=value.replace(/[^\d^\.]/g,'')"
                placeholder="请输入销售数量"
                @blur="saleAmountChange"
              >
              </el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="销售数量单位:" prop="saleAmountUnit">
              <el-select v-model="row.saleAmountUnit" clearable placeholder="请选择销售数量单位">
                <el-option v-for="item in amountUnitOptions" :key="item.code" :label="item.name" :value="item.code">
                </el-option>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>

        <span class="t-btn">
          <el-button type="info" @click="dialogShow = false">取 消</el-button>
          <el-button type="success" @click="submitRow">提 交</el-button>
        </span>
      </el-form>
    </el-dialog>
  </div>
</template>

<script>
import { Debounce } from '../../assets/js/tool';
export default {
  data() {
    return {
      // 参数条件
      params: {
        page: null,
        limit: null,
        orderNumber: null,
        productBatchNumber: null,
        sellerId: null,
        sendDateS: null,
        enterpriseId: window.sessionStorage.getItem('enterpriseId'),
        baseType: '种植基地',
      },
      // 表单
      rows: [],
      row: {},
      // 修改or新增flag
      flag: '',
      // 分页
      currentPage: 1,
      total: 0,
      pageSize: 20,
      // 表单高度
      curHeight: 0,
      // 对话框
      dialogShow: false,
      // 选中删除项
      selectRows: [],
      // 多选框
      retailerOptions: [],
      amountUnitOptions: [],
      productBatchOptions: [],
      // 验证规则
      rules: {
        // orderNumber: [{ required: true, message: '请输入订单编号', trigger: 'blur' }],
        productBatchNumber: [{ required: true, message: '请输入追溯码', trigger: 'blur' }],
        sellerId: [{ required: true, message: '请选择销售商', trigger: 'blur' }],
        sendDate: [{ required: true, message: '请选择发货时间', trigger: 'blur' }],
        // sender: [{ required: true, message: '请输入发货人', trigger: 'blur' }],
        // receiver: [{ required: true, message: '请输入收货人', trigger: 'blur' }],
        // startPlace: [{ required: true, message: '请输入始发地', trigger: 'blur' }],
        endPlace: [{ required: true, message: '请输入目的地', trigger: 'blur' }],
        saleAmount: [{ required: true, message: '请输入销售数量', trigger: 'blur' }],
        saleAmountUnit: [{ required: true, message: '请选择销售数量单位', trigger: 'blur' }],
      },
    };
  },
  created() {
    this.getAmountUnitDictionary();
    this.getProcessBatchDictionary();
    this.getRetailerDictionary();
    this.getAllList();
    this.setTableHeight();
    window.onresize = () => {
      this.setTableHeight();
    };
  },
  methods: {
    // 获取数据字典
    async getAmountUnitDictionary() {
      const { data: res } = await this.$http.get('comSys/DataDictionary/getAllMayToCondition', {
        params: { type: '数量单位' },
      });
      if (res.status !== 200) {
        return this.$message({
          duration: 1000,
          type: 'error',
          message: '获取数量单位失败！',
        });
      }
      this.amountUnitOptions = res.data;
    },
    async getProcessBatchDictionary() {
      const { data: res } = await this.$http.get('zsSys/ProcessBatch/pageByEId', {
        params: {
          enterpriseId: window.sessionStorage.getItem('enterpriseId'),
          baseType: '种植基地',
        },
      });
      if (res.status !== 200) {
        return this.$message({
          duration: 1000,
          type: 'error',
          message: '获取追溯码数据失败！',
        });
      }
      for (let i = 0; i < res.data.rows.length; i++) {
        this.productBatchOptions.push(res.data.rows[i].productBatchNumber);
      }
    },
    async getRetailerDictionary() {
      const { data: res } = await this.$http.get('zsSys/Seller/pageByEId', {
        params: {
          enterpriseId: window.sessionStorage.getItem('enterpriseId'),
        },
      });
      if (res.status !== 200) {
        return this.$message({
          duration: 1000,
          type: 'error',
          message: '获取销售商数据失败！',
        });
      }
      this.retailerOptions = res.data.rows;
    },
    // 获取全部的列表数据
    async getAllList() {
      this.params.page = this.currentPage;
      this.params.limit = this.pageSize;
      const { data: res } = await this.$http.get('zsSys/Sale/pageByEId', { params: this.params });
      if (res.status !== 200) {
        return this.$message({
          duration: 1000,
          type: 'error',
          message: '获取销售数据失败！',
        });
      }
      this.rows = res.data.rows;
      this.total = res.data.total;
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
    // 设定表格高度
    setTableHeight() {
      let h = document.documentElement.clientHeight || document.body.clientHeight;
      this.curHeight = h - 380;
    },
    // 处理选中数据
    handleSelectionChange(val) {
      this.selectRows = val;
    },
    // 清空全部筛选
    clearFilter() {
      this.$refs.myTable.clearSelection();
    },
    // 关闭对话框
    closeDialog() {
      this.row = {};
      this.getAllList();
      this.$refs.row.clearValidate();
    },
    // 编辑数据
    editRow(row) {
      this.row = row;
      this.dialogShow = true;
      this.flag = 'edit';
      if (this.getName(row.sellerId) == undefined) {
        this.row.sellerId = '';
      }
    },
    // 新增数据
    addRow() {
      this.dialogShow = true;
      this.flag = 'add';
    },
    // 查询数据
    searchRow() {
      this.currentPage = 1;
      this.getAllList();
    },
    // 删除数据
    deleteList() {
      this.$confirm('此操作将删除选中的数据, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning',
      })
        .then(async () => {
          let delIds = [];
          for (let i = 0; i < this.selectRows.length; i++) {
            let item = this.selectRows[i];
            delIds.push(item.id);
          }
          const { data: res } = await this.$http.post('zsSys/Sale/batchDelete', delIds);
          if (res.status !== 200) {
            return this.$message({
              duration: 2000,
              type: 'error',
              message: '删除订单失败！',
            });
          }
          this.$message({
            duration: 2000,
            type: 'success',
            message: '删除订单成功！',
          });
          this.clearFilter();
          this.getAllList();
        })
        .catch(() => {
          this.$message({
            duration: 1000,
            type: 'info',
            message: '已取消操作',
          });
        });
    },
    // 提交数据
    submitRow: Debounce(function() {
      this.row.enterpriseId = window.sessionStorage.getItem('enterpriseId');
      this.$refs.row.validate(async (valid) => {
        if (!valid) return this.$message.error('信息填写不完整或不准确，请检查再提交！');
        if (this.flag === 'add') {
          const { data: res } = await this.$http.post('zsSys/Sale', this.row);
          if (res.status !== 200) {
            return this.$message({
              duration: 2000,
              type: 'error',
              message: '新增订单失败！',
            });
          }
          this.getAllList();
          this.dialogShow = false;
          this.$message({
            duration: 2000,
            type: 'success',
            message: '新增订单成功！',
          });
        } else if (this.flag === 'edit') {
          const { data: res } = await this.$http.put('zsSys/Sale', this.row);
          if (res.status !== 200) {
            return this.$message({
              duration: 2000,
              type: 'error',
              message: '修改订单失败！',
            });
          }
          this.getAllList();
          this.dialogShow = false;
          this.$message({
            duration: 2000,
            type: 'success',
            message: '修改订单成功！',
          });
        }
      });
    }, 3000),
    // 过滤时间
    filterTime(time) {
      if (time) {
        return time.slice(0, 10);
      }
      return time;
    },
    // 解决只能输入数字输入框的值绑定问题
    saleAmountChange(e) {
      this.$set(this.row, 'saleAmount', e.target.value);
    },
    // 根据id获取name
    getName(id) {
      for (let i = 0; i < this.retailerOptions.length; i++) {
        if (id === this.retailerOptions[i].id) {
          return this.retailerOptions[i].name;
        }
      }
    },
    // 根据code转换成name
    getDictionaryName(code) {
      for (let i = 0; i < this.amountUnitOptions.length; i++) {
        if (code === this.amountUnitOptions[i].code) {
          return this.amountUnitOptions[i].name;
        }
      }
    },
  },
};
</script>

<style scoped lang="less"></style>
