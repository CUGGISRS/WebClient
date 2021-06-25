<template>
  <div>
    <!-- 面包屑导航区域 -->
    <el-breadcrumb separator-class="el-icon-arrow-right">
      <el-breadcrumb-item>溯源信息监督</el-breadcrumb-item>
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
				<el-table-column align="center" label="操作" width="160">
          <template slot-scope="scope">
						<el-button size="mini" type="warning" icon="el-icon-view" @click="showSensorData(scope.row)">查看数据</el-button>
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
		<!-- 传感器数据对话框 -->
		<el-dialog v-cloak title="传感器数据" center :visible.sync="dialogSensorShow" width="1000px" class="row-form" top="10vh">
			<el-table :data="sensorRows"
				border stripe fit highlight-current-row 
				:row-style="{height:'5px'}"
				:cell-style="{padding:'5px 0'}"
				:height="curHeight"
				style="margin-top: 0">
				<el-table-column align="center" label="序号" width="60">
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
export default {
  data() {
		return {
			// 参数条件
			params: {
				page: null,
				limit: null,
				productName: null,
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
			// 分页
			currentPage: 1,
			total: 0,
			pageSize: 20,
			// 传感器数据
			sensorRows: [],
			sensorRow: {},
			// 传感器名称
			name: '',
			// 分页
			sensorCurrentPage: 1,
			sensorTotal: 0,
			sensorPageSize: 20,
			// 表单高度
			curHeight: 0,
			// 传感器数据对话框
			dialogSensorShow: false,
			// 多选框
			productOptions: [],
			// 分组列表
			groupLists: [],
			// 传感器列表
			sensorLists: [],
		}
	},
	created() {
		this.getProductDictionary();
		this.getAllList();
		this.setTableHeight();
		window.onresize = () => {
			this.setTableHeight();
		}
	},
	methods: {
		// 获取数据字典
		async getProductDictionary() {
			const { data: res } = await this.$http.get('zsSys/Product/pageByEId', {
				params: {
					enterpriseId: window.sessionStorage.getItem('enterpriseId'),
					baseType: this.params.baseType
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
      this.curHeight = h - 425;
		},
		// 查询数据
		searchRow() {
			this.currentPage = 1;
			this.getAllList();
		},
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
		// 数据切换
		changeEnterpriseType() {
			this.getProductDictionary();
			this.getAllList();
		}
	}
}
</script>