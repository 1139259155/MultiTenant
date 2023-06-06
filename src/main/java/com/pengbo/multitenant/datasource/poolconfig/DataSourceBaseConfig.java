package com.pengbo.multitenant.datasource.poolconfig;

import com.pengbo.multitenant.datasource.exception.DataSourceException;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

@Getter
@Setter
public class DataSourceBaseConfig {
    private static final String DATABASE_PREFIX = "dim_%s";

    private String tenantId;

    private String host;

    private String port;
    private String user;
    private String password;
    private String database;
    private String schema;
    private String url;

    private int connectTimeout = 10;

    private int socketTimeout = 30;

    private String applicationName;

    public DataSourceBaseConfig(String ipAndPort, String user, String password) {
        this.tenantId = parseTenantId(user);

        this.host = ipAndPort.split(":")[0];
        this.port = ipAndPort.split(":")[1];
        this.user = user;
        this.password = password;
        this.database = String.format(DATABASE_PREFIX, tenantId);
        this.schema = user;
        this.url = ipAndPort;
        this.applicationName = getApplicationName();
    }

    public DataSourceBaseConfig(String ipAndPort, String user, String password, String applicationName) {
        this(ipAndPort,user,password);
        this.applicationName = getApplicationName();
    }

    public void update(String ipAndPort, String user, String password) {
        this.tenantId = parseTenantId(user);

        this.host = ipAndPort.split(":")[0];
        this.port = ipAndPort.split(":")[1];
        this.user = user;
        this.password = password;
        this.database = String.format(DATABASE_PREFIX, tenantId);
        this.schema = user;
        this.url = ipAndPort;
    }

    private String getApplicationName() {
        // 从环境变量中获取app名称
        return "dimcbb";
    }

    private String parseTenantId(String user) {
        if (isNormalUser(user)) {
            return StringUtils.split(user, "_", 2)[1];
        }
        throw new DataSourceException("invalid user:{}", user);
    }

    private boolean isNormalUser(String user) {
        return StringUtils.split(user, "_", 2)[0].endsWith("db");
    }

    public String toString() {
        return "jdbc:postgresql://" + url + "/" + database + "?ApplicationName=" + applicationName + "&connectTimeout="
                + connectTimeout + "&socketTimeout=" + socketTimeout;
    }
}
