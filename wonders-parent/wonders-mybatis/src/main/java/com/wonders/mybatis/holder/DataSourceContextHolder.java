package com.wonders.mybatis.holder;

import com.alibaba.druid.util.StringUtils;

import java.util.HashSet;
import java.util.Set;

/**
 * @author wuxx
 */
public class DataSourceContextHolder {
    private static final ThreadLocal<String> CONTEXT_HOLDER = new InheritableThreadLocal<>();
    public static final Set<Object> ALL_DATA_SOURCE_KEY = new HashSet<>();

    public DataSourceContextHolder() {
    }

    public static void setDataSource(String dataSource) {
        if (isExist(dataSource)) {
            CONTEXT_HOLDER.set(dataSource);
        } else {
            throw new NullPointerException("数据源查找键（Look up key）【" + dataSource + "】不存在");
        }
    }

    public static String getDataSource() {
        return (String)CONTEXT_HOLDER.get();
    }

    public static void remove() {
        CONTEXT_HOLDER.remove();
    }

    public static boolean isExist(String dataSource) {
        if (StringUtils.isEmpty(dataSource)) {
            return false;
        } else {
            return ALL_DATA_SOURCE_KEY.contains(dataSource);
        }
    }
}
