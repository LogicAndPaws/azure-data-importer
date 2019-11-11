package com.epam.azuredataimporter.config;

import com.epam.azuredataimporter.*;
import com.epam.azuredataimporter.azure.AzureConnector;
import com.epam.azuredataimporter.dao.PostgresDAO;
import com.epam.azuredataimporter.data.DataParser;
import com.epam.azuredataimporter.data.DataValidator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringConfig {
    @Bean
    public Reporter reporter(){
        return new Reporter();
    }
    @Bean
    public ApplicationConfig config(){
        return new ApplicationConfig();
    }
    @Bean
    public DataParser parser(ResultsObserver observer){
        return new DataParser(observer);
    }
    @Bean
    public DataValidator validator(ResultsObserver observer){
        return new DataValidator(observer);
    }
    @Bean
    public PostgresDAO dao(ApplicationConfig config, ResultsObserver observer){
        return new PostgresDAO(observer,
                config.getParameter("dbUrl"),
                config.getParameter("dbUser"),
                config.getParameter("dbPassword"));
    }
    @Bean
    public AzureConnector connector(ApplicationConfig config, ResultsObserver observer){
        return new AzureConnector(observer,config.getParameter("blobName"));
    }
}
