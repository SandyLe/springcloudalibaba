package com.wonders.mybatis.annotation;

import java.lang.annotation.*;

/**
 * 多数据源注解
 * @author wuxx
 * @date 2021-05-27 17:27
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
@Documented
public @interface DataSourceAnnotation {
    String value() default "db1";
}
