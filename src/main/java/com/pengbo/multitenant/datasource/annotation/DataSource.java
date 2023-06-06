package com.pengbo.multitenant.datasource.annotation;

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
public @interface DataSource {

    /**
     * 业务数据库名
     *
     * @return
     */
    String schema() default "ALL";

    /**
     * 当前租户
     *
     * @return
     */
    String tenantId() default "1002";
}
