package com.pengbo.multitenant.datasource.provider;

import com.pengbo.multitenant.datasource.DataSourceLookupKey;
import com.pengbo.multitenant.datasource.poolconfig.DataSourceBaseConfig;
import org.springframework.beans.factory.annotation.Value;

import java.io.File;
import java.util.Map;

public class DynamicDataSourceSecretProvider extends AbstractDynamicDataSourceProvider {
    @Value("${PG_SECRET_MOUNT_PATH:/opt/pg-secret}")
    private String mountPath;

    @Override
    public Map<DataSourceLookupKey, DataSourceBaseConfig> loadDataSources() {

        String secretPath = String.join(File.separator, this.mountPath, "diminit-pms-secret");

        // get secret

        // parse secret

        return this.dataSourceBaseConfigs;
    }
}
