package com.pengbo.multitenant.crontask.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 多租定时任务注解
 *
 * @author pengbo
 * @since 2022-9-10
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface MultiTenantCronTask {

    /**
     * 多租类型 ALL表示全部租户，STACK表示当前堆栈租户
     * @return
     */
    String multiType() default  "ALL";
}
