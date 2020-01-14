package com.epam.azuredataimporter.validation;

import com.epam.azuredataimporter.entity.Entity;
import com.epam.azuredataimporter.reporting.ResultsObserver;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.validation.DataBinder;
import org.springframework.validation.ObjectError;
import org.springframework.validation.Validator;

import java.util.ArrayList;
import java.util.List;

@Component
public class UniversalValidator<T extends Entity> implements ObjectValidator<T> {
    private final List<Validator> validators;
    private final ResultsObserver observer;

    public UniversalValidator(ApplicationContext context, ResultsObserver observer) {
        validators = new ArrayList<>(context.getBeansOfType(Validator.class).values());
        this.observer = observer;
    }

    @Override
    public boolean isValid(T object) {
        DataBinder binder = new DataBinder(object);
        for (Validator validator : validators)
            if (validator.supports(object.getClass())) {
                binder.addValidators(validator);
                binder.validate();
                if (binder.getBindingResult().hasErrors()) {
                    for (ObjectError err : binder.getBindingResult().getAllErrors())
                        observer.failed("(Validator) " + object.getUniqueId() + ": " + err.getDefaultMessage());
                    return false;
                } else return true;
            }
        return false;
    }
}
