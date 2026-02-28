package com.xdfc.playground.adapter.in.web.rest.annotation;

import com.xdfc.playground.adapter.in.web.rest.validator.DestinationCanReceiveAmountValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = DestinationCanReceiveAmountValidator.class)
public @interface DestinationCanReceiveAmount {
    String message() default "validation.transfer.receiver.cant.receive";

    Class<?>[] groups () default {};
    Class<? extends Payload>[] payload() default {};
}
