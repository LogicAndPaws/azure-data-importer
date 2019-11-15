package com.epam.azuredataimporter;

public interface ResultsObserver {
    void failed(String message);
    void warning(String message);
    void success();
}
