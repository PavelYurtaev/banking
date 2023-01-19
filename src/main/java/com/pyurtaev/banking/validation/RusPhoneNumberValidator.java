package com.pyurtaev.banking.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class RusPhoneNumberValidator implements ConstraintValidator<RusPhoneNumberFormat, String> {

    @Override
    public void initialize(RusPhoneNumberFormat field) {
    }

    @Override
    public boolean isValid(String field, ConstraintValidatorContext cxt) {
        return field != null && field.matches("^7\\d{10}$");
    }

}