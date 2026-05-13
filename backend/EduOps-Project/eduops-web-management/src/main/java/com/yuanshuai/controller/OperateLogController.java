package com.yuanshuai.controller;

import com.yuanshuai.pojo.OperateLog;
import com.yuanshuai.pojo.OperateLogQueryParam;
import com.yuanshuai.pojo.PageResult;
import com.yuanshuai.pojo.Result;
import com.yuanshuai.service.OperateLogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;


@Slf4j
@RequestMapping("/log/page")
@RestController
public class OperateLogController {
    @Autowired
    private OperateLogService operateLogService;

    @GetMapping
    public Result page(OperateLogQueryParam operateLogQueryParam) {
        log.info("分页查询：{}", operateLogQueryParam);
        PageResult<OperateLog> operateLogList = operateLogService.page(operateLogQueryParam);
        return Result.success(operateLogList);
    }
}