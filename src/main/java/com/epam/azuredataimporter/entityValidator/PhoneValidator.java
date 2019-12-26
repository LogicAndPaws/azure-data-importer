package com.epam.azuredataimporter.entityValidator;

import com.epam.azuredataimporter.entity.Phone;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Service
public class PhoneValidator implements Validator {
    @Override
    public boolean supports(Class<?> clazz) {
        return Phone.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {

    }
}
