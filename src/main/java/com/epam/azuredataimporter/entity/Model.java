package com.epam.azuredataimporter.entity;

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
}
