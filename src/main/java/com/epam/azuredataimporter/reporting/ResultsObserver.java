package com.epam.azuredataimporter.reporting;

public interface ResultsObserver {
    void failed(String message);

    void critical(String message);

    void success();

    void changeFile(String nextFile);
}
