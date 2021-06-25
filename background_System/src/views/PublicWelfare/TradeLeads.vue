<template>
  <div>
    <!-- 面包屑导航区域 -->
    <el-breadcrumb separator-class="el-icon-arrow-right">
      <el-breadcrumb-item :to="{ path: '/home' }">首页</el-breadcrumb-item>
      <el-breadcrumb-item>公益性子系统</el-breadcrumb-item>
      <el-breadcrumb-item>供求信息管理</el-breadcrumb-item>
    </el-breadcrumb>

    <el-tabs v-model="activeName" @tab-click="handleClick">
      <!-- 供应信息管理 -->
      <el-tab-pane label="全部供求信息" name="all"></el-tab-pane>
      <!-- 供应信息管理 -->
      <el-tab-pane label="供应信息管理" name="supply"></el-tab-pane>
      <!-- 求购信息管理 -->
      <el-tab-pane label="求购信息管理" name="demand"></el-tab-pane>
    </el-tabs>
    <el-card class="article-card">
      <!-- 添加区域 -->
      <div>
        <el-button type="danger" size="small" icon="el-icon-delete" @click="handleBatchRemove()">批量删除</el-button>
        <el-button type="primary" size="small" icon="el-icon-circle-plus" @click="addRow">添加供求信息</el-button>
        <!--        <el-select-->
        <!--          v-model="listQuery.productCategory"-->
        <!--          size="small"-->
        <!--          placeholder="产品分类"-->
        <!--          clearable-->
        <!--          class="filter-item"-->
        <!--          style="width: 130px;margin-left: 2rem"-->
        <!--        >-->
        <!--          <el-option v-for="item in productOptions" :key="item" :label="item" :value="item" />-->
        <!--        </el-select>-->
        <el-input
          v-model="listQuery.title"
          size="small"
          style="width: 120px;margin-left: 20px"
          clearable
          placeholder="标题"
        ></el-input>
        <el-input
          v-model="listQuery.contactPerson"
          size="small"
          style="width: 120px;margin-left: 20px"
          clearable
          placeholder="联系人"
        ></el-input>

        <el-select
          v-model="listQuery.status"
          size="small"
          placeholder="审核状态"
          clearable
          class="filter-item"
          style="width: 130px;margin-left: 0.5rem"
        >
          <el-option v-for="item in stateTypeOptions" :key="item.key" :label="item.display_name" :value="item.key" />
        </el-select>
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
            {{ (listQuery.page - 1) * listQuery.size + scope.$index + 1 }}
          </template>
        </el-table-column>
        <el-table-column align="center" label="标题" min-width="160">
          <template slot-scope="scope">
            <el-tooltip effect="light" :content="scope.row.title" placement="top">
              <el-link type="primary" :underline="false" @click="editDialogRow(scope.row)"
                >{{ scope.row.title }}
              </el-link>
            </el-tooltip>
          </template>
        </el-table-column>
        <el-table-column align="center" label="联系人" width="160">
          <template slot-scope="scope">
            {{ scope.row.contactPerson }}
          </template>
        </el-table-column>
        <el-table-column align="center" label="联系电话" width="120">
          <template slot-scope="scope">
            <span>{{ scope.row.phone }}</span>
          </template>
        </el-table-column>
        <el-table-column align="center" label="所在地区" width="120">
          <template slot-scope="scope">
            <span>{{ scope.row.area }}</span>
          </template>
        </el-table-column>
        <el-table-column align="center" label="发布时间" width="160">
          <template slot-scope="scope">
            <span>{{ scope.row.pubTime }}</span>
          </template>
        </el-table-column>
        <el-table-column align="center" label="描述" min-width="160">
          <template slot-scope="scope">
            <el-popover placement="top-start" title="描述" width="250" trigger="hover">
              <div>{{ scope.row.description }}</div>
              <span
                slot="reference"
                v-if="scope.row.hasOwnProperty('description') && JSON.stringify(scope.row.description).length > 15"
                >{{ scope.row.description.substr(0, 15) }}...
              </span>
              <span v-else slot="reference">
                {{ scope.row.description }}
              </span>
            </el-popover>
          </template>
        </el-table-column>

        <el-table-column align="center" label="审核状态" width="120">
          <template slot-scope="scope">
            <el-button
              v-if="scope.row.status === '0'"
              type="danger"
              size="mini"
              style="width: 80px"
              @click="handleModifyStatus(scope.row, '1')"
            >
              待审核
            </el-button>
            <el-button
              v-if="scope.row.status === '1'"
              type="success"
              size="mini"
              style="width: 80px"
              @click="handleModifyStatus(scope.row, '-1')"
            >
              审核通过
            </el-button>
            <el-button
              v-if="scope.row.status === '-1'"
              type="warning"
              size="mini"
              style="width: 90px"
              @click="handleModifyStatus(scope.row, '0')"
            >
              审核未通过
            </el-button>
            <el-button
              v-if="scope.row.status !== '-1' && scope.row.status !== '0' && scope.row.status !== '1'"
              type="info"
              size="mini"
              style="width: 90px"
              @click="handleModifyStatus(scope.row, '0')"
            >
              未知状态
            </el-button>
          </template>
        </el-table-column>
        <el-table-column align="center" label="操作" width="200">
          <template slot-scope="scope">
            <el-button size="mini" type="primary" icon="el-icon-edit" @click="editDialogRow(scope.row)"
              >编辑
            </el-button>
            <el-button size="mini" type="danger" icon="el-icon-delete" @click="deleteRow(scope.row.id)"
              >删除
            </el-button>
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

    <!-- 新增供求信息对话框 -->
    <el-dialog
      v-cloak
      title="供求信息管理"
      @close="DialogClose()"
      center
      :visible.sync="dialogShow"
      top="7vh"
      width="50%"
      class="row-form"
    >
      <el-form :model="row" label-width="100px" class="demo-ruleForm" :rules="rules" ref="row">
        <el-tabs type="border-card">
          <el-tab-pane label="基本信息">
            <el-row>
              <el-col :span="12">
                <el-form-item label="标题:" prop="title">
                  <el-input v-model="row.title" clearable maxlength="20" placeholder="请输入标题"></el-input>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="供求类型" prop="pubType">
                  <el-select v-model="row.pubType" clearable style="width: 100%" placeholder="请选择供求类型">
                    <el-option
                      v-for="item in pubTypeOptions"
                      :key="item.display_name"
                      :label="item.display_name"
                      :value="item.key"
                    ></el-option>
                  </el-select>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="12">
                <el-form-item label="联系人:" prop="contactPerson">
                  <el-input v-model="row.contactPerson" clearable maxlength="20" placeholder="请输入联系人"></el-input>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="联系电话:" prop="phone">
                  <el-input v-model="row.phone" clearable maxlength="11" placeholder="请输入联系电话"></el-input>
                </el-form-item>
              </el-col>
            </el-row>

            <el-row>
              <el-col :span="12">
                <el-form-item label="所在地区:" prop="area">
                  <el-input v-model="row.area" clearable maxlength="20" placeholder="请输入所在地区"></el-input>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="审核状态:" prop="status">
                  <el-radio v-model="row.status" label="0">待审核</el-radio>
                  <el-radio v-model="row.status" label="1">审核通过</el-radio>
                  <el-radio v-model="row.status" label="-1">审核未通过</el-radio>
                </el-form-item>
              </el-col>
            </el-row>

            <el-row>
              <el-col :span="12">
                <el-form-item label="描述:" prop="description">
                  <el-input
                    v-model="row.description"
                    clearable
                    type="textarea"
                    :autosize="{ minRows: 5, maxRows: 10 }"
                    placeholder="请输入产品描述"
                  ></el-input>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="发布时间:" prop="pubTime">
                  <el-date-picker v-model="row.pubTime" type="datetime" style="width: 100%" placeholder="选择发布时间">
                  </el-date-picker>
                </el-form-item>
              </el-col>
            </el-row>

            <el-row>
              <el-col :span="24" class="register-upload">
                <el-form-item label="产品图片:" prop="pic">
                  <el-upload
                    :action="updateUrl"
                    :http-request="httpRequest"
                    :before-upload="beforeAvatarUpload"
                    :on-preview="handleProductPreview"
                    :on-remove="handleProductRemove"
                    :on-change="handleProductChange"
                    list-type="picture-card"
                    :file-list="fileProductList"
                  >
                    <i class="el-icon-plus"></i>
                  </el-upload>
                  <!-- 农产品图片预览 -->
                  <el-dialog :visible.sync="dialogProductVisible" width="70%" top="5vh" :append-to-body="true">
                    <img width="100%" :src="imageUrl" alt="" />
                  </el-dialog>

                  <div class="upload-div">
                    <div class="upload-div-list" v-for="(item, index) of imgRemoveLists" :key="index">
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
    <el-dialog :visible.sync="dialogVisible">
      <img width="100%" :src="dialogImageUrl" alt="" />
    </el-dialog>
  </div>
</template>

<script>
import userService from '../../globals/service/user';

const stateTypeOptions = [
  { key: '0', display_name: '待审核' },
  { key: '1', display_name: '审核通过' },
  { key: '-1', display_name: '审核不通过' }
];
const pubTypeOptions = [
  { key: '0', display_name: '供应信息' },
  { key: '1', display_name: '求购信息' }
];

export default {
  data() {
    return {
      // 审核状态信息
      stateTypeOptions,
      // 供求类型信息
      pubTypeOptions,
      activeName: 'all',
      // 搜索条件
      listQuery: {
        page: 1,
        size: 15,
        pubType: '', //供求类型
        title: '', //标题
        contactPerson: '' // 联系人
      },
      total: 0,
      // 数据列表
      rows: [],
      // 表单数据
      row: {
        title: '', //标题
        contactPerson: '', //联系人
        phone: '', // 联系电话
        area: '', // 所在地区
        description: '', // 描述
        pubType: '', // 发布类型（供应信息、求购信息）
        pubTime: '', // 发布时间
        status: '审核通过', // 审核状态
        imgLists: [] //图片列表
      },
      //编辑删除图片id的数组
      deleteIds: [],
      // 预览图片src列表
      srcList: [],
      dialogProductVisible: false,
      productRemoveListPhoto: [],
      //预览的图片数组
      imgRemoveLists: [],
      //上传图片文件列表
      fileProductList: [],
      // 图片文件上传
      updateUrl: '#',
      imageUrl: '',
      // 新增供求信息弹窗
      dialogShow: false,
      listLoading: false,
      // 表单高度
      curHeight: 0,
      //删除ids数组
      ids: [],
      //提交状态
      flag: 'add',
      // 验证规则
      rules: {
        title: [{ required: true, message: '请输入标题', trigger: 'blur' }],
        contactPerson: [{ required: true, message: '请输入联系人', trigger: 'blur' }],
        phone: [{ required: true, message: '请输入联系电话', trigger: 'blur' }],
        area: [{ required: true, message: '请输入服务地区', trigger: 'blur' }],
        description: [{ required: true, message: '请输入描述信息', trigger: 'blur' }],
        pubType: [{ required: true, message: '请选择供求类型', trigger: 'blur' }],
        status: [{ required: true, message: '请选择审核状态', trigger: 'blur' }]
      },
      dialogImageUrl: '',
      dialogVisible: false
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
    //删除已上传的图片
    fileDelIdPhoto(index, id) {
      this.$confirm('此操作将永久删除该图片, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      })
        .then(() => {
          this.deleteIds.push(id);
          this.imgRemoveLists.splice(index, 1);
        })
        .catch(() => {
          this.$message({
            duration: 1000,
            type: 'info',
            message: '已取消操作'
          });
        });
    },
    //上传图片列表文件
    handleProductChange(file, fileList) {
      this.fileProductList = fileList;
    },
    //删除要上传列表中的图片
    handleProductRemove(file, fileLists) {
      for (let i in this.productRemoveListPhoto) {
        if (this.productRemoveListPhoto[i].key === file.uid) {
          this.productRemoveListPhoto.splice(i, 1);
        }
      }
      this.fileProductList = fileLists;
    },
    handleProductPreview(file) {
      this.imageUrl = file.url;
      this.dialogProductVisible = true;
    },
    // 获取供求信息
    getList() {
      this.listLoading = true;
      if (this.activeName === 'supply') {
        this.listQuery.pubType = '0';
      } else if (this.activeName === 'demand') {
        this.listQuery.pubType = '1';
      } else if (this.activeName === 'all') {
        this.listQuery.pubType = '';
      }
      let params = this.listQuery;
      userService.getSupplyDemandListByPage(params).then(res => {
        if (res.status !== 200) return this.$message.error('获取失败');
        this.rows = res.data.rows;
        this.listLoading = false;
        this.total = res.data.total;
      });
    },
    //图片预览url处理
    handleImgUrl(data) {
      let imgList = [];
      data.map(item => {
        imgList.push('http://' + item);
      });
      return imgList;
    },
    // 查询
    handleFilter() {
      this.listQuery.page = 1;
      this.getList();
    },
    //点击tab栏事件
    handleClick() {
      this.getList();
    },
    //全选框事件
    checkSelect(data) {
      data.forEach(item => {
        this.ids.push(item.id);
      });
    },
    async handleBatchRemove() {
      if (this.ids.length === 0) return this.$message.warning('请先选中要删除的供求信息');
      const confirmResult = await this.$confirm('此操作将删除选中供求信息,是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).catch(err => err);
      if (confirmResult !== 'confirm') return this.$message.info('已经取消删除');
      userService.delSupplyDemandById(this.ids).then(res => {
        if (res.status !== 200) return this.$message.error('删除供求信息失败');
        this.$message.success('删除供求信息成功');
        this.getList();
      });
    },
    // 修改审核状态
    handleModifyStatus(row, item) {
      let status;
      if (item === '-1') status = '审核不通过';
      if (item === '0') status = '待审核';
      if (item === '1') status = '审核通过';
      this.$confirm(`是否将审核状态修改为 ${status} ?`, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      })
        .then(() => {
          let formData = new FormData();
          row.status = item;
          formData.append('status', row.status);
          row.pubTime = this.$moment(new Date()).format('YYYY-MM-DD HH:mm:ss');
          formData.append('pubTime', row.pubTime);
          formData.append('id', row.id);
          userService.updateSupplyDemandStatus(formData).then(res => {
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
    // 新增
    addRow() {
      this.flag = 'add';
      if (this.activeName === 'supply') {
        this.row.pubType = '0'; // 供应信息
      } else if (this.activeName === 'demand') {
        this.row.pubType = '1'; // 求购信息
      } else {
        this.row.pubType = '0'; // 供应信息
      }
      this.row.pubTime = this.$moment(new Date()).format('YYYY-MM-DD HH:mm:ss');
      (this.row.status = '1'), (this.dialogShow = true);
    },
    // 设定表格高度
    setTableHeight() {
      let h = document.documentElement.clientHeight || document.body.clientHeight;
      this.curHeight = h - 280;
    },
    // 编辑
    editDialogRow(row) {
      this.flag = 'edit';
      this.row = row;
      this.imgRemoveLists = row.imgLists;
      row.imgLists.map(item => {
        this.srcList.push('http://' + item.path);
      });
      this.dialogShow = true;
    },
    //获取分页每页显示数据条数
    handleSizeChange(newSize) {
      this.listQuery.size = newSize;
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
      const confirmResult = await this.$confirm('此操作将删除该供求信息,是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).catch(err => err);
      if (confirmResult !== 'confirm') return this.$message.info('已经取消删除');
      userService.delSupplyDemandById(ids).then(res => {
        if (res.status !== 200) return this.$message.error('删除供求信息失败');
        this.$message.success('删除供求信息成功');
        this.getList();
      });
    },
    DialogClose() {
      this.row = {};
      this.fileProductList = [];
      this.imgRemoveLists = [];
      this.deleteIds = [];
      this.$refs.row.resetFields();
      this.$refs.row.clearValidate();
      this.getList();
    },
    // 处理图片
    httpRequest(data) {
      let rd = new FileReader(); // 创建文件读取对象
      let file = data.file;
      rd.readAsDataURL(file); // 文件读取装换为base64类型
      rd.onloadend = function() {
        this.imageUrl = this.result; // this指向当前方法onloadend的作用域
      };
    },
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
    handleExceed() {
      this.$message.warning(`只能上传 ${this.limit} 张图片。`);
    },
    // 提交 供求信息
    submitRow() {
      this.$refs.row.validate(async valid => {
        if (!valid) return this.$message.error('信息填写不完整或不准确，请检查再提交！');
        let formData = new FormData();
        formData.append('files', this.multfileImg);
        formData.append('title', this.row.title);
        formData.append('contactPerson', this.row.contactPerson);
        formData.append('phone', this.row.phone);
        formData.append('status', this.row.status);
        this.row.pubTime = this.$moment(new Date()).format('YYYY-MM-DD HH:mm:ss');
        formData.append('pubTime', this.row.pubTime);
        formData.append('area', this.row.area);
        formData.append('pubType', this.row.pubType);
        formData.append('description', this.row.description);
        this.fileProductList.forEach(item => {
          formData.append('files', item.raw);
        });
        if (this.flag === 'add') {
          userService.addSupplyDemand(formData).then(res => {
            if (res.status !== 200) return this.$message.error('失败');
            this.$message.success('新增成功');
            this.dialogShow = false;
            this.getList();
          });
        } else if (this.flag === 'edit') {
          this.deleteIds.map(item => {
            formData.append('delIds', item);
          });
          formData.append('id', this.row.id);
          userService.updateSupplyDemand(formData).then(res => {
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
    width: 220px;
    height: 220px;
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
    width: 220px;
    height: 220px;
    line-height: 220px;
    text-align: center;
  }

  .avatar {
    width: 220px;
    height: 220px;
    display: block;
  }
}

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
