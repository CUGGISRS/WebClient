<template>
  <div>
    <!-- 面包屑导航区域 -->
    <el-breadcrumb separator-class="el-icon-arrow-right">
      <el-breadcrumb-item>溯源信息监督</el-breadcrumb-item>
      <el-breadcrumb-item>产品管理</el-breadcrumb-item>
    </el-breadcrumb>
    <!-- 卡片视图区域 -->
    <el-card>
			<!-- 查询区域 -->
			<div class="query-condition">
				<div class="query-item">
					<span>产品名称:</span>
					<el-input v-model="params.name" clearable style="width: 200px;" placeholder="请输入产品名称"></el-input>
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
				<el-table-column align="center" label="产品名称" width="240">
          <template slot-scope="scope">
            {{ scope.row.name }}
          </template>
        </el-table-column>
				<el-table-column align="center" label="养殖基地名称" min-width="120">
          <template slot-scope="scope">
            {{ getBaseName(scope.row.baseId) }}
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
				<el-table-column align="center" label="商品详情" width="240">
          <template slot-scope="scope">
						<el-tag effect="dark" @click="showDetail(scope.row.productDetailId)" style="cursor: pointer;">
							{{ getProductName(scope.row.productDetailId) }}
						</el-tag>
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
		<!-- 产品信息对话框 -->
		<el-dialog v-cloak title="产品信息" @close="closeDialog" center :visible.sync="dialogShow" width="1000px" class="row-form">
			<el-form :model="row" label-width="110px" class="demo-ruleForm">
				<el-row>
					<el-col :span="12">
						<el-form-item label="产品名称:">
							<el-input v-model="row.name" readonly></el-input>
						</el-form-item>
					</el-col>
					<el-col :span="12">
						<el-form-item label="基地名称:">
							<el-select v-model="row.baseId" readonly>
								<el-option
									v-for="item in plantingBaseOptions"
									:key="item.id"
									:label="item.name"
									:value="item.id">
								</el-option>
							</el-select>
						</el-form-item>
					</el-col>
				</el-row>

				<el-row>
					<el-col :span="12">
						<el-form-item label="开始时间:">
							<el-date-picker
								readonly
								value-format="yyyy-MM-dd"
								v-model="row.startDate"
								type="date">
							</el-date-picker>
						</el-form-item>
					</el-col>
					<el-col :span="12">
						<el-form-item label="结束时间:">
							<el-date-picker
								readonly
								value-format="yyyy-MM-dd"
								v-model="row.endDate"
								type="date">
							</el-date-picker>
						</el-form-item>
					</el-col>
				</el-row>

				<el-row>
					<el-col :span="12">
						<el-form-item label="商品选择:">
							<el-select v-model="row.productDetailId" readonly>
								<el-option
									v-for="item in productRows"
									:key="item.id"
									:label="item.name"
									:value="item.id">
								</el-option>
							</el-select>
						</el-form-item>
					</el-col>
				</el-row>
			</el-form>
		</el-dialog>
		<el-dialog v-cloak title="商品详情" @close="closeProductDialog" center :visible.sync="dialogProductShow" width="1000px" class="row-form">
			<el-form :model="productRow" label-width="110px" class="demo-ruleForm">
				<el-tabs type="border-card" v-model="activeName">
					<el-tab-pane label="基本信息" name="first">
						<el-row>
							<el-col :span="12">
								<el-form-item label="商品名称:">
									<el-input v-model="productRow.name" readonly></el-input>
								</el-form-item>
							</el-col>
							<el-col :span="12">
								<el-form-item label="商品类型:">
									<el-select v-model="productRow.type" readonly>
										<el-option
											v-for="item in typeOptions"
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
								<el-form-item label="商品条码:">
									<el-input v-model="productRow.code" readonly></el-input>
								</el-form-item>
							</el-col>
							<el-col :span="12">
								<el-form-item label="商品规格:">
									<el-input v-model="productRow.specs" readonly></el-input>
								</el-form-item>
							</el-col>
						</el-row>

						<el-row>
							<el-col :span="12">
								<el-form-item label="商品用途:">
									<el-input v-model="productRow.purpose" readonly></el-input>
								</el-form-item>
							</el-col>
							<el-col :span="12">
								<el-form-item label="商品保质期:">
									<el-input v-model="productRow.shelfLife" readonly></el-input>
								</el-form-item>
							</el-col>
						</el-row>

						<el-row>
							<el-col :span="24">
								<el-form-item label="商品介绍:">
									<el-input
										readonly
										type="textarea"
										:rows="2"
										v-model="productRow.introduce">
									</el-input>
								</el-form-item>
							</el-col>
						</el-row>
					</el-tab-pane>
					<el-tab-pane label="商品图片" name="second">
						<div class="upload-div">
							<div class="upload-div-list" v-for="(item,index) of imgProductPictureLists" :key="index">
								<el-image :src="baseUrl + item.url" :preview-src-list="srcList"></el-image>
							</div>
						</div>
					</el-tab-pane>
					<el-tab-pane label="商品证书" name="third">
						<div class="upload-div">
							<div class="upload-div-list" v-for="(item,index) of imgProductCertificateLists" :key="index">
								<el-image :src="baseUrl + item.url" :preview-src-list="srcList02"></el-image>
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
				name: null,
				startDateS: null,
				endDateE: null,
				enterpriseId: window.sessionStorage.getItem('enterpriseId'),
				baseType: '种植基地'
			},
			params2: {
				page: null,
				limit: null,
				sysModule: 2,
				enterpriseId: window.sessionStorage.getItem('enterpriseId')
			},
			params3: {
				type: '种植产品类型',
				remark: 'X2'
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
			productRows: [],
			productRow: {},
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
			dialogProductShow: false,
			// 预览图片src列表
			baseUrl: this.$baseImgUrl,
			srcList: [],
			srcList02: [],
			// 产品图片
			imgProductPictureLists: [],
			// 产品证书
			imgProductCertificateLists: [],
			// 多选框
			plantingBaseOptions: [],
			typeOptions: []
		}
	},
	created() {
		this.getTypeDictionary();
		this.getBaseDictionary();
		this.getAllList();
		this.getAllProductList();
		this.setTableHeight();
		window.onresize = () => {
			this.setTableHeight();
		}
	},
	methods: {
		// 获取数据字典
		async getTypeDictionary() {
			const { data: res } = await this.$http.get('comSys/DataDictionary/getAllMayToCondition', {
				params: this.params3
			});
			if (res.status !== 200) {
				return this.$message({
					duration: 1000,
					type: 'error',
					message: '获取产品类型失败！'
				});
			}
			this.typeOptions = res.data;
		},
		async getBaseDictionary() {
			const { data: res } = await this.$http.get('zsSys/BaseInfo/pageByEId', {
				params: {
					baseType: this.params.baseType,
					enterpriseId: window.sessionStorage.getItem('enterpriseId')
				}
			});
			if (res.status !== 200) {
				return this.$message({
					duration: 1000,
					type: 'error',
					message: '获取基地数据失败！'
				});
			}
			this.plantingBaseOptions = res.data.rows;
		},
		// 获取全部的列表数据
		async getAllList() {
			this.params.page = this.currentPage;
			this.params.limit = this.pageSize;
			const { data: res } = await this.$http.get('zsSys/Product/pageByEId', { params: this.params });
			if (res.status !== 200) {
				return this.$message({
					duration: 1000,
					type: 'error',
					message: '获取产品数据失败！'
				});
			}
			this.rows = res.data.rows;
			this.total = res.data.total;
		},
		async getAllProductList() {
			this.params2.page = this.productCurrentPage;
			this.params2.limit = this.productPageSize;
			const { data: res } = await this.$http.get('zsSys/ProductDetail/pageByEId', { params: this.params2 });
			if (res.status !== 200) {
				return this.$message({
					duration: 1000,
					type: 'error',
					message: '获取商品数据失败！'
				});
			}
			this.productRows = res.data.rows;
			this.productTotal = res.data.total;
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
		closeProductDialog() {
			this.productRow = {};
			this.srcList = [];
			this.srcList02 = [];
			this.imgProductPictureLists = [];
			this.imgProductCertificateLists = [];
			this.activeName = 'first';
			this.getAllProductList();
		},
		// 展示商品详情
		showDetail(id) {
			for(let i=0; i<this.productRows.length; i++) {
				if(id === this.productRows[i].id) {
					this.productRow = this.productRows[i];
					if(this.productRows[i].pictureList.length !== 0) {
						this.imgProductPictureLists = this.productRows[i].pictureList;
						for(let i=0; i<this.imgProductPictureLists.length; i++) {
							this.srcList.push(this.baseUrl + this.imgProductPictureLists[i].url);
						}
					}
					if(this.productRows[i].certificateList.length !== 0) {
						this.imgProductCertificateLists = this.productRows[i].certificateList;
						for(let i=0; i<this.imgProductCertificateLists.length; i++) {
							this.srcList02.push(this.baseUrl + this.imgProductCertificateLists[i].url);
						}
					}
					this.dialogProductShow = true;
				}
			}
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
		// 根据code转换成name
		getDictionaryName(code) {
			for(let i=0; i<this.typeOptions.length; i++) {
				if(code === this.typeOptions[i].code) {
					return this.typeOptions[i].name;
				}
			}
		},
		getBaseName(id) {
			for(let i=0; i<this.plantingBaseOptions.length; i++) {
				if(id === this.plantingBaseOptions[i].id) {
					return this.plantingBaseOptions[i].name;
				}
			}
		},
		getProductName(id) {
			for(let i=0; i<this.productRows.length; i++) {
				if(id === this.productRows[i].id) {
					return this.productRows[i].name;
				}
			}
		},
		// 数据切换
		changeEnterpriseType(value) {
			if(value === '种植基地') {
				this.params2.sysModule = 2;
				this.params3.type = '种植产品类型';
			} else if(value === '养殖基地') {
				this.params2.sysModule = 1;
				this.params3.type = '水产产品类型';
			}
			this.getTypeDictionary();
			this.getAllList();
			this.getBaseDictionary();
			this.getAllProductList();
		}
	}
}
</script>

<style scoped lang="less">
</style>