package com.pengbo.multitenant.tenantmgmt.annotation;

import com.pengbo.multitenant.tenantmgmt.delegate.MultiTenantModifyDelegate;
import com.pengbo.multitenant.tenantmgmt.util.EventBusCenter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

@Slf4j
public class MultiTenantLifeCycleBeanPostProcessor implements BeanPostProcessor {

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        try {
            MultiTenantLifeCycle multiTenantLifeCycle = bean.getClass().getAnnotation(MultiTenantLifeCycle.class);
            if (multiTenantLifeCycle != null) {
                EventBusCenter.register(new MultiTenantModifyDelegate(bean, multiTenantLifeCycle));
            }
        } catch (Exception e) {

        }
        return bean;
    }
}
