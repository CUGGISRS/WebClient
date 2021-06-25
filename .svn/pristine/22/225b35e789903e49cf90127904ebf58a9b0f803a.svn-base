<template>
  <div>
    <!-- 面包屑导航区域 -->
    <el-breadcrumb separator-class="el-icon-arrow-right">
      <el-breadcrumb-item :to="{ path: '/home' }">首页</el-breadcrumb-item>
      <el-breadcrumb-item>公益性子系统</el-breadcrumb-item>
      <el-breadcrumb-item>轮播图管理</el-breadcrumb-item>
    </el-breadcrumb>

    <!-- 卡片视图区域 -->
    <el-card>
      <!-- 添加区域 -->
      <div>
        <el-button type="danger" size="small" icon="el-icon-delete" @click="handleBatchRemove()">批量删除</el-button>
        <el-button type="primary" size="small" icon="el-icon-circle-plus" @click="addSwiperInfo">添加</el-button>
      </div>

      <!-- 表格显示区域 -->
      <el-table
        :data="rows"
        :height="curHeight"
        v-loading="listLoading"
        border
        stripe
        fit
        highlight-current-row
        :row-style="{ height: '5px' }"
        :cell-style="{ padding: '5px 0' }"
        @selection-change="checkSelect"
      >
        <el-table-column type="selection" width="40" label="全选"></el-table-column>
        <el-table-column align="center" label="序号" width="60">
          <template slot-scope="scope">
            {{ (listQuery.page - 1) * listQuery.size + scope.$index + 1 }}
          </template>
        </el-table-column>
        <el-table-column align="center" label="文章类型" width="150">
          <template slot-scope="scope">
            {{ scope.row.articleTypeid }}
          </template>
        </el-table-column>
        <el-table-column align="center" label="文章标题" min-width="400">
          <template slot-scope="scope">
            {{ scope.row.title }}
          </template>
        </el-table-column>
        <el-table-column align="center" label="产品图片" width="260">
          <template slot-scope="scope">
            <div class="products-img-container">
              <el-image :preview-src-list="srcList" :src="scope.row.path"></el-image>
            </div>
          </template>
        </el-table-column>
        <el-table-column align="center" label="是否发布" width="120">
          <template slot-scope="scope">
            <el-switch
              v-model="scope.row.status"
              inactive-color="#ff4949"
              active-value="1"
              inactive-value="0"
              @change="changSwitch($event, scope.row)"
            >
            </el-switch>
          </template>
        </el-table-column>
        <el-table-column align="center" label="发布者" width="120">
          <template slot-scope="scope">
            {{ scope.row.publisher }}
          </template>
        </el-table-column>
        <el-table-column align="center" label="操作" width="200">
          <template slot-scope="scope">
            <el-button type="primary" size="mini" icon="el-icon-edit" @click="editSwiperInfo(scope.row)"
              >编辑
            </el-button>
            <el-button type="danger" size="mini" icon="el-icon-delete" @click="deleteSwiperInfo(scope.row.id)"
              >删除
            </el-button>
          </template>
        </el-table-column>
      </el-table>
      <!-- 分页 -->
      <el-pagination
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
        :current-page="listQuery.page"
        :page-sizes="[5, 10, 15, 20]"
        :page-size="listQuery.size"
        layout="total, sizes, prev, pager, next, jumper"
        :total="total"
      >
      </el-pagination>
    </el-card>

    <el-dialog
      v-cloak
      title="轮播图信息"
      @close="closeDialog"
      center
      :visible.sync="dialogShow"
      top="7vh"
      width="70%"
      class="row-form"
    >
      <el-form :model="row" label-width="100px" :rules="rules" ref="row">
        <el-tabs type="border-card">
          <el-tab-pane label="轮播图信息">
            <el-row>
              <el-col :span="12">
                <el-form-item label="系统类型：" prop="targetSystem">
                  <el-select
                    allow-create
                    filterable
                    v-model="row.targetSystem"
                    placeholder="请选择系统类型"
                    clearable
                    style="width: 300px"
                  >
                    <el-option v-for="item in sysTypeOptions" :key="item" :label="item" :value="item" />
                  </el-select>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="文章类型：" prop="articleTypeid">
                  <el-select
                    allow-create
                    filterable
                    v-model="row.articleTypeid"
                    placeholder="请选择文章类型"
                    clearable
                    style="width: 300px"
                    @change="getArticleListBySystemType"
                  >
                    <el-option v-for="item in articleTypeOptions" :key="item" :label="item" :value="item" />
                  </el-select>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="20">
                <el-form-item label="文章标题：" prop="title" @focus="getArticleListBySystemType">
                  <el-select filterable v-model="row.title" placeholder="请选择文章标题" clearable style="width: 500px">
                    <el-option
                      v-for="item in articleTitleOptions"
                      :key="item.id"
                      :label="item.title"
                      :value="item.id"
                    />
                  </el-select>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="12">
                <el-form-item label="发布者：" prop="publisher">
                  <el-input v-model="row.publisher" placeholder="请输入发布者名称" style="width: 300px"></el-input>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="发布状态：" prop="status">
                  <el-radio v-model="row.status" label="1">发布</el-radio>
                  <el-radio v-model="row.status" label="0">未发布</el-radio>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="12">
                <el-form-item label="轮播图图片:">
                  <el-upload
                    :action="uploadUrl"
                    accept="image/jpeg,image/png,image/jpg"
                    :class="{ disabled: uploadDisabled }"
                    list-type="picture-card"
                    :on-change="handleChange"
                    :on-preview="handlePictureCardPreview"
                    :on-remove="handleRemove"
                    :http-request="httpRequest"
                    :auto-upload="false"
                    :file-list="swiperListPhoto"
                  >
                    <i class="el-icon-plus"></i>
                  </el-upload>
                  <el-dialog :modal="false" :visible.sync="dialogVisible">
                    <img width="100%" :src="dialogImageUrl" alt="" />
                  </el-dialog>
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
import userService from '@/globals/service/user.js';
import { Debounce } from '@/utils/tool.js';

export default {
  data() {
    return {
      //删除ids数组
      ids: [],
      // 表单高度
      curHeight: 0,
      //列表加载圈圈
      listLoading: false,
      //列表渲染db
      rows: [],
      //新增编辑弹出框对象
      row: {
        imgId: '',
        targetSystem: '',
        articleTypeid: '',
        title: '',
        publisher: '',
        status: '',
        orderId: []
      },
      //预览图片数组
      srcList: [],
      //图片弹出框地址
      dialogImageUrl: '',
      //预览图片弹出框
      dialogVisible: false,
      //是否显示添加图片按钮
      uploadDisabled: false,
      //显示预览图片弹出框
      dialogShow: false,
      //上传图片的数量
      limitcount: 1,
      //上传列表中的图片
      swiperListPhoto: [],
      //删除列表中的图片
      // swiperRemoveListPhoto: [],
      //系统类型选择
      sysTypeOptions: ['公益性系统'],
      //文章类型
      articleTypeOptions: [
        '政策法规',
        '农业要闻',
        '灾害预警',
        '种植技术',
        '水产养殖技术',
        '畜牧兽医技术',
        '农机技术',
        '时政要闻',
        '科技要闻'
      ],
      //文章标题
      articleTitleOptions: [],
      //轮播图排序orderId
      swiperOrderOptions: [
        { label: '1', disabled: false },
        { label: '2', disabled: false },
        { label: '3', disabled: false },
        { label: '4', disabled: false },
        { label: '5', disabled: false }
      ],
      //图片上传地址
      uploadUrl: '#',
      //分页对象
      listQuery: {
        page: 1,
        size: 5,
        //目标系统
        targetSystem: '公益性系统'
      },
      rules: {
        targetSystem: [{ required: true, message: '请选择系统类型', trigger: 'blur' }],
        articleTypeid: [{ required: true, message: '请选择文章类型', trigger: 'blur' }],
        title: [{ required: true, message: '请选择文章标题', trigger: 'blur' }],
        publisher: [{ required: true, message: '请输入发布者名称', trigger: 'blur' }],
        status: [{ required: true, message: '请选择发布状态', trigger: 'blur' }],
        orderId: [{ required: true, message: '请选择图片排序', trigger: 'blur' }]
      },
      total: 0,
      flag: 'add'
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
    //获取所有列表数据
    getAllList() {
      this.listLoading = true;
      let params = this.listQuery;
      userService.getInformationSwiperList(params).then(res => {
        if (res.status !== 200) return this.$message.error('获取失败');
        this.rows = res.data.rows;
        this.listLoading = false;
        this.total = res.data.total;
      });
    },
    //全选框事件
    checkSelect(data) {
      data.forEach(item => {
        this.ids.push(item.id);
      });
    },
    async handleBatchRemove() {
      if (this.ids.length === 0) return this.$message.warning('请先选中要删除的轮播图信息');
      const confirmResult = await this.$confirm('此操作将删除选中轮播图信息,是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).catch(err => err);
      if (confirmResult !== 'confirm') return this.$message.info('已经取消删除');
      this.ids.forEach(id => {
        userService.swiperInformationDelete(id).then(res => {
          if (res.status !== 200) return this.$message.error('删除轮播图信息失败');
          this.$message.success('删除轮播图信息成功');
          this.getAllList();
        });
      });
    },
    // 处理图片
    httpRequest(data) {
      let rd = new FileReader(); // 创建文件读取对象
      let file = data.file;
      rd.readAsDataURL(file); // 文件读取装换为base64类型
      rd.onloadend = function() {
        this.dialogImageUrl = this.result; // this指向当前方法onloadend的作用域
      };
    },
    //图片文件改变
    handleChange(file, fileList) {
      this.uploadDisabled = fileList.length >= this.limitcount;
      this.swiperListPhoto = fileList;
    },
    //删除图片
    handleRemove(file, fileList) {
      // this.swiperRemoveListPhoto.forEach((item, index) => {
      //   if (item.key == file.uid) {
      //     this.swiperRemoveListPhoto.splice(index, 1);
      //   }
      // });
      this.uploadDisabled = fileList.length >= this.limitcount;
      this.swiperListPhoto = fileList;
    },
    //上传图片的钩子
    handlePictureCardPreview(file) {
      this.dialogImageUrl = file.url;
      this.dialogVisible = true;
    },
    //提交按钮
    submitRow: Debounce(function() {
      this.$refs.row.validate(valid => {
        if (!valid) return this.$message.error('信息填写不完整或不准确，请检查再提交！');
        if (this.swiperListPhoto.length == 0) return this.$message.error('请上传图片！');
        let formData = new FormData();
        formData.append('file', this.swiperListPhoto[0].raw);
        formData.append('targetSystem', this.row.targetSystem);
        formData.append('articleTypeid', this.row.articleTypeid);
        formData.append('publisher', this.row.publisher);
        formData.append('articleId', this.row.articleId);
        formData.append('status', this.row.status);
        // formData.append('orderId', this.row.orderId);
        if (this.flag === 'add') {
          userService.addCarousel(formData).then(res => {
            if (res.status !== 200) return this.$message.error('失败');
            this.$message.success('新增成功');
            // 重新获取列表
            this.getAllList();
            this.dialogShow = false;
          }, 3000);
        } else if (this.flag === 'edit') {
          formData.append('id', this.row.id);
          formData.append('url', 'url');
          formData.append('imgId', this.row.imgId);
          userService.updateCarousel(formData).then(res => {
            if (res.status !== 200) return this.$message.error('失败');
            this.$message.success('更新成功');
            // 重新获取列表
            this.getAllList();
            this.dialogShow = false;
          }, 3000);
        }
      });
    }),
    //发布状态的按钮
    changSwitch(data, item) {
      let id = item.id;
      let status = item.status;
      let formData = new FormData();
      formData.append('id', id);
      formData.append('status', status);
      userService.swiperStatusEdit(formData).then(res => {
        if (res.status !== 200) return this.$message.error('状态更新失败');
        this.$message.success('状态更新成功');
        // 重新获取列表
        this.getAllList();
      });
    },
    //增加轮播图按钮事件
    addSwiperInfo() {
      this.flag = 'add';
      this.row.targetSystem = this.sysTypeOptions[0]; // 给默认目标系统
      this.row.articleTypeid = this.articleTypeOptions[0]; // 给默认文章类型
      this.getArticleListBySystemType();
      // this.row.orderId = this.swiperOrderOptions[0].label; // 初始化轮播图顺序
      // this.row.status = '1'; //发布状态初始化为已发布
      this.dialogShow = true;
    },
    getArticleListBySystemType() {
      let searchQuery = {
        pageNum: 1,
        pageRow: 10,
        sysType: this.row.targetSystem,
        articleType: this.row.articleTypeid,
        status: 1,
        title: this.row.title
      };

      // //根据系统名称和文章类型模糊查询文章。限制条数。
      userService.getArticleList(searchQuery).then(res => {
        if (res.status === 200) {
          this.row.title = '';
          this.articleTitleOptions = [];

          res.data.rows.forEach(item => {
            this.articleTitleOptions.push(item);
          });
        }
      });
    },
    //编辑按钮事件
    editSwiperInfo(row) {
      this.flag = 'edit';
      this.row = row;
      this.row.imgId = row.img;
      this.row.articleId = row.articleId;
      this.row.targetSystem = this.sysTypeOptions[0]; // 给默认目标系统
      this.row.articleTypeid = this.articleTypeOptions[0]; // 给默认文章类型
      let swiperPhotoObj = { name: row.img, url: row.path };
      this.swiperListPhoto.push(swiperPhotoObj);
      this.uploadDisabled = this.swiperListPhoto.length >= this.limitcount;
      this.dialogShow = true;
    },
    //删除轮播图信息按钮
    async deleteSwiperInfo(id) {
      const confirmResult = await this.$confirm('此操作将删除该轮播图信息，是否继续？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).catch(err => err);
      if (confirmResult !== 'confirm') return this.$message.info('已经取消删除');
      userService.swiperInformationDelete(id).then(res => {
        if (res.status !== 200) return this.$message.error('删除轮播图信息失败');
        this.$message.success('删除轮播图信息成功');
        this.getAllList();
      });
    },
    //关闭对话框
    closeDialog() {
      this.row = {};
      this.swiperListPhoto = [];
      this.uploadDisabled = false;
      this.$refs.row.clearValidate();
      this.getAllList();
    },
    // 当前页
    handleCurrentChange(page) {
      this.listQuery.page = page;
      this.getAllList();
    },
    // 每页多少条
    handleSizeChange(size) {
      this.listQuery.size = size;
      this.getAllList();
    },
    // 设定表格高度
    setTableHeight() {
      let h = document.documentElement.clientHeight || document.body.clientHeight;
      this.curHeight = h - 280;
    }
  }
};
</script>

<style lang="less">
.products-img-container {
  height: 140px !important;
  padding: 10px;
  overflow: auto;
}

.disabled .el-upload--picture-card {
  display: none;
}

.upload-div {
  width: 350px;
  display: flex;
  justify-content: start;
  align-items: center;
  flex-wrap: wrap;
}

.upload-div-del {
  cursor: pointer;
}

.upload-div-del:hover {
  color: #64b7ff;
}

.el-image-preview {
  width: 130px;
  height: 130px;
}
</style>
