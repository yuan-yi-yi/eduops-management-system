import { ElLoading } from "element-plus";

let loadingInstance = null;
let loadingCount = 0;

// 开启loading
export const showLoading = (options = {}) => {
  loadingCount++;
  if (loadingCount === 1) {
    loadingInstance = ElLoading.service({
      lock: true,
      text: options.text || "数据加载中...",
      background: "rgba(255, 255, 255, 0.9)",
      customClass: "global-loading",
      ...options,
    });
  }
};

// 关闭loading
export const hideLoading = () => {
  loadingCount--;
  if (loadingCount <= 0) {
    loadingCount = 0;
    if (loadingInstance) {
      loadingInstance.close();
      loadingInstance = null;
    }
  }
};

// 强制关闭所有loading
export const closeAllLoading = () => {
  loadingCount = 0;
  if (loadingInstance) {
    loadingInstance.close();
    loadingInstance = null;
  }
};

// 重置loading计数
export const resetLoadingCount = () => {
  loadingCount = 0;
};
