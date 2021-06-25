<template>
  <el-container class="home-container">
    <!-- 头部 -->
    <el-header height="70px">
      <img src="../assets/img/市场监督管理.png" />
      <h2>监利市农产品质量安全监督管理系统</h2>
      <div class="head-user" @click="handleUserInfo">
        <span class="el-icon-user-solid"></span>
        <el-tooltip effect="light" content="个人中心" placement="left">
          <span>{{ form.user }}</span>
        </el-tooltip>
      </div>
      <el-tooltip effect="light" content="退出登录" placement="left">
        <span class="iconfont icon-tuichudenglu" @click="logout"></span>
      </el-tooltip>
    </el-header>
    <el-container>
      <!-- 侧边栏 -->
      <el-aside :width="isCollapse ? '64px' : '300px'">
        <div class="toggle-button" @click="toggleCollapse">|||</div>
        <!-- 侧边菜单栏区域 -->
        <el-menu
          :default-active="activePath"
          background-color="#333744"
          text-color="#fff"
          class="el-menu-vertical-demo"
          active-text-color="#409eff"
          unique-opened
          :collapse="isCollapse"
          :collapse-transition="false"
          router
        >
          <!-- 企业信息审核 -->
          <el-menu-item index="/registration-audit" @click="saveNavState('/registration-audit')">
            <i class="iconfont icon-shenhe"></i>
            <span slot="title">企业信息审核</span>
          </el-menu-item>

          <!-- 溯源信息监督 -->
          <el-menu-item index="/retrospect-supervise" @click="saveNavState('/retrospect-supervise')">
            <i class="iconfont icon-jiandu"></i>
            <span slot="title">溯源信息监督</span>
          </el-menu-item>
          <!-- 检验管理 -->
          <el-menu-item index="/inspect" @click="saveNavState('/inspect')">
            <i class="iconfont icon-jianyanjilu"></i>
            <span slot="title">检验管理</span>
          </el-menu-item>
          <el-menu-item index="/credit-rating" @click="saveNavState('/credit-rating')">
            <i class="el-icon-trophy"></i>
            <span slot="title">企业信用评定</span>
          </el-menu-item>
          <!-- 统计分析 -->
          <el-menu-item index="/statistics" @click="saveNavState('/statistics')">
            <i class="iconfont icon-tongjifenxi"></i>
            <span slot="title">统计分析</span>
          </el-menu-item>
          <!-- 安全预警 -->
          <el-menu-item index="/forewarning" @click="saveNavState('/forewarning')">
            <i class="iconfont icon-yujing"></i>
            <span slot="title">安全预警</span>
          </el-menu-item>
        </el-menu>
      </el-aside>

      <!-- 个人中心弹出框 -->
      <el-dialog title="个人中心" :visible.sync="dialogFormVisible" width="30%">
        <el-form :model="form">
          <el-form-item label="用户名">
            <el-input v-model="form.user" disabled></el-input>
          </el-form-item>
          <el-form-item label="电话">
            <el-input v-model="form.phone"></el-input>
          </el-form-item>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button @click="dialogFormVisible = false">取 消</el-button>
          <el-button type="primary" @click="handleEditUserInfo">确定编辑</el-button>
        </div>
      </el-dialog>
      <!-- 主界面 -->
      <el-main>
        <router-view></router-view>
      </el-main>
    </el-container>
  </el-container>
</template>

<script>
export default {
  data() {
    return {
      // 是否折叠
      isCollapse: false,
      // 被激活的链接地址
      activePath: '/registration-audit',
      form: {
        user: window.sessionStorage.getItem('username'),
        phone: '',
      },
      dialogFormVisible: false,
      id: '',
    };
  },
  created() {
    // 进入被激活的地址
    this.activePath = window.sessionStorage.getItem('activePath')
      ? window.sessionStorage.getItem('activePath')
      : this.activePath;
  },
  methods: {
    async handleUserInfo() {
      const { data: res } = await this.$http.get('comSys/getCurrentUser');
      if (res.status == 200) {
        this.id = res.data.id;
      }
      const { data: res2 } = await this.$http.get('comSys/User/' + this.id);
      if (res2.status == 200) {
        this.form.phone = res2.data.phone;
      }
      this.dialogFormVisible = true;
    },
    async handleEditUserInfo() {
      const { data: res } = await this.$http.put('comSys/User', {
        id: this.id,
        phone: this.form.phone,
      });
      if (res.status == 200) {
        this.$message.success('更新电话成功');
      }
      this.dialogFormVisible = false;
    },
    // 退出登录
    logout() {
      this.$confirm('此操作将退出登录, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning',
      })
        .then(() => {
          // 清除token
          window.sessionStorage.clear();
          // 跳转至登录界面
          this.$router.push('/login');
        })
        .catch(() => {
          this.$message({
            duration: 1000,
            type: 'info',
            message: '已取消操作',
          });
        });
    },
    // 是否折叠
    toggleCollapse() {
      this.isCollapse = !this.isCollapse;
    },
    // 保存链接的激活状态
    saveNavState(activePath) {
      window.sessionStorage.setItem('activePath', activePath);
      this.activePath = activePath;
    },
  },
};
</script>

<style scoped lang="less">
// 页面容器
.home-container {
  width: 100%;
  height: 100%;
}

// 顶部样式
.el-header {
  width: 100%;
  background-color: #333744;
  position: relative;

  // logo样式
  img {
    width: 35px;
    height: 35px;
    position: absolute;
    left: 20px;
    top: 50%;
    transform: translateY(-50%);
  }

  // 标题样式
  h2 {
    // width: 400px;
    height: 70px;
    margin: 0;
    position: absolute;
    left: 70px;
    top: 0;
    line-height: 70px;
    font-size: 30px;
    color: #fff;
  }

  // 登录用户样式
  .head-user {
    // width: 100px;
    height: 70px;
    position: absolute;
    right: 80px;
    top: 50%;
    color: #fff;
    font-size: 20px;
    line-height: 70px;
    transform: translateY(-50%);
    cursor: pointer;

    span {
      margin-right: 10px;
    }
  }

  // 退出登录样式
  .icon-tuichudenglu {
    position: absolute;
    top: 50%;
    transform: translateY(-50%);
    right: 40px;
    font-size: 30px;
    color: #fff;
    cursor: pointer;
  }
}

// 侧边导航栏
.el-aside {
  background-color: #333744;

  .toggle-button {
    background-color: #4a5064;
    color: #fff;
    font-size: 10px;
    line-height: 24px;
    text-align: center;
    letter-spacing: 0.2em;
    cursor: pointer;
  }

  .el-menu {
    border-right: none;
  }

  .iconfont {
    font-size: 20px;
    margin-right: 8px;
  }

  span {
    font-size: 16px;
  }

  .el-menu-item {
    font-size: 14px;
  }
}

// 主界面
.el-main {
  background-color: #eaedf1;
}

// 底部样式
.el-footer {
  background-color: #333744;
  text-align: center;
  line-height: 30px;

  a {
    color: #fff;
    font-size: 16px;
    // text-decoration: none;
  }

  span {
    color: #fff;
    font-size: 16px;
  }
}
</style>
