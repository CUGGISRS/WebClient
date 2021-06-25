<template>
  <el-container class="home-container">
    <!-- 头部区域 -->
    <el-header>
      <div>
        <span>监利市产业园三维可视化后台管理系统</span>
      </div>
      <div>

       <!-- <el-button type="info" size="mini" @click="logout">退出登录</el-button>-->
        <el-tooltip class="item" effect="dark" content="退出登录" placement="bottom" >
          <el-avatar  @click.native="logout"> {{username}} </el-avatar>
        </el-tooltip>
      </div>
    </el-header>
    <!-- 页面主体 -->
    <el-container>
      <!-- 侧边栏 -->
      <el-aside :width="isCollapse ? '60px' : '240px'" class=" page-left">
        <div class="sidebar-header">
          <div class="collapse-icon-container" @click="toggleCollapse">
            <img
              class="collapse-icon"
              :class="{ unfold: isCollapse }"
              src="@/assets/images/fold.png"
            />
          </div>
        </div>
        <!-- 侧边栏菜单 -->
        <el-menu
          background-color="#333744"
          text-color="#fff"
          unique-opened
          :collapse="isCollapse"
          :collapse-transition="false"
          router
          :default-active="activePath"
        >
          <!-- 一级菜单 电子商务 -->
          <el-submenu
            :index="`${item.id}`"
            v-for="item in menulist"
            :key="item.id"
          >
            <!-- 一级菜单模板区 -->
            <template slot="title">
              <!-- 图标 -->
              <i :class="item.icon"></i>
              <!-- 文本 -->
              <span>{{ item.systemName }}</span>
            </template>
            <!-- 二级菜单 -->
            <el-menu-item
              :index="`/${subItem.path}`"
              @click="saveNavState(`/${subItem.path}`)"
              v-for="subItem in item.children"
              :key="subItem.id"
            >
              <template slot="title">
                <!-- 图标 -->
                <i :class="subItem.icon"></i>
                <!-- 文本 -->
                <span>{{ subItem.systemItem }}</span>
              </template>
            </el-menu-item>
          </el-submenu>
        </el-menu>
      </el-aside>
      <!-- 右侧主体内容 -->
      <el-main>
        <!-- 路由占位符 -->
        <router-view></router-view>
      </el-main>
    </el-container>
  </el-container>
</template>

<script>
  import DataStore from "@/globals/storage/index.js";
export default {
  data() {
    return {
      //左侧菜单数据
      menulist: [
        {
          id: 1,
          systemName: "服务管理",
          icon: "iconfont icon-fuwuguanli",
          children: [
           /* {
              id: 1,
              systemItem: "服务设置",
              path: "service-set",
              icon: "iconfont icon-data-management"
            },*/
            {
              id: 1,
              systemItem: "服务状态",
              path: "service-state",
              icon: "iconfont icon-B"
            }
          ]
        },
        {
          id: 2,
          systemName: "数据管理",
          icon: "iconfont icon-shujuguanli",
          children: [
            {
              id: 1,
              systemItem: "数据管理",
              path: "data-admin",
              icon: "iconfont icon-shujuguanli"
            },
            {
              id: 2,
              systemItem: "数据配置",
              path: "data-config",
              icon: "iconfont icon-yuanshujupeizhi"
            }
          ]
        },
        {
          id: 3,
          systemName: "用户管理",
          icon: "iconfont icon-yonghuguanli",
          children: [
            {
              id: 1,
              systemItem: "用户管理",
              path: "user-manage",
              icon: "iconfont icon-yonghuguanli"
            }
          ]
        }
      ],
      //是否折叠
      isCollapse: false,
      //被激活的链接地址
      activePath: "/service-set",
      username: ""
    };
  },
  created() {
    this.username= DataStore.getUsername();
    // this.getMenuList();
    this.activePath = window.sessionStorage.getItem("activePath")
      ? window.sessionStorage.getItem("activePath")
      : this.activePath;
  },
  methods: {
    // 退出登录
    logout() {
      this.$confirm("此操作将退出登录, 是否继续?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      })
        .then(() => {
          //清空本地存储数据
          DataStore.clear();
          // 跳转至登录界面
          this.$router.push("/login");
        })
        .catch(() => {
          this.$message({
            duration: 1000,
            type: "info",
            message: "已取消操作"
          });
        });
    },
    //点击按钮，切换菜单的折叠与展开
    toggleCollapse() {
      this.isCollapse = !this.isCollapse;
    },
    //保存连接的激活状态
    saveNavState(path) {
      window.sessionStorage.setItem("activePath", path);
      this.activePath = path;
      this.$router.push(path);
    }
  }
};
</script>

<style lang="less" scoped>
.home-container {
  height: 100%;
}

.el-header {
  background-color: #373d41;
  display: flex;
  justify-content: space-between;
  align-items: center;
  color: #fff;
  font-size: 20px;
  > div {
    display: flex;
    align-items: center;
    img {
      width: 40px;
      height: 40px;
      border-radius: 50%;
      background-color: #fff;
    }
    span {
      margin-left: 15px;
    }
  }
}

.el-aside {
  background-color: #333744;
  .el-menu {
    border-right: none;
  }
}

.el-main {
  background-color: #eaedf1;
}

.page-left {
  z-index: 101;
  flex: none;
  display: flex;
  flex-direction: column;
  width: 246px;
  transition: all 0.2s ease-out;
  box-shadow: 2px 0 6px rgba(0, 21, 41, 0.35);
  .sidebar-header {
    flex: none;
    padding: 8px;
    height: 48px;
    overflow: hidden;
    box-shadow: 2px 0 6px rgba(0, 21, 41, 0.35);
    .collapse-icon-container {
      height: 32px;
      text-align: center;
      background: #4a5064;
      border-radius: 3px;
      cursor: pointer;
      &:hover {
        background: #495170;
      }
      .collapse-icon {
        margin-top: 6px;
        width: 20px;
        height: 20px;
        transition: all 0.2s ease-out;
        &.unfold {
          transform: rotate(-180deg);
        }
      }
    }
  }
}
</style>
