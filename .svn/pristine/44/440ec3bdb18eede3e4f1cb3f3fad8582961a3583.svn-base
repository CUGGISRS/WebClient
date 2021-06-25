<template>
  <div>
    <!-- 面包屑导航区域 -->
    <el-breadcrumb separator-class="el-icon-arrow-right">
      <el-breadcrumb-item :to="{ path: '/home' }">首页</el-breadcrumb-item>
      <el-breadcrumb-item>公益性子系统</el-breadcrumb-item>
      <el-breadcrumb-item>用户管理</el-breadcrumb-item>
    </el-breadcrumb>
    <!-- 卡片视图区域 -->
    <el-card>
      <!-- 添加区域 -->
      <div>
        <el-button type="danger" size="small" icon="el-icon-delete" @click="handleBatchRemove()">批量删除</el-button>
        <!--        <el-button type="primary" size="small" icon="el-icon-circle-plus" @click="addRow">添加用户信息</el-button>-->
        <el-input
          v-model="listQuery.name"
          size="small"
          style="width: 120px;margin-left: 10px"
          clearable
          placeholder="用户姓名"
        ></el-input>
        <el-input
          v-model="listQuery.phone"
          size="small"
          style="width: 120px;margin-left: 10px"
          clearable
          placeholder="手机号"
        ></el-input>
        <el-button
          class="filter-item"
          size="small"
          type="primary"
          style=";margin-left: 0.5rem"
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
        <el-table-column align="center" label="账号" width="280">
          <template slot-scope="scope">
            <el-tooltip effect="light" :content="scope.row.account" placement="top">
              <el-link type="primary" :underline="false">{{ scope.row.account }} </el-link>
            </el-tooltip>
          </template>
        </el-table-column>
        <el-table-column align="center" label="姓名" width="180">
          <template slot-scope="scope">
            {{ scope.row.name }}
          </template>
        </el-table-column>
        <el-table-column align="center" label="手机号" min-width="120">
          <template slot-scope="scope">
            {{ scope.row.phone }}
          </template>
        </el-table-column>
        <el-table-column align="center" label="注册时间" min-width="120">
          <template slot-scope="scope">
            {{ scope.row.regTime }}
          </template>
        </el-table-column>
        <el-table-column align="center" label="启用状态" min-width="80">
          <template slot-scope="scope">
            <el-button
              v-if="scope.row.status === '0'"
              type="danger"
              size="mini"
              plain
              @click="handleChangeStatus(scope.row)"
              >已禁用</el-button
            >
            <el-button v-else type="success" size="mini" plain @click="handleChangeStatus(scope.row)">已启用</el-button>
          </template>
        </el-table-column>
        <!-- <el-table-column align="center" label="认证状态" min-width="120">
          <template slot-scope="scope">
            <el-button
              plain
              size="mini"
              v-if="scope.row.isVerify === 0 && scope.row.targetSystem !== '后台管理系统'"
              type="danger"
              @click="handleCertificationStatus(scope.row)"
              >待认证</el-button
            >
            <el-button v-else size="mini" type="success" plain @click="handleCertificationStatus(scope.row)"
              >已认证</el-button
            >
          </template>
        </el-table-column> -->
        <el-table-column align="center" label="身份" min-width="120">
          <template slot-scope="scope">
            <el-button plain size="mini" v-if="scope.row.targetSystem === '后台管理系统'" type="success"
              >管理员</el-button
            >
            <el-button plain size="mini" v-else type="info">普通用户</el-button>
          </template>
        </el-table-column>

        <!-- <el-table-column align="center" label="账户状态" min-width="120">
          <template slot-scope="scope">
            <el-button type="danger" size="mini" style="width: 80px" @click="handleModifyStatus(scope.row, '1')">
              恢复
            </el-button>
          </template>
        </el-table-column> -->
        <el-table-column align="center" label="操作" width="240">
          <template slot-scope="scope">
            <el-button size="mini" type="primary" icon="el-icon-edit" @click="reset(scope.row)">重置密码</el-button>
            <el-button size="mini" type="danger" icon="el-icon-delete" @click="deleteRow(scope.row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
      <!-- 分页 -->
      <el-pagination
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
        :current-page="listQuery.pageNum"
        :page-sizes="[15, 20, 25, 30]"
        :page-size="listQuery.pageRow"
        layout="total, sizes, prev, pager, next, jumper"
        :total="total"
      >
      </el-pagination>
    </el-card>
    <!-- 新增编辑对话框 -->
    <el-dialog
      v-cloak
      title="用户信息"
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
                <el-form-item label="账号:" prop="account">
                  <el-input v-model="row.account" maxlength="50" placeholder="请输入账号"></el-input>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="姓名:" prop="name">
                  <el-input v-model="row.name" placeholder="请输入姓名"></el-input>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="12">
                <el-form-item label="手机号:" prop="text">
                  <el-input v-model="row.phone" clearable placeholder="请输入手机号"></el-input>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="注册时间:" prop="remark">
                  <el-input v-model="row.regTime" clearable placeholder="请输入注册时间"></el-input>
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
    <el-dialog title="重置密码" center width="20%" :visible.sync="dialogResetPwdVisible">
      <el-form :model="resetRow" :rules="pwdRules" ref="pwdRulesRow">
        <el-form-item prop="newPwd">
          <el-input v-model="resetRow.newPwd" clearable maxlength="15" placeholder="请输入新密码"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button size="small" @click="dialogResetPwdVisible = false">取 消</el-button>
        <el-button size="small" type="primary" @click="resetPwd">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import userService from '../../globals/service/user';

export default {
  data() {
    return {
      //提交状态,是提交还是编辑
      flag: 'add',
      //loading
      listLoading: false,
      // activeName: 'all',
      // 搜索条件
      listQuery: {
        page: 1,
        limit: 15,
        name: '', // 用户姓名
        phone: '', // 手机号
        isDeleted: '' // 账户是否被删除
      },
      maxCode: '',
      total: 0,
      // 表单高度
      curHeight: 0,
      //删除ids数组
      ids: [],
      // 对话框
      dialogShow: false,
      dialogResetPwdVisible: false,
      // 表单
      rows: [],
      resetRow: {
        id: '',
        newPwd: ''
      },
      newPwd: '', //重置密码的新密码
      poductList: [],
      row: {
        name: '', // 用户姓名
        account: '', // 账号
        phone: '', // 手机号
        regTime: '', // 注册时间
        status: '', // 账户启用状态
        isVerify: '' //是否实名认证
      },
      rules: {},
      pwdRules: {
        newPwd: [{ required: true, message: '请输入新密码', trigger: 'blur' }]
      }
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
    handleChangeStatus(val) {
      let status = val.status == 1 ? 0 : 1;
      let formData = new FormData();
      formData.append('id', val.id);
      formData.append('status', status);

      this.$confirm('是否修改用户状态?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      })
        .then(() => {
          userService.updateUserStatus(formData).then(res => {
            if (res.status !== 200) return this.$message.error('服务错误！');
            this.$message.success('修改成功!');
            this.getAllList();
          });
        })
        .catch(() => {
          this.$message({
            type: 'info',
            message: '已取消修改'
          });
        });
    },
    // 获取全部的列表数据
    getAllList() {
      userService.getAllUserInfo(this.listQuery).then(res => {
        if (res.status !== 200) return this.$message.error('获取失败');
        this.listLoading = false;
        this.rows = res.data.rows;
        this.total = res.data.total;
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
    // handleModifyStatus(row) {
    //   this.$confirm(`是否恢复该账户?`, '提示', {
    //     confirmButtonText: '确定',
    //     cancelButtonText: '取消',
    //     type: 'warning'
    //   })
    //     .then(() => {
    //       let formData = new FormData();
    //       formData.append('id', row.id);
    //       formData.append('isDeleted', 0);
    //       userService.updateUserInfo(formData).then(res => {
    //         if (res.status === 200) {
    //           this.$message({
    //             message: '恢复成功',
    //             type: 'success'
    //           });
    //           this.getAllList();
    //         }
    //       });
    //     })
    //     .catch(() => {
    //       this.$message({ type: 'info', message: '已取消操作' });
    //     });
    // },
    //全选框事件
    checkSelect(data) {
      data.forEach(item => {
        this.ids.push(item.id);
      });
      this.ids = [...new Set(this.ids)];
    },

    async handleBatchRemove() {
      if (this.ids.length === 0) return this.$message.warning('请先选中要删除的用户信息');
      const confirmResult = await this.$confirm('此操作将删除选中用户信息,是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).catch(err => err);
      if (confirmResult !== 'confirm') return this.$message.info('已经取消删除');
      userService.delUserList(this.ids).then(res => {
        if (res.status !== 200) return this.$message.error('删除失败');
        this.$message.success('删除成功');
        this.getAllList();
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
      this.curHeight = h - 310;
    },
    // 新增
    addRow() {
      this.listQuery.text = '';
      this.getAllList();
      this.row.type = this.listQuery.type;
      // this.row.code = this.maxCode;
      this.dialogShow = true;
    },
    // 编辑
    editRow(row) {
      this.flag = 'edit';
      this.row = row;
      this.dialogShow = true;
    },
    //重置用户密码
    reset(row) {
      this.resetRow.id = row.id;
      this.resetRow.newPwd = 123456;
      this.dialogResetPwdVisible = true;
    },
    resetPwd() {
      this.$refs.pwdRulesRow.validate(async valid => {
        if (!valid) return;
        let formData = new FormData();
        formData.append('id', this.resetRow.id);
        formData.append('newPwd', this.resetRow.newPwd);
        userService.resetPwd(formData).then(res => {
          if (res.status !== 200) return this.$message.error('重置失败');
          // 刷新数据列表
          this.getAllList();
          this.$message.success('密码重置成功');
          this.dialogResetPwdVisible = false;
        });
      });
    },
    // 删除
    deleteRow(row) {
      // 弹出删除确认框
      this.$confirm('此操作将删除一条用户信息, 是否继续?', '警告', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      })
        .then(() => {
          this.listLoading = true;
          this.delUserInfo(row);
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
    submitRow() {},
    //编辑的提交
    editSubmit() {},
    // 删除
    delUserInfo(row) {
      userService.delUserInfo(row.id).then(res => {
        if (res.status !== 200) return this.$message.error('删除失败');
        // 刷新数据列表
        this.getAllList();
        // 提醒删除成功
        this.$message.success('删除成功');
        // 关闭对话框
        this.dialogShow = false;
      });
    }
  }
};
</script>

<style lang="less" scoped>
.el-tabs__header {
  padding: 0;
  position: relative;
}
</style>
