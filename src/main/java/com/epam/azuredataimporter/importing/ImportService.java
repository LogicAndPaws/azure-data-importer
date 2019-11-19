package com.epam.azuredataimporter.importing;

import com.epam.azuredataimporter.ServiceStatus;

import java.util.Queue;

public interface ImportService<T>{
    void startAsyncImport(Queue<T> queue) throws Exception;
    void endAsyncValidation();
    ServiceStatus getStatus();
}
