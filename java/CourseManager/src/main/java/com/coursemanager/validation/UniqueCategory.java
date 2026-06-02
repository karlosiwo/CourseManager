package com.coursemanager.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = UniqueCategoryValidator.class)
@Documented
public @interface UniqueCategory {
    String message() default "Kategoria już istnieje";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}