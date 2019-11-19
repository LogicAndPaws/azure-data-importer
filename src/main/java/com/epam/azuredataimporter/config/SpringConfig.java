package com.epam.azuredataimporter.config;

import com.epam.azuredataimporter.*;
import com.epam.azuredataimporter.sources.AzureConnector;
import com.epam.azuredataimporter.sources.FileSource;
import com.epam.azuredataimporter.importing.PostgresDAO;
import com.epam.azuredataimporter.parsing.UserParser;
import com.epam.azuredataimporter.validation.UserValidator;
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
    public UserParser parser(ResultsObserver observer){
        return new UserParser(observer);
    }
    @Bean
    public UserValidator validator(ResultsObserver observer){
        return new UserValidator(observer);
    }
    @Bean
    public PostgresDAO dao(ApplicationConfig config, ResultsObserver observer){
        return new PostgresDAO(observer,
                config.getDbUrl(),
                config.getDbUser(),
                config.getDbPassword());
    }
    @Bean
    public FileSource connector(ApplicationConfig config, ResultsObserver observer){
        return new AzureConnector(observer,config.getBlobName(),config.getImportTrigger());
    }
    @Bean
    public MainLine mainline(Reporter reporter, AzureConnector connector, PostgresDAO dao, UserParser parser, UserValidator validator){
        return new MainLine(reporter, connector, dao, parser, validator);
    }
}
