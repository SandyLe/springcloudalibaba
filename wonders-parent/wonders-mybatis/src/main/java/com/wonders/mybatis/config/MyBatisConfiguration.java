package com.wonders.mybatis.config;

import com.alibaba.druid.filter.Filter;
import com.alibaba.druid.filter.stat.StatFilter;
import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceBuilder;
import com.alibaba.druid.support.http.StatViewServlet;
import com.alibaba.druid.support.http.WebStatFilter;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * mybatis 配置
 * @author wuxx
 * @date 2021-05-27 17:37
 */
@Configuration
@MapperScan("com.wonders.**.dao")
public class MyBatisConfiguration {

    private final Environment env;

    public MyBatisConfiguration(Environment env) {
        this.env = env;
    }

    @Primary
    @Bean(
            name = {"db1"}
    )
    @ConfigurationProperties(
            prefix = "spring.custom.datasource.db1"
    )
    public DataSource db1() {
        return DruidDataSourceBuilder.create().build();
    }

    @Bean(
            name = {"db2"}
    )
    @ConfigurationProperties(
            prefix = "spring.custom.datasource.db2"
    )
    public DataSource db2() {
        return DruidDataSourceBuilder.create().build();
    }

    @Bean(
            name = {"db3"}
    )
    @ConfigurationProperties(
            prefix = "spring.custom.datasource.db3"
    )
    public DataSource db3() {
        return DruidDataSourceBuilder.create().build();
    }

    @Bean(
            name = {"dynamicDataSource"}
    )
    public DataSource dataSource(@Qualifier("db1") DataSource db1, @Qualifier("db2") DataSource db2, @Qualifier("db3") DataSource db3) {
        Map<Object, Object> targetDataSources = new HashMap<>();
        targetDataSources.put("db1", db1);
        targetDataSources.put("db2", db2);
        targetDataSources.put("db3", db3);
        return new DynamicDataSource(db1, targetDataSources);
    }

    @Bean
    public SqlSessionFactory sqlSessionFactory() throws Exception {
        SqlSessionFactoryBean sqlSessionFactory = new SqlSessionFactoryBean();
        sqlSessionFactory.setDataSource(this.dataSource(this.db1(), this.db2(), this.db3()));
        sqlSessionFactory.setMapperLocations((new PathMatchingResourcePatternResolver()).getResources(Objects.requireNonNull(this.env.getProperty("mybatis.mapper-locations"))));
        sqlSessionFactory.setConfigLocation((new PathMatchingResourcePatternResolver()).getResource(Objects.requireNonNull(this.env.getProperty("mybatis.config-location"))));
        return sqlSessionFactory.getObject();
    }

    @Bean
    public DataSourceTransactionManager transactionManager(@Qualifier("dynamicDataSource") DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    @Bean
    public Filter statFilter() {
        StatFilter filter = new StatFilter();
        filter.setSlowSqlMillis(5000L);
        filter.setLogSlowSql(true);
        filter.setMergeSql(true);
        return filter;
    }

    @Bean
    public ServletRegistrationBean<StatViewServlet> servletRegistrationBean() {
        ServletRegistrationBean<StatViewServlet> servletRegistrationBean = new ServletRegistrationBean(new StatViewServlet(), "/druid/*");
        servletRegistrationBean.addInitParameter("logSlowSql", "true");
        servletRegistrationBean.addInitParameter("resetEnable", "false");
        return servletRegistrationBean;
    }

    @Bean
    public FilterRegistrationBean<WebStatFilter> druidStatFilter() {
        FilterRegistrationBean<WebStatFilter> filterRegistrationBean = new FilterRegistrationBean<>();
        filterRegistrationBean.setFilter(new WebStatFilter());
        filterRegistrationBean.addUrlPatterns("/*");
        filterRegistrationBean.addInitParameter("exclusions", "*.js,*.gif,*.jpg,*.png,*.css,*.ico,/druid/*");
        filterRegistrationBean.addInitParameter("profileEnable", "true");
        return filterRegistrationBean;
    }
}
