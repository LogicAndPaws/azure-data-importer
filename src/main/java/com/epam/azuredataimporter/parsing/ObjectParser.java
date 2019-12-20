package com.epam.azuredataimporter.parsing;

public interface ObjectParser<T> {
    T parse(String line, Class<T> t);
}
