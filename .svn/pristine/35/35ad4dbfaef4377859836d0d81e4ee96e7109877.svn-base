<template>
  <div>
    <!-- 面包屑导航区域 -->
    <el-breadcrumb separator-class="el-icon-arrow-right">
      <el-breadcrumb-item>首页</el-breadcrumb-item>
      <el-breadcrumb-item>生产管理</el-breadcrumb-item>
      <el-breadcrumb-item>检验报告</el-breadcrumb-item>
    </el-breadcrumb>
    <!-- 卡片视图区域 -->
    <el-card>
			<!-- 查询区域 -->
			<div class="query-condition">
				<div class="query-item">
					<span>追溯码:</span>
					<el-select v-model="params.productBatchNumber" clearable filterable style="width: 240px;" placeholder="请选择追溯码">
						<el-option
							v-for="item in productBatchOptions"
							:key="item"
							:label="item"
							:value="item">
						</el-option>
					</el-select>
				</div>
				<div class="query-item">
					<span>检验时间:</span>
					<el-date-picker
						value-format="yyyy-MM-dd"
						v-model="params.testDateS"
						type="date" clearable
						style="width: 160px;"
						placeholder="请选择检验时间">
					</el-date-picker>
				</div>
				<div class="query-item">
					<span>检验结果:</span>
					<el-select v-model="params.testResult" clearable style="width: 160px;" placeholder="请选择检验结果">
						<el-option
							v-for="item in inspectionOptions"
							:key="item"
							:label="item"
							:value="item">
						</el-option>
					</el-select>
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
				<el-table-column align="center" label="追溯码" min-width="200">
          <template slot-scope="scope">
            {{ scope.row.productBatchNumber }}
          </template>
        </el-table-column>
				<el-table-column align="center" label="检验数量" width="240">
          <template slot-scope="scope">
            {{ scope.row.testAmount + getDictionaryName(scope.row.testAmountUnit) }}
          </template>
        </el-table-column>
				<el-table-column align="center" label="检验时间" width="160">
          <template slot-scope="scope">
            {{ filterTime(scope.row.testDate) }}
          </template>
        </el-table-column>
				<el-table-column align="center" label="检验结果" width="160">
          <template slot-scope="scope">
						<el-tag v-if="scope.row.testResult === '合格'" type="success">合格</el-tag>
						<el-tag v-else type="danger">未合格</el-tag>
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
		<!-- 产品批次检验信息对话框 -->
		<el-dialog v-cloak title="产品批次检验信息" @close="closeDialog" center :visible.sync="dialogShow" width="1000px" class="row-form">
			<el-form :model="row" label-width="120px" class="demo-ruleForm">
				<el-tabs type="border-card" v-model="activeName">
					<el-tab-pane label="基本信息" name="first">
						<el-row>
							<el-col :span="12">
								<el-form-item label="追溯码:">
									<el-input v-model="row.productBatchNumber" readonly></el-input>
								</el-form-item>
							</el-col>
							<el-col :span="12">
								<el-form-item label="检验时间:">
									<el-date-picker
										value-format="yyyy-MM-dd"
										v-model="row.testDate"
										type="date" readonly>
									</el-date-picker>
								</el-form-item>
							</el-col>
						</el-row>

						<el-row>
							<el-col :span="12">
								<el-form-item label="检验数量:">
									<el-input v-model="row.testAmount" readonly></el-input>
								</el-form-item>
							</el-col>
							<el-col :span="12">
								<el-form-item label="检验数量单位:">
									<el-select v-model="row.testAmountUnit" readonly>
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
								<el-form-item label="检验结果:">
									<el-tag v-if="row.testResult === '合格'" type="success">合格</el-tag>
									<el-tag v-else type="danger">未合格</el-tag>
								</el-form-item>
							</el-col>
						</el-row>
					</el-tab-pane>
					<el-tab-pane label="检验结果" name="second">
						<div class="upload-div">
							<div class="upload-div-list" v-for="(item,index) of imgInspectionPictureLists" :key="index">
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
				productBatchNumber: null,
				testDateS: null,
				testResult: null,
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
			// 检验结果
			baseUrl: this.$baseImgUrl,
			srcList: [],
			imgInspectionPictureLists: [],
			// 多选框
			// 多选框
			productBatchOptions: [],
			inspectionOptions: ['合格', '未合格'],
			amountUnitOptions: []
		}
	},
	created() {
		this.getAmountUnitDictionary();
		this.getProductBatchDictionary();
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
		async getProductBatchDictionary() {
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
					message: '获取追溯码数据失败！'
				});
			}
			for(let i=0; i<res.data.rows.length; i++) {
				this.productBatchOptions.push(res.data.rows[i].productBatchNumber);
			}
		},
		// 获取全部的列表数据
		async getAllList() {
			this.params.page = this.currentPage;
			this.params.limit = this.pageSize;
			const { data: res } = await this.$http.get('zsSys/FinishProductTest/pageByEId', { params: this.params });
			if (res.status !== 200) {
				return this.$message({
					duration: 1000,
					type: 'error',
					message: '获取检验数据失败！'
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
			this.imgInspectionPictureLists = [];
			this.activeName = 'first';
			this.getAllList();
		},
		// 查看数据
		showRow(row) {
			this.row = row;
			if(row.testResultPictureList && row.testResultPictureList.length !== 0) {
        this.imgInspectionPictureLists = row.testResultPictureList;
        for(let i=0; i<this.imgInspectionPictureLists.length; i++) {
          this.srcList.push(this.baseUrl + this.imgInspectionPictureLists[i].url);
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
			this.getProductBatchDictionary();
			this.getAllList();
		}
	}
}
</script>

<style scoped lang="less">
</style>