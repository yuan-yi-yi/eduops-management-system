package com.yuanshuai.service;

import com.yuanshuai.pojo.IndexStats;

public interface IndexService {
    /**
     * 获取首页统计数据
     * @param username 用户名
     * @return 统计数据和员工信息
     */
    IndexStats getIndexStats(String username);
}