package com.wonders.mybatis.aspect;

import com.wonders.mybatis.holder.DataSourceContextHolder;
import com.wonders.mybatis.annotation.DataSourceAnnotation;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * @author wuxx
 */
@Slf4j
@Component
@Aspect
public class DataSourceAspect implements Ordered {

    public DataSourceAspect() {
    }

    @Pointcut("@within(com.wonders.mybatis.annotation.DataSourceAnnotation) || @annotation(com.wonders.mybatis.annotation.DataSourceAnnotation)")
    public void dataSourcePointCut() {
    }

    @Before("dataSourcePointCut()")
    public void beforeSwitchDs(JoinPoint point) {
        Class<?> className = point.getTarget().getClass();
        String methodName = point.getSignature().getName();
        Class[] argClass = ((MethodSignature)point.getSignature()).getParameterTypes();
        String dataSource = "db1";

        try {
            Method method = className.getMethod(methodName, argClass);
            if (method.isAnnotationPresent(DataSourceAnnotation.class)) {
                DataSourceAnnotation annotation = method.getAnnotation(DataSourceAnnotation.class);
                dataSource = annotation.value();
            }
        } catch (Exception var8) {
            var8.printStackTrace();
        }
        DataSourceContextHolder.setDataSource(dataSource);
        log.info("数据源已经切换成：{} ",dataSource);
    }

    @After("dataSourcePointCut()")
    public void afterSwitchDs(JoinPoint point) {
        DataSourceContextHolder.remove();
    }

    @Override
    public int getOrder() {
        return 1;
    }
}

