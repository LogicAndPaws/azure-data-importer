package com.epam.azuredataimporter.entity;

import java.util.ArrayList;
import java.util.List;

public class Phone implements Entity {
    int id;
    String model;
    String company;
    String currentLocation;
    double cost;

    public Phone(String[] fields) {
        if (fields.length != 5) throw new IllegalArgumentException();
        id = Integer.parseInt(fields[0]);
        model = fields[1];
        company = fields[2];
        currentLocation = fields[3];
        cost = Double.parseDouble(fields[4]);
    }

    public int getId() {
        return id;
    }

    public String getModel() {
        return model;
    }

    public String getCompany() {
        return company;
    }

    public String getCurrentLocation() {
        return currentLocation;
    }

    public double getCost() {
        return cost;
    }

    @Override
    public String getUniqueId() {
        return Integer.toString(id);
    }

    @Override
    public List<Object> getFields() {
        List<Object> values = new ArrayList<>();
        values.add(id);
        values.add(model);
        values.add(company);
        values.add(currentLocation);
        values.add(cost);
        return values;
    }

    @Override
    public String getTableName() {
        return "phone";
    }
}
