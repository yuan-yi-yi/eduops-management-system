import request from "@/utils/request";

// 首页统计数据
export const getIndexStatsApi = (data) => request.post("/index", data);
