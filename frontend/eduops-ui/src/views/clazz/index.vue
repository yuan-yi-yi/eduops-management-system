<script setup>
import { onMounted, ref, watch } from "vue";
import {
  queryPageApi,
  addApi,
  queryInfoApi,
  updateApi,
  deleteApi,
} from "@/api/clazz";
import { queryAllApi as queryAllEmpApi } from "@/api/emp";
import { ElMessage, ElMessageBox } from "element-plus";
import {
  Search,
  Refresh,
  Plus,
  Delete,
  Edit,
  School,
} from "@element-plus/icons-vue";

//学科列表数据
const subjects = ref([
  { name: "Java", value: 1 },
  { name: "前端", value: 2 },
  { name: "大数据", value: 3 },
  { name: "Python", value: 4 },
  { name: "Go", value: 5 },
  { name: "嵌入式", value: 6 },
]);
//搜索表单对象
let searchClazz = ref({ begin: "", end: "", date: [], name: "" });
//列表展示数据
let tableData = ref([]);
//加载状态
const loading = ref(false);
const saveLoading = ref(false);

//钩子函数 - 页面加载时触发
onMounted(() => {
  queryPage();
  queryAllEmp();
});

//所有的员工数据
let emps = ref([]);
//加载所有的员工数据
const queryAllEmp = async () => {
  let result = await queryAllEmpApi();
  if (result.code) {
    emps.value = result.data;
  }
};

//分页组件
const pagination = ref({ currentPage: 1, pageSize: 10, total: 0 });
//每页展示记录数发生变化时触发
const handleSizeChange = (pageSize) => {
  pagination.value.pageSize = pageSize;
  queryPage();
};
//当前页码发生变化时触发
const handleCurrentChange = (page) => {
  pagination.value.currentPage = page;
  queryPage();
};

//分页条件查询
const queryPage = async () => {
  loading.value = true;
  try {
    const result = await queryPageApi(
      searchClazz.value.begin,
      searchClazz.value.end,
      searchClazz.value.name,
      pagination.value.currentPage,
      pagination.value.pageSize
    );

    if (result.code) {
      tableData.value = result.data.rows;
      pagination.value.total = result.data.total;
    }
  } finally {
    loading.value = false;
  }
};

//清空搜索栏
const clear = () => {
  searchClazz.value = { begin: "", end: "", date: [], name: "" };
  queryPage();
};

//监听searchEmp的date属性
watch(
  () => searchClazz.value.date,
  (newVal, oldVal) => {
    if (newVal && newVal.length > 0) {
      searchClazz.value.begin = newVal[0];
      searchClazz.value.end = newVal[1];
    } else {
      searchClazz.value.begin = "";
      searchClazz.value.end = "";
    }
  }
);

//----------- 新增 / 修改 ---------------------------
let dialogFormVisible = ref(false);
let labelWidth = ref(80);
let formTitle = ref("");
let clazz = ref({
  id: "",
  name: "",
  room: "",
  beginDate: "",
  endDate: "",
  subject: "",
  masterId: "",
});

//清空表单
const clearClazz = () => {
  clazz.value = {
    id: "",
    name: "",
    room: "",
    beginDate: "",
    endDate: "",
    subject: "",
    masterId: "",
  };
};

//新增班级
const addClazz = () => {
  dialogFormVisible.value = true;
  formTitle.value = "新增班级";
  clearClazz();
};

//修改班级
const updateClazz = async (id) => {
  clearClazz();
  dialogFormVisible.value = true;
  formTitle.value = "修改班级";
  let result = await queryInfoApi(id);
  if (result.code) {
    clazz.value = result.data;
  }
};

//表单校验规则
const clazzFormRef = ref();
const rules = ref({
  name: [
    { required: true, message: "班级名称为必填项", trigger: "blur" },
    { min: 4, max: 30, message: "用户名长度为4-30个字", trigger: "blur" },
  ],
  room: [
    { min: 1, max: 20, message: "班级教室长度为1-20个字", trigger: "blur" },
  ],
  beginDate: [
    { required: true, message: "开课时间为必填项", trigger: "change" },
  ],
  endDate: [{ required: true, message: "结课时间为必填项", trigger: "change" }],
  subject: [{ required: true, message: "学科为必填项", trigger: "change" }],
});

//重置表单
const resetForm = (clazzForm) => {
  if (!clazzForm) return;
  clazzForm.resetFields();
};

//-------------保存班级信息
const save = (clazzForm) => {
  if (!clazzForm) return;
  clazzForm.validate(async (valid) => {
    if (valid) {
      saveLoading.value = true;
      try {
        let api;
        if (clazz.value.id) {
          api = updateApi(clazz.value);
        } else {
          api = addApi(clazz.value);
        }

        let result = await api;
        if (result.code) {
          ElMessage.success("操作成功");
          dialogFormVisible.value = false;
          queryPage();
        } else {
          ElMessage.error(result.msg);
        }
      } finally {
        saveLoading.value = false;
      }
    } else {
      return false;
    }
  });
};

//------- 删除班级
const delById = async (id) => {
  ElMessageBox.confirm("您确认删除此数据吗?", "删除班级", {
    confirmButtonText: "确认",
    cancelButtonText: "取消",
    type: "warning",
  })
    .then(async () => {
      let result = await deleteApi(id);
      if (result.code) {
        ElMessage.success("删除成功");
        queryPage();
      } else {
        ElMessage.error(result.msg);
      }
    })
    .catch(() => {
      ElMessage.info("取消删除");
    });
};
</script>

<template>
  <div class="page-container">
    <!-- 页面标题 -->
    <div class="page-header">
      <div class="header-content">
        <el-icon :size="24" color="white"><School /></el-icon>
        <span class="header-title">班级管理</span>
      </div>
    </div>

    <!-- 搜索区域 -->
    <el-card shadow="hover" class="search-card">
      <el-form :inline="true" :model="searchClazz" class="search-form">
        <el-form-item label="班级名称">
          <el-input
            v-model="searchClazz.name"
            placeholder="请输入班级名称"
            clearable
          />
        </el-form-item>

        <el-form-item label="结课时间">
          <el-date-picker
            v-model="searchClazz.date"
            type="daterange"
            range-separator=" 至 "
            start-placeholder="开始时间"
            end-placeholder="结束时间"
            value-format="YYYY-MM-DD"
          />
        </el-form-item>

        <el-form-item>
          <el-button type="primary" :icon="Search" @click="queryPage()"
            >查询</el-button
          >
          <el-button :icon="Refresh" @click="clear()">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <!-- 操作按钮 -->
    <div class="operation-bar">
      <el-button
        type="primary"
        :icon="Plus"
        @click="
          addClazz();
          resetForm(clazzFormRef);
        "
        >新增班级</el-button
      >
    </div>

    <!-- 列表展示 -->
    <el-card shadow="hover" class="table-card">
      <el-table
        :data="tableData"
        border
        style="width: 100%"
        fit
        v-loading="loading"
      >
        <el-table-column type="index" label="序号" width="55" align="center" />
        <el-table-column
          prop="name"
          label="班级名称"
          align="center"
          width="200"
        />
        <el-table-column
          prop="room"
          label="班级教室"
          align="center"
          width="100"
        />
        <el-table-column
          prop="masterName"
          label="班主任"
          align="center"
          width="100"
        />
        <el-table-column
          prop="beginDate"
          label="开课时间"
          align="center"
          width="150"
        />
        <el-table-column
          prop="endDate"
          label="结课时间"
          align="center"
          width="150"
        />
        <el-table-column
          prop="status"
          label="状态"
          align="center"
          width="130"
        />
        <el-table-column
          prop="updateTime"
          label="最后修改时间"
          align="center"
        />
        <el-table-column label="操作" align="center" width="200" fixed="right">
          <template #default="scope">
            <el-button
              type="primary"
              size="small"
              :icon="Edit"
              @click="
                updateClazz(scope.row.id);
                resetForm(clazzFormRef);
              "
              >编辑</el-button
            >
            <el-button
              type="danger"
              size="small"
              :icon="Delete"
              @click="delById(scope.row.id)"
              >删除</el-button
            >
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页组件 -->
      <div class="pagination-wrapper">
        <el-pagination
          v-model:current-page="pagination.currentPage"
          v-model:page-size="pagination.pageSize"
          :page-sizes="[5, 10, 20, 50, 100]"
          layout="total, sizes, prev, pager, next, jumper"
          :total="pagination.total"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
        />
      </div>
    </el-card>

    <!-- 新增/修改班级对话框 -->
    <el-dialog
      v-model="dialogFormVisible"
      :title="formTitle"
      width="35%"
      destroy-on-close
    >
      <el-form
        :model="clazz"
        ref="clazzFormRef"
        :rules="rules"
        label-width="80px"
      >
        <el-form-item label="班级名称" prop="name">
          <el-input v-model="clazz.name" placeholder="请输入班级名称" />
        </el-form-item>

        <el-form-item label="班级教室" prop="room">
          <el-input v-model="clazz.room" placeholder="请输入班级教室" />
        </el-form-item>

        <el-form-item label="开课时间" prop="beginDate">
          <el-date-picker
            v-model="clazz.beginDate"
            type="date"
            placeholder="请选择开课时间"
            value-format="YYYY-MM-DD"
            style="width: 100%"
          />
        </el-form-item>

        <el-form-item label="结课时间" prop="endDate">
          <el-date-picker
            v-model="clazz.endDate"
            type="date"
            placeholder="请选择结课时间"
            value-format="YYYY-MM-DD"
            style="width: 100%"
          />
        </el-form-item>

        <el-form-item label="班主任">
          <el-select
            v-model="clazz.masterId"
            placeholder="请选择班主任"
            style="width: 100%"
          >
            <el-option v-for="emp in emps" :label="emp.name" :value="emp.id" />
          </el-select>
        </el-form-item>

        <el-form-item label="学科" prop="subject">
          <el-select
            v-model="clazz.subject"
            placeholder="请选择学科"
            style="width: 100%"
          >
            <el-option
              v-for="sub in subjects"
              :label="sub.name"
              :value="sub.value"
            />
          </el-select>
        </el-form-item>
      </el-form>

      <template #footer>
        <el-button
          @click="
            dialogFormVisible = false;
            resetForm(clazzFormRef);
          "
          >取消</el-button
        >
        <el-button
          type="primary"
          @click="save(clazzFormRef)"
          :loading="saveLoading"
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
</style>
