package com.king.train.config.druid;

import lombok.extern.slf4j.Slf4j;

/**
 * 当前线程数据源持有者
 *
 * 实现读写分离：动态数据源管理器的实现
 *
 * 将数据源Key（DataSourceName）包装为线程变量
 *
 * 继承AbstractRoutingDataSource，并实现抽象方法determineCurrentLookupKey：告诉管理器从线程变量中去取当前指定的DataSourceName
 *
 * 定义数据源切换的setDataSource方法，并写入线程变量
 *
 * @author yangjian
 * @date 2019-12-31
 */
@Slf4j
public class CurrentDataSource {

    private static final ThreadLocal<DataSourceName> current = new ThreadLocal<>();

    public static void set(DataSourceName dataSourceName) {
        log.info("set current datasource => {}", dataSourceName);
        current.set(dataSourceName);
    }

    public static DataSourceName get() {
        log.info("get current datasource => {}", current.get());
        return current.get();
    }

    public static void clear() {
        log.info("clear current datasource => {}", current.get());
        current.remove();
    }

}
