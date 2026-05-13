<script setup>
import { ref, reactive } from "vue";
import { ElMessage } from "element-plus";
import { Lock, Key, Check, InfoFilled } from "@element-plus/icons-vue";
import request from "@/utils/request";

const dialogVisible = ref(false);
const formRef = ref(null);
const loading = ref(false);

const formData = reactive({
  oldPassword: "",
  newPassword: "",
  confirmPassword: "",
});

const formRules = reactive({
  oldPassword: [{ required: true, message: "请输入原密码", trigger: "blur" }],
  newPassword: [
    { required: true, message: "请输入新密码", trigger: "blur" },
    { min: 6, max: 20, message: "密码长度必须在6-20位之间", trigger: "blur" },
  ],
  confirmPassword: [
    { required: true, message: "请确认新密码", trigger: "blur" },
    {
      validator: (rule, value, callback) => {
        if (value !== formData.newPassword) {
          callback(new Error("两次输入的密码不一致"));
        } else {
          callback();
        }
      },
      trigger: "blur",
    },
  ],
});

const openDialog = () => {
  resetForm();
  dialogVisible.value = true;
};

const closeDialog = () => {
  dialogVisible.value = false;
  resetForm();
};

const resetForm = () => {
  formData.oldPassword = "";
  formData.newPassword = "";
  formData.confirmPassword = "";
  formRef.value?.resetFields();
};

const handleSubmit = async () => {
  if (!formRef.value) return;

  await formRef.value.validate(async (valid) => {
    if (valid) {
      loading.value = true;
      try {
        const response = await request.put("/login/password", {
          oldPassword: formData.oldPassword,
          newPassword: formData.newPassword,
        });

        // code=1 表示成功，code=0 表示失败
        if (response.code === 1) {
          ElMessage.success("密码修改成功，请重新登录");
          localStorage.removeItem("loginUser");
          setTimeout(() => {
            window.location.href = "/login";
          }, 1500);
        } else {
          ElMessage.error(response.msg || "修改密码失败");
        }
      } catch (error) {
        console.error("修改密码失败:", error);
        ElMessage.error("修改密码失败，请稍后重试");
      } finally {
        loading.value = false;
      }
    }
  });
};

defineExpose({
  openDialog,
});
</script>

<template>
  <el-dialog
    v-model="dialogVisible"
    title="🔐 修改密码"
    width="480px"
    :close-on-click-modal="false"
    :close-on-press-escape="false"
    class="password-dialog"
    destroy-on-close
  >
    <div class="dialog-content">
      <div class="dialog-tip">
        <el-icon><InfoFilled /></el-icon>
        <span>为了您的账户安全，请定期修改密码</span>
      </div>

      <el-form
        ref="formRef"
        :model="formData"
        :rules="formRules"
        label-width="90px"
        class="password-form"
        status-icon
      >
        <el-form-item label="原密码" prop="oldPassword">
          <el-input
            v-model="formData.oldPassword"
            type="password"
            placeholder="请输入原密码"
            show-password
            clearable
            :prefix-icon="Lock"
            size="large"
          />
        </el-form-item>

        <el-form-item label="新密码" prop="newPassword">
          <el-input
            v-model="formData.newPassword"
            type="password"
            placeholder="请输入6-20位新密码"
            show-password
            clearable
            :prefix-icon="Key"
            size="large"
          />
        </el-form-item>

        <el-form-item label="确认密码" prop="confirmPassword">
          <el-input
            v-model="formData.confirmPassword"
            type="password"
            placeholder="请再次输入新密码"
            show-password
            clearable
            :prefix-icon="Check"
            size="large"
          />
        </el-form-item>
      </el-form>
    </div>

    <template #footer>
      <div class="dialog-footer">
        <el-button @click="closeDialog" size="large">取消</el-button>
        <el-button
          type="primary"
          @click="handleSubmit"
          :loading="loading"
          size="large"
          class="submit-btn"
        >
          {{ loading ? "修改中..." : "确认修改" }}
        </el-button>
      </div>
    </template>
  </el-dialog>
</template>

<style scoped>
.dialog-content {
  padding: 10px 20px;
}

.dialog-tip {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 12px 16px;
  background: linear-gradient(135deg, #e0e7ff 0%, #c7d2fe 100%);
  border-radius: 10px;
  margin-bottom: 24px;
  color: #4338ca;
  font-size: 13px;
}

.dialog-tip .el-icon {
  font-size: 16px;
}

.password-form :deep(.el-input__wrapper) {
  border-radius: 10px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.04);
}

.password-form :deep(.el-input__wrapper:hover) {
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.08);
}

.dialog-footer {
  display: flex;
  justify-content: flex-end;
  gap: 12px;
  padding: 10px 20px 20px;
}

.submit-btn {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  border: none;
}

.submit-btn:hover {
  background: linear-gradient(135deg, #5a67d8 0%, #6b46c1 100%);
}
</style>
