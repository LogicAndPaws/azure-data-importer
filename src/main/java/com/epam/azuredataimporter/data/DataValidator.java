package com.epam.azuredataimporter.data;

import com.epam.azuredataimporter.ResultsObserver;

public class DataValidator {
    private ResultsObserver observer;
    public DataValidator(ResultsObserver observer){
        this.observer = observer;
    }
}
