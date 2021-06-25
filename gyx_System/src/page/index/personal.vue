<template>
  <div id="index">
    <div class="title">
      <div class="title_a">
        <div class="title_b">
          <a href>返回首页</a>
        </div>
      </div>
    </div>
    <!-- logo -->
    <div class="summary">
      <div class="logo">logo</div>
      <div class="summarylogo">
        <h1>监利市农业公益服务系统</h1>
      </div>
    </div>
    <!-- 个人中心  -->
    <div class="userinfo">
      <div class="user">
        <el-col>
          <el-menu
            default-active="2"
            unique-opened="false"
            class="el-menu-vertical-demo"
            @open="handleOpen"
            @close="handleClose"
          >
            <el-submenu index="1">
              <template slot="title">
                <i class="el-icon-location"></i>
                <span>用户信息</span>
              </template>
              <el-menu-item-group>
                <el-menu-item index="1-1">基本信息</el-menu-item>
                <el-menu-item index="1-2">修改资料</el-menu-item>
                <el-menu-item index="1-3">更换头像</el-menu-item>
                <el-menu-item index="1-4">修改密码</el-menu-item>
                <el-menu-item index="1-5">实名认证</el-menu-item>
              </el-menu-item-group>
            </el-submenu>
            <el-submenu index="2">
              <template slot="title">
                <i class="el-icon-location"></i>
                <span>我的咨询</span>
              </template>
              <el-menu-item-group>
                <el-menu-item index="2-1">1</el-menu-item>
                <el-menu-item index="2-2">2</el-menu-item>
              </el-menu-item-group>
            </el-submenu>
            <el-submenu index="3">
              <template slot="title">
                <i class="el-icon-location"></i>
                <span>我的供求</span>
              </template>
              <el-menu-item-group>
                <el-menu-item index="3-1">1</el-menu-item>
                <el-menu-item index="3-2">2</el-menu-item>
              </el-menu-item-group>
            </el-submenu>
          </el-menu>
        </el-col>
      </div>
      <div class="info">
        <div class="info_title">
          <span>当前位置:{{item}}</span>
        </div>
        <div class="info_content">
          <div class="infoLeft">
            <ul>
              <li v-for="(val,key) in userInforCN" :key="key">
                <span>{{val}}:</span>
                <span>{{userInfor[key]}}</span>
              </li>
            </ul>
          </div>
          <div class="infoRight">
            <img src="../../assets/image/new.jpg" alt />
          </div>
        </div>
      </div>
    </div>

    <!-- 頁面内容 -->
    <div class="home_content">
      <router-view />
    </div>
  </div>
</template>

<script>
export default {
  name: "personal",
  props: {},
  data() {
    return {
      userInfor: {
        userType: "个人用户",
        sex: "保密",
        qq: "",
        email: "",
        userName: "1532037346",
        phone: "13667217563",
        wechat: "",
        istrue: "否",
      },
      userInforCN: {
        userType: "用户类型",
        sex: "性别",
        qq: "QQ",
        email: "邮箱",
        userName: "用户名",
        phone: "手机号码",
        wechat: "微信",
        istrue: "是否认证",
      },
    };
  },
  computed: {},
  created() {},
  mounted() {},
  watch: {},
  methods: {
    handleOpen(key, keyPath) {
      console.log(key, keyPath);
    },
    handleClose(key, keyPath) {
      console.log(key, keyPath);
    },
  },
  components: {},
};
</script>

<style scoped >
#index {
  width: 100%;
  height: 1000px;
  margin: 0 auto;
  background-image: url(../../assets/image/beijing.jpg);
}
.title {
  width: 100%;
  height: 40px;
  background-color: #333333;
}
.title_a {
  width: 75%;
  height: 40px;
}
.title_a > .title_b {
  display: flex;
  flex-direction: row-reverse;
}
.title_a > .title_b > a {
  color: aliceblue;
  line-height: 40px;
}
.summary {
  width: 50rem;
  background-color: antiquewhite;
  height: 110px;
  margin: 50px auto;
  display: flex;
}
.summary > .logo {
  width: 123px;
  height: 110px;
  background-color: blue;
}
.summary > .summarylogo {
  height: 110px;
  width: 605px;
  display: flex;
  align-items: center;
  justify-content: center;
}
</style>
<style scoped lang="less">
.userinfo {
  height: 536px;
  width: 1024px;
  margin: 0 auto;
  display: flex;
  .user {
    height: 100%;
    flex: 2;
    margin-right: 10px;
    background-color: aliceblue;
  }
  .info {
    flex: 7;
    background-color: aliceblue;
    .info_title {
      height: 40px;
      background-color: red;
      align-content: center;
      line-height: 40px;
    }
    .info_content {
      display: flex;
      height: 496px;
      width: 100%;
      //   background-color: navy;
      .infoLeft {
        flex: 7;
        // background-color: blue;
        text-align: center;
       
      }
      .infoRight {
        flex: 3;
        display: table-cell;
        vertical-align: middle;
        text-align: center;
        > img {
          margin-top: 20px;
          width: 150px;
          height: 150px;
          align-content: center;
        }
      }
    }
  }
}

.home_content {
  width: 1045px;
  margin: 0 auto;
  float: left;
}
</style>