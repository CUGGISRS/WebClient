<template>
  <div>
    <!-- 面包屑导航区域 -->
    <el-breadcrumb separator-class="el-icon-arrow-right">
      <el-breadcrumb-item :to="{ path: '/home' }">首页</el-breadcrumb-item>
      <el-breadcrumb-item>电子商务子系统</el-breadcrumb-item>
      <el-breadcrumb-item>优质农产品管理</el-breadcrumb-item>
    </el-breadcrumb>
    <!-- 卡片视图区域 -->
    <el-card>
      <!-- 添加区域 -->
      <div>
        <el-button type="danger" size="small" icon="el-icon-delete" @click="handleBatchRemove()">批量删除</el-button>
        <el-button type="primary" size="small" icon="el-icon-circle-plus" @click="addRow">添加</el-button>
        <el-input
          v-model="listQuery.productName"
          size="small"
          style="width: 120px;margin-left: 10px"
          clearable
          placeholder="产品名称"
        ></el-input>
        <el-select
          v-model="listQuery.productCategory"
          size="small"
          placeholder="产品分类"
          clearable
          class="filter-item"
          style="width: 130px;margin-left: 10px"
        >
          <el-option v-for="item in productOptions" :key="item" :label="item" :value="item" />
        </el-select>
        <el-select
          v-model="listQuery.status"
          size="small"
          placeholder="审核状态"
          clearable
          class="filter-item"
          style="width: 130px;margin-left: 10px"
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
            {{ (listQuery.page - 1) * listQuery.limit + scope.$index + 1 }}
          </template>
        </el-table-column>
        <el-table-column align="center" label="产品名称" width="120">
          <template slot-scope="scope">
            <el-link type="primary" :underline="false" @click="editRow(scope.row)">{{ scope.row.productName }}</el-link>
          </template>
        </el-table-column>
        <el-table-column align="center" label="产品分类" width="160">
          <template slot-scope="scope">
            {{ scope.row.productCategory }}
          </template>
        </el-table-column>
        <el-table-column align="center" label="价格" width="160">
          <template slot-scope="scope">
            {{ scope.row.price }}
          </template>
        </el-table-column>
        <el-table-column align="center" label="发布日期" width="150">
          <template slot-scope="scope">
            <span>{{ scope.row.pubTime }}</span>
          </template>
        </el-table-column>
        <el-table-column align="center" label="更新日期" width="150">
          <template slot-scope="scope">
            <span>{{ scope.row.uptTime }}</span>
          </template>
        </el-table-column>
        <el-table-column align="center" label="描述" min-width="160">
          <template slot-scope="scope">
            <el-popover placement="top-start" title="描述" width="250" trigger="hover">
              <div>{{ scope.row.description }}</div>
              <span
                slot="reference"
                v-if="scope.row.hasOwnProperty('description') && JSON.stringify(scope.row.description).length > 20"
                >{{ scope.row.description.substr(0, 20) }}...
              </span>
              <span v-else slot="reference">
                {{ scope.row.description }}
              </span>
            </el-popover>
          </template>
        </el-table-column>
        <el-table-column align="center" label="状态" width="120">
          <template slot-scope="scope">
            <el-button
              v-if="scope.row.status !== '审核通过'"
              type="danger"
              size="mini"
              style="width: 80px"
              @click="handleModifyStatus(scope.row, '审核通过')"
            >
              待审核
            </el-button>

            <el-button
              v-else
              type="success"
              size="mini"
              style="width: 80px"
              @click="handleModifyStatus(scope.row, '待审核')"
            >
              审核通过
            </el-button>
          </template>
        </el-table-column>
        <el-table-column align="center" label="操作" width="200">
          <template slot-scope="scope">
            <el-button type="primary" size="mini" icon="el-icon-edit" @click="editRow(scope.row)">编辑 </el-button>
            <el-button type="danger" size="mini" icon="el-icon-delete" @click="deleteRow(scope.row.id)"
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
        :page-sizes="[15, 20, 30, 40]"
        :page-size="listQuery.limit"
        layout="total, sizes, prev, pager, next, jumper"
        :total="total"
      >
      </el-pagination>
    </el-card>
    <!-- 新增编辑对话框 -->
    <el-dialog
      v-cloak
      title="优质农产品信息"
      @close="closeDialog"
      center
      :visible.sync="dialogShow"
      top="7vh"
      width="70%"
      class="row-form"
    >
      <el-form :model="row" label-width="100px" class="demo-ruleForm" :rules="rules" ref="row">
        <el-tabs type="border-card">
          <el-tab-pane label="基本信息">
            <el-row>
              <el-col :span="12">
                <el-form-item label="产品分类:" prop="productCategory">
                  <el-select
                    v-model="row.productCategory"
                    clearable
                    style="width: 100%"
                    @change="getDic"
                    placeholder="请选择产品分类"
                  >
                    <el-option v-for="item in productOptions" :key="item" :label="item" :value="item"></el-option>
                  </el-select>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="产品名称:" prop="productName">
                  <el-select
                    v-model="row.productName"
                    filterable
                    allow-create
                    style="width: 100%"
                    placeholder="请输入产品名称关键词"
                  >
                    <el-option v-for="item in options" :key="item.value" :label="item.label" :value="item.value">
                    </el-option>
                  </el-select>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="12">
                <el-form-item label="价格:" prop="price">
                  <el-input
                    v-model="row.price"
                    clearable
                    onkeyup="this.value=this.value.replace(/[^\d\.]/ig,'')"
                    placeholder="请输入价格"
                  ></el-input>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="联系方式:" prop="purchaseWay">
                  <el-input
                    v-model="row.purchaseWay"
                    clearable
                    maxlength="50"
                    placeholder="格式：张三 13588886666"
                  ></el-input>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="12">
                <el-form-item label="产品产地:" prop="productOrigin">
                  <el-input v-model="row.productOrigin" clearable placeholder="请输入产品产地"></el-input>
                </el-form-item>
              </el-col>
              <el-col :span="6">
                <el-form-item label="热度:" prop="popularity">
                  <el-input
                    v-model="row.popularity"
                    clearable
                    onkeyup="this.value=this.value.replace(/[^\d]/g,'')"
                    maxlength="20"
                    placeholder="请输入产品热度"
                  ></el-input>
                </el-form-item>
              </el-col>
              <el-col :span="6">
                <el-form-item label="状态:" prop="status">
                  <el-radio v-model="row.status" label="审核通过">审核通过</el-radio>
                  <el-radio v-model="row.status" label="待审核">待审核</el-radio>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="22">
                <el-form-item label="描述:">
                  <el-input
                    v-model="row.description"
                    type="textarea"
                    :autosize="{ minRows: 5, maxRows: 10 }"
                    placeholder="请输入产品描述"
                  ></el-input>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="24" class="register-upload">
                <el-form-item label="产品图片:">
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
  </div>
</template>

<script>
import userService from '../../globals/service/user';

const stateTypeOptions = [
  { key: '待审核', display_name: '待审核' },
  { key: '审核通过', display_name: '审核通过' }
];

export default {
  data() {
    return {
      // 审核状态
      stateTypeOptions,
      //提交状态
      flag: 'add',
      productList: [],
      options: [],
      // 搜索条件
      listQuery: {
        page: 1,
        limit: 15,
        productName: '', // 产品名称
        productCategory: '', // 产品分类
        status: '' //审核状态
      },
      // 总数
      total: 0,
      // 表单高度
      curHeight: 0,
      //删除ids数组
      ids: [],
      // 对话框
      dialogShow: false,
      listLoading: false,
      // 表单
      rows: [],
      row: {
        productName: '', // 产品名称
        productCategory: '', // 产品分类
        price: '', // 价格
        description: '', //描述
        productOrigin: '', // 产品产地
        purchaseWay: '', // 购买方式
        uptTime: '', //更新时间
        pubTime: '', // 发布时间
        status: '审核通过', // 审核状态
        popularity: 0, // 热度
        urlList: []
      },
      // 图片文件上传
      updateUrl: '#',
      imageUrl: '',
      // 上传文件
      multfileImg: null,
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
      // 多选框
      productOptions: ['粮油', '蔬菜', '水果', '牛羊猪肉', '家禽蛋类', '水产品', '其他'],
      // 验证规则
      rules: {
        productCategory: [{ required: true, message: '请选择农产品分类', trigger: 'blur' }],
        productName: [{ required: true, message: '请输入农产品名称', trigger: 'blur' }],
        price: [{ required: true, message: '请选择产品价格', trigger: 'blur' }],
        productOrigin: [{ required: false, message: '请输入产品产地', trigger: 'blur' }],
        purchaseWay: [{ required: true, message: '请输入购买方式', trigger: 'blur' }],
        popularity: [{ required: false, message: '请输入产品热度', trigger: 'blur' }],
        status: [{ required: true, message: '请选择审核状态', trigger: 'blur' }]
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
    // 获取全部的列表数据
    async getAllList() {
      this.listLoading = true;
      let params = this.listQuery;
      userService.getProductListByPage(params).then(res => {
        if (res.status !== 200) return this.$message.error('获取失败');
        this.rows = res.data.rows;
        this.listLoading = false;
        this.total = res.data.total;
      });
    },
    // 查询
    handleFilter() {
      this.listQuery.page = 1;
      this.getAllList();
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
          userService.ModifyProductStatus(row).then(res => {
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
    //全选框事件
    checkSelect(data) {
      data.forEach(item => {
        this.ids.push(item.id);
      });
    },
    async handleBatchRemove() {
      if (this.ids.length === 0) return this.$message.warning('请先选中要删除的优质农产品');
      const confirmResult = await this.$confirm('此操作将删除选中优质农产品,是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).catch(err => err);
      if (confirmResult !== 'confirm') return this.$message.info('已经取消删除');
      userService.delHighQualityProductById(this.ids).then(res => {
        if (res.status !== 200) return this.$message.error('删除优质农产品信息失败');
        this.$message.success('删除优质农产品信息成功');
        this.getAllList();
      });
    },

    // 当前页
    handleCurrentChange(val) {
      this.listQuery.page = val;
      this.getAllList();
    },
    // 每页多少条
    handleSizeChange(val) {
      this.listQuery.limit = val;
      this.getAllList();
    },
    // 设定表格高度
    setTableHeight() {
      let h = document.documentElement.clientHeight || document.body.clientHeight;
      this.curHeight = h - 280;
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
      //
      return isJPG || isPng;
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
    getDic() {
      let params = {
        type: this.row.productCategory
      };
      userService.getProductNameDicByType(params).then(res => {
        if (res.status !== 200) return this.$message.error('获取失败');
        this.productList = res.data.rows;
        this.options = this.productList.map(item => {
          return { value: `${item.text}`, label: `${item.text}` };
        });
        this.row.productName = this.options[0].value;
      });
    },
    // 新增
    addRow() {
      this.flag = 'add';
      this.row.popularity = 0;
      this.row.productCategory = this.productOptions[0];
      this.getDic();
      this.dialogShow = true;
    },
    // 编辑
    editRow(row) {
      this.flag = 'edit';
      this.row = row;
      this.imgRemoveLists = row.imgLists;
      row.imgLists.map(item => {
        this.srcList.push('http://' + item.path);
      });
      this.dialogShow = true;
    },
    // 删除
    async deleteRow(id) {
      let ids = [];
      ids.push(id);
      const confirmResult = await this.$confirm('此操作将删除该优质农产品信息,是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).catch(err => err);
      if (confirmResult !== 'confirm') return this.$message.info('已经取消删除');
      userService.delHighQualityProductById(ids).then(res => {
        if (res.status !== 200) return this.$message.error('删除优质农产品信息失败');
        this.$message.success('删除优质农产品信息成功');
        this.getAllList();
      });
    },
    // 关闭对话框
    closeDialog() {
      this.row = {};
      this.fileProductList = [];
      this.imgRemoveLists = [];
      this.deleteIds = [];
      this.$refs.row.clearValidate();
      this.getAllList();
    },
    // 提交数据
    submitRow() {
      this.$refs.row.validate(async valid => {
        if (!valid) return this.$message.error('信息填写不完整或不准确，请检查再提交！');
        let formData = new FormData();
        formData.append('productName', this.row.productName);
        formData.append('productCategory', this.row.productCategory);
        formData.append('price', this.row.price);
        formData.append('description', this.row.description);
        formData.append('productOrigin', this.row.productOrigin);
        formData.append('purchaseWay', this.row.purchaseWay);
        formData.append('status', this.row.status);
        formData.append('popularity', this.row.popularity);
        this.fileProductList.forEach(item => {
          formData.append('files', item.raw);
        });
        if (this.flag === 'add') {
          userService.addHighQualityProduct(formData).then(res => {
            if (res.status !== 200) return this.$message.error('失败');
            this.$message.success('新增成功');
            this.dialogShow = false;
            this.getAllList();
          });
        } else if (this.flag === 'edit') {
          this.row.pubTime = this.$moment(this.row.pubTime).format('YYYY-MM-DD HH:mm:ss');
          formData.append('pubTime', this.row.pubTime);
          this.deleteIds.map(item => {
            formData.append('delIds', item);
          });
          formData.append('id', this.row.id);
          userService.updateHighQualityProduct(formData).then(res => {
            if (res.status !== 200) return this.$message.error('失败');
            this.$message.success('更新成功');
            this.dialogShow = false;
            this.getAllList();
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
</style>
