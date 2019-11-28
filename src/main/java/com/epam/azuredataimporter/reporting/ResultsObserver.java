package com.epam.azuredataimporter.reporting;

public interface ResultsObserver {
    void failed(String message);

    void success();
}
