package com.king.train.config.druid;

/**
 * @PackageName:com.king.train.config
 * @ClassName:DataSourceAspect
 * @Description:
 *
 * 实现读写分离：数据源切换的切面类实现
 *
 * dataSourcePointCut：此处代表方法中包含CurDataSource注解的，都会被作为切点识别
 * around：根据方法上CurDataSource注解的数据源Key属性，操作动态数据源管理类，更换数据源
 *
 * @Author:王宗保
 * @Version V1.0.0
 * @Date:2020/11/9 9:54
 */

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Aspect
@Component
public class DataSourceAspect {
    @Pointcut("@annotation(com.king.train.config.druid.CurDataSource)")
    public void dataSourcePointCut() {
    }
    @Around("dataSourcePointCut()")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        MethodSignature signature = (MethodSignature) point.getSignature();
        Method method = signature.getMethod();
        CurDataSource ds = method.getAnnotation(CurDataSource.class);
        if (ds == null) {
            CurrentDataSource.set(DataSourceName.write);
        } else {
            CurrentDataSource.set(ds.source());
        }
        return point.proceed();
    }
}
