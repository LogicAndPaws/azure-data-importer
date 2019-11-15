package com.epam.azuredataimporter.data;

import com.epam.azuredataimporter.ImportConfig;
import com.epam.azuredataimporter.ResultsObserver;
import com.epam.azuredataimporter.entity.User;

import java.util.HashMap;
import java.util.Map;

public class DataParser {
    private ResultsObserver observer;
//    public void setObserver(ResultsObserver observer){
//        DataParser.observer = observer;
//    }
    public DataParser(ResultsObserver resultsObserver){
        this.observer = resultsObserver;
    }
    public User parseUser(String line){
        String[] fields = line.split(",");
        if(fields.length!=3){
            observer.failed("(Critical) Wrong file format");
            return new User(0,null,null);
        }
        try {
            User newUser = new User(Integer.parseInt(fields[0]), fields[1], fields[2]);
            return newUser;
        }catch (NumberFormatException e){
            observer.failed("(Parser) Line with first value("+fields[0]+") has wrong id format");
            return null;
        }
    }
    public ImportConfig parseTrigger(String trigger){
        ImportConfig config = new ImportConfig();
        String[] lines = trigger.split("\r\n");
        for(String line : lines){
            String[] param = line.split("::");
           if(param[0].equals("targetFile"))config.targetFile = param[1];
        }
        if(config.targetFile==null)observer.failed("(Critical) Wrong trigger format");
        return config;
    }
}
