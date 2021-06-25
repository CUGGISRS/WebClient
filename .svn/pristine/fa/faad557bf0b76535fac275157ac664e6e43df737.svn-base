<template>
  <div>
    <!-- 面包屑导航区域 -->
    <el-breadcrumb separator-class="el-icon-arrow-right">
      <el-breadcrumb-item :to="{ path: '/home' }">首页</el-breadcrumb-item>
      <el-breadcrumb-item>公益性子系统</el-breadcrumb-item>
      <el-breadcrumb-item>农业视频管理</el-breadcrumb-item>
    </el-breadcrumb>
    <el-card>
      <!-- 添加区域 -->
      <div>
        <el-button type="danger" size="small" icon="el-icon-delete" @click="handleBatchRemoveVideo()"
          >批量删除视频信息</el-button
        >
        <el-button type="primary" size="small" icon="el-icon-circle-plus" @click="addRow">添加视频信息</el-button>
        <el-input
          v-model="listQuery.expertName"
          size="small"
          style="width: 200px;margin-left: 20px"
          clearable
          @input="handleFilter"
          placeholder="视频名称"
        ></el-input>
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
        <el-table-column align="center" label="视频名称" min-width="160">
          <template slot-scope="scope">
            <el-tooltip effect="light" :content="scope.row.videoName" placement="top">
              <el-link type="primary" :underline="false" @click="playDialogRow(scope.row)">{{
                scope.row.videoName
              }}</el-link>
            </el-tooltip>
          </template>
        </el-table-column>
        <el-table-column align="center" label="作者" width="160">
          <template slot-scope="scope">
            {{ scope.row.author }}
          </template>
        </el-table-column>
        <el-table-column align="center" label="视频分类" width="120">
          <template slot-scope="scope">
            <span>{{ scope.row.type }}</span>
          </template>
        </el-table-column>
        <el-table-column align="center" label="浏览量" width="120">
          <template slot-scope="scope">
            <span>{{ scope.row.watchTimes }}</span>
          </template>
        </el-table-column>
        <el-table-column align="center" label="发布时间" width="160">
          <template slot-scope="scope">
            <span>{{ scope.row.pubTime }}</span>
          </template>
        </el-table-column>
        <el-table-column align="center" label="视频描述" min-width="160">
          <template slot-scope="scope">
            <el-popover placement="top-start" title="视频描述" width="250" trigger="hover">
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
        <el-table-column align="center" label="视频状态" width="120">
          <template slot-scope="scope">
            <span v-if="scope.row.state === 0" style="color: #F78989">上传失败</span>
            <span v-else style="color: #67C23A">上传成功</span>
          </template>
        </el-table-column>
        <el-table-column align="center" label="操作" width="220">
          <template slot-scope="scope">
            <el-button size="mini" type="primary" icon="el-icon-video-play" @click="playDialogRow(scope.row)"
              >播放视频</el-button
            >
            <el-button size="mini" type="danger" icon="el-icon-delete" @click="deleteRow(scope.row.id)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
      <!-- 分页 -->
      <el-pagination
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
        :current-page="listQuery.page"
        :page-sizes="[15, 20, 25, 30]"
        :page-size="listQuery.limit"
        layout="total, sizes, prev, pager, next, jumper"
        :total="total"
      >
      </el-pagination>
    </el-card>

    <!-- 视频播放对话框 -->
    <el-dialog title :visible.sync="dialogPlay" width="60%" @close="closeDialog">
      <video :src="videoUrl" controls autoplay class="video" width="100%"></video>
    </el-dialog>

    <!-- 新增视频信息对话框 -->
    <el-dialog
      v-cloak
      title="视频信息管理"
      @close="DialogClose()"
      center
      :visible.sync="dialogShow"
      top="7vh"
      width="50%"
      class="row-form"
    >
      <el-form :model="row" label-width="100px" class="demo-ruleForm" :rules="rules" ref="addFormRef">
        <el-tabs type="border-card">
          <el-tab-pane label="视频基本信息">
            <el-row>
              <el-col :span="12">
                <el-form-item label="视频名称:" prop="videoName">
                  <el-input v-model="row.videoName" clearable maxlength="20" placeholder="请输入视频名称"></el-input>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="作者" prop="author">
                  <el-input v-model="row.author" clearable placeholder="请输入作者"></el-input>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="12">
                <el-form-item label="视频分类:" prop="type">
                  <el-select v-model="row.type" placeholder="请输入视频分类">
                    <el-option
                      v-for="item in videoTypeOptions"
                      :key="item.display_name"
                      :label="item.display_name"
                      :value="item.display_name"
                    >
                    </el-option>
                  </el-select>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="浏览量:" prop="watchTimes">
                  <el-input
                    v-model="row.watchTimes"
                    onkeyup="this.value=this.value.replace(/[^\d]/g,'')"
                    placeholder="请输入浏览量"
                  ></el-input>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="24">
                <el-form-item label="发布时间:" prop="pubTime">
                  <el-date-picker
                    v-model="row.pubTime"
                    :clearable="false"
                    type="datetime"
                    style="width: 100%"
                    placeholder="选择发布时间"
                  >
                  </el-date-picker>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="24">
                <el-form-item label="视频描述:" prop="description">
                  <el-input
                    v-model="row.description"
                    clearable
                    type="textarea"
                    :autosize="{ minRows: 5, maxRows: 10 }"
                    placeholder="请输入视频描述"
                  ></el-input>
                </el-form-item>
              </el-col>
            </el-row>

            <el-row>
              <el-col :span="12" class="register-upload">
                <el-form-item label="上传视频:" prop="videoFile">
                  <el-upload
                    class="upload-demo"
                    ref="upload"
                    v-model="row.videoFile"
                    drag
                    action
                    :before-upload="beforeUploadVideo"
                    :on-change="handleChange"
                    :limit="1"
                    :on-exceed="handleExceed"
                    :file-list="fileList"
                  >
                    <i class="el-icon-upload"></i>
                    <div class="el-upload__text">将文件拖到此处，或<em>点击上传</em></div>
                    <div class="el-upload__tip" slot="tip">只能上传视频文件，且不超过500M</div>
                  </el-upload>
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

const videoTypeOptions = [
  { key: '专家视频', display_name: '专家视频' },
  { key: '农业科技', display_name: '农业科技' },
  { key: '农技讲座', display_name: '农技讲座' },
  { key: '法律法规', display_name: '法律法规' },
  { key: '种植视频', display_name: '种植视频' },
  { key: '养殖视频', display_name: '养殖视频' },
  { key: '渔业生产', display_name: '渔业生产' },
  { key: '蔬菜行业', display_name: '蔬菜行业' },
  { key: '其他', display_name: '其他' },
];

export default {
  data() {
    return {
      videoTypeOptions,
      activeName: '',
      // 搜索条件
      listQuery: {
        page: 1,
        limit: 15,
        videoName: '' // 视频名称
      },
      total: 0,
      // 数据列表
      rows: [],
      // 表单数据
      row: {
        videoName: '', // 视频名称
        author: '', // 作者
        type: '', //视频分类
        description: '', // 视频描述
        state: '', // 视频状态 0表示未完成，1表示已完成
        watchTimes: '0', // 浏览量
        pubTime: '', // 发布时间
        videoFile: null
      },
      video: '',
      // 表单高度
      curHeight: 0,
      // 新增信息弹窗
      dialogShow: false,
      listLoading: false,
      // 文件上传
      videoUrl: '',
      // videoFile: null,
      dialogVideoVisible: false,
      // 上传文件
      multfileImg: null,
      //提交状态
      flag: 'add',
      // 验证规则
      rules: {
        videoFile: [{ required: true, message: '请上传视频', trigger: 'change' }],
        videoName: [{ required: true, message: '请输入视频名称', trigger: 'blur' }],
        author: [{ required: true, message: '请输入作者名称', trigger: 'blur' }],
        type: [{ required: true, message: '请输入视频类型', trigger: 'blur' }],
        description: [{ required: true, message: '请输入视频描述', trigger: 'blur' }],
        watchTimes: [{ required: true, message: '请输入浏览量', trigger: 'blur' }]
      },
      //视频文件列表
      fileList: [],
      //删除ids数组
      ids: [],
      dialogVideo: '',
      dialogPlay: false
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
    //关闭视频播放对话框
    closeDialog() {
      this.videoUrl = ''; //清空数据 关闭视频播放
    },
    handleChange(file, fileList) {
      this.row.videoFile = file;

      this.fileList = [fileList[fileList.length - 1]];
      this.$refs.addFormRef.clearValidate();
    },
    handleExceed() {
      this.$message.warning('只能上传一个视频');
    },
    //上传前回调
    beforeUploadVideo(file) {
      var fileSize = file.size / 1024 / 1024 < 500;
      if (
        ['video/mp4', 'video/ogg', 'video/flv', 'video/avi', 'video/wmv', 'video/rmvb', 'video/mov'].indexOf(
          file.type
        ) === -1
      ) {
        this.$message.error('不支持此视频格式');
        this.fileList = [];
      } else if (!fileSize) {
        this.$message.error('视频大小不能超过500MB');
        this.fileList = [];
      } else {
        this.row.videoFile = file;
      }

      // 不使用upload自带的上传方式，而是使用axios，所以阻止upload自带的上传
      return false;
    },
    // 获取供求信息
    getAllList() {
      this.listLoading = true;
      let params = this.listQuery;
      userService.getVideoList(params).then(res => {
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
    //批量删除视频信息
    async handleBatchRemoveVideo() {
      if (this.ids.length === 0) return this.$message.warning('请先选中要删除的视频信息');
      const confirmResult = await this.$confirm('此操作将删除选中视频信息,是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).catch(err => err);
      if (confirmResult !== 'confirm') return this.$message.info('已经取消删除');
      userService.videoInfoDel(this.ids).then(res => {
        if (res.status !== 200) return this.$message.error('删除视频信息失败');
        this.$message.success('删除视频信息成功');
        this.getAllList();
      });
    },
    //全选框事件
    checkSelect(data) {
      data.forEach(item => {
        this.ids.push(item.id);
      });
    },
    // 新增
    addRow() {
      this.flag = 'add';
      this.row.watchTimes = 0;
      this.row.pubTime = this.$moment(new Date()).format('YYYY-MM-DD HH:mm:ss');
      this.row.type = this.videoTypeOptions[0].display_name;
      this.videoUrl = '';
      this.dialogShow = true;
    },
    // 编辑
    playDialogRow(row) {
      this.dialogPlay = true;
      this.videoUrl = 'http://' + row.path;
    },
    handleModifyStatus(row, item) {
      row.state = item;
    },
    //获取分页每页显示数据条数
    handleSizeChange(newSize) {
      this.listQuery.limit = newSize;
      this.getAllList();
    },
    //获取当前分页页数
    handleCurrentChange(newPage) {
      this.listQuery.page = newPage;
      this.getAllList();
    },
    // 删除
    async deleteRow(id) {
      let ids = [];
      ids.push(id);
      const confirmResult = await this.$confirm('此操作将删除该视频信息,是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).catch(err => err);
      if (confirmResult !== 'confirm') return this.$message.info('已经取消删除');
      userService.videoInfoDel(ids).then(res => {
        if (res.status !== 200) return this.$message.error('删除视频信息失败');
        this.$message.success('删除视频信息成功');
        this.getAllList();
      });
    },
    DialogClose() {
      this.$refs.addFormRef.resetFields();
      this.$refs.addFormRef.clearValidate();
      this.row.videoFile = '';
      this.$refs.upload.clearFiles();
      this.getAllList();
    },
    // 设定表格高度
    setTableHeight() {
      let h = document.documentElement.clientHeight || document.body.clientHeight;
      this.curHeight = h - 280;
    },
    // 提交 供求信息
    submitRow() {
      // 点击提交，先向数据库插入一条视频信息。
      // 获取视频后缀
      // var extension = this.video.name.substring(this.video.name.lastIndexOf('.'), this.video.name.length);
      // let params ={
      //   videoName: this.video.name,  // 视频名称
      //   watchTimes: this.row.watchTimes, // 浏览量
      //   description: this.row.description, // 描述
      //   type: this.row.type, // 视频分类
      //   extension:extension   // 视频后缀
      // }

      this.$refs.addFormRef.validate(valid => {
        if (!valid) return this.$message.error('信息填写不完整或不准确，请检查再提交！');
        let formData = new FormData();
        formData.append('videoName', this.row.videoName);
        formData.append('author', this.row.author);
        formData.append('type', this.row.type);
        formData.append('description', this.row.description);
        formData.append('watchTimes', this.row.watchTimes);
        formData.append('file', this.row.videoFile);
        userService
          .videoInfoAdd(formData)
          .then(res => {
            if (res.status !== 200) return this.$message.error('失败');
            this.$message.success('新增成功');
            // 隐藏对话框
            this.dialogShow = false;
            // 重新获取列表信息
            this.getAllList();
          })
          .catch(() => {
            this.$message({ type: 'error', message: '异常' });
          });
      });
    }
  }
};
</script>

<style lang="less" scoped>
.register-upload {
  .el-upload__tip {
    min-width: 300px;
  }
}
</style>
