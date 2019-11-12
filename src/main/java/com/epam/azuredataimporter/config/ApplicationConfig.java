package com.epam.azuredataimporter.config;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class ApplicationConfig {
    private final String dbUrl;
    private final String dbPassword;
    private final String dbUser;
    private final String blobName;
    private final String importTrigger;
    public ApplicationConfig(){
        Map<String,String> params = readConfig();
        if(params==null){
            createConfigFile();
            params = readConfig();
        }
        assert params != null;
        dbUrl = params.get("dbUrl");
        dbUser = params.get("dbUser");
        dbPassword = params.get("dbPassword");
        blobName = params.get("blobName");
        importTrigger = params.get("importTrigger");
    }

    public File createConfigFile(){
        try {
            File configFile = new File("config.cfg");
            if(configFile.createNewFile()) {
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(configFile)));
                writer.write("dbUrl::jdbc:postgresql://localhost:5432\r\n");
                writer.write("dbUser::user\r\n");
                writer.write("dbPassword::password\r\n");
                writer.write("blobName::blob\r\n");
                writer.write("importTrigger::toimport.trigger\r\n");
                writer.close();
                return configFile;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    private Map<String,String> readConfig(){
        try {
            if(new File("config.cfg").exists()) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream("config.cfg")));
                Map<String,String> params = new HashMap<>();
                while(reader.ready()){
                    String[] line = reader.readLine().split("::");
                    try {
                        params.put(line[0], line[1]);
                    }catch (ArrayIndexOutOfBoundsException e){return null;}
                }
                return params;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

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
