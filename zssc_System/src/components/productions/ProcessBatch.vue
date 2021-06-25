<template>
  <div>
    <!-- 面包屑导航区域 -->
    <el-breadcrumb separator-class="el-icon-arrow-right">
      <el-breadcrumb-item>首页</el-breadcrumb-item>
      <el-breadcrumb-item>生产管理</el-breadcrumb-item>
      <el-breadcrumb-item>加工批次管理</el-breadcrumb-item>
    </el-breadcrumb>
    <!-- 卡片视图区域 -->
    <el-card>
      <!-- 查询区域 -->
      <div class="query-condition">
        <div class="query-item">
          <span>加工基地名称:</span>
          <el-select v-model="params.baseId" clearable style="width: 200px;" placeholder="请选择加工基地">
            <el-option v-for="item in productionBaseOptions" :key="item.id" :label="item.name" :value="item.id">
            </el-option>
          </el-select>
        </div>
        <div class="query-item">
          <span>追溯码:</span>
          <el-input
            v-model="params.productBatchNumber"
            clearable
            style="width: 220px;"
            placeholder="请输入追溯码"
          ></el-input>
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
        <el-table-column align="center" label="加工基地名称" min-width="200">
          <template slot-scope="scope">
            {{ getName(scope.row.baseId) }}
          </template>
        </el-table-column>
        <el-table-column align="center" label="追溯码" width="240">
          <template slot-scope="scope">
            {{ scope.row.productBatchNumber }}
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
        <el-table-column align="center" label="加工数量" width="240">
          <template slot-scope="scope">
            {{ scope.row.processAmount + getDictionaryName(scope.row.processAmountUnit) }}
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
      title="加工批次信息"
      @close="closeDialog"
      center
      :visible.sync="dialogShow"
      width="1000px"
      class="row-form"
    >
      <el-form :model="row" label-width="120px" class="demo-ruleForm" :rules="rules" ref="row">
        <el-tabs type="border-card" v-model="activeName">
          <el-tab-pane label="基本信息" name="first">
            <el-row>
              <el-col :span="12">
                <el-form-item label="加工基地名称:" prop="baseId">
                  <el-select v-model="row.baseId" clearable placeholder="请选择加工基地">
                    <el-option v-for="item in productionBaseOptions" :key="item.id" :label="item.name" :value="item.id">
                    </el-option>
                  </el-select>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="追溯码:">
                  <el-input v-model="row.productBatchNumber" readonly placeholder="自动生成追溯码"></el-input>
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
                <el-form-item label="加工数量:" prop="processAmount">
                  <el-input
                    v-model="row.processAmount"
                    clearable
                    onkeyup="value=value.replace(/[^\d^\.]/g,'')"
                    placeholder="请输入加工数量"
                    @blur="processAmountChange"
                  >
                  </el-input>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="加工数量单位:" prop="processAmountUnit">
                  <el-select v-model="row.processAmountUnit" clearable placeholder="请选择加工数量单位">
                    <el-option v-for="item in amountUnitOptions" :key="item.code" :label="item.name" :value="item.code">
                    </el-option>
                  </el-select>
                </el-form-item>
              </el-col>
            </el-row>

            <el-row>
              <el-col :span="12">
                <el-form-item label="加工方式:" prop="processAmount">
                  <el-input v-model="row.processWay" clearable maxlength="50" placeholder="请输入加工方式"></el-input>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="加工材料来源:" prop="sourceRadio">
                  <el-radio v-model="row.sourceRadio" :label="1" :disabled="isDisabled">收获</el-radio>
                  <el-radio v-model="row.sourceRadio" :label="2" :disabled="isDisabled">收购</el-radio>
                </el-form-item>
              </el-col>
            </el-row>

            <el-row>
              <el-col :span="12">
                <el-form-item label="产品名称:">
                  <el-select
                    v-model="row.harvestId"
                    :disabled="row.sourceRadio !== 1"
                    clearable
                    placeholder="请选择产品"
                  >
                    <el-option
                      v-for="item in harvestOptions"
                      :key="item.id"
                      :label="getProductName(item.productId)"
                      :value="item.id"
                    >
                    </el-option>
                  </el-select>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="收购物名称:">
                  <el-select v-model="row.buyId" :disabled="row.sourceRadio !== 2" clearable placeholder="请选择收购物">
                    <el-option v-for="item in buyOptions" :key="item.id" :label="item.name" :value="item.id">
                    </el-option>
                  </el-select>
                </el-form-item>
              </el-col>
            </el-row>

            <el-row>
              <el-col :span="12">
                <el-form-item label="商品名称:" prop="productDetailId">
                  <el-select v-model="row.productDetailId" clearable placeholder="请选择商品">
                    <el-option v-for="item in productDetailOptions" :key="item.id" :label="item.name" :value="item.id">
                    </el-option>
                  </el-select>
                </el-form-item>
              </el-col>
            </el-row>
          </el-tab-pane>
          <el-tab-pane label="产品加工" name="second">
            <!-- 上传 -->
            <el-upload
              :action="updateUrl"
              multiple
              :http-request="httpRequest"
              list-type="picture-card"
              :on-preview="handleProcessPicturePreview"
              :on-remove="handleProcessPictureRemove"
              :before-upload="beforeAvatarUpload"
              :on-change="handleChangeProcessPicturePhoto"
              :file-list="fileProcessPictureLists"
            >
              <i class="el-icon-plus"></i>
            </el-upload>
            <!-- 预览 -->
            <el-dialog :visible.sync="dialogProcessPictureVisible" width="70%" top="5vh" :append-to-body="true">
              <img width="100%" :src="dialogProcessPictureImageUrl" alt="" />
            </el-dialog>
            <!-- 展示 -->
            <div class="upload-div">
              <div class="upload-div-list" v-for="(item, index) of imgProcessPictureLists" :key="index">
                <div class="upload-div-top">
                  <span class="upload-div-del" @click="fileProcessPictureDelIdPhoto(index, item.id)">X</span>
                </div>
                <el-image :src="baseUrl + item.url" :preview-src-list="srcList"></el-image>
              </div>
            </div>
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
import { Debounce } from '../../assets/js/tool';
export default {
  data() {
    var validateSource = (rule, value, callback) => {
      if (!value) {
        callback(new Error('请选择加工材料来源!'));
      } else if (value === 1 && !this.row.harvestId) {
        callback(new Error('请选择产品!'));
      } else if (value === 2 && !this.row.buyId) {
        callback(new Error('请选择收购物!'));
      } else {
        callback();
      }
    };
    return {
      // 参数条件
      params: {
        page: null,
        limit: null,
        baseId: null,
        productBatchNumber: null,
        startDateS: null,
        endDateE: null,
        enterpriseId: window.sessionStorage.getItem('enterpriseId'),
        baseType: '养殖基地',
      },
      // 选项禁用
      isDisabled: false,
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
      // 活跃tab栏
      activeName: 'first',
      // 对话框
      dialogShow: false,
      // 选中删除项
      selectRows: [],
      // 产品加工
      updateUrl: '#',
      baseUrl: this.$baseImgUrl,
      // 预览图片src列表
      srcList: [],
      picProcessPictureListPhoto: [],
      dialogProcessPictureImageUrl: '',
      dialogProcessPictureVisible: false,
      imgProcessPictureLists: [],
      // 上传文件
      fileProcessPictureLists: [],
      // 多选框
      productionBaseOptions: [],
      harvestOptions: [],
      buyOptions: [],
      productOptions: [],
      productDetailOptions: [],
      amountUnitOptions: [],
      // 验证规则
      rules: {
        baseId: [{ required: true, message: '请选择加工基地', trigger: 'blur' }],
        startDate: [{ required: true, message: '请选择开始时间', trigger: 'blur' }],
        endDate: [{ required: true, message: '请选择结束时间', trigger: 'blur' }],
        processAmount: [{ required: true, message: '请输入加工数量', trigger: 'blur' }],
        processAmountUnit: [{ required: true, message: '请选择加工数量单位', trigger: 'blur' }],
        productDetailId: [{ required: true, message: '请选择商品', trigger: 'blur' }],
        // processWay: [{ required: true, message: '请输入加工方式', trigger: 'blur' }],
        sourceRadio: [{ required: true, validator: validateSource, trigger: 'blur' }],
      },
    };
  },
  created() {
    this.getAmountUnitDictionary();
    this.getBaseDictionary();
    this.getHarvestDictionary();
    this.getBuytDictionary();
    this.getProductDictionary();
    this.getProductDetailDictionary();
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
    async getBaseDictionary() {
      const { data: res } = await this.$http.get('zsSys/BaseInfo/pageByEId', {
        params: {
          baseType: '加工基地',
          enterpriseId: window.sessionStorage.getItem('enterpriseId'),
        },
      });
      if (res.status !== 200) {
        return this.$message({
          duration: 1000,
          type: 'error',
          message: '获取生产基地数据失败！',
        });
      }
      this.productionBaseOptions = res.data.rows;
    },
    async getHarvestDictionary() {
      const { data: res } = await this.$http.get('zsSys/Harvest/pageByEId', {
        params: {
          enterpriseId: window.sessionStorage.getItem('enterpriseId'),
          baseType: '养殖基地',
        },
      });
      if (res.status !== 200) {
        return this.$message({
          duration: 1000,
          type: 'error',
          message: '获取捕捞数据失败！',
        });
      }
      this.harvestOptions = res.data.rows;
    },
    async getBuytDictionary() {
      const { data: res } = await this.$http.get('zsSys/BuyInfo/pageByEId', {
        params: {
          enterpriseId: window.sessionStorage.getItem('enterpriseId'),
          sysModule: 1,
        },
      });
      if (res.status !== 200) {
        return this.$message({
          duration: 1000,
          type: 'error',
          message: '获取收购数据失败！',
        });
      }
      this.buyOptions = res.data.rows;
    },
    async getProductDictionary() {
      const { data: res } = await this.$http.get('zsSys/Product/pageByEId', {
        params: {
          enterpriseId: window.sessionStorage.getItem('enterpriseId'),
          baseType: '养殖基地',
        },
      });
      if (res.status !== 200) {
        return this.$message({
          duration: 1000,
          type: 'error',
          message: '获取产品数据失败！',
        });
      }
      this.productOptions = res.data.rows;
    },
    async getProductDetailDictionary() {
      const { data: res } = await this.$http.get('zsSys/ProductDetail/pageByEId', {
        params: {
          enterpriseId: window.sessionStorage.getItem('enterpriseId'),
          sysModule: 1,
        },
      });
      if (res.status !== 200) {
        return this.$message({
          duration: 1000,
          type: 'error',
          message: '获取商品数据失败！',
        });
      }
      this.productDetailOptions = res.data.rows;
    },
    // 获取全部的列表数据
    async getAllList() {
      this.params.page = this.currentPage;
      this.params.limit = this.pageSize;
      const { data: res } = await this.$http.get('zsSys/ProcessBatch/pageByEId', { params: this.params });
      if (res.status !== 200) {
        return this.$message({
          duration: 1000,
          type: 'error',
          message: '获取加工批次数据失败！',
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
    // 处理图片
    httpRequest(data) {
      let rd = new FileReader(); // 创建文件读取对象
      let file = data.file;
      rd.readAsDataURL(file); // 文件读取装换为base64类型
      rd.onloadend = function() {
        this.dialogProcessPictureImageUrl = this.result; // this指向当前方法onloadend的作用域
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
    // 产品加工图片预览对话框
    handleProcessPicturePreview(file) {
      this.dialogProcessPictureImageUrl = file.url;
      this.dialogProcessPictureVisible = true;
    },
    // 删除要上传列表中的图片
    handleProcessPictureRemove(file, fileLists) {
      for (let i in this.picProcessPictureListPhoto) {
        if (this.picProcessPictureListPhoto[i].key === file.uid) {
          this.picProcessPictureListPhoto.splice(i, 1);
        }
      }
      this.fileProcessPictureLists = fileLists;
    },
    // 上传图片列表文件
    handleChangeProcessPicturePhoto(file, fileList) {
      this.fileProcessPictureLists = fileList;
    },
    // 删除已上传的图片
    fileProcessPictureDelIdPhoto(index, id) {
      this.$confirm('此操作将永久删除该图片, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning',
      })
        .then(() => {
          this.row.delFileIds.push(id);
          this.imgProcessPictureLists.splice(index, 1);
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
      this.srcList = [];
      this.picProcessPictureListPhoto = [];
      this.imgProcessPictureLists = [];
      this.fileProcessPictureLists = [];
      this.activeName = 'first';
      this.getAllList();
      this.$refs.row.clearValidate();
    },
    // 编辑数据
    editRow(row) {
      this.row = row;
      this.isDisabled = true;
      this.row.delFileIds = [];
      if (row.productBatchNumberPictureList && row.productBatchNumberPictureList.length !== 0) {
        this.imgProcessPictureLists = row.productBatchNumberPictureList;
        for (let i = 0; i < this.imgProcessPictureLists.length; i++) {
          this.srcList.push(this.baseUrl + this.imgProcessPictureLists[i].url);
        }
      }
      if (row.harvestId) {
        this.$set(this.row, 'sourceRadio', 1);
      } else {
        this.$set(this.row, 'sourceRadio', 2);
      }
      if (this.getName(row.baseId) == undefined) {
        this.row.baseId = '';
      }
      // if (this.getProductName(row.productDetailId) == undefined) {
      //   this.row.productDetailId = '';
      // }
      this.dialogShow = true;
      console.log(row);
      this.flag = 'edit';
    },
    // 新增数据
    addRow() {
      this.isDisabled = false;
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
          const { data: res } = await this.$http.post('zsSys/ProcessBatch/batchDelete', delIds);
          if (res.status !== 200) {
            return this.$message({
              duration: 2000,
              type: 'error',
              message: '删除加工批次失败！',
            });
          }
          this.$message({
            duration: 2000,
            type: 'success',
            message: '删除加工批次成功！',
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
      if (this.row.sourceRadio === 1) {
        this.row.buyId = null;
      } else {
        this.row.harvestId = null;
      }
      this.$refs.row.validate(async (valid) => {
        if (!valid) return this.$message.error('信息填写不完整或不准确，请检查再提交！');
        let fd = new FormData();
        for (let key in this.row) {
          let value = this.row[key];
          // 过滤
          if (key === 'productBatchNumberPictureList') {
            // fd.append(key, '');
          } else if (key === 'sourceRadio') {
            // fd.append(key, '');
          } else {
            if (value !== null) {
              fd.append(key, value);
            }
          }
        }

        //图片文件
        if (this.fileProcessPictureLists != null) {
          for (let i = 0; i < this.fileProcessPictureLists.length; i++) {
            fd.append('productBatchNumberPictureFiles	', this.fileProcessPictureLists[i].raw);
          }
        }

        if (this.flag === 'add') {
          const { data: res } = await this.$http.post('zsSys/ProcessBatch/addDataAndFile', fd);
          if (res.status !== 200) {
            return this.$message({
              duration: 2000,
              type: 'error',
              message: '新增加工批次失败！',
            });
          }
          this.getAllList();
          this.dialogShow = false;
          this.$message({
            duration: 2000,
            type: 'success',
            message: '新增加工批次成功！',
          });
        } else if (this.flag === 'edit') {
          const { data: res } = await this.$http.put('zsSys/ProcessBatch/updateDataAndFile', fd);
          if (res.status !== 200) {
            return this.$message({
              duration: 2000,
              type: 'error',
              message: '修改加工批次失败！',
            });
          }
          this.getAllList();
          this.dialogShow = false;
          this.$message({
            duration: 2000,
            type: 'success',
            message: '修改加工批次成功！',
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
    processAmountChange(e) {
      this.$set(this.row, 'processAmount', e.target.value);
    },
    // 根据id获取name
    getName(id) {
      for (let i = 0; i < this.productionBaseOptions.length; i++) {
        if (id === this.productionBaseOptions[i].id) {
          return this.productionBaseOptions[i].name;
        }
      }
    },
    getProductName(id) {
      for (let i = 0; i < this.productOptions.length; i++) {
        if (id === this.productOptions[i].id) {
          return this.productOptions[i].name;
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
