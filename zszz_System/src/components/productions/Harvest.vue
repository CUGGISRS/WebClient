<template>
  <div>
    <!-- 面包屑导航区域 -->
    <el-breadcrumb separator-class="el-icon-arrow-right">
      <el-breadcrumb-item>首页</el-breadcrumb-item>
      <el-breadcrumb-item>生产管理</el-breadcrumb-item>
      <el-breadcrumb-item>采收管理</el-breadcrumb-item>
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
		<!-- 新增编辑对话框 -->
		<el-dialog v-cloak title="采收信息" @close="closeDialog" center :visible.sync="dialogShow" width="1000px" class="row-form">
			<el-form :model="row" label-width="120px" class="demo-ruleForm" :rules="rules" ref="row">
				<el-tabs type="border-card" v-model="activeName">
					<el-tab-pane label="基本信息" name="first">
						<el-row>
							<el-col :span="12">
								<el-form-item label="产品名称:" prop="productId">
									<el-select v-model="row.productId" filterable clearable placeholder="请选择产品">
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
								<el-form-item label="收获方式:" prop="harvestWay">
									<el-select v-model="row.harvestWay" clearable placeholder="请选择收获方式">
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
								<el-form-item label="收获数量:" prop="harvestAmount">
									<el-input v-model="row.harvestAmount"
										clearable
										onkeyup="value=value.replace(/[^\d^\.]/g,'')"
										placeholder="请输入收获数量"
										@blur="harvestAmountChange">
									</el-input>
								</el-form-item>
							</el-col>
							<el-col :span="12">
								<el-form-item label="收获数量单位:" prop="harvestAmountUnit">
									<el-select v-model="row.harvestAmountUnit" clearable placeholder="请选择收获数量单位">
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
								<el-form-item label="收获时间:" prop="harvestDate">
                  <el-date-picker
										value-format="yyyy-MM-dd"
										v-model="row.harvestDate"
										type="date" clearable
										placeholder="请选择收获时间">
									</el-date-picker>
								</el-form-item>
							</el-col>
							<el-col :span="12">
								<el-form-item label="天气状况:" prop="weather">
									<el-input v-model="row.weather" clearable maxlength="50" placeholder="请输入天气状况"></el-input>
								</el-form-item>
							</el-col>
						</el-row>

						<el-row>
							<el-col :span="12">
								<el-form-item label="收购码:">
									<el-input v-model="row.traceCode" readonly placeholder="自动生成收购码"></el-input>
								</el-form-item>
							</el-col>
						</el-row>
					</el-tab-pane>
					<el-tab-pane label="收获图片" name="second">
						<!-- 上传 -->
						<el-upload
              :action="updateUrl"
							multiple
              :http-request="httpRequest"
              list-type="picture-card"
              :on-preview="handleHarvestPicturePreview"
              :on-remove="handleHarvestPictureRemove"
              :before-upload="beforeAvatarUpload"
              :on-change="handleChangeHarvestPicturePhoto"
              :file-list="fileHarvestPictureLists">
              <i class="el-icon-plus"></i>
            </el-upload>
						<!-- 预览 -->
						<el-dialog :visible.sync="dialogHarvestPictureVisible" width="70%" top="5vh" :append-to-body="true">
							<img width="100%" :src="dialogHarvestPictureImageUrl" alt="">
						</el-dialog>
						<!-- 展示 -->
						<div class="upload-div">
							<div class="upload-div-list" v-for="(item,index) of imgHarvestPictureLists" :key="index">
								<div class="upload-div-top">
									<span class="upload-div-del" @click="fileHarvestPictureDelIdPhoto(index,item.id)">X</span>
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
				productId: null,
				harvestDateS: null,
				enterpriseId: window.sessionStorage.getItem('enterpriseId'),
				baseType: '种植基地'
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
			// 收获图片
			updateUrl: '#',
			baseUrl: this.$baseImgUrl,
			// 预览图片src列表
			srcList: [],
			picHarvestPictureListPhoto: [],
			dialogHarvestPictureImageUrl: '',
			dialogHarvestPictureVisible: false,
			imgHarvestPictureLists: [],
			// 上传文件
			fileHarvestPictureLists:[],
			// 多选框
			amountUnitOptions: [],
			harvestWayOptions: ['机器收割', '人工收割'],
			productOptions: [],
			// 验证规则
			rules: {
				productId: [{ required: true, message: '请选择产品', trigger: 'blur' }],
				harvestWay: [{ required: true, message: '请选择收获方式', trigger: 'blur' }],
				harvestAmount: [{ required: true, message: '请输入收获数量', trigger: 'blur' }],
				harvestAmountUnit: [{ required: true, message: '请选择收获数量单位', trigger: 'blur' }],
				harvestDate: [{ required: true, message: '请选择收获时间', trigger: 'blur' }],
				// weather: [{ required: true, message: '请输入天气状况', trigger: 'blur' }]
			}
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
					baseType: '种植基地'
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
					message: '获取采收数据失败！'
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
				this.dialogHarvestPictureImageUrl = this.result; // this指向当前方法onloadend的作用域
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
		// 收获图片预览对话框
		handleHarvestPicturePreview(file) {
			this.dialogHarvestPictureImageUrl = file.url;
			this.dialogHarvestPictureVisible = true;
		},
		// 删除要上传列表中的图片
		handleHarvestPictureRemove(file, fileLists) {
			for (let i in this.picHarvestPictureListPhoto) {
				if (this.picHarvestPictureListPhoto[i].key === file.uid) {
					this.picHarvestPictureListPhoto.splice(i, 1)
				}
			}
			this.fileHarvestPictureLists = fileLists;
		},
		// 上传图片列表文件
		handleChangeHarvestPicturePhoto(file, fileList) {
			this.fileHarvestPictureLists = fileList;
		},
		// 删除已上传的图片
		fileHarvestPictureDelIdPhoto(index,id){
			this.$confirm('此操作将永久删除该图片, 是否继续?', '提示', {
				confirmButtonText: '确定',
				cancelButtonText: '取消',
				type: 'warning'
			}).then(() => {
				this.row.delFileIds.push(id);
        this.imgHarvestPictureLists.splice(index,1);
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
			this.picHarvestPictureListPhoto = [];
			this.imgHarvestPictureLists = [];
			this.fileHarvestPictureLists = [];
			this.activeName = 'first';
			this.getAllList();
			this.$refs.row.clearValidate();
		},
		// 编辑数据
		editRow(row) {
			this.row = row;
			this.row.delFileIds = [];
			if(row.harvestPictureList && row.harvestPictureList.length !== 0) {
        this.imgHarvestPictureLists = row.harvestPictureList;
        for(let i=0; i<this.imgHarvestPictureLists.length; i++) {
          this.srcList.push(this.baseUrl + this.imgHarvestPictureLists[i].url);
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
				const { data: res } = await this.$http.post('zsSys/Harvest/batchDelete', delIds);
				if(res.status !== 200) {
					return this.$message({
						duration: 2000,
						type: 'error',
						message: '删除采收失败！'
					});
				}
				this.$message({
					duration: 2000,
					type: 'success',
					message: '删除采收成功！'
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
					if(key === 'harvestPictureList') {
						// fd.append(key, '');
          } else {
            if (value !== null) {
              fd.append(key, value);
            }
          }
				}

				//图片文件
        if (this.fileHarvestPictureLists != null) {
					for (let i = 0; i < this.fileHarvestPictureLists.length; i++) {
            fd.append('harvestPictureFiles	', this.fileHarvestPictureLists[i].raw);
          }
        }

				if(this.flag === 'add') {
					const { data: res } = await this.$http.post('zsSys/Harvest/addDataAndFile', fd);
					if(res.status !== 200) {
						return this.$message({
							duration: 2000,
							type: 'error',
							message: '新增采收失败！'
						});
					}
					this.getAllList();
					this.dialogShow = false;
					this.$message({
						duration: 2000,
						type: 'success',
						message: '新增采收成功！'
					});
				} else if(this.flag === 'edit') {
					const { data: res } = await this.$http.put('zsSys/Harvest/updateDataAndFile', fd);
					if(res.status !== 200) {
						return this.$message({
							duration: 2000,
							type: 'error',
							message: '修改采收失败！'
						});
					}
					this.getAllList();
					this.dialogShow = false;
					this.$message({
						duration: 2000,
						type: 'success',
						message: '修改采收成功！'
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
		harvestAmountChange(e) {
			this.$set(this.row, 'harvestAmount', e.target.value);
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
		}
	}
}
</script>

<style scoped lang="less">
</style>