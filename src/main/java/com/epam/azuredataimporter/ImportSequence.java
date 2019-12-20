package com.epam.azuredataimporter;

import com.epam.azuredataimporter.entity.Entity;

import java.util.LinkedList;
import java.util.Queue;

public class ImportSequence {
    private Queue<String> csvSequence = new LinkedList<>();
    private Queue<Class<? extends Entity>> classesSequence = new LinkedList<>();

    public void addPare(String csv, Class<? extends Entity> clazz) {
        csvSequence.add(csv);
        classesSequence.add(clazz);
    }

    public ImportConfig getNext() {
        return new ImportConfig(csvSequence.remove(), classesSequence.remove());
    }

    public boolean hasNext() {
        return !csvSequence.isEmpty();
    }
}
