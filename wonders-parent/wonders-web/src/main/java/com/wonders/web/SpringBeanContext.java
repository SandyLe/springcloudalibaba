package com.wonders.web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;


/**
 * 上下文初始化
 * @author wuxx
 */
@Component
@Slf4j
public class SpringBeanContext implements ApplicationContextAware {

    protected static ApplicationContext applicationContext;

    public SpringBeanContext() {
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext1) throws BeansException {
        applicationContext = applicationContext1;
    }

    public static Object getBean(String beanName) {
        if (applicationContext == null) {
            log.error("未初始化Spring上下文");
            return null;
        } else if (!applicationContext.containsBean(beanName)) {
            log.warn("Spring上下文中不存在要查找的对象[{}]", beanName);
            return null;
        } else {
            return applicationContext.getBean(beanName);
        }
    }

    public static <T> T getBean(Class<T> clazz) {
        if (applicationContext == null) {
            log.error("未初始化Spring上下文");
            return null;
        } else {
            return applicationContext.getBean(clazz);
        }
    }

    public static <T> T getBean(String name, Class<T> clazz) {
        if (applicationContext == null) {
            log.error("未初始化Spring上下文");
            return null;
        } else {
            return applicationContext.getBean(name, clazz);
        }
    }

    public String[] getBeanNamesForType(Class<?> type) {
        return applicationContext.getBeanNamesForType(type);
    }
}
