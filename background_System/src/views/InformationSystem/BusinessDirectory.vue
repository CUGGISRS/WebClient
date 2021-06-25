<template>
  <div>
    <!-- 面包屑导航区域 -->
    <el-breadcrumb separator-class="el-icon-arrow-right">
      <el-breadcrumb-item :to="{ path: '/home' }">首页</el-breadcrumb-item>
      <el-breadcrumb-item>农产品追溯信息系统</el-breadcrumb-item>
      <el-breadcrumb-item>企业名录管理</el-breadcrumb-item>
    </el-breadcrumb>

    <!-- 卡片视图区域 -->
    <el-card>
      <!-- 添加区域 -->
      <div>
        <el-button type="danger" size="small" icon="el-icon-delete" @click="handleBatchRemove()">批量删除</el-button>
        <el-button type="primary" size="small" icon="el-icon-circle-plus" @click="addCompanyInfo">添加</el-button>
        <el-input
          v-model="companyName"
          placeholder="公司名称"
          clearable
          size="small"
          style="width: 200px;margin-left: 2rem"
        ></el-input>
        <el-button size="small" type="primary" style=";margin-left: 0.5rem" icon="el-icon-search" @click="handleFilter">
          查询
        </el-button>
      </div>

      <!-- 表格显示区域 -->
      <el-table
        :data="rows"
        :height="curHeight"
        v-loading="listLoading"
        border
        stripe
        fit
        highlight-current-row
        :row-style="{ height: '5px' }"
        :cell-style="{ padding: '5px 0' }"
        @selection-change="checkSelect"
      >
        <el-table-column type="selection" width="40" label="全选"></el-table-column>
        <el-table-column align="center" label="序号" width="60">
          <template slot-scope="scope">
            {{ (listQuery.page - 1) * listQuery.size + scope.$index + 1 }}
          </template>
        </el-table-column>
        <el-table-column align="center" label="企业名称" width="120">
          <template slot-scope="scope">
            <el-link type="primary" :underline="false" @click="editCompanyInfo(scope.row)">{{
              scope.row.companyName
            }}</el-link>
          </template>
        </el-table-column>
        <el-table-column align="center" label="企业类型" width="160">
          <template slot-scope="scope">
            {{ scope.row.companyType }}
          </template>
        </el-table-column>
        <el-table-column align="center" label="注册资金" width="120">
          <template slot-scope="scope">
            {{ scope.row.registMoney }}
          </template>
        </el-table-column>
        <el-table-column align="center" label="营业执照代码" width="180">
          <template slot-scope="scope">
            {{ scope.row.businessCode }}
          </template>
        </el-table-column>
        <el-table-column align="center" label="行业" width="150">
          <template slot-scope="scope">
            {{ scope.row.industry }}
          </template>
        </el-table-column>
        <el-table-column align="center" label="介绍" min-width="160">
          <template slot-scope="scope">
            <el-popover placement="top-start" title="企业介绍" width="250" trigger="hover">
              <div>{{ scope.row.introduction }}</div>
              <span
                slot="reference"
                v-if="scope.row.hasOwnProperty('introduction') && JSON.stringify(scope.row.introduction).length > 20"
                >{{ scope.row.introduction.substr(0, 50) }}...
              </span>
              <span v-else slot="reference">
                {{ scope.row.introduction }}
              </span>
            </el-popover>
          </template>
        </el-table-column>
        <el-table-column align="center" label="企业所在地" width="160">
          <template slot-scope="scope">
            {{ scope.row.place }}
          </template>
        </el-table-column>
        <el-table-column align="center" label="操作" width="200">
          <template slot-scope="scope">
            <el-button type="primary" size="mini" icon="el-icon-edit" @click="editCompanyInfo(scope.row)"
              >编辑
            </el-button>
            <el-button type="danger" size="mini" icon="el-icon-delete" @click="deleteCompanyInfo(scope.row.id)"
              >删除
            </el-button>
          </template>
        </el-table-column>
      </el-table>
      <!-- 分页 -->
      <el-pagination
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
        :current-page="listQuery.page"
        :page-sizes="[5, 10, 15, 20]"
        :page-size="listQuery.size"
        layout="total, sizes, prev, pager, next, jumper"
        :total="total"
      >
      </el-pagination>
    </el-card>

    <!-- 新增编辑对话框 -->
    <el-dialog
      v-cloak
      title="企业名录信息"
      @close="closeDialog"
      :visible.sync="dialogShow"
      top="7vh"
      width="70%"
      class="row-form"
    >
      <el-form :model="row" label-width="100px" :rules="rules" ref="row" label-position="left">
        <el-tabs type="border-card">
          <el-tab-pane label="企业信息">
            <el-row>
              <el-col :span="12">
                <el-form-item label="企业名称：" prop="companyName">
                  <el-input v-model="row.companyName" style="width: 300px" placeholder="请输入企业名称"></el-input>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="企业类型 ：" prop="companyType">
                  <el-input v-model="row.companyType" style="width: 300px" placeholder="请输入企业类型"></el-input>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="12">
                <el-form-item label="企业法人：" prop="chargeMan">
                  <el-input style="width: 300px" placeholder="请输入企业法人名称" v-model="row.chargeMan" clearable>
                  </el-input>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="联系方式：" prop="phone">
                  <el-input style="width: 300px" placeholder="请输入企业联系方式" v-model="row.phone" clearable>
                  </el-input>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="12">
                <el-form-item label="注册资金：" prop="registMoney">
                  <el-input style="width: 300px" placeholder="请输入注册资金" v-model="row.registMoney" clearable>
                  </el-input>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="营业执照代码：" prop="businessCode" label-width="120">
                  <el-input style="width: 300px" placeholder="请输入营业执照代码" v-model="row.businessCode" clearable>
                  </el-input>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="12">
                <el-form-item label="所属行业：" prop="industry">
                  <el-input style="width: 300px" placeholder="请输入行业信息" v-model="row.industry" clearable>
                  </el-input>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="企业所在地：" prop="place" label-width="110">
                  <el-input style="width: 300px" placeholder="请输入企业所在地" v-model="row.place" clearable>
                  </el-input>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="12">
                <el-form-item label="企业简介：" prop="introduction">
                  <el-input
                    style="width: 300px"
                    type="textarea"
                    :rows="10"
                    placeholder="请输入企业简介"
                    v-model="row.introduction"
                  >
                  </el-input>
                </el-form-item>
              </el-col>
              <el-col :span="12" class="register-upload">
                <el-form-item label="企业图片:">
                  <el-upload
                    :action="updateUrl"
                    :http-request="httpRequest"
                    :before-upload="beforeAvatarUpload"
                    :on-preview="handleCompanyPreview"
                    :on-remove="handleCompanyRemove"
                    :on-change="handleCompanyChange"
                    list-type="picture-card"
                    :file-list="fileCompanyList"
                  >
                    <i class="el-icon-plus"></i>
                  </el-upload>
                  <!-- 企业图片预览 -->
                  <el-dialog :visible.sync="dialogCompanyVisible" width="70%" top="5vh" :append-to-body="true">
                    <img width="100%" :src="imageUrl" alt="" />
                  </el-dialog>

                  <div class="upload-div">
                    <div class="upload-div-list" v-for="(item, index) of imgPreviewLists" :key="index">
                      <div class="upload-div-top">
                        <span class="upload-div-del" @click="fileDelIdPhoto(index, item.id)">X</span>
                      </div>
                      <el-image
                        class="el-image-preview"
                        :src="'http://' + item.path"
                        :preview-src-list="srcList"
                      ></el-image>
                    </div>
                  </div>
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
import userService from '@/globals/service/user.js';
import { Debounce } from '@/utils/tool.js';

export default {
  data() {
    return {
      //公司名称
      companyName: '',
      //查询企业选择框db
      companyNameOptions: '',
      // 表单高度
      curHeight: 0,
      //删除ids数组
      ids: [],
      //列表加载圈圈
      listLoading: false,
      //列表渲染db
      rows: [],
      //新增编辑弹出框对象
      row: {
        id: '',
        companyName: '',
        companyType: '',
        chargeMan: '',
        phone: '',
        registMoney: '',
        businessCode: '',
        industry: '',
        place: '',
        introduction: ''
      },
      // 图片文件上传地址
      updateUrl: '#',
      //预览的图片
      imageUrl: '',
      // 预览图片src列表
      srcList: [],
      //上传图片文件列表
      fileCompanyList: [],
      //预览的图片数组
      imgPreviewLists: [],
      //编辑删除图片id的数组
      deleteIds: [],
      //分页对象
      listQuery: {
        page: 1,
        size: 10
      },
      //总条数
      total: 0,
      //显示新增编辑对话框
      dialogShow: false,
      //显示图片预览弹出框
      dialogCompanyVisible: false,
      //删除的图片数组
      companyRemoveListPhoto: [],
      //新增和编辑对话框切换
      flag: 'add',
      //对话框验证规则
      rules: {
        companyName: [{ required: true, message: '请输入企业名称', trigger: 'blur' }],
        companyType: [{ required: true, message: '请选择企业类型', trigger: 'blur' }],
        chargeMan: [{ required: true, message: '请输入企业法人名称', trigger: 'blur' }],
        phone: [{ required: true, message: '请输入企业联系方式', trigger: 'blur' }],
        registMoney: [{ required: true, message: '请输入注册资金', trigger: 'blur' }],
        businessCode: [{ required: true, message: '请输入营业执照代码', trigger: 'blur' }],
        industry: [{ required: true, message: '请输入行业信息', trigger: 'blur' }],
        place: [{ required: true, message: '请输入企业所在地', trigger: 'blur' }],
        introduction: [{ required: true, message: '请输入企业简介', trigger: 'blur' }]
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
    //获取所有列表数据
    getAllList() {
      this.listLoading = true;
      if (this.companyName !== '') {
        this.listQuery.companyName = this.companyName;
      } else {
        this.listQuery.companyName = '';
      }
      let params = this.listQuery;
      userService.getCompanylist(params).then(res => {
        if (res.status !== 200) return this.$message.error('获取失败');
        this.rows = res.data.rows;
        this.listLoading = false;
        this.total = res.data.total;
      });
    },
    //添加按钮事件
    addCompanyInfo() {
      this.flag = 'add';
      this.dialogShow = true;
    },
    //查询公司按钮事件
    handleFilter() {
      this.listQuery.page = 1;
      this.getAllList();
    },
    //修改企业信息按钮
    editCompanyInfo(row) {
      this.flag = 'edit';
      this.row = row;
      this.imgPreviewLists = row.imgLists;
      row.imgLists.map(item => {
        this.srcList.push('http://' + item.path);
      });
      this.dialogShow = true;
    },
    //全选框事件
    checkSelect(data) {
      data.forEach(item => {
        this.ids.push(item.id);
      });
    },
    async handleBatchRemove() {
      if (this.ids.length === 0) return this.$message.warning('请先选中要删除的企业信息');
      const confirmResult = await this.$confirm('此操作将删除选中企业信息,是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).catch(err => err);
      if (confirmResult !== 'confirm') return this.$message.info('已经取消删除');
      userService.businessDirectoryDel(this.ids).then(res => {
        if (res.status !== 200) return this.$message.error('删除企业信息失败');
        this.$message.success('删除企业信息成功');
        this.getAllList();
      });
    },
    //删除企业信息
    async deleteCompanyInfo(id) {
      let ids = [];
      ids.push(id);
      const confirmResult = await this.$confirm('此操作将删除该企业信息,是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).catch(err => err);
      if (confirmResult !== 'confirm') return this.$message.info('已经取消删除');
      userService.businessDirectoryDel(ids).then(res => {
        if (res.status !== 200) return this.$message.error('删除企业信息失败');
        this.$message.success('删除企业信息成功');
        this.getAllList();
      });
    },
    //关闭新增编辑对话框
    closeDialog() {
      this.row = {};
      this.fileCompanyList = [];
      this.imgPreviewLists = [];
      this.deleteIds = [];
      this.$refs.row.clearValidate();
      this.getAllList();
    },
    //对话框提交按钮事件
    submitRow: Debounce(function() {
      if (this.flag === 'add') {
        this.$refs.row.validate(valid => {
          if (!valid) return this.$message.error('信息填写不完整或不准确，请检查再提交！');
          let formData = new FormData();
          formData.append('companyName', this.row.companyName);
          formData.append('companyType', this.row.companyType);
          formData.append('chargeMan', this.row.chargeMan);
          formData.append('phone', this.row.phone);
          formData.append('registMoney', this.row.registMoney);
          formData.append('businessCode', this.row.businessCode);
          formData.append('industry', this.row.industry);
          formData.append('place', this.row.place);
          formData.append('introduction', this.row.introduction);
          this.fileCompanyList.forEach(item => {
            formData.append('files', item.raw);
          });
          userService
            .businessDirectoryAdd(formData)
            .then(res => {
              if (res.status !== 200) return this.$message.error('失败');
              this.$message.success('新增成功');
              // 隐藏对话框
              this.dialogShow = false;
              // 重新获取文章
              this.getAllList();
            })
            .catch(() => {
              this.$message({ type: 'error', message: '异常' });
            });
        });
      } else if (this.flag === 'edit') {
        this.$refs.row.validate(valid => {
          if (!valid) return this.$message.error('信息填写不完整或不准确，请检查再提交！');
          let formData = new FormData();
          formData.append('id', this.row.id);
          formData.append('companyName', this.row.companyName);
          formData.append('companyType', this.row.companyType);
          formData.append('chargeMan', this.row.chargeMan);
          formData.append('phone', this.row.phone);
          formData.append('registMoney', this.row.registMoney);
          formData.append('businessCode', this.row.businessCode);
          formData.append('industry', this.row.industry);
          formData.append('place', this.row.place);
          formData.append('introduction', this.row.introduction);
          this.fileCompanyList.forEach(item => {
            formData.append('files', item.raw);
          });
          this.deleteIds.map(item => {
            formData.append('deleteIds', item);
          });
          userService
            .businessDirectoryEdit(formData)
            .then(res => {
              if (res.status !== 200) return this.$message.error('失败');
              this.$message.success('更新成功');
              // 隐藏对话框
              this.dialogShow = false;
              // 重新获取列表
              this.getAllList();
            })
            .catch(() => {
              this.$message({ type: 'error', message: '异常' });
            });
        });
      }
    }, 3000),
    // // 处理图片
    httpRequest(data) {
      let rd = new FileReader(); // 创建文件读取对象
      let file = data.file;
      rd.readAsDataURL(file); // 文件读取装换为base64类型
      rd.onloadend = function() {
        this.imageUrl = this.result; // this指向当前方法onloadend的作用域
      };
    },
    //上传之前的钩子函数
    beforeAvatarUpload(file) {
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
    //企业图片预览对话框
    handleCompanyPreview(file) {
      this.imageUrl = file.url;
      this.dialogCompanyVisible = true;
    },
    //上传图片列表文件
    handleCompanyChange(file, fileList) {
      this.fileCompanyList = fileList;
    },
    //删除要上传列表中的图片
    handleCompanyRemove(file, fileLists) {
      this.companyRemoveListPhoto.forEach((item, index) => {
        if (item.key == file.uid) {
          this.companyRemoveListPhoto.splice(index, 1);
        }
      });
      this.fileCompanyList = fileLists;
    },
    //删除已上传的图片
    fileDelIdPhoto(index, id) {
      this.$confirm('此操作将永久删除该图片, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      })
        .then(() => {
          this.deleteIds.push(id);
          this.imgPreviewLists.splice(index, 1);
        })
        .catch(() => {
          this.$message({
            duration: 1000,
            type: 'info',
            message: '已取消操作'
          });
        });
    },
    // 当前页
    handleCurrentChange(page) {
      this.listQuery.page = page;
      this.getAllList();
    },
    // 每页多少条
    handleSizeChange(size) {
      this.listQuery.size = size;
      this.getAllList();
    },
    // 设定表格高度
    setTableHeight() {
      let h = document.documentElement.clientHeight || document.body.clientHeight;
      this.curHeight = h - 280;
    }
  }
};
</script>

<style lang="less" scoped>
.upload-div {
  width: 350px;
  display: flex;
  justify-content: start;
  align-items: center;
  flex-wrap: wrap;
}

.upload-div-del {
  cursor: pointer;
}

.upload-div-del:hover {
  color: #64b7ff;
}

.link-type,
.link-type:focus {
  color: #337ab7;
  cursor: pointer;

  & :hover {
    color: rgb(32, 160, 255);
  }
}

.products-img-container {
  height: 100px;
  padding: 10px;
  overflow: auto;
}

.el-image-preview {
  width: 100px;
  height: 100px;
  margin-right: 10px;
}
</style>
