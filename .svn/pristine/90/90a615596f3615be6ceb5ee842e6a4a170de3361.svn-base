<template>
  <div>
    <!-- 面包屑导航区域 -->
    <el-breadcrumb separator-class="el-icon-arrow-right">
      <el-breadcrumb-item>首页</el-breadcrumb-item>
      <el-breadcrumb-item>企业基本信息</el-breadcrumb-item>
    </el-breadcrumb>
    <!-- 卡片视图区域 -->
    <el-card>
      <el-form :model="row" label-width="160px" class="row-form" :rules="rules" ref="row" style="width: 90%">
        <el-tabs type="border-card">
          <el-tab-pane label="基本信息" :style="curHeight">
            <el-row>
              <el-col :span="12">
                <el-row>
                  <el-col :span="24">
                    <el-form-item prop="name" label="企业名称:">
                      <el-input v-model="row.name" clearable maxlength="50" placeholder="由1-20个汉字组成"></el-input>
                    </el-form-item>
                  </el-col>
                </el-row>
                <el-row>
                  <el-col :span="24">
                    <el-form-item prop="businessType" label="企业类型:">
                      <el-select v-model="row.businessType" clearable placeholder="请选择企业类型">
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
                    <el-form-item prop="socialCode" label="统一社会信用代码:">
                      <el-input v-model="row.socialCode" clearable maxlength="50" placeholder="由18位组成"></el-input>
                    </el-form-item>
                  </el-col>
                </el-row>
                <el-row>
                  <el-col :span="24">
                    <el-form-item prop="district" label="行政区规划:">
                      <el-select v-model="row.district" clearable placeholder="请选择行政区">
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
              <el-col :span="12" class="user-upload">
                <el-form-item label="企业形象图:">
                  <el-upload
                    class="avatar-uploader"
                    :action="updateUrl"
                    :show-file-list="false"
                    :http-request="httpRequest01"
                    :before-upload="beforeAvatarUpload"
                  >
                    <img v-if="imageUrl" :src="imageUrl" class="avatar" />
                    <i v-else class="el-icon-plus avatar-uploader-icon"></i>
                  </el-upload>
                </el-form-item>
              </el-col>
            </el-row>

            <el-row>
              <el-col :span="12">
                <el-form-item prop="organizationForm" label="组织形式:">
                  <el-select v-model="row.organizationForm" clearable placeholder="请选择组织形式">
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
                <el-form-item prop="subjectAttribute" label="主体属性:">
                  <el-select v-model="row.subjectAttribute" clearable placeholder="请选择主体属性">
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
                <el-form-item prop="registerAddress" label="注册地址:">
                  <el-input
                    v-model="row.registerAddress"
                    clearable
                    maxlength="255"
                    placeholder="营业执照上的地址"
                  ></el-input>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item prop="establishDate" label="企业成立日期:">
                  <el-date-picker
                    value-format="yyyy-MM-dd"
                    v-model="row.establishDate"
                    type="date"
                    clearable
                    style="width: 100%"
                    placeholder="选择企业成立日期"
                  >
                  </el-date-picker>
                </el-form-item>
              </el-col>
            </el-row>

            <el-row>
              <el-col :span="12">
                <el-form-item prop="registerMoney" label="注册资金(万元):">
                  <el-input
                    v-model="row.registerMoney"
                    clearable
                    placeholder="请输入注册资金"
                    onkeyup="value=value.replace(/[^\d^\.]/g,'')"
                    @blur="registerMoneyChange"
                  >
                  </el-input>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item prop="contactPerson" label="联系人:">
                  <el-input
                    v-model="row.contactPerson"
                    clearable
                    maxlength="50"
                    placeholder="请输入联系人姓名"
                  ></el-input>
                </el-form-item>
              </el-col>
            </el-row>

            <el-row>
              <el-col :span="12">
                <el-form-item prop="phone" label="联系人手机号:">
                  <el-input
                    v-model="row.phone"
                    clearable
                    maxlength="50"
                    placeholder="请输入联系人手机号"
                    onkeyup="value=value.replace(/[^\d]/g,'')"
                    @blur="phoneChange"
                  >
                  </el-input>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item prop="mailbox" label="电子邮箱:">
                  <el-input
                    v-model="row.mailbox"
                    clearable
                    maxlength="50"
                    placeholder="请输入正确格式的邮箱"
                  ></el-input>
                </el-form-item>
              </el-col>
            </el-row>

            <el-row>
              <el-col :span="12">
                <el-form-item prop="deputyName" label="法定代表人姓名:">
                  <el-input
                    v-model="row.deputyName"
                    clearable
                    maxlength="50"
                    placeholder="请输入法定代表人姓名"
                  ></el-input>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item prop="deputyIdCard" label="法定代表人身份证号:">
                  <el-input
                    v-model="row.deputyIdCard"
                    clearable
                    maxlength="50"
                    placeholder="身份证号由18位组成"
                  ></el-input>
                </el-form-item>
              </el-col>
            </el-row>

            <el-row>
              <el-col :span="12">
                <el-form-item prop="deputyPhone" label="法定代表人联系电话:">
                  <el-input
                    v-model="row.deputyPhone"
                    clearable
                    maxlength="50"
                    placeholder="请输入法定代表人联系电话"
                    onkeyup="value=value.replace(/[^\d]/g,'')"
                    @blur="deputyPhoneChange"
                  >
                  </el-input>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item prop="postcode" label="邮政编码:">
                  <el-input
                    v-model="row.postcode"
                    clearable
                    maxlength="6"
                    placeholder="请输入邮政编码"
                    onkeyup="value=value.replace(/[^\d]/g,'')"
                    @blur="postcodeChange"
                  >
                  </el-input>
                </el-form-item>
              </el-col>
            </el-row>

            <el-row>
              <el-col :span="24">
                <el-form-item label="经纬度:" prop="longitude">
                  <el-form-item prop="longitude" style="float: left">
                    <el-input
                      v-model="row.longitude"
                      readonly
                      placeholder="请输入经度"
                      style="width: 244px;margin-right: 20px"
                      @focus="showMap"
                    ></el-input>
                  </el-form-item>
                  <el-form-item prop="latitude" style="float: left">
                    <el-input
                      v-model="row.latitude"
                      readonly
                      placeholder="请输入纬度"
                      style="width: 244px;"
                      @focus="showMap"
                    ></el-input>
                  </el-form-item>
                </el-form-item>
              </el-col>
            </el-row>
          </el-tab-pane>
          <el-tab-pane label="营业执照" :style="curHeight">
            <!-- 上传 -->
            <el-upload
              :action="updateUrl"
              multiple
              :http-request="httpRequest02"
              list-type="picture-card"
              :on-preview="handleBusinessLicensePreview"
              :on-remove="handleBusinessLicenseRemove"
              :before-upload="beforeAvatarUpload02"
              :on-change="handleChangeBusinessLicensePhoto"
              :file-list="fileBusinessLicenseLists"
            >
              <i class="el-icon-plus"></i>
            </el-upload>
            <!-- 营业执预览 -->
            <el-dialog :visible.sync="dialogBusinessLicenseVisible" width="70%" top="5vh">
              <img width="100%" :src="dialogBusinessLicenseImageUrl" alt="" />
            </el-dialog>
            <!-- 展示 -->
            <div class="upload-div">
              <div class="upload-div-list" v-for="(item, index) of imgBusinessLicenseLists" :key="index">
                <!-- <div class="upload-div-top">
                  <span class="upload-div-del" @click="fileBusinessLicenseDelIdPhoto(index, item.id)">X</span>
                </div> -->
                <el-image :src="baseUrl + item.url" :preview-src-list="srcList"></el-image>
              </div>
            </div>
          </el-tab-pane>
          <el-tab-pane label="法人身份证照片（请上传身份证正面照片和反面照片）" :style="curHeight">
            <!-- 上传 -->
            <el-upload
              :action="updateUrl"
              multiple
              :http-request="httpRequest03"
              list-type="picture-card"
              :on-preview="handleCorporateIdentityCardPreview"
              :on-remove="handleCorporateIdentityCardRemove"
              :before-upload="beforeAvatarUpload02"
              :on-change="handleChangeCorporateIdentityCardPhoto"
              :file-list="fileCorporateIdentityCardLists"
            >
              <i class="el-icon-plus"></i>
            </el-upload>
            <!-- 预览 -->
            <el-dialog :visible.sync="dialogCorporateIdentityCardVisible" width="70%" top="5vh">
              <img width="100%" :src="dialogCorporateIdentityCardImageUrl" alt="" />
            </el-dialog>
            <!-- 展示 -->
            <div class="upload-div">
              <div class="upload-div-list" v-for="(item, index) of imgCorporateIdentityCardLists" :key="index">
                <div class="upload-div-top">
                  <span class="upload-div-del" @click="fileCorporateIdentityCardDelIdPhoto(index, item.id)">X</span>
                </div>
                <el-image :src="baseUrl + item.url" :preview-src-list="srcList2"></el-image>
              </div>
            </div>
          </el-tab-pane>
        </el-tabs>

        <span class="t-btn-user">
          <el-button type="warning" @click="dialogIoTVisible = true">物联网关联</el-button>
          <el-button type="info" @click="getList">重 置</el-button>
          <el-button type="success" @click="submitRow">提 交</el-button>
        </span>
      </el-form>
    </el-card>
    <!-- 地图显示 -->
    <el-dialog v-cloak title="经纬度" center :visible.sync="dialogMapVisible" width="50%">
      <div class="container">
        <div class="search-box">
          <input v-model="searchKey" type="search" id="search" />
          <button @click="searchByHand">搜索</button>
          <div class="tip-box" id="searchTip"></div>
        </div>
        <el-amap
          class="amap-box"
          :amap-manager="amapManager"
          :vid="'amap-vue'"
          :zoom="zoom"
          :center="center"
          :plugin="plugin"
          :events="events"
        >
          <!-- 标记 -->
          <el-amap-marker v-for="(marker, index) in markers" :position="marker" :key="index"></el-amap-marker>
        </el-amap>
      </div>

      <span slot="footer">
        <el-button type="info" @click="dialogMapVisible = false">取 消</el-button>
        <el-button type="success" @click="getCoordinate">确 定</el-button>
      </span>
    </el-dialog>
    <!-- 物联网关联 -->
    <el-dialog v-cloak title="物联网账号" center :visible.sync="dialogIoTVisible" width="1000px">
      <!-- 按钮操作区域 -->
      <div style="margin-top: -20px;">
        <el-button size="small" type="success" icon="el-icon-plus" @click="addIoTRow">新增</el-button>
      </div>
      <!-- 表格显示区域 -->
      <el-table
        :data="ioTRows"
        ref="myTable"
        border
        stripe
        fit
        highlight-current-row
        :cell-style="{ padding: '5px 0' }"
        :height="curHeight2"
      >
        <el-table-column align="center" label="序号" width="60">
          <template slot-scope="scope">
            {{ (currentPage - 1) * pageSize + scope.$index + 1 }}
          </template>
        </el-table-column>
        <el-table-column align="center" label="操作" width="200">
          <template slot-scope="scope">
            <el-button type="primary" size="mini" plain icon="el-icon-edit-outline" @click="editIoTRow(scope.row)"
              >编辑</el-button
            >
            <el-button type="danger" size="mini" plain icon="el-icon-delete" @click="deleteIoTRow(scope.row.id)"
              >删除</el-button
            >
          </template>
        </el-table-column>
        <el-table-column align="center" label="关联编号" min-width="160">
          <template slot-scope="scope">
            {{ scope.row.name }}
          </template>
        </el-table-column>
        <el-table-column align="center" label="物联网账号用户名" width="160">
          <template slot-scope="scope">
            {{ scope.row.username }}
          </template>
        </el-table-column>
        <el-table-column align="center" label="物联网账号密码" width="160">
          <template slot-scope="scope">
            {{ scope.row.password }}
          </template>
        </el-table-column>
        <el-table-column align="center" label="物联网设备供应商" width="200">
          <template slot-scope="scope">
            {{ getDictionaryName(scope.row.type) }}
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
    </el-dialog>
    <el-dialog
      v-cloak
      title="物联网账号关联"
      @close="closeDialog"
      center
      :visible.sync="dialogIoTEditVisible"
      width="1000px"
      class="row-form"
    >
      <el-form :model="ioTRow" label-width="160px" class="demo-ruleForm" :rules="rules2" ref="ioTRow">
        <el-row>
          <el-col :span="12">
            <el-form-item label="关联编号:" prop="name">
              <el-input v-model="ioTRow.name" clearable maxlength="50" placeholder="请输入关联编号"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="物联网设备供应商:" prop="type">
              <el-select v-model="ioTRow.type" clearable placeholder="请选择物联网设备供应商">
                <el-option v-for="item in ioTOptions" :key="item.code" :label="item.name" :value="item.code">
                </el-option>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row>
          <el-col :span="12">
            <el-form-item label="物联网账号用户名:" prop="username">
              <el-input v-model="ioTRow.username" clearable maxlength="50" placeholder="请输入用户名"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="物联网账号密码:" prop="password">
              <el-input
                v-model="ioTRow.password"
                show-password
                clearable
                maxlength="50"
                placeholder="请输入密码"
              ></el-input>
            </el-form-item>
          </el-col>
        </el-row>

        <span class="t-btn">
          <el-button type="info" @click="dialogIoTEditVisible = false">取 消</el-button>
          <el-button type="success" @click="submitIoTRow">提 交</el-button>
        </span>
      </el-form>
    </el-dialog>
  </div>
</template>

<script>
import { checkIdNumber, checkPhoneNumber, Debounce } from '../../assets/js/tool';
import { AMapManager, lazyAMapApiLoaderInstance } from 'vue-amap';
let amapManager = new AMapManager();
export default {
  data() {
    let self = this;
    var validatePhone = (rule, value, callback) => {
      if (value && !checkPhoneNumber(value)) {
        callback(new Error('手机号不正确!'));
      } else {
        callback();
      }
    };
    var validateIdnumber = (rule, value, callback) => {
      if (value && !checkIdNumber(value)) {
        callback(new Error('身份证号码不正确!'));
      } else {
        callback();
      }
    };
    var validateName = (rule, value, callback) => {
      if (value && this.ioTRows.length > 0) {
        this.ioTRows.map((item) => {
          if (item.name === value) {
            callback(new Error('关联编号不能相同!'));
          } else {
            callback();
          }
        });
      } else {
        callback();
      }
    };
    return {
      // 卡片高度
      curHeight: {
        height: '',
        overflow: 'auto',
      },
      curHeight2: 0,
      // 地图
      dialogMapVisible: false,
      address: null,
      // 修改or新增flag
      flag: '',
      // 物联网
      params: {
        enterpriseId: window.sessionStorage.getItem('enterpriseId'),
        page: null,
        limit: null,
      },
      dialogIoTVisible: false,
      dialogIoTEditVisible: false,
      ioTRows: [],
      ioTRow: {},
      // 分页
      currentPage: 1,
      total: 0,
      pageSize: 20,
      // 搜索关键词
      searchKey: '',
      // 地图管理对象
      amapManager,
      markers: [],
      searchOption: {
        city: '全国',
        citylimit: true,
      },
      // 地图中心坐标
      center: [121.329402, 31.228667],
      // 初始化地图显示层级
      zoom: 17,
      lng: 0,
      lat: 0,
      loaded: false,
      events: {
        init() {
          lazyAMapApiLoaderInstance.load().then(() => {
            self.initSearch();
          });
        },
        // 点击获取地址的数据
        click(e) {
          // console.log(e)
          self.markers = [];
          let { lng, lat } = e.lnglat;
          self.lng = lng;
          self.lat = lat;
          self.center = [lng, lat];
          self.markers.push([lng, lat]);
          // 这里通过高德 SDK 完成。
          let geocoder = new AMap.Geocoder({
            radius: 1000,
            extensions: 'all',
          });
          geocoder.getAddress([lng, lat], function(status, result) {
            if (status === 'complete' && result.info === 'OK') {
              if (result && result.regeocode) {
                // console.log(result.regeocode.formattedAddress);
                self.address = result.regeocode.formattedAddress;
                self.searchKey = result.regeocode.formattedAddress;
                self.$nextTick();
              }
            }
          });
        },
      },
      // 一些工具插件
      plugin: [
        {
          // 定位
          pName: 'Geolocation',
          events: {
            init(o) {
              // o是高德地图定位插件实例
              o.getCurrentPosition((status, result) => {
                if (result && result.position) {
                  // 设置经度
                  // self.lng = result.position.lng;
                  // 设置维度
                  // self.lat = result.position.lat;
                  // 设置坐标
                  self.center = [self.lng, self.lat];
                  self.markers.push([self.lng, self.lat]);
                  // load
                  self.loaded = true;
                  // 页面渲染好后
                  self.$nextTick();
                }
              });
            },
          },
        },
        {
          // 工具栏
          pName: 'ToolBar',
        },
        {
          // 搜索
          pName: 'PlaceSearch',
        },
      ],
      // 表单
      row: {},
      // 图片文件上传
      updateUrl: '#',
      baseUrl: this.$baseImgUrl,
      imageUrl: '',
      // 预览src列表
      srcList: [],
      srcList2: [],
      // 上传文件
      multfileImg: null,
      // 营业执照
      picBusinessLicenseListPhoto: [],
      dialogBusinessLicenseImageUrl: '',
      dialogBusinessLicenseVisible: false,
      imgBusinessLicenseLists: [],
      // 上传文件
      fileBusinessLicenseLists: [],
      // 法人身份证照片
      picCorporateIdentityCardListPhoto: [],
      dialogCorporateIdentityCardImageUrl: '',
      dialogCorporateIdentityCardVisible: false,
      imgCorporateIdentityCardLists: [],
      // 上传文件
      fileCorporateIdentityCardLists: [],
      // 多选框
      businessTypeOptions: [],
      organizationFormOptions: [],
      subjectAttributeOptions: [],
      districtOptions: [],
      ioTOptions: [],
      // 验证规则
      rules: {
        name: [
          { required: true, message: '请输入企业名称', trigger: 'blur' },
          { min: 1, max: 20, message: '长度在 1 到 20个字符', trigger: 'blur' },
        ],
        businessType: [{ required: true, message: '请选择企业类型', trigger: 'blur' }],
        socialCode: [
          { required: true, message: '请输入统一社会信用代码', trigger: 'blur' },
          { min: 18, max: 18, message: '长度为18个字符', trigger: 'blur' },
        ],
        district: [{ required: true, message: '请选择行政区规划', trigger: 'blur' }],
        // registerAddress: [{ required: true, message: '请输入注册地址', trigger: 'blur' }],
        organizationForm: [{ required: true, message: '请选择组织形式', trigger: 'blur' }],
        subjectAttribute: [{ required: true, message: '请选择主体属性', trigger: 'blur' }],
        // establishDate: [{ required: true, message: '请选择企业成立日期', trigger: 'blur' }],
        // registerMoney: [{ required: true, message: '请输入注册资金', trigger: 'blur' }],
        contactPerson: [{ required: true, message: '请输入联系人', trigger: 'blur' }],
        phone: [
          { required: true, message: '请输入联系人手机号', trigger: 'blur' },
          { required: true, validator: validatePhone, trigger: 'blur' },
        ],
        // mailbox: [{ required: true, message: '请输入电子邮箱', trigger: 'blur' }],
        deputyName: [{ required: true, message: '请输入法定代表人姓名', trigger: 'blur' }],
        deputyIdCard: [
          // { required: true, message: '请输入法定代表人身份证号', trigger: 'blur' },
          { validator: validateIdnumber, trigger: 'blur' },
        ],
        deputyPhone: [
          // { required: true, message: '请输入法定代表人联系电话', trigger: 'blur' },
          { validator: validatePhone, trigger: 'blur' },
        ],
        // postcode: [
        // 	{ required: true, message: '请输入邮政编码', trigger: 'blur' },
        // 	{ min: 6, max: 6, message: '长度为6个数字', trigger: 'blur' }
        // ],
        // longitude: [{ required: true, message: '请选择经度', trigger: 'change' }],
        // latitude: [{ required: true, message: '请选择纬度', trigger: 'change' }]
      },
      rules2: {
        name: [
          { required: true, message: '请输入关联编号', trigger: 'blur' },
          { validator: validateName, trigger: 'blur' },
        ],
        type: [{ required: true, message: '请选择物联网设备供应商', trigger: 'blur' }],
        username: [{ required: true, message: '请输入用户名', trigger: 'blur' }],
        password: [{ required: true, message: '请输入密码', trigger: 'blur' }],
      },
    };
  },
  created() {
    this.getBusinessTypeDictionary();
    this.getOrganizationFormDictionary();
    this.getSubjectAttributeDictionary();
    this.getDistrictDictionary();
    this.getIoTDictionary();
    this.getList();
    this.getIoTList();
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
    async getIoTDictionary() {
      const { data: res } = await this.$http.get('comSys/DataDictionary/getAllMayToCondition', {
        params: { type: '物联网供应' },
      });
      if (res.status !== 200) {
        return this.$message({
          duration: 1000,
          type: 'error',
          message: '物联网设备供应商失败！',
        });
      }
      this.ioTOptions = res.data;
    },
    // 地图显示
    showMap() {
      this.dialogMapVisible = true;
      this.lng = this.row.longitude;
      this.lat = this.row.latitude;
    },
    // 获取经纬度
    getCoordinate() {
      // 使用 Vue.set(object, propertyName, value) 方法向嵌套对象添加响应式属性
      this.$set(this.row, 'longitude', this.lng);
      this.$set(this.row, 'latitude', this.lat);
      this.dialogMapVisible = false;
    },
    initSearch() {
      let vm = this;
      let map = this.amapManager.getMap();
      AMapUI.loadUI(['misc/PoiPicker'], function(PoiPicker) {
        var poiPicker = new PoiPicker({
          input: 'search',
          placeSearchOptions: {
            map: map,
            pageSize: 10,
          },
          suggestContainer: 'searchTip',
          searchResultsContainer: 'searchTip',
        });
        vm.poiPicker = poiPicker;
        // 监听poi选中信息
        poiPicker.on('poiPicked', function(poiResult) {
          // console.log(poiResult)
          let source = poiResult.source;
          let poi = poiResult.item;
          if (source !== 'search') {
            poiPicker.searchByKeyword(poi.name);
          } else {
            poiPicker.clearSearchResults();
            vm.markers = [];
            let lng = poi.location.lng;
            let lat = poi.location.lat;
            let address = poi.cityname + poi.adname + poi.name;
            vm.center = [lng, lat];
            vm.markers.push([lng, lat]);
            vm.lng = lng;
            vm.lat = lat;
            vm.address = address;
            vm.searchKey = address;
          }
        });
      });
    },
    // 搜索
    searchByHand() {
      if (this.searchKey !== '') {
        this.poiPicker.searchByKeyword(this.searchKey);
      }
    },
    // 设定表格高度
    setTableHeight() {
      let h = document.documentElement.clientHeight || document.body.clientHeight;
      this.curHeight.height = h - 346 + 'px';
      this.curHeight2 = h - 500;
    },
    // 获取数据
    async getList() {
      this.row = {};
      this.srcList = [];
      this.srcList2 = [];
      this.fileBusinessLicenseLists = [];
      this.fileCorporateIdentityCardLists = [];
      this.imgBusinessLicenseLists = [];
      this.imgCorporateIdentityCardLists = [];
      const { data: res } = await this.$http.get('zsSys/Enterprise/' + window.sessionStorage.getItem('enterpriseId'));
      if (res.status !== 200) {
        return this.$message({
          duration: 1000,
          type: 'error',
          message: '获取企业基本信息失败！',
        });
      }
      this.row = res.data;
      this.row.delFileIds = [];
      if (res.data.enterpriseImageList && res.data.enterpriseImageList.length !== 0) {
        this.imageUrl = this.baseUrl + res.data.enterpriseImageList[0].url;
      }
      if (res.data.businessLicenseList && res.data.businessLicenseList.length !== 0) {
        this.imgBusinessLicenseLists = res.data.businessLicenseList;
        for (let i = 0; i < this.imgBusinessLicenseLists.length; i++) {
          this.srcList.push(this.baseUrl + this.imgBusinessLicenseLists[i].url);
        }
      }
      if (res.data.deputyIdCardPhotoList && res.data.deputyIdCardPhotoList.length !== 0) {
        this.imgCorporateIdentityCardLists = res.data.deputyIdCardPhotoList;
        for (let i = 0; i < this.imgCorporateIdentityCardLists.length; i++) {
          this.srcList2.push(this.baseUrl + this.imgCorporateIdentityCardLists[i].url);
        }
      }
      this.$nextTick(() => {
        this.$refs.row.clearValidate();
      });
    },
    async getIoTList() {
      this.params.page = this.currentPage;
      this.params.limit = this.pageSize;
      const { data: res } = await this.$http.get('zsSys/EnterpriseWlUser/pageByEId', { params: this.params });
      if (res.status !== 200) {
        return this.$message({
          duration: 1000,
          type: 'error',
          message: '获取物联网账号信息失败！',
        });
      }
      this.ioTRows = res.data.rows;
      this.total = res.data.total;
    },
    // 每页多少条
    handleSizeChange(val) {
      this.pageSize = val;
      this.getIoTList();
    },
    // 当前页
    handleCurrentChange(val) {
      this.currentPage = val;
      this.getIoTList();
    },
    // 关闭物联网对话框
    closeDialog() {
      this.ioTRow = {};
      this.getIoTList();
      this.$refs.ioTRow.clearValidate();
    },
    // 新增物联网账号
    addIoTRow() {
      this.dialogIoTEditVisible = true;
      this.flag = 'add';
    },
    // 修改物联网账号
    editIoTRow(row) {
      this.ioTRow = row;
      this.dialogIoTEditVisible = true;
      this.flag = 'edit';
    },
    // 删除物联网账号
    deleteIoTRow(id) {
      this.$confirm('此操作将删除选中的数据, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning',
      })
        .then(async () => {
          const { data: res } = await this.$http.delete('zsSys/EnterpriseWlUser/' + id);
          if (res.status !== 200) {
            return this.$message({
              duration: 2000,
              type: 'error',
              message: '删除物联网账号失败！',
            });
          }
          this.$message({
            duration: 2000,
            type: 'success',
            message: '删除物联网账号成功！',
          });
          await this.getIoTList();
          if (this.ioTRows.length === 0) {
            this.row.isViewpoint = 0;
            const { data: res } = await this.$http.put('zsSys/Enterprise', this.row);
            if (res.status !== 200) return this.$message.error('修改失败！');
          }
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
    submitIoTRow: Debounce(function() {
      this.$refs.ioTRow.validate(async (valid) => {
        if (!valid) return this.$message.error('信息填写不完整或不准确，请检查再提交！');
        this.ioTRow.enterpriseId = window.sessionStorage.getItem('enterpriseId');
        if (this.flag === 'add') {
          const { data: res } = await this.$http.post('zsSys/EnterpriseWlUser', this.ioTRow);
          if (res.status !== 200) {
            return this.$message({
              duration: 2000,
              type: 'error',
              message: '新增物联网账号失败！',
            });
          }
          await this.getIoTList();
          if (this.row.isViewpoint === 0 && this.ioTRows.length !== 0) {
            this.row.isViewpoint = 1;
            const { data: res } = await this.$http.put('zsSys/Enterprise', this.row);
            if (res.status !== 200) return this.$message.error('修改失败！');
          }
          this.dialogIoTEditVisible = false;
          this.$message({
            duration: 2000,
            type: 'success',
            message: '新增物联网账号成功！',
          });
        } else if (this.flag === 'edit') {
          const { data: res } = await this.$http.put('zsSys/EnterpriseWlUser', this.ioTRow);
          if (res.status !== 200) {
            return this.$message({
              duration: 2000,
              type: 'error',
              message: '修改物联网账号失败！',
            });
          }
          this.getIoTList();
          this.dialogIoTEditVisible = false;
          this.$message({
            duration: 2000,
            type: 'success',
            message: '修改物联网账号成功！',
          });
        }
      });
    }, 3000),
    // 处理图片
    httpRequest01(data) {
      let _this = this;
      let rd = new FileReader(); // 创建文件读取对象
      let file = data.file;
      rd.readAsDataURL(file); // 文件读取装换为base64类型
      rd.onloadend = function() {
        _this.imageUrl = this.result; // this指向当前方法onloadend的作用域
        _this.row.delFileIds.push(_this.row.enterpriseImageList[0].id);
      };
    },
    httpRequest02(data) {
      let _this = this;
      let rd = new FileReader(); // 创建文件读取对象
      let file = data.file;
      rd.readAsDataURL(file); // 文件读取装换为base64类型
      rd.onloadend = function() {
        _this.dialogBusinessLicenseImageUrl = this.result; // this指向当前方法onloadend的作用域
      };
    },
    httpRequest03(data) {
      let _this = this;
      let rd = new FileReader(); // 创建文件读取对象
      let file = data.file;
      rd.readAsDataURL(file); // 文件读取装换为base64类型
      rd.onloadend = function() {
        _this.dialogCorporateIdentityCardImageUrl = this.result; // this指向当前方法onloadend的作用域
      };
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
    beforeAvatarUpload02(file) {
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
    // 营业执照图片
    handleBusinessLicensePreview(file) {
      this.dialogBusinessLicenseImageUrl = file.url;
      this.dialogBusinessLicenseVisible = true;
    },
    handleBusinessLicenseRemove(file, fileLists) {
      for (let i in this.picBusinessLicenseListPhoto) {
        if (this.picBusinessLicenseListPhoto[i].key === file.uid) {
          this.picBusinessLicenseListPhoto.splice(i, 1);
        }
      }
      this.fileBusinessLicenseLists = fileLists;
    },
    handleChangeBusinessLicensePhoto(file, fileList) {
      this.fileBusinessLicenseLists = fileList;
    },
    // 删除已上传的图片
    fileBusinessLicenseDelIdPhoto(index, id) {
      this.$confirm('此操作将永久删除该图片, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning',
      })
        .then(() => {
          this.row.delFileIds.push(id);
          this.imgBusinessLicenseLists.splice(index, 1);
        })
        .catch(() => {
          this.$message({
            duration: 1000,
            type: 'info',
            message: '已取消操作',
          });
        });
    },
    // 法人身份证照片
    handleCorporateIdentityCardPreview(file) {
      this.dialogCorporateIdentityCardImageUrl = file.url;
      this.dialogCorporateIdentityCardVisible = true;
    },
    handleCorporateIdentityCardRemove(file, fileLists) {
      for (let i in this.picCorporateIdentityCardListPhoto) {
        if (this.picCorporateIdentityCardListPhoto[i].key === file.uid) {
          this.picCorporateIdentityCardListPhoto.splice(i, 1);
        }
      }
      this.fileCorporateIdentityCardLists = fileLists;
    },
    handleChangeCorporateIdentityCardPhoto(file, fileList) {
      this.fileCorporateIdentityCardLists = fileList;
    },
    // 删除已上传的图片
    fileCorporateIdentityCardDelIdPhoto(index, id) {
      this.$confirm('此操作将永久删除该图片, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning',
      })
        .then(() => {
          this.row.delFileIds.push(id);
          this.imgCorporateIdentityCardLists.splice(index, 1);
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
      this.$confirm('此操作将修改企业基本信息, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning',
      })
        .then(() => {
          this.$refs.row.validate(async (valid) => {
            if (!valid) return this.$message.error('信息填写不完整或不准确，请检查再提交！');
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

            //图片文件
            if (this.multfileImg != null) {
              fd.append('enterpriseImageFiles', this.multfileImg);
            }
            if (this.fileBusinessLicenseLists != null) {
              for (let i = 0; i < this.fileBusinessLicenseLists.length; i++) {
                fd.append('businessLicenseFiles', this.fileBusinessLicenseLists[i].raw);
              }
            }
            if (this.fileCorporateIdentityCardLists != null) {
              for (let i = 0; i < this.fileCorporateIdentityCardLists.length; i++) {
                fd.append('deputyIdCardPhotoFiles	', this.fileCorporateIdentityCardLists[i].raw);
              }
            }
            const { data: res } = await this.$http.put('zsSys/Enterprise/updateDataAndFile', fd);
            if (res.status !== 200) return this.$message.error('修改失败！');
            this.$message.success('修改成功！');
            this.getList();
          });
        })
        .catch(() => {
          this.$message({
            duration: 1000,
            type: 'info',
            message: '已取消操作',
          });
        });
    }, 3000),
    // 解决只能输入数字输入框的值绑定问题
    registerMoneyChange(e) {
      this.$set(this.row, 'registerMoney', e.target.value);
    },
    phoneChange(e) {
      this.$set(this.row, 'phone', e.target.value);
    },
    deputyPhoneChange(e) {
      this.$set(this.row, 'deputyPhone', e.target.value);
    },
    postcodeChange(e) {
      this.$set(this.row, 'postcode', e.target.value);
    },
    // 根据code转换成name
    getDictionaryName(code) {
      for (let i = 0; i < this.ioTOptions.length; i++) {
        if (code === this.ioTOptions[i].code) {
          return this.ioTOptions[i].name;
        }
      }
    },
  },
};
</script>

<style scoped lang="less">
// 表单居中
.el-form {
  margin: 0 auto;
}

// 按钮居中
.t-btn-user {
  display: flex;
  justify-content: center;
  margin-top: 15px;
}

// 地图插件样式
.amap-box {
  width: 100%;
  height: 400px;
  position: relative;
}

.container {
  width: 100%;
  height: 100%;
  border: 1px solid #999;
  position: relative;
}

// 搜索框样式
.search-box {
  position: absolute;
  z-index: 5;
  width: 70%;
  left: 13%;
  top: 10px;
  height: 30px;

  input {
    float: left;
    width: 80%;
    height: 100%;
    border: 1px solid #30ccc1;
    padding: 0 8px;
    outline: none;
  }

  button {
    float: left;
    width: 20%;
    height: 100%;
    background: #30ccc1;
    border: 1px solid #30ccc1;
    color: #fff;
    outline: none;
  }
}

// 搜索提示框样式
.tip-box {
  width: 100%;
  max-height: 260px;
  position: absolute;
  top: 30px;
  overflow-y: auto;
  background-color: #fff;
}
</style>
<style lang="less">
// 上传企业形象图插件样式
.user-upload {
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
</style>
