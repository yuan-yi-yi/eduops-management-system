package com.yuanshuai.interceptor;

import com.yuanshuai.utils.JwtUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Slf4j
@Component
public class TokenInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //1.获取到请求路径
        String requestURI = request.getRequestURI();

        //2.判断是否是登录请求，如果是登录请求，则放行
        if(requestURI.contains("/login")){
            log.info("登录请求，放行: {}", requestURI);
            return true;
        }

        //3.获取请求头中的token
        String token = request.getHeader("token");

        //4.判断token是否为空，如果为空，说明用户未登录，则返回错误信息401
        if(token == null || token.isEmpty()){
            log.info("令牌为空，响应401");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return false;
        }

        //5.如果token存在，则解析token，如果校验失败，则返回错误信息401
        try {
            JwtUtils.parseToken(token);
        } catch (Exception e) {
            log.info("令牌解析失败，响应401");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return false;
        }

        //6.校验通过，则放行
        log.info("令牌解析成功，放行");
        return true;
    }
}