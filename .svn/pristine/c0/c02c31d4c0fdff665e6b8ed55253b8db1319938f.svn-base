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
		<!-- 新增编辑对话框 -->
		<el-dialog v-cloak title="产品批次检验信息" @close="closeDialog" center :visible.sync="dialogShow" width="1000px" class="row-form">
			<el-form :model="row" label-width="120px" class="demo-ruleForm" :rules="rules" ref="row">
				<el-tabs type="border-card" v-model="activeName">
					<el-tab-pane label="基本信息" name="first">
						<el-row>
							<el-col :span="12">
								<el-form-item label="追溯码:" prop="productBatchNumber">
									<el-select v-model="row.productBatchNumber" clearable filterable placeholder="请选择追溯码">
										<el-option
											v-for="item in productBatchOptions"
											:key="item"
											:label="item"
											:value="item">
										</el-option>
									</el-select>
								</el-form-item>
							</el-col>
							<el-col :span="12">
								<el-form-item label="检验时间:" prop="testDate">
									<el-date-picker
										value-format="yyyy-MM-dd"
										v-model="row.testDate"
										type="date" clearable
										placeholder="请选择检验时间">
									</el-date-picker>
								</el-form-item>
							</el-col>
						</el-row>

						<el-row>
							<el-col :span="12">
								<el-form-item label="检验数量:" prop="testAmount">
									<el-input v-model="row.testAmount"
										clearable
										onkeyup="value=value.replace(/[^\d^\.]/g,'')"
										placeholder="请输入检验数量"
										@blur="testAmountChange">
									</el-input>
								</el-form-item>
							</el-col>
							<el-col :span="12">
								<el-form-item label="检验数量单位:" prop="testAmountUnit">
									<el-select v-model="row.testAmountUnit" clearable placeholder="请选择检验数量单位">
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
								<el-form-item label="检验结果:" prop="testResult">
									<el-radio v-model="row.testResult" label="合格">合格</el-radio>
									<el-radio v-model="row.testResult" label="未合格">未合格</el-radio>
								</el-form-item>
							</el-col>
						</el-row>
					</el-tab-pane>
					<el-tab-pane label="检验结果" name="second">
						<!-- 上传 -->
						<el-upload
              :action="updateUrl"
							multiple
              :http-request="httpRequest"
              list-type="picture-card"
              :on-preview="handleInspectionPicturePreview"
              :on-remove="handleInspectionPictureRemove"
              :before-upload="beforeAvatarUpload"
              :on-change="handleChangeInspectionPicturePhoto"
              :file-list="fileInspectionPictureLists">
              <i class="el-icon-plus"></i>
            </el-upload>
						<!-- 预览 -->
						<el-dialog :visible.sync="dialogInspectionPictureVisible" width="70%" top="5vh" :append-to-body="true">
							<img width="100%" :src="dialogInspectionPictureImageUrl" alt="">
						</el-dialog>
						<!-- 展示 -->
						<div class="upload-div">
							<div class="upload-div-list" v-for="(item,index) of imgInspectionPictureLists" :key="index">
								<div class="upload-div-top">
									<span class="upload-div-del" @click="fileInspectionPictureDelIdPhoto(index,item.id)">X</span>
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
				productBatchNumber: null,
				testDateS: null,
				testResult: null,
				enterpriseId: window.sessionStorage.getItem('enterpriseId'),
				baseType: '养殖基地'
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
			// 检验结果
			updateUrl: '#',
			baseUrl: this.$baseImgUrl,
			// 预览图片src列表
			srcList: [],
			picInspectionPictureListPhoto: [],
			dialogInspectionPictureImageUrl: '',
			dialogInspectionPictureVisible: false,
			imgInspectionPictureLists: [],
			// 上传文件
			fileInspectionPictureLists:[],
			// 多选框
			productBatchOptions: [],
			inspectionOptions: ['合格', '未合格'],
			amountUnitOptions: [],
			// 验证规则
			rules: {
				productBatchNumber: [{ required: true, message: '请选择追溯码', trigger: 'blur' }],
				testDate: [{ required: true, message: '请选择检验时间', trigger: 'blur' }],
				testAmount: [{ required: true, message: '请输入检验数量', trigger: 'blur' }],
				testAmountUnit: [{ required: true, message: '请选择检验数量单位', trigger: 'blur' }],
				testResult: [{ required: true, message: '请选择检验结果', trigger: 'blur' }]
			}
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
			const { data: res } = await this.$http.get('zsSys/ProcessBatch/pageByEId', {
				params: {
					enterpriseId: window.sessionStorage.getItem('enterpriseId'),
					baseType: '养殖基地'
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
				this.dialogInspectionPictureImageUrl = this.result; // this指向当前方法onloadend的作用域
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
		// 检验结果图片预览对话框
		handleInspectionPicturePreview(file) {
			this.dialogInspectionPictureImageUrl = file.url;
			this.dialogInspectionPictureVisible = true;
		},
		// 删除要上传列表中的图片
		handleInspectionPictureRemove(file, fileLists) {
			for (let i in this.picInspectionPictureListPhoto) {
				if (this.picInspectionPictureListPhoto[i].key === file.uid) {
					this.picInspectionPictureListPhoto.splice(i, 1)
				}
			}
			this.fileInspectionPictureLists = fileLists;
		},
		// 上传图片列表文件
		handleChangeInspectionPicturePhoto(file, fileList) {
			this.fileInspectionPictureLists = fileList;
		},
		// 删除已上传的图片
		fileInspectionPictureDelIdPhoto(index,id){
			this.$confirm('此操作将永久删除该图片, 是否继续?', '提示', {
				confirmButtonText: '确定',
				cancelButtonText: '取消',
				type: 'warning'
			}).then(() => {
				this.row.delFileIds.push(id);
        this.imgInspectionPictureLists.splice(index,1);
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
			this.picInspectionPictureListPhoto = [];
			this.imgInspectionPictureLists = [];
			this.fileInspectionPictureLists = [];
			this.activeName = 'first';
			this.getAllList();
			this.$refs.row.clearValidate();
		},
		// 编辑数据
		editRow(row) {
			this.row = row;
			this.row.delFileIds = [];
			if(row.testResultPictureList && row.testResultPictureList.length !== 0) {
        this.imgInspectionPictureLists = row.testResultPictureList;
        for(let i=0; i<this.imgInspectionPictureLists.length; i++) {
          this.srcList.push(this.baseUrl + this.imgInspectionPictureLists[i].url);
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
				const { data: res } = await this.$http.post('zsSys/FinishProductTest/batchDelete', delIds);
				if(res.status !== 200) {
					return this.$message({
						duration: 2000,
						type: 'error',
						message: '删除检验失败！'
					});
				}
				this.$message({
					duration: 2000,
					type: 'success',
					message: '删除检验成功！'
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
			this.$refs.row.validate(async valid => {
				if(!valid) return this.$message.error('信息填写不完整或不准确，请检查再提交！');
				let fd = new FormData();
				for(let key in this.row) {
					let value = this.row[key];
					// 过滤
					if(key === 'testResultPictureList') {
						// fd.append(key, '');
          } else {
            if (value !== null) {
              fd.append(key, value);
            }
          }
				}

				//图片文件
        if (this.fileInspectionPictureLists != null) {
					for (let i = 0; i < this.fileInspectionPictureLists.length; i++) {
            fd.append('testResultPictureFiles', this.fileInspectionPictureLists[i].raw);
          }
        }

				if(this.flag === 'add') {
					const { data: res } = await this.$http.post('zsSys/FinishProductTest/addDataAndFile', fd);
					if(res.status !== 200) {
						return this.$message({
							duration: 2000,
							type: 'error',
							message: '新增检验失败！'
						});
					}
					this.getAllList();
					this.dialogShow = false;
					this.$message({
						duration: 2000,
						type: 'success',
						message: '新增检验成功！'
					});
				} else if(this.flag === 'edit') {
					const { data: res } = await this.$http.put('zsSys/FinishProductTest/updateDataAndFile', fd);
					if(res.status !== 200) {
						return this.$message({
							duration: 2000,
							type: 'error',
							message: '修改检验失败！'
						});
					}
					this.getAllList();
					this.dialogShow = false;
					this.$message({
						duration: 2000,
						type: 'success',
						message: '修改检验成功！'
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
		// 解决只能输入数字输入框的值绑定问题
		testAmountChange(e) {
			this.$set(this.row, 'testAmount	', e.target.value);
		},
		// 根据code转换成name
		getDictionaryName(code) {
			for(let i=0; i<this.amountUnitOptions.length; i++) {
				if(code === this.amountUnitOptions[i].code) {
					return this.amountUnitOptions[i].name;
				}
			}
		}
	}
}
</script>

<style scoped lang="less">
</style>