package com.pengbo.multitenant.tenantmgmt.util;

import com.google.common.eventbus.EventBus;

public class EventBusCenter {

    private static EventBus eventBus = new EventBus("tenant change");

    private EventBusCenter() {
    }

    public static EventBus getInstance() {
        return eventBus;
    }

    public static void register(Object obj) {
        eventBus.register(obj);
    }

    public static void unregister(Object obj) {
        eventBus.unregister(obj);
    }

    public static void post(Object obj) {
        eventBus.post(obj);
    }


}
