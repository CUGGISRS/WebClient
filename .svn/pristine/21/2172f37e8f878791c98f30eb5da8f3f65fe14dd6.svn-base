<template>
  <el-container class="home-container">
    <!-- 头部 -->
    <el-header height="70px">
      <h2>{{$sc.system_name}}</h2>
      <div class="head-user">
        <span class="el-icon-user-solid"></span>
        <span>{{ user }}</span>
      </div>
      <el-tooltip effect="light" content="退出登录" placement="bottom">
        <span class="iconfont icon-tuichudenglu" @click="logout"></span>
      </el-tooltip>
    </el-header>
    <el-container class="sub-container">
      <!-- 侧边栏 -->
      <el-aside :width="isCollapse ? '64px' : '200px'">
        <div class="toggle-button" @click="toggleCollapse">|||</div>
        <!-- 侧边菜单栏区域 -->
        <el-menu :default-active="activePath"
          background-color="#333744"
          text-color="#fff"
          class="el-menu-vertical-demo"
          active-text-color="#409eff"
          unique-opened
          :collapse="isCollapse"
          :collapse-transition="false"
          router>
          <!-- 设备监控 -->
          <el-menu-item index="/equipment-monitoring" @click="saveNavState('/equipment-monitoring')">
            <i class="iconfont icon-ziyuanxhdpi"></i>
            <span slot="title">设备监控</span>
          </el-menu-item>
          <!-- 数据查询 -->
          <el-menu-item index="/data-search" @click="saveNavState('/data-search')">
            <i class="iconfont icon-shujuchaxun"></i>
            <span slot="title">数据查询</span>
          </el-menu-item>
          <!-- 数据分析 -->
          <el-menu-item index="/data-analysis" @click="saveNavState('/data-analysis')">
            <i class="iconfont icon-shujufenxi"></i>
            <span slot="title">数据分析</span>
          </el-menu-item>
          <!-- 报警信息 -->
          <el-menu-item index="/warning-info" @click="saveNavState('/warning-info')">
            <i class="el-icon-bell"></i>
            <span slot="title">报警信息</span>
          </el-menu-item>
          <!-- 设备管理 -->
          <el-menu-item index="/device-group" @click="saveNavState('/device-group')">
            <i class="iconfont icon-shebeifenzu"></i>
            <span slot="title">设备管理</span>
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
      // 登录用户
      user: window.sessionStorage.getItem('enterpriseName'),
      // 被激活的链接地址
      activePath: '/equipment-monitoring'
    }
  },
  created() {
    // 进入被激活的地址
    this.activePath = window.sessionStorage.getItem('activePath') ? window.sessionStorage.getItem('activePath') : this.activePath;
  },
  methods: {
    // 退出登录
    logout() {
      this.$confirm('此操作将退出登录, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        // 清除token
        window.sessionStorage.clear();
        // 跳转至登录界面
				this.$router.push('/login');
      }).catch(() => {
        this.$message({
					duration: 1000,
          type: 'info',
          message: '已取消操作'
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
    }
  },
}
</script>

<style scoped lang="less">
  // 页面容器
  .home-container {
    width: 100%;
    height: 100%;
  }

  // 顶部样式
  .el-header {
    height: 70px;
    width: 100%;
    background-color: #333744;
    position: relative;

    // 标题样式
    h2 {
      width: 420px;
      height: 70px;
      margin: 0;
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

      span {
        margin-right: 10px;
      }
    }

    // 退出登录样式
    .icon-tuichudenglu {
      position: absolute;
      right: 40px;
      top: 50%;
      transform: translateY(-50%);
      font-size: 30px;
      color: #fff;
      cursor: pointer;
    }
  }

  .sub-container{
    top: 70px;
    left: 0;
    right: 0;
    bottom: 0;
    position: absolute;
  }

  // 侧边导航栏
  .el-aside{
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
