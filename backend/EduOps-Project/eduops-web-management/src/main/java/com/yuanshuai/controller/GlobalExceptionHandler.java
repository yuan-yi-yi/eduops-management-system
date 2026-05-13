package com.yuanshuai.controller;

import com.yuanshuai.exception.BusinessException;
import com.yuanshuai.pojo.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理器：统一处理项目中所有控制器抛出的异常
 */
@Slf4j
@RestControllerAdvice // 组合注解：@ControllerAdvice + @ResponseBody，返回JSON格式结果
public class GlobalExceptionHandler {
    /**
     * 处理自定义业务异常（比如班级不存在、参数非法）
     * @param e 业务异常对象
     * @return 统一的Result格式响应
     */
    @ExceptionHandler(BusinessException.class) // 只捕获BusinessException类型的异常
    public Result handleBusinessException(Exception e){
        log.warn("业务异常: {}",e.getMessage()); // 用warn级别，区分普通错误和系统异常
        // 返回业务异常(友好提示给前端)
        return Result.error(e.getMessage());
    }

    /**
     * 处理系统通用异常（所有未被上面捕获的异常）
     * @param e 通用异常对象
     * @return 统一的Result格式响应
     */
    @ExceptionHandler(Exception.class) // 捕获所有Exception类型的异常（兜底）
    public Result handleException(Exception e) {
        log.error("程序出错了！", e);
        // 返回通用提示（避免暴露系统细节）
        return Result.error("出错！请联系管理员！");
    }
}