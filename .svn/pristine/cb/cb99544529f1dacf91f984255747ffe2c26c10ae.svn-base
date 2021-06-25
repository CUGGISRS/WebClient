<template>
  <div>
    <!-- 面包屑导航区域 -->
    <el-breadcrumb separator-class="el-icon-arrow-right">
      <el-breadcrumb-item :to="{ path: '/home' }">首页</el-breadcrumb-item>
      <el-breadcrumb-item>电子商务子系统</el-breadcrumb-item>
      <el-breadcrumb-item>留言板信息管理</el-breadcrumb-item>
    </el-breadcrumb>
    <!-- 卡片视图区域 -->
    <el-card>
      <!-- 添加区域 -->
      <div>
        <el-button type="danger" size="small" icon="el-icon-delete" @click="handleBatchRemove()">批量删除</el-button>
        <el-select
          v-model="listQuery.status"
          placeholder="审核状态"
          size="small"
          clearable
          class="filter-item"
          style="width: 130px;margin-left: 10px"
        >
          <el-option v-for="item in stateTypeOptions" :key="item.key" :label="item.display_name" :value="item.key" />
        </el-select>
        <el-select
          v-model="listQuery.isReply"
          placeholder="回复状态"
          clearable
          size="small"
          class="filter-item"
          style="width: 130px;margin-left: 0.5rem"
        >
          <el-option v-for="item in replyStateOptions" :key="item.key" :label="item.display_name" :value="item.key" />
        </el-select>
        <el-button
          size="small"
          class="filter-item"
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
        v-loading="listLoading"
        :data="tables"
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
        <el-table-column align="center" label="标题">
          <template slot-scope="scope">
            <el-link type="primary" :underline="false" @click="replyRow(scope.row)">{{ scope.row.title }}</el-link>
          </template>
        </el-table-column>
        <el-table-column align="center" label="留言内容">
          <template slot-scope="scope">
            <el-popover placement="top-start" title="留言内容" width="250" trigger="hover">
              <div>{{ scope.row.content }}</div>
              <span
                slot="reference"
                v-if="scope.row.hasOwnProperty('content') && JSON.stringify(scope.row.content).length > 20"
                >{{ scope.row.content.substr(0, 20) }}...
              </span>
              <span v-else slot="reference">
                {{ scope.row.content }}
              </span>
            </el-popover>
          </template>
        </el-table-column>
        <el-table-column align="center" label="留言人" width="120">
          <template slot-scope="scope">
            {{ scope.row.commenter }}
          </template>
        </el-table-column>

        <el-table-column align="center" label="联系方式" width="120">
          <template slot-scope="scope">
            {{ scope.row.commenterPhone }}
          </template>
        </el-table-column>

        <el-table-column align="center" label="留言时间" width="160">
          <template slot-scope="scope">
            {{ scope.row.messageTime }}
          </template>
        </el-table-column>
        <el-table-column align="center" label="回复状态" width="120">
          <template slot-scope="scope">
            <span v-if="scope.row.isReply === 0" style="color: #F78989">未回复</span>
            <span v-else style="color: #67C23A">已回复</span>
          </template>
        </el-table-column>
        <el-table-column align="center" label="审核状态" width="120">
          <template slot-scope="scope">
            <el-button
              v-if="scope.row.status !== '待审核'"
              type="success"
              size="mini"
              style="width: 80px"
              @click="handleModifyStatus(scope.row, '待审核')"
            >
              审核通过
            </el-button>
            <el-button
              v-if="scope.row.status !== '审核通过'"
              type="danger"
              size="mini"
              style="width: 80px"
              @click="handleModifyStatus(scope.row, '审核通过')"
            >
              待审核
            </el-button>
          </template>
        </el-table-column>
        <el-table-column align="center" label="操作" width="200">
          <template slot-scope="scope">
            <el-button type="primary" size="mini" icon="el-icon-edit" @click="replyRow(scope.row)">回复</el-button>
            <el-button type="danger" size="mini" icon="el-icon-delete" @click="deleteRow(scope.row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
      <!-- 分页 -->
      <el-pagination
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
        :current-page="listQuery.page"
        :page-sizes="[10, 20, 30, 40]"
        :page-size="listQuery.limit"
        layout="total, sizes, prev, pager, next, jumper"
        :total="total"
      >
      </el-pagination>
    </el-card>
    <el-dialog
      v-cloak
      title="留言详情"
      @close="closeDialog"
      center
      :visible.sync="dialogShow"
      top="7vh"
      width="40%"
      class="row-form"
    >
      <el-form :model="form" label-width="100px" class="demo-ruleForm" ref="row">
        <el-tabs type="border-card">
          <el-row>
            <el-col :span="22">
              <el-form-item label="标题:" prop="productCategory">
                <el-input v-model="form.title" readonly></el-input>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="22">
              <el-form-item label="内容:">
                <el-input
                  v-model="form.content"
                  type="textarea"
                  :autosize="{ minRows: 3, maxRows: 6 }"
                  readonly
                ></el-input>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="10">
              <el-form-item label="留言人:" prop="productCategory">
                <el-input v-model="form.commenter" readonly></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="联系方式:" prop="productCategory">
                <el-input v-model="form.commenterPhone" readonly></el-input>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="10">
              <el-form-item label="留言时间:" prop="productCategory">
                <el-input v-model="form.messageTime" readonly></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="审核状态:" prop="productCategory">
                <el-input v-model="form.status" readonly></el-input>
              </el-form-item>
            </el-col>
          </el-row>
        </el-tabs>
        <el-divider></el-divider>
      </el-form>
      <el-form :model="replyForm" label-width="100px" class="demo-ruleForm" ref="replyRow">
        <el-tabs type="border-card" style="margin-top: 2rem">
          <el-row>
            <el-col :span="22">
              <el-form-item label="回复内容:">
                <el-input
                  v-model="replyForm.content"
                  placeholder="请输入回复内容"
                  type="textarea"
                  :autosize="{ minRows: 3, maxRows: 5 }"
                ></el-input>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="11">
              <el-form-item label="回复人:">
                <el-input v-model="replyForm.responder" placeholder="请输入回复人"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="11">
              <el-form-item label="回复时间:">
                <el-input v-model="replyForm.replyTime" readonly></el-input>
              </el-form-item>
            </el-col>
          </el-row>
        </el-tabs>
      </el-form>
      <span class="t-btn">
        <el-button type="info" @click="dialogShow = false">取 消</el-button>
        <el-button type="success" @click="submitRow">提 交</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import userService from '../../globals/service/user';

const stateTypeOptions = [
  { key: '待审核', display_name: '待审核' },
  { key: '审核通过', display_name: '审核通过' }
];
const replyStateOptions = [
  { key: '0', display_name: '未回复' },
  { key: '1', display_name: '已回复' }
];
const targetSystem = '电子商务系统';

export default {
  name: 'MessageBoard',
  data() {
    return {
      // 当前系统名称
      targetSystem,
      //审核状态
      stateTypeOptions,
      // 回复状态
      replyStateOptions,
      // 分页
      // currentPage: 1,
      // pageSize: 10,
      total: 0,
      // 表单高度
      curHeight: 0,
      // 对话框
      dialogShow: false,
      // 删除提示框
      dialogVisible: false,
      //loading
      listLoading: false,
      // 搜索条件
      listQuery: {
        page: 1,
        limit: 10,
        status: '', //审核状态
        isReply: '', // 回复状态
        targetSystem: targetSystem //目标系统
      },
      // 表单
      form: {
        id: '', // id
        title: '', //标题
        content: '', // 内容
        messageTime: '', // 留言时间
        userid: '', // 留言人id
        commenter: '', // 留言人
        commenterPhone: '', // 留言人联系方式
        address: '', // 地址
        targetSystem: '', // 目标系统
        isReply: '', // 是否已回复
        status: '' // 留言状态
      },
      replyForm: {
        qaId: '', // 回复id
        content: '', // 回复内容
        responder: '', // 回复人
        replyTime: '' //回复时间
      },
      row: {},
      //删除ids数组
      ids: [],
      // el-table的数据
      tables: []
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
    // 获取全部的留言信息
    async getAllList() {
      this.listLoading = true;
      let params = this.listQuery;
      userService.getQAInfoByPage(params).then(res => {
        if (res.status !== 200) return this.$message.error('获取失败');
        this.tables = res.data.rows;
        this.total = res.data.total;
        this.listLoading = false;
      });
    },
    //全选框事件
    checkSelect(data) {
      data.forEach(item => {
        this.ids.push(item.id);
      });
    },
    async handleBatchRemove() {
      if (this.ids.length === 0) return this.$message.warning('请先选中要删除的留言信息');
      const confirmResult = await this.$confirm('此操作将删除选中留言信息,是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).catch(err => err);
      if (confirmResult !== 'confirm') return this.$message.info('已经取消删除');
      this.ids.forEach(id => {
        userService.delQAById(id).then(res => {
          if (res.status !== 200) return this.$message.error('删除留言信息失败');
          this.getAllList();
        });
      });
    },
    handleFilter() {
      this.listQuery.page = 1;
      this.getAllList();
    },
    // 点击回复按钮
    replyRow(row) {
      this.flag = 'reply';
      this.form = row;
      this.replyForm = {
        replyTime: this.$moment(new Date()).format('YYYY-MM-DD HH:mm:ss')
      };
      if (row.isReply === 1) {
        userService.getQAReplyInfo(row.id).then(res => {
          if (res.status === 200) {
            this.replyForm = res.data[0];
          }
        });
      }
      this.dialogShow = true;
    },
    submitRow() {
      // 指定回复留言id
      this.replyForm.qaId = this.form.id;
      // 回复时间
      this.replyForm.replyTime = this.$moment(new Date()).format('YYYY-MM-DD HH:mm:ss');
      if (this.form.isReply === 0) {
        userService.replyQAById(this.replyForm).then(res => {
          if (res.status === 200) {
            this.$message({
              message: `回复成功`,
              type: 'success'
            });
            this.dialogShow = false;
            this.getAllList();
          }
        });
      } else {
        userService.updateReplyQAInfo(this.replyForm).then(res => {
          if (res.status === 200) {
            this.$message({
              message: `更新回复消息成功`,
              type: 'success'
            });
            this.dialogShow = false;
            this.getAllList();
          }
        });
      }
    },
    // 修改审核状态
    handleModifyStatus(row, status) {
      this.$confirm(`是否将审核状态修改为 ${status} ?`, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      })
        .then(() => {
          row.status = status;
          userService.ModifyQAStatus(row).then(res => {
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
    async deleteRow(row) {
      const confirmResult = await this.$confirm('此操作将删除该留言信息,是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).catch(err => err);
      if (confirmResult !== 'confirm') return this.$message.info('已经取消删除');
      userService.delQAById(row.id).then(res => {
        if (res.status !== 200) return this.$message.error('删除留言信息失败');
        this.$message.success('删除留言信息成功');
        this.getAllList();
      });
    },
    // 每页多少条
    handleSizeChange(val) {
      this.listQuery.limit = val;
      this.getAllList();
    },
    // 当前页
    handleCurrentChange(val) {
      this.listQuery.page = val;
      this.getAllList();
    },
    // 设定表格高度
    setTableHeight() {
      let h = document.documentElement.clientHeight || document.body.clientHeight;
      this.curHeight = h - 280;
    },
    // 关闭对话框
    closeDialog() {
      this.row = {};
      this.$refs.row.clearValidate();
      this.$refs.replyRow.clearValidate();
    }
  }
};
</script>

<style lang="less" scoped></style>
<style lang="less">
.link-type,
.link-type:focus {
  color: #337ab7;
  cursor: pointer;

  & :hover {
    color: rgb(32, 160, 255);
  }
}
</style>
