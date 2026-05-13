package com.yuanshuai.controller;

import com.yuanshuai.anno.Log;
import com.yuanshuai.pojo.Result;
import com.yuanshuai.utils.AliyunOSSOperator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;


@RestController
@RequestMapping("/upload")
@Slf4j
public class UploadController {

    // 注入文件上传工具类对应的Bean
    @Autowired
    private AliyunOSSOperator ossOperator;

    @Log
    @PostMapping
    // 上传文件
    public Result uploadFile(MultipartFile file) throws IOException {
        log.info("上传文件: {}", file);
        String ossPath = ossOperator.upload(file);
        return Result.success(ossPath);
    }
}
