package com.epam.azuredataimporter.entity;

import java.util.ArrayList;
import java.util.List;

public class Model implements Entity {
    String modelName;
    String companyName;

    public Model(String[] fields) {
        if (fields.length != 2) throw new IllegalArgumentException();
        modelName = fields[0];
        companyName = fields[1];
    }

    public String getModelName() {
        return modelName;
    }

    public String getCompanyName() {
        return companyName;
    }

    @Override
    public String getUniqueId() {
        return modelName;
    }

    @Override
    public List<Object> getFields() {
        List<Object> values = new ArrayList<>();
        values.add(modelName);
        values.add(companyName);
        return values;
    }

    @Override
    public String getTableName() {
        return "model";
    }
}
