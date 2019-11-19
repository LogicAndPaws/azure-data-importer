package com.epam.azuredataimporter.sources;

import java.io.File;

public interface FileSource {
    File readFile(String filename);

    boolean sendFile(File file);

    boolean sendStringToFile(String stringLine, String filename);
}
