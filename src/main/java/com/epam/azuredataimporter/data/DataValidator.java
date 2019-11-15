package com.epam.azuredataimporter.data;

import com.epam.azuredataimporter.ResultsObserver;
import com.epam.azuredataimporter.entity.User;

public class DataValidator {
    private ResultsObserver observer;
    public DataValidator(ResultsObserver observer){
        this.observer = observer;
    }
    private boolean validateId(User user){
        if(user.getId()<10000000 || user.getId()>99999999){
            observer.failed("(Validator) User with id("+user.getId()+") has id with more or less than 8 numbers");
            return false;
        }
        return true;
    }
    private boolean validatePassword(User user){
        if(user.getPassword().length()<8 || user.getPassword().length()>32){
            observer.failed("(Validator) User with id("+user.getId()+") has password with more then 32 or less than 8 symbols");
            return false;
        }
        return true;
    }
    private boolean validateName(User user){
        if(user.getName().length()<4 || user.getName().length()>32){
            observer.failed("(Validator) User with id("+user.getId()+") has name with more then 32 or less than 3 symbols");
            return false;
        }
        return true;
    }
    public boolean validUser(User user){
        return validateId(user) & validateName(user) & validatePassword(user);
    }
}
