package com.epam.azuredataimporter.entity_validator;

import com.epam.azuredataimporter.entity.Location;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

@Service
public class LocationValidator implements Validator {
    @Override
    public boolean supports(Class<?> clazz) {
        return Location.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Location location = (Location) target;
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "name", "", "Location name can't be empty");
        if (location.getManagerId() < 10000000 || location.getManagerId() > 99999999) {
            errors.rejectValue("managerId", "", "Wrong manager id");
        }
    }
}
