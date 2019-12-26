package com.epam.azuredataimporter.entity;

import java.util.ArrayList;
import java.util.List;

public class Worker implements Entity {
    int id;
    String name;
    String phoneNumber;

    public Worker(String[] fields) {
        if (fields.length != 3) throw new IllegalArgumentException();
        id = Integer.parseInt(fields[0]);
        name = fields[1];
        phoneNumber = fields[2];
    }

    public String getUniqueId() {
        return Integer.toString(id);
    }

    @Override
    public List<Object> getFields() {
        List<Object> values = new ArrayList<>();
        values.add(id);
        values.add(name);
        values.add(phoneNumber);
        return values;
    }

    @Override
    public String getTableName() {
        return "worker";
    }

    public String getName() {
        return name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public int getId() {
        return id;
    }
}
