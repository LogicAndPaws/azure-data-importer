package com.epam.azuredataimporter.entity;

import java.util.ArrayList;
import java.util.List;

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

    @Override
    public String getUniqueId() {
        return name;
    }

    @Override
    public List<Object> getFields() {
        List<Object> values = new ArrayList<>();
        values.add(name);
        values.add(address);
        values.add(managerId);
        return values;
    }

    @Override
    public String getTableName() {
        return "location";
    }
}
