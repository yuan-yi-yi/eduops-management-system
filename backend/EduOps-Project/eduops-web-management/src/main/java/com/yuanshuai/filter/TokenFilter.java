package com.yuanshuai.filter;

import com.yuanshuai.utils.CurrentHolder;
import com.yuanshuai.utils.JwtUtils;
import io.jsonwebtoken.Claims;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

@Slf4j
@WebFilter(urlPatterns = "/*")
public class TokenFilter implements Filter {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        //1.获取到请求路径
        String requestURI = request.getRequestURI();

        //2.判断是否是登录请求，如果是登录请求，则放行
        // 注意：/login 是登录接口，但 /login/password 是修改密码接口，需要token验证
        if(requestURI.equals("/login") || requestURI.equals("/login/")){
            log.info("登录请求，放行: {}", requestURI);
            filterChain.doFilter(request,response);
            return;
        }

        //3.获取请求头中的token
        String token = request.getHeader("token");

        //4.判断token是否为空，如果为空，说明用户未登录，则返回错误信息401
        if(token == null || token.isEmpty()){
            log.info("令牌为空，响应401");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        //5.如果token存在，则解析token，如果校验失败，则返回错误信息401
        try {
            Claims claims = JwtUtils.parseToken(token);
            Integer empId = (Integer) claims.get("id");
            CurrentHolder.setCurrentId(empId); //存入, 用完后要删除
            log.info("当前用户id: {},将其存入ThreadLocal", empId);
        } catch (Exception e) {
            log.info("令牌解析失败，响应401");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        //6.校验通过，则放行
        log.info("令牌解析成功，放行");
        filterChain.doFilter(request,response);



        //7.用完后删除ThreadLocal中的数据
        CurrentHolder.remove();

    }
}
