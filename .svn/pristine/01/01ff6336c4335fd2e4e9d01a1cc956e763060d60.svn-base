<template>
  <div class="login_container">
    <div class="login_box">
      <div class="login_box_container">
        <!-- 标题区域 -->
        <div class="login_top">
          <span>{{$sc.system_name}}</span>
        </div>
        <!-- 登录表单区域 -->
        <el-form :model="loginForm" class="login_form">
          <!-- 用户名 -->
          <el-form-item>
            <el-input v-model="loginForm.username" prefix-icon="el-icon-user" placeholder="请输入用户名"></el-input>
          </el-form-item>
          <!-- 密码 -->
          <el-form-item prop="password">
            <el-input v-model="loginForm.password" prefix-icon="el-icon-lock" show-password
                      placeholder="请输入密码"></el-input>
          </el-form-item>
          <!-- 按钮 -->
          <span>
            <el-button type="primary" @click="login">登 录</el-button>
          </span>
        </el-form>
      </div>
    </div>
    <div class="login_footer">
        <span>
          <span v-html="foot1"></span>
          <a target="_blank" href="http://www.jianli.gov.cn/"
             style="display:inline-block;text-decoration:none;height:20px;line-height:20px;">
            <p>监利市农业农村局 </p>
          </a>
          <span v-html="foot2"></span>
          <a target="_blank" href="http://www.zd-tech.net/"
             style="display:inline-block;text-decoration:none;height:20px;line-height:20px;">
            <p>武汉中迪联创科技有限公司 </p>
          </a>
          <span v-html="foot3"></span>
          <br/>
          <p style="font-size: 12px;">
            地址：湖北监利市江城路69号  &nbsp;&nbsp;&nbsp;  电话号码：0716－ 3300339
          </p>
          <!--            <p style="font-size: 12px;">-->
          <!--              <a style="color: rgb(128, 128, 128); text-decoration: none; display: inline-block;"-->
          <!--                 href="https://beian.miit.gov.cn/#/Integrated/index" target="_blank">-->
          <!--              备案序号：浙ICP备16035102号-2-->
          <!--                &lt;!&ndash;<p style="color:#939393;font-size: 14px"><img style="position: relative;top: 4px" src="./img/备案图标.png">浙公网安备 33060202000302号</p>&ndash;&gt;-->
          <!--              </a>-->
          <!--            </p>-->
        </span>
    </div>
  </div>
</template>

<script>
export default {
  data() {
    return {
      loginForm: {},
      foot1: 'Copyright  &copy; &nbsp;',
      foot2: '&nbsp;版权所有&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;',
      foot3: '&nbsp;技术支持'
    }
  },
  created() {
    let that = this;
    document.onkeydown = function (e) {
      e = window.event || e;
      //验证在登录界面和按得键是回车键enter
      if (that.$route.path === '/login' && (e.keyCode === 13 || e.which === 13)) {
        //登录函数
        that.login();
      }
    }
  },
  methods: {
    // 登录
    async login() {
      this.loginForm.sysName = 'X2';
      const {data: res} = await this.$http.post('comSys/login/user', this.loginForm);
      if (res.status !== 200) return this.$message.error(res.message);
      await window.sessionStorage.setItem('retrospectToken', res.data);
      await this.getList();
    },
    // 获取当前用户信息
    async getList() {
      const {data: res} = await this.$http.get('comSys/getCurrentUser');
      window.sessionStorage.setItem('enterpriseName', res.data.name);
      if (res.status !== 200) return this.$message.error(res.message);
      window.sessionStorage.setItem('enterpriseId', res.data.enterpriseId);
      if (res.data.isViewpoint !== 0) {
        this.$message({
          duration: 1000,
          type: 'success',
          message: '登录成功！'
        });
        this.$router.push('/home');
      } else {
        this.$message({
          duration: 1000,
          type: 'error',
          message: '未关联物联网账号，请先关联物联网账号！'
        });
      }
    }
  }
};
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped lang="less">
// 容器样式
.login_container {
  width: 100%;
  height: 100%;
  background-color: #2d3a4b;
  background-image: url("../assets/img/bgimage.jpg");
  background-size: 100% 100%;
}

// 登录框样式
.login_box {
  width: 100%;
  height: 100%;
  position: relative;

  .login_box_container {
    width: 500px;
    height: 400px;
    position: absolute;
    left: 50%;
    top: 25%;
    transform: translateX(-50%);
    background: rgba(125, 125, 125, 0.5);
    border-radius: 25px;
    padding: 0 35px;
  }
}

// 登录框顶部样式
.login_top {
  width: 100%;
  height: 100px;
  color: #eee;
  font-size: 32px;
  font-weight: 600;
  text-align: center;
  line-height: 100px;
}

// 登录框表单样式
.login_form {
  width: 100%;
  box-sizing: border-box;

  // 输入框样式
  .el-input {
    width: 100%;
    margin: 10px auto;
  }

  // 按钮样式
  .el-button {
    width: 100%;
    height: 48px;
    font-size: 18px;
    margin-top: 10px;
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

// 登录输入框样式
.login_form .el-input .el-input__inner {
  background-color: transparent;
  border: 2px solid hsla(0, 0%, 100%, .1);
  height: 48px;
  color: #ddd;
  caret-color: #ddd;
}

.login_form .el-input .el-input__prefix {
  line-height: 48px;
}
</style>
