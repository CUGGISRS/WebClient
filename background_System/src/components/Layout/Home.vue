<template>
  <el-container class="home-container">
    <!-- 头部区域 -->
    <el-header height="70px">
      <div class="system-title">
        <h1>监利市农业信息后台管理系统</h1>
      </div>
      <div class="system-user">
        <div class="system-user-logo">
          <img src="@/assets/images/user-logo.png" alt="user-logo" />
          <span class="user-name">{{ userName }}</span>
        </div>
        <div>
          <el-button type="info" size="small" @click="logout">退出登录</el-button>
        </div>
      </div>
    </el-header>
    <!-- 页面主体 -->
    <el-container>
      <!-- 侧边栏 -->
      <el-aside :width="isCollapse ? '64px' : '260px'" class=" page-left">
        <div class="sidebar-header">
          <div class="collapse-icon-container" @click="toggleCollapse">
            <img class="collapse-icon" :class="{ unfold: isCollapse }" src="@/assets/images/fold.png" />
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
          <el-submenu :index="`${item.id}`" v-for="item in menulist" :key="item.id">
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
export default {
  data() {
    return {
      //用户名
      userName: '',
      //左侧菜单数据
      menulist: [
        {
          id: 1,
          systemName: '电子商务子系统',
          icon: 'el-icon-s-platform',
          children: [
            {
              id: 1,
              systemItem: '文章管理',
              path: 'commerce-article',
              icon: 'iconfont icon-wenzhang-copy'
            },
            {
              id: 2,
              systemItem: '市场行情管理',
              path: 'mark-condition',
              icon: 'iconfont icon-shichangdongtai'
            },
            {
              id: 3,
              systemItem: '农产品产销信息',
              path: 'productioin-marketing-information',
              icon: 'iconfont icon-nongchanxiaoshou'
            },
            {
              id: 4,
              systemItem: '价格行情管理',
              path: 'price-quotation',
              icon: 'iconfont icon-jiage'
            },
            {
              id: 5,
              systemItem: '优质农产品管理',
              path: 'high-quality-product',
              icon: 'iconfont icon-nongchanpin'
            },
            {
              id: 6,
              systemItem: '留言板信息管理',
              path: 'message-board',
              icon: 'iconfont icon-liuyanban-05'
            },
            {
              id: 7,
              systemItem: '轮播图管理',
              path: 'commerce-admin-swiper',
              icon: 'iconfont icon-lunbotu'
            },
            {
              id: 8,
              systemItem: '基础数据管理',
              path: 'data-dictionary',
              icon: 'iconfont icon-tesezhuanti'
            }
          ]
        },
        {
          id: 2,
          systemName: '公益性子系统',
          icon: 'iconfont icon-gongyi',
          children: [
            {
              id: 1,
              systemItem: '文章管理',
              path: 'admin-article',
              icon: 'iconfont icon-wenzhang-copy'
            },
            {
              id: 2,
              systemItem: '供求信息管理',
              path: 'trade-leads',
              icon: 'iconfont icon-informatiom'
            },
            {
              id: 3,
              systemItem: '专家服务',
              path: 'expert-service',
              icon: 'iconfont icon-zhuanjia'
            },
            {
              id: 4,
              systemItem: '农业问答',
              path: 'farming-question-answer',
              icon: 'iconfont icon-wenda'
            },
            {
              id: 5,
              systemItem: '农业视频管理',
              path: 'farming-admin-video',
              icon: 'iconfont icon-shipinbofangyingpian2'
            },
            {
              id: 6,
              systemItem: '轮播图管理',
              path: 'public-welfare-swiper',
              icon: 'iconfont icon-lunbotu'
            },
            {
              id: 7,
              systemItem: '用户管理',
              path: 'user-management',
              icon: 'iconfont icon-lunbotu'
            }
          ]
        },
        {
          id: 3,
          systemName: '农产品追溯信息系统',
          icon: 'iconfont icon-informatiom',
          children: [
            {
              id: 1,
              systemItem: '文章管理',
              path: 'admin-farm-article',
              icon: 'iconfont icon-wenzhang-copy'
            },
            {
              id: 2,
              systemItem: '特色农产品管理',
              path: 'special-agricultural-products',
              icon: 'iconfont icon-tesezhuanti'
            },
            {
              id: 3,
              systemItem: '企业名录管理',
              path: 'business/directory',
              icon: 'iconfont icon-qiye'
            },
            {
              id: 4,
              systemItem: '投诉建议管理',
              path: 'complaint-advice',
              icon: 'iconfont icon-tousuyujianyi'
            },
            {
              id: 5,
              systemItem: '轮播图管理',
              path: 'information-system-admin-swiper',
              icon: 'iconfont icon-lunbotu'
            }
          ]
        }
      ],
      //是否折叠
      isCollapse: false,
      //被激活的链接地址
      activePath: '/commerce-article'
    };
  },
  created() {
    this.getUserName();
    this.activePath = window.sessionStorage.getItem('activePath')
      ? window.sessionStorage.getItem('activePath')
      : this.activePath;
  },
  methods: {
    //获取用户名
    getUserName() {
      let userInfo = window.localStorage.getItem('userInfo');
      if(userInfo){
        this.userName = JSON.parse(userInfo).account;
      }else{
        this.$router.push({ path:'/'})
      }
      
    },
    // 退出登录
    logout() {
      this.$confirm('此操作将退出登录, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      })
        .then(() => {
          // 清除token
          window.localStorage.clear();
          // 跳转至登录界面
          this.$router.push('/login');
        })
        .catch(() => {
          this.$message({
            duration: 1000,
            type: 'info',
            message: '已取消操作'
          });
        });
    },
    //点击按钮，切换菜单的折叠与展开
    toggleCollapse() {
      this.isCollapse = !this.isCollapse;
    },
    //保存连接的激活状态
    saveNavState(path) {
      window.sessionStorage.setItem('activePath', path);
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
  .system-title {
    display: flex;
    align-items: center;
    h1 {
      margin-left: 15px;
    }
  }
  .system-user {
    display: flex;
    justify-content: center;
    align-items: center;
    .system-user-logo {
      display: flex;
      justify-content: start;
      align-items: center;
      width: 200px;
      img {
        width: 40px;
        height: 40px;
      }
      .user-name {
        margin-left: 10px;
      }
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
