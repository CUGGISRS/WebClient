import Vue from 'vue'
import Router from 'vue-router'
import home from '@/page/home/home.vue'//首页
import login from '@/page/index/login.vue'//登录
import trend from '@/page/home/trend.vue'
import personal from '@/page/index/personal.vue'//个人中心
import video from '@/page/cyclopedia/video.vue'
Vue.use(Router)

export default new Router({
  routes: [
    {
      path: "/",
      redirect: "/home"
    },

    {
      path: '/home',
      name: 'home',
      component: home,
      redirect: "/home/index",
      children: [
        {
          path: "index",
          name: "index",
          component: () => import("@/page/index/index.vue")//首页
        },
        {
          path: "newslist",
          name: "newslist",
          component: () => import("@/page/journalism/newslist.vue")//农业政策
        },
        {
          path: "bazaar",
          name: "bazaar",
          component: () => import("@/page/market/bazaar.vue")//市场动态
        }, 
        {
          path: "plant",
          name: "plant",
          component: () => import("@/page/cyclopedia/plant.vue")//市场动态
        },
        {
          path: "video",
          name: "video",
          component: () => import("@/page/cyclopedia/video.vue")//农技视频
        }
      ]
    },
    {
      path: '/login',
      name: 'login',
      component: login
    },
    {
      path: '/personal',
      name: 'personal',
      component: personal
    },
    {
      path: '/trend',
      name: 'trend',
      component: trend
    },
  ]
})
