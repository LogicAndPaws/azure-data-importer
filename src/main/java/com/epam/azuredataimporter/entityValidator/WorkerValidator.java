package com.epam.azuredataimporter.entityValidator;

import com.epam.azuredataimporter.entity.Worker;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

@Service
public class WorkerValidator implements Validator {
    @Override
    public boolean supports(Class<?> clazz) {
        return Worker.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Worker worker = (Worker) target;
        if (worker.getId() < 10000000 || worker.getId() > 99999999) {
            errors.rejectValue("id", "negative.or.not8symbols");
        }
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "name", "name.field.required");
        if (!worker.getName().matches("(\\w{3,} |\\w{3,}){2,3}|\\w{3,} (\\w\\.|\\w\\. ){1,2}")) {
            errors.rejectValue("name", "name.wrong.format");
        }
    }
}
