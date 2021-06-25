<template>
  <div>
    <!-- 面包屑导航区域 -->
    <el-breadcrumb separator-class="el-icon-arrow-right">
      <el-breadcrumb-item>首页</el-breadcrumb-item>
      <el-breadcrumb-item>基本配置</el-breadcrumb-item>
      <el-breadcrumb-item>销售商管理</el-breadcrumb-item>
    </el-breadcrumb>
    <!-- 卡片视图区域 -->
    <el-card>
			<!-- 查询区域 -->
			<div class="query-condition">
				<div class="query-item">
					<span>销售商名称:</span>
					<el-input v-model="params.name" clearable style="width: 200px;" placeholder="请输入销售商名称"></el-input>
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
				<el-table-column align="center" label="销售商名称" width="240">
          <template slot-scope="scope">
            {{ scope.row.name }}
          </template>
        </el-table-column>
				<el-table-column align="center" label="联系人" width="160">
          <template slot-scope="scope">
            {{ scope.row.contactPerson }}
          </template>
        </el-table-column>
				<el-table-column align="center" label="联系电话" width="120">
          <template slot-scope="scope">
            {{ scope.row.phone }}
          </template>
        </el-table-column>
				<el-table-column align="center" label="销售许可证号" width="240">
          <template slot-scope="scope">
            {{ scope.row.saleLicenseNumber }}
          </template>
        </el-table-column>
				<el-table-column align="center" label="销售商地址" min-width="100">
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
		<el-dialog v-cloak title="销售商信息" @close="closeDialog" center :visible.sync="dialogShow" width="1000px" class="row-form">
			<el-form :model="row" label-width="140px" class="demo-ruleForm" :rules="rules" ref="row">
				<el-row>
					<el-col :span="12">
						<el-form-item label="销售商名称:" prop="name">
							<el-input v-model="row.name" clearable maxlength="50" placeholder="请输入销售商名称"></el-input>
						</el-form-item>
					</el-col>
					<el-col :span="12">
						<el-form-item label="销售许可证号:" prop="saleLicenseNumber">
							<el-input v-model="row.saleLicenseNumber" clearable maxlength="50" placeholder="请输入销售许可证号"></el-input>
						</el-form-item>
					</el-col>
				</el-row>

				<el-row>
					<el-col :span="12">
						<el-form-item label="联系人:" prop="contactPerson">
              <el-input v-model="row.contactPerson" clearable maxlength="50" placeholder="请输入联系人姓名"></el-input>
						</el-form-item>
					</el-col>
					<el-col :span="12">
						<el-form-item label="联系电话:" prop="phone">
							<el-input v-model="row.phone"
								clearable maxlength="50"
								onkeyup="value=value.replace(/[^\d]/g,'')" 
								placeholder="请输入联系人电话"
								@blur="phoneChange">
							</el-input>
						</el-form-item>
					</el-col>
				</el-row>

				<el-row>
					<el-col :span="12">
						<el-form-item label="邮箱:" prop="mailbox">
							<el-input v-model="row.mailbox" clearable maxlength="50" placeholder="请输入邮箱"></el-input>
						</el-form-item>
					</el-col>
					<el-col :span="12">
						<el-form-item label="销售商地址:" prop="address">
							<el-input v-model="row.address" clearable maxlength="255" placeholder="请输入销售商地址"></el-input>
						</el-form-item>
					</el-col>
				</el-row>

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
			// 选中删除项
			selectRows: [],
			// 验证规则
			rules: {
				name: [{ required: true, message: '请输入销售商名称', trigger: 'blur' }],
				saleLicenseNumber: [{ required: true, message: '请输入销售许可证号', trigger: 'blur' }],
				// organizationCode: [{ required: true, message: '请输入组织机构代码证号', trigger: 'blur' }],
				// address: [{ required: true, message: '请输入销售商地址', trigger: 'blur' }],
				contactPerson: [{ required: true, message: '请输入联系人', trigger: 'blur' }],
				phone: [
					{ required: true, message: '请输入联系电话', trigger: 'blur' },
					{ required: true, validator: validatePhone, trigger: 'blur' }
				],
				// mailbox: [{ required: true, message: '请输入邮箱', trigger: 'blur' }],
				// isOpen: [{ required: true, message: '请选择是否向公众公开', trigger: 'blur' }]
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
			const { data: res } = await this.$http.get('zsSys/Seller/pageByEId', { params: this.params });
			if (res.status !== 200) {
				return this.$message({
					duration: 1000,
					type: 'error',
					message: '获取销售商数据失败！'
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
		// 关闭对话框
		closeDialog() {
			this.row = {};
			this.getAllList();
			this.$refs.row.clearValidate();
		},
		// 编辑数据
		editRow(row) {
			this.row = row;
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
				const { data: res } = await this.$http.post('zsSys/Seller/batchDelete', delIds);
				if(res.status !== 200) {
					return this.$message({
						duration: 2000,
						type: 'error',
						message: '删除销售商失败！'
					});
				}
				this.$message({
					duration: 2000,
					type: 'success',
					message: '删除销售商成功！'
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
				if(this.flag === 'add') {
					const { data: res } = await this.$http.post('zsSys/Seller', this.row);
					if(res.status !== 200) {
						return this.$message({
							duration: 2000,
							type: 'error',
							message: '新增销售商失败！'
						});
					}
					this.getAllList();
					this.dialogShow = false;
					this.$message({
						duration: 2000,
						type: 'success',
						message: '新增销售商成功！'
					});
				} else if(this.flag === 'edit') {
					const { data: res } = await this.$http.put('zsSys/Seller', this.row);
					if(res.status !== 200) {
						return this.$message({
							duration: 2000,
							type: 'error',
							message: '修改销售商失败！'
						});
					}
					this.getAllList();
					this.dialogShow = false;
					this.$message({
						duration: 2000,
						type: 'success',
						message: '修改销售商成功！'
					});
				}
			});
		}, 3000),
		// 解决只能输入数字输入框的值绑定问题
		phoneChange(e) {
			this.$set(this.row, 'phone', e.target.value);
		}
	}
}
</script>

<style scoped lang="less">
</style>