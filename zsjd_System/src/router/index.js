import Vue from "vue";
import VueRouter from "vue-router";

// 登录
const Login = () => import( /* webpackChunkName: "about" */ "../components/Login.vue");
// 首页
const Home = () => import( /* webpackChunkName: "about" */ "../components/Home.vue");
// 企业信息审核
const RegistrationAudit = () => import( /* webpackChunkName: "about" */ "../components/informationAudit/RegistrationAudit.vue");
// 溯源信息监督
const RetrospectSupervise = () => import( /* webpackChunkName: "about" */ "../components/retrospectSupervise/RetrospectSupervise.vue");
const RetrospectHome = () => import( /* webpackChunkName: "about" */ "../components/retrospectSupervise/RetrospectHome.vue");
const CreditRating = () => import( /* webpackChunkName: "about" */ "../components/creditRating/CreditRating.vue");
// 检验管理
const Inspect = () => import( /* webpackChunkName: "about" */ "../components/inspect/Inspect.vue");
// 统计分析
const Statistics = () => import( /* webpackChunkName: "about" */ "../components/statistics/Statistics.vue");
// 安全预警
const Forewarning = () => import( /* webpackChunkName: "about" */ "../components/forewarning/Forewarning.vue");

// 基本配置
const Qualification = () => import( /* webpackChunkName: "about" */ "../components/retrospectSupervise/bases/Qualification.vue");
const Retailer = () => import( /* webpackChunkName: "about" */ "../components/retrospectSupervise/bases/Retailer.vue");
const PlantingBase = () => import( /* webpackChunkName: "about" */ "../components/retrospectSupervise/bases/PlantingBase.vue");
const ProductionBase = () => import( /* webpackChunkName: "about" */ "../components/retrospectSupervise/bases/ProductionBase.vue");
// 产品管理
const Product = () => import( /* webpackChunkName: "about" */ "../components/retrospectSupervise/products/Product.vue");
// 种植/养殖管理
const BasicOperation = () => import( /* webpackChunkName: "about" */ "../components/retrospectSupervise/plants/BasicOperation.vue");
const GrowingEnviroment = () => import( /* webpackChunkName: "about" */ "../components/retrospectSupervise/plants/GrowingEnviroment.vue");
// 生产管理
const Harvest = () => import( /* webpackChunkName: "about" */ "../components/retrospectSupervise/productions/Harvest.vue");
const Buy = () => import( /* webpackChunkName: "about" */ "../components/retrospectSupervise/productions/Buy.vue");
const ProcessBatch = () => import( /* webpackChunkName: "about" */ "../components/retrospectSupervise/productions/ProcessBatch.vue");
const Inspection = () => import( /* webpackChunkName: "about" */ "../components/retrospectSupervise/productions/Inspection.vue");
// 销售管理
const Order = () => import( /* webpackChunkName: "about" */ "../components/retrospectSupervise/orders/Order.vue");
// 追溯管理
const Retrospect = () => import( /* webpackChunkName: "about" */ "../components/retrospectSupervise/retrospects/Retrospect.vue");
// 统计详情
const RetrospectStatistics = () => import( /* webpackChunkName: "about" */ "../components/retrospectSupervise/statistics/Statistics.vue");
// 企业基本信息
const User = () => import( /* webpackChunkName: "about" */ "../components/retrospectSupervise/users/User.vue");

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
    path: '/home',
    name: 'Home',
    component: Home,
    redirect: '/registration-audit',
    children: [{
        path: '/registration-audit',
        component: RegistrationAudit
      },
      {
        path: '/credit-rating',
        component: CreditRating
      },
      {
        path: '/retrospect-supervise',
        component: RetrospectSupervise
      },
      {
        path: '/inspect',
        component: Inspect
      },
      {
        path: '/statistics',
        component: Statistics
      },
      {
        path: '/forewarning',
        component: Forewarning
      },
      {
        path: '/retrospect-home',
        component: RetrospectHome,
        redirect: '/qualification',
        children: [{
            path: '/qualification',
            component: Qualification
          },
          {
            path: '/retailer',
            component: Retailer
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
            path: '/retrospect-statistics',
            component: RetrospectStatistics
          },
          {
            path: '/user',
            component: User
          }
        ]
      }
    ]
  }
];

// 解决路由重复的报错
// 获取原型对象上的push函数
const originalPush = VueRouter.prototype.push;
// 修改原型对象中的push方法
VueRouter.prototype.push = function push(location) {
  return originalPush.call(this, location).catch(err => err)
}

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
  const tokenStr = window.sessionStorage.getItem('supervisionToken');
  if (!tokenStr) return next('/login')
  next();
});

export default router;