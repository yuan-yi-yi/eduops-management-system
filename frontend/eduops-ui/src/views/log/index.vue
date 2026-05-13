<script setup>
import { onMounted, ref } from "vue";
import { Document } from "@element-plus/icons-vue";
import { queryPageApi } from "@/api/log";

//列表展示数据
let tableData = ref([]);
const loading = ref(false);

//钩子函数 - 页面加载时触发
onMounted(() => {
  queryPage();
});

//分页组件
const pagination = ref({ currentPage: 1, pageSize: 15, total: 0 });
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
</script>

<template>
  <div class="page-container">
    <!-- 页面标题 -->
    <div class="page-header">
      <div class="header-content">
        <el-icon :size="24" color="white"><Document /></el-icon>
        <span class="header-title">日志管理</span>
      </div>
    </div>

    <!-- 列表展示 -->
    <el-card shadow="hover" class="table-card">
      <el-table
        :data="tableData"
        border
        style="width: 100%"
        fit
        size="small"
        v-loading="loading"
      >
        <el-table-column
          prop="operateEmpName"
          label="操作人"
          align="center"
          width="100"
        />
        <el-table-column
          prop="operateTime"
          label="操作时间"
          align="center"
          width="160"
        />
        <el-table-column
          prop="className"
          label="类名"
          align="center"
          min-width="250"
        />
        <el-table-column
          prop="methodName"
          label="方法名"
          align="center"
          width="120"
        />
        <el-table-column
          prop="costTime"
          label="操作耗时(ms)"
          align="center"
          width="110"
        />
        <el-table-column
          prop="methodParams"
          label="请求参数"
          align="center"
          min-width="200"
        >
          <template #default="scope">
            <el-popover
              effect="light"
              trigger="hover"
              placement="top"
              width="auto"
              popper-style="font-size:12px"
            >
              <template #default>
                <div>参数: {{ scope.row.methodParams }}</div>
              </template>
              <template #reference>
                <el-tag
                  v-if="
                    scope.row.methodParams &&
                    scope.row.methodParams.length <= 30
                  "
                  size="small"
                  >{{ scope.row.methodParams }}</el-tag
                >
                <el-tag v-else size="small">{{
                  scope.row.methodParams
                    ? scope.row.methodParams.substring(0, 30) + "..."
                    : ""
                }}</el-tag>
              </template>
            </el-popover>
          </template>
        </el-table-column>
        <el-table-column
          prop="returnValue"
          label="返回值"
          align="center"
          min-width="150"
        >
          <template #default="scope">
            <el-popover
              effect="light"
              trigger="hover"
              placement="top"
              width="auto"
              popper-style="font-size:12px"
            >
              <template #default>
                <div>返回值: {{ scope.row.returnValue }}</div>
              </template>
              <template #reference>
                <el-tag
                  v-if="
                    scope.row.returnValue && scope.row.returnValue.length <= 30
                  "
                  size="small"
                  type="success"
                  >{{ scope.row.returnValue }}</el-tag
                >
                <el-tag v-else size="small" type="success">{{
                  scope.row.returnValue
                    ? scope.row.returnValue.substring(0, 30) + "..."
                    : ""
                }}</el-tag>
              </template>
            </el-popover>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页组件 -->
      <div class="pagination-wrapper">
        <el-pagination
          v-model:current-page="pagination.currentPage"
          v-model:page-size="pagination.pageSize"
          :page-sizes="[10, 20, 50, 100]"
          layout="total, sizes, prev, pager, next, jumper"
          :total="pagination.total"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
        />
      </div>
    </el-card>
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
</style>
