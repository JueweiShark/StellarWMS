package com.example.wmsspringbootproject.common.validators;

import com.example.wmsspringbootproject.common.Annotation.Listener;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class ValidListener implements ConstraintValidator<Listener,String> {
    @Override
    public void initialize(Listener constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return true;
    }
}
