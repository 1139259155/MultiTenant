package com.pengbo.multitenant.tenantmgmt.event;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Set;

@Getter
@Setter
@ToString
@EqualsAndHashCode
public class SimpleEvent {

    private EventTypeEnum eventType;

    private Set<String> tenantIds;


    public SimpleEvent(Set<String> tenantList, EventTypeEnum eventType) {
        this.tenantIds = tenantList;
        this.eventType = eventType;
    }
}
