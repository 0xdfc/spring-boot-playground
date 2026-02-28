package com.xdfc.playground.adapter.in.web.rest.annotation;

import com.xdfc.playground.adapter.in.web.rest.validator.SufficientBalanceInSourceValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = SufficientBalanceInSourceValidator.class)
public @interface SufficientBalanceInSource {
    String message() default "validation.transfer.balance.insufficient";

    Class<?>[] groups () default {};
    Class<? extends Payload>[] payload() default {};

}
