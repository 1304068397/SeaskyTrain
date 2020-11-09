package com.king.train.config.druid;

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceBuilder;
import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import com.baomidou.mybatisplus.extension.spring.MybatisSqlSessionFactoryBean;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.plugin.Interceptor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

/**
 * 数据源配置类
 *
 * 实现读写分离：DataSourceConfiguration的实现
 *
 * txManager：创建事务管理器
 * wirteDataSource：加载write数据源的配置并返回连接对象
 * readDataSource：加载read数据源的配置并返回连接对象
 * dataSouce：将所有定义的连接对象加载到动态数据源管理器
 * sqlSessionFactory：因数据源信息被druid接管，因此需调整myBatis的数据源为动态数据源管理器，同时指定xml的扫描路径
 *
 * @author yangjian
 * @since 2019-12-31
 */

@Slf4j
@Configuration
public class DataSourceConfig {

    @Bean
    public PlatformTransactionManager txManager() {
        return new DataSourceTransactionManager(dynamicDataSource());
    }

    @Primary
    @Bean
    @ConfigurationProperties(prefix = "spring.datasource.druid.write")
    public DataSource writeDataSource() {
        return DruidDataSourceBuilder.create().build();
    }

    @Bean
    @ConfigurationProperties(prefix = "spring.datasource.druid.read")
    public DataSource readDataSource() {
        return DruidDataSourceBuilder.create().build();
    }

//    @Bean
//    @ConfigurationProperties(prefix = "spring.datasource.druid.third")
//    public DataSource thirdDataSource() {
//        return DruidDataSourceBuilder.create().build();
//    }

    @Bean
    public AbstractRoutingDataSource dynamicDataSource() {
        AbstractRoutingDataSource routingDataSource = new AbstractRoutingDataSource() {
            @Override
            protected Object determineCurrentLookupKey() {
                return CurrentDataSource.get();
            }
        };
        Map<Object, Object> datasources = new HashMap<>(3);
        datasources.put(DataSourceName.write, writeDataSource());
        datasources.put(DataSourceName.read, readDataSource());
        //datasources.put(DataSourceName.third, thirdDataSource());
        routingDataSource.setTargetDataSources(datasources);
        routingDataSource.setDefaultTargetDataSource(writeDataSource());
        return routingDataSource;
    }

    @Bean
    @ConfigurationProperties(prefix = "mybatis-plus")
    public MybatisSqlSessionFactoryBean sqlSessionFactory() throws Exception {

        MybatisSqlSessionFactoryBean mybatisPlus = new MybatisSqlSessionFactoryBean();
        mybatisPlus.setDataSource(dynamicDataSource());
        mybatisPlus.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath*:/mapper/**/*.xml"));
        mybatisPlus.setPlugins(new Interceptor[]{new PaginationInterceptor()});

        return mybatisPlus;
    }

}
