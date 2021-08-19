package com.wonders.mybatis.config;

import com.wonders.mybatis.holder.DataSourceContextHolder;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import javax.sql.DataSource;
import java.util.Map;

/**
 * @author wuxx
 */
public class DynamicDataSource extends AbstractRoutingDataSource {
    public DynamicDataSource(DataSource defaultTargetDataSource, Map<Object, Object> targetDataSources) {
        super.setDefaultTargetDataSource(defaultTargetDataSource);
        super.setTargetDataSources(targetDataSources);
        super.setLenientFallback(true);
        super.setDataSourceLookup(null);
        super.afterPropertiesSet();
        DataSourceContextHolder.ALL_DATA_SOURCE_KEY.addAll(targetDataSources.keySet());
    }

    public static DynamicDataSource build(DataSource defaultTargetDataSource, Map<Object, Object> targetDataSources) {
        return new DynamicDataSource(defaultTargetDataSource, targetDataSources);
    }

    @Override
    protected Object determineCurrentLookupKey() {
        return DataSourceContextHolder.getDataSource();
    }
}
