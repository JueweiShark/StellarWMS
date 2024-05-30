package com.example.wmsspringbootproject.common.Annotation;

import com.example.wmsspringbootproject.common.validators.ValidListener;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;
import java.util.List;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Documented
@Constraint(validatedBy = ValidListener.class)
public @interface Listener {
    boolean value() default true;
    String[] filedName() default "";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
