<template>
  <el-container class="retrospect-home-container">
    <!-- 头部 -->
    <el-header>
      <el-menu default-active="/qualification"
        background-color="#333744"
        text-color="#fff"
        class="el-menu-vertical-demo"
        active-text-color="#409eff"
        unique-opened
        mode="horizontal"
        router>
        <!-- 基本配置 -->
        <el-submenu index="/qualification">
          <template slot="title">
            <span slot="title">基本配置</span>
          </template>
          <el-menu-item-group>
            <el-menu-item index="/qualification">企业资质管理</el-menu-item>
            <el-menu-item index="/retailer">销售商管理</el-menu-item>
            <el-menu-item index="/plantingbase">种植/养殖基地管理</el-menu-item>
            <el-menu-item index="/productionbase">生产基地管理</el-menu-item>
          </el-menu-item-group>
        </el-submenu>
        <!-- 产品管理 -->
        <el-menu-item index="/product">
          <span slot="title">产品管理</span>
        </el-menu-item>
        <!-- 种植/养殖管理 -->
        <el-submenu index="/basicoperation">
          <template slot="title">
            <span slot="title">种植/养殖管理</span>
          </template>
          <el-menu-item-group>
            <el-menu-item index="/basicoperation">基本作业管理</el-menu-item>
            <el-menu-item index="/growingenviroment">生长环境管理</el-menu-item>
          </el-menu-item-group>
        </el-submenu>
        <!-- 生产管理 -->
        <el-submenu index="/harvest">
          <template slot="title">
            <span slot="title">生产管理</span>
          </template>
          <el-menu-item-group>
            <el-menu-item index="/harvest">采收/捕捞管理</el-menu-item>
            <el-menu-item index="/buy">收购管理</el-menu-item>
            <el-menu-item index="/processbatch">加工批次管理</el-menu-item>
            <el-menu-item index="/inspection">检验报告</el-menu-item>
          </el-menu-item-group>
        </el-submenu>
        <!-- 销售管理 -->
        <el-menu-item index="/order">
          <span slot="title">销售管理</span>
        </el-menu-item>
        <!-- 追溯管理 -->
        <el-menu-item index="/retrospect">
          <span slot="title">追溯管理</span>
        </el-menu-item>
        <!-- 统计详情 -->
        <el-menu-item index="/retrospect-statistics">
          <span slot="title">统计详情</span>
        </el-menu-item>
        <!-- 企业基本信息 -->
        <el-menu-item index="/user">
          <span slot="title">企业基本信息</span>
        </el-menu-item>
      </el-menu>
      <span class="el-icon-close" @click="returnBack"></span>
    </el-header>
    <!-- 主界面 -->
    <el-main>
      <router-view></router-view>
    </el-main>
  </el-container>
</template>

<script>
export default {
  created() {
    // 页面刷新时，显示为第一个页面
    window.onload = () => {
      this.$router.push('/retrospect-home');
    }
  },
  methods: {
    returnBack() {
      this.$router.push('/retrospect-supervise');
    }
  }
}
</script>

<style scoped lang="less">
  // 页面容器
  .retrospect-home-container {
    width: 100%;
    height: 100%;
  }

  // 顶部样式
  .el-header {
    width: 100%;
    padding: 0;
    position: relative;

    .el-menu {
      border-right: none;
    }

    span {
      font-size: 16px;
    }

    .el-menu-item {
      font-size: 14px;
    }

    .el-icon-close {
      position: absolute;
      right: 20px;
      top: 50%;
      transform: translateY(-50%);
      color: #fff;
      font-size: 24px;
      cursor: pointer;
    }
  }

  // 主界面
  .el-main{
    background-color: #eaedf1;
  }
</style>