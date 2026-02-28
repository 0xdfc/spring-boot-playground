package com.xdfc.playground.adapter.in.web.rest.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;

import java.lang.annotation.*;


@Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER})
@Documented
// We're seemingly forced to define this constraint as without it
// the grouping is never seen as a validatable annotation.
@Constraint(validatedBy = {})
@Digits(integer = 6, fraction = 2)
@DecimalMin(value = "0", inclusive = false)
@DecimalMax(value = "100000.00")
@Retention(RetentionPolicy.RUNTIME)
public @interface TransferAmountLimits {
    String message() default "validation.transfer.sum";

    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
