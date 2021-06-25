<template>
  <div class="login_container">
    <div class="login_box">
      <!-- 标题区域 -->
      <div class="login_top">
        <div>监利市农产品质量安全追溯应用系统</div>
        <div>(种植企业版)</div>
      </div>
      <!-- 登录表单区域 -->
      <el-form :model="loginForm" ref="loginFormRef" label-width="0" :rules="loginFormRules" class="login_form">
        <!-- 用户名 -->
        <el-form-item prop="username">
          <span class="login_head_text">用户名:</span>
          <el-input v-model="loginForm.username" prefix-icon="el-icon-user" placeholder="请输入用户名"></el-input>
        </el-form-item>
        <!-- 密码 -->
        <el-form-item prop="password">
          <span class="login_head_text">密 码:</span>
          <el-input
            v-model="loginForm.password"
            prefix-icon="el-icon-lock"
            show-password
            placeholder="请输入密码"
          ></el-input>
        </el-form-item>
        <!-- 按钮 -->
        <span>
          <el-button type="primary" @click="login">登 录</el-button>
          <el-button type="primary" @click="register">注 册</el-button>
        </span>
      </el-form>
    </div>
    <div class="login_footer">
      <span>
        <span v-html="foot1"></span>
        <a
          target="_blank"
          href="http://www.jianli.gov.cn/"
          style="display:inline-block;text-decoration:none;height:20px;line-height:20px;"
        >
          <p>监利市农业农村局</p>
        </a>
        <span v-html="foot2"></span>
        <a
          target="_blank"
          href="http://www.zd-tech.net/"
          style="display:inline-block;text-decoration:none;height:20px;line-height:20px;"
        >
          <p>武汉中迪联创科技有限公司</p>
        </a>
        <span v-html="foot3"></span>
        <br />
        <p style="font-size: 12px;">
          地址：湖北监利市江城路69号 &nbsp; &nbsp; &nbsp; 电话号码：0716-3300339
        </p>
      </span>
    </div>
  </div>
</template>

<script>
export default {
  data() {
    return {
      foot1: 'Copyright  &copy; &nbsp;',
      foot2: '&nbsp;版权所有&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;',
      foot3: '&nbsp;技术支持',
      loginForm: {},
      loginFormRules: {
        // 验证用户名
        username: [
          { required: true, message: '请输入用户名', trigger: 'blur' },
          { min: 4, max: 18, message: '长度在 4 到 18个字符', trigger: 'blur' },
        ],
        // 验证密码
        password: [
          { required: true, message: '请输入密码', trigger: 'blur' },
          { min: 6, max: 16, message: '长度在 6 到 16 个字符', trigger: 'blur' },
        ],
      },
    };
  },
  created() {
    let that = this;
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
    // 登录验证
    login() {
      this.loginForm.sysName = 'X2';
      this.$refs.loginFormRef.validate(async (valid) => {
        if (!valid) return;
        const { data: res } = await this.$http.post('comSys/login/user', this.loginForm);
        if (res.status !== 200) return this.$message.error(res.message);
        this.$message({
          duration: 1000,
          type: 'success',
          message: '登录成功！',
        });
        await window.sessionStorage.setItem('retrospectToken', res.data);
        await this.getList();
        this.$router.push('/home');
      });
    },
    // 获取当前用户信息
    async getList() {
      const { data: res } = await this.$http.get('comSys/getCurrentUser');
      if (res.status !== 200) return this.$message.error(res.message);
      await window.sessionStorage.setItem('enterpriseId', res.data.enterpriseId);
      await window.sessionStorage.setItem('userName', res.data.username);
    },
    // 注册页面跳转
    register() {
      this.$router.push('/register');
    },
  },
};
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped lang="less">
// 容器样式
.login_container {
  width: 100%;
  height: 100%;
  background: url('../assets/img/login_bj.jpg') no-repeat fixed center;
  background-size: 100% 100%;
}

// 登录框样式
.login_box {
  width: 600px;
  height: 400px;
  position: absolute;
  left: 50%;
  top: 50%;
  transform: translate(-50%, -50%);
  background-color: #284570;
  border-radius: 20px;
}

// 登录框顶部样式
.login_top {
  width: 100%;
  height: 100px;
  color: #fff;
  text-align: center;
  position: relative;
  background-color: #00a0ff;
  border-top-left-radius: 20px;
  border-top-right-radius: 20px;

  div:nth-child(1) {
    width: 100%;
    font-size: 32px;
    position: absolute;
    left: 0;
    top: 15px;
  }
  div:nth-child(2) {
    width: 100%;
    font-size: 20px;
    position: absolute;
    left: 0;
    bottom: 15px;
  }
}

// 登录框表单样式
.login_form {
  width: 400px;
  box-sizing: border-box;
  position: absolute;
  left: 50%;
  top: 140px;
  transform: translateX(-50%);

  // 输入框label
  .login_head_text {
    display: inline-block;
    width: 60px;
    margin-right: 10px;
    font-size: 18px;
    color: #fff;
    text-align: right;
  }

  // 输入框样式
  .el-input {
    width: 330px;
    margin: 10px auto;
    font-size: 18px;
  }

  // 按钮样式
  .el-button {
    width: 180px;
    height: 40px;
    margin-top: 10px;
  }

  .el-button:nth-child(1) {
    margin-right: 30px;
  }
}

// 页面底部提示文字样式
.login_footer {
  position: absolute;
  bottom: 0;
  left: 0;
  //height: 30px;
  border-top: 1px solid #fff;
  border-radius: 0 0 5px 5px;
  background: #f7f7f7;
  clear: both;
  width: 100%;
  margin: 0 auto;
  text-align: center;
  line-height: 30px;
  color: #808080;
}

.login_form {
  /* 输入验证错误提示文字位置 */
  .el-form-item__error {
    left: 70px;
  }
}
</style>
