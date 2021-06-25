<template>
  <div>
    <!-- 面包屑导航区域 -->
    <el-breadcrumb separator-class="el-icon-arrow-right">
      <el-breadcrumb-item>溯源信息监督</el-breadcrumb-item>
      <el-breadcrumb-item>企业基本信息</el-breadcrumb-item>
    </el-breadcrumb>
    <!-- 卡片视图区域 -->
    <el-card>
			<el-form :model="row" label-width="160px" class="row-form" style="width: 90%">
				<el-tabs type="border-card">
					<el-tab-pane label="基本信息" :style="curHeight">
						<el-row>
							<el-col :span="12">
								<el-row>
									<el-col :span="24">
										<el-form-item label="企业名称:">
											<el-input v-model="row.name" readonly></el-input>
										</el-form-item>
									</el-col>
								</el-row>
								<el-row>
									<el-col :span="24">
										<el-form-item label="企业类型:">
											<el-select v-model="row.businessType" readonly>
												<el-option
													v-for="item in businessTypeOptions"
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
										<el-form-item label="统一社会信用代码:">
											<el-input v-model="row.socialCode" readonly></el-input>
										</el-form-item>
									</el-col>
								</el-row>
								<el-row>
									<el-col :span="24">
										<el-form-item label="行政区规划:">
											<el-select v-model="row.district" readonly>
												<el-option
													v-for="item in districtOptions"
													:key="item.code"
													:label="item.name"
													:value="item.code">
												</el-option>
											</el-select>
										</el-form-item>
									</el-col>
								</el-row>
							</el-col>
							<el-col :span="12">
								<el-form-item label="企业形象图:">
									<img :src="imageUrl" class="avatar">
								</el-form-item>
							</el-col>
						</el-row>

						<el-row>
							<el-col :span="12">
								<el-form-item label="组织形式:">
									<el-select v-model="row.organizationForm" readonly>
										<el-option
											v-for="item in organizationFormOptions"
											:key="item.code"
											:label="item.name"
											:value="item.code">
										</el-option>
									</el-select>
								</el-form-item>
							</el-col>
							<el-col :span="12">
								<el-form-item label="主体属性:">
									<el-select v-model="row.subjectAttribute" readonly>
										<el-option
											v-for="item in subjectAttributeOptions"
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
								<el-form-item label="注册地址:">
									<el-input v-model="row.registerAddress" readonly></el-input>
								</el-form-item>
							</el-col>
							<el-col :span="12">
								<el-form-item prop="establishDate" label="企业成立日期:">
									<el-date-picker
										v-model="row.establishDate"
										type="date"
										clearable readonly>
									</el-date-picker>
								</el-form-item>
							</el-col>
						</el-row>

						<el-row>
							<el-col :span="12">
								<el-form-item label="注册资金(万元):">
									<el-input v-model="row.registerMoney" readonly></el-input>
								</el-form-item>
							</el-col>
							<el-col :span="12">
								<el-form-item label="联系人:">
									<el-input v-model="row.contactPerson" readonly></el-input>
								</el-form-item>
							</el-col>
						</el-row>

						<el-row>
							<el-col :span="12">
								<el-form-item label="联系人手机号:">
									<el-input v-model="row.phone" readonly></el-input>
								</el-form-item>
							</el-col>
							<el-col :span="12">
								<el-form-item label="电子邮箱:">
									<el-input v-model="row.mailbox" readonly></el-input>
								</el-form-item>
							</el-col>
						</el-row>

						<el-row>
							<el-col :span="12">
								<el-form-item label="法定代表人姓名:">
									<el-input v-model="row.deputyName" readonly></el-input>
								</el-form-item>
							</el-col>
							<el-col :span="12">
								<el-form-item label="法定代表人身份证号:">
									<el-input v-model="row.deputyIdCard" readonly></el-input>
								</el-form-item>
							</el-col>
						</el-row>

						<el-row>
							<el-col :span="12">
								<el-form-item label="法定代表人联系电话:">
									<el-input v-model="row.deputyPhone" readonly></el-input>
								</el-form-item>
							</el-col>
							<el-col :span="12">
								<el-form-item label="邮政编码:">
									<el-input v-model="row.postcode" readonly></el-input>
								</el-form-item>
							</el-col>
						</el-row>

						<el-row>
							<el-col :span="24">
								<el-form-item label="经纬度:">
										<el-input v-model="row.longitude" readonly style="width: 21%;"></el-input>
										<span> - </span>
										<el-input v-model="row.latitude" readonly style="width: 21%;"></el-input>
								</el-form-item>
							</el-col>
						</el-row>
					</el-tab-pane>
					<el-tab-pane label="营业执照" :style="curHeight">
						<div class="upload-div">
							<div class="upload-div-list" v-for="(item,index) of imgBusinessLicenseLists" :key="index">
								<el-image :src="baseUrl + item.url" :preview-src-list="srcList"></el-image>
							</div>
						</div>
					</el-tab-pane>
					<el-tab-pane label="法人身份证照片（请上传身份证正面照片和反面照片）" :style="curHeight">
						<div class="upload-div">
							<div class="upload-div-list" v-for="(item,index) of imgCorporateIdentityCardLists" :key="index">
								<el-image :src="baseUrl + item.url" :preview-src-list="srcList2"></el-image>
							</div>
						</div>
					</el-tab-pane>
				</el-tabs>
			</el-form>
    </el-card>
  </div>
</template>

<script>
export default {
  data() {
		return {
			// 卡片高度
			curHeight: {
				height: '',
				overflow: 'auto'
			},
			// 表单
			row: {},
			// 企业形象图
			baseUrl: this.$baseImgUrl,
			imageUrl: '',
			// 营业执照
			imgBusinessLicenseLists: [],
			srcList: [],
			// 法人身份证照片
			imgCorporateIdentityCardLists: [],
			srcList2: [],
			// 多选框
			businessTypeOptions: [],
			organizationFormOptions: [],
			subjectAttributeOptions: [],
			districtOptions: []
		}
	},
	created() {
		this.getBusinessTypeDictionary();
		this.getOrganizationFormDictionary();
		this.getSubjectAttributeDictionary();
		this.getDistrictDictionary();
		this.getList();
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
		// 设定表格高度
		setTableHeight() {
      let h = document.documentElement.clientHeight || document.body.clientHeight;
			this.curHeight.height = h - 382 + 'px';
		},
		// 获取数据
		async getList() {
			this.row = {};
			this.srcList = [];
			this.srcList2 = [];
			this.imgBusinessLicenseLists = [];
			this.imgCorporateIdentityCardLists = [];
			const { data: res } = await this.$http.get('zsSys/Enterprise/' + window.sessionStorage.getItem('enterpriseId'));
			if (res.status !== 200) {
				return this.$message({
					duration: 1000,
					type: 'error',
					message: '获取企业基本信息失败！'
				});
			}
			this.row = res.data;
			if(res.data.enterpriseImageList.length !== 0) {
				this.imageUrl = this.baseUrl + res.data.enterpriseImageList[0].url;
			}
			if(res.data.businessLicenseList.length !== 0) {
        this.imgBusinessLicenseLists = res.data.businessLicenseList;
        for(let i=0; i<this.imgBusinessLicenseLists.length; i++) {
          this.srcList.push(this.baseUrl + this.imgBusinessLicenseLists[i].url);
        }
			}
			if(res.data.deputyIdCardPhotoList.length !== 0) {
        this.imgCorporateIdentityCardLists = res.data.deputyIdCardPhotoList;
        for(let i=0; i<this.imgCorporateIdentityCardLists.length; i++) {
          this.srcList2.push(this.baseUrl + this.imgCorporateIdentityCardLists[i].url);
        }
			}
		},
	}
}
</script>

<style scoped lang="less">
	// 表单居中
	.el-form {
		margin: 0 auto;
	}

	// 企业形象图
	.avatar {
		width: 220px;
		height: 220px;
	}
</style>