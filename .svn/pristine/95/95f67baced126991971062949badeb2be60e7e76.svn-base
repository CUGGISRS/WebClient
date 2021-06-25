<template>
  <div>
    <!-- 面包屑导航区域 -->
    <el-breadcrumb separator-class="el-icon-arrow-right">
      <el-breadcrumb-item>首页</el-breadcrumb-item>
      <el-breadcrumb-item>基本配置</el-breadcrumb-item>
      <el-breadcrumb-item>生产基地管理</el-breadcrumb-item>
    </el-breadcrumb>
    <!-- 卡片视图区域 -->
    <el-card>
			<!-- 查询区域 -->
			<div class="query-condition">
				<div class="query-item">
					<span>生产基地名称:</span>
					<el-input v-model="params.name" clearable style="width: 200px;" placeholder="请输入生产基地名称"></el-input>
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
				<el-table-column align="center" label="基地负责人" width="160">
          <template slot-scope="scope">
            {{ scope.row.lender }}
          </template>
        </el-table-column>
				<el-table-column align="center" label="基地负责人电话" width="160">
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
				<el-table-column align="center" label="生产基地位置" min-width="120">
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
		<!-- 新增编辑对话框 -->
		<el-dialog v-cloak title="生产基地信息" @close="closeDialog" center :visible.sync="dialogShow" width="1000px" class="row-form">
			<el-form :model="row" label-width="150px" class="demo-ruleForm" :rules="rules" ref="row">
				<el-tabs type="border-card" v-model="activeName">
					<el-tab-pane label="基本信息" name="first">
						<el-row>
							<el-col :span="12">
								<el-form-item label="生产基地名称:" prop="name">
									<el-input v-model="row.name" clearable maxlength="50" placeholder="请输入生产基地名称"></el-input>
								</el-form-item>
							</el-col>
							<el-col :span="12">
								<el-form-item label="生产基地位置:" prop="address">
									<el-input v-model="row.address" clearable maxlength="50" placeholder="请输入生产基地位置"></el-input>
								</el-form-item>
							</el-col>
						</el-row>

						<el-row>
							<el-col :span="12">
								<el-form-item label="基地面积/规模(亩):" prop="area">
									<el-input v-model="row.area"
										clearable
										onkeyup="value=value.replace(/[^\d^\.]/g,'')"
										placeholder="请输入基地面积/规模(亩)"
										@blur="areaChange">
									</el-input>
								</el-form-item>
							</el-col>
							<el-col :span="12">
								<el-form-item label="生产环境检验结果:" prop="isReportQualified">
									<el-radio v-model="row.isReportQualified" :label="1">合格</el-radio>
									<el-radio v-model="row.isReportQualified" :label="0">不合格</el-radio>
								</el-form-item>
							</el-col>
						</el-row>

						<el-row>
							<el-col :span="12">
								<el-form-item label="基地负责人:" prop="lender">
                  <el-input v-model="row.lender" clearable maxlength="50" placeholder="请输入基地负责人姓名"></el-input>
								</el-form-item>
							</el-col>
							<el-col :span="12">
								<el-form-item label="基地负责人电话:" prop="lenderPhone">
									<el-input v-model="row.lenderPhone"
										clearable maxlength="50"
										onkeyup="value=value.replace(/[^\d]/g,'')"
										placeholder="请输入基地负责人电话"
										@blur="lenderPhoneChange">
									</el-input>
								</el-form-item>
							</el-col>
						</el-row>
					</el-tab-pane>
					<el-tab-pane label="基地图片" name="second">
						<!-- 上传 -->
						<el-upload
              :action="updateUrl"
							multiple
              :http-request="httpRequest2"
              list-type="picture-card"
              :on-preview="handleBasePicturePreview"
              :on-remove="handleBasePictureRemove"
              :before-upload="beforeAvatarUpload"
              :on-change="handleChangeBasePicturePhoto"
              :file-list="fileBasePictureLists">
              <i class="el-icon-plus"></i>
            </el-upload>
						<!-- 预览 -->
						<el-dialog :visible.sync="dialogBasePictureVisible" width="70%" top="5vh" :append-to-body="true">
							<img width="100%" :src="dialogBasePictureImageUrl" alt="">
						</el-dialog>
						<!-- 展示 -->
						<div class="upload-div">
							<div class="upload-div-list" v-for="(item,index) of imgBasePictureLists" :key="index">
								<div class="upload-div-top">
									<span class="upload-div-del" @click="fileBasePictureDelIdPhoto(index,item.id)">X</span>
								</div>
								<el-image :src="baseUrl + item.url" :preview-src-list="srcList2"></el-image>
							</div>
						</div>
					</el-tab-pane>
					<el-tab-pane label="检验报告" name="third">
						<!-- 上传 -->
						<el-upload
              :action="updateUrl"
							multiple
              :http-request="httpRequest"
              list-type="picture-card"
              :on-preview="handleInspectionReportPreview"
              :on-remove="handleInspectionReportRemove"
              :before-upload="beforeAvatarUpload"
              :on-change="handleChangeInspectionReportPhoto"
              :file-list="fileInspectionReportLists">
              <i class="el-icon-plus"></i>
            </el-upload>
						<!-- 预览 -->
						<el-dialog :visible.sync="dialogInspectionReportVisible" width="70%" top="5vh" :append-to-body="true">
							<img width="100%" :src="dialogInspectionReportImageUrl" alt="">
						</el-dialog>
						<!-- 展示 -->
						<div class="upload-div">
							<div class="upload-div-list" v-for="(item,index) of imgInspectionReportLists" :key="index">
								<div class="upload-div-top">
									<span class="upload-div-del" @click="fileInspectionReportDelIdPhoto(index,item.id)">X</span>
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
import { checkPhoneNumber, Debounce } from "../../assets/js/tool";
export default {
  data() {
		var validatePhone = (rule, value, callback) => {
      if (!checkPhoneNumber(value)) {
        callback(new Error('手机号不正确!'));
      } else {
        callback();
      }
    };
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
			// 修改or新增flag
			flag: '',
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
			// 选中删除项
			selectRows: [],
			// 生产基地环境检验结果图片
			updateUrl: '#',
			baseUrl: this.$baseImgUrl,
			// 预览图片src列表
			srcList: [],
			srcList2: [],
			picInspectionReportListPhoto: [],
			dialogInspectionReportImageUrl: '',
			dialogInspectionReportVisible: false,
			imgInspectionReportLists: [],
			// 上传文件
			fileInspectionReportLists:[],
			// 基地图片
			picBasePictureListPhoto: [],
			dialogBasePictureImageUrl: '',
			dialogBasePictureVisible: false,
			imgBasePictureLists: [],
			// 上传文件
			fileBasePictureLists:[],
			// 验证规则
			rules: {
				name: [{ required: true, message: '请输入生产基地名称', trigger: 'blur' }],
				address: [{ required: true, message: '请输入生产基地位置', trigger: 'blur' }],
				area: [{ required: true, message: '请输入基地面积/规模', trigger: 'blur' }],
				isReportQualified: [{ required: true, message: '请选择生产环境检验结果', trigger: 'blur' }],
				lender: [{ required: true, message: '请输入基地负责人', trigger: 'blur' }],
				lenderPhone: [
					{ required: true, message: '请输入基地负责人电话', trigger: 'blur' },
					{ required: true, validator: validatePhone, trigger: 'blur' }
				]
			}
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
				this.dialogInspectionReportImageUrl = this.result; // this指向当前方法onloadend的作用域
			};
		},
		httpRequest2(data) {
			let rd = new FileReader(); // 创建文件读取对象
			let file = data.file;
			rd.readAsDataURL(file); // 文件读取装换为base64类型
			rd.onloadend = function () {
				this.dialogBasePictureImageUrl = this.result; // this指向当前方法onloadend的作用域
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
		// 检验报告图片预览对话框
		handleInspectionReportPreview(file) {
			this.dialogInspectionReportImageUrl = file.url;
			this.dialogInspectionReportVisible = true;
		},
		// 删除要上传列表中的图片
		handleInspectionReportRemove(file, fileLists) {
			for (let i in this.picInspectionReportListPhoto) {
				if (this.picInspectionReportListPhoto[i].key === file.uid) {
					this.picInspectionReportListPhoto.splice(i, 1)
				}
			}
			this.fileInspectionReportLists = fileLists;
		},
		// 上传图片列表文件
		handleChangeInspectionReportPhoto(file, fileList) {
			this.fileInspectionReportLists = fileList;
		},
		// 删除已上传的图片
		fileInspectionReportDelIdPhoto(index,id){
			this.$confirm('此操作将永久删除该图片, 是否继续?', '提示', {
				confirmButtonText: '确定',
				cancelButtonText: '取消',
				type: 'warning'
			}).then(() => {
				this.row.delFileIds.push(id);
        this.imgInspectionReportLists.splice(index,1);
			}).catch(() => {
				this.$message({
					duration: 1000,
					type: 'info',
					message: '已取消操作'
				});          
			});
		},
		// 基地图片预览对话框
		handleBasePicturePreview(file) {
			this.dialogBasePictureImageUrl = file.url;
			this.dialogBasePictureVisible = true;
		},
		// 删除要上传列表中的图片
		handleBasePictureRemove(file, fileLists) {
			for (let i in this.picBasePictureListPhoto) {
				if (this.picBasePictureListPhoto[i].key === file.uid) {
					this.picBasePictureListPhoto.splice(i, 1)
				}
			}
			this.fileBasePictureLists = fileLists;
		},
		// 上传图片列表文件
		handleChangeBasePicturePhoto(file, fileList) {
			this.fileBasePictureLists = fileList;
		},
		// 删除已上传的图片
		fileBasePictureDelIdPhoto(index,id){
			this.$confirm('此操作将永久删除该图片, 是否继续?', '提示', {
				confirmButtonText: '确定',
				cancelButtonText: '取消',
				type: 'warning'
			}).then(() => {
				this.row.delFileIds.push(id);
        this.imgBasePictureLists.splice(index,1);
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
			this.srcList2 = [];
			this.picInspectionReportListPhoto = [];
			this.imgInspectionReportLists = [];
			this.fileInspectionReportLists = [];
			this.picBasePictureListPhoto = [];
			this.imgBasePictureLists = [];
			this.fileBasePictureLists = [];
			this.activeName = 'first';
			this.getAllList();
			this.$refs.row.clearValidate();
		},
		// 编辑数据
		editRow(row) {
			this.row = row;
			this.row.delFileIds = [];
			if(row.reportPictureList && row.reportPictureList.length !== 0) {
        this.imgInspectionReportLists = row.reportPictureList;
        for(let i=0; i<this.imgInspectionReportLists.length; i++) {
          this.srcList.push(this.baseUrl + this.imgInspectionReportLists[i].url);
        }
			}
			if(row.reportPictureList && row.basePictureList.length !== 0) {
        this.imgBasePictureLists = row.basePictureList;
        for(let i=0; i<this.imgBasePictureLists.length; i++) {
          this.srcList2.push(this.baseUrl + this.imgBasePictureLists[i].url);
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
				const { data: res } = await this.$http.post('zsSys/BaseInfo/batchDelete', delIds);
				if(res.status !== 200) {
					return this.$message({
						duration: 2000,
						type: 'error',
						message: '删除生产基地失败！'
					});
				}
				this.$message({
					duration: 2000,
					type: 'success',
					message: '删除生产基地成功！'
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
			this.row.baseType = '加工基地';
			this.row.enterpriseId = window.sessionStorage.getItem('enterpriseId');
			this.$refs.row.validate(async valid => {
				if(!valid) return this.$message.error('信息填写不完整或不准确，请检查再提交！');
				let fd = new FormData();
				for(let key in this.row) {
					let value = this.row[key];
					// 过滤
					if(key === 'reportPictureList') {
						// fd.append(key, '');
          } else if(key=='basePictureList') {
						// fd.append(key, '');
					} else {
            if (value !== null) {
              fd.append(key, value);
            }
          }
				}

				//图片文件
        if (this.fileInspectionReportLists != null) {
					for (let i = 0; i < this.fileInspectionReportLists.length; i++) {
            fd.append('reportPictureFiles', this.fileInspectionReportLists[i].raw);
          }
				}
				if (this.fileBasePictureLists != null) {
					for (let i = 0; i < this.fileBasePictureLists.length; i++) {
            fd.append('basePictureFiles',this.fileBasePictureLists[i].raw);
          }
        }

				if(this.flag === 'add') {
					const { data: res } = await this.$http.post('zsSys/BaseInfo/addDataAndFile', fd);
					if(res.status !== 200) {
						return this.$message({
							duration: 2000,
							type: 'error',
							message: '新增生产基地失败！'
						});
					}
					this.getAllList();
					this.dialogShow = false;
					this.$message({
						duration: 2000,
						type: 'success',
						message: '新增生产基地成功！'
					});
				} else if(this.flag === 'edit') {
					const { data: res } = await this.$http.put('zsSys/BaseInfo/updateDataAndFile', fd);
					if(res.status !== 200) {
						return this.$message({
							duration: 2000,
							type: 'error',
							message: '修改生产基地失败！'
						});
					}
					this.getAllList();
					this.dialogShow = false;
					this.$message({
						duration: 2000,
						type: 'success',
						message: '修改生产基地成功！'
					});
				}
			});
		}, 3000),
		// 解决只能输入数字输入框的值绑定问题
		areaChange(e) {
			this.$set(this.row, 'area', e.target.value);
		},
		lenderPhoneChange(e) {
			this.$set(this.row, 'lenderPhone', e.target.value);
		}
	}
}
</script>

<style scoped lang="less">
</style>