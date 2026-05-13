package com.yuanshuai.utils;           // 包路径

import com.aliyun.oss.ClientBuilderConfiguration;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.common.auth.CredentialsProviderFactory;
import com.aliyun.oss.common.auth.EnvironmentVariableCredentialsProvider;
import com.aliyun.oss.common.comm.SignVersion;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Component                           // Spring：把这个类交给容器管理
public class AliyunOSSOperator {

    @Autowired                         // Spring：自动注入AliyunOSSProperties对象
    private AliyunOSSProperties ossProperties;  // 配置对象，包含endpoint/bucketName/region

    private OSS ossClient;             // OSS客户端，真正操作阿里云OSS的对象

    /**
     * 初始化方法：对象创建后自动执行，只执行一次
     * 作用：创建OSS客户端，后续上传复用，不用每次都创建
     */
    @PostConstruct
    public void init() throws Exception {
        // 从系统环境变量读取AccessKeyId和AccessKeySecret
        // 需要提前设置环境变量：OSS_ACCESS_KEY_ID 和 OSS_ACCESS_KEY_SECRET
        EnvironmentVariableCredentialsProvider credentialsProvider =
                CredentialsProviderFactory.newEnvironmentVariableCredentialsProvider();

        // 客户端配置对象
        ClientBuilderConfiguration clientBuilderConfiguration = new ClientBuilderConfiguration();
        // 设置签名版本为V4（新版推荐，更安全）
        clientBuilderConfiguration.setSignatureVersion(SignVersion.V4);

        // 构建OSS客户端
        ossClient = OSSClientBuilder.create()
                .endpoint(ossProperties.getEndpoint())           // 服务器地址
                .credentialsProvider(credentialsProvider)        // 访问凭证
                .clientConfiguration(clientBuilderConfiguration) // 客户端配置
                .region(ossProperties.getRegion())               // 地域
                .build();                                        // 创建完成
    }

    /**
     * 销毁方法：对象销毁前自动执行
     * 作用：关闭OSS客户端，释放资源
     */
    @PreDestroy
    public void destroy() {
        if (ossClient != null) {       // 如果不为空
            ossClient.shutdown();      // 关闭客户端，释放连接
        }
    }

    /**
     * 上传文件（MultipartFile版本）
     * 作用：Controller直接传上传的文件对象，更方便
     */
    public String upload(MultipartFile file) throws IOException {
        // try-with-resources：自动关闭输入流，不用手动close
        try (InputStream inputStream = file.getInputStream()) {
            // 读取文件字节，调用下面的upload方法
            return upload(file.getBytes(), file.getOriginalFilename());
        }
    }

    /**
     * 上传文件（byte[]版本）
     * 作用：底层上传方法，生成路径并上传
     */
    public String upload(byte[] content, String originalFilename) {
        // 获取当前日期，格式化为 "2026/05"
        String dir = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy/MM"));

        // 生成新文件名：UUID + 原后缀，如 "a1b2c3d4.jpg"
        String newFileName = UUID.randomUUID() + originalFilename.substring(originalFilename.lastIndexOf("."));

        // 完整路径：2026/05/a1b2c3d4.jpg
        String objectName = dir + "/" + newFileName;

        // 上传文件到OSS
        // 参数1：bucket名称（存储空间）
        // 参数2：文件在OSS中的路径
        // 参数3：文件内容（字节数组转成输入流）
        ossClient.putObject(ossProperties.getBucketName(), objectName, new ByteArrayInputStream(content));

        // 拼接并返回访问URL
        // 如：https://java-ai-yuanshuai.oss-cn-beijing.aliyuncs.com/2026/05/a1b2c3d4.jpg
        return ossProperties.getCdnDomain() + "/" + objectName;

    }
}
