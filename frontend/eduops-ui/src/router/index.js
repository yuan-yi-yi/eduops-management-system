import { createRouter, createWebHistory } from "vue-router";
import { ElMessage } from "element-plus";

const routes = [
  {
    path: "/",
    component: () => import("@/views/layout/index.vue"),
    redirect: "/index",
    children: [
      { path: "/index", component: () => import("@/views/index/index.vue") },
      { path: "/clazz", component: () => import("@/views/clazz/index.vue") },
      { path: "/stu", component: () => import("@/views/stu/index.vue") },
      { path: "/dept", component: () => import("@/views/dept/index.vue") },
      { path: "/emp", component: () => import("@/views/emp/index.vue") },
      {
        path: "/report/emp",
        component: () => import("@/views/report/emp/index.vue"),
      },
      {
        path: "/report/stu",
        component: () => import("@/views/report/stu/index.vue"),
      },
      { path: "/log", component: () => import("@/views/log/index.vue") },
    ],
  },
  {
    path: "/login",
    component: () => import("@/views/login/index.vue"),
    meta: { requiresAuth: false },
  },
];

const router = createRouter({
  history: createWebHistory(),
  routes,
});

// 路由前置守卫
router.beforeEach((to, from, next) => {
  const loginUser = localStorage.getItem("loginUser");

  if (to.path !== "/login" && !loginUser) {
    if (from.path !== "/login") {
      setTimeout(() => {
        ElMessage.warning("请先登录后再访问");
      }, 100);
    }
    next("/login");
  } else if (to.path === "/login" && loginUser) {
    next("/index");
  } else {
    next();
  }
});

export default router;
