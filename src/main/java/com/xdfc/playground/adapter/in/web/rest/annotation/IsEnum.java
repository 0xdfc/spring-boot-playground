package com.xdfc.playground.adapter.in.web.rest.annotation;

import com.xdfc.playground.adapter.in.web.rest.validator.IsEnumValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target({
    ElementType.METHOD,
    ElementType.FIELD,
    ElementType.ANNOTATION_TYPE,
    ElementType.CONSTRUCTOR,
    ElementType.PARAMETER
})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = IsEnumValidator.class)
public @interface IsEnum {
    String message() default "validation.enum.is";

    Class<? extends Enum<?>> enumerable();

    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
