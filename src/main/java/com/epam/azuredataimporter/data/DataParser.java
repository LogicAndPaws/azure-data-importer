package com.epam.azuredataimporter.data;

import com.epam.azuredataimporter.ResultsObserver;
import com.epam.azuredataimporter.entity.User;

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
}
