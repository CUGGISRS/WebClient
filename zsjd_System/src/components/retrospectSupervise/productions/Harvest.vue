<template>
  <div>
    <!-- 面包屑导航区域 -->
    <el-breadcrumb separator-class="el-icon-arrow-right">
      <el-breadcrumb-item>溯源信息监督</el-breadcrumb-item>
      <el-breadcrumb-item>生产管理</el-breadcrumb-item>
      <el-breadcrumb-item>采收/捕捞管理</el-breadcrumb-item>
    </el-breadcrumb>
    <!-- 卡片视图区域 -->
    <el-card>
			<!-- 查询区域 -->
			<div class="query-condition">
				<div class="query-item">
					<span>产品名称:</span>
					<el-select v-model="params.productId" filterable clearable style="width: 200px;" placeholder="请选择产品">
						<el-option
							v-for="item in productOptions"
							:key="item.id"
							:label="item.name"
							:value="item.id">
						</el-option>
					</el-select>
				</div>
				<div class="query-item">
					<span>收获时间:</span>
					<el-date-picker
						value-format="yyyy-MM-dd"
						v-model="params.harvestDateS"
						type="date" clearable
						style="width: 160px;"
						placeholder="请选择收获时间">
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
				<el-table-column align="center" label="产品名称" min-width="120">
          <template slot-scope="scope">
            {{ getName(scope.row.productId) }}
          </template>
        </el-table-column>
				<el-table-column align="center" label="收购码" width="200">
          <template slot-scope="scope">
            {{ scope.row.traceCode }}
          </template>
        </el-table-column>
				<el-table-column align="center" label="收获数量" width="240">
          <template slot-scope="scope">
            {{ scope.row.harvestAmount + getDictionaryName(scope.row.harvestAmountUnit) }}
          </template>
        </el-table-column>
				<el-table-column align="center" label="收获方式" width="120">
          <template slot-scope="scope">
            {{ scope.row.harvestWay }}
          </template>
        </el-table-column>
				<el-table-column align="center" label="收获时间" width="120">
          <template slot-scope="scope">
            {{ filterTime(scope.row.harvestDate) }}
          </template>
        </el-table-column>
				<el-table-column align="center" label="天气状况" width="120">
          <template slot-scope="scope">
            {{ scope.row.weather }}
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
		<!-- 采收/捕捞信息对话框 -->
		<el-dialog v-cloak title="采收/捕捞信息" @close="closeDialog" center :visible.sync="dialogShow" width="1000px" class="row-form">
			<el-form :model="row" label-width="120px" class="demo-ruleForm">
				<el-tabs type="border-card" v-model="activeName">
					<el-tab-pane label="基本信息" name="first">
						<el-row>
							<el-col :span="12">
								<el-form-item label="产品名称:">
									<el-select v-model="row.productId" readonly>
										<el-option
											v-for="item in productOptions"
											:key="item.id"
											:label="item.name"
											:value="item.id">
										</el-option>
									</el-select>
								</el-form-item>
							</el-col>
							<el-col :span="12">
								<el-form-item label="收获方式:">
									<el-select v-model="row.harvestWay" readonly>
										<el-option
											v-for="item in harvestWayOptions"
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
								<el-form-item label="收获数量:">
									<el-input v-model="row.harvestAmount" readonly></el-input>
								</el-form-item>
							</el-col>
							<el-col :span="12">
								<el-form-item label="收获数量单位:">
									<el-select v-model="row.harvestAmountUnit" readonly>
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
								<el-form-item label="收获时间:">
                  <el-date-picker
										value-format="yyyy-MM-dd"
										v-model="row.harvestDate"
										type="date"
										clearable readonly>
									</el-date-picker>
								</el-form-item>
							</el-col>
							<el-col :span="12">
								<el-form-item label="天气状况:">
									<el-input v-model="row.weather" readonly></el-input>
								</el-form-item>
							</el-col>
						</el-row>

						<el-row>
							<el-col :span="12">
								<el-form-item label="收购码:">
									<el-input v-model="row.traceCode" readonly></el-input>
								</el-form-item>
							</el-col>
						</el-row>
					</el-tab-pane>
					<el-tab-pane label="收获图片" name="second">
						<div class="upload-div">
							<div class="upload-div-list" v-for="(item,index) of imgHarvestPictureLists" :key="index">
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
				productId: null,
				harvestDateS: null,
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
			// 活跃tab栏
			activeName: 'first',
			// 对话框
			dialogShow: false,
			// 收获图片
			baseUrl: this.$baseImgUrl,
			srcList: [],
			imgHarvestPictureLists: [],
			// 多选框
			amountUnitOptions: [],
			harvestWayOptions: ['机器捕捞', '人工捕捞'],
			productOptions: []
		}
	},
	created() {
		this.getAmountUnitDictionary();
		this.getProductDictionary();
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
		// 获取全部的列表数据
		async getAllList() {
			this.params.page = this.currentPage;
			this.params.limit = this.pageSize;
			const { data: res } = await this.$http.get('zsSys/Harvest/pageByEId', { params: this.params });
			if (res.status !== 200) {
				return this.$message({
					duration: 1000,
					type: 'error',
					message: '获取捕捞数据失败！'
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
			this.imgHarvestPictureLists = [];
			this.activeName = 'first';
			this.getAllList();
		},
		// 查看数据
		showRow(row) {
			this.row = row;
			if(row.harvestPictureList && row.harvestPictureList.length !== 0) {
        this.imgHarvestPictureLists = row.harvestPictureList;
        for(let i=0; i<this.imgHarvestPictureLists.length; i++) {
          this.srcList.push(this.baseUrl + this.imgHarvestPictureLists[i].url);
        }
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
		changeEnterpriseType() {
			this.getProductDictionary();
			this.getAllList();
		}
	}
}
</script>

<style scoped lang="less">
</style>