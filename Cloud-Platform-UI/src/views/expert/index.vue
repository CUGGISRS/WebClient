<template>
  <div class="app-container calendar-list-container">
    <div class="filter-container">
      <el-input @keyup.enter.native="handleFilter" style="width: 200px;" class="filter-item" placeholder="姓名或账户"
                v-model="listQuery.name"></el-input>
      <el-button class="filter-item" type="primary" v-waves icon="search" @click="handleFilter">搜索</el-button>
      <el-button class="filter-item" v-if="userManager_btn_add" style="margin-left: 10px;" @click="handleCreate"
                 type="primary" icon="edit">添加
      </el-button>
    </div>
    <el-table :key='tableKey' :data="list" v-loading.body="listLoading" border fit highlight-current-row
              style="width: 100%">
      <el-table-column align="center" label="序号" width="65">
        <template slot-scope="scope">
          <span>{{scope.row.id}}</span>
        </template>
      </el-table-column>
      <el-table-column width="100" align="center" label="姓名">
        <template slot-scope="scope">
          <span>{{scope.row.name}}</span>
        </template>
      </el-table-column>
      <el-table-column width="200" align="center" label="业务专长">
        <template slot-scope="scope">
          <span>{{scope.row.business}}</span>
        </template>
      </el-table-column>
      <el-table-column width="110" align="center" label="职务类型">
        <template slot-scope="scope">
          <span>{{scope.row.official}}</span>
        </template>
      </el-table-column>
      <el-table-column width="150" align="center" label="所在地区">
        <template slot-scope="scope">
          <span>{{scope.row.location}}</span>
        </template>
      </el-table-column>
      <el-table-column width="150" align="center" label="联系方式">
        <template slot-scope="scope">
          <span>{{scope.row.telephone}}</span>
        </template>
      </el-table-column>
      <el-table-column width="150" align="center" label="微信号码">
        <template slot-scope="scope">
          <span>{{scope.row.wechat}}</span>
        </template>
      </el-table-column>
      <el-table-column width="592" align="center" label="专家简介">
        <template slot-scope="scope">
          <span style="overflow: hidden;text-overflow:ellipsis;white-space: nowrap;">{{scope.row.introduction}}</span>
        </template>
      </el-table-column>
      <el-table-column align="center" label="操作" width="150">
        <template slot-scope="scope">
          <el-button v-if="userManager_btn_edit" size="small" type="success" @click="handleUpdate(scope.row)">编辑
          </el-button>
          <el-button v-if="userManager_btn_del" size="small" type="danger" @click="handleDelete(scope.row)">删除
          </el-button>
        </template>
      </el-table-column>
    </el-table>
    <div v-show="!listLoading" class="pagination-container">
      <el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange"
                     :current-page.sync="listQuery.page" :page-sizes="[10,20,30, 50]" :page-size="listQuery.limit"
                     layout="total, sizes, prev, pager, next, jumper" :total="total"></el-pagination>
    </div>
    <el-dialog :title="textMap[dialogStatus]" :visible.sync="dialogFormVisible">
      <el-form :model="form" :rules="rules" ref="form" label-width="100px">
        <el-form-item label="姓名" prop="name">
          <el-input v-if="dialogStatus == 'create'" v-model="form.name" placeholder="请输入姓名"></el-input>
          <el-input v-else v-model="form.name" placeholder="请输入姓名" readonly></el-input>
        </el-form-item>
        <!--<el-form-item label="账户" prop="username">
          <el-input v-if="dialogStatus == 'create'" v-model="form.username" placeholder="请输入账户"></el-input>
          <el-input v-else v-model="form.username" placeholder="请输入账户" readonly></el-input>
        </el-form-item>
        <el-form-item label="密码" placeholder="请输入密码" prop="password">
          <el-input type="password" v-model="form.password" placeholder="请输入密码"></el-input>
        </el-form-item>-->
        <el-form-item label="业务专长" prop="business">
          <el-input v-model="form.business" placeholder="请输入业务专长"></el-input>
        </el-form-item>
        <!--<el-form-item v-if="dialogStatus == 'create'" label="职务/职称" placeholder="请输入职务/职称" prop="password">
          <el-input v-model="form.password" placeholder="请输入职务/职称"></el-input>
        </el-form-item>-->
        <el-form-item label="职务/职称" prop="official">
          <el-input v-model="form.official" placeholder="请输入职务或职称"></el-input>
        </el-form-item>
        <!--<el-form-item label="职务/职称" prop="official">
          <el-select class="filter-item" v-model="form.official" filterable placeholder="请选择">
            <el-option v-for="item in  sexOptions"
                       :key="item"
                       :label="item"
                       :value="item">
            </el-option>
          </el-select>
        </el-form-item>-->
        <el-form-item label="所在地区" prop="location">
          <el-input v-model="form.location" placeholder="请输入所在地区"></el-input>
        </el-form-item>
        <el-form-item label="联系方式" prop="telephone">
          <el-input type="text" v-model="form.telephone" placeholder="请输入手机号"></el-input>
        </el-form-item>
        <el-form-item label="微信号" prop="wechat">
          <el-input v-model="form.wechat" placeholder="请输入微信号"></el-input>
        </el-form-item>
        <el-form-item label="专家简介">
          <el-input type="textarea" :autosize="{ minRows: 3, maxRows: 5}" placeholder="请输入内容"
                    v-model="form.introduction">
          </el-input>
        </el-form-item>
        <!--<el-form-item label="上传照片" prop="image">
          &lt;!&ndash;<el-upload
            v-model="form.image"
            action="https://jsonplaceholder.typicode.com/posts/"
            accept="image/jpeg,image/png,image/jpg"
            :before-upload="onBeforeUpload">

            <el-button slot="trigger" size="small" type="primary">点击上传</el-button>
            <div slot="tip" class="el-upload__tip">只能上传jpg/png/jpeg文件</div>
          </el-upload>&ndash;&gt;
          <el-form-item style="margin-bottom: 30px;">
            <Upload v-model="form.image"/>
          </el-form-item>
        </el-form-item>-->
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="cancel('form')">取 消</el-button>
        <el-button v-if="dialogStatus=='create'" type="primary" @click="create('form')">确 定</el-button>
        <el-button v-else type="primary" @click="update('form')">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
  import {
    expertList,
    addExpert,
    deleteExpert,
    fetchExpert,
    putExpert
  } from 'api/expert';
  import { mapGetters } from 'vuex';
  import Upload from 'components/Upload/SingleImage3'

  export default {
    name: 'user',
    components: { Upload },
    data() {
      return {
        form: {
          id: 0,
          business: undefined, // 业务专长
          name: undefined, // 名字
          official: '', // 职务/职称
          introduction: undefined,
          location: undefined,
          telephone: undefined,
          wechat: undefined,
          image: ''
        },
        rules: {
          name: [
            {
              required: true,
              message: '请输入姓名',
              trigger: 'blur'
            }
          ],
          username: [
            {
              required: true,
              message: '请输入用户名',
              trigger: 'blur'
            }
          ],
          password: [
            {
              required: true,
              message: '请输入密码',
              trigger: 'blur'
            },
            {
              min: 6,
              max: 20,
              message: '密码长度为 6 到 20 个字符'
              // trigger: 'blur'
            }
          ],
          business: [
            {
              required: true,
              message: '请输入专长'
             // trigger: 'blur'
            },
            {
              min: 1,
              max: 20,
              message: '长度在 1 到 20 个字符'
             // trigger: 'blur'
            }
          ],
          official: [
            {
              required: true,
              message: '请选择职务或职称'
            }
          ],
          location: [
            {
              required: true,
              message: '请输入您所在地区'
            }
          ],
          telephone: [
            {
              required: true,
              message: '请输入手机号'
            }, {
              min: 11,
              max: 11,
              message: '请输入正确手机号',
              trigger: 'blur'
            }
          ],
          wechat: [
            {
              required: true,
              message: '请输入微信号'
            }
          ],
          image: [
            {
              required: true,
              message: '请添加照片',
              trigger: 'blur'
            }
          ]
        },
        list: null,
        total: null,
        listLoading: true,
        listQuery: {
          page: 1,
          limit: 20,
          name: undefined
        },

        dialogFormVisible: false,
        dialogStatus: '',
        userManager_btn_edit: false,
        userManager_btn_del: false,
        userManager_btn_add: false,
        textMap: {
          update: '编辑',
          create: '专家申请'
        },
        tableKey: 0
      }
    },
    created() {
      this.getList();
      this.userManager_btn_edit = this.elements['userManager:btn_edit'];
      this.userManager_btn_del = this.elements['userManager:btn_del'];
      this.userManager_btn_add = this.elements['userManager:btn_add'];
    },
    computed: {
      ...mapGetters([
        'elements'
      ])
    },
    methods: {
      getList() {
        this.listLoading = true;
        expertList(this.listQuery)
          .then(response => {
            console.log(response);
            this.list = response.data.rows;
            this.total = response.data.total;
            this.listLoading = false;
          })
      },
      onBeforeUpload(file) {
        const isIMAGE = file.type === 'image/jpeg' || 'image/jpg' || 'image/png';
        const isLt1M = file.size / 1024 / 1024 < 1;

        if (!isIMAGE) {
          this.$message.error('上传文件只能是图片格式!');
        }
        if (!isLt1M) {
          this.$message.error('上传文件大小不能超过 1MB!');
        }
        return isIMAGE && isLt1M;
      },
      handleFilter() {
        this.getList();
      },
      handleSizeChange(val) {
        this.listQuery.limit = val;
        this.getList();
      },
      handleCurrentChange(val) {
        this.listQuery.page = val;
        this.getList();
      },
      handleCreate() {
        this.resetTemp();
        this.dialogStatus = 'create';
        this.dialogFormVisible = true;
      },
      handleUpdate(row) {
        fetchExpert(row.id).then(response => {
          this.form = response.data;
          this.dialogFormVisible = true;
          this.dialogStatus = 'update';
        });
      },
      handleDelete(row) {
        this.$confirm('此操作将永久删除, 是否继续?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        })
          .then(() => {
            deleteExpert(row.id)
              .then(() => {
                this.$notify({
                  title: '成功',
                  message: '删除成功',
                  type: 'success',
                  duration: 2000
                });
                const index = this.list.indexOf(row);
                this.list.splice(index, 1);
              });
          });
      },
      create(formName) {
        console.log(this.form);
        const set = this.$refs;
        set[formName].validate(valid => {
          if (valid) {
            addExpert(this.form)
              .then(() => {
                this.dialogFormVisible = false;
                this.getList();
                this.$notify({
                  title: '成功',
                  message: '创建成功',
                  type: 'success',
                  duration: 2000
                });
              })
          } else {
            return false;
          }
        });
      },
      cancel(formName) {
        this.dialogFormVisible = false;
        this.$refs[formName].resetFields();
      },
      update(formName) {
        const set = this.$refs;
        set[formName].validate(valid => {
          if (valid) {
            this.dialogFormVisible = false;
            this.form.image = '';
            console.log(this.form.id);
            console.log(this.form);
            putExpert(this.form.id, this.form).then(() => {
              this.dialogFormVisible = false;
              this.getList();
              this.$notify({
                title: '成功',
                message: '创建成功',
                type: 'success',
                duration: 2000
              });
            });
          } else {
            return false;
          }
        });
      },
      resetTemp() {
        this.form = {
          id: 0,
          business: undefined,
          name: undefined,
          official: '',
          introduction: undefined,
          location: undefined,
          telephone: undefined,
          wechat: undefined,
          image: ''
        };
      }
    }
  }
</script>
