<template>
  <div>
    <!-- 面包屑导航区域 -->
    <el-breadcrumb separator-class="el-icon-arrow-right">
      <el-breadcrumb-item>首页</el-breadcrumb-item>
      <el-breadcrumb-item>溯源信息监督</el-breadcrumb-item>
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
			<el-table :data="rows"
				border stripe fit highlight-current-row 
				:row-style="{height:'5px'}"
				:cell-style="{padding:'5px 0'}"
				:height="curHeight">
				<el-table-column align="center" label="序号" width="80">
          <template slot-scope="scope">
            {{  (currentPage - 1) * pageSize + scope.$index + 1 }}
          </template>
        </el-table-column>
				<el-table-column align="center" label="操作" width="200">
          <template slot-scope="scope">
            <el-button size="small" type="warning" icon="el-icon-view" @click="showRow(scope.row)">查看</el-button>
            <el-button size="small" type="danger" icon="el-icon-delete" @click="deleteRow(scope.row.id)">删除</el-button>
          </template>
        </el-table-column>
				<el-table-column align="center" label="企业名称" min-width="200">
          <template slot-scope="scope">
            {{ scope.row.name }}
          </template>
        </el-table-column>
				<el-table-column align="center" label="企业类型" width="120">
          <template slot-scope="scope">
            {{ getDictionaryName(scope.row.businessType) }}
          </template>
        </el-table-column>
				<el-table-column align="center" label="统一社会信用代码" width="200">
          <template slot-scope="scope">
            {{ scope.row.socialCode }}
          </template>
        </el-table-column>
				<el-table-column align="center" label="行政区规划" width="140">
          <template slot-scope="scope">
            {{ getDictionaryName4(scope.row.district) }}
          </template>
        </el-table-column>
				<el-table-column align="center" label="组织形式" width="160">
          <template slot-scope="scope">
            {{ getDictionaryName2(scope.row.organizationForm) }}
          </template>
        </el-table-column>
				<el-table-column align="center" label="主体属性" width="140">
          <template slot-scope="scope">
            {{ getDictionaryName3(scope.row.subjectAttribute) }}
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
				status: 1
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
			// 多选框
			businessTypeOptions: [],
			organizationFormOptions: [],
			subjectAttributeOptions: [],
			districtOptions: [],
		}
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
		}
	},
	methods: {
		// 获取数据字典
		async getBusinessTypeDictionary() {
			const { data: res } = await this.$http.get('comSys/DataDictionary/getAllMayToCondition', {
				params: {
					type: '企业类型',
					remark: 'X2'
				}
			});
			if (res.status !== 200) {
				return this.$message({
					duration: 1000,
					type: 'error',
					message: '获取企业类型失败！'
				});
			}
			this.businessTypeOptions = res.data;
		},
		async getOrganizationFormDictionary() {
			const { data: res } = await this.$http.get('comSys/DataDictionary/getAllMayToCondition', {
				params: {
					type: '企业组织形式',
					remark: 'X2'
				}
			});
			if (res.status !== 200) {
				return this.$message({
					duration: 1000,
					type: 'error',
					message: '获取企业组织形式失败！'
				});
			}
			this.organizationFormOptions = res.data;
		},
		async getSubjectAttributeDictionary() {
			const { data: res } = await this.$http.get('comSys/DataDictionary/getAllMayToCondition', {
				params: {
					type: '企业主体属性',
					remark: 'X2'
				}
			});
			if (res.status !== 200) {
				return this.$message({
					duration: 1000,
					type: 'error',
					message: '获取企业主体属性失败！'
				});
			}
			this.subjectAttributeOptions = res.data;
		},
		async getDistrictDictionary() {
			const { data: res } = await this.$http.get('comSys/DataDictionary/getAllMayToCondition', {
				params: { type: '镇乡' }
			});
			if (res.status !== 200) {
				return this.$message({
					duration: 1000,
					type: 'error',
					message: '获取行政区规划失败！'
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
					message: '获取企业数据失败！'
				});
			}
			this.rows = res.data.rows;
			this.total = res.data.total;
		},
		// 删除企业用户
		deleteRow(delIds) {
			this.$confirm('此操作将删除选中的数据, 是否继续?', '提示', {
				confirmButtonText: '确定',
				cancelButtonText: '取消',
				type: 'warning'
			}).then(async () => {
				const { data: res } = await this.$http.delete('zsSys/Enterprise/' + delIds);
				if(res.status !== 200) {
					return this.$message({
						duration: 2000,
						type: 'error',
						message: '删除企业失败！'
					});
				}
				this.$message({
					duration: 2000,
					type: 'success',
					message: '删除成功！'
				});
				this.getAllList();
			}).catch(() => {
				this.$message({
					duration: 1000,
					type: 'info',
					message: '已取消操作'
				});          
			});
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
		},
		// 关闭对话框
		closeDialog() {
			this.row = {};
			this.getAllList();
		},
		// 展示数据
		showRow(row) {
			window.sessionStorage.setItem('enterpriseId', row.id);
			this.$router.push('/retrospect-home');
		},
		// 查询数据
		searchRow() {
			this.currentPage = 1;
			this.getAllList();
		},
		// 根据code转换成name
		getDictionaryName(code) {
			for(let i=0; i<this.businessTypeOptions.length; i++) {
				if(code === this.businessTypeOptions[i].code) {
					return this.businessTypeOptions[i].name;
				}
			}
		},
		getDictionaryName2(code) {
			for(let i=0; i<this.organizationFormOptions.length; i++) {
				if(code === this.organizationFormOptions[i].code) {
					return this.organizationFormOptions[i].name;
				}
			}
		},
		getDictionaryName3(code) {
			for(let i=0; i<this.subjectAttributeOptions.length; i++) {
				if(code === this.subjectAttributeOptions[i].code) {
					return this.subjectAttributeOptions[i].name;
				}
			}
		},
		getDictionaryName4(code) {
			for(let i=0; i<this.districtOptions.length; i++) {
				if(code === this.districtOptions[i].code) {
					return this.districtOptions[i].name;
				}
			}
		}
	}
}
</script>

<style scoped lang="less">
</style>