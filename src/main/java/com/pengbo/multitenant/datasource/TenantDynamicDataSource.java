package com.pengbo.multitenant.datasource;

import com.alibaba.druid.pool.DruidDataSource;
import com.pengbo.multitenant.datasource.holder.DynamicDataSourceContextHolder;
import com.pengbo.multitenant.datasource.poolconfig.DataSourceBaseConfig;
import com.pengbo.multitenant.datasource.provider.AbstractDynamicDataSourceProvider;
import com.pengbo.multitenant.mateinfo.ContextManager;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class TenantDynamicDataSource extends AbstractRoutingDataSource implements DisposableBean {

    /**
     * provider提供Datasource来源
     */
    @Autowired
    private List<AbstractDynamicDataSourceProvider> providers;

    /**
     * 核心数据结构
     * 保持所有数据源，<schema+tenantId, DataSource>
     */
    @Getter
    private Map<DataSourceLookupKey, DataSource> dataSourceMap = new ConcurrentHashMap<>();


    /**
     * 核心数据结构
     * 保持所有数据源原始配置，<schema+tenantId, DataSourceBaseConfig>
     */
    @Getter
    private Map<DataSourceLookupKey, DataSourceBaseConfig> dataSourceBaseConfigMap = new ConcurrentHashMap<>();

    @Override
    protected DataSourceLookupKey determineCurrentLookupKey() {
        return DynamicDataSourceContextHolder.get();
    }

    @Override
    protected DataSource determineTargetDataSource() {
        DataSourceLookupKey dsKey = determineCurrentLookupKey();
        return dataSourceMap.get(dsKey);
    }

    @Override
    public void afterPropertiesSet() {
        log.warn("DIM-dynamic-datasource initial begin...");

        Map<DataSourceLookupKey, DataSource> dataSources = new HashMap<>();

        // load db config from local secret
        for (AbstractDynamicDataSourceProvider provider : providers) {
            // save datasource
            dataSources.putAll(provider.get());

            // save datasource baseConfig
            dataSourceBaseConfigMap.putAll(provider.getDataSourceBaseConfigs());
        }

        // safely add to dataSourceMap with closing old datasource
        for (Map.Entry<DataSourceLookupKey, DataSource> dsItem : dataSources.entrySet()) {
            addDataSource(dsItem.getKey(), dsItem.getValue());
        }

        log.warn("DIM-dynamic-datasource initial end..., datasouce total: {}, [{}]", dataSources.size(), dataSources.keySet());


        super.afterPropertiesSet();
    }

    private void addDataSource(DataSourceLookupKey dsKey, DataSource dataSource) {
        DataSource oldDataSource = dataSourceMap.put(dsKey, dataSource);
        // close old datasource
        if (oldDataSource != null) {
            ((DruidDataSource) oldDataSource).close();
        }
    }

    @Override
    public void destroy() throws Exception {

    }
}
