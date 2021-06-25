<template>
    <div>
        <!-- 面包屑导航区域 -->
        <el-breadcrumb separator-class="el-icon-arrow-right">
            <el-breadcrumb-item :to="{ path: '/welcome' }">首页</el-breadcrumb-item>
            <el-breadcrumb-item>数据管理</el-breadcrumb-item>
            <el-breadcrumb-item>数据管理</el-breadcrumb-item>
        </el-breadcrumb>
        <div id="toolDiv">
            <el-input v-model="params.title" clearable placeholder="请输入标题"></el-input>
            <el-button @click="getLocalAgent()" type="primary" size="small" circle icon="el-icon-search" ></el-button>
            <el-button @click="addData()" type="primary" size="small" circle icon="el-icon-plus" ></el-button>

        </div>
        <el-table stripe style="width:95%"
                  :height="tableHeight"
                  :data="LocalAgents">
            <el-table-column prop="title" label="标题"/>
            <el-table-column prop="ip" label="ip地址"/>
            <el-table-column prop="port" label="端口"/>
            <el-table-column prop="path" label="本地路径"/>
            <el-table-column
                    fixed="right"
                    label="操作"
                    width="120">
                <template slot-scope="scope">
                    <el-button @click="editData(scope.row)" icon="el-icon-edit"  type="primary" size="small" circle></el-button>
                    <el-button @click="delData(scope.row.id)" icon="el-icon-delete" type="primary" size="small" circle></el-button>
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
                title="数据管理详情"
                :visible.sync="dialogVisible"
                width="40%">
            <el-form label-width="100px"  :rules="rules"
            ref="LocalAgent" :model="LocalAgent">
                <el-form-item label="标题" prop="title">
                    <el-input v-model="LocalAgent.title"></el-input>
                </el-form-item>
                <el-form-item label="ip地址" prop="ip">
                    <el-input v-model="LocalAgent.ip"></el-input>
                </el-form-item>
                <el-form-item label="端口" prop="port">
                    <el-input v-model="LocalAgent.port"></el-input>
                </el-form-item>
                <el-form-item label="本地路径" prop="path">
                    <el-input v-model="LocalAgent.path"></el-input>
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
            if (value==null||!API.isValidIP(value)) {
                callback(new Error('ip地址格式不正确!'));
            } else {
                callback();
            }
            };
            var validateNum = (rule, value, callback) => {
                if (value==null||!API.isValidNumber(value)) {
                    callback(new Error('端口不为数字格式!'));
                } else {
                    callback();
                }
            };
            return {
                tableHeight:0,//表格高度
                // 分页
                total: 0,
                params:{
                    page:1,
                    limit:15
                },
                LocalAgents: [],
                LocalAgent:{},
                title: "本地代理",
                dialogVisible:false,
                optType:"",
                // 验证规则
                rules: {
                    title: [{ required: true, message: '请输入标题', trigger: 'blur' }],
                    ip: [{ required: true, message: '请输入地址', trigger: 'blur' },
                        { validator: validateIp, trigger: 'blur' }],
                    port: [{ required: true,message: '请输入端口', trigger: 'blur' },
                        { validator: validateNum, trigger: 'blur' }],
                    path: [{ required: true, message: '请输入本地路径', trigger: 'blur' }]
                }
            }
        },
        created() {
            this.getLocalAgent();

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
                this.getLocalAgent();
            },
            // 当前页
            handleCurrentChange(val) {
                this.params.page = val;
                this.getLocalAgent();
            },
            //获得本地代理数据
            getLocalAgent: function () {
                DataList.getLocalAgent(this.params).then(res => {
                    if (res.status == 200) {
                        this.LocalAgents = res.data.rows;
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

                        DataList.delLocalAgent(val).then(res => {
                            if (res.status == 200) {
                                this.$message({
                                    duration: 1000,
                                    type: 'success',
                                    message: '删除成功！'
                                });
                                this.getLocalAgent();
                            }else {
                                this.$message.error(res.message);
                            }
                        });
                    })
                    .catch(() => {
                        this.$message({type: "info", message: "已取消删除"});
                    });

            },
            addData:function () {
                this.dialogVisible=true;
                this.optType="add";
                this.LocalAgent={};
            },
            editData: function (val) {
                this.dialogVisible=true;
                this.optType="edit";
                this.LocalAgent=val;

            },
            submit:function () {
                this.$refs.LocalAgent.validate( valid => {
                    if (valid) {
                        if(this.optType=="add"){
                            DataList.addLocalAgent(this.LocalAgent).then(res => {
                                if (res.status == 200) {
                                    this.$message({
                                        duration: 1000,
                                        type: 'success',
                                        message: '添加成功！'
                                    });
                                    this.getLocalAgent();
                                    this.dialogVisible=false;
                                }else {
                                    this.$message.error(res.message);
                                }
                            });
                        }else if(this.optType=="edit"){
                            DataList.editLocalAgent(this.LocalAgent).then(res => {
                                if (res.status == 200) {
                                    this.$message({
                                        duration: 1000,
                                        type: 'success',
                                        message: '修改成功！'
                                    });
                                    this.getLocalAgent();
                                    this.dialogVisible=false;
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
            closed:function () {
                this.dialogVisible=false;
            }
        }
    };
</script>
<style >
    #toolDiv .el-input{
        width: 300px;
    }

</style>
