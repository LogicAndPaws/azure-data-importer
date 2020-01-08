package com.epam.azuredataimporter.entity_validator;

import com.epam.azuredataimporter.entity.Phone;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

@Service
public class PhoneValidator implements Validator {
    @Override
    public boolean supports(Class<?> clazz) {
        return Phone.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Phone phone = (Phone) target;
        if (phone.getCost() < 0.0) {
            errors.rejectValue("cost", "", "Cost can't be negative");
        }
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "company", "", "Company can't be empty");
    }
}
