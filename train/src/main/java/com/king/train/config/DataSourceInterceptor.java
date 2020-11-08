package com.king.train.config;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * @PackageName:com.king.train.config
 * @ClassName:DataSourceInterceptor
 * @Description:
 * @Author:王宗保
 * @Version V1.0.0
 * @Date:2020/11/8 23:50
 */
@Slf4j
@Aspect
@Order(Ordered.LOWEST_PRECEDENCE - 1)
@Component
public class DataSourceInterceptor {

    private boolean isDebug = log.isDebugEnabled();

    @Before("execution(* *..service.*.select*(..))|| execution(* *..service.*.get*(..))")
    public void setReadDataSourceType() {
        DynamicDataSource.slave();
        if (isDebug) {
            log.info("dataSource切换到：slave");
        }
    }

    @Before("execution(* *..service.*.insert*(..)) || execution(* *..service.*.update*(..)) || execution(* *..service.*.delete*(..)) || execution(* *..service.*.add*(..)) || execution(* *..service.*.save*(..))")
    public void setWriteDataSourceType() {
        DynamicDataSource.master();
        if (isDebug) {
            log.info("dataSource切换到：master");
        }
    }

    @AfterReturning("execution(* *..service.*.*(..))")
    public void clearDataSourceType() {
        DynamicDataSource.clear();
    }
}
