package com.epam.azuredataimporter.trigger;

import com.epam.azuredataimporter.reporting.ResultsObserver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
class TriggerParser {
    @Autowired
    private ResultsObserver observer;

    ImportConfig parseTrigger(String trigger) {
        ImportConfig config = new ImportConfig();
        try {
            String[] lines = trigger.split("\r\n");
            for (String line : lines) {
                String[] param = line.split("=");
                if (param[0].equals("targetFile")) config.targetFile = param[1];
                if (param[0].equals("noRemove")) config.noDelete = Boolean.parseBoolean(param[1]);
            }
            if (config.targetFile == null) observer.failed("(Critical) Wrong trigger format");
            return config;
        } catch (Exception e) {
            observer.failed("(Critical) Wrong trigger format");
            return null;
        }
    }
}
