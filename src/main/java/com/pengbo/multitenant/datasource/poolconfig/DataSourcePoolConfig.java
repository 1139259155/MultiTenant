package com.pengbo.multitenant.datasource.poolconfig;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;

public class DataSourcePoolConfig {

    private String defaultTenantId = "1002";

    private String defaultSchema = "cmdbcoresvrdb";

    private int connectTimeout = 10;
    private int socketTimeout = 30;

    private Map<String, DataSourcePoolDetailConfig> pool = new HashMap<>();

    public DataSourcePoolDetailConfig getDetailConfig(String database, String tenantId) {
        if (StringUtils.isBlank(database)) {
            return new DataSourcePoolDetailConfig();
        }
        return pool.get(database + "_" + tenantId);
    }

    @Getter
    @Setter
    public static class DataSourcePoolDetailConfig {
        private boolean testOnBorrow = false;
        private boolean testOnReturn = false;
        private boolean testWhileIdle = true;
        private boolean keepAlive = true;

        private long keepAliveBetweenTimeMillis = 2 * 60 * 1000L;
        private long maxEvictableIdleTimeMillis = 60 * 60 * 1000L;
        private long minEvictableIdleTimeMillis = 5 * 60 * 1000L;
        private long timeBetweenEvictionRunsMillis = 60 * 1000L;

        private boolean removeAbandoned = true;

        private long removeAbandonedTimeoutMillis = 7200 * 1000L;

        private boolean logAbandoned = true;

        private int minIdle = 2;
        private int maxActive = 75;

        private long maxWait = 10 * 1000L;

        private boolean fastFailed = false;

        private int connectionErrorRetryAttempts = 3;

        private String validationQuery = "SELECT 1";
    }
}
