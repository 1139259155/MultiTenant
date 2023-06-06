package com.pengbo.multitenant.tenantmgmt.delegate;

import com.pengbo.multitenant.mateinfo.ContextManager;
import com.pengbo.multitenant.tenantmgmt.annotation.MultiTenantLifeCycle;
import com.pengbo.multitenant.tenantmgmt.event.EventTypeEnum;
import com.pengbo.multitenant.tenantmgmt.event.SimpleEvent;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Member;
import java.lang.reflect.Method;

@Slf4j
public class MultiTenantModifyDelegate {

    private Object target;

    private String[] deleteMethodNames;

    private String[] initMethodNames;

    private String alias;

    public MultiTenantModifyDelegate(Object obj, MultiTenantLifeCycle multiTenantLifeCycle) {
        this.target = obj;
        this.deleteMethodNames = multiTenantLifeCycle.deleteMethods();
        this.initMethodNames = multiTenantLifeCycle.initMethods();
    }

    public void invoke(SimpleEvent event) {
        String[] runMethodArray = null;
        if (EventTypeEnum.ADD.equals(event.getEventType())) {
            runMethodArray = initMethodNames;
        } else if (EventTypeEnum.DELETE.equals(event.getEventType())) {
            runMethodArray = deleteMethodNames;
        } else {
            return;
        }

        try {
            for (String tenantId : event.getTenantIds()) {
                for (String methodName : runMethodArray) {
                    ContextManager.setCurrentTenantId(tenantId);
                    Method method = target.getClass().getMethod(methodName);
                    method.invoke(target);
                }
            }
        } catch (Exception e) {

        }
    }

}
