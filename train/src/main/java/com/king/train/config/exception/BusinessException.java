package com.king.train.config.exception;

import org.springframework.ui.ModelMap;

import java.util.Objects;

/**
 * 业务异常
 *
 * @author yangjian
 * @since 2019-12-30
 */
@SuppressWarnings("serial")
public class BusinessException extends Exception{
    private Error response;

    public BusinessException(Error response) {
        super(response.getMessage());
        this.response = response;
    }

    public BusinessException(Throwable e, Error response) {
        super(e.getMessage() + response.getMessage(), e);
        this.response = response;
    }

}
