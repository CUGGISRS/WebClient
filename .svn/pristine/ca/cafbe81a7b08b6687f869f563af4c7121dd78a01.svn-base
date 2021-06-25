// eslint-disable-next-line
import { UserLayout, BasicLayout, RouteView, BlankLayout, PageView } from '@/layouts'
import { bxAnaalyse } from '@/core/icons'

export const asyncRouterMap = [

  {
    path: '/',
    name: 'index',
    component: BasicLayout,
    meta: { title: '首页' },
    redirect: '/spider',
    children: [
      // spider 蜘蛛
      {
        path: '/spider',
        name: 'spider',
        component: PageView,
        redirect: '/spider/spiderList',
        meta: { title: '蜘蛛', icon: 'table' },
        children: [
          {
            path: '/spider/spiderList',
            name: 'SpiderList',
            hideChildrenInMenu: true, // 强制显示 MenuItem 而不是 SubMenu
            component: () => import('@/views/spider/SpiderList'),
            meta: { title: '蜘蛛群', keepAlive: false }
          },
          {
            path: '/spider/jobList',
            name: 'JobList',
            hideChildrenInMenu: true,
            component: () => import('@/views/spider/JobList'),
            meta: { title: '爬取任务', keepAlive: false }
          },
          {
            path: '/spider/jobRecord/:jobUuid',
            name: 'jobRecord',
            hideChildrenInMenu: true,
            component: () => import('@/views/spider/JobRecord'),
            meta: { title: '爬取结果', keepAlive: false },
            hidden: true
          }
        ]
      },
      //数据入库
      {
        path: '/storage',
        name: 'storage',
        component: PageView,
        redirect: '/storage/storageList',
        meta: { title: '数据入库', icon: 'table' },
        children: [
          {
            path: '/storage/storageList',
            name: 'storageList',
            hideChildrenInMenu: true, // 强制显示 MenuItem 而不是 SubMenu
            component: () => import('@/views/storage/storageList'),
            meta: { title: '数据列', keepAlive: false }
          }
        ]
      }
      // proxy 代理ip爬取 暂时不需要
      /*{
        path: '/proxy',
        name: 'proxy',
        component: PageView,
        redirect: '/proxy/proxyList',
        meta: { title: '代理', icon: 'form' },
        children: [
          {
            path: '/proxy/crateProxy',
            name: 'crateProxy',
            hideChildrenInMenu: true,
            component: () => import('@/views/proxy/createProxy'),
            meta: { title: '创建代理', keepAlive: false }
          },

          {
            path: '/proxy/proxyList',
            name: 'proxyList',
            hideChildrenInMenu: true, // 强制显示 MenuItem 而不是 SubMenu
            component: () => import('@/views/proxy/proxyList'),
            meta: { title: '我的代理', keepAlive: false }
          }
        ]
      }*/
    ]
  },
  {
    path: '*', redirect: '/404', hidden: true
  }
]

/**
 * 基础路由
 * @type { *[] }
 */
export const constantRouterMap = [
  {
    path: '/user',
    component: UserLayout,
    redirect: '/user/login',
    hidden: true,
    children: [
      {
        path: 'login',
        name: 'login',
        component: () => import(/* webpackChunkName: "user" */ '@/views/user/Login')
      },
      {
        path: 'register',
        name: 'register',
        component: () => import(/* webpackChunkName: "user" */ '@/views/user/Register')
      },
      {
        path: 'register-result',
        name: 'registerResult',
        component: () => import(/* webpackChunkName: "user" */ '@/views/user/RegisterResult')
      }
    ]
  },

  {
    path: '/test',
    component: BlankLayout,
    redirect: '/test/home',
    children: [
      {
        path: 'home',
        name: 'TestHome',
        component: () => import('@/views/Home')
      }
    ]
  },

  {
    path: '/404',
    component: () => import(/* webpackChunkName: "fail" */ '@/views/exception/404')
  }
]
