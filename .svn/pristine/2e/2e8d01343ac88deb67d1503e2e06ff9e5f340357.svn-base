<template>
  <div>
    <!-- 面包屑导航区域 -->
    <el-breadcrumb separator-class="el-icon-arrow-right">
      <el-breadcrumb-item>首页</el-breadcrumb-item>
      <el-breadcrumb-item>种植管理</el-breadcrumb-item>
      <el-breadcrumb-item>基本作业管理</el-breadcrumb-item>
    </el-breadcrumb>
    <!-- 卡片视图区域 -->
    <el-card>
      <!-- 查询区域 -->
      <div class="query-condition">
        <div class="query-item">
          <span>产品名称:</span>
          <el-select v-model="params.productId" filterable clearable style="width: 200px;" placeholder="请选择产品">
            <el-option v-for="item in productOptions" :key="item.id" :label="item.name" :value="item.id"> </el-option>
          </el-select>
        </div>
        <div class="query-item">
          <span>作业类型:</span>
          <el-select v-model="params.workType" clearable style="width: 200px;" placeholder="请选择作业类型">
            <el-option v-for="item in baseOperationOptions" :key="item.code" :label="item.name" :value="item.code">
            </el-option>
          </el-select>
        </div>
        <div class="query-item">
          <span>作业开始日期:</span>
          <el-date-picker
            value-format="yyyy-MM-dd"
            v-model="params.startDateS"
            type="date"
            style="width: 160px;"
            :picker-options="pickerStartDate"
            placeholder="请选择开始日期"
          >
          </el-date-picker>
        </div>
        <div class="query-item">
          <span>作业结束日期:</span>
          <el-date-picker
            value-format="yyyy-MM-dd"
            v-model="params.endDateE"
            type="date"
            style="width: 160px;"
            :picker-options="pickerCloseDate"
            placeholder="请选择结束日期"
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
        <el-table-column align="center" label="产品名称" min-width="160">
          <template slot-scope="scope">
            {{ getName(scope.row.productId) }}
          </template>
        </el-table-column>
        <el-table-column align="center" label="使用物品类型" width="120">
          <template slot-scope="scope">
            {{ getDictionaryName2(scope.row.workType) }}
          </template>
        </el-table-column>
        <el-table-column align="center" label="使用物品名称" width="200">
          <template slot-scope="scope">
            {{ scope.row.workObjectName }}
          </template>
        </el-table-column>
        <el-table-column align="center" label="使用物品数量" width="200">
          <template slot-scope="scope">
            {{ scope.row.workObjectAmount + getDictionaryName(scope.row.workObjectAmountUnit) }}
          </template>
        </el-table-column>
        <el-table-column align="center" label="作业开始时间" width="140">
          <template slot-scope="scope">
            {{ filterTime(scope.row.startDate) }}
          </template>
        </el-table-column>
        <el-table-column align="center" label="作业结束时间" width="140">
          <template slot-scope="scope">
            {{ filterTime(scope.row.endDate) }}
          </template>
        </el-table-column>
        <el-table-column align="center" label="天气状况" width="120">
          <template slot-scope="scope">
            {{ scope.row.weather }}
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
      title="基本作业信息"
      @close="closeDialog"
      center
      :visible.sync="dialogShow"
      width="1000px"
      class="row-form"
    >
      <el-form :model="row" label-width="160px" class="demo-ruleForm" :rules="rules" ref="row">
        <el-tabs type="border-card" v-model="activeName">
          <el-tab-pane label="基本信息" name="first">
            <el-row>
              <el-col :span="12">
                <el-form-item label="产品名称:" prop="productId">
                  <el-select v-model="row.productId" filterable clearable placeholder="请选择产品">
                    <el-option v-for="item in productOptions" :key="item.id" :label="item.name" :value="item.id">
                    </el-option>
                  </el-select>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="作业类型:" prop="workType">
                  <el-select v-model="row.workType" clearable placeholder="请选择作业类型">
                    <el-option
                      v-for="item in baseOperationOptions"
                      :key="item.code"
                      :label="item.name"
                      :value="item.code"
                    >
                    </el-option>
                  </el-select>
                </el-form-item>
              </el-col>
            </el-row>

            <el-row>
              <el-col :span="12">
                <el-form-item label="使用物品名称:" prop="workObjectName">
                  <el-input
                    v-model="row.workObjectName"
                    clearable
                    maxlength="50"
                    placeholder="请输入使用物品名称"
                  ></el-input>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="使用物品类型:" prop="workObjectType">
                  <el-input
                    v-model="row.workObjectType"
                    clearable
                    maxlength="50"
                    placeholder="请输入使用物品类型"
                  ></el-input>
                </el-form-item>
              </el-col>
            </el-row>

            <el-row>
              <el-col :span="12">
                <el-form-item label="使用物品来源:" prop="workObjectSource">
                  <!-- <el-input
                    v-model="row.workObjectSource"
                    clearable
                    maxlength="50"
                    placeholder="请输入使用物品来源"
                  ></el-input> -->
                  <el-select
                    v-model="row.workObjectSource"
                    filterable
                    allow-create
                    default-first-option
                    placeholder="请输入使用物品来源"
                  >
                    <el-option v-for="item in supplyOptions" :key="item.value" :label="item.label" :value="item.value">
                    </el-option>
                  </el-select>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="天气状况:" prop="weather">
                  <el-input v-model="row.weather" clearable maxlength="50" placeholder="请输入天气状况"></el-input>
                </el-form-item>
              </el-col>
            </el-row>

            <el-row>
              <el-col :span="12">
                <el-form-item label="使用物品数量:" prop="workObjectAmount">
                  <el-input
                    v-model="row.workObjectAmount"
                    clearable
                    onkeyup="value=value.replace(/[^\d^\.]/g,'')"
                    placeholder="请输入使用物品数量"
                    @blur="workObjectAmountChange"
                  >
                  </el-input>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="数量单位:" prop="workObjectAmountUnit">
                  <el-select v-model="row.workObjectAmountUnit" clearable placeholder="请选择数量单位">
                    <el-option v-for="item in amountUnitOptions" :key="item.code" :label="item.name" :value="item.code">
                    </el-option>
                  </el-select>
                </el-form-item>
              </el-col>
            </el-row>

            <el-row>
              <el-col :span="12">
                <el-form-item label="作业开始日期:" prop="startDate">
                  <el-date-picker
                    value-format="yyyy-MM-dd"
                    v-model="row.startDate"
                    type="date"
                    placeholder="选择作业开始日期"
                    :picker-options="pickerStartDate2"
                  >
                  </el-date-picker>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="作业结束日期:" prop="endDate">
                  <el-date-picker
                    value-format="yyyy-MM-dd"
                    v-model="row.endDate"
                    type="date"
                    placeholder="选择作业结束日期"
                    :picker-options="pickerCloseDate2"
                  >
                  </el-date-picker>
                </el-form-item>
              </el-col>
            </el-row>
          </el-tab-pane>
          <el-tab-pane label="作业图片" name="second">
            <!-- 上传 -->
            <el-upload
              :action="updateUrl"
              multiple
              :http-request="httpRequest"
              list-type="picture-card"
              :on-preview="handleOperationPicturePreview"
              :on-remove="handleOperationPictureRemove"
              :before-upload="beforeAvatarUpload"
              :on-change="handleChangeOperationPicturePhoto"
              :file-list="fileOperationPictureLists"
            >
              <i class="el-icon-plus"></i>
            </el-upload>
            <!-- 预览 -->
            <el-dialog :visible.sync="dialogOperationPictureVisible" width="70%" top="5vh" :append-to-body="true">
              <img width="100%" :src="dialogOperationPictureImageUrl" alt="" />
            </el-dialog>
            <!-- 展示 -->
            <div class="upload-div">
              <div class="upload-div-list" v-for="(item, index) of imgOperationPictureLists" :key="index">
                <div class="upload-div-top">
                  <span class="upload-div-del" @click="fileOperationPictureDelIdPhoto(index, item.id)">X</span>
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
    return {
      // 参数条件
      params: {
        page: null,
        limit: null,
        productId: null,
        workType: null,
        startDateS: null,
        endDateE: null,
        enterpriseId: window.sessionStorage.getItem('enterpriseId'),
        baseType: '种植基地',
      },
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
      // 基本作业图片
      updateUrl: '#',
      baseUrl: this.$baseImgUrl,
      // 预览图片src列表
      srcList: [],
      picOperationPictureListPhoto: [],
      dialogOperationPictureImageUrl: '',
      dialogOperationPictureVisible: false,
      imgOperationPictureLists: [],
      // 上传文件
      fileOperationPictureLists: [],
      // 多选框
      productOptions: [],
      baseOperationOptions: [],
      amountUnitOptions: [],
      // 验证规则
      rules: {
        productId: [{ required: true, message: '请选择产品', trigger: 'blur' }],
        workType: [{ required: true, message: '请选择作业类型', trigger: 'blur' }],
        workObjectName: [{ required: true, message: '请输入使用物品名称', trigger: 'blur' }],
        workObjectType: [{ required: true, message: '请输入使用物品类型', trigger: 'blur' }],
        workObjectSource: [{ required: true, message: '请输入使用物品来源', trigger: 'blur' }],
        // weather: [{ required: true, message: '请输入天气状况', trigger: 'blur' }],
        workObjectAmount: [{ required: true, message: '请输入使用物品数量', trigger: 'blur' }],
        workObjectAmountUnit: [{ required: true, message: '请选择数量单位', trigger: 'blur' }],
        startDate: [{ required: true, message: '请选择作业开始日期', trigger: 'blur' }],
        endDate: [{ required: true, message: '请选择作业结束日期', trigger: 'blur' }],
      },
      //供应商数组
      supplyOptions: [],
    };
  },
  created() {
    this.getAmountUnitDictionary();
    this.getBaseOperationDictionary();
    this.getProductDictionary();
    this.getAllList();
    this.setTableHeight();
    window.onresize = () => {
      this.setTableHeight();
    };
  },
  computed: {
    pickerStartDate() {
      const that = this;
      return {
        disabledDate(time) {
          if (that.params.endDateE && that.params.endDateE !== '') {
            return time.getTime() > new Date(that.params.endDateE).getTime();
          }
        },
      };
    },
    pickerCloseDate() {
      const that = this;
      return {
        disabledDate(time) {
          if (that.params.startDateS && that.params.startDateS !== '') {
            return time.getTime() + 3600 * 1000 * 24 < new Date(that.params.startDateS).getTime();
          }
        },
      };
    },
    pickerStartDate2() {
      const that = this;
      return {
        disabledDate(time) {
          if (that.row.endDate && that.row.endDate !== '') {
            return time.getTime() > new Date(that.row.endDate).getTime();
          }
        },
      };
    },
    pickerCloseDate2() {
      const that = this;
      return {
        disabledDate(time) {
          if (that.row.startDate && that.row.startDate !== '') {
            return time.getTime() + 3600 * 1000 * 24 < new Date(that.row.startDate).getTime();
          }
        },
      };
    },
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
    async getBaseOperationDictionary() {
      const { data: res } = await this.$http.get('comSys/DataDictionary/getAllMayToCondition', {
        params: {
          type: '种植作业类型',
          remark: 'X2',
        },
      });
      if (res.status !== 200) {
        return this.$message({
          duration: 1000,
          type: 'error',
          message: '获取种植作业类型失败！',
        });
      }
      this.baseOperationOptions = res.data;
    },
    async getProductDictionary() {
      const { data: res } = await this.$http.get('zsSys/Product/pageByEId', {
        params: {
          enterpriseId: window.sessionStorage.getItem('enterpriseId'),
          baseType: '种植基地',
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
    // 获取全部的列表数据
    async getAllList() {
      this.params.page = this.currentPage;
      this.params.limit = this.pageSize;
      const { data: res } = await this.$http.get('zsSys/BaseWork/pageByEId', { params: this.params });
      if (res.status !== 200) {
        return this.$message({
          duration: 1000,
          type: 'error',
          message: '获取基本作业数据失败！',
        });
      }
      this.rows = res.data.rows;
      this.total = res.data.total;
      const { data: res2 } = await this.$http.get('zsSys/Supplier/pageByEId', { params: this.params });
      if (res2.status == 200) {
        this.supplyOptions = res2.data.rows.map((item) => {
          return {
            value: item.id,
            label: item.name,
          };
        });
      }
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
        this.dialogOperationPictureImageUrl = this.result; // this指向当前方法onloadend的作用域
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
    // 基本作业图片预览对话框
    handleOperationPicturePreview(file) {
      this.dialogOperationPictureImageUrl = file.url;
      this.dialogOperationPictureVisible = true;
    },
    // 删除要上传列表中的图片
    handleOperationPictureRemove(file, fileLists) {
      for (let i in this.picOperationPictureListPhoto) {
        if (this.picOperationPictureListPhoto[i].key === file.uid) {
          this.picOperationPictureListPhoto.splice(i, 1);
        }
      }
      this.fileOperationPictureLists = fileLists;
    },
    // 上传图片列表文件
    handleChangeOperationPicturePhoto(file, fileList) {
      this.fileOperationPictureLists = fileList;
    },
    // 删除已上传的图片
    fileOperationPictureDelIdPhoto(index, id) {
      this.$confirm('此操作将永久删除该图片, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning',
      })
        .then(() => {
          this.row.delFileIds.push(id);
          this.imgOperationPictureLists.splice(index, 1);
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
      this.picOperationPictureListPhoto = [];
      this.imgOperationPictureLists = [];
      this.fileOperationPictureLists = [];
      this.activeName = 'first';
      this.getAllList();
      this.$refs.row.clearValidate();
    },
    // 编辑数据
    editRow(row) {
      this.row = row;
      this.row.delFileIds = [];
      if (row.workPictureList && row.workPictureList.length !== 0) {
        this.imgOperationPictureLists = row.workPictureList;
        for (let i = 0; i < this.imgOperationPictureLists.length; i++) {
          this.srcList.push(this.baseUrl + this.imgOperationPictureLists[i].url);
        }
      }
      this.dialogShow = true;
      this.flag = 'edit';
    },
    // 新增数据
    addRow() {
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
          const { data: res } = await this.$http.post('zsSys/BaseWork/batchDelete', delIds);
          if (res.status !== 200) {
            return this.$message({
              duration: 2000,
              type: 'error',
              message: '删除基本作业失败！',
            });
          }
          this.$message({
            duration: 2000,
            type: 'success',
            message: '删除基本作业成功！',
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
      this.$refs.row.validate(async (valid) => {
        if (!valid) return this.$message.error('信息填写不完整或不准确，请检查再提交！');
        let fd = new FormData();
        for (let key in this.row) {
          let value = this.row[key];
          // 过滤
          if (key === 'workPictureList') {
            // fd.append(key, '');
          } else {
            if (value !== null) {
              fd.append(key, value);
            }
          }
        }

        //图片文件
        if (this.fileOperationPictureLists != null) {
          for (let i = 0; i < this.fileOperationPictureLists.length; i++) {
            fd.append('workPictureFiles	', this.fileOperationPictureLists[i].raw);
          }
        }

        if (this.flag === 'add') {
          const { data: res } = await this.$http.post('zsSys/BaseWork/addDataAndFile', fd);
          if (res.status !== 200) {
            return this.$message({
              duration: 2000,
              type: 'error',
              message: '新增基本作业失败！',
            });
          }
          this.getAllList();
          this.dialogShow = false;
          this.$message({
            duration: 2000,
            type: 'success',
            message: '新增基本作业成功！',
          });
        } else if (this.flag === 'edit') {
          const { data: res } = await this.$http.put('zsSys/BaseWork/updateDataAndFile', fd);
          if (res.status !== 200) {
            return this.$message({
              duration: 2000,
              type: 'error',
              message: '修改基本作业失败！',
            });
          }
          this.getAllList();
          this.dialogShow = false;
          this.$message({
            duration: 2000,
            type: 'success',
            message: '修改基本作业成功！',
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
    workObjectAmountChange(e) {
      this.$set(this.row, 'workObjectAmount', e.target.value);
    },
    // 根据id获取name
    getName(id) {
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
    getDictionaryName2(code) {
      for (let i = 0; i < this.baseOperationOptions.length; i++) {
        if (code === this.baseOperationOptions[i].code) {
          return this.baseOperationOptions[i].name;
        }
      }
    },
  },
};
</script>

<style scoped lang="less"></style>
