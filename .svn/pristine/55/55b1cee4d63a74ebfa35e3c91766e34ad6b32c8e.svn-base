import Vue from "vue";
import VueRouter from "vue-router";
import routes from "./routes.js";
import DataStore from "@/globals/storage/index.js";

Vue.use(VueRouter);

const originalPush = VueRouter.prototype.push;

VueRouter.prototype.push = function push(location) {
  return originalPush.call(this, location).catch(err => err);
};

const router = new VueRouter({
  mode: "hash",
  base: process.env.BASE_URL,
  routes
});
//验证是否存在token
router.beforeEach((to, from, next) => {
  //to即将进入的目标路由对象，from当前导航正要离开的路由， next : 下一步执行的函数钩子
  if(to.path === '/login') {
    next()   // 如果即将进入登录路由，则直接放行
  }else { //进入的不是登录路由
    if(!DataStore.getToken()) {
      next({ path: '/login' })   //下一跳路由需要登录验证，并且还未登录，则路由定向到 登录路由
    } else {
      next()  //如果token存在，则直接放行
    }
  }
});
router.firstInit = false;

export default router;
