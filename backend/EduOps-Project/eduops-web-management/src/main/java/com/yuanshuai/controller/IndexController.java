package com.yuanshuai.controller;

import com.yuanshuai.pojo.IndexStats;
import com.yuanshuai.pojo.Result;
import com.yuanshuai.service.IndexService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/index")
public class IndexController {

    @Autowired
    private IndexService indexService;

    /**
     * 获取首页统计数据
     * @param params 包含username的JSON参数
     * @return 统计数据和员工信息
     */
    @PostMapping
    public Result index(@RequestBody Map<String, String> params) {
        String username = params.get("username");
        log.info("获取首页统计数据，用户名：{}", username);
        IndexStats stats = indexService.getIndexStats(username);
        return Result.success(stats);
    }
}