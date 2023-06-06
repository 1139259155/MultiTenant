package com.pengbo.multitenant.datasource.callback;

import com.pengbo.multitenant.datasource.TenantDynamicDataSource;
import com.pengbo.multitenant.datasource.poolconfig.DataSourcePoolConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import javax.annotation.PostConstruct;

@Slf4j
public class DBSecretUpdateCallBack {

    @Autowired
    private TenantDynamicDataSource dynamicDataSource;

    @Autowired
    private DataSourcePoolConfig dataSourcePoolConfig;

    @Value("${PG_SECRET_MOUNT_PATH:/opt/pg-secret}")
    private String mountPath;

    /**
     * 启动时，注册secret监听
     */
    @PostConstruct
    public void passwdRegister(){
        // todo
    }

    /**
     * secret变更回调
     * @param secretContent
     */
    public void onPwdUpdate(String secretContent){
        // 1.parse secret latest config

        // 2.get current config

        /**
         * 3.latest config compare to current config {@link TenantDynamicDataSource#dataSourceBaseConfigMap}
         * get result diff: <left, middle, right>=<add, modify, delete>
         */

        log.warn("compare result:{}");

        // 4.根据diff对比结果，更新DataSource

        // 5.根据diff对比结果，触发租户变更计划
    }
}
