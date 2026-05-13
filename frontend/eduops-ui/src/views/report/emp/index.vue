<script setup>
import { onMounted, ref } from "vue";
import * as echarts from "echarts";
import { Histogram, PieChart } from "@element-plus/icons-vue";
import { queryEmpJobDataApi, queryEmpGenderDataApi } from "@/api/report";

const loading = ref(false);

//钩子函数 - 加载报表
onMounted(() => {
  loadCharts();
});

const loadCharts = async () => {
  loading.value = true;
  try {
    await loadJobChart();
    await loadGenderChart();
  } finally {
    loading.value = false;
  }
};

//获取职位统计报表
const loadJobChart = async () => {
  let result = await queryEmpJobDataApi();
  if (result.code) {
    let jobList = result.data.jobList;
    let dataList = result.data.dataList;
    initJobChart(jobList, dataList);
  }
};

//获取性别统计报表
const loadGenderChart = async () => {
  let result = await queryEmpGenderDataApi();
  if (result.code) {
    initGenderChart(result.data);
  }
};

function initJobChart(jobList, dataList) {
  var myChart = echarts.init(document.getElementById("container1"));
  myChart.setOption({
    title: {
      text: "员工职位统计",
      textStyle: { fontSize: 18, fontWeight: "bold" },
      left: "center",
    },
    grid: { left: "3%", right: "4%", bottom: "3%", containLabel: true },
    tooltip: { trigger: "axis" },
    xAxis: { data: jobList, axisLabel: { rotate: 30 } },
    yAxis: {},
    series: [
      {
        name: "人数",
        type: "bar",
        data: dataList,
        itemStyle: {
          color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
            { offset: 0, color: "#667eea" },
            { offset: 1, color: "#764ba2" },
          ]),
          borderRadius: [8, 8, 0, 0],
        },
        barWidth: "50%",
      },
    ],
  });
}

function initGenderChart(genderDataList) {
  var myChart = echarts.init(document.getElementById("container2"));
  myChart.setOption({
    title: {
      text: "员工性别统计",
      textStyle: { fontSize: 18, fontWeight: "bold" },
      left: "center",
    },
    tooltip: { trigger: "item" },
    legend: { top: "10%", left: "center" },
    series: [
      {
        name: "性别",
        type: "pie",
        radius: ["40%", "70%"],
        avoidLabelOverlap: false,
        top: "5%",
        itemStyle: { borderRadius: 8, borderColor: "#fff", borderWidth: 2 },
        label: { show: false, position: "center" },
        emphasis: {
          label: { show: true, fontSize: 20, fontWeight: "bold" },
        },
        data: genderDataList,
      },
    ],
  });
}
</script>

<template>
  <div class="page-container">
    <!-- 页面标题 -->
    <div class="page-header">
      <div class="header-content">
        <el-icon :size="24" color="white"><Histogram /></el-icon>
        <span class="header-title">员工信息统计</span>
      </div>
    </div>

    <!-- 图表区域 -->
    <el-row :gutter="20" class="chart-row" v-loading="loading">
      <el-col :span="12">
        <el-card shadow="hover" class="chart-card">
          <div class="chart-container" id="container1"></div>
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card shadow="hover" class="chart-card">
          <div class="chart-container" id="container2"></div>
        </el-card>
      </el-col>
    </el-row>
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

.chart-row {
  margin-top: 10px;
}

.chart-card {
  border-radius: 12px;
  height: 500px;
}

.chart-container {
  width: 100%;
  height: 450px;
}
</style>
