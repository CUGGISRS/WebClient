<template>
  <div>
    <!-- 面包屑导航区域 -->
    <el-breadcrumb separator-class="el-icon-arrow-right">
      <el-breadcrumb-item>首页</el-breadcrumb-item>
      <el-breadcrumb-item>生产管理</el-breadcrumb-item>
      <el-breadcrumb-item>收购管理</el-breadcrumb-item>
    </el-breadcrumb>
    <!-- 卡片视图区域 -->
    <el-card>
			<!-- 查询区域 -->
			<div class="query-condition">
				<div class="query-item">
					<span>收购产品名称:</span>
					<el-input v-model="params.name" clearable style="width: 200px;" placeholder="请输入收购产品名称"></el-input>
				</div>
				<div class="query-item">
					<span>收购起始时间:</span>
					<el-date-picker
						value-format="yyyy-MM-dd"
						v-model="params.buyDateS"
						type="date" clearable
						style="width: 160px;"
						placeholder="请选择起始时间">
					</el-date-picker>
				</div>
				<div class="query-item">
					<span>收购结束时间:</span>
					<el-date-picker
						value-format="yyyy-MM-dd"
						v-model="params.buyDateE"
						type="date" clearable
						style="width: 160px;"
						placeholder="请选择结束时间">
					</el-date-picker>
				</div>
				<el-button size="small" type="primary" icon="el-icon-search" @click="searchRow">查询</el-button>
				<div class="changeType">
					<el-select v-model="params.sysModule" @change="changeEnterpriseType">
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
				<el-table-column align="center" label="收购码" width="200">
          <template slot-scope="scope">
            {{ scope.row.traceCode }}
          </template>
        </el-table-column>
				<el-table-column align="center" label="收购产品名称" width="200">
          <template slot-scope="scope">
            {{ scope.row.name }}
          </template>
        </el-table-column>
				<el-table-column align="center" label="收购总价(元)" width="160">
          <template slot-scope="scope">
            {{ scope.row.buyTotal }}
          </template>
        </el-table-column>
				<el-table-column align="center" label="收购数量" width="240">
          <template slot-scope="scope">
            {{ scope.row.buyAmount + getDictionaryName(scope.row.buyAmountUnit) }}
          </template>
        </el-table-column>
				<el-table-column align="center" label="收购时间" width="120">
          <template slot-scope="scope">
            {{ filterTime(scope.row.buyDate) }}
          </template>
        </el-table-column>
				<el-table-column align="center" label="描述" min-width="100">
          <template slot-scope="scope">
            {{ scope.row.description }}
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
		<el-dialog v-cloak title="收购信息" @close="closeDialog" center :visible.sync="dialogShow" width="1000px" class="row-form">
			<el-form :model="row" label-width="120px" class="demo-ruleForm">
				<el-tabs type="border-card" v-model="activeName">
					<el-tab-pane label="基本信息" name="first">
						<el-row>
							<el-col :span="12">
								<el-form-item label="收购码:">
									<el-input v-model="row.traceCode" readonly></el-input>
								</el-form-item>
							</el-col>
							<el-col :span="12">
								<el-form-item label="收购产品名称:">
									<el-input v-model="row.name" readonly></el-input>
								</el-form-item>
							</el-col>
						</el-row>

						<el-row>
							<el-col :span="12">
								<el-form-item label="收购总价(元):">
									<el-input v-model="row.buyTotal" readonly>
									</el-input>
								</el-form-item>
							</el-col>
							<el-col :span="12">
								<el-form-item label="收购时间:">
                  <el-date-picker
										readonly
										value-format="yyyy-MM-dd"
										v-model="row.buyDate"
										type="date">
									</el-date-picker>
								</el-form-item>
							</el-col>
						</el-row>

						<el-row>
							<el-col :span="12">
								<el-form-item label="收购数量:">
									<el-input v-model="row.buyAmount" readonly>
									</el-input>
								</el-form-item>
							</el-col>
							<el-col :span="12">
								<el-form-item label="收购数量单位:">
									<el-select v-model="row.buyAmountUnit" readonly>
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
							<el-col :span="24">
								<el-form-item label="描述:">
									<el-input
										readonly
										type="textarea"
										:rows="2"
										v-model="row.description">
									</el-input>
								</el-form-item>
							</el-col>
						</el-row>
					</el-tab-pane>
					<el-tab-pane label="收购图片" name="second">
						<div class="upload-div">
							<div class="upload-div-list" v-for="(item,index) of imgBuyPictureLists" :key="index">
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
				name: null,
				buyDateS: null,
				buyDateE: null,
				enterpriseId: window.sessionStorage.getItem('enterpriseId'),
				sysModule: 2
			},
			// 企业类型
			enterpriseTypeOptions: [
				{
					label: '种植数据',
					value: 2
				},
				{
					label: '水产数据',
					value: 1
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
			// 收购图片
			baseUrl: this.$baseImgUrl,
			// 预览图片src列表
			srcList: [],
			imgBuyPictureLists: [],
			// 多选框
			amountUnitOptions: []
		}
	},
	created() {
		this.getAmountUnitDictionary();
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
		// 获取全部的列表数据
		async getAllList() {
			this.params.page = this.currentPage;
			this.params.limit = this.pageSize;
			const { data: res } = await this.$http.get('zsSys/BuyInfo/pageByEId', { params: this.params });
			if (res.status !== 200) {
				return this.$message({
					duration: 1000,
					type: 'error',
					message: '获取收购数据失败！'
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
			this.imgBuyPictureLists = [];
			this.activeName = 'first';
			this.getAllList();
		},
		// 展示数据
		showRow(row) {
			this.row = row;
			if(row.buyPictureList && row.buyPictureList.length !== 0) {
        this.imgBuyPictureLists = row.buyPictureList;
        for(let i=0; i<this.imgBuyPictureLists.length; i++) {
          this.srcList.push(this.baseUrl + this.imgBuyPictureLists[i].url);
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
			this.getAllList();
		}
	}
}
</script>

<style scoped lang="less">
</style>