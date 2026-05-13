package com.yuanshuai.service;

import com.yuanshuai.pojo.OperateLog;
import com.yuanshuai.pojo.OperateLogQueryParam;
import com.yuanshuai.pojo.PageResult;

public interface OperateLogService {

    /**
     * 分页查询操作日志
     *
     * @param operateLogQueryParam
     * @return
     */
    PageResult<OperateLog> page(OperateLogQueryParam operateLogQueryParam);
}