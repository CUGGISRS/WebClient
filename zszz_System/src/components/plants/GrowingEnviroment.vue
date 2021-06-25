<template>
  <div>
    <!-- 面包屑导航区域 -->
    <el-breadcrumb separator-class="el-icon-arrow-right">
      <el-breadcrumb-item>首页</el-breadcrumb-item>
      <el-breadcrumb-item>种植管理</el-breadcrumb-item>
      <el-breadcrumb-item>生长环境管理</el-breadcrumb-item>
    </el-breadcrumb>
    <!-- 卡片视图区域 -->
    <el-card>
      <!-- 查询区域 -->
			<div class="query-condition">
				<div class="query-item">
					<span>产品名称:</span>
          <el-select v-model="params.productId" filterable clearable style="width: 240px;" placeholder="请选择产品">
						<el-option
							v-for="item in productOptions"
							:key="item.id"
							:label="item.name"
							:value="item.id">
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
				<el-table-column align="center" label="操作" width="240">
          <template slot-scope="scope">
            <el-button size="mini" plain type="primary" icon="el-icon-edit-outline" @click="editRow(scope.row)">编辑</el-button>
            <el-button size="mini" plain type="warning" icon="el-icon-view" @click="showSensorData(scope.row)">查看数据</el-button>
          </template>
        </el-table-column>
				<el-table-column align="center" label="产品名称">
          <template slot-scope="scope">
						{{ getName(scope.row.productId) }}
          </template>
        </el-table-column>
				<el-table-column align="center" label="传感器名称">
          <template slot-scope="scope">
						{{ scope.row.deviceSensor.name }}
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
		<el-dialog v-cloak title="生长环境信息" @close="closeDialog" center :visible.sync="dialogShow" width="800px" class="row-form">
			<el-form :model="row" label-width="120px" class="demo-ruleForm" :rules="rules" ref="row">
				<el-row>
					<el-col :span="24">
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
				</el-row>

				<el-row>
					<el-col :span="24">
						<el-form-item label="分组名称:" prop="groupId">
							<el-select v-model="row.groupId" filterable clearable placeholder="请选择分组" @change="changeGroup">
                <el-option
                  v-for="item in groupLists"
                  :key="item.id"
                  :label="item.name"
                  :value="item.id">
                </el-option>
              </el-select>
						</el-form-item>
					</el-col>
				</el-row>

				<el-row>
					<el-col :span="24">
						<el-form-item label="传感器名称:" prop="deviceSensorId">
							<el-select v-model="row.deviceSensorId" filterable clearable placeholder="请选择传感器" @change="updateData">
                <el-option
                  v-for="item in sensorLists"
                  :key="item.id"
                  :label="item.name"
                  :value="item.id">
                </el-option>
              </el-select>
						</el-form-item>
					</el-col>
				</el-row>

				<span class="t-btn">
					<el-button type="info" @click="dialogShow = false">取 消</el-button>
					<el-button type="success" @click="submitRow">提 交</el-button>
				</span>
			</el-form>
		</el-dialog>

		<!-- 传感器数据对话框 -->
		<el-dialog v-cloak title="传感器数据" center :visible.sync="dialogSensorShow" width="1000px" class="row-form" top="10vh">
			<el-table :data="sensorRows"
				border stripe fit highlight-current-row 
				:row-style="{height:'5px'}"
				:cell-style="{padding:'5px 0'}"
				:height="curHeight"
				style="margin-top: 0">
				<el-table-column align="center" label="序号" width="100">
          <template slot-scope="scope">
            {{ (sensorCurrentPage - 1) * sensorPageSize + scope.$index + 1 }}
          </template>
        </el-table-column>
				<el-table-column align="center" label="日期">
          <template slot-scope="scope">
						{{ scope.row.recordTime }}
          </template>
        </el-table-column>
				<el-table-column align="center" :label="name">
          <template slot-scope="scope">
						{{ scope.row.recordData }}
          </template>
        </el-table-column>
			</el-table>
      <!-- 分页 -->
			<el-pagination
				background
				@size-change="handleSizeChange2"
				@current-change="handleCurrentChange2"
				:current-page="sensorCurrentPage"
				:page-sizes="[10, 20, 30, 40]"
				:page-size="sensorPageSize"
				layout="total, sizes, prev, pager, next, jumper"
				:total="sensorTotal">
			</el-pagination>
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
				deviceName: null,
				name: null,
				enterpriseId: window.sessionStorage.getItem('enterpriseId'),
				baseType: '种植基地'
			},
			// 传感器数据查询参数
			params2: {
				recordTimeS: null,
				recordTimeE: null,
				interval: 1,
				sensorId: null
			},
			// 表单
			rows: [],
			row: {},
			// 修改or新增flag
			flag: '',
			// 分页
			currentPage: 1,
			pageSize: 20,
			total: 0,
			// 传感器数据
			sensorRows: [],
			sensorRow: {},
			// 传感器名称
			name: '',
			// 分页
			sensorCurrentPage: 1,
			sensorPageSize: 20,
			sensorTotal: 0,
			// 表单高度
			curHeight: 0,
			// 对话框
			dialogShow: false,
			// 传感器数据对话框
			dialogSensorShow: false,
			// 选中删除项
			selectRows: [],
			// 多选框
			productOptions: [],
			// 分组列表
			groupLists: [],
			// 传感器列表
			sensorLists: [],
			// 验证规则
			rules: {
        productId: [{ required: true, message: '请选择产品', trigger: 'blur' }],
				groupId: [{ required: true, message: '请选择分组', trigger: 'blur' }],
				deviceSensorId: [{ required: true, message: '请选择传感器', trigger: 'blur' }]
      }
		}
	},
	created() {
		this.getProductDictionary();
		this.getAllList();
		this.getAllGroup();
		this.setTableHeight();
		window.onresize = () => {
			this.setTableHeight();
		}
	},
	methods: {
		// 获取分组
		async getAllGroup() {
      const { data: res } = await this.$http.get('zsSys/DeviceGroup/pageByCId', {
        params: { creatorId: window.sessionStorage.getItem('enterpriseId') },
      });
      if (res.status !== 200) return this.$message.error('获取全部分组失败!');
			this.groupLists = res.data.rows;
		},
		// 切换分组
		changeGroup(value) {
			this.row.deviceSensorId = null;
			this.getSensorByGroupId(value);
		},
		// 根据分组id获取传感器信息
    async getSensorByGroupId(groupId) {
      const { data: res } = await this.$http.get('zsSys/DeviceSensor/pageByGId', {
        params: {
					groupId,
					type: 1
				},
      });
      if (res.status !== 200) return this.$message.error('获取传感器数据失败!');
			this.sensorLists = res.data.rows;
    },
		// 获取数据字典
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
			const { data: res } = await this.$http.get('zsSys/GrowEnvironment/pageByEId', { params: this.params });
			if (res.status !== 200) {
				return this.$message({
					duration: 1000,
					type: 'error',
					message: '获取生长环境数据失败！'
				});
			}
			this.rows = res.data.rows;
			this.total = res.data.total;
		},
		// 获取传感器数据
		async getSensroData(sensorId) {
			this.params2.page = this.sensorCurrentPage
			this.params2.limit = this.sensorPageSize;
			this.params2.sensorId = sensorId;
			const { data: res } = await this.$http.get('zsSys/DeviceSensorData/pageBySId', { params: this.params2 });
			this.sensorRows = res.data.rows;
			this.sensorTotal = res.data.total;
		},
		// 展示传感器数据
		showSensorData(row) {
			this.sensorRows = [];
			this.sensorRow = row;
			this.name = row.deviceSensor.unit ? `${row.deviceSensor.name}(${row.deviceSensor.unit})` : row.deviceSensor.name;
			this.getTime(row.productId);
			this.getSensroData(row.deviceSensorId);
			this.dialogSensorShow = true;
		},
		// 每页多少条
		handleSizeChange(val) {
			this.pageSize = val;
			this.getAllList();
		},
		handleSizeChange2(val) {
			this.sensorPageSize = val;
			this.getSensroData(this.sensorRow.deviceSensorId);
		},
		// 当前页
    handleCurrentChange(val) {
			this.currentPage = val;
			this.getAllList();
		},
    handleCurrentChange2(val) {
			this.sensorCurrentPage = val;
			this.getSensroData(this.sensorRow.deviceSensorId);
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
		// 关闭对话框
		closeDialog() {
			this.row = {};
			this.sensorLists = [];
			this.getAllList();
			this.$refs.row.clearValidate();
		},
		// 编辑数据
		editRow(row) {
			this.row = row;
			this.row.groupId = row.deviceSensor.groupId;
			this.getSensorByGroupId(this.row.groupId);
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
				const { data: res } = await this.$http.post('zsSys/GrowEnvironment/batchDelete', delIds);
				if(res.status !== 200) {
					return this.$message({
						duration: 2000,
						type: 'error',
						message: '删除生长环境失败！'
					});
				}
				this.$message({
					duration: 2000,
					type: 'success',
					message: '删除生长环境成功！'
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
				if(this.flag === 'add') {
					const { data: res } = await this.$http.post('zsSys/GrowEnvironment', this.row);
					if(res.status !== 200) {
						return this.$message({
							duration: 2000,
							type: 'error',
							message: '新增生长环境失败！'
						});
					}
					this.getAllList();
					this.dialogShow = false;
					this.$message({
						duration: 2000,
						type: 'success',
						message: '新增生长环境成功！'
					});
				} else if(this.flag === 'edit') {
					const { data: res } = await this.$http.put('zsSys/GrowEnvironment', this.row);
					if(res.status !== 200) {
						return this.$message({
							duration: 2000,
							type: 'error',
							message: '修改生长环境失败！'
						});
					}
					this.getAllList();
					this.dialogShow = false;
					this.$message({
						duration: 2000,
						type: 'success',
						message: '修改生长环境成功！'
					});
				}
			});
		}, 3000),
		// 根据id获取name
		getName(id) {
			for(let i=0; i<this.productOptions.length; i++) {
				if(id === this.productOptions[i].id) {
					return this.productOptions[i].name;
				}
			}
		},
		// 根据id获取time
		getTime(id) {
			for(let i=0; i<this.productOptions.length; i++) {
				if(id === this.productOptions[i].id) {
					this.params2.recordTimeS = this.productOptions[i].startDate;
					this.params2.recordTimeE = this.productOptions[i].endDate;
				}
			}
		},
		// 解决数据层次太多，render函数没有自动更新，需手动强制刷新
    updateData() {
      this.$forceUpdate();
    }
	}
}
</script>