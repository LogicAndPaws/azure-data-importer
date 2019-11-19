package com.epam.azuredataimporter.validation;

import com.epam.azuredataimporter.ServiceStatus;

import java.util.Queue;

public interface ValidationService<T> {
    Queue<T> startAsyncValidation(Queue<T> queue) throws Exception;
    void endAsyncValidation();
    ServiceStatus getStatus();
}
