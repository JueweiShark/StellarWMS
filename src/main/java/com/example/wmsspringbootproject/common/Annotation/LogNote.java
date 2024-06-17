package com.example.wmsspringbootproject.common.Annotation;

import jakarta.validation.Payload;

import java.lang.annotation.*;

/**
 * <p>
 * 日志记录
 * </p>
 *
 * @author 初秋
 * @since 2024-06-09
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD,ElementType.TYPE})
@Documented
public @interface LogNote {
    String value() default "";
    String description() default "";
    String operation() default "";
    String operationType() default "";
    String module() default "";
    String moduleType() default "";
    String moduleId() default "";
    String moduleIdName() default "";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
