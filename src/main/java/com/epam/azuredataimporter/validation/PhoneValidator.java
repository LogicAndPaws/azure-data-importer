package com.epam.azuredataimporter.validation;

import com.epam.azuredataimporter.entity.Phone;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class PhoneValidator implements Validator {
    @Override
    public boolean supports(Class<?> clazz) {
        return Phone.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {

    }
}
