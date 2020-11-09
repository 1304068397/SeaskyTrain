package com.king.train.config.druid;

/**
 * @PackageName:com.king.train.config
 * @ClassName:CurDataSource
 * @Description:
 * 实现读写分离：自定义注解，用来标记方法
 *
 * @Target：定义该注解可以标记的对象，此处指只能标记在方法上，支持定义多个对象。
 * @Retention：定义该注解何时生效，此处指只在运行过程中生效，支持定义多个对象。
 * Source：定义了一个注解参数，类型为DataSourceName，默认为write数据源
 *
 * @Author:王宗保
 * @Version V1.0.0
 * @Date:2020/11/9 9:41
 */

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface CurDataSource {
    DataSourceName source() default DataSourceName.write;
}
