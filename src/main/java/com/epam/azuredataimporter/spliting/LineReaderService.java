package com.epam.azuredataimporter.spliting;

import com.epam.azuredataimporter.ServiceStatus;

import java.io.InputStream;
import java.util.Queue;

public interface LineReaderService {
    Queue<String> splitStream(InputStream stream) throws Exception;
    ServiceStatus getStatus();
}
