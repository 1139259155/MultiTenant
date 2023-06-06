package com.pengbo.multitenant.tenantmgmt.tenantplan;

import com.pengbo.multitenant.tenantmgmt.TenantList;
import com.pengbo.multitenant.tenantmgmt.event.EventTypeEnum;
import com.pengbo.multitenant.tenantmgmt.event.SimpleEvent;
import com.pengbo.multitenant.tenantmgmt.util.EventBusCenter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;

import java.util.Set;

public class TenantDeletePlan {


    public void delete(Set<String> tenantIds) {
        EventBusCenter.post(new SimpleEvent(tenantIds, EventTypeEnum.DELETE));
    }
}
