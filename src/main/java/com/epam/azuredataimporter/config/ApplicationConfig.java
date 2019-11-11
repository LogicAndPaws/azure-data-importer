package com.epam.azuredataimporter.config;

import java.util.HashMap;
import java.util.Map;

public class ApplicationConfig {
    private Map<String,String> parameters = new HashMap<>();
    public ApplicationConfig(){
        parameters.put("dbUrl","jdbc:postgresql://host.docker.internal:5432/testbase");
        parameters.put("dbUser","sunshade");
        parameters.put("dbPassword","4141");
        parameters.put("csvFile","users.csv");
        parameters.put("blobName","testblob");
    }
    public String getParameter(String parameterName){
        return parameters.get(parameterName);
    }
}
