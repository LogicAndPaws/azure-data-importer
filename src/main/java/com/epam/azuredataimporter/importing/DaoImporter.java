package com.epam.azuredataimporter.importing;

public interface DaoImporter<T> {
    void insertObject(T object);
}
