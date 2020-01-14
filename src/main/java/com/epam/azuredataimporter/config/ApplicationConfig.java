package com.epam.azuredataimporter.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationConfig {
    @Getter
    @Value("${db.dbUrl}")
    private String dbUrl;
    @Getter
    @Value("${db.dbPassword}")
    private String dbPassword;
    @Getter
    @Value("${db.dbUser}")
    private String dbUser;
    @Getter
    @Value("${azure.blobName}")
    private String blobName;

}
