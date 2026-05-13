<script setup>
import { ref, reactive } from "vue";
import { useRouter } from "vue-router";
import { ElMessage } from "element-plus";
import { updatePasswordApi } from "@/api/login";

const router = useRouter();
const formRef = ref(null);
const loading = ref(false);

const form = reactive({
  oldPassword: "",
  newPassword: "",
  confirmPassword: "",
});

const validateConfirmPassword = (rule, value, callback) => {
  if (value === "") {
    callback(new Error("请再次输入新密码"));
  } else if (value !== form.newPassword) {
    callback(new Error("两次输入的密码不一致"));
  } else {
    callback();
  }
};

const rules = {
  oldPassword: [{ required: true, message: "请输入原密码", trigger: "blur" }],
  newPassword: [
    { required: true, message: "请输入新密码", trigger: "blur" },
    { min: 6, max: 20, message: "密码长度在6到20个字符", trigger: "blur" },
  ],
  confirmPassword: [
    { required: true, message: "请确认新密码", trigger: "blur" },
    { validator: validateConfirmPassword, trigger: "blur" },
  ],
};

const handleSubmit = async () => {
  if (!formRef.value) return;

  await formRef.value.validate(async (valid) => {
    if (valid) {
      loading.value = true;
      try {
        console.log("Sending password update request:", {
          oldPassword: form.oldPassword,
          newPassword: form.newPassword,
        });

        const res = await updatePasswordApi({
          oldPassword: form.oldPassword,
          newPassword: form.newPassword,
        });

        console.log("Password update response:", res);

        // 兼容不同的响应格式：code为0或200表示成功
        if (res.code === 0 || res.code === 200) {
          ElMessage.success("密码修改成功，请重新登录");
          localStorage.removeItem("loginUser");
          router.push("/login");
        } else {
          // 兼容 message 和 msg 字段
          const errorMsg = res.message || res.msg || "密码修改失败";
          ElMessage.error(errorMsg);
        }
      } catch (error) {
        console.error("修改密码失败:", error);
        ElMessage.error("网络请求失败，请稍后重试");
      } finally {
        loading.value = false;
      }
    }
  });
};

const handleReset = () => {
  formRef.value?.resetFields();
};
</script>

<template>
  <div class="password-container">
    <el-card class="password-card">
      <template #header>
        <div class="card-header">
          <span>修改密码</span>
        </div>
      </template>

      <el-form
        ref="formRef"
        :model="form"
        :rules="rules"
        label-width="100px"
        class="password-form"
      >
        <el-form-item label="原密码" prop="oldPassword">
          <el-input
            v-model="form.oldPassword"
            type="password"
            placeholder="请输入原密码"
            show-password
          />
        </el-form-item>

        <el-form-item label="新密码" prop="newPassword">
          <el-input
            v-model="form.newPassword"
            type="password"
            placeholder="请输入新密码（6-20位）"
            show-password
          />
        </el-form-item>

        <el-form-item label="确认密码" prop="confirmPassword">
          <el-input
            v-model="form.confirmPassword"
            type="password"
            placeholder="请再次输入新密码"
            show-password
          />
        </el-form-item>

        <el-form-item>
          <el-button type="primary" @click="handleSubmit" :loading="loading">
            确定
          </el-button>
          <el-button @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<style scoped>
.password-container {
  padding: 20px;
  display: flex;
  justify-content: center;
  align-items: flex-start;
  min-height: calc(100vh - 100px);
}

.password-card {
  width: 500px;
  margin-top: 50px;
}

.card-header {
  font-size: 18px;
  font-weight: bold;
  text-align: center;
}

.password-form {
  margin-top: 20px;
}
</style>
