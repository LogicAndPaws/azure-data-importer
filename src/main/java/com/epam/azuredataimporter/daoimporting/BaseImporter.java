package com.epam.azuredataimporter.daoimporting;

public interface BaseImporter<T> {
    void insertObject(T object);
}
