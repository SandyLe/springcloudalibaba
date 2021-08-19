package com.wonders.mybatis.config;

import org.apache.ibatis.executor.ErrorContext;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;

/**
 * @author wuxx
 */
public class MySqlSessionFactory extends SqlSessionFactoryBean {
    public MySqlSessionFactory() {
    }

    @Override
    protected SqlSessionFactory buildSqlSessionFactory() throws Exception {
        SqlSessionFactory var1;
        try {
            var1 = super.buildSqlSessionFactory();
        } catch (Exception var5) {
            var5.printStackTrace();
            throw var5;
        } finally {
            ErrorContext.instance().reset();
        }

        return var1;
    }
}
