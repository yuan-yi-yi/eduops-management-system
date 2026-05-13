<script setup>
import { ref, watch, onMounted } from "vue";
import { ElMessage, ElMessageBox } from "element-plus";
import {
  Search,
  Refresh,
  Plus,
  Delete,
  Edit,
  UserFilled,
} from "@element-plus/icons-vue";
import {
  queryPageApi,
  addEmpApi,
  queryInfoApi,
  updateEmpApi,
  deleteEmpApi,
} from "@/api/emp";
import { queryAllApi as queryAllDeptApi } from "@/api/dept";

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

// 获取头像颜色
const getAvatarColor = (name) => {
  const index = name ? name.length % avatarColors.length : 0;
  return avatarColors[index];
};

// 获取名字首字母
const getNameInitial = (name) => {
  if (!name) return "用";
  return name.charAt(0).toUpperCase();
};

//职位列表数据
const jobs = ref([
  { name: "班主任", value: 1 },
  { name: "讲师", value: 2 },
  { name: "学工主管", value: 3 },
  { name: "教研主管", value: 4 },
  { name: "咨询师", value: 5 },
  { name: "其他", value: 6 },
]);
//性别列表数据
const genders = ref([
  { name: "男", value: 1 },
  { name: "女", value: 2 },
]);
//部门列表数据
const deptList = ref([]);

const searchEmp = ref({
  name: "",
  gender: "",
  date: [],
  begin: "",
  end: "",
});

//加载状态
const loading = ref(false);
const saveLoading = ref(false);

//侦听searchEmp中的date属性
watch(
  () => searchEmp.value.date,
  (newValue, oldValue) => {
    if (newValue && newValue.length == 2) {
      searchEmp.value.begin = newValue[0];
      searchEmp.value.end = newValue[1];
    } else {
      searchEmp.value.begin = "";
      searchEmp.value.end = "";
    }
  }
);

//声明token
const token = ref("");

onMounted(async () => {
  handleSearch();

  //加载所有部门数据
  const result = await queryAllDeptApi();
  if (result.code) {
    deptList.value = result.data;
  }

  //加载localStorage存储的员工登录信息
  const loginUser = localStorage.getItem("loginUser");
  if (loginUser) {
    token.value = JSON.parse(loginUser).token;
  }
});

//查询员工
const handleSearch = async () => {
  loading.value = true;
  try {
    const result = await queryPageApi(
      searchEmp.value.name,
      searchEmp.value.gender,
      searchEmp.value.begin,
      searchEmp.value.end,
      currentPage.value,
      pageSize.value
    );
    if (result.code) {
      empList.value = result.data.rows;
      total.value = result.data.total;
    }
  } finally {
    loading.value = false;
  }
};

const handleReset = () => {
  searchEmp.value = {
    name: "",
    gender: "",
    date: [],
    begin: "",
    end: "",
  };
  handleSearch();
};

// 示例数据
const empList = ref([]);

// 分页配置
const currentPage = ref(1);
const pageSize = ref(10);
const total = ref(0);

// 职位转换函数
const getJobTitle = (job) => {
  switch (job) {
    case 1:
      return "班主任";
    case 2:
      return "讲师";
    case 3:
      return "学工主管";
    case 4:
      return "教研主管";
    case 5:
      return "咨询师";
    default:
      return "其他";
  }
};

// 分页处理
const handleSizeChange = (val) => {
  pageSize.value = val;
  handleSearch();
};
const handleCurrentChange = (val) => {
  currentPage.value = val;
  handleSearch();
};

// 操作处理
const handleEdit = async (id) => {
  const result = await queryInfoApi(id);
  if (result.code) {
    dialogVisible.value = true;
    dialogTitle.value = "修改员工";
    employee.value = result.data;

    //处理返回的工作经历信息
    let exprList = employee.value.exprList;
    if (exprList && exprList.length > 0) {
      exprList.forEach((expr) => {
        expr.exprDate = [expr.begin, expr.end];
      });
    }
  }
};

// 删除单个员工
const handleDelete = async (id) => {
  ElMessageBox.confirm("确定删除该员工吗?", "提示", {
    confirmButtonText: "确定",
    cancelButtonText: "取消",
    type: "warning",
  }).then(async () => {
    const result = await deleteEmpApi(id);
    if (result.code) {
      ElMessage.success("删除员工成功");
      handleSearch();
    } else {
      ElMessage.error(result.msg);
    }
  });
};

//新增员工
const addEmp = () => {
  dialogVisible.value = true;
  dialogTitle.value = "新增员工";
  employee.value = {
    username: "",
    name: "",
    gender: "",
    phone: "",
    job: "",
    salary: "",
    deptId: "",
    entryDate: "",
    image: "",
    exprList: [],
  };
  employeeFormRef.value?.resetFields();
};

//新增/修改表单
const employeeFormRef = ref(null);
const employee = ref({
  username: "",
  name: "",
  gender: "",
  phone: "",
  job: "",
  salary: "",
  deptId: "",
  entryDate: "",
  image: "",
  exprList: [],
});

//表单校验规则
const rules = ref({
  username: [
    { required: true, message: "请输入用户名", trigger: "blur" },
    {
      min: 2,
      max: 20,
      message: "用户名长度应在2到20个字符之间",
      trigger: "blur",
    },
  ],
  name: [
    { required: true, message: "请输入姓名", trigger: "blur" },
    {
      min: 2,
      max: 10,
      message: "姓名长度应在2到10个字符之间",
      trigger: "blur",
    },
  ],
  gender: [{ required: true, message: "请选择性别", trigger: "change" }],
  phone: [
    { required: true, message: "请输入手机号", trigger: "blur" },
    { pattern: /^1\d{10}$/, message: "请输入有效的手机号", trigger: "blur" },
  ],
});

// 控制弹窗
const dialogVisible = ref(false);
const dialogTitle = ref("新增员工");

// 图片上传成功后触发
const handleAvatarSuccess = (response, uploadFile) => {
  employee.value.image = response.data;
};
// 文件上传之前触发
const beforeAvatarUpload = (rawFile) => {
  if (rawFile.type !== "image/jpeg" && rawFile.type !== "image/png") {
    ElMessage.error("只支持上传图片");
    return false;
  } else if (rawFile.size / 1024 / 1024 > 10) {
    ElMessage.error("只能上传10M以内图片");
    return false;
  }
  return true;
};

//工作经历
const addExprItem = () => {
  employee.value.exprList.push({
    exprDate: [],
    begin: "",
    end: "",
    company: "",
    job: "",
  });
};

const delExprItem = (index) => {
  employee.value.exprList.splice(index, 1);
};

//监听-employee员工对象中的工作经历数据
watch(
  employee,
  (newVal, oldVal) => {
    if (employee.value.exprList) {
      employee.value.exprList.forEach((expr) => {
        expr.begin = expr.exprDate[0];
        expr.end = expr.exprDate[1];
      });
    }
  },
  { deep: true }
);

//保存员工信息
const save = async () => {
  employeeFormRef.value.validate(async (valid) => {
    if (valid) {
      saveLoading.value = true;
      try {
        let result;
        if (employee.value.id) {
          result = await updateEmpApi(employee.value);
        } else {
          result = await addEmpApi(employee.value);
        }
        if (result.code) {
          ElMessage.success(
            employee.value.id ? "修改员工成功" : "新增员工成功"
          );
          dialogVisible.value = false;
          handleSearch();
        } else {
          ElMessage.error(result.msg);
        }
      } finally {
        saveLoading.value = false;
      }
    }
  });
};

// 存储选中的 ID
const selectedIds = ref([]);

// 处理复选框选择变化的函数
function handleSelectionChange(selection) {
  const ids = selection.map((item) => item.id);
  selectedIds.value = ids;
}

//批量删除
const deleteByIds = async () => {
  if (selectedIds.value.length === 0) {
    ElMessage.warning("请选择要删除的员工");
    return;
  }
  ElMessageBox.confirm("确定删除选中员工吗?", "提示", {
    confirmButtonText: "确定",
    cancelButtonText: "取消",
    type: "warning",
  }).then(async () => {
    const result = await deleteEmpApi(selectedIds.value);
    if (result.code) {
      ElMessage.success("删除员工成功");
      handleSearch();
    } else {
      ElMessage.error(result.msg);
    }
  });
};
</script>

<template>
  <div class="page-container">
    <!-- 页面标题 -->
    <div class="page-header">
      <div class="header-content">
        <el-icon :size="24" color="white"><UserFilled /></el-icon>
        <span class="header-title">员工管理</span>
      </div>
    </div>

    <!-- 搜索区域 -->
    <el-card shadow="hover" class="search-card">
      <el-form :inline="true" :model="searchEmp" class="search-form">
        <el-form-item label="姓名">
          <el-input
            v-model="searchEmp.name"
            placeholder="请输入员工姓名"
            clearable
          />
        </el-form-item>

        <el-form-item label="性别">
          <el-select v-model="searchEmp.gender" placeholder="请选择" clearable>
            <el-option label="男" value="1"></el-option>
            <el-option label="女" value="2"></el-option>
          </el-select>
        </el-form-item>

        <el-form-item label="入职日期">
          <el-date-picker
            v-model="searchEmp.date"
            type="daterange"
            range-separator="至"
            start-placeholder="开始日期"
            end-placeholder="结束日期"
            value-format="YYYY-MM-DD"
          />
        </el-form-item>

        <el-form-item>
          <el-button type="primary" :icon="Search" @click="handleSearch"
            >查询</el-button
          >
          <el-button :icon="Refresh" @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <!-- 操作按钮 -->
    <div class="operation-bar">
      <el-button type="primary" :icon="Plus" @click="addEmp"
        >新增员工</el-button
      >
      <el-button type="danger" :icon="Delete" @click="deleteByIds"
        >批量删除</el-button
      >
    </div>

    <!-- 表格 -->
    <el-card shadow="hover" class="table-card">
      <el-table
        :data="empList"
        border
        style="width: 100%"
        @selection-change="handleSelectionChange"
        v-loading="loading"
      >
        <el-table-column
          type="selection"
          width="55"
          align="center"
        ></el-table-column>
        <el-table-column
          prop="name"
          label="姓名"
          width="120"
          align="center"
        ></el-table-column>
        <el-table-column label="性别" width="80" align="center">
          <template #default="scope">
            <el-tag
              :type="scope.row.gender == 1 ? 'success' : 'danger'"
              size="small"
            >
              {{ scope.row.gender == 1 ? "男" : "女" }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="头像" width="100" align="center">
          <template #default="scope">
            <div class="table-avatar-wrapper">
              <el-avatar
                v-if="scope.row.image"
                :size="40"
                :src="scope.row.image"
                class="table-avatar"
              />
              <div
                v-else
                class="table-avatar-fallback"
                :style="{ background: getAvatarColor(scope.row.name) }"
              >
                <span class="table-avatar-initial">{{
                  getNameInitial(scope.row.name)
                }}</span>
              </div>
            </div>
          </template>
        </el-table-column>
        <el-table-column
          prop="deptName"
          label="部门名称"
          width="150"
          align="center"
        ></el-table-column>
        <el-table-column label="职位" width="120" align="center">
          <template #default="scope">
            <el-tag size="small" effect="plain">{{
              getJobTitle(scope.row.job)
            }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column
          prop="entryDate"
          label="入职日期"
          width="150"
          align="center"
        ></el-table-column>
        <el-table-column
          prop="updateTime"
          label="最后操作时间"
          width="180"
          align="center"
        ></el-table-column>
        <el-table-column label="操作" fixed="right" align="center" width="200">
          <template #default="scope">
            <el-button
              size="small"
              type="primary"
              :icon="Edit"
              @click="handleEdit(scope.row.id)"
              >编辑</el-button
            >
            <el-button
              size="small"
              type="danger"
              :icon="Delete"
              @click="handleDelete(scope.row.id)"
              >删除</el-button
            >
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页 -->
      <div class="pagination-wrapper">
        <el-pagination
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
          :current-page="currentPage"
          :page-sizes="[10, 20, 30, 40]"
          :page-size="pageSize"
          layout="total, sizes, prev, pager, next, jumper"
          :total="total"
        />
      </div>
    </el-card>

    <!-- 新增/修改员工的对话框 -->
    <el-dialog
      v-model="dialogVisible"
      :title="dialogTitle"
      width="60%"
      destroy-on-close
    >
      <el-form
        ref="employeeFormRef"
        :model="employee"
        :rules="rules"
        label-width="80px"
      >
        <!-- 基本信息 -->
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="用户名" prop="username">
              <el-input
                v-model="employee.username"
                placeholder="请输入员工用户名，2-20个字"
              ></el-input>
            </el-form-item>
          </el-col>

          <el-col :span="12">
            <el-form-item label="姓名" prop="name">
              <el-input
                v-model="employee.name"
                placeholder="请输入员工姓名，2-10个字"
              ></el-input>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="性别" prop="gender">
              <el-select
                v-model="employee.gender"
                placeholder="请选择性别"
                style="width: 100%"
              >
                <el-option
                  v-for="gender in genders"
                  :key="gender.name"
                  :label="gender.name"
                  :value="gender.value"
                ></el-option>
              </el-select>
            </el-form-item>
          </el-col>

          <el-col :span="12">
            <el-form-item label="手机号" prop="phone">
              <el-input
                v-model="employee.phone"
                placeholder="请输入员工手机号"
              ></el-input>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="职位">
              <el-select
                v-model="employee.job"
                placeholder="请选择职位"
                style="width: 100%"
              >
                <el-option
                  v-for="job in jobs"
                  :key="job.name"
                  :label="job.name"
                  :value="job.value"
                ></el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="薪资" prop="salary">
              <el-input
                v-model="employee.salary"
                placeholder="请输入员工薪资"
              ></el-input>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="所属部门">
              <el-select
                v-model="employee.deptId"
                placeholder="请选择部门"
                style="width: 100%"
              >
                <el-option
                  v-for="dept in deptList"
                  :key="dept.id"
                  :label="dept.name"
                  :value="dept.id"
                ></el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="入职日期">
              <el-date-picker
                v-model="employee.entryDate"
                type="date"
                style="width: 100%"
                placeholder="选择日期"
                format="YYYY-MM-DD"
                value-format="YYYY-MM-DD"
              ></el-date-picker>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="24">
            <el-form-item label="头像">
              <el-upload
                class="avatar-uploader"
                action="/api/upload"
                :headers="{ token: token }"
                :show-file-list="false"
                :on-success="handleAvatarSuccess"
                :before-upload="beforeAvatarUpload"
              >
                <img
                  v-if="employee.image"
                  :src="employee.image"
                  class="avatar"
                />
                <el-icon v-else class="avatar-uploader-icon"><Plus /></el-icon>
              </el-upload>
            </el-form-item>
          </el-col>
        </el-row>

        <!-- 工作经历 -->
        <el-row :gutter="10">
          <el-col :span="24">
            <el-form-item label="工作经历">
              <el-button type="success" size="small" @click="addExprItem"
                >+ 添加工作经历</el-button
              >
            </el-form-item>
          </el-col>
        </el-row>

        <el-row
          :gutter="3"
          v-for="(expr, index) in employee.exprList"
          :key="index"
        >
          <el-col :span="10">
            <el-form-item size="small" label="时间" label-width="80px">
              <el-date-picker
                type="daterange"
                v-model="expr.exprDate"
                range-separator="至"
                start-placeholder="开始日期"
                end-placeholder="结束日期"
                format="YYYY-MM-DD"
                value-format="YYYY-MM-DD"
              ></el-date-picker>
            </el-form-item>
          </el-col>

          <el-col :span="6">
            <el-form-item size="small" label="公司" label-width="60px">
              <el-input
                placeholder="请输入公司名称"
                v-model="expr.company"
              ></el-input>
            </el-form-item>
          </el-col>

          <el-col :span="6">
            <el-form-item size="small" label="职位" label-width="60px">
              <el-input placeholder="请输入职位" v-model="expr.job"></el-input>
            </el-form-item>
          </el-col>

          <el-col :span="2">
            <el-form-item size="small" label-width="0px">
              <el-button type="danger" @click="delExprItem(index)"
                >- 删除</el-button
              >
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>

      <!-- 底部按钮 -->
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="save" :loading="saveLoading"
          >保存</el-button
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

.search-card {
  margin-bottom: 20px;
  border-radius: 12px;
}

.search-form {
  display: flex;
  flex-wrap: wrap;
  gap: 10px;
}

.operation-bar {
  margin-bottom: 20px;
  display: flex;
  gap: 10px;
}

.table-card {
  border-radius: 12px;
}

.pagination-wrapper {
  display: flex;
  justify-content: flex-end;
  margin-top: 20px;
  padding-top: 20px;
  border-top: 1px solid #f0f0f0;
}

:deep(.el-button--primary) {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  border: none;
}

:deep(.el-button--primary:hover) {
  background: linear-gradient(135deg, #5a67d8 0%, #6b46c1 100%);
}

.avatar-uploader .avatar {
  width: 78px;
  height: 78px;
  display: block;
  border-radius: 8px;
}

.avatar-uploader .el-upload {
  border: 1px dashed var(--el-border-color);
  border-radius: 8px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
  transition: var(--el-transition-duration-fast);
}

.avatar-uploader .el-upload:hover {
  border-color: var(--el-color-primary);
}

.el-icon.avatar-uploader-icon {
  font-size: 28px;
  color: #8c939d;
  width: 78px;
  height: 78px;
  text-align: center;
  border: 1px dashed var(--el-border-color);
  border-radius: 8px;
}

/* 表格头像样式 */
.table-avatar-wrapper {
  display: inline-flex;
}

.table-avatar {
  border: 2px solid rgba(102, 126, 234, 0.3);
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  transition: transform 0.2s, box-shadow 0.2s;
}

.table-avatar:hover {
  transform: scale(1.1);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
}

.table-avatar-fallback {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  border: 2px solid rgba(102, 126, 234, 0.3);
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  transition: transform 0.2s, box-shadow 0.2s;
}

.table-avatar-fallback:hover {
  transform: scale(1.1);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
}

.table-avatar-initial {
  color: white;
  font-weight: 600;
  font-size: 16px;
}
</style>
