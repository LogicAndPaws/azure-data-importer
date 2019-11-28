package com.epam.azuredataimporter.parsing;

import java.util.Queue;

public interface ParseService<T> {
    Queue<T> startAsyncParse(Queue<String> queue) throws Exception;

    void endAsyncParse();

    boolean isDone();
}
