<script setup>
import { ref, onMounted, computed, watch } from "vue";
import { ElMessage, ElMessageBox } from "element-plus";
import { useRouter, useRoute } from "vue-router";
import {
  Promotion,
  Menu,
  HomeFilled,
  UserFilled,
  Tools,
  HelpFilled,
  Avatar,
  Histogram,
  InfoFilled,
  Share,
  Document,
  SwitchButton,
  EditPen,
  Fold,
  Expand,
  Setting,
} from "@element-plus/icons-vue";
import ChangePassword from "@/components/ChangePassword.vue";

const router = useRouter();
const route = useRoute();
const changePasswordRef = ref(null);
const isCollapse = ref(false);

const loginName = ref("");
const loginAvatar = ref("");
const avatarError = ref(false);
const loginUser = ref(null);

// 页面标题映射
const pageTitles = {
  "/index": "首页",
  "/clazz": "班级管理",
  "/stu": "学员管理",
  "/dept": "部门管理",
  "/emp": "员工管理",
  "/report/emp": "员工信息统计",
  "/report/stu": "学员信息统计",
  "/log": "日志信息统计",
};

// 动态页面标题
const currentPageTitle = computed(() => {
  return pageTitles[route.path] || "EduOps";
});

// 监听路由变化，更新页面标题
watch(
  () => route.path,
  () => {
    document.title = `${currentPageTitle.value} - EduOps教培运营管理系统`;
  },
  { immediate: true }
);

// 根据名字生成渐变色
const avatarColors = [
  "linear-gradient(135deg, #667eea 0%, #764ba2 100%)",
  "linear-gradient(135deg, #f093fb 0%, #f5576c 100%)",
  "linear-gradient(135deg, #4facfe 0%, #00f2fe 100%)",
  "linear-gradient(135deg, #43e97b 0%, #38f9d7 100%)",
  "linear-gradient(135deg, #fa709a 0%, #fee140 100%)",
  "linear-gradient(135deg, #a18cd1 0%, #fbc2eb 100%)",
  "linear-gradient(135deg, #ffecd2 0%, #fcb69f 100%)",
  "linear-gradient(135deg, #ff9a9e 0%, #fecfef 100%)",
];

const avatarColor = computed(() => {
  const index = loginName.value.length % avatarColors.length;
  return avatarColors[index];
});

// 获取名字首字母
const nameInitial = computed(() => {
  if (!loginName.value) return "用";
  return loginName.value.charAt(0).toUpperCase();
});

// 处理头像加载错误
const handleAvatarError = () => {
  avatarError.value = true;
};

onMounted(() => {
  const userData = JSON.parse(localStorage.getItem("loginUser"));
  if (userData) {
    loginUser.value = userData;
    loginName.value =
      userData.name || userData.empName || userData.username || "用户";
    loginAvatar.value = userData.empImage || userData.image || "";
  }
});

const logout = () => {
  ElMessageBox.confirm("确认退出登录吗?", "提示", {
    confirmButtonText: "确定",
    cancelButtonText: "取消",
    type: "warning",
    customClass: "logout-confirm-dialog",
  }).then(() => {
    ElMessage.success("退出登录成功");
    localStorage.removeItem("loginUser");
    router.push("/login");
  });
};

const handleChangePassword = () => {
  changePasswordRef.value?.openDialog();
};

const toggleSidebar = () => {
  isCollapse.value = !isCollapse.value;
};

const menuItems = [
  { index: "/index", icon: Promotion, title: "首页" },
  {
    index: "/manage",
    icon: Menu,
    title: "班级学员管理",
    children: [
      { index: "/clazz", icon: HomeFilled, title: "班级管理" },
      { index: "/stu", icon: UserFilled, title: "学员管理" },
    ],
  },
  {
    index: "/system",
    icon: Tools,
    title: "系统信息管理",
    children: [
      { index: "/dept", icon: HelpFilled, title: "部门管理" },
      { index: "/emp", icon: Avatar, title: "员工管理" },
    ],
  },
  {
    index: "/report",
    icon: Histogram,
    title: "数据统计管理",
    children: [
      { index: "/report/emp", icon: InfoFilled, title: "员工信息统计" },
      { index: "/report/stu", icon: Share, title: "学员信息统计" },
      { index: "/log", icon: Document, title: "日志信息统计" },
    ],
  },
];
</script>

<template>
  <div class="common-layout">
    <el-container class="layout-container">
      <!-- Header 区域 -->
      <el-header class="header">
        <div class="header-left">
          <div class="logo">
            <div class="logo-icon">📚</div>
            <span class="title">EduOps教培运营管理系统</span>
            <span
              v-if="currentPageTitle && currentPageTitle !== '首页'"
              class="page-title-divider"
              >|</span
            >
            <span
              v-if="currentPageTitle && currentPageTitle !== '首页'"
              class="page-title"
              >{{ currentPageTitle }}</span
            >
          </div>
        </div>
        <div class="header-right">
          <div class="user-info">
            <div class="header-avatar-wrapper">
              <el-avatar
                v-if="loginAvatar && !avatarError"
                :size="36"
                :src="loginAvatar"
                class="header-avatar"
                @error="handleAvatarError"
              />
              <div
                v-else
                class="header-avatar-fallback"
                :style="{ background: avatarColor }"
              >
                <span class="avatar-initial">{{ nameInitial }}</span>
              </div>
            </div>
            <span class="user-name">{{ loginName }}</span>
          </div>
          <el-divider direction="vertical" />
          <el-dropdown>
            <span class="user-dropdown">
              <el-icon><Setting /></el-icon>
            </span>
            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item @click="handleChangePassword">
                  <el-icon><EditPen /></el-icon>修改密码
                </el-dropdown-item>
                <el-dropdown-item divided @click="logout">
                  <el-icon><SwitchButton /></el-icon>退出登录
                </el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
        </div>
      </el-header>

      <el-container class="main-container">
        <!-- 左侧菜单 -->
        <el-aside :width="isCollapse ? '64px' : '220px'" class="aside">
          <div class="sidebar-header">
            <el-button text class="collapse-btn" @click="toggleSidebar">
              <el-icon :size="18">
                <Fold v-if="!isCollapse" />
                <Expand v-else />
              </el-icon>
            </el-button>
          </div>

          <el-menu
            router
            :collapse="isCollapse"
            :collapse-transition="true"
            :default-active="route.path"
            class="sidebar-menu"
            background-color="#1e293b"
            text-color="#94a3b8"
            active-text-color="#ffffff"
          >
            <template v-for="item in menuItems" :key="item.index">
              <el-sub-menu v-if="item.children" :index="item.index">
                <template #title>
                  <el-icon><component :is="item.icon" /></el-icon>
                  <span>{{ item.title }}</span>
                </template>
                <el-menu-item
                  v-for="child in item.children"
                  :key="child.index"
                  :index="child.index"
                >
                  <el-icon><component :is="child.icon" /></el-icon>
                  <span>{{ child.title }}</span>
                </el-menu-item>
              </el-sub-menu>
              <el-menu-item v-else :index="item.index">
                <el-icon><component :is="item.icon" /></el-icon>
                <span>{{ item.title }}</span>
              </el-menu-item>
            </template>
          </el-menu>
        </el-aside>

        <!-- 主展示区域 -->
        <el-main class="main-content">
          <router-view />
        </el-main>
      </el-container>
    </el-container>

    <!-- 修改密码弹窗 -->
    <ChangePassword ref="changePasswordRef" />
  </div>
</template>

<style scoped>
.layout-container {
  height: 100vh;
}

/* Header */
.header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 24px;
  background: linear-gradient(135deg, #1e3a5f 0%, #2d5a87 50%, #1e3a5f 100%);
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.2);
  z-index: 100;
}

.header-left {
  display: flex;
  align-items: center;
}

.logo {
  display: flex;
  align-items: center;
  gap: 12px;
}

.logo-icon {
  font-size: 28px;
  animation: logoPulse 3s ease-in-out infinite;
}

@keyframes logoPulse {
  0%,
  100% {
    transform: scale(1);
  }
  50% {
    transform: scale(1.1);
  }
}

.title {
  color: white;
  font-size: 20px;
  font-weight: 700;
  letter-spacing: 1px;
  text-shadow: 0 2px 10px rgba(0, 0, 0, 0.2);
}

.page-title-divider {
  color: rgba(255, 255, 255, 0.4);
  margin: 0 8px;
}

.page-title {
  color: rgba(255, 255, 255, 0.85);
  font-size: 15px;
  font-weight: 500;
}

.header-right {
  display: flex;
  align-items: center;
  gap: 16px;
}

.user-info {
  display: flex;
  align-items: center;
  gap: 10px;
}

.header-avatar-wrapper {
  position: relative;
}

.header-avatar {
  border: 3px solid rgba(255, 255, 255, 0.4);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.2);
  transition: transform 0.3s cubic-bezier(0.175, 0.885, 0.32, 1.275),
    box-shadow 0.3s;
}

.header-avatar:hover {
  transform: scale(1.15);
  box-shadow: 0 6px 20px rgba(0, 0, 0, 0.35);
}

.header-avatar-fallback {
  width: 36px;
  height: 36px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  border: 3px solid rgba(255, 255, 255, 0.4);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.2);
  transition: transform 0.3s cubic-bezier(0.175, 0.885, 0.32, 1.275),
    box-shadow 0.3s;
}

.header-avatar-fallback:hover {
  transform: scale(1.15);
  box-shadow: 0 6px 20px rgba(0, 0, 0, 0.35);
}

.avatar-initial {
  color: white;
  font-weight: 700;
  font-size: 16px;
  letter-spacing: 1px;
}

.user-name {
  color: white;
  font-size: 14px;
  font-weight: 600;
  text-shadow: 0 1px 5px rgba(0, 0, 0, 0.2);
}

.user-dropdown {
  color: white;
  cursor: pointer;
  font-size: 20px;
  padding: 8px;
  border-radius: 8px;
  transition: background 0.3s, transform 0.3s;
}

.user-dropdown:hover {
  background: rgba(255, 255, 255, 0.18);
  transform: rotate(15deg);
}

/* Sidebar */
.aside {
  background: linear-gradient(180deg, #1e293b 0%, #1a2332 100%);
  transition: width 0.3s cubic-bezier(0.175, 0.885, 0.32, 1.275);
  box-shadow: 4px 0 20px rgba(0, 0, 0, 0.15);
}

.sidebar-header {
  display: flex;
  justify-content: flex-end;
  padding: 12px;
  border-bottom: 1px solid #334155;
}

.collapse-btn {
  color: #94a3b8;
  padding: 8px;
  border-radius: 8px;
  transition: all 0.3s;
}

.collapse-btn:hover {
  color: white;
  background: rgba(255, 255, 255, 0.1);
  transform: rotate(180deg);
}

.sidebar-menu {
  border-right: none;
  background: transparent;
}

.sidebar-menu :deep(.el-menu-item),
.sidebar-menu :deep(.el-sub-menu__title) {
  height: 50px;
  line-height: 50px;
  margin: 4px 10px;
  border-radius: 10px;
  transition: all 0.3s;
}

.sidebar-menu :deep(.el-menu-item:hover),
.sidebar-menu :deep(.el-sub-menu__title:hover) {
  background: rgba(255, 255, 255, 0.1) !important;
  transform: translateX(4px);
}

.sidebar-menu :deep(.el-menu-item.is-active) {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%) !important;
  box-shadow: 0 4px 15px rgba(102, 126, 234, 0.35);
}

.sidebar-menu :deep(.el-sub-menu .el-menu-item) {
  background: transparent;
  margin: 2px 10px;
}

.sidebar-menu :deep(.el-menu-item .el-icon),
.sidebar-menu :deep(.el-sub-menu__title .el-icon) {
  font-size: 18px;
}

/* Main Content */
.main-content {
  background: linear-gradient(180deg, #f1f5f9 0%, #e8ecf4 100%);
  padding: 20px;
  overflow-y: auto;
}

/* Transition */
.fade-enter-active,
.fade-leave-active {
  transition: opacity 0.3s ease;
}

.fade-enter-from,
.fade-leave-to {
  opacity: 0;
}

/* Dropdown */
:deep(.el-dropdown-menu__item) {
  display: flex;
  align-items: center;
  gap: 8px;
  font-weight: 500;
}

:deep(.logout-confirm-dialog .el-button--primary) {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  border: none;
}
</style>
