package com.lemon.app.properties;

import jakarta.validation.constraints.NotNull;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "gcp")
public class BigTableDataProperties {

    private String projectId;
    private String instanceId;
    @NotNull(message = "Table id cannot be null.")
    private String tableId;
    private String columnFamilyTempC;
    private String columnFamilyHum;
    private String adminCredentialsPath;

    public BigTableDataProperties() {
    }

    public BigTableDataProperties(
            String projectId,
            String instanceId,
            String tableId,
            String columnFamilyTempC,
            String columnFamilyHum,
            String adminCredentialsPath
    ) {
        this.projectId = projectId;
        this.instanceId = instanceId;
        this.tableId = tableId;
        this.columnFamilyTempC = columnFamilyTempC;
        this.columnFamilyHum = columnFamilyHum;
        this.adminCredentialsPath = adminCredentialsPath;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getInstanceId() {
        return instanceId;
    }

    public void setInstanceId(String instanceId) {
        this.instanceId = instanceId;
    }

    public String getTableId() {
        return tableId;
    }

    public void setTableId(String tableId) {
        this.tableId = tableId;
    }

    public String getColumnFamilyTempC() {
        return columnFamilyTempC;
    }

    public void setColumnFamilyTempC(String columnFamilyTempC) {
        this.columnFamilyTempC = columnFamilyTempC;
    }

    public String getColumnFamilyHum() {
        return columnFamilyHum;
    }

    public void setColumnFamilyHum(String columnFamilyHum) {
        this.columnFamilyHum = columnFamilyHum;
    }

    public String getAdminCredentialsPath() {
        return adminCredentialsPath;
    }

    public void setAdminCredentialsPath(String adminCredentialsPath) {
        this.adminCredentialsPath = adminCredentialsPath;
    }
}
