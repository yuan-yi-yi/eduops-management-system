<script setup>
import { ref, onMounted } from "vue";
import { ElMessage, ElMessageBox } from "element-plus";
import { Plus, Delete, Edit, OfficeBuilding } from "@element-plus/icons-vue";
import {
  queryAllApi,
  addDeptApi,
  queryInfoApi,
  updateDeptApi,
  deleteDeptApi,
} from "@/api/dept";

// 声明列表展示数据
let tableData = ref([]);
const loading = ref(false);
const saveLoading = ref(false);

// 动态加载数据 - 查询部门
const queryAll = async () => {
  loading.value = true;
  try {
    const result = await queryAllApi();
    if (result.code) {
      tableData.value = result.data;
    }
  } finally {
    loading.value = false;
  }
};

// 钩子函数
onMounted(() => {
  queryAll();
});

const formTitle = ref("");
//新增部门
const add = () => {
  formTitle.value = "新增部门";
  showDialog.value = true;
  deptForm.value = { name: "" };
};

// 编辑部门 - 根据ID查询回显数据
const handleEdit = async (row) => {
  formTitle.value = "修改部门";
  showDialog.value = true;
  deptForm.value = { name: "" };

  const result = await queryInfoApi(row.id);
  if (result.code) {
    deptForm.value = result.data;
  }
};

// 删除部门 - 根据ID删除部门
const handleDelete = (row) => {
  ElMessageBox.confirm("此操作将永久删除该部门, 是否继续?", "提示", {
    confirmButtonText: "确定",
    cancelButtonText: "取消",
    type: "warning",
  }).then(async () => {
    const result = await deleteDeptApi(row.id);
    if (result.code) {
      ElMessage.success("删除成功");
      queryAll();
    } else {
      ElMessage.error(result.msg);
    }
  });
};

// 新增部门对话框的状态
const showDialog = ref(false);
// 表单数据
const deptForm = ref({ name: "" });
// 表单验证规则
const formRules = ref({
  name: [
    { required: true, message: "请输入部门名称", trigger: "blur" },
    { min: 2, max: 10, message: "长度在 2 到 10 个字符", trigger: "blur" },
  ],
});

// 表单引用
const deptFormRef = ref(null);

// 重置表单
const resetForm = () => {
  deptFormRef.value?.resetFields();
};

// 提交表单
const save = async () => {
  await deptFormRef.value.validate(async (valid) => {
    if (!valid) return;
    saveLoading.value = true;
    try {
      let result = null;
      if (deptForm.value.id) {
        result = await updateDeptApi(deptForm.value);
      } else {
        result = await addDeptApi(deptForm.value);
      }
      if (result.code) {
        ElMessage.success("操作成功");
        showDialog.value = false;
        resetForm();
        queryAll();
      } else {
        ElMessage.error(result.msg);
      }
    } finally {
      saveLoading.value = false;
    }
  });
};
</script>

<template>
  <div class="page-container">
    <!-- 页面标题 -->
    <div class="page-header">
      <div class="header-content">
        <el-icon :size="24" color="white"><OfficeBuilding /></el-icon>
        <span class="header-title">部门管理</span>
      </div>
    </div>

    <!-- 操作按钮 -->
    <div class="operation-bar">
      <el-button type="primary" :icon="Plus" @click="add()">新增部门</el-button>
    </div>

    <!-- 数据展示表格 -->
    <el-card shadow="hover" class="table-card">
      <el-table
        :data="tableData"
        border
        style="width: 100%"
        v-loading="loading"
      >
        <el-table-column type="index" label="序号" width="100" align="center" />
        <el-table-column
          prop="name"
          label="部门名称"
          min-width="200"
          align="center"
        />
        <el-table-column
          prop="updateTime"
          label="最后修改时间"
          min-width="200"
          align="center"
        />
        <el-table-column fixed="right" label="操作" align="center" width="200">
          <template #default="{ row }">
            <el-button
              size="small"
              type="primary"
              :icon="Edit"
              @click="handleEdit(row)"
              >修改</el-button
            >
            <el-button
              size="small"
              type="danger"
              :icon="Delete"
              @click="handleDelete(row)"
              >删除</el-button
            >
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <!-- 新增部门的对话框 -->
    <el-dialog
      v-model="showDialog"
      :title="formTitle"
      width="400px"
      @close="resetForm"
      destroy-on-close
    >
      <el-form
        :model="deptForm"
        :rules="formRules"
        ref="deptFormRef"
        label-width="80px"
      >
        <el-form-item label="部门名称" prop="name">
          <el-input
            v-model="deptForm.name"
            autocomplete="off"
            placeholder="请输入部门名称"
          ></el-input>
        </el-form-item>
      </el-form>

      <template #footer>
        <el-button @click="showDialog = false">取消</el-button>
        <el-button type="primary" @click="save" :loading="saveLoading"
          >确定</el-button
        >
      </template>
    </el-dialog>
  </div>
</template>

<style scoped>
.page-container {
  padding: 0;
}

.page-header {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  padding: 20px 24px;
  border-radius: 12px;
  margin-bottom: 20px;
  box-shadow: 0 4px 15px rgba(102, 126, 234, 0.3);
}

.header-content {
  display: flex;
  align-items: center;
  gap: 12px;
}

.header-title {
  color: white;
  font-size: 20px;
  font-weight: 600;
}

.operation-bar {
  margin-bottom: 20px;
  display: flex;
  gap: 10px;
}

.table-card {
  border-radius: 12px;
}

:deep(.el-button--primary) {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  border: none;
}

:deep(.el-button--primary:hover) {
  background: linear-gradient(135deg, #5a67d8 0%, #6b46c1 100%);
}
</style>
