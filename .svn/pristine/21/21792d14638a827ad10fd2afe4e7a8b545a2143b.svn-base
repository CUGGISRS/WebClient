<template>
  <div>
    <!-- 面包屑导航区域 -->
    <el-breadcrumb separator-class="el-icon-arrow-right">
			<el-breadcrumb-item>溯源信息监督</el-breadcrumb-item>
      <el-breadcrumb-item>基本配置</el-breadcrumb-item>
      <el-breadcrumb-item>销售商管理</el-breadcrumb-item>
    </el-breadcrumb>
    <!-- 卡片视图区域 -->
    <el-card>
			<!-- 查询区域 -->
			<div class="query-condition">
				<div class="query-item">
					<span>销售商名称:</span>
					<el-input v-model="params.name" clearable style="width: 240px;" placeholder="请输入销售商名称"></el-input>
				</div>
				<el-button size="small" type="primary" icon="el-icon-search" @click="searchRow">查询</el-button>
			</div>
			<!-- 表格显示区域 -->
			<el-table :data="rows"
				border stripe fit highlight-current-row 
				:row-style="{height:'5px'}"
				:cell-style="{padding:'5px 0'}"
				:height="curHeight">
				<el-table-column align="center" label="序号" width="80">
          <template slot-scope="scope">
            {{ (currentPage - 1) * pageSize + scope.$index + 1 }}
          </template>
        </el-table-column>
				<el-table-column align="center" label="操作" width="100">
          <template slot-scope="scope">
            <el-button size="small" type="primary" icon="el-icon-view" @click="showRow(scope.row)">查看</el-button>
          </template>
        </el-table-column>
				<el-table-column align="center" label="销售商名称" width="240">
          <template slot-scope="scope">
            {{ scope.row.name }}
          </template>
        </el-table-column>
				<el-table-column align="center" label="联系人" width="160">
          <template slot-scope="scope">
            {{ scope.row.contactPerson }}
          </template>
        </el-table-column>
				<el-table-column align="center" label="联系电话" width="120">
          <template slot-scope="scope">
            {{ scope.row.phone }}
          </template>
        </el-table-column>
				<el-table-column align="center" label="销售许可证号" width="240">
          <template slot-scope="scope">
            {{ scope.row.saleLicenseNumber }}
          </template>
        </el-table-column>
				<el-table-column align="center" label="销售商地址" min-width="100">
          <template slot-scope="scope">
            <el-tooltip effect="light" :content="scope.row.address" placement="top">
              <span>{{ scope.row.address }}</span>
            </el-tooltip>
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
				:total="total">
			</el-pagination>
    </el-card>
		<!-- 销售商信息对话框 -->
		<el-dialog v-cloak title="销售商信息" @close="closeDialog" center :visible.sync="dialogShow" width="1000px" class="row-form">
			<el-form :model="row" label-width="140px" class="demo-ruleForm">
				<el-row>
					<el-col :span="12">
						<el-form-item label="销售商名称:">
							<el-input v-model="row.name" readonly></el-input>
						</el-form-item>
					</el-col>
					<el-col :span="12">
						<el-form-item label="销售许可证号:">
							<el-input v-model="row.saleLicenseNumber" readonly></el-input>
						</el-form-item>
					</el-col>
				</el-row>

				<el-row>
					<el-col :span="12">
						<el-form-item label="联系人:">
              <el-input v-model="row.contactPerson" readonly></el-input>
						</el-form-item>
					</el-col>
					<el-col :span="12">
						<el-form-item label="联系电话:">
							<el-input v-model="row.phone" readonly></el-input>
						</el-form-item>
					</el-col>
				</el-row>

				<el-row>
					<el-col :span="12">
						<el-form-item label="邮箱:">
							<el-input v-model="row.mailbox" readonly></el-input>
						</el-form-item>
					</el-col>
					<el-col :span="12">
						<el-form-item label="销售商地址:">
							<el-input v-model="row.address" readonly></el-input>
						</el-form-item>
					</el-col>
				</el-row>
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
				enterpriseId: window.sessionStorage.getItem('enterpriseId')
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
			// 对话框
			dialogShow: false
		}
	},
	created() {
		this.getAllList();
		this.setTableHeight();
		window.onresize = () => {
			this.setTableHeight();
		}
	},
	methods: {
		// 获取全部的列表数据
		async getAllList() {
			this.params.page = this.currentPage;
			this.params.limit = this.pageSize;
			const { data: res } = await this.$http.get('zsSys/Seller/pageByEId', { params: this.params });
			if (res.status !== 200) {
				return this.$message({
					duration: 1000,
					type: 'error',
					message: '获取销售商数据失败！'
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
      this.curHeight = h - 425;
		},
		// 关闭对话框
		closeDialog() {
			this.row = {};
			this.getAllList();
		},
		// 查看数据
		showRow(row) {
			this.row = row;
			this.dialogShow = true;
		},
		// 查询数据
		searchRow() {
			this.currentPage = 1;
			this.getAllList();
		}
	}
}
</script>

<style scoped lang="less">
</style>