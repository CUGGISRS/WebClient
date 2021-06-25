<template>
  <div class="register_container">
    <div class="register_box">
      <div class="register_top">
        <h2>企业用户注册</h2>
        <span></span>
      </div>
      <!-- 登录表单区域 -->
      <el-form
        :model="registerForm"
        ref="registerFormRef"
        label-width="160px"
        :rules="registerFormRules"
        class="register_form"
        :style="curHeight"
      >
        <el-row>
          <el-col :span="12">
            <el-form-item prop="username" label="用户名:">
              <el-input
                v-model="registerForm.username"
                clearable
                maxlength="50"
                placeholder="由字母或数字组成，4-18位"
              ></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item prop="password" label="密码:">
              <el-input
                v-model="registerForm.password"
                clearable
                maxlength="50"
                show-password
                placeholder="由字母、数字组成，6-16位"
              ></el-input>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row>
          <el-col :span="12">
            <el-form-item prop="checkPwd" label="确认密码:">
              <el-input
                v-model="registerForm.checkPwd"
                clearable
                maxlength="50"
                show-password
                placeholder="请再次输入密码"
              ></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item prop="name" label="企业名称:">
              <el-input v-model="registerForm.name" clearable maxlength="50" placeholder="由1-20个汉字组成"></el-input>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row>
          <el-col :span="12">
            <el-form-item prop="businessType" label="企业类型:">
              <el-select v-model="registerForm.businessType" clearable placeholder="请选择企业类型">
                <el-option v-for="item in businessTypeOptions" :key="item.code" :label="item.name" :value="item.code">
                </el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item prop="socialCode" label="统一社会信用代码:">
              <el-input v-model="registerForm.socialCode" clearable maxlength="50" placeholder="由18位组成"></el-input>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row>
          <el-col :span="12">
            <el-form-item prop="district" label="行政区规划:">
              <el-select v-model="registerForm.district" clearable placeholder="请选择行政区">
                <el-option v-for="item in districtOptions" :key="item.code" :label="item.name" :value="item.code">
                </el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item prop="registerAddress" label="注册地址:">
              <el-input
                v-model="registerForm.registerAddress"
                clearable
                maxlength="255"
                placeholder="营业执照上的地址"
              ></el-input>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row>
          <el-col :span="12">
            <el-form-item prop="organizationForm" label="组织形式:">
              <el-select v-model="registerForm.organizationForm" clearable placeholder="请选择组织形式">
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
              <el-select v-model="registerForm.subjectAttribute" clearable placeholder="请选择主体属性">
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
            <el-form-item prop="establishDate" label="企业成立日期:">
              <el-date-picker
                value-format="yyyy-MM-dd"
                v-model="registerForm.establishDate"
                type="date"
                clearable
                placeholder="选择企业成立日期"
              >
              </el-date-picker>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item prop="registerMoney" label="注册资金(万元):">
              <el-input
                v-model="registerForm.registerMoney"
                clearable
                placeholder="请输入注册资金"
                onkeyup="value=value.replace(/[^\d^\.]/g,'')"
                @blur="registerMoneyChange"
              >
              </el-input>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row>
          <el-col :span="12">
            <el-form-item prop="contactPerson" label="联系人:">
              <el-input v-model="registerForm.contactPerson" clearable placeholder="请输入联系人姓名"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item prop="phone" label="联系人手机号:">
              <el-input
                v-model="registerForm.phone"
                clearable
                placeholder="请输入联系人手机号"
                onkeyup="value=value.replace(/[^\d]/g,'')"
                @blur="phoneChange"
              >
              </el-input>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row>
          <el-col :span="12">
            <el-form-item prop="mailbox" label="电子邮箱:">
              <el-input
                v-model="registerForm.mailbox"
                clearable
                maxlength="50"
                placeholder="请输入正确格式的邮箱"
              ></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item prop="deputyName" label="法定代表人姓名:">
              <el-input
                v-model="registerForm.deputyName"
                clearable
                maxlength="50"
                placeholder="请输入法定代表人姓名"
              ></el-input>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row>
          <el-col :span="12">
            <el-form-item prop="deputyIdCard" label="法定代表人身份证号:">
              <el-input
                v-model="registerForm.deputyIdCard"
                clearable
                maxlength="50"
                placeholder="身份证号由18位组成"
              ></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item prop="deputyPhone" label="法定代表人联系电话:">
              <el-input
                v-model="registerForm.deputyPhone"
                clearable
                maxlength="50"
                placeholder="请输入法定代表人联系电话"
                onkeyup="value=value.replace(/[^\d]/g,'')"
                @blur="deputyPhoneChange"
              >
              </el-input>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row>
          <el-col :span="12">
            <el-form-item prop="postcode" label="邮政编码:">
              <el-input
                v-model="registerForm.postcode"
                clearable
                maxlength="6"
                placeholder="请输入邮政编码"
                onkeyup="value=value.replace(/[^\d]/g,'')"
                @blur="postcodeChange"
              >
              </el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="经纬度:" prop="longitude">
              <el-form-item prop="longitude" style="float: left">
                <el-input
                  v-model="registerForm.longitude"
                  readonly
                  placeholder="请选择经度"
                  style="width: 120px;"
                  @focus="showMap"
                ></el-input>
              </el-form-item>
              <el-form-item prop="latitude" style="float: left">
                <el-input
                  v-model="registerForm.latitude"
                  readonly
                  placeholder="请选择纬度"
                  style="width: 120px;"
                  @focus="showMap"
                ></el-input>
              </el-form-item>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row>
          <el-col :span="24">
            <el-tabs type="border-card" style="height:220px;">
              <el-tab-pane label="企业形象图" class="register-upload">
                <el-form-item prop="file" label-width="0">
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
              </el-tab-pane>
              <el-tab-pane label="营业执照">
                <el-form-item prop="file2" label-width="0">
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
                </el-form-item>
              </el-tab-pane>
              <el-tab-pane label="法人身份证照片(请上传身份证正面照片和反面照片)">
                <el-form-item prop="file3" label-width="0">
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
                </el-form-item>
              </el-tab-pane>
            </el-tabs>
          </el-col>
        </el-row>
      </el-form>
      <!-- 按钮 -->
      <span class="register_btn">
        <el-button type="info" @click="resetForm">重置</el-button>
        <el-button type="success" @click="register">注册</el-button>
      </span>
      <!-- 营业执照图片预览 -->
      <el-dialog :visible.sync="dialogBusinessLicenseVisible" width="70%" top="5vh">
        <img width="100%" :src="dialogBusinessLicenseImageUrl" />
      </el-dialog>
      <!-- 法人身份证照片预览 -->
      <el-dialog :visible.sync="dialogCorporateIdentityCardVisible" width="70%" top="5vh">
        <img width="100%" :src="dialogCorporateIdentityCardImageUrl" />
      </el-dialog>
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
    </div>
  </div>
</template>

<script>
import { checkIdNumber, checkPhoneNumber, Debounce } from '../assets/js/tool';
import { AMapManager, lazyAMapApiLoaderInstance } from 'vue-amap';
let amapManager = new AMapManager();
export default {
  data() {
    let self = this;
    var validatePass = (rule, value, callback) => {
      if (value !== this.registerForm.password) {
        callback(new Error('两次输入密码不一致!'));
      } else {
        callback();
      }
    };
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
    return {
      curHeight: {
        height: '',
        width: '830px',
        overflow: 'auto',
      },
      // 地图
      dialogMapVisible: false,
      address: null,
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
                  self.lng = result.position.lng;
                  // 设置维度
                  self.lat = result.position.lat;
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
      // 注册表单
      registerForm: {},
      // 图片文件上传
      updateUrl: '#',
      imageUrl: '',
      // 上传文件
      multfileImg: null,
      // 营业执照
      picBusinessLicenseListPhoto: [],
      dialogBusinessLicenseImageUrl: '',
      dialogBusinessLicenseVisible: false,
      // 上传文件
      fileBusinessLicenseLists: [],
      // 法人身份证照片
      picCorporateIdentityCardListPhoto: [],
      dialogCorporateIdentityCardImageUrl: '',
      dialogCorporateIdentityCardVisible: false,
      // 上传文件
      fileCorporateIdentityCardLists: [],
      // 多选框
      businessTypeOptions: [],
      organizationFormOptions: [],
      subjectAttributeOptions: [],
      districtOptions: [],
      // 验证规则
      registerFormRules: {
        username: [
          { required: true, message: '请输入用户名', trigger: 'blur' },
          { min: 4, max: 18, message: '长度在 4 到 18个字符', trigger: 'blur' },
        ],
        password: [
          { required: true, message: '请输入密码', trigger: 'blur' },
          { min: 6, max: 16, message: '长度在 6 到 16个字符', trigger: 'blur' },
        ],
        checkPwd: [
          { required: true, message: '请确认密码', trigger: 'blur' },
          { required: true, validator: validatePass, trigger: 'blur' },
        ],
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
        longitude: [{ required: true, message: '请输入经度', trigger: 'change' }],
        latitude: [{ required: true, message: '请输入纬度', trigger: 'change' }],
        file: [{ required: true, message: '请上传企业形象图片' }],
        file2: [{ required: true, message: '请上传营业执照图片' }],
        file3: [{ required: true, message: '请上传法人身份证照片' }],
      },
    };
  },
  created() {
    this.getBusinessTypeDictionary();
    this.getOrganizationFormDictionary();
    this.getSubjectAttributeDictionary();
    this.getDistrictDictionary();
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
    // 设定表格高度
    setTableHeight() {
      let h = document.documentElement.clientHeight || document.body.clientHeight;
      this.curHeight.height = h - 160 + 'px';
    },
    // 地图显示
    showMap() {
      this.dialogMapVisible = true;
    },
    // 获取经纬度
    getCoordinate() {
      // 使用 Vue.set(object, propertyName, value) 方法向嵌套对象添加响应式属性
      this.$set(this.registerForm, 'longitude', this.lng);
      this.$set(this.registerForm, 'latitude', this.lat);
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
    // 重置表单
    resetForm() {
      this.$confirm('此操作将重置表单, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning',
      })
        .then(() => {
          this.imageUrl = '';
          this.multfileImg = null;
          this.fileBusinessLicenseLists = [];
          this.fileCorporateIdentityCardLists = [];
          this.registerFormRules.file = [{ required: true, message: '请上传企业形象图片' }];
          this.registerFormRules.file2 = [{ required: true, message: '请上传营业执照图片' }];
          this.registerFormRules.file3 = [{ required: true, message: '请上传法人身份证照片' }];
          this.$refs.registerFormRef.resetFields();
        })
        .catch(() => {
          this.$message({
            duration: 1000,
            type: 'info',
            message: '已取消操作',
          });
        });
    },
    // 提交注册
    register: Debounce(function() {
      this.registerForm.sysName = 'X2';
      this.$refs.registerFormRef.validate(async (valid) => {
        if (!valid) return this.$message.error('注册信息填写不完整或不准确，请检查再提交！');
        let fd = new FormData();
        for (let key in this.registerForm) {
          let value = this.registerForm[key];
          // 过滤
          if (key === 'checkPwd') {
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
        const { data: res } = await this.$http.post('zsSys/Enterprise/addDataAndFile', fd).catch((error) => {
          this.$message.error(error.response.data.message);
        });
        if (res.status !== 200) return this.$message.error(res.message);
        this.$http.get(`zsSys/Enterprise/sendPromptSms?name=${this.registerForm.name}`);
        this.$message.success('注册成功，等待审核通过，由管理员通知！');
        this.$router.push('/login');
      });
    }, 3000),
    // 处理图片
    beforeAvatarUpload(file) {
      this.multfileImg = file;
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
    httpRequest01(data) {
      let _this = this;
      let rd = new FileReader(); // 创建文件读取对象
      let file = data.file;
      rd.readAsDataURL(file); // 文件读取装换为base64类型
      rd.onloadend = function() {
        _this.imageUrl = this.result; // this指向当前方法onloadend的作用域
        if (_this.multfileImg != null) {
          _this.registerFormRules.file = [];
          _this.$refs.registerFormRef.clearValidate('file');
        }
      };
    },
    httpRequest02(data) {
      let _this = this;
      let rd = new FileReader(); // 创建文件读取对象
      let file = data.file;
      rd.readAsDataURL(file); // 文件读取装换为base64类型
      rd.onloadend = function() {
        _this.dialogBusinessLicenseImageUrl = this.result; // this指向当前方法onloadend的作用域
        if (_this.fileBusinessLicenseLists.length != 0) {
          _this.registerFormRules.file2 = [];
          _this.$refs.registerFormRef.clearValidate('file2');
        }
      };
    },
    httpRequest03(data) {
      let _this = this;
      let rd = new FileReader(); // 创建文件读取对象
      let file = data.file;
      rd.readAsDataURL(file); // 文件读取装换为base64类型
      rd.onloadend = function() {
        _this.dialogCorporateIdentityCardImageUrl = this.result; // this指向当前方法onloadend的作用域
        if (_this.fileCorporateIdentityCardLists.length != 0) {
          _this.registerFormRules.file3 = [];
          _this.$refs.registerFormRef.clearValidate('file3');
        }
      };
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
      if (this.fileBusinessLicenseLists.length == 0) {
        this.registerFormRules.file2 = [{ required: true, message: '请上传营业执照图片' }];
      }
    },
    handleChangeBusinessLicensePhoto(file, fileList) {
      this.fileBusinessLicenseLists = fileList;
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
      if (this.fileCorporateIdentityCardLists.length == 0) {
        this.registerFormRules.file3 = [{ required: true, message: '请上传法人身份证照片' }];
      }
    },
    handleChangeCorporateIdentityCardPhoto(file, fileList) {
      this.fileCorporateIdentityCardLists = fileList;
    },
    // 解决只能输入数字输入框的值绑定问题
    postcodeChange(e) {
      this.$set(this.registerForm, 'postcode', e.target.value);
    },
    deputyPhoneChange(e) {
      this.$set(this.registerForm, 'deputyPhone', e.target.value);
    },
    phoneChange(e) {
      this.$set(this.registerForm, 'phone', e.target.value);
    },
    registerMoneyChange(e) {
      this.$set(this.registerForm, 'registerMoney', e.target.value);
    },
  },
};
</script>

<style scoped lang="less">
// 容器样式
.register_container {
  width: 100%;
  height: 100%;
}

// 表单页面样式
.register_box {
  width: 900px;
  height: 100%;
  margin: 0 auto;
}

// 表单顶部样式
.register_top {
  width: 100%;
  height: 80px;
  position: relative;

  // 标题
  h2 {
    font-size: 32px;
    color: #027db4;
    position: absolute;
    left: 50px;
    top: 0;
  }

  // 下划线
  span {
    display: inline-block;
    width: 800px;
    height: 1px;
    background-color: #027db4;
    position: absolute;
    left: 50%;
    bottom: 0;
    transform: translateX(-50%);
  }
}

// 表单样式
.register_form {
  width: 800px;
  height: 100px;
  margin: 20px auto;
  margin-bottom: 5px;

  // 输入框样式
  .el-input,
  .el-select {
    width: 240px;
  }
}

// 按钮居中
.register_btn {
  display: inline-block;
  width: 100%;
  height: 36px;
  text-align: center;

  // 按钮样式
  .el-button {
    width: 100px;
    height: 36px;
    margin: 0 20px;
  }
}

// 地图样式
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

.search-box {
  position: absolute;
  z-index: 5;
  width: 70%;
  left: 13%;
  top: 10px;
  height: 30px;
  background-color: skyblue;

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
.register-upload {
  .avatar-uploader {
    height: 148px;
  }
  .avatar-uploader .el-upload {
    border: 1px dashed #d9d9d9;
    border-radius: 6px;
    width: 148px;
    height: 148px;
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
    width: 148px;
    height: 148px;
    line-height: 146px;
    text-align: center;
  }
  .avatar {
    width: 148px;
    height: 148px;
    display: block;
  }
}
</style>
