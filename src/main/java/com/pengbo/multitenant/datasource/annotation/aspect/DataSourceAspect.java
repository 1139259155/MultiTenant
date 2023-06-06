package com.pengbo.multitenant.datasource.annotation.aspect;

import com.pengbo.multitenant.datasource.DataSourceLookupKey;
import com.pengbo.multitenant.datasource.annotation.DataSource;
import com.pengbo.multitenant.datasource.holder.DynamicDataSourceContextHolder;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;

@Aspect
public class DataSourceAspect {

    /**
     * 设置数据源
     *
     * @param point
     * @return
     * @throws Throwable
     */
    @Around(value = "@annotation(com.pengbo.multitenant.datasource.annotation.DataSource)")
    public Object process(ProceedingJoinPoint point) throws Throwable {
        DataSource dataSource = ((MethodSignature) point.getSignature()).getMethod().getAnnotation(DataSource.class);
        DataSourceLookupKey preKey = DynamicDataSourceContextHolder.get();
        try {
            DynamicDataSourceContextHolder.set(dataSource.tenantId(), dataSource.schema());
            return point.proceed(point.getArgs());
        } finally {
            if (StringUtils.isAllEmpty(preKey.getSchema(), preKey.getTenantId())) {
                DynamicDataSourceContextHolder.clearAll();
            } else {
                DynamicDataSourceContextHolder.set(preKey.getTenantId(), preKey.getSchema());
            }
        }
    }
}
