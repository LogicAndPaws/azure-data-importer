package com.epam.azuredataimporter.sources;

import java.io.File;

public interface FileSource {
    File readFile(String filename);
}
