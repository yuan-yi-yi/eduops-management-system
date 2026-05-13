package com.yuanshuai.aop;

import com.yuanshuai.mapper.OperateLogMapper;
import com.yuanshuai.pojo.OperateLog;
import com.yuanshuai.utils.CurrentHolder;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Arrays;

@Slf4j
@Aspect
@Component
public class OperationLogAspect {

    // 注入Mapper对象
    @Autowired
    private OperateLogMapper operateLogMapper;

    @Around("@annotation(com.yuanshuai.anno.Log)")
    public Object around(ProceedingJoinPoint pjp) throws Throwable {
        // 开始记录日志
        System.out.println("开始记录日志...");
        long startTime = System.currentTimeMillis();

        // 执行原始方法
        Object result = pjp.proceed();

        // 记录日志耗时
        long endTime = System.currentTimeMillis();
        long costTime = endTime - startTime;

        // 构建日志对象
        OperateLog olog = new OperateLog();
        olog.setOperateEmpId(getCurrentUserId());
        olog.setOperateTime(LocalDateTime.now());
        olog.setClassName(pjp.getTarget().getClass().getName());
        olog.setMethodName(pjp.getSignature().getName());
        olog.setMethodParams(Arrays.toString(pjp.getArgs()));
        olog.setReturnValue(result == null ? "void" : result.toString());
        olog.setCostTime(costTime);

        // 保存日志
        log.info("记录操作日志: {}", olog);

        // 调用Mapper对象保存日志
        operateLogMapper.insert(olog);

        // 返回结果
        System.out.println("结束记录日志...");
        return result;
    }

    private Integer getCurrentUserId() {
        return CurrentHolder.getCurrentId();
    }
}