<template>
  <div>
    <!-- 面包屑导航区域 -->
    <el-breadcrumb separator-class="el-icon-arrow-right">
      <el-breadcrumb-item>首页</el-breadcrumb-item>
      <el-breadcrumb-item>基本配置</el-breadcrumb-item>
      <el-breadcrumb-item>企业资质管理</el-breadcrumb-item>
    </el-breadcrumb>
    <!-- 卡片视图区域 -->
    <el-card>
			<!-- 查询区域 -->
			<div class="query-condition">
				<div class="query-item">
					<span>企业资质代码:</span>
					<el-input v-model="params.aptitudeCode" clearable style="width: 200px;" placeholder="请输入企业资质代码"></el-input>
				</div>
				<el-button size="small" type="primary" icon="el-icon-search" @click="searchRow">查询</el-button>
			</div>
			<!-- 按钮操作区域 -->
			<div>
				<el-button size="small" type="success" icon="el-icon-plus" @click="addRow">新增</el-button>
				<el-button size="small" type="danger" icon="el-icon-delete" @click="deleteList">删除</el-button>
			</div>
			<!-- 表格显示区域 -->
			<el-table :data="rows"
				ref="myTable"
				border stripe fit highlight-current-row 
				:row-style="{height:'5px'}"
				:cell-style="{padding:'5px 0'}"
				:height="curHeight"
				@selection-change="handleSelectionChange">
				<el-table-column type="selection" align="center"></el-table-column>
				<el-table-column align="center" label="序号" width="60">
          <template slot-scope="scope">
            {{ (currentPage - 1) * pageSize + scope.$index + 1 }}
          </template>
        </el-table-column>
				<el-table-column align="center" label="操作" width="100">
          <template slot-scope="scope">
            <el-button size="mini"  icon="el-icon-edit-outline" @click="editRow(scope.row)">编辑</el-button>
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
				<el-table-column align="center" label="发证单位" width="240">
          <template slot-scope="scope">
            {{ scope.row.certificationUnitName }}
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
		<!-- 新增编辑对话框 -->
		<el-dialog v-cloak title="企业资质信息" @close="closeDialog" center :visible.sync="dialogShow" width="1000px" class="row-form">
			<el-form :model="row" label-width="120px" class="demo-ruleForm" :rules="rules" ref="row">
				<el-tabs type="border-card" v-model="activeName">
					<el-tab-pane label="基本信息" name="first">
						<el-row>
							<el-col :span="12">
								<el-form-item label="企业资质类型:" prop="aptitudeType">
									<el-select v-model="row.aptitudeType" clearable placeholder="请选择企业资质类型">
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
								<el-form-item label="企业资质代码:" prop="aptitudeCode">
									<el-input v-model="row.aptitudeCode" clearable maxlength="50" placeholder="请输入企业资质代码"></el-input>
								</el-form-item>
							</el-col>
						</el-row>

						<el-row>
							<el-col :span="12">
								<el-form-item label="发证时间:" prop="certificationTime">
									<el-date-picker
										value-format="yyyy-MM-dd"
										v-model="row.certificationTime"
										type="date" clearable
										placeholder="请选择发证时间">
									</el-date-picker>
								</el-form-item>
							</el-col>
							<el-col :span="12">
								<el-form-item label="有效期至:" prop="effectiveTime">
                  <el-date-picker
										value-format="yyyy-MM-dd"
										v-model="row.effectiveTime"
										type="date" clearable
										placeholder="请选择有效期">
									</el-date-picker>
								</el-form-item>
							</el-col>
						</el-row>

						<el-row>
							<el-col :span="24">
								<el-form-item label="发证单位:" prop="certificationUnitName">
									<el-input v-model="row.certificationUnitName" clearable maxlength="50" placeholder="请输入发证单位"></el-input>
								</el-form-item>
							</el-col>
						</el-row>

						<el-row>
							<el-col :span="24">
								<el-form-item label="许可范围:" prop="permitRange">
									<el-input v-model="row.permitRange"
										type="textarea"
										:rows="2"
										placeholder="请输入许可范围"
										maxlength="255" clearable>
									</el-input>
								</el-form-item>
							</el-col>
						</el-row>
					</el-tab-pane>
					<el-tab-pane label="资质证书" name="second">
						<!-- 上传 -->
						<el-upload
              :action="updateUrl"
							multiple
              :http-request="httpRequest"
              list-type="picture-card"
              :on-preview="handleQualificationCertificatePreview"
              :on-remove="handleQualificationCertificateRemove"
              :before-upload="beforeAvatarUpload"
              :on-change="handleChangeQualificationCertificatePhoto"
              :file-list="fileQualificationCertificateLists">
              <i class="el-icon-plus"></i>
            </el-upload>
						<!-- 预览 -->
						<el-dialog :visible.sync="dialogQualificationCertificateVisible" width="70%" top="5vh" :append-to-body="true">
							<img width="100%" :src="dialogQualificationCertificateImageUrl" alt="">
						</el-dialog>
						<!-- 展示 -->
						<div class="upload-div">
							<div class="upload-div-list" v-for="(item,index) of imgQualificationCertificateLists" :key="index">
								<div class="upload-div-top">
									<span class="upload-div-del" @click="fileQualificationCertificateDelIdPhoto(index,item.id)">X</span>
								</div>
								<el-image :src="baseUrl + item.url" :preview-src-list="srcList"></el-image>
							</div>
						</div>
					</el-tab-pane>
				</el-tabs>

				<span class="t-btn">
					<el-button type="info" @click="dialogShow = false">取 消</el-button>
					<el-button type="success" @click="submitRow">提 交</el-button>
				</span>
			</el-form>
		</el-dialog>
  </div>
</template>

<script>
import { Debounce } from "../../assets/js/tool";
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
			// 修改or新增flag
			flag: '',
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
			// 选中删除项
			selectRows: [],
			// 资质证书
			updateUrl: '#',
			baseUrl: this.$baseImgUrl,
			// 预览图片src列表
			srcList: [],
			picQualificationCertificateListPhoto: [],
			dialogQualificationCertificateImageUrl: '',
			dialogQualificationCertificateVisible: false,
			imgQualificationCertificateLists: [],
			// 上传文件
			fileQualificationCertificateLists:[],
			// 多选框
			aptitudeTypeOptions: [],
			// 验证规则
			rules: {
				aptitudeCode: [{ required: true, message: '请输入企业资质代码', trigger: 'blur' }],
				aptitudeType: [{ required: true, message: '请选择企业资质类型', trigger: 'blur' }],
				// certificationUnitName: [{ required: true, message: '请输入发证单位', trigger: 'blur' }],
				certificationTime: [{ required: true, message: '请选择发证时间', trigger: 'blur' }],
				// effectiveTime: [{ required: true, message: '请选择有效期', trigger: 'blur' }],
				// permitRange: [{ required: true, message: '请输入许可范围', trigger: 'blur' }]
			}
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
      this.curHeight = h - 380;
		},
		// 处理选中数据
		handleSelectionChange(val) {
			this.selectRows = val;
		},
		// 清空全部筛选
		clearFilter() {
      this.$refs.myTable.clearSelection();
		},
		// 处理图片
		httpRequest(data) {
			let rd = new FileReader(); // 创建文件读取对象
			let file = data.file;
			rd.readAsDataURL(file); // 文件读取装换为base64类型
			rd.onloadend = function () {
				this.dialogQualificationCertificateImageUrl = this.result; // this指向当前方法onloadend的作用域
			};
		},
		beforeAvatarUpload(file) {
			const isJPG = file.type === 'image/jpeg';
			const isPng = file.type === 'image/png';
			if (!isJPG && !isPng) {
				this.$message.error('上传图片格式只能为.jpg或.png!');
			}
			// const isLt10M = file.size / 1024 / 1024 < 10;
			// if (!isLt10M) {
      //   this.$message.error('上传头像图片大小不能超过 10MB!');
			// }
			return isJPG || isPng;
		},
		// 资质证书图片预览对话框
		handleQualificationCertificatePreview(file) {
			this.dialogQualificationCertificateImageUrl = file.url;
			this.dialogQualificationCertificateVisible = true;
		},
		// 删除要上传列表中的图片
		handleQualificationCertificateRemove(file, fileLists) {
			for (let i in this.picQualificationCertificateListPhoto) {
				if (this.picQualificationCertificateListPhoto[i].key === file.uid) {
					this.picQualificationCertificateListPhoto.splice(i, 1)
				}
			}
			this.fileQualificationCertificateLists = fileLists;
		},
		// 上传图片列表文件
		handleChangeQualificationCertificatePhoto(file, fileList) {
			this.fileQualificationCertificateLists = fileList;
		},
		// 删除已上传的图片
		fileQualificationCertificateDelIdPhoto(index,id){
			this.$confirm('此操作将永久删除该图片, 是否继续?', '提示', {
				confirmButtonText: '确定',
				cancelButtonText: '取消',
				type: 'warning'
			}).then(() => {
				this.row.delFileIds.push(id);
        this.imgQualificationCertificateLists.splice(index,1);
			}).catch(() => {
				this.$message({
					duration: 1000,
					type: 'info',
					message: '已取消操作'
				});          
			});
    },
		// 关闭对话框
		closeDialog() {
			this.row = {};
			this.srcList = [];
			this.picQualificationCertificateListPhoto = [];
			this.imgQualificationCertificateLists = [];
			this.fileQualificationCertificateLists = [];
			this.activeName = 'first';
			this.getAllList();
			this.$refs.row.clearValidate();
		},
		// 编辑数据
		editRow(row) {
			this.row = row;
			this.row.delFileIds = [];
			this.dialogShow = true;
			if(row.aptitudeCertificateList && row.aptitudeCertificateList.length !== 0) {
        this.imgQualificationCertificateLists = row.aptitudeCertificateList;
        for(let i=0; i<this.imgQualificationCertificateLists.length; i++) {
          this.srcList.push(this.baseUrl + this.imgQualificationCertificateLists[i].url);
        }
			}
			this.dialogShow = true;
			this.flag = 'edit';
		},
		// 新增数据
		addRow() {
			this.dialogShow = true;
			this.flag = 'add';
		},
		// 查询数据
		searchRow() {
			this.currentPage = 1;
			this.getAllList();
		},
		// 删除数据
		deleteList() {
			this.$confirm('此操作将删除选中的数据, 是否继续?', '提示', {
				confirmButtonText: '确定',
				cancelButtonText: '取消',
				type: 'warning'
			}).then(async () => {
				let delIds=[];
        for (let i=0; i<this.selectRows.length; i++){
          let item = this.selectRows[i];
          delIds.push(item.id);
				}
				const { data: res } = await this.$http.post('zsSys/EnterpriseQualification/batchDelete', delIds);
				if(res.status !== 200) {
					return this.$message({
						duration: 2000,
						type: 'error',
						message: '删除企业资质失败！'
					});
				}
				this.$message({
					duration: 2000,
					type: 'success',
					message: '删除企业资质成功！'
				});
				this.clearFilter();
				this.getAllList();
			}).catch(() => {
				this.$message({
					duration: 1000,
					type: 'info',
					message: '已取消操作'
				});          
			});
		},
		// 提交数据
		submitRow: Debounce(function() {
			this.row.enterpriseId = window.sessionStorage.getItem('enterpriseId');
			this.$refs.row.validate(async valid => {
				if(!valid) return this.$message.error('信息填写不完整或不准确，请检查再提交！');
				let fd = new FormData();
				for(let key in this.row) {
					let value = this.row[key];
					// 过滤
					if(key=='aptitudeCertificateList') {
						// fd.append(key, '');
          } else {
            if (value !== null) {
              fd.append(key, value);
            }
          }
				}

				//图片文件
        if (this.fileQualificationCertificateLists != null) {
					for (let i = 0; i < this.fileQualificationCertificateLists.length; i++) {
            fd.append('aptitudeCertificateFiles', this.fileQualificationCertificateLists[i].raw);
          }
        }

				if(this.flag === 'add') {
					const { data: res } = await this.$http.post('zsSys/EnterpriseQualification/addDataAndFile', fd);
					if(res.status !== 200) {
						return this.$message({
							duration: 2000,
							type: 'error',
							message: '新增企业资质失败！'
						});
					}
					this.getAllList();
					this.dialogShow = false;
					this.$message({
						duration: 2000,
						type: 'success',
						message: '新增企业资质成功！'
					});
				} else if(this.flag === 'edit') {
					const { data: res } = await this.$http.put('zsSys/EnterpriseQualification/updateDataAndFile', fd);
					if(res.status !== 200) {
						return this.$message({
							duration: 2000,
							type: 'error',
							message: '修改企业资质失败！'
						});
					}
					this.getAllList();
					this.dialogShow = false;
					this.$message({
						duration: 2000,
						type: 'success',
						message: '修改企业资质成功！'
					});
				}
			});
		}, 3000),
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