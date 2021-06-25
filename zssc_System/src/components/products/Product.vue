<template>
  <div>
    <!-- 面包屑导航区域 -->
    <el-breadcrumb separator-class="el-icon-arrow-right">
      <el-breadcrumb-item>首页</el-breadcrumb-item>
      <el-breadcrumb-item>产品管理</el-breadcrumb-item>
    </el-breadcrumb>
    <!-- 卡片视图区域 -->
    <el-card>
      <!-- 查询区域 -->
      <div class="query-condition">
        <div class="query-item">
          <span>产品名称:</span>
          <el-input v-model="params.name" clearable style="width: 200px;" placeholder="请输入产品名称"></el-input>
        </div>
        <div class="query-item">
          <span>开始时间:</span>
          <el-date-picker
            value-format="yyyy-MM-dd"
            v-model="params.startDateS"
            type="date"
            clearable
            style="width: 160px;"
            placeholder="请选择开始时间"
          >
          </el-date-picker>
        </div>
        <div class="query-item">
          <span>结束时间:</span>
          <el-date-picker
            value-format="yyyy-MM-dd"
            v-model="params.endDateE"
            type="date"
            clearable
            style="width: 160px;"
            placeholder="请选择结束时间"
          >
          </el-date-picker>
        </div>
        <el-button size="small" type="primary" icon="el-icon-search" @click="searchRow">查询</el-button>
      </div>
      <!-- 按钮操作区域 -->
      <div>
        <el-button size="small" type="success" icon="el-icon-plus" @click="addRow">新增</el-button>
        <el-button size="small" type="danger" icon="el-icon-delete" @click="deleteList">删除</el-button>
        <el-button size="small" type="warning" icon="el-icon-setting" @click="showProductSetting">商品配置</el-button>
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
        <el-table-column align="center" label="产品名称" width="240">
          <template slot-scope="scope">
            {{ scope.row.name }}
          </template>
        </el-table-column>
        <el-table-column align="center" label="养殖基地名称" min-width="120">
          <template slot-scope="scope">
            {{ getBaseName(scope.row.baseId) }}
          </template>
        </el-table-column>
        <el-table-column align="center" label="开始时间" width="120">
          <template slot-scope="scope">
            {{ filterTime(scope.row.startDate) }}
          </template>
        </el-table-column>
        <el-table-column align="center" label="结束时间" width="120">
          <template slot-scope="scope">
            {{ filterTime(scope.row.endDate) }}
          </template>
        </el-table-column>
        <el-table-column align="center" label="商品详情" width="240">
          <template slot-scope="scope">
            <el-tag effect="dark" @click="showDetail(scope.row.productDetailId)" style="cursor: pointer;">
              {{ getProductName(scope.row.productDetailId) }}
            </el-tag>
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
      title="产品信息"
      @close="closeDialog"
      center
      :visible.sync="dialogShow"
      width="1000px"
      class="row-form"
    >
      <el-form :model="row" label-width="110px" class="demo-ruleForm" :rules="rules" ref="row">
        <el-row>
          <el-col :span="12">
            <el-form-item label="产品名称:" prop="name">
              <el-input v-model="row.name" clearable maxlength="50" placeholder="请输入产品名称"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="基地名称:" prop="baseId">
              <el-select v-model="row.baseId" clearable placeholder="请选择养殖基地">
                <el-option v-for="item in plantingBaseOptions" :key="item.id" :label="item.name" :value="item.id">
                </el-option>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row>
          <el-col :span="12">
            <el-form-item label="开始时间:" prop="startDate">
              <el-date-picker
                value-format="yyyy-MM-dd"
                v-model="row.startDate"
                type="date"
                clearable
                placeholder="请选择开始时间"
              >
              </el-date-picker>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="结束时间:" prop="endDate">
              <el-date-picker
                value-format="yyyy-MM-dd"
                v-model="row.endDate"
                type="date"
                clearable
                placeholder="请选择结束时间"
              >
              </el-date-picker>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row>
          <el-col :span="12">
            <el-form-item label="商品选择:" prop="productDetailId">
              <el-select v-model="row.productDetailId" clearable placeholder="请选择商品">
                <el-option v-for="item in productRows" :key="item.id" :label="item.name" :value="item.id"> </el-option>
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
    <el-dialog
      v-cloak
      title="商品配置"
      @close="closeProductRowsDialog"
      center
      :visible.sync="dialogProductRowsShow"
      width="1000px"
      class="row-form"
    >
      <!-- 按钮操作区域 -->
      <div>
        <el-button size="small" type="success" icon="el-icon-plus" @click="addProductRow">新增</el-button>
        <el-button size="small" type="danger" icon="el-icon-delete" @click="deleteProductList">删除</el-button>
      </div>
      <!-- 表格显示区域 -->
      <el-table
        :data="productRows"
        ref="myTable2"
        border
        stripe
        fit
        highlight-current-row
        :row-style="{ height: '5px' }"
        :cell-style="{ padding: '5px 0' }"
        :height="curHeight2"
        @selection-change="handleSelectionChange2"
      >
        <el-table-column type="selection" align="center"></el-table-column>
        <el-table-column align="center" label="序号" width="60">
          <template slot-scope="scope">
            {{ (productCurrentPage - 1) * productPageSize + scope.$index + 1 }}
          </template>
        </el-table-column>
        <el-table-column align="center" label="操作" width="100">
          <template slot-scope="scope">
            <el-button size="mini" icon="el-icon-edit-outline" @click="editProductRow(scope.row)">编辑</el-button>
          </template>
        </el-table-column>
        <el-table-column align="center" label="商品名称" min-width="200">
          <template slot-scope="scope">
            {{ scope.row.name }}
          </template>
        </el-table-column>
        <el-table-column align="center" label="商品条码" width="240">
          <template slot-scope="scope">
            {{ scope.row.code }}
          </template>
        </el-table-column>
        <el-table-column align="center" label="商品类型" width="120">
          <template slot-scope="scope">
            {{ getDictionaryName(scope.row.type) }}
          </template>
        </el-table-column>
        <!-- <el-table-column align="center" label="商品介绍" min-width="100">
          <template slot-scope="scope">
            <el-tooltip effect="light" :content="scope.row.introduce" placement="top">
              <span>{{ scope.row.introduce }}</span>
            </el-tooltip>
          </template>
        </el-table-column> -->
      </el-table>
      <!-- 分页 -->
      <el-pagination
        background
        @size-change="handleSizeChange2"
        @current-change="handleCurrentChange2"
        :current-page="productCurrentPage"
        :page-sizes="[10, 20, 30, 40]"
        :page-size="productPageSize"
        layout="total, sizes, prev, pager, next, jumper"
        :total="productTotal"
      >
      </el-pagination>
    </el-dialog>
    <el-dialog
      v-cloak
      title="商品详情"
      @close="closeProductDialog"
      center
      :visible.sync="dialogProductShow"
      width="1000px"
      class="row-form"
    >
      <el-form :model="productRow" label-width="110px" class="demo-ruleForm" :rules="rules2" ref="productRow">
        <el-tabs type="border-card" v-model="activeName">
          <el-tab-pane label="基本信息" name="first">
            <el-row>
              <el-col :span="12">
                <el-form-item label="商品名称:" prop="name">
                  <el-input
                    v-model="productRow.name"
                    :readonly="isReadonly"
                    clearable
                    maxlength="50"
                    placeholder="请输入商品名称"
                  ></el-input>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="商品类型:" prop="type">
                  <el-select v-model="productRow.type" :readonly="isReadonly" clearable placeholder="请选择商品类型">
                    <el-option v-for="item in typeOptions" :key="item.code" :label="item.name" :value="item.code">
                    </el-option>
                  </el-select>
                </el-form-item>
              </el-col>
            </el-row>

            <el-row>
              <el-col :span="12">
                <el-form-item label="商品条码:" prop="code">
                  <el-input
                    v-model="productRow.code"
                    :readonly="isReadonly"
                    clearable
                    maxlength="50"
                    placeholder="请输入商品条码"
                  ></el-input>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="商品规格:" prop="specs">
                  <el-input
                    v-model="productRow.specs"
                    :readonly="isReadonly"
                    clearable
                    maxlength="50"
                    placeholder="请输入商品规格"
                  ></el-input>
                </el-form-item>
              </el-col>
            </el-row>

            <el-row>
              <el-col :span="12">
                <el-form-item label="商品用途:" prop="purpose">
                  <el-input
                    v-model="productRow.purpose"
                    :readonly="isReadonly"
                    clearable
                    maxlength="50"
                    placeholder="请输入商品用途"
                  ></el-input>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="商品保质期:" prop="shelfLife">
                  <el-input
                    v-model="productRow.shelfLife"
                    :readonly="isReadonly"
                    clearable
                    maxlength="50"
                    placeholder="请输入商品保质期"
                  ></el-input>
                </el-form-item>
              </el-col>
            </el-row>

            <el-row>
              <el-col :span="24">
                <el-form-item label="商品介绍:" prop="introduce">
                  <el-input
                    :readonly="isReadonly"
                    type="textarea"
                    :rows="2"
                    placeholder="请输入商品介绍"
                    v-model="productRow.introduce"
                    maxlength="255"
                  >
                  </el-input>
                </el-form-item>
              </el-col>
            </el-row>
          </el-tab-pane>
          <el-tab-pane label="商品图片" name="second">
            <!-- 上传 -->
            <el-upload
              :style="{ display: visible }"
              :action="updateUrl"
              multiple
              :http-request="httpRequest"
              list-type="picture-card"
              :on-preview="handleProductPicturePreview"
              :on-remove="handleProductPictureRemove"
              :before-upload="beforeAvatarUpload"
              :on-change="handleChangeProductPicturePhoto"
              :file-list="fileProductPictureLists"
            >
              <i class="el-icon-plus"></i>
            </el-upload>
            <!-- 预览 -->
            <el-dialog :visible.sync="dialogProductPictureVisible" width="70%" top="5vh" :append-to-body="true">
              <img width="100%" :src="dialogProductPictureImageUrl" alt="" />
            </el-dialog>
            <!-- 展示 -->
            <div class="upload-div">
              <div class="upload-div-list" v-for="(item, index) of imgProductPictureLists" :key="index">
                <div class="upload-div-top" :style="{ display: visible2 }">
                  <span class="upload-div-del" @click="fileProductPictureDelIdPhoto(index, item.id)">X</span>
                </div>
                <el-image :src="baseUrl + item.url" :preview-src-list="srcList"></el-image>
              </div>
            </div>
          </el-tab-pane>
          <el-tab-pane label="商品证书" name="third">
            <!-- 上传 -->
            <el-upload
              :style="{ display: visible }"
              :action="updateUrl"
              multiple
              :http-request="httpRequest02"
              list-type="picture-card"
              :on-preview="handleProductCertificatePreview"
              :on-remove="handleProductCertificateRemove"
              :before-upload="beforeAvatarUpload"
              :on-change="handleChangeProductCertificatePhoto"
              :file-list="fileProductCertificateLists"
            >
              <i class="el-icon-plus"></i>
            </el-upload>
            <!-- 预览 -->
            <el-dialog :visible.sync="dialogProductCertificateVisible" width="70%" top="5vh" :append-to-body="true">
              <img width="100%" :src="dialogProductCertificateImageUrl" alt="" />
            </el-dialog>
            <!-- 展示 -->
            <div class="upload-div">
              <div class="upload-div-list" v-for="(item, index) of imgProductCertificateLists" :key="index">
                <div class="upload-div-top" :style="{ display: visible2 }">
                  <span class="upload-div-del" @click="fileProductCertificateDelIdPhoto(index, item.id)">X</span>
                </div>
                <el-image :src="baseUrl + item.url" :preview-src-list="srcList02"></el-image>
              </div>
            </div>
          </el-tab-pane>
        </el-tabs>

        <span class="t-btn">
          <el-button :style="{ display: visible }" type="info" @click="dialogProductShow = false">取 消</el-button>
          <el-button :style="{ display: visible }" type="success" @click="submitProductRow">提 交</el-button>
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
        name: null,
        startDateS: null,
        endDateE: null,
        enterpriseId: window.sessionStorage.getItem('enterpriseId'),
        baseType: '养殖基地',
      },
      params2: {
        page: null,
        limit: null,
        sysModule: 1,
        enterpriseId: window.sessionStorage.getItem('enterpriseId'),
      },
      // 表单
      rows: [],
      row: {},
      productRows: [],
      productRow: {},
      // 修改or新增flag
      flag: '',
      flag2: '',
      // 分页
      currentPage: 1,
      total: 0,
      pageSize: 20,
      productCurrentPage: 1,
      productTotal: 0,
      productPageSize: 10,
      // 表单高度
      curHeight: 0,
      curHeight2: 0,
      // 活跃tab栏
      activeName: 'first',
      // 对话框
      dialogShow: false,
      dialogProductRowsShow: false,
      dialogProductShow: false,
      // 点击商品详情隐藏提交按钮
      isReadonly: false,
      visible: 'inline',
      visible2: 'block',
      // 选中删除项
      selectRows: [],
      selectRows2: [],
      // 上传地址
      updateUrl: '#',
      baseUrl: this.$baseImgUrl,
      // 预览图片src列表
      srcList: [],
      srcList02: [],
      // 产品图片
      picProductPictureListPhoto: [],
      dialogProductPictureImageUrl: '',
      dialogProductPictureVisible: false,
      imgProductPictureLists: [],
      // 上传文件
      fileProductPictureLists: [],
      // 产品证书
      picProductCertificateListPhoto: [],
      dialogProductCertificateImageUrl: '',
      dialogProductCertificateVisible: false,
      imgProductCertificateLists: [],
      // 上传文件
      fileProductCertificateLists: [],
      // 多选框
      plantingBaseOptions: [],
      typeOptions: [],
      // 验证规则
      rules: {
        name: [{ required: true, message: '请输入产品名称', trigger: 'blur' }],
        baseId: [{ required: true, message: '请选择养殖基地', trigger: 'blur' }],
        startDate: [{ required: true, message: '请选择开始时间', trigger: 'blur' }],
        endDate: [{ required: true, message: '请选择结束时间', trigger: 'blur' }],
        productDetailId: [{ required: true, message: '请选择商品', trigger: 'blur' }],
      },
      rules2: {
        name: [{ required: true, message: '请输入商品名称', trigger: 'blur' }],
        type: [{ required: true, message: '请选择商品类型', trigger: 'blur' }],
        // code: [{ required: true, message: '请输入商品条码', trigger: 'blur' }],
        // specs: [{ required: true, message: '请输入商品规格', trigger: 'blur' }],
        // purpose: [{ required: true, message: '请输入商品用途', trigger: 'blur' }],
        // shelfLife: [{ required: true, message: '请输入商品保质期', trigger: 'blur' }],
        // introduce: [{ required: true, message: '请输入商品介绍', trigger: 'blur' }]
      },
    };
  },
  created() {
    this.getTypeDictionary();
    this.getBaseDictionary();
    this.getAllList();
    this.getAllProductList();
    this.setTableHeight();
    window.onresize = () => {
      this.setTableHeight();
    };
  },
  methods: {
    // 获取数据字典
    async getTypeDictionary() {
      const { data: res } = await this.$http.get('comSys/DataDictionary/getAllMayToCondition', {
        params: {
          type: '水产产品类型',
          remark: 'X2',
        },
      });
      if (res.status !== 200) {
        return this.$message({
          duration: 1000,
          type: 'error',
          message: '获取产品类型失败！',
        });
      }
      this.typeOptions = res.data;
    },
    async getBaseDictionary() {
      const { data: res } = await this.$http.get('zsSys/BaseInfo/pageByEId', {
        params: {
          baseType: '养殖基地',
          enterpriseId: window.sessionStorage.getItem('enterpriseId'),
        },
      });
      if (res.status !== 200) {
        return this.$message({
          duration: 1000,
          type: 'error',
          message: '获取养殖基地数据失败！',
        });
      }
      this.plantingBaseOptions = res.data.rows;
    },
    // 获取全部的列表数据
    async getAllList() {
      this.params.page = this.currentPage;
      this.params.limit = this.pageSize;
      const { data: res } = await this.$http.get('zsSys/Product/pageByEId', { params: this.params });
      if (res.status !== 200) {
        return this.$message({
          duration: 1000,
          type: 'error',
          message: '获取产品数据失败！',
        });
      }
      this.rows = res.data.rows;
      this.total = res.data.total;
    },
    async getAllProductList() {
      this.params2.page = this.productCurrentPage;
      this.params2.limit = this.productPageSize;
      const { data: res } = await this.$http.get('zsSys/ProductDetail/pageByEId', { params: this.params2 });
      if (res.status !== 200) {
        return this.$message({
          duration: 1000,
          type: 'error',
          message: '获取商品数据失败！',
        });
      }
      this.productRows = res.data.rows;
      this.productTotal = res.data.total;
    },
    // 每页多少条
    handleSizeChange(val) {
      this.pageSize = val;
      this.getAllList();
    },
    handleSizeChange2(val) {
      this.productPageSize = val;
      this.getAllProductList();
    },
    // 当前页
    handleCurrentChange(val) {
      this.currentPage = val;
      this.getAllList();
    },
    handleCurrentChange2(val) {
      this.productCurrentPage = val;
      this.getAllProductList();
    },
    // 设定表格高度
    setTableHeight() {
      let h = document.documentElement.clientHeight || document.body.clientHeight;
      this.curHeight = h - 380;
      this.curHeight2 = h - 520;
    },
    // 处理选中数据
    handleSelectionChange(val) {
      this.selectRows = val;
    },
    handleSelectionChange2(val) {
      this.selectRows2 = val;
    },
    // 处理图片
    httpRequest(data) {
      let rd = new FileReader(); // 创建文件读取对象
      let file = data.file;
      rd.readAsDataURL(file); // 文件读取装换为base64类型
      rd.onloadend = function() {
        this.dialogProductPictureImageUrl = this.result; // this指向当前方法onloadend的作用域
      };
    },
    httpRequest02(data) {
      let rd = new FileReader(); // 创建文件读取对象
      let file = data.file;
      rd.readAsDataURL(file); // 文件读取装换为base64类型
      rd.onloadend = function() {
        this.dialogProductCertificateImageUrl = this.result; // this指向当前方法onloadend的作用域
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
      //   this.$message.error('上传头像图片大小不能超过 10MB!');
      // }
      return isJPG || isPng;
    },
    // 产品图片预览对话框
    handleProductPicturePreview(file) {
      this.dialogProductPictureImageUrl = file.url;
      this.dialogProductPictureVisible = true;
    },
    // 删除要上传列表中的图片
    handleProductPictureRemove(file, fileLists) {
      for (let i in this.picProductPictureListPhoto) {
        if (this.picProductPictureListPhoto[i].key === file.uid) {
          this.picProductPictureListPhoto.splice(i, 1);
        }
      }
      this.fileProductPictureLists = fileLists;
    },
    // 上传图片列表文件
    handleChangeProductPicturePhoto(file, fileList) {
      this.fileProductPictureLists = fileList;
    },
    // 删除已上传的图片
    fileProductPictureDelIdPhoto(index, id) {
      this.$confirm('此操作将永久删除该图片, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning',
      })
        .then(() => {
          this.productRow.delFileIds.push(id);
          this.imgProductPictureLists.splice(index, 1);
        })
        .catch(() => {
          this.$message({
            duration: 1000,
            type: 'info',
            message: '已取消操作',
          });
        });
    },
    // 产品证书图片预览对话框
    handleProductCertificatePreview(file) {
      this.dialogProductCertificateImageUrl = file.url;
      this.dialogProductCertificateVisible = true;
    },
    // 删除要上传列表中的图片
    handleProductCertificateRemove(file, fileLists) {
      for (let i in this.picProductCertificateListPhoto) {
        if (this.picProductCertificateListPhoto[i].key === file.uid) {
          this.picProductCertificateListPhoto.splice(i, 1);
        }
      }
      this.fileProductCertificateLists = fileLists;
    },
    // 上传图片列表文件
    handleChangeProductCertificatePhoto(file, fileList) {
      this.fileProductCertificateLists = fileList;
    },
    // 删除已上传的图片
    fileProductCertificateDelIdPhoto(index, id) {
      this.$confirm('此操作将永久删除该图片, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning',
      })
        .then(() => {
          this.productRow.delFileIds.push(id);
          this.imgProductCertificateLists.splice(index, 1);
        })
        .catch(() => {
          this.$message({
            duration: 1000,
            type: 'info',
            message: '已取消操作',
          });
        });
    },
    // 关闭对话框
    closeDialog() {
      this.row = {};
      this.getAllList();
      this.$refs.row.clearValidate();
    },
    closeProductRowsDialog() {
      this.$refs.myTable2.clearSelection();
    },
    closeProductDialog() {
      this.productRow = {};
      this.srcList = [];
      this.srcList02 = [];
      this.picProductPictureListPhoto = [];
      this.picProductCertificateListPhoto = [];
      this.imgProductPictureLists = [];
      this.imgProductCertificateLists = [];
      this.fileProductPictureLists = [];
      this.fileProductCertificateLists = [];
      this.activeName = 'first';
      this.getAllProductList();
      this.$refs.productRow.clearValidate();
    },
    // 展示商品配置
    showProductSetting() {
      this.dialogProductRowsShow = true;
    },
    // 展示商品详情
    showDetail(id) {
      for (let i = 0; i < this.productRows.length; i++) {
        if (id === this.productRows[i].id) {
          this.productRow = this.productRows[i];
          if (this.productRows[i].pictureList.length !== 0) {
            this.imgProductPictureLists = this.productRows[i].pictureList;
            for (let i = 0; i < this.imgProductPictureLists.length; i++) {
              this.srcList.push(this.baseUrl + this.imgProductPictureLists[i].url);
            }
          }
          if (this.productRows[i].certificateList.length !== 0) {
            this.imgProductCertificateLists = this.productRows[i].certificateList;
            for (let i = 0; i < this.imgProductCertificateLists.length; i++) {
              this.srcList02.push(this.baseUrl + this.imgProductCertificateLists[i].url);
            }
          }
          this.dialogProductShow = true;
          this.visible = 'none';
          this.visible2 = 'none';
          this.isReadonly = true;
        }
      }
    },
    // 编辑数据
    editRow(row) {
      this.row = row;
      this.dialogShow = true;
      this.flag = 'edit';

      if (this.getProductName(row.productDetailId) == undefined) {
        this.row.productDetailId = '';
      }
    },
    editProductRow(row) {
      this.visible = 'inline';
      this.visible2 = 'block';
      this.isReadonly = false;
      this.productRow = row;
      this.productRow.delFileIds = [];
      if (row.pictureList && row.pictureList.length !== 0) {
        this.imgProductPictureLists = row.pictureList;
        for (let i = 0; i < this.imgProductPictureLists.length; i++) {
          this.srcList.push(this.baseUrl + this.imgProductPictureLists[i].url);
        }
      }
      if (row.pictureList && row.certificateList.length !== 0) {
        this.imgProductCertificateLists = row.certificateList;
        for (let i = 0; i < this.imgProductCertificateLists.length; i++) {
          this.srcList02.push(this.baseUrl + this.imgProductCertificateLists[i].url);
        }
      }
      this.dialogProductShow = true;
      this.flag2 = 'edit';
    },
    // 新增数据
    addRow() {
      this.dialogShow = true;
      this.flag = 'add';
    },
    addProductRow() {
      this.visible = 'inline';
      this.visible2 = 'block';
      this.isReadonly = false;
      this.dialogProductShow = true;
      this.flag2 = 'add';
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
          const { data: res } = await this.$http.post('zsSys/Product/batchDelete', delIds);
          if (res.status !== 200) {
            return this.$message({
              duration: 2000,
              type: 'error',
              message: '删除产品失败！',
            });
          }
          this.$message({
            duration: 2000,
            type: 'success',
            message: '删除产品成功！',
          });
          this.$refs.myTable.clearSelection();
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
    deleteProductList() {
      this.$confirm('此操作将删除选中的数据, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning',
      })
        .then(async () => {
          let delIds = [];
          for (let i = 0; i < this.selectRows2.length; i++) {
            let item = this.selectRows2[i];
            delIds.push(item.id);
          }
          const { data: res } = await this.$http.post('zsSys/ProductDetail/batchDelete', delIds);
          if (res.status !== 200) {
            return this.$message({
              duration: 2000,
              type: 'error',
              message: '删除商品失败！',
            });
          }
          this.$message({
            duration: 2000,
            type: 'success',
            message: '删除商品成功！',
          });
          this.$refs.myTable2.clearSelection();
          this.getAllProductList();
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
      this.$refs.row.validate(async (valid) => {
        if (!valid) return this.$message.error('信息填写不完整或不准确，请检查再提交！');
        let fd = new FormData();
        for (let key in this.row) {
          let value = this.row[key];
          // 过滤
          if (key === 'pictureList') {
            // fd.append(key, '');
          } else {
            if (value !== null) {
              fd.append(key, value);
            }
          }
        }

        if (this.flag === 'add') {
          const { data: res } = await this.$http.post('zsSys/Product/addDataAndFile', fd);
          if (res.status !== 200) {
            return this.$message({
              duration: 2000,
              type: 'error',
              message: '新增产品失败！',
            });
          }
          this.getAllList();
          this.dialogShow = false;
          this.$message({
            duration: 2000,
            type: 'success',
            message: '新增产品成功！',
          });
        } else if (this.flag === 'edit') {
          const { data: res } = await this.$http.put('zsSys/Product/updateDataAndFile', fd);
          if (res.status !== 200) {
            return this.$message({
              duration: 2000,
              type: 'error',
              message: '修改产品失败！',
            });
          }
          this.getAllList();
          this.dialogShow = false;
          this.$message({
            duration: 2000,
            type: 'success',
            message: '修改产品成功！',
          });
        }
      });
    }, 3000),
    submitProductRow: Debounce(function() {
      this.productRow.enterpriseId = window.sessionStorage.getItem('enterpriseId');
      this.productRow.sysModule = 1;
      this.$refs.productRow.validate(async (valid) => {
        if (!valid) return this.$message.error('信息填写不完整或不准确，请检查再提交！');
        let fd = new FormData();
        for (let key in this.productRow) {
          let value = this.productRow[key];
          // 过滤
          if (key === 'pictureList') {
            // fd.append(key, '');
          } else if (key === 'certificateList') {
            // fd.append(key, '');
          } else {
            if (value !== null) {
              fd.append(key, value);
            }
          }
        }

        //图片文件
        if (this.fileProductPictureLists != null) {
          for (let i = 0; i < this.fileProductPictureLists.length; i++) {
            fd.append('pictureFiles', this.fileProductPictureLists[i].raw);
          }
        }
        if (this.fileProductCertificateLists != null) {
          for (let i = 0; i < this.fileProductCertificateLists.length; i++) {
            fd.append('certificateFiles', this.fileProductCertificateLists[i].raw);
          }
        }

        if (this.flag2 === 'add') {
          const { data: res } = await this.$http.post('zsSys/ProductDetail/addDataAndFile', fd);
          if (res.status !== 200) {
            return this.$message({
              duration: 2000,
              type: 'error',
              message: '新增商品失败！',
            });
          }
          this.getAllProductList();
          this.dialogProductShow = false;
          this.$message({
            duration: 2000,
            type: 'success',
            message: '新增商品成功！',
          });
        } else if (this.flag2 === 'edit') {
          const { data: res } = await this.$http.put('zsSys/ProductDetail/updateDataAndFile', fd);
          if (res.status !== 200) {
            return this.$message({
              duration: 2000,
              type: 'error',
              message: '修改商品失败！',
            });
          }
          this.getAllProductList();
          this.dialogProductShow = false;
          this.$message({
            duration: 2000,
            type: 'success',
            message: '修改商品成功！',
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
    // 根据code转换成name
    getDictionaryName(code) {
      for (let i = 0; i < this.typeOptions.length; i++) {
        if (code === this.typeOptions[i].code) {
          return this.typeOptions[i].name;
        }
      }
    },
    getBaseName(id) {
      for (let i = 0; i < this.plantingBaseOptions.length; i++) {
        if (id === this.plantingBaseOptions[i].id) {
          return this.plantingBaseOptions[i].name;
        }
      }
    },
    getProductName(id) {
      for (let i = 0; i < this.productRows.length; i++) {
        if (id === this.productRows[i].id) {
          return this.productRows[i].name;
        }
      }
    },
  },
};
</script>

<style scoped lang="less"></style>
