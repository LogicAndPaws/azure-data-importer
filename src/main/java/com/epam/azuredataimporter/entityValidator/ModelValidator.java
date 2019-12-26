package com.epam.azuredataimporter.entityValidator;

import com.epam.azuredataimporter.entity.Model;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Service
public class ModelValidator implements Validator {
    @Override
    public boolean supports(Class<?> clazz) {
        return Model.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {

    }
}
