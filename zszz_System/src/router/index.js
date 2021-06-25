import Vue from "vue";
import VueRouter from "vue-router";

// 登录
const Login = () => import( /* webpackChunkName: "about" */ "../components/Login.vue");
// 注册
const Register = () => import( /* webpackChunkName: "about" */ "../components/Register.vue");
// 首页
const Home = () => import( /* webpackChunkName: "about" */ "../components/Home.vue");
// 基本配置
const Qualification = () => import( /* webpackChunkName: "about" */ "../components/bases/Qualification.vue");
const Retailer = () => import( /* webpackChunkName: "about" */ "../components/bases/Retailer.vue");
const Supply = () => import( /* webpackChunkName: "about" */ "@/components/bases/Supply.vue");
const PlantingBase = () => import( /* webpackChunkName: "about" */ "../components/bases/PlantingBase.vue");
const ProductionBase = () => import( /* webpackChunkName: "about" */ "../components/bases/ProductionBase.vue");
// 产品管理
const Product = () => import( /* webpackChunkName: "about" */ "../components/products/Product.vue");
// 种植管理
const BasicOperation = () => import( /* webpackChunkName: "about" */ "../components/plants/BasicOperation.vue");
const GrowingEnviroment = () => import( /* webpackChunkName: "about" */ "../components/plants/GrowingEnviroment.vue");
// 生产管理
const Harvest = () => import( /* webpackChunkName: "about" */ "../components/productions/Harvest.vue");
const Buy = () => import( /* webpackChunkName: "about" */ "../components/productions/Buy.vue");
const ProcessBatch = () => import( /* webpackChunkName: "about" */ "../components/productions/ProcessBatch.vue");
const Inspection = () => import( /* webpackChunkName: "about" */ "../components/productions/Inspection.vue");
// 销售管理
const Order = () => import( /* webpackChunkName: "about" */ "../components/orders/Order.vue");
// 追溯管理
const Retrospect = () => import( /* webpackChunkName: "about" */ "../components/retrospects/Retrospect.vue");
// 统计详情
const Statistics = () => import( /* webpackChunkName: "about" */ "../components/statistics/Statistics.vue");
// 企业基本信息
const User = () => import( /* webpackChunkName: "about" */ "../components/users/User.vue");

Vue.use(VueRouter);

const routes = [{
    path: '/',
    redirect: '/login'
  },
  {
    path: '/login',
    name: 'Login',
    component: Login
  },
  {
    path: '/register',
    name: 'Register',
    component: Register
  },
  {
    path: '/home',
    name: 'Home',
    component: Home,
    redirect: '/user',
    children: [{
        path: '/qualification',
        component: Qualification
      },
      {
        path: '/retailer',
        component: Retailer
      },
      {
        path: '/supply',
        component: Supply
      },
      {
        path: '/plantingbase',
        component: PlantingBase
      },
      {
        path: '/productionbase',
        component: ProductionBase
      },
      {
        path: '/product',
        component: Product
      },
      {
        path: '/basicoperation',
        component: BasicOperation
      },
      {
        path: '/growingenviroment',
        component: GrowingEnviroment
      },
      {
        path: '/harvest',
        component: Harvest
      },
      {
        path: '/buy',
        component: Buy
      },
      {
        path: '/processbatch',
        component: ProcessBatch
      },
      {
        path: '/inspection',
        component: Inspection
      },
      {
        path: '/order',
        component: Order
      },
      {
        path: '/retrospect',
        component: Retrospect
      },
      {
        path: '/statistics',
        component: Statistics
      },
      {
        path: '/user',
        component: User
      }
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
  if (to.path === '/register') return next()
  //  获取 token
  const tokenStr = window.sessionStorage.getItem('retrospectToken');
  if (!tokenStr) return next('/login')
  next();
});

export default router;