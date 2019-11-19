package com.epam.azuredataimporter.parsing;

import com.epam.azuredataimporter.ServiceStatus;

import java.util.Queue;

public interface ParseService<T> {
    Queue<T> startAsyncParse(Queue<String> queue) throws Exception;
    void endAsyncParse();
    ServiceStatus getStatus();
}
