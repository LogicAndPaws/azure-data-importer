package com.epam.azuredataimporter.parsing;

import com.epam.azuredataimporter.entity.Entity;
import com.epam.azuredataimporter.reporting.ResultsObserver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

@Component
public class UniversalParser<T extends Entity> implements ObjectParser<T> {
    @Autowired
    private ResultsObserver observer;

    @Override
    public T parse(String line, Class<T> t) {
        String[] fields = line.split(",");
//        printFields(fields);
        try {
            Constructor<?> constructor = t.getConstructor(String[].class);
            return (T) constructor.newInstance(new Object[]{fields});
        } catch (IllegalArgumentException e) {
            observer.failed("(Parser) Line with first value(" + fields[0] + ") has wrong format");
        } catch (IllegalAccessException | NoSuchMethodException | InstantiationException | InvocationTargetException e) {
            observer.failed("(Parser) Line with first value(" + fields[0] + "). Internal exception");
        }
        return null;
    }

    private void printFields(String[] fields) {
        for (String field : fields)
            System.out.print(field + " ");
        System.out.println();
    }
}
