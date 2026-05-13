package com.yuanshuai.utils;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
// ConfigurationProperties 注解用于将 application.yml 中的配置项映射到类的属性上
@ConfigurationProperties(prefix = "aliyun.oss")
public class AliyunOSSProperties {
    private String endpoint;
    private String bucketName;
    private String region;
    private String cdnDomain;    // 新增这一行
}
