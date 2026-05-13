import axios from "axios";
import { ElMessage } from "element-plus";
import router from "../router";
import { showLoading, hideLoading } from "./loading";

const request = axios.create({
  baseURL: "/api",
  timeout: 600000,
});

// 不需要loading的URL白名单
const noLoadingUrls = [
  // 可以添加不需要loading的接口路径
];

request.interceptors.request.use(
  (config) => {
    let loginUser = JSON.parse(localStorage.getItem("loginUser"));
    if (loginUser && loginUser.token) {
      config.headers.token = loginUser.token;
    }

    // 默认所有请求都显示loading，除非明确设置showLoading: false
    if (config.showLoading !== false) {
      const loadingText = config.loadingText || "数据加载中...";
      showLoading({ text: loadingText });
    }

    return config;
  },
  (error) => {
    hideLoading();
    return Promise.reject(error);
  }
);

request.interceptors.response.use(
  (response) => {
    hideLoading();
    return response.data;
  },
  (error) => {
    hideLoading();
    if (error.response) {
      const { status } = error.response;
      switch (status) {
        case 401:
          ElMessage.error("登录失效, 请重新登录");
          localStorage.removeItem("loginUser");
          router.push("/login");
          break;
        case 403:
          ElMessage.error("拒绝访问");
          break;
        case 404:
          ElMessage.error("请求资源不存在");
          break;
        case 500:
          ElMessage.error("服务器错误");
          break;
        default:
          ElMessage.error(error.response.data?.message || "请求失败");
      }
    } else {
      ElMessage.error("网络连接失败");
    }
    return Promise.reject(error);
  }
);

export default request;
