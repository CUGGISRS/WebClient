<template>
  <div>
    <!-- 面包屑导航区域 -->
    <el-breadcrumb separator-class="el-icon-arrow-right">
      <el-breadcrumb-item :to="{ path: '/home' }">首页</el-breadcrumb-item>
      <el-breadcrumb-item>公益性子系统</el-breadcrumb-item>
      <el-breadcrumb-item>专家服务</el-breadcrumb-item>
    </el-breadcrumb>
    <el-card>
      <!-- 添加区域 -->
      <div>
        <el-button type="danger" size="small" icon="el-icon-delete" @click="handleBatchRemove()">批量删除</el-button>
        <el-button type="primary" size="small" icon="el-icon-circle-plus" @click="addRow">添加专家信息</el-button>
        <el-input
          v-model="listQuery.expertName"
          size="small"
          style="width: 120px;margin-left: 20px"
          clearable
          @input="handleFilter"
          placeholder="专家名称"
        ></el-input>
        <el-input
          v-model="listQuery.serviceArea"
          size="small"
          style="width: 120px;margin-left: 20px"
          clearable
          @input="handleFilter"
          placeholder="服务地区"
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
        v-loading="listLoading"
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
        <el-table-column align="center" label="专家姓名" min-width="160">
          <template slot-scope="scope">
            <el-tooltip effect="light" :content="scope.row.expertName" placement="top">
              <el-link type="primary" :underline="false" @click="editDialogRow(scope.row)">{{
                scope.row.expertName
              }}</el-link>
            </el-tooltip>
          </template>
        </el-table-column>
        <el-table-column align="center" label="联系电话" width="160">
          <template slot-scope="scope">
            {{ scope.row.phone }}
          </template>
        </el-table-column>
        <el-table-column align="center" label="微信号" width="120">
          <template slot-scope="scope">
            <span>{{ scope.row.wechat }}</span>
          </template>
        </el-table-column>
        <el-table-column align="center" label="业务专长" width="120">
          <template slot-scope="scope">
            <span>{{ scope.row.expertise }}</span>
          </template>
        </el-table-column>
        <el-table-column align="center" label="职称" width="120">
          <template slot-scope="scope">
            <span>{{ scope.row.jobTitle }}</span>
          </template>
        </el-table-column>
        <el-table-column align="center" label="服务地区" width="120">
          <template slot-scope="scope">
            <span>{{ scope.row.serviceArea }}</span>
          </template>
        </el-table-column>
        <el-table-column align="center" label="专家简介" min-width="160">
          <template slot-scope="scope">
            <el-popover placement="top-start" title="专家简介" width="250" trigger="hover">
              <div>{{ scope.row.introduction }}</div>
              <span
                slot="reference"
                v-if="scope.row.hasOwnProperty('introduction') && JSON.stringify(scope.row.introduction).length > 20"
                >{{ scope.row.introduction.substr(0, 20) }}...
              </span>
              <span v-else slot="reference">
                {{ scope.row.introduction }}
              </span>
            </el-popover>
          </template>
        </el-table-column>
        <el-table-column align="center" label="操作" width="200">
          <template slot-scope="scope">
            <el-button size="mini" type="primary" icon="el-icon-edit" @click="editDialogRow(scope.row)">编辑</el-button>
            <el-button size="mini" type="danger" icon="el-icon-delete" @click="deleteRow(scope.row.id)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
      <!-- 分页 -->
      <el-pagination
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
        :current-page="listQuery.page"
        :page-sizes="[15, 20, 25, 30]"
        :page-size="listQuery.limit"
        layout="total, sizes, prev, pager, next, jumper"
        :total="total"
      >
      </el-pagination>
    </el-card>

    <!-- 新增专家信息对话框 -->
    <el-dialog
      v-cloak
      title="专家信息管理"
      @close="DialogClose()"
      center
      :visible.sync="dialogShow"
      top="7vh"
      width="50%"
      class="row-form"
    >
      <el-form :model="row" label-width="100px" class="demo-ruleForm" :rules="rules" ref="addFormRef">
        <el-tabs type="border-card">
          <el-tab-pane label="专家基本信息">
            <el-row>
              <el-col :span="12">
                <el-form-item label="专家姓名:" prop="expertName">
                  <el-input v-model="row.expertName" clearable maxlength="20" placeholder="请输入专家姓名"></el-input>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="联系电话" prop="phone">
                  <el-input
                    v-model="row.phone"
                    clearable
                    onkeyup="this.value=this.value.replace(/[^\d]/g,'')"
                    maxlength="11"
                    placeholder="请输入联系电话"
                  ></el-input>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="12">
                <el-form-item label="微信号:" prop="wechat">
                  <el-input v-model="row.wechat" clearable maxlength="20" placeholder="请输入微信号"></el-input>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="业务专长:" prop="expertise">
                  <el-input v-model="row.expertise" clearable placeholder="请输入业务专长"></el-input>
                </el-form-item>
              </el-col>
            </el-row>

            <el-row>
              <el-col :span="12">
                <el-form-item label="职称:" prop="jobTitle">
                  <el-input v-model="row.jobTitle" clearable maxlength="20" placeholder="请输入职称"></el-input>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="服务地区:" prop="serviceArea">
                  <el-input v-model="row.serviceArea" clearable placeholder="请输入服务地区"></el-input>
                </el-form-item>
              </el-col>
            </el-row>

            <el-row>
              <el-col :span="22">
                <el-form-item label="专家简介:" prop="introduction">
                  <el-input
                    v-model="row.introduction"
                    clearable
                    type="textarea"
                    :autosize="{ minRows: 5, maxRows: 10 }"
                    placeholder="请输入专家简介"
                  ></el-input>
                </el-form-item>
              </el-col>
            </el-row>

            <el-row>
              <el-col :span="12" class="register-upload">
                <el-form-item label="上传专家照片:">
                  <el-upload
                    class="avatar-uploader"
                    :action="updateUrl"
                    :show-file-list="false"
                    :http-request="httpRequest"
                    :before-upload="beforeAvatarUpload"
                    :on-preview="handleProductPreview"
                  >
                    <img v-if="imageUrl" :src="imageUrl" class="avatar" />
                    <i v-else class="el-icon-plus avatar-uploader-icon"></i>
                  </el-upload>
                  <el-dialog :visible.sync="dialogProductVisible" width="70%" top="5vh" :append-to-body="true">
                    <img width="100%" :src="imageUrl" alt="" />
                  </el-dialog>
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
      activeName: '',
      // 搜索条件
      listQuery: {
        page: 1,
        limit: 15,
        expertName: '', // 专家姓名
        serviceArea: '' // 服务地区
      },
      total: 0,
      // 数据列表
      rows: [],
      // 表单数据
      row: {
        expertName: '', //专家姓名
        phone: '', //联系电话
        wechat: '', // 微信号
        expertise: '', // 业务专长
        jobTitle: '', // 职称
        serviceArea: '', // 服务地区
        introduction: '' // 专家简介
      },
      // 表单高度
      curHeight: 0,
      //删除ids数组
      ids: [],
      // 新增专家信息弹窗
      dialogShow: false,
      listLoading: false,
      // 图片文件上传
      updateUrl: '#',
      imageUrl: '',
      dialogProductVisible: false,
      // 上传文件
      multfileImg: null,
      //提交状态
      flag: 'add',
      // 验证规则
      rules: {
        expertName: [{ required: true, message: '请输入专家姓名', trigger: 'blur' }],
        phone: [{ required: true, message: '请输入联系电话', trigger: 'blur' }],
        wechat: [{ required: true, message: '请输入微信号', trigger: 'blur' }],
        expertise: [{ required: true, message: '请输入业务专长', trigger: 'blur' }],
        jobTitle: [{ required: true, message: '请输入职称', trigger: 'blur' }],
        serviceArea: [{ required: true, message: '请输入服务地区', trigger: 'blur' }],
        introduction: [{ required: true, message: '请输入专家简介', trigger: 'blur' }]
      }
    };
  },
  created() {
    this.getList();
    this.setTableHeight();
    window.onresize = () => {
      this.setTableHeight();
    };
  },
  methods: {
    handleProductPreview(file) {
      this.imageUrl = file.url;
      this.dialogProductVisible = true;
    },
    // 获取供求信息
    getList() {
      this.listLoading = true;
      let params = this.listQuery;

      userService.getExpertList(params).then(res => {
        if (res.status !== 200) return this.$message.error('获取失败');
        this.rows = res.data.rows;
        this.listLoading = false;
        this.total = res.data.total;
      });
    },
    // 查询
    handleFilter() {
      this.listQuery.page = 1;
      this.getList();
    },
    //全选框事件
    checkSelect(data) {
      data.forEach(item => {
        this.ids.push(item.id);
      });
    },
    async handleBatchRemove() {
      if (this.ids.length === 0) return this.$message.warning('请先选中要删除的专家信息');
      const confirmResult = await this.$confirm('此操作将删除选中专家信息,是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).catch(err => err);
      if (confirmResult !== 'confirm') return this.$message.info('已经取消删除');
      userService.delExpertById(this.ids).then(res => {
        if (res.status !== 200) return this.$message.error('删除专家信息失败');
        this.$message.success('删除专家信息成功');
        this.getList();
      });
    },
    // 新增
    addRow() {
      this.flag = 'add';
      this.row = {};
      this.imageUrl = '';
      this.dialogShow = true;
    },
    // 编辑
    editDialogRow(row) {
      this.flag = 'edit';
      this.row = {};
      userService.getExpertInfo(row.id).then(res => {
        this.row = res.data;
        this.imageUrl = res.data.photo;
      });
      this.dialogShow = true;
    },
    //获取分页每页显示数据条数
    handleSizeChange(newSize) {
      this.listQuery.limit = newSize;
      this.getList();
    },
    //获取当前分页页数
    handleCurrentChange(newPage) {
      this.listQuery.page = newPage;
      this.getList();
    },
    // 删除
    async deleteRow(id) {
      let ids = [];
      ids.push(id);
      const confirmResult = await this.$confirm('此操作将删除该专家信息,是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).catch(err => err);
      if (confirmResult !== 'confirm') return this.$message.info('已经取消删除');
      userService.delExpertById(ids).then(res => {
        if (res.status !== 200) return this.$message.error('删除专家信息失败');
        this.$message.success('删除专家信息成功');
        this.getList();
      });
    },
    DialogClose() {
      this.row = {};
      this.$refs.addFormRef.clearValidate();
    },
    // 处理图片
    httpRequest(data) {
      let _this = this;
      let rd = new FileReader(); // 创建文件读取对象
      let file = data.file;
      rd.readAsDataURL(file); // 文件读取装换为base64类型
      rd.onloadend = function() {
        _this.imageUrl = this.result; // this指向当前方法onloadend的作用域
      };
    },
    // 设定表格高度
    setTableHeight() {
      let h = document.documentElement.clientHeight || document.body.clientHeight;
      this.curHeight = h - 280;
    },
    beforeAvatarUpload(file) {
      this.multfileImg = file;
      const isJPG = file.type === 'image/jpeg';
      const isPng = file.type === 'image/png';
      if (!isJPG && !isPng) {
        this.$message.error('上传图片格式只能为.jpg或.png!');
      }
      // const isLt10M = file.size / 1024 / 1024 < 10;
      // if (!isLt10M) {
      //     this.$message.error('上传头像图片大小不能超过 10MB!');
      // }
      return isJPG || isPng;
    },
    // 提交 供求信息
    submitRow() {
      let formData = new FormData();
      formData.append('file', this.multfileImg);
      formData.append('expertName', this.row.expertName); //专家姓名
      formData.append('phone', this.row.phone); // 专家联系电话
      formData.append('wechat', this.row.wechat); // 专家微信号
      formData.append('expertise', this.row.expertise); // 业务专长
      formData.append('jobTitle', this.row.jobTitle); // 职称
      formData.append('serviceArea', this.row.serviceArea); // 服务地区
      formData.append('introduction', this.row.introduction); // 专家简介
      this.$refs.addFormRef.validate(async valid => {
        if (!valid) return this.$message.error('信息填写不完整或不准确，请检查再提交！');
        if (this.flag === 'add') {
          userService.addExpertInfo(formData).then(res => {
            if (res.status !== 200) return this.$message.error('失败');
            this.$message.success('新增成功');
            this.dialogShow = false;
            this.getList();
          });
        } else if (this.flag === 'edit') {
          formData.append('id', this.row.id);
          userService.updateExpert(formData).then(res => {
            if (res.status !== 200) return this.$message.error('失败');
            this.$message.success('更新成功');
            this.dialogShow = false;
            this.getList();
          });
        }
      });
    }
  }
};
</script>

<style lang="less" scoped></style>
<style lang="less">
.register-upload {
  .avatar-uploader .el-upload {
    border: 1px dashed #d9d9d9;
    border-radius: 6px;
    width: 120px;
    height: 120px;
    cursor: pointer;
    position: relative;
    overflow: hidden;
  }

  .avatar-uploader .el-upload:hover {
    border-color: #409eff;
  }

  .avatar-uploader-icon {
    font-size: 28px;
    color: #8c939d;
    width: 120px;
    height: 120px;
    line-height: 120px;
    text-align: center;
  }

  .avatar {
    width: 120px;
    height: 120px;
    display: block;
  }
}
</style>
