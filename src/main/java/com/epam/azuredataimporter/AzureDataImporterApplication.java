package com.epam.azuredataimporter;

import com.epam.azuredataimporter.entity.User;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;

import java.io.*;
import java.util.List;

@SpringBootApplication
@ComponentScan("com.epam.azuredataimporter.config")
public class AzureDataImporterApplication {
	public static void main(String[] args) {
		ConfigurableApplicationContext applicationContext = SpringApplication.run(AzureDataImporterApplication.class, args);
	}

}
