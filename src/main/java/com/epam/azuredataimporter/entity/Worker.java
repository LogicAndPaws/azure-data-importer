package com.epam.azuredataimporter.entity;

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

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }
}
