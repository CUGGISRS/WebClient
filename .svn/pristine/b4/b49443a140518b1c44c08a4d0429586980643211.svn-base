<template>
  <div>
    <!-- 面包屑导航区域 -->
    <el-breadcrumb separator-class="el-icon-arrow-right">
      <el-breadcrumb-item>溯源信息监督</el-breadcrumb-item>
      <el-breadcrumb-item>生产管理</el-breadcrumb-item>
      <el-breadcrumb-item>加工批次管理</el-breadcrumb-item>
    </el-breadcrumb>
    <!-- 卡片视图区域 -->
    <el-card>
			<!-- 查询区域 -->
			<div class="query-condition">
				<div class="query-item">
					<span>加工基地名称:</span>
					<el-select v-model="params.baseId" clearable style="width: 200px;" placeholder="请选择加工基地">
						<el-option
							v-for="item in productionBaseOptions"
							:key="item.id"
							:label="item.name"
							:value="item.id">
						</el-option>
					</el-select>
				</div>
				<div class="query-item">
					<span>追溯码:</span>
					<el-input v-model="params.productBatchNumber" clearable style="width: 220px;" placeholder="请输入追溯码"></el-input>
				</div>
				<div class="query-item">
					<span>开始时间:</span>
					<el-date-picker
						value-format="yyyy-MM-dd"
						v-model="params.startDateS"
						type="date" clearable
						style="width: 160px;"
						placeholder="请选择开始时间">
					</el-date-picker>
				</div>
				<div class="query-item">
					<span>结束时间:</span>
					<el-date-picker
						value-format="yyyy-MM-dd"
						v-model="params.endDateE"
						type="date" clearable
						style="width: 160px;"
						placeholder="请选择结束时间">
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
				<el-table-column align="center" label="加工基地名称" min-width="200">
          <template slot-scope="scope">
            {{ getName(scope.row.baseId) }}
          </template>
        </el-table-column>
				<el-table-column align="center" label="追溯码" width="240">
          <template slot-scope="scope">
            {{ scope.row.productBatchNumber }}
          </template>
        </el-table-column>
				<el-table-column align="center" label="开始时间" width="120">
          <template slot-scope="scope">
            {{ filterTime(scope.row.startDate) }}
          </template>
        </el-table-column>
				<el-table-column align="center" label="结束时间" width="120">
          <template slot-scope="scope">
            {{ filterTime(scope.row.endDate) }}
          </template>
        </el-table-column>
				<el-table-column align="center" label="加工数量" width="240">
          <template slot-scope="scope">
            {{ scope.row.processAmount + getDictionaryName(scope.row.processAmountUnit) }}
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
		<!-- 加工批次信息话框 -->
		<el-dialog v-cloak title="加工批次信息" @close="closeDialog" center :visible.sync="dialogShow" width="1000px" class="row-form">
			<el-form :model="row" label-width="120px" class="demo-ruleForm">
				<el-tabs type="border-card" v-model="activeName">
					<el-tab-pane label="基本信息" name="first">
						<el-row>
							<el-col :span="12">
								<el-form-item label="加工基地名称:">
									<el-select v-model="row.baseId" readonly>
										<el-option
											v-for="item in productionBaseOptions"
											:key="item.id"
											:label="item.name"
											:value="item.id">
										</el-option>
									</el-select>
								</el-form-item>
							</el-col>
							<el-col :span="12">
								<el-form-item label="追溯码:">
									<el-input v-model="row.productBatchNumber" readonly></el-input>
								</el-form-item>
							</el-col>
						</el-row>

						<el-row>
							<el-col :span="12">
								<el-form-item label="开始时间:">
									<el-date-picker
										value-format="yyyy-MM-dd"
										v-model="row.startDate"
										type="date" readonly>
									</el-date-picker>
								</el-form-item>
							</el-col>
							<el-col :span="12">
								<el-form-item label="结束时间:">
									<el-date-picker
										value-format="yyyy-MM-dd"
										v-model="row.endDate"
										type="date" readonly>
									</el-date-picker>
								</el-form-item>
							</el-col>
						</el-row>

						<el-row>
							<el-col :span="12">
								<el-form-item label="加工数量:">
                  <el-input v-model="row.processAmount" readonly></el-input>
								</el-form-item>
							</el-col>
							<el-col :span="12">
								<el-form-item label="加工数量单位:">
									<el-select v-model="row.processAmountUnit" readonly>
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

						<el-row>
							<el-col :span="12">
								<el-form-item label="加工方式:">
                  <el-input v-model="row.processWay" readonly></el-input>
								</el-form-item>
							</el-col>
							<el-col :span="12">
								<el-form-item label="加工材料来源:">
									<el-radio v-model="row.sourceRadio" :label="1" disabled>收获</el-radio>
									<el-radio v-model="row.sourceRadio" :label="2" disabled>收购</el-radio>
								</el-form-item>
							</el-col>
						</el-row>

						<el-row>
							<el-col :span="12">
								<el-form-item label="产品名称:">
									<el-select v-model="row.harvestId" :disabled="row.sourceRadio !== 1" readonly>
										<el-option
											v-for="item in harvestOptions"
											:key="item.id"
											:label="getProductName(item.productId)"
											:value="item.id	">
										</el-option>
									</el-select>
								</el-form-item>
							</el-col>
							<el-col :span="12">
								<el-form-item label="收购物名称:">
									<el-select v-model="row.buyId" :disabled="row.sourceRadio !== 2" readonly>
										<el-option
											v-for="item in buyOptions"
											:key="item.id"
											:label="item.name"
											:value="item.id	">
										</el-option>
									</el-select>
								</el-form-item>
							</el-col>
						</el-row>

						<el-row>
							<el-col :span="12">
								<el-form-item label="商品名称:">
									<el-select v-model="row.productDetailId" readonly>
										<el-option
											v-for="item in productDetailOptions"
											:key="item.id"
											:label="item.name"
											:value="item.id	">
										</el-option>
									</el-select>
								</el-form-item>
							</el-col>
						</el-row>
					</el-tab-pane>
					<el-tab-pane label="产品加工" name="second">
						<div class="upload-div">
							<div class="upload-div-list" v-for="(item,index) of imgProcessPictureLists" :key="index">
								<el-image :src="baseUrl + item.url" :preview-src-list="srcList"></el-image>
							</div>
						</div>
					</el-tab-pane>
				</el-tabs>
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
				baseId: null,
				productBatchNumber: null,
				startDateS: null,
				endDateE: null,
				enterpriseId: window.sessionStorage.getItem('enterpriseId'),
				baseType: '种植基地'
			},
			params2: {
				enterpriseId: window.sessionStorage.getItem('enterpriseId'),
				sysModule: 2
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
			// 活跃tab栏
			activeName: 'first',
			// 对话框
			dialogShow: false,
			// 产品加工
			baseUrl: this.$baseImgUrl,
			srcList: [],
			imgProcessPictureLists: [],
			// 多选框
			productionBaseOptions: [],
			harvestOptions: [],
			buyOptions: [],
			productOptions: [],
			productDetailOptions: [],
			amountUnitOptions: []
		}
	},
	created() {
		this.getAmountUnitDictionary();
		this.getBaseDictionary();
		this.getHarvestDictionary();
		this.getBuytDictionary();
		this.getProductDictionary();
		this.getProductDetailDictionary();
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
		async getBaseDictionary() {
			const { data: res } = await this.$http.get('zsSys/BaseInfo/pageByEId', {
				params: {
					baseType: '加工基地',
					enterpriseId: window.sessionStorage.getItem('enterpriseId')
				}
			});
			if (res.status !== 200) {
				return this.$message({
					duration: 1000,
					type: 'error',
					message: '获取生产基地数据失败！'
				});
			}
			this.productionBaseOptions = res.data.rows;
		},
		async getHarvestDictionary() {
			const { data: res } = await this.$http.get('zsSys/Harvest/pageByEId', {
				params: {
					enterpriseId: window.sessionStorage.getItem('enterpriseId'),
					baseType: this.params.baseType
				}
			});
			if (res.status !== 200) {
				return this.$message({
					duration: 1000,
					type: 'error',
					message: '获取采收/捕捞数据失败！'
				});
			}
			this.harvestOptions = res.data.rows;
		},
		async getBuytDictionary() {
			const { data: res } = await this.$http.get('zsSys/BuyInfo/pageByEId', {
				params: this.params2
			});
			if (res.status !== 200) {
				return this.$message({
					duration: 1000,
					type: 'error',
					message: '获取收购数据失败！'
				});
			}
			this.buyOptions = res.data.rows;
		},
		async getProductDictionary() {
			const { data: res } = await this.$http.get('zsSys/Product/pageByEId', {
				params: {
					enterpriseId: window.sessionStorage.getItem('enterpriseId'),
					baseType: this.params.baseType
				}
			});
			if (res.status !== 200) {
				return this.$message({
					duration: 1000,
					type: 'error',
					message: '获取产品数据失败！'
				});
			}
			this.productOptions = res.data.rows;
		},
		async getProductDetailDictionary() {
			const { data: res } = await this.$http.get('zsSys/ProductDetail/pageByEId', {
				params: this.params2
			});
			if (res.status !== 200) {
				return this.$message({
					duration: 1000,
					type: 'error',
					message: '获取商品数据失败！'
				});
			}
			this.productDetailOptions = res.data.rows;
		},
		// 获取全部的列表数据
		async getAllList() {
			this.params.page = this.currentPage;
			this.params.limit = this.pageSize;
			const { data: res } = await this.$http.get('zsSys/ProcessBatch/pageByEId', { params: this.params });
			if (res.status !== 200) {
				return this.$message({
					duration: 1000,
					type: 'error',
					message: '获取加工批次数据失败！'
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
			this.srcList = [];
			this.imgProcessPictureLists = [];
			this.activeName = 'first';
			this.getAllList();
		},
		// 查看数据
		showRow(row) {
			this.row = row;
			if(row.productBatchNumberPictureList && row.productBatchNumberPictureList.length !== 0) {
        this.imgProcessPictureLists = row.productBatchNumberPictureList;
        for(let i=0; i<this.imgProcessPictureLists.length; i++) {
          this.srcList.push(this.baseUrl + this.imgProcessPictureLists[i].url);
        }
			}
			if(row.harvestId) {
				this.$set(this.row, 'sourceRadio', 1);
			} else {
				this.$set(this.row, 'sourceRadio', 2);
			}
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
			for(let i=0; i<this.productionBaseOptions.length; i++) {
				if(id === this.productionBaseOptions[i].id) {
					return this.productionBaseOptions[i].name;
				}
			}
		},
		getProductName(id) {
			for(let i=0; i<this.productOptions.length; i++) {
				if(id === this.productOptions[i].id) {
					return this.productOptions[i].name;
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
		changeEnterpriseType(value) {
			if(value === '种植基地') {
				this.params2.sysModule = 2;
			} else if (value === '养殖基地') {
				this.params2.sysModule = 1;
			}
			this.getHarvestDictionary();
			this.getBuytDictionary();
			this.getProductDictionary();
			this.getAllList();
		}
	}
}
</script>

<style scoped lang="less">
</style>