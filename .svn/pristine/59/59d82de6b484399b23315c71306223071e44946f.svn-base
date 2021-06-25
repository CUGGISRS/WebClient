<template>
  <div id="index">
    <div class="title">
      <div class="title_a">
        <div class="title_b">
          <a href>返回</a>
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
    <!-- 登录  -->
    <div class="enter">
      <el-form :model="formData">
        <el-form-item  prop="userName">
          <el-input v-model="formData.userName" @keyup.enter.native="onSubmit"></el-input>
        </el-form-item>
        <el-form-item  prop="password">
          <el-input v-model="formData.password" show-password @keyup.enter.native="onSubmit"></el-input>
        </el-form-item>
        <el-checkbox v-model="checked" @change="keepChecked">记住用户名</el-checkbox>

        <el-form-item class="buttons">
          <el-button type="primary" @click="null" round>登录</el-button>
       
        </el-form-item>
       
      </el-form>
    </div>
    <!-- 頁面内容 -->
    <div class="home_content">
      <router-view />
    </div>
  </div>
</template>

<script>

export default {
    name: "login",
  props: {},
  data() {
    return {
      checked: false,
      formData: {
        userName: "admin",
        password: "123456",
      },
    
    };
  },
  computed: {},
  created() {},
  mounted() {},
  watch: {},
  methods: {
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
.enter {
  width: 858px;
  height: 223px;
display: flex;
  margin: 0 auto;
  .el-form {
    margin: 0 auto;
    width: 600px;
    color: red;
    padding: 50px 50px 30px;
    box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);

    .buttons {
      display: flex;
      flex-direction: row;
      justify-content: space-around;
    }
  }
  .el-checkbox {
    display: block;
    margin-top: 20px;
    display: flex;
    justify-content: center;
  }
}

.home_content {
  width: 1045px;
  margin: 0 auto;
  float: left;
}
</style>