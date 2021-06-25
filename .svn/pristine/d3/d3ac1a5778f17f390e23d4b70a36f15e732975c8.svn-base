<template>
  <div class="article-page">
    <!-- 面包屑导航区域 -->
    <el-breadcrumb separator-class="el-icon-arrow-right">
      <el-breadcrumb-item :to="{ path: '/home' }">首页</el-breadcrumb-item>
      <el-breadcrumb-item>电子商务子系统</el-breadcrumb-item>
      <el-breadcrumb-item>文章管理</el-breadcrumb-item>
    </el-breadcrumb>
    <el-tabs v-model="activeName" @tab-click="handleClick">
      <!-- 文章管理栏 -->
      <el-tab-pane label="文章管理" name="first"> </el-tab-pane>
      <!-- 未发布文章管理栏 -->
      <el-tab-pane label="未发布文章管理" name="second"> </el-tab-pane>
    </el-tabs>

    <el-card class="article-card">
      <!-- 添加区域 -->
      <el-button type="danger" size="small" icon="el-icon-delete" @click="handleBatchRemoveArticle()"
        >批量删除文章</el-button
      >
      <el-button type="primary" size="small" icon="el-icon-circle-plus" @click="addArticle">添加文章</el-button>
      <el-select
        class="article-select-admin"
        @change="selectArticleTypeList"
        v-model="articleType"
        size="small"
        placeholder="请选择文章类型"
      >
        <el-option v-for="item in options" :key="item.value" :label="item.label" :value="item.value"> </el-option>
      </el-select>
      <el-input
        v-model="listQuery.title"
        size="small"
        style="width: 120px;margin-left: 10px"
        clearable
        placeholder="标题"
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

      <!-- 表格显示区域 -->
      <el-table
        :height="curHeight"
        :data="articleList"
        v-loading="listLoading"
        border
        stripe
        highlight-current-row
        :row-style="{ height: '5px' }"
        :cell-style="{ padding: '5px 0' }"
        @selection-change="checkSelect"
      >
        <el-table-column type="selection" width="40" label="全选"></el-table-column>
        <el-table-column align="center" label="序号" width="60">
          <template slot-scope="scope">
            {{ (pageNum - 1) * pageSize + scope.$index + 1 }}
          </template>
        </el-table-column>
        <el-table-column align="center" label="标题" min-width="160">
          <template slot-scope="scope">
            <el-tooltip effect="light" :content="scope.row.title" placement="top">
              <!--              <span>{{ scope.row.title }}</span>-->
              <el-link type="primary" :underline="false" @click="editDialogRow(scope.row.id)">{{
                scope.row.title
              }}</el-link>
            </el-tooltip>
          </template>
        </el-table-column>
        <el-table-column align="center" label="浏览量" width="100">
          <template slot-scope="scope">
            <el-tag v-if="scope.row.hasOwnProperty('pageviews')" type="success"> {{ scope.row.pageviews }}</el-tag>
            <el-tag v-else type="success">0</el-tag>
          </template>
        </el-table-column>
        <el-table-column align="center" label="文章状态" width="120">
          <template slot-scope="scope">
            <el-button
              v-if="activeName === 'first'"
              type="success"
              size="mini"
              style="width: 80px"
              @click="handleModifyStatus(scope.row, 0)"
            >
              已发布
            </el-button>
            <el-button
              v-if="activeName === 'second'"
              type="danger"
              size="mini"
              style="width: 80px"
              @click="handleModifyStatus(scope.row, 1)"
            >
              待发布
            </el-button>
          </template>
        </el-table-column>
        <el-table-column align="center" label="发布时间" width="160">
          <template slot-scope="scope">
            {{ scope.row.pub_time }}
          </template>
        </el-table-column>
        <el-table-column align="center" label="作者" width="120">
          <template slot-scope="scope">
            <span>{{ scope.row.author }}</span>
          </template>
        </el-table-column>
        <el-table-column align="center" label="操作" width="200">
          <template slot-scope="scope">
            <el-button size="mini" type="primary" icon="el-icon-edit" @click="editDialogRow(scope.row.id)"
              >编辑</el-button
            >
            <el-button size="mini" type="danger" icon="el-icon-delete" @click="deleteRow(scope.row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
      <!-- 分页 -->
      <el-pagination
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
        :current-page="pageNum"
        :page-sizes="[15, 20, 25, 30]"
        :page-size="pageSize"
        layout="total, sizes, prev, pager, next, jumper"
        :total="total"
      >
      </el-pagination>
    </el-card>

    <!-- 新增文章对话框 -->
    <el-dialog
      v-cloak
      title="文章管理"
      @close="DialogClose()"
      center
      :visible.sync="dialogShow"
      top="7vh"
      width="80%"
      class="row-form"
    >
      <el-form :model="row" label-width="100px" class="demo-ruleForm" :rules="rules" ref="addFormRef">
        <el-tabs type="border-card">
          <el-tab-pane label="基本信息">
            <el-row>
              <el-col :span="12">
                <el-form-item label="文章分类:" prop="articleTypeid">
                  <el-select
                    v-model="row.articleTypeid"
                    @change="selectArticleType"
                    clearable
                    style="width: 100%"
                    placeholder="请选择文章分类"
                  >
                    <el-option v-for="item in options" :key="item.value" :label="item.label" :value="item.value">
                    </el-option>
                  </el-select>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="文章作者:" prop="author">
                  <el-input v-model="row.author" clearable maxlength="50" placeholder="请输入文章作者"></el-input>
                </el-form-item>
              </el-col>
            </el-row>

            <el-row>
              <el-col :span="12">
                <el-form-item label="发布系统:" prop="systemId">
                  <el-select v-model="row.systemId" multiple placeholder="请选择发布系统" style="width:100%">
                    <el-option
                      v-for="item in sysOptions"
                      :key="item.label"
                      :label="item.label"
                      :value="item.label"
                      :disabled="item.disabled"
                    >
                    </el-option>
                  </el-select>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="文章状态:" prop="articleStatus">
                  <el-radio v-model="row.articleStatus" label="1">立即发布</el-radio>
                  <el-radio v-model="row.articleStatus" label="0">暂存未发布</el-radio>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="12">
                <el-form-item label="文章标题:" prop="title">
                  <el-input v-model="row.title" clearable maxlength="100" placeholder="请输入文章标题"></el-input>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="发布时间:" prop="pubTime">
                  <el-date-picker
                    v-model="row.pubTime"
                    value-format="yyyy-MM-dd HH:mm:ss"
                    type="datetime"
                    placeholder="选择日期时间"
                    @input="$forceUpdate()"
                  >
                  </el-date-picker>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="24">
                <el-form-item label="文章内容:" prop="content">
                  <quill-editor class="editor" ref="myTextEditor" v-model="row.content" :options="editorOption">
                  </quill-editor>
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
import { dzswArticleType } from '../../plugins/dictionary';

export default {
  // inject: ['reload'],
  data() {
    return {
      listQuery: {
        title: ''
      },
      //提交状态
      flag: 'add',
      // 当前的页数
      pageNum: 1,
      // 当前每页显示数据条数
      pageSize: 15,
      //加载动画
      listLoading: false,
      //系统类型
      sysType: '电子商务系统',
      //文章类型
      articleType: '政策法规',
      //发布状态
      status: 1,
      total: 0,
      articleList: [],
      activeName: 'first',
      // 增加文章对象
      row: {
        articleTypeid: '',
        author: '',
        articleStatus: '1',
        title: '',
        content: '',
        systemId: [],
        simple: '',
        pubTime: ''
      },
      //删除ids数组
      ids: [],
      // 表单高度
      curHeight: 0,
      // 新增文章对话框的显示
      dialogShow: false,
      // 富文本
      editorOption: {
        modules: {
          toolbar: [
            ['bold', 'italic', 'underline', 'strike'], // 加粗 斜体 下划线 删除线
            ['blockquote', 'code-block'], // 引用  代码块
            [{ header: 1 }, { header: 2 }], // 1、2 级标题
            [{ list: 'ordered' }, { list: 'bullet' }], // 有序、无序列表
            [{ script: 'sub' }, { script: 'super' }], // 上标/下标
            [{ indent: '-1' }, { indent: '+1' }], // 缩进
            // [{'direction': 'rtl'}],                         // 文本方向
            [{ size: ['small', false, 'large', 'huge'] }], // 字体大小
            [{ header: [1, 2, 3, 4, 5, 6, false] }], // 标题
            [{ color: [] }, { background: [] }], // 字体颜色、字体背景颜色
            [{ font: [] }], // 字体种类
            [{ align: [] }], // 对齐方式
            ['clean'], // 清除文本格式
            ['link', 'image'] // 链接、图片
          ] //工具菜单栏配置
        },
        placeholder: '请在这里添加文章内容', //提示
        readyOnly: false, //是否只读
        theme: 'snow', //主题 snow/bubble
        syntax: true //语法检测
      },
      // 验证规则
      rules: {
        title: [{ required: true, message: '请输入文章标题', trigger: 'blur' }],
        pubTime: [{ required: true, message: '请选择日期和时间', trigger: 'change' }],
        content: [{ required: true, message: '请输入文章内容', trigger: 'blur' }],
        systemId: [{ required: true, message: '请选择系统分类', trigger: 'blur' }],
        articleTypeid: [{ required: true, message: '请选择文章分类', trigger: 'blur' }],
        author: [{ required: true, message: '请输入作者', trigger: 'blur' }],
        articleStatus: [{ required: true, message: '请输入状态', trigger: 'blur' }]
      },
      options: dzswArticleType,
      //系统选择框
      sysOptions: [
        {
          value: '1',
          label: '电子商务系统',
          disabled: false
        },
        {
          value: '2',
          label: '公益性系统',
          disabled: true
        },
        {
          value: '3',
          label: '追溯信息系统',
          disabled: true
        }
      ]
    };
  },
  created() {
    this.getArticleList();
    this.setTableHeight();
    window.onresize = () => {
      this.setTableHeight();
    };
  },
  methods: {
    addArticle() {
      this.row.articleTypeid = this.articleType;
      if (this.row.articleTypeid === '政策法规') {
        this.sysOptions.map(item => {
          this.row.systemId.push(item.label);
        });
      } else {
        this.sysOptions.map(item => {
          if (item.disabled === false) {
            this.row.systemId.push(item.label);
          }
        });
      }
      this.dialogShow = true;
    },
    // 设定表格高度
    setTableHeight() {
      let h = document.documentElement.clientHeight || document.body.clientHeight;
      this.curHeight = h - 320;
    },
    //全选框事件
    checkSelect(data) {
      data.forEach(item => {
        this.ids.push(item.id);
      });
    },
    //批量删除文章
    async handleBatchRemoveArticle() {
      if (this.ids.length === 0) return this.$message.warning('请先选中要删除的文章');
      const confirmResult = await this.$confirm('此操作将删除选中文章,是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).catch(err => err);
      if (confirmResult !== 'confirm') return this.$message.info('已经取消删除');
      this.ids.forEach((id, index) => {
        let params = {
          id: id,
          sysType: this.sysType
        };
        userService.articleDelete(params).then(res => {
          if (res.status !== 200) return this.$message.error('删除文章失败');
          this.$message.success('删除成功');
          this.ids.splice(index, 1);
          this.getArticleList();
        });
      });
    },
    // 查询
    handleFilter() {
      this.pageNum = 1;
      this.getArticleList();
    },
    //点击tab栏事件
    handleClick() {
      this.getArticleList();
    },
    //获取增加文章下拉框选择的值
    selectArticleType(val) {
      this.row.systemId = [];
      let obj = {};
      obj = this.options.find(item => {
        return item.value === val;
      });
      this.row.articleTypeid = obj.label;
      //如果是发布政策法规可选三个系统
      if (this.row.articleTypeid === '政策法规') {
        this.sysOptions.map(item => {
          item.disabled = false;
        });
      } else {
        this.sysOptions.slice(1).map(item => {
          item.disabled = true;
        });
      }
      if (this.row.articleTypeid === '政策法规') {
        this.sysOptions.map(item => {
          this.row.systemId.push(item.label);
        });
      } else {
        this.sysOptions.map(item => {
          if (item.disabled === false) {
            this.row.systemId.push(item.label);
          }
        });
      }
    },
    //获取筛选文章列表下拉框的值
    selectArticleTypeList(val) {
      let obj = {};
      obj = this.options.find(item => {
        return item.value === val;
      });
      this.articleType = obj.label;
      this.getArticleList();
    },
    //获取文章列表
    getArticleList() {
      let status = this.activeName == 'first' ? '1' : '0';
      let isDelete = 0;
      if (this.activeName === 'third') isDelete = 1;
      this.listLoading = true;
      let params = {
        pageNum: this.pageNum,
        pageRow: this.pageSize,
        sysType: this.sysType,
        articleType: this.articleType,
        status,
        isDelete
      };
      if (this.listQuery.title !== '') {
        params.title = this.listQuery.title;
      }
      userService.getArticleList(params).then(res => {
        if (res.status !== 200) return this.$message.error('获取文章列表失败');
        this.listLoading = false;
        this.articleList = res.data.rows;
        this.total = res.data.total;
      });
    },
    //获取分页每页显示数据条数
    handleSizeChange(newSize) {
      this.pageSize = newSize;
      this.getArticleList();
    },
    //获取当前分页页数
    handleCurrentChange(newPage) {
      this.pageNum = newPage;
      this.getArticleList();
    },
    // 展示修改文章对话框
    editDialogRow(id) {
      //状态
      this.flag = 'edit';
      let status = this.activeName == 'first' ? '1' : '0';
      userService.getArticleId(id).then(res => {
        let sysArr = [];
        res.data.articleSysList.map(item => {
          sysArr.push(item.systemId);
        });
        this.row.articleTypeid = res.data.articleSysList[0].articleTypeid;
        this.row.author = res.data.author;
        this.row.articleStatus = status;
        this.row.systemId = sysArr;
        this.row.title = res.data.title;
        this.row.pubTime = JSON.parse(JSON.stringify(res.data.pubTime));
        this.row.content = res.data.content;
        this.row.id = id;
        this.dialogShow = true;
      });
    },
    // 删除文章
    async deleteRow(data) {
      let params = {
        id: data.id,
        sysType: this.sysType
      };
      const confirmResult = await this.$confirm('此操作将删除该文章,是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).catch(err => err);
      if (confirmResult !== 'confirm') return this.$message.info('已经取消删除');
      userService.articleDelete(params).then(res => {
        if (res.status !== 200) return this.$message.error('删除文章失败');
        this.$message.success('删除文章成功');
        this.getArticleList();
      });
    },
    // 关闭增加文章对话框
    DialogClose() {
      this.row = {
        articleTypeid: '',
        author: '',
        articleStatus: '',
        title: '',
        content: '',
        systemId: []
      };
      this.flag = 'add';
      this.$refs.addFormRef.resetFields();
    },
    // 新增文章的提交
    submitRow() {
      if (this.flag == 'add') {
        this.$refs.addFormRef.validate(valid => {
          if (!valid) return this.$message.error('信息填写不完整或不准确，请检查再提交！');
          this.row.simple = this.$refs.myTextEditor.quill.getText().substring(0, 200);
          userService.articleAdd(this.row).then(res => {
            if (res.status !== 200) return this.$message.error('失败');
            this.$message.success('新增文章成功');
            // 隐藏添加文章的对话框
            this.dialogShow = false;
            // 重新获取文章
            this.getArticleList();
          });
        });
      } else if (this.flag == 'edit') {
        this.editArticleSubmit();
      }
    },
    // 修改文章发布状态
    handleModifyStatus(row, item) {
      let status = item === 0 ? '待发布' : '已发布';
      this.$confirm(`是否将文章状态修改为 ${status} ?`, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      })
        .then(() => {
          userService.getArticleId(row.id).then(res => {
            let sysArr = [];
            sysArr.push(this.sysType);
            this.row.articleTypeid = res.data.articleSysList[0].articleTypeid;
            this.row.author = res.data.author;
            this.row.articleStatus = item;
            this.row.systemId = sysArr;
            this.row.title = res.data.title;
            this.row.content = res.data.content;
            this.row.id = row.id;
            // this.row.simple = this.$refs.myTextEditor.quill.getText().substring(0, 200);
            userService.articleUpdate(this.row).then(res => {
              if (res.status !== 200) return this.$message.error('更新失败');
              this.getArticleList();
              this.$message.success('更新成功');
            });
          });
        })
        .catch(() => {
          this.$message({ type: 'info', message: '已取消操作' });
        });
    },
    //编辑文章的提交
    editArticleSubmit() {
      this.row.simple = this.$refs.myTextEditor.quill.getText().substring(0, 200);
      this.$refs.addFormRef.validate(valid => {
        if (!valid) return this.$message.error('信息填写不完整或不准确，请检查再提交！');
        userService.articleUpdate(this.row).then(res => {
          if (res.status !== 200) return this.$message.error('更新文章信息失败');
          // 关闭对话框
          this.dialogShow = false;
          // 刷新数据列表
          this.getArticleList();
          // 提醒更新文章成功
          this.$message.success('更新文章成功');
          // this.reload();
        });
      });
    }
  }
};
</script>

<style lang="less">
.article-card {
  margin: 0;
}

.article-select-admin {
  margin-left: 20px;
}
</style>
