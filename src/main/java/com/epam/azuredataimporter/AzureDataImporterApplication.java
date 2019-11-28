package com.epam.azuredataimporter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.Banner;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("com.epam.azuredataimporter.config")
@ComponentScan("com.epam.azuredataimporter.*")
public class AzureDataImporterApplication implements CommandLineRunner {

    @Autowired
    private ImportService importService;

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(AzureDataImporterApplication.class);
        app.setBannerMode(Banner.Mode.OFF);
        app.run(args);
    }

    @Override
    public void run(String... args) {
        importService.startImport();
    }
}
