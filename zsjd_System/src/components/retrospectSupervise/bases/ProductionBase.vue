<template>
  <div>
    <!-- 面包屑导航区域 -->
    <el-breadcrumb separator-class="el-icon-arrow-right">
			<el-breadcrumb-item>溯源信息监督</el-breadcrumb-item>
      <el-breadcrumb-item>基本配置</el-breadcrumb-item>
      <el-breadcrumb-item>生产基地管理</el-breadcrumb-item>
    </el-breadcrumb>
    <!-- 卡片视图区域 -->
    <el-card>
			<!-- 查询区域 -->
			<div class="query-condition">
				<div class="query-item">
					<span>生产基地名称:</span>
					<el-input v-model="params.name" clearable style="width: 240px;" placeholder="请输入生产基地名称"></el-input>
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
				<el-table-column align="center" label="生产基地名称" width="240">
          <template slot-scope="scope">
            {{ scope.row.name }}
          </template>
        </el-table-column>
				<el-table-column align="center" label="基地面积/规模" width="140">
          <template slot-scope="scope">
            {{ scope.row.area + '亩'}}
          </template>
        </el-table-column>
				<el-table-column align="center" label="基地负责人" width="140">
          <template slot-scope="scope">
            {{ scope.row.lender }}
          </template>
        </el-table-column>
				<el-table-column align="center" label="基地负责人电话" width="120">
          <template slot-scope="scope">
            {{ scope.row.lenderPhone }}
          </template>
        </el-table-column>
				<el-table-column align="center" label="生产环境检验结果" width="140">
          <template slot-scope="scope">
            <el-tag type="success" v-if="scope.row.isReportQualified">合格</el-tag>
						<el-tag type="danger" v-else>不合格</el-tag>
          </template>
        </el-table-column>
				<el-table-column align="center" label="生产基地位置" min-width="100">
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
		<!-- 生产基地信息对话框 -->
		<el-dialog v-cloak title="生产基地信息" @close="closeDialog" center :visible.sync="dialogShow" width="1000px" class="row-form">
			<el-form :model="row" label-width="150px" class="demo-ruleForm">
				<el-tabs type="border-card" v-model="activeName">
					<el-tab-pane label="基本信息" name="first">
						<el-row>
							<el-col :span="12">
								<el-form-item label="生产基地名称:">
									<el-input v-model="row.name" readonly></el-input>
								</el-form-item>
							</el-col>
							<el-col :span="12">
								<el-form-item label="生产基地位置:">
									<el-input v-model="row.address" readonly></el-input>
								</el-form-item>
							</el-col>
						</el-row>

						<el-row>
							<el-col :span="12">
								<el-form-item label="基地面积/规模(亩):">
									<el-input v-model="row.area" readonly></el-input>
								</el-form-item>
							</el-col>
							<el-col :span="12">
								<el-form-item label="生产环境检验结果:">
									<el-tag type="success" v-if="row.isReportQualified">合格</el-tag>
									<el-tag type="danger" v-else>不合格</el-tag>
								</el-form-item>
							</el-col>
						</el-row>

						<el-row>
							<el-col :span="12">
								<el-form-item label="基地负责人:">
                  <el-input v-model="row.lender" readonly></el-input>
								</el-form-item>
							</el-col>
							<el-col :span="12">
								<el-form-item label="基地负责人电话:">
									<el-input v-model="row.lenderPhone" readonly></el-input>
								</el-form-item>
							</el-col>
						</el-row>
					</el-tab-pane>
					<el-tab-pane label="基地图片" name="second">
						<div class="upload-div">
							<div class="upload-div-list" v-for="(item,index) of imgBasePictureLists" :key="index">
								<el-image :src="baseUrl + item.url" :preview-src-list="srcList2"></el-image>
							</div>
						</div>
					</el-tab-pane>
					<el-tab-pane label="检验报告" name="third">
						<div class="upload-div">
							<div class="upload-div-list" v-for="(item,index) of imgInspectionReportLists" :key="index">
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
				baseType: '加工基地',
				enterpriseId: window.sessionStorage.getItem('enterpriseId')
			},
			// 表单
			rows: [],
			row: {},
			// 分页
			currentPage: 1,
			total: 0,
			pageSize: 10,
			// 表单高度
			curHeight: 0,
			// 活跃tab栏
			activeName: 'first',
			// 对话框
			dialogShow: false,
			// 生产基地环境检验结果图片
			baseUrl: this.$baseImgUrl,
			srcList: [],
			srcList2: [],
			imgInspectionReportLists: [],
			imgBasePictureLists: [],
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
			const { data: res } = await this.$http.get('zsSys/BaseInfo/pageByEId', { params: this.params });
			if (res.status !== 200) {
				return this.$message({
					duration: 1000,
					type: 'error',
					message: '获取生产基地数据失败！'
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
			this.srcList2 = [];
			this.imgInspectionReportLists = [];
			this.imgBasePictureLists = [];
			this.activeName = 'first';
			this.getAllList();
		},
		// 查看数据
		showRow(row) {
			this.row = row;
			if(row.reportPictureList && row.reportPictureList.length !== 0) {
        this.imgInspectionReportLists = row.reportPictureList;
        for(let i=0; i<this.imgInspectionReportLists.length; i++) {
          this.srcList.push(this.baseUrl + this.imgInspectionReportLists[i].url);
        }
			}
			if(row.basePictureList && row.basePictureList.length !== 0) {
        this.imgBasePictureLists = row.basePictureList;
        for(let i=0; i<this.imgBasePictureLists.length; i++) {
          this.srcList2.push(this.baseUrl + this.imgBasePictureLists[i].url);
        }
			}
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