package com.pengbo.multitenant.datasource.poolconfig;

import com.alibaba.druid.pool.DruidDataSource;
import com.google.common.base.Suppliers;
import com.pengbo.multitenant.datasource.DataSourceLookupKey;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

/**
 * Druid 数据库连接池 工厂类
 */
public class DataSourceConfigFactory {

    /**
     * 单例工厂配置
     */
    private static final Supplier<DataSourceConfigFactory> HOLDER = Suppliers.memoize(DataSourceConfigFactory::new);

    private DataSourceConfigFactory() {
    }

    public static DataSourceConfigFactory getInstance() {
        return HOLDER.get();
    }

    /**
     * 批量构建数据源
     *
     * @param supplier
     * @param dataSourcePoolConfig
     * @return
     */
    public Map<DataSourceLookupKey, DataSource> build(Supplier<? extends Map<DataSourceLookupKey, DataSourceBaseConfig>> supplier, DataSourcePoolConfig dataSourcePoolConfig) {

        Map<DataSourceLookupKey, DataSourceBaseConfig> baseConfigs = supplier.get();
        return build(baseConfigs, dataSourcePoolConfig);

    }

    /**
     * 批量构建数据源
     *
     * @param baseConfigs
     * @param dataSourcePoolConfig
     * @return
     */
    public Map<DataSourceLookupKey, DataSource> build(Map<DataSourceLookupKey, DataSourceBaseConfig> baseConfigs, DataSourcePoolConfig dataSourcePoolConfig) {

        Map<DataSourceLookupKey, DataSource> druidDataSources = new HashMap<>();

        for (Map.Entry<DataSourceLookupKey, DataSourceBaseConfig> baseConfig : baseConfigs.entrySet()) {
            // 获取连接池pool 详细配置detailConfig
            DataSourcePoolConfig.DataSourcePoolDetailConfig detailConfig = dataSourcePoolConfig.getDetailConfig(baseConfig.getKey().getSchema(), baseConfig.getKey().getTenantId());

            // 生成datasource
            DruidDataSource druidDataSource = buildDataSource(baseConfig.getValue(), detailConfig);
            druidDataSources.put(baseConfig.getKey(), druidDataSource);
        }
        return druidDataSources;

    }

    /**
     * 生产druid数据源
     *
     * @param baseConfig
     * @param detailConfig
     * @return
     */
    public DruidDataSource buildDataSource(DataSourceBaseConfig baseConfig, DataSourcePoolConfig.DataSourcePoolDetailConfig detailConfig) {
        DruidDataSource druidDataSource = new DruidDataSource();

        druidDataSource.setUrl(baseConfig.toString());
        druidDataSource.setUsername(baseConfig.getUser());
        druidDataSource.setPassword(baseConfig.getPassword());
        druidDataSource.setDriverClassName("org.postgresql.Driver");

        druidDataSource.setMinIdle(detailConfig.getMinIdle());
        druidDataSource.setValidationQuery(detailConfig.getValidationQuery());
        druidDataSource.setKeepAlive(detailConfig.isKeepAlive());
        druidDataSource.setKeepAliveBetweenTimeMillis(detailConfig.getKeepAliveBetweenTimeMillis());
        druidDataSource.setMaxActive(detailConfig.getMaxActive());
        druidDataSource.setMaxEvictableIdleTimeMillis(detailConfig.getMaxEvictableIdleTimeMillis());
        druidDataSource.setMinEvictableIdleTimeMillis(detailConfig.getMinEvictableIdleTimeMillis());
        druidDataSource.setTimeBetweenEvictionRunsMillis(detailConfig.getTimeBetweenEvictionRunsMillis());
        druidDataSource.setTestOnBorrow(detailConfig.isTestOnBorrow());
        druidDataSource.setTestOnReturn(detailConfig.isTestOnReturn());
        druidDataSource.setTestWhileIdle(detailConfig.isTestWhileIdle());
        druidDataSource.setRemoveAbandoned(detailConfig.isRemoveAbandoned());
        druidDataSource.setRemoveAbandonedTimeoutMillis(detailConfig.getRemoveAbandonedTimeoutMillis());
        druidDataSource.setLogAbandoned(detailConfig.isLogAbandoned());
        druidDataSource.setMaxWait(detailConfig.getMaxWait());
        druidDataSource.setFailFast(detailConfig.isFastFailed());

        // 失败后重连的次数
        druidDataSource.setConnectionErrorRetryAttempts(detailConfig.getConnectionErrorRetryAttempts());
        // 到达请求失败次数后不中断
        druidDataSource.setBreakAfterAcquireFailure(false);

        return druidDataSource;

    }
}
