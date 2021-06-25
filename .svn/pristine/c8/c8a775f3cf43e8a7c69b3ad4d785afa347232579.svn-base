<template>
  <div>
    <!-- 面包屑导航区域 -->
    <el-breadcrumb separator-class="el-icon-arrow-right">
      <el-breadcrumb-item>首页</el-breadcrumb-item>
      <el-breadcrumb-item>销售管理</el-breadcrumb-item>
    </el-breadcrumb>
    <!-- 卡片视图区域 -->
    <el-card>
			<!-- 查询区域 -->
			<div class="query-condition">
				<div class="query-item">
					<span>订单编号:</span>
					<el-input v-model="params.orderNumber" clearable style="width: 200px;" placeholder="请输入订单编号"></el-input>
				</div>
				<div class="query-item">
					<span>追溯码:</span>
					<el-select v-model="params.productBatchNumber" clearable filterable style="width: 220px;" placeholder="请选择追溯码">
						<el-option
							v-for="item in productBatchOptions"
							:key="item"
							:label="item"
							:value="item">
						</el-option>
					</el-select>
				</div>
				<div class="query-item">
					<span>销售商名称:</span>
					<el-select v-model="params.sellerId" clearable style="width: 200px;" placeholder="请选择销售商">
						<el-option
							v-for="item in retailerOptions"
							:key="item.id"
							:label="item.name"
							:value="item.id">
						</el-option>
					</el-select>
				</div>
				<div class="query-item">
					<span>发货日期:</span>
					<el-date-picker
						value-format="yyyy-MM-dd"
						v-model="params.sendDateS"
						type="date"
						style="width: 160px;"
						placeholder="请选择发货日期">
					</el-date-picker>
				</div>
				<el-button size="small" type="primary" icon="el-icon-search" @click="searchRow">查询</el-button>
				<div class="changeType">
					<el-select v-model="params.baseType" @change="changeEnterpriseType">
						<el-option
							v-for="item in enterpriseTypeOptions"
							:key="item.value"
							:label="item.label"
							:value="item.value">
						</el-option>
					</el-select>
				</div>
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
				<el-table-column align="center" label="订单编号" width="200">
          <template slot-scope="scope">
            {{ scope.row.orderNumber }}
          </template>
        </el-table-column>
				<el-table-column align="center" label="追溯码" width="200">
          <template slot-scope="scope">
            {{ scope.row.productBatchNumber }}
          </template>
        </el-table-column>
				<el-table-column align="center" label="销售商" width="200">
          <template slot-scope="scope">
            {{ getName(scope.row.sellerId) }}
          </template>
        </el-table-column>
				<el-table-column align="center" label="销售数量" width="200">
          <template slot-scope="scope">
            {{ scope.row.saleAmount + getDictionaryName(scope.row.saleAmountUnit) }}
          </template>
        </el-table-column>
				<el-table-column align="center" label="发货时间" width="120">
          <template slot-scope="scope">
            {{ filterTime(scope.row.sendDate) }}
          </template>
        </el-table-column>
				<el-table-column align="center" label="目的地" min-width="100">
          <template slot-scope="scope">
            <el-tooltip effect="light" :content="scope.row.endPlace" placement="top">
              <span>{{ scope.row.endPlace }}</span>
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
		<!-- 新增编辑对话框 -->
		<el-dialog v-cloak title="订单信息" @close="closeDialog" center :visible.sync="dialogShow" width="1000px" class="row-form">
			<el-form :model="row" label-width="120px" class="demo-ruleForm">
				<el-row>
					<el-col :span="12">
						<el-form-item label="订单编号:">
							<el-input v-model="row.orderNumber" readonly></el-input>
						</el-form-item>
					</el-col>
					<el-col :span="12">
						<el-form-item label="追溯码:">
							<el-select v-model="row.productBatchNumber" readonly>
								<el-option
									v-for="item in productBatchOptions"
									:key="item"
									:label="item"
									:value="item">
								</el-option>
							</el-select>
						</el-form-item>
					</el-col>
				</el-row>

				<el-row>
					<el-col :span="12">
						<el-form-item label="销售商名称:">
							<el-select v-model="row.sellerId" readonly>
								<el-option
									v-for="item in retailerOptions"
									:key="item.id"
									:label="item.name"
									:value="item.id">
								</el-option>
							</el-select>
						</el-form-item>
					</el-col>
					<el-col :span="12">
						<el-form-item label="发货时间:">
							<el-date-picker
								value-format="yyyy-MM-dd"
								v-model="row.sendDate"
								type="date" readonly>
							</el-date-picker>
						</el-form-item>
					</el-col>
				</el-row>

				<el-row>
					<el-col :span="12">
						<el-form-item label="发货人:">
              <el-input v-model="row.sender" readonly></el-input>
						</el-form-item>
					</el-col>
					<el-col :span="12">
						<el-form-item label="收货人:">
							<el-input v-model="row.receiver" readonly></el-input>
						</el-form-item>
					</el-col>
				</el-row>

				<el-row>
					<el-col :span="12">
						<el-form-item label="始发地:">
              <el-input v-model="row.startPlace" readonly></el-input>
						</el-form-item>
					</el-col>
					<el-col :span="12">
						<el-form-item label="目的地:">
							<el-input v-model="row.endPlace" readonly></el-input>
						</el-form-item>
					</el-col>
				</el-row>

				<el-row>
					<el-col :span="12">
						<el-form-item label="销售数量:">
              <el-input v-model="row.saleAmount" readonly></el-input>
						</el-form-item>
					</el-col>
					<el-col :span="12">
						<el-form-item label="销售数量单位:">
							<el-select v-model="row.saleAmountUnit" readonly>
								<el-option
									v-for="item in amountUnitOptions"
									:key="item.code"
									:label="item.name"
									:value="item.code">
								</el-option>
							</el-select>
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
				orderNumber: null,
				productBatchNumber: null,
				sellerId: null,
				sendDateS: null,
				enterpriseId: window.sessionStorage.getItem('enterpriseId'),
				baseType: '种植基地'
			},
			// 企业类型
			enterpriseTypeOptions: [
				{
					label: '种植数据',
					value: '种植基地'
				},
				{
					label: '水产数据',
					value: '养殖基地'
				}
			],
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
			dialogShow: false,
			// 多选框
			retailerOptions: [],
			amountUnitOptions: [],
			productBatchOptions: []
		}
	},
	created() {
		this.getAmountUnitDictionary();
		this.getProcessBatchDictionary();
		this.getRetailerDictionary();
		this.getAllList();
		this.setTableHeight();
		window.onresize = () => {
			this.setTableHeight();
		}
	},
	methods: {
		// 获取数据字典
		async getAmountUnitDictionary() {
			const { data: res } = await this.$http.get('comSys/DataDictionary/getAllMayToCondition', {
				params: { type: '数量单位' }
			});
			if (res.status !== 200) {
				return this.$message({
					duration: 1000,
					type: 'error',
					message: '获取数量单位失败！'
				});
			}
			this.amountUnitOptions = res.data;
		},
		async getProcessBatchDictionary() {
			this.productBatchOptions = [];
			const { data: res } = await this.$http.get('zsSys/ProcessBatch/pageByEId', {
				params: {
					enterpriseId: window.sessionStorage.getItem('enterpriseId'),
					baseType: this.params.baseType
				}
			});
			if (res.status !== 200) {
				return this.$message({
					duration: 1000,
					type: 'error',
					message: '获取产品批次号数据失败！'
				});
			}
			for(let i=0; i<res.data.rows.length; i++) {
				this.productBatchOptions.push(res.data.rows[i].productBatchNumber);
			}
		},
		async getRetailerDictionary() {
			const { data: res } = await this.$http.get('zsSys/Seller/pageByEId', {
				params: {
					enterpriseId: window.sessionStorage.getItem('enterpriseId')
				}
			});
			if (res.status !== 200) {
				return this.$message({
					duration: 1000,
					type: 'error',
					message: '获取销售商数据失败！'
				});
			}
			this.retailerOptions = res.data.rows;
		},
		// 获取全部的列表数据
		async getAllList() {
			this.params.page = this.currentPage;
			this.params.limit = this.pageSize;
			const { data: res } = await this.$http.get('zsSys/Sale/pageByEId', { params: this.params });
			if (res.status !== 200) {
				return this.$message({
					duration: 1000,
					type: 'error',
					message: '获取销售数据失败！'
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
		},
		// 过滤时间
		filterTime(time) {
			if(time) {
				return time.slice(0, 10);
			}
			return time
		},
		// 根据id获取name
		getName(id) {
			for(let i=0; i<this.retailerOptions.length; i++) {
				if(id === this.retailerOptions[i].id) {
					return this.retailerOptions[i].name;
				}
			}
		},
		// 根据code转换成name
		getDictionaryName(code) {
			for(let i=0; i<this.amountUnitOptions.length; i++) {
				if(code === this.amountUnitOptions[i].code) {
					return this.amountUnitOptions[i].name;
				}
			}
		},
		// 数据切换
		changeEnterpriseType() {
			this.getProcessBatchDictionary();
			this.getAllList();
		}
	}
}
</script>

<style scoped lang="less">
</style>