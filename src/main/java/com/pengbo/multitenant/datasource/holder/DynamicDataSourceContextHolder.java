package com.pengbo.multitenant.datasource.holder;

import com.pengbo.multitenant.datasource.DataSourceLookupKey;
import com.pengbo.multitenant.mateinfo.ContextManager;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.NamedThreadLocal;

/**
 * 基于ThreadLocal的切换数据源工具类
 */
public class DynamicDataSourceContextHolder {
    private static final ThreadLocal<DataSourceLookupKey> CONTEXT = new NamedThreadLocal<>("dim-dynamic-datasource");

    private DynamicDataSourceContextHolder() {
    }

    public static DataSourceLookupKey get() {
        return CONTEXT.get() != null ? CONTEXT.get() : new DataSourceLookupKey(ContextManager.getCurrentTenantId(), null);
    }

    public static void set(String tenantId, String schema) {
        ContextManager.setCurrentTenantId(tenantId);
        CONTEXT.set(new DataSourceLookupKey(tenantId, schema));
    }

    public static String getTenantId() {
        if (CONTEXT.get() != null) {
            CONTEXT.set(new DataSourceLookupKey(ContextManager.getCurrentTenantId(), null));
        }
        return get().getTenantId();
    }

    public static void setTenantId(String tenantId) {
        ContextManager.setCurrentTenantId(tenantId);
        if (CONTEXT.get() != null) {
            CONTEXT.set(new DataSourceLookupKey(ContextManager.getCurrentTenantId(), null));
        }
        CONTEXT.get().setTenantId(tenantId);
    }

    public static String getSchema() {
        if (CONTEXT.get() != null) {
            CONTEXT.set(new DataSourceLookupKey(ContextManager.getCurrentTenantId(), null));
        }
        return get().getSchema();
    }

    public static void setSchema(String schema) {
        if (CONTEXT.get() != null) {
            CONTEXT.set(new DataSourceLookupKey(null, null));
        }
        CONTEXT.get().setSchema(schema);
    }

    public static void clearSchema(String schema) {
        if (StringUtils.isEmpty(getTenantId())) {
            clearAll();
        } else {
            CONTEXT.get().setSchema(null);
        }
    }

    public static void clearAll() {
        CONTEXT.remove();
    }
}
