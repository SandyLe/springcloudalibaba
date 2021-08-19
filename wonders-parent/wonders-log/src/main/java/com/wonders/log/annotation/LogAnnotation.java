package com.wonders.log.annotation;

import java.lang.annotation.*;

/**
 * @author wuxx
 * @date 2021-05-27 17:06
 */
@Target({ElementType.METHOD})//目标是方法
@Retention(RetentionPolicy.RUNTIME) // 注解会在class字节码文件中存在，在运行时可以通过反射获取到
@Documented//文档生成时,该注解将被包含在javadoc中,可以去掉
public @interface LogAnnotation {
    /**
     * action()解释说明：
     * 这个是我们写注解时需要传入的参数值的名称，比如上述的用的是action，
     * 那么我们在注解时就应该这样注解@LogAnnotation(action=”测试”)，
     * 当然如果你只有一个参数需要传的，那么建议各位使用value(),这样注解时会默认传入的，
     * 这样我们就可以向其他注解一样@LogAnnotation(“测试”)。
     * targetType()解释说明：自定义,我这边是分为save、delete、update、select
     * remark() ： 特殊信息备注
     *
     * @return
     */
    String action() default "";

    String targetType() default "";

    String remark() default "";
}
