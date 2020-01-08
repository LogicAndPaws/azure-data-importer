package com.epam.azuredataimporter.entity_validator;

import com.epam.azuredataimporter.entity.Company;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

@Service
public class CompanyValidator implements Validator {
    @Override
    public boolean supports(Class<?> clazz) {
        return Company.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Company company = (Company) target;
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "companyName", "", "Company name can't be empty");
        if (!company.getManagerPhoneNumber().matches("")) {
            errors.rejectValue("managerPhoneNumber", "", "Manager number has wrong format");
        }
    }
}
