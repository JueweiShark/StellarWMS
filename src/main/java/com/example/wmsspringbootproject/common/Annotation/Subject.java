package com.example.wmsspringbootproject.common.Annotation;

import com.example.wmsspringbootproject.common.validators.ValidSubject;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Documented
@Constraint(validatedBy = ValidSubject.class)
public @interface Subject {
    boolean value() default true;
    String filedName();
    Class<?> observer();
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
