package com.pengbo.multitenant.mateinfo;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ContextManager {
    private static String currentTenantId;

    public static String getCurrentTenantId() {
        return "1002";
    }

    public static String getCurrentTenantId(String s) {
        return "1002";
    }


    public static void setCurrentTenantId(String s) {
        currentTenantId = s;
    }
}
