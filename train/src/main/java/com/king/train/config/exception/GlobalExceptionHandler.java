package com.king.train.config.exception;

/**
 * @PackageName:com.king.train.config.exception
 * @ClassName:GlobalExceptionHandler
 * @Description: 全局异常处理器
 * @Author:王宗保
 * @Version V1.0.0
 * @Date:2020/11/9 13:58
 */

import com.king.train.entities.CommonResult;
import com.king.train.enums.ResponseCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    /**
     * 方法参数校验
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public CommonResult handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        log.error(e.getMessage(), e);
        return new CommonResult(ResponseCode.FAILURE.value(), e.getBindingResult().getFieldError().getDefaultMessage());
    }

    /**
     * ValidationException
     */
    @ExceptionHandler(ValidationException.class)
    public CommonResult handleValidationException(ValidationException e) {
        log.error(e.getMessage(), e);
        return new CommonResult(ResponseCode.FAILURE.value(), e.getCause().getMessage());
    }

    /**
     * ConstraintViolationException
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public CommonResult handleConstraintViolationException(ConstraintViolationException e) {
        log.error(e.getMessage(), e);
        String resMesg = e.getMessage().substring(e.getMessage().lastIndexOf(":" )+1);
        return new CommonResult(ResponseCode.FAILURE.value(),resMesg);
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public CommonResult handlerNoFoundException(Exception e) {
        log.error(e.getMessage(), e);
        return new CommonResult(ResponseCode.FAILURE.value(), "路径不存在，请检查路径是否正确");
    }

}
