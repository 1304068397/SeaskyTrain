package com.king.train.config;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

/**
 * @PackageName:com.king.train.config
 * @ClassName:DynamicDataSource
 * @Description:
 * @Author:王宗保
 * @Version V1.0.0
 * @Date:2020/11/8 23:41
 */
public class DynamicDataSource extends AbstractRoutingDataSource {

   private static final ThreadLocal<DatabaseType> contextHolder = new ThreadLocal<>();

    @Override
    protected Object determineCurrentLookupKey() {
        Object lookupKey = contextHolder.get();
        return lookupKey;
    }

    public static void master() {
        contextHolder.set(DatabaseType.Master);
    }


    public static void slave() {
        contextHolder.set(DatabaseType.Slave);
    }

    public static void clear() {
        contextHolder.remove();
    }

    public static DatabaseType getType() {
        return contextHolder.get();
    }

    public enum DatabaseType {
        Master, Slave
    }
}
