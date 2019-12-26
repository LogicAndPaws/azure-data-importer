package com.epam.azuredataimporter.entity;

import java.util.ArrayList;
import java.util.List;

public class Company implements Entity {
    String companyName;
    String managerPhoneNumber;

    public Company(String[] fields) {
        if (fields.length != 2) throw new IllegalArgumentException();
        companyName = fields[0];
        managerPhoneNumber = fields[1];
    }

    public String getCompanyName() {
        return companyName;
    }

    public String getManagerPhoneNumber() {
        return managerPhoneNumber;
    }

    @Override
    public String getUniqueId() {
        return companyName;
    }

    @Override
    public List<Object> getFields() {
        List<Object> values = new ArrayList<>();
        values.add(companyName);
        values.add(managerPhoneNumber);
        return values;
    }

    @Override
    public String getTableName() {
        return "company";
    }
}
