<template>
  <div class="app-container calendar-list-container">
    <div class="filter-container">
      <el-input @keyup.enter.native="handleFilter" style="width: 150px;" class="filter-item" placeholder="年月"
                v-model="listQuery.date"></el-input>
      <el-button class="filter-item" type="primary" v-waves  @click="handleFilter">搜索</el-button>
    </div>

    <el-table :key='tableKey' :data="list" v-loading.body="listLoading" border fit highlight-current-row
              style="width: 99%">
      <el-table-column align="center" label="日期" width="140">
        <template slot-scope="scope">
          <span>{{scope.row.day}}</span>
        </template>
      </el-table-column>
      <el-table-column align="center" label="星期" width="150">
        <template slot-scope="scope">
          <span>{{scope.row.week}}</span>
        </template>
      </el-table-column>
      <el-table-column width="1195" align="center" label="值班专家">
        <template slot-scope="{row}">
          <template v-for="e in row.expertduties">
            <el-tag :type="e.expertname | statusFilter">
              {{ e.expertname}}
            </el-tag>
          </template>
        </template>
      </el-table-column>
      <el-table-column align="center" label="操作" width="148">
        <template slot-scope="scope">
          <el-button v-if="userManager_btn_edit" size="small" type="success" @click="handleUpdate(scope.row)">编辑
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-dialog :title="textMap[dialogStatus]" :visible.sync="dialogFormVisible">
      <el-form :model="form" ref="form" label-width="100px">
        <el-table :key='tableKey' :data="form" v-loading.body="listLoading" border fit highlight-current-row
                  style="width: 85%">
          <el-table-column align="center" label="序号" width="80">
            <template slot-scope="scope">
              <span>{{scope.row.id}}</span>
            </template>
          </el-table-column>
          <el-table-column align="center" label="专家姓名" width="120">
            <template slot-scope="scope">
              <span>{{scope.row.name}}</span>
            </template>
          </el-table-column>
          <el-table-column align="center" label="业务专长" width="320">
            <template slot-scope="scope">
              <span>{{scope.row.business}}</span>
            </template>
          </el-table-column>
          <el-table-column align="center" label="职务/职位" width="180">
            <template slot-scope="scope">
              <span>{{scope.row.official}}</span>
            </template>
          </el-table-column>
             <el-table-column align="center" label="操作" width="80">
               <template slot-scope="scope">
                 <div id = "gouxuan">
                   <input name="check" class="selBtn" type="checkbox" v-model="scope.row.checked" ref="Check">
                 </div>
               </template>
             </el-table-column>
        </el-table>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="cancel('form')">取 消</el-button>
        <el-button v-if="dialogStatus==='update'" type="primary" @click="update('form')">保 存</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
  import {
    expertList,
    expertdutyList,
    saveDuty
  } from 'api/expert';
  import { mapGetters } from 'vuex';
  import Upload from 'components/Upload/SingleImage3'


  export default {
    name: 'duty',
    components: { Upload },
    filters: {
      statusFilter(status) {
        const statusMap = {
          published: 'success',
          draft: 'info',
          deleted: 'danger'
        };
        return statusMap[status]
      }
    },

    data() {
      return {
        dutyform: {
          id: 0,
          date: undefined,
          expertid: undefined,
          expertname: undefined
        },
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
        editDay: null, // 定义一个新的函数，为空
        list: null,
        total: null,
        listLoading: true,
        listQuery: {
          year: 2019,
          month: 9,
          date: undefined
        },
        listexpertQuery: {
          page: 1,
          limit: 20
        },
        dialogFormVisible: false,
        dialogStatus: '',
        userManager_btn_edit: false,
        userManager_btn_del: false,
        textMap: {
          update: '编辑',
          create: '专家排班'
        },
        tableKey: 0,
        freeMon: ''
      }
    },
    created() {
      this.getList();
      this.userManager_btn_edit = this.elements['userManager:btn_edit'];
      this.userManager_btn_del = this.elements['userManager:btn_del'];
    },
    computed: {
      ...mapGetters([
        'elements'
      ])
    },
    methods: {
      getList() {
        this.listLoading = true;
        expertdutyList(this.listQuery)
          .then(response => {
            this.list = response.data;
            console.log(response);
            this.listLoading = false;
          });
      },
      handleFilter() {
        this.getdutyList();
      },
      handleUpdate(row) {
        this.editDay = row.day; // 点击编辑，获取编辑按钮对应的当天的日期存进editDay中
        console.log(this.editDay);
        expertList(row.id).then(response => {
          this.form = response.data.rows;
          this.dialogFormVisible = true;
          this.dialogStatus = 'update';
          for (let i = 0; i < this.form.length; i++) {  // 循环遍历专家列表和排班列表，如果专家id和排班的expertid相同，则默认选中状态
            for (let j = 0; j < row.expertduties.length; j++) {
              if (row.expertduties[j].expertid === this.form[i].id) {
                this.form[i].checked = true;
              }
            }
          }
        });
      },
      cancel(listName) {
        this.dialogFormVisible = false;
        this.$refs[listName].resetFields();
      },
      update(listName) {
        console.log(this.list);
        const newlist = []; // 定义一个list，存放选中的专家信息为专家排班列表中的字段格式
        for (let i = 0; i < this.form.length; i++) {
          if (this.form[i].checked === true) {
            newlist.push({
              id: null,
              date: this.listQuery.year + '-' + this.listQuery.month + '-' + this.editDay + ' 00:00:00',
              expertid: this.form[i].id,
              expertname: this.form[i].name
            });
          }
        }
        const set = this.$refs;
        set[listName].validate(valid => {
          if (valid) {
            this.dialogFormVisible = false;
            saveDuty(this.editDay, newlist).then(() => {  // 点击保存，将专家排班情况添加进对应的日期的专家排班列表中
              this.dialogFormVisible = false;
              this.getList();
              this.$notify({
                title: '成功',
                message: '保存成功',
                type: 'success',
                duration: 2000
              })
            })
          } else {
            return false;
          }
        })
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
        this.dutyform = {
          id: 0,
          date: undefined,
          expertid: undefined,
          expertname: undefined
        }
      }
    }
  }

</script>
