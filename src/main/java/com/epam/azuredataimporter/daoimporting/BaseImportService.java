package com.epam.azuredataimporter.daoimporting;

import java.util.Queue;

public interface BaseImportService<T> {
    void startAsyncImport(Queue<T> queue) throws Exception;

    void endAsyncImport();

    boolean isDone();
}
