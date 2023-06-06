package com.pengbo.multitenant.datasource.provider;

import com.pengbo.multitenant.datasource.DataSourceLookupKey;
import com.pengbo.multitenant.datasource.poolconfig.DataSourceBaseConfig;
import com.pengbo.multitenant.datasource.poolconfig.DataSourceConfigFactory;
import com.pengbo.multitenant.datasource.poolconfig.DataSourcePoolConfig;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public abstract class AbstractDynamicDataSourceProvider implements Supplier<Map<DataSourceLookupKey, DataSource>> {

    @Autowired
    protected DataSourcePoolConfig dataSourcePoolConfig;

    @Getter
    protected Map<DataSourceLookupKey, DataSourceBaseConfig> dataSourceBaseConfigs = new HashMap<>();

    /**
     * 加载所有数据源配置
     *
     * @return
     */
    Map<DataSourceLookupKey, DataSourceBaseConfig> loadDataSources() {
        return null;
    }

    /**
     * 获取生成数据源，工厂方式生成
     *
     * @return
     */
    @Override
    public Map<DataSourceLookupKey, DataSource> get() {
        DataSourceConfigFactory factory = DataSourceConfigFactory.getInstance();
        Map<DataSourceLookupKey, DataSource> druidDataSources = factory.build(() -> loadDataSources(), dataSourcePoolConfig);

        return druidDataSources;
    }
}
