package com.yuanshuai.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 配置类
 */
@Configuration // 标识当前类是一个配置类
public class WebConfig implements WebMvcConfigurer {
    // @Autowired
    // private DemoInterceptor demoInterceptor;
    //
    // @Override
    // public void addInterceptors(InterceptorRegistry registry) {
    //     registry.addInterceptor(demoInterceptor).addPathPatterns("/**"); // 添加拦截器，拦截所有请求
    //     //注册自定义拦截器对象
    //     registry.addInterceptor(demoInterceptor)
    //             .addPathPatterns("/**")//设置拦截器拦截的请求路径（ /** 表示拦截所有请求）
    //             .excludePathPatterns("/login");//设置不拦截的请求路径
    // }
}