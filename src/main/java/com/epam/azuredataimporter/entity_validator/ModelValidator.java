package com.epam.azuredataimporter.entity_validator;

import com.epam.azuredataimporter.entity.Model;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

@Service
public class ModelValidator implements Validator {
    @Override
    public boolean supports(Class<?> clazz) {
        return Model.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Model model = (Model) target;
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "modelName", "", "Model name can't be empty");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "companyName", "", "Company name can't be empty");
    }
}
