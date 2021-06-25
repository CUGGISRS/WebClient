import Vue from "vue";
import VueRouter from "vue-router";

// 登录
const Login = () => import(/* webpackChunkName: "about" */ "../components/Login.vue");
// 首页
const Home = () => import(/* webpackChunkName: "about" */ "../components/Home.vue");
// 设备监控
const EquipmentMonitoring = () => import(/* webpackChunkName: "about" */ "../views/equipmentMonitoring/EquipmentMonitoring.vue");
// 数据分析
const DataSearch = () => import(/* webpackChunkName: "about" */ "../views/dataSearch/DataSearch.vue");
// 数据分析
const DataAnalysis = () => import(/* webpackChunkName: "about" */ "../views/dataAnalysis/DataAnalysis.vue");
// 设备管理
const DeviceGroup = () => import(/* webpackChunkName: "about" */ "../views/deviceGroup/DeviceGroup.vue");
// 报警信息
const WarningInfo = () => import(/* webpackChunkName: "about" */ "../views/warningInfo/WarningInfo.vue");

Vue.use(VueRouter);

const routes = [
  {
    path: '/',
    redirect: '/login'
  },
  {
    path: '/login',
    name: 'Login',
    component: Login
  },
  {
    path: '/home',
    name: 'Home',
    component: Home,
    redirect: '/equipment-monitoring',
    children: [
      { path: '/equipment-monitoring', component: EquipmentMonitoring },
      { path: '/data-search', component: DataSearch },
      { path: '/data-analysis', component: DataAnalysis },
      { path: '/warning-info', component: WarningInfo },
      { path: '/device-group', component: DeviceGroup }
    ]
  }
];

const router = new VueRouter({
  routes
});

// 挂载路由导航守卫
router.beforeEach((to, from, next) => {
  // to 将要访问的路径
  // from 代表从哪个路径跳转而来
  // next 是一个函数，表示放行
  // next() 放行  next('/login') 强制跳转
  if (to.path === '/login') return next()
  //  获取 token
  const tokenStr = window.sessionStorage.getItem('retrospectToken');
  if (!tokenStr) return next('/login')
  next();
});

export default router;
