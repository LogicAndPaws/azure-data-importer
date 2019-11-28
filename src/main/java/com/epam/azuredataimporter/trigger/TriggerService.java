package com.epam.azuredataimporter.trigger;

public interface TriggerService {
    ImportConfig getTrigger();

    void deleteTrigger();
}
