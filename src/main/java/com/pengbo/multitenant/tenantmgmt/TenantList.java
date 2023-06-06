package com.pengbo.multitenant.tenantmgmt;

import com.google.common.collect.Sets;
import com.pengbo.multitenant.datasource.TenantDynamicDataSource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Set;

@Slf4j
public class TenantList {

    @Autowired
    private TenantDynamicDataSource tenantDynamicDataSource;

    public Set<String> getTenantList(){

        return Sets.newHashSet("1002");
    }

    public Set<String> getStackTenantList(){

        return Sets.newHashSet("1002");
    }

}
