export default [
  {
    path: "/",
    redirect: "/home"
  },
  {
    path: "/login",
    name: "Login",
    component: () => import("@/views/Login.vue")
  },
  {
    path: "/home",
    component: () => import("@/components/Home.vue"),
    redirect: "/welcome",
    children: [
      {
        path: "/welcome",
        component: () => import("@/views/Welcome.vue")
      },
      {
        path: "/data-config",
        component: () => import("@/views/DataAdmin/DataConfig.vue")
      },
      {
        path: "/data-admin",
        component: () => import("@/views/DataAdmin/DataAdmin.vue")
      },
      {
        path: "/user-manage",
        component: () => import("@/views/UserManage/UserManage.vue")
      },
      {
        path: "/service-state",
        component: () => import("@/views/ServiceManage/ServiceState.vue")
      }

    ]
  }
];
