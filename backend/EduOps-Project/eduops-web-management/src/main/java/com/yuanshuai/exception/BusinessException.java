package com.yuanshuai.exception;

/**
 * 自定义业务异常（比如班级不存在、参数错误等）
 */
public class BusinessException extends RuntimeException {

    // 错误提示信息
    private String message;

    public BusinessException(String message) {
        super(message);
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}