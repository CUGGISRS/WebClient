<template>
  <div>
    <!-- 面包屑导航区域 -->
    <el-breadcrumb separator-class="el-icon-arrow-right">
      <el-breadcrumb-item>首页</el-breadcrumb-item>
      <el-breadcrumb-item>企业信息审核</el-breadcrumb-item>
    </el-breadcrumb>
    <!-- 卡片视图区域 -->
    <el-card>
      <!-- 查询区域 -->
      <div class="query-condition">
        <div class="query-item">
          <span>企业名称:</span>
          <el-input v-model="params.name" clearable style="width: 240px;" placeholder="请输入企业名称"></el-input>
        </div>
        <el-button size="small" type="primary" icon="el-icon-search" @click="searchRow">查询</el-button>
      </div>
      <!-- 表格显示区域 -->
      <el-table
        :data="rows"
        border
        stripe
        fit
        highlight-current-row
        :row-style="{ height: '5px' }"
        :cell-style="{ padding: '5px 0' }"
        :height="curHeight"
      >
        <el-table-column align="center" label="序号" width="80">
          <template slot-scope="scope">
            {{ (currentPage - 1) * pageSize + scope.$index + 1 }}
          </template>
        </el-table-column>
        <el-table-column align="center" label="操作" width="100">
          <template slot-scope="scope">
            <el-button size="small" type="warning" icon="el-icon-view" @click="showRow(scope.row)">审核</el-button>
          </template>
        </el-table-column>
        <el-table-column align="center" label="企业名称" min-width="200">
          <template slot-scope="scope">
            {{ scope.row.name }}
          </template>
        </el-table-column>
        <el-table-column align="center" label="组织形式" width="160">
          <template slot-scope="scope">
            {{ getDictionaryName2(scope.row.organizationForm) }}
          </template>
        </el-table-column>
        <el-table-column align="center" label="主体属性" width="160">
          <template slot-scope="scope">
            {{ getDictionaryName3(scope.row.subjectAttribute) }}
          </template>
        </el-table-column>
        <el-table-column align="center" label="企业类型" width="120">
          <template slot-scope="scope">
            {{ getDictionaryName(scope.row.businessType) }}
          </template>
        </el-table-column>
        <el-table-column align="center" label="行政区规划" width="140">
          <template slot-scope="scope">
            {{ getDictionaryName4(scope.row.district) }}
          </template>
        </el-table-column>
        <el-table-column align="center" label="统一社会信用代码" width="200">
          <template slot-scope="scope">
            {{ scope.row.socialCode }}
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
    <!-- 审核对话框 -->
    <el-dialog v-cloak title="企业资质信息" @close="closeDialog" center :visible.sync="dialogShow" width="1000px">
      <el-form :model="row" label-width="150px" class="row-form">
        <el-tabs type="border-card" v-model="activeName">
          <el-tab-pane label="基本信息" :style="curHeight2" name="first">
            <el-row>
              <el-col :span="12">
                <el-row>
                  <el-col :span="24">
                    <el-form-item label="企业名称:">
                      <el-input v-model="row.name" readonly></el-input>
                    </el-form-item>
                  </el-col>
                </el-row>
                <el-row>
                  <el-col :span="24">
                    <el-form-item label="企业类型:">
                      <el-select disabled v-model="row.businessType" readonly>
                        <el-option
                          v-for="item in businessTypeOptions"
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
                  <el-col :span="24">
                    <el-form-item label="统一社会信用代码:">
                      <el-input v-model="row.socialCode" readonly></el-input>
                    </el-form-item>
                  </el-col>
                </el-row>
                <el-row>
                  <el-col :span="24">
                    <el-form-item label="行政区规划:">
                      <el-select v-model="row.district" readonly>
                        <el-option
                          v-for="item in districtOptions"
                          :key="item.code"
                          :label="item.name"
                          :value="item.code"
                        >
                        </el-option>
                      </el-select>
                    </el-form-item>
                  </el-col>
                </el-row>
              </el-col>
              <el-col :span="12">
                <el-form-item label="企业形象图:">
                  <img :src="imageUrl" class="avatar" />
                </el-form-item>
              </el-col>
            </el-row>

            <el-row>
              <el-col :span="12">
                <el-form-item label="组织形式:">
                  <el-select v-model="row.organizationForm" readonly>
                    <el-option
                      v-for="item in organizationFormOptions"
                      :key="item.code"
                      :label="item.name"
                      :value="item.code"
                    >
                    </el-option>
                  </el-select>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="主体属性:">
                  <el-select v-model="row.subjectAttribute" readonly>
                    <el-option
                      v-for="item in subjectAttributeOptions"
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
                <el-form-item label="注册地址:">
                  <el-input v-model="row.registerAddress" readonly></el-input>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="企业成立日期:">
                  <el-date-picker v-model="row.establishDate" type="date" readonly style="width: 100%">
                  </el-date-picker>
                </el-form-item>
              </el-col>
            </el-row>

            <el-row>
              <el-col :span="12">
                <el-form-item label="注册资金(万元):">
                  <el-input v-model="row.registerMoney" readonly></el-input>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="联系人:">
                  <el-input v-model="row.contactPerson" readonly></el-input>
                </el-form-item>
              </el-col>
            </el-row>

            <el-row>
              <el-col :span="12">
                <el-form-item label="联系人手机号:">
                  <el-input v-model="row.phone" readonly></el-input>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="电子邮箱:">
                  <el-input v-model="row.mailbox" readonly></el-input>
                </el-form-item>
              </el-col>
            </el-row>

            <el-row>
              <el-col :span="12">
                <el-form-item label="法定代表人姓名:">
                  <el-input v-model="row.deputyName" readonly></el-input>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="法定代表人身份证号:">
                  <el-input v-model="row.deputyIdCard" readonly></el-input>
                </el-form-item>
              </el-col>
            </el-row>

            <el-row>
              <el-col :span="12">
                <el-form-item label="法定代表人联系电话:">
                  <el-input v-model="row.deputyPhone" readonly></el-input>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="邮政编码:">
                  <el-input v-model="row.postcode" readonly></el-input>
                </el-form-item>
              </el-col>
            </el-row>

            <el-row>
              <el-col :span="24">
                <el-form-item label="经纬度:">
                  <el-input v-model="row.longitude" readonly style="width: 147px;"></el-input>
                  <span> - </span>
                  <el-input v-model="row.latitude" readonly style="width: 147px;"></el-input>
                </el-form-item>
              </el-col>
            </el-row>
          </el-tab-pane>
          <el-tab-pane label="营业执照" :style="curHeight2" name="second">
            <div class="upload-div">
              <div class="upload-div-list" v-for="(item, index) of imgBusinessLicenseLists" :key="index">
                <el-image :src="baseUrl + item.url" :preview-src-list="srcList"></el-image>
              </div>
            </div>
          </el-tab-pane>
          <el-tab-pane label="法人身份证照片（请上传身份证正面照片和反面照片）" :style="curHeight2" name="third">
            <div class="upload-div">
              <div class="upload-div-list" v-for="(item, index) of imgCorporateIdentityCardLists" :key="index">
                <el-image :src="baseUrl + item.url" :preview-src-list="srcList2"></el-image>
              </div>
            </div>
          </el-tab-pane>
        </el-tabs>

        <span class="t-btn-user">
          <el-button type="danger" @click="failRow(row.id, row.phone)">未通过</el-button>
          <el-button type="success" @click="successRow">通 过</el-button>
        </span>
      </el-form>
    </el-dialog>
  </div>
</template>

<script>
export default {
  data() {
    return {
      // 参数条件
      params: {
        page: null,
        limit: null,
        name: null,
        status: 0,
      },
      // 表单
      rows: [],
      row: {},
      // 分页
      currentPage: 1,
      total: 0,
      pageSize: 20,
      // 表单高度
      curHeight: 0,
      // 卡片高度
      curHeight2: {
        height: '',
        overflow: 'auto',
      },
      // 对话框
      dialogShow: false,
      // 活跃tab栏
      activeName: 'first',
      // 多选框
      businessTypeOptions: [],
      organizationFormOptions: [],
      subjectAttributeOptions: [],
      districtOptions: [],
      // 预览图片src列表
      srcList: [],
      srcList2: [],
      imageUrl: '',
      baseUrl: this.$baseImgUrl,
      imgBusinessLicenseLists: [],
      imgCorporateIdentityCardLists: [],
    };
  },
  created() {
    this.getBusinessTypeDictionary();
    this.getOrganizationFormDictionary();
    this.getSubjectAttributeDictionary();
    this.getDistrictDictionary();
    this.getAllList();
    this.setTableHeight();
    window.onresize = () => {
      this.setTableHeight();
    };
  },
  methods: {
    // 获取数据字典
    async getBusinessTypeDictionary() {
      const { data: res } = await this.$http.get('comSys/DataDictionary/getAllMayToCondition', {
        params: {
          type: '企业类型',
          remark: 'X2',
        },
      });
      if (res.status !== 200) {
        return this.$message({
          duration: 1000,
          type: 'error',
          message: '获取企业类型失败！',
        });
      }
      this.businessTypeOptions = res.data;
    },
    async getOrganizationFormDictionary() {
      const { data: res } = await this.$http.get('comSys/DataDictionary/getAllMayToCondition', {
        params: {
          type: '企业组织形式',
          remark: 'X2',
        },
      });
      if (res.status !== 200) {
        return this.$message({
          duration: 1000,
          type: 'error',
          message: '获取企业组织形式失败！',
        });
      }
      this.organizationFormOptions = res.data;
    },
    async getSubjectAttributeDictionary() {
      const { data: res } = await this.$http.get('comSys/DataDictionary/getAllMayToCondition', {
        params: {
          type: '企业主体属性',
          remark: 'X2',
        },
      });
      if (res.status !== 200) {
        return this.$message({
          duration: 1000,
          type: 'error',
          message: '获取企业主体属性失败！',
        });
      }
      this.subjectAttributeOptions = res.data;
    },
    async getDistrictDictionary() {
      const { data: res } = await this.$http.get('comSys/DataDictionary/getAllMayToCondition', {
        params: { type: '镇乡' },
      });
      if (res.status !== 200) {
        return this.$message({
          duration: 1000,
          type: 'error',
          message: '获取行政区规划失败！',
        });
      }
      this.districtOptions = res.data;
    },
    // 获取全部的列表数据
    async getAllList() {
      this.params.page = this.currentPage;
      this.params.limit = this.pageSize;
      const { data: res } = await this.$http.get('zsSys/Enterprise/pageByCondition', { params: this.params });
      if (res.status !== 200) {
        return this.$message({
          duration: 1000,
          type: 'error',
          message: '获取企业数据失败！',
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
      this.curHeight = h - 330;
      this.curHeight2.height = h - 550 + 'px';
    },
    // 关闭对话框
    closeDialog() {
      this.row = {};
      this.imageUrl = '';
      this.imgBusinessLicenseLists = [];
      this.imgCorporateIdentityCardLists = [];
      this.srcList = [];
      this.srcList2 = [];
      this.activeName = 'first';
      this.getAllList();
    },
    // 审核数据
    showRow(row) {
      this.row = row;
      if (row.enterpriseImageList.length !== 0) {
        this.imageUrl = this.baseUrl + row.enterpriseImageList[0].url;
      }
      if (row.businessLicenseList.length !== 0) {
        this.imgBusinessLicenseLists = row.businessLicenseList;
        for (let i = 0; i < this.imgBusinessLicenseLists.length; i++) {
          this.srcList.push(this.baseUrl + this.imgBusinessLicenseLists[i].url);
        }
      }
      if (row.deputyIdCardPhotoList.length !== 0) {
        this.imgCorporateIdentityCardLists = row.deputyIdCardPhotoList;
        for (let i = 0; i < this.imgCorporateIdentityCardLists.length; i++) {
          this.srcList2.push(this.baseUrl + this.imgCorporateIdentityCardLists[i].url);
        }
      }
      this.dialogShow = true;
    },
    // 查询数据
    searchRow() {
      this.currentPage = 1;
      this.getAllList();
    },
    // 审核通过
    successRow() {
      this.$confirm('此操作将通过注册请求, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning',
      })
        .then(async () => {
          this.row.status = 1;
          let fd = new FormData();
          for (let key in this.row) {
            let value = this.row[key];
            // 过滤
            if (key === 'enterpriseImageList') {
              // fd.append(key, '');
            } else if (key === 'businessLicenseList') {
              // fd.append(key, '');
            } else if (key === 'deputyIdCardPhotoList') {
              // fd.append(key, '');
            } else {
              if (value !== null) {
                fd.append(key, value);
              }
            }
          }
          const { data: res } = await this.$http.put('zsSys/Enterprise/updateDataAndFile', fd);
          if (res.status !== 200) return this.$message.error('修改失败！');
          this.$message({
            type: 'success',
            message: '通过注册请求',
          });
          this.dialogShow = false;
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
    // 审核未通过
    failRow(id, phone) {
      this.$prompt('请输入审核未通过理由', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        inputType: 'textarea',
        inputValidator: this.validateInformation,
      })
        .then(async ({ value }) => {
          const { data: res } = await this.$http.get(
            'zsSys/Enterprise/sendSms?phone=' + `${phone}` + '&&reason=' + `${value}`
          );
          if (res.status !== 200) return this.$message.error('审核未通过失败！');
          const { data: res1 } = await this.$http.delete('zsSys/Enterprise/' + id);
          if (res1.status == 200) {
            this.getAllList();
          }
          this.dialogShow = false;
          this.$message.success('审核完成！');
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
    // 审核未通过理由规则验证
    validateInformation(value) {
      if (!value) {
        return '审核未通过理由不能为空！';
      }
      return true;
    },
    // 根据code转换成name
    getDictionaryName(code) {
      for (let i = 0; i < this.businessTypeOptions.length; i++) {
        if (code === this.businessTypeOptions[i].code) {
          return this.businessTypeOptions[i].name;
        }
      }
    },
    getDictionaryName2(code) {
      for (let i = 0; i < this.organizationFormOptions.length; i++) {
        if (code === this.organizationFormOptions[i].code) {
          return this.organizationFormOptions[i].name;
        }
      }
    },
    getDictionaryName3(code) {
      for (let i = 0; i < this.subjectAttributeOptions.length; i++) {
        if (code === this.subjectAttributeOptions[i].code) {
          return this.subjectAttributeOptions[i].name;
        }
      }
    },
    getDictionaryName4(code) {
      for (let i = 0; i < this.districtOptions.length; i++) {
        if (code === this.districtOptions[i].code) {
          return this.districtOptions[i].name;
        }
      }
    },
  },
};
</script>

<style scoped lang="less">
// 按钮居中
.t-btn-user {
  display: flex;
  justify-content: center;
  margin-top: 15px;
}

// 企业形象图
.avatar {
  width: 220px;
  height: 220px;
}
</style>
