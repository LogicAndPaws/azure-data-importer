package com.epam.azuredataimporter.validation;

public interface ObjectValidator<T> {
    boolean isValid(T object);
}
