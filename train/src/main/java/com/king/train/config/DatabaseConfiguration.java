package com.king.train.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceBuilder;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @PackageName:com.king.train.config
 * @ClassName:DatabaseConfiguration
 * @Description:
 * @Author:王宗保
 * @Version V1.0.0
 * @Date:2020/11/8 23:42
 */
@Component
public class DatabaseConfiguration {
    @Primary
    @Bean
    @ConfigurationProperties("spring.datasource.druid.master")
    public DruidDataSource masterDataSource() {
        return DruidDataSourceBuilder.create().build();
    }

    @Bean
    @ConfigurationProperties("spring.datasource.druid.slave")
    public DruidDataSource slaveDataSource() {
        return DruidDataSourceBuilder.create().build();
    }

    @Bean
    public DynamicDataSource dynamicDataSource(DruidDataSource masterDataSource, @Qualifier("slaveDataSource") DruidDataSource slaveDataSource) {
        Map<Object, Object> targetDataSources = new HashMap<>(2);
        targetDataSources.put(DynamicDataSource.DatabaseType.Master, masterDataSource);
        targetDataSources.put(DynamicDataSource.DatabaseType.Slave, slaveDataSource);
        DynamicDataSource dataSource = new DynamicDataSource();
        dataSource.setTargetDataSources(targetDataSources);
        dataSource.setDefaultTargetDataSource(masterDataSource);
        dataSource.afterPropertiesSet();
        return dataSource;
    }
}
