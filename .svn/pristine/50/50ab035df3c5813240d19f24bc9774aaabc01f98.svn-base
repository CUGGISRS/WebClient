<template>
    <div>
        <!-- 面包屑导航区域 -->
        <el-breadcrumb separator-class="el-icon-arrow-right">
            <el-breadcrumb-item :to="{ path: '/welcome' }">首页</el-breadcrumb-item>
            <el-breadcrumb-item>用户管理</el-breadcrumb-item>
            <el-breadcrumb-item>用户管理</el-breadcrumb-item>
        </el-breadcrumb>
        <div id="toolDiv">
            <el-input v-model="params.name" clearable placeholder="请输入名称"></el-input>
            <el-button @click="getData()" type="primary" size="small" circle icon="el-icon-search" ></el-button>
            <el-button @click="addData()" type="primary" size="small" circle icon="el-icon-plus"></el-button>
        </div>
        <el-table stripe  style="width:95%"
                  :height="tableHeight"
                  :data="rows">
            <el-table-column prop="name" label="名称"/>
            <el-table-column prop="username" label="用户名"/>
            <el-table-column prop="type" label="类型">
                <template slot-scope="scope">
                    <el-tag v-if="scope.row.type==1" type="primary"    >管理员</el-tag>
                    <el-tag v-if="scope.row.type==2" type="info"    >普通用户</el-tag>
                </template>
            </el-table-column>

            <el-table-column prop="phone" label="联系电话"/>
            <el-table-column prop="contactPerson" label="联系人"/>
            <el-table-column prop="mailbox" label="邮箱"/>

            <el-table-column prop="unitName" label="单位名称"/>
            <el-table-column prop="unitAddress" label="单位地址"/>
            <el-table-column prop="ip" label="ip地址"/>
            <el-table-column  prop="createTime" label="创建时间" width="150"/>


            <el-table-column prop="status" label="状态">
                <template slot-scope="scope">
                    <el-tag v-if="scope.row.status==0" type="danger"  >禁用</el-tag>
                    <el-tag v-if="scope.row.status==1" type="success"   >启用</el-tag>
                </template>
            </el-table-column>
            <el-table-column
                    fixed="right"
                    label="操作"
                    width="120">
                <template slot-scope="scope">
                    <el-button @click="editData(scope.row)" icon="el-icon-edit" type="primary" size="small"
                               circle></el-button>
                    <el-button @click="delData(scope.row.id)" icon="el-icon-delete" type="primary" size="small"
                               circle></el-button>
                </template>
            </el-table-column>
        </el-table>
        <!-- 分页 -->
        <el-pagination
                background
                @size-change="handleSizeChange"
                @current-change="handleCurrentChange"
                :current-page="params.page"
                :page-sizes="[10,15, 20, 25, 30]"
                :page-size="params.limit"
                layout="total, sizes, prev, pager, next, jumper"
                :total="total">
        </el-pagination>

        <el-dialog
                title="用户管理详情"
                :visible.sync="dialogVisible"
                width="40%">
            <el-form label-width="100px" :rules="rules"
                     ref="row" :model="row">
                <el-form-item label="名称" prop="name">
                    <el-input v-model="row.name"></el-input>
                </el-form-item>
                <el-form-item label="用户名" prop="username">
                    <el-input v-model="row.username"></el-input>
                </el-form-item>

                <el-form-item label="密码" prop="password" v-if="optType!='edit'">
                    <el-input v-model="row.password"></el-input>
                </el-form-item>
                <el-form-item label="类型" prop="type">
                    <el-select v-model="row.type"  placeholder="">
                        <el-option
                                v-for="item in typeOptions"
                                :key="item.value"
                                :label="item.name"
                                :value="item.value">
                        </el-option>
                    </el-select>
                </el-form-item>

                <el-form-item label="联系电话" prop="phone">
                    <el-input v-model="row.phone"></el-input>
                </el-form-item>
                <el-form-item label="联系人" prop="contactPerson">
                    <el-input v-model="row.contactPerson"></el-input>
                </el-form-item>
                <el-form-item label="邮箱" prop="mailbox">
                    <el-input v-model="row.mailbox"></el-input>
                </el-form-item>

                <el-form-item label="单位名称" prop="unitName">
                    <el-input v-model="row.unitName"></el-input>
                </el-form-item>
                <el-form-item label="单位地址" prop="unitAddress">
                    <el-input v-model="row.unitAddress"></el-input>
                </el-form-item>
                <el-form-item label="ip地址" prop="ip">
                    <el-input v-model="row.ip"></el-input>
                </el-form-item>

                <el-form-item label="状态" prop="status" v-if="optType=='edit'">
                    <el-select  v-model=row.status placeholder="">
                        <el-option
                                v-for="item in statusOptions"
                                :key="item.value"
                                :label="item.name"
                                :value="item.value">
                        </el-option>
                    </el-select>
                </el-form-item>

                <div style="text-align: center">
                    <el-button type="primary" @click="submit">提交</el-button>
                    <el-button type="primary" @click="closed">关闭</el-button>
                </div>
            </el-form>
        </el-dialog>
    </div>
</template>

<script>
    import DataList from "@/globals/service/dataList.js";
    import API from "@/globals/request/api.js";

    export default {
        data() {
            var validateIp = (rule, value, callback) => {

                if (value != null &&value!="" && !API.isValidIP(value)) {
                    callback(new Error('ip地址格式不正确!'));
                } else {
                    callback();
                }
            };
            return {
                tableHeight: 0,//表格高度
                // 分页
                total: 0,
                params: {
                    page: 1,
                    limit: 15
                },
                rows: [],
                row: {},
                dialogVisible: false,
                optType: "",
                // 验证规则
                rules: {
                    username: [{required: true, message: '请输入用户名', trigger: 'blur'}],
                    password: [{required: true, message: '请输入密码', trigger: 'blur'}],
                    type: [{required: true, message: '请选择类型', trigger: 'blur'}],
                    ip: [{validator: validateIp, trigger: 'blur'}]
                },

                typeOptions:[{name:"管理员",value:1},{name:"普通用户",value:2}],
                statusOptions:[{name:"禁用",value:0},{name:"启用",value:1}]
            }
        },
        created() {
            this.getData();

            this.setTableHeight();
            window.onresize = () => {
                this.setTableHeight();
            }
        },
        methods: {
            // 设定表格高度
            setTableHeight() {
                let h = document.documentElement.clientHeight || document.body.clientHeight;
                this.tableHeight = h - 198;
            },
            // 每页多少条
            handleSizeChange(val) {
                this.params.limit = val;
                this.getData();
            },
            // 当前页
            handleCurrentChange(val) {
                this.params.page = val;
                this.getData();
            },
            //获得本地代理数据
            getData: function () {
                DataList.getUser(this.params).then(res => {
                    if (res.status == 200) {
                        this.rows = res.data.rows;
                        this.total = res.data.total;
                    }else {
                        this.$message.error(res.message);
                    }
                });
            },
            delData: function (val) {
                this.$confirm(`确定删除?`, "提示", {
                    confirmButtonText: "确定",
                    cancelButtonText: "取消",
                    type: "warning"
                })
                    .then(() => {

                        DataList.delUser(val).then(res => {
                            if (res.status == 200) {
                                this.$message({
                                    duration: 1000,
                                    type: 'success',
                                    message: '删除成功！'
                                });
                                this.getData();
                            }else {
                                this.$message.error(res.message);
                            }
                        });

                    })
                    .catch(() => {
                        this.$message({type: "info", message: "已取消删除"});
                    });

            },
            addData: function () {
                this.dialogVisible = true;
                this.optType = "add";
                this.row = {};
            },
            editData: function (val) {
                this.dialogVisible = true;
                this.optType = "edit";
                this.row = val;

            },
            submit: function () {
                this.$refs.row.validate(valid => {
                    if (valid) {
                        if (this.optType == "add") {
                            DataList.addUser(this.row).then(res => {
                                if (res.status == 200) {
                                    this.$message({
                                        duration: 1000,
                                        type: 'success',
                                        message: '添加成功！'
                                    });
                                    this.getData();
                                    this.dialogVisible = false;
                                }else {
                                    this.$message.error(res.message);
                                }
                            });
                        } else if (this.optType == "edit") {
                            DataList.editUser(this.row).then(res => {
                                if (res.status == 200) {
                                    this.$message({
                                        duration: 1000,
                                        type: 'success',
                                        message: '修改成功！'
                                    });
                                    this.getData();
                                    this.dialogVisible = false;
                                }else {
                                    this.$message.error(res.message);
                                }
                            });
                        }
                    } else {
                        this.$message.error('信息填写不完整或不准确，请检查再提交！');
                        return false;
                    }
                });
            },
            closed: function () {
                this.dialogVisible = false;
            }
        }
    };
</script>


<style >
    #toolDiv .el-input{
        width: 300px;
    }
    .el-select{
        width: 100%;
    }

</style>
