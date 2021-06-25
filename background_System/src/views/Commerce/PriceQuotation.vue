<template>
  <div>
    <!-- 面包屑导航区域 -->
    <el-breadcrumb separator-class="el-icon-arrow-right">
      <el-breadcrumb-item :to="{ path: '/home' }">首页</el-breadcrumb-item>
      <el-breadcrumb-item>电子商务子系统</el-breadcrumb-item>
      <el-breadcrumb-item>价格行情管理</el-breadcrumb-item>
    </el-breadcrumb>
    <!-- 卡片视图区域 -->
    <el-card>
      <!-- 添加区域 -->
      <div>
        <el-button type="danger" size="small" icon="el-icon-delete" @click="handleBatchRemove()">批量删除 </el-button>
        <el-button type="primary" size="small" icon="el-icon-circle-plus" @click="addRow">录入价格</el-button>
        <el-button type="primary" size="small" icon="el-icon-circle-plus" @click="importByExcel">Excel导入 </el-button>
        <el-date-picker
          style="margin-left: 10px;width: 450px"
          v-model="timeRange"
          type="daterange"
          size="small"
          align="right"
          unlink-panels
          range-separator="至"
          start-placeholder="开始日期"
          end-placeholder="结束日期"
          :picker-options="pickerOptions"
        >
        </el-date-picker>

        <!--        <el-date-picker-->
        <!--          v-model="listQuery.startTime"-->
        <!--          type="date"-->
        <!--          style="width: 200px;margin-left: 10px"-->
        <!--          placeholder="选择起始时间"-->
        <!--          value-format="yyyy-MM-dd"-->
        <!--          format="yyyy-MM-dd"-->
        <!--        >-->
        <!--        </el-date-picker>-->
        <!--        <el-date-picker-->
        <!--          v-model="listQuery.endTime"-->
        <!--          type="date"-->
        <!--          style="width: 200px;margin-left: 10px"-->
        <!--          placeholder="选择结束时间"-->
        <!--          value-format="yyyy-MM-dd"-->
        <!--          format="yyyy-MM-dd"-->
        <!--        >-->
        <!--        </el-date-picker>-->
        <el-button
          class="filter-item"
          size="small"
          type="primary"
          style="margin-left: 10px"
          icon="el-icon-search"
          @click="handleFilter"
        >
          查询
        </el-button>
      </div>
      <!-- 表格显示区域 -->
      <el-table
        :data="priceList"
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
        <el-table-column align="center" label="产品名称" min-width="160">
          <template slot-scope="scope">
            <el-tooltip effect="light" :content="scope.row.productName" placement="top">
              <el-link type="primary" :underline="false" @click="editRow(scope.row)"
                >{{ scope.row.productName }}
              </el-link>
            </el-tooltip>
          </template>
        </el-table-column>
        <el-table-column align="center" label="产品分类">
          <template slot-scope="scope">
            {{ scope.row.productCategory }}
          </template>
        </el-table-column>
        <el-table-column align="center" label="价格" width="120">
          <template slot-scope="scope">
            {{ scope.row.price }}
          </template>
        </el-table-column>

        <el-table-column align="center" label="最高价">
          <template slot-scope="scope">
            {{ scope.row.highestPrice }}
          </template>
        </el-table-column>

        <el-table-column align="center" label="最低价">
          <template slot-scope="scope">
            {{ scope.row.lowestPrice }}
          </template>
        </el-table-column>

        <el-table-column align="center" label="报价时间" width="160">
          <template slot-scope="scope">
            {{ scope.row.offerTime }}
          </template>
        </el-table-column>
        <el-table-column align="center" label="采集点" width="160">
          <template slot-scope="scope">
            {{ scope.row.collectionPoint }}
          </template>
        </el-table-column>
        <el-table-column align="center" label="乡镇">
          <template slot-scope="scope">
            {{ scope.row.district }}
          </template>
        </el-table-column>
        <el-table-column align="center" label="报价员" width="120">
          <template slot-scope="scope">
            <span>{{ scope.row.pricingOffer }}</span>
          </template>
        </el-table-column>
        <el-table-column align="center" label="操作" width="200">
          <template slot-scope="scope">
            <el-button type="primary" size="mini" icon="el-icon-edit" @click="editRow(scope.row)">编辑 </el-button>
            <el-button type="danger" size="mini" icon="el-icon-delete" @click="deleteRow(scope.row)">删除 </el-button>
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
      title="价格行情信息"
      @close="closeDialog"
      center
      :visible.sync="dialogShow"
      top="7vh"
      width="50%"
      class="row-form"
    >
      <el-form :model="form" label-width="100px" class="demo-ruleForm" :rules="rules" ref="row">
        <el-tabs type="border-card">
          <el-tab-pane label="价格信息">
            <el-row>
              <el-col :span="12">
                <el-form-item label="产品分类:" prop="productCategory">
                  <!-- <el-input v-model="row.productCategory" clearable maxlength="50" placeholder="请输入产品分类"></el-input> -->
                  <el-select
                    v-model="form.productCategory"
                    style="width: 100%"
                    placeholder="请选择产品分类"
                    @change="getDic"
                  >
                    <el-option v-for="item in productOptions" :key="item" :label="item" :value="item"></el-option>
                  </el-select>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="产品名称:" prop="productName">
                  <el-select
                    v-model="form.productName"
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
                <el-form-item label="产品报价:" prop="price">
                  <el-input
                    v-model="form.price"
                    onkeyup="this.value=this.value.replace(/[^\d\.]/ig,'')"
                    clearable
                    maxlength="20"
                    placeholder="请输入产品报价"
                  ></el-input>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="报价时间:" prop="offerTime">
                  <el-date-picker
                    v-model="form.offerTime"
                    type="datetime"
                    style="width: 100%"
                    placeholder="选择报价时间"
                  >
                  </el-date-picker>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="12">
                <el-form-item label="采价地点:" prop="collectionPoint">
                  <el-input
                    v-model="form.collectionPoint"
                    clearable
                    maxlength="20"
                    placeholder="请输入采价地点"
                  ></el-input>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="报价员:" prop="pricingOffer">
                  <el-input v-model="form.pricingOffer" clearable maxlength="20" placeholder="请输入报价员"></el-input>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="12">
                <el-form-item label="最高价:" prop="highestPrice">
                  <el-input
                    v-model="form.highestPrice"
                    onkeyup="this.value=this.value.replace(/[^\d\.]/ig,'')"
                    clearable
                    maxlength="20"
                    placeholder="请输入最高价"
                  ></el-input>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="最低价:" prop="lowestPrice">
                  <el-input
                    v-model="form.lowestPrice"
                    onkeyup="this.value=this.value.replace(/[^\d\.]/ig,'')"
                    clearable
                    maxlength="20"
                    placeholder="请输入最低价"
                  ></el-input>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="12">
                <el-form-item label="乡镇:" prop="district">
                  <el-select
                    v-model="form.district"
                    filterable
                    allow-create
                    style="width: 100%"
                    placeholder="请选择乡镇名称"
                  >
                    <el-option v-for="(item, index) in townOptions" :key="index" :label="item" :value="item">
                    </el-option>
                  </el-select>
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
    <!--  导入提示框-->
    <el-dialog title="导入价格行情信息" :visible.sync="dialogVisible" width="30%">
      <el-upload
        class="upload-demo"
        ref="upload"
        action="#"
        :on-preview="handlePreview"
        :on-remove="handleRemove"
        :on-change="handleProductChange"
        :file-list="fileList"
        :limit="1"
        :auto-upload="false"
      >
        <el-button slot="trigger" size="small" type="primary">选取文件</el-button>
        <div slot="tip" class="el-upload__tip">只能上传xlsx/xls文件.</div>
      </el-upload>
      <span slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="submitUpload">上 传</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import userService from '../../globals/service/user';

export default {
  data() {
    return {
      timeRange: '',
      pickerOptions: {
        shortcuts: [
          {
            text: '最近一周',
            onClick(picker) {
              const end = new Date();
              const start = new Date();
              start.setTime(start.getTime() - 3600 * 1000 * 24 * 7);
              picker.$emit('pick', [start, end]);
            }
          },
          {
            text: '最近一个月',
            onClick(picker) {
              const end = new Date();
              const start = new Date();
              start.setTime(start.getTime() - 3600 * 1000 * 24 * 30);
              picker.$emit('pick', [start, end]);
            }
          },
          {
            text: '最近三个月',
            onClick(picker) {
              const end = new Date();
              const start = new Date();
              start.setTime(start.getTime() - 3600 * 1000 * 24 * 90);
              picker.$emit('pick', [start, end]);
            }
          }
        ]
      },
      productList: [],
      options: [],
      townOptions: [],
      //提交状态,是提交还是编辑
      flag: 'add',
      // 搜索条件
      listQuery: {
        page: 1,
        limit: 15,
        startTime: '',
        endTime: ''
      },
      total: 0,
      // 表单高度
      curHeight: 0,
      //删除ids数组
      ids: [],
      // 对话框
      dialogShow: false,
      // 删除提示框
      dialogVisible: false,
      //loading
      listLoading: false,
      fileList: [],
      // 表单
      form: {
        id: '',
        productName: '', //产品名称
        highestPrice: '', //最高价
        lowestPrice: '', // 最低价
        price: '', // 价格
        offerTime: '', // 报价时间
        pricingOffer: '', // 报价员
        productCategory: '', // 产品分类
        collectionPoint: '', //采集点
        district: ''
      },
      // 多选框
      productOptions: ['粮油', '蔬菜', '水果', '牛羊猪肉', '家禽蛋类', '水产品', '其他'],
      priceList: [], // 价格行情列表
      // 验证规则
      rules: {
        productCategory: [{ required: true, message: '请选择产品分类', trigger: 'blur' }],
        productName: [{ required: true, message: '请输入农产品名称', trigger: 'blur' }],
        price: [{ required: true, message: '请选择产品报价', trigger: 'blur' }],
        offerTime: [{ required: true, message: '请选择报价时间', trigger: 'blur' }],
        collectionPoint: [{ required: true, message: '请输入采集地点', trigger: 'blur' }],
        pricingOffer: [{ required: true, message: '请输入报价员', trigger: 'blur' }],
        district: [{ required: true, message: '请选择乡镇', trigger: 'blur' }]
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
    submitUpload() {
      let formData = new FormData();
      this.fileList.forEach(item => {
        formData.append('file', item.raw);
      });
      userService.importByExcel(formData).then(res => {
        if (res.status === 200) {
          this.$message.success('数据导入成功');
        }
        this.getAllList();
        this.dialogVisible = false;
        this.fileList = [];
      });
    },
    handleProductChange(file, fileList) {
      this.fileList = fileList;
    },
    handleRemove(file, fileList) {
      console.log(file, fileList);
    },
    handlePreview(file) {
      console.log(file);
    },
    // 获取全部的列表数据
    async getAllList() {
      let params = this.listQuery;
      userService.getPriceInfoListByPage(params).then(res => {
        if (res.status !== 200) return this.$message.error('获取失败');
        this.priceList = res.data.rows;
        this.total = res.data.total;
      });
      let formData = new FormData();
      formData.append('type', '乡镇');
      userService.getTownDic(formData).then(res => {
        res.data.rows.map(item => {
          this.townOptions.push(item.text);
        });
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
    // 查询
    handleFilter() {
      if (this.timeRange !== '' && this.timeRange !== null) {
        this.listQuery.startTime = this.$moment(new Date(this.timeRange[0])).format('YYYY-MM-DD HH:mm:ss');
        this.listQuery.endTime = this.$moment(new Date(this.timeRange[1])).format('YYYY-MM-DD HH:mm:ss');
      }
      if (this.timeRange === null) {
        this.listQuery.startTime = '';
        this.listQuery.endTime = '';
      }
      this.listQuery.page = 1;
      this.getAllList();
    },
    //全选框事件
    checkSelect(data) {
      data.forEach(item => {
        this.ids.push(item.id);
      });
    },
    async handleBatchRemove() {
      if (this.ids.length === 0) return this.$message.warning('请先选中要删除的价格信息');
      const confirmResult = await this.$confirm('此操作将删除选中价格信息,是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).catch(err => err);
      if (confirmResult !== 'confirm') return this.$message.info('已经取消删除');
      this.ids.forEach((id,index) => {
        userService.delPriceInfoById(id).then(res => {
          if (res.status !== 200) return this.$message.error('删除失败');
          this.$message.success('删除成功');
          this.ids.splice(index, 1);
          this.getAllList();
        });
      });
    },
    // 设定表格高度
    setTableHeight() {
      let h = document.documentElement.clientHeight || document.body.clientHeight;
      this.curHeight = h - 280;
    },
    getDic() {
      let params = {
        type: this.form.productCategory
      };
      userService.getProductNameDicByType(params).then(res => {
        if (res.status !== 200) return this.$message.error('获取失败');
        this.productList = res.data.rows;
        this.options = this.productList.map(item => {
          return { value: `${item.text}`, label: `${item.text}` };
        });
        this.form.productName = this.options[0].value;
      });
    },
    // 新增
    addRow() {
      this.flag = 'add';
      this.form.offerTime = this.$moment(new Date()).format('YYYY-MM-DD HH:mm:ss');
      this.form.productCategory = this.productOptions[0];
      this.getDic();
      this.dialogShow = true;
    },
    //Excel 导入
    importByExcel() {
      this.fileList = [];
      this.dialogVisible = true;
    },
    // 编辑
    editRow(row) {
      this.form = row;
      this.flag = 'edit';
      this.dialogShow = true;
    },
    // 删除
    deleteRow(row) {
      // 弹出删除确认框
      this.$confirm('此操作将删除一条价格行情信息, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      })
        .then(() => {
          this.listLoading = true;
          this.delPriceInfo(row.id);
        })
        .catch(() => {
          this.$message({ type: 'info', message: '已取消删除' });
        });
    },
    // 关闭对话框
    closeDialog() {
      this.row = {};
      this.$refs.row.clearValidate();
      this.getAllList();
    },
    // 提交数据
    submitRow() {
      if (this.flag == 'add') {
        this.$refs.row.validate(async valid => {
          if (!valid) return this.$message.error('信息填写不完整或不准确，请检查再提交！');

          this.form.offerTime = this.$moment(new Date()).format('YYYY-MM-DD HH:mm:ss');

          userService.addPriceInfo(this.form).then(res => {
            if (res.status !== 200) return this.$message.error('失败');
            this.$message.success('新增一条价格行情成功');
            // 隐藏添加价格行情的对话框
            this.dialogShow = false;
            // 重新获取价格行情列表
            this.getAllList();
          });
        });
      } else if (this.flag == 'edit') {
        this.editPriceInfoSubmit();
      }
    },
    //编辑价格行情的提交
    editPriceInfoSubmit() {
      this.form.offerTime = this.$moment(new Date(this.form.offerTime)).format('YYYY-MM-DD HH:mm:ss');
      this.$refs.row.validate(async valid => {
        if (!valid) return this.$message.error('信息填写不完整或不准确，请检查再提交！');
        userService.updatePriceInfo(this.form).then(res => {
          if (res.status !== 200) return this.$message.error('更新失败');
          // 关闭对话框
          this.dialogShow = false;
          // 刷新数据列表
          this.getAllList();
          // 提醒更新成功
          this.$message.success('更新成功');
        });
      });
    },
    delPriceInfo(id) {
      userService.delPriceInfoById(id).then(res => {
        if (res.status !== 200) return this.$message.error('删除失败');
        // 关闭对话框
        this.dialogShow = false;
        // 刷新数据列表
        this.getAllList();
        // 提醒删除成功
        this.$message.success('删除成功');
      });
    }
  }
};
</script>

<style lang="less" scoped></style>
