import Vue from 'vue';
import Router from 'vue-router';
/* layout */
import Layout from '../views/layout/Layout';

const _import = require('./_import_' + process.env.NODE_ENV);
// in development env not use Lazy Loading,because Lazy Loading large page will cause webpack hot update too slow.so only in production use Lazy Loading

Vue.use(Router);

/**
 * icon : the icon show in the sidebar
 * hidden : if `hidden:true` will not show in the sidebar
 * redirect : if `redirect:noredirect` will no redirct in the levelbar
 * noDropdown : if `noDropdown:true` will has no submenu
 * meta : { role: ['admin'] }  will control the page role
 **/
export const constantRouterMap = [{
  path: '/login',
  component: _import('login/index'),
  hidden: true
},
{
  path: '/authredirect',
  component: _import('login/authredirect'),
  hidden: true
},
{
  path: '/404',
  component: _import('error/404'),
  hidden: true
},
{
  path: '/401',
  component: _import('error/401'),
  hidden: true
},
{
  path: '/',
  component: Layout,
  redirect: '/dashboard',
  name: '首页',
  hidden: true,
  children: [{
    path: 'dashboard',
    component: _import('dashboard/index')
  }]
},
{
  path: '/introduction',
  component: Layout,
  redirect: '/introduction/index',
  icon: 'form',
  noDropdown: true,
  children: [{
    path: 'index',
    component: _import('introduction/index'),
    name: '简述'
  }]
}
];

export default new Router({
  // mode: 'history', //后端支持可开
  scrollBehavior: () => ({
    y: 0
  }),
  routes: constantRouterMap
});

export const asyncRouterMap = [{
  path: '/baseManager',
  component: Layout,
  name: '基础配置管理',
  icon: 'setting',
  authority: 'baseManager',
  children: [{
    path: 'userManager',
    icon: 'fa-user',
    component: _import('admin/user/index'),
    name: '用户管理',
    authority: 'userManager'
  }, {
    path: 'menuManager',
    icon: 'category',
    component: _import('admin/menu/index'),
    name: '菜单管理',
    authority: 'menuManager'
  }, {
    path: 'groupManager',
    icon: 'group_fill',
    component: _import('admin/group/index'),
    name: '角色权限管理',
    authority: 'groupManager'
  }, {
    path: 'groupTypeManager',
    icon: 'fa-users',
    component: _import('admin/groupType/index'),
    name: '角色类型管理',
    authority: 'groupTypeManager'
  }, {
    path: 'gateLogManager',
    icon: 'viewlist',
    component: _import('admin/gateLog/index'),
    name: '操作日志管理',
    authority: 'gateLogManager'
  }
  ]
},
{
  path: '/authManager',
  component: Layout,
  name: '基础配置管理',
  icon: 'setting',
  authority: 'authManager',
  children: [{
    path: 'serviceManager',
    component: _import('auth/service/index'),
    name: '用户管理',
    authority: 'serviceManager'
  }]
},
{
  path: '/monitorManager',
  component: Layout,
  name: '监控模块管理',
  icon: 'setting',
  authority: 'monitorManager',
  children: [{
    path: 'serviceEurekaManager',
    component: _import('monitor/eureka/index'),
    name: 'Eureka注册中心',
    authority: 'serviceEurekaManager'
  }, {
    path: 'serviceMonitorManager',
    component: _import('monitor/service/index'),
    name: '服务状态监控',
    authority: 'serviceMonitorManager'
  }, {
    path: 'serviceZipkinManager',
    component: _import('monitor/zipkin/index'),
    name: '服务状态监控',
    authority: 'serviceZipkinManager'
  }]
},
{
  path: '/example',
  component: Layout,
  name: '文章管理',
  icon: 'example',
  authority: 'example',
  children: [{
    path: 'CreateArticle',
    component: _import('example/create'),
    name: '创建文章',
    icon: 'edit',
    authority: 'CreateArticle'
  }, {
    path: 'EditArticle/:id(\\d+)',
    component: _import('example/edit'),
    name: '编辑文章',
    authority: 'EditArticle',
    noCache: true,
    activeMenu: '/example/list',
    hidden: true
  }, {
    path: 'ArticleList',
    component: _import('example/list'),
    name: '文章列表',
    authority: 'ArticleList',
    icon: 'list'
  }]
},
{
  path: '/expertManagement',
  component: Layout,
  name: '专家管理',
  icon: 'null',
  authority: 'expertManagement',
  children: [{
    path: 'expert',
    component: _import('expert/index'),
    name: '专家列表',
    icon: 'null',
    authority: 'expert'
  }, {
    path: 'duty',
    component: _import('expert/duty'),
    name: '专家排班',
    icon: 'null',
    authority: 'duty'
  }]
},
{
  path: '/videoManagement',
  component: Layout,
  name: '视频管理',
  icon: 'null',
  authority: 'videoManagement',
  children: [{
    path: 'uploadvideo',
    component: _import('video/index'),
    name: '农技视频上传',
    icon: 'null',
    authority: 'uploadvideo'
  }]
}
];
