package com.epam.azuredataimporter.validation;

import com.epam.azuredataimporter.entity.Model;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class ModelValidator implements Validator {
    @Override
    public boolean supports(Class<?> clazz) {
        return Model.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {

    }
}
