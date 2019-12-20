package com.epam.azuredataimporter.entity;

public class Location implements Entity {
    String name;
    String address;
    int managerId;

    public Location(String[] fields) {
        if (fields.length != 3) throw new IllegalArgumentException();
        name = fields[0];
        address = fields[1];
        managerId = Integer.parseInt(fields[2]);
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public int getManagerId() {
        return managerId;
    }
}
