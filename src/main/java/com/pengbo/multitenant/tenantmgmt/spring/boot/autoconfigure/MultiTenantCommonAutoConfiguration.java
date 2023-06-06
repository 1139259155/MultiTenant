package com.pengbo.multitenant.tenantmgmt.spring.boot.autoconfigure;

import com.pengbo.multitenant.datasource.TenantDynamicDataSource;
import com.pengbo.multitenant.datasource.callback.DBSecretUpdateCallBack;
import com.pengbo.multitenant.datasource.poolconfig.DataSourcePoolConfig;
import com.pengbo.multitenant.datasource.provider.AbstractDynamicDataSourceProvider;
import com.pengbo.multitenant.datasource.provider.DynamicDataSourceSecretProvider;
import com.pengbo.multitenant.tenantmgmt.TenantList;
import com.pengbo.multitenant.tenantmgmt.annotation.MultiTenantLifeCycleBeanPostProcessor;
import com.pengbo.multitenant.tenantmgmt.tenantplan.TenantAddPlan;
import com.pengbo.multitenant.tenantmgmt.tenantplan.TenantDeletePlan;
import com.pengbo.multitenant.tenantmgmt.tenantplan.TenantUpdatePlan;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@ConditionalOnProperty(name = "multitenant.mgmt.enable", havingValue = "true", matchIfMissing = true)
public class MultiTenantCommonAutoConfiguration {


    @Bean
    public MultiTenantLifeCycleBeanPostProcessor multiTenantLifeCycleBeanPostProcessor() {
        return new MultiTenantLifeCycleBeanPostProcessor();
    }


    @Bean
    public TenantList tenantList() {
        return new TenantList();
    }

    @Bean
    public TenantAddPlan tenantAddPlan() {
        return new TenantAddPlan();
    }

    @Bean
    public TenantDeletePlan tenantDeletePlan() {
        return new TenantDeletePlan();
    }

    @Bean
    public TenantUpdatePlan tenantUpdatePlan() {
        return new TenantUpdatePlan();
    }
}
