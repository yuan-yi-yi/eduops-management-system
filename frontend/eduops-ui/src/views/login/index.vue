<script setup>
import { ref, computed, onMounted } from "vue";
import { ElMessage } from "element-plus";
import { User, Lock, Star } from "@element-plus/icons-vue";
import { useRouter } from "vue-router";
import request from "@/utils/request";

const router = useRouter();
const loading = ref(false);

// 智能问候
const greeting = computed(() => {
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

const loginForm = ref({
  username: "",
  password: "",
});

const login = async () => {
  if (!loginForm.value.username || !loginForm.value.password) {
    ElMessage.warning("请输入用户名和密码");
    return;
  }

  loading.value = true;
  try {
    const result = await request.post("/login", loginForm.value);

    if (result.code === 1) {
      ElMessage.success("登录成功");
      localStorage.setItem("loginUser", JSON.stringify(result.data));
      router.push("/");
    } else {
      ElMessage.error(result.msg || "登录失败");
    }
  } catch (error) {
    console.error("登录失败:", error);
    ElMessage.error("登录失败，请稍后重试");
  } finally {
    loading.value = false;
  }
};

const cancel = () => {
  loginForm.value = {
    username: "",
    password: "",
  };
};
</script>

<template>
  <div class="login-container">
    <div class="login-bg">
      <div class="bg-pattern">
        <div class="floating-circle circle-1"></div>
        <div class="floating-circle circle-2"></div>
        <div class="floating-circle circle-3"></div>
        <div class="floating-circle circle-4"></div>
      </div>
    </div>

    <div class="login-content">
      <div class="login-left">
        <div class="brand">
          <div class="brand-icon">📚</div>
          <h1 class="brand-title">EduOps</h1>
          <p class="brand-subtitle">教培运营管理系统</p>
        </div>
        <div class="features">
          <div class="feature-item">
            <div class="feature-icon">📊</div>
            <div class="feature-text">
              <div class="feature-title">数据统计</div>
              <div class="feature-desc">实时掌握运营数据</div>
            </div>
          </div>
          <div class="feature-item">
            <div class="feature-icon">👥</div>
            <div class="feature-text">
              <div class="feature-title">人员管理</div>
              <div class="feature-desc">高效管理学员员工</div>
            </div>
          </div>
          <div class="feature-item">
            <div class="feature-icon">📈</div>
            <div class="feature-text">
              <div class="feature-title">智能分析</div>
              <div class="feature-desc">辅助决策支持</div>
            </div>
          </div>
        </div>
      </div>

      <div class="login-right">
        <div class="login-form-wrapper">
          <div class="form-header">
            <div class="welcome-icon">
              <el-icon :size="32"><Star /></el-icon>
            </div>
            <h2 class="form-title">{{ greeting }}</h2>
            <p class="form-subtitle">欢迎回来，请登录系统</p>
          </div>

          <el-form class="login-form" @submit.prevent="login">
            <el-form-item>
              <el-input
                v-model="loginForm.username"
                placeholder="请输入用户名"
                size="large"
                :prefix-icon="User"
                clearable
                class="login-input"
              />
            </el-form-item>

            <el-form-item>
              <el-input
                v-model="loginForm.password"
                type="password"
                placeholder="请输入密码"
                size="large"
                :prefix-icon="Lock"
                show-password
                clearable
                class="login-input"
                @keyup.enter="login"
              />
            </el-form-item>

            <el-form-item>
              <el-button
                type="primary"
                size="large"
                class="login-btn"
                :loading="loading"
                @click="login"
              >
                {{ loading ? "登录中..." : "登 录" }}
              </el-button>
            </el-form-item>

            <el-form-item>
              <el-button size="large" class="reset-btn" @click="cancel">
                重 置
              </el-button>
            </el-form-item>
          </el-form>

          <div class="form-footer">
            <p>© 2026 EduOps 教培运营管理系统</p>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
.login-container {
  min-height: 100vh;
  display: flex;
  position: relative;
  overflow: hidden;
}

.login-bg {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 50%, #667eea 100%);
  background-size: 400% 400%;
  animation: gradientBG 15s ease infinite;
}

@keyframes gradientBG {
  0% {
    background-position: 0% 50%;
  }
  50% {
    background-position: 100% 50%;
  }
  100% {
    background-position: 0% 50%;
  }
}

.bg-pattern {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background-image: radial-gradient(
      circle at 20% 50%,
      rgba(255, 255, 255, 0.1) 0%,
      transparent 50%
    ),
    radial-gradient(
      circle at 80% 80%,
      rgba(255, 255, 255, 0.1) 0%,
      transparent 50%
    ),
    radial-gradient(
      circle at 40% 20%,
      rgba(255, 255, 255, 0.05) 0%,
      transparent 50%
    );
}

.floating-circle {
  position: absolute;
  border-radius: 50%;
  background: rgba(255, 255, 255, 0.1);
  animation: float 20s infinite;
}

.circle-1 {
  width: 300px;
  height: 300px;
  top: -100px;
  left: -100px;
  animation-delay: 0s;
}

.circle-2 {
  width: 200px;
  height: 200px;
  top: 50%;
  right: -50px;
  animation-delay: -5s;
}

.circle-3 {
  width: 150px;
  height: 150px;
  bottom: -50px;
  left: 30%;
  animation-delay: -10s;
}

.circle-4 {
  width: 100px;
  height: 100px;
  top: 20%;
  right: 20%;
  animation-delay: -15s;
}

@keyframes float {
  0%,
  100% {
    transform: translate(0, 0) scale(1);
  }
  33% {
    transform: translate(30px, -30px) scale(1.1);
  }
  66% {
    transform: translate(-20px, 20px) scale(0.9);
  }
}

.login-content {
  display: flex;
  width: 100%;
  max-width: 1200px;
  margin: auto;
  padding: 40px;
  gap: 60px;
  z-index: 1;
}

.login-left {
  flex: 1;
  display: flex;
  flex-direction: column;
  justify-content: center;
  color: white;
  padding: 40px;
}

.brand {
  margin-bottom: 60px;
}

.brand-icon {
  font-size: 72px;
  margin-bottom: 20px;
  animation: bounce 2s infinite;
}

@keyframes bounce {
  0%,
  100% {
    transform: translateY(0);
  }
  50% {
    transform: translateY(-10px);
  }
}

.brand-title {
  font-size: 56px;
  font-weight: 800;
  margin: 0 0 10px 0;
  letter-spacing: 3px;
  text-shadow: 0 4px 20px rgba(0, 0, 0, 0.2);
}

.brand-subtitle {
  font-size: 24px;
  opacity: 0.95;
  margin: 0;
  font-weight: 300;
  letter-spacing: 2px;
}

.features {
  display: flex;
  flex-direction: column;
  gap: 24px;
}

.feature-item {
  display: flex;
  align-items: center;
  gap: 16px;
  padding: 20px 24px;
  background: rgba(255, 255, 255, 0.12);
  border-radius: 16px;
  backdrop-filter: blur(15px);
  border: 1px solid rgba(255, 255, 255, 0.15);
  transition: all 0.4s cubic-bezier(0.175, 0.885, 0.32, 1.275);
}

.feature-item:hover {
  transform: translateX(10px) scale(1.02);
  background: rgba(255, 255, 255, 0.18);
  box-shadow: 0 10px 40px rgba(0, 0, 0, 0.1);
}

.feature-icon {
  font-size: 36px;
}

.feature-title {
  font-size: 18px;
  font-weight: 700;
  margin-bottom: 4px;
}

.feature-desc {
  font-size: 14px;
  opacity: 0.85;
}

.login-right {
  width: 440px;
  display: flex;
  align-items: center;
}

.login-form-wrapper {
  width: 100%;
  padding: 48px 40px;
  background: rgba(255, 255, 255, 0.95);
  border-radius: 24px;
  box-shadow: 0 30px 80px -15px rgba(0, 0, 0, 0.3);
  backdrop-filter: blur(20px);
  border: 1px solid rgba(255, 255, 255, 0.5);
  animation: slideIn 0.6s ease-out;
}

@keyframes slideIn {
  from {
    transform: translateY(30px);
    opacity: 0;
  }
  to {
    transform: translateY(0);
    opacity: 1;
  }
}

.form-header {
  text-align: center;
  margin-bottom: 36px;
}

.welcome-icon {
  width: 64px;
  height: 64px;
  margin: 0 auto 20px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  color: white;
  box-shadow: 0 8px 25px rgba(102, 126, 234, 0.3);
  animation: pulseIcon 2s ease-in-out infinite;
}

@keyframes pulseIcon {
  0%,
  100% {
    transform: scale(1);
  }
  50% {
    transform: scale(1.05);
  }
}

.form-title {
  font-size: 32px;
  font-weight: 800;
  color: #1f2937;
  margin: 0 0 8px 0;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
}

.form-subtitle {
  font-size: 15px;
  color: #6b7280;
  margin: 0;
}

.login-form {
  margin-top: 24px;
}

.login-input :deep(.el-input__wrapper) {
  border-radius: 16px;
  padding: 12px 20px;
  box-shadow: 0 4px 15px rgba(0, 0, 0, 0.05);
  border: 2px solid #f0f0f0;
  background: #fafafa;
  transition: all 0.3s ease;
}

.login-input :deep(.el-input__wrapper:hover) {
  border-color: #667eea;
  box-shadow: 0 4px 20px rgba(102, 126, 234, 0.15);
  background: white;
}

.login-input :deep(.el-input__wrapper.is-focus) {
  border-color: #667eea;
  box-shadow: 0 0 0 4px rgba(102, 126, 234, 0.1);
  background: white;
}

.login-input :deep(.el-input__inner) {
  font-weight: 500;
}

.login-btn {
  width: 100%;
  height: 52px;
  border-radius: 16px;
  font-size: 17px;
  font-weight: 700;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  border: none;
  transition: all 0.4s cubic-bezier(0.175, 0.885, 0.32, 1.275);
  letter-spacing: 2px;
  box-shadow: 0 8px 25px rgba(102, 126, 234, 0.35);
}

.login-btn:hover {
  transform: translateY(-3px);
  box-shadow: 0 12px 35px rgba(102, 126, 234, 0.45);
}

.login-btn:active {
  transform: translateY(-1px);
}

.reset-btn {
  width: 100%;
  height: 52px;
  border-radius: 16px;
  font-size: 17px;
  font-weight: 600;
  border: 2px solid #e5e7eb;
  color: #6b7280;
  background: white;
  transition: all 0.3s ease;
}

.reset-btn:hover {
  border-color: #667eea;
  color: #667eea;
  background: rgba(102, 126, 234, 0.08);
  transform: translateY(-2px);
}

.form-footer {
  text-align: center;
  margin-top: 32px;
  padding-top: 24px;
  border-top: 1px solid #f0f0f0;
}

.form-footer p {
  font-size: 13px;
  color: #9ca3af;
  margin: 0;
}

@media (max-width: 900px) {
  .login-content {
    flex-direction: column;
    gap: 30px;
    padding: 20px;
  }

  .login-left {
    display: none;
  }

  .login-right {
    width: 100%;
    max-width: 420px;
    margin: 0 auto;
  }

  .login-form-wrapper {
    padding: 36px 28px;
  }
}
</style>
