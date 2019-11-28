package com.epam.azuredataimporter.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationConfig {
    @Value("${db.dbUrl}")
    private String dbUrl;
    @Value("${db.dbPassword}")
    private String dbPassword;
    @Value("${db.dbUser}")
    private String dbUser;
    @Value("${azure.blobName}")
    private String blobName;
    @Value("${azure.importTrigger}")
    private String importTrigger;

    public String getDbUrl() {
        return dbUrl;
    }

    public String getDbPassword() {
        return dbPassword;
    }

    public String getDbUser() {
        return dbUser;
    }

    public String getBlobName() {
        return blobName;
    }

    public String getImportTrigger() {
        return importTrigger;
    }
}
