package com.epam.azuredataimporter.entity;

import java.util.List;

public interface Entity {
    String getUniqueId();

    List<Object> getFields();

    String getTableName();
}
