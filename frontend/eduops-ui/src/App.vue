<script setup>
//引入views/layout/index.vue命名为Layout
import Layout from "@/views/layout/index.vue";
</script>

<template>
  <!-- <Layout></Layout> -->
  <router-view></router-view>
</template>

<style>
/* 全局Loading样式 */
.global-loading .el-loading-spinner {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 16px;
}

.global-loading .el-loading-spinner .circular {
  width: 50px;
  height: 50px;
  animation: loading-rotate 2s linear infinite;
}

.global-loading .el-loading-spinner .path {
  stroke-dasharray: 150, 200;
  stroke-dashoffset: 0;
  stroke-width: 3;
  stroke: #667eea;
  stroke-linecap: round;
  animation: loading-dash 1.5s ease-in-out infinite;
}

.global-loading .el-loading-text {
  font-size: 16px;
  color: #667eea;
  font-weight: 500;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
  animation: pulse 2s ease-in-out infinite;
}

@keyframes loading-rotate {
  100% {
    transform: rotate(360deg);
  }
}

@keyframes loading-dash {
  0% {
    stroke-dasharray: 1, 200;
    stroke-dashoffset: 0;
  }
  50% {
    stroke-dasharray: 90, 150;
    stroke-dashoffset: -40px;
  }
  100% {
    stroke-dasharray: 90, 150;
    stroke-dashoffset: -120px;
  }
}

@keyframes pulse {
  0%,
  100% {
    opacity: 1;
  }
  50% {
    opacity: 0.6;
  }
}
</style>
