package com.yuanshuai.filter;

import jakarta.servlet.*;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

// @WebFilter(urlPatterns = "/*") // 拦截所有请求
@Slf4j
public class DemoFilter implements Filter {

    // 初始化,web服务启动时执行,只执行一次
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        log.info("DemoFilter初始化...");
    }

    // 拦截到请求时执行，每次请求都会执行
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException, IOException {
        log.info("拦截到了请求,DemoFilter执行...");
        // 放行
        filterChain.doFilter(servletRequest, servletResponse);
    }

    // 销毁,web服务停止时执行,只执行一次
    @Override
    public void destroy() {
        log.info("DemoFilter销毁...");
    }
}