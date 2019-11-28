package com.epam.azuredataimporter.trigger;

public interface TriggerSource {
    String getTrigger();

    void deleteTrigger();
}
