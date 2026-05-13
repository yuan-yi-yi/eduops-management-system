<script setup>
import { ref, onMounted, computed } from "vue";
import { ElMessage } from "element-plus";
import {
  User,
  School,
  UserFilled,
  OfficeBuilding,
  ArrowUp,
  ArrowDown,
  Clock,
  Calendar,
  Star,
  Trophy,
  DataAnalysis,
} from "@element-plus/icons-vue";
import request from "@/utils/request";

const statsData = ref({
  studentCount: 0,
  clazzCount: 0,
  empCount: 0,
  deptCount: 0,
  empImage: "",
  empName: "",
});

const loading = ref(false);
const avatarError = ref(false);

// 概览数据
const overviewItems = computed(() => {
  return [
    {
      icon: User,
      label: "在册学员",
      value: statsData.value.studentCount || 0,
      color: "linear-gradient(135deg, #667eea 0%, #764ba2 100%)",
      progress: Math.min(
        ((statsData.value.studentCount || 0) / 100) * 100,
        100
      ),
    },
    {
      icon: School,
      label: "开设班级",
      value: statsData.value.clazzCount || 0,
      color: "linear-gradient(135deg, #f093fb 0%, #f5576c 100%)",
      progress: Math.min(((statsData.value.clazzCount || 0) / 20) * 100, 100),
    },
    {
      icon: UserFilled,
      label: "在职员工",
      value: statsData.value.empCount || 0,
      color: "linear-gradient(135deg, #4facfe 0%, #00f2fe 100%)",
      progress: Math.min(((statsData.value.empCount || 0) / 50) * 100, 100),
    },
    {
      icon: OfficeBuilding,
      label: "部门架构",
      value: statsData.value.deptCount || 0,
      color: "linear-gradient(135deg, #43e97b 0%, #38f9d7 100%)",
      progress: Math.min(((statsData.value.deptCount || 0) / 10) * 100, 100),
    },
  ];
});

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
  const name = statsData.value.empName || "用户";
  const index = name.length % avatarColors.length;
  return avatarColors[index];
});

// 获取名字首字母
const nameInitial = computed(() => {
  const name = statsData.value.empName || "用户";
  return name.charAt(0).toUpperCase();
});

// 智能问候语
const getGreeting = computed(() => {
  const hour = new Date().getHours();
  if (hour < 6) return "夜深了";
  if (hour < 9) return "早上好";
  if (hour < 12) return "上午好";
  if (hour < 14) return "中午好";
  if (hour < 17) return "下午好";
  if (hour < 19) return "傍晚好";
  if (hour < 22) return "晚上好";
  return "夜深了";
});

// 励志语录
const motivationalQuotes = [
  "教育不是注满一桶水，而是点燃一把火。",
  "每一位学员都是一颗独特的种子。",
  "教学相长，共同成长。",
  "用爱心浇灌，让希望发芽。",
  "优秀的团队创造卓越的未来。",
  "细节决定成败，态度决定高度。",
];

const randomQuote = computed(() => {
  return motivationalQuotes[
    Math.floor(Math.random() * motivationalQuotes.length)
  ];
});

// 当前时间显示
const currentTime = ref(new Date());
onMounted(() => {
  fetchStats();
  setInterval(() => {
    currentTime.value = new Date();
  }, 1000);
});

const formattedTime = computed(() => {
  return currentTime.value.toLocaleTimeString("zh-CN", {
    hour: "2-digit",
    minute: "2-digit",
    second: "2-digit",
  });
});

const formattedDate = computed(() => {
  return currentTime.value.toLocaleDateString("zh-CN", {
    year: "numeric",
    month: "long",
    day: "numeric",
    weekday: "long",
  });
});

// 处理头像加载错误
const handleAvatarError = () => {
  avatarError.value = true;
};

const fetchStats = async () => {
  loading.value = true;
  try {
    const loginUserStr = localStorage.getItem("loginUser");
    const loginUser = loginUserStr ? JSON.parse(loginUserStr) : {};
    const username = loginUser.username || loginUser.name || "";

    const response = await request.post("/index", { username });

    if (response.code === 1) {
      statsData.value = response.data || {};
    } else {
      ElMessage.error(response.msg || "获取统计数据失败");
    }
  } catch (error) {
    console.error("获取统计数据失败:", error);
    ElMessage.error("获取统计数据失败，请稍后重试");
  } finally {
    loading.value = false;
  }
};
</script>

<template>
  <div
    class="index-stats"
    v-loading="loading"
    element-loading-text="数据加载中..."
    element-loading-background="rgba(255, 255, 255, 0.9)"
  >
    <!-- 顶部用户信息区域 -->
    <div class="user-welcome">
      <div class="avatar-wrapper">
        <el-avatar
          v-if="statsData.empImage && !avatarError"
          :size="80"
          :src="statsData.empImage"
          class="user-avatar"
          @error="handleAvatarError"
        />
        <div
          v-else
          class="user-avatar-fallback"
          :style="{ background: avatarColor }"
        >
          <span class="avatar-initial">{{ nameInitial }}</span>
        </div>
        <div class="online-indicator"></div>
      </div>
      <div class="welcome-info">
        <div class="greeting-row">
          <h2 class="welcome-title">
            {{ getGreeting }}，{{ statsData.empName || "用户" }}！
          </h2>
          <span class="current-time">
            <el-icon><Clock /></el-icon>
            {{ formattedTime }}
          </span>
        </div>
        <p class="welcome-subtitle">
          <el-icon><Calendar /></el-icon>
          {{ formattedDate }}
        </p>
        <p class="motivational-quote">
          <el-icon><Star /></el-icon>
          {{ randomQuote }}
        </p>
      </div>
    </div>

    <!-- 统计卡片区域 -->
    <el-row :gutter="20" class="stats-row">
      <el-col :xs="24" :sm="12" :md="12" :lg="6" :xl="6">
        <el-card shadow="hover" class="stat-card stat-student">
          <div class="card-decoration top-left"></div>
          <div class="stat-content-wrapper">
            <div class="stat-icon-wrapper">
              <el-icon class="stat-icon"><User /></el-icon>
            </div>
            <div class="stat-content">
              <div class="stat-value animate-count">
                {{ statsData.studentCount || 0 }}
              </div>
              <div class="stat-label">学员总数</div>
            </div>
            <div class="stat-trend up">
              <el-icon><Star /></el-icon>
              <span>活跃</span>
            </div>
          </div>
          <div class="card-bg-icon">
            <el-icon :size="80"><User /></el-icon>
          </div>
        </el-card>
      </el-col>

      <el-col :xs="24" :sm="12" :md="12" :lg="6" :xl="6">
        <el-card shadow="hover" class="stat-card stat-clazz">
          <div class="card-decoration top-right"></div>
          <div class="stat-content-wrapper">
            <div class="stat-icon-wrapper">
              <el-icon class="stat-icon"><School /></el-icon>
            </div>
            <div class="stat-content">
              <div class="stat-value animate-count">
                {{ statsData.clazzCount || 0 }}
              </div>
              <div class="stat-label">班级总数</div>
            </div>
            <div class="stat-trend up">
              <el-icon><Star /></el-icon>
              <span>稳定</span>
            </div>
          </div>
          <div class="card-bg-icon">
            <el-icon :size="80"><School /></el-icon>
          </div>
        </el-card>
      </el-col>

      <el-col :xs="24" :sm="12" :md="12" :lg="6" :xl="6">
        <el-card shadow="hover" class="stat-card stat-emp">
          <div class="card-decoration bottom-left"></div>
          <div class="stat-content-wrapper">
            <div class="stat-icon-wrapper">
              <el-icon class="stat-icon"><UserFilled /></el-icon>
            </div>
            <div class="stat-content">
              <div class="stat-value animate-count">
                {{ statsData.empCount || 0 }}
              </div>
              <div class="stat-label">员工总数</div>
            </div>
            <div class="stat-trend up">
              <el-icon><Trophy /></el-icon>
              <span>优秀</span>
            </div>
          </div>
          <div class="card-bg-icon">
            <el-icon :size="80"><UserFilled /></el-icon>
          </div>
        </el-card>
      </el-col>

      <el-col :xs="24" :sm="12" :md="12" :lg="6" :xl="6">
        <el-card shadow="hover" class="stat-card stat-dept">
          <div class="card-decoration bottom-right"></div>
          <div class="stat-content-wrapper">
            <div class="stat-icon-wrapper">
              <el-icon class="stat-icon"><OfficeBuilding /></el-icon>
            </div>
            <div class="stat-content">
              <div class="stat-value animate-count">
                {{ statsData.deptCount || 0 }}
              </div>
              <div class="stat-label">部门总数</div>
            </div>
            <div class="stat-trend up">
              <el-icon><DataAnalysis /></el-icon>
              <span>完整</span>
            </div>
          </div>
          <div class="card-bg-icon">
            <el-icon :size="80"><OfficeBuilding /></el-icon>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 数据概览图表区域 -->
    <el-row :gutter="20" class="chart-row">
      <el-col :span="24">
        <el-card shadow="hover" class="chart-card">
          <template #header>
            <div class="card-header">
              <el-icon><DataAnalysis /></el-icon>
              <span class="header-title">数据概览</span>
              <div class="header-decoration"></div>
            </div>
          </template>
          <div class="overview-content">
            <div
              class="overview-item"
              v-for="(item, index) in overviewItems"
              :key="index"
            >
              <div class="overview-icon" :style="{ background: item.color }">
                <el-icon :size="28" color="white">
                  <component :is="item.icon" />
                </el-icon>
              </div>
              <div class="overview-info">
                <div class="overview-value">{{ item.value }}</div>
                <div class="overview-label">{{ item.label }}</div>
              </div>
              <div class="overview-progress">
                <div
                  class="progress-bar"
                  :style="{
                    width: item.progress + '%',
                    background: item.color,
                  }"
                ></div>
              </div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<style scoped>
.index-stats {
  padding: 20px;
  min-height: 500px;
}

/* 用户欢迎区域 */
.user-welcome {
  display: flex;
  align-items: center;
  gap: 24px;
  padding: 28px 36px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  border-radius: 20px;
  margin-bottom: 28px;
  box-shadow: 0 15px 50px rgba(102, 126, 234, 0.35);
  position: relative;
  overflow: hidden;
}

.user-welcome::before {
  content: "";
  position: absolute;
  top: -50%;
  right: -50%;
  width: 100%;
  height: 100%;
  background: radial-gradient(
    circle,
    rgba(255, 255, 255, 0.1) 0%,
    transparent 60%
  );
  pointer-events: none;
}

.avatar-wrapper {
  position: relative;
  z-index: 1;
}

.user-avatar {
  border: 5px solid rgba(255, 255, 255, 0.6);
  box-shadow: 0 8px 30px rgba(0, 0, 0, 0.3);
  transition: transform 0.4s cubic-bezier(0.175, 0.885, 0.32, 1.275),
    box-shadow 0.4s;
}

.user-avatar:hover {
  transform: scale(1.1) rotate(5deg);
  box-shadow: 0 12px 40px rgba(0, 0, 0, 0.4);
}

.user-avatar-fallback {
  width: 80px;
  height: 80px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  border: 5px solid rgba(255, 255, 255, 0.6);
  box-shadow: 0 8px 30px rgba(0, 0, 0, 0.3);
  transition: transform 0.4s cubic-bezier(0.175, 0.885, 0.32, 1.275),
    box-shadow 0.4s;
}

.user-avatar-fallback:hover {
  transform: scale(1.1) rotate(5deg);
  box-shadow: 0 12px 40px rgba(0, 0, 0, 0.4);
}

.avatar-initial {
  color: white;
  font-weight: 800;
  font-size: 32px;
  letter-spacing: 2px;
}

.online-indicator {
  position: absolute;
  bottom: 4px;
  right: 4px;
  width: 18px;
  height: 18px;
  background: #52c41a;
  border: 3px solid white;
  border-radius: 50%;
  animation: pulse 2s infinite;
}

@keyframes pulse {
  0%,
  100% {
    transform: scale(1);
    opacity: 1;
  }
  50% {
    transform: scale(1.2);
    opacity: 0.8;
  }
}

.welcome-info {
  color: white;
  flex: 1;
  z-index: 1;
}

.greeting-row {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 8px;
}

.welcome-title {
  margin: 0;
  font-size: 26px;
  font-weight: 700;
  text-shadow: 0 2px 10px rgba(0, 0, 0, 0.2);
}

.current-time {
  display: flex;
  align-items: center;
  gap: 6px;
  font-size: 16px;
  background: rgba(255, 255, 255, 0.2);
  padding: 8px 16px;
  border-radius: 30px;
  backdrop-filter: blur(10px);
}

.welcome-subtitle {
  margin: 0 0 12px 0;
  font-size: 15px;
  opacity: 0.9;
  display: flex;
  align-items: center;
  gap: 6px;
}

.motivational-quote {
  margin: 0;
  font-size: 14px;
  opacity: 0.85;
  font-style: italic;
  display: flex;
  align-items: center;
  gap: 8px;
  background: rgba(255, 255, 255, 0.15);
  padding: 10px 16px;
  border-radius: 12px;
  backdrop-filter: blur(5px);
}

/* 统计卡片 */
.stats-row {
  margin-bottom: 24px;
}

.stat-card {
  position: relative;
  padding: 24px;
  border-radius: 16px;
  transition: all 0.4s cubic-bezier(0.175, 0.885, 0.32, 1.275);
  border: none;
  overflow: hidden;
  min-height: 140px;
}

.stat-card:hover {
  transform: translateY(-8px) scale(1.02);
  box-shadow: 0 20px 50px rgba(0, 0, 0, 0.15) !important;
}

.card-decoration {
  position: absolute;
  width: 60px;
  height: 60px;
  border-radius: 50%;
  opacity: 0.1;
  pointer-events: none;
}

.card-decoration.top-left {
  top: -20px;
  left: -20px;
}

.card-decoration.top-right {
  top: -20px;
  right: -20px;
}

.card-decoration.bottom-left {
  bottom: -20px;
  left: -20px;
}

.card-decoration.bottom-right {
  bottom: -20px;
  right: -20px;
}

.stat-student .card-decoration {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
}

.stat-clazz .card-decoration {
  background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
}

.stat-emp .card-decoration {
  background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);
}

.stat-dept .card-decoration {
  background: linear-gradient(135deg, #43e97b 0%, #38f9d7 100%);
}

.stat-content-wrapper {
  display: flex;
  align-items: flex-start;
  position: relative;
  z-index: 1;
}

.stat-icon-wrapper {
  width: 64px;
  height: 64px;
  border-radius: 16px;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-right: 18px;
  flex-shrink: 0;
}

.stat-student .stat-icon-wrapper {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
}

.stat-clazz .stat-icon-wrapper {
  background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
}

.stat-emp .stat-icon-wrapper {
  background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);
}

.stat-dept .stat-icon-wrapper {
  background: linear-gradient(135deg, #43e97b 0%, #38f9d7 100%);
}

.stat-icon {
  font-size: 32px;
  color: white;
}

.stat-content {
  flex: 1;
}

.stat-value {
  font-size: 36px;
  font-weight: 800;
  color: #1f2937;
  line-height: 1.1;
  margin-bottom: 4px;
}

.animate-count {
  animation: countUp 0.5s ease-out;
}

@keyframes countUp {
  from {
    transform: translateY(20px);
    opacity: 0;
  }
  to {
    transform: translateY(0);
    opacity: 1;
  }
}

.stat-label {
  font-size: 14px;
  color: #6b7280;
  font-weight: 500;
}

.stat-trend {
  display: flex;
  align-items: center;
  gap: 4px;
  font-size: 13px;
  padding: 4px 10px;
  border-radius: 20px;
  margin-top: 8px;
}

.stat-trend.up {
  background: linear-gradient(135deg, #d4edda 0%, #c3e6cb 100%);
  color: #155724;
}

.card-bg-icon {
  position: absolute;
  right: -10px;
  bottom: -10px;
  opacity: 0.05;
  pointer-events: none;
}

/* 图表区域 */
.chart-row {
  margin-top: 24px;
}

.chart-card {
  border-radius: 16px;
  overflow: hidden;
}

.card-header {
  display: flex;
  align-items: center;
  gap: 10px;
  font-size: 18px;
  font-weight: 700;
  color: #1f2937;
  position: relative;
}

.header-decoration {
  flex: 1;
  height: 3px;
  background: linear-gradient(90deg, #667eea 0%, transparent 100%);
  margin-left: 12px;
  border-radius: 2px;
}

.overview-content {
  display: flex;
  justify-content: space-around;
  align-items: stretch;
  padding: 24px 0;
  gap: 20px;
}

.overview-item {
  flex: 1;
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 12px;
  padding: 20px;
  border-radius: 16px;
  transition: all 0.3s ease;
}

.overview-item:hover {
  background: linear-gradient(
    135deg,
    rgba(102, 126, 234, 0.05) 0%,
    rgba(118, 74, 162, 0.05) 100%
  );
  transform: translateY(-4px);
}

.overview-icon {
  width: 56px;
  height: 56px;
  border-radius: 16px;
  display: flex;
  align-items: center;
  justify-content: center;
  box-shadow: 0 4px 15px rgba(0, 0, 0, 0.1);
}

.overview-info {
  text-align: center;
}

.overview-value {
  font-size: 28px;
  font-weight: 800;
  color: #1f2937;
}

.overview-label {
  font-size: 13px;
  color: #6b7280;
  margin-top: 2px;
  font-weight: 500;
}

.overview-progress {
  width: 100%;
  height: 6px;
  background: #e5e7eb;
  border-radius: 3px;
  overflow: hidden;
  margin-top: 8px;
}

.progress-bar {
  height: 100%;
  border-radius: 3px;
  transition: width 1s ease-out;
  animation: progressGrow 1.5s ease-out;
}

@keyframes progressGrow {
  from {
    width: 0;
  }
}

@media (max-width: 1200px) {
  .overview-content {
    flex-wrap: wrap;
  }
}

@media (max-width: 768px) {
  .user-welcome {
    flex-direction: column;
    text-align: center;
    padding: 24px 20px;
  }

  .greeting-row {
    flex-direction: column;
    gap: 12px;
  }

  .welcome-title {
    font-size: 22px;
  }

  .overview-content {
    flex-wrap: wrap;
    gap: 16px;
  }

  .overview-item {
    min-width: 160px;
  }
}
</style>
