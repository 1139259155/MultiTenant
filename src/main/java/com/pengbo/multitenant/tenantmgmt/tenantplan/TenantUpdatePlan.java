package com.pengbo.multitenant.tenantmgmt.tenantplan;

import com.pengbo.multitenant.tenantmgmt.event.EventTypeEnum;
import com.pengbo.multitenant.tenantmgmt.event.SimpleEvent;
import com.pengbo.multitenant.tenantmgmt.util.EventBusCenter;

import java.util.Set;

public class TenantUpdatePlan {


    public void update(Set<String> tenantIds) {
        EventBusCenter.post(new SimpleEvent(tenantIds, EventTypeEnum.UPDATE));
    }
}
