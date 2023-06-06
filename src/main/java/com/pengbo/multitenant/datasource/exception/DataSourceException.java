package com.pengbo.multitenant.datasource.exception;

public class DataSourceException extends RuntimeException{
    private String errorCode;
    private String[] errorArgs;

    public DataSourceException() {
        super();
    }

    public DataSourceException(String errorCode, String errorMsg, String... errorArgs) {
        super(errorMsg);
        this.errorCode = errorCode;
        this.errorArgs = errorArgs;
    }
}
