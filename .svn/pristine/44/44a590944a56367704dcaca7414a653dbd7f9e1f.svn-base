<template>
  <el-container class="home-container">
    <!-- 头部 -->
    <el-header height="70px">
      <h2>监利市农产品质量安全追溯应用系统（种植企业版）</h2>
      <div class="user-section">
        <div class="user-name">用户：{{ userName }}</div>
        <el-tooltip effect="light" content="退出登录" placement="left">
          <span class="iconfont icon-tuichu" @click="logout"></span>
        </el-tooltip>
      </div>
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
          <!-- 企业基本信息 -->
          <el-menu-item index="/user" @click="saveNavState('/user')">
            <i class="iconfont icon-qiyejibenxinxi"></i>
            <span slot="title">企业基本信息</span>
          </el-menu-item>
          <!-- 基本配置 -->
          <el-submenu index="1">
            <template slot="title">
              <i class="iconfont icon-icon"></i>
              <span slot="title">基本配置</span>
            </template>
            <el-menu-item-group>
              <el-menu-item index="/qualification" @click="saveNavState('/qualification')">企业资质管理</el-menu-item>
              <el-menu-item index="/retailer" @click="saveNavState('/retailer')">销售商管理</el-menu-item>
              <el-menu-item index="/supply" @click="saveNavState('/supply')">供应商管理</el-menu-item>
              <el-menu-item index="/plantingbase" @click="saveNavState('/plantingbase')">种植基地管理</el-menu-item>
              <el-menu-item index="/productionbase" @click="saveNavState('/productionbase')">生产基地管理</el-menu-item>
            </el-menu-item-group>
          </el-submenu>
          <!-- 产品管理 -->
          <el-menu-item index="/product" @click="saveNavState('/product')">
            <i class="iconfont icon-chanpinpeizhi"></i>
            <span slot="title">产品管理</span>
          </el-menu-item>
          <!-- 种植管理 -->
          <el-submenu index="/basicoperation">
            <template slot="title">
              <i class="iconfont icon-zhongzhi"></i>
              <span slot="title">种植管理</span>
            </template>
            <el-menu-item-group>
              <el-menu-item index="/basicoperation" @click="saveNavState('/basicoperation')">基本作业管理</el-menu-item>
              <el-menu-item index="/growingenviroment" @click="saveNavState('/growingenviroment')"
                >生长环境管理</el-menu-item
              >
            </el-menu-item-group>
          </el-submenu>
          <!-- 生产管理 -->
          <el-submenu index="/harvest">
            <template slot="title">
              <i class="iconfont icon-shengchanguanli"></i>
              <span slot="title">生产管理</span>
            </template>
            <el-menu-item-group>
              <el-menu-item index="/harvest" @click="saveNavState('/harvest')">采收管理</el-menu-item>
              <el-menu-item index="/buy" @click="saveNavState('/buy')">收购管理</el-menu-item>
              <el-menu-item index="/processbatch" @click="saveNavState('/processbatch')">加工批次管理</el-menu-item>
              <el-menu-item index="/inspection" @click="saveNavState('/inspection')">检验报告</el-menu-item>
            </el-menu-item-group>
          </el-submenu>
          <!-- 销售管理 -->
          <el-menu-item index="/order" @click="saveNavState('/order')">
            <i class="iconfont icon-xiaoshouguanli_icox"></i>
            <span slot="title">销售管理</span>
          </el-menu-item>
          <!-- 追溯管理 -->
          <el-menu-item index="/retrospect" @click="saveNavState('/retrospect')">
            <i class="iconfont icon-zhuisu"></i>
            <span slot="title">追溯管理</span>
          </el-menu-item>
          <!-- 统计详情 -->
          <el-menu-item index="/statistics" @click="saveNavState('/statistics')">
            <i class="iconfont icon-tongji"></i>
            <span slot="title">统计详情</span>
          </el-menu-item>
        </el-menu>
      </el-aside>
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
      activePath: '/user',
      userName: '',
    };
  },
  created() {
    this.userName = window.sessionStorage.getItem('userName');
    // 进入被激活的地址
    this.activePath = window.sessionStorage.getItem('activePath')
      ? window.sessionStorage.getItem('activePath')
      : this.activePath;
  },
  methods: {
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
  display: flex;
  align-items: center;
  justify-content: space-between;
  .user-section {
    color: #fff;
    font-size: 16px;
    display: flex;
    justify-content: space-between;
    align-items: center;
    .user-name {
      padding-right: 20px;
      height: 32px;
      line-height: 32px;
    }
  }
  // 标题样式
  h2 {
    // width: 600px;
    height: 70px;
    margin: 0;
    text-align: center;
    line-height: 70px;
    font-size: 30px;
    color: #fff;
  }

  // 退出登录样式
  .icon-tuichu {
    font-size: 30px;
    color: #fff;
    margin-right: 20px;
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
