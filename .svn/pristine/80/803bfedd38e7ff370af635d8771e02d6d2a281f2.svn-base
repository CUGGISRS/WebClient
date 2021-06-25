<template>
  <div>
    <!-- 面包屑导航区域 -->
    <el-breadcrumb separator-class="el-icon-arrow-right">
      <el-breadcrumb-item :to="{ path: '/home' }">首页</el-breadcrumb-item>
      <el-breadcrumb-item>农产品追溯信息系统</el-breadcrumb-item>
      <el-breadcrumb-item>投诉建议管理</el-breadcrumb-item>
    </el-breadcrumb>

    <el-tabs v-model="activeName" @tab-click="handleClick">
      <!-- 投诉管理栏 -->
      <el-tab-pane label="投诉管理" name="first"></el-tab-pane>
      <!-- 建议管理栏 -->
      <el-tab-pane label="建议管理" name="second"></el-tab-pane>
    </el-tabs>

    <!-- 卡片视图区域 -->
    <el-card style="margin: 0">
      <!-- 添加区域 -->
      <div>
        <el-input
          v-model="suggestInfo"
          size="small"
          placeholder="投诉建议"
          clearable
          style="width: 200px;margin-left: 2rem"
          @input="handleFilter"
        ></el-input>
        <el-button size="small" type="primary" style=";margin-left: 0.5rem" icon="el-icon-search" @click="handleFilter">
          查询
        </el-button>
      </div>
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
      >
        <el-table-column align="center" label="序号" width="60">
          <template slot-scope="scope">
            {{ (listQuery.page - 1) * listQuery.limit + scope.$index + 1 }}
          </template>
        </el-table-column>
        <el-table-column align="center" label="类型" width="120">
          <template slot-scope="scope">
            {{ scope.row.type }}
          </template>
        </el-table-column>
        <el-table-column align="center" label="主题" width="200">
          <template slot-scope="scope">
            <el-link type="primary" :underline="false">{{ scope.row.theme }}</el-link>
          </template>
        </el-table-column>
        <el-table-column align="center" label="内容" min-width="180">
          <template slot-scope="scope">
            <el-popover placement="top-start" width="250" trigger="hover">
              <div>{{ scope.row.content }}</div>
              <span
                slot="reference"
                v-if="scope.row.hasOwnProperty('content') && JSON.stringify(scope.row.content).length > 20"
                >{{ scope.row.content.substr(0, 20) }}...
              </span>
              <span v-else slot="reference">
                {{ scope.row.content }}
              </span>
            </el-popover>
          </template>
        </el-table-column>
        <el-table-column align="center" label="姓名" width="120">
          <template slot-scope="scope">
            {{ scope.row.name }}
          </template>
        </el-table-column>
        <el-table-column align="center" label="联系方式" width="160">
          <template slot-scope="scope">
            {{ scope.row.phone }}
          </template>
        </el-table-column>
        <el-table-column align="center" label="地址" width="200">
          <template slot-scope="scope">
            {{ scope.row.address }}
          </template>
        </el-table-column>
        <el-table-column align="center" label="是否处理" width="200">
          <template slot-scope="scope">
            <el-switch
              v-model="scope.row.status"
              active-color="#13ce66"
              inactive-color="#ff4949"
              :active-value="1"
              :inactive-value="0"
              active-text="已处理"
              inactive-text="未处理"
              :disabled="scope.row.status ? true : false"
              @change="changSwitch($event, scope.row)"
            >
            </el-switch>
          </template>
        </el-table-column>
        <el-table-column align="center" label="修改人" width="120">
          <template slot-scope="scope">
            {{ scope.row.updName }}
          </template>
        </el-table-column>
        <!--        <el-table-column align="center" label="操作" width="120">-->
        <!--          <template slot-scope="scope">-->
        <!--            <el-button type="danger" size="mini" icon="el-icon-delete" @click="deleteInfo(scope.row.id)"-->
        <!--            >删除-->
        <!--            </el-button>-->
        <!--          </template>-->
        <!--        </el-table-column>-->
      </el-table>
      <!-- 分页 -->
      <el-pagination
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
        :current-page="listQuery.page"
        :page-sizes="[5, 10, 15, 20]"
        :page-size="listQuery.limit"
        layout="total, sizes, prev, pager, next, jumper"
        :total="total"
      >
      </el-pagination>
    </el-card>
  </div>
</template>

<script>
import userService from '@/globals/service/user.js';

export default {
  data() {
    return {
      //建议内容
      suggestInfo: '',
      //搜索建议下拉框
      suggestOptions: [],
      // 表单高度
      curHeight: 0,
      //列表加载圈圈
      listLoading: false,
      //列表渲染db
      rows: [],
      //分页对象
      listQuery: {
        page: 1,
        limit: 10,
        //投诉建议切换
        typeList: ''
      },
      //总条数
      total: 0,
      //导航栏默认栏
      activeName: 'first',
      //是否处理
      isDispose: false,
      //处理的状态
      state: '',
      //处理人名称
      updName: '汪昇'
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
    // 列表渲染
    getAllList() {
      this.listLoading = true;
      this.listQuery.typeList = this.activeName == 'first' ? '投诉' : '建议';
      let formData = new FormData();
      formData.append('page', this.listQuery.page);
      formData.append('limit', this.listQuery.limit);
      formData.append('type', this.listQuery.typeList);
      userService.getSuggestList(formData).then(res => {
        if (res.status !== 200) return this.$message.error('获取失败');
        this.listLoading = false;
        this.rows = res.data.rows;
        this.total = res.data.total;
      });
    },
    //点击tab栏事件
    handleClick() {
      this.getAllList();
    },
    handleFilter() {
      if (this.suggestInfo !== '') {
        this.listLoading = true;
        this.listQuery.typeList = this.activeName == 'first' ? '投诉' : '建议';
        let formData = new FormData();
        formData.append('page', this.listQuery.page);
        formData.append('limit', this.listQuery.limit);
        formData.append('theme', this.suggestInfo);
        userService.getSuggestList(formData).then(res => {
          if (res.status !== 200) return this.$message.error('获取失败');
          this.listLoading = false;
          this.rows = res.data.rows;
          this.total = res.data.total;
        });
      } else {
        this.getAllList();
      }
    },
    // 当前页
    handleCurrentChange(page) {
      this.listQuery.page = page;
      this.getAllList();
    },
    //处理状态的按钮
    changSwitch(data, item) {
      let id = item.id;
      let params = {
        id: id,
        updName: this.updName
      };
      userService.suggestStatusEdit(params).then(res => {
        if (res.status !== 200) return this.$message.error('处理失败');
        this.$message.success('处理成功');
        // 重新获取列表
        this.getAllList();
      });
    },
    //删除投诉建议信息
    async deleteInfo(id) {
      let ids = [];
      ids.push(id);
      const confirmResult = await this.$confirm('此操作将删除该企业信息,是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).catch(err => err);
      if (confirmResult !== 'confirm') return this.$message.info('已经取消删除');
      userService.businessDirectoryDel(ids).then(res => {
        if (res.status !== 200) return this.$message.error('删除企业信息失败');
        this.$message.success('删除企业信息成功');
        this.getAllList();
      });
    },
    // 每页多少条
    handleSizeChange(size) {
      this.listQuery.limit = size;
      this.getAllList();
    },
    // 设定表格高度
    setTableHeight() {
      let h = document.documentElement.clientHeight || document.body.clientHeight;
      this.curHeight = h - 310;
    }
  }
};
</script>

<style></style>
