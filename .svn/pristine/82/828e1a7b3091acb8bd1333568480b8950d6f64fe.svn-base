<template>
  <div>
    <!-- 面包屑导航区域 -->
    <el-breadcrumb separator-class="el-icon-arrow-right">
      <el-breadcrumb-item>首页</el-breadcrumb-item>
      <el-breadcrumb-item>检验管理</el-breadcrumb-item>
    </el-breadcrumb>
    <!-- 卡片视图区域 -->
    <el-card>
      <!-- 查询区域 -->
      <div class="query-condition">
        <div class="query-item">
          <span>企业名称:</span>
          <el-select v-model="params.enterpriseId" clearable filterable style="width: 240px;" placeholder="请选择企业">
            <el-option v-for="item in enterpriseOptions" :key="item.id" :label="item.name" :value="item.id">
            </el-option>
          </el-select>
        </div>
        <div class="query-item">
          <span>追溯码:</span>
          <el-input
            v-model="params.productBatchNumber"
            clearable
            style="width: 220px;"
            placeholder="请输入完整的追溯码"
          ></el-input>
        </div>
        <div class="query-item">
          <span>检测日期:</span>
          <el-date-picker
            value-format="yyyy-MM-dd"
            v-model="params.testDateS"
            type="date"
            style="width: 160px;"
            placeholder="请选择检测日期"
          >
          </el-date-picker>
        </div>
        <div class="query-item">
          <span>结束日期:</span>
          <el-date-picker
            value-format="yyyy-MM-dd"
            v-model="params.testDateE"
            type="date"
            style="width: 160px;"
            placeholder="请选择结束日期"
          >
          </el-date-picker>
        </div>
        <el-button size="small" type="primary" icon="el-icon-search" @click="searchRow">查询</el-button>
      </div>
      <!-- 按钮操作区域 -->
      <div>
        <el-button size="small" type="success" icon="el-icon-plus" @click="addRow">新增</el-button>
        <el-button size="small" type="warning" icon="el-icon-setting" @click="showItem">检验项目配置</el-button>
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
        <el-table-column align="center" label="企业名称" min-width="160">
          <template slot-scope="scope">
            {{ getName2(scope.row.enterpriseId) }}
          </template>
        </el-table-column>
        <el-table-column align="center" label="检验商品" width="240">
          <template slot-scope="scope">
            {{ getName3(scope.row.productDetailId) }}
          </template>
        </el-table-column>
        <el-table-column align="center" label="追溯码" width="200">
          <template slot-scope="scope">
            {{ scope.row.productBatchNumber }}
          </template>
        </el-table-column>
        <el-table-column align="center" label="检验日期" width="120">
          <template slot-scope="scope">
            {{ filterTime(scope.row.testDate) }}
          </template>
        </el-table-column>
        <el-table-column align="center" label="检验数量" width="160">
          <template slot-scope="scope">
            {{ scope.row.testAmount + getDictionaryName(scope.row.testAmountUnit) }}
          </template>
        </el-table-column>
        <el-table-column align="center" label="检验结果" width="100">
          <template slot-scope="scope">
            <el-tag v-if="scope.row.testResult === '合格'" type="success">合格</el-tag>
            <el-tag v-else type="danger">未合格</el-tag>
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
      title="抽检信息"
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
                <el-form-item label="企业名称:" prop="enterpriseId">
                  <el-select
                    v-model="row.enterpriseId"
                    clearable
                    filterable
                    placeholder="请选择企业"
                    @change="getEnterpriseId"
                  >
                    <el-option v-for="item in enterpriseOptions" :key="item.id" :label="item.name" :value="item.id">
                    </el-option>
                  </el-select>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="商品名称:" prop="productDetailId">
                  <el-select
                    v-model="row.productDetailId"
                    clearable
                    filterable
                    placeholder="请选择商品"
                    @change="getProductDetailId"
                  >
                    <el-option v-for="item in productDetailOptions" :key="item.id" :label="item.name" :value="item.id">
                    </el-option>
                  </el-select>
                </el-form-item>
              </el-col>
            </el-row>

            <el-row>
              <el-col :span="12">
                <el-form-item label="追溯码:" prop="productBatchNumber">
                  <el-select
                    v-model="row.productBatchNumber"
                    clearable
                    filterable
                    placeholder="请选择追溯码"
                    @change="updateData"
                  >
                    <el-option v-for="item in productBatchOptions" :key="item" :label="item" :value="item"></el-option>
                  </el-select>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="检验日期:" prop="testDate">
                  <el-date-picker
                    value-format="yyyy-MM-dd"
                    v-model="row.testDate"
                    type="date"
                    clearable
                    placeholder="请选择检验日期"
                  >
                  </el-date-picker>
                </el-form-item>
              </el-col>
            </el-row>

            <el-row>
              <el-col :span="12">
                <el-form-item label="检验数量:" prop="testAmount">
                  <el-input
                    v-model="row.testAmount"
                    clearable
                    onkeyup="value=value.replace(/[^\d^\.]/g,'')"
                    placeholder="请输入检验数量"
                    @blur="testAmountChange"
                  >
                  </el-input>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="检验数量单位:" prop="testAmountUnit">
                  <el-select v-model="row.testAmountUnit" clearable placeholder="请选择检验数量单位">
                    <el-option v-for="item in amountUnitOptions" :key="item.code" :label="item.name" :value="item.code">
                    </el-option>
                  </el-select>
                </el-form-item>
              </el-col>
            </el-row>

            <el-row>
              <el-col :span="12">
                <el-form-item label="检验结果:" prop="testResult">
                  <el-radio v-model="row.testResult" label="合格">合格</el-radio>
                  <el-radio v-model="row.testResult" label="未合格">未合格</el-radio>
                </el-form-item>
              </el-col>
            </el-row>
          </el-tab-pane>
          <el-tab-pane label="检验结果" name="second">
            <!-- 上传 -->
            <el-upload
              :action="updateUrl"
              :http-request="httpRequest"
              list-type="picture-card"
              :on-preview="handleInspectionPicturePreview"
              :on-remove="handleInspectionPictureRemove"
              :before-upload="beforeAvatarUpload"
              :on-change="handleChangeInspectionPicturePhoto"
              :file-list="fileInspectionPictureLists"
            >
              <i class="el-icon-plus"></i>
            </el-upload>
            <!-- 预览 -->
            <el-dialog :visible.sync="dialogInspectionPictureVisible" width="70%" top="5vh" :append-to-body="true">
              <img width="100%" :src="dialogInspectionPictureImageUrl" alt="" />
            </el-dialog>
            <!-- 展示 -->
            <div class="upload-div">
              <div class="upload-div-list" v-for="(item, index) of imgInspectionPictureLists" :key="index">
                <div class="upload-div-top">
                  <span class="upload-div-del" @click="fileInspectionPictureDelIdPhoto(index, item.id)">X</span>
                </div>
                <el-image :src="baseUrl + item.url" :preview-src-list="srcList"></el-image>
              </div>
            </div>
          </el-tab-pane>
          <el-tab-pane label="检验项目" name="third">
            <!-- 按钮操作区域 -->
            <div>
              <el-button size="small" type="success" icon="el-icon-plus" @click="addItemRow">新增</el-button>
            </div>
            <!-- 表格显示区域 -->
            <el-table
              :data="testItemRows"
              border
              stripe
              fit
              highlight-current-row
              :row-style="{ height: '5px' }"
              :cell-style="{ padding: '5px 0' }"
              :height="curHeight3"
            >
              <el-table-column align="center" label="序号" width="60">
                <template slot-scope="scope">
                  {{ scope.$index + 1 }}
                </template>
              </el-table-column>
              <el-table-column align="center" label="操作" width="200">
                <template slot-scope="scope">
                  <el-button size="mini" type="primary" icon="el-icon-edit-outline" @click="editItemRow(scope.row)"
                    >编辑
                  </el-button>
                  <el-button size="mini" type="danger" icon="el-icon-delete" @click="deleteItemRow(scope.row)"
                    >删除
                  </el-button>
                </template>
              </el-table-column>
              <el-table-column align="center" label="检验项目名称" width="160">
                <template slot-scope="scope">
                  {{ getName(scope.row.itemId) }}
                </template>
              </el-table-column>
              <el-table-column align="center" label="检验项目结果" min-width="100">
                <template slot-scope="scope">
                  <el-tooltip effect="light" :content="scope.row.testResult" placement="top">
                    <span>{{ scope.row.testResult }}</span>
                  </el-tooltip>
                </template>
              </el-table-column>
            </el-table>
          </el-tab-pane>
        </el-tabs>

        <span class="t-btn">
          <el-button type="info" @click="dialogShow = false">取 消</el-button>
          <el-button type="success" @click="submitRow">提 交</el-button>
        </span>
      </el-form>
    </el-dialog>
    <el-dialog
      v-cloak
      title="检验项目配置"
      center
      :visible.sync="dialogItemSettingShow"
      width="1000px"
      class="row-form"
    >
      <!-- 按钮操作区域 -->
      <div>
        <el-button size="small" type="success" icon="el-icon-plus" @click="addItem">新增</el-button>
      </div>
      <!-- 表格显示区域 -->
      <el-table
        :data="itemRows"
        border
        stripe
        fit
        highlight-current-row
        :row-style="{ height: '5px' }"
        :cell-style="{ padding: '5px 0' }"
        :height="curHeight2"
      >
        <el-table-column align="center" label="序号" width="60">
          <template slot-scope="scope">
            {{ (itemCurrentPage - 1) * itemPageSize + scope.$index + 1 }}
          </template>
        </el-table-column>
        <el-table-column align="center" label="操作" width="200">
          <template slot-scope="scope">
            <el-button size="mini" type="primary" icon="el-icon-edit-outline" @click="editItem(scope.row)"
              >编辑
            </el-button>
            <el-button size="mini" type="danger" icon="el-icon-delete" @click="deleteItem(scope.row.id)"
              >删除
            </el-button>
          </template>
        </el-table-column>
        <el-table-column align="center" label="检验项目名称" width="160">
          <template slot-scope="scope">
            {{ scope.row.name }}
          </template>
        </el-table-column>
        <el-table-column align="center" label="检验项目描述" min-width="100">
          <template slot-scope="scope">
            <el-tooltip effect="light" :content="scope.row.description" placement="top">
              <span>{{ scope.row.description }}</span>
            </el-tooltip>
          </template>
        </el-table-column>
      </el-table>
      <!-- 分页 -->
      <el-pagination
        background
        @size-change="handleSizeChange2"
        @current-change="handleCurrentChange2"
        :current-page="itemCurrentPage"
        :page-sizes="[10, 20, 30, 40]"
        :page-size="itemPageSize"
        layout="total, sizes, prev, pager, next, jumper"
        :total="itemTotal"
      >
      </el-pagination>
    </el-dialog>
    <el-dialog
      v-cloak
      title="检验项目"
      @close="closeItemDialog"
      center
      :visible.sync="dialogItemShow"
      width="1000px"
      class="row-form"
    >
      <el-form :model="itemRow" label-width="120px" class="demo-ruleForm" :rules="rules2" ref="itemRow">
        <el-row>
          <el-col :span="24">
            <el-form-item label="检验项目名称:" prop="name">
              <el-input v-model="itemRow.name" clearable maxlength="50" placeholder="请输入检验项目名称"></el-input>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row>
          <el-col :span="24">
            <el-form-item label="检验项目描述:" prop="description">
              <el-input
                type="textarea"
                :rows="2"
                placeholder="请输入检验项目描述"
                v-model="itemRow.description"
                maxlength="255"
              >
              </el-input>
            </el-form-item>
          </el-col>
        </el-row>

        <span class="t-btn">
          <el-button type="info" @click="dialogItemShow = false">取 消</el-button>
          <el-button type="success" @click="submitItemRow">提 交</el-button>
        </span>
      </el-form>
    </el-dialog>
    <el-dialog
      v-cloak
      title="检验项目结果"
      @close="closeItemRowDialog"
      center
      :visible.sync="dialogItemRowShow"
      width="1000px"
      class="row-form"
    >
      <el-form :model="testItemRow" label-width="120px" class="demo-ruleForm" :rules="rules3" ref="testItemRow">
        <el-row>
          <el-col :span="24">
            <el-form-item label="检验项目名称:" prop="itemId">
              <el-select v-model="testItemRow.itemId" clearable placeholder="请选择检验项目">
                <el-option v-for="item in itemRows" :key="item.id" :label="item.name" :value="item.id"></el-option>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row>
          <el-col :span="24">
            <el-form-item label="检验项目结果:" prop="testResult">
              <el-input v-model="testItemRow.testResult" clearable maxlength="50" placeholder="请输入检验项目结果">
              </el-input>
            </el-form-item>
          </el-col>
        </el-row>

        <span class="t-btn">
          <el-button type="info" @click="dialogItemRowShow = false">取 消</el-button>
          <el-button type="success" @click="submitTestItemRow">确 定</el-button>
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
        enterpriseId: null,
        productBatchNumber: null,
        testDateS: null,
        testDateE: null
      },
      // 表单
      rows: [],
      row: {},
      // 检验项目表单
      itemRows: [],
      itemRow: {},
      testItemRows: [],
      del_testItemRows: [],
      testItemRow: {},
      // 修改or新增flag
      flag: '',
      flag2: '',
      flag3: '',
      // 分页
      currentPage: 1,
      total: 0,
      pageSize: 20,
      itemCurrentPage: 1,
      itemTotal: 0,
      itemPageSize: 10,
      // 表单高度
      curHeight: 0,
      curHeight2: 0,
      curHeight3: 0,
      // 对话框
      dialogShow: false,
      dialogItemSettingShow: false,
      dialogItemShow: false,
      dialogItemRowShow: false,
      // 活跃tab栏
      activeName: 'first',
      // 选中删除项
      selectRows: [],
      // 检验结果
      updateUrl: '#',
      baseUrl: this.$baseImgUrl,
      // 预览图片src列表
      srcList: [],
      picInspectionPictureListPhoto: [],
      dialogInspectionPictureImageUrl: '',
      dialogInspectionPictureVisible: false,
      imgInspectionPictureLists: [],
      // 上传文件
      fileInspectionPictureLists: [],
      // 多选框
      amountUnitOptions: [],
      enterpriseOptions: [],
      productDetailAllOptions: [],
      productDetailOptions: [],
      productBatchOptions: [],
      // 企业id
      enterpriseId: '',
      // 商品id
      productDetailId: '',
      // 验证规则
      rules: {
        enterpriseId: [{ required: true, message: '请选择企业', trigger: 'blur' }],
        productDetailId: [{ required: true, message: '请选择商品', trigger: 'blur' }],
        productBatchNumber: [{ required: true, message: '请选择追溯码', trigger: 'blur' }],
        testDate: [{ required: true, message: '请选择检验日期', trigger: 'blur' }],
        testAmount: [{ required: true, message: '请输入检验数量', trigger: 'blur' }],
        testAmountUnit: [{ required: true, message: '请选择检验数量单位', trigger: 'blur' }],
        testResult: [{ required: true, message: '请选择检验结果', trigger: 'blur' }],
      },
      rules2: {
        name: [{ required: true, message: '请输入检验项目名称', trigger: 'blur' }],
        description: [{ required: true, message: '请输入检验项目描述', trigger: 'blur' }],
      },
      rules3: {
        itemId: [{ required: true, message: '请选择检验项目', trigger: 'blur' }],
        testResult: [{ required: true, message: '请输入检验项目结果', trigger: 'blur' }],
      },
      testType: 'JL3',
    };
  },
  created() {
    this.getAmountUnitDictionary();
    this.getEnterpriseDictionary();
    this.getProductDetailAllDictionary();
    this.getTestItem();
    this.getAllList();
    this.setTableHeight();
    window.onresize = () => {
      this.setTableHeight();
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
    async getEnterpriseDictionary() {
      const { data: res } = await this.$http.get('zsSys/Enterprise/pageByCondition', {
        params: {
          status: 1,
        },
      });
      if (res.status !== 200) {
        return this.$message({
          duration: 1000,
          type: 'error',
          message: '获取企业名称数据失败！',
        });
      }
      this.enterpriseOptions = res.data.rows;
    },
    async getProductDetailAllDictionary() {
      this.productDetailOptions = [];
      const { data: res } = await this.$http.get('zsSys/ProductDetail/page');
      if (res.status !== 200) {
        return this.$message({
          duration: 1000,
          type: 'error',
          message: '获取全部商品名称数据失败！',
        });
      }
      this.productDetailAllOptions = res.data.rows;
    },
    async getProductDetailDictionary() {
      this.productDetailOptions = [];
      const { data: res } = await this.$http.get('zsSys/ProductDetail/pageByEId', {
        params: {
          enterpriseId: this.enterpriseId,
        },
      });
      if (res.status !== 200) {
        return this.$message({
          duration: 1000,
          type: 'error',
          message: '获取商品名称数据失败！',
        });
      }
      this.productDetailOptions = res.data.rows;
    },
    async getProductBatchDictionary() {
      this.productBatchOptions = [];
      const { data: res } = await this.$http.get('zsSys/ProcessBatch/pageByEId', {
        params: {
          enterpriseId: this.enterpriseId,
          productDetailId: this.productDetailId,
        },
      });
      if (res.status !== 200) {
        return this.$message({
          duration: 1000,
          type: 'error',
          message: '获取追溯码数据失败！',
        });
      }
      this.row.productBatchNumber = res.data.rows[0].productBatchNumber;
      for (let i = 0; i < res.data.rows.length; i++) {
        this.productBatchOptions.push(res.data.rows[i].productBatchNumber);
      }
    },
    async getTestItem() {
      const { data: res } = await this.$http.get('zsSys/ItemInfo/page', {
        params: {
          page: this.itemCurrentPage,
          limit: this.itemPageSize,
        },
      });
      if (res.status !== 200) {
        return this.$message({
          duration: 1000,
          type: 'error',
          message: '获取检验项目数据失败！',
        });
      }
      this.itemRows = res.data.rows;
      this.itemTotal = res.data.total;
    },
    // 获取全部的列表数据
    async getAllList() {
      this.params.page = this.currentPage;
      this.params.limit = this.pageSize;
      const { data: res } = await this.$http.get('jdSys/DeptTest/pageByEId', { params: this.params });
      if (res.status !== 200) {
        return this.$message({
          duration: 1000,
          type: 'error',
          message: '获取检验数据失败！',
        });
      }
      this.rows = res.data.rows;
      this.total = res.data.total;
    },
    async getTestItemList(id) {
      const { data: res } = await this.$http.get('zsSys/BetweenTestItem/pageByTIdAndTType', {
        params: {
          testId: id,
          testType: this.testType,
        },
      });
      if (res.status !== 200) {
        return this.$message({
          duration: 1000,
          type: 'error',
          message: '获取检验数据失败！',
        });
      }
      this.testItemRows = res.data.rows;
      console.log(this.testItemRows);
    },
    // 每页多少条
    handleSizeChange(val) {
      this.pageSize = val;
      this.getAllList();
    },
    handleSizeChange2(val) {
      this.itemPageSize = val;
      this.getTestItem();
    },
    // 当前页
    handleCurrentChange(val) {
      this.currentPage = val;
      this.getAllList();
    },
    handleCurrentChange2(val) {
      this.itemCurrentPage = val;
      this.getTestItem();
    },
    // 处理图片
    httpRequest(data) {
      let rd = new FileReader(); // 创建文件读取对象
      let file = data.file;
      rd.readAsDataURL(file); // 文件读取装换为base64类型
      rd.onloadend = function() {
        this.dialogInspectionPictureImageUrl = this.result; // this指向当前方法onloadend的作用域
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
    // 检验结果图片预览对话框
    handleInspectionPicturePreview(file) {
      this.dialogInspectionPictureImageUrl = file.url;
      this.dialogInspectionPictureVisible = true;
    },
    // 删除要上传列表中的图片
    handleInspectionPictureRemove(file, fileLists) {
      for (let i in this.picInspectionPictureListPhoto) {
        if (this.picInspectionPictureListPhoto[i].key === file.uid) {
          this.picInspectionPictureListPhoto.splice(i, 1);
        }
      }
      this.fileInspectionPictureLists = fileLists;
    },
    // 上传图片列表文件
    handleChangeInspectionPicturePhoto(file, fileList) {
      this.fileInspectionPictureLists = fileList;
    },
    // 删除已上传的图片
    fileInspectionPictureDelIdPhoto(index, id) {
      this.$confirm('此操作将永久删除该图片, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning',
      })
        .then(() => {
          this.row.delFileIds.push(id);
          this.imgInspectionPictureLists.splice(index, 1);
        })
        .catch(() => {
          this.$message({
            duration: 1000,
            type: 'info',
            message: '已取消操作',
          });
        });
    },
    // 设定表格高度
    setTableHeight() {
      let h = document.documentElement.clientHeight || document.body.clientHeight;
      this.curHeight = h - 377;
      this.curHeight2 = h - 520;
      this.curHeight3 = h - 620;
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
      this.productDetailOptions = [];
      this.productBatchOptions = [];
      this.testItemRows = [];
      this.srcList = [];
      this.picInspectionPictureListPhoto = [];
      this.imgInspectionPictureLists = [];
      this.fileInspectionPictureLists = [];
      this.enterpriseId = '';
      this.activeName = 'first';
      this.getAllList();
      this.$refs.row.clearValidate();
    },
    closeItemDialog() {
      this.itemRow = {};
      this.getTestItem();
    },
    closeItemRowDialog() {
      this.testItemRow = {};
    },
    // 展示检验项目
    showItem() {
      this.dialogItemSettingShow = true;
    },
    // 编辑数据
    editRow(row) {
      console.log(row);
      this.row = row;
      this.del_testItemRows = [];
      this.row.delFileIds = [];
      if (row.testResultPictureList.length !== 0) {
        this.imgInspectionPictureLists = row.testResultPictureList;
        for (let i = 0; i < this.imgInspectionPictureLists.length; i++) {
          this.srcList.push(this.baseUrl + this.imgInspectionPictureLists[i].url);
        }
      }
      this.dialogShow = true;
      this.flag = 'edit';
      this.getTestItemList(row.id);
      if (this.getName3(row.productDetailId) == undefined) {
        this.row.productDetailId = '';
      }
      this.enterpriseId = row.enterpriseId;
      this.productDetailId = row.productDetailId;
      this.getProductDetailDictionary();
      this.getProductBatchDictionary();
    },
    editItem(row) {
      this.itemRow = row;
      this.dialogItemShow = true;
      this.flag2 = 'edit';
    },
    editItemRow(row) {
      this.testItemRow = row;
      this.dialogItemRowShow = true;
      this.flag3 = 'edit';
    },
    // 新增数据
    addRow() {
      this.dialogShow = true;
      this.flag = 'add';
    },
    addItem() {
      this.dialogItemShow = true;
      this.flag2 = 'add';
    },
    addItemRow() {
      this.dialogItemRowShow = true;
      this.flag3 = 'add';
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
          const { data: res } = await this.$http.post('jdSys/DeptTest/batchDelete', delIds);
          if (res.status !== 200) {
            return this.$message({
              duration: 2000,
              type: 'error',
              message: '删除检验数据失败！',
            });
          }
          this.$message({
            duration: 2000,
            type: 'success',
            message: '删除检验数据成功！',
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
    deleteItem(id) {
      this.$confirm('此操作将删除选中的数据, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning',
      })
        .then(async () => {
          const { data: res } = await this.$http.delete('zsSys/ItemInfo/' + id);
          if (res.status !== 200) {
            return this.$message({
              duration: 2000,
              type: 'error',
              message: '删除检验项目失败！',
            });
          }
          this.$message({
            duration: 2000,
            type: 'success',
            message: '删除检验项目成功！',
          });
          this.getTestItem();
        })
        .catch(() => {
          this.$message({
            duration: 1000,
            type: 'info',
            message: '已取消操作',
          });
        });
    },
    deleteItemRow(row) {
      this.$confirm('此操作将删除选中的数据, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning',
      })
        .then(async () => {
          if (row.request != 'add') {
            this.del_testItemRows.push(row);
          }
          this.testItemRows.splice(this.testItemRows.indexOf(row), 1);
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
          if (key === 'testResultPictureList') {
            // fd.append(key, '');
          } else {
            if (value !== null) {
              fd.append(key, value);
            }
          }
        }

        //图片文件
        if (this.fileInspectionPictureLists != null) {
          for (let i = 0; i < this.fileInspectionPictureLists.length; i++) {
            fd.append('testResultPictureFiles', this.fileInspectionPictureLists[i].raw);
          }
        }

        if (this.flag === 'add') {
          const { data: res } = await this.$http.post('jdSys/DeptTest/addDataAndFile', fd);
          if (res.status !== 200) {
            return this.$message({
              duration: 2000,
              type: 'error',
              message: '新增检验失败！',
            });
          }
          for (const testItem of this.testItemRows) {
            testItem.testId = res.data.id;
            const { data: res_test } = await this.$http.post('zsSys/BetweenTestItem', testItem);
            if (res_test.status !== 200) {
              console.log('新增检验项目失败！id: ' + testItem.itemId);
            }
          }
          this.getAllList();
          this.dialogShow = false;
          this.$message({
            duration: 2000,
            type: 'success',
            message: '新增检验成功！',
          });
        } else if (this.flag === 'edit') {
          const { data: res } = await this.$http.put('jdSys/DeptTest/updateDataAndFile', fd);
          if (res.status !== 200) {
            return this.$message({
              duration: 2000,
              type: 'error',
              message: '修改检验失败！',
            });
          }
          for (const del_testItem of this.del_testItemRows) {
            const { data: res_test } = await this.$http.delete('zsSys/BetweenTestItem/' + del_testItem.id);
            if (res_test.status !== 200) {
              console.log('删除检验项目失败！id: ' + del_testItem.id);
            }
          }
          for (const testItem of this.testItemRows) {
            testItem.testId = res.data.id;
            if (testItem.request == 'add') {
              const { data: res } = await this.$http.post('zsSys/BetweenTestItem', testItem);
              if (res.status !== 200) {
                console.log('新增检验项目失败！id: ' + testItem.itemId);
              }
            } else if (testItem.request == 'edit') {
              const { data: res } = await this.$http.put('zsSys/BetweenTestItem', testItem);
              if (res.status !== 200) {
                console.log('编辑检验项目失败！id: ' + testItem.itemId);
              }
            }
          }
          this.getAllList();
          this.dialogShow = false;
          this.$message({
            duration: 2000,
            type: 'success',
            message: '修改检验成功！',
          });
        }
      });
    }, 3000),
    submitItemRow: Debounce(function() {
      this.itemRow.type = this.testType;
      this.$refs.itemRow.validate(async (valid) => {
        if (!valid) return this.$message.error('信息填写不完整或不准确，请检查再提交！');
        if (this.flag2 === 'add') {
          const { data: res } = await this.$http.post('zsSys/ItemInfo', this.itemRow);
          if (res.status !== 200) {
            return this.$message({
              duration: 2000,
              type: 'error',
              message: '新增检验项目失败！',
            });
          }
          this.getTestItem();
          this.dialogItemShow = false;
          this.$message({
            duration: 2000,
            type: 'success',
            message: '新增检验项目成功！',
          });
        } else if (this.flag2 === 'edit') {
          const { data: res } = await this.$http.put('zsSys/ItemInfo', this.itemRow);
          if (res.status !== 200) {
            return this.$message({
              duration: 2000,
              type: 'error',
              message: '修改检验项目失败！',
            });
          }
          this.getTestItem();
          this.dialogItemShow = false;
          this.$message({
            duration: 2000,
            type: 'success',
            message: '修改检验项目成功！',
          });
        }
      });
    }, 3000),
    submitTestItemRow: Debounce(function() {
      this.$refs.testItemRow.validate(async (valid) => {
        if (!valid) return this.$message.error('信息填写不完整或不准确，请检查再提交！');
        this.testItemRow.testId = this.row.id;
        this.testItemRow.testType = this.testType;
        if (this.flag3 === 'add') {
          this.testItemRow.request = 'add';
          this.testItemRows.push(this.testItemRow);
        } else if (this.flag3 === 'edit' && this.testItemRow.request != 'add') {
          this.testItemRow.request = 'edit';
        }
        this.dialogItemRowShow = false;
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
    testAmountChange(e) {
      this.$set(this.row, 'testAmount	', e.target.value);
    },
    // 根据code转换成name
    getDictionaryName(code) {
      for (let i = 0; i < this.amountUnitOptions.length; i++) {
        if (code === this.amountUnitOptions[i].code) {
          return this.amountUnitOptions[i].name;
        }
      }
    },
    // 获取企业id
    getEnterpriseId(value) {
      this.enterpriseId = value;
      this.row.productDetailId = null;
      this.row.productBatchNumber = null;
      this.productDetailOptions = [];
      this.productBatchOptions = [];
      this.getProductDetailDictionary();
    },
    // 获取商品id
    getProductDetailId(value) {
      this.productDetailId = value;
      this.productBatchOptions = [];
      this.row.productBatchNumber = null;
      this.getProductBatchDictionary();
    },
    // 解决数据层次太多，render函数没有自动更新，需手动强制刷新
    updateData() {
      this.$forceUpdate();
    },
    // 根据id获取name
    getName(id) {
      for (let i = 0; i < this.itemRows.length; i++) {
        if (id === this.itemRows[i].id) {
          return this.itemRows[i].name;
        }
      }
    },
    getName2(id) {
      for (let i = 0; i < this.enterpriseOptions.length; i++) {
        if (id === this.enterpriseOptions[i].id) {
          return this.enterpriseOptions[i].name;
        }
      }
    },
    getName3(id) {
      for (let i = 0; i < this.productDetailAllOptions.length; i++) {
        if (id === this.productDetailAllOptions[i].id) {
          return this.productDetailAllOptions[i].name;
        }
      }
    },
  },
};
</script>

<style scoped lang="less"></style>
