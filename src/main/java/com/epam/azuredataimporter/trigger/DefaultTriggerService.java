package com.epam.azuredataimporter.trigger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DefaultTriggerService implements TriggerService {
    @Autowired
    private TriggerSource triggerSource;
    @Autowired
    private TriggerParser parser;

    @Override
    public ImportConfig getTrigger() {
        String triggerString = triggerSource.getTrigger();
        if (triggerString == null) return null;
        return parser.parseTrigger(triggerString);
    }

    @Override
    public void deleteTrigger() {
        triggerSource.deleteTrigger();
    }
}
