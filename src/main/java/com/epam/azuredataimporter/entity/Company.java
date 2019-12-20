package com.epam.azuredataimporter.entity;

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
}
