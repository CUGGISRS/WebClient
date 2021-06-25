<template >
  <div class="login-container">
    <div class="login-box">
      <div class="login-title">监利市产业园三维平台后台管理系统</div>
      <!-- 登录表单区域 -->
      <el-form
        ref="loginFormRef"
        :model="loginForm"
        :rules="loginFormRules"
        label-width="0px"
        class="login-form"
      >
        <!-- 用户名 -->
        <el-form-item prop="username">
          <el-input
            v-model="loginForm.username"
            prefix-icon="el-icon-user"
            placeholder="请输入用户名"
          ></el-input>
        </el-form-item>
        <!-- 密码 -->
        <el-form-item prop="password">
          <el-input
            v-model="loginForm.password"
            prefix-icon="el-icon-lock"
            type="password"
            placeholder="请输入密码"
          ></el-input>
        </el-form-item>
        <!-- 按钮区域 -->
        <el-form-item class="btns">
          <el-button type="primary" @click="login">登录</el-button>
          <el-button type="info" @click="resetLoginForm">清空</el-button>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>

<script>
  import DataList from "@/globals/service/dataList.js";
  import DataStore from "@/globals/storage/index.js";
export default {
  data() {
    return {
      //登录表单数据绑定对象
      loginForm: {username:"",password:""},
      loginFormRules: {
        // 验证用户名
        username: [
          { required: true, message: '请输入用户名', trigger: 'blur' }
        ],
        // 验证密码
        password: [
          { required: true, message: '请输入密码', trigger: 'blur' }
       //   { min: 6, max: 16, message: '长度在 6 到 16 个字符', trigger: 'blur' }
        ],
      }
    };
  },
  created() {
    var that=this;
    document.onkeydown = function(e) {
      e = window.event || e;
      //验证在登录界面和按得键是回车键enter
      if (that.$route.path == '/login' && (e.keyCode == 13 || e.which == 13)) {
        //登录函数
        that.login();
      }
    };

  },
  methods: {
    //点击重置登录表单
    resetLoginForm() {
      this.$refs.loginFormRef.resetFields();
    },
    // 登录验证
    login() {
      this.$refs.loginFormRef.validate(async (valid) => {
        if (!valid) return;

        DataList.login(this.loginForm).then(res => {
          if (res.status == 200) {
            this.$message.success("登录成功！");

            DataStore.setToken(res.data);
            DataStore.setUsername( this.loginForm.username);
            this.$router.push('/home');
          }else {
            this.$message.error(res.message);
          }
        });


      });
    }
  }
};
</script>

<style lang="less" scoped>
.login-container {
  background: url("../assets/earth.jpg")no-repeat;
  background-size:100% 100%;
  height: 100%;
  .login-box {
    width: 550px;
    height: 360px;
    background-color:#FFFFFF;
    border-radius: 5px;
    position: absolute;
    left: 50%;
    top: 50%;
    transform: translate(-50%, -50%);

    .login-title {
      position: absolute;
      width: 550px;
      height: 50px;
      line-height: 50px;
      font-size: 34px;
      font-weight: 600;
      color: #fff;
      text-align: center;
      top: -80px;
      left: 0;
    }
  }

  .login-form {
    margin-top: 100px;
    padding: 0 30px;
  }

  .btns {
    display: flex;
    justify-content: flex-end;
  }
}
</style>
