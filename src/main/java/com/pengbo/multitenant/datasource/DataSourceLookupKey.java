package com.pengbo.multitenant.datasource;

import com.pengbo.multitenant.mateinfo.ContextManager;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

import java.util.Objects;

@Getter
public class DataSourceLookupKey {

    private String tenantId = "1002";

    private String schema = "cmdbcoresvrdb";

    public DataSourceLookupKey(String tenantId, String schema) {
        if (StringUtils.contains(schema, "_")) {
            this.schema = StringUtils.split(schema, "_", 2)[0];
            this.tenantId = StringUtils.split(schema, "_", 2)[1];
        } else {
            this.tenantId = StringUtils.defaultIfBlank(tenantId, ContextManager.getCurrentTenantId("1002"));
            this.schema = StringUtils.defaultIfBlank(schema, "cmdbcoresvrdb");
        }
    }


    public void setTenantId(String tenantId) {
        this.tenantId = StringUtils.defaultIfBlank(tenantId, ContextManager.getCurrentTenantId("1002"));
    }

    public void setSchema(String schema) {
        if (StringUtils.contains(schema, "_")) {
            this.schema = StringUtils.split(schema, "_", 2)[0];
            this.tenantId = StringUtils.split(schema, "_", 2)[1];
        }else {
            this.schema = StringUtils.defaultIfBlank(schema, "cmdbcoresvrdb");
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DataSourceLookupKey that = (DataSourceLookupKey) o;
        return Objects.equals(tenantId, that.tenantId) && Objects.equals(schema, that.schema);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tenantId, schema);
    }

    @Override
    public String toString() {
        return "DataSourceLookupKey{" +
                "tenantId='" + tenantId + '\'' +
                ", schema='" + schema + '\'' +
                '}';
    }
}
