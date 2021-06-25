<template>
    <div>
        <!-- 面包屑导航区域 -->
        <el-breadcrumb separator-class="el-icon-arrow-right">
            <el-breadcrumb-item :to="{ path: '/welcome' }">首页</el-breadcrumb-item>
            <el-breadcrumb-item>服务管理</el-breadcrumb-item>
            <el-breadcrumb-item>服务状态</el-breadcrumb-item>
        </el-breadcrumb>
        <div id="toolDiv">
            <el-input v-model="params.name" clearable placeholder="请输入名称"></el-input>
            <el-button @click="getData()" type="primary" size="small" circle icon="el-icon-search" ></el-button>
        </div>
        <el-table stripe  style="width:95%"
                  :height="tableHeight"
                  :data="rows">
            <el-table-column prop="name" label="名称"/>

            <el-table-column prop="status" label="状态" width="200">
                <template slot-scope="scope">
                    <el-button v-if="scope.row.status==0" @click="editState(scope.row)" type="danger" plain  size="mini" >停止</el-button>
                    <el-button v-else @click="editState(scope.row)" type="success" plain  size="mini" >启动</el-button>
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


    </div>
</template>

<script>
    import DataList from "@/globals/service/dataList.js";

    export default {
        data() {

            return {
                tableHeight: 0,//表格高度
                // 分页
                total: 0,
                params: {
                    page: 1,
                    limit: 15
                },
                rows: [],
                row: {}
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
            //获得数据
            getData: function () {
                DataList.getServiceState(this.params).then(res => {
                    if (res.status == 200) {
                        this.rows = res.data.rows;
                        this.total = res.data.total;
                    }else {
                        this.$message.error(res.message);
                    }
                });
            },
            editState: function (val) {

                this.$confirm(`确定修改状态?`, "提示", {
                    confirmButtonText: "确定",
                    cancelButtonText: "取消",
                    type: "warning"
                })
                    .then(() => {
                        var status=val.status;
                        if(status===0){
                            status=1;
                        }else if(status===1){
                            status=0;
                        }

                        DataList.editServiceState({id:val.id,status:status}).then(res => {
                            if (res.status == 200) {
                                this.$message.success("修改状态成功");
                                this.getData();
                            }else {
                                this.$message.error(res.message);
                            }
                        });

                    })
                    .catch(() => {
                        this.$message({type: "info", message: "已取消修改"});
                    });

            }
        }
    };
</script>
