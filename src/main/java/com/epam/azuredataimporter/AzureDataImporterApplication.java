package com.epam.azuredataimporter;

import com.epam.azuredataimporter.config.ApplicationConfig;
import com.epam.azuredataimporter.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;

import java.io.*;
import java.util.List;

@SpringBootApplication
@ComponentScan("com.epam.azuredataimporter.config")
public class AzureDataImporterApplication implements CommandLineRunner {

	@Autowired
	private MainLine mainLine;

	public static void main(String[] args) {
		SpringApplication.run(AzureDataImporterApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		mainLine.startImport();
	}
}
