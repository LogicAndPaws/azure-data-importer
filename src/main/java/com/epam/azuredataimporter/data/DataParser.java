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
        return new User(Integer.parseInt(fields[0]),fields[1],fields[2]);
    }
    public ImportConfig parseTrigger(String trigger){
        ImportConfig config = new ImportConfig();
        String[] lines = trigger.split("\r\n");
        for(String line : lines){
            String[] param = line.split("::");
           if(param[0].equals("targetFile"))config.targetFile = param[1];
        }
        return config;
    }
}
