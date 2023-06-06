package com.pengbo.multitenant.datasource.spring.boot.autoconfigure;

import com.pengbo.multitenant.crontask.annotation.MultiTenantCronTaskConfigurer;
import com.pengbo.multitenant.crontask.dynamic.DynamicScheduledTaskRegister;
import com.pengbo.multitenant.datasource.TenantDynamicDataSource;
import com.pengbo.multitenant.datasource.callback.DBSecretUpdateCallBack;
import com.pengbo.multitenant.datasource.poolconfig.DataSourcePoolConfig;
import com.pengbo.multitenant.datasource.provider.AbstractDynamicDataSourceProvider;
import com.pengbo.multitenant.datasource.provider.DynamicDataSourceSecretProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@ConditionalOnProperty(name = "multitenant.database.enable", havingValue = "true", matchIfMissing = true)
public class MultiTenantDataSourceAutoConfiguration {

    /**
     * 注册数据源
     *
     * @return
     */
    @Bean
    public TenantDynamicDataSource tenantDynamicDataSource() {
        return new TenantDynamicDataSource();
    }

    /**
     * 注册数据源事务管理器
     *
     * @param dataSource
     * @return
     */
    @Bean
    @ConditionalOnMissingBean
    public PlatformTransactionManager transactionManager(TenantDynamicDataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    /**
     * 数据源提供者
     *
     * @return
     */
    @Bean
    public AbstractDynamicDataSourceProvider dynamicDataSourceProvider() {
        return new DynamicDataSourceSecretProvider();
    }


    @Bean
    @ConfigurationProperties(prefix = "dbsdk.datasource")
    public DataSourcePoolConfig dataSourcePoolConfig() {
        return new DataSourcePoolConfig();
    }

    @Bean
    public DBSecretUpdateCallBack secretUpdateCallBack() {
        return new DBSecretUpdateCallBack();
    }
}
