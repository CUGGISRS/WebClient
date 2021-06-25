<template>
  <div class="container">
    <el-tabs v-model="activeName" type="card" @tab-click="handleClick">
      <el-tab-pane label="设备" name="first">
        <div class="container-group">
          <el-button size="small" class="el-icon-plus" type="primary" @click="addGroup">新增设备</el-button>
          <el-button size="small" class="el-icon-edit" type="warning" @click="editGroup" style="margin-left: 0"
            >修改设备
          </el-button>
          <ul :style="curHeight">
            <li
              v-for="(item, index) in groupLists"
              :key="item.id"
              @click="changeGroup(item.id, index)"
              class="group-li"
            >
              <span>{{ item.name }}</span>
              <i class="el-icon-close" @click="deleteGroup(item.id)"></i>
            </li>
          </ul>
        </div>
      </el-tab-pane>
      <el-tab-pane label="监控" name="second">
        <div class="container-list">
          <el-button size="small" class="el-icon-plus" type="primary" @click="addVideo">新增监控</el-button>
          <ul :style="curHeight">
            <li v-for="(item, index) in videoRows" :key="item.id" @click="changeVideo(index)" class="video-li">
              <span>{{ item.title }}</span>
              <i class="el-icon-close" @click="deleteVideo(item.id)"></i>
            </li>
          </ul>
        </div>
      </el-tab-pane>
      <el-tab-pane label="管理员" name="third">
        <div class="container-users">
          <el-button size="small" class="el-icon-plus" type="primary" @click="addUser">新增管理员</el-button>
          <ul :style="curHeight">
            <li v-for="(item, index) in userRows" :key="item.id" @click="changeUser(index)" class="user-li">
              <span>{{ item.name }}</span>
              <i class="el-icon-close" @click="deleteUser(item.id)"></i>
            </li>
          </ul>
        </div>
      </el-tab-pane>
    </el-tabs>
    <div class="container-sensor" v-if="showItem === 'group'">
      <el-button size="small" class="el-icon-plus" type="primary" @click="addSensor">新增传感器</el-button>
      <div class="data-table">
        <el-table
          :data="sensorLists"
          border
          stripe
          fit
          class="table-scroll-bar"
          highlight-current-row
          height="100%"
          :cell-style="{ padding: '5px 0' }"
        >
          <el-table-column align="center" label="序号" width="60">
            <template slot-scope="scope">
              {{ (currentPage - 1) * pageSize + scope.$index + 1 }}
            </template>
          </el-table-column>
          <el-table-column align="center" label="操作" width="320">
            <template slot-scope="scope">
              <el-button size="mini" type="primary" plain icon="el-icon-edit-outline" @click="editSensor(scope.row)">
                编辑
              </el-button>
              <el-button size="mini" type="warning" plain icon="el-icon-setting" @click="showThreshold(scope.row.id)">
                报警配置
              </el-button>
              <el-button size="mini" type="danger" plain icon="el-icon-delete" @click="deleteSensor(scope.row.id)">
                删除
              </el-button>
            </template>
          </el-table-column>
          <el-table-column align="center" label="设备ID">
            <template slot-scope="scope">
              {{ scope.row.facId }}
            </template>
          </el-table-column>
          <el-table-column align="center" label="设备通道号">
            <template slot-scope="scope">
              {{ scope.row.facPass }}
            </template>
          </el-table-column>
          <el-table-column align="center" label="传感器名称">
            <template slot-scope="scope">
              {{ scope.row.name }}
            </template>
          </el-table-column>
          <el-table-column align="center" label="经度">
            <template slot-scope="scope">
              {{ scope.row.longitude }}
            </template>
          </el-table-column>
          <el-table-column align="center" label="纬度">
            <template slot-scope="scope">
              {{ scope.row.latitude }}
            </template>
          </el-table-column>
        </el-table>
      </div>
      <!-- 分页 -->
      <el-pagination
        background
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
        :current-page="currentPage"
        :page-size="pageSize"
        layout="total, sizes, prev, pager, next, jumper"
        :total="total"
      >
      </el-pagination>
    </div>
    <div class="container-video" v-else-if="showItem === 'video'">
      <el-card class="box-card">
        <el-form :model="videoRow" label-width="120px" :rules="rules2" ref="videoRow" class="row-form">
          <el-row>
            <el-form-item label="设备名称:" prop="title">
              <el-input v-model="videoRow.title" clearable placeholder="请输入设备名称"></el-input>
            </el-form-item>
          </el-row>
          <el-row>
            <el-form-item label="设备序列号:" prop="serialNum">
              <el-input v-model="videoRow.serialNum" clearable placeholder="请输入设备序列号"></el-input>
            </el-form-item>
          </el-row>
          <el-row>
            <el-form-item label="通道号:" prop="passNum">
              <el-input v-model="videoRow.passNum" clearable placeholder="请输入通道号"></el-input>
            </el-form-item>
          </el-row>
          <el-row>
            <el-form-item label="appKey:" prop="appKey">
              <el-input v-model="videoRow.appKey" clearable placeholder="请输入AppKey"></el-input>
            </el-form-item>
          </el-row>
          <el-row>
            <el-form-item label="appSecret:" prop="appSecret">
              <el-input v-model="videoRow.appSecret" clearable placeholder="请输入AppSecret"></el-input>
            </el-form-item>
          </el-row>
          <el-row>
            <el-form-item label="验证码:">
              <el-input v-model="videoRow.verifyCode" clearable placeholder="加密设备必须输入验证码"></el-input>
            </el-form-item>
          </el-row>
          <el-row>
            <el-form-item label="自动播放:" prop="isAutoPlay">
              <el-select v-model="videoRow.isAutoPlay" clearable placeholder="是否自动播放">
                <el-option v-for="item in selectOptions" :key="item.value" :label="item.label" :value="item.value">
                </el-option>
              </el-select>
            </el-form-item>
          </el-row>
          <el-row>
            <el-form-item label="开启音频:" prop="isOpenAudio">
              <el-select v-model="videoRow.isOpenAudio" clearable placeholder="是否开启音频">
                <el-option v-for="item in selectOptions" :key="item.value" :label="item.label" :value="item.value">
                </el-option>
              </el-select>
            </el-form-item>
          </el-row>

          <span class="row-btn">
            <el-button size="small" type="info" @click="getAllVideo">取 消</el-button>
            <el-button size="small" type="success" @click="submitVideo">提 交</el-button>
          </span>
        </el-form>
      </el-card>
    </div>
    <div class="container-user" v-else-if="showItem === 'user'">
      <el-card class="box-card">
        <el-form :model="userRow" label-width="120px" :rules="rules4" ref="userRow" class="row-form">
          <el-row>
            <el-form-item label="管理员名称:" prop="name">
              <el-input v-model="userRow.name" clearable placeholder="请输入管理员名称"></el-input>
            </el-form-item>
          </el-row>
          <el-row>
            <el-form-item label="管理员性别:">
              <el-radio v-model="userRow.sex" label="男">男</el-radio>
              <el-radio v-model="userRow.sex" label="女">女</el-radio>
            </el-form-item>
          </el-row>
          <el-row>
            <el-form-item label="管理员电话:" prop="phone">
              <el-input
                v-model="userRow.phone"
                clearable
                placeholder="请输入管理员电话"
                onkeyup="value=value.replace(/[^\d]/g,'')"
                @blur="phoneChange"
              >
              </el-input>
            </el-form-item>
          </el-row>
          <el-row>
            <el-form-item label="管理员邮箱:">
              <el-input v-model="userRow.mailbox" clearable placeholder="请输入管理员邮箱"></el-input>
            </el-form-item>
          </el-row>
          <el-row>
            <el-form-item label="创建日期:">
              <el-date-picker
                value-format="yyyy-MM-dd HH:mm:ss"
                v-model="userRow.createTime"
                type="datetime"
                placeholder="请选创建日期"
              >
              </el-date-picker>
            </el-form-item>
          </el-row>

          <span class="row-btn">
            <el-button size="small" type="info" @click="getAllUser">取 消</el-button>
            <el-button size="small" type="success" @click="submitUser">提 交</el-button>
          </span>
        </el-form>
      </el-card>
    </div>
    <!-- 传感器 -->
    <el-dialog
      v-cloak
      title="传感器信息"
      center
      @close="closeDialog"
      :visible.sync="dialogShow"
      width="1000px"
      class="row-form"
    >
      <el-form :model="sensorRow" label-width="120px" class="demo-ruleForm" :rules="rules" ref="sensorRow">
        <el-row>
          <el-col :span="12">
            <el-form-item label="设备供应商:" prop="enterpriseWlUserId">
              <el-select
                v-model="sensorRow.enterpriseWlUserId"
                clearable
                placeholder="请选择设备供应商"
                @change="changeIoTUserOptions"
                :disabled="editDisabled"
              >
                <el-option v-for="item in ioTUserLists" :key="item.id" :label="item.name" :value="item.id"> </el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="设备:" prop="facId">
              <el-select
                v-model="sensorRow.facId"
                clearable
                placeholder="请选择设备"
                @change="changeOptionsByFacId"
                :disabled="editDisabled"
              >
                <el-option v-for="item in facIdOptions" :key="item.facId" :label="item.facName" :value="item.facId">
                </el-option>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row>
          <el-col :span="12">
            <el-form-item label="设备通道:" prop="facPass">
              <el-select
                v-model="sensorRow.facPass"
                clearable
                placeholder="请选择设备通道"
                @change="updateData"
                :disabled="editDisabled"
              >
                <el-option
                  v-for="item in facPassOptions"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value"
                  :disabled="item.disabled"
                >
                </el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="传感器名称:" prop="name">
              <el-input v-model="sensorRow.name" clearable placeholder="请输入传感器名称"></el-input>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row>
          <el-col :span="12">
            <el-form-item label="经度:" prop="longitude">
              <el-input v-model="sensorRow.longitude" clearable placeholder="请输入经度"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="纬度:" prop="latitude">
              <el-input v-model="sensorRow.latitude" clearable placeholder="请输入纬度"></el-input>
            </el-form-item>
          </el-col>
        </el-row>

        <span class="row-btn">
          <el-button size="small" type="info" @click="dialogShow = false">取 消</el-button>
          <el-button size="small" type="success" @click="submitSensor">提 交</el-button>
        </span>
      </el-form>
    </el-dialog>
    <!-- 报警阈值信息 -->
    <el-dialog
      v-cloak
      title="报警阈值信息"
      center
      :visible.sync="dialogThresholdInfoShow"
      width="1200px"
      class="threshold-dialog"
    >
      <el-button size="small" class="el-icon-plus" type="primary" @click="addThreshold" style="margin: 0 0 10px;">
        新增报警阈值配置
      </el-button>
      <div class="data-table">
        <el-table :data="thresholdRows" border stripe fit highlight-current-row :cell-style="{ padding: '5px 0' }">
          <el-table-column align="center" label="序号" width="60">
            <template slot-scope="scope">
              {{ (currentThresholdPage - 1) * pageThresholdSize + scope.$index + 1 }}
            </template>
          </el-table-column>
          <el-table-column align="center" label="操作" width="250">
            <template slot-scope="scope">
              <el-button size="mini" type="primary" plain icon="el-icon-edit-outline" @click="editThreshold(scope.row)">
                编辑
              </el-button>
              <el-button size="mini" type="danger" plain icon="el-icon-delete" @click="deleteThreshold(scope.row.id)">
                删除
              </el-button>
            </template>
          </el-table-column>
          <el-table-column align="center" label="级别">
            <template slot-scope="scope">
              {{ scope.row.level + '级' }}
            </template>
          </el-table-column>
          <el-table-column align="center" label="是否发送短信">
            <template slot-scope="scope">
              <el-tag type="success" v-if="scope.row.isSend === 1">是</el-tag>
              <el-tag type="danger" v-else>否</el-tag>
            </template>
          </el-table-column>
          <el-table-column align="center" label="发送短信间隔">
            <template slot-scope="scope">
              {{ scope.row.sendInterval + '分钟' }}
            </template>
          </el-table-column>
          <el-table-column align="center" label="管理员">
            <template slot-scope="scope">
              {{ getUserName(scope.row.enterpriseWLManagerId) }}
            </template>
          </el-table-column>
          <el-table-column align="center" label="预警信息">
            <template slot-scope="scope">
              <el-tooltip effect="light" :content="scope.row.warningInfo" placement="top-end">
                <span>{{ textSubstr(scope.row.warningInfo, 20) }}</span>
              </el-tooltip>
            </template>
          </el-table-column>
          <el-table-column align="center" label="最后发送短信时间" width="160">
            <template slot-scope="scope">
              {{ scope.row.lastRecordTime }}
            </template>
          </el-table-column>
        </el-table>
      </div>
      <!-- 分页 -->
      <el-pagination
        background
        @size-change="handleThresholdSizeChange"
        @current-change="handleThresholdCurrentChange"
        :current-page="currentThresholdPage"
        :page-size="pageThresholdSize"
        layout="total, prev, pager, next, jumper"
        :total="totalThreshold"
      >
      </el-pagination>
    </el-dialog>
    <!-- 报警阈值配置 -->
    <el-dialog
      v-cloak
      title="报警阈值配置"
      center
      @close="closeThresholdDialog"
      :visible.sync="dialogThresholdShow"
      width="1000px"
      class="row-form"
    >
      <el-form :model="thresholdRow" label-width="150px" class="demo-ruleForm" :rules="rules3" ref="thresholdRow">
        <el-row>
          <el-col :span="12">
            <el-form-item label="级别:" prop="level">
              <el-select v-model="thresholdRow.level" clearable placeholder="请选择级别">
                <el-option v-for="item in levelOptions" :key="item.value" :label="item.label" :value="item.value">
                </el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="是否发送短信:" prop="isSend">
              <el-select v-model="thresholdRow.isSend" clearable placeholder="请选择是否发送短信">
                <el-option v-for="item in selectOptions" :key="item.value" :label="item.label" :value="item.value">
                </el-option>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row>
          <el-col :span="12">
            <el-form-item label="管理员:" prop="enterpriseWLManagerId">
              <el-select v-model="thresholdRow.enterpriseWLManagerId" clearable placeholder="请选择管理员">
                <el-option v-for="item in userRows" :key="item.id" :label="item.name" :value="item.id"> </el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="发送短信间隔(分钟):">
              <el-input
                v-model="thresholdRow.sendInterval"
                clearable
                onkeyup="value=value.replace(/[^\d^\.]/g,'')"
                placeholder="默认为15.单位为分钟"
                @blur="sendIntervalChange"
              >
              </el-input>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row>
          <el-col :span="12">
            <el-form-item label="最大值:" prop="maxValue">
              <el-input
                v-model="thresholdRow.maxValue"
                clearable
                onkeyup="value=value.replace(/[^\d^\.]/g,'')"
                placeholder="请输入最大值"
                @blur="maxValueChange"
              >
              </el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="最小值:" prop="minValue">
              <el-input
                v-model="thresholdRow.minValue"
                clearable
                placeholder="请输入最小值"
                onkeyup="value=value.replace(/[^\d^\.]/g,'')"
                @blur="minValueChange"
              >
              </el-input>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row v-if="flag3 === 'edit'">
          <el-col :span="24">
            <el-form-item label="最后发送短信时间:">
              <el-date-picker
                value-format="yyyy-MM-dd HH:mm:ss"
                v-model="thresholdRow.lastRecordTime"
                type="datetime"
                readonly
                placeholder="最后发送短信时间"
              >
              </el-date-picker>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row>
          <el-col :span="24">
            <el-form-item label="预警信息:" prop="warningInfo">
              <el-input
                v-model="thresholdRow.warningInfo"
                type="textarea"
                :rows="2"
                clearable
                placeholder="请输入预警信息"
              >
              </el-input>
            </el-form-item>
          </el-col>
        </el-row>

        <span class="row-btn">
          <el-button size="medium" type="info" @click="dialogThresholdShow = false">取 消</el-button>
          <el-button size="medium" type="success" @click="submitThreshold">提 交</el-button>
        </span>
      </el-form>
    </el-dialog>
  </div>
</template>

<script>
import { Debounce, checkPhoneNumber } from '../../assets/js/tool';

export default {
  data() {
    var validatePhone = (rule, value, callback) => {
      if (value && !checkPhoneNumber(value)) {
        callback(new Error('手机号不正确!'));
      } else {
        callback();
      }
    };
    return {
      // 设备列表
      groupLists: [],
      // 物联网用户列表
      ioTUserLists: [],
      // 传感器列表
      sensorLists: [],
      // 已选择的传感器
      selectedSensor: [],
      // 当前设备id
      defaultGroupId: 0,
      // 当前设备索引
      defaultIndex: 0,
      // 分页
      currentPage: 1,
      pageSize: 20,
      total: 0,
      // 页面高度自适应
      curHeight: {
        height: '',
        overflow: 'auto',
      },
      curHeight2: {
        width: '',
        height: '',
        overflow: 'auto',
      },
      curHeight3: 0,
      curHeight4: 0,
      // 修改or新增flag
      flag: '',
      // 对话框
      dialogShow: false,
      // 单条传感器信息
      sensorRow: {},
      // 设备Id
      facIdOptions: [],
      // 设备通道号
      facPassOptions: [],
      // 单个设备要素数组
      eleList: [],
      // 要素索引
      eleNumlist: [],
      editDisabled: false,
      // 分页
      activeName: 'first',
      // 展示设备或监控
      showItem: 'group',
      // 监控列表
      videoRows: [],
      // 单个监控信息
      videoRow: {},
      // 当前监控索引
      defaultVideoIndex: 0,
      // 修改or新增flag
      flag2: '',
      // 选择项
      selectOptions: [
        { label: '是', value: 1 },
        { label: '否', value: 0 },
      ],
      // 报警配置信息列表对话框
      dialogThresholdInfoShow: false,
      // 报警配置对话框
      dialogThresholdShow: false,
      // 报警配置信息列表
      thresholdRows: [],
      // 单个报警配置
      thresholdRow: {},
      // 分页
      currentThresholdPage: 1,
      pageThresholdSize: 10,
      totalThreshold: 0,
      // 传感器id
      sensorId: null,
      // 修改or新增flag
      flag3: '',
      // 管理员列表
      userRows: [],
      // 单个管理员信息
      userRow: {},
      // 当前管理员索引
      defaultUserIndex: 0,
      // 修改or新增flag
      flag4: '',
      // 级别项
      levelOptions: [
        { label: '1级', value: 1 },
        { label: '2级', value: 2 },
        { label: '3级', value: 3 },
        { label: '4级', value: 4 },
        { label: '5级', value: 5 },
      ],
      // 验证规则
      rules: {
        enterpriseWlUserId: [{ required: true, message: '请选择设备供应商', trigger: 'blur' }],
        facId: [{ required: true, message: '请选择设备', trigger: 'blur' }],
        facPass: [{ required: true, message: '请选择设备通道', trigger: 'blur' }],
        name: [{ required: true, message: '请输入传感器名称', trigger: 'blur' }],
        longitude: [{ required: true, message: '请输入经度', trigger: 'blur' }],
        latitude: [{ required: true, message: '请输入纬度', trigger: 'blur' }],
      },
      rules2: {
        serialNum: [{ required: true, message: '请输入设备序列号', trigger: 'blur' }],
        passNum: [{ required: true, message: '请输入通道号', trigger: 'blur' }],
        appKey: [{ required: true, message: '请输入AppKey', trigger: 'blur' }],
        appSecret: [{ required: true, message: '请输入AppSecret', trigger: 'blur' }],
        isAutoPlay: [{ required: true, message: '请选择是否自动播放', trigger: 'blur' }],
        isOpenAudio: [{ required: true, message: '请选择是否开启音频', trigger: 'blur' }],
      },
      rules3: {
        level: [{ required: true, message: '请选择级别', trigger: 'blur' }],
        maxValue: [{ required: true, message: '请输入最大值', trigger: 'blur' }],
        minValue: [{ required: true, message: '请输入最小值', trigger: 'blur' }],
        warningInfo: [{ required: true, message: '请输入预警信息', trigger: 'blur' }],
        isSend: [{ required: true, message: '请选择是否发送短信', trigger: 'blur' }],
        enterpriseWLManagerId: [{ required: true, message: '请选择管理员', trigger: 'blur' }],
      },
      rules4: {
        name: [{ required: true, message: '请输入管理员名称', trigger: 'blur' }],
        phone: [
          { required: true, message: '请输入联系电话', trigger: 'blur' },
          { required: true, validator: validatePhone, trigger: 'blur' },
        ],
      },
    };
  },
  created() {
    this.getAllGroup();
    this.getAllVideo();
    this.getAllUser();
    this.getIoTUserByEnterpriseId();
    // this.setHeight();
    // window.onresize = () => {
    //   this.setHeight();
    // };
  },
  methods: {
    // 分页
    handleClick(tab) {
      if (tab.name === 'first') {
        this.showItem = 'group';
      } else if (tab.name === 'second') {
        this.showItem = 'video';
      } else if (tab.name === 'third') {
        this.showItem = 'user';
      }
    },
    // 设定高度
    setHeight() {
      let w = document.documentElement.clientWidth || document.body.clientWidth;
      let h = document.documentElement.clientHeight || document.body.clientHeight;
      this.curHeight.height = h - 222 + 'px';
      this.curHeight2.width = w - 440 + 'px';
      this.curHeight2.height = h - 232 + 'px';
      this.curHeight3 = h - 632;
      this.curHeight4 = h - 532;
    },
    // 获取全部设备
    async getAllGroup() {
      this.defaultIndex = 0;
      const { data: res } = await this.$http.get('zsSys/DeviceGroup/pageByCId', {
        params: { creatorId: window.sessionStorage.getItem('enterpriseId') },
      });
      if (res.status !== 200) return this.$message.error('获取全部设备失败!');
      this.groupLists = res.data.rows;
      setTimeout(() => {
        if (this.groupLists && this.groupLists.length > 0) {
          this.changeGroup(this.groupLists[0].id, this.defaultIndex);
        }
      }, 0);
    },
    // 根据设备id获取传感器信息
    async getSensorByGroupId(groupId) {
      const { data: res } = await this.$http.get('zsSys/DeviceSensor/pageByGId', {
        params: {
          groupId,
          page: this.currentPage,
          limit: this.pageSize,
        },
      });
      if (res.status !== 200) return this.$message.error('获取传感器数据失败!');
      this.sensorLists = res.data.rows;
      this.total = res.data.total;
    },
    // 根据企业id获取设备供应商信息
    async getIoTUserByEnterpriseId() {
      const { data: res } = await this.$http.get('zsSys/EnterpriseWlUser/pageByEId', {
        params: {
          enterpriseId: window.sessionStorage.getItem('enterpriseId'),
          page: this.currentPage,
          limit: this.pageSize,
        },
      });
      if (res.status !== 200) return this.$message.error('获取传感器数据失败!');
      this.ioTUserLists = res.data.rows;
    },
    // 登录物联网设备
    async loginIoT(params) {
      const { data: res } = await this.$http2.post('login', params);
      if (!res) return this.$message.error('获取设备失败！');
      window.sessionStorage.setItem('ioTToken', res.token);
      this.getAllDevice(params.username);
    },
    // 获取当前用户下的全部设备
    async getAllDevice(username) {
      const { data: res } = await this.$http2.get('user/' + username);
      if (!res) return this.$message.error('获取设备失败！');
      this.facIdOptions = res.devices;
      this.getAllEle();
    },
    // 通过设备id获取要素信息
    async getDeviceInfo(deviceId) {
      const { data: res } = await this.$http2.get('device/' + deviceId);
      if (!res) return this.$message.error('获取单个设备信息失败！');
      this.facPassOptions = this.filterData(res.eleName.split('/'), '-', 'e');
      this.eleNumlist = res.eleNum.split('/');
    },
    // 获取全部要素信息
    async getAllEle() {
      const { data: res } = await this.$http2.get('element');
      if (!res) return this.$message.error('获取要素失败！');
      this.eleList = res;
    },
    // 通过设备id查询已选传感器
    async getSensorByFacId() {
      const { data: res } = await this.$http.get('zsSys/DeviceSensor/pageByCId', {
        params: { creatorId: window.sessionStorage.getItem('enterpriseId') },
      });
      if (res.status !== 200) return this.$message.error('获取传感器数据失败!');
      this.selectedSensor = res.data.rows;
    },
    // 通过设备id获取要素信息
    async changeOptionsByFacId(value) {
      this.sensorRow.facPass = null;
      this.facPassOptions = [];
      if (value) {
        await this.getSensorByFacId();
        this.getDeviceInfo(value);
      }
    },
    // 数据过滤
    filterData(arr, value, name) {
      let tmpArr = [];
      let tmpObj = {};
      for (let i = 0; i < arr.length; i++) {
        tmpObj = {};
        // 除去空数据
        if (arr[i] !== value) {
          tmpObj.label = arr[i];
          tmpObj.value = name + (i + 1) + '';
          // 将已选择的数据禁选
          for (let item of this.selectedSensor) {
            if (tmpObj.value === item.facPass) {
              tmpObj.disabled = true;
            }
          }
          tmpArr.push(tmpObj);
        }
      }
      return tmpArr;
    },
    // 新增设备
    addGroup() {
      this.$prompt('请输入设备名称', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        inputPattern: /\S/,
        inputErrorMessage: '设备名称不能为空',
      })
        .then(async ({ value }) => {
          const { data: res } = await this.$http.post('zsSys/DeviceGroup', {
            name: value,
            creatorId: window.sessionStorage.getItem('enterpriseId'),
          });
          if (res.status !== 200) return this.$message.error('新增失败！');
          this.getAllGroup();
        })
        .catch(() => {
          this.$message({
            type: 'info',
            message: '取消新增',
          });
        });
    },
    // 修改设备名称
    editGroup() {
      this.$prompt('请输入设备名称', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        inputPattern: /\S/,
        inputErrorMessage: '设备名称不能为空',
        inputPlaceholder: this.groupLists[this.defaultIndex].name,
      })
        .then(async ({ value }) => {
          const { data: res } = await this.$http.put('zsSys/DeviceGroup', {
            id: this.defaultGroupId,
            name: value,
            creatorId: window.sessionStorage.getItem('enterpriseId'),
          });
          if (res.status !== 200) return this.$message.error('修改失败！');
          this.getAllGroup();
        })
        .catch(() => {
          this.$message({
            type: 'info',
            message: '取消修改',
          });
        });
    },
    // 删除设备
    deleteGroup(id) {
      this.$confirm('此操作将删除选中的设备, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning',
      })
        .then(async () => {
          const { data: res } = await this.$http.delete('zsSys/DeviceGroup/' + id);
          if (res.status !== 200) {
            return this.$message({
              duration: 2000,
              type: 'error',
              message: '删除设备失败！',
            });
          }
          this.getAllGroup();
        })
        .catch(() => {
          this.$message({
            duration: 1000,
            type: 'info',
            message: '已取消操作',
          });
        });
    },
    // 点击切换设备
    changeGroup(groupId, index) {
      let lis = document.getElementsByClassName('group-li');
      for (let i = 0; i < lis.length; i++) {
        lis[i].classList.remove('activeLi');
        if (index === i) {
          lis[i].classList.add('activeLi');
          this.defaultIndex = index;
          this.defaultGroupId = groupId;
        }
      }
      this.getSensorByGroupId(groupId);
    },
    // 每页多少条
    handleSizeChange(val) {
      this.pageSize = val;
      this.getSensorByGroupId(this.defaultGroupId);
    },
    // 当前页
    handleCurrentChange(val) {
      this.currentPage = val;
      this.getSensorByGroupId(this.defaultGroupId);
    },
    // 新增传感器
    addSensor() {
      this.flag = 'add';
      this.editDisabled = false;
      this.dialogShow = true;
    },
    // 编辑传感器
    editSensor(row) {
      this.sensorRow = row;
      this.ioTUserLists.map(async (item) => {
        if (row.enterpriseWlUserId === item.id) {
          await this.loginIoT({
            username: item.username,
            password: item.password,
          });
          await this.getDeviceInfo(row.facId);
        }
      });
      this.flag = 'edit';
      this.editDisabled = true;
      this.dialogShow = true;
    },
    // 关闭对话框
    closeDialog() {
      this.sensorRow = {};
      this.facIdOptions = [];
      this.facPassOptions = [];
      this.$refs.sensorRow.clearValidate();
      this.getSensorByGroupId(this.defaultGroupId);
    },
    // 删除传感器
    deleteSensor(id) {
      this.$confirm('此操作将删除选中的传感器, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning',
      })
        .then(async () => {
          const { data: res } = await this.$http.delete('zsSys/DeviceSensor/' + id);
          if (res.status !== 200) {
            return this.$message({
              duration: 2000,
              type: 'error',
              message: '删除传感器失败！',
            });
          }
          this.getAllGroup();
        })
        .catch(() => {
          this.$message({
            duration: 1000,
            type: 'info',
            message: '已取消操作',
          });
        });
    },
    // 保存传感器信息
    submitSensor: Debounce(function() {
      this.$refs.sensorRow.validate(async (valid) => {
        this.sensorRow.groupId = this.defaultGroupId;
        this.sensorRow.type = 1;
        let index = this.eleNumlist[this.sensorRow.facPass.slice(1) - 1];
        for (let i = 0; i < this.eleList.length; i++) {
          if (index == this.eleList[i].index) {
            this.sensorRow.unit = this.eleList[i].unit;
            this.sensorRow.preC = this.eleList[i].prec;
            this.sensorRow.number = this.eleList[i].index;
          }
        }
        if (!valid) return this.$message.error('信息填写不完整，请检查再提交！');
        if (this.flag === 'add') {
          const { data: res } = await this.$http.post('zsSys/DeviceSensor', this.sensorRow);
          if (res.status !== 200) {
            return this.$message({
              duration: 2000,
              type: 'error',
              message: '新增传感器失败！',
            });
          }
          this.dialogShow = false;
        } else if (this.flag === 'edit') {
          const { data: res } = await this.$http.put('zsSys/DeviceSensor', this.sensorRow);
          if (res.status !== 200) {
            return this.$message({
              duration: 2000,
              type: 'error',
              message: '修改传感器失败！',
            });
          }
          this.dialogShow = false;
        }
        this.getSensorByGroupId(this.defaultGroupId);
      });
    }, 3000),
    // 更改物联网账号选项
    changeIoTUserOptions(value) {
      this.sensorRow.facPass = null;
      this.sensorRow.facId = null;
      this.facIdOptions = [];
      this.facPassOptions = [];
      this.ioTUserLists.map((item) => {
        if (value === item.id) {
          this.loginIoT({
            username: item.username,
            password: item.password,
          });
        }
      });
    },
    // 解决数据层次太多，render函数没有自动更新，需手动强制刷新
    updateData() {
      this.$forceUpdate();
    },
    // 获取全部监控
    async getAllVideo() {
      this.defaultVideoIndex = 0;
      const { data: res } = await this.$http.get('zsSys/DeviceVideo/pageByEId', {
        params: { enterpriseId: window.sessionStorage.getItem('enterpriseId') },
      });
      if (res.status !== 200) return this.$message.error('获取全部监控失败!');
      res.data.rows.map((item) => {
        item.appKey = '';
        item.appSecret = '';
        let app = JSON.parse(item.accessToken);
        if (app) {
          item.app = app;
          if (app.appKey) {
            item.appKey = app.appKey;
          }
          if (app.appSecret) {
            item.appSecret = app.appSecret;
          }
        }
      });
      this.videoRows = res.data.rows;
      if (this.videoRows.length > 0) {
        this.flag2 = 'edit';
      } else {
        this.flag2 = 'add';
      }
      setTimeout(() => {
        this.changeVideo(this.defaultVideoIndex);
      }, 0);
    },
    // 获取全部管理员
    async getAllUser() {
      this.defaultUserIndex = 0;
      const { data: res } = await this.$http.get('zsSys/EnterpriseWLManager/pageByEId', {
        params: { enterpriseId: window.sessionStorage.getItem('enterpriseId') },
      });
      if (res.status !== 200) return this.$message.error('获取全部管理员失败!');
      this.userRows = res.data.rows;
      if (this.userRows.length > 0) {
        this.flag4 = 'edit';
      } else {
        this.flag4 = 'add';
      }
      setTimeout(() => {
        this.changeUser(this.defaultUserIndex);
      }, 0);
    },
    // 点击切换监控
    changeVideo(index) {
      let lis = document.getElementsByClassName('video-li');
      for (let i = 0; i < lis.length; i++) {
        lis[i].classList.remove('activeLi');
        if (index === i) {
          lis[i].classList.add('activeLi');
          this.defaultVideoIndex = index;
          this.videoRow = this.videoRows[this.defaultVideoIndex];
          this.flag2 = 'edit';
        }
      }
    },
    // 点击切换管理员
    changeUser(index) {
      let lis = document.getElementsByClassName('user-li');
      for (let i = 0; i < lis.length; i++) {
        lis[i].classList.remove('activeLi');
        if (index === i) {
          lis[i].classList.add('activeLi');
          this.defaultUserIndex = index;
          this.userRow = this.userRows[this.defaultUserIndex];
          this.flag4 = 'edit';
        }
      }
    },
    // 新增视频监控
    addVideo() {
      this.videoRow = {};
      this.flag2 = 'add';
    },
    // 新增管理员
    addUser() {
      this.userRow = {};
      this.flag4 = 'add';
    },
    // 删除监控
    deleteVideo(id) {
      this.$confirm('此操作将删除选中的监控, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning',
      })
        .then(async () => {
          const { data: res } = await this.$http.delete('zsSys/DeviceVideo/' + id);
          if (res.status !== 200) {
            return this.$message({
              duration: 2000,
              type: 'error',
              message: '删除监控失败！',
            });
          }
          this.$message({
            duration: 2000,
            type: 'success',
            message: '删除监控成功！',
          });
          this.getAllVideo();
        })
        .catch(() => {
          this.$message({
            duration: 1000,
            type: 'info',
            message: '已取消操作',
          });
        });
    },
    // 删除监控
    deleteUser(id) {
      this.$confirm('此操作将删除选中的管理员, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning',
      })
        .then(async () => {
          const { data: res } = await this.$http.delete('zsSys/EnterpriseWLManager/' + id);
          if (res.status !== 200) {
            return this.$message({
              duration: 2000,
              type: 'error',
              message: '删除管理员失败！',
            });
          }
          this.$message({
            duration: 2000,
            type: 'success',
            message: '删除管理员成功！',
          });
          this.getAllUser();
        })
        .catch(() => {
          this.$message({
            duration: 1000,
            type: 'info',
            message: '已取消操作',
          });
        });
    },
    // 保存监控信息
    submitVideo: Debounce(function() {
      this.videoRow.enterpriseId = window.sessionStorage.getItem('enterpriseId');
      this.$refs.videoRow.validate(async (valid) => {
        if (!valid) return this.$message.error('信息填写不完整，请检查再提交！');
        this.videoRow.accessToken = JSON.stringify({
          appKey: this.videoRow.appKey,
          appSecret: this.videoRow.appSecret,
        });
        if (this.flag2 === 'add') {
          const { data: res } = await this.$http.post('zsSys/DeviceVideo', this.videoRow);
          if (res.status !== 200) {
            return this.$message({
              duration: 2000,
              type: 'error',
              message: '新增监控失败！',
            });
          }
          this.$message({
            duration: 2000,
            type: 'success',
            message: '新增监控成功！',
          });
          this.getAllVideo();
        } else if (this.flag2 === 'edit') {
          const { data: res } = await this.$http.put('zsSys/DeviceVideo', this.videoRow);
          if (res.status !== 200) {
            return this.$message({
              duration: 2000,
              type: 'error',
              message: '修改监控失败！',
            });
          }
          this.$message({
            duration: 2000,
            type: 'success',
            message: '修改监控成功！',
          });
          this.getAllVideo();
        }
      });
    }, 3000),
    // 保存管理员信息
    submitUser: Debounce(function() {
      this.userRow.enterpriseId = window.sessionStorage.getItem('enterpriseId');
      this.$refs.userRow.validate(async (valid) => {
        if (!valid) return this.$message.error('信息填写不完整，请检查再提交！');
        if (this.flag4 === 'add') {
          const { data: res } = await this.$http.post('zsSys/EnterpriseWLManager', this.userRow);
          if (res.status !== 200) {
            return this.$message({
              duration: 2000,
              type: 'error',
              message: '新增管理员失败！',
            });
          }
          this.$message({
            duration: 2000,
            type: 'success',
            message: '新增管理员成功！',
          });
          this.getAllUser();
        } else if (this.flag4 === 'edit') {
          const { data: res } = await this.$http.put('zsSys/EnterpriseWLManager', this.userRow);
          if (res.status !== 200) {
            return this.$message({
              duration: 2000,
              type: 'error',
              message: '修改管理员失败！',
            });
          }
          this.$message({
            duration: 2000,
            type: 'success',
            message: '修改管理员成功！',
          });
          this.getAllUser();
        }
      });
    }, 3000),
    // 展示报警配置列表
    showThreshold(sensorId) {
      this.sensorId = sensorId;
      this.dialogThresholdInfoShow = true;
      this.getAllThreshold(sensorId);
    },
    // 获取全部报警配置列表
    async getAllThreshold(sensorId) {
      const { data: res } = await this.$http.get('zsSys/WarningThreshold/pageBySId', {
        params: {
          sensorId,
          page: this.currentThresholdPage,
          limit: this.pageThresholdSize,
        },
      });
      if (res.status !== 200) return this.$message.error('获取报警配置数据失败!');
      this.thresholdRows = res.data.rows;
      this.totalThreshold = res.data.total;
    },
    // 每页多少条
    handleThresholdSizeChange(val) {
      this.pageThresholdSize = val;
      this.getAllThreshold(this.sensorId);
    },
    // 当前页
    handleThresholdCurrentChange(val) {
      this.currentThresholdPage = val;
      this.getAllThreshold(this.sensorId);
    },
    // 新增阈值配置
    addThreshold() {
      this.flag3 = 'add';
      this.dialogThresholdShow = true;
      this.levelOptions.map((item) => {
        if (this.thresholdRows.length > 0) {
          this.thresholdRows.map((item2) => {
            if (item.value === item2.level) {
              item.disabled = true;
            } else {
              item.disabled = false;
            }
          });
        } else {
          item.disabled = false;
        }
      });
    },
    // 编辑阈值配置
    editThreshold(row) {
      this.thresholdRow = row;
      this.flag3 = 'edit';
      this.levelOptions.map((item) => {
        if (this.thresholdRows.length > 0) {
          this.thresholdRows.map((item2) => {
            if (item.value === item2.level) {
              item.disabled = true;
            } else {
              item.disabled = false;
            }
          });
        } else {
          item.disabled = false;
        }
      });
      this.dialogThresholdShow = true;
    },
    // 关闭对话框
    closeThresholdDialog() {
      this.thresholdRow = {};
      this.$refs.thresholdRow.clearValidate();
      this.getAllThreshold(this.sensorId);
    },
    // 删除阈值配置
    deleteThreshold(id) {
      this.$confirm('此操作将删除选中的报警阈值配置, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning',
      })
        .then(async () => {
          const { data: res } = await this.$http.delete('zsSys/WarningThreshold/' + id);
          if (res.status !== 200) {
            return this.$message({
              duration: 2000,
              type: 'error',
              message: '删除报警阈值配置失败！',
            });
          }
          this.$message({
            duration: 2000,
            type: 'success',
            message: '删除报警阈值配置成功！',
          });
          this.getAllThreshold(this.sensorId);
        })
        .catch(() => {
          this.$message({
            duration: 1000,
            type: 'info',
            message: '已取消操作',
          });
        });
    },
    // 保存阈值配置信息
    submitThreshold: Debounce(function() {
      this.$refs.thresholdRow.validate(async (valid) => {
        this.thresholdRow.sensorId = this.sensorId;
        if (!valid) return this.$message.error('信息填写不完整，请检查再提交！');
        if (this.flag3 === 'add') {
          const { data: res } = await this.$http.post('zsSys/WarningThreshold', this.thresholdRow);
          if (res.status !== 200) {
            return this.$message({
              duration: 2000,
              type: 'error',
              message: '新增报警阈值配置失败！',
            });
          }
          this.$message({
            duration: 2000,
            type: 'success',
            message: '新增报警阈值配置成功！',
          });
          this.dialogThresholdShow = false;
          this.getAllThreshold(this.sensorId);
        } else if (this.flag3 === 'edit') {
          const { data: res } = await this.$http.put('zsSys/WarningThreshold', this.thresholdRow);
          if (res.status !== 200) {
            return this.$message({
              duration: 2000,
              type: 'error',
              message: '修改报警阈值配置失败！',
            });
          }
          this.$message({
            duration: 2000,
            type: 'success',
            message: '修改报警阈值配置成功！',
          });
          this.dialogThresholdShow = false;
          this.getAllThreshold(this.sensorId);
        }
      });
    }, 3000),
    // 解决只能输入数字输入框的值绑定问题
    maxValueChange(e) {
      this.$set(this.thresholdRow, 'maxValue', e.target.value);
    },
    minValueChange(e) {
      this.$set(this.thresholdRow, 'minValue', e.target.value);
    },
    sendIntervalChange(e) {
      this.$set(this.thresholdRow, 'sendInterval', e.target.value);
    },
    phoneChange(e) {
      this.$set(this.userRow, 'phone', e.target.value);
    },
    // 文字截取
    textSubstr(value, length) {
      let val = value;
      if (value == '' || value == undefined) {
        return '';
      }
      if (value.length > length) {
        val = val.substring(0, length) + '...';
      }
      return val;
    },
    // 根据id获取name
    getUserName(id) {
      if (id) {
        for (let i = 0; i < this.userRows.length; i++) {
          if (id === this.userRows[i].id) {
            return this.userRows[i].name;
          }
        }
      } else {
        return '';
      }
    },
  },
};
</script>

<style lang="less" scoped>
// 容器样式
.container {
  width: 100%;
  height: 100%;
  display: flex;

  // 设备
  .container-group {
    width: 220px;
    height: 100%;

    // 新增设备样式
    .el-button {
      margin: 10px;
      margin-top: 0;
    }

    // 设备栏样式
    ul {
      width: 100%;
      margin: 0;
      padding: 0;
      list-style: none;
      text-align: center;

      .group-li:nth-child(1) {
        border-top: 1px solid #e1e1e1;
      }

      // 单个设备样式
      .group-li {
        width: 100%;
        height: 40px;
        line-height: 40px;
        font-size: 14px;
        color: #606266;
        border-bottom: 1px solid #e1e1e1;
        cursor: pointer;
        position: relative;

        // 删除标签样式
        i {
          color: #373a40;
          font-size: 16px;
          font-weight: 600;
          position: absolute;
          right: 2px;
          top: 2px;
        }
      }

      // 活跃设备
      .activeLi {
        background-color: rgb(45, 183, 245);
        color: #fff;
        border: 0;

        i {
          color: #fff;
        }
      }
    }

    // 隐藏滚动条
    ul::-webkit-scrollbar {
      width: 0 !important;
      height: 0;
    }
  }

  // 传感器
  .container-sensor {
    width: 100%;
    height: 100%;
    position: relative;
    left: 0;
    bottom: 0;
    margin-left: 10px;

    .data-table {
      position: absolute;
      top: 52px;
      right: 0;
      left: 0;
      bottom: 32px;
      margin-bottom: 10px;

      .el-table {
        margin-top: 0;
      }
    }

    .el-pagination {
      height: 32px;
      bottom: 0;
      left: 0;
      right: 0;
      position: absolute;
      margin-top: 0;
      margin-bottom: 0;
    }

    // 新增传感器样式
    .el-button {
      margin: 10px 0;
    }

    // 滚动条样式
    .table-scroll-bar::-webkit-scrollbar {
      width: 6px !important;
      height: 10px;
    }

    //滑块部分
    .table-scroll-bar::-webkit-scrollbar-thumb {
      height: 50%;
      border-radius: 5px;
      background-color: #bbbfca;
    }
  }

  // 监控列表
  .container-list {
    width: 220px;
    height: 100%;

    // 新增监控列表样式
    .el-button {
      margin: 10px;
      margin-top: 0;
    }

    // 监控列表栏样式
    ul {
      width: 100%;
      margin: 0;
      padding: 0;
      list-style: none;
      text-align: center;

      .video-li:nth-child(1) {
        border-top: 1px solid #e1e1e1;
      }

      // 单个监控样式
      .video-li {
        width: 100%;
        height: 40px;
        line-height: 40px;
        font-size: 14px;
        color: #606266;
        border-bottom: 1px solid #e1e1e1;
        cursor: pointer;
        position: relative;

        // 删除标签样式
        i {
          color: #373a40;
          font-size: 16px;
          font-weight: 600;
          position: absolute;
          right: 2px;
          top: 2px;
        }
      }

      // 活跃监控
      .activeLi {
        background-color: rgb(45, 183, 245);
        color: #fff;
        border: 0;

        i {
          color: #fff;
        }
      }
    }

    // 隐藏滚动条
    ul::-webkit-scrollbar {
      width: 0 !important;
      height: 0;
    }
  }

  // 监控
  .container-video {
    width: 100%;
    height: 100%;
    margin-left: 10px;
    display: flex;
    justify-content: center;
    align-items: center;

    .box-card {
      width: 90%;
      height: 90%;

      // 对话框
      .row-form {
        margin-top: 20px;

        .el-input,
        .el-select {
          width: 100%;
        }

        .row-btn {
          margin-top: 20px;
          display: inline-block;
          width: 100%;
          text-align: center;
        }
      }
    }
  }

  // 管理员列表
  .container-users {
    width: 220px;
    height: 100%;

    // 新增监控列表样式
    .el-button {
      margin: 10px;
      margin-top: 0;
    }

    // 监控列表栏样式
    ul {
      width: 100%;
      margin: 0;
      padding: 0;
      list-style: none;
      text-align: center;

      .user-li:nth-child(1) {
        border-top: 1px solid #e1e1e1;
      }

      // 单个监控样式
      .user-li {
        width: 100%;
        height: 40px;
        line-height: 40px;
        font-size: 14px;
        color: #606266;
        border-bottom: 1px solid #e1e1e1;
        cursor: pointer;
        position: relative;

        // 删除标签样式
        i {
          color: #373a40;
          font-size: 16px;
          font-weight: 600;
          position: absolute;
          right: 2px;
          top: 2px;
        }
      }

      // 活跃监控
      .activeLi {
        background-color: rgb(45, 183, 245);
        color: #fff;
        border: 0;

        i {
          color: #fff;
        }
      }
    }

    // 隐藏滚动条
    ul::-webkit-scrollbar {
      width: 0 !important;
      height: 0;
    }
  }

  // 管理员
  .container-user {
    width: 100%;
    height: 100%;
    margin-left: 10px;
    display: flex;
    justify-content: center;
    align-items: center;

    .box-card {
      width: 90%;
      height: 90%;

      // 对话框
      .row-form {
        margin-top: 20px;

        .el-input,
        .el-select {
          width: 100%;
        }

        .row-btn {
          margin-top: 20px;
          display: inline-block;
          width: 100%;
          text-align: center;
        }
      }
    }
  }

  // 对话框
  .row-form {
    .el-input,
    .el-select {
      width: 100%;
    }

    .row-btn {
      display: inline-block;
      width: 100%;
      text-align: center;
    }
  }

  // 默认element样式修改
  .el-table {
    .el-button {
      margin: 5px;
    }
  }
}
</style>
<style lang="less">
.threshold-dialog {
  .el-dialog__body {
    padding-top: 0;
  }

  .el-dialog {
    height: 70%;
  }

  .data-table {
    position: absolute;
    top: 102px;
    right: 0;
    left: 0;
    bottom: 32px;
    margin-bottom: 10px;

    .el-table {
      margin-top: 0;
      position: absolute;
      top: 54px;
      bottom: 0;
      left: 0;
      right: 0;
    }
  }

  .el-pagination {
    height: 32px;
    bottom: 0;
    left: 0;
    right: 0;
    position: absolute;
    margin-top: 0;
    margin-bottom: 0;
  }
}
</style>
