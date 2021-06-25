<template>
  <div id="index">
    <div class="title">
      <div class="title_a">
        <div class="title_b">
          <a href>登陆</a>
          <a href>注册 |</a>
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
    <!-- 导航 -->
    <div class="nav">
      <el-menu
          :default-active="activeIndex"
        class="el-menu-demo"
        mode="horizontal"
        background-color="#34C82C"
        text-color="#fff"
        @select="handleSelect"
        router
      >
        <el-menu-item index="/home/index">首页</el-menu-item>
        <el-menu-item index="/home/newslist">农业政策</el-menu-item>
        <el-menu-item index="/home/bazaar">市场动态</el-menu-item>
        <el-menu-item index="/home/plant">农业百科</el-menu-item>
        <el-menu-item index="/home/index">益农服务</el-menu-item>
        <el-menu-item index="/home/index">培训体验</el-menu-item>
      </el-menu>
      <div class="searchBox">
        <input class="search" type="text" name placeholder="请输入关键字" />
        <button class="searchBth">搜索</button>
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
  props: {},
  data() {
    return {
      activeIndex: "1",
      activeIndex2: "1",
    };
  },
  computed: {},
  created() {},
  mounted() {},
  watch: {},
  methods: {
    handleSelect(key, keyPath) {
      console.log(key, keyPath);
    },
  },
  components: {},
};
</script>

<style scoped lang="less">
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
// .nav > ul {
//   width: 1045px;
//   height: 50px;
//   margin: 0 auto;
//   background-color: #457a10;
// }
// .nav > ul > li {
//   width: 142px;
//   height: 50px;
//   list-style: none;
//   display: flex;
//   float: left;
//   justify-content: center;
//   align-items: center;
// }
.nav {
  display: flex;
  width: 1045px;
  margin: 0 auto;
  height: 50px;
  background-color: #16ad39;
  font-size: 18px;

  .el-menu {
    flex: 4;
    background-color: #16ad39;
    display: flex;
    color: aliceblue;

    > li {
      width: 142px;
      height: 50px;
      list-style: none;
      display: flex;
      float: left;
      justify-content: center;
      align-items: center;
    }
  }
  .searchBox {
    flex: 1;
    display: flex;
    align-items: center;
    input,
    button {
      height: 31px;
      width: 142px;
      box-sizing: border-box;
    }
    button {
      width: 45px;
      background-color: #00ffff;
    }
  }
}
.home_content {
  width: 1045px;
  margin: 0 auto;
}
</style>