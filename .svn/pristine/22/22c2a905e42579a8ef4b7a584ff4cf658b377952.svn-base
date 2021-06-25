<template>
  <div>
    <!-- 面包屑导航区域 -->
    <el-breadcrumb separator-class="el-icon-arrow-right">
			<el-breadcrumb-item>溯源信息监督</el-breadcrumb-item>
      <el-breadcrumb-item>基本配置</el-breadcrumb-item>
      <el-breadcrumb-item>企业资质管理</el-breadcrumb-item>
    </el-breadcrumb>
    <!-- 卡片视图区域 -->
    <el-card>
			<!-- 查询区域 -->
			<div class="query-condition">
				<div class="query-item">
					<span>企业资质代码:</span>
					<el-input v-model="params.aptitudeCode" clearable style="width: 240px;" placeholder="请输入企业资质代码"></el-input>
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
				<el-table-column align="center" label="企业资质代码" width="240">
          <template slot-scope="scope">
            {{ scope.row.aptitudeCode }}
          </template>
        </el-table-column>
				<el-table-column align="center" label="企业资质类型" width="120">
          <template slot-scope="scope">
            {{ getDictionaryName(scope.row.aptitudeType) }}
          </template>
        </el-table-column>
				<el-table-column align="center" label="发证时间" width="120">
          <template slot-scope="scope">
            {{ filterTime(scope.row.certificationTime) }}
          </template>
        </el-table-column>
				<el-table-column align="center" label="有效时间" width="240">
          <template slot-scope="scope">
            {{ filterTime(scope.row.effectiveTime) }}
          </template>
        </el-table-column>
				<el-table-column align="center" label="许可范围" min-width="100">
          <template slot-scope="scope">
            <el-tooltip effect="light" :content="scope.row.permitRange" placement="top">
              <span>{{ textSubstr(scope.row.permitRange, 20) }}</span>
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
		<!-- 企业资质信息对话框 -->
		<el-dialog v-cloak title="企业资质信息" @close="closeDialog" center :visible.sync="dialogShow" width="1000px" class="row-form">
			<el-form :model="row" label-width="120px" class="demo-ruleForm">
				<el-tabs type="border-card" v-model="activeName">
					<el-tab-pane label="基本信息" name="first">
						<el-row>
							<el-col :span="12">
								<el-form-item label="企业资质类型:">
									<el-select v-model="row.aptitudeType" readonly>
										<el-option
											v-for="item in aptitudeTypeOptions"
											:key="item.code"
											:label="item.name"
											:value="item.code">
										</el-option>
									</el-select>
								</el-form-item>
							</el-col>
							<el-col :span="12">
								<el-form-item label="企业资质代码:">
									<el-input v-model="row.aptitudeCode" readonly></el-input>
								</el-form-item>
							</el-col>
						</el-row>

						<el-row>
							<el-col :span="12">
								<el-form-item label="发证单位:">
									<el-input v-model="row.certificationUnitName" readonly></el-input>
								</el-form-item>
							</el-col>
							<el-col :span="12">
								<el-form-item label="发证时间:">
									<el-date-picker
										value-format="yyyy-MM-dd"
										v-model="row.certificationTime"
										type="date" readonly>
									</el-date-picker>
								</el-form-item>
							</el-col>
						</el-row>

						<el-row>
							<el-col :span="12">
								<el-form-item label="有效期至:">
                  <el-date-picker
										value-format="yyyy-MM-dd"
										v-model="row.effectiveTime"
										type="date" readonly>
									</el-date-picker>
								</el-form-item>
							</el-col>
						</el-row>

						<el-row>
							<el-col :span="24">
								<el-form-item label="许可范围:">
									<el-input v-model="row.permitRange"
										type="textarea"
										:rows="2" readonly>
									</el-input>
								</el-form-item>
							</el-col>
						</el-row>
					</el-tab-pane>
					<el-tab-pane label="资质证书" name="second">
						<div class="upload-div">
							<div class="upload-div-list" v-for="(item,index) of imgQualificationCertificateLists" :key="index">
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
				aptitudeCode: null,
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
			dialogShow: false,
			// 活跃tab栏
			activeName: 'first',
			// 资质证书
			baseUrl: this.$baseImgUrl,
			srcList: [],
			imgQualificationCertificateLists: [],
			// 多选框
			aptitudeTypeOptions: []
		}
	},
	created() {
		this.getAptitudeTypeDictionary();
		this.getAllList();
		this.setTableHeight();
		window.onresize = () => {
			this.setTableHeight();
		}
	},
	methods: {
		// 获取数据字典
		async getAptitudeTypeDictionary() {
			const { data: res } = await this.$http.get('comSys/DataDictionary/getAllMayToCondition', {
				params: {
					type: '企业资质类型',
					remark: 'X2'
				}
			});
			if (res.status !== 200) {
				return this.$message({
					duration: 1000,
					type: 'error',
					message: '获取企业资质类型失败！'
				});
			}
			this.aptitudeTypeOptions = res.data;
		},
		// 获取全部的列表数据
		async getAllList() {
			this.params.page = this.currentPage;
			this.params.limit = this.pageSize;
			const { data: res } = await this.$http.get('zsSys/EnterpriseQualification/pageByEId', { params: this.params });
			if (res.status !== 200) {
				return this.$message({
					duration: 1000,
					type: 'error',
					message: '获取企业资质数据失败！'
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
			this.imgQualificationCertificateLists = [];
			this.activeName = 'first';
			this.getAllList();
		},
		// 查看数据
		showRow(row) {
			this.row = row;
			if(row.aptitudeCertificateList && row.aptitudeCertificateList.length !== 0) {
        this.imgQualificationCertificateLists = row.aptitudeCertificateList;
        for(let i=0; i<this.imgQualificationCertificateLists.length; i++) {
          this.srcList.push(this.baseUrl + this.imgQualificationCertificateLists[i].url);
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
			for(let i=0; i<this.aptitudeTypeOptions.length; i++) {
				if(code === this.aptitudeTypeOptions[i].code) {
					return this.aptitudeTypeOptions[i].name;
				}
			}
		},
		// 文字截取
		textSubstr(value, length) {
			let val = value
			if (value == '' || value == undefined) {
				return ''
			}
			if (value.length > length) {
				val = val.substring(0, length) + '...'
			}
			return val
		}
	}
}
</script>

<style scoped lang="less">
</style>