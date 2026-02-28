package com.xdfc.playground.adapter.in.web.rest.annotation;

import com.xdfc.playground.adapter.in.web.rest.validator.SameCurrencyTransferValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = SameCurrencyTransferValidator.class)
public @interface SameCurrencyTransfer {
    String message() default "validation.transfer.currency.mismatch";

    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
