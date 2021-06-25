<template>
  <div>
    <!-- 面包屑导航区域 -->
    <el-breadcrumb separator-class="el-icon-arrow-right">
      <el-breadcrumb-item :to="{ path: '/home' }">首页</el-breadcrumb-item>
      <el-breadcrumb-item>电子商务子系统</el-breadcrumb-item>
      <el-breadcrumb-item>基础数据管理</el-breadcrumb-item>
    </el-breadcrumb>
    <!-- 卡片视图区域 -->
    <el-card>
      <!-- 添加区域 -->
      <div>
        <el-button type="danger" size="small" icon="el-icon-delete" @click="handleBatchRemove()">批量删除</el-button>
        <el-button type="primary" size="small" icon="el-icon-circle-plus" @click="addRow">添加基础数据信息</el-button>
        <el-select
          v-model="listQuery.type"
          size="small"
          placeholder="数据类型"
          class="filter-item"
          style="width: 130px;margin-left: 10px"
          @change="handleFilter"
        >
          <el-option v-for="item in productOptions" :key="item" :label="item" :value="item" />
        </el-select>
        <el-input
          v-model="listQuery.text"
          size="small"
          style="width: 120px;margin-left: 10px"
          clearable
          placeholder="名称"
        ></el-input>
        <el-button
          class="filter-item"
          size="small"
          type="primary"
          style="margin-left: 0.5rem;"
          icon="el-icon-search"
          @click="handleFilter"
        >
          查询
        </el-button>
      </div>
      <!-- 表格显示区域 -->
      <el-table
        :data="rows"
        border
        stripe
        fit
        highlight-current-row
        :row-style="{ height: '5px' }"
        :cell-style="{ padding: '5px 0' }"
        :height="curHeight"
        @selection-change="checkSelect"
      >
        <el-table-column type="selection" width="40" label="全选"></el-table-column>
        <el-table-column align="center" label="序号" width="60">
          <template slot-scope="scope">
            {{ (listQuery.page - 1) * listQuery.limit + scope.$index + 1 }}
          </template>
        </el-table-column>
        <el-table-column align="center" label="编号" width="180">
          <template slot-scope="scope">
            <el-tooltip effect="light" :content="scope.row.code" placement="top">
              <el-link type="primary" :underline="false" @click="editRow(scope.row)">{{ scope.row.code }} </el-link>
            </el-tooltip>
          </template>
        </el-table-column>
        <el-table-column align="center" label="名称" width="280">
          <template slot-scope="scope">
            {{ scope.row.text }}
          </template>
        </el-table-column>
        <el-table-column align="center" label="备注" min-width="120">
          <template slot-scope="scope">
            {{ scope.row.remark }}
          </template>
        </el-table-column>
        <el-table-column align="center" label="操作" width="200">
          <template slot-scope="scope">
            <el-button size="mini" type="primary" icon="el-icon-edit" @click="editRow(scope.row)">编辑 </el-button>
            <el-button size="mini" type="danger" icon="el-icon-delete" @click="deleteRow(scope.row)">删除 </el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
    <!-- 新增编辑对话框 -->
    <el-dialog
      v-cloak
      title="基础数据信息"
      @close="closeDialog"
      center
      :visible.sync="dialogShow"
      top="7vh"
      width="40%"
      class="row-form"
    >
      <el-form :model="row" label-width="100px" class="demo-ruleForm" :rules="rules" ref="row">
        <el-tabs type="border-card">
          <el-tab-pane label="基本信息">
            <el-row>
              <el-col :span="12">
                <el-form-item label="分类:" prop="type">
                  <!-- <el-input v-model="row.productCategory" clearable maxlength="50" placeholder="请输入产品分类"></el-input> -->
                  <el-select v-model="row.type" style="width: 100%" placeholder="请选择农产品分类">
                    <el-option v-for="item in productOptions" :key="item" :label="item" :value="item"></el-option>
                  </el-select>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="编号:" prop="code">
                  <el-input v-model="row.code" :disabled="noInput" placeholder="请输入编号"></el-input>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="12">
                <el-form-item label="名称:" prop="text">
                  <el-input v-model="row.text" clearable placeholder="请输入名称"></el-input>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="备注:" prop="remark">
                  <el-input v-model="row.remark" clearable placeholder="请输入备注信息"></el-input>
                </el-form-item>
              </el-col>
            </el-row>
          </el-tab-pane>
        </el-tabs>
        <span class="t-btn">
          <el-button type="info" @click="dialogShow = false">取 消</el-button>
          <el-button type="success" @click="submitRow">提 交</el-button>
        </span>
      </el-form>
    </el-dialog>
  </div>
</template>

<script>
import userService from '../../globals/service/user';

export default {
  data() {
    return {
      //编号能否输入
      noInput: true,
      //提交状态,是提交还是编辑
      flag: '',
      //loading
      listLoading: false,
      // 搜索条件
      listQuery: {
        page: 1,
        limit: 10,
        type: '粮油',
        text: ''
      },
      maxCode: '',
      total: 0,
      // 表单高度
      curHeight: 0,
      //删除ids数组
      ids: [],
      // 对话框
      dialogShow: false,
      // 表单
      rows: [],
      poductList: [],
      row: {
        type: '',
        code: '',
        text: '',
        remark: ''
      },
      rules: {
        code: [{ required: true, message: '请输入编号', trigger: 'blur' }],
        type: [{ required: true, message: '请选择农产品分类', trigger: 'blur' }],
        text: [{ required: true, message: '请选择名称', trigger: 'blur' }]
      },
      // 多选框
      productOptions: ['粮油', '蔬菜', '水果', '牛羊猪肉', '家禽蛋类', '水产品', '其他']
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
    // 获取全部的列表数据
    getAllList() {
      userService.getProductNameDicByType(this.listQuery).then(res => {
        if (res.status !== 200) return this.$message.error('获取失败');
        this.listLoading = false;
        this.rows = res.data.rows;
        this.total = res.data.total;
        if (this.total !== 0) {
          if (this.total.toString().length === 1) {
            this.maxCode = this.rows[0].code.substring(0, 2) + '000' + (this.total + 1);
          } else if (this.total.toString().length === 2) {
            this.maxCode = this.rows[0].code.substring(0, 2) + '00' + (this.total + 1);
          } else if (this.total.toString().length === 3) {
            this.maxCode = this.rows[0].code.substring(0, 2) + '0' + (this.total + 1);
          } else if (this.total.toString().length === 4) {
            this.maxCode = this.rows[0].code.substring(0, 2) + (this.total + 1);
          }
          this.row.code = this.maxCode;
        }
      });
    },
    // 查询
    handleFilter() {
      this.listQuery.page = 1;
      this.getAllList();
    },
    // 每页多少条
    handleSizeChange(val) {
      this.listQuery.limit = val;
      this.getAllList();
    },
    //全选框事件
    checkSelect(data) {
      data.forEach(item => {
        this.ids.push(item);
      });
    },
    async handleBatchRemove() {
      if (this.ids.length === 0) return this.$message.warning('请先选中要删除的基础数据');
      const confirmResult = await this.$confirm('此操作将删除选中基础数据,是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).catch(err => err);
      if (confirmResult !== 'confirm') return this.$message.info('已经取消删除');
      this.ids.forEach((row, index) => {
        let formData = new FormData();
        formData.append('code', row.code);
        userService.delDicInfo(formData).then(res => {
          if (res.status == 200) {
            // 提醒删除成功
            this.$message.success('删除成功');
            this.ids.splice(index, 1);
          }
          // 刷新数据列表
          this.getAllList();
          // 关闭对话框
          this.dialogShow = false;
        });
      });
    },
    handleModifyStatus(row, status) {
      this.$confirm(`是否将发布状态修改为 ${status} ?`, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      })
        .then(() => {
          // console.log(row, status);
          row.status = status;
          userService.updateMarketingInfo(row).then(res => {
            if (res.status === 200) {
              this.$message({
                message: `操作成功，已修改为${status}`,
                type: 'success'
              });
            }
          });
        })
        .catch(() => {
          this.$message({ type: 'info', message: '已取消操作' });
        });
    },
    // 当前页
    handleCurrentChange(val) {
      this.listQuery.page = val;
      this.getAllList();
    },
    // 设定表格高度
    setTableHeight() {
      let h = document.documentElement.clientHeight || document.body.clientHeight;
      this.curHeight = h - 220;
    },
    // 新增
    addRow() {
      this.flag = 'add';
      this.noInput = false;
      this.listQuery.text = '';
      this.row = {};
      // this.getAllList();
      // this.row.type = this.listQuery.type;
      // this.row.code = this.maxCode;
      this.dialogShow = true;
    },
    // 编辑
    editRow(row) {
      this.flag = 'edit';
      this.noInput = true;
      this.row = row;
      this.dialogShow = true;
    },
    // 删除
    deleteRow(row) {
      // 弹出删除确认框
      this.$confirm('此操作将删除一条基础数据信息, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      })
        .then(() => {
          this.listLoading = true;
          let formData = new FormData();
          formData.append('code', row.code);
          // formData.append('remark', row.remark);
          userService.delDicInfo(formData).then(res => {
            if (res.status !== 200) return this.$message.error(res.message);
            // 刷新数据列表
            this.getAllList();
            // 提醒删除成功
            this.$message.success('删除成功');
            // 关闭对话框
            this.dialogShow = false;
          });
        })
        .catch(() => {
          this.$message({ type: 'info', message: '已取消删除' });
        });
    },
    // 关闭对话框
    closeDialog() {
      this.row = {};
      this.$refs.row.clearValidate();
    },
    // 提交数据
    submitRow() {
      this.$refs.row.validate(async valid => {
        if (!valid) return this.$message.error('信息填写不完整或不准确，请检查再提交！');
        let formData = new FormData();
        formData.append('code', this.row.code);
        formData.append('type', this.row.type);
        formData.append('text', this.row.text);
        if (this.row.remark === undefined) {
          this.row.remark = '';
        }
        formData.append('remark', this.row.remark);
        if (this.flag === 'add') {
          userService.addDicInfo(formData).then(res => {
            if (res.status !== 200) return this.$message.error(res.message);
            this.$message.success('新增一条基础数据成功');
            // 隐藏新增的对话框
            this.dialogShow = false;
            // 重新获取列表
            this.getAllList();
          });
        } else if (this.flag === 'edit') {
          this.editSubmit(formData);
        }
      });
    },
    //编辑的提交
    editSubmit(formDate) {
      userService.updateDicInfo(formDate).then(res => {
        if (res.status !== 200) return this.$message.error(res.message);
        // 关闭对话框
        this.dialogShow = false;
        // 刷新数据列表
        this.getAllList();
        // 提醒更新成功
        this.$message.success('更新成功');
      });
    }
    // 删除
    // delDicInfo(row) {
    //   console.log(row);
    //   let formData = new FormData();
    //   formData.append('code', row.code);
    //   formData.append('type', row.type);
    //   formData.append('text', row.text);
    //   formData.append('remark', row.remark);
    //   userService.delDicInfo(formData).then(res => {
    //     console.log(res);
    //     if (res.status !== 200) return this.$message.error('删除失败');
    //     // 刷新数据列表
    //     this.getAllList();
    //     // 提醒删除成功
    //     this.$message.success('删除成功');
    //     // 关闭对话框
    //     this.dialogShow = false;
    //   });
    // }
  }
};
</script>

<style lang="less" scoped></style>
