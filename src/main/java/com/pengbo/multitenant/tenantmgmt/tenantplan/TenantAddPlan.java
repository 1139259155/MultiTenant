package com.pengbo.multitenant.tenantmgmt.tenantplan;

import com.pengbo.multitenant.tenantmgmt.TenantList;
import com.pengbo.multitenant.tenantmgmt.event.EventTypeEnum;
import com.pengbo.multitenant.tenantmgmt.event.SimpleEvent;
import com.pengbo.multitenant.tenantmgmt.util.EventBusCenter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;

import java.util.Set;

public class TenantAddPlan implements ApplicationListener<ApplicationReadyEvent> {

    @Autowired
    public TenantList tenantList;

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        EventBusCenter.post(new SimpleEvent(tenantList.getTenantList(), EventTypeEnum.ADD));
    }

    /**
     * 服务运行时，追加开通新租户
     *
     * @param tenantIds
     */
    public void add(Set<String> tenantIds) {
        EventBusCenter.post(new SimpleEvent(tenantIds, EventTypeEnum.ADD));
    }
}
