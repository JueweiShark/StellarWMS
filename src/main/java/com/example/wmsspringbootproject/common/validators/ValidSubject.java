package com.example.wmsspringbootproject.common.validators;


import com.example.wmsspringbootproject.common.Annotation.Subject;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;


public class ValidSubject implements ConstraintValidator<Subject,String> {

    @Override
    public void initialize(Subject constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return true;
    }
}
