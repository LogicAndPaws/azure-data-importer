package com.epam.azuredataimporter;

import com.epam.azuredataimporter.azure.AzureConnector;
import com.epam.azuredataimporter.dao.PostgresDAO;
import com.epam.azuredataimporter.data.DataParser;
import com.epam.azuredataimporter.data.DataValidator;

public class MainLine {
    private Reporter reporter;
    private AzureConnector azureConnector;
    private PostgresDAO dao;
    private DataParser parser;
    private DataValidator validator;
    public MainLine(Reporter reporter,AzureConnector connector, PostgresDAO dao, DataParser parser, DataValidator validator){
        this.reporter = reporter;
        azureConnector = connector;
        this.dao = dao;
        this.parser = parser;
        this.validator = validator;
    }
    public void startImport(String importConfigFile){

    }
}
