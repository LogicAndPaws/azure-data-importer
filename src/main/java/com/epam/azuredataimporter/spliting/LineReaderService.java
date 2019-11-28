package com.epam.azuredataimporter.spliting;

import java.io.InputStream;
import java.util.Queue;

public interface LineReaderService {
    Queue<String> splitStream(InputStream stream) throws Exception;

    boolean isDone();
}
