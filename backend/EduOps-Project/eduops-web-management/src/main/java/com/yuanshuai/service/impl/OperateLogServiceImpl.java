package com.yuanshuai.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.yuanshuai.mapper.OperateLogMapper;
import com.yuanshuai.pojo.OperateLog;
import com.yuanshuai.pojo.OperateLogQueryParam;
import com.yuanshuai.pojo.PageResult;
import com.yuanshuai.service.OperateLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OperateLogServiceImpl implements OperateLogService {

    @Autowired
    private OperateLogMapper operateLogMapper;

    @Override
    public PageResult<OperateLog> page(OperateLogQueryParam operateLogQueryParam) {
        // 1. 设置分页参数
        PageHelper.startPage(operateLogQueryParam.getPage(), operateLogQueryParam.getPageSize());
        // 2. 执行查询
        List<OperateLog> operateLogList = operateLogMapper.page(operateLogQueryParam);
        // 3. 解析查询结果并封装
        Page<OperateLog> p = (Page<OperateLog>) operateLogList;
        return new PageResult<OperateLog>(p.getTotal(), p.getResult());
    }
}