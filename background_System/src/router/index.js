import Vue from 'vue';
import VueRouter from 'vue-router';
import routes from './routes.js';
Vue.use(VueRouter);

const originalPush = VueRouter.prototype.push;

VueRouter.prototype.push = function push(location) {
  return originalPush.call(this, location).catch(err => err);
};

const router = new VueRouter({
  mode: 'hash',
  base: process.env.BASE_URL,
  routes
});

router.firstInit = false;

//挂载路由导航守卫
// router.beforeEach((to, from, next) => {
//   // to 将要访问的路径
//   // from 代表从哪个路径跳转而来
//   //next 是一个函数 表示放行
//   //    next()  放行  next('/login) 强制跳转
//   if (to.path === '/login') return next();
//   const tokenStr = window.sessionStorage.getItem('token');
//   if (!tokenStr) {
//     next('/login')
//     return;
//   }
//   next();
// });

export default router;
