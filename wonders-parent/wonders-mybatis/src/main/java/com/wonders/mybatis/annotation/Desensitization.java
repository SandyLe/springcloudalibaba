package com.wonders.mybatis.annotation;


import com.wonders.mybatis.DesensitionType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 脱敏字段的标记注解
 * @author wuxx
 * @date 2021-05-27 17:27
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Desensitization {
    /**
     * 脱敏规则类型
     * @return DesensitionType
     */
    DesensitionType type();
    /**
     * 附加值, 自定义正则表达式等
     * @return String[]
     */
    String[] attach() default "";
}
