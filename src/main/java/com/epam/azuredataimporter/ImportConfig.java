package com.epam.azuredataimporter;


import com.epam.azuredataimporter.entity.Entity;

public class ImportConfig<T extends Entity> {
    private String csv;
    private Class<T> clazz;

    public ImportConfig(String csv, Class<T> clazz) {
        this.csv = csv;
        this.clazz = clazz;
    }

    public String getCsv() {
        return csv;
    }

    public Class<T> getClazz() {
        return clazz;
    }
}
