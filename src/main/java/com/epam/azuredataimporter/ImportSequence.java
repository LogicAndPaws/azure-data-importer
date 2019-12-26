package com.epam.azuredataimporter;

import com.epam.azuredataimporter.entity.Entity;

import java.util.LinkedList;
import java.util.Queue;

public class ImportSequence<T extends Entity> {
    private Queue<String> csvSequence = new LinkedList<>();
    private Queue<Class<T>> classesSequence = new LinkedList<>();

    public void addPare(String csv, Class<T> clazz) {
        csvSequence.add(csv);
        classesSequence.add(clazz);
    }

    public ImportConfig<T> getNext() {
        return new ImportConfig<>(csvSequence.remove(), classesSequence.remove());
    }

    public boolean hasNext() {
        return !csvSequence.isEmpty();
    }
}
