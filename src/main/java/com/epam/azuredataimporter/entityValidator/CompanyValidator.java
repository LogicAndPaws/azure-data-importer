package com.epam.azuredataimporter.entityValidator;

import com.epam.azuredataimporter.entity.Company;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Service
public class CompanyValidator implements Validator {
    @Override
    public boolean supports(Class<?> clazz) {
        return Company.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {

    }
}
