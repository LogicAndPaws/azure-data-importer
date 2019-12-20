package com.epam.azuredataimporter;


import com.epam.azuredataimporter.entity.Entity;

public class ImportConfig {
    private String csv;
    private Class<? extends Entity> clazz;

    public ImportConfig(String csv, Class<? extends Entity> clazz) {
        this.csv = csv;
        this.clazz = clazz;
    }

    public String getCsv() {
        return csv;
    }

    public Class<? extends Entity> getClazz() {
        return clazz;
    }
}
