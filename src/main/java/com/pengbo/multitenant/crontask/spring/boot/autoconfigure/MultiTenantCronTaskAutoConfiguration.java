package com.pengbo.multitenant.crontask.spring.boot.autoconfigure;

import com.pengbo.multitenant.crontask.annotation.MultiTenantCronTaskConfigurer;
import com.pengbo.multitenant.crontask.dynamic.DynamicScheduledTaskRegister;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(name = "multitenant.crontask.enable", havingValue = "true", matchIfMissing = true)
public class MultiTenantCronTaskAutoConfiguration {

    @Bean
    public MultiTenantCronTaskConfigurer multiTenantCronTaskConfigurer() {
        return new MultiTenantCronTaskConfigurer();
    }

    @Bean
    public DynamicScheduledTaskRegister dynamicScheduledTaskRegister() {
        return new DynamicScheduledTaskRegister();
    }
}
