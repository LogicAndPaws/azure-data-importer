package com.epam.azuredataimporter.parsing;

import com.epam.azuredataimporter.entity.User;
import com.epam.azuredataimporter.reporting.ResultsObserver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserParser implements ObjectParser<User> {
    @Autowired
    private ResultsObserver observer;

    @Override
    public User parse(String line, Class<User> userClass) {
        String[] fields = line.split(",");
        if (fields.length != 3) {
            observer.failed("(Parse) Wrong line format around " + fields[0]);
            return null;
        }
        try {
            return new User(Integer.parseInt(fields[0]), fields[1], fields[2]);
        } catch (NumberFormatException e) {
            observer.failed("(Parser) Line with first value(" + fields[0] + ") has wrong id format");
            return null;
        }
    }
}
