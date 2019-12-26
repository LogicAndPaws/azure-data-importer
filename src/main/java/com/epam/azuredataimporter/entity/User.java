package com.epam.azuredataimporter.entity;

import java.util.ArrayList;
import java.util.List;

public class User implements Entity {
    private int id;
    private String name;
    private String password;

    public User(int id, String name, String password) {
        this.id = id;
        this.name = name;
        this.password = password;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public String getUniqueId() {
        return Integer.toString(id);
    }

    @Override
    public List<Object> getFields() {
        List<Object> values = new ArrayList<>();
        values.add(id);
        values.add(name);
        values.add(password);
        return values;
    }

    @Override
    public String getTableName() {
        return "user";
    }
}
