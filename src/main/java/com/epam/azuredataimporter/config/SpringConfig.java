package com.epam.azuredataimporter.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
@ConfigurationProperties(prefix = "")
public class SpringConfig {
    private static final String DRIVER = "org.postgresql.Driver";

    @Bean
    public DataSource dataSource(ApplicationConfig config) {
        HikariConfig dsConfig = new HikariConfig();
        dsConfig.setDriverClassName(DRIVER);
        dsConfig.setJdbcUrl(config.getDbUrl());
        dsConfig.setUsername(config.getDbUser());
        dsConfig.setPassword(config.getDbPassword());
        dsConfig.setMaximumPoolSize(20);
        return new HikariDataSource(dsConfig);
    }
}
