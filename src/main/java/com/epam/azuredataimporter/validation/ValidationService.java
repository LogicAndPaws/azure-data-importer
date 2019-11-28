package com.epam.azuredataimporter.validation;

import java.util.Queue;

public interface ValidationService<T> {
    Queue<T> startAsyncValidation(Queue<T> queue) throws Exception;

    void endAsyncValidation();

    boolean isDone();
}
