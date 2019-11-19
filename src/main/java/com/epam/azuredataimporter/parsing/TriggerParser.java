package com.epam.azuredataimporter.parsing;

import com.epam.azuredataimporter.ImportConfig;
import com.epam.azuredataimporter.ResultsObserver;
import org.springframework.beans.factory.annotation.Autowired;

public class TriggerParser {
    @Autowired
    private ResultsObserver observer;

    public ImportConfig parseTrigger(String trigger){
        ImportConfig config = new ImportConfig();
        String[] lines = trigger.split("\r\n");
        for(String line : lines){
            String[] param = line.split("::");
                if (param[0].equals("targetFile")) config.targetFile = param[1];
                if (param[0].equals("noRemove")) config.noDelete = Boolean.parseBoolean(param[1]);
        }
        if(config.targetFile==null)observer.failed("(Critical) Wrong trigger format");
        return config;
    }
}
