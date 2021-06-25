<template>
  <div class="app-container">
    <el-table v-loading="listLoading" :data="list" border fit highlight-current-row style="width: 100%">
      <el-table-column align="center" label="ID" width="90">
        <template slot-scope="scope">
          <span>{{ scope.row.newsid }}</span>
        </template>
      </el-table-column>

      <el-table-column width="180px" align="center" label="时间">
        <template slot-scope="scope">
          <span>{{ scope.row.updatetime }}</span>
        </template>
      </el-table-column>

      <el-table-column width="120px" align="center" label="作者">
        <template slot-scope="scope">
          <span>{{ scope.row.author }}</span>
        </template>
      </el-table-column>

      <el-table-column class-name="status-col" label="类型" width="110">
        <template slot-scope="{row}">
          <el-tag :type="row.type | statusFilter">
            {{ row.type }}
          </el-tag>
        </template>
      </el-table-column>

      <el-table-column min-width="300px" label="标题">
        <template slot-scope="{row}">
          <router-link :to="'/example/EditArticle/'+ row.newsid" class="link-type">
            <span>{{ row.title }}</span>
          </router-link>
        </template>
      </el-table-column>

      <el-table-column align="center" label="操作" width="140">
        <template slot-scope="scope">
          <router-link :to="'/example/EditArticle/'+ scope.row.newsid">
            <el-button type="primary" size="small" >
              编辑
            </el-button>
          </router-link>
          <el-button size="small" type="primary" @click="onDelete(scope.row)" style="background: pink;border-color: #ffa4ba;color: black">
            删除
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total>0" :total="total" :page.sync="listQuery.page" :limit.sync="listQuery.limit"
                @pagination="getList"/>
  </div>
</template>

<script>
  import { fetchList, deleteArticle } from 'api/article'
  import Pagination from 'components/Pagination' // 基于EL分页的二次包

  export default {
    name: 'ArticleList',
    components: { Pagination },
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
        newsid: null,
        updatetime: '',
        author: '',
        type: '',
        title: '',
        status: 'draft',
        list: null,
        total: 0,
        listLoading: true,
        listQuery: {
          page: 1,
          limit: 20
        }
      }
    },
    created() {
      this.getList()
    },
    methods: {
      getList() {
        this.listLoading = true;
        fetchList(this.listQuery).then(this.getFetchData)
      },
      getFetchData(response) {
        this.list = response.data.rows;
        console.log(this.list);
        this.total = response.data.total;
        this.listLoading = false
      },
      onDelete(row) {
        deleteArticle(row.newsid).then(() => {
          this.$notify({
            title: '成功',
            message: '删除成功',
            type: 'success',
            duration: 2000
          });
          const index = this.list.indexOf(row);
          this.list.splice(index, 1);
        })
      }
    }
  }
</script>

<style scoped>
  .edit-input {
    padding-right: 100px;
  }

  .cancel-btn {
    position: absolute;
    right: 15px;
    top: 10px;
  }
</style>
